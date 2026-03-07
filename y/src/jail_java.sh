#!/bin/bash

# bash <(curl -s https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/jail_java.sh) desmonta
# bash <(curl -s https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/jail_java.sh)

set -euo pipefail

CHROOT=/jaula
JAVA_SRC=/usr/lib/jvm/java-21-openjdk-amd64

usage() {
    echo "Uso: sudo bash jail_java.sh [comando]"
    echo ""
    echo "  (sem argumento)   Cria e entra na jaula"
    echo "  desmonta          Desmonta tudo e remove a jaula"
    echo ""
}

cleanup() {
    # CRÍTICO: desativar o trap ERR imediatamente para evitar recursão infinita.
    # Sem isso: falha em qualquer comando aqui → ERR dispara → cleanup() de novo → loop.
    trap - ERR INT TERM

    echo "[*] Desmontando..."
    # /dev/pts é um devpts próprio (não bind do host) — seguro desmontar
    umount "$CHROOT/dev/pts" 2>/dev/null || \
        umount -f "$CHROOT/dev/pts" 2>/dev/null || true
    # /dev NÃO é mais bind-mounted: só contém device nodes criados por mknod
    # (nada a desmontar para /dev)
    umount "$CHROOT/usr/lib/jvm/java-21-openjdk-amd64" 2>/dev/null || \
        umount -f "$CHROOT/usr/lib/jvm/java-21-openjdk-amd64" 2>/dev/null || true
    umount "$CHROOT/sys"  2>/dev/null || \
        umount -f "$CHROOT/sys"  2>/dev/null || true
    umount "$CHROOT/proc" 2>/dev/null || \
        umount -f "$CHROOT/proc" 2>/dev/null || true

    # Segurança: verificar o campo mountpoint com precisão (não substring genérica).
    # grep -q "$CHROOT" /proc/mounts daria match em "/jaulaextra" — falso positivo.
    if awk '{print $2}' /proc/mounts | grep -q "^${CHROOT}"; then
        echo "[!] ERRO: ainda há mounts ativos em $CHROOT, abortando remoção:"
        awk '{print $2}' /proc/mounts | grep "^${CHROOT}"
        exit 1
    fi

    echo "[*] Removendo $CHROOT..."
    # "${CHROOT:?}" aborta se a variável estiver vazia ou não definida.
    # Sem isso: rm -rf $CHROOT com CHROOT="" → rm -rf / → catástrofe.
    rm -rf "${CHROOT:?variavel CHROOT nao pode ser vazia}"
    echo "[*] Feito."
}

if [[ ${1:-} == "--help" || ${1:-} == "-h" ]]; then
    usage; exit 0
fi

if [[ ${1:-} == "desmonta" ]]; then
    cleanup
    exit 0
fi

# Valida dependências antes de qualquer operação destrutiva
if [[ ! -d "$JAVA_SRC" ]]; then
    echo "[!] ERRO: JDK não encontrado em $JAVA_SRC"
    echo "    Instale com: sudo apt install openjdk-21-jdk"
    exit 1
fi
if [[ ! -x "$JAVA_SRC/bin/java" ]]; then
    echo "[!] ERRO: $JAVA_SRC/bin/java não encontrado ou não executável"
    exit 1
fi

# Garante limpeza dos mounts se o script falhar ou for interrompido
trap 'echo "[!] Interrompido, limpando mounts..."; cleanup' ERR INT TERM

echo "[*] Criando estrutura de diretórios..."
mkdir -p "$CHROOT"/{proc,sys,dev,dev/pts,tmp,root}
mkdir -p "$CHROOT/usr/lib/jvm/java-21-openjdk-amd64"
mkdir -p "$CHROOT"/{lib,lib64,usr/lib,usr/bin,bin}

# Copia as libs de um binário automaticamente
copy_libs() {
    local bin=$1
    ldd "$bin" 2>/dev/null | grep -oP '(/[^ ]+\.so[^ ]*)' | while read -r lib; do
        local dest="$CHROOT$(dirname "$lib")"
        mkdir -p "$dest"
        cp -n "$lib" "$dest/" 2>/dev/null || true
    done
}

echo "[*] Copiando shell e utilitários básicos..."
cp /bin/bash "$CHROOT/bin/bash"
copy_libs /bin/bash
for util in cat ls cp mv rm mkdir touch; do
    bin=$(type -P "$util" 2>/dev/null) || continue
    cp "$bin" "$CHROOT/bin/"
    copy_libs "$bin"
done

echo "[*] Montando sistemas de arquivos virtuais..."
# Montar instâncias novas de proc e sysfs — NÃO bind mounts do host.
# Bind mount exporia o /proc e /sys reais; instâncias novas são isoladas.
mount -t proc  proc  "$CHROOT/proc"
mount -t sysfs sysfs "$CHROOT/sys"

echo "[*] Criando device nodes mínimos (sem bind mount do /dev do host)..."
# Bind mount de /dev expõe o /dev real e, se o unmount falhar, rm -rf apaga
# arquivos do host. Usar mknod isola completamente a jaula.
mknod -m 666 "$CHROOT/dev/null"    c 1 3
mknod -m 666 "$CHROOT/dev/zero"    c 1 5
mknod -m 666 "$CHROOT/dev/random"  c 1 8
mknod -m 666 "$CHROOT/dev/urandom" c 1 9
mknod -m 666 "$CHROOT/dev/tty"     c 5 0
# devpts próprio da jaula para suporte a terminal (não compartilhado com host)
mount -t devpts devpts "$CHROOT/dev/pts" -o newinstance,ptmxmode=0666
mknod -m 666 "$CHROOT/dev/ptmx" c 5 2

echo "[*] Copiando libs do sistema (necessárias para o Java)..."
# Copia em vez de bind mount para não arriscar deletar arquivos do host
copy_libs /usr/lib/jvm/java-21-openjdk-amd64/bin/java
copy_libs /usr/lib/jvm/java-21-openjdk-amd64/bin/javac
copy_libs /usr/lib/jvm/java-21-openjdk-amd64/lib/server/libjvm.so

echo "[*] Montando JDK (read-only)..."
mount --bind "$JAVA_SRC" "$CHROOT/usr/lib/jvm/java-21-openjdk-amd64"
mount -o remount,bind,ro "$CHROOT/usr/lib/jvm/java-21-openjdk-amd64"

echo "[*] Configurando /etc mínimo..."
mkdir -p "$CHROOT/etc"
echo "root:x:0:0:root:/root:/bin/bash" > "$CHROOT/etc/passwd"
echo "root:x:0:"                        > "$CHROOT/etc/group"
cp /etc/nsswitch.conf "$CHROOT/etc/"    2>/dev/null || true
cp /etc/ld.so.cache   "$CHROOT/etc/"    2>/dev/null || true
cp /etc/ld.so.conf    "$CHROOT/etc/"    2>/dev/null || true
# Configuração de segurança do Java (Ubuntu separa do JDK em /etc/java-21-openjdk/)
if [[ -d /etc/java-21-openjdk ]]; then
    cp -r /etc/java-21-openjdk "$CHROOT/etc/"
fi

echo "[*] Criando Hello.java de teste..."
cat > "$CHROOT/tmp/Hello.java" << 'EOF'
public class Hello {
  public static void main(String[] args) {
      System.out.println("Hello, World!");
  }
}
EOF

echo "[*] Entrando na jaula..."
# Desativa o trap de erro durante o chroot (saída normal da shell não é erro)
trap - ERR INT TERM
chroot "$CHROOT" /bin/bash --login -c "
    export PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:\$PATH
    echo 'Java version:'
    java -version
    echo '[*] Testando compilação Java...'
    javac /tmp/Hello.java -d /tmp
    java -cp /tmp Hello
    exec /bin/bash
"

# Limpa após sair da jaula
cleanup