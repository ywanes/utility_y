# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/wmic2.ps1 | iex
#

$__oldEAP = $ErrorActionPreference
$ErrorActionPreference = 'Stop'

function Ok($m)   { Write-Host "[OK]    $m" -ForegroundColor Green }
function Info($m) { Write-Host "[..]    $m" -ForegroundColor Cyan }
function Warn($m) { Write-Host "[AVISO] $m" -ForegroundColor Yellow }
function Fail($m) { throw $m }   # capturado no fim; NAO fecha a janela

# ================= arquivos do shim (gravados em System32\Wbem) =================
$SHIM_CMD = @'
@echo off
"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -NoLogo -NonInteractive -ExecutionPolicy Bypass -File "%~dp0wmic-shim.ps1" %*
'@

$SHIM_PS1 = @'
# wmic-shim.ps1 - emula o wmic.exe (removido pela Microsoft; sem FoD desde ago/2026) via CIM.
# Instalado por wmic.ps1 (utility_y). Chamado por wmic.cmd. Requer Windows PowerShell 5.1.
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Management

$ALIASES = @{
  'baseboard'='Win32_BaseBoard'; 'bios'='Win32_BIOS'; 'bootconfig'='Win32_BootConfiguration'
  'cdrom'='Win32_CDROMDrive'; 'computersystem'='Win32_ComputerSystem'; 'cpu'='Win32_Processor'
  'csproduct'='Win32_ComputerSystemProduct'; 'datafile'='CIM_DataFile'; 'dcomapp'='Win32_DCOMApplication'
  'desktop'='Win32_Desktop'; 'desktopmonitor'='Win32_DesktopMonitor'; 'devicememoryaddress'='Win32_DeviceMemoryAddress'
  'diskdrive'='Win32_DiskDrive'; 'diskquota'='Win32_DiskQuota'; 'dmachannel'='Win32_DMAChannel'
  'environment'='Win32_Environment'; 'fsdir'='Win32_Directory'; 'group'='Win32_Group'
  'idecontroller'='Win32_IDEController'; 'irq'='Win32_IRQResource'; 'job'='Win32_ScheduledJob'
  'loadorder'='Win32_LoadOrderGroup'; 'logicaldisk'='Win32_LogicalDisk'; 'logon'='Win32_LogonSession'
  'memcache'='Win32_CacheMemory'; 'memorychip'='Win32_PhysicalMemory'; 'memphysical'='Win32_PhysicalMemoryArray'
  'netclient'='Win32_NetworkClient'; 'netlogin'='Win32_NetworkLoginProfile'; 'netprotocol'='Win32_NetworkProtocol'
  'netuse'='Win32_NetworkConnection'; 'nic'='Win32_NetworkAdapter'; 'nicconfig'='Win32_NetworkAdapterConfiguration'
  'ntdomain'='Win32_NTDomain'; 'ntevent'='Win32_NTLogEvent'; 'nteventlog'='Win32_NTEventlogFile'
  'onboarddevice'='Win32_OnBoardDevice'; 'os'='Win32_OperatingSystem'; 'pagefile'='Win32_PageFileUsage'
  'pagefileset'='Win32_PageFileSetting'; 'partition'='Win32_DiskPartition'; 'port'='Win32_PortResource'
  'portconnector'='Win32_PortConnector'; 'printer'='Win32_Printer'; 'printerconfig'='Win32_PrinterConfiguration'
  'printjob'='Win32_PrintJob'; 'process'='Win32_Process'; 'product'='Win32_Product'
  'qfe'='Win32_QuickFixEngineering'; 'quotasetting'='Win32_QuotaSetting'; 'recoveros'='Win32_OSRecoveryConfiguration'
  'registry'='Win32_Registry'; 'scsicontroller'='Win32_SCSIController'; 'server'='Win32_PerfRawData_PerfNet_Server'
  'service'='Win32_Service'; 'shadowcopy'='Win32_ShadowCopy'; 'shadowstorage'='Win32_ShadowStorage'
  'share'='Win32_Share'; 'softwareelement'='Win32_SoftwareElement'; 'softwarefeature'='Win32_SoftwareFeature'
  'sounddev'='Win32_SoundDevice'; 'startup'='Win32_StartupCommand'; 'sysaccount'='Win32_SystemAccount'
  'sysdriver'='Win32_SystemDriver'; 'systemenclosure'='Win32_SystemEnclosure'; 'systemslot'='Win32_SystemSlot'
  'tapedrive'='Win32_TapeDrive'; 'temperature'='root/wmi|MSAcpi_ThermalZoneTemperature'; 'timezone'='Win32_TimeZone'
  'ups'='Win32_UninterruptiblePowerSupply'; 'usbcontroller'='Win32_USBController'; 'useraccount'='Win32_UserAccount'
  'videocontroller'='Win32_VideoController'; 'voltage'='Win32_VoltageProbe'; 'volume'='Win32_Volume'
  'volumequotasetting'='Win32_VolumeQuotaSetting'; 'volumeuserquota'='Win32_VolumeUserQuota'; 'wmiset'='Win32_WMISetting'
}
# colunas do "list brief" (chave em minusculo)
$BRIEF = @{
  'win32_operatingsystem'=@('BuildNumber','Organization','RegisteredUser','SerialNumber','SystemDirectory','Version')
  'win32_process'=@('HandleCount','Name','Priority','ProcessId','ThreadCount','WorkingSetSize')
  'win32_service'=@('ExitCode','Name','ProcessId','StartMode','State','Status')
  'win32_processor'=@('AddressWidth','DeviceID','Manufacturer','MaxClockSpeed','Name','SocketDesignation')
  'win32_logicaldisk'=@('DeviceID','DriveType','FreeSpace','ProviderName','Size','VolumeName')
  'win32_diskdrive'=@('Caption','DeviceID','Model','Partitions','Size')
  'win32_computersystem'=@('Domain','Manufacturer','Model','Name','PrimaryOwnerName','TotalPhysicalMemory')
  'win32_bios'=@('Manufacturer','Name','SerialNumber','SMBIOSBIOSVersion','Version')
  'win32_computersystemproduct'=@('IdentifyingNumber','Name','Vendor','Version','Caption')
  'win32_physicalmemory'=@('Capacity','DeviceLocator','MemoryType','Name','Tag','TotalWidth')
  'win32_networkadapter'=@('AdapterType','DeviceID','MACAddress','Name','NetworkAddresses','Speed')
  'win32_networkadapterconfiguration'=@('DHCPEnabled','IPAddress','MACAddress','ServiceName')
  'win32_useraccount'=@('AccountType','Caption','Domain','SID','FullName','Name')
  'win32_baseboard'=@('Manufacturer','Model','Name','PartNumber','Product','SerialNumber','SKU','Version')
  'win32_quickfixengineering'=@('Caption','CSName','Description','FixComments','HotFixID','InstallDate','InstalledBy','InstalledOn','Name','ServicePackInEffect','Status')
  'win32_startupcommand'=@('Caption','Command','Location','Name','User')
  'win32_share'=@('Caption','Name','Path','Status')
  'win32_product'=@('IdentifyingNumber','Name','Vendor','Version','Caption')
  'win32_videocontroller'=@('AdapterRAM','DriverVersion','Name','VideoProcessor')
  'win32_group'=@('Caption','Domain','Name','SID')
  'win32_environment'=@('Name','UserName','VariableValue')
}
$VERBS = @('get','list','call','delete','set','create','assoc')
$OUT = New-Object System.Collections.Generic.List[string]
function Emit { param([AllowEmptyString()][string]$s) $OUT.Add($s) }

function Show-Help {
  Emit 'wmic (shim CIM/PowerShell - o wmic.exe original foi removido pela Microsoft)'
  Emit ''
  Emit 'Uso:'
  Emit '  wmic <alias | path Classe> [where "cond"] get p1,p2[,...] [/value | /format:list|csv|table]'
  Emit '  wmic <alias | path Classe> [where "cond"] list [brief|full]'
  Emit '  wmic <alias | path Classe> [where "cond"] call Metodo [arg1,arg2,...]'
  Emit '  wmic <alias | path Classe>  where "cond"  delete'
  Emit '  wmic <alias | path Classe>  where "cond"  set Prop=Valor[,Prop2=Valor2]'
  Emit 'Globais: /node:pc1[,pc2] /user:usuario /password:senha /namespace:\\root\cimv2 /output:arq /append:arq'
  Emit ('Aliases: ' + (($ALIASES.Keys | Sort-Object) -join ', '))
  Emit ''
  Emit 'Exemplos:'
  Emit '  wmic os get caption,version /value'
  Emit '  wmic process where "name=''notepad.exe''" get processid,commandline'
  Emit '  wmic process where processid=1234 delete'
  Emit '  wmic process call create "notepad.exe"'
  Emit ''
  Emit 'Equivalente nativo: Get-CimInstance Win32_Process | Select-Object Name'
}

function Has-Qual($obj, [string]$name) {
  foreach ($q in $obj.Qualifiers) { if ($q.Name -ieq $name) { return $true } }
  return $false
}

function Fmt($v, [bool]$csv) {
  if ($null -eq $v) { return '' }
  if ($v -is [datetime]) { return [System.Management.ManagementDateTimeConverter]::ToDmtfDateTime($v) }
  if ($v -is [bool]) { if ($v) { return 'TRUE' } else { return 'FALSE' } }
  if ($v -is [array]) {
    if ($csv) { return '{' + (@($v | ForEach-Object { Fmt $_ $true }) -join ';') + '}' }
    $items = @($v | ForEach-Object { if ($_ -is [string]) { '"' + $_ + '"' } else { Fmt $_ $false } })
    return '{' + ($items -join ', ') + '}'
  }
  return [string]$v
}

function Convert-CimValue([string]$s, $type) {
  $t = [string]$type
  if ($t -like '*Array') { return @($s -split ';') }
  switch -regex ($t) {
    '^UInt64'  { return [uint64]$s }
    '^UInt32'  { return [uint32]$s }
    '^UInt16'  { return [uint16]$s }
    '^UInt8'   { return [byte]$s }
    '^SInt64'  { return [int64]$s }
    '^SInt32'  { return [int32]$s }
    '^SInt16'  { return [int16]$s }
    '^SInt8'   { return [sbyte]$s }
    '^Boolean' { return [bool]($s -match '^(1|true|yes|sim)$') }
    '^DateTime'{ return [System.Management.ManagementDateTimeConverter]::ToDateTime($s) }
    default    { return $s }
  }
}

function Emit-Table($insts, [string[]]$props, [bool]$csv) {
  if ($csv) {
    Emit ''
    Emit ((@('Node') + $props) -join ',')
    foreach ($i in $insts) {
      $node = $i.PSComputerName; if (-not $node) { $node = $env:COMPUTERNAME }
      $vals = @($node) + @($props | ForEach-Object { Fmt $i.$_ $true })
      Emit ($vals -join ',')
    }
    return
  }
  $rows = New-Object System.Collections.Generic.List[object]
  foreach ($i in $insts) {
    $r = New-Object string[] $props.Count
    for ($k = 0; $k -lt $props.Count; $k++) { $r[$k] = Fmt $i.($props[$k]) $false }
    $rows.Add($r)
  }
  $w = New-Object int[] $props.Count
  for ($k = 0; $k -lt $props.Count; $k++) {
    $w[$k] = $props[$k].Length
    foreach ($r in $rows) { if ($r[$k].Length -gt $w[$k]) { $w[$k] = $r[$k].Length } }
    $w[$k] += 2
  }
  $line = ''; for ($k = 0; $k -lt $props.Count; $k++) { $line += $props[$k].PadRight($w[$k]) }; Emit $line
  foreach ($r in $rows) { $line = ''; for ($k = 0; $k -lt $props.Count; $k++) { $line += $r[$k].PadRight($w[$k]) }; Emit $line }
  Emit ''
}

function Emit-Values($insts, [string[]]$props) {
  foreach ($i in $insts) {
    Emit ''
    foreach ($p in $props) { Emit ('{0}={1}' -f $p, (Fmt $i.$p $false)) }
    Emit ''
  }
}

function Split-Args([string[]]$toks) {
  # "a,b" -> a,b ; "1, 2" -> 1,2 ; "cmd /c x y" (um token com espaco) -> 1 arg
  if ($toks.Count -eq 0) { return @() }
  if ($toks.Count -eq 1 -and $toks[0] -match '\s') { $parts = @($toks[0]) }
  else { $parts = @(($toks -join ' ') -split ',') }
  return @($parts | ForEach-Object { $_.Trim().Trim('"', "'") } | Where-Object { $_ -ne '' })
}

$sess = $null
try {
  # ---- switches globais (/x ou /x:valor), em qualquer posicao ----
  $opt = @{ node = @(); user = $null; password = $null; namespace = 'root/cimv2'; format = $null; output = $null; append = $null; help = $false }
  $rest = New-Object System.Collections.Generic.List[string]
  foreach ($t in @($args)) {
    if ($t -match '^/([A-Za-z?]+)(?::(.*))?$') {
      $k = $Matches[1].ToLower(); $v = $Matches[2]
      switch ($k) {
        'node'      { $opt.node = @(($v -split ',') | ForEach-Object { $_.Trim().Trim('"', "'") } | Where-Object { $_ }) }
        'user'      { $opt.user = $v }
        'password'  { $opt.password = $v }
        'namespace' { $opt.namespace = ($v -replace '^\\\\', '') }
        'format'    { $opt.format = (([string]$v).ToLower() -replace '\..*$', '').Trim('"', "'") }
        'value'     { $opt.format = 'value' }
        'output'    { $opt.output = $v }
        'append'    { $opt.append = $v }
        '?'         { $opt.help = $true }
        default     { }   # /interactive /failfast /every /repeat /trace /privileges /locale ... ignorados
      }
    } else { $rest.Add($t) }
  }
  if ($opt.help -or $rest.Count -eq 0) { Show-Help; foreach ($l in $OUT) { [Console]::Out.WriteLine($l) }; exit 0 }

  # ---- alias / classe ----
  $i = 0
  $alias = $rest[$i]; $i++
  $ns = $opt.namespace
  if ($alias -ieq 'path' -or $alias -ieq 'class') {
    if ($i -ge $rest.Count) { throw 'Informe a classe apos "path".' }
    $class = $rest[$i]; $i++
  } else {
    $map = $ALIASES[$alias.ToLower()]
    if (-not $map) { throw ("Alias '{0}' nao reconhecido. Use: wmic path Win32_Classe ..." -f $alias) }
    if ($map -like '*|*') { $ns, $class = $map -split '\|', 2 } else { $class = $map }
  }

  # ---- where / verbo ----
  $where = $null; $verb = 'list'; $verbArgs = @()
  while ($i -lt $rest.Count) {
    $t = $rest[$i].ToLower()
    if ($t -eq 'where') {
      $i++; $parts = @()
      while ($i -lt $rest.Count -and $VERBS -notcontains $rest[$i].ToLower()) { $parts += $rest[$i]; $i++ }
      $where = (($parts -join ' ').Trim())
      if ($where -match '^\((.*)\)$') { $where = $Matches[1].Trim() }
      if (-not $where) { throw 'Clausula WHERE vazia.' }
    } elseif ($VERBS -contains $t) {
      $verb = $t; $i++
      while ($i -lt $rest.Count) { $verbArgs += $rest[$i]; $i++ }
    } else { $i++ }
  }

  # ---- sessao CIM ----
  if ($opt.node.Count -gt 0) {
    $sp = @{ ComputerName = $opt.node }
    if ($opt.user) {
      if (-not $opt.password) { throw 'Com /user informe tambem /password (modo nao interativo).' }
      $sp.Credential = New-Object System.Management.Automation.PSCredential($opt.user, (ConvertTo-SecureString $opt.password -AsPlainText -Force))
    }
    $sess = New-CimSession @sp
  }
  $cim = @{ Namespace = $ns }; if ($sess) { $cim.CimSession = $sess }

  $cls = Get-CimClass -ClassName $class @cim
  $class = $cls.CimClassName
  $allProps = @($cls.CimClassProperties | ForEach-Object { $_.Name })
  $q = @{ ClassName = $class } + $cim; if ($where) { $q.Filter = $where }
  $node = $env:COMPUTERNAME; if ($opt.node.Count -gt 0) { $node = $opt.node[0] }

  $fmt = $opt.format
  switch ($verb) {
    'get' {
      $want = @()
      if ($verbArgs.Count -gt 0) { $want = @((($verbArgs -join ' ') -split ',') | ForEach-Object { $_.Trim() } | Where-Object { $_ }) }
      if ($want.Count -eq 0 -or $want -contains '*') { $props = $allProps }
      else {
        $props = @(foreach ($p in $want) {
          $m = $allProps | Where-Object { $_ -ieq $p } | Select-Object -First 1
          if (-not $m) { throw ("Invalid GET Expression: '{0}' nao e propriedade de {1}" -f $p, $class) }
          $m
        })
      }
      $insts = @(Get-CimInstance @q)
      if ($insts.Count -eq 0) { Emit 'No Instance(s) Available.' }
      elseif ($fmt -in 'value', 'list', 'textvaluelist') { Emit-Values $insts $props }
      elseif ($fmt -eq 'csv') { Emit-Table $insts $props $true }
      else { Emit-Table $insts $props $false }
    }
    'list' {
      $mode = 'full'; if ($verbArgs.Count -gt 0) { $mode = $verbArgs[0].ToLower() }
      $insts = @(Get-CimInstance @q)
      if ($insts.Count -eq 0) { Emit 'No Instance(s) Available.' }
      elseif ($mode -eq 'brief') {
        $props = $BRIEF[$class.ToLower()]; if (-not $props) { $props = $allProps }
        $props = @($props | Where-Object { $allProps -contains $_ })
        if ($fmt -eq 'csv') { Emit-Table $insts $props $true } elseif ($fmt -in 'value', 'list') { Emit-Values $insts $props } else { Emit-Table $insts $props $false }
      } else {
        if ($fmt -eq 'csv') { Emit-Table $insts $allProps $true } elseif ($fmt -eq 'table') { Emit-Table $insts $allProps $false } else { Emit-Values $insts $allProps }
      }
    }
    'call' {
      if ($verbArgs.Count -eq 0) { throw 'Informe o metodo: call Metodo [args]' }
      $mname = $verbArgs[0]
      $margs = @(Split-Args @($verbArgs | Select-Object -Skip 1))
      $meth = $cls.CimClassMethods | Where-Object { $_.Name -ieq $mname } | Select-Object -First 1
      if (-not $meth) { throw ("Metodo '{0}' nao existe em {1}" -f $mname, $class) }
      $inParams = @($meth.Parameters | Where-Object { Has-Qual $_ 'In' })
      $h = @{}
      for ($k = 0; $k -lt $margs.Count; $k++) {
        if ($k -ge $inParams.Count) { throw ("Metodo {0} aceita {1} argumento(s)." -f $meth.Name, $inParams.Count) }
        $h[$inParams[$k].Name] = Convert-CimValue $margs[$k] $inParams[$k].CimType
      }
      $results = @()
      if (Has-Qual $meth 'Static') {
        Emit ('Executing ({0})->{1}()' -f $class, $meth.Name)
        $results += ,(Invoke-CimMethod -ClassName $class -MethodName $meth.Name -Arguments $h @cim)
      } else {
        $insts = @(Get-CimInstance @q)
        if ($insts.Count -eq 0) { Emit 'No Instance(s) Available.' }
        foreach ($inst in $insts) {
          Emit ('Executing (\\{0}\{1}:{2})->{3}()' -f $node, $ns, $class, $meth.Name)
          $results += ,(Invoke-CimMethod -InputObject $inst -MethodName $meth.Name -Arguments $h)
        }
      }
      foreach ($r in $results) {
        Emit 'Method execution successful.'
        Emit 'Out Parameters:'
        Emit 'instance of __PARAMETERS'
        Emit '{'
        foreach ($p in $r.PSObject.Properties) {
          if ($p.Name -in 'PSComputerName', 'PSShowComputerName') { continue }
          Emit ("`t{0} = {1};" -f $p.Name, (Fmt $p.Value $false))
        }
        Emit '};'
        Emit ''
      }
    }
    'delete' {
      $insts = @(Get-CimInstance @q)
      if ($insts.Count -eq 0) { Emit 'No Instance(s) Available.' }
      $keys = @($cls.CimClassProperties | Where-Object { Has-Qual $_ 'key' } | ForEach-Object { $_.Name })
      foreach ($inst in $insts) {
        $kd = @($keys | ForEach-Object { '{0}="{1}"' -f $_, $inst.$_ }) -join ','
        Emit ('Deleting instance \\{0}\{1}:{2}.{3}' -f $node, $ns, $class, $kd)
        Remove-CimInstance -InputObject $inst
        Emit 'Instance deletion successful.'
      }
    }
    'set' {
      if ($verbArgs.Count -eq 0) { throw 'Informe: set Prop=Valor[,Prop2=Valor2]' }
      $h = @{}
      foreach ($kv in @((($verbArgs -join ' ') -split ',') | ForEach-Object { $_.Trim() } | Where-Object { $_ })) {
        $kk, $vv = $kv -split '=', 2
        $pm = $cls.CimClassProperties | Where-Object { $_.Name -ieq $kk.Trim() } | Select-Object -First 1
        if (-not $pm) { throw ("Propriedade '{0}' nao existe em {1}" -f $kk, $class) }
        $h[$pm.Name] = Convert-CimValue ([string]$vv).Trim().Trim('"', "'") $pm.CimType
      }
      $insts = @(Get-CimInstance @q)
      if ($insts.Count -eq 0) { Emit 'No Instance(s) Available.' }
      foreach ($inst in $insts) {
        Emit ('Updating property(s) of \\{0}\{1}:{2}' -f $node, $ns, $class)
        Set-CimInstance -InputObject $inst -Property $h
        Emit 'Property(s) update successful.'
      }
    }
    default { throw ("Verbo '{0}' nao suportado pelo shim (use PowerShell/CIM)." -f $verb) }
  }

  # ---- saida ----
  if ($opt.output -or $opt.append) {
    $text = ($OUT -join "`r`n") + "`r`n"
    if ($opt.append) { [IO.File]::AppendAllText($opt.append, $text, [Text.Encoding]::Unicode) }
    else { [IO.File]::WriteAllText($opt.output, $text, [Text.Encoding]::Unicode) }
  } else {
    foreach ($l in $OUT) { [Console]::Out.WriteLine($l) }
  }
  exit 0
} catch {
  foreach ($l in $OUT) { [Console]::Out.WriteLine($l) }
  [Console]::Out.WriteLine('ERROR:')
  [Console]::Out.WriteLine('Description = ' + $_.Exception.Message)
  exit 1
} finally {
  if ($sess) { Remove-CimSession $sess -ErrorAction SilentlyContinue }
}
'@
# ===============================================================================

function Write-Shim([string]$dir) {
    $cmd = ($SHIM_CMD -replace "`r?`n", "`r`n").TrimEnd() + "`r`n"
    $ps1 = ($SHIM_PS1 -replace "`r?`n", "`r`n").TrimEnd() + "`r`n"
    [IO.File]::WriteAllText((Join-Path $dir 'wmic.cmd'),      $cmd, [Text.Encoding]::ASCII)   # .cmd NAO pode ter BOM
    [IO.File]::WriteAllText((Join-Path $dir 'wmic-shim.ps1'), $ps1, (New-Object Text.UTF8Encoding $true))
}

function Main {
    # ---------- 1) Admin / 64 bits ----------
    $id      = [Security.Principal.WindowsIdentity]::GetCurrent()
    $isAdmin = (New-Object Security.Principal.WindowsPrincipal $id).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
    if (-not $isAdmin) { Fail "Rode em um PowerShell como Administrador (botao direito no PowerShell > Executar como administrador)." }
    if ([Environment]::Is64BitOperatingSystem -and -not [Environment]::Is64BitProcess) { Fail "Use o PowerShell 64 bits (nao o '(x86)')." }
    Ok "PowerShell em modo Administrador ($($id.Name))."

    # ---------- 2) Windows ----------
    $cv    = Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows NT\CurrentVersion'
    $build = [int]$cv.CurrentBuildNumber
    Info "Windows build $build.$($cv.UBR) ($($cv.DisplayVersion))."

    $wbem    = Join-Path $env:SystemRoot 'System32\Wbem'
    $wbem32  = Join-Path $env:SystemRoot 'SysWOW64\wbem'
    $wmicExe = Join-Path $wbem 'wmic.exe'
    $usouShim      = $false

    # ---------- 3) wmic.exe existe? ----------
    if (Test-Path $wmicExe) {
        Ok "wmic.exe original existe: $wmicExe"
    } else {
        if ($build -ge 26100) {
            Info "wmic.exe nao existe. Neste Windows a Microsoft removeu o WMIC e o Feature on Demand nao esta mais disponivel (desde ago/2026)."
        } else {
            Warn "wmic.exe nao existe, mas neste build ele deveria ser nativo. Depois rode: sfc /scannow"
        }

        Info "Instalando shim 'wmic' (wmic.cmd + wmic-shim.ps1, emula o wmic via CIM/PowerShell)..."
        Write-Shim $wbem
        Ok "Shim gravado em $wbem"
        if (Test-Path $wbem32) { Write-Shim $wbem32; Ok "Shim gravado em $wbem32 (para processos 32 bits)" }
        $usouShim = $true
    }

    # ---------- 4) PATH do sistema contem Wbem? ----------
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
        try {
            $sig = '[DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)] public static extern IntPtr SendMessageTimeout(IntPtr hWnd, uint Msg, UIntPtr wParam, string lParam, uint fuFlags, uint uTimeout, out UIntPtr lpdwResult);'
            $nm  = Add-Type -MemberDefinition $sig -Name 'NativeMethods' -Namespace 'Win32Env' -PassThru
            $res = [UIntPtr]::Zero
            $nm::SendMessageTimeout([IntPtr]0xFFFF, 0x1A, [UIntPtr]::Zero, 'Environment', 2, 5000, [ref]$res) | Out-Null
        } catch { Warn "Nao consegui notificar o sistema sobre o PATH (WM_SETTINGCHANGE): $($_.Exception.Message)" }
        Ok "PATH do sistema atualizado."
    }
    if (@($env:Path -split ';' | Where-Object { $_.TrimEnd('\') -ieq $wbem }).Count -eq 0) { $env:Path += ";$wbem" }

    # ---------- 5) Teste real no cmd ----------
    Info "Testando no cmd: wmic os get caption /value"
    $out  = & cmd.exe /d /c "wmic os get caption /value 2>&1"
    $code = $LASTEXITCODE
    $txt  = ($out -join "`n")
    if ($code -eq 0 -and $txt -match 'Caption=') {
        $capt = ($out | Where-Object { $_ -match 'Caption=' } | Select-Object -First 1).Trim()
        Ok "wmic respondeu no cmd: $capt"
    } else {
        Fail "wmic nao respondeu corretamente no cmd (codigo $code):`n$txt"
    }
    $where = & cmd.exe /d /c "where wmic 2>nul"
    if ($LASTEXITCODE -eq 0) { Ok "cmd resolve 'wmic' em: $(($where | Select-Object -First 1).Trim())" }
    else { Warn "cmd nao resolveu 'wmic' pelo PATH neste processo." }

    # ---------- Resumo ----------
    Write-Host ""
    if ($usouShim) {
        Ok "Concluido: 'wmic' funcionando no cmd via shim CIM/PowerShell."
        Info "Shim cobre: <alias|path Classe> [where ...] get/list/call/delete/set, /value, /format:list|csv|table, /node, /output. 'wmic /?' mostra a ajuda."
        Info "Limite: programas que chamam 'wmic.exe' com a extensao nao enxergam o shim."
    } else {
        Ok "Concluido: WMIC habilitado e funcionando para o cmd."
    }
    if ($pathChanged)   { Warn "Janelas de cmd/PowerShell que JA estavam abertas precisam ser reabertas para enxergar o PATH novo." }
}

try { Main }
catch { Write-Host ""; Write-Host "[ERRO]  $($_.Exception.Message)" -ForegroundColor Red }
finally { $ErrorActionPreference = $__oldEAP }
