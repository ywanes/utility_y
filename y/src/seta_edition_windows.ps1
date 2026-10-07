# powershell adm
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/seta_edition_windows.ps1))) -List
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/seta_edition_windows.ps1))) -Set Professional
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/seta_edition_windows.ps1))) -Set Professional -Restart
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/seta_edition_windows.ps1))) -Set ProfessionalWorkstation
# & ([scriptblock]::Create((irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/seta_edition_windows.ps1))) -Set ProfessionalWorkstation -Restart


<#
.SYNOPSIS
  Lista ou troca a edicao do Windows (ex: Pro -> Pro for Workstations).

.DESCRIPTION
  -List (padrao) : mostra a edicao atual e as edicoes de destino suportadas
                   pelo sistema (DISM /Get-TargetEditions).
  -Set <Edicao>  : troca a edicao via changepk.exe usando a chave generica KMS
                   (GVLK) publicada pela Microsoft. A CHAVE NAO ATIVA O WINDOWS!!,
                   para ativar windows e office é outro lugar:
                        irm https://get.activated.win | iex
                   so define a edicao. Depois, insira sua licenca real.
  -Keys          : mostra a tabela edicao -> chave generica.

  Mensagens sem acento de proposito: o Windows PowerShell 5.1 le .ps1 sem BOM
  como ANSI e acentos viram lixo.

.EXAMPLE
  .\windows-edition.ps1
  .\windows-edition.ps1 -Keys
  .\windows-edition.ps1 -Set ProfessionalWorkstation
  .\windows-edition.ps1 -Set ProfessionalWorkstation -Restart

.EXAMPLE
  No SetupComplete.cmd (NAO use -Restart ali):
  powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0windows-edition.ps1" -Set ProfessionalWorkstation -Log "%LOG%"
#>
[CmdletBinding(DefaultParameterSetName = 'List')]
param(
    [Parameter(ParameterSetName = 'List')]
    [switch]$List,

    [Parameter(ParameterSetName = 'Set', Mandatory = $true)]
    [string]$Set,

    # Pula a checagem contra DISM /Get-TargetEditions.
    [Parameter(ParameterSetName = 'Set')]
    [switch]$Force,

    # Reinicia ao final para concluir a troca.
    [Parameter(ParameterSetName = 'Set')]
    [switch]$Restart,

    [Parameter(ParameterSetName = 'Keys')]
    [switch]$Keys,

    # Arquivo de log opcional (append).
    [string]$Log
)

$ErrorActionPreference = 'Stop'

# Chaves genericas KMS (GVLK) publicas da Microsoft.
# Fonte: learn.microsoft.com -> "KMS client activation and product keys".
$EditionKeys = [ordered]@{
    'Professional'             = 'W269N-WFGWX-YVC9B-4J6C9-T83GX'
    'ProfessionalN'            = 'MH37W-N47XK-V7XM9-C7227-GCQG9'
    'ProfessionalWorkstation'  = 'NRG8B-VKK3Q-CXVCJ-9G2XF-6Q84J'
    'ProfessionalWorkstationN' = '9FNHH-K3HBT-3W4TD-6383H-6XYWF'
    'ProfessionalEducation'    = '6TP4R-GNPTD-KYYHQ-7B7DP-J447Y'
    'ProfessionalEducationN'   = 'YVWGF-BXNMC-HTQYQ-CPQ99-66QFC'
    'Education'                = 'NW6C2-QMPVW-D7KKK-3GKT6-VCFB2'
    'EducationN'               = '2WH4N-8QGBV-H22JP-CT43Q-MDWWJ'
    'Enterprise'               = 'NPPR9-FWDCX-D2C8J-H872K-2YT43'
    'EnterpriseN'              = 'DPH2V-TTNVB-4X9Q3-TJR4H-KHJW4'
}

$FriendlyNames = @{
    'Core'                     = 'Home'
    'CoreN'                    = 'Home N'
    'CoreSingleLanguage'       = 'Home Single Language'
    'CoreCountrySpecific'      = 'Home China'
    'Professional'             = 'Pro'
    'ProfessionalN'            = 'Pro N'
    'ProfessionalWorkstation'  = 'Pro for Workstations'
    'ProfessionalWorkstationN' = 'Pro N for Workstations'
    'ProfessionalEducation'    = 'Pro Education'
    'ProfessionalEducationN'   = 'Pro Education N'
    'Education'                = 'Education'
    'EducationN'               = 'Education N'
    'Enterprise'               = 'Enterprise'
    'EnterpriseN'              = 'Enterprise N'
}

function Write-Log([string]$Message) {
    Write-Host $Message
    if ($Log) {
        $line = '[{0}] {1}' -f (Get-Date -Format 'yyyy-MM-dd HH:mm:ss'), $Message
        try { Add-Content -Path $Log -Value $line -Encoding UTF8 } catch { }
    }
}

function Get-Friendly([string]$Id) {
    if ($FriendlyNames.ContainsKey($Id)) { return $FriendlyNames[$Id] }
    return $Id
}

function Test-Admin {
    $id = [Security.Principal.WindowsIdentity]::GetCurrent()
    ([Security.Principal.WindowsPrincipal]$id).IsInRole(
        [Security.Principal.WindowsBuiltInRole]::Administrator)
}

function Get-CurrentEdition {
    (Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows NT\CurrentVersion').EditionID
}

function Get-TargetEditions {
    # /English garante saida em ingles mesmo em Windows pt-BR (parse estavel).
    $out = & dism.exe /English /Online /Get-TargetEditions 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "DISM falhou (codigo $LASTEXITCODE): $(($out | Out-String).Trim())"
    }
    foreach ($l in $out) {
        if ($l -match '^\s*Target Edition\s*:\s*(\S+)') { $Matches[1] }
    }
}

function Get-ChangePkPath {
    # PowerShell 32-bit em Windows 64-bit precisa do Sysnative.
    $sys = 'System32'
    if ([Environment]::Is64BitOperatingSystem -and -not [Environment]::Is64BitProcess) {
        $sys = 'Sysnative'
    }
    Join-Path $env:SystemRoot "$sys\changepk.exe"
}

function Show-Keys {
    Write-Host ''
    Write-Host 'Edicao (EditionID)          Nome                      Chave generica (GVLK)'
    Write-Host '--------------------------  ------------------------  -----------------------------'
    foreach ($k in $EditionKeys.Keys) {
        Write-Host ('{0,-26}  {1,-24}  {2}' -f $k, (Get-Friendly $k), $EditionKeys[$k])
    }
    Write-Host ''
    Write-Host 'Obs: GVLK so define a edicao; ativacao exige licenca real (ou KMS).'
}

# ------------------------------------------------------------------ main ----
try {
    if ($PSCmdlet.ParameterSetName -eq 'Keys') {
        Show-Keys
        exit 0
    }

    if (-not (Test-Admin)) {
        Write-Log 'ERRO: rode como Administrador (DISM e changepk exigem).'
        exit 2
    }

    $current = Get-CurrentEdition

    if ($PSCmdlet.ParameterSetName -eq 'List') {
        Write-Host ''
        Write-Host ('Edicao atual: {0} ({1})' -f $current, (Get-Friendly $current))
        Write-Host ''
        $targets = @(Get-TargetEditions)
        if ($targets.Count -eq 0) {
            Write-Host 'Nenhuma edicao de destino disponivel a partir desta.'
        } else {
            Write-Host 'Edicoes de destino suportadas:'
            foreach ($t in $targets) {
                $mark = if ($EditionKeys.Contains($t)) { 'pode usar -Set' } else { 'sem chave no script' }
                Write-Host ('  {0,-26} {1,-24} [{2}]' -f $t, (Get-Friendly $t), $mark)
            }
        }
        Write-Host ''
        Write-Host 'Para trocar: .\windows-edition.ps1 -Set <EditionID>'
        exit 0
    }

    # ---- Set ----
    $target = $EditionKeys.Keys | Where-Object { $_ -eq $Set } | Select-Object -First 1
    if (-not $target) {
        Write-Log "ERRO: edicao '$Set' desconhecida. Validas:"
        foreach ($k in $EditionKeys.Keys) { Write-Log "  $k" }
        exit 1
    }

    Write-Log ('Edicao atual: {0} ({1})' -f $current, (Get-Friendly $current))
    Write-Log ('Destino:      {0} ({1})' -f $target, (Get-Friendly $target))

    if ($current -eq $target) {
        Write-Log 'Ja esta nesta edicao. Nada a fazer.'
        exit 0
    }

    if (-not $Force) {
        $targets = @(Get-TargetEditions)
        if ($targets -notcontains $target) {
            Write-Log "ERRO: '$target' nao e destino suportado a partir de '$current'."
            Write-Log ("Destinos validos: {0}" -f ($(if ($targets) { $targets -join ', ' } else { 'nenhum' })))
            Write-Log 'Use -Force para tentar mesmo assim.'
            exit 1
        }
    }

    $changepk = Get-ChangePkPath
    if (-not (Test-Path $changepk)) {
        Write-Log "ERRO: changepk.exe nao encontrado em $changepk"
        exit 1
    }

    Write-Log "Executando changepk.exe com a chave generica de $target..."
    $p = Start-Process -FilePath $changepk `
                       -ArgumentList '/ProductKey', $EditionKeys[$target] `
                       -Wait -PassThru -NoNewWindow
    Write-Log "changepk.exe terminou com codigo $($p.ExitCode)."

    if ($p.ExitCode -ne 0) {
        Write-Log 'ERRO: a troca falhou. Veja o Visualizador de Eventos (Application / Security-SPP).'
        exit 1
    }

    Write-Log 'Troca agendada. Reinicie para concluir; depois insira sua licenca real.'
    if ($Restart) {
        Write-Log 'Reiniciando em 10 segundos...'
        Start-Sleep -Seconds 10
        Restart-Computer -Force
    }
    exit 0
}
catch {
    Write-Log "ERRO: $($_.Exception.Message)"
    exit 1
}