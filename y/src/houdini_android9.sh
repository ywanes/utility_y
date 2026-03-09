cat << 'EOF' > /sdcard/install_h.sh
#!/system/bin/sh
# nenhuma linha pode passar de 55 digitos
# deixa arm 32bits compativel
SRC_Y="http://dl.android-x86.org/houdini/9_y/houdini.sfs"
SRC_Z="https://archive.org/download/androidx86-houdini"
SRC_Z="${SRC_Z}/Houdini%20%5BPie%5D%20%5B9.0%5D"
SRC_Z="${SRC_Z}/houdini9.zip"

dl() {
rm -f "$2"
if command -v curl >/dev/null 2>&1; then
  curl -fLo "$2" "$1" && return 0
fi
wget -O "$2" "$1" 2>/dev/null && return 0
rm -f "$2"
return 1
}

echo "=== Instalador Houdini Android 9 ==="
mount -o remount,rw /system 2>/dev/null
echo "[OK] /system montado como rw"
mkdir -p /data/arm
cd /data/arm

echo "[1/4] Baixando houdini9_y.sfs..."
dl "$SRC_Y" houdini9_y.sfs || {
echo "[ERRO] Falha no download y"
exit 1
}
cp houdini9_y.sfs /system/etc/houdini9_y.sfs
chmod 644 /system/etc/houdini9_y.sfs
echo "[OK] houdini9_y.sfs instalado"

ARCH=$(uname -m)
if [ "$ARCH" = "x86_64" ]; then
echo "[2/4] Baixando houdini9.zip..."
if dl "$SRC_Z" houdini9.zip; then
  unzip -o houdini9.zip houdini9_z.sfs
  if [ -f houdini9_z.sfs ]; then
	cp houdini9_z.sfs \
	  /system/etc/houdini9_z.sfs
	chmod 644 /system/etc/houdini9_z.sfs
	echo "[OK] houdini9_z.sfs instalado"
  else
	echo "[AVISO] Falha ao extrair z"
  fi
else
  echo "[AVISO] ARM64 falhou"
  echo "[AVISO] Precisa de curl p/ HTTPS"
fi
else
echo "[2/4] Pulando ARM64 (arch: $ARCH)"
fi

echo "[3/4] Ativando native bridge..."
setprop persist.sys.nativebridge 1
if [ -f /system/bin/enable_nativebridge ]; then
/system/bin/enable_nativebridge
echo "[OK] enable_nativebridge executado"
else
D32=/system/lib/arm
D64=/system/lib64/arm64
BF=/proc/sys/fs/binfmt_misc
H32="\x7f\x45\x4c\x46\x01\x01\x01\x00"
H32="${H32}\x00\x00\x00\x00\x00\x00\x00\x00"
H64="\x7f\x45\x4c\x46\x02\x01\x01\x00"
H64="${H64}\x00\x00\x00\x00\x00\x00\x00\x00"
mkdir -p $D32
mount /system/etc/houdini9_y.sfs $D32
S=$D32/libhoudini.so
T=/system/lib/libhoudini.so
mount --bind $S $T 2>/dev/null
if [ -f /system/etc/houdini9_z.sfs ]; then
  mkdir -p $D64
  mount /system/etc/houdini9_z.sfs $D64
  S=$D64/libhoudini.so
  T=/system/lib64/libhoudini.so
  mount --bind $S $T 2>/dev/null
fi
if [ ! -e $BF/register ]; then
  mount -t binfmt_misc none $BF
fi
if [ -e $BF/register ]; then
  R=":arm_exe:M::${H32}\x02\x00\x28"
  echo "${R}::${D32}/houdini:P" > $BF/register
  R=":arm_dyn:M::${H32}\x03\x00\x28"
  echo "${R}::${D32}/houdini:P" > $BF/register
  if [ -f /system/etc/houdini9_z.sfs ]; then
	R=":arm64_exe:M::${H64}\x02\x00\xb7"
	echo "${R}::${D64}/houdini64:P" > $BF/register
	R=":arm64_dyn:M::${H64}\x03\x00\xb7"
	echo "${R}::${D64}/houdini64:P" > $BF/register
  fi
  echo "[OK] binfmt_misc registrado"
fi
setprop ro.dalvik.vm.isa.arm x86
setprop ro.enable.native.bridge.exec 1
setprop ro.dalvik.vm.native.bridge libhoudini.so
if [ -f /system/etc/houdini9_z.sfs ]; then
  setprop ro.dalvik.vm.isa.arm64 x86_64
  setprop ro.enable.native.bridge.exec64 1
fi
echo "[OK] Native bridge ativado manualmente"
fi
rm -f /data/arm/houdini9_y.sfs
rm -f /data/arm/houdini9_z.sfs
rm -f /data/arm/houdini9.zip
echo "[4/4] PRONTO! Reinicie: reboot"
EOF

sh /sdcard/install_h.sh
