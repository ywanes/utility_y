# curl -fsSL "https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/config_tap0.sh?$(date +%s)" | bash
# =====================================================================================
# usuario base
# rodar com o usuario base (outro usuario da erro)
# ssh configurado pronto para receber uma conexao de sshgen senha init, tap0 f100::200, ping crontab f100::100
#                 e teclado ABNT2, fuso de Sao Paulo, porta 22 no ufw

main() {
  set -eu

  # valores padrao
  IFACE="${IFACE:-ens4}"
  IP_VM="${IP_VM:-f100::200/64}"
  IP_WIN="${IP_WIN:-f100::100}"
  USUARIO="base"
  # fonte da chave (cria id_ed25519_local, a privada, e id_ed25519_local.pub, a publica):
  #   y sshgen ed25519 id_ed25519_local -pass "init" -C "base@local"
  # aqui entra so o conteudo do id_ed25519_local.pub; a privada fica no Windows e nunca vem para ca
  # conectando pelo Windows, com a porta 2222 do host encaminhada para a 22 da VM (pede a senha da chave, "init"):
  #   ssh -i id_ed25519_local -p 2222 base@127.0.0.1
  # sem a chave, com a senha do usuario (base):
  #   ssh -p 2222 base@127.0.0.1
  CHAVE="ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPPfeMieTMaASakrABuk61juO3YKLmC3EFKi0Xy3u0co base@local"

  msg() { echo "==> $*"; }

  # so roda no usuario base (com sudo, vale quem chamou o sudo, nao o root)
  CORRENTE="${SUDO_USER:-$(id -un)}"
  if [ "$CORRENTE" != "$USUARIO" ]; then
    echo "ERRO: rode como o usuario $USUARIO (usuario corrente: $CORRENTE); nada foi alterado" >&2
    exit 1
  fi

  # root roda direto; outro usuario usa sudo (pede a senha uma vez, pelo terminal)
  if [ "$(id -u)" -eq 0 ]; then S=""; else S="sudo"; $S -v; fi

  # instala so os pacotes que faltam
  instala() {
    falta=""
    for p in "$@"; do dpkg -s "$p" >/dev/null 2>&1 || falta="$falta $p"; done
    [ -z "$falta" ] && return 0
    msg "instalando:$falta"
    $S apt-get update -qq </dev/null
    # shellcheck disable=SC2086
    $S env DEBIAN_FRONTEND=noninteractive apt-get install -y -qq $falta </dev/null
  }

  # ---------------------------------------------------------------------------------
  # teclado ABNT2 (console e X11; no GNOME cada usuario ainda escolhe o seu em Configuracoes)
  msg "teclado ABNT2"
  $S localectl set-x11-keymap br abnt2 >/dev/null 2>&1 || true
  if [ -f /etc/default/keyboard ]; then
    $S sed -i 's/^XKBMODEL=.*/XKBMODEL="abnt2"/; s/^XKBLAYOUT=.*/XKBLAYOUT="br"/; s/^XKBVARIANT=.*/XKBVARIANT=""/' /etc/default/keyboard
  fi
  if command -v setupcon >/dev/null 2>&1; then $S setupcon -k --force </dev/null >/dev/null 2>&1 || true; fi

  # ---------------------------------------------------------------------------------
  # fuso de Sao Paulo
  msg "fuso America/Sao_Paulo"
  $S timedatectl set-timezone America/Sao_Paulo 2>/dev/null \
    || $S ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime

  # ---------------------------------------------------------------------------------
  # SSH instalado e ligado, aceitando senha e a chave
  # o arquivo 00-* e lido antes dos outros (ex: 50-cloud-init.conf) e no sshd vale o primeiro valor
  msg "SSH: servidor, senha e chave"
  instala openssh-server
  echo "PasswordAuthentication yes" | $S tee /etc/ssh/sshd_config.d/00-tap0.conf >/dev/null
  $S sshd -t
  if systemctl is-enabled ssh.socket >/dev/null 2>&1; then
    # Ubuntu 24.04+: o SSH sobe pelo socket
    $S systemctl daemon-reload
    $S systemctl restart ssh.socket
  else
    $S systemctl enable --now ssh >/dev/null 2>&1 || true
    $S systemctl restart ssh
  fi

  # chave publica em ~base/.ssh/authorized_keys (a privada fica no Windows)
  HOME_U="$(getent passwd "$USUARIO" | cut -d: -f6)"
  GRUPO_U="$(id -gn "$USUARIO")"
  AK="$HOME_U/.ssh/authorized_keys"
  $S install -d -m 700 -o "$USUARIO" -g "$GRUPO_U" "$HOME_U/.ssh"
  $S touch "$AK"
  if ! $S grep -qxF "$CHAVE" "$AK"; then
    echo "$CHAVE" | $S tee -a "$AK" >/dev/null
  fi
  $S chown "$USUARIO:$GRUPO_U" "$AK"
  $S chmod 600 "$AK"

  # ---------------------------------------------------------------------------------
  # IPv6 fixo na 2a placa (a ligada no tap0 do Windows); o Windows fica em IP_WIN
  #   renderer: NetworkManager se o sistema tiver (Desktop), senao networkd (Server)
  #   optional: sem essa placa o boot nao fica esperando por ela
  if command -v netplan >/dev/null 2>&1; then
    msg "rede: $IP_VM em $IFACE"
    R=networkd
    [ -x /usr/sbin/NetworkManager ] && R=NetworkManager
    F=/etc/netplan/60-tap0.yaml
    $S tee "$F" >/dev/null <<EOF
network:
  version: 2
  renderer: $R
  ethernets:
    $IFACE:
      dhcp4: false
      dhcp6: false
      accept-ra: false
      addresses:
        - $IP_VM
      optional: true
EOF
    $S chmod 600 "$F"
    if ip link show "$IFACE" >/dev/null 2>&1; then
      $S netplan apply || msg "AVISO: netplan apply falhou; confira com: sudo netplan try"
    else
      msg "AVISO: placa $IFACE nao existe (confira com: ip -br link); arquivo gravado, vale quando ela aparecer"
      $S netplan generate || true
    fi
  else
    msg "AVISO: netplan nao encontrado; IPv6 da $IFACE nao configurado"
  fi

  # ---------------------------------------------------------------------------------
  # ping ao Windows pela 2a placa a cada 30 min
  msg "cron: ping $IP_WIN a cada 30 min"
  instala cron iputils-ping
  echo "*/30 * * * * root ping -6 -c 1 $IP_WIN >/dev/null 2>&1" | $S tee /etc/cron.d/ping-tap0 >/dev/null
  $S chmod 644 /etc/cron.d/ping-tap0

  # ---------------------------------------------------------------------------------
  # libera a porta 22 no ufw (se estiver desligado, a regra so vale ao ativar com: sudo ufw enable)
  msg "ufw: porta 22/tcp"
  instala ufw
  $S ufw allow 22/tcp >/dev/null

  # ---------------------------------------------------------------------------------
  echo
  msg "pronto"
  echo "    usuario da chave: $USUARIO"
  echo "    IPv6: $IP_VM em $IFACE (Windows em $IP_WIN)"
  echo "    teste no Windows: ssh -i id_ed25519_local -p 2222 base@127.0.0.1"
}

main "$@"