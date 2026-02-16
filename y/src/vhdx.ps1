# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1 | iex
#
# versao limpando cache:
# $headers = @{"Cache-Control"="no-cache"; "Pragma"="no-cache"}
# irm -Uri "https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1" -Headers $headers | iex
#
# link do desenvolvedor(acesso antecipado)
# irm http://4.203.cloudns.cl:8000/z_outros/src/vhdx.ps1 | iex
#
# vhdx win11 pro refs sem tpm 98G pronto para colocar usuario
# http://203.cloudns.cl:8000/z_outros/vhdx/win11_98G.vhdx
#

$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
$validaAdm=$currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if ( ! $validaAdm ){ Write-Host "Erro: Requer Admin." -ForegroundColor Red; pause; exit }

function Get-VHDXBootEntries {
    try {
        $bcdRaw = cmd /c "bcdedit /enum all /v"
        $results = @()
        $currentEntry = $null
        foreach ($line in $bcdRaw) {
            if ($line -match "{([a-f0-9-]{36})}") {
                if ($currentEntry -and $currentEntry.IsVHD) {
                    $results += [PSCustomObject]@{ GUID = $currentEntry.GUID; Desc = $currentEntry.Desc; Path = $currentEntry.Path }
                }
                $currentEntry = @{ GUID = $matches[0]; Desc = "n/a"; Path = "n/a"; IsVHD = $false }
            }
            if ($line -match "(?i)(descri|desc).*?\s+(.*)") {
                if ($currentEntry) { $currentEntry.Desc = $matches[2].Trim() }
            }
            if ($line -match "vhd=\[(.*?)\](.*)") {
                if ($currentEntry) {
                    $cleanPath = ("[$($matches[1])]$($matches[2])").Split(',')[0]
                    $currentEntry.Path  = $cleanPath
                    $currentEntry.IsVHD = $true
                }
            }
        }
        if ($currentEntry -and $currentEntry.IsVHD) {
            $results += [PSCustomObject]@{ GUID = $currentEntry.GUID; Desc = $currentEntry.Desc; Path = $currentEntry.Path }
        }
        return $results
    }
    catch { return @() }
}

function Add-VHDXToDualBoot {
    param ([string]$VHDXPath, [string]$CustomDesc)
    
    $path = $VHDXPath.Replace('"', '').Trim()
    if (-not (Test-Path $path)) { Write-Host "arquivo nao encontrado " -ForegroundColor Red; return }

    if ([string]::IsNullOrWhiteSpace($CustomDesc)) {
        $CustomDesc = [System.IO.Path]::GetFileNameWithoutExtension($path)
    }

    try {
        $drive = [System.IO.Path]::GetPathRoot($path).Replace("\","")
        $relPath = $path.Substring($drive.Length)
        if (-not $relPath.StartsWith("\")) { $relPath = "\" + $relPath }
        
        Write-Host "Adicionando entrada: $CustomDesc..." -ForegroundColor Cyan
        
        $copyOutput = cmd /c "bcdedit /copy {current} /d `"$CustomDesc`"" 2>$null
        if ($null -eq $copyOutput -or $copyOutput -match "incorret|error") {
            $copyOutput = cmd /c "bcdedit /copy {default} /d `"$CustomDesc`""
        }
        
        if ($copyOutput -match "{([a-fA-F0-9-]+)}") {
            $guid = $matches[0]
            $vhdStr = "vhd=[$drive]$relPath"
            cmd /c "bcdedit /set $guid device `"$vhdStr`""
            cmd /c "bcdedit /set $guid osdevice `"$vhdStr`""
            cmd /c "bcdedit /set $guid detecthal on"
            Write-Host "Sucesso! Adicionado com GUID $guid" -ForegroundColor Green
        }
    }
    catch { Write-Host "Erro ao adicionar." -ForegroundColor Red }
}

function Remove-AllVHDXBootEntries {
    try {
        Write-Host "Limpando entradas VHDX..." -ForegroundColor Cyan
        $bcdRaw = cmd /c "bcdedit /enum all /v"
        $targets = @()
        $currentBoot = bcdedit /get {current} | Select-String "identifier"
        $safeGuid = ""
        if ($currentBoot -match "{([a-f0-9-]{36})}") { $safeGuid = $matches[0] }

        foreach ($line in $bcdRaw) {
            if ($line -match "{([a-f0-9-]{36})}") { $lastGUID = $matches[0] }
            if ($line -match "\.vhd") {
                if ($lastGUID -and $lastGUID -ne $safeGuid -and $lastGUID -notmatch "{current}|{default}") {
                    $targets += $lastGUID
                }
            }
        }
        $targets | Select-Object -Unique | ForEach-Object {
            cmd /c "bcdedit /delete $_ /f"
            Write-Host "Removido: $_" -ForegroundColor Yellow
        }
    }
    catch { Write-Host "Erro na limpeza." -ForegroundColor Red }
}

function Set-BootTimeout {
    $currentTimeout = bcdedit /timeout | Select-String "\d+"
    Write-Host "`nTempo atual: $($currentTimeout) segundos." -ForegroundColor Cyan
    $newTimeout = Read-Host "Digite o novo tempo (segundos)"
    if ($newTimeout -match "^\d+$") {
        cmd /c "bcdedit /timeout $newTimeout"
        Write-Host "Tempo alterado!" -ForegroundColor Green
    }
}

# --- Menu ---
do {
    Write-Host "`n=== GERENCIADOR BOOT VHDX (v11.0) ===" -ForegroundColor Magenta
    Write-Host "1. Listar Entradas"
    Write-Host "2. Adicionar VHDX (Com Descricao)"
    Write-Host "3. Limpar Tudo (VHDX)"
    Write-Host "4. Ajustar Tempo (Timeout)"
    Write-Host "5. Sair"
    
    $op = Read-Host "Opcao"
    switch ($op) {
        "1" { 
            $v = Get-VHDXBootEntries
            if ($v) { $v | Format-Table -AutoSize } else { Write-Host "Vazio." -ForegroundColor Yellow }
        }
        "2" { 
            $p = Read-Host "Caminho do .vhdx"
            $d = Read-Host "Descricao"
            Add-VHDXToDualBoot -VHDXPath $p -CustomDesc $d 
        }
        "3" { Remove-AllVHDXBootEntries }
        "4" { Set-BootTimeout }
    }
} while ($op -ne "5")