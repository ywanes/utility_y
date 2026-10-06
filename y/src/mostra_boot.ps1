# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/mostra_boot.ps1 | iex
#
<#
    Analisa o boot REAL (particao EFI / Sistema em disco fisico, ignorando VHDX).
    Responde se a particao pequena (EFI) eh INDEPENDENTE: se o sistema padrao
    boota sem depender de nenhuma particao GRANDE do mesmo tipo.
    Somente leitura - nao altera nada. Rode como administrador.
#>
#Requires -RunAsAdministrator
$ErrorActionPreference = 'Stop'

# Discos virtuais (VHD/VHDX) = BusType "File Backed Virtual" -> ignorar
$discosVirtuais = (Get-Disk | Where-Object { $_.BusType -eq 'File Backed Virtual' }).Number

# Particoes de boot em disco fisico (EFI ou flag System)
$bootParts = Get-Partition | Where-Object {
    (
        $_.GptType -eq '{c12a7328-f81f-11d2-ba4b-00a0c93ec93b}' -or
        $_.IsSystem -eq $true
    ) -and ($discosVirtuais -notcontains $_.DiskNumber)
}

if (-not $bootParts) {
    Write-Host "particao pequena independente        : N/A (sem EFI em disco fisico)" -ForegroundColor Yellow
    return
}

foreach ($p in $bootParts) {
    $sizeMB    = [math]::Round($p.Size/1MB)
    $ehPequena = $sizeMB -le 1024

    # Letra desta particao (normalmente a EFI nao tem)
    $letraEfi = if ($p.DriveLetter) { "$($p.DriveLetter):" } else { $null }

    # Letras no mesmo disco fisico (fora a propria)
    $letras = (Get-Partition -DiskNumber $p.DiskNumber |
               Where-Object { $_.DriveLetter -and $_.PartitionNumber -ne $p.PartitionNumber } |
               ForEach-Object { $_.DriveLetter }) -join ", "
    if (-not $letras) { $letras = "(nenhuma)" }

    # --- A particao pequena eh independente? ---
    if (-not $ehPequena) {
        # Nao ha particao pequena separada: boot junto do Windows
        $independente = "N/A"
    } else {
        # Ver de que o SISTEMA PADRAO (default) depende.
        # Pega o osdevice da entrada {default}.
        $defDev = (bcdedit /enum '{default}' 2>$null |
                   Select-String -Pattern '^\s*osdevice\s+(.+)$').Matches.Groups[1].Value
        if (-not $defDev) { $defDev = "" }
        $defDev = $defDev.Trim()

        # Independente = SIM se o sistema padrao NAO roda de uma particao
        # grande com letra (partition=X:). Se roda de vhd=... (outro disco)
        # ou de ramdisk, a EFI nao depende das particoes grandes locais.
        if ($defDev -match 'partition=([A-Za-z]):') {
            $letraDefault = "$($matches[1]):"
            # Se o default roda de uma particao grande (com letra), a EFI
            # depende dela -> NAO independente.
            $independente = "NAO"
            $motivo = "sistema padrao roda de $letraDefault"
        } else {
            $independente = "SIM"
            $motivo = "sistema padrao roda de VHDX/outro disco; EFI se basta"
        }
    }

    Write-Host ("particao pequena independente        : {0}" -f $independente)
    if ($independente -eq "NAO" -and $motivo) {
        Write-Host ("   -> {0}" -f $motivo) -ForegroundColor Yellow
    }
    Write-Host ("quais letras estao junto nesse disco : {0}" -f $letras)
}
