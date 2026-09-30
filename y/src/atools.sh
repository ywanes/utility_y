#!/bin/bash
# Airflow DAG upload/download helpers
# Usage: source /opt/atools.sh

ATOOLS_REMOTE_HOST="data-warehouse-azure"
ATOOLS_REMOTE_DIR="airflow-datawarehouse/dags"
ATOOLS_GH_REPO="bng-health/airflow-datawarehouse"
ATOOLS_GH_BASE="main"
ATOOLS_GH_REF="origin/$ATOOLS_GH_BASE"
ATOOLS_GIT_DAGS="dags"
ATOOLS_GH_WORKFLOW="deploy-dag.yml"

unset -f _ssh_run
unset -f adelgit

ahelp() {
    echo "
# ===>   . /opt/atools.sh            <===
#        . /opt/atools_internal.sh


SERVIDOR
  alist                        Lista .py no servidor  (YYYY-MM-DD  md5x5  arquivo)
  adown    <padrao>            Baixa arquivo do servidor
  adown    all                 Baixa todos os .py do servidor
  aup   <padrao>               Envia arquivo para o servidor

GIT (github.com/bng-health/airflow-datawarehouse)
  alistgit                     Lista .py na main      (YYYY-MM-DD  md5x5  arquivo)
  adowngit <padrao>            Baixa da main para ~/claude (sobrescreve)
  adowngit all                 Baixa todos os .py da main para ~/claude
  aupgit   <padrao> [descricao]  Envia para o servidor, commita, abre PR, aprova e faz o deploy
  aprovagit <numero> [token]   Aprova um PR (exige token de OUTRA conta)
  adeploy  <padrao> [ref]      Dispara o workflow \"Deploy DAG\" (ref default: main)

COMPARACAO
  adiff                        Diferenças e ausências entre local e servidor
  adiff    <padrao>            Diff colorido de um arquivo: local vs servidor
  adiffgit                     Diferenças e ausências entre local e main
  adiffgit <padrao>            Diff colorido de um arquivo: local vs main

  Saida do diff: linhas em verde (+) sao locais, vermelho (-) sao remoto/git
"
}

_ssh_run() {
    local tmp_err result exit_code
    tmp_err=$(mktemp)
    result=$(ssh "$@" 2>"$tmp_err")
    exit_code=$?
    if [ $exit_code -ne 0 ]; then
        echo "Erro SSH: $(cat "$tmp_err")" >&2
        rm -f "$tmp_err"
        return $exit_code
    fi
    rm -f "$tmp_err"
    printf '%s\n' "$result"
}

_airflow_check_env() {
    if [ "$(id -u)" -eq 0 ]; then
        echo "Erro: nao execute como root." >&2
        return 1
    fi
    if [ "$(pwd)" != "$HOME/claude" ]; then
        echo "Erro: voce precisa estar no diretorio ~/claude (atual: $(pwd))." >&2
        return 1
    fi
    return 0
}

alist() {
    _airflow_check_env || return 1
    ssh data-warehouse-azure 'cd ~/airflow-datawarehouse/dags && for f in *.py; do [ -f "$f" ] || continue; date=$(stat -c "%y" "$f" | cut -c1-10); md5=$(md5sum "$f" | awk "{print \$1}"); echo "$date $md5 $f"; done' \
        | awk '{print $1, substr($2, length($2)-4), $3}' \
        | sort
}

adown() {
    _airflow_check_env || return 1

    local pattern="$1"
    if [ -z "$pattern" ]; then
        echo "Uso: adown <padrao>" >&2
        return 1
    fi

    local remote_host="$ATOOLS_REMOTE_HOST"
    local remote_dir="$ATOOLS_REMOTE_DIR"

    local remote_list
    remote_list=$(_ssh_run "$remote_host" "ls ~/$remote_dir") || return 1

    if [ "$pattern" = "all" ]; then
        local all_files
        all_files=$(echo "$remote_list" | grep '\.py$')
        if [ -z "$all_files" ]; then
            echo "Nenhum arquivo .py encontrado em ~/$remote_dir." >&2
            return 1
        fi
        echo "Baixando todos os arquivos .py:"
        echo "$all_files"
        echo "$all_files" | while IFS= read -r f; do
            scp "$remote_host:~/$remote_dir/$f" . || echo "Erro ao baixar: $f" >&2
        done
        return
    fi

    local matches
    matches=$(echo "$remote_list" | grep "$pattern")

    local count=0
    [ -n "$matches" ] && count=$(echo "$matches" | wc -l)

    if [ "$count" -eq 0 ] || [ -z "$matches" ]; then
        echo "Nenhum arquivo encontrado com o padrao '$pattern' em ~/$remote_dir." >&2
        return 1
    elif [ "$count" -gt 1 ]; then
        echo "Mais de um arquivo encontrado com o padrao '$pattern':" >&2
        echo "$matches" >&2
        return 1
    fi

    echo "Baixando: $matches"
    scp "$remote_host:~/$remote_dir/$matches" .
}

aup() {
    _airflow_check_env || return 1

    local pattern="$1"
    if [ -z "$pattern" ]; then
        echo "Uso: aup <padrao>" >&2
        return 1
    fi

    local remote_host="$ATOOLS_REMOTE_HOST"
    local remote_dir="$ATOOLS_REMOTE_DIR"

    local matches
    matches=$(ls | grep "$pattern")

    local count=0
    [ -n "$matches" ] && count=$(echo "$matches" | wc -l)

    if [ "$count" -eq 0 ] || [ -z "$matches" ]; then
        echo "Nenhum arquivo local encontrado com o padrao '$pattern'." >&2
        return 1
    elif [ "$count" -gt 1 ]; then
        echo "Mais de um arquivo local encontrado com o padrao '$pattern':" >&2
        echo "$matches" >&2
        return 1
    fi

    echo "Enviando: $matches"
    scp "$matches" "$remote_host:~/$remote_dir/"
}


adiff() {
    _airflow_check_env || return 1

    local remote_host="$ATOOLS_REMOTE_HOST"
    local remote_dir="$ATOOLS_REMOTE_DIR"

    if [ -n "$1" ]; then
        local local_match remote_match local_count remote_count
        local_match=$(ls *.py 2>/dev/null | grep "$1")
        local_count=$(echo "$local_match" | grep -c .)
        if [ -z "$local_match" ] || [ "$local_count" -eq 0 ]; then
            echo "Nenhum arquivo local encontrado com o padrao '$1'." >&2; return 1
        elif [ "$local_count" -gt 1 ]; then
            echo "Mais de um arquivo local encontrado com o padrao '$1':" >&2
            echo "$local_match" >&2; return 1
        fi
        local remote_list_sdiff
        remote_list_sdiff=$(_ssh_run "$remote_host" "ls ~/$remote_dir") || return 1
        remote_match=$(echo "$remote_list_sdiff" | grep '\.py$' | grep "$1")
        remote_count=$(echo "$remote_match" | grep -c .)
        if [ -z "$remote_match" ] || [ "$remote_count" -eq 0 ]; then
            echo "Nenhum arquivo no servidor encontrado com o padrao '$1'." >&2; return 1
        elif [ "$remote_count" -gt 1 ]; then
            echo "Mais de um arquivo no servidor encontrado com o padrao '$1':" >&2
            echo "$remote_match" >&2; return 1
        fi
        local tmp_remote
        tmp_remote=$(mktemp)
        scp "$remote_host:~/$remote_dir/$remote_match" "$tmp_remote"
        _adiff_show "$tmp_remote" "$local_match"
        rm -f "$tmp_remote"
        return
    fi

    local tmp_local tmp_remote
    tmp_local=$(mktemp)
    tmp_remote=$(mktemp)

    ls *.py 2>/dev/null | LC_ALL=C sort > "$tmp_local"
    local remote_list_all
    remote_list_all=$(_ssh_run "$remote_host" "ls ~/$remote_dir") || { rm -f "$tmp_local" "$tmp_remote"; return 1; }
    echo "$remote_list_all" | grep '\.py$' | LC_ALL=C sort > "$tmp_remote"

    local only_local only_remote both
    only_local=$(LC_ALL=C comm -23 "$tmp_local" "$tmp_remote")
    only_remote=$(LC_ALL=C comm -13 "$tmp_local" "$tmp_remote")
    both=$(LC_ALL=C comm -12 "$tmp_local" "$tmp_remote")

    rm -f "$tmp_local" "$tmp_remote"

    local different=""
    if [ -n "$both" ]; then
        local remote_md5s
        remote_md5s=$(echo "$both" | _ssh_run "$remote_host" "cd ~/$remote_dir && xargs md5sum") || return 1
        while IFS= read -r file; do
            [ -z "$file" ] && continue
            local lmd5 rmd5
            lmd5=$(md5sum "$file" | awk '{print $1}')
            rmd5=$(echo "$remote_md5s" | awk -v f="$file" '$2==f {print $1}')
            [ "$lmd5" != "$rmd5" ] && different+="$file\n"
        done <<< "$both"
    fi

    local output=""
    [ -n "$only_local" ]  && output+="=== Somente local ===\n$only_local\n\n"
    [ -n "$only_remote" ] && output+="=== Somente no servidor ===\n$only_remote\n\n"
    [ -n "$different" ]   && output+="=== Diferentes ===\n$different"
    [ -z "$output" ] && echo "Tudo sincronizado." || echo -e "$output"
}


_adiff_show() {
    diff -U 0 "$1" "$2" \
        | grep -v '^---\|^+++\|^@@' \
        | sed 's/^\(+.*\)/\x1b[32m\1\x1b[0m/;s/^\(-.*\)/\x1b[31m\1\x1b[0m/'
}

_agit() {
    git -C /opt/git_atools "$@"
}

_agit_token() {
    _agit remote get-url origin | sed -n 's|https://\([^@]*\)@.*|\1|p'
}

_agit_json() {
    python3 -c 'import json,sys; print(json.dumps(dict(zip(sys.argv[1::2], sys.argv[2::2]))))' "$@"
}

_agit_campo() {
    python3 -c 'import json,sys
try:
    d = json.load(sys.stdin)
except Exception:
    sys.exit(0)
v = d
for k in sys.argv[1].split("."):
    if not isinstance(v, dict):
        sys.exit(0)
    v = v.get(k)
    if v is None:
        sys.exit(0)
print(v)' "$1"
}

_agit_api() {
    local metodo="$1" caminho="$2" corpo="$3"
    local token
    token=$(_agit_token)
    if [ -z "$token" ]; then
        echo "Erro: token nao encontrado no remote origin do git local." >&2
        return 1
    fi

    if [ -n "$corpo" ]; then
        curl -sS -X "$metodo" \
            -H "Authorization: token $token" \
            -H "Accept: application/vnd.github+json" \
            -d "$corpo" \
            "https://api.github.com/repos/$ATOOLS_GH_REPO$caminho"
    else
        curl -sS -X "$metodo" \
            -H "Authorization: token $token" \
            -H "Accept: application/vnd.github+json" \
            "https://api.github.com/repos/$ATOOLS_GH_REPO$caminho"
    fi
}

_agit_automerge() {
    local node_id="$1"
    local token
    token=$(_agit_token)
    [ -z "$token" ] && return 1

    local consulta
    consulta=$(_agit_json query "mutation { enablePullRequestAutoMerge(input: {pullRequestId: \"$node_id\", mergeMethod: MERGE}) { clientMutationId } }")

    curl -sS -X POST \
        -H "Authorization: token $token" \
        -H "Accept: application/vnd.github+json" \
        -d "$consulta" \
        "https://api.github.com/graphql"
}

_agit_sync() {
    if ! _agit fetch origin "$ATOOLS_GH_BASE" >/dev/null 2>&1; then
        echo "Aviso: git fetch falhou -- o resultado abaixo e do ultimo fetch e pode estar velho." >&2
        return 1
    fi
    return 0
}

_agit_avisa_pendente() {
    local ahead
    ahead=$(_agit rev-list --count "$ATOOLS_GH_REF..HEAD" 2>/dev/null)
    if [ -n "$ahead" ] && [ "$ahead" -gt 0 ]; then
        echo "Aviso: $ahead commit(s) no git local ainda nao estao em $ATOOLS_GH_BASE (PR pendente?)." >&2
    fi
}

_agit_arquivos_main() {
    _agit ls-tree --name-only "$ATOOLS_GH_REF" "$ATOOLS_GIT_DAGS/" 2>/dev/null \
        | sed 's|.*/||' \
        | grep '\.py$'
}

_agit_conteudo_main() {
    _agit show "$ATOOLS_GH_REF:$ATOOLS_GIT_DAGS/$1" 2>/dev/null
}

_agit_um_da_main() {
    local pattern="$1" lista match count
    lista=$(_agit_arquivos_main)
    match=$(echo "$lista" | grep "$pattern")
    count=0
    [ -n "$match" ] && count=$(echo "$match" | grep -c .)

    if [ "$count" -eq 0 ]; then
        echo "Nenhum arquivo em $ATOOLS_GH_BASE com o padrao '$pattern'." >&2
        return 1
    elif [ "$count" -gt 1 ]; then
        echo "Mais de um arquivo em $ATOOLS_GH_BASE com o padrao '$pattern':" >&2
        echo "$match" >&2
        return 1
    fi
    printf '%s\n' "$match"
}

alistgit() {
    _airflow_check_env || return 1
    _agit_sync
    _agit_avisa_pendente

    local output="" f data md5 md5_short
    while IFS= read -r f; do
        [ -z "$f" ] && continue
        data=$(_agit log -1 --format="%as" "$ATOOLS_GH_REF" -- "$ATOOLS_GIT_DAGS/$f" 2>/dev/null)
        [ -z "$data" ] && data="0000-00-00"
        md5=$(_agit_conteudo_main "$f" | md5sum | awk '{print $1}')
        md5_short=${md5: -5}
        output+="$data $md5_short $f\n"
    done <<< "$(_agit_arquivos_main)"

    if [ -z "$output" ]; then
        echo "Nenhum .py em $ATOOLS_GH_REF:$ATOOLS_GIT_DAGS/." >&2
        return 1
    fi
    echo -e "$output" | sed '/^$/d' | sort
}

adiffgit() {
    _airflow_check_env || return 1
    _agit_sync
    _agit_avisa_pendente

    # ---- modo com padrao: diff colorido de um arquivo ----
    if [ -n "$1" ]; then
        local local_match local_count main_match tmp_main
        local_match=$(ls *.py 2>/dev/null | grep "$1")
        local_count=$(echo "$local_match" | grep -c .)
        if [ -z "$local_match" ] || [ "$local_count" -eq 0 ]; then
            echo "Nenhum arquivo local encontrado com o padrao '$1'." >&2
            return 1
        elif [ "$local_count" -gt 1 ]; then
            echo "Mais de um arquivo local encontrado com o padrao '$1':" >&2
            echo "$local_match" >&2
            return 1
        fi

        main_match=$(_agit_um_da_main "$1") || return 1

        tmp_main=$(mktemp)
        _agit_conteudo_main "$main_match" > "$tmp_main"
        _adiff_show "$tmp_main" "$local_match"
        rm -f "$tmp_main"
        return
    fi

    # ---- modo inventario ----
    local tmp_local tmp_main
    tmp_local=$(mktemp)
    tmp_main=$(mktemp)

    ls *.py 2>/dev/null | LC_ALL=C sort > "$tmp_local"
    _agit_arquivos_main | LC_ALL=C sort > "$tmp_main"

    local only_local only_main both
    only_local=$(LC_ALL=C comm -23 "$tmp_local" "$tmp_main")
    only_main=$(LC_ALL=C comm -13 "$tmp_local" "$tmp_main")
    both=$(LC_ALL=C comm -12 "$tmp_local" "$tmp_main")

    rm -f "$tmp_local" "$tmp_main"

    local different="" file lmd5 mmd5
    while IFS= read -r file; do
        [ -z "$file" ] && continue
        lmd5=$(md5sum "$file" | awk '{print $1}')
        mmd5=$(_agit_conteudo_main "$file" | md5sum | awk '{print $1}')
        [ "$lmd5" != "$mmd5" ] && different+="$file\n"
    done <<< "$both"

    local output=""
    [ -n "$only_local" ] && output+="=== Somente local ===\n$only_local\n\n"
    [ -n "$only_main" ]  && output+="=== Somente na $ATOOLS_GH_BASE ===\n$only_main\n\n"
    [ -n "$different" ]  && output+="=== Diferentes ===\n$different"
    [ -z "$output" ] && echo "Tudo sincronizado com $ATOOLS_GH_BASE." || echo -e "$output"
}

adowngit() {
    _airflow_check_env || return 1

    local pattern="$1"
    if [ -z "$pattern" ]; then
        echo "Uso: adowngit <padrao>|all" >&2
        return 1
    fi

    _agit_sync
    _agit_avisa_pendente

    local alvos
    if [ "$pattern" = "all" ]; then
        alvos=$(_agit_arquivos_main)
        if [ -z "$alvos" ]; then
            echo "Nenhum .py em $ATOOLS_GH_REF:$ATOOLS_GIT_DAGS/." >&2
            return 1
        fi
        echo "Baixando de $ATOOLS_GH_BASE todos os .py:"
    else
        alvos=$(_agit_um_da_main "$pattern") || return 1
    fi

    local f tmp antes depois
    while IFS= read -r f; do
        [ -z "$f" ] && continue

        tmp=$(mktemp)
        if ! _agit_conteudo_main "$f" > "$tmp" || [ ! -s "$tmp" ]; then
            rm -f "$tmp"
            echo "Erro ao ler $f de $ATOOLS_GH_REF" >&2
            continue
        fi

        antes=""
        [ -f "$f" ] && antes=$(md5sum "$f" | awk '{print $1}')
        depois=$(md5sum "$tmp" | awk '{print $1}')

        mv "$tmp" "$f"
        if [ -z "$antes" ]; then
            echo "Baixado: $f (novo)"
        elif [ "$antes" != "$depois" ]; then
            echo "Baixado: $f (local sobrescrito)"
        else
            echo "Baixado: $f (identico)"
        fi
    done <<< "$alvos"
}

aupgit() {
    _airflow_check_env || return 1

    local pattern="$1"
    local descricao="$2"
    if [ -z "$pattern" ]; then
        echo "Uso: aupgit <padrao> [descricao]" >&2
        return 1
    fi

    local matches
    matches=$(ls | grep "$pattern")

    local count=0
    [ -n "$matches" ] && count=$(echo "$matches" | wc -l)

    if [ "$count" -eq 0 ] || [ -z "$matches" ]; then
        echo "Nenhum arquivo local encontrado com o padrao '$pattern'." >&2
        return 1
    elif [ "$count" -gt 1 ]; then
        echo "Mais de um arquivo local encontrado com o padrao '$pattern':" >&2
        echo "$matches" >&2
        return 1
    fi

    # NAO enviar daqui: aup manda para o servidor, aupgit manda para o github. Quem entrega no
    # servidor e o workflow "Deploy DAG", a partir do que foi mergeado -- e com rename atomico.

    local msg="Update $matches"
    [ -n "$descricao" ] && msg="$msg

$descricao"

    # --autostash: nao trava quando ha outro arquivo modificado no git local
    _agit pull --rebase --autostash origin "$ATOOLS_GH_BASE" || return 1

    cp "$matches" /opt/git_atools/dags/
    _agit reset HEAD "dags/$matches"
    _agit add "dags/$matches"
    _agit commit --allow-empty -m "$msg"

    local branch="aupgit/${matches%.py}-$(date +%Y%m%d-%H%M%S)"
    echo "Branch: $branch"
    _agit push origin "HEAD:refs/heads/$branch" || return 1

    local titulo="Update $matches"
    [ -n "$descricao" ] && titulo="$titulo - $descricao"

    local resp numero url node_id
    resp=$(_agit_api POST /pulls "$(_agit_json title "$titulo" head "$branch" base "$ATOOLS_GH_BASE" body "$msg")")
    numero=$(printf '%s' "$resp" | _agit_campo number)
    url=$(printf '%s' "$resp" | _agit_campo html_url)
    node_id=$(printf '%s' "$resp" | _agit_campo node_id)

    if [ -z "$numero" ]; then
        echo "Erro ao abrir o PR: $(printf '%s' "$resp" | _agit_campo message)" >&2
        echo "A branch ficou no remoto: $branch" >&2
        return 1
    fi

    echo "PR #$numero aberto: $url"

    # ---- approve ----
    _agit_aprova "$numero" "$matches" || echo "Seguindo sem review: a $ATOOLS_GH_BASE nao e protegida, o merge nao depende dele." >&2

    local mresp merged
    mresp=$(_agit_api PUT "/pulls/$numero/merge" "$(_agit_json merge_method merge commit_title "$titulo (#$numero)")")
    merged=$(printf '%s' "$mresp" | _agit_campo merged)

    if [ "$merged" = "True" ] || [ "$merged" = "true" ]; then
        echo "PR #$numero mergeado em $ATOOLS_GH_BASE."
        _agit pull --rebase --autostash origin "$ATOOLS_GH_BASE"
        _agit push origin --delete "$branch" >/dev/null 2>&1

        # ---- deploy pelo GitHub Actions, ja com o arquivo na $ATOOLS_GH_BASE ----
        _agit_deploy "$matches" "$ATOOLS_GH_BASE" || {
            echo "Rode depois: adeploy $matches" >&2
            return 1
        }
        return 0
    fi

    echo "Merge direto negado: $(printf '%s' "$mresp" | _agit_campo message)"

    if [ -n "$node_id" ]; then
        local aresp erro
        aresp=$(_agit_automerge "$node_id")
        erro=$(printf '%s' "$aresp" | _agit_erro_detalhe)
        if [ -z "$erro" ]; then
            echo "Auto-merge armado: cai em $ATOOLS_GH_BASE sozinho no primeiro approve."
        else
            echo "Auto-merge nao armado: $erro"
        fi
    fi

    echo "Falta 1 approve. Peca a revisao em: $url"
    echo "Deploy nao disparado: a $ATOOLS_GH_BASE ainda nao tem esta versao. Depois do merge, rode: adeploy $matches"
    return 0
}

aprovagit() {
    local numero="$1"
    local token="${2:-$AUPGIT_REVIEW_TOKEN}"

    if [ -z "$numero" ]; then
        echo "Uso: aprovagit <numero-do-PR> [token]" >&2
        return 1
    fi
    if [ -z "$token" ]; then
        echo "Erro: informe o token do revisor ou exporte AUPGIT_REVIEW_TOKEN." >&2
        return 1
    fi

    local resp estado
    resp=$(_agit_review "$numero" APPROVE "$token")

    estado=$(printf '%s' "$resp" | _agit_campo state)
    if [ "$estado" = "APPROVED" ]; then
        echo "PR #$numero aprovado."
    else
        echo "Falhou: $(printf '%s' "$resp" | _agit_campo message)" >&2
        return 1
    fi
}

# ---- approve do PR + deploy pelo GitHub Actions (usado por aupgit, aprovagit e adeploy) ----
# Workflow alvo: .github/workflows/deploy-dag.yml ("Deploy DAG")
#   input unico: dag_name -- valida ^[A-Za-z0-9._-]+\.py$ e exige dags/<dag_name> na ref

_agit_review() {
    local numero="$1" evento="${2:-APPROVE}" token="$3" corpo="$4" payload
    [ -z "$token" ] && token=$(_agit_token)
    if [ -z "$token" ]; then
        echo "Erro: token nao encontrado no remote origin do git local." >&2
        return 1
    fi

    # COMMENT exige body; APPROVE aceita sem
    if [ -n "$corpo" ]; then
        payload=$(_agit_json event "$evento" body "$corpo")
    else
        payload=$(_agit_json event "$evento")
    fi

    curl -sS -X POST \
        -H "Authorization: token $token" \
        -H "Accept: application/vnd.github+json" \
        -d "$payload" \
        "https://api.github.com/repos/$ATOOLS_GH_REPO/pulls/$numero/reviews"
}

_agit_erro_detalhe() {
    # o GitHub manda "Unprocessable Entity" em message e o motivo real em errors[]
    python3 -c 'import json,sys
try:
    d = json.load(sys.stdin)
except Exception:
    sys.exit(0)
partes = []
if d.get("message"):
    partes.append(str(d["message"]))
for e in (d.get("errors") or []):
    m = e.get("message") if isinstance(e, dict) else str(e)
    if m:
        partes.append(str(m))
print(" -- ".join(partes))'
}

_agit_aprova() {
    local numero="$1" dag="$2" resp estado msg

    resp=$(_agit_review "$numero" APPROVE) || return 1
    estado=$(printf '%s' "$resp" | _agit_campo state)
    if [ "$estado" = "APPROVED" ]; then
        echo "PR #$numero aprovado."
        return 0
    fi
    msg=$(printf '%s' "$resp" | _agit_erro_detalhe)

    # o GitHub recusa APPROVE do proprio autor -- se houver token de revisor, tenta com ele
    if [ -n "$AUPGIT_REVIEW_TOKEN" ]; then
        resp=$(_agit_review "$numero" APPROVE "$AUPGIT_REVIEW_TOKEN")
        estado=$(printf '%s' "$resp" | _agit_campo state)
        if [ "$estado" = "APPROVED" ]; then
            echo "PR #$numero aprovado com AUPGIT_REVIEW_TOKEN."
            return 0
        fi
        echo "Approve com AUPGIT_REVIEW_TOKEN falhou: $(printf '%s' "$resp" | _agit_erro_detalhe)" >&2
    fi

    # o GitHub barra APPROVE do proprio autor, mas aceita review COMMENT: fica
    # registrado como review seu, so nao com o estado APPROVED
    resp=$(_agit_review "$numero" COMMENT "" "revisado")
    estado=$(printf '%s' "$resp" | _agit_campo state)
    if [ "$estado" = "COMMENTED" ]; then
        echo "PR #$numero: review COMMENT registrado (APPROVE barrado: $msg)."
        return 0
    fi

    echo "Approve nao registrado: $msg" >&2
    echo "Review COMMENT tambem falhou: $(printf '%s' "$resp" | _agit_erro_detalhe)" >&2
    return 1
}

_agit_dispatch() {
    local dag="$1" ref="${2:-$ATOOLS_GH_BASE}"
    local token corpo tmp code

    token=$(_agit_token)
    if [ -z "$token" ]; then
        echo "Erro: token nao encontrado no remote origin do git local." >&2
        return 1
    fi

    # mesma regra do deploy-dag.yml: so nome de arquivo .py
    if ! [[ "$dag" =~ ^[A-Za-z0-9._-]+\.py$ ]]; then
        echo "Erro: dag_name invalido para o workflow: $dag" >&2
        return 1
    fi

    corpo=$(python3 -c 'import json,sys; print(json.dumps({"ref": sys.argv[1], "inputs": {"dag_name": sys.argv[2]}}))' "$ref" "$dag")

    tmp=$(mktemp)
    code=$(curl -sS -o "$tmp" -w '%{http_code}' -X POST \
        -H "Authorization: token $token" \
        -H "Accept: application/vnd.github+json" \
        -d "$corpo" \
        "https://api.github.com/repos/$ATOOLS_GH_REPO/actions/workflows/$ATOOLS_GH_WORKFLOW/dispatches")

    if [ "$code" = "204" ]; then
        rm -f "$tmp"
        return 0
    fi
    echo "Erro no dispatch (HTTP $code): $(_agit_campo message < "$tmp")" >&2
    rm -f "$tmp"
    return 1
}

_agit_run_recente() {
    python3 -c 'import json,sys
try:
    d = json.load(sys.stdin)
except Exception:
    sys.exit(0)
for r in d.get("workflow_runs", []):
    if r.get("created_at", "") >= sys.argv[1]:
        print(r["id"], r.get("status") or "-", r.get("conclusion") or "-", r.get("html_url") or "-")
        break' "$1"
}

_agit_deploy_acompanha() {
    local desde="$1" dag="$2"
    local i=0 linha id status conclusion url mostrou=""

    while [ "$i" -lt 40 ]; do
        linha=$(_agit_api GET "/actions/workflows/$ATOOLS_GH_WORKFLOW/runs?event=workflow_dispatch&per_page=10" | _agit_run_recente "$desde")
        if [ -n "$linha" ]; then
            read -r id status conclusion url <<< "$linha"
            if [ -z "$mostrou" ]; then
                echo "Run #$id: $url"
                mostrou=1
            fi
            if [ "$status" = "completed" ]; then
                if [ "$conclusion" = "success" ]; then
                    echo "Deploy concluido: $dag em producao."
                    return 0
                fi
                echo "Deploy falhou ($conclusion): $url" >&2
                return 1
            fi
        fi
        sleep 5
        i=$((i + 1))
    done

    echo "Deploy ainda rodando (parei de acompanhar). Veja: ${url:-https://github.com/$ATOOLS_GH_REPO/actions}" >&2
    return 0
}

_agit_deploy() {
    local dag="$1" ref="${2:-$ATOOLS_GH_BASE}" desde

    desde=$(date -u -d '60 seconds ago' +%Y-%m-%dT%H:%M:%SZ)
    echo "Disparando \"Deploy DAG\" (dag_name=$dag, ref=$ref)..."
    _agit_dispatch "$dag" "$ref" || return 1
    _agit_deploy_acompanha "$desde" "$dag"
}

adeploy() {
    _airflow_check_env || return 1

    local pattern="$1"
    if [ -z "$pattern" ]; then
        echo "Uso: adeploy <padrao> [ref]" >&2
        return 1
    fi

    _agit_sync

    local alvo
    alvo=$(_agit_um_da_main "$pattern") || return 1

    _agit_deploy "$alvo" "${2:-$ATOOLS_GH_BASE}"
}