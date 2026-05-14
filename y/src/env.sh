# setup env/maquina
# curl  https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/env.sh | bash
# test
# docker run -itd --name test node
# docker exec -it test bash
# curl  https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/env.sh | bash
# docker stop test
# docker rm test

if [ `whoami` != "root" ]
then
  echo Eh preciso estar logado em root
  exit 1
fi

type javac 2> /tmp/not
if [ $((`cat /tmp/not | wc -l`)) -ge 1 ]
then
  echo instale o javac, use o comando abaixo:
  echo "apt install -y openjdk-21-jdk" # limpa -> apt purge -y openjdk-* default-jdk default-jre && apt autoremove -y
  echo Mas antes, faça apt update e apt upgrade
  exit 1
fi

type curl 2> /tmp/not
if [ $((`cat /tmp/not | wc -l`)) -ge 1 ]
then
  echo instale o curl, use o comando abaixo:
  echo apt install curl
  exit 1
fi

if [ ! -e /opt ]
then
    mkdir /opt
fi
chmod 777 /opt

if [ ! -e /opt/y ]
then
    mkdir /opt/y
fi
chmod 777 /opt/y

cat <<'EOF'> /opt/env_
#!/bin/bash
alias lss='ls -ltr'
if [ -e /usr/bin/ls ] # remove color
then
  alias lss='/usr/bin/ls -ltr'
  alias grep='/usr/bin/grep'
  alias ls='/usr/bin/ls'
fi
alias pss='ps -ef'
export PATH="$PATH":.
alias y='java -Dfile.encoding=UTF-8 -cp /opt/y:/opt/y/ojdbc6.jar:/opt/y/sqljdbc4-3.0.jar:/opt/y/mysql-connector-java-8.0.26.jar:/opt/y/postgresql-42.7.5.jar:/opt/y/jsch-0.1.55.jar:. Y'
rm -f /opt/.u_flag
alias u='/opt/.u'
alias uu='/opt/.u_c'
# old #if [ `whoami` == "root" ] && [ `apt upgrade 2>/dev/null < /dev/null | grep "not upgraded" | grep -v "and 0 not upgraded" | wc -l` -eq 1 ]
if [ `whoami` == "root" ] && [ `apt upgrade 2>/dev/null < /dev/null | grep "Not Upgrading:" | wc -l` -eq 1 ]
then  
  echo "" > /opt/.u_c
  apt list --upgradable -a 2>/dev/null | while read linha 
  do
    if [ "$linha" != "" ] && [ "$linha" != "Listing..." ] && [ "$linha" != "Listagem..." ] && [ `echo "$linha" | grep ",now" | wc -l` -eq 0 ]
    then
      p1=`echo $linha | awk ' { print $1 } '`
	  echo $p1
    fi    
  done | head -1 | while read linha
  do
    echo "apt-get install --only-upgrade $linha" > /opt/.u_c
	chmod 777 /opt/.u_c
	echo digite uu # esse script só carrega quando entrar em root, se precisar entre em root varias vezes
  done
fi
if [ "1" == "1" ] # verify new ubuntu and LTS
then
  if [ `whoami` == "root" ] && [ -e /etc/os-release ]
  then
    # ultima atualização em 17/10/2025
    v1=`cat /etc/os-release | tr '"' ' ' | grep VERSION_ID | awk ' { print $2 } '`
    v2=`curl https://cdimage.ubuntu.com/daily-live/current/ 2>/dev/null | grep title | head -1 | awk ' { print $2 } '`
    v3=`curl http://changelogs.ubuntu.com/meta-release-development 2>/dev/null | grep "Version: " | tail -1 | awk ' { print $2 } '`
    if [ "$v1" != "$v2" ] && [ "$v2" == "$v3" ] && [ "$v3" != "" ]
    then
      echo "New ubuntu --> $v3"
    fi
    if [ -e /etc/update-manager/release-upgrades ] && [ `cat /etc/update-manager/release-upgrades | grep ^Prompt=normal$ | wc -l` == 0 ]
    then
      echo Alerta LTS, roda o comando abaixo!!:
      echo sed -i "s/Prompt=lts/Prompt=normal/g" /etc/update-manager/release-upgrades
    fi
    # desfazendo apt-mark unhold libegl-mesa0
    #if [ $((`apt-mark showhold | grep ^libegl-mesa0$ | wc -l`)) == 0 ] && [ $((`dmidecode  | grep -i product | grep VMware | wc -l`)) -ge 1 ]
    #then
    #  echo o comando abaixo foi disparado automaticamente por questao de segurança
    #  apt-mark hold libegl-mesa0
    #fi
    ip6m=2002:2002:2002:2002
    ip6b=::100
    ipn=renato
    if [ `cat /etc/hosts | grep ${ip6m}${ip6b} | wc -l` -eq 0 ]
    then
      echo ${ip6m}${ip6b}' '${ipn} >> /etc/hosts
    fi
    ip6b=:ff:feb3:63e9
    ipn=local
    if [ `cat /etc/hosts | grep ${ip6m}${ip6b} | wc -l` -eq 0 ]
    then
      echo ${ip6m}${ip6b}' '${ipn} >> /etc/hosts
    fi
  fi
  alias u1='echo u1/u7..;apt update'
  alias u2='echo u2/u7..;apt upgrade'
  alias u3='echo u3/u7..;do-release-upgrade'
  alias u4='echo u4/u7..;u1'
  alias u5='echo u5/u7..;u2'
  alias u6='echo u6/u7..;apt dist-upgrade'
  alias u7='echo u7/u7..;do-release-upgrade -d'
else
  echo "disable -> verify new ubuntu and LTS"
fi
EOF

cat <<'EOF'> /opt/porteiro.py
#!/usr/bin/env python3
import sys
import os
import socket
import threading
import time
import random


terminal_lock = threading.Lock()


def tprint(*args, **kwargs):
    with terminal_lock:
        print(*args, **kwargs)


class Porteiro:
    def __init__(self):
        self.prefixos_permitidos = []

    def carregar_config(self):
        if os.name == 'nt':
            path = r'c:\programFiles\serverrouter_ponteiro.cfg'
        else:
            path = '/opt/serverrouter_porteiro.cfg'

        if not os.path.exists(path):
            print(f"ERRO: Arquivo de configuracao do porteiro nao encontrado: {path}")
            return False

        with open(path, 'r') as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith('#'):
                    self.prefixos_permitidos.append(line.lower())

        print(f"Porteiro: {len(self.prefixos_permitidos)} prefixo(s) carregado(s): {self.prefixos_permitidos}")
        return True

    def is_permitido(self, post_body):
        body_lower = self._remover_comentarios_sql(post_body.strip().lower())
        for prefixo in self.prefixos_permitidos:
            if body_lower.startswith(prefixo):
                return True
        return False

    def _remover_comentarios_sql(self, texto):
        linhas = []
        for linha in texto.splitlines():
            linha_limpa = linha.strip()
            if not linha_limpa.startswith('--'):
                linhas.append(linha_limpa)
        return '\n'.join(linhas).strip()

    def is_post(self, request):
        return request.upper().startswith("POST ")

    def extrair_body_post(self, request):
        idx = request.find("\r\n\r\n")
        if idx != -1:
            return request[idx + 4:]
        idx = request.find("\n\n")
        if idx != -1:
            return request[idx + 2:]
        return None

    def perguntar_terminal(self, post_body):
        with terminal_lock:
            print("-------------------------------------------")
            print("PORTEIRO - Conteudo do POST:")
            print(post_body)
            print("-------------------------------------------")
            resposta = input("Enter para aceitar ou n: ")
            return resposta.strip().lower() != 'n'


RESP_403 = b"HTTP/1.1 403 Forbidden\r\nContent-Length: 0\r\nConnection: close\r\n\r\n"


def ponte(client_sock, dest_host, dest_port, ponte_id, porteiro=None):
    accumulated = b""
    dest_sock = None
    try:
        if porteiro:
            # acumula dados por 1 segundo antes de analisar
            client_sock.settimeout(0.1)
            start = time.time()
            try:
                while time.time() - start < 0.1:
                    chunk = client_sock.recv(4096)
                    if not chunk:
                        return
                    accumulated += chunk
            except socket.timeout:
                pass
            client_sock.settimeout(None)

            if not accumulated:
                return

            request_str = accumulated.decode('utf-8', errors='replace')

            if porteiro.is_post(request_str):
                post_body = porteiro.extrair_body_post(request_str)
                if post_body:
                    if not porteiro.is_permitido(post_body):
                        # nao auto-aprovado, perguntar no terminal
                        if not porteiro.perguntar_terminal(post_body):
                            # negado
                            client_sock.sendall(RESP_403)
                            client_sock.close()
                            return

        # conecta ao destino
        dest_sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        dest_sock.connect((dest_host, dest_port))

        # se porteiro, envia dados acumulados
        if accumulated:
            dest_sock.sendall(accumulated)

        # ponte bidirecional
        stop = threading.Event()

        def forward(src, dst):
            try:
                while not stop.is_set():
                    data = src.recv(4096)
                    if not data:
                        break
                    dst.sendall(data)
            except Exception:
                pass
            stop.set()
            try:
                src.close()
            except Exception:
                pass
            try:
                dst.close()
            except Exception:
                pass

        t1 = threading.Thread(target=forward, args=(client_sock, dest_sock), daemon=True)
        t2 = threading.Thread(target=forward, args=(dest_sock, client_sock), daemon=True)
        t1.start()
        t2.start()
        t1.join()
        t2.join()

    except Exception:
        pass
    finally:
        try:
            client_sock.close()
        except Exception:
            pass
        if dest_sock:
            try:
                dest_sock.close()
            except Exception:
                pass
    tprint(f"finalizando ponte id {ponte_id}")


def server_router(host0, port0, host1, port1, mode):
    porteiro = None
    if mode == "porteiro":
        porteiro = Porteiro()
        if not porteiro.carregar_config():
            sys.exit(1)

    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    try:
        server.bind((host0, port0))
    except Exception as e:
        print(f"Nao foi possivel utilizar a porta {port0} - {e}")
        sys.exit(1)
    server.listen(50)

    tprint("ServerRouter criado.")
    if porteiro:
        tprint(f"Modo PORTEIRO ativo. Prefixos permitidos: {porteiro.prefixos_permitidos}")
    tprint("obs: A ponte so estabelece conexao com o destino quando detectar o inicio da origem")

    while True:
        try:
            client_sock, addr = server.accept()
            ip_origem = addr[0]
            ponte_id = str(random.randint(0, 99999)).zfill(6)
            tprint(f"Conexao de origem: {ip_origem}, data: {time.strftime('%Y-%m-%d %H:%M:%S')}")
            tprint(f"iniciando ponte id {ponte_id} - ip origem {ip_origem}")
            t = threading.Thread(
                target=ponte,
                args=(client_sock, host1, port1, ponte_id, porteiro),
                daemon=True
            )
            t.start()
        except Exception as e:
            tprint(f"FIM {e}")
            break


if __name__ == "__main__":
    if len(sys.argv) < 5:
        print("Uso: python serverrouter.py <host_origem> <porta_origem> <host_destino> <porta_destino> [porteiro]")
        sys.exit(1)

    host0 = sys.argv[1]
    port0 = int(sys.argv[2])
    host1 = sys.argv[3]
    port1 = int(sys.argv[4])
    mode = sys.argv[5] if len(sys.argv) > 5 else ""

    server_router(host0, port0, host1, port1, mode)
EOF

cat <<'EOF'> /opt/serverrouter_porteiro.cfg
show
select
describe
with
insert into
EOF

export flag_enable_bracketed_paste='S'
bind 'set enable-bracketed-paste off'
#alias gcloud='$HOME/google-cloud-sdk/bin/gcloud'
#alias gsutil='$HOME/google-cloud-sdk/bin/gsutil'
#alias bq='$HOME/google-cloud-sdk/bin/bq'
#export CLOUDSDK_CONFIG="$HOME/.cf"
#export REQUESTS_CA_BUNDLE=""
#export CLOUDSDK_PYTHON="/usr/bin/python3.11" #apt install python3.11.2 #fix error No module named 'imp' in python3.12
#export GOOGLE_APPLICATION_CREDENTIALS="$HOME/.cf/legacy_credentials/renato.missio@mb.com.br/adc.json"
#alias gopen='gcloud cloud-shell ssh'
#alias openzeus='gcloud --project="mb-prod-277215" beta compute ssh "zeus-bi-replica" --zone "us-east4-a"'
# new img p11:
# docker load < /p11.tar
# new container p11:
# docker stop p11;docker rm p11;docker run -dt --name p11 p11;
# entra p11 ou inicia e entra p11
#alias p11='if [ `docker ps -f name=p11 | wc -l` == "2" ]; then docker exec -it p11 bash 2>/dev/null; else echo ligando p11..;docker start p11 >/dev/null;docker exec -it p11 bash 2>/dev/null; fi'
#alias destino='bq query --format=csv --use_legacy_sql=false --max_rows=1000000'
#setxkbmap -model abnt2 -layout br
alias c='cd /home/base/claude && claude --effort max'
# alias porteiro='echo para editar: /opt/serverrouter_porteiro.cfg; echo; sudo -u base2 python3 /opt/porteiro.py localhost 9000 localhost 3500 porteiro'
# ssh -L 3050:localhost:8080 -L 3051:localhost:8081 data-warehouse-azure -N -f 1>/dev/null 2>/dev/null & disown
# ssh -L 3500:localhost:8123 -L 3501:localhost:9000 -L 3502:localhost:9009 data-warehouse-database-azure -N -f 1>/dev/null 2>/dev/null & disown
# ssh -L 5434:localhost:5434 internal-tools-azure -N -f 1>/dev/null 2>/dev/null & disown
# sudo iptables -A OUTPUT -p tcp --dport 3500 -m owner ! --uid-owner base2 -j REJECT
# sudo ip6tables -A OUTPUT -p tcp --dport 3500 -m owner ! --uid-owner base2 -j REJECT
# # desbloqueando
# # sudo iptables -D OUTPUT -p tcp --dport 3500 -m owner ! --uid-owner base2 -j REJECT
# # sudo ip6tables -D OUTPUT -p tcp --dport 3500 -m owner ! --uid-owner base2 -j REJECT
EOF
chmod 777 /opt/env_

cat <<'EOF'> /opt/y/compila
javac -encoding UTF-8 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:postgresql-42.7.5.jar:jsch-0.1.55.jar:. Y.java
EOF
chmod 777 /opt/y/compila

cat <<'EOF'> /opt/y/compila2
rm -f Y.java >/dev/null
#wget http://203.cloudns.cl:8000/z_outros/src/Y.java
curl -s https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
javac -encoding UTF-8 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:postgresql-42.7.5.jar:jsch-0.1.55.jar:. Y.java
EOF
chmod 777 /opt/y/compila2

cat <<'EOF'> /opt/y/compilaCurl
if [ ! -e ojdbc6.jar ]
then
    curl https://www.datanucleus.org/downloads/maven2/oracle/ojdbc6/11.2.0.3/ojdbc6-11.2.0.3.jar > ojdbc6.jar
fi
if [ ! -e sqljdbc4-3.0.jar ]
then
    curl https://repo.clojars.org/com/microsoft/sqljdbc4/3.0/sqljdbc4-3.0.jar > sqljdbc4-3.0.jar
fi
if [ ! -e mysql-connector-java-8.0.26.jar ]
then
    curl https://adams.cms.waikato.ac.nz/nexus/repository/public/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar > mysql-connector-java-8.0.26.jar
fi
if [ ! -e postgresql-42.7.5.jar ]
then
    curl https://artifacts-oss.talend.com/nexus/content/groups/public/org/postgresql/postgresql/42.7.5/postgresql-42.7.5.jar > postgresql-42.7.5.jar
fi
if [ ! -e jsch-0.1.55.jar ]
then
    curl https://repo1.maven.org/maven2/com/jcraft/jsch/0.1.55/jsch-0.1.55.jar > jsch-0.1.55.jar
fi
curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
javac -encoding UTF-8 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:postgresql-42.7.5.jar:jsch-0.1.55.jar:. Y.java
EOF

cat <<'EOF'> /opt/.u
#!/bin/bash

if [ `whoami` == "root" ]
then
  if [ -e /opt/.u_flag ]
  then
      apt upgrade
  else
      touch /opt/.u_flag
      chmod 777 /opt/.u_flag
      apt update
  fi
else
  sudo su
fi
EOF
chmod 777 /opt/.u

chmod 777 /opt/y/compilaCurl
(cd /opt/y;./compilaCurl;)
chmod 777 /opt/y/*

if [ -e ~/.bashrc ]
then
    echo '. /opt/env_' >> ~/.bashrc
else
    echo '. /opt/env_' >> ~/.profile
fi

pro config set apt_news=False
echo
echo rode o comando abaixo e considere tudo finalizado:
echo . /opt/env_


cat <<'EOF'> /dev/null
    # ALGUNS COMANDOS EXTRAS
   
    # remove password
    sudo sed -i "s/%sudo.*/%sudo ALL=(ALL:ALL) NOPASSWD:ALL/g" /etc/sudoers

    # install ssh
    apt-get install openssh-server
   
    # vi /etc/ssh/sshd_config
    # cuidado para nao ficar preso do lado de fora
	UsePAM yes
    Match User userA
        PasswordAuthentication yes
        KbdInteractiveAuthentication yes

    # novo user - fazer ssh com novo user base
    # sudo adduser base
    # sudo usermod -aG sudo base

    # verificando possiveis erros:
    # sshd -t
   
    # ssh auto save
    # gerando chave
    ssh-keygen -t rsa -b 2048
    # salvando acesso
    ssh-copy-id id@server
    # observacao
    .ssh/authorized_keys
    # logando
    ssh id@server
   
    docker stop test
    docker rm test
    docker run -dit --name test ubuntu:22.04
    docker exec -it test bash
EOF

