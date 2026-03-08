<#
.SYNOPSIS
    Script Gerenciador de Android 9 via QEMU + HAXM.
#>

# ==========================================================
# Exemplo de chamada:
# adb connect 127.0.0.1:5556 && adb shell
# http://203.cloudns.cl:8000/z_outros/android11.qcow2
# https://dn720809.ca.archive.org/0/items/android-x-86-11-r-arm-x86-64-iso/Android-x86%2011-R%202022-05-04%20%28x86_64%29%20K5.4.40-M21-arm-noGapps-by-Xigo.iso
# https://apkpure.com/br/leek-factory-tycoon-idle-game/com.gaman.games.leek.factory.tycoon
# adb -s 127.0.0.1:5556 install-multiple com.gaman.games.leek.factory.tycoon.apk config.armeabi_v7a.apk config.en.apk config.vi.apk
# https://apkpure.com/br/dnschanger-for-ipv4-ipv6/com.frostnerd.dnschanger/download?utm_content=1008
# adb -s 127.0.0.1:5556 install "DNSChanger for IPv4_IPv6_1.16.5.11_APKPure.apk"
# criando f1 e f7 no adb
# su -c "mount -o remount,rw /"
# echo -e '#!/system/bin/sh\nservice call SurfaceFlinger 1008 i32 0 i32 0 && chvt 1' > /system/xbin/f1 && chmod 755 /system/xbin/f1
# echo -e '#!/system/bin/sh\nservice call SurfaceFlinger 1008 i32 0 i32 2 && sleep 2 && chvt 7' > /system/xbin/f7 && chmod 755 /system/xbin/f7
# tira cursor piscando(su)
# mount -o remount,rw / && echo -e '#!/system/bin/sh\nwhile true; do\n  echo 0 > /sys/class/graphics/fbcon/cursor_blink\n  sleep 10\ndone' > /system/xbin/nocursor && chmod 755 /system/xbin/nocursor && echo -e 'service nocursor /system/xbin/nocursor\n    class main\n    user root' > /system/etc/init/nocursor.rc && chmod 644 /system/etc/init/nocursor.rc
# bootloader 1 segundo
# mount -o remount,rw / && mkdir -p /tmp/boot && mount -t ext4 /dev/block/vda1 /tmp/boot && sed -i 's|timeout=6|timeout=1|' /tmp/boot/grub/menu.lst
# apaga tudo
# adb shell "su -c 'pm clear com.gaman.games.leek.factory.tycoon'"
# backup
# adb shell "su -c 'tar czf - --exclude=lib -C /data/data com.gaman.games.leek.factory.tycoon | base64'" > a.txt
# restore
# adb shell am force-stop com.gaman.games.leek.factory.tycoon && adb shell "su -c 'settings put secure android_id $(cat /proc/sys/kernel/random/uuid | cut -d- -f1-2)'" && y cat a.txt | adb shell "su -c 'rm -rf /data/data/com.gaman.games.leek.factory.tycoon && base64 -d | tar xzf - -C /data/data && restorecon -R /data/data/com.gaman.games.leek.factory.tycoon'"
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
$DISK = "$P2\android11.qcow2"
$QEMU_IMG = "$P1\qemu-img.exe"
$QEMU_MEMORIA = "4G"
$QEMU_N_PROCESSADORES = "4"
$QEMU_SHARE = "$P4"
$ANDROID_ISO = "$P3\Android-x86 11-R 2022-05-04 (x86_64) K5.4.40-M21-arm-noGapps-by-Xigo.iso"

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
    #$QEMU_ARGS += "-vga", "std" 
	
    $QEMU_ARGS += "-vga", "qxl" 
	$QEMU_ARGS += "-monitor", "stdio"
	
    #$QEMU_ARGS += "-device", "virtio-vga", "-serial", "none", "-monitor", "none" 
	#$QEMU_ARGS += "-append", "quiet video=1280x720"
	
	
    $QEMU_ARGS += "-display", "sdl"
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
Write-Host "Iniciando QEMU..." -ForegroundColor Green
Write-Host "Comandos: $QEMU_ARGS" -ForegroundColor Gray
& $BIN $QEMU_ARGS


