
# powershell adm
# irm https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/env.ps1 | iex

$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
$validaAdm=$currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if ( ! $validaAdm ){
  echo ""
  echo "Erro... Eh preciso estar no powershell adm"
  echo ""
  pause
  exit
}


New-Item c:/programFiles -ItemType Directory -ea 0
cd c:/programFiles

# https://www.openlogic.com/openjdk-downloads
Invoke-WebRequest -uri "https://builds.openlogic.com/downloadJDK/openlogic-openjdk/8u412-b08/openlogic-openjdk-8u412-b08-windows-x64.zip" -Method "GET"  -Outfile -Outfile java.zip
# teste.. Invoke-WebRequest -uri "https://arch.iit.edu/files/zip/22344/150-dpi.zip" -Method "GET"  -Outfile java.zip
Expand-Archive -LiteralPath 'java.zip'
[Environment]::SetEnvironmentVariable("JAVA_HOME", "c:\programFiles\java\openlogic-openjdk-8u412-b08-windows-64", "Machine")

$NewPATH = ("%JAVA_HOME%\bin;c:\programFiles\java\openlogic-openjdk-8u412-b08-windows-64\bin;" + [Environment]::GetEnvironmentVariable("PATH"))
[Environment]::SetEnvironmentVariable("PATH", $NewPath, [EnvironmentVariableTarget]::Machine)   



New-Item c:/y -ItemType Directory -ea 0
cd c:/y
Invoke-WebRequest -uri "https://www.datanucleus.org/downloads/maven2/oracle/ojdbc6/11.2.0.3/ojdbc6-11.2.0.3.jar" -Method "GET"  -Outfile ojdbc6.jar
Invoke-WebRequest -uri "https://repo.clojars.org/com/microsoft/sqljdbc4/3.0/sqljdbc4-3.0.jar" -Method "GET"  -Outfile sqljdbc4-3.0.jar
Invoke-WebRequest -uri "https://adams.cms.waikato.ac.nz/nexus/repository/public/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar" -Method "GET"  -Outfile mysql-connector-java-8.0.26.jar
Invoke-WebRequest -uri "https://repo1.maven.org/maven2/com/jcraft/jsch/0.1.55/jsch-0.1.55.jar" -Method "GET"  -Outfile jsch-0.1.55.jar
Invoke-WebRequest -uri "https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java" -Method "GET"  -Outfile Y.java
c:\programFiles\java\openlogic-openjdk-8u412-b08-windows-64\bin\javac.exe -encoding UTF-8 -cp "ojdbc6.jar;sqljdbc4-3.0.jar;mysql-connector-java-8.0.26.jar;jsch-0.1.55.jar;." Y.java

Set-Content c:/windows/y.bat '@echo off
(set \n=^^^

^

)
if "%1" equ "echo" (
echo %* | y trataEcho -ignore "Se Vc esta lendo esta msg, significa que ocorreu o bug de " na quantidade impar"
) else (
if "%1" equ "printf" (
echo %* | y trataPrintf -ignore "Se Vc esta lendo esta msg, significa que ocorreu o bug de " na quantidade impar"
) else (
java -Dfile.encoding=UTF-8 -Dline.separator=%\n% -cp c:\\y;c:\\y\\ojdbc6.jar;c:\\y\\sqljdbc4-3.0.jar;c:\\y\\mysql-connector-java-8.0.26.jar;c:\\y\\jsch-0.1.55.jar Y %1 %2 %3 %4 %5 %6 %7 %8 %9
)
)
'

Set-Content c:/windows/cd0.bat '@echo off
set argC=0
for %%x in (%*) do Set /A argC+=1
if "%argC%" equ "0" (
pushd %userprofile%
) else (
pushd %*
)
'

Set-Content config_y.reg 'Windows Registry Editor Version 5.00

[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Command Processor]
"Autorun"="doskey cd=cd0 $* && chcp 65001 && doskey cat=y cat $* && doskey printf=y printf $* && doskey sed=y sed $* && doskey ls=y ls $* && doskey lss=y lss $* && doskey pss=y pss $* && doskey du=y du $*"
'


Set-Content compila2.cmd 'curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
javac -encoding UTF-8 -cp ojdbc6.jar;sqljdbc4-3.0.jar;mysql-connector-java-8.0.26.jar;jsch-0.1.55.jar;. Y.java
'

Set-Content compilaCurl.cmd 'curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
javac -encoding UTF-8 -cp ojdbc6.jar;sqljdbc4-3.0.jar;mysql-connector-java-8.0.26.jar;jsch-0.1.55.jar;. Y.java
pause
'


reg import ./config_y.reg

echo "FIM!"

