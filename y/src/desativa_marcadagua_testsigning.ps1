# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/desativa_marcadagua_testsigning.ps1 | iex
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/desativa_marcadagua_testsigning.ps1))) -Desativa
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/desativa_marcadagua_testsigning.ps1))) -Reativa
# Sem parametro (ou via "| iex"), desativa.

param(
    [switch]$Reativa,
    [switch]$Desativa,
    [switch]$Reiniciar
)

$isAdmin = ([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()
           ).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "Execute este script em um PowerShell aberto como Administrador." -ForegroundColor Red
    return
}

if ($Reativa -and $Desativa) {
    Write-Host "Use apenas -Reativa OU -Desativa." -ForegroundColor Red
    return
}

if (-not $Reativa -and -not $Desativa) { $Desativa = $true }

$ativo = (bcdedit /enum "{current}" | Out-String) -match "testsigning\s+Yes"

if ($Reativa) {
    if ($ativo) { Write-Host "O Modo de Teste ja esta ligado." -ForegroundColor Yellow; return }
    bcdedit /set testsigning on | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Falhou. Provavelmente o Secure Boot esta ligado (desligue na BIOS/UEFI)." -ForegroundColor Red
        return
    }
    Write-Host "Modo de Teste LIGADO." -ForegroundColor Green
}

if ($Desativa) {
    if (-not $ativo) { Write-Host "O Modo de Teste ja esta desligado." -ForegroundColor Yellow; return }
    bcdedit /set testsigning off | Out-Null
    bcdedit /set nointegritychecks off | Out-Null
    Write-Host "Modo de Teste DESLIGADO." -ForegroundColor Green
}

if ($Reiniciar) {
    Restart-Computer -Force
} elseif ((Read-Host "E preciso reiniciar para aplicar. Reiniciar agora? (S/N)") -match '^[Ss]') {
    Restart-Computer -Force
}