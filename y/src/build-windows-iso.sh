#!/usr/bin/env bash
#
#   sudo bash -c 'bash <(curl -fsSL https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/build-windows-iso.sh)'
#   sudo bash -c 'bash <(curl -fsSL https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/build-windows-iso.sh) RC'
#   sudo bash -c 'bash <(curl -fsSL https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/build-windows-iso.sh) DEV'
#   sudo bash -c 'bash <(curl -fsSL https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/build-windows-iso.sh) list'
#   ou
#   chmod +x build-windows-iso.sh
#   sudo ./build-windows-iso.sh                 # canal oficial (24H2 estável), Pro pt-BR
#   sudo ./build-windows-iso.sh RC              # canal Release Preview (25H2)
#   sudo ./build-windows-iso.sh DEV             # canal Dev/Insider (build mais novo, 26H1+)
#   sudo ./build-windows-iso.sh list            # só mostra o build do canal e sai
#   sudo ./build-windows-iso.sh <uuid>          # força um id específico do uupdump
#
#   baixando o chrome offline
#   curl.exe -L -o chrome_offline.msi "https://dl.google.com/dl/chrome/install/googlechromestandaloneenterprise64.msi"
#
# Builder de ISO do Windows 11 (Pro, pt-BR) rodando 100% no Linux.
# NÃO baixa uma ISO pronta: o uupdump entrega um PACOTE DE SCRIPTS que puxa os
# pacotes UUP direto dos servidores da Microsoft e CONVERTE em ISO localmente
# (aria2c + wimlib + cabextract + chntpw + genisoimage). Este script é um wrapper:
#   1) descobre o build mais novo via API do uupdump (igual o discover_suite faz no Ubuntu)
#   2) baixa o pacote de conversão daquele build, já filtrado p/ Pro + pt-BR
#   3) instala as dependências de conversão no host
#   4) dispara o conversor oficial e coleta a ISO
#
# Variáveis: EDITION=Professional  LANG_WIN=pt-br  ARCH=amd64
#            APPS=0|1 (1 = inclui apps da Store; 0 = sem apps, ISO menor)
#            CHROME=0|1 (1 = Google Chrome instala no 1º boot, padrão 1)
#            WINGET=0|1 (1 = winget/App Installer provisionado no 1º boot, padrão 1)
#            WORK=/caminho   OUT=/caminho.iso
#
# Chrome + winget entram OFFLINE dentro da install.wim e são instalados no PRIMEIRO
# boot por um SetupComplete.cmd (roda como SYSTEM no fim do Setup, antes do logon,
# SEM precisar de rede). Chrome via msiexec; winget provisionado via DISM.
# Log no Windows: C:\Windows\Setup\Scripts\buildiso-postinstall.log
#
# obs: requer ~25 GB livres em $WORK (pacotes UUP + WIM + ISO). O download
#      vem da Microsoft; a velocidade depende da sua rede, não do uupdump.
# status de validação (2026-06):
#   - api.uupdump.net listid/listlangs/listeditions: testado, 200 OK
#   - uupdump.net/get.php?...&autodl=2: testado, devolve o ZIP de conversão
#   - uup_download_linux.sh em si (download MS + conversão p/ ISO): NÃO testado
#     ponta a ponta — depende da rede do host de build (servidores da Microsoft)
set -euo pipefail

# Diretório de invocação ANTES de qualquer cd — usado p/ resolver OUT no final.
ORIG_PWD="$(pwd)"

# ----------------------------- configuração ---------------------------------
ARCH="${ARCH:-amd64}"
EDITION="${EDITION:-PROFESSIONAL}"     # MAIÚSCULAS! o get.php exige o nome exato do editionList (PROFESSIONAL/CORE)
LANG_WIN="${LANG_WIN:-pt-br}"          # idioma no formato do uupdump (minúsculo)
APPS="${APPS:-1}"                      # 1 = com apps da Store; 0 = sem (menor)
# Apps offline enxertados na install.wim — instalam no 1º boot via SetupComplete.cmd
# (roda como SYSTEM no fim do Setup, ANTES do logon e SEM precisar de rede):
CHROME="${CHROME:-1}"                  # 1 = inclui Google Chrome (MSI enterprise offline)
WINGET="${WINGET:-1}"                  # 1 = inclui winget/App Installer (provisionado via DISM)
WINGET_REPO="${WINGET_REPO:-microsoft/winget-cli}"   # release de onde sai o App Installer
CHROME_URL="${CHROME_URL:-https://dl.google.com/dl/chrome/install/googlechromestandaloneenterprise64.msi}"
# Canal: oficial (24H2 estável) | RC (25H2 Release Preview) | DEV (Insider, build mais novo).
# Via argumento posicional (RC/DEV) ou env CHANNEL=. Compat: RC=1 / DEV=1 também funcionam.
CHANNEL="${CHANNEL:-oficial}"
[ "${RC:-0}"  = "1" ] && CHANNEL="RC"
[ "${DEV:-0}" = "1" ] && CHANNEL="DEV"
# WORK_BASE é a base; o WORK real vira WORK_BASE/<uuid> (1 pasta por build).
# Assim cada canal/build fica isolado e o download de cada build persiste na sua
# própria pasta (é o "cache" do download — re-rodar reaproveita).
WORK_BASE="${WORK:-$ORIG_PWD/win-iso-build}"
CACHE="${CACHE:-1}"                    # 1 = cache de conversores (content-addressed, compartilhado)
# Cache dos conversores no mesmo FS do WORK p/ 'mv' ser rename atômico.
CACHE_DIR="${UUP_CACHE:-${WORK_BASE%/*}/.uup-cache}"
API="https://api.uupdump.net"
SITE="https://uupdump.net"

# Exige root: apt-get e algumas etapas do conversor (mount/loop) só rodam como root.
if [ "$(id -u)" -ne 0 ]; then
  echo "ERRO: rode como root (use: sudo $0 $*)" >&2
  exit 1
fi

# Argumentos posicionais: 'RC'/'DEV' escolhem o canal. 'list' é tratado adiante.
# O que sobrar (se houver) é interpretado como UUID.
_args=()
for _a in "$@"; do
  case "$_a" in
    RC|rc)   CHANNEL="RC" ;;
    DEV|dev) CHANNEL="DEV" ;;
    *)       _args+=("$_a") ;;
  esac
done
set -- "${_args[@]}"

# ----------------------------- pré-requisitos do HOST ------------------------
# O conversor oficial precisa destas ferramentas. Em Debian/Ubuntu:
need_pkgs="curl jq aria2 cabextract wimtools genisoimage chntpw unzip"
echo ">> Verificando dependências de conversão no host..."
missing=""
# Lista de binários espelha need_pkgs (os .bat entram via ISODIR, sem xorriso).
for c in curl jq aria2c cabextract wimlib-imagex genisoimage chntpw hivexsh unzip; do
  command -v "$c" >/dev/null 2>&1 || missing="$missing $c"
done
if [ -n "$missing" ]; then
  echo ">> Instalando dependências ($missing )..."
  apt-get update -y
  # nomes de PACOTE (não de binário): wimlib-imagex vem de 'wimtools',
  # hivexsh de 'libhivex-bin' (e chntpw cobre parte do registro).
  apt-get install -y $need_pkgs libhivex-bin || {
    echo "ERRO: não consegui instalar as dependências. Instale manualmente:"
    echo "      $need_pkgs libhivex-bin"
    exit 1
  }
fi

# ----------------------------- descoberta do build --------------------------
# Mesmo espírito do discover_suite do builder Ubuntu: pergunta à API qual é o
# build 11 amd64 mais novo e pega o uuid. (Seu alvo, parametrizado.)
# Mapeia canal -> prefixo de título p/ filtrar a API. DEV usa o prefixo genérico
# -> max_by(build) pega o Insider mais novo (hoje 26H1/28000.x); oficial e RC
# fixam a série estável. (Ao mudar de ano, ajuste 24H2/25H2 aqui.)
prefix_for_channel() {
  case "$1" in
    RC)  echo "Windows 11, version 25H2" ;;
    DEV) echo "Windows 11, version " ;;
    *)   echo "Windows 11, version 24H2" ;;
  esac
}
# Normaliza o canal atual e define prefixo + rótulo do build em andamento.
case "$CHANNEL" in RC) canal="RC" ;; DEV) canal="DEV" ;; *) canal="oficial"; CHANNEL="oficial" ;; esac
TITLE_PREFIX="$(prefix_for_channel "$CHANNEL")"

# Acha o build mais novo de um prefixo. Arg opcional = prefixo (default: do canal).
# Pode receber o JSON já baixado em $2 p/ evitar baixar de novo (usado no 'list').
discover_uuid() {
  local pfx="${1:-$TITLE_PREFIX}" json="${2:-}"
  [ -n "$json" ] || json="$(curl -fsSL -m 30 "$API/listid.php?search=windows%2011" 2>/dev/null || true)"
  printf '%s' "$json" \
    | jq -r --arg pfx "$pfx" --arg arch "$ARCH" '.response.builds
        | map(select(.arch==$arch and (.title | startswith($pfx))))
        | max_by(.build | split(".") | map(tonumber))
        | "\(.uuid)\t\(.title)\tbuild: \(.build)"' 2>/dev/null \
    || true
}

case "${1:-}" in
  list|-l|--list)
    echo ">> Builds mais novos por canal ($ARCH) no uupdump:"
    echo
    _json="$(curl -fsSL -m 30 "$API/listid.php?search=windows%2011" 2>/dev/null || true)"
    [ -n "$_json" ] || { echo "ERRO: API não respondeu (sem rede para $API?)."; exit 1; }
    for _ch in oficial RC DEV; do
      _info="$(discover_uuid "$(prefix_for_channel "$_ch")" "$_json" || true)"
      _cmd="sudo ./build-windows-iso.sh"; [ "$_ch" != "oficial" ] && _cmd="$_cmd $_ch"
      if [ -n "$_info" ] && [ "$_info" != "null" ]; then
        printf '  %-9s %s\n            %s\n            uuid:  %s\n            build: %s\n\n' \
          "[$_ch]" "$(printf '%s' "$_info" | cut -f2)" "$_cmd" \
          "$(printf '%s' "$_info" | cut -f1)" "$(printf '%s' "$_info" | cut -f3 | sed 's/^build: //')"
      else
        printf '  %-9s (nenhum build encontrado)\n\n' "[$_ch]"
      fi
    done
    exit 0
    ;;
esac

# uuid: do argumento, ou descoberto automaticamente
# BUILD = número do build (ex: 28000.2269), usado no nome da ISO. Pode ficar
# vazio se o uuid veio por argumento — resolvemos um fallback mais adiante.
BUILD=""
if [ -n "${1:-}" ]; then
  UUID="$1"
  echo ">> Usando uuid informado: $UUID"
else
  echo ">> Descobrindo o build 11 ($ARCH) no uupdump (canal: $canal — filtro '$TITLE_PREFIX')..."
  info="$(discover_uuid || true)"          # validado contra a API
  [ -n "$info" ] || { echo "ERRO: API não respondeu (sem rede para $API?)."; exit 1; }
  UUID="$(printf '%s' "$info" | cut -f1)"
  BUILD="$(printf '%s' "$info" | cut -f3 | sed 's/^build: //')"
  echo ">> Alvo: $(printf '%s' "$info" | cut -f2-)"
  echo ">> uuid: $UUID"
fi

# 1 pasta por build: cada UUID tem seu próprio WORK. Builds/canais diferentes não
# colidem e cada download persiste no seu diretório, reaproveitado em re-runs.
WORK="$WORK_BASE/$UUID"
echo ">> Pasta deste build: $WORK"

# ----------------------------- preparação do WORK ----------------------------
# Blindagem do rm -rf, no mesmo molde do builder Ubuntu: recusa caminhos perigosos.
_WORK_RP="$(readlink -m "$WORK")"
case "$_WORK_RP" in
  /|"$HOME"|/root|/home|/etc|/usr|/bin|/sbin|/lib|/lib64|/boot|/dev|/proc|/run|/sys|/var|/opt|/srv|/mnt|/media)
    echo "ERRO: WORK aponta para um caminho perigoso ('$_WORK_RP')."
    echo "      Use um diretório dedicado, ex: WORK=\$HOME/win-build"
    exit 1
    ;;
esac
# Lock: impede duas execuções simultâneas sobre o MESMO WORK (mesmo build), que
# se atrapalhariam escrevendo nos mesmos arquivos. flock segura um fd exclusivo;
# se outro processo já tem o lock, abortamos com mensagem clara em vez de colidir.
# (builds de UUIDs diferentes têm WORK/lock distintos e podem rodar em paralelo.)
LOCK="${TMPDIR:-/tmp}/win-iso-build$(printf '%s' "$_WORK_RP" | tr '/ ' '__').lock"
exec 9>"$LOCK"
if ! flock -n 9; then
  echo "ERRO: já existe um build em andamento usando WORK='$_WORK_RP'."
  echo "      Espere ele terminar ou aborte-o antes de iniciar outro."
  echo "      (lock: $LOCK)"
  exit 1
fi
# Nunca destrói $WORK: o download UUP é caro e cada build tem pasta própria, então
# re-rodar reaproveita o que já baixou (aria2 -c). Para zerar um build, apague a
# pasta manualmente: rm -rf "$WORK_BASE/<uuid>".
mkdir -p "$WORK"
cd "$WORK"

# ----------------------------- baixar o pacote de conversão ------------------
# O endpoint get.php gera um .zip com o uup_download_linux.sh + ConvertConfig.ini
# JÁ FILTRADOS pelos parâmetros: edition (Pro), lang (pt-br), e a flag de apps.
#   pack=<lang>  edition=<edição>  autodl=2 (baixa+converte)  updates/apps via ConvertConfig
# Montamos a URL com os filtros; assim o pacote já vem só com Pro pt-BR.
echo ">> Validando idioma e edição contra a API (evita o erro 400)..."
# A API expõe os idiomas (listlangs) e, por idioma, as edições (listeditions).
# Conferimos ANTES de montar o get.php: o 400 acontecia por edição com nome
# fora do catálogo (precisa ser MAIÚSCULA, ex: PROFESSIONAL — não "Professional").
# `|| true` em cada pipe: com pipefail, curl falhando aborta a cmdsub.
# Tratamos a string vazia logo depois (validação só dispara se a API respondeu).
_langs="$(curl -fsSL -m 30 "$API/listlangs.php?id=${UUID}" 2>/dev/null \
          | jq -r '.response.langList[]?' 2>/dev/null || true)"
if [ -n "$_langs" ] && ! printf '%s\n' "$_langs" | grep -qxF "$LANG_WIN"; then
  echo "ERRO: idioma '$LANG_WIN' não existe neste build. Disponíveis:"
  printf '%s\n' "$_langs" | paste -sd' ' -
  exit 1
fi
_eds="$(curl -fsSL -m 30 "$API/listeditions.php?id=${UUID}&lang=${LANG_WIN}" 2>/dev/null \
        | jq -r '.response.editionList[]?' 2>/dev/null || true)"
# normaliza a edição pedida p/ MAIÚSCULA e confere contra o catálogo
EDITION="$(printf '%s' "$EDITION" | tr '[:lower:]' '[:upper:]')"
if [ -n "$_eds" ] && ! printf '%s\n' "$_eds" | grep -qxF "$EDITION"; then
  echo "ERRO: edição '$EDITION' não existe p/ $LANG_WIN neste build. Disponíveis:"
  printf '%s\n' "$_eds" | paste -sd' ' -
  exit 1
fi
echo ">> OK: $LANG_WIN + $EDITION confirmados no catálogo do build."

echo ">> Baixando o pacote de conversão ($EDITION $LANG_WIN) do uupdump..."
ZIP="$WORK/uup_pack.zip"
# IMPORTANTE: o ZIP do conversor é gerado pelo SITE (uupdump.net), NÃO pela API
# (api.uupdump.net — esta só serve JSON de metadados). Parâmetros confirmados:
#   pack=<idioma>  edition=<edição>  autodl=2  → Content-Type: archive/zip
PACK_URL="$SITE/get.php?id=${UUID}&pack=${LANG_WIN}&edition=${EDITION}&autodl=2"
if ! curl -fSL -m 120 -o "$ZIP" "$PACK_URL"; then
  echo "ERRO: falha ao baixar o pacote de conversão (HTTP)."
  echo "      URL tentada: $PACK_URL"
  echo "      Abra no navegador p/ comparar: ${SITE}/selectlang.php?id=${UUID}"
  exit 1
fi
unzip -o "$ZIP" -d "$WORK" >/dev/null
[ -f "$WORK/uup_download_linux.sh" ] || {
  echo "ERRO: pacote sem uup_download_linux.sh (zip inesperado)."
  echo "      Conteúdo:"; ls -la "$WORK"
  exit 1
}
chmod +x "$WORK/uup_download_linux.sh"

# ----------------------------- pré-baixar os conversores ---------------------
# O conversor oficial usa aria2c (-x16, timeout curto) p/ baixar convert.sh e
# convert_ve_plugin de git.uupdump.net — um mirror Cloudflare LENTO e instável
# (já vimos 522 e respostas de ~20s). O aria2 desiste e derruba o build inteiro.
# Solução: buscamos esses arquivos ANTES com curl paciente (--retry, http1.1),
# conferimos o sha-256, e ZERAMOS files/converter_multi. Com a lista vazia o
# aria2c sai com código 0 ("No files to download") e o convert.sh já está no
# lugar. Isso NÃO afeta o download pesado da Microsoft (etapa seguinte, intacta).
prefetch_converters() {
  local list="$WORK/files/converter_multi"
  [ -f "$list" ] || { echo ">> (sem converter_multi; nada a pré-baixar)"; return 0; }
  local url out sum got cdir cfile dst tmp
  cdir="$CACHE_DIR/converter"
  # awk agrupa cada stanza (URL / out= / checksum=) numa linha url<TAB>out<TAB>sum
  while IFS="$(printf '\t')" read -r url out sum; do
    [ -n "$url" ] && [ -n "$out" ] || continue
    dst="$WORK/files/$out"
    # Cache content-addressed: o nome do arquivo no cache É o sha256. Se existe,
    # é íntegro por definição — copia e pula o mirror lento.
    cfile="$cdir/$sum"
    if [ "$CACHE" = "1" ] && [ -n "$sum" ] && [ -f "$cfile" ]; then
      cp -f "$cfile" "$dst"
      echo ">> cache HIT: '$out' (sha ${sum:0:12}…) — sem tocar no mirror."
      continue
    fi
    echo ">> pré-baixando '$out' via curl (mirror lento, com retry)..."
    tmp="$dst.partial"
    if curl -fsSL --http1.1 -m 120 --retry 5 --retry-delay 5 -o "$tmp" "$url"; then
      if [ -n "$sum" ]; then
        got="$(sha256sum "$tmp" | cut -d' ' -f1)"
        if [ "$got" != "$sum" ]; then
          echo "ERRO: checksum de '$out' não confere (got=$got want=$sum)."
          rm -f "$tmp"
          # convert_ve_plugin é opcional (convert.sh só faz 'source' se existir);
          # qualquer outro arquivo é obrigatório.
          [ "$out" = "convert_ve_plugin" ] && { echo ">> (opcional; seguindo sem ele)"; continue; }
          return 1
        fi
      fi
      mv -f "$tmp" "$dst"   # finalize atômico: só vira o nome real depois de OK
      echo ">> OK: '$out' ($(wc -c < "$dst") bytes, checksum confere)."
      # popula o cache (tmp -> rename); content-addressed só faz sentido com sum.
      if [ "$CACHE" = "1" ] && [ -n "$sum" ]; then
        mkdir -p "$cdir"
        cp -f "$dst" "$cfile.tmp.$$" && mv -f "$cfile.tmp.$$" "$cfile" \
          && echo ">> cache SAVE: '$out' -> $cdir/${sum:0:12}…" \
          || { rm -f "$cfile.tmp.$$"; echo ">> (aviso: não consegui cachear '$out')"; }
      fi
    else
      rm -f "$tmp"
      echo ">> aviso: falhou baixar '$out' após retries."
      [ "$out" = "convert_ve_plugin" ] && { echo ">> (opcional; seguindo sem ele)"; continue; }
      return 1
    fi
  done < <(awk '
      /^https?:\/\// { if (url) print url"\t"out"\t"sum; url=$0; out=""; sum="" }
      /^[[:space:]]*out=/ { sub(/^[[:space:]]*out=/,""); out=$0 }
      /^[[:space:]]*checksum=sha-256=/ { sub(/^[[:space:]]*checksum=sha-256=/,""); sum=$0 }
      END { if (url) print url"\t"out"\t"sum }
    ' "$list")
  # lista zerada: aria2c vira no-op (exit 0); os arquivos já estão em files/.
  : > "$list"
  echo ">> Conversores prontos em $WORK/files (lista do aria2 zerada)."
}
echo ">> Pré-baixando os scripts do conversor (contorna o mirror lento)..."
prefetch_converters

# ------------------- arquivos extras embutidos (.bat na raiz) ----------------
# Os .bat ficam EMBUTIDOS aqui (heredoc), não dependem de arquivo local. São
# escritos em $WORK/_extras e copiados p/ ISODIR ANTES do genisoimage rodar —
# assim entram na raiz da ISO (no UDF, que o Windows lê) pela mesma ferramenta
# que monta o boot. NÃO usamos xorriso (não relê o UDF da ISO do Windows).
EXTRAS_DIR="$WORK/_extras"
# Instaladores offline (Chrome/winget) que vão p/ DENTRO da install.wim. Cache
# compartilhado entre builds (por nome de arquivo) p/ não rebaixar ~370MB toda vez.
PAYLOAD_DIR="$WORK/_payload"
PAYLOAD_CACHE="${PAYLOAD_CACHE:-$CACHE_DIR/payload}"
write_embedded_extras() {
  mkdir -p "$EXTRAS_DIR"
  cat > "$EXTRAS_DIR/notpm.bat" <<'NOTPM_EOF'
reg add "HKLM\SYSTEM\Setup\LabConfig" /v "BypassTPMCheck" /t REG_DWORD /d 1 /f
reg add "HKLM\SYSTEM\Setup\LabConfig" /v "BypassSecureBootCheck" /t REG_DWORD /d 1 /f
reg add "HKLM\SYSTEM\Setup\LabConfig" /v "BypassRAMCheck" /t REG_DWORD /d 1 /f
reg add "HKLM\SYSTEM\Setup\LabConfig" /v "BypassCPUCheck" /t REG_DWORD /d 1 /f
reg add "HKLM\SYSTEM\Setup\LabConfig" /v "BypassStorageCheck" /t REG_DWORD /d 1 /f
NOTPM_EOF
  cat > "$EXTRAS_DIR/refs_formata_automatico.bat" <<'REFS_EOF'
@echo off
echo ========================================
echo    FORMATADOR AUTOMATICO DE DISCO
echo ========================================
echo.
echo ATENCAO: Este script ira APAGAR TODOS OS DADOS
echo do Disco 0. Certifique-se de fazer backup!
echo.
pause
echo.

echo Executando Diskpart...
(
echo sel dis 0
echo clean
echo conv gpt
echo cre par efi size=512
echo format fs=fat32 quick
echo assign letter w
echo cre par pri
echo format fs=refs quick
echo assign letter c
echo exit
) | diskpart

echo.
echo ========================================
echo Processo concluido com sucesso!
echo Particoes criadas:
echo - W: (EFI, FAT32, 512MB)
echo - C: (PRIMARY, ReFS)
echo ========================================
pause
REFS_EOF
  # add_user.bat — cria a conta local "base" (admin) e reinicia. Rodado pelo
  # operador via Shift+F10 durante o Setup/OOBE. (msoobe é legado: em Win10/11
  # pode não existir; o && garante que o shutdown só roda se o msoobe existir.)
  cat > "$EXTRAS_DIR/add_user.bat" <<'ADDUSER_EOF'
net user base /add
net user base base
rem tentativa br
net localgroup administradores base /add
rem tentativa en
net localgroup administrators base /add
rem & (nao &&): no cmd e' sequencial - roda o msoobe e ESPERA, depois reinicia
rem SEMPRE (mesmo num errorlevel inesperado). -t 0 = reinicia na hora (sem os 30s).
oobe\msoobe & shutdown -r -t 0
ADDUSER_EOF
  # SetupComplete.cmd: o Windows roda este arquivo automaticamente no FIM do Setup,
  # como SYSTEM, antes do primeiro logon. Ideal p/ instalar apps offline (sem rede).
  # Vai p/ \Windows\Setup\Scripts\ dentro da install.wim (ver inject_extras...).
  cat > "$EXTRAS_DIR/SetupComplete.cmd" <<'SETUPC_EOF'
@echo off
setlocal
set "LOG=%SystemRoot%\Setup\Scripts\buildiso-postinstall.log"
set "SRC=%SystemRoot%\Setup\Files"
echo [buildiso] SetupComplete iniciado %DATE% %TIME%>>"%LOG%"

rem ---- Google Chrome (MSI enterprise offline, instala por maquina) ----
if exist "%SRC%\chrome.msi" (
  echo [buildiso] Instalando Google Chrome...>>"%LOG%"
  msiexec /i "%SRC%\chrome.msi" /qn /norestart>>"%LOG%" 2>&1
)

rem ---- winget / App Installer ----
rem  O SetupComplete roda DEPOIS do OOBE, quando o 1o usuario JA existe. Provisionar
rem  (DISM) so registra p/ usuarios criados DEPOIS disso -> nao pega o 1o usuario.
rem  Por isso: (1) provisiona p/ usuarios FUTUROS e (2) agenda um RunOnce que, no
rem  1o logon, registra o winget p/ o usuario ATUAL via Add-AppxPackage (por-usuario,
rem  sem timing nem licenca). O passo (2) e' o que faz o winget "vir" de verdade.
if exist "%SRC%\winget\AppInstaller.msixbundle" (
  echo [buildiso] Provisionando winget p/ usuarios futuros (DISM)...>>"%LOG%"
  powershell -NoProfile -ExecutionPolicy Bypass -File "%SystemRoot%\Setup\Scripts\install-winget.ps1">>"%LOG%" 2>&1
  echo [buildiso] Agendando registro do winget p/ o 1o logon (RunOnce)...>>"%LOG%"
  reg add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\RunOnce" /v "BuildisoWinget" /t REG_SZ /d "powershell -NoProfile -ExecutionPolicy Bypass -File \"%SystemRoot%\Setup\Scripts\register-winget.ps1\"" /f>>"%LOG%" 2>&1
)
echo [buildiso] SetupComplete concluido %DATE% %TIME%>>"%LOG%"
endlocal
exit /b 0
SETUPC_EOF

  # Provisiona o App Installer (winget) p/ todos os usuarios, com suas dependencias.
  # Add-AppxProvisionedPackage aceita o array de deps direto — bem mais limpo que cmd.
  cat > "$EXTRAS_DIR/install-winget.ps1" <<'WINGETPS_EOF'
$ErrorActionPreference = 'Continue'
$wg      = Join-Path $env:SystemRoot 'Setup\Files\winget'
$bundle  = Join-Path $wg 'AppInstaller.msixbundle'
$license = Join-Path $wg 'License.xml'
$deps = @(Get-ChildItem -Path (Join-Path $wg 'deps') -Recurse -Include *.appx,*.msix -ErrorAction SilentlyContinue | ForEach-Object { $_.FullName })
if (Test-Path $bundle) {
  try {
    if (Test-Path $license) {
      Add-AppxProvisionedPackage -Online -PackagePath $bundle -DependencyPackagePath $deps -LicensePath $license | Out-Null
    } else {
      Add-AppxProvisionedPackage -Online -PackagePath $bundle -DependencyPackagePath $deps -SkipLicense | Out-Null
    }
    Write-Output "winget/App Installer provisionado com sucesso ($($deps.Count) dependencias)."
  } catch {
    Write-Output "ERRO ao provisionar winget: $($_.Exception.Message)"
  }
} else {
  Write-Output "AVISO: bundle do App Installer nao encontrado em $bundle"
}
WINGETPS_EOF

  # register-winget.ps1: roda no 1º LOGON do usuário (via RunOnce do SetupComplete),
  # no contexto DELE. Add-AppxPackage registra o winget p/ o usuário atual — é o que
  # garante o winget no PATH (provisionar via DISM não pega o 1º usuário do OOBE).
  cat > "$EXTRAS_DIR/register-winget.ps1" <<'REGWG_EOF'
$ErrorActionPreference = 'SilentlyContinue'
$log = Join-Path $env:TEMP 'buildiso-winget-register.log'
$wg  = Join-Path $env:SystemRoot 'Setup\Files\winget'
"[buildiso] register-winget iniciado $(Get-Date)" | Out-File -FilePath $log -Append
# Dependências PRIMEIRO (VCLibs / UI.Xaml / WindowsAppRuntime), depois o bundle.
Get-ChildItem -Path (Join-Path $wg 'deps') -Recurse -Include *.appx,*.msix -ErrorAction SilentlyContinue | ForEach-Object {
  try { Add-AppxPackage -Path $_.FullName -ForceApplicationShutdown; "  dep OK: $($_.Name)" | Out-File -FilePath $log -Append }
  catch { "  dep ERRO: $($_.Name) :: $($_.Exception.Message)" | Out-File -FilePath $log -Append }
}
$bundle = Join-Path $wg 'AppInstaller.msixbundle'
if (Test-Path $bundle) {
  try { Add-AppxPackage -Path $bundle -ForceApplicationShutdown; "  bundle OK -> winget registrado p/ $env:USERNAME" | Out-File -FilePath $log -Append }
  catch { "  bundle ERRO: $($_.Exception.Message)" | Out-File -FilePath $log -Append }
}
REGWG_EOF

  # Windows espera CRLF em .bat/.cmd/.ps1 (idempotente: colapsa \r* p/ um \r só).
  sed -i 's/\r*$/\r/' "$EXTRAS_DIR"/*.bat "$EXTRAS_DIR"/*.cmd "$EXTRAS_DIR"/*.ps1
  echo ">> extras embutidos gerados (CRLF): $(ls "$EXTRAS_DIR" | paste -sd' ' -)"
}

# Baixa os instaladores offline p/ $PAYLOAD_DIR (cache compartilhado por nome de
# arquivo). Chrome = MSI enterprise. winget = bundle + license + deps (VCLibs /
# UI.Xaml / WindowsAppRuntime) extraídas do DesktopAppInstaller_Dependencies.zip da
# release oficial. Falhas são NÃO-fatais: apenas pula o app que não baixou.
fetch_payloads() {
  [ "$CHROME" = "1" ] || [ "$WINGET" = "1" ] || { echo ">> Chrome e winget desativados; sem payloads."; return 0; }
  mkdir -p "$PAYLOAD_DIR"
  mkdir -p "$PAYLOAD_CACHE" 2>/dev/null || true   # cache é best-effort (pode ser read-only)
  # baixa url -> cache/<nome> (se faltar) e copia p/ destino. Sem cache, baixa direto.
  _dl_cached() { # url  cachename  dest
    local url="$1" cname="$2" dest="$3" cf="$PAYLOAD_CACHE/$2"
    mkdir -p "$(dirname "$dest")"
    if [ -w "$PAYLOAD_CACHE" ] && [ -s "$cf" ]; then
      echo ">> cache HIT: $cname"; cp -f "$cf" "$dest"; return 0
    fi
    echo ">> baixando $cname ..."
    curl -fSL --http1.1 -m 1800 --retry 5 --retry-delay 5 -o "$dest.partial" "$url" \
      || { rm -f "$dest.partial"; return 1; }
    mv -f "$dest.partial" "$dest"
    [ -w "$PAYLOAD_CACHE" ] && cp -f "$dest" "$cf" 2>/dev/null || true
    return 0
  }

  if [ "$CHROME" = "1" ]; then
    _dl_cached "$CHROME_URL" "chrome.msi" "$PAYLOAD_DIR/chrome.msi" \
      || { echo ">> AVISO: falha ao baixar o Chrome; seguindo SEM ele."; rm -f "$PAYLOAD_DIR/chrome.msi"; }
  fi

  if [ "$WINGET" = "1" ]; then
    # arch do zip de deps: amd64->x64, arm64->arm64 (o uupdump é amd64 por padrão).
    local zarch; case "$ARCH" in arm64) zarch="arm64" ;; *) zarch="x64" ;; esac
    local rel burl lurl durl tag
    rel="$(curl -fsSL -m 60 "https://api.github.com/repos/$WINGET_REPO/releases/latest" 2>/dev/null || true)"
    burl="$(printf '%s' "$rel" | jq -r '.assets[]?|select(.name|endswith(".msixbundle"))|.browser_download_url' 2>/dev/null | head -1)"
    lurl="$(printf '%s' "$rel" | jq -r '.assets[]?|select(.name|endswith("_License1.xml"))|.browser_download_url' 2>/dev/null | head -1)"
    durl="$(printf '%s' "$rel" | jq -r '.assets[]?|select(.name=="DesktopAppInstaller_Dependencies.zip")|.browser_download_url' 2>/dev/null | head -1)"
    tag="$(printf '%s' "$rel" | jq -r '.tag_name // "latest"' 2>/dev/null)"
    if [ -n "$burl" ] && [ "$burl" != "null" ]; then
      echo ">> winget release: $tag (deps: $zarch)"
      local ok=1
      _dl_cached "$burl" "winget_${tag}_AppInstaller.msixbundle" "$PAYLOAD_DIR/winget/AppInstaller.msixbundle" || ok=0
      if [ -n "$lurl" ] && [ "$lurl" != "null" ]; then
        _dl_cached "$lurl" "winget_${tag}_License.xml" "$PAYLOAD_DIR/winget/License.xml" \
          || echo ">> (sem License.xml; uso -SkipLicense no provisionamento)"
      fi
      if [ -n "$durl" ] && [ "$durl" != "null" ]; then
        if _dl_cached "$durl" "winget_${tag}_deps.zip" "$PAYLOAD_DIR/winget/_deps.zip"; then
          rm -rf "$PAYLOAD_DIR/winget/deps"; mkdir -p "$PAYLOAD_DIR/winget/deps"
          # -j: descarta a pasta arch; pega só os pacotes da arquitetura do build.
          unzip -o -j "$PAYLOAD_DIR/winget/_deps.zip" "$zarch/*" -d "$PAYLOAD_DIR/winget/deps" >/dev/null 2>&1 \
            || { echo ">> AVISO: não extraí deps '$zarch'; tentando todas."; unzip -o -j "$PAYLOAD_DIR/winget/_deps.zip" -d "$PAYLOAD_DIR/winget/deps" >/dev/null 2>&1 || ok=0; }
          rm -f "$PAYLOAD_DIR/winget/_deps.zip"
        else
          ok=0
        fi
      fi
      if [ "$ok" != "1" ] || [ ! -s "$PAYLOAD_DIR/winget/AppInstaller.msixbundle" ]; then
        echo ">> AVISO: payload do winget incompleto; seguindo SEM winget."; rm -rf "$PAYLOAD_DIR/winget"
      fi
    else
      echo ">> AVISO: não descobri o asset do winget (API GitHub indisponível?); seguindo SEM winget."
    fi
  fi

  echo ">> Payloads prontos:"; find "$PAYLOAD_DIR" -type f -printf '   %p (%s bytes)\n' 2>/dev/null || true
}

# Injeta no convert.sh, logo antes da criação da ISO: (1) os .bat na raiz da ISO e
# (2) Chrome/winget DENTRO da install.wim (todos os índices) + SetupComplete.cmd.
# A lógica fica num arquivo separado ($WORK/buildiso_inject.sh) que o convert.sh
# faz 'source' em runtime — assim ele enxerga as variáveis do conversor ($type,
# $tempDir, $indexesExported, ISODIR) com aspas/escapes normais, sem inferno de awk.
# Idempotente E atualizável: remove qualquer injeção anterior (bloco novo entre
# sentinelas OU o formato antigo só-.bat já presente no cache) antes de reaplicar.
inject_extras_into_convert() {
  local cs="$WORK/files/convert.sh"
  [ -f "$cs" ] || { echo "ERRO: convert.sh ausente — não dá p/ injetar os extras."; exit 1; }
  # Falha CEDO (antes da conversão longa) se o ponto-âncora não existir.
  grep -q 'Creating ISO image' "$cs" || {
    echo "ERRO: âncora 'Creating ISO image' não encontrada no convert.sh."
    echo "      A versão do conversor mudou — ajuste inject_extras_into_convert."
    exit 1
  }
  # Limpa injeções anteriores: bloco novo (sentinelas) + formato antigo (.bat inline).
  sed -i \
    -e '/# BUILDISO_INJECT_BEGIN/,/# BUILDISO_INJECT_END/d' \
    -e '/BUILDISO_EXTRA_INJECT/d' \
    -e '\#_extras/notpm.bat.*ISODIR/#d' \
    "$cs"

  # Arquivo de injeção (sourced em runtime). Heredoc QUOTED: $type/$tempDir/etc.
  # ficam literais p/ o convert.sh avaliar; @@EXTRAS@@/@@PAYLOAD@@ são placeholders.
  local inj="$WORK/buildiso_inject.sh"
  cat > "$inj" <<'INJ_EOF'
# extras do build-windows-iso.sh — rodado via 'source' pelo convert.sh
__EX="@@EXTRAS@@"
__PL="@@PAYLOAD@@"

# (1) .bat na raiz da ISO (gravados no UDF pelo genisoimage logo a seguir)
cp -f "$__EX/notpm.bat" "$__EX/refs_formata_automatico.bat" "$__EX/add_user.bat" ISODIR/ 2>/dev/null \
  && echo "  [extra] notpm.bat + refs_formata_automatico.bat + add_user.bat -> raiz da ISO" || true

# (2) Chrome + winget DENTRO da install.$type (todos os índices) + SetupComplete.cmd.
#     Instalam no 1º boot (SetupComplete.cmd roda como SYSTEM, sem rede).
__wim="ISODIR/sources/install.$type"
if [ -f "$__wim" ] && [ -f "$__EX/SetupComplete.cmd" ] \
   && { [ -f "$__PL/chrome.msi" ] || [ -f "$__PL/winget/AppInstaller.msixbundle" ]; }; then
  __u="${tempDir:-/tmp}/buildiso_payload_update.txt"
  {
    echo "add \"$__EX/SetupComplete.cmd\"    /Windows/Setup/Scripts/SetupComplete.cmd"
    echo "add \"$__EX/install-winget.ps1\"   /Windows/Setup/Scripts/install-winget.ps1"
    echo "add \"$__EX/register-winget.ps1\"  /Windows/Setup/Scripts/register-winget.ps1"
    [ -f "$__PL/chrome.msi" ] && echo "add \"$__PL/chrome.msi\" /Windows/Setup/Files/chrome.msi"
    if [ -f "$__PL/winget/AppInstaller.msixbundle" ]; then
      echo "add \"$__PL/winget/AppInstaller.msixbundle\" /Windows/Setup/Files/winget/AppInstaller.msixbundle"
      [ -f "$__PL/winget/License.xml" ] && echo "add \"$__PL/winget/License.xml\" /Windows/Setup/Files/winget/License.xml"
      for __d in "$__PL"/winget/deps/*; do
        [ -f "$__d" ] && echo "add \"$__d\" /Windows/Setup/Files/winget/deps/$(basename "$__d")"
      done
    fi
  } > "$__u"
  __i=1
  while [ "$__i" -le "${indexesExported:-1}" ]; do
    if wimlib-imagex update "$__wim" "$__i" < "$__u" >/dev/null 2>&1; then
      echo "  [extra] Chrome/winget -> install.$type (índice $__i)"
    else
      echo "  [extra] AVISO: falhou enxertar payloads no índice $__i de install.$type"
    fi
    __i=$((__i + 1))
  done
else
  echo "  [extra] (sem payloads para install.$type — Chrome/winget pulados)"
fi
true
INJ_EOF
  # Resolve os placeholders p/ caminhos absolutos (# como delim: paths têm /).
  sed -i -e "s#@@EXTRAS@@#$EXTRAS_DIR#g" -e "s#@@PAYLOAD@@#$PAYLOAD_DIR#g" "$inj"

  # Injeta UMA linha de 'source' antes de "Creating ISO image", entre sentinelas.
  awk -v inj="$inj" '
    /Creating ISO image/ && !done {
      print "# BUILDISO_INJECT_BEGIN"
      print ". \"" inj "\""
      print "# BUILDISO_INJECT_END"
      done=1
    }
    { print }
  ' "$cs" > "$cs.new" && mv "$cs.new" "$cs"
  chmod +x "$cs"
  echo ">> convert.sh ajustado: .bat na raiz + Chrome/winget na install.wim (antes do genisoimage)."
}
write_embedded_extras
fetch_payloads
inject_extras_into_convert

# ----------------------------- ajustar ConvertConfig -------------------------
# O ConvertConfig.ini controla o que entra na ISO. Forçamos:
#   wim->esd off (mantém install.wim), e a flag de apps conforme $APPS.
# (As edições/idioma já vieram filtrados no pacote.)
CFG="$WORK/ConvertConfig.ini"
if [ -f "$CFG" ]; then
  # [NÃO TESTADO] — chaves do ConvertConfig podem mudar entre builds do uupdump;
  # estes sed são best-effort e não quebram o build se a chave não existir.
  if [ "$APPS" = "0" ]; then
    sed -i 's/^AddDesktopApps.*/AddDesktopApps=0/' "$CFG" 2>/dev/null || true
    sed -i 's/^AddTeamsApp.*/AddTeamsApp=0/'       "$CFG" 2>/dev/null || true
  fi
  sed -i 's/^wim2esd.*/wim2esd=0/' "$CFG" 2>/dev/null || true
fi

# NOTA: não há "cache de UUPs" separado. Como o WORK é por-UUID, o download
# (UUPs/) já persiste na pasta do build; re-rodar o MESMO build reaproveita
# (aria2 -c verifica e pula). Trocar de canal usa outra pasta — sem colisão.

# ----------------------------- converter -> ISO ------------------------------
echo ">> Baixando pacotes UUP da Microsoft e convertendo em ISO (demora)..."
echo "   (esta etapa é do conversor OFICIAL do uupdump; pode levar bem tempo)"
# O convert.sh oficial usa `mktemp -d`, que cai em /tmp. Aqui /tmp é tmpfs de
# ~1.7G (RAM) e a extração de FODs grandes (ex: Edge-WebView ~227MB comprimido)
# estoura isso -> "Failed to extract ...cab". mktemp respeita $TMPDIR, então
# apontamos pro disco grande do $WORK. Não precisa editar o script oficial.
export TMPDIR="$WORK/tmp"
rm -rf "$TMPDIR"; mkdir -p "$TMPDIR"   # limpo: cada tentativa do mktemp deixa sobra
echo ">> TMPDIR do conversor -> $TMPDIR (evita estouro do /tmp tmpfs)."

# Daqui pra frente quem trabalha é o script oficial do uupdump. Os conversores
# já foram pré-baixados acima; o retry aqui cobre blips no download dos pacotes
# UUP da Microsoft. aria2 usa -c (continue), então retoma de onde parou.
MAX_TRIES="${RETRIES:-3}"
SLEEP_BETWEEN="${SLEEP_BETWEEN:-10}"
attempt=1
while : ; do
  echo ">> Tentativa $attempt/$MAX_TRIES do conversor..."
  # IMPORTANTE: capturar rc no ELSE. Um `if cmd; then..; fi` SEM else retorna 0
  # quando cmd falha, então `rc=$?` após o `fi` pegaria 0 e mascararia a falha.
  if "$WORK/uup_download_linux.sh"; then
    echo ">> Conversor terminou OK (tentativa $attempt)."
    break
  else
    rc=$?
  fi
  if [ "$attempt" -ge "$MAX_TRIES" ]; then
    echo "ERRO: conversor falhou em todas as $MAX_TRIES tentativas (último rc=$rc)."
    exit "$rc"
  fi
  echo ">> Falhou (rc=$rc). Aguardando ${SLEEP_BETWEEN}s antes de tentar de novo..."
  sleep "$SLEEP_BETWEEN"
  attempt=$((attempt + 1))
done

# ----------------------------- coletar a ISO ---------------------------------
ISO_FOUND="$(ls -1t "$WORK"/*.ISO "$WORK"/*.iso 2>/dev/null | head -1 || true)"
if [ -z "$ISO_FOUND" ]; then
  echo "ERRO: a conversão terminou mas não achei .iso em $WORK."
  echo "      Verifique a saída do conversor acima (download da MS pode ter falhado)."
  exit 1
fi

# BUILD pode estar vazio se o uuid veio por argumento; tenta extrair do nome do
# .iso que o conversor gerou (costuma começar com o número do build).
if [ -z "$BUILD" ]; then
  BUILD="$(basename "$ISO_FOUND" | grep -oE '^[0-9]+\.[0-9]+' || true)"
  [ -n "$BUILD" ] || BUILD="desconhecido"
fi

# Os .bat já foram embutidos na raiz da ISO ANTES do genisoimage (ver
# inject_extras_into_convert) — a ISO crua aqui JÁ os contém. Verificação não-fatal
# via mount UDF (os arquivos ficam no UDF; ISO9660 está com --hide "*").
_mp="$(mktemp -d)"
if mount -t udf -o loop,ro "$ISO_FOUND" "$_mp" 2>/dev/null \
   || mount -o loop,ro "$ISO_FOUND" "$_mp" 2>/dev/null; then
  if [ -f "$_mp/notpm.bat" ] && [ -f "$_mp/refs_formata_automatico.bat" ]; then
    echo ">> confirmado: notpm.bat e refs_formata_automatico.bat estão na raiz da ISO."
  else
    echo ">> AVISO: não encontrei os .bat na raiz montada — verifique antes de gravar."
  fi
  umount "$_mp" 2>/dev/null
else
  echo ">> (não consegui montar p/ verificar; injeção é determinística, deve estar ok)"
fi
rmdir "$_mp" 2>/dev/null || true

# ----------------------------- nomear e mover --------------------------------
# Padrão: win11_pro_ptbr_<build>_<canal>_com_script_anti_tpm_e_refs.iso
# ($canal = oficial|RC|DEV, definido na seção de descoberta por canal)
DEFAULT_NAME="win11_pro_ptbr_${BUILD}_${canal}_com_script_anti_tpm_e_refs.iso"
OUT="${OUT:-$ORIG_PWD/$DEFAULT_NAME}"
mv -f "$ISO_FOUND" "$OUT" 2>/dev/null || OUT="$ISO_FOUND"

echo ">> Pronto!"
command -v sha256sum >/dev/null 2>&1 && sha256sum "$OUT"
echo
echo "ISO gerada em: $OUT"
echo "Inclui na raiz: notpm.bat (bypass TPM/SecureBoot/RAM/CPU/Storage) + refs_formata_automatico.bat"
{ [ "$CHROME" = "1" ] || [ "$WINGET" = "1" ]; } && \
  echo "Instala no 1º boot (SetupComplete.cmd, sem rede):$([ "$CHROME" = "1" ] && echo ' Google Chrome')$([ "$WINGET" = "1" ] && echo ' winget/App-Installer') — log em C:\\Windows\\Setup\\Scripts\\buildiso-postinstall.log"
echo "Para um pendrive multiboot junto com a ISO do Ubuntu, use o Ventoy:"
echo "  sudo sh Ventoy2Disk.sh -i /dev/sdX   # apaga o pendrive (confira com lsblk!)"
echo "  cp '$OUT' ubuntu-*.iso /caminho/do/pendrive/"
