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
  echo "apt install openjdk-8-jdk(ou openjdk-11-jdk)"
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
alias y='java -Dfile.encoding=ISO-8859-1 -cp /opt/y:/opt/y/ojdbc6.jar:/opt/y/sqljdbc4-3.0.jar:/opt/y/mysql-connector-java-8.0.26.jar:/opt/y/jsch-0.1.55.jar:. Y'
rm -f /opt/.u_flag
alias u='/opt/.u'
EOF
chmod 777 /opt/env_

cat <<'EOF'> /opt/y/compila
javac -encoding ISO-8859-1 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:jsch-0.1.55.jar:. Y.java
EOF
chmod 777 /opt/y/compila

cat <<'EOF'> /opt/y/compilaCurl
curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
javac -encoding ISO-8859-1 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:jsch-0.1.55.jar:. Y.java
EOF
chmod 777 /opt/y/compilaCurl

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

cd /opt/y
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
    curl http://121.42.227.72:8081/nexus/content/groups/public/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar > mysql-connector-java-8.0.26.jar
fi
if [ ! -e jsch-0.1.55.jar ]
then
    curl https://ufpr.dl.sourceforge.net/project/jsch/jsch.jar/0.1.55/jsch-0.1.55.jar > jsch-0.1.55.jar
fi
curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
javac -encoding ISO-8859-1 -cp .:ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:jsch-0.1.55.jar Y.java
chmod 777 *

if [ -e ~/.bashrc ]
then
    echo '. /opt/env_' >> ~/.bashrc
else
    if [ -e ~/.profile ]
    then
        echo '. /opt/env_' >> ~/.profile
    fi
fi

echo ""
echo rode o comando abaixo e considere tudo finalizado:
echo . /opt/env_


cat <<'EOF'> /dev/null
    # ALGUNS COMANDOS EXTRAS
   
    # remove password
    sudo sed -i "s/%sudo.*/%sudo ALL=(ALL:ALL) NOPASSWD:ALL/g" /etc/sudoers

    # install ssh
    apt-get install openssh-server
   
    # vi /etc/ssh/sshd_config
	UsePAM yes
    Match User userA
        PasswordAuthentication yes
        KbdInteractiveAuthentication yes

    # add user
    sudo adduser username
   
    # add group sudoers
    sudo usermod -aG sudo username
   
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

