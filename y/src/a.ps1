<#
.SYNOPSIS
    Script unificado para gerenciamento de VM Ubuntu via QEMU + HAXM.
#>

# ==========================================================
# FLAGS DE CONFIGURAÇÃO (Ajuste aqui)
# ==========================================================
$MODE_INSTALL   = $false          # $true: Modo instalação (monta ISOs) | $false: Modo uso (apenas disco)
$GUI_ENABLE     = $false          # $true: Abre janela do QEMU | $false: Modo terminal (-nographic)
$AUTO_INSTALL   = $true          # $true: Tenta automação via cloud-init/cidata (requer kernel/initrd extraídos)
$TAP_NETWORK    = $true          # $true: Ativa a segunda placa de rede via tap0 (Windows bridge-like)
$DELETE_DISK    = $false         # $true: Apaga o disco atual e cria um novo do zero
# ==========================================================

# Caminhos de diretórios
$P1 = "C:\qemu"
$P2 = "C:\qemu_hax"
$P3 = "C:\qemu_isos"
$P4 = "C:\qemu_share"
$BIN = "$P1\qemu-system-x86_64.exe"
$DISK = "$P2\ubuntu_disk.qcow2"
$QEMU_IMG = "$P1\qemu-img.exe"
$QEMU_MEMORIA = "4G"
$QEMU_N_PROCESSADORES = "4"
$QEMU_FWD = "user,id=net0,hostfwd=tcp::2222-:22"
$QEMU_SHARE = "$P4"
$QEMU_KERNEL = "$P2\vmlinuz_gui"
$QEMU_INIT = "$P2\initrd_gui"
$UBUNTU_ISO = "$P3\ubuntu-25.10-desktop-amd64.iso"
$UBUNTU_SERVER_ISO = "$P3\ubuntu-25.10-live-server-amd64.iso"
$CIDATA_ISO = "$P2\cidata.iso"

# 1. Limpeza de processos anteriores
taskkill /f /im qemu-system-x86_64.exe 2>$null

# 2. Gerenciamento do Disco (qcow2)
if ($DELETE_DISK -and (Test-Path $DISK)) {
    Write-Host "Removendo disco existente..." -ForegroundColor Yellow
    Remove-Item $DISK -Force
}

if (!(Test-Path $DISK)) {
    Write-Host "Criando novo disco virtual de 900GB..." -ForegroundColor Cyan
    # qemu-img create define o formato (qcow2) e o tamanho máximo (dinâmico)
    & $QEMU_IMG create -f qcow2 $DISK 900G
}

# 3. Verificação do Acelerador HAXM
$haxmStatus = sc.exe query intelhaxm
if ($LASTEXITCODE -ne 0 -or !($haxmStatus -match "RUNNING")) {
    Write-Error "ERRO: O Intel HAXM não está rodando. Verifique a instalação."
    Write-Error "# https://github.com/intel/haxm/releases/download/v7.8.0/haxm-windows_v7_8_0.zip"
    Write-Error "# fonte: https://github.com/intel/haxm/releases/tag/v7.8.0"
    Write-Error "# instale assim cmd adm"
    Write-Error "# haxm-7.8.0-setup.exe /S"
    exit
}

# 4. Construção dos Argumentos do QEMU
$QEMU_ARGS = @()

# Define o caminho das BIOS/Firmware
$QEMU_ARGS += "-L", $QEMU_SHARE

# Memória RAM e quantidade de processadores (SMP)
$QEMU_ARGS += "-m", $QEMU_MEMORIA
$QEMU_ARGS += "-smp", $QEMU_N_PROCESSADORES

# Aceleração HAX (Intel Hardware Accelerated Execution Manager)
$QEMU_ARGS += "-accel", "hax"

# Modelo de CPU compatível com HAX
$QEMU_ARGS += "-cpu", "core2duo"

# Disco Principal usando interface Virtio para melhor performance de E/S
$QEMU_ARGS += "-drive", "file=$DISK,if=virtio"

# Placa de Vídeo / Interface Gráfica
if ($GUI_ENABLE) {
    # Habilita VGA padrão e suporte a Tablet (para sincronizar mouse sem lag)
    $QEMU_ARGS += "-vga", "std"
    $QEMU_ARGS += "-device", "usb-ehci,id=usb,bus=pci.0,addr=0x7"
    $QEMU_ARGS += "-device", "usb-tablet"
} else {
    # Desativa interface gráfica e redireciona a serial para o terminal do host
    $QEMU_ARGS += "-nographic"
    $QEMU_ARGS += "-serial", "mon:stdio"
	$UBUNTU_ISO = $UBUNTU_SERVER_ISO
	$QEMU_KERNEL = "$P2\vmlinuz_nogui"
	$QEMU_INIT = "$P2\initrd_nogui"

}

# Rede 1: Modo Usuário (NAT) com Redirecionamento de Porta SSH (2222 -> 22)
$QEMU_ARGS += "-netdev", $QEMU_FWD
$QEMU_ARGS += "-device", "virtio-net-pci,netdev=net0"

# Rede 2: Interface TAP (Opcional - Requer driver TAP-Windows instalado e nomeado como tap0)
if ($TAP_NETWORK) {
    # Conecta a VM a um adaptador de rede real/virtual no Windows
    $QEMU_ARGS += "-netdev", "tap,id=net1,ifname=tap0"
    $QEMU_ARGS += "-device", "virtio-net-pci,netdev=net1"
}

# Configurações de Instalação (ISOs e Kernel)
if ($MODE_INSTALL) {
    # Monta a ISO do Sistema Operacional
    $QEMU_ARGS += "-drive", "file=$UBUNTU_ISO,media=cdrom,readonly=on"
    
    # Monta a ISO de configuração (Cloud-init / CIDATA)
    $QEMU_ARGS += "-drive", "file=$CIDATA_ISO,media=cdrom,readonly=on"
    
    # Ordem de boot: Prioridade para o Drive de CD/DVD
    $QEMU_ARGS += "-boot", "d"

    if ($AUTO_INSTALL) {
        # Para autoinstall funcionar nographic, passamos o kernel/initrd externamente
        # Certifique-se de ter extraído vmlinuz e initrd da ISO para a pasta $P2
        if ((Test-Path $QEMU_KERNEL) -and (Test-Path $QEMU_INIT)) {
            $QEMU_ARGS += "-kernel", $QEMU_KERNEL
            $QEMU_ARGS += "-initrd", $QEMU_INIT
            $QEMU_ARGS += "-append", "console=ttyS0,115200n8 autoinstall ds=nocloud"
        } else {
            Write-Host "AVISO: vmlinuz/initrd não encontrados em $P2. Autoinstall pode falhar no console." -ForegroundColor Red
        }
    }
}

Write-Host "comandos usados:"
Write-Host $QEMU_ARGS

# 5. Execução
Write-Host "Iniciando QEMU com as configuracoes definidas..." -ForegroundColor Green
& $BIN $QEMU_ARGS


# depois de tudo configurado, rode:
# powershell -ExecutionPolicy Bypass -File "C:\qemu_hax\a.ps1"

# liberar powershell
# Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
# ou
# Set-ExecutionPolicy Bypass -Scope Process

# qemu testei com essa versao
# https://qemu.weilnetz.de/w64/2022/qemu-w64-setup-20221230.exe

# install hax
# https://github.com/intel/haxm/releases/download/v7.8.0/haxm-windows_v7_8_0.zip
# fonte: https://github.com/intel/haxm/releases/tag/v7.8.0
# ou https://github.com/ywanes/utility_y/raw/refs/heads/master/y/utils_exe/haxm-7.8.0-setup.exe
# instale assim cmd adm
# haxm-7.8.0-setup.exe /S

# baixa o cidata.iso para instalacao automatica, login e senha base
# https://github.com/ywanes/utility_y/raw/refs/heads/master/y/utils_exe/cidata.iso

# liberando ssh
# sudo apt update && sudo apt install openssh-server -y
# sudo systemctl enable --now ssh
# sudo ufw allow 22 # liberando firewall
# ss -tln | grep :22 # porta escutando
# no windows, conectar com ssh base@127.0.0.1 -p 2222
# assim o .ssh nao fica reclamando
# ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null base@127.0.0.1 -p 2222

# login base senha base
# usar com ssh é melhor

# install tap0
# instalar tap0 para o host olhar o ip interno da vm
# a vm ja consegue ver seu dns caso vc o tenha
# https://build.openvpn.net/downloads/releases/
# OpenVPN-2.7_rc5-I013-amd64.msi
# ou https://github.com/ywanes/utility_y/raw/refs/heads/master/y/utils_exe/OpenVPN-2.7_rc5-I013-amd64.msi
# verifique o nome(powershell adm)
# Get-NetAdapter | select Name, InterfaceDescription, Status
# OpenVPN TAP-Windows6
# vc é obrigado a trocar o nome, troque para tap0, tem que ser tap0
# Rename-NetAdapter -Name "OpenVPN TAP-Windows6" -NewName "tap0"

# configurando tap0
# windows powershell adm
# netsh interface ipv6 set address interface="tap0" address=f100::100
# linux
# sudo ip -6 addr add f100::200/64 dev ens4
# sudo ip link set ens4 up
# depois ajuste o /etc/netplan/00-installer-config.yaml
#     ens4:
#       addresses:
#         - f100::200/64
#       optional: true
# depois aplique
# netplan apply
# coloque no crontab -e # para ajudar
# */30 * * * * ping6 -c 1 f100::100 > /dev/null 2>&1
# é para o windows e linux se verem
# teste no linux
# ping6 f100::100
# no Windows
# y ping f100::200

# linux live-server para terminal
# https://releases.ubuntu.com/25.10/ubuntu-25.10-live-server-amd64.iso
# linux gui
# https://releases.ubuntu.com/questing/ubuntu-25.10-desktop-amd64.iso

# montando ISO cidata:
# y iso source cidata.iso -cidata

# sair do terminal qemu:
# control a depois x
