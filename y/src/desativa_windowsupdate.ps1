# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/desativa_windowsupdate.ps1 | iex
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/desativa_windowsupdate.ps1))) -Restaurar

<#

# limpando cache
Stop-Service wuauserv, UsoSvc, bits -Force -ErrorAction SilentlyContinue
Remove-Item "C:\Windows\SoftwareDistribution\Download\*" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\WindowsUpdate\Auto Update\RebootRequired" -Recurse -Force -ErrorAction SilentlyContinue

#>

param(
    [switch]$Restaurar
)

# --- Checagem de admin -------------------------------------------------------
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()
           ).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "Execute este script como Administrador." -ForegroundColor Red
    exit 1
}

$servicos  = @('wuauserv', 'UsoSvc', 'WaaSMedicSvc')
$pastasTarefas = @('\Microsoft\Windows\WindowsUpdate\', '\Microsoft\Windows\UpdateOrchestrator\')
$chaveAU   = 'HKLM:\SOFTWARE\Policies\Microsoft\Windows\WindowsUpdate\AU'

function Set-InicioServico {
    param([string]$Nome, [int]$Valor)   # 2=Automático, 3=Manual, 4=Desabilitado
    $chave = "HKLM:\SYSTEM\CurrentControlSet\Services\$Nome"
    if (Test-Path $chave) {
        # Set-Service falha no WaaSMedicSvc (acesso negado); o registro funciona
        Set-ItemProperty -Path $chave -Name Start -Value $Valor -Force
    }
}

# =============================================================================
if ($Restaurar) {
    Write-Host "`n>> Restaurando Windows Update..." -ForegroundColor Cyan

    if (Test-Path $chaveAU) {
        Remove-ItemProperty -Path $chaveAU -Name NoAutoUpdate -ErrorAction SilentlyContinue
        Write-Host "  [ok] Política NoAutoUpdate removida"
    }

    Set-InicioServico -Nome 'wuauserv'     -Valor 3
    Set-InicioServico -Nome 'UsoSvc'       -Valor 2
    Set-InicioServico -Nome 'WaaSMedicSvc' -Valor 3
    foreach ($s in $servicos) {
        Start-Service $s -ErrorAction SilentlyContinue
        Write-Host "  [ok] Serviço $s restaurado"
    }

    foreach ($pasta in $pastasTarefas) {
        Get-ScheduledTask -TaskPath $pasta -ErrorAction SilentlyContinue |
            Enable-ScheduledTask -ErrorAction SilentlyContinue | Out-Null
    }
    Write-Host "  [ok] Tarefas agendadas reativadas"

    Write-Host "`nPronto. Reinicie o computador para concluir." -ForegroundColor Green
    exit 0
}

# =============================================================================
Write-Host "`n>> Desligando Windows Update..." -ForegroundColor Cyan

# 1. Política de registro
New-Item -Path $chaveAU -Force | Out-Null
Set-ItemProperty -Path $chaveAU -Name NoAutoUpdate -Value 1 -Type DWord -Force
Write-Host "  [ok] Política NoAutoUpdate = 1"

# 2. Serviços
foreach ($s in $servicos) {
    Stop-Service $s -Force -ErrorAction SilentlyContinue
    Set-InicioServico -Nome $s -Valor 4
    Write-Host "  [ok] Serviço $s parado e desabilitado"
}

# 3. Tarefas agendadas
$qtd = 0
foreach ($pasta in $pastasTarefas) {
    $tarefas = Get-ScheduledTask -TaskPath $pasta -ErrorAction SilentlyContinue
    foreach ($t in $tarefas) {
        Disable-ScheduledTask -InputObject $t -ErrorAction SilentlyContinue | Out-Null
        $qtd++
    }
}
Write-Host "  [ok] $qtd tarefas agendadas desabilitadas"

Write-Host "`nPronto. Reinicie o computador para concluir." -ForegroundColor Green
Write-Host "Lembre de aplicar patches de segurança manualmente de vez em quando." -ForegroundColor Yellow
Write-Host "Para reverter: desativa_windowsupdate.ps1 -Restaurar`n"