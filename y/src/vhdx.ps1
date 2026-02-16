# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1 | iex
#
# versao limpando cache:
# $headers = @{"Cache-Control"="no-cache"; "Pragma"="no-cache"}
# irm -Uri "https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1" -Headers $headers | iex


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

        if ($results.Count -gt 0) {
            Write-Host "`n--- Entradas VHDX Encontradas ---" -ForegroundColor Cyan
            $results | Format-Table -AutoSize
        } else {
            Write-Host "`nNenhuma entrada VHDX detectada." -ForegroundColor Yellow
        }
    }
    catch { Write-Host "Erro na consulta." -ForegroundColor Red }
}

function Add-VHDXToDualBoot {
    param ([string]$VHDXPath, [string]$CustomDesc)
    
    $path = $VHDXPath.Replace('"', '').Trim()
    if (-not (Test-Path $path)) { Write-Host "Caminho invalido: $path" -ForegroundColor Red; return }

    # Fallback se a descricao estiver vazia
    if ([string]::IsNullOrWhiteSpace($CustomDesc)) {
        $CustomDesc = [System.IO.Path]::GetFileNameWithoutExtension($path)
    }

    try {
        $drive = [System.IO.Path]::GetPathRoot($path).Replace("\","")
        $relPath = $path.Substring($drive.Length)
        if (-not $relPath.StartsWith("\")) { $relPath = "\" + $relPath }
        
        Write-Host "Criando entrada: $CustomDesc..." -ForegroundColor Cyan
        
        # Execucao via CMD para garantir integridade das chaves {}
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
            
            Write-Host "Sucesso! Entrada '$CustomDesc' adicionada com GUID $guid" -ForegroundColor Green
        } else {
            Write-Host "Falha ao capturar GUID. Saida: $copyOutput" -ForegroundColor Red
        }
    }
    catch { Write-Host "Erro ao adicionar VHDX." -ForegroundColor Red }
}

function Remove-AllVHDXBootEntries {
    try {
        Write-Host "Iniciando limpeza forcada..." -ForegroundColor Cyan
        $bcdRaw = cmd /c "bcdedit /enum all /v"
        $targets = @()
        $lastGUID = ""

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

        $targets = $targets | Select-Object -Unique
        foreach ($g in $targets) {
            cmd /c "bcdedit /delete $g /f"
            Write-Host "Removido: $g" -ForegroundColor Yellow
        }
        Write-Host "Limpeza concluida." -ForegroundColor Green
    }
    catch { Write-Host "Erro na limpeza." -ForegroundColor Red }
}

function Set-BootTimeout {
    try {
        $currentTimeout = bcdedit /timeout | Select-String "\d+"
        Write-Host "`nTempo atual de espera: $($currentTimeout) segundos." -ForegroundColor Cyan
        $newTimeout = Read-Host "Digite o novo tempo em segundos"
        if ($newTimeout -match "^\d+$") {
            cmd /c "bcdedit /timeout $newTimeout"
            Write-Host "Tempo alterado para $newTimeout segundos!" -ForegroundColor Green
        }
    }
    catch { Write-Host "Erro ao ajustar timeout." -ForegroundColor Red }
}

# --- Menu ---
do {
    Write-Host "`n=== GERENCIADOR BOOT VHDX (v9.0) ===" -ForegroundColor Magenta
    Write-Host "1. Listar VHDX"
    Write-Host "2. Adicionar VHDX (Caminho + Descricao)"
    Write-Host "3. Limpar Tudo (FORCE)"
    Write-Host "4. Ajustar Tempo de Boot (Timeout)"
    Write-Host "5. Sair"
    
    $op = Read-Host "Opcao"
    switch ($op) {
        "1" { Get-VHDXBootEntries }
        "2" { 
            $p = Read-Host "Digite o caminho completo do .vhdx"
            $d = Read-Host "Digite o nome para o menu de boot (Ex: Windows 11 Trabalho)"
            Add-VHDXToDualBoot -VHDXPath $p -CustomDesc $d 
        }
        "3" { Remove-AllVHDXBootEntries }
        "4" { Set-BootTimeout }
        "5" { break }
    }
} while ($op -ne "5")