# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1 | iex
#
# versao limpando cache:
# $headers = @{"Cache-Control"="no-cache"; "Pragma"="no-cache"}
# irm -Uri "https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1" -Headers $headers | iex


$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
$validaAdm=$currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if ( ! $validaAdm ){
    Write-Host "`nErro... É preciso executar como Administrador!`n" -ForegroundColor Red
    pause
    exit
}

function Get-VHDXBootEntries {
    try {
        $bcdEnum = bcdedit /enum all /v
        $entries = $bcdEnum -split "(?i)(?=\r?\nidentificador|identifier)"
        $results = @()

        foreach ($entry in $entries) {
            if ($entry -match "vhd=\[(.*?)\]") {
                $guidMatch = $entry | Select-String "(?i)({[a-f0-9-]+})"
                $pathMatch = $entry | Select-String "vhd=\[(.*?)\]"
                $descMatch = $entry | Select-String "(?i)(description|descrição)\s+(.*)"
                
                if ($guidMatch -and $pathMatch) {
                    $results += [PSCustomObject]@{
                        GUID        = $guidMatch.Matches[0].Value
                        Description = if ($descMatch) { ($descMatch.Matches.Groups[2].Value).Trim() } else { "Sem Descrição" }
                        VHDXPath    = $pathMatch.Matches.Groups[1].Value
                    }
                }
            }
        }
        
        if ($results.Count -gt 0) {
            Write-Host "`n--- Entradas VHDX Encontradas ---" -ForegroundColor Cyan
            $results | Format-Table -AutoSize
        } else {
            Write-Host "`nNenhuma entrada VHDX encontrada no BCD." -ForegroundColor Yellow
        }
    }
    catch {
        Write-Error "Erro ao consultar entradas: $_"
    }
}

function Add-VHDXToDualBoot {
    param (
        [Parameter(Mandatory=$true)]
        [string]$VHDXPath
    )

    if (-not (Test-Path $VHDXPath)) {
        Write-Host "ERRO: Arquivo não encontrado no caminho: $VHDXPath" -ForegroundColor Red
        return
    }

    try {
        $drive = [System.IO.Path]::GetPathRoot($VHDXPath).Replace("\","")
        $relativeVHDPath = $VHDXPath.Substring($drive.Length)
        if (-not $relativeVHDPath.StartsWith("\")) { $relativeVHDPath = "\" + $relativeVHDPath }
        
        $fileName = [System.IO.Path]::GetFileNameWithoutExtension($VHDXPath)
        $description = "$fileName (VHDX)"

        Write-Host "Criando entrada para: $fileName..." -ForegroundColor Cyan
        
        # Tentativa 1: Usando {current}. Tentativa 2: Usando {default} se a 1 falhar.
        # Usamos --% para parar o parsing do PowerShell e enviar o comando puro ao CMD
        $copyOutput = cmd /c "bcdedit /copy {current} /d `"$description`"" 2>$null
        
        if ($null -eq $copyOutput -or $copyOutput -match "incorreto" -or $copyOutput -eq "") {
            $copyOutput = cmd /c "bcdedit /copy {default} /d `"$description`""
        }
        
        if ($copyOutput -match "{([a-fA-F0-9-]+)}") {
            $guid = "{$($matches[1])}"
            $vhdBcdPath = "vhd=[$drive]$relativeVHDPath"
            
            # Configuração das propriedades
            cmd /c "bcdedit /set $guid device `"$vhdBcdPath`""
            cmd /c "bcdedit /set $guid osdevice `"$vhdBcdPath`""
            cmd /c "bcdedit /set $guid detecthal on"
            
            Write-Host "Sucesso: VHDX adicionado com GUID $guid" -ForegroundColor Green
        } else {
            Write-Host "Erro: O BCD não aceitou o comando de cópia." -ForegroundColor Red
            Write-Host "Saída: $copyOutput"
        }
    }
    catch {
        Write-Error "Falha ao adicionar VHDX: $_"
    }
}

function Remove-AllVHDXBootEntries {
    try {
        $bcdEnum = bcdedit /enum all /v
        $entries = $bcdEnum -split "(?i)(?=\r?\nidentificador|identifier)"
        $count = 0

        foreach ($entry in $entries) {
            if ($entry -match "\.vhdx?") {
                $guidMatch = $entry | Select-String "(?i)({[a-f0-9-]+})"
                if ($guidMatch) {
                    $guid = $guidMatch.Matches[0].Value
                    if ($entry -notmatch "{current}" -and $entry -notmatch "{default}") {
                        cmd /c "bcdedit /delete $guid"
                        Write-Host "Removida entrada: $guid" -ForegroundColor Yellow
                        $count++
                    }
                }
            }
        }
        Write-Host "Limpeza concluída. Total de entradas removidas: $count" -ForegroundColor Green
    }
    catch {
        Write-Error "Falha ao remover entradas: $_"
    }
}

# --- Menu ---
do {
    Write-Host "`n========================================" -ForegroundColor Magenta
    Write-Host "    GERENCIADOR DE BOOT VHDX (ADMIN)    " -ForegroundColor White
    Write-Host "========================================" -ForegroundColor Magenta
    Write-Host "1) Consultar entradas VHDX atuais"
    Write-Host "2) Adicionar novo VHDX ao Dual Boot"
    Write-Host "3) Remover TODAS as entradas VHDX"
    Write-Host "4) Sair"
    Write-Host "----------------------------------------"
    
    $choice = Read-Host "Selecione uma opção"

    switch ($choice) {
        "1" { Get-VHDXBootEntries }
        "2" {
            $path = Read-Host "Digite o caminho completo do arquivo .vhdx"
            $path = $path.Replace('"', '').Trim()
            Add-VHDXToDualBoot -VHDXPath $path
        }
        "3" {
            $confirm = Read-Host "Tem certeza que deseja remover TODAS as entradas VHDX? (S/N)"
            if ($confirm -eq "S" -or $confirm -eq "s") {
                Remove-AllVHDXBootEntries
            }
        }
        "4" {
            Write-Host "Encerrando..." -ForegroundColor Cyan
            break
        }
        Default { Write-Host "Opção inválida." -ForegroundColor Red }
    }
} while ($choice -ne "4")