# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1 | iex
#
# versao limpando cache:
# $headers = @{"Cache-Control"="no-cache"; "Pragma"="no-cache"}
# irm -Uri "https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/vhdx.ps1" -Headers $headers | iex


$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
$validaAdm=$currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if ( ! $validaAdm ){
  echo ""
  echo "Erro... Eh preciso estar no powershell adm"
  echo ""
  pause
  exit
}

function Get-VHDXBootEntries {
    <#
    .SYNOPSIS
        Lista todas as entradas de boot que utilizam arquivos VHDX.
    #>
    try {
        $bcdEnum = bcdedit /enum all /v
        $entries = $bcdEnum -split "(?=\r?\nIdentificador)"
        $results = @()

        foreach ($entry in $entries) {
            if ($entry -match "vhd=\[(.*?)\]") {
                $guidMatch = $entry | Select-String "identificador\s+({[a-z0-9-]+})"
                $pathMatch = $entry | Select-String "vhd=\[(.*?)\]"
                $descMatch = $entry | Select-String "description\s+(.*)"
                
                if ($guidMatch -and $pathMatch) {
                    $results += [PSCustomObject]@{
                        GUID        = $guidMatch.Matches.Groups[1].Value
                        Description = ($descMatch.Matches.Groups[1].Value).Trim()
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
    <#
    .SYNOPSIS
        Adiciona um arquivo VHDX ao menu de boot.
    #>
    param (
        [Parameter(Mandatory=$true)]
        [string]$VHDXPath
    )

    if (-not (Test-Path $VHDXPath)) {
        Write-Host "ERRO: Arquivo não encontrado no caminho: $VHDXPath" -ForegroundColor Red
        return
    }

    try {
        $copyCmd = bcdedit /copy {current} /d "novo_item"
        
        if ($copyCmd -match "{([a-z0-9-]+)}") {
            $guid = "{$($matches[1])}"
            
            bcdedit /set $guid device "vhd=[$VHDXPath]"
            bcdedit /set $guid osdevice "vhd=[$VHDXPath]"
            bcdedit /set $guid detecthal on
            
            Write-Host "Sucesso: VHDX adicionado com GUID $guid" -ForegroundColor Green
        }
    }
    catch {
        Write-Error "Falha ao adicionar VHDX: $_"
    }
}

function Remove-AllVHDXBootEntries {
    <#
    .SYNOPSIS
        Remove todas as entradas do menu de boot que contenham a extensão .vhdx.
    #>
    try {
        $bcdEnum = bcdedit /enum all /v
        $entries = $bcdEnum -split "(?=\r?\nIdentificador)"
        $count = 0

        foreach ($entry in $entries) {
            if ($entry -match "\.vhdx") {
                $guidMatch = $entry | Select-String "identificador\s+({[a-z0-9-]+})"
                if ($guidMatch) {
                    $guid = $guidMatch.Matches.Groups[1].Value
                    bcdedit /delete $guid
                    Write-Host "Removida entrada: $guid" -ForegroundColor Yellow
                    $count++
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
    Write-Host "   GERENCIADOR DE BOOT VHDX (ADMIN)   " -ForegroundColor White
    Write-Host "========================================" -ForegroundColor Magenta
    Write-Host "1) Consultar entradas VHDX atuais"
    Write-Host "2) Adicionar novo VHDX ao Dual Boot"
    Write-Host "3) Remover TODAS as entradas VHDX"
    Write-Host "4) Sair"
    Write-Host "----------------------------------------"
    
    $choice = Read-Host "Selecione uma opção"

    switch ($choice) {
        "1" {
            Get-VHDXBootEntries
        }
        "2" {
            $path = Read-Host "Digite o caminho completo do arquivo .vhdx"
            # Remove aspas caso o usuário tenha colado com elas
            $path = $path.Replace('"', '') 
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
        Default {
            Write-Host "Opção inválida." -ForegroundColor Red
        }
    }
} while ($choice -ne "4")