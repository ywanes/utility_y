#!/usr/bin/env bash
#
#   sudo bash -c 'bash <(curl -fsSL https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/build-ubuntu-iso.sh) 26.10'
#   sudo bash -c 'bash <(curl -fsSL https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/build-ubuntu-iso.sh) list'
#   ou
#   chmod +x build-ubuntu-iso.sh
#   sudo ./build-ubuntu-iso.sh 2610        # ou 26.04, 2610, 24.04, 25.10 ...
#
# Builder GENÉRICO de ISO live do Ubuntu (BIOS + UEFI, foco em VMware e qemu).
# Você passa SÓ a versão; o script descobre o codinome/suíte sozinho no archive.
#
# Complementos pouco usados:
#   PROFILE=desktop|nogui    SKIP_GPG_CHECK=true|false  LANG_LIVE=pt_BR.UTF-8
#   WORK=/caminho  OUT=/caminho.iso  APTCACHE=/caminho
#
# obs: essa iso tem a função LIVE GUI e LIVE NOGUI. Dentro de cada uma delas é possivel instalar o sistema com o comando abaixo
#          sudo install-to-disk.sh /dev/sda 
#          sudo USERNAME=meuuser USERPASS=minhasenha install-to-disk.sh /dev/sda
#          # o /dev/sda vai ser apagado no processo
# obs2: essa iso suporta cidata, mas nao suporta Subiquity
# obs3: iso fica uns 2 gigas e o sistema instalado uns 4.5 gigas
# obs4: versoes muito antigas pode nao funcionar, depende do repositorio
set -euo pipefail

# ------------------------------- modo LIST ----------------------------------
# 'list' mostra as versões do Ubuntu e seus codinomes de DUAS palavras.
# NÃO precisa de root. Os codinomes de 2 palavras vêm do distro-info-data
# (CSV oficial do sistema) — o archive só expõe o codinome de 1 palavra (a
# "suite"), que é o que este builder resolve sozinho na hora de construir.
case "${1:-}" in
  list|-l|--list)
    CSV="/usr/share/distro-info/ubuntu.csv"
    if [ -r "$CSV" ]; then
      echo "Versões do Ubuntu  (número | codinome | suite):"
      echo
      # CSV: version,codename,series,created,release,eol[,eol-server,eol-esm]
      awk -F, 'NR>1 && $2 ~ / / {printf "  %-11s %-24s %s\n", $1, $2, $3}' "$CSV" \
        | sort -V
      echo
      echo "Para construir, passe o NÚMERO. ex.:  build-ubuntu-iso.sh 26.04"
      echo "A 'suite' (1 palavra) é o codinome que o build resolve no archive."
    else
      echo "distro-info-data não encontrado em $CSV."
      echo "Para ver os codinomes de 2 palavras:  sudo apt install distro-info-data"
      echo
      echo "Suites disponíveis no archive agora (apenas 1 palavra):"
      curl -fsSL "http://archive.ubuntu.com/ubuntu/dists/" 2>/dev/null \
        | grep -oE 'href="[a-z]+/"' \
        | sed -E 's#href="([a-z]+)/"#  \1#' \
        | grep -vE '  (devel|stable|oldstable)$' \
        | sort -u \
        || echo "  (sem rede para consultar o archive)"
    fi
    exit 0
    ;;
esac

# ----------------------------- versão (parâmetro) ----------------------------
# Aceita "2604", "26.04", "2610", etc. Normaliza para o formato AA.MM.
VERSION_INPUT="${1:-${VERSION:-}}"
if [ -z "$VERSION_INPUT" ]; then
  echo "Uso: sudo ./build-ubuntu-iso.sh <versao>"
  echo "  ex: sudo ./build-ubuntu-iso.sh 2604      (= 26.04)"
  echo "      sudo ./build-ubuntu-iso.sh 26.10"
  exit 1
fi
_digits="$(printf '%s' "$VERSION_INPUT" | tr -cd '0-9')"
if [ "${#_digits}" -ne 4 ]; then
  echo "ERRO: versão inválida '$VERSION_INPUT'. Use AAMM (ex: 2604) ou AA.MM (ex: 26.04)."
  exit 1
fi
VERSION="${_digits:0:2}.${_digits:2:2}"        # ex: 26.04

# Fail-fast: o Ubuntu só lança em abril (.04) e outubro (.10). Se o mês não for
# um desses, nem adianta consultar a rede — recusa já com uma dica do que você
# provavelmente quis. (Sim, o 6.06 'Dapper' de 2006 fugiu à regra; é a única
# exceção da história e não builda mais via debootstrap, então ignoramos.)
_yy="${_digits:0:2}"; _mm="${_digits:2:2}"
if [ "$_mm" != "04" ] && [ "$_mm" != "10" ]; then
  echo "ERRO: versão ${VERSION} não existe — o Ubuntu só lança em abril (.04) e outubro (.10)."
  echo "      Você quis dizer ${_yy}04 (${_yy}.04) ou ${_yy}10 (${_yy}.10)?"
  exit 1
fi

# ----------------------------- configuração ---------------------------------
# >>> VALIDAÇÃO DE ASSINATURA <<<
# true  = NÃO valida a assinatura do repositório (passa --no-check-gpg ao debootstrap)
# false = valida normalmente (recomendado; é rápido e garante a integridade)
SKIP_GPG_CHECK="${SKIP_GPG_CHECK:-true}"

ARCH="amd64"
MIRROR="http://archive.ubuntu.com/ubuntu"             # releases atuais/dev
OLD_MIRROR="http://old-releases.ubuntu.com/ubuntu"    # releases já EOL
SUITE=""                                              # <- descoberto adiante
PROFILE="${PROFILE:-desktop}"                 # 'desktop' ou 'nogui'
WORK="${WORK:-$(pwd)/ubuntu-iso-build}"       # diretório de trabalho
CHROOT="$WORK/chroot"
IMAGE="$WORK/image"
OUT="${OUT:-$(pwd)/ubuntu-${VERSION}-${PROFILE}-amd64.iso}"

# --- descoberta da suíte/codinome a partir da versão (consulta o archive) ----
# Varre os codinomes-base de /dists/, lê o topo do Release de cada um
# (com Range: só os ~2 KB iniciais, onde ficam Suite/Version/Codename) e
# devolve aquele cujo "Version:" bate com a versão pedida. Tenta primeiro o
# archive normal e, se não achar, o old-releases (versões já EOL).
discover_suite() {
  local want="$1" m d ver
  for m in "$MIRROR" "$OLD_MIRROR"; do
    for d in $(curl -fsSL "$m/dists/" 2>/dev/null \
                 | grep -oE 'href="[a-z]+/"' \
                 | sed -E 's/.*"([a-z]+)\/".*/\1/' | sort -u); do
      case "$d" in devel|stable|oldstable) continue;; esac
      ver="$(curl -fsSL -r 0-2047 "$m/dists/$d/Release" 2>/dev/null \
               | awk -F': ' '/^Version:/{print $2; exit}')"
      if [ "$ver" = "$want" ]; then
        echo "$d|$m"          # codinome|mirror
        return 0
      fi
    done
  done
  return 1
}
# cache persistente de .deb: fica FORA do $WORK, então não é apagado entre builds.
# Se o .deb já estiver aqui, debootstrap/apt reaproveitam e pulam pro próximo.
APTCACHE="${APTCACHE:-$(pwd)/apt-cache}"
HOSTNAME_LIVE="ubuntu"
LANG_LIVE="pt_BR.UTF-8"                        # troque p/ en_US.UTF-8 se preferir

# normaliza nomes de perfil (compat: 'minimal' agora é 'nogui')
[ "$PROFILE" = "minimal" ] && PROFILE="nogui"

# pacotes por perfil. NO_RECO controla --no-install-recommends por perfil.
if [ "$PROFILE" = "desktop" ]; then
  # ubiquity* = instalador gráfico + integração com a live (ícone "Instalar Ubuntu").
  # libgl1-mesa-dri & cia = drivers GL da Mesa (inclui llvmpipe, render por software).
  #   Sem isto o GNOME Shell não renderiza -> tela preta com cursor numa VM.
  # xserver-xorg* = servidor X11 completo + driver de vídeo/entrada da VMware.
  #   O build mínimo só traz Xwayland; como forçamos X11 (WaylandEnable=false),
  #   precisamos do servidor Xorg de verdade, senão = tela preta.
  # open-vm-tools-desktop = clipboard, resize automático e pastas compartilhadas no VMware
  # Esta imagem é DUAL: sobe GUI (opção 1 do menu) OU console (opção 2 nogui).
  # Por isso inclui também os itens "de servidor", úteis no modo nogui:
  #   openssh-server = acesso remoto; cloud-init = autoinstalação via volume CIDATA;
  #   squashfs-tools = 'unsquashfs', exigido pelo install-to-disk.sh.
  EXTRA_PKGS="ubuntu-desktop-minimal ubiquity ubiquity-frontend-gtk ubiquity-casper \
              libgl1-mesa-dri libglx-mesa0 libegl-mesa0 \
              xserver-xorg xserver-xorg-core xserver-xorg-video-vmware xserver-xorg-input-libinput \
              rsync \
              network-manager open-vm-tools open-vm-tools-desktop \
              openssh-server cloud-init squashfs-tools qemu-guest-agent"
  # Recommends LIGADO no desktop: o GNOME "completo" mora em boa parte nos Recommends.
  # Fica maior (~1,5 GB a mais), mas vem completo e evita o jogo de gato e rato.
  # Para tentar a versão enxuta, troque para: NO_RECO="--no-install-recommends"
  NO_RECO=""
elif [ "$PROFILE" = "nogui" ]; then
  # Imagem HEADLESS (console), pensada p/ VMware e p/ autoinstalação não-assistida.
  #   openssh-server = acesso remoto à live; open-vm-tools (sem -desktop) = integração VMware;
  #   cloud-init     = consome um seed NoCloud de um volume EXTERNO rotulado CIDATA;
  #   squashfs-tools = traz o 'unsquashfs', exigido pelo install-to-disk.sh na hora de instalar.
  EXTRA_PKGS="network-manager openssh-server open-vm-tools cloud-init squashfs-tools qemu-guest-agent"
  NO_RECO="--no-install-recommends"
else
  echo "ERRO: PROFILE inválido ('$PROFILE'). Use 'desktop' ou 'nogui'."
  exit 1
fi

# ----------------------------- preflight -------------------------------------
# (validação de root removida a pedido — atenção: ainda PRECISA rodar como root,
#  senão debootstrap/chroot/mksquashfs vão falhar adiante.)
mkdir -p "$APTCACHE"

# curl é necessário JÁ para descobrir a suíte no archive.
if ! command -v curl >/dev/null 2>&1; then
  echo ">> Instalando curl (necessário para a descoberta)..."
  apt-get update -y
  apt-get install -y curl ca-certificates
fi

echo ">> Descobrindo a suíte da versão ${VERSION} no archive..."
_found="$(discover_suite "$VERSION" || true)"
if [ -z "$_found" ]; then
  echo "ERRO: não encontrei nenhuma suíte com Version=${VERSION}."
  echo "      Possíveis causas:"
  echo "        - a versão ainda não existe (release futura / não publicada);"
  echo "        - você digitou errado (use AAMM, ex: 2604, ou AA.MM, ex: 26.04);"
  echo "        - é uma versão muito antiga e nem o old-releases tem mais."
  exit 1
fi
SUITE="${_found%%|*}"        # codinome (ex: noble)
MIRROR="${_found##*|}"       # mirror onde foi achada (archive ou old-releases)
echo ">> Versão ${VERSION} => suíte '${SUITE}' em ${MIRROR}"

echo ">> Instalando ferramentas de build no host..."
apt-get update -y
apt-get install -y \
  debootstrap squashfs-tools xorriso isolinux syslinux-common \
  grub-pc-bin grub-efi-amd64-bin grub-common mtools dosfstools ca-certificates

# GOTCHA real: debootstrap recém-instalado não tem o script da suíte nova.
# Todas as releases do Ubuntu usam o mesmo script 'gutsy' -> basta linkar.
if [ ! -e "/usr/share/debootstrap/scripts/${SUITE}" ]; then
  echo ">> Criando symlink do script de release para ${SUITE}..."
  ln -sf /usr/share/debootstrap/scripts/gutsy "/usr/share/debootstrap/scripts/${SUITE}"
fi

# --- BLINDAGEM do rm -rf "$WORK" ---------------------------------------------
# 1) Recusa rodar se WORK apontar para um caminho perigoso (/, $HOME, ou um
#    diretório de sistema de topo). Subpastas comuns (ex: ~/algo/ubuntu-iso-build)
#    continuam permitidas — só barramos os alvos catastróficos.
_WORK_RP="$(readlink -m "$WORK")"
case "$_WORK_RP" in
  /|"$HOME"|/root|/home|/etc|/usr|/bin|/sbin|/lib|/lib64|/boot|/dev|/proc|/run|/sys|/var|/opt|/srv|/mnt|/media)
    echo "ERRO: WORK aponta para um caminho perigoso ('$_WORK_RP')."
    echo "      Use um diretório dedicado, ex: WORK=\$HOME/build-iso"
    exit 1
    ;;
esac
# 2) Pega-ratão do kill -9: se uma execução anterior morreu deixando /dev,/proc,
#    /sys,/run ainda bind-montados sob $CHROOT, o 'rm -rf' recursaria PARA DENTRO
#    deles e apagaria coisas do HOST. Desmonta defensivamente ANTES de apagar.
if [ -d "$CHROOT" ]; then
  for fs in /run /sys /proc /dev/pts /dev; do
    mountpoint -q "$CHROOT$fs" && { echo ">> Desmontando resíduo: $CHROOT$fs"; umount -lf "$CHROOT$fs"; } || true
  done
fi

rm -rf "$WORK"
mkdir -p "$CHROOT" "$IMAGE/casper" "$IMAGE/boot/grub" "$IMAGE/EFI/boot" "$IMAGE/.disk"

# ----------------------------- 1) base (debootstrap) -------------------------
echo ">> debootstrap da base ${SUITE} (${ARCH})..."
# monta o flag de gpg conforme a configuração do topo
GPG_FLAG=""
if [ "$SKIP_GPG_CHECK" = "true" ]; then
  GPG_FLAG="--no-check-gpg"
  echo ">> AVISO: validação de assinatura DESLIGADA (SKIP_GPG_CHECK=true)."
fi
# --cache-dir: reaproveita .deb já baixados; só busca os que faltam.
# --components=main: a base só usa main; os outros 3 ficam no sources.list do chroot.
debootstrap --arch="$ARCH" --variant=minbase \
  ${GPG_FLAG} \
  --cache-dir="$APTCACHE" \
  --components=main \
  "$SUITE" "$CHROOT" "$MIRROR"

# ----------------------------- 2) sources + mounts ---------------------------
# O repositório de segurança fica em security.ubuntu.com nas releases atuais,
# mas em versões EOL ele também está no old-releases (mesmo mirror do resto).
if [ "$MIRROR" = "$OLD_MIRROR" ]; then
  SEC_URL="$MIRROR"
else
  SEC_URL="http://security.ubuntu.com/ubuntu"
fi
cat > "$CHROOT/etc/apt/sources.list" <<EOF
deb $MIRROR $SUITE main restricted universe multiverse
deb $MIRROR ${SUITE}-updates main restricted universe multiverse
deb $SEC_URL ${SUITE}-security main restricted universe multiverse
EOF

# Versão EOL (old-releases): o Release tem "Valid-Until" no passado e o apt
# recusa com "Release file is expired". Desliga essa checagem SÓ nesse caso
# (em release atual isso fica ligado, como deve ser).
if [ "$MIRROR" = "$OLD_MIRROR" ]; then
  echo ">> Build a partir do old-releases: desligando checagem de validade do Release."
  mkdir -p "$CHROOT/etc/apt/apt.conf.d"
  printf 'Acquire::Check-Valid-Until "false";\n' \
    > "$CHROOT/etc/apt/apt.conf.d/99no-check-valid-until"
fi

cp /etc/resolv.conf "$CHROOT/etc/resolv.conf"
echo "$HOSTNAME_LIVE" > "$CHROOT/etc/hostname"

for fs in /dev /dev/pts /proc /sys /run; do
  mkdir -p "$CHROOT$fs"; mount --bind "$fs" "$CHROOT$fs"
done
cleanup() {
  for fs in /run /sys /proc /dev/pts /dev; do
    mountpoint -q "$CHROOT$fs" && umount -lf "$CHROOT$fs" || true
  done
}
trap cleanup EXIT

# ----------------------------- 3) provisionar o chroot -----------------------
echo ">> Instalando kernel, casper e pacotes (${PROFILE})..."
# semeia o cache: copia .deb já baixados para dentro do chroot (pula os existentes),
# assim o apt reaproveita e não rebaixa o que já temos.
mkdir -p "$CHROOT/var/cache/apt/archives"
cp -an "$APTCACHE"/*.deb "$CHROOT/var/cache/apt/archives/" 2>/dev/null || true

cat > "$CHROOT/root/setup.sh" <<CHROOT_EOF
#!/bin/bash
set -e
export DEBIAN_FRONTEND=noninteractive
export LANG=C

apt-get update
# firefox- : o deb do firefox é só um stub que faz 'snap install firefox' no
# pós-instalação; o snapd NÃO roda dentro do chroot, então isso falha e derruba
# o build. Excluímos aqui; instale depois no sistema com 'snap install firefox'.
apt-get install -y ${NO_RECO} \
  linux-generic casper initramfs-tools \
  grub-efi-amd64-signed grub-pc-bin shim-signed \
  systemd-resolved \
  locales sudo curl ${EXTRA_PKGS} \
  firefox-

# locale
locale-gen ${LANG_LIVE} || true
update-locale LANG=${LANG_LIVE} || true

# --- alvo de boot / sessão --------------------------------------------------
if [ "${PROFILE}" = "desktop" ]; then
  # Imagem DUAL: o PADRÃO é gráfico (opção 1 do menu). A opção 2 (nogui) força
  # multi-user.target via linha de comando do kernel, sem mexer aqui.
  # 1) padrão = modo gráfico.
  systemctl set-default graphical.target || true
  # 2) gdm3 habilitado como gerenciador de login.
  systemctl enable gdm3.service 2>/dev/null || true
  # 3) desliga o Wayland no gdm: no VMware o driver de vídeo costuma não dar conta
  #    do Wayland e a sessão não sobe. Forçar X11 é bem mais confiável aqui.
  mkdir -p /etc/gdm3
  if [ -f /etc/gdm3/custom.conf ]; then
    sed -i 's/^#\?WaylandEnable=.*/WaylandEnable=false/' /etc/gdm3/custom.conf
    grep -q '^WaylandEnable=false' /etc/gdm3/custom.conf || \
      sed -i '/^\[daemon\]/a WaylandEnable=false' /etc/gdm3/custom.conf
  else
    printf '[daemon]\nWaylandEnable=false\n' > /etc/gdm3/custom.conf
  fi
  # 4) itens do modo nogui (opção 2): SSH ligado e cloud-init só com NoCloud.
  systemctl enable ssh.service 2>/dev/null || true
  mkdir -p /etc/cloud/cloud.cfg.d
  printf 'datasource_list: [ NoCloud, None ]\n' \
    > /etc/cloud/cloud.cfg.d/99_nocloud.cfg
else
  # nogui: console puro, sem gerenciador gráfico.
  systemctl set-default multi-user.target || true
  systemctl enable ssh.service 2>/dev/null || true
  # cloud-init: usar SOMENTE o datasource NoCloud (volume externo rotulado CIDATA)
  # e, na ausência dele, cair em None (= não faz nada, boot rápido pro console).
  mkdir -p /etc/cloud/cloud.cfg.d
  printf 'datasource_list: [ NoCloud, None ]\n' \
    > /etc/cloud/cloud.cfg.d/99_nocloud.cfg
fi

# --- autologin no console (tty1) para a sessão LIVE -------------------------
# O usuário da live é 'ubuntu' (padrão do casper) e NÃO tem senha. No modo
# gráfico o gdm faz autologin; no console (nogui) não havia nada -> ficava preso
# no prompt 'login:'. Este override loga o 'ubuntu' automaticamente no tty1.
# (No modo GUI é inofensivo: o gdm assume o VT.) O install-to-disk.sh remove
# este arquivo do sistema instalado, para o disco não herdar o autologin.
mkdir -p /etc/systemd/system/getty@tty1.service.d
cat > /etc/systemd/system/getty@tty1.service.d/autologin.conf <<'AUTOLOGIN'
[Service]
ExecStart=
ExecStart=-/sbin/agetty --autologin ubuntu --noclear %I $TERM
AUTOLOGIN

# --- não travar o boot esperando a rede ficar "online" ----------------------
# O 'systemd-networkd-wait-online' (e o equivalente do NetworkManager) bloqueiam
# o network-online.target enquanto a NIC não sobe. Como a etapa FINAL do
# cloud-init (onde roda o runcmd da autoinstalação) é ordenada depois desse
# target, uma NIC que demora/não sobe (ex: ens33 sem DHCP) PRENDE a
# autoinstalação. Mascarar faz o network-online.target ser atingido na hora.
# A instalação é local (não precisa de rede), então isto é seguro.
systemctl mask systemd-networkd-wait-online.service 2>/dev/null || true
systemctl mask NetworkManager-wait-online.service 2>/dev/null || true

# regenera o initrd com casper incluso (essencial p/ a live bootar)
update-initramfs -u

# limpeza p/ reduzir o tamanho da imagem (NÃO limpamos o cache de .deb aqui;
# ele é devolvido ao cache persistente do host logo após este script)
rm -rf /tmp/* /var/lib/apt/lists/* /var/tmp/*
rm -f /etc/machine-id /var/lib/dbus/machine-id
# NÃO zerar o resolv.conf: arquivo vazio = sessão live sem DNS
# ("Temporary failure resolving archive.ubuntu.com"). Aponta pro stub do
# systemd-resolved, que é como o Ubuntu resolve nomes no boot.
rm -f /etc/resolv.conf
ln -sf ../run/systemd/resolve/stub-resolv.conf /etc/resolv.conf
systemctl enable systemd-resolved.service 2>/dev/null || true
CHROOT_EOF
chmod +x "$CHROOT/root/setup.sh"
chroot "$CHROOT" /root/setup.sh
rm -f "$CHROOT/root/setup.sh"

# devolve ao cache persistente os .deb novos (pula os já existentes), depois
# esvazia o archive do chroot pra imagem não carregar os pacotes baixados.
cp -an "$CHROOT/var/cache/apt/archives/"*.deb "$APTCACHE/" 2>/dev/null || true
rm -f "$CHROOT/var/cache/apt/archives/"*.deb 2>/dev/null || true

# ----------------------------- 4) extrair kernel/initrd ----------------------
echo ">> Copiando kernel e initrd para a árvore da ISO..."
cp "$CHROOT"/boot/vmlinuz-*    "$IMAGE/casper/vmlinuz"
cp "$CHROOT"/boot/initrd.img-* "$IMAGE/casper/initrd"

# ---- embute o instalador de disco na imagem (instala com 1 comando na live) ----
# Assim o ISO novo já nasce com /usr/local/bin/install-to-disk.sh pronto.
# Heredoc com aspas ('INSTALLER_EOF') => conteúdo gravado literal (preserva os
# heredocs e variáveis internos do instalador).
echo ">> Embutindo /usr/local/bin/install-to-disk.sh na imagem..."
mkdir -p "$CHROOT/usr/local/bin"
cat > "$CHROOT/usr/local/bin/install-to-disk.sh" <<'INSTALLER_EOF'
#!/usr/bin/env bash
# Instala este Ubuntu (da live) no disco, sem calamares. BIOS/MBR.
# Uso:  sudo USERNAME=base USERPASS=base install-to-disk.sh /dev/sda
set -euo pipefail
[ "$(id -u)" -eq 0 ] || { echo "ERRO: rode com sudo."; exit 1; }
DISK="${1:-/dev/sda}"
USERNAME="${USERNAME:-ubuntu}"
USERPASS="${USERPASS:-ubuntu}"
HOSTNAME_INST="${HOSTNAME_INST:-ubuntu}"
TARGET="/mnt/target"
SQUASH=""
for c in /cdrom/casper/filesystem.squashfs /run/live/medium/casper/filesystem.squashfs /isodevice/casper/filesystem.squashfs; do
  [ -f "$c" ] && { SQUASH="$c"; break; }
done
[ -n "$SQUASH" ] || { echo "ERRO: squashfs não encontrado."; exit 1; }
echo "Disco: $DISK  | usuário: $USERNAME  | TUDO no disco será APAGADO."
echo "Ctrl+C em 6s para abortar..."; sleep 6
swapoff -a 2>/dev/null || true
umount -R "$TARGET" 2>/dev/null || true
wipefs -a "$DISK"
parted -s "$DISK" mklabel msdos
parted -s "$DISK" mkpart primary ext4 1MiB 100%
parted -s "$DISK" set 1 boot on
partprobe "$DISK"; sleep 2
PART="$(lsblk -lnpo NAME "$DISK" | sed -n '2p')"
[ -b "$PART" ] || { echo "ERRO: partição não criada."; exit 1; }
mkfs.ext4 -F "$PART"
mkdir -p "$TARGET"; mount "$PART" "$TARGET"
echo ">> Extraindo o sistema (unsquashfs)..."
unsquashfs -f -d "$TARGET" "$SQUASH"
KVER="$(ls "$TARGET/lib/modules" | head -1)"
[ -n "$KVER" ] || { echo "ERRO: /lib/modules vazio."; exit 1; }
mkdir -p "$TARGET/boot"
cp /cdrom/casper/vmlinuz "$TARGET/boot/vmlinuz-$KVER"
ROOT_UUID="$(blkid -s UUID -o value "$PART")"
echo "UUID=$ROOT_UUID  /  ext4  defaults,noatime  0 1" > "$TARGET/etc/fstab"
for fs in /dev /dev/pts /proc /sys /run; do mount --bind "$fs" "$TARGET$fs"; done
cp /etc/resolv.conf "$TARGET/etc/resolv.conf" 2>/dev/null || true
echo "$HOSTNAME_INST" > "$TARGET/etc/hostname"
printf '127.0.0.1\tlocalhost\n127.0.1.1\t%s\n' "$HOSTNAME_INST" > "$TARGET/etc/hosts"
# Modo do sistema instalado: respeita a opção escolhida no menu do GRUB.
# A entrada "nogui" passa 'xinstall=nogui' na linha de comando do kernel; se a
# imagem nem tiver gdm3 (ISO nogui enxuta), também cai em console.
if grep -qw xinstall=nogui /proc/cmdline; then
  INSTALL_MODE=nogui
elif [ -e "$TARGET/usr/sbin/gdm3" ] || [ -e "$TARGET/var/lib/dpkg/info/gdm3.list" ]; then
  INSTALL_MODE=gui
else
  INSTALL_MODE=nogui
fi
echo ">> Modo de instalação: $INSTALL_MODE"
chroot "$TARGET" /bin/bash -e <<CHROOT
export DEBIAN_FRONTEND=noninteractive
apt-get purge -y casper ubiquity ubiquity-frontend-gtk ubiquity-casper calamares 2>/dev/null || true
grub-install --target=i386-pc --recheck "$DISK"
update-initramfs -c -k "$KVER" 2>/dev/null || update-initramfs -u 2>/dev/null || true
update-grub
id "$USERNAME" >/dev/null 2>&1 || useradd -m -s /bin/bash -G sudo,adm,cdrom,dip,plugdev,lpadmin "$USERNAME"
# Define a senha e garante a conta DESTRAVADA na hora da instalacao. O '|| true' e
# o '2>/dev/null || true' sao defensivos: evitam que um aviso/erro do chpasswd ou do
# usermod aborte o bloco sob 'bash -e'. (OBS: o real motivo de o login nascer travado
# NAO era aqui - era o cloud-init re-travando no 1o boot; ver o 'cloud-init.disabled'
# logo abaixo. Mantemos este desbloqueio mesmo assim, por seguranca.)
echo "$USERNAME:$USERPASS" | chpasswd || true
usermod -U "$USERNAME" 2>/dev/null || true
passwd -u "$USERNAME" 2>/dev/null || true
# CAUSA REAL DO LOGIN TRAVADO: o cloud-init, no PRIMEIRO boot do sistema instalado,
# reprocessa o usuario padrao e, com 'lock_passwd: true' (padrao), RE-TRAVA a senha
# (prepende '!' ao mesmo hash). Por isso o shadow vinha limpo do instalador mas
# nascia travado apos o 1o boot. Como este sistema e instalado manualmente e nao
# precisa de cloud-init, desabilitamos ele NO ALVO (so afeta o disco; a live segue
# usando cloud-init normalmente, p/ o fluxo cidata). Assim a conta fica destravada.
touch /etc/cloud/cloud-init.disabled 2>/dev/null || true
# verificacao: se ainda estiver travada ('!' no shadow), avisa alto (nao deveria acontecer).
if grep -q "^$USERNAME:!" /etc/shadow; then
  echo "AVISO: a conta $USERNAME continua TRAVADA apos o desbloqueio!" >&2
fi
rm -f /etc/machine-id /var/lib/dbus/machine-id; systemd-machine-id-setup 2>/dev/null || true
# alvo de boot conforme o modo escolhido no menu (calculado fora do chroot).
if [ "${INSTALL_MODE}" = "nogui" ]; then
  systemctl set-default multi-user.target 2>/dev/null || true
  systemctl enable ssh.service 2>/dev/null || true
else
  systemctl set-default graphical.target 2>/dev/null || true
  systemctl enable gdm3.service 2>/dev/null || true
fi
# limpeza: o instalador e o lembrete só fazem sentido na live; tira do disco.
rm -f /usr/local/bin/install-to-disk.sh
rm -f /etc/skel/LEIA-ME-INSTALAR.txt /etc/skel/Desktop/LEIA-ME-INSTALAR.txt 2>/dev/null || true
rm -f "/etc/skel/Área de Trabalho/LEIA-ME-INSTALAR.txt" 2>/dev/null || true
rm -f "/home/$USERNAME/LEIA-ME-INSTALAR.txt" "/home/$USERNAME/Desktop/LEIA-ME-INSTALAR.txt" 2>/dev/null || true
rm -f "/home/$USERNAME/Área de Trabalho/LEIA-ME-INSTALAR.txt" 2>/dev/null || true
rmdir "/home/$USERNAME/Desktop" "/home/$USERNAME/Área de Trabalho" 2>/dev/null || true
# o autologin do tty1 era só p/ a live (usuario 'ubuntu', que nao existe aqui);
# remove para o sistema instalado nao ficar tentando autologar um usuario ausente.
rm -f /etc/systemd/system/getty@tty1.service.d/autologin.conf 2>/dev/null || true
rmdir /etc/systemd/system/getty@tty1.service.d 2>/dev/null || true
CHROOT
sync
for fs in /run /sys /proc /dev/pts /dev; do umount -lf "$TARGET$fs" 2>/dev/null || true; done
umount -R "$TARGET" 2>/dev/null || true
echo ">> CONCLUÍDO. Desligue, remova a ISO do CD e ligue. Login: $USERNAME"
INSTALLER_EOF
chmod +x "$CHROOT/usr/local/bin/install-to-disk.sh"

# ---- lembrete na sessão live: como instalar no disco ----
# Vai no /etc/skel, então aparece na home do usuário da live (e em qualquer
# usuário novo). É só informativo — NÃO é um lançador clicável (seria perigoso
# um clique disparar formatação). A instalação remove este arquivo do disco.
# Colocamos na raiz da home (sempre visível no Arquivos) e, best-effort, numa
# pasta Desktop (no pt_BR o GNOME pode renomear p/ "Área de Trabalho").
echo ">> Criando lembrete LEIA-ME-INSTALAR.txt no /etc/skel..."
mkdir -p "$CHROOT/etc/skel/Desktop"
cat > "$CHROOT/etc/skel/LEIA-ME-INSTALAR.txt" <<'README_EOF'
=== Instalar este Ubuntu no disco ===

Esta e uma imagem LIVE (esta rodando da ISO, ainda nao instalada).

Para instalar no disco, abra um terminal e rode:

    sudo install-to-disk.sh /dev/sda

Opcional: defina usuario e senha (padrao = ubuntu / ubuntu):

    sudo USERNAME=meuuser USERPASS=minhasenha install-to-disk.sh /dev/sda

ATENCAO: isso APAGA TODO o disco indicado (/dev/sda). Confira qual e o disco
com 'lsblk' ANTES de rodar. Apos o comando voce tem 6 segundos para abortar
com Ctrl+C.

Alternativa grafica: o icone "Instalar Ubuntu" (ubiquity) na area de trabalho.

(Este arquivo so existe na live; ele e removido automaticamente do sistema
 ja instalado.)
README_EOF
cp "$CHROOT/etc/skel/LEIA-ME-INSTALAR.txt" \
   "$CHROOT/etc/skel/Desktop/LEIA-ME-INSTALAR.txt"
# No GNOME, os ícones da mesa vêm da pasta de área de trabalho LOCALIZADA.
# Em pt o nome é "Área de Trabalho" (não "Desktop"); então copiamos também p/ lá,
# senão o ícone não aparece na sessão gráfica em português.
case "$LANG_LIVE" in
  pt*)
    mkdir -p "$CHROOT/etc/skel/Área de Trabalho"
    cp "$CHROOT/etc/skel/LEIA-ME-INSTALAR.txt" \
       "$CHROOT/etc/skel/Área de Trabalho/LEIA-ME-INSTALAR.txt"
    ;;
esac

# ---- apply-network.sh na home (~) via /etc/skel -----------------------------
# Aplica a config de rede do cidata (netplan) num sistema já rodando, sem
# reinstalar. Fica em ~/apply-network.sh tanto na live quanto no instalado.
echo ">> Colocando apply-network.sh em /etc/skel (vai pra ~)..."
cat > "$CHROOT/etc/skel/apply-network.sh" <<'APPLYNET'
#!/usr/bin/env bash
# Aplica a rede em 2 placas (nomes do QEMU): ens3 -> DHCP ; ens4 -> IPv6 fixo.
#   sudo ~/apply-network.sh        (ja vem executavel, nao precisa de chmod)
# Nomes FIXOS (ens3/ens4). Trocar pontualmente:
#   sudo IF_DHCP=ens3 IF_STATIC=ens4 STATIC_ADDR="f100::200/64" ./apply-network.sh
set -euo pipefail
[ "$(id -u)" -eq 0 ] || { echo "ERRO: rode com sudo."; exit 1; }

IF_DHCP="${IF_DHCP:-ens3}"
IF_STATIC="${IF_STATIC:-ens4}"
STATIC_ADDR="${STATIC_ADDR:-f100::200/64}"

if systemctl is-active --quiet NetworkManager; then
  RENDERER="NetworkManager"
elif systemctl is-active --quiet systemd-networkd; then
  RENDERER="networkd"
else
  RENDERER="networkd"; systemctl enable --now systemd-networkd 2>/dev/null || true
fi

echo ">> Renderer  : $RENDERER"
echo ">> DHCP      : $IF_DHCP"
echo ">> IPv6 fixo : $IF_STATIC -> $STATIC_ADDR"

NETPLAN="/etc/netplan/99-cidata-net.yaml"
cat > "$NETPLAN" <<YAML
network:
  version: 2
  renderer: ${RENDERER}
  ethernets:
    ${IF_DHCP}:
      dhcp4: true
      dhcp6: true
      optional: true
    ${IF_STATIC}:
      addresses:
        - "${STATIC_ADDR}"
      optional: true
YAML
chmod 600 "$NETPLAN"

echo ">> Validando e aplicando o netplan ..."
netplan generate
netplan apply
sleep 3

echo
echo ">> Rede atual:"
ip -brief address
echo
echo "OK. Se uma placa nao pegou, confira com 'ip -brief link' (assumi ens3/ens4)."
APPLYNET
chmod +x "$CHROOT/etc/skel/apply-network.sh"

cleanup; trap - EXIT   # desmonta antes de empacotar o squashfs

# ----------------------------- 5) squashfs -----------------------------------
echo ">> Gerando filesystem.squashfs (pode demorar)..."
mksquashfs "$CHROOT" "$IMAGE/casper/filesystem.squashfs" \
  -noappend -comp xz -e boot
printf '%s' "$(du -sx --block-size=1 "$CHROOT" | cut -f1)" > "$IMAGE/casper/filesystem.size"

# filesystem.manifest: lista TUDO que está instalado (o ubiquity usa p/ a cópia).
# Sem isto a instalação aborta no comecinho. dpkg-query lê a base do dpkg do chroot.
chroot "$CHROOT" dpkg-query -W --showformat='${Package} ${Version}\n' \
  > "$IMAGE/casper/filesystem.manifest"

# *.manifest-remove: pacotes só-da-live que o instalador REMOVE do sistema final
# (casper, o próprio ubiquity, etc.). Gera as duas variantes que o instalador procura.
cat > "$IMAGE/casper/filesystem.manifest-remove" <<'REMOVE'
casper
ubiquity
ubiquity-frontend-gtk
ubiquity-casper
REMOVE
cp "$IMAGE/casper/filesystem.manifest-remove" \
   "$IMAGE/casper/filesystem.manifest-minimal-remove"

echo "Ubuntu ${VERSION} (${SUITE}) - custom build $(date +%Y%m%d)" > "$IMAGE/.disk/info"

# ----------------------------- 6) bootloaders --------------------------------
echo ">> Escrevendo configuração do GRUB (BIOS + UEFI)..."
# Cabeçalho comum
cat > "$IMAGE/boot/grub/grub.cfg" <<EOF
set timeout=10
set default=0

# Aponta o \$root para o volume que realmente contém o kernel (o ISO9660).
# Sem isto, ao bootar pela partição EFI o GRUB procura /casper na partição
# errada e dá "error: file '/casper/vmlinuz' not found".
search --no-floppy --set=root --file /casper/vmlinuz

EOF

if [ "$PROFILE" = "desktop" ]; then
  # Imagem DUAL: opção 1 sobe a GUI; opção 2 (nogui) força multi-user.target
  # e marca 'xinstall=nogui' (o instalador lê isso p/ instalar em modo console).
  cat >> "$IMAGE/boot/grub/grub.cfg" <<EOF
menuentry "Ubuntu ${VERSION} (${SUITE}) - Live GUI / Instalar GUI" {
    linux /casper/vmlinuz boot=casper ---
    initrd /casper/initrd
}
menuentry "Ubuntu ${VERSION} (${SUITE}) - Live nogui / Instalar nogui" {
    linux /casper/vmlinuz boot=casper systemd.unit=multi-user.target xinstall=nogui ---
    initrd /casper/initrd
}
EOF
else
  # Imagem nogui enxuta: console direto (sem entrada de GUI, que não existe nela).
  cat >> "$IMAGE/boot/grub/grub.cfg" <<EOF
menuentry "Ubuntu ${VERSION} (${SUITE}) - Live nogui / Instalar nogui" {
    linux /casper/vmlinuz boot=casper ---
    initrd /casper/initrd
}
EOF
fi

# imagem EFI (FAT) com grub embarcado
echo ">> Montando bootloader UEFI..."
grub-mkstandalone \
  --format=x86_64-efi \
  --output="$WORK/bootx64.efi" \
  --modules="part_gpt part_msdos fat iso9660 normal linux search search_fs_file search_label echo all_video" \
  --locales="" --fonts="" \
  "boot/grub/grub.cfg=$IMAGE/boot/grub/grub.cfg"

( cd "$WORK"
  dd if=/dev/zero of=efiboot.img bs=1M count=10
  mkfs.vfat efiboot.img
  mmd -i efiboot.img ::/EFI ::/EFI/BOOT
  mcopy -i efiboot.img "$WORK/bootx64.efi" ::/EFI/BOOT/BOOTX64.EFI
)
cp "$WORK/efiboot.img" "$IMAGE/EFI/boot/efiboot.img"
cp "$WORK/bootx64.efi" "$IMAGE/EFI/boot/BOOTX64.EFI"

# imagem El Torito p/ BIOS legado
grub-mkstandalone \
  --format=i386-pc \
  --output="$WORK/core.img" \
  --install-modules="linux normal iso9660 biosdisk memdisk search search_fs_file search_label tar ls" \
  --modules="linux normal iso9660 biosdisk search search_fs_file" \
  --locales="" --fonts="" \
  "boot/grub/grub.cfg=$IMAGE/boot/grub/grub.cfg"
cat /usr/lib/grub/i386-pc/cdboot.img "$WORK/core.img" > "$WORK/bios.img"

# ----------------------------- 7) montar a ISO híbrida -----------------------
echo ">> Gerando a ISO híbrida (BIOS + UEFI)..."
xorriso -as mkisofs \
  -iso-level 3 -full-iso9660-filenames \
  -volid "Ubuntu-${VERSION}-amd64" \
  -eltorito-boot boot/grub/bios.img \
    -no-emul-boot -boot-load-size 4 -boot-info-table \
    --eltorito-catalog boot/grub/boot.cat \
  --grub2-boot-info --grub2-mbr /usr/lib/grub/i386-pc/boot_hybrid.img \
  -eltorito-alt-boot \
    -e EFI/boot/efiboot.img -no-emul-boot \
    -isohybrid-gpt-basdat \
  -append_partition 2 0xef "$IMAGE/EFI/boot/efiboot.img" \
  -output "$OUT" \
  -graft-points \
    "$IMAGE" \
    "/boot/grub/bios.img=$WORK/bios.img" \
    "/boot/grub/efi.img=$WORK/efiboot.img"

# ----------------------------- 8) checksum -----------------------------------
echo ">> Pronto!"
sha256sum "$OUT"
echo
echo "ISO gerada em: $OUT"
echo "No VMware: crie a VM com firmware UEFI, >=25 GB de disco, >=4 GB RAM (6 GB recomendado),"
echo "aponte o CD/DVD para essa ISO e dê boot."