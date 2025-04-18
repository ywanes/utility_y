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
  echo "apt install openjdk-11-jdk(ou openjdk-17-jdk)"
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
    if [ "$linha" != "" ] && [ "$linha" != "Listing..." ] && [ `echo "$linha" | grep ",now" | wc -l` -eq 0 ]
    then
      p1=`echo $linha | awk ' { print $1 } '`
	  echo $p1
    fi    
  done | head -1 | while read linha
  do
    echo "apt-get install --only-upgrade $linha" > /opt/.u_c
	chmod 777 /opt/.u_c
	echo digite uu
  done
fi
if [ "1" == "1" ] # verify new ubuntu and LTS
then
  if [ `whoami` == "root" ] && [ -e /etc/os-release ]
  then
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
    if [ $((`apt-mark showhold | grep ^libegl-mesa0$ | wc -l`)) == 0 ] && [ $((`dmidecode  | grep -i product | grep VMware | wc -l`)) -ge 1 ]
    then
      echo o comando abaixo foi disparado automaticamente por questao de segurança
      apt-mark hold libegl-mesa0
    fi
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
alias gcloud='$HOME/google-cloud-sdk/bin/gcloud'
alias gsutil='$HOME/google-cloud-sdk/bin/gsutil'
alias bq='$HOME/google-cloud-sdk/bin/bq'
export CLOUDSDK_CONFIG="$HOME/.cf"
export REQUESTS_CA_BUNDLE=""
#export CLOUDSDK_PYTHON="/usr/bin/python3.11" #apt install python3.11.2 #fix error No module named 'imp' in python3.12
export GOOGLE_APPLICATION_CREDENTIALS="$HOME/.cf/legacy_credentials/renato.missio@mb.com.br/adc.json"
alias gopen='gcloud cloud-shell ssh'
alias openzeus='gcloud --project="mb-prod-277215" beta compute ssh "zeus-bi-replica" --zone "us-east4-a"'
# new img p11:
# docker load < /p11.tar
# new container p11:
# docker stop p11;docker rm p11;docker run -dt --name p11 p11;
# entra p11 ou inicia e entra p11
alias p11='if [ `docker ps -f name=p11 | wc -l` == "2" ]; then docker exec -it p11 bash 2>/dev/null; else echo ligando p11..;docker start p11 >/dev/null;docker exec -it p11 bash 2>/dev/null; fi'
alias destino='bq query --format=csv --use_legacy_sql=false --max_rows=1000000'

#setxkbmap -model abnt2 -layout br
EOF
chmod 777 /opt/env_

cat <<'EOF'> /opt/y/compila
javac -encoding UTF-8 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:postgresql-42.7.5.jar:jsch-0.1.55.jar:. Y.java
EOF
chmod 777 /opt/y/compila

cat <<'EOF'> /opt/y/compila2
rm -f Y.java >/dev/null
#wget http://203.cloudns.cl:8000/z_outros/src/Y.java
curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
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

