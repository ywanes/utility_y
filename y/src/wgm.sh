# WGetMinecraft
# curl  https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/wgm.sh | bash
# testado no mac

d=`ps -ef | grep "net.minecraft.client.main.Main" | grep -v grep | sed 's/ Launcher /_/g' | sed 's/Library\/Application Support/minecraft\/client/g' | sed 's/\/java /\n/g' | tail -1`
if [ $((`echo $d | wc -l`)) -eq 0 ]
then
  echo Erro, nao foi possivel encontrar o minecraft aberto
  exit 1
fi
nick=`echo $d | sed 's/--username/\n/g' | tail -1 | sed 's/--version/\n/g' | head -1 | sed -e 's/^[[:space:]]*//'`
d=`echo $d | sed 's/--username '$nick'/--username $nick /g'`

mkdir ~/minecraft
mkdir ~/minecraft/client
cp -R ~"/Library/Application Support/minecraft" ~/minecraft/client
echo '#!/bin/bash' > ~/minecraft/client/minecraft.sh
echo 'nick='$nick >> ~/minecraft/client/minecraft.sh
echo 'java '$d >> ~/minecraft/client/minecraft.sh
chmod 777 ~/minecraft/client/minecraft.sh

mkdir ~/minecraft/server
version=`echo $d | sed 's/--version/\n/g' | tail -2 | head -1 | sed 's/ /\n/g' | head -2 | tail -1`
e=`curl https://mcversions.net/download/$version | tr ' ' '\n' | tr '"' '\n' | grep server.jar`
curl $e --output ~/minecraft/server/server.jar
echo 'executar o server.bat pela primeira vez, depois alterar o arquivo eula.txt preenchendo eula=true e alterar o arquivo server.properties preenchendo online-mode=false' > ~/minecraft/server/informacoes.txt

echo fim!
echo comando para rodar o minecraft:
echo '~/minecraft/client/minecraft.sh'

