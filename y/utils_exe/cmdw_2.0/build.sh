#!/bin/bash
# build.sh - Gera cmdw.exe (com UAC) e cmdw-admin.exe (sem UAC)
#
# Requer: mingw-w64
#   sudo apt install mingw-w64
#
# Uso: bash build.sh

set -e

CC=x86_64-w64-mingw32-gcc
RC=x86_64-w64-mingw32-windres
SRC=cmdw.c

# ── 1. cmdw.exe padrão (UAC normal) ──────────────────────────────────────────
echo "[1/2] Compilando cmdw.exe (modo padrão)..."
$CC -o cmdw.exe "$SRC" -lshell32 -static-libgcc
echo "      OK → cmdw.exe"

# ── 2. cmdw-admin.exe (requireAdministrator — UAC apenas na 1ª abertura) ─────
echo "[2/2] Compilando cmdw-admin.exe (sem UAC após 1ª abertura)..."

MANIFEST=$(mktemp /tmp/cmdw_XXXXXX.manifest)
RC_FILE=$(mktemp /tmp/cmdw_XXXXXX.rc)
OBJ_FILE=$(mktemp /tmp/cmdw_XXXXXX.o)

# Manifest que pede elevação automática ao Windows
cat > "$MANIFEST" << 'MANIFEST_EOF'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<assembly xmlns="urn:schemas-microsoft-com:asm.v1" manifestVersion="1.0">
  <trustInfo xmlns="urn:schemas-microsoft-com:asm.v3">
    <security>
      <requestedPrivileges>
        <requestedExecutionLevel level="requireAdministrator" uiAccess="false"/>
      </requestedPrivileges>
    </security>
  </trustInfo>
</assembly>
MANIFEST_EOF

# RC que embute o manifest como recurso RT_MANIFEST (ID 1)
echo "1 RT_MANIFEST \"$MANIFEST\"" > "$RC_FILE"

# Compila o recurso
$RC "$RC_FILE" "$OBJ_FILE"

# Linka tudo em um único exe
$CC -o cmdw-admin.exe "$SRC" "$OBJ_FILE" -lshell32 -static-libgcc

# Limpa temporários
rm -f "$MANIFEST" "$RC_FILE" "$OBJ_FILE"

echo "      OK → cmdw-admin.exe"
echo ""
echo "Pronto!"
echo "  cmdw.exe        → uso geral (pede UAC quando /admin)"
echo "  cmdw-admin.exe  → abre com UAC UMA VEZ, depois /admin sem confirmação"