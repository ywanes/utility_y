#!/bin/bash
# Airflow DAG upload/download helpers
# Usage: source /opt/atools.sh

ATOOLS_REMOTE_HOST="data-warehouse-azure"
ATOOLS_REMOTE_DIR="airflow-datawarehouse/dags"

ahelp() {
    echo "
=== atools — Airflow DAG helpers ===

SERVIDOR
  alist                        Lista .py no servidor  (YYYY-MM-DD  md5x5  arquivo)
  adown    <padrao>            Baixa arquivo do servidor
  adown    all                 Baixa todos os .py do servidor
  aup   <padrao>               Envia arquivo para o servidor

GIT (github.com/bng-health/airflow-datawarehouse)
  alistgit                     Lista .py no git       (YYYY-MM-DD  md5x5  arquivo)
  adowngit <padrao>            Baixa do servidor e commita no git local
  adowngit all                 Baixa todos os .py do servidor e commita cada um no git local
  aupgit   <padrao> [descricao]  Envia para o servidor, commita e push para o GitHub

COMPARACAO
  adiff                        Diferenças e ausências entre local e servidor
  adiff    <padrao>            Diff colorido de um arquivo: local vs servidor
  adiffgit                     Diferenças e ausências entre local e git
  adiffgit <padrao>            Diff colorido de um arquivo: local vs git

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

alistgit() {
    _airflow_check_env || return 1

    if ! _agit fetch origin > /dev/null; then
        echo "Aviso: git fetch falhou, listando estado local do git." >&2
    fi

    local output=""
    for f in /opt/git_atools/dags/*.py; do
        [ -f "$f" ] || continue
        local basename_f date md5 md5_short
        basename_f=$(basename "$f")
        date=$(_agit log -1 --format="%as" -- "dags/$basename_f")
        [ -z "$date" ] && date="0000-00-00"
        md5=$(md5sum "$f" | awk '{print $1}')
        md5_short=${md5: -5}
        output+="$date $md5_short $basename_f\n"
    done
    echo -e "$output" | sort
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
    only_local=$(comm -23 "$tmp_local" "$tmp_remote")
    only_remote=$(comm -13 "$tmp_local" "$tmp_remote")
    both=$(comm -12 "$tmp_local" "$tmp_remote")

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

adiffgit() {
    _airflow_check_env || return 1

    local git_dags="/opt/git_atools/dags"

    if ! _agit fetch origin > /dev/null; then
        echo "Aviso: git fetch falhou, comparando com estado local do git." >&2
    fi

    if [ -n "$1" ]; then
        local local_match git_match local_count git_count
        local_match=$(ls *.py 2>/dev/null | grep "$1")
        local_count=$(echo "$local_match" | grep -c .)
        if [ -z "$local_match" ] || [ "$local_count" -eq 0 ]; then
            echo "Nenhum arquivo local encontrado com o padrao '$1'." >&2; return 1
        elif [ "$local_count" -gt 1 ]; then
            echo "Mais de um arquivo local encontrado com o padrao '$1':" >&2
            echo "$local_match" >&2; return 1
        fi
        git_match=$(ls "$git_dags"/*.py 2>/dev/null | xargs -I{} basename {} | grep "$1")
        git_count=$(echo "$git_match" | grep -c .)
        if [ -z "$git_match" ] || [ "$git_count" -eq 0 ]; then
            echo "Nenhum arquivo no git encontrado com o padrao '$1'." >&2; return 1
        elif [ "$git_count" -gt 1 ]; then
            echo "Mais de um arquivo no git encontrado com o padrao '$1':" >&2
            echo "$git_match" >&2; return 1
        fi
        _adiff_show "$git_dags/$git_match" "$local_match"
        return
    fi

    local tmp_local tmp_git
    tmp_local=$(mktemp)
    tmp_git=$(mktemp)

    ls *.py 2>/dev/null | LC_ALL=C sort > "$tmp_local"
    ls "$git_dags"/*.py 2>/dev/null | xargs -I{} basename {} | LC_ALL=C sort > "$tmp_git"

    local only_local only_git both
    only_local=$(comm -23 "$tmp_local" "$tmp_git")
    only_git=$(comm -13 "$tmp_local" "$tmp_git")
    both=$(comm -12 "$tmp_local" "$tmp_git")

    rm -f "$tmp_local" "$tmp_git"

    local different=""
    while IFS= read -r file; do
        [ -z "$file" ] && continue
        local lmd5 gmd5
        lmd5=$(md5sum "$file" | awk '{print $1}')
        gmd5=$(md5sum "$git_dags/$file" | awk '{print $1}')
        [ "$lmd5" != "$gmd5" ] && different+="$file\n"
    done <<< "$both"

    local output=""
    [ -n "$only_local" ] && output+="=== Somente local ===\n$only_local\n\n"
    [ -n "$only_git" ]   && output+="=== Somente no git ===\n$only_git\n\n"
    [ -n "$different" ]  && output+="=== Diferentes ===\n$different"
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

adowngit() {
    _airflow_check_env || return 1

    local pattern="$1"
    if [ -z "$pattern" ]; then
        echo "Uso: adowngit <padrao>" >&2
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
        echo "Baixando e commitando todos os arquivos .py:"
        echo "$all_files"
        echo "$all_files" | while IFS= read -r f; do
            if scp "$remote_host:~/$remote_dir/$f" .; then
                cp "$f" /opt/git_atools/dags/
                _agit reset HEAD "dags/$f"
                _agit add "dags/$f"
                _agit commit --allow-empty -m "Update $f"
            else
                echo "Erro ao baixar: $f" >&2
            fi
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
    scp "$remote_host:~/$remote_dir/$matches" . || return 1

    cp "$matches" /opt/git_atools/dags/
    _agit reset HEAD "dags/$matches"
    _agit add "dags/$matches"
    _agit commit --allow-empty -m "Update $matches"
}

aupgit() {
    _airflow_check_env || return 1

    local pattern="$1"
    local descricao="$2"
    if [ -z "$pattern" ]; then
        echo "Uso: aupgit <padrao> [descricao]" >&2
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
    scp "$matches" "$remote_host:~/$remote_dir/" || return 1

    local msg="Update $matches"
    [ -n "$descricao" ] && msg="$msg

$descricao"

    _agit pull --rebase
    cp "$matches" /opt/git_atools/dags/
    _agit reset HEAD "dags/$matches"
    _agit add "dags/$matches"
    _agit commit --allow-empty -m "$msg"
    _agit push
}