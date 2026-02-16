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
        # Busca no BCD ignorando maiúsculas/minúsculas
        $bcdEnum = bcdedit /enum all /v
        # Split baseado em Identificador ou Identifier
        $entries = $bcdEnum -split "(?i)(?=\r?\nidentificador|identifier)"
        $results = @()

        foreach ($entry in $entries) {
            if ($entry -match "vhd=\[(.*?)\]") {
                # Regex flexível para Identificador/Identifier e GUID
                $guidMatch = $entry | Select-String "(?i)(identificador|identifier)\s+({[a-z0-9-]+})"
                $pathMatch = $entry | Select-String "vhd=\[(.*?)\]"
                $descMatch = $entry | Select-String "(?i)(description|descrição)\s+(.*)"
                
                if ($guidMatch -and $pathMatch) {
                    $results += [PSCustomObject]@{
                        GUID        = $guidMatch.Matches.Groups[2].Value
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
        # Extrai a letra da unidade (ex: C:) do caminho completo
        $driveLetter = [System.IO.Path]::GetPathRoot($VHDXPath).Replace("\","")
        $fileName = [System.IO.Path]::GetFileNameWithoutExtension($VHDXPath)

        Write-Host "Criando entrada para: $fileName..." -ForegroundColor Cyan
        
        # Cria a cópia e captura a saída
        $copyOutput = bcdedit /copy {current} /d "$fileName (VHDX)"
        
        if ($copyOutput -match "({[a-z0-9-]+})") {
            $guid = $matches[1]
            
            # Configura os parâmetros do VHDX
            bcdedit /set $guid device "vhd=[$driveLetter]\$([System.IO.Path]::GetFileName($VHDXPath))"
            bcdedit /set $guid osdevice "vhd=[$driveLetter]\$([System.IO.Path]::GetFileName($VHDXPath))"
            bcdedit /set $guid detecthal on
            
            Write-Host "Sucesso: VHDX adicionado com GUID $guid" -ForegroundColor Green
        } else {
            Write-Host "Erro ao capturar o GUID do novo item." -ForegroundColor Red
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
            # Procura por .vhd ou .vhdx na entrada
            if ($entry -match "\.vhd") {
                $guidMatch = $entry | Select-String "(?i)(identificador|identifier)\s+({[a-z0-9-]+})"
                if ($guidMatch) {
                    $guid = $guidMatch.Matches.Groups[2].Value
                    # Evita deletar a entrada atual por segurança
                    if ($entry -notmatch "{current}") {
                        bcdedit /delete $guid
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

# --- Menu Interativo ---
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