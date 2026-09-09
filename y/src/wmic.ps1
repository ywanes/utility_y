# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/wmic.ps1 | iex
#


$__oldEAP = $ErrorActionPreference
$ErrorActionPreference = 'Stop'

function Ok($m)   { Write-Host "[OK]    $m" -ForegroundColor Green }
function Info($m) { Write-Host "[..]    $m" -ForegroundColor Cyan }
function Warn($m) { Write-Host "[AVISO] $m" -ForegroundColor Yellow }
function Fail($m) { throw $m }   # capturado no fim; NAO fecha a janela

$CAP    = 'WMIC~~~~'
$wbem   = Join-Path $env:SystemRoot 'System32\Wbem'
$wbem32 = Join-Path $env:SystemRoot 'SysWOW64\wbem'
$exe    = Join-Path $wbem 'wmic.exe'
$script:restartNeeded = $false
$script:copiados = 0

# ---------------------------------------------------------------- utilitarios
function Decode-Bytes([byte[]]$b) {
    if ($b.Length -ge 2 -and $b[0] -eq 0xFF -and $b[1] -eq 0xFE) { return [Text.Encoding]::Unicode.GetString($b, 2, $b.Length - 2) }
    if ($b.Length -ge 4 -and $b[1] -eq 0 -and $b[3] -eq 0)       { return [Text.Encoding]::Unicode.GetString($b) }
    return [Console]::OutputEncoding.GetString($b)   # texto do cmd sai em OEM (850)
}

function Run-Cmd([string]$linha) {
    # roda no cmd de verdade, captura em arquivo (o wmic grava UTF-16) e devolve @{ rc; texto }
    $tmp = Join-Path $env:TEMP ('wmic_out_' + [guid]::NewGuid().ToString('N') + '.txt')
    try {
        & cmd.exe /d /c "$linha > `"$tmp`" 2>&1" | Out-Null
        $rc = $LASTEXITCODE
        $txt = ''
        if (Test-Path $tmp) { $txt = Decode-Bytes ([IO.File]::ReadAllBytes($tmp)) }
        return @{ rc = $rc; texto = $txt }
    } finally { if (Test-Path $tmp) { [IO.File]::Delete($tmp) } }
}

function Test-Wmic {
    if (-not (Test-Path $exe)) { return @{ ok = $false; resumo = "wmic.exe nao existe em $wbem" } }
    $t = Run-Cmd 'wmic os get caption'
    $v = Run-Cmd 'wmic os get caption /value'
    $tl = @($t.texto -split "`r?`n" | Where-Object { $_ -match '\S' })
    $vl = @($v.texto -split "`r?`n" | Where-Object { $_ -match 'Caption=' })
    $tabelaOk = ($t.rc -eq 0 -and $tl.Count -ge 2 -and $tl[0] -match '^Caption')
    $valueOk  = ($v.rc -eq 0 -and $vl.Count -ge 1)
    $resumo = "tabela: " + $(if ($tabelaOk) { 'OK' } else { "FALHOU (rc $($t.rc)) " + ($tl -join ' | ') }) + "; /value: " + $(if ($valueOk) { $vl[0].Trim() } else { "FALHOU (rc $($v.rc)) " + $v.texto.Trim() })
    return @{ ok = ($tabelaOk -and $valueOk); resumo = $resumo }
}

function Is-MicrosoftFile([string]$path) {
    try {
        $vi = (Get-Item -LiteralPath $path).VersionInfo
        if ($vi.CompanyName -notmatch 'Microsoft') { return $false }
        $s = Get-AuthenticodeSignature -LiteralPath $path
        return ($s.Status -eq 'Valid' -and $s.SignerCertificate.Subject -match 'Microsoft')
    } catch { return $false }
}

function Copy-One([string]$src, [string]$dst) {
    # ja existe igual, ou ja e um arquivo do proprio Windows (TrustedInstaller): mantem. Falha de um arquivo vira aviso, nao derruba a etapa.
    try {
        if (Test-Path -LiteralPath $dst) {
            if ((Get-Item -LiteralPath $dst).Length -eq (Get-Item -LiteralPath $src).Length) { return }
            if ($dst -match '\.(exe|mui)$' -and (Is-MicrosoftFile $dst)) { return }
        }
        $dir = Split-Path $dst
        if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
        Copy-Item -LiteralPath $src -Destination $dst -Force
        $script:copiados++
    } catch { Warn "nao copiei $dst : $($_.Exception.Message)" }
}

function Copy-Files([string]$srcDir, [string]$dstDir, [string]$filtro) {
    Get-ChildItem -LiteralPath $srcDir -File | Where-Object { $_.Name -match $filtro } | ForEach-Object { Copy-One $_.FullName (Join-Path $dstDir $_.Name) }
}

$FILTRO_RAIZ = '(?i)^(wmic\.exe|xsl-mappings\.xml|.*\.xsl|cli\.mof|cliegaliases\.mof)$'
$FILTRO_LANG = '(?i)^(wmic\.exe\.mui|.*\.xsl|.*\.mfl)$'

# ---------------------------------------------------------------- 2) WinSxS
function Find-WinSxS {
    # devolve hashtable "tipo|arch|lang" -> pasta (versao mais alta), so pastas com arquivos reais (ignora f\ e r\ de deltas)
    $best = @{}
    $root = Join-Path $env:SystemRoot 'WinSxS'
    $dirs = Get-ChildItem -LiteralPath $root -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -match '^(amd64|wow64|x86)_microsoft-windows-w\.\..*(utility|lity-base)' }   # nomes truncados: w..ommand-line-utility, w..d-line-utility-base, w..e-utility.resources, w..lity-base.resources
    foreach ($d in $dirs) {
        if ($d.Name -notmatch '^(amd64|wow64|x86)_.*_(\d+\.\d+\.\d+\.\d+)_([a-z]{2}(?:-[a-z0-9]+)?|none)_') { continue }
        $arch = $Matches[1]; $ver = [version]$Matches[2]; $lang = $Matches[3]
        if ($lang -ne 'none') { try { $lang = [Globalization.CultureInfo]::GetCultureInfo($lang).Name } catch { } }   # pt-br -> pt-BR
        $files = @(Get-ChildItem -LiteralPath $d.FullName -File -ErrorAction SilentlyContinue | ForEach-Object { $_.Name.ToLower() })
        if ($files.Count -eq 0) { continue }
        $tipo = $null
        if     ($files -contains 'wmic.exe')         { $tipo = 'exe' }
        elseif ($files -contains 'wmic.exe.mui')     { $tipo = 'mui' }
        elseif ($files -contains 'xsl-mappings.xml' -or $files -contains 'texttable.xsl') { $tipo = 'base' }
        elseif ($files -contains 'csv.xsl' -or $files -contains 'cli.mfl')                { $tipo = 'base-lang' }
        if (-not $tipo) { continue }
        $k = "$tipo|$arch|$lang"
        if (-not $best[$k] -or $best[$k].ver -lt $ver) { $best[$k] = @{ dir = $d.FullName; ver = $ver; arch = $arch; lang = $lang; tipo = $tipo } }
    }
    return $best
}

function Restore-FromWinSxS {
    $best = Find-WinSxS
    if ($best.Count -eq 0) { Info "WinSxS: nenhum componente do WMIC encontrado."; return $false }
    $best.Values | Sort-Object { $_.tipo }, { $_.arch }, { $_.lang } | ForEach-Object { Info ("WinSxS: {0,-9} {1,-5} {2,-6} v{3}" -f $_.tipo, $_.arch, $_.lang, $_.ver) }
    $fez = $false
    foreach ($arch in 'amd64', 'wow64') {
        $dest = if ($arch -eq 'amd64') { $wbem } else { $wbem32 }
        if ($arch -eq 'wow64' -and -not (Test-Path $wbem32)) { continue }
        $e = $best["exe|$arch|none"]; $b = $best["base|$arch|none"]
        if (-not $e -and -not (Test-Path (Join-Path $dest 'wmic.exe'))) { Warn "WinSxS: wmic.exe ($arch) nao encontrado."; if ($arch -eq 'amd64') { continue } }
        if ($e) {
            $src = Join-Path $e.dir 'WMIC.exe'
            if (Is-MicrosoftFile $src) { Copy-One $src (Join-Path $dest 'wmic.exe') } else { Warn "WinSxS: $src sem assinatura Microsoft valida; ignorado." }
        }
        if ($b) { Copy-Files $b.dir $dest $FILTRO_RAIZ } else { Warn "WinSxS: componente base (${arch}: xsl-mappings.xml, .xsl, .mof) nao encontrado." }
        foreach ($k in @($best.Keys)) {
            $v = $best[$k]
            if ($v.arch -ne $arch -or $v.lang -eq 'none') { continue }
            Copy-Files $v.dir (Join-Path $dest $v.lang) $FILTRO_LANG
        }
        $verTxt = if ($e) { "v$($e.ver)" } else { 'exe ja existente' }
        Ok "WinSxS: WMIC $arch ($verTxt) conferido/copiado em $dest"
        if ($arch -eq 'amd64') { $fez = $true }
    }
    return $fez
}

# ---------------------------------------------------------------- 3/4) pasta wbem de outro Windows
function Restore-FromWbem([string]$srcWbem, [string]$rotulo) {
    if (-not (Test-Path -LiteralPath (Join-Path $srcWbem 'wmic.exe'))) { Info "${rotulo}: sem wmic.exe em $srcWbem"; return $false }
    $src = Join-Path $srcWbem 'wmic.exe'
    if (-not (Is-MicrosoftFile $src)) { Warn "${rotulo}: $src nao tem assinatura Microsoft valida; ignorado."; return $false }
    $pares = @(@{ s = $srcWbem; d = $wbem })
    $src32 = $srcWbem -ireplace 'System32', 'SysWOW64'
    if ($src32 -ne $srcWbem -and (Test-Path -LiteralPath (Join-Path $src32 'wmic.exe')) -and (Test-Path $wbem32)) { $pares += @{ s = $src32; d = $wbem32 } }
    foreach ($p in $pares) {
        Copy-Files $p.s $p.d $FILTRO_RAIZ
        Get-ChildItem -LiteralPath $p.s -Directory | Where-Object { $_.Name -match '^[a-z]{2}(-[A-Za-z0-9]+)?$' } | ForEach-Object { Copy-Files $_.FullName (Join-Path $p.d $_.Name) $FILTRO_LANG }
        Ok "${rotulo}: WMIC copiado de $($p.s) para $($p.d)"
    }
    return $true
}

# ---------------------------------------------------------------- 5/6) DISM
function Invoke-Dism([string[]]$dismArgs) {
    Info ("dism " + ($dismArgs -join ' ') + "   (inicio " + (Get-Date -Format 'HH:mm:ss') + ")")
    $p = Start-Process -FilePath "$env:SystemRoot\System32\dism.exe" -ArgumentList $dismArgs -NoNewWindow -PassThru
    $null = $p.Handle   # sem isso o .ExitCode volta nulo no PowerShell 5.1
    $p.WaitForExit()
    Write-Host ""
    $rc = $p.ExitCode; if ($null -eq $rc) { $rc = -2 }
    Info ("dism terminou com codigo $rc (" + (Get-Date -Format 'HH:mm:ss') + ")")
    return $rc
}

function Get-CapState { try { return (Get-WindowsCapability -Online -Name $CAP).State } catch { return 'Desconhecido' } }

function Install-Fod([string]$fonte) {
    $st = Get-CapState
    Info "Estado do recurso $CAP no CBS: $st"
    if ($st -eq 'Installed' -and -not (Test-Path $exe)) {
        Warn "CBS diz 'Installed' mas o wmic.exe nao existe (registro orfao do upgrade). Removendo o registro para reinstalar..."
        [void](Invoke-Dism @('/Online', '/Remove-Capability', "/CapabilityName:$CAP", '/NoRestart'))
    } elseif ($st -eq 'Installed') {
        Info "CBS ja diz 'Installed' e o wmic.exe existe; o DISM nao vai trazer nada novo. Completando com o WinSxS."
        return (Test-Path $exe)
    }
    $a = @('/Online', '/Add-Capability', "/CapabilityName:$CAP", '/NoRestart')
    if ($fonte) { $a += "/Source:$fonte"; $a += '/LimitAccess' }
    $rc = Invoke-Dism $a
    if ($rc -eq 3010) { $script:restartNeeded = $true }
    elseif ($rc -ne 0) { Warn "DISM falhou (codigo $rc). Detalhes: $env:SystemRoot\Logs\DISM\dism.log" }
    return (Test-Path $exe)
}

# ---------------------------------------------------------------- PATH
function Ensure-Path {
    $regKey   = 'HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
    $rawPath  = (Get-Item $regKey).GetValue('Path', '', 'DoNotExpandEnvironmentNames')
    $expanded = [Environment]::ExpandEnvironmentVariables($rawPath)
    $hasWbem  = @($expanded -split ';' | Where-Object { $_.TrimEnd('\') -ieq $wbem }).Count -gt 0
    if ($hasWbem) { Ok "PATH do sistema ja contem $wbem"; $changed = $false }
    else {
        Info "Adicionando %SystemRoot%\System32\Wbem ao PATH do sistema..."
        Set-ItemProperty -Path $regKey -Name Path -Value ($rawPath.TrimEnd(';') + ';%SystemRoot%\System32\Wbem') -Type ExpandString
        try {
            $sig = '[DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)] public static extern IntPtr SendMessageTimeout(IntPtr hWnd, uint Msg, UIntPtr wParam, string lParam, uint fuFlags, uint uTimeout, out UIntPtr lpdwResult);'
            $nm  = Add-Type -MemberDefinition $sig -Name 'NativeMethods' -Namespace 'Win32Env' -PassThru
            $res = [UIntPtr]::Zero
            $nm::SendMessageTimeout([IntPtr]0xFFFF, 0x1A, [UIntPtr]::Zero, 'Environment', 2, 5000, [ref]$res) | Out-Null
        } catch { Warn "Nao consegui notificar o sistema sobre o PATH (WM_SETTINGCHANGE): $($_.Exception.Message)" }
        Ok "PATH do sistema atualizado."; $changed = $true
    }
    if (@($env:Path -split ';' | Where-Object { $_.TrimEnd('\') -ieq $wbem }).Count -eq 0) { $env:Path += ";$wbem" }
    return $changed
}

# ---------------------------------------------------------------- etapa generica: tenta e testa (nunca desfaz)
function Try-Step([string]$nome, [scriptblock]$acao) {
    Write-Host ""
    Info "Etapa: $nome"
    $script:copiados = 0
    $fez = $false
    try { $fez = & $acao } catch { Warn "$nome falhou: $($_.Exception.Message)" }
    if ($script:copiados -gt 0) { Info "$($script:copiados) arquivo(s) copiado(s)." }
    if (-not $fez -and $script:copiados -eq 0) { return $false }
    $t = Test-Wmic
    if ($t.ok) { Ok "wmic respondeu no cmd. $($t.resumo)"; return $true }
    Warn "$nome nao bastou. $($t.resumo)"
    return $false
}

# ---------------------------------------------------------------- principal
function Main {
    $id      = [Security.Principal.WindowsIdentity]::GetCurrent()
    $isAdmin = (New-Object Security.Principal.WindowsPrincipal $id).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
    if (-not $isAdmin) { Fail "Rode em um PowerShell como Administrador (botao direito no PowerShell > Executar como administrador)." }
    if ([Environment]::Is64BitOperatingSystem -and -not [Environment]::Is64BitProcess) { Fail "Use o PowerShell 64 bits (nao o '(x86)')." }
    Ok "PowerShell em modo Administrador ($($id.Name))."
    $cv = Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows NT\CurrentVersion'
    Info "Windows build $($cv.CurrentBuildNumber).$($cv.UBR) ($($cv.DisplayVersion)), idioma $((Get-UICulture).Name)."

    $pathChanged = Ensure-Path

    $origem = $null
    $t = Test-Wmic
    if ($t.ok) { Ok "wmic ja responde no cmd. $($t.resumo)"; $origem = 'ja instalado' }
    else {
        Info "wmic nao esta funcionando. $($t.resumo)"

        if (-not $origem -and (Try-Step 'WinSxS (componentes originais desta maquina)' { Restore-FromWinSxS })) { $origem = 'WinSxS' }

        $old = Join-Path $env:SystemDrive 'Windows.old\Windows\System32\wbem'
        if (-not $origem -and (Test-Path $old) -and (Try-Step 'Windows.old' { Restore-FromWbem $old 'Windows.old' })) { $origem = 'Windows.old' }

        if (-not $origem -and $env:WMIC_ORIGEM -and (Try-Step "WMIC_ORIGEM ($($env:WMIC_ORIGEM))" { Restore-FromWbem $env:WMIC_ORIGEM 'WMIC_ORIGEM' })) { $origem = 'WMIC_ORIGEM' }
        elseif (-not $origem -and -not $env:WMIC_ORIGEM) { Info "WMIC_ORIGEM nao definido (pasta wbem de outro Windows); pulando." }

        if (-not $origem -and $env:WMIC_FONTE -and (Try-Step "WMIC_FONTE ($($env:WMIC_FONTE)) via DISM" { if (Install-Fod $env:WMIC_FONTE) { [void](Restore-FromWinSxS); $true } else { $false } })) { $origem = 'Feature on Demand (WMIC_FONTE)' }
        elseif (-not $origem -and -not $env:WMIC_FONTE) { Info "WMIC_FONTE nao definido (pasta com os .cab do FoD); pulando." }

        if (-not $origem) {
            Write-Host ""
            Warn "Restou o Windows Update. Pode levar ~20 min."
            $resp = 'S'
            try { $resp = Read-Host "Tentar pelo Windows Update agora? [S/n]" } catch { $resp = 'S' }
            if ($resp -match '^\s*[nN]') { Warn "Windows Update pulado a pedido." }
            elseif (Try-Step 'Windows Update via DISM' { if (Install-Fod '') { [void](Restore-FromWinSxS); $true } else { $false } }) { $origem = 'Feature on Demand (Windows Update)' }
        }
    }

    Write-Host ""
    if ($origem) {
        $where = & cmd.exe /d /c "where wmic 2>nul"
        if ($LASTEXITCODE -eq 0) { Ok "cmd resolve 'wmic' em: $(($where | Select-Object -First 1).Trim())" }
        $vi = (Get-Item $exe).VersionInfo
        Ok "Concluido: WMIC original da Microsoft funcionando (v$($vi.FileVersion), origem: $origem)."
    } else {
        Write-Host "[ERRO]  Nao consegui deixar o wmic funcionando por nenhuma das fontes." -ForegroundColor Red
        Write-Host "        Opcoes que faltaram: `$env:WMIC_ORIGEM = pasta wbem de um Windows que ainda tenha o wmic (ex.: \\pc\c`$\Windows\System32\wbem)" -ForegroundColor Red
        Write-Host "                             `$env:WMIC_FONTE  = pasta com os .cab do FoD (ISO 'Languages and Optional Features')" -ForegroundColor Red
        Write-Host "        Defina uma delas e rode o script de novo." -ForegroundColor Red
    }
    if ($pathChanged)          { Warn "Janelas de cmd/PowerShell que JA estavam abertas precisam ser reabertas para enxergar o PATH novo." }
    if ($script:restartNeeded) { Warn "O DISM pediu reinicializacao; o wmic.exe pode aparecer so depois do reboot." }
}

try { Main }
catch { Write-Host ""; Write-Host "[ERRO]  $($_.Exception.Message)" -ForegroundColor Red }
finally { $ErrorActionPreference = $__oldEAP }
