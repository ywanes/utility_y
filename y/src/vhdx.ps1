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

# colocando windows disco no dual boot
# # com o cmd como administrador, rode:
# bcdboot E:\Windows /d
# # olhe
# bcdedit /enum
# # Procure a entrada cujo device é partition=E:, copie o identificador (algo como {a1b2c3d4-...}) e renomeie:
# bcdedit /set {a1b2c3d4-...} description "Win11 ReFS"
# # Para garantir que o menu apareça e dê tempo de escolher:
# bcdedit /set {bootmgr} displaybootmenu yes
# bcdedit /timeout 10


# se precisar fazer um efi novo quando o disco é novo e tem espaço para criar o efi
# diskpart
# list disk
# Veja qual número tem o disco de ~40 GB e use no lugar do N:
# select disk N
# create partition efi size=100
# format quick fs=fat32 label="System"
# assign letter=S
# exit
# bcdboot E:\Windows /s S: /f UEFI /addlast
# removendo a letra S:
# mountvol S: /d
# obs: efi nao precisa estar no começo do disco!


# download https://wimlib.net
# copiando windows para outra particao na mao - recomendado para destino menos com refs:
# wimlib-imagex capture F:\ C:\temp.wim --compress=none
# wimlib-imagex apply C:\temp.wim 1 E:\

$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
$validaAdm = $currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if ( ! $validaAdm ){ Write-Host "Erro: Requer Admin." -ForegroundColor Red; pause; exit }

$RX_GUID = '\{[0-9a-fA-F-]{36}\}'

# ---------------------------------------------------------------
# Leitura do BCD
# ---------------------------------------------------------------

# Entradas de SO (osloader). Uma entrada nova so comeca na linha
# identifier/identificador, assim GUIDs de inherit/recoverysequence
# nao sao confundidos com a entrada.
function Get-BcdOsEntries {
    $entries = @(); $cur = $null
    foreach ($line in (bcdedit /enum osloader /v)) {
        if ($line -match "^(identifier|identificador)\s+($RX_GUID)") {
            if ($cur) { $entries += [pscustomobject]$cur }
            $cur = @{ GUID = $matches[2].ToLower(); Desc = ''; Device = ''; OsDevice = ''; Path = ''; IsVHD = $false }
        }
        elseif ($cur -and $line -match '^descri\S*\s+(.*)$') { $cur.Desc     = $matches[1].Trim() }
        elseif ($cur -and $line -match '^device\s+(.*)$')     { $cur.Device   = $matches[1].Trim() }
        elseif ($cur -and $line -match '^osdevice\s+(.*)$')   { $cur.OsDevice = $matches[1].Trim() }
    }
    if ($cur) { $entries += [pscustomobject]$cur }

    foreach ($e in $entries) {
        $dev = if ($e.OsDevice) { $e.OsDevice } else { $e.Device }
        $e.IsVHD = ($dev -like 'vhd=*')
        $e.Path  = ($dev -replace '^vhd=', '').Split(',')[0]
    }
    return $entries
}

# GUID real da entrada em uso
function Get-CurrentGuid {
    $m = bcdedit /enum '{current}' /v | Select-String "^(identifier|identificador)\s+($RX_GUID)" | Select-Object -First 1
    if ($m) { return $m.Matches[0].Groups[2].Value.ToLower() }
    return ''
}

# Ordem do menu, entrada padrao e timeout do Boot Manager
function Get-BootManagerInfo {
    $order = @(); $default = ''; $timeout = ''; $inOrder = $false
    foreach ($line in (bcdedit /enum '{bootmgr}' /v)) {
        $g = $null
        if ($line -match "^displayorder\s+($RX_GUID)") { $inOrder = $true; $g = $matches[1] }
        elseif ($inOrder -and $line -match "^\s+($RX_GUID)") { $g = $matches[1] }
        else {
            $inOrder = $false
            if ($line -match "^default\s+($RX_GUID)") { $default = $matches[1].ToLower() }
            if ($line -match '^timeout\s+(\d+)')      { $timeout = $matches[1] }
        }
        if ($g) { $order += $g.ToLower() }
    }
    return [pscustomobject]@{ Order = $order; Default = $default; Timeout = $timeout }
}

# Menu de boot na ordem atual, com marcas
function Get-BootMenu {
    $mgr = Get-BootManagerInfo
    $os  = @{}; Get-BcdOsEntries | ForEach-Object { $os[$_.GUID] = $_ }
    $cur = Get-CurrentGuid
    $i = 0
    foreach ($g in $mgr.Order) {
        $i++
        $e = $os[$g]
        $marcas = @()
        if ($g -eq $mgr.Default) { $marcas += 'padrao' }
        if ($g -eq $cur)         { $marcas += 'atual' }
        [pscustomobject]@{
            N         = $i
            Descricao = if ($e) { $e.Desc } else { '(outro)' }
            Tipo      = if ($e -and $e.IsVHD) { 'VHDX' } else { 'Disco' }
            Caminho   = if ($e) { $e.Path } else { '' }
            Marcas    = $marcas -join ','
            GUID      = $g
        }
    }
}

function Backup-BCD {
    $dir = "$env:SystemDrive\bcd_backup"
    New-Item -ItemType Directory -Path $dir -Force | Out-Null
    $f = Join-Path $dir ("bcd_" + (Get-Date -Format 'yyyyMMdd_HHmmss'))
    bcdedit /export $f | Out-Null
    if ($LASTEXITCODE -eq 0) { Write-Host "Backup do BCD: $f" -ForegroundColor DarkGray }
    else { Write-Host "Aviso: falha ao gerar backup do BCD." -ForegroundColor Yellow }
}

# ---------------------------------------------------------------
# Acoes
# ---------------------------------------------------------------

function Show-BootEntries {
    $menu = @(Get-BootMenu)
    if ($menu.Count -eq 0) { Write-Host "Vazio." -ForegroundColor Yellow; return }
    $menu | Format-Table N, Descricao, Tipo, Caminho, Marcas, GUID -AutoSize

    # VHDX que existem no BCD mas estao fora do menu
    $ocultas = @(Get-BcdOsEntries | Where-Object { $_.IsVHD -and ($menu.GUID -notcontains $_.GUID) })
    if ($ocultas.Count -gt 0) {
        Write-Host "VHDX fora do menu de boot (nao aparecem na inicializacao):" -ForegroundColor Yellow
        $ocultas | Format-Table Desc, Path, GUID -AutoSize
    }
}

function Add-VHDXToDualBoot {
    param ([string]$VHDXPath, [string]$CustomDesc)

    $path = $VHDXPath.Replace('"', '').Trim()
    if (-not (Test-Path $path)) { Write-Host "arquivo nao encontrado" -ForegroundColor Red; return }
    $path = (Resolve-Path $path).Path

    if ($path -like '\\*') { Write-Host "Boot nativo exige disco local (caminho de rede nao suportado)." -ForegroundColor Red; return }
    if ([System.IO.Path]::GetExtension($path) -notmatch '^\.vhdx?$') { Write-Host "O arquivo precisa ser .vhd ou .vhdx." -ForegroundColor Red; return }

    if ([string]::IsNullOrWhiteSpace($CustomDesc)) {
        $CustomDesc = [System.IO.Path]::GetFileNameWithoutExtension($path)
    }

    $drive   = [System.IO.Path]::GetPathRoot($path).TrimEnd('\')
    $relPath = $path.Substring($drive.Length)
    if (-not $relPath.StartsWith("\")) { $relPath = "\" + $relPath }
    $vhdStr  = "vhd=[$drive]$relPath"

    Backup-BCD
    Write-Host "Adicionando entrada: $CustomDesc..." -ForegroundColor Cyan

    # So tenta {default} se a copia de {current} NAO criou entrada
    $copyOutput = (bcdedit /copy '{current}' /d $CustomDesc 2>&1) -join ' '
    if ($copyOutput -notmatch $RX_GUID) {
        $copyOutput = (bcdedit /copy '{default}' /d $CustomDesc 2>&1) -join ' '
    }
    if ($copyOutput -match $RX_GUID) { $guid = $matches[0] }
    else { Write-Host "Erro ao copiar entrada: $copyOutput" -ForegroundColor Red; return }

    bcdedit /set $guid device   $vhdStr | Out-Null
    bcdedit /set $guid osdevice $vhdStr | Out-Null

    # Confere relendo o BCD (nao depende de codigo de saida)
    $nova = Get-BcdOsEntries | Where-Object { $_.GUID -eq $guid.ToLower() }
    if ($nova -and $nova.Device -like 'vhd=*' -and $nova.OsDevice -like 'vhd=*') {
        Write-Host "Sucesso! Adicionado com GUID $guid (no fim do menu - use a opcao 6 para reordenar)" -ForegroundColor Green
    } else {
        Write-Host "Erro ao configurar o VHDX. Removendo entrada incompleta..." -ForegroundColor Red
        bcdedit /delete $guid /f | Out-Null
    }
}

function Remove-AllVHDXBootEntries {
    $cur    = Get-CurrentGuid
    $todas  = @(Get-BcdOsEntries | Where-Object { $_.IsVHD })
    $alvos  = @($todas | Where-Object { $_.GUID -ne $cur })

    if ($todas.Count -ne $alvos.Count) {
        Write-Host "A entrada em uso (VHDX atual) sera mantida." -ForegroundColor Yellow
    }
    if ($alvos.Count -eq 0) { Write-Host "Nenhuma entrada VHDX para remover." -ForegroundColor Yellow; return }

    Backup-BCD
    foreach ($a in $alvos) { bcdedit /delete $a.GUID /f | Out-Null }

    # Confere relendo o BCD (nao depende de codigo de saida)
    $restantes = @(Get-BcdOsEntries | ForEach-Object { $_.GUID })
    foreach ($a in $alvos) {
        if ($restantes -contains $a.GUID) { Write-Host "Falha ao remover: $($a.Desc) $($a.GUID)" -ForegroundColor Red }
        else { Write-Host "Removido: $($a.Desc) $($a.GUID)" -ForegroundColor Yellow }
    }
}

function Set-BootOrder {
    $menu = @(Get-BootMenu)
    if ($menu.Count -eq 0) { Write-Host "Menu de boot vazio." -ForegroundColor Yellow; return }
    $menu | Format-Table N, Descricao, Tipo, Marcas -AutoSize

    if ($menu.Count -ge 2) {
        Write-Host "Nova ordem: numeros separados por espaco (ex: 2 1 3)."
        Write-Host "Pode informar so os primeiros; os demais seguem na ordem atual. Enter = manter."
        $in = Read-Host "Nova ordem"

        if (-not [string]::IsNullOrWhiteSpace($in)) {
            $nums = @($in -split '[\s,;]+' | Where-Object { $_ })
            $invalido = $nums | Where-Object { $_ -notmatch '^\d+$' -or [int]$_ -lt 1 -or [int]$_ -gt $menu.Count }
            if ($invalido) { Write-Host "Numero invalido: $($invalido -join ' ')" -ForegroundColor Red; return }
            if (@($nums | Select-Object -Unique).Count -ne $nums.Count) { Write-Host "Numero repetido." -ForegroundColor Red; return }

            $escolhidos = @($nums | ForEach-Object { [int]$_ })
            $resto      = @(1..$menu.Count | Where-Object { $escolhidos -notcontains $_ })
            $guids      = @($escolhidos + $resto | ForEach-Object { $menu[$_ - 1].GUID })

            Backup-BCD
            bcdedit /displayorder @guids | Out-Null
            if ($LASTEXITCODE -ne 0) { Write-Host "Erro ao alterar a ordem." -ForegroundColor Red; return }
            Write-Host "Ordem alterada!" -ForegroundColor Green

            $menu = @(Get-BootMenu)
            $menu | Format-Table N, Descricao, Tipo, Marcas -AutoSize
        }
    }

    Write-Host "A entrada padrao e a que inicia sozinha quando o timeout acaba."
    $d = Read-Host "Numero da entrada padrao (Enter = manter)"
    if ([string]::IsNullOrWhiteSpace($d)) { return }
    if ($d -notmatch '^\d+$' -or [int]$d -lt 1 -or [int]$d -gt $menu.Count) { Write-Host "Invalido." -ForegroundColor Red; return }

    bcdedit /default $menu[[int]$d - 1].GUID | Out-Null
    if ($LASTEXITCODE -eq 0) { Write-Host "Padrao: $($menu[[int]$d - 1].Descricao)" -ForegroundColor Green }
    else { Write-Host "Erro ao definir padrao." -ForegroundColor Red }
}

function Set-BootTimeout {
    $atual = (Get-BootManagerInfo).Timeout
    Write-Host "`nTempo atual: $atual segundos." -ForegroundColor Cyan
    $newTimeout = Read-Host "Digite o novo tempo (segundos)"
    if ($newTimeout -match "^\d+$") {
        bcdedit /timeout $newTimeout | Out-Null
        if ($LASTEXITCODE -eq 0) { Write-Host "Tempo alterado!" -ForegroundColor Green }
        else { Write-Host "Erro ao alterar o tempo." -ForegroundColor Red }
    }
}

function Reset-EFIPartition {
    Write-Host "`n=== Refazer UEFI ===" -ForegroundColor Cyan

    $letter = Read-Host "Letra da particao UEFI"
    if ([string]::IsNullOrWhiteSpace($letter)) { Write-Host "Invalido." -ForegroundColor Red; return }
    $letter = $letter.TrimEnd(':').ToUpper()

    if (-not (Test-Path "${letter}:\")) { Write-Host "Particao ${letter}: nao acessivel." -ForegroundColor Red; return }

    cmd /c "bcdboot C:\Windows /s ${letter}: /f UEFI /p"
}

# --- Menu ---
do {
    Write-Host "`n=== GERENCIADOR BOOT VHDX (v13.0) ===" -ForegroundColor Magenta
    Write-Host "1. Listar Entradas"
    Write-Host "2. Adicionar VHDX (Com Descricao)"
    Write-Host "3. Limpar Tudo (VHDX)"
    Write-Host "4. Ajustar Tempo (Timeout)"
    Write-Host "5. Refazer UEFI"
    Write-Host "6. Alterar Ordem / Padrao"
    Write-Host "7. Sair"
    Write-Host "obs: para deletar na mao cdm adm:"
    Write-Host "     bcdedit /delete {55281582-b647-11ed-b9e4-9b5ba3d8e273}"

    $op = Read-Host "Opcao"
    switch ($op) {
        "1" { Show-BootEntries }
        "2" {
            $p = Read-Host "Caminho do .vhdx"
            $d = Read-Host "Descricao"
            Add-VHDXToDualBoot -VHDXPath $p -CustomDesc $d
        }
        "3" { Remove-AllVHDXBootEntries }
        "4" { Set-BootTimeout }
        "5" { Reset-EFIPartition }
        "6" { Set-BootOrder }
    }
} while ($op -ne "7")