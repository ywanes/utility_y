# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/wmic.ps1 | iex
#

$ErrorActionPreference = 'Stop'

function Ok($m)   { Write-Host "[OK]    $m" -ForegroundColor Green }
function Info($m) { Write-Host "[..]    $m" -ForegroundColor Cyan }
function Warn($m) { Write-Host "[AVISO] $m" -ForegroundColor Yellow }
function Fail($m) { Write-Host "[ERRO]  $m" -ForegroundColor Red; exit 1 }

# ---------- 1) Admin? ----------
$id      = [Security.Principal.WindowsIdentity]::GetCurrent()
$isAdmin = (New-Object Security.Principal.WindowsPrincipal $id).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Fail "Este script precisa rodar em um PowerShell como Administrador (botao direito no PowerShell > Executar como administrador)."
}
Ok "PowerShell em modo Administrador ($($id.Name))."

# ---------- 2) wmic.exe existe? Se nao, instala ----------
$wbem    = Join-Path $env:SystemRoot 'System32\Wbem'
$wmicExe = Join-Path $wbem 'wmic.exe'
$capName = 'WMIC~~~~'
$restartNeeded = $false

if (Test-Path $wmicExe) {
    Ok "wmic.exe ja existe: $wmicExe"
} else {
    Info "wmic.exe nao encontrado. Verificando recurso opcional '$capName'..."
    $cap = $null
    try { $cap = Get-WindowsCapability -Online -Name $capName } catch { Warn "Get-WindowsCapability falhou: $($_.Exception.Message)" }

    if ($cap) { Info "Estado atual do recurso: $($cap.State)" }

    if ($cap -and $cap.State -eq 'Installed') {
        Warn "Recurso consta como Installed mas o exe nao existe. Removendo para reinstalar..."
        try { Remove-WindowsCapability -Online -Name $capName | Out-Null } catch { Warn "Remove falhou: $($_.Exception.Message)" }
    }

    Info "Instalando '$capName' (baixa do Windows Update; pode levar alguns minutos)..."
    $installed = $false
    try {
        $r = Add-WindowsCapability -Online -Name $capName
        if ($r.RestartNeeded) { $restartNeeded = $true }
        $installed = $true
    } catch {
        Warn "Add-WindowsCapability falhou: $($_.Exception.Message)"
        Info "Tentando via DISM..."
        & dism.exe /Online /Add-Capability /CapabilityName:$capName /NoRestart | Out-Host
        if ($LASTEXITCODE -eq 0)    { $installed = $true }
        if ($LASTEXITCODE -eq 3010) { $installed = $true; $restartNeeded = $true }
    }

    if (-not $installed -or -not (Test-Path $wmicExe)) {
        Fail ("Nao foi possivel instalar o WMIC. Verifique acesso ao Windows Update " +
              "(a politica 'Especificar configuracoes para instalacao de componentes opcionais' pode bloquear) " +
              "ou instale manualmente: Configuracoes > Sistema > Recursos opcionais > Adicionar um recurso > WMIC.")
    }
    Ok "WMIC instalado: $wmicExe"
}

# ---------- 3) PATH do sistema contem Wbem? ----------
$regKey   = 'HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
$rawPath  = (Get-Item $regKey).GetValue('Path', '', 'DoNotExpandEnvironmentNames')
$expanded = [Environment]::ExpandEnvironmentVariables($rawPath)
$hasWbem  = @($expanded -split ';' | Where-Object { $_.TrimEnd('\') -ieq $wbem }).Count -gt 0
$pathChanged = $false

if ($hasWbem) {
    Ok "PATH do sistema ja contem $wbem"
} else {
    Info "Adicionando %SystemRoot%\System32\Wbem ao PATH do sistema..."
    $newPath = $rawPath.TrimEnd(';') + ';%SystemRoot%\System32\Wbem'
    Set-ItemProperty -Path $regKey -Name Path -Value $newPath -Type ExpandString
    $pathChanged = $true

    # avisa o Explorer/novos processos que o ambiente mudou (WM_SETTINGCHANGE)
    try {
        $sig = '[DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)] public static extern IntPtr SendMessageTimeout(IntPtr hWnd, uint Msg, UIntPtr wParam, string lParam, uint fuFlags, uint uTimeout, out UIntPtr lpdwResult);'
        $nm  = Add-Type -MemberDefinition $sig -Name 'NativeMethods' -Namespace 'Win32Env' -PassThru
        $res = [UIntPtr]::Zero
        $nm::SendMessageTimeout([IntPtr]0xFFFF, 0x1A, [UIntPtr]::Zero, 'Environment', 2, 5000, [ref]$res) | Out-Null
    } catch { Warn "Nao consegui notificar o sistema sobre o PATH (WM_SETTINGCHANGE): $($_.Exception.Message)" }
    Ok "PATH do sistema atualizado."
}

# garante no processo atual (para o teste abaixo)
if (@($env:Path -split ';' | Where-Object { $_.TrimEnd('\') -ieq $wbem }).Count -eq 0) { $env:Path += ";$wbem" }

# ---------- 4) Teste real no cmd ----------
Info "Testando no cmd: wmic os get caption /value"
$out  = & cmd.exe /d /c "wmic os get caption /value 2>&1"
$code = $LASTEXITCODE
$txt  = ($out -join "`n")
if ($code -eq 0 -and $txt -match 'Caption=') {
    $cap = ($out | Where-Object { $_ -match 'Caption=' } | Select-Object -First 1).Trim()
    Ok "wmic respondeu no cmd: $cap"
} else {
    Fail "wmic nao respondeu corretamente no cmd (codigo $code):`n$txt"
}

$where = & cmd.exe /d /c "where wmic 2>nul"
if ($LASTEXITCODE -eq 0) { Ok "cmd resolve 'wmic' em: $(($where | Select-Object -First 1).Trim())" }
else { Warn "cmd nao resolveu 'wmic' pelo PATH neste processo." }

# ---------- Resumo ----------
Write-Host ""
Ok "Concluido: WMIC habilitado e funcionando para o cmd."
if ($pathChanged)   { Warn "Janelas de cmd/PowerShell que JA estavam abertas precisam ser fechadas e reabertas para enxergar o PATH novo." }
if ($restartNeeded) { Warn "O Windows pediu reinicializacao para concluir a instalacao do recurso." }
exit 0
