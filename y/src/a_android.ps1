<#
.SYNOPSIS
    Script Gerenciador de Android 9 via QEMU + HAXM.
#>

# ==========================================================
# Exemplo de chamada:
# powershell -ExecutionPolicy Bypass -File "C:\qemu_hax\android.ps1" -GUI_ENABLE 1
# adb -s 127.0.0.1:5556 shell
# http://203.cloudns.cl:8000/z_outros/android9.qcow2
# https://mirrors.dotsrc.org/osdn/android-x86/71931/android-x86_64-9.0-r2.iso
# ==========================================================
$MODE_INSTALL = $false
$GUI_ENABLE   = $true
$DELETE_DISK  = $false
# ==========================================================

for ($i = 0; $i -lt $args.Count; $i += 2) {
    $paramName = $args[$i].Replace("-", "").ToUpper()
    $paramValue = $args[$i + 1]
    $boolValue = ($paramValue -eq "true" -or $paramValue -eq "1" -or $paramValue -eq $true)

    switch ($paramName) {
        "MODE_INSTALL" { $MODE_INSTALL = $boolValue }
        "GUI_ENABLE"   { $GUI_ENABLE   = $boolValue }
        "DELETE_DISK"  { $DELETE_DISK  = $boolValue }
    }
}

# Caminhos de diretórios
$P1 = "C:\qemu"
$P2 = "C:\qemu_hax6"
$P3 = "C:\qemu_isos"
$P4 = "C:\qemu_share"
$BIN = "$P1\qemu-system-x86_64.exe"
$DISK = "$P2\android9.qcow2"
$QEMU_IMG = "$P1\qemu-img.exe"
$QEMU_MEMORIA = "4G"
$QEMU_N_PROCESSADORES = "4"
$QEMU_SHARE = "$P4"
$ANDROID_ISO = "$P3\android-x86_64-9.0-r2.iso"

# Redirecionamento de Portas: SSH (222) e ADB (5556)
$QEMU_FWD = "user,id=net0,hostfwd=tcp::222-:22,hostfwd=tcp::5556-:5555"

# 1. Limpeza de processos anteriores
# taskkill /f /im qemu-system-x86_64.exe 2>$null

# 2. Verificação do Acelerador HAXM
$haxmStatus = sc.exe query intelhaxm
if ($LASTEXITCODE -ne 0 -or !($haxmStatus -match "RUNNING")) {
    Write-Error "ERRO: O Intel HAXM não está rodando. Instale com 'haxm-7.8.0-setup.exe /S'"
    exit
}

# 3. Construção dos Argumentos do QEMU
$QEMU_ARGS = @()
$QEMU_ARGS += "-L", $QEMU_SHARE
$QEMU_ARGS += "-m", $QEMU_MEMORIA
$QEMU_ARGS += "-smp", $QEMU_N_PROCESSADORES
$QEMU_ARGS += "-accel", "hax"
$QEMU_ARGS += "-cpu", "kvm64" # 'max' funciona melhor que 'core2duo' para Android 9

# Disco Principal
$QEMU_ARGS += "-drive", "file=$DISK,if=virtio,cache=writeback"

# Configuração de Vídeo e Mouse
if ($GUI_ENABLE) {
    # virtio-vga é o padrão para aceleração no Android-x86
    $QEMU_ARGS += "-vga", "std" 
    $QEMU_ARGS += "-display", "gtk"
    $QEMU_ARGS += "-device", "usb-ehci,id=usb,bus=pci.0,addr=0x7"
    $QEMU_ARGS += "-device", "usb-tablet" # Essencial para o mouse não dessincronizar
} else {
    $QEMU_ARGS += "-nographic"
}

# Rede com Forwarding para ADB
$QEMU_ARGS += "-netdev", $QEMU_FWD
$QEMU_ARGS += "-device", "rtl8139,netdev=net0"

# Modo de Instalação (ISO)
if ($MODE_INSTALL) {
    if (Test-Path $ANDROID_ISO) {
        $QEMU_ARGS += "-drive", "file=$ANDROID_ISO,media=cdrom,readonly=on"
        $QEMU_ARGS += "-boot", "d"
    } else {
        Write-Warning "ISO não encontrada em $ANDROID_ISO. Iniciando pelo disco."
    }
}

# Gerenciamento do Disco
if ($DELETE_DISK -and (Test-Path $DISK)) {
    Remove-Item $DISK -Force
}

if (!(Test-Path $DISK)) {
    Write-Host "Criando novo disco Android de 20GB..." -ForegroundColor Cyan
    & $QEMU_IMG create -f qcow2 $DISK 20G
}

# 4. Execução
Write-Host "Iniciando QEMU (Android 9)..." -ForegroundColor Green
Write-Host "Comandos: $QEMU_ARGS" -ForegroundColor Gray
& $BIN $QEMU_ARGS

# 5. Instruções Pós-Boot
Write-Host "`nPara conectar o ADB, aguarde o Android carregar e use:" -ForegroundColor Yellow
Write-Host "adb connect 127.0.0.1:5556" -ForegroundColor White