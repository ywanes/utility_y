/*
    curl https://www.datanucleus.org/downloads/maven2/oracle/ojdbc6/11.2.0.3/ojdbc6-11.2.0.3.jar > ojdbc6.jar
    curl https://repo.clojars.org/com/microsoft/sqljdbc4/3.0/sqljdbc4-3.0.jar > sqljdbc4-3.0.jar
    curl http://121.42.227.72:8081/nexus/content/groups/public/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar > mysql-connector-java-8.0.26.jar
    curl https://ufpr.dl.sourceforge.net/project/jsch/jsch.jar/0.1.55/jsch-0.1.55.jar > jsch-0.1.55.jar
    curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
    javac -encoding UTF-8 -cp ojdbc6.jar:sqljdbc4-3.0.jar:mysql-connector-java-8.0.26.jar:jsch-0.1.55.jar:. Y.java
    javac -encoding UTF-8 -cp ojdbc6.jar;sqljdbc4-3.0.jar;mysql-connector-java-8.0.26.jar;jsch-0.1.55.jar;. Y.java
    alias y='java -Dfile.encoding=UTF-8 -cp /y:/y/ojdbc6.jar:/y/sqljdbc4-3.0.jar:/y/mysql-connector-java-8.0.26.jar:/y/jsch-0.1.55.jar:. Y'
    java -XshowSettings 2>&1 | grep "file.encoding "
    configurando terminal windows cmd -> chcp 65001
    rc linux -> echo $?
    rc windows -> echo %ERRORLEVEL%
    Y.bat(c:/windows/system32)
@echo off
(set \n=^^^

^

)
if "%1" equ "echo2" (
echo %*
) else (
java -Dfile.encoding=UTF-8 -Dline.separator=%\n% -cp c:\\y;c:\\y\\ojdbc6.jar;c:\\y\\sqljdbc4-3.0.jar;c:\\y\\jsch-0.1.55.jar Y %1 %2 %3 %4 %5 %6 %7 %8 %9
)
    créditos "ssh/scp/sftp/sshExec" https://ufpr.dl.sourceforge.net/project/jsch/jsch.jar/0.1.55/jsch-0.1.55.jar 
    créditos https://github.com/is/jsch/tree/master/examples
*/

import com.jcraft.jsch.*;
import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.security.Security;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;


public class Y extends Util{    
    //public static String local_env=null;
    public static String local_env="c:\\tmp";
    
    public static String linhaCSV=null;
    public static int ponteiroLinhaCSV=0;        
    public static int n_lines_buffer_DEFAULT=4000;        
    public String [] ORAs=new String[]{};
    public String [] suportIconv=new String[]{"ISO-8859-1","UTF-8","UTF-8BOM","UCS-2LE","UCS-2LEBOM"};
    public int [] BOM_UTF_8=new int[]{239,187,191};    
    public int [] BOM_UCS_2LE=new int[]{255,254};        
    public String erroSequenciaIlegal="Erro, sequencia ilegal!";
    
    // octal bytes
    public static String [] OD_BC_B=new String[]{ " 000"," 001"," 002"," 003"," 004"," 005"," 006"," 007"," 010"," 011",
        " 012"," 013"," 014"," 015"," 016"," 017"," 020"," 021"," 022"," 023"," 024"," 025"," 026"," 027"," 030"," 031",
        " 032"," 033"," 034"," 035"," 036"," 037"," 040"," 041"," 042"," 043"," 044"," 045"," 046"," 047"," 050"," 051",
        " 052"," 053"," 054"," 055"," 056"," 057"," 060"," 061"," 062"," 063"," 064"," 065"," 066"," 067"," 070"," 071",
        " 072"," 073"," 074"," 075"," 076"," 077"," 100"," 101"," 102"," 103"," 104"," 105"," 106"," 107"," 110"," 111",
        " 112"," 113"," 114"," 115"," 116"," 117"," 120"," 121"," 122"," 123"," 124"," 125"," 126"," 127"," 130"," 131",
        " 132"," 133"," 134"," 135"," 136"," 137"," 140"," 141"," 142"," 143"," 144"," 145"," 146"," 147"," 150"," 151",
        " 152"," 153"," 154"," 155"," 156"," 157"," 160"," 161"," 162"," 163"," 164"," 165"," 166"," 167"," 170"," 171",
        " 172"," 173"," 174"," 175"," 176"," 177"," 200"," 201"," 202"," 203"," 204"," 205"," 206"," 207"," 210"," 211",
        " 212"," 213"," 214"," 215"," 216"," 217"," 220"," 221"," 222"," 223"," 224"," 225"," 226"," 227"," 230"," 231",
        " 232"," 233"," 234"," 235"," 236"," 237"," 240"," 241"," 242"," 243"," 244"," 245"," 246"," 247"," 250"," 251",
        " 252"," 253"," 254"," 255"," 256"," 257"," 260"," 261"," 262"," 263"," 264"," 265"," 266"," 267"," 270"," 271",
        " 272"," 273"," 274"," 275"," 276"," 277"," 300"," 301"," 302"," 303"," 304"," 305"," 306"," 307"," 310"," 311",
        " 312"," 313"," 314"," 315"," 316"," 317"," 320"," 321"," 322"," 323"," 324"," 325"," 326"," 327"," 330"," 331",
        " 332"," 333"," 334"," 335"," 336"," 337"," 340"," 341"," 342"," 343"," 344"," 345"," 346"," 347"," 350"," 351",
        " 352"," 353"," 354"," 355"," 356"," 357"," 360"," 361"," 362"," 363"," 364"," 365"," 366"," 367"," 370"," 371",
        " 372"," 373"," 374"," 375"," 376"," 377"};
    // caracteres
    public static String [] OD_BC_C=new String[]{ "  \\0"," 001"," 002"," 003"," 004"," 005"," 006","  \\a","  \\b","  \\t",
        "  \\n","  \\v","  \\f","  \\r"," 016"," 017"," 020"," 021"," 022"," 023"," 024"," 025"," 026"," 027"," 030"," 031",
        " 032"," 033"," 034"," 035"," 036"," 037","    ","   !","   \"","   #","   $","   %","   &","   '","   (","   )",
        "   *","   +","   ,","   -","   .","   /","   0","   1","   2","   3","   4","   5","   6","   7","   8","   9",
        "   :","   ;","   <","   =","   >","   ?","   @","   A","   B","   C","   D","   E","   F","   G","   H","   I",
        "   J","   K","   L","   M","   N","   O","   P","   Q","   R","   S","   T","   U","   V","   W","   X","   Y",
        "   Z","   [","   \\","   ]","   ^","   _","   `","   a","   b","   c","   d","   e","   f","   g","   h","   i",
        "   j","   k","   l","   m","   n","   o","   p","   q","   r","   s","   t","   u","   v","   w","   x","   y"
            ,"   z","   {","   |","   }","   ~"," 177"," 200"," 201"," 202"," 203"," 204"," 205"," 206"," 207"," 210"
            ," 211"," 212"," 213"," 214"," 215"," 216"," 217"," 220"," 221"," 222"," 223"," 224"," 225"," 226"," 227"
            ," 230"," 231"," 232"," 233"," 234"," 235"," 236"," 237"," 240"," 241"," 242"," 243"," 244"," 245"," 246"
            ," 247"," 250"," 251"," 252"," 253"," 254"," 255"," 256"," 257"," 260"," 261"," 262"," 263"," 264"," 265"
            ," 266"," 267"," 270"," 271"," 272"," 273"," 274"," 275"," 276"," 277"," 300"," 301"," 302"," 303"," 304",
            " 305"," 306"," 307"," 310"," 311"," 312"," 313"," 314"," 315"," 316"," 317"," 320"," 321"," 322"," 323",
            " 324"," 325"," 326"," 327"," 330"," 331"," 332"," 333"," 334"," 335"," 336"," 337"," 340"," 341"," 342",
            " 343"," 344"," 345"," 346"," 347"," 350"," 351"," 352"," 353"," 354"," 355"," 356"," 357"," 360"," 361",
            " 362"," 363"," 364"," 365"," 366"," 367"," 370"," 371"," 372"," 373"," 374"," 375"," 376"," 377"};
    // 0...256
    public static String [] OD_BC_R=new String[]{"   0","   1","   2","   3","   4","   5","   6","   7","   8","   9",
        "  10","  11","  12","  13","  14","  15","  16","  17","  18","  19","  20","  21","  22","  23","  24","  25",
        "  26","  27","  28","  29","  30","  31","  32","  33","  34","  35","  36","  37","  38","  39","  40","  41",
        "  42","  43","  44","  45","  46","  47","  48","  49","  50","  51","  52","  53","  54","  55","  56","  57",
        "  58","  59","  60","  61","  62","  63","  64","  65","  66","  67","  68","  69","  70","  71","  72","  73",
        "  74","  75","  76","  77","  78","  79","  80","  81","  82","  83","  84","  85","  86","  87","  88","  89",
        "  90","  91","  92","  93","  94","  95","  96","  97","  98","  99"," 100"," 101"," 102"," 103"," 104"," 105",
        " 106"," 107"," 108"," 109"," 110"," 111"," 112"," 113"," 114"," 115"," 116"," 117"," 118"," 119"," 120"," 121",
        " 122"," 123"," 124"," 125"," 126"," 127"," 128"," 129"," 130"," 131"," 132"," 133"," 134"," 135"," 136"," 137",
        " 138"," 139"," 140"," 141"," 142"," 143"," 144"," 145"," 146"," 147"," 148"," 149"," 150"," 151"," 152"," 153",
        " 154"," 155"," 156"," 157"," 158"," 159"," 160"," 161"," 162"," 163"," 164"," 165"," 166"," 167"," 168"," 169",
        " 170"," 171"," 172"," 173"," 174"," 175"," 176"," 177"," 178"," 179"," 180"," 181"," 182"," 183"," 184"," 185",
        " 186"," 187"," 188"," 189"," 190"," 191"," 192"," 193"," 194"," 195"," 196"," 197"," 198"," 199"," 200"," 201",
        " 202"," 203"," 204"," 205"," 206"," 207"," 208"," 209"," 210"," 211"," 212"," 213"," 214"," 215"," 216"," 217",
        " 218"," 219"," 220"," 221"," 222"," 223"," 224"," 225"," 226"," 227"," 228"," 229"," 230"," 231"," 232"," 233",
        " 234"," 235"," 236"," 237"," 238"," 239"," 240"," 241"," 242"," 243"," 244"," 245"," 246"," 247"," 248"," 249",
        " 250"," 251"," 252"," 253"," 254"," 255"};
    // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=
    public static int [] indexBase64 = new int []{65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,
        89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,48,49,50,
        51,52,53,54,55,56,57,43,47};
    // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=
    public static String txtBase64="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
   
    
    int BARRA_R=13;     // \r
    int CHAR_R=114;     // r
    int BARRA_N=10;     // \n
    int CHAR_N=110;     // n
    
    int BARRA_0=0;      // \0
    int CHAR_0=48;      // 0
    int BARRA_A=7;      // \a
    int CHAR_A=97;      // a
    int BARRA_B=8;      // \b
    int CHAR_B=98;      // b
    int BARRA_T=9;      // \t
    int CHAR_T=116;     // t
    int BARRA_V=11;     // \v
    int CHAR_V=118;     // v
    int BARRA_F=12;     // \f
    int CHAR_F=102;     // f
    int CHAR_BARRA=92; // \\ => \
   
    int V_0b00000011=3; // 0b00000011 (3)
    int V_0b0000000011=3; // 0b0000000011 (3)
    int V_0b0000001111=15; // 0b0000001111 (15)
    int V_0b000000001111=15; // 0b000000001111 (15)
    int V_0b000000111111=63; // 0b000000111111 (63)
    int V_0b11111100=252; // 0b11111100 (252)
    int V_0b1111110000=1008; // 0b1111110000 (1008)
    int V_0b1111111100=1020; // 0b1111111100 (1020)
    int V_0b111111000000=4032; // 0b111111000000 (4032)
    int V_0b111111110000=4080; // 0b111111110000 (4080)    
    
    public static void main(String[] args) {

/*
# criando tabela a com 400k registros
y banco conn,hash execute "drop table a"
y banco conn,hash execute "create table a ( C1 varchar2(3000), C2 varchar2(3000), C3 varchar2(3000) )"
y banco conn,hash execute "insert into a values('123456789','123456789','123456789')"
y echo "select a.* from a,(select level from dual connect by level <= 399999)" | y banco connIn,hash connOut,hash -outTable a carga
y banco conn,hash select "select count(1) from a"
# 34k linhas/s - table to table
# y banco $conn execute "create table a ( C1 varchar2(3000), C2 varchar2(3000), C3 varchar2(3000) )"
y banco conn,desktop execute "drop table b"
y banco conn,desktop execute "create table b as select * from a where rownum <= 1"
y banco conn,desktop selectInsert "select b.* from b,(select level from dual connect by level <= 160000)" | y banco buffer -n_line 500 -log buffer.log | y banco conn,desktop executeInsert
cat buffer.log
# y banco conn,desktop select "select count(1) from b"
# 65k linhas/s - lendo arquivo
y cat a a a | y banco buffer -n_lines 500 -log buffer.log | y dev_null
cat buffer.log
# 4k linhas/s - gravando arquivo
y cat a | y banco buffer -n_lines 500 -log buffer.log > aa
cat buffer.log
# 34k linhas/s - table to null               
y banco conn,desktop selectInsert "select * from a" | y banco buffer -n_lines 500 -log buffer.log | y dev_null
cat buffer.log
# 59k linhas/s - file to table
y banco conn,desktop execute "drop table b"
y banco conn,desktop execute "create table b as select * from a where rownum <= 1"
y cat b | y banco buffer -n_lines 500 -log buffer.log | y banco conn,desktop executeInsert
cat buffer.log
*/
        //teste
        //y serverRouter 192.168.0.100 8080 localhost 9090 show        
        //args=new String[]{"serverRouter","192.168.0.100","25565","192.168.0.200","25565","show"};        
        //args=new String[]{"serverRouter","192.168.0.100","25565","192.168.0.200","25565"};             
        
        //args=new String[]{"xlsxToCSV","C:\\tmp\\aa\\a.xlsx","nomeAba","Gestão de Mud (CITSD_change)"};                
        //args=new String[]{"xlsxToCSV","C:\\tmp\\aa\\a.xlsx","nomeAba","N A (AccountLeads)"};
        //args=new String[]{"xlsxToCSV","C:\\tmp\\aa\\a.xlsx","exportAll"};                                        
        
        //args=new String[]{"selectCSV","-csv","c:\\tmp\\tmp\\a.csv","select CAMPO2, CAMPO2 from this"};                                        
        
        //args=new String[]{"xlsxToCSV","C:\\tmp\\aa\\a.xlsx","mostraEstrutura"};
        //args=new String[]{"xlsxToCSV","C:\\tmp\\aa\\a.xlsx","numeroAba","1"};
        //args=new String[]{"xml","C:\\tmp\\aa\\sheet438.xml","mostraTags"};        
        //args=new String[]{"xlsxToCSV","C:\\tmp\\tmp\\012020.xlsx","exportAll"};
        //args=new String[]{"xlsxToCSV","C:\\tmp\\tmp\\012020.xlsx","numeroAba","1"};        
        //args=new String[]{"xlsxToCSV","C:\\tmp\\tmp\\012020.xlsx","mostraEstrutura"};
        //args=new String[]{"find", "/"};
        //Util.testOn(); args=new String[]{"json", "[elem for elem in data['items']]"};        
        //Util.testOn(); args=new String[]{"json", "[elem for elem in data['b']]"};        
        //Util.testOn(); args=new String[]{"json", "mostraEstrutura"};
        //Util.testOn(); args=new String[]{"json", "mostraEstruturaObs"};
        //Util.testOn(); args=new String[]{"json", "mostraTabela"};
        //args=new String[]{"regua"};                
        //args=new String[]{"find", ".", "-mtime", "1"};                
        //args=new String[]{"date","+%m/%d/%Y","%H:%M:%S:%N","%Z","%s"};
        //args=new String[]{"curl","-v","https://www.youtube.com"};
        //args=new String[]{"curl","-v","http://dota.freeoda.com/100/"};
        
        new Y().go(args);
    }

    public void go(String[] args){    
        System.setProperty("https.protocols", "TLSv1.1");
        System.setProperty("line.separator", "\n");
        try_load_libraries();
        try_load_ORAs();

        if ( args.length == 0 ){
            System.err.println(      
                somente_mini("/y/manual")
            );
            return;
        }
        if ( args[0].equals("banco") ){            
            if ( args.length == 1 ){
                System.err.println(
                    somente_banco("/y/manual")
                );            
                return;
            }

            // fromCSV
            if ( args.length == 5 && args[1].equals("fromCSV") && args[2].equals("-outTable") && args[4].equals("selectInsert") ){
                selectInsert("","","",null,args[3],"");
                return;
            }
            
            // conn
            if ( args[1].equals("-conn") || ( args[1].startsWith("conn,") ) ){
                String [] ConnAppParm=getConnAppParm(args);

                if ( ConnAppParm == null ){
                    comando_invalido(args);
                    return;
                }

                String conn=ConnAppParm[0];
                String app=ConnAppParm[1];
                String parm=ConnAppParm[2];

                // comandos app
                if ( app.equals("select") ){
                    select(conn,parm);
                    return;
                }
                if ( app.equals("selectInsert") ){
                    selectInsert(conn,"",parm,null,"","");
                    return;
                }
                if ( app.equals("selectCSV") ){
                    selectCSV(conn,parm);
                    return;
                }
                if ( app.equals("executeInsert") ){
                    executeInsert(conn,System.in);
                    return;
                }
                if ( app.equals("execute") ){
                    execute(conn,parm);
                    return;
                }       
                if ( app.equals("createjobexecute") ){
                    try{
                        createjobexecute(conn);
                    }catch(Exception e){
                        System.err.println(e.toString());
                        System.exit(1);
                    }
                    return;
                }
            }
            // connIn/fileCSV
            if ( args[1].equals("-connIn") || args[1].startsWith("connIn,") || args[1].equals("-fileCSV") || args[1].startsWith("fileCSV,") ){
                String [] connIn_fileCSV_connOut_outTable_trunc_app=get_connIn_fileCSV_connOut_outTable_trunc_app(args);
                if ( connIn_fileCSV_connOut_outTable_trunc_app == null ){
                    comando_invalido(args);
                    return;
                }
                String connIn=connIn_fileCSV_connOut_outTable_trunc_app[0];
                String fileCSV=connIn_fileCSV_connOut_outTable_trunc_app[1];
                String connOut=connIn_fileCSV_connOut_outTable_trunc_app[2];
                String outTable=connIn_fileCSV_connOut_outTable_trunc_app[3];
                String trunc=connIn_fileCSV_connOut_outTable_trunc_app[4];
                String app=connIn_fileCSV_connOut_outTable_trunc_app[5];

                //[y banco connIn,hash connOut,hash outTable,tabelaA carga]
                //[y banco connIn,hash connOut,hash outTable,tabelaA trunc carga]
                //[y banco connIn,hash connOut,hash outTable,tabelaA createjobcarga]
                //[y banco connIn,hash connOut,hash outTable,tabelaA trunc createjobcarga]

                if ( app.equals("carga") )
                {
                    carga(connIn,fileCSV,connOut,outTable,trunc);
                    return;
                }
                if ( app.equals("createjobcarga") )
                {
                    try{
                        createjobcarga(connIn,fileCSV,connOut,outTable,trunc,app);
                    }catch(Exception e){
                        System.err.println(e.toString());
                        System.exit(1);
                    }
                    return;
                }
            }
            // executejob
            if ( args[1].equals("executejob") && args.length == 2 ){
                executejob();
                return;
            }
            // buffer
            if ( args[1].equals("buffer") 
                && (
                    ( args.length == 2 )
                    || ( args.length == 4 && args[2].equals("-n_lines") )
                    || ( args.length == 4 && args[2].equals("-log") )
                    || ( args.length == 6 && args[2].equals("-n_lines") && args[4].equals("-log") )
                )
            ){
                buffer(args);
                return;
            }

            comando_invalido(args);
            return;
        }
        
        if ( args[0].equals("selectCSV") ){
            try{
                selectCSV(args);
                return;
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
        }
        
        if ( args[0].equals("xlsxToCSV") && args.length >= 3 ){
            //args=new String[]{"xlsxToCSV","teste.xlsx","mostraEstrutura"};
            //args=new String[]{"xlsxToCSV","teste.xlsx","listaAbas"};
            //args=new String[]{"xlsxToCSV","teste.xlsx","numeroAba","1"};
            //args=new String[]{"xlsxToCSV","teste.xlsx","nomeAba","A"};

            try{
                if ( new File(args[1]).exists() ){
                    if ( args.length == 3 && args[2].equals("mostraEstrutura") ){
                        xlsxToCSV(args[1],true,false,-1,"",System.out,false);
                        return;
                    }
                    if ( args.length == 3 && args[2].equals("listaAbas") ){
                        xlsxToCSV(args[1],false,true,-1,"",System.out,false);
                        for ( int i=0;i<xlsxToCSV_nomes.size();i++ )
                            System.out.println(xlsxToCSV_nomes.get(i));
                        return;
                    }
                    if ( args.length == 3 && args[2].equals("exportAll") ){
                        xlsxToCSV(args[1],false,true,-1,"",null,false);
                        ArrayList<String> bkp_lista=xlsxToCSV_nomes;
                        OutputStream out=null;
                        boolean suprimeHeader=false;
                        boolean config_SystemOuput=true;
                        String abaSequencial=get_abaSequencial(bkp_lista);
                        if ( abaSequencial != null ){
                            if ( config_SystemOuput ){
                                out=System.out;
                            }else{
                                System.out.println("exportando "+abaSequencial+".csv");
                                out=new FileOutputStream(abaSequencial+".csv");
                            }
                            for ( int i=0;i<bkp_lista.size();i++ ){
                                if ( i == 0)
                                    suprimeHeader=false;
                                else
                                    suprimeHeader=true;
                                xlsxToCSV(args[1],false,false,-1,bkp_lista.get(i),out,suprimeHeader);
                            }                            
                            if ( out != null ){
                                out.flush();
                                out.close();
                            }
                        }else{
                            for ( int i=0;i<bkp_lista.size();i++ ){
                                System.out.println("exportando("+(i+1)+"/"+bkp_lista.size()+") arquivo: "+bkp_lista.get(i)+".csv");
                                out=new FileOutputStream(bkp_lista.get(i)+".csv");
                                xlsxToCSV(args[1],false,false,-1,bkp_lista.get(i),out,false);
                                if ( out != null ){
                                    out.flush();
                                    out.close();
                                }
                            }
                        }
                        return;
                    }
                    if ( args.length == 4 && args[2].equals("numeroAba") ){
                        try{
                            xlsxToCSV(args[1],false,false,Integer.parseInt(args[3]),"",System.out,false);
                            return;
                        }catch(Exception e){}
                    }
                    if ( args.length == 4 && args[2].equals("nomeAba") && args[3].length() > 0 ){
                        xlsxToCSV(args[1],false,false,-1,args[3],System.out,false);
                        return;
                    }
                }
            }catch(Exception e){
                System.err.println("Erro, "+e.toString());
                System.exit(1);
            }
        }   
        
        if ( args[0].equals("xml") && ( args.length == 2 || args.length == 3 ) ){
            try{
                if ( args.length == 2 && args[1].equals("mostraEstrutura") ){
                    XML.loadIs(System.in,true,false,null,false,null,false);
                    return;
                }            
                if ( args.length == 2 && args[1].equals("mostraTags") ){
                    XML.loadIs(System.in,false,true,null,false,null,false);
                    return;
                }            
                if ( args.length == 3 && new File(args[1]).exists() && args[2].equals("mostraEstrutura") ){
                    FileInputStream is = new FileInputStream(args[1]);
                    XML.loadIs(is,true,false,null,false,null,false);
                    is.close();                
                    return;
                }            
                if ( args.length == 3 && new File(args[1]).exists() && args[2].equals("mostraTags") ){
                    FileInputStream is = new FileInputStream(args[1]);
                    XML.loadIs(is,false,true,null,false,null,false);
                    is.close();                
                    return;                
                }          
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
        }
        
        if ( args[0].equals("token") ){
            if ( args.length == 1 ){
                System.err.println("usage:"
                + "\n  [y token value]"
                + "\n  return hash"
                + "\n  [y gettoken hash]"
                + "\n  return value");
                return;
            }
            String dir_token=getenv();
            if ( ! env_ok(dir_token) )
                return;
            String value=args[1];
            String hash=salvando_token(dir_token,value);
            if ( hash == null ){
                System.err.println("Não foi possível utilizar a pasta "+dir_token);
                return;
            }
            System.out.println(hash);
            return;
        }
        if ( args[0].equals("gettoken") ){
            if ( args.length == 1 ){
                System.err.println("usage:"
                + "\n  [y token value]"
                + "\n  return hash"
                + "\n  [y gettoken hash]"
                + "\n  return value");
                return;
            }
            String value=gettoken(args[1]);
            if ( value == null )
            {
                System.err.println("Não foi possível encontrar o token "+args[1]);
                return;
            }
            System.out.println(value);
            return;
        }
        
        if ( args[0].equals("json") && args.length > 1 ){
            String parm=args[1];
            boolean list_on=false;
            if ( args.length > 2 && args[1].equals("list") ){
                list_on = true;
                parm=args[2];
            }                
            boolean mostraTabela=parm.equals("mostraTabela");
            boolean mostraEstrutura=parm.equals("mostraEstrutura");            
            boolean mostraEstruturaObs=parm.equals("mostraEstruturaObs");
            String command=parm.contains("for elem in data")?parm:"";
            if ( !command.equals("") || mostraTabela || mostraEstrutura || mostraEstruturaObs ){
                if ( new JSON().go(command,mostraTabela,mostraEstrutura,mostraEstruturaObs,list_on) ){
                    return;
                }
            }
        }
            
        /*
        y zip add File1.txt > saida.zip
        cat File1.txt | y zip add -name File1.txt > saida.zip
        y zip add /pasta1 > saida.zip
        y zip list arquivo.zip
        cat arquivo.zip | y zip list
        y zip extract entrada.zip
        cat entrada.zip | y zip extract
        y zip extract entrada.zip -out /destino
        cat entrada.zip | y zip extract -out /destino
        y zip extractSelected entrada.zip pasta1/unicoArquivoParaExtrair.txt -out /destino
        cat entrada.zip | y zip extractSelected pasta1/unicoArquivoParaExtrair.txt -out /destino
        y zip extractSelected entrada.zip pasta1/unicoArquivoParaExtrair.txt > /destino/unicoArquivoParaExtrair.txt
        cat entrada.zip | y zip extractSelected pasta1/unicoArquivoParaExtrair.txt > /destino/unicoArquivoParaExtrair.txt
        */
        if ( args[0].equals("zip") ){
            try{
                if ( args.length == 2 && args[1].equals("add") ){
                    zip_add(null,null);
                    return;
                }
                if ( args.length == 3 && args[1].equals("add") ){
                    zip_add(args[2],null);
                    return;
                }
                if ( args.length == 4 && args[1].equals("add") && args[2].equals("-name")){
                    zip_add(null,args[3]);
                    return;
                }
                if ( args.length == 2 && args[1].equals("list") ){
                    zip_list(null);
                    return;
                }
                if ( args.length == 3 && args[1].equals("list") ){
                    zip_list(args[2]);
                    return;
                }
                
                if ( args.length == 2 && args[1].equals("extract") ){
                    zip_extract(null,null,null);
                    return;
                }
                if ( args.length == 3 && args[1].equals("extract") ){
                    zip_extract(args[2],null,null);
                    return;
                }
                if ( args.length == 4 && args[1].equals("extract") && args[2].equals("-out")){
                    zip_extract(null,args[3],null);
                    return;
                }
                if ( args.length == 5 && args[1].equals("extract") && args[3].equals("-out")){
                    zip_extract(args[2],args[4],null);
                    return;
                }
                if ( args.length == 3 && args[1].equals("extractSelected") ){
                    zip_extract(null,null,args[2]);
                    return;
                }
                if ( args.length == 4 && args[1].equals("extractSelected") ){
                    zip_extract(args[2],null,args[3]);
                    return;
                }
                if ( args.length == 5 && args[1].equals("extractSelected") && args[3].equals("-out")){
                    zip_extract(null,args[4],args[2]);
                    return;
                }
                if ( args.length == 6 && args[1].equals("extractSelected") && args[4].equals("-out")){
                    zip_extract(args[2],args[5],args[3]);
                    return;
                }
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
        }
        if ( args[0].equals("gzip") ){
            gzip();
            return;
        }        
        if ( args[0].equals("gunzip") ){
            gunzip();
            return;
        }   
        if ( args[0].equals("tar") && args.length == 2 ){
            try{
                new Tar().tar(null, args[1]);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        if ( args[0].equals("tar") && args.length == 3 ){
            try{
                new Tar().tar(args[1], args[2]);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        if ( args[0].equals("untar") && args.length == 1 ){
            try{
                new Tar().untar(null, null);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        if ( args[0].equals("untar") && args.length == 2 ){
            try{
                new Tar().untar(args[1], null);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        if ( args[0].equals("untar") && args.length == 3 ){
            try{
                new Tar().untar(args[1], args[2]);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        if ( args[0].equals("tarlist") && args.length == 2 ){
            try{
                new Tar().tarlist(args[1]);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        if ( args[0].equals("echo") ){
            echo(args);
            return;
        }      
        if ( args[0].equals("printf") ){
            printf(args);
            return;
        }              
        if ( args[0].equals("cat") ){
            cat(args);
            return;
        }        
        if ( args[0].equals("xor") && args.length <= 2 ){
            try{
                Integer parm=100;
                if ( args.length == 2 )
                    Integer.parseInt(args[1]);
                xor(parm);
                return;
            }catch(Exception e){}            
        }        
        if ( args[0].equals("md5") ){
            digest("MD5");
            return;
        }        
        if ( args[0].equals("sha1") ){
            digest("SHA-1");
            return;
        }        
        if ( args[0].equals("sha256") ){
            digest("SHA-256");
            return;
        }        
        if ( args[0].equals("aes") && args.length > 1 ){
            String[] senha_isEncoding_md_salt=get_senha_isEncoding_md_salt(args);
            if ( senha_isEncoding_md_salt != null ){
                aes(senha_isEncoding_md_salt[0],senha_isEncoding_md_salt[1].equals("S"),senha_isEncoding_md_salt[2],senha_isEncoding_md_salt[3]==null?null:hexTobytes(senha_isEncoding_md_salt[3]));
                return;
            }
        }        
        if ( args[0].equals("base64") 
            && ( 
                args.length == 1 
                || ( args.length == 2 && args[1].equals("-e") )
                || ( args.length == 2 && args[1].equals("-d") )
                || ( args.length == 3 && args[1].equals("-e") && args[2].length() > 0 )
                || ( args.length == 3 && args[1].equals("-d") && args[2].length() > 0 )
            )    
        ){
            try{
                if ( args.length == 1 ){
                    base64(System.in,System.out,true);
                    return;
                }
                boolean encoding=args[1].equals("-e");
                if ( args.length == 2 ){
                    base64(System.in,System.out,encoding);
                    return;
                }
                if ( args.length == 3 ){
                    System.out.println(
                        base64(args[2], encoding)
                    );
                    return;
                }
                System.err.println("Erro inesperado!");
                System.exit(1);
            }catch(Exception e){
                System.err.println(e.toString());
                System.exit(1);
            }
            return;
        }
        
        if ( args[0].equals("grep") ){
            grep(args);
            return;
        }        
        if ( args.length == 2 && args[0].equals("wc") && args[1].equals("-l") ){
            wc_l();
            return;
        }       
        if ( args.length == 1 && args[0].equals("len")){
            len();
            return;
        }       
        if ( args[0].equals("head") 
            && (
                args.length == 1 
                || ( args.length == 2 && args[1].startsWith("-") && args[1].length() > 1 )
            ) 
        ){
            head(args);
            return;
        }
        if ( args[0].equals("tail") 
            && (
                args.length == 1 
                || ( args.length == 2 && args[1].startsWith("-") && args[1].length() > 1 )
            ) 
        ){
            tail(args);
            return;
        }
        if ( args[0].equals("cut") && args.length == 2 && args[1].startsWith("-c") && args[1].length() > 2 ){
            cut(args);
            return;
        }
        if ( args[0].equals("curl") ){
            if ( curl(args) ){
                return;
            }
        }
        if ( args[0].equals("sed") || args[0].equals("tr") ){
            if ( args.length == 3 ){
                sed(args);
                return;
            }
            if ( args.length%2 == 1 ){
                sedBasic(args);
                return;
            }
        }
        if ( args[0].equals("n") ){
            n();
            return;
        }
        if ( args[0].equals("rn") ){
            rn();
            return;
        }
        if ( args[0].equals("bytesToInts") || args[0].equals("bi") ){
            boolean dif_128=false;
            if ( args.length > 1 && args[1].equals("-128") )
                dif_128=true;
            bytesToInts(dif_128);
            return;
        }       
        if ( args[0].equals("intsToBytes") || args[0].equals("ib") ){
            intsToBytes(args);
            return;
        }    
        if ( args[0].equals("od") ){
            if ( args.length == 1 ){
                od("");
                return;
            }
            if ( args.length == 2 && ! args[1].equals("") ){
                if ( args[1].startsWith("-") && args[1].substring(1).replace("b","").replace("c","").replace("r","").equals("") ){
                    od(args[1].substring(1));
                    return;
                } 
                if ( args[1].replace("b","").replace("c","").replace("r","").equals("") ){
                    od(args[1]);
                    return;
                } 
            }
        }       
        if ( args[0].equals("touch") && (args.length == 2 || args.length == 3) ){
            touch(args);
            return;
        }
        if ( args[0].equals("rm") && args.length > 1 ){
            rm(args);
            return;
        }
        if ( args[0].equals("cp") && args.length == 3 ){
            cp(new File(args[1]), new File(args[2]), false, true);
            return;
        }
        if ( args[0].equals("cp") && args.length == 4 && args[1].equals("-R")){
            cp(new File(args[2]), new File(args[3]), true, true);
            return;
        }
        if ( args[0].equals("mkdir") && args.length == 2 ){
            mkdir(new File(args[1]));
            return;
        }        
        if ( args[0].equals("M") || args[0].equals("m") )
        {
            if ( args.length == 1 ){
                System.out.println("Parametro inválido!");
                System.out.println("Modelo:");
                System.out.println("y M ClassePrincipal Caminho Senha");
                System.out.println("y M pacote1/ClassePrincipal Caminho Senha");
                return;
            }    
            if ( args.length == 4 ){
                String txt="";        
                boolean principal_encontrado=false;
                String classe="";
                String path="";
                byte[] data=null;

                String principal=args[1].replace("/",".");
                String dir=args[2];
                String senha=args[3];                    
                
                // chamada principal
                txt=","+principal;

                for ( String item : nav_custom(dir) ){
                    if ( ! item.contains("|") ){
                        System.err.println("Erro fatal 44");
                        System.exit(1);
                    }
                    classe=item.split("\\|")[0];
                    path=item.split("\\|")[1];
                    
                    if ( classe.equals(principal) )
                        principal_encontrado=true;
                    try{
                        data=readAllBytes( path );
                    }catch(Exception e){
                        System.out.println("Erro na leitrua do arquivo: "+path+". "+e.toString());
                        System.exit(1);
                    }
                    txt+=","+classe;
                    try{
                        txt+=","+base64_B_S(data,true);        
                    }catch(Exception e){
                        System.out.println("Erro interno!");
                        System.exit(1);
                    }
                }

                if ( !principal_encontrado ){
                    System.out.println("Erro, classe principal nao encontrada!");
                    System.exit(1);
                }
                try{
                    txt=base64_B_S(new AES().encrypt(txt.getBytes(),senha,null,null) ,true);
                }catch(Exception e){
                    System.out.println("Erro interno !! "+ e.toString());
                    if(e.toString().contains("java.security.InvalidKeyException: Illegal key size"))
                        System.out.println("Erro conhecido no windows!");
                    System.exit(1);
                }

                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("public class M {");
                System.out.println("    public static void main(String[] args) throws Exception {");
                System.out.println("        String M=System.getenv(\"M\");");
                System.out.println("        if ( M != null && M.length() > 0 ){");
                System.out.println("            try{");
                System.out.println("                M=new String( new M_AES().decrypt(M_Base64.base64(M, false),\"\",null) );");
                System.out.println("                if ( M_Loader.loader(M,args) )");
                System.out.println("                    return;");
                System.out.println("            }catch(Exception e){");
                System.out.println("                System.out.println(e.toString());");
                System.out.println("            }");
                System.out.println("            System.out.println(\"Acesso negado!\");");
                System.out.println("        }");
                System.out.println("        pedeSenha();");
                System.out.println("    }");
                System.out.println("    public static void pedeSenha(){");
                System.out.println("        try{");
                System.out.println("            String M=M_UtilConsole.getPasswordConsole(\"Digite a senha: \");");
                System.out.println("            if ( M == null || M.length() == 0 ){");
                System.out.println("                System.out.println(\"Erro, nenhuma senha digitada!\");");
                System.out.println("                System.exit(1);");
                System.out.println("            }");
                System.out.println("            M=M_Base64.base64(new M_AES().encrypt(M.getBytes(),\"\",null,null),true);");
                System.out.println("            System.out.println(\"digite o comando export abaixo para ambientes nao windows:\");");
                System.out.println("            System.out.println(\"export M=\"+M);            ");
                System.out.println("            System.out.println(\"digite o comando export abaixo para ambientes windows:\");");
                System.out.println("            System.out.println(\"set M=\"+M);");
                System.out.println("            System.out.println(\"para debug(ver os loadClass) use export showLoadClass=S ou set showLoadClass=S\");");                
                System.out.println("        }catch(Exception e){");
                System.out.println("            System.out.println(\"Erro interno!\");");
                System.out.println("            System.exit(1);");
                System.out.println("        }");
                System.out.println("    }");
                System.out.println("}");
                System.out.println("// openssl aes-256-cbc -base64 -pass pass:<secret> -md md5");
                System.out.println("// creditos: https://github.com/chmduquesne/minibackup/blob/master/samples/OpensslAES.java");
                System.out.println("// new M_AES().encrypt(bytes,password,null,null);");
                System.out.println("// new M_AES().decrypt(bytes,password,null);");                
                System.out.println("class M_AES{ byte [] deriveKeyAndIV(byte[] password, String md, byte[] salt) throws Exception{         if ( md == null || md.equals(\"\") ) md=\"MD5\"; byte[] res = new byte[48]; final java.security.MessageDigest md5 = java.security.MessageDigest.getInstance(md); md5.update(password); md5.update(salt); byte[] hash1 = md5.digest(); md5.reset(); md5.update(hash1); md5.update(password); md5.update(salt); byte[] hash2 = md5.digest(); md5.reset(); md5.update(hash2); md5.update(password); md5.update(salt); byte[] hash3 = md5.digest(); if ( md == null || md.equals(\"MD5\")){ System.arraycopy(hash1, 0, res, 0, 16); System.arraycopy(hash2, 0, res, 16, 16); System.arraycopy(hash3, 0, res, 32, 16); }else{ System.arraycopy(hash1, 0, res, 0, 32); System.arraycopy(hash2, 0, res, 32, 16); } return res; } public void encrypt(java.io.InputStream pipe_in, java.io.OutputStream pipe_out, String senha, String md, byte[] salt) throws Exception { byte[] salt_ = new byte[8]; java.security.SecureRandom sr = new java.security.SecureRandom(); sr.nextBytes(salt_); if ( salt==null ) salt=salt_; byte[] keyAndIV = deriveKeyAndIV(senha.getBytes(),md,salt); byte[] key = java.util.Arrays.copyOfRange(keyAndIV, 0, 32); byte[] iv = java.util.Arrays.copyOfRange(keyAndIV, 32, 48); javax.crypto.spec.SecretKeySpec skeySpec = new javax.crypto.spec.SecretKeySpec(key, \"AES\"); javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv); javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(\"AES/CBC/PKCS5Padding\"); cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, skeySpec, ivspec); int BUFFER_SIZE=1024; byte[] buff=new byte[BUFFER_SIZE]; int len=0; pipe_out.write(\"Salted__\".getBytes()); pipe_out.write(salt); while ( (len=pipe_in.read(buff,0,BUFFER_SIZE)) > 0 ) pipe_out.write( cipher.update(buff,0,len) ); pipe_out.write(cipher.doFinal()); pipe_out.flush();        } public byte[] encrypt(byte[] data, String senha, String md, byte[] salt) throws Exception{ java.io.ByteArrayInputStream bais=new java.io.ByteArrayInputStream(data); java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream(); encrypt(bais,baos,senha,md,salt); return baos.toByteArray(); } public void decrypt(java.io.InputStream pipe_in,java.io.OutputStream pipe_out,String senha,String md) throws Exception { int p=0; p=pipe_in.read(new byte[8]); if ( p != 8 ){ System.err.println(\"Erro fatal 0!\"); System.exit(1); } byte[] salt=new byte[8]; p=pipe_in.read(salt); if ( p != 8 ){ System.err.println(\"Erro fatal 0!\"); System.exit(1); }        byte[] keyAndIV=deriveKeyAndIV(senha.getBytes(),md,salt); byte[] key=java.util.Arrays.copyOfRange(keyAndIV, 0, 32); byte[] iv=java.util.Arrays.copyOfRange(keyAndIV, 32, 48); javax.crypto.spec.SecretKeySpec skeySpec = new javax.crypto.spec.SecretKeySpec(key, \"AES\"); javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv); javax.crypto.Cipher cipher; cipher=javax.crypto.Cipher.getInstance(\"AES/CBC/PKCS5Padding\"); cipher.init(javax.crypto.Cipher.DECRYPT_MODE, skeySpec, ivspec); int BUFFER_SIZE=1024; byte[] buff=new byte[BUFFER_SIZE]; int len=0; while ( (len=pipe_in.read(buff,0,BUFFER_SIZE)) > 0 ) pipe_out.write( cipher.update(buff,0,len) ); pipe_out.write(cipher.doFinal()); pipe_out.flush(); } public byte[] decrypt(byte[] data, String senha, String md) throws Exception{ java.io.ByteArrayInputStream bais=new java.io.ByteArrayInputStream(data); java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream(); decrypt(bais,baos,senha,md); return baos.toByteArray(); } private static String bytesToHex(byte[] a){ StringBuilder sb = new StringBuilder(); for (byte b : a) { sb.append(String.format(\"%02X\", b)); } return sb.toString(); } private static byte[] hexTobytes(String s) { int len = s.length(); byte[] data = new byte[len / 2]; for (int i = 0; i < len; i += 2) { data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16)); } return data;}}");
                System.out.println("// M_Base64.base64(bytes,true) // retorna string encriptado");
                System.out.println("// M_Base64.base64(texto,false) // retorna bytes decriptado");
                System.out.println("class M_Base64{ public static String erroSequenciaIlegal=\"Erro, sequencia ilegal!\"; public static int [] indexBase64 = new int []{65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,48,49,50,51,52,53,54,55,56,57,43,47}; public static String txtBase64=\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\"; public static byte[] base64(String txt,boolean encoding) throws Exception{        java.io.ByteArrayInputStream bais=new java.io.ByteArrayInputStream(txt.getBytes()); java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream(); base64(bais,baos,encoding);         return baos.toByteArray(); } public static String base64(byte[] bytes,boolean encoding) throws Exception{        java.io.ByteArrayInputStream bais=new java.io.ByteArrayInputStream(bytes); java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream(); base64(bais,baos,encoding);         return baos.toString(); } public static void base64(java.io.InputStream pipe_in,java.io.OutputStream pipe_out,boolean encoding) throws Exception{        if ( encoding ) base64encode(pipe_in,pipe_out); else base64decode(pipe_in,pipe_out);                    } public static void base64encode(java.io.InputStream pipe_in,java.io.OutputStream pipe_out) throws Exception{        int BUFFER_SIZE_ = 1; byte [] buf=new byte[BUFFER_SIZE_]; int len=-1; int entrada=-1; int agulha=0; int agulha_count=0; int indexPadding=61;  while(true){ while( (len=pipe_in.read(buf,0,BUFFER_SIZE_)) == 0 ){} if ( len == -1 ){ if ( agulha_count == 4 ){ pipe_out.write( indexBase64[ agulha<<2 ] ); pipe_out.write( indexPadding ); } if ( agulha_count == 2 ){ pipe_out.write( indexBase64[ agulha<<4 ] ); pipe_out.write( indexPadding ); pipe_out.write( indexPadding ); }  break; } entrada=byte_to_int_java(buf[0]); agulha=(agulha<<8)|entrada; agulha_count+=8; while(agulha_count>=6){ if ( agulha_count == 6 ){ pipe_out.write( indexBase64[ agulha ] ); agulha=0; agulha_count-=6; continue; } if ( agulha_count == 8 ){ pipe_out.write( indexBase64[ (agulha & 252)>>2 ] ); agulha&=3; agulha_count-=6; continue; } if ( agulha_count == 10 ){ pipe_out.write( indexBase64[ (agulha & 1008)>>4 ] ); agulha&=15; agulha_count-=6; continue; } if ( agulha_count == 12 ){ pipe_out.write( indexBase64[ (agulha & 4032)>>6 ] ); agulha&=63; agulha_count-=6; continue; } } }    pipe_out.flush(); } public static void base64decode(java.io.InputStream pipe_in,java.io.OutputStream pipe_out) throws Exception{        int BUFFER_SIZE_ = 1; byte [] buf=new byte[BUFFER_SIZE_]; int len=-1; int entrada=-1; int agulha=0; int agulha_count=0;        int padding_count=0; while(true){ while( (len=pipe_in.read(buf,0,BUFFER_SIZE_)) == 0 ){} if ( len == -1 ){ if ( agulha_count == 0 && padding_count == 0 && agulha == 0 ){ break; } if ( agulha_count == 4 && padding_count == 2 && agulha == 0 ){ break; } if ( agulha_count == 2 && padding_count == 1 && agulha == 0 ){ break; } throw new Exception(erroSequenciaIlegal); } entrada=byte_to_int_java(buf[0]); if ( entrada == 10 || entrada == 13 ) continue; entrada=txtBase64.indexOf((char)entrada); if ( entrada == -1 ){ System.err.println(erroSequenciaIlegal); System.exit(1); } if ( entrada == 64 ){ padding_count++; continue; }            agulha=(agulha<<6)|entrada; agulha_count+=6; while(agulha_count>=8){ if ( agulha_count == 8 ){ pipe_out.write( agulha ); agulha=0; agulha_count-=8; continue; } if ( agulha_count == 10 ){ pipe_out.write( (agulha & 1020)>>2 ); agulha&=3; agulha_count-=8; continue; } if ( agulha_count == 12 ){ pipe_out.write( (agulha & 4080)>>4 ); agulha&=15; agulha_count-=8; continue; } } }    pipe_out.flush();        } public static int byte_to_int_java(byte a) { int i=(int)a; if ( i < 0 ) i+=256; return i;}}");
                System.out.println("// String senha=M_UtilConsole.getPasswordConsole(\"Digite a senha: \");");
                System.out.println("// String texto=M_UtilConsole.getTextConsole(\"Digite o texto: \");");
                System.out.println("class M_UtilConsole{ public static String getPasswordConsole(String txt) { String retorno=\"\"; java.io.Console console=System.console(); if ( console == null ){ System.out.println(\"Error, input nao suportado nesse ambiente, rodando no netbeans?...\"); System.exit(1); } char [] passChar = System.console().readPassword(txt); if ( passChar != null ) retorno=new String(passChar); if ( retorno == null ){ System.out.println(\"Error, not input found\"); System.exit(1); } return retorno;}public static String getTextConsole(String txt) { String retorno=\"\"; java.io.Console console=System.console(); if ( console == null ){ System.out.println(\"Error, input nao suportado nesse ambiente, rodando no netbeans?...\"); System.exit(1); } System.out.print(txt);retorno=System.console().readLine();if ( retorno == null ){ System.out.println(\"Error, not input found\"); System.exit(1); } return retorno;}}");
                System.out.println("//M_Loader");
                System.out.println("//[classe principal],[load classA]  ,[load classB] ");
                System.out.println("//,classA           ,classA,dados...,classB,dados...");
                System.out.println("class M_Loader{ public static boolean loader(String senha,String[] args) throws Exception { String showLoadClass_txt=System.getenv(\"showLoadClass\"); boolean showLoadClass=showLoadClass_txt != null && showLoadClass_txt.equals(\"S\"); try{ if ( showLoadClass ) System.out.println(\"showLoadClass...\"); java.util.HashMap classes=new java.util.HashMap(); String base=M_Dados.get(); String txt=new String( new M_AES().decrypt( M_Base64.base64(base,false) ,senha,null) ); if (!txt.startsWith(\",\")){ throw new Exception(\"Erro fatal!\"); }else{ txt=txt.substring(1); } String partes[]=txt.split(\",\"); String id=null; String principal=null; for ( int i=0;i<partes.length;i++ ){ if ( principal == null ){ principal=partes[i]; continue; } if ( id == null ){ id=partes[i]; continue; } classes.put(id,partes[i]); id=null; } ClassLoader classLoader=new ClassLoader() {            @Override protected Class<?> findClass(String name) throws ClassNotFoundException { if ( classes.containsKey(name) ){ try { byte[] data=M_Base64.base64((String)classes.get(name),false); if ( showLoadClass ) System.out.println(\"showLoadClass...loadClasse...\"+name); return defineClass(name,data,0,data.length);        } catch (Exception e) { System.err.println(\"Erro no carregamento da classe \"+name); System.exit(1); } } if ( showLoadClass ) System.out.println(\"showLoadClass...loadClasseNative...\"+name); return super.findClass(name); } }; Class c=classLoader.loadClass(principal); java.lang.reflect.Method method=c.getDeclaredMethod(\"main\", String[].class ); method.invoke(null, new Object[]{ args } ); }catch(Exception e){ return false;} return true;} }");
                
                int len=txt.length();
                int method_len=1;
                int cont=0;
                int cont_len=1000;
                
                // methods
                System.out.println("class M_Dados_"+method_len+" {");
                System.out.println("    public static String get(){");
                System.out.println("        StringBuilder sb=new StringBuilder();");
                for ( int i=0;i<len;i+=200 ){
                    if ( cont > cont_len ){
                        cont=0;
                        method_len++;
                        System.out.println("        return sb.toString();");
                        System.out.println("    }");
                        System.out.println("}");
                        System.out.println("class M_Dados_"+method_len+" {");
                        System.out.println("    public static String get(){");
                        System.out.println("        StringBuilder sb=new StringBuilder();");
                    }
                    if ( i+200 > len )
                        System.out.println("        sb.append(\""+txt.substring(i,len)+"\");");
                    else
                        System.out.println("        sb.append(\""+txt.substring(i,i+200)+"\");");
                    cont++;
                }
                System.out.println("        return sb.toString();");
                System.out.println("    }");
                System.out.println("}");
                
                //finish
                System.out.println("//M_Dados");
                System.out.println("class M_Dados {");
                System.out.println("    public static String get(){");
                for ( int i=1;i<=method_len;i++ ){
                    if ( i == 1 && method_len == 1 ){
                        System.out.println("        return M_Dados.get_1();");
                    }else{
                        if ( i == 1 ){
                            System.out.println("        return M_Dados_1.get()");
                        }else{
                            if ( i < method_len ){
                                System.out.println("        + M_Dados_"+i+".get()");                                
                            }else{
                                System.out.println("        + M_Dados_"+i+".get();");
                            }
                        }
                    }                    
                }
                System.out.println("    }");
                System.out.println("}");
                return;
            }
        }        
        if ( args[0].equals("i1") ){ // atalho iconv
            if ( args.length == 1 ){
                args=new String[]{"iconv", "-f", "UTF-8", "-t", "ISO-8859-1"};
            }else{
                if ( args.length == 2 ){
                    args=new String[]{"iconv", "-f", "UTF-8", "-t", "ISO-8859-1", args[1]};
                }
            }
        }
        if ( args[0].equals("i2") ){ // atalho iconv
            if ( args.length == 1 ){
                args=new String[]{"iconv", "-f", "ISO-8859-1", "-t", "UTF-8"};
            }else{
                if ( args.length == 2 ){
                    args=new String[]{"iconv", "-f", "ISO-8859-1", "-t", "UTF-8", args[1]};
                }
            }
        }
        if ( args[0].equals("iconv") ){
            if ( args.length == 1 ){
                System.out.println("Tipos suportados de iconv:");
                for(int i=0;i<suportIconv.length;i++)
                    System.out.println("  "+suportIconv[i]);
                System.out.println("Ex:");
                System.out.println("y iconv -f UTF-8 -t ISO-8859-1 file");
                return;
            }               
            if ( 
                ( args.length == 5 || args.length == 6 )
                && ( (args[1].equals("-f") && args[3].equals("-t")) || (args[1].equals("-t") && args[3].equals("-f")) )
                && isSuportIconv(args[2]) && isSuportIconv(args[4]) && ! args[2].equals(args[4])
            ){
                String file_=null;
                if ( args.length == 6 ){
                    if ( ! new File(args[5]).exists() ){
                        System.err.println("Erro, este arquivo não existe: "+args[5]);
                        System.exit(1);
                    }         
                    file_=args[5];
                }
                String tipoOrigem=args[2];
                String tipoDestino=args[4];
                if ( args[1].equals("-t") ){
                    tipoOrigem=args[4];
                    tipoDestino=args[2];
                }
                // tipo em BOM(puro)
                String tipoOrigemPuro=tipoOrigem.endsWith("BOM")?tipoOrigem.substring(0, tipoOrigem.length()-3):tipoOrigem;
                String tipoDestinoPuro=tipoDestino.endsWith("BOM")?tipoDestino.substring(0, tipoDestino.length()-3):tipoDestino;                    

                try{
                    iconv(tipoOrigem,tipoOrigemPuro,tipoDestino,tipoDestinoPuro,file_);
                }catch(Exception e){
                    System.out.println(e.toString());
                    System.exit(1);
                }            
                return;
            }        
        }        
        if ( args[0].equals("tee") && args.length == 2 ){
            tee(args[1]);
            return;
        }
        if ( args[0].equals("uniq") ){
            uniq_quebra(false);
            return;
        }
        if ( args[0].equals("quebra") ){
            uniq_quebra(true);
            return;
        }
        if ( args[0].equals("seq") ){
            try{                
                if ( args.length == 3 ){
                    if (args[1].split("-").length > 1 || args[1].split("/").length > 1)
                        seqDate(args[1], args[2]);
                    else
                        seq(Integer.parseInt(args[1]),Integer.parseInt(args[2]),0);
                    return;
                }
                if ( args.length == 4 ){
                    seq(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                    return;
                }
            }catch(Exception e){}
        }
        if ( args[0].equals("add") && args.length == 2 && (args[1].split("-").length > 1 || args[1].split("/").length > 1) ){
            try{                
                add(args[1]);
                return;
            }catch(Exception e){}
        }
        if ( args[0].equals("awk") )
        {
            if ( args.length >= 3 && args[1].equals("print") )
            {
                awk_print(args);
                return;
            }
            awk_start_end(args);
            return;
        }
        if ( args[0].equals("dev_null") ){
            dev_null();
            return;
        }
        if ( args[0].equals("dev_in") ){
            dev_in();
            return;
        }
        if ( args[0].equals("scp") ){
            scp(args);
            return;
        }        
        if ( args[0].equals("execSsh") ){
            execSsh(args);
            return;
        }        
        if ( args[0].equals("ssh") ){
            ssh(args);
            return;
        }    
        if ( args[0].equals("sshinfo") && args.length == 1){
            sshinfo(null, null);
            return;
        }    
        if ( args[0].equals("sshinfo") && args.length == 2){
            sshinfo(args[1], null);
            return;
        }    
        if ( args[0].equals("sshinfo") && args.length == 3){
            sshinfo(args[1], args[2]);
            return;
        }    
        if ( args[0].equals("sftp") ){
            sftp(args);
            return;
        }        
        if ( args[0].equals("serverRouter"))
        {
            serverRouter(args);
            return;            
        }
        if ( args[0].equals("httpServer"))
        {
            String host="localhost";
            if ( args.length > 1 )
                host=args[1];
            else
                System.out.println("host preenchido automaticamente => " + host);
            String titulo_url="tmp";
            if ( args.length > 2 )
                titulo_url=args[2];
            else
                System.out.println("titulo url preenchido automaticamente => " + titulo_url);     
            String titulo="tmp";
            if ( args.length > 3 )
                titulo=args[3];
            else
                System.out.println("titulo preenchido automaticamente => " + titulo);        
            String port="8888";
            if ( args.length > 4 )
                port=args[4];
            else
                System.out.println("porta preenchido automaticamente => " + port);     
            String dir=".";
            if ( args.length > 5 )
                dir=args[5];
            else
                System.out.println("diretorio preenchido automaticamente => " + dir);     
            String endsWiths="";
            if ( args.length > 6 )
                endsWiths=args[6];
            else
                System.out.println("extensoes validas preenchido automaticamente => " + endsWiths);     
            String ips_banidos="";
            if ( args.length > 7 )
                ips_banidos=args[7];
            else
                System.out.println("ips banidos preenchido automaticamente => " + ips_banidos);     
            new HttpServer(new String[]{host, titulo_url, titulo, port, dir, endsWiths, ips_banidos},false);
            return;
        }  
        if ( args[0].equals("playlist")){
            String host="localhost";
            if ( args.length > 1 )
                host=args[1];
            else
                System.out.println("host preenchido automaticamente => " + host);
            String titulo_url="tmp";
            System.out.println("titulo url preenchido automaticamente => " + titulo_url);     
            String titulo="tmp";
            System.out.println("titulo preenchido automaticamente => " + titulo);        
            String port="8888";
            if ( args.length > 2 )
                port=args[2];
            else
                System.out.println("porta preenchido automaticamente => " + port);     
            String dir=".";
            System.out.println("diretorio preenchido automaticamente => " + dir);     
            String endsWiths="";
            System.out.println("extensoes validas preenchido automaticamente => " + endsWiths);     
            String ips_banidos="";
            System.out.println("ips banidos preenchido automaticamente => " + ips_banidos);     
            new HttpServer(new String[]{host, titulo_url, titulo, port, dir, endsWiths, ips_banidos},true);
            return;            
        }
            
        if ( args[0].equals("wget")){
            wget(args);
            return;            
        }
        if ( args[0].equals("pwd")){
            System.out.println(System.getProperty("user.dir"));
            return;            
        }
        if ( args[0].equals("find") && args.length >= 1 && args.length <= 4 ){
            float mtime = 0;
            if ( args.length == 1 ){
                find(null, false, mtime);
                return;
            }
            if ( args.length == 2 ){
                find(args[1], false, mtime);
                return;
            }
            if ( args.length == 3 && args[1].equals("-mtime") ){
                try{
                    mtime=Float.parseFloat(args[2]);
                    mtime*=24*60*60*1000;
                    find(null, false, mtime);
                    return;
                }catch(Exception e){}
            }
            if ( args.length == 4 && args[2].equals("-mtime") ){
                try{
                    mtime=Float.parseFloat(args[3]);
                    mtime*=24*60*60*1000;
                    find(args[1], false, mtime);
                    return;
                }catch(Exception e){}
            }
            if ( args.length == 4 && args[1].equals("-mtime") ){
                try{
                    mtime=Float.parseFloat(args[2]);
                    mtime*=24*60*60*1000;
                    find(args[3], false, mtime);
                    return;
                }catch(Exception e){}
            }            
        }
        if ( args[0].equals("ls") ){
            int len_antes=args.length;
            args = bind_asterisk(args);
            if ( args.length == 1 ){
                find(null, true, 0);
                return;
            }
            if ( args.length == 2 ){
                find(args[1], true, 0);
                return;
            }
            for( int i=1;i<args.length;i++ ){
                if ( len_antes > 2 )
                    System.out.println("\n"+args[i]+":");
                find(args[i], true, 0);                
            }
            return;
        }
        if ( args[0].equals("sleep") && (args.length == 1 || args.length == 2) ){
            if ( args.length == 2 ){
                try{
                    sleep(Float.parseFloat(args[1]));
                    return;
                }catch(Exception e){}                                
            }else{
                sleep(null);
                return;
            }
        }
        if ( args[0].equals("split") ){
            String [] BytesLinesPrefixParm=getBytesLinesPrefixParm(args);

            if ( BytesLinesPrefixParm == null ){
                comando_invalido(args);
                return;
            }

            String bytes=BytesLinesPrefixParm[0];
            String lines=BytesLinesPrefixParm[1];
            String prefix=BytesLinesPrefixParm[2];
            String parm=BytesLinesPrefixParm[3];
            
            split(bytes, lines, prefix, parm);
            return;
        }
        if ( args[0].equals("regua") ){
            if ( args.length == 2 )
                regua(Integer.parseInt(args[1]));
            else
                regua(130);
            return;
        }
        if ( args[0].equals("unique") ){
            System.out.println("Vc quis dizer uniq?");
            return;
        }
        if ( args[0].equals("link") && args.length == 3 ){
            link(args[1], args[2]);
            return;            
        }
        if ( args[0].equals("os")){
            os();
            return;            
        }
        if ( args[0].equals("date")){
            date(args);
            return;
        }
        if ( args[0].equals("uptime")){
            if ( args.length == 2 && args[1].equals("-ms") ){
                uptime(true);
                return;
            }
            if ( args.length == 1 ){
                uptime(false);
                return;
            }
        }
       
        if ( args[0].equals("cronometro") ){
            if ( args.length == 2 && (args[1].equals("start") || args[1].equals("flag") || args[1].equals("end")) ){
                cronometro(args[1]);
                return;
            }
            if ( args.length == 1 ){
                cronometro(null);
                return;
            }
        }
        if ( args[0].equals("help") || args[0].equals("-help") || args[0].equals("--help") ){
            String retorno=null;
            if ( args.length == 2 )
                retorno=helplike(args[1]);
            if ( retorno == null )
                System.err.println(
                    "Utilitário Y versão:" + lendo_arquivo_pacote("/y/versao") + "\n"
                    + lendo_arquivo_pacote("/y/manual")
                    + "\n\nUtilitário Y versão:" + lendo_arquivo_pacote("/y/versao")
                );
            else
                System.err.println(
                    retorno
                );
            return;
        }
        
        comando_invalido(args);
        return;
    }

    public String [] getConnAppParm(String [] args){
        
        //[y banco -conn ... select]
        //[y banco -conn ... select select..]
        //[y banco -conn ... selectInsert]
        //[y banco -conn ... selectInsert select..]
        //[y banco -conn ... selectCSV]
        //[y banco -conn ... selectCSV select..]
        //[y banco -conn ... executeInsert]
        //[y banco -conn ... execute]
        //[y banco -conn ... execute execute..]
        //[y banco conn,hash select]
        //[y banco conn,hash select select..]
        //[y banco conn,hash selectInsert]
        //[y banco conn,hash selectInsert select..]
        //[y banco conn,hash selectCSV]
        //[y banco conn,hash selectCSV select..]
        //[y banco conn,hash executeInsert]
        //[y banco conn,hash execute]
        //[y banco conn,hash execute execute..]

        String value_=""; // tmp
        
        String conn="";
        String app="";
        String parm="";
        
        if ( args.length > 0 && args[0].equals("banco") )
            args=sliceParm(1,args);

        if ( args.length > 0 && args[0].startsWith("conn,") )
        {
            value_=gettoken(args[0].split(",")[1]);
            if ( value_ == null )
            {
                System.err.println("Não foi possível encontrar o token "+args[0].split(",")[1]);
                return null;
            }
            conn=value_;
            if ( ! conn.contains(":")){
                try{
                    conn=base64_S_S(conn,false);
                }catch(Exception e){}
            }
            conn=conn.replace("\n","");
            args=sliceParm(1,args);
        }
        
        if ( conn.equals("") && args.length > 1 && args[0].equals("-conn") )
        {
            conn=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 )
        {
            app=args[0];
            args=sliceParm(1,args);
        }
        
        if ( args.length > 0 )
        {
            parm=args[0];
            args=sliceParm(1,args);
        }
        
        if ( conn.equals("") || app.equals("") ) // parm é opcional
            return null;
            
        return new String[]{conn,app,parm};
    }
    
    public String [] get_connIn_fileCSV_connOut_outTable_trunc_app(String [] args){
        //[y banco connIn,hash connOut,hash outTable,tabelaA carga]
        //[y banco connIn,hash connOut,hash outTable,tabelaA trunc carga]
        //[y banco connIn,hash connOut,hash outTable,tabelaA createjobcarga]
        //[y banco connIn,hash connOut,hash outTable,tabelaA trunc createjobcarga]
        String value_=""; // tmp
        
        String connIn="";
        String fileCSV="";
        String connOut="";
        String outTable="";
        String trunc="";
        String createTable=""; // o valor irá para trunc
        String app="";       
        
        if ( args.length > 0 && args[0].equals("banco") )
            args=sliceParm(1,args);
        
        if ( args.length > 0 && args[0].startsWith("connIn,") )
        {
            value_=gettoken(args[0].split(",")[1]);
            if ( value_ == null )
            {
                System.err.println("Não foi possível encontrar o token "+args[0].split(",")[1]);
                return null;
            }
            connIn=value_;
            args=sliceParm(1,args);
        }
        
        if ( connIn.equals("") && args.length > 1 && args[0].equals("-connIn") )
        {
            connIn=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 && args[0].startsWith("fileCSV,") )
        {
            value_=gettoken(args[0].split(",")[1]);
            if ( value_ == null )
            {
                System.err.println("Não foi possível encontrar o token "+args[0].split(",")[1]);
                return null;
            }
            fileCSV=value_;
            args=sliceParm(1,args);
        }
        
        if ( connIn.equals("") && args.length > 1 && args[0].equals("-fileCSV") )
        {
            fileCSV=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 && args[0].startsWith("connOut,") )
        {
            value_=gettoken(args[0].split(",")[1]);
            if ( value_ == null )
            {
                System.err.println("Não foi possível encontrar o token "+args[0].split(",")[1]);
                return null;
            }
            connOut=value_;
            args=sliceParm(1,args);
        }
        
        if ( args.length > 1 && connOut.equals("") && args[0].equals("-connOut") )
        {
            connOut=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 && args[0].startsWith("outTable,") )
        {
            value_=gettoken(args[0].split(",")[1]);
            if ( value_ == null )
            {
                System.err.println("Não foi possível encontrar o token "+args[0].split(",")[1]);
                return null;
            }
            outTable=value_;
            args=sliceParm(1,args);
        }
        
        if ( outTable.equals("") && args.length > 1 && args[0].equals("-outTable") )
        {
            outTable=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 && args[0].equals("trunc") ){
            trunc="S";
            args=sliceParm(1,args);
        }
        
        if ( args.length > 0 && args[0].equals("createTable") ){
            createTable="CREATETABLE";
            args=sliceParm(1,args);
        }
        
        if ( args.length == 1 ){
            app=args[0];
            args=sliceParm(1,args);
        }
        
        if ( ! trunc.equals("") && ! createTable.equals("") )
            return null;
        if ( ! createTable.equals("") )
            trunc=createTable;
        if ( trunc.equals("") )
            trunc="N";
        if ( (connIn.equals("") && fileCSV.equals("")) || (!connIn.equals("") && !fileCSV.equals("")) )
            return null;
        if ( connOut.equals("") || outTable.equals("") || trunc.equals("") || app.equals("") )
            return null;

        return new String[]{connIn,fileCSV,connOut,outTable,trunc,app};
    }
        
    public ArrayList<String> get_v_i_txt(String [] args){
        String v="N";
        String i="N";
        String txt="";
        ArrayList<String> lista=new ArrayList<String>();
        
        if ( args.length > 0 && args[0].equals("grep") )
            args=sliceParm(1,args);
        
        // parametros -v -i
        if ( args.length > 2 && args[0].equals("-v") && args[1].equals("-i") ){
            v="S";
            i="S";
            args=sliceParm(2,args);
        }else{
            if ( args.length > 2 && args[0].equals("-i") && args[1].equals("-v") ){
                i="S";
                v="S";
                args=sliceParm(2,args);
            }else{
                if ( args.length > 1 && args[0].equals("-v") ){
                    v="S";
                    args=sliceParm(1,args);
                }else{
                    if ( args.length > 1 && args[0].equals("-i") ){
                        i="S";
                        args=sliceParm(1,args);
                    }
                }
            }            
        }

        lista.add(v);
        lista.add(i);

        while(args.length > 0){
            String starting="N";
            String ending="N";
            
            txt=args[0];
            if ( i.equals("S") )
                txt=txt.toUpperCase();
            
            if ( txt.startsWith("^") ){
                txt=txt.substring(1);
                starting="S";
            }
            if ( txt.endsWith("$") ){
                txt=txt.substring(0,txt.length()-1);
                ending="S";
            }  
            lista.add(starting);
            lista.add(ending);
            lista.add(txt);
            args=sliceParm(1,args);
        }
        return lista;
    }
    
    public String[] get_csvFile_sqlFile_sqlText(String[] args) {
        String csvFile="";
        String sqlFile="";
        String sqlText="";
        
        if ( args.length > 0 && args[0].equals("selectCSV") )
            args=sliceParm(1,args);
        
        if ( args.length > 1 && args[0].equals("-csv") ){
            csvFile=args[1];
            args=sliceParm(2,args);
        }
        if ( args.length > 1 && args[0].equals("-sql") ){
            sqlFile=args[1];
            args=sliceParm(2,args);
        }
        if ( args.length == 1 ){
            sqlText=args[0];
            args=sliceParm(1,args);
        }
        
        if ( sqlFile.equals("") && sqlText.equals("") )
            return null;
        if ( !sqlFile.equals("") && !sqlText.equals("") )
            return null;
        return new String[]{csvFile,sqlFile,sqlText};        
    }
    
    
    public String [] get_senha_isEncoding_md_salt(String [] args){
        String senha=null;
        String isEncoding="S";
        String md=null;
        String salt=null;
        
        if ( args.length > 0 && args[0].equals("aes") )
            args=sliceParm(1,args);
                
        if ( args.length > 0 && ( args[0].equals("-e") || args[0].equals("-d") ) ){
            if ( args[0].equals("-e") )
                isEncoding="S";
            if ( args[0].equals("-d") )
                isEncoding="N";
            args=sliceParm(1,args);
        }

        if ( args.length > 0 ){
            senha=args[0];
            args=sliceParm(1,args);
        }
        
        if ( args.length > 1 && args[0].equals("-md") ){            
            if ( args[1].toUpperCase().equals("MD5") || args[1].toUpperCase().equals("SHA256") || args[1].toUpperCase().equals("SHA-256") ){
                args=sliceParm(1,args);
                md=args[0].toUpperCase();
                args=sliceParm(1,args);
                if ( md.equals("SHA256") )
                    md="SHA-256";
            }
        }
        
        if ( args.length > 1 && args[0].equals("-S") ){
            args=sliceParm(1,args);
            salt=args[0];
            args=sliceParm(1,args);
            if ( salt.length() != 16 || !isHex(salt) ){
                System.err.println("Erro, conteudo hex do Salt inválido: "+salt);
                System.exit(1);
            }
        }

        if ( args.length > 0 ){
            return null;
        }
        if ( senha==null )
            return null;
        
        return new String[]{senha,isEncoding,md,salt};        
    }
            
    private boolean isHex(String a){
        String tmp="0123456789ABCDEF";
        for ( int i=0;i<a.length();i++ )
            if ( tmp.indexOf(a.substring(i,i+1)) == -1 )
                return false;
        return true;
    }
    
    private String[] getNegativaStartEnd(String[] args) {
        String negativa="N";        
        String start=null;
        String end=null;
        
        if ( args.length > 0 && args[0].equals("awk") ){
            args=sliceParm(1,args);
        }
        
        if ( args.length > 0 && args[0].equals("-v") ){
            negativa="S";        
            args=sliceParm(1,args);
        }

        if ( args.length > 1 && args[0].equals("start") ){
            start=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 1 && args[0].equals("end") ){
            end=args[1];
            args=sliceParm(2,args);
        }

        if ( args.length > 0 ){
            return null;
        }
        
        if ( start == null && end == null ){
            return null;
        }
        
        return new String[]{negativa,start,end};        
    }
    
    public void select(String conn,String parm){
        String parm_=parm;
        
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;

        // configuracao TZ UTC NATAL
        String dataT=" ";
        String dataZ=" ";
        boolean flag_natal=false;
        String format_data=System.getenv("FORMAT_DATA_Y");
        if ( format_data != null && format_data.equals("TZ") ){
            dataT="T";
            dataZ="Z";
        }
        if ( format_data != null && format_data.equals("UTC") )
            dataZ=" UTC";
        if ( format_data != null && format_data.equals("NATAL") )
            flag_natal=true;

        try{            
            con = getcon(conn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return;
            }
            
            if ( parm.equals("") ){
                String line;
                while( (line=readLine()) != null )
                    parm+=line+"\n";
                closeLine();
            }
            parm=removePontoEVirgual(parm);

            stmt = con.createStatement();
            rs=null;
            ResultSetMetaData rsmd;
            ArrayList<String> campos=new ArrayList<String>();
            ArrayList<Integer> tipos=new ArrayList<Integer>();
            StringBuilder sb=null;
            String tmp="";

            rs=stmt.executeQuery(parm);
            rsmd=rs.getMetaData();

            for ( int i=1;i<=rsmd.getColumnCount();i++ )
            {
                campos.add(rsmd.getColumnName(i));                
                tipos.add(rsmd.getColumnType(i));
            }

            while ( rs.next() ){
                sb=new StringBuilder();
                for ( int i=0;i<campos.size();i++ ){
                    tmp=rs.getString(campos.get(i));
                    if ( tmp == null  ){
                        if ( i == campos.size()-1 )
                        {
                            sb.append("null");
                        }else{
                            sb.append("null\t");
                        }
                        continue;
                    }
                    if ( tipo_cadastrado(tipos.get(i)) ){
                        if ( tipo_numerico(tipos.get(i)) && tmp.startsWith("."))
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            if ( flag_natal )
                                tmp="25/12/"+tmp.substring(0, 4)+dataT+tmp.substring(11, 19)+dataZ;                                
                            else
                                tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+dataT+tmp.substring(11, 19)+dataZ;
                        //tmp=tmp.replace("'","''"); // nao usado no select
                        if ( i == campos.size()-1 ){
                            sb.append(tmp);
                        }else{
                            sb.append(tmp);
                            sb.append("\t");
                        }
                        continue;
                    }
                    throw new Exception("tipo desconhecido:"+tipos.get(i) + " -> " + rs.getString(campos.get(i)) );
                }
                System.out.println(sb.toString());
            }                 
        }
        catch(Exception e)
        {            
            System.err.println("Erro: "+e.toString()+" -> "+parm_);
        }
        close(rs,stmt,con);
    }

    public void selectInsert(String conn,String fileCSV,String parm, PipedOutputStream out,String table,String nemVouExplicar){ // table opcional
        if ( ! conn.equals("") )
        {
            pipeSelectInsertConn(conn,fileCSV,parm, out,table,nemVouExplicar);            
        }else{
            pipeSelectInsertCSV(conn,fileCSV,parm, out,table,nemVouExplicar);
        }        
    }
    
    public void pipeSelectInsertConn(String conn,String fileCSV,String parm, PipedOutputStream out,String table,String nemVouExplicar){ // table opcional
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;

        // configuracao TZ UTC NATAL
        String dataT=" ";
        String dataZ=" ";
        boolean flag_natal=false;
        String format_data=System.getenv("FORMAT_DATA_Y");
        if ( format_data != null && format_data.equals("TZ") ){
            dataT="T";
            dataZ="Z";
        }
        if ( format_data != null && format_data.equals("UTC") )
            dataZ=" UTC";
        if ( format_data != null && format_data.equals("NATAL") )
            flag_natal=true;
        
        int countCommit=0;
        try{
            con = getcon(conn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return;
            }

            if ( parm.equals("") ){
                String line;
                while( (line=readLine()) != null )
                    parm+=line+"\n";
                closeLine();
            }
            
            parm=removePontoEVirgual(parm);

            stmt = con.createStatement();
            rs=null;
            ResultSetMetaData rsmd;
            ArrayList<String> campos=new ArrayList<String>();
            ArrayList<Integer> tipos=new ArrayList<Integer>();
            StringBuilder sb=null;
            String tmp="";            

            rs=stmt.executeQuery(parm);
            rsmd=rs.getMetaData();
            if ( table.equals("") )
                table=getTableByParm(parm);

            for ( int i=1;i<=rsmd.getColumnCount();i++ )
            {
                campos.add(rsmd.getColumnName(i));
                tipos.add(rsmd.getColumnType(i));
            }

            while ( rs.next() ){
                sb=new StringBuilder();
                for ( int i=0;i<campos.size();i++ ){
                    tmp=rs.getString(campos.get(i));

                    if ( tmp == null  ){
                        if ( i == campos.size()-1 )
                        {
                            sb.append("''");
                        }else{
                            sb.append("'',");
                        }
                        continue;
                    }
                    if ( tipo_cadastrado(tipos.get(i)) ){
                        if ( tipo_numerico(tipos.get(i)) && tmp.startsWith("."))
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            if ( flag_natal )
                                tmp="to_date('25/12/"+tmp.substring(0, 4)+dataT+tmp.substring(11, 19)+dataZ+"','DD/MM/YYYY HH24:MI:SS')";
                            else
                                tmp="to_date('"+tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+dataT+tmp.substring(11, 19)+dataZ+"','DD/MM/YYYY HH24:MI:SS')";
                        if ( tmp.length() <= 4000 ){
                            if ( tipos.get(i) == 93 ){ // DATA
                                sb.append(tmp);
                            }else{
                                sb.append("'");
                                sb.append(tmp.replace("'","''"));
                                sb.append("'");
                            }
                        }else{
                            tmp=formatacaoInsertClobComAspetas(tmp);
                            sb.append(tmp);
                        }
                        if ( i != campos.size()-1 )
                            sb.append(",");
                        continue;
                    }
                    close(rs,stmt,con);
                    throw new Exception("tipo desconhecido:"+tipos.get(i) + " -> " + rs.getString(campos.get(i)) );
                }                
                if ( out == null )
                    System.out.println("insert into "+table+" values("+ sb.toString()+");");
                else
                    out.write( ("insert into "+table+" values("+ sb.toString()+");\n").getBytes() );
                if ( countCommit++ >= 10000 ){
                    if ( out == null )
                        System.out.println("commit;");
                    else
                        out.write("commit;\n".getBytes());
                    countCommit=0;
                }
            }
        }
        catch(Exception e)
        {
            System.err.println("Erro: "+e.toString()+" -> "+parm);
            close(rs,stmt,con);
            System.exit(1);
        }        
        close(rs,stmt,con);        
    }
    
    public void pipeSelectInsertCSV(String conn,String fileCSV,String parm, PipedOutputStream out,String table,String nemVouExplicar){ // table opcional        
        
        /*
        Estrutura CSV padrao:
        
        HEADER_CAMPO1;BB;CC;3;4;5;
        11;;";;""""""11';;";55;55;55
        11;;";;""""""11';;";55;55;55
        11;;";;""""""11';;";55;55;55;
        33;44
        33;44
        33;44;44;44;44;44;44;44;44;44;44;44;44;44;44;44
        33;44;44;44;44;44;44;44;44;44;44;44;44;44;44;44
        obs: campos além do headr nao serão considerados
        */
        
        int countCommit=0;
        try{
            if ( ! fileCSV.equals("") )
                readLine(fileCSV);
            String line;
            String [] camposCSV=null;
            int qntCamposCSV=0;
            String valorColuna=null;
            StringBuilder sb=null;
            
            while ( (line=readLine()) != null ){
                if ( qntCamposCSV == 0 )
                {
                    camposCSV=getCamposCSV(line);
                    qntCamposCSV=camposCSV.length;
                    if ( ! nemVouExplicar.equals("") ){
                        String create=getCreateByCamposCSV(camposCSV,table);
                        if ( ! execute(nemVouExplicar, create) )
                            return;                                        
                    }
                    continue;
                }
                if ( line.trim().equals("") && qntCamposCSV > 1 )
                    break;
                sb=new StringBuilder();
                readColunaCSV(line); // init linhaCSV                
                for ( int i=0;i<qntCamposCSV;i++ ){                    
                    if ( linhaCSV != null ){
                        valorColuna=readColunaCSV();
                        if ( valorColuna == null )
                            linhaCSV=null; // nao precisar ler mais nada    
                    }
                    if ( valorColuna == null )
                        sb.append("''");
                    else
                        sb.append("'"+valorColuna.replace("'","''").replace("\"\"","\"")+"'");
                    if ( i != qntCamposCSV-1 )
                        sb.append(",");                        
                }                
                if ( out == null )
                    System.out.println("insert into "+table+" values("+ sb.toString()+");");
                else
                    out.write( ("insert into "+table+" values("+ sb.toString()+");\n").getBytes() );
                if ( countCommit++ >= 10000 ){
                    if ( out == null )
                        System.out.println("commit;");
                    else
                        out.write("commit;\n".getBytes());
                    countCommit=0;
                }
            }
            closeLine();
        }
        catch(Exception e)
        {
            if ( ! fileCSV.equals("") )
                System.err.println("Erro: "+e.toString());
            else
                System.err.println("Erro: "+e.toString()+" file:"+fileCSV);
            System.exit(1);
        }        
        
    }

    // comando "y selectCSV"(nao confundir com "y banco selectCSV")
    public String [] selectCSV_camposName=null;
    public String [] selectCSV_camposValue=null;
    public String [] selectCSV_camposNameSaida=null;
    public String [] selectCSV_camposNameSaidaAlias=null;    
    public String [] selectCSV_tratativasWhere=null;    
    public void selectCSV(String[] args) throws Exception {        
        
        String [] csvFile_sqlFile_sqlText=get_csvFile_sqlFile_sqlText(args);
        if ( csvFile_sqlFile_sqlText == null ){
            comando_invalido(args);
            return;
        }
        
        String csvFile=csvFile_sqlFile_sqlText[0];
        String sqlFile=csvFile_sqlFile_sqlText[1];
        String sqlText=csvFile_sqlFile_sqlText[2];
        OutputStream out=System.out;
        
        if ( !sqlFile.equals("") ){
            String line=null;
            sqlText="";
            readLineB(sqlFile);
            while ( (line=readLineB()) != null )
                sqlText+=line+" ";
            closeLineB();
        }

        if ( sqlText.equals("") ){ // trava de segurança
            comando_invalido(args);
            return;
        }
        
        try{
            if ( ! csvFile.equals("") )
                readLine(csvFile);
            String line;            
            int qntCamposCSV=0;
            String valorColuna=null;
                
            while ( (line=readLine()) != null ){
                // tratando header
                if ( qntCamposCSV == 0 )
                {
                    selectCSV_camposName=getCamposCSV(line);
                    qntCamposCSV=selectCSV_camposName.length;
                    selectCSV_camposValue=new String[selectCSV_camposName.length];
                    interpretaSqlParaSelectCSV(sqlText);
                    
                    StringBuilder sb=new StringBuilder();
                    for ( int i=0;i<selectCSV_camposNameSaidaAlias.length;i++ ){                    
                        sb.append("\"");
                        sb.append(selectCSV_camposNameSaidaAlias[i]);
                        if ( i < selectCSV_camposNameSaidaAlias.length-1 )
                            sb.append("\";");
                        else
                            sb.append("\"");
                    }
                    sb.append("\n");
                    out.write(sb.toString().getBytes());
                    continue;
                }
                
                if ( line.trim().equals("") && qntCamposCSV > 1 )
                    break;
                
                readColunaCSV(line); // init linhaCSV                
                
                for ( int i=0;i<qntCamposCSV;i++ ){                    
                    if ( linhaCSV != null ){
                        valorColuna=readColunaCSV();
                        if ( valorColuna == null )
                            linhaCSV=null; // nao precisar ler mais nada    
                    }
                    selectCSV_camposValue[i]=valorColuna;
                }     
                processaRegistroSqlParaSelectCSV(out);                
            }
            closeLine();
        }
        catch(Exception e)
        {
            if ( ! csvFile.equals("") )
                System.err.println("Erro: "+e.toString());
            else
                System.err.println("Erro: "+e.toString()+" file:"+csvFile);
            System.exit(1);
        }    
        out.flush();
        out.close();
    }

    // comando "y banco selectCSV"(nao confundir com "y selectCSV")
    public void selectCSV(String conn,String parm){
        
        boolean onlychar=false;
        String onlychar_=System.getenv("CSV_ONLYCHAR_Y");
        if ( onlychar_ != null && onlychar_.equals("S") )
            onlychar=true;
        boolean com_separador_final=false;
        String com_separador_final_=System.getenv("COM_SEPARADOR_FINAL_CSV_Y");
        if ( com_separador_final_ != null && com_separador_final_.equals("S") )
            com_separador_final=true;
        boolean semHeader=false;
        String semHeader_=System.getenv("SEM_HEADER_CSV_Y");
        if ( semHeader_ != null && semHeader_.equals("S") )
            semHeader=true;
        
                
        // configuracao TZ UTC NATAL YYYY-MM-DD
        String dataT=" ";
        String dataZ=" ";
        boolean flag_natal=false;
        boolean flag_ymd=false;
        String format_data=System.getenv("FORMAT_DATA_Y");
        if ( format_data != null && format_data.equals("TZ") ){
            dataT="T";
            dataZ="Z";
        }
        if ( format_data != null && format_data.equals("UTC") )
            dataZ=" UTC";
        if ( format_data != null && format_data.equals("NATAL") )
            flag_natal=true;
        if ( format_data != null && format_data.equals("YYYY-MM-DD") )
            flag_ymd=true;
        
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;
        long count=0;
        String parm_=parm;
        
        try{
            con = getcon(conn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return;
            }

            if ( parm.equals("") ){
                String line;
                while( (line=readLine()) != null )
                    parm+=line+"\n";                
                closeLine();
            }
            parm=removePontoEVirgual(parm);

            stmt = con.createStatement();
            rs=null;
            ResultSetMetaData rsmd;
            ArrayList<String> campos=new ArrayList<String>();
            ArrayList<Integer> tipos=new ArrayList<Integer>();
            StringBuilder sb=null;
            String tmp="";
            String table="";
            String header="";
            String first_detail="";
            boolean first=true;

            rs=stmt.executeQuery(parm);
            rsmd=rs.getMetaData();
            table=getTableByParm(parm);

            for ( int i=1;i<=rsmd.getColumnCount();i++ )
            {
                campos.add(rsmd.getColumnName(i));
                tipos.add(rsmd.getColumnType(i));
            }

            while ( rs.next() ){
                sb=new StringBuilder();
                for ( int i=0;i<campos.size();i++ ){
                    tmp=rs.getString(campos.get(i));

                    if ( tmp == null  ){
                        if ( first )
                        {
                            if ( i == campos.size()-1 && !com_separador_final ){
                                header+="\""+campos.get(i)+"\"";
                                first_detail+="\"\"";
                            }else{
                                header+="\""+campos.get(i)+"\""+sepCSV;
                                first_detail+="\"\""+sepCSV;
                            }
                        }else{
                            sb.append("\"\"");
                            sb.append(sepCSV);
                        }
                        continue;
                    }
                    if ( tipo_cadastrado(tipos.get(i)) ){
                        if ( tipo_numerico(tipos.get(i)) && tmp.startsWith(".") )
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            if ( flag_natal )
                                tmp="25/12/"+tmp.substring(0, 4)+dataT+tmp.substring(11, 19)+dataZ;
                            else
                                if ( flag_ymd )
                                    tmp=tmp.substring(0, 4)+"-"+tmp.substring(5, 7)+"-"+tmp.substring(8, 10)+dataT+tmp.substring(11, 19)+dataZ;
                                else
                                    tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+dataT+tmp.substring(11, 19)+dataZ;
                        // o csv suporta ".."".." mas para ficar mais simples o comando abaixo tira o "
                        tmp=tmp.replace("\"","").replace("\n","");
                        tmp=tmp.trim();
                        
                        if ( first )
                        {
                            if ( i == campos.size()-1 && !com_separador_final ){
                                header+="\""+campos.get(i)+"\"";
                                if ( onlychar && tipo_numerico(tipos.get(i)) ){
                                    first_detail+=tmp;
                                }else{
                                    first_detail+="\""+tmp+"\"";
                                }
                            }else{
                                header+="\""+campos.get(i)+"\""+sepCSV;
                                if ( onlychar && tipo_numerico(tipos.get(i)) ){
                                    first_detail+=tmp+sepCSV;
                                }else{
                                    first_detail+="\""+tmp+"\""+sepCSV;
                                }
                            }
                        }else{
                            // nao imprime delimitador em onlychar e tipos.get(i) == 2
                            if ( onlychar && tipo_numerico(tipos.get(i)) ){
                            }else{
                                sb.append("\"");
                            }
                            sb.append(tmp);
                            if ( onlychar && tipo_numerico(tipos.get(i)) ){
                            }else{
                                sb.append("\"");
                            }
                            if ( i == campos.size()-1 && !com_separador_final ){
                            }else{
                                sb.append(sepCSV);
                            }
                        }

                        continue;
                    }
                    close(rs,stmt,con);
                    throw new Exception("tipo desconhecido:"+tipos.get(i) + " -> " + rs.getString(campos.get(i)) );
                }

                if ( first ){
                    first=false;
                    if ( !semHeader )
                        System.out.println(header);
                    System.out.println(first_detail);
                    count++;
                    continue;
                }

                System.out.println(sb.toString());                
                count++;
            }        
        }
        catch(Exception e){
            System.err.println("Erro: "+e.toString()+" -> "+parm_);
            close(rs,stmt,con);
            System.exit(1);
        }   
        close(rs,stmt,con);
        try_finish_and_count(count);
    }

    public void executeInsert(String conn, InputStream pipe){        
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;
        
        boolean par=true;
        String line="";
        StringBuilder sb=null;
        boolean ok=true;
        
        int limiteAgulha=50000; // all.length();
        String initial_sb=" insert all";
        String final_sb=" select * from dual";
        
        String ii;
        StringBuilder all=new StringBuilder(initial_sb);
        ArrayList<String> cover=new ArrayList<String>();

        String command="";
        boolean achou=false;
        
        readLineB(pipe);
        
        try{
            con = getcon(conn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return;
            }
            con.setAutoCommit(false);
            stmt = con.createStatement();

            while( (line=readLineB()) != null ){
                if ( par && line.trim().equals("") )
                    continue;
                if ( par ){
                    if ( line.trim().startsWith("commit") || line.trim().startsWith("COMMIT") )
                    {
                        try{  
                            ii=removePontoEVirgual(line);
                            stmt.execute(ii);
                        }catch(Exception e){
                            System.err.println("Erro: "+e.toString()+" -> "+line);
                            close(rs,stmt,con);
                            System.exit(1);
                        }
                        continue;
                    }
                    if ( startingInsert(line) )
                    {
                        if ( par=countParAspeta(par,line) ){
                            try{
                                ii=removePontoEVirgual(line);                                
                                if ( all.length() >= limiteAgulha ){
                                    all.append(ii.substring(6));
                                    cover.add(ii);
                                    all.append(final_sb);
                                    try{
                                        stmt.execute(all.toString());
                                    }catch(Exception e){
                                        // repescagem
                                        for(String iii : cover){
                                            try{
                                                stmt.execute(iii);
                                            }catch(Exception ee){
                                                achou=false;
                                                for ( String ora : ORAs ){
                                                    if ( ee.toString().contains(ora) ){
                                                        System.out.println("Warnning: "+iii);
                                                        ok=false;
                                                        achou=true;
                                                        break;
                                                    }
                                                }
                                                if ( ! achou ){
                                                    command=iii;
                                                    throw ee;
                                                }
                                            }                                            
                                        }
                                    }
                                    
                                    all=null;                                    
                                    all=new StringBuilder(initial_sb);
                                    cover=null;
                                    cover=new ArrayList<String>();
                                }else{
                                    all.append(ii.substring(6));
                                    cover.add(ii);
                                }
                                
                            }catch(Exception e){
                                System.err.println("Erro: "+e.toString()+" -> "+line);
                                close(rs,stmt,con);
                                System.exit(1);
                            }
                            continue;
                        }else{
                            sb=null;// forçando limpeza de memoria
                            sb=new StringBuilder(line);
                        }
                        continue;
                    }
                    close(rs,stmt,con);
                    throw new Exception("Erro, linha inesperada:" +line);
                }else{
                    if ( par=countParAspeta(par,line) ){
                        try{
                            sb.append("\n");
                            sb.append(removePontoEVirgual(line));
                            ii=sb.toString();
                            if ( all.length() >= limiteAgulha ){
                                all.append(ii.substring(6));
                                cover.add(ii);
                                all.append(final_sb);
                                
                                try{
                                    stmt.execute(all.toString());
                                }catch(Exception e){
                                    // repescagem
                                    for(String iii : cover){
                                        try{
                                            stmt.execute(iii);
                                        }catch(Exception ee){
                                            achou=false;
                                            for ( String ora : ORAs ){
                                                if ( ee.toString().contains(ora) ){
                                                    System.out.println("Warnning: "+iii);
                                                    ok=false;
                                                    achou=true;
                                                    break;
                                                }
                                            }
                                            if ( ! achou ){
                                                command=iii;
                                                throw ee;
                                            }
                                        }                                            
                                    }
                                }
                                    
                                all=null;                                
                                all=new StringBuilder(initial_sb);
                                cover=null;
                                cover=new ArrayList<String>();
                            }else{
                                all.append(ii.substring(6));
                                cover.add(ii);
                            }
                            
                        }catch(Exception e){
                            System.err.println("Erro: "+e.toString()+" -> "+line);
                            close(rs,stmt,con);
                            System.exit(1);
                        }
                        continue;
                    }else{
                        sb.append("\n");
                        sb.append(line);
                    }
                }
            }
            if ( ! all.toString().equals(initial_sb) ){
                all.append(final_sb);
                
                try{
                    stmt.execute(all.toString());
                }catch(Exception e){
                    // repescagem
                    for(String iii : cover){
                        try{
                            stmt.execute(iii);
                        }catch(Exception ee){
                            achou=false;
                            for ( String ora : ORAs ){
                                if ( ee.toString().contains(ora) ){
                                    System.out.println("Warnning: "+iii);
                                    ok=false;
                                    achou=true;
                                    break;
                                }
                            }
                            if ( ! achou ){
                                command=iii;
                                close(rs,stmt,con);
                                throw ee;
                            }
                        }                                            
                    }
                }

                all=null;
                cover=null;
            }
            close(rs,stmt,con);
        }catch(Exception e){
            if ( ! command.equals(""))
                System.err.println("Erro: "+e.toString().replace("\n","")+" -> "+command);                
            else
                System.err.println("Erro: "+e.toString());
            close(rs,stmt,con);
            System.exit(1);
        }   
        if ( ok )
            System.out.println("OK");
        close(rs,stmt,con);
        closeLineB();                
    }

    public boolean execute(String conn,String parm){
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;
        
        try{
            con = getcon(conn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return false;
            }

            if ( parm.equals("") ){
                String line;
                while( (line=readLine()) != null )
                    parm+=line+"\n";   
                closeLine();
            }

            if ( ! parm.trim().toUpperCase().startsWith("DECLARE") )
                parm=removePontoEVirgual(parm);
            
            stmt = con.createStatement();
            stmt.execute(parm);
            System.out.println("OK");
        }
        catch(Exception e)
        {
            System.err.println("Erro: "+e.toString()+" -> "+parm);
            close(rs,stmt,con);
            System.exit(1);
            return false;
        }        
        close(rs,stmt,con);
        return true;
    }

    public void buffer(String [] args){
        if ( args.length == 2 ){
            buffer(n_lines_buffer_DEFAULT,null);
            return;
        }
        if ( args.length == 4 && args[2].equals("-n_lines") ){
            buffer(tryConvertNumberPositiveByString(n_lines_buffer_DEFAULT,args[3]),null);
            return;
        }
        if ( args.length == 4 && args[2].equals("-log") ){
            buffer(n_lines_buffer_DEFAULT,args[3]);
            return;
        }
        if ( args.length == 6 && args[2].equals("-n_lines") && args[4].equals("-log") ){
            buffer(tryConvertNumberPositiveByString(n_lines_buffer_DEFAULT,args[3]),args[5]);
            return;
        }
    }
            
    public void buffer(final int n_lines_buffer,String caminhoLog_){
        try{     
            final List<String> lista=Collections.synchronizedList(new ArrayList<String>());
            
            final boolean [] finishIn=new boolean[]{false};
            final long [] countLinhasIn=new long []{0,0,0}; // 0-> contador;1-> contador informado; 2-> contador desligado
            final long [] countLinhasOut=new long []{0,0,0}; // 0-> contador;1-> contador informado; 2-> contador desligado
            final int sizeMaskSpeedCount=6;
            final int sizeMaskBufferCount=(n_lines_buffer+"").length();
            final String [] caminhoLog=new String[]{caminhoLog_};
            final PrintWriter [] out=new PrintWriter[1];
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
            
            // abrindo arquivo
            if ( caminhoLog[0] != null )
            {
                try{                    
                    out[0]=new PrintWriter(caminhoLog[0], "UTF-8");                    
                }catch(Exception e){
                    caminhoLog[0]=null;
                }
            }

            // thread log
            if ( caminhoLog[0] != null )
            {
                new Thread() {
                    public void run() {
                        try{
                            long time1=System.currentTimeMillis();                        
                            long time2=time1;                            
                            long countIn;
                            long countOut;

                            out[0].println(formatter.format(new Date()) + " - start");
                            out[0].flush();
                            while(true)
                            {
                                time2=System.currentTimeMillis();
                                if ( countLinhasIn[2] == 1 && countLinhasOut[2] == 1 ){ // fim
                                    break;
                                }
                                if ( time2 >= time1+1000 )
                                {
                                    time1=time2;                                    
                                    countIn=0;
                                    countOut=0;
                                    
                                    if ( countLinhasIn[1] == 0 ){
                                        countIn=countLinhasIn[0];
                                        countLinhasIn[1]=countIn;
                                    }
                                    if ( countLinhasOut[1] == 0 ){
                                        countOut=countLinhasOut[0];
                                        countLinhasOut[1]=countOut;
                                    }
                                    out[0].println( formatter.format(new Date()) + " - linhas/s[in]: " + lpad(countIn,sizeMaskSpeedCount," ") + " - linhas/s[out]: " + lpad(countOut,sizeMaskSpeedCount," ") + " - buffer: " + lpad(lista.size(),sizeMaskBufferCount," ") );
                                    out[0].flush();
                                }
                                Thread.sleep(100);
                            }
                        }catch(Exception e){
                            caminhoLog[0]=null;
                        }
                    }
                }.start();  
            }

            // thread in
            new Thread() {
                public void run() {
                    String line;
                    while( true ){
                        if ( lista.size() < n_lines_buffer )
                        {
                            if ( (line=readLine()) != null )
                            {
                                lista.add(line);
                                if ( caminhoLog[0] != null )
                                    contabiliza(countLinhasIn);
                            }else{
                                finishIn[0]=true;
                                break;
                            }
                        }
                    }
                    closeLine();
                    countLinhasIn[2]=1;
                }
            }.start();        
            
            // saida
            while( true ){
                if ( finishIn[0] ){
                    while(lista.size() > 0){
                        System.out.println(lista.get(0));
                        lista.remove(0);
                        if ( caminhoLog[0] != null )
                            contabiliza(countLinhasOut);
                    }
                    countLinhasOut[2]=1;
                    break;
                }
                if (lista.size() > 0){
                    System.out.println(lista.get(0));
                    lista.remove(0);
                    if ( caminhoLog[0] != null )
                        contabiliza(countLinhasOut);
                }  
            }

            // fechando arquivo
            if ( caminhoLog[0] != null )
            {
                try{
                    out[0].println(formatter.format(new Date()) + " - end");
                    out[0].flush();
                    out[0].close();                    
                }catch(Exception e){
                    System.out.println(e.toString());
                }
            }
            
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void contabiliza(long [] countLinhas){
        if ( countLinhas[1] > 0 )
        {
            countLinhas[0]-=countLinhas[1];
            if ( countLinhas[0] < 0 )
                countLinhas[0]=0;
            countLinhas[1]=0;                
        }
        countLinhas[0]++;
    }

    public String lpad(long inputLong, int length,String append) {
        if ( inputLong < 0 )
            return lpad(true,(inputLong+"").substring(1),length,append);
        else
            return lpad(false,inputLong+"",length,append);
    }

    public String lpad(String inputString, int length,String append) {
        return lpad(false,inputString,length,append);
    }

    public String lpad(boolean sinalNegativo, String inputString, int length,String append) {
        int len_input=inputString.length();
        if ( len_input >= length) {
            if ( sinalNegativo )
                return "-"+inputString;
            else
                return inputString;
        }
        StringBuilder sb = new StringBuilder(sinalNegativo?"-":"");
        for ( int i=0;i<length-len_input;i++ )
            sb.append(append);
        sb.append(inputString);
        return sb.toString();
    }

    public String gettoken(String hash){
        
        String dir_token=getenv();
        if ( ! env_ok(dir_token) )
            return null;
        return lendo_token(dir_token,hash);
    }

    public String apresentacao(String [] programas)
    {
        String retorno="";
        for ( int i=0;i<programas.length;i++ )
            retorno+="\n  [" + programas[i] + "]";
        return "usage:"+retorno;
    }
    public boolean env_ok(String dir_token)
    {
        if ( dir_token == null ){
            System.err.println("Para usar o token é necessário ter a variável de ambiente TOKEN_Y definida, ex export TOKEN_Y=/home/user/.token_y");
            return false;
        }
        File f = new File(dir_token);
        if ( ! f.exists() ){
            if ( ! f.mkdir() ){
                System.err.println("Não foi possível utilizar/criar a pasta "+dir_token);
                return false;
            }
        }
        if ( ! f.isDirectory() ){
            System.err.println("O caminho "+dir_token+" não é um diretório");
            return false;

        }
        return true;
    }
    public String salvando_token(String dir_token,String value){
        dir_token=fix_caminho(dir_token);
        String md5=digest_text(value,"MD5");
        if(salvando_file(value+"\n",new File(dir_token+md5)))
            return md5;
        return null;
    }
    public String lendo_token(String dir_token,String md5){
        dir_token=fix_caminho(dir_token);
        if ( ! new File(dir_token+md5).exists() )
            return null;
        return lendo_arquivo(dir_token+md5);
    }
    public boolean salvando_file(String texto, File arquivo) {
        return salvando_file(texto, arquivo, false);
    }
    public boolean salvando_file(String texto, File arquivo, boolean append) {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(arquivo, append));
            out.write(texto);
            out.flush();
            out.close();
            return true;
        }catch(Exception e){
            System.err.println(e.toString());
        }        
        return false; 
    }

    public void try_load_libraries(){
        try{
            Class.forName("oracle.jdbc.OracleDriver");
        }catch (Exception e){
            System.err.println("Não foi possível carregar a biblioteca Oracle");
            System.exit(1);
        }            
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch (Exception e){
            System.err.println("Não foi possível carregar a biblioteca SQL Server");
            System.exit(1);
        }                    
    }
    public void try_load_ORAs() {        
        ORAs=lendo_arquivo_pacote("/y/ORAs").split("\n");
        
        try{
            String caminho=System.getenv("ORAs_Y");
            if ( ! new File(caminho).exists() ) return;
            ArrayList<String> lista=new ArrayList<String>();
            String line;

            readLine(caminho);
            while ((line = readLine()) != null)
                lista.add(line);
            closeLine();

            if ( lista.size() > 0 )
            {
                ORAs=new String[lista.size()];
                for ( int i=0;i<lista.size();i++ )
                    ORAs[i]=lista.get(i);
            }
        }catch (Exception e){
            //try_load_ORAs
        }
    }
    
    public String fix_caminho(String caminho){
        if ( ! caminho.endsWith("/") && caminho.contains("/") )
            return caminho+"/";
        if ( ! caminho.endsWith("\\") && caminho.contains("\\") )
            return caminho+"\\";
        return caminho;
    }
    public String getenv(){
        if ( local_env != null && new File(local_env).exists() )
            return local_env;
        return System.getenv("TOKEN_Y");
    }

    public String getTableByParm(String parm){
        String retorno="";
        try{
            retorno=parm.toUpperCase().replace(")"," ").replace("\n"," ").replace(","," ").replace("*"," ").split("FROM ")[1].split(" ")[0].trim();
            if ( retorno.length() == 0 )
                return "";
        }catch(Exception e){
            return "";
        }
        return retorno;
    }
            
    public String removePontoEVirgual(String txt){
        String retorno=RTRIM(txt);
        if ( retorno.endsWith(";") )
            return retorno.substring(0,retorno.length()-1);
        return retorno;
    }

    public boolean startingInsert(String txt){
        txt=LTRIM(txt);
        return txt.startsWith("insert") || txt.startsWith("INSERT");
    }

    public boolean countParAspeta(boolean par,String txt){        
        int p=0;
        int len=txt.length();
        while(true){
            p=txt.indexOf("'",p);
            if ( p == -1 )
                return par;
            else{
                par=!par;
                p++;
                if ( p >= len )
                    return par;
            }            
        }
    }

    public String LTRIM(String txt){
        return txt.replaceAll("^\\s+","");
    }

    public String RTRIM(String txt){
        return txt.replaceAll("\\s+$","");
    }

    public char[] encodeHex(byte[] data) {
        char[] toDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public Connection getcon(String stringcon){
        if ( stringcon.startsWith("jdbc:sqlserver") ){
            //SQLServer            
            try {      
                //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                return DriverManager.getConnection(stringcon);
            } catch (Exception x) {
                System.err.println("Erro na conexão:"+x.toString());
            }
        }else{
            //Oracle
            if ( stringcon.split("\\|").length != 3){
                System.err.println("Erro na conexão: Login e senha não encontrado!");
                return null;
            }else{
                String par = stringcon.split("\\|")[0];
                String user = stringcon.split("\\|")[1];
                String pass = stringcon.split("\\|")[2];
                try {
                    //Class.forName("oracle.jdbc.OracleDriver");
                    return DriverManager.getConnection(par, user, pass);
                } catch (Exception x) {
                    System.err.println("Erro na conexão:"+x.toString());
                }
            }
        }
        return null;
    }
    
    /*
    y zip add File1.txt > saida.zip
    cat File1.txt | y zip add -name File1.txt > saida.zip
    y zip add /pasta1 > saida.zip
    y zip list arquivo.zip
    cat arquivo.zip | y zip list
    y zip extract entrada.zip
    cat entrada.zip | y zip extract
    y zip extract entrada.zip -out /destino
    cat entrada.zip | y zip extract -out /destino
    y zip extractSelected entrada.zip pasta1/unicoArquivoParaExtrair.txt -out /destino
    cat entrada.zip | y zip extractSelected pasta1/unicoArquivoParaExtrair.txt -out /destino
    y zip extractSelected entrada.zip pasta1/unicoArquivoParaExtrair.txt > /destino/unicoArquivoParaExtrair.txt
    cat entrada.zip | y zip extractSelected pasta1/unicoArquivoParaExtrair.txt > /destino/unicoArquivoParaExtrair.txt
    */
    private void zip_add(String a,String dummy_name) throws Exception {
        if ( a != null ){            
            valida_leitura_arquivo(a);
            if ( a.equals(".") || a.equals("..") ){
                System.err.println("Erro, esse tipo de diretório não é válido!: "+a);
                System.exit(1);
            }
        }
        zip_add(a,dummy_name,System.out);
    }

    private void valida_leitura_arquivo(String a){
        if ( ! new File(a).exists() ){
            System.err.println("Erro, esse conteudo nao existe: "+a);
            System.exit(1);
        }
    }
    
    private java.util.zip.ZipOutputStream zip_output=null;
    private ArrayList<String> zip_elementos=null;
    private void zip_add(String a, String dummy_name, OutputStream os) throws Exception {
        zip_output = new java.util.zip.ZipOutputStream(os);        
        File elem=null;
        String dummy="dummy";
        if ( dummy_name != null && dummy_name.length() > 0 )
            dummy=dummy_name;
        if ( a!=null )
            elem=new File(a);        
        if ( elem==null || elem.isFile() ){            
            java.util.zip.ZipEntry e=null;
            if ( elem == null )
                e=new java.util.zip.ZipEntry(dummy);
            else
                e=new java.util.zip.ZipEntry(elem.getName());
            zip_output.putNextEntry(e);
            if ( elem!=null )
                readBytes(elem);
            byte[] buf = new byte[BUFFER_SIZE];                                    
            int len;
            while ((len = readBytes(buf)) > -1)
                zip_output.write(buf, 0, len);
            closeBytes();
        }else{
            String path=elem.getAbsolutePath().replace("\\","/");
            if ( ! path.endsWith("/") )
                path+="/";
            zip_elementos=new ArrayList<String>();
            if ( !a.contains("/") && !a.contains("\\") ){
                path+="../";
                zip_elementos.add(a+"/");
                zip_navega(elem,a+"/");
            }else{
                zip_navega(elem,"");
            }
            int lenB=zip_elementos.size();
            for ( int i=0;i<lenB;i++ ){
                java.util.zip.ZipEntry e=new java.util.zip.ZipEntry( zip_elementos.get(i) );
                zip_output.putNextEntry(e);
                if ( ! zip_elementos.get(i).endsWith("/") ){                    
                    readBytes(path+zip_elementos.get(i));
                    byte[] buf = new byte[BUFFER_SIZE];                        
                    int len;
                    while ((len = readBytes(buf)) > -1)
                        zip_output.write(buf, 0, len);                
                    closeBytes();            
                }
            }
        }        
        zip_output.closeEntry();
        zip_output.flush();
        zip_output.close();
    }
    
    private void zip_navega(File a, String caminho) {
        java.io.File[] filhos=a.listFiles();
        for ( int i=0;i<filhos.length;i++ ){
            if ( filhos[i].isFile() )
                zip_elementos.add(caminho+filhos[i].getName());
            if ( filhos[i].isDirectory() ){
                zip_elementos.add(caminho+filhos[i].getName()+"/");
                zip_navega(filhos[i],caminho+filhos[i].getName()+"/");
            }
        }
    }
    
    private void zip_list(String a) throws Exception {
        valida_leitura_arquivo(a);
        if ( a == null ){
            ZipInputStream zis=new ZipInputStream(System.in);
            ZipEntry entry=null;
            while( (entry=zis.getNextEntry()) != null ){
                System.out.println(entry.getName());
            }
        }else{
            ZipFile zipFile = new ZipFile(a);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();            
                System.out.println(entry.getName());
            }                
        }
    }

    private int zip_extract_count_encontrados=0;
    private void zip_extract(String a, String pre_dir, String filtro) throws Exception {
        zip_extract_count_encontrados=0;
        if ( filtro != null && filtro.endsWith("/") ){
            System.err.println("Erro, o item selecionado não pode ser uma pasta!: "+filtro);
            System.exit(1);
        }
        if ( pre_dir != null ){
            pre_dir=pre_dir.trim();
            if ( pre_dir.length() == 0 ){
                System.err.println("Erro, preenchimento incorreto de pasta!");
                System.exit(1);
            }
            File pasta_=new File(pre_dir);
            if ( ! pasta_.exists() ){
                System.err.println("Erro, a pasta "+pre_dir+ " não existe!");
                System.exit(1);
            }else{
                if ( ! pasta_.isDirectory() ){
                    System.err.println("Erro, o caminho a seguir não é uma pasta: "+pre_dir);
                    System.exit(1);
                }
            }
            pre_dir=pre_dir.replace("\\","/");
            if ( !pre_dir.endsWith("/") )
                pre_dir+="/";
        }else
            pre_dir="";
        if ( a != null )
            valida_leitura_arquivo(a);
        if ( a == null ){
            ZipInputStream zis=new ZipInputStream(System.in);
            ZipEntry entry=null;
            while( (entry=zis.getNextEntry()) != null ){                                
                if ( entry.getName().endsWith("/") ){
                    zip_extract_grava(pre_dir,entry.getName(),null,filtro);
                }else{
                    zip_extract_grava(pre_dir,entry.getName(),zis,filtro);
                }
            }
        }else{
            ZipFile zipFile = new ZipFile(a);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();            
                if ( entry.getName().endsWith("/") ){
                    zip_extract_grava(pre_dir,entry.getName(),null,filtro);
                }else{
                    zip_extract_grava(pre_dir,entry.getName(),zipFile.getInputStream(entry),filtro);
                }
            }                
        }
        if ( filtro != null && zip_extract_count_encontrados == 0 ){
            System.err.println("Erro, elemento "+filtro+" não encontrado!");
            System.exit(1);
        }
    }

    private void zip_extract_grava(String pre_dir, String name, InputStream is,String filtro) throws Exception {
        String [] partes=name.split("/");
        String dir="";
        File tmp=null;
        boolean out_console=false;
        if ( filtro != null && pre_dir.equals("") )
            out_console=true;
        for ( int i=0;i<partes.length;i++ ){
            if ( i == partes.length-1 ){
                if ( is == null ){
                    dir+=partes[i]+"/";
                    if ( filtro != null && filtro.indexOf(dir) == -1 )
                        continue;
                    if ( ! out_console ){
                        tmp=new File(pre_dir+dir);
                        if ( tmp.exists() ){
                            if ( !tmp.isDirectory() ){
                                System.err.println("Erro, não é possível utilizar o caminho a seguir como pasta: "+pre_dir+dir);
                                System.exit(1);
                            }
                        }else
                            tmp.mkdir();
                    }
                }else{
                    dir+=partes[i];
                    if ( filtro != null && filtro.indexOf(dir) == -1 )
                        continue;
                    if ( ! out_console ){
                        tmp=new File(pre_dir+dir);
                        if ( tmp.exists() ){
                            if ( tmp.isDirectory() ){
                                System.err.println("Erro, não é possível utilizar o caminho a seguir como arquivo: "+pre_dir+dir);
                                System.exit(1);
                            }
                        }
                    }
                    if ( filtro == null ){
                        copiaByStream(is,new FileOutputStream(new File(pre_dir+dir)));
                    }else{
                        if ( filtro.equals(dir) ){
                            zip_extract_count_encontrados++;
                            if ( out_console ){
                                copiaByStream(is,System.out);
                            }else{
                                copiaByStream(is,new FileOutputStream(new File(pre_dir+dir)));
                            }
                        }
                    }
                }
            }else{
                dir+=partes[i]+"/";
                if ( filtro != null && filtro.indexOf(dir) == -1 )
                    continue;
                if ( ! out_console ){
                    tmp=new File(pre_dir+dir);
                    if ( tmp.exists() ){
                        if ( !tmp.isDirectory() ){
                            System.err.println("Erro, não é possível utilizar o caminho a seguir como pasta: "+pre_dir+dir);
                            System.exit(1);
                        }
                    }else
                        tmp.mkdir();
                }
            }
        }
    }
    
    public void gzip()
    {
        try{            
            byte[] buf = new byte[BUFFER_SIZE];            
            java.util.zip.GZIPOutputStream out = new java.util.zip.GZIPOutputStream(System.out);
            int len;
            while ((len = readBytes(buf)) > -1)
                out.write(buf, 0, len);
            out.finish();  
            closeBytes();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void gunzip()
    {
        try{            
            byte[] buf = new byte[BUFFER_SIZE];
            java.util.zip.GZIPInputStream out = new java.util.zip.GZIPInputStream(System.in);
            int len;
            while ((len = out.read(buf)) > -1)
                System.out.write(buf, 0, len);            
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void echo(String [] args)
    {
        args = bind_asterisk(args);
        printf(args);
        System.out.println();
    }

    public void printf(String [] args)
    {
        if ( args.length > 1 )
            System.out.print(args[1]);
        for ( int i=2;i<args.length;i++ )
            System.out.print(" "+args[i]);        
    }
    
    public void cat(String [] caminhos)
    {
        try{
            if ( caminhos.length > 1 ){
                for ( int i=1;i<caminhos.length;i++ )
                {
                    if ( ! new File(caminhos[i]).exists() ){
                        System.err.println("Erro, este arquivo não existe: "+caminhos[i]);
                        return;
                    }
                }
                for ( int i=1;i<caminhos.length;i++ )
                {                
                    byte[] buf = new byte[BUFFER_SIZE];            
                    FileInputStream fis = new FileInputStream(caminhos[i]);
                    int len;
                    while ((len = fis.read(buf)) > -1)
                        System.out.write(buf, 0, len);            
                    fis.close();
                }
            }else{
                InputStream inputStream_pipe=System.in;
                int BUFFER_SIZE=1024;
                byte[] buf = new byte[BUFFER_SIZE];
                int len=0;
                while( (len=inputStream_pipe.read(buf,0,BUFFER_SIZE)) > 0 ){
                    System.out.write(buf, 0, len);
                }
                System.out.flush();
                System.out.close();
            }
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
        }
    }

    public void xor(int parm){
        while(parm < 0)
            parm+=256;
        while(parm >= 256)
            parm-=256;        
        try{
            InputStream inputStream_pipe=System.in;
            int BUFFER_SIZE=1024;
            byte[] buf = new byte[BUFFER_SIZE];
            int len=0;
            while( (len=inputStream_pipe.read(buf,0,BUFFER_SIZE)) > 0 ){
                for( int i=0;i<len;i++ )
                    buf[i]^=parm;
                System.out.write(buf, 0, len);
            }
            System.out.flush();
            System.out.close();
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
        }
    }
    
    public void digest(String tipo){        
        try {
            MessageDigest digest=MessageDigest.getInstance(tipo);            
            byte[] buf = new byte[BUFFER_SIZE];            
            int len;
            while( (len=readBytes(buf)) > -1 )
                digest.update(buf, 0, len);
            closeBytes();
            System.out.println(new String(encodeHex(digest.digest())));
        } catch (Exception ex) {
            System.err.println("Erro: "+ex.toString());
        }
    }

    public String digest_text(String txt,String tipo){
        try {
            byte[] bytesOfMessage = txt.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance(tipo);            
            return new String(encodeHex(md.digest(bytesOfMessage)));                
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }
    
    public int tryConvertNumberPositiveByString(int n_lines_buffer,String value){
        try{
            int tmp=Integer.parseInt(value);
            if ( tmp >= 0 )
                return tmp;
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return n_lines_buffer;
    }
    
    public void grep(String [] args)
    {
        boolean print=false;
        String line_bkp="";
        String line="";

        ArrayList<Boolean> lista_starting=new ArrayList<Boolean>();
        ArrayList<Boolean> lista_ending=new ArrayList<Boolean>();
        ArrayList<String> lista_txt=new ArrayList<String>();
        
        ArrayList<String> lista=get_v_i_txt(args);
        if ( lista == null ){
            comando_invalido(args);
            return;
        }
        boolean v_=lista.get(0).equals("S");
        boolean i_=lista.get(1).equals("S");
        
        for ( int i=2;i<lista.size();i+=3 ){
            lista_starting.add(lista.get(i).equals("S"));
            lista_ending.add(lista.get(i+1).equals("S"));
            lista_txt.add(lista.get(i+2));
        }
        
        if ( i_ )
            lista_txt.set(0,lista_txt.get(0).toUpperCase());
        
        try {            
            while ( (line=line_bkp=readLine()) != null ) {
                print=false;
                if ( i_ )
                    line=line.toUpperCase();
                for ( int i=0;i<lista_txt.size();i++ ){
                    if ( lista_txt.get(i).equals("") && lista_starting.get(i) && lista_ending.get(i) ){ // ^$
                        if (line.equals("")){
                            print=true;
                            break;
                        }else
                            continue;
                    }
                    if ( !lista_starting.get(i) && !lista_ending.get(i) && line.contains(lista_txt.get(i)) ){
                        print=true;
                        break;
                    }
                    if ( lista_starting.get(i) && !lista_ending.get(i) && line.startsWith(lista_txt.get(i)) ){
                        print=true;
                        break;
                    }
                    if ( !lista_starting.get(i) && lista_ending.get(i) && line.endsWith(lista_txt.get(i)) ){
                        print=true;
                        break;
                    }
                    if ( lista_starting.get(i) && lista_ending.get(i) && line.startsWith(lista_txt.get(i)) && line.endsWith(lista_txt.get(i)) ){
                        print=true;
                        break;
                    }
                }
                
                if ( v_ )
                    print=!print;
                
                if ( print )
                    System.out.println(line);
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString()+" - "+line_bkp);
        }
    }
    
    public void wc_l()
    {
        try{
            InputStream inputStream_pipe=System.in;
            int BUFFER_SIZE=1024;
            byte[] buf = new byte[BUFFER_SIZE];
            int count=0;
            int len=0;
            byte[] n_="\n".getBytes();
            while( (len=inputStream_pipe.read(buf,0,BUFFER_SIZE)) > 0 ){
                for ( int i=0;i<len;i++ )
                    if (buf[i] == n_[0])
                        count++;
            }
            System.out.println(count);
        }catch(Exception e){
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    public void len()
    {
        try{
            InputStream inputStream_pipe=System.in;
            int BUFFER_SIZE=1024;
            byte[] buf = new byte[BUFFER_SIZE];            
            int len=0;
            int count=0;
            byte[] n_="\n".getBytes();
            byte[] r_="\r".getBytes();
            boolean any=false;
            while( (len=inputStream_pipe.read(buf,0,BUFFER_SIZE)) > 0 ){
                any=true;
                for ( int i=0;i<len;i++ ){
                    if (buf[i] == n_[0]){
                        System.out.println(count);
                        count=0;
                    }else{
                        if (buf[i] != r_[0])
                            count++;
                    }
                }
            }
            if ( count > 0 || !any )
                System.out.println(count);            
        }catch(Exception e){
            System.err.println(e.toString());
            System.exit(1);
        }
    }
    
    public void head(String [] args)
    {
        long p;
        String line;
        long count=0;
        
        try{
            if ( args.length == 1 )
                p=10;
            else
                p=Long.parseLong(args[1].substring(1));
        }catch(Exception e){
            comando_invalido(args);
            return;
        }
        
        try {
            while ( (line=readLine()) != null ) {
                if ( ++count <= p )
                    System.out.println(line);
                else{
                    closeLine();
                    break;
                }
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
            
    public void tail(String [] args)
    {                
        int p;
        String line;
        ArrayList<String> lista=new ArrayList<String>();
        
        try{
            if ( args.length == 1 )
                p=10;
            else
                p=Integer.parseInt(args[1].substring(1));
        }catch(Exception e){
            comando_invalido(args);
            return;
        }
        
        try {
            while ( (line=readLine()) != null ) {
                lista.add(line);
                if ( lista.size() > p )
                    lista.remove(0);
            }
            closeLine();
            for ( int i=0;i<lista.size();i++ ){
                System.out.println(lista.get(i));
            }
            lista=null;
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
           
    public void cut(String [] args){
        String [] partes=args[1].substring(2).split(",");
        int [] elem=new int[partes.length*2];
        int count=0;
        try{
            for ( int i=0;i<partes.length;i++ )
            {
                if ( 
                    partes[i].startsWith("-") 
                    && ! partes[i].endsWith("-") 
                    && ! partes[i].substring(1).contains("-")
                    && Integer.parseInt(partes[i].substring(1)) >= 1
                )
                {
                    elem[count++]=-1;
                    elem[count++]=Integer.parseInt(partes[i].substring(1));
                    continue;
                }
                if ( 
                    ! partes[i].startsWith("-") 
                    && partes[i].endsWith("-") 
                    && ! partes[i].substring(0,partes[i].length()-2).contains("-")
                    && Integer.parseInt(partes[i].substring(0,partes[i].length()-1)) >= 1
                )
                {
                    elem[count++]=Integer.parseInt(partes[i].substring(0,partes[i].length()-1));
                    elem[count++]=-1;
                    continue;
                }
                if ( 
                    ! partes[i].startsWith("-") 
                    && ! partes[i].endsWith("-") 
                    && partes[i].split("-").length == 2 
                    && Integer.parseInt(partes[i].split("-")[0]) >= 1 
                    && Integer.parseInt(partes[i].split("-")[1]) >= 1 
                    && Integer.parseInt(partes[i].split("-")[0]) <= Integer.parseInt(partes[i].split("-")[1])
                )
                {
                    elem[count++]=Integer.parseInt(partes[i].split("-")[0]);
                    elem[count++]=Integer.parseInt(partes[i].split("-")[0]);
                    continue;
                }
                if ( 
                    ! partes[i].contains("-") 
                    && Integer.parseInt(partes[i]) >= 1 
                )
                {
                    elem[count++]=Integer.parseInt(partes[i]);
                    elem[count++]=Integer.parseInt(partes[i]);
                    continue;
                }
                comando_invalido(args);
                return;
            }
        }catch(Exception e){
            comando_invalido(args);
            return;
        }
        
        try {
            String line=null;
            while ( (line=readLine()) != null ) {
                for ( int i=0;i<elem.length;i+=2 ){
                    if ( elem[i] == -1 ){
                        if ( line.length() < elem[i+1] )
                            System.out.print(line);
                        else
                            System.out.print(line.substring(0,elem[i+1]));
                        continue;
                    }
                    if ( elem[i+1] == -1 ){
                        if ( line.length() < elem[i] )
                            System.out.print("");
                        else
                            System.out.print(line.substring(elem[i]-1));
                        continue;
                    }
                    if ( line.length() < elem[i] )
                        System.out.print("");
                    else
                        if ( line.length() < elem[i+1] )
                            System.out.print(line.substring(elem[i]-1));
                        else
                            System.out.print(line.substring(elem[i]-1,elem[i+1]));
                    continue;
                }
                System.out.println("");                
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public boolean curl(String [] args){        
        boolean parms_curl_its_ok=true;

        String host = "";
        try{            
            String path="/";
            String method="GET";
            String header="";
            String protocol="HTTP";
            boolean verbose=false;
            boolean raw=false;
            int len=0;
            int port = 80;            

            
            for ( int i=1;i<args.length;i++ ){
                if ( args[i].equals("-H") && i+1 < args.length ){
                    i++;
                    header+=args[i]+"\r\n";
                    continue;
                }
                if ( args[i].equals("-X") && i+1 < args.length ){                    
                    i++;
                    if ( !args[i].toUpperCase().equals("POST") && !args[i].toUpperCase().equals("GET") )
                        return false; // parm not ok
                    method=args[i].toUpperCase();
                    continue;
                }
                if ( args[i].equals("-v") ){
                    verbose=true;
                    continue;
                }
                if ( args[i].equals("--raw") ){
                    raw=true;
                    continue;
                }
                if ( host.equals("") ){
                    host=args[i];
                    continue;
                }
                return false; // parm not ok                
            }
            header+="\r\n";            
            
            if ( host.toUpperCase().startsWith("HTTP://") ){
                host=host.substring(7);
                port = 80;
                protocol="HTTP";
            }
            if ( host.toUpperCase().startsWith("HTTPS://") ){
                host=host.substring(8);
                port = 443;
                protocol="HTTPS";
            }
            int p=host.indexOf(":");
            while(host.indexOf(":",p+1) > -1)
                p=host.indexOf(":",p+1);
            if ( p > -1 ){
                path=host.substring(p+1);
                host=host.substring(0,p);            
                String port_aux="";
                String numbers="0123456789";
                while(path.length()>0&&numbers.contains(path.substring(0,1))){
                    port_aux+=path.substring(0,1);
                    path=path.substring(1);
                }
                port=Integer.parseInt(port_aux);
            }else{
                p=host.indexOf("/");
                if ( p > -1 ){
                    path=host.substring(p);
                    host=host.substring(0,p);                            
                }    
            }
            
            Socket socket=null;
            if ( protocol.equals("HTTP") ){
                socket=new Socket(host, port);
            }else{
                javax.net.ssl.SSLSocketFactory sf = (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault();
                socket = sf.createSocket(host, port);                
            }

            byte[] buffer = new byte[2048];
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream(); 
            StringBuilder sb = new StringBuilder();
            String http_version="HTTP/1.1";
            boolean chunked=false;
            
            // not implemented            
            //if ( protocol.equals("HTTPS"))
            //    http_version="HTTP/2";
            http_version="HTTP/1.0";
            //http_version="HTTP/1.1"; not implemented - problem with "Transfer-Encoding: chunked"
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if ( method.equals("POST") ){
                InputStream inputStream_pipe=System.in;                
                while( (len=inputStream_pipe.read(buffer,0,buffer.length)) > 0 )
                    baos.write(buffer, 0, len);
            }
            
            String init_msg=method + " " + path + " " + http_version + "\r\n";
            String pre_header="";
            if ( !(init_msg+pre_header+header).contains("\r\nHost: ") )
                pre_header+="Host: " + host + "\r\n";
            if ( !(init_msg+pre_header+header).contains("\r\nuser-agent: ") )
                pre_header+="user-agent: curl/7.87.0\r\n";
            if ( !(init_msg+pre_header+header).contains("\r\naccept: ") )
                pre_header+="accept: */*\r\n";
            if ( method.equals("POST") && !(init_msg+pre_header+header).contains("\r\nContent-Type: ") )
                pre_header+="Content-Type: application/x-www-form-urlencoded\r\n";
            if ( method.equals("POST") && !(init_msg+pre_header+header).contains("\r\nContent-Length: ") )
                pre_header+="Content-Length: " + baos.toByteArray().length + "\r\n";
            
            sb.append(init_msg);
            sb.append(pre_header);
            sb.append(header);            
            if ( verbose ){
                System.out.println("* Connected " + socket.getInetAddress().toString().replace("/", " - ") + " port " + port);
                System.out.print(init_msg);
                System.out.print(pre_header);
                System.out.print(header);
                System.out.println(baos.toString());
            }
            os.write(sb.toString().getBytes());                        
            os.write(baos.toByteArray());            
            os.flush();
            
            try{
                boolean heading=true;
                String header_response="";
                byte[] ending_head = new byte[4]; // \r\n\r\n 13 10 13 10
                while( is.available() >= 0 && (len=is.read(buffer)) > -1 ){
                    if ( heading ){
                        for ( int i=0;i<len;i++ ){
                            if ( verbose ){
                                System.out.write(buffer, i, 1);                        
                                System.out.flush();
                            }
                            header_response+=(char)buffer[i];

                            ending_head[0] = ending_head[1];
                            ending_head[1] = ending_head[2];
                            ending_head[2] = ending_head[3];
                            ending_head[3] = buffer[i];
                            if ( ending_head[0] == 13 && ending_head[1] == 10 && ending_head[2] == 13 && ending_head[3] == 10 ){                                
                                heading=false;
                                i++;
                                if ( !raw && header_response.contains("\r\nTransfer-Encoding: chunked"))
                                    chunked=true;
                                if ( i < len ){
                                    if ( chunked ){
                                        if ( curl_chunk_write(buffer, i, len-i) ){
                                            System.exit(0);
                                        }
                                    }else{
                                        System.out.write(buffer, i, len-i); 
                                    }                                    
                                    break;
                                }
                            }
                        }
                    }else{
                        if ( chunked ){
                            if ( curl_chunk_write(buffer, 0, len) ){
                                System.exit(0);
                            }
                        }else{
                            System.out.write(buffer, 0, len);
                        }
                    }
                }
                System.out.flush();
            }catch(Exception e){
                System.out.println("\nError "+e.toString());
            }

        }catch(UnknownHostException e){
            System.err.println("Error UnknownHost: " + host);
        }catch(Exception e){
            System.err.println("Error: " + e.toString());
        }
        return parms_curl_its_ok;
    }
    
    boolean flip=true; // true => head chunked | false => data chunked
    int len_data_chunked=-1;
    String txt_head_chunked="";
    public boolean curl_chunk_write(byte buffer[], int off, int len) {
        while(off < len){
            if ( flip ){
                if(buffer[off] == 13){
                    off++;
                    continue;
                }
                if(buffer[off] == 10){
                    off++;
                    len_data_chunked=hex_string_to_int(txt_head_chunked);
                    if ( len_data_chunked == 0 ){
                        return true; // finish
                    }
                    txt_head_chunked="";
                    flip=false;                    
                    continue;
                }
                txt_head_chunked+=((char)buffer[off++]+"").toUpperCase();
                continue;
            }else{
                if ( len_data_chunked >= len-off ){
                    System.out.write(buffer, off, len-off);  
                    System.out.flush();
                    len_data_chunked-=len-off;
                    if ( len_data_chunked == 0 ){
                        flip=true;
                    }
                    off=len;
                    continue;
                }else{
                    System.out.write(buffer, off, len_data_chunked);  
                    System.out.flush();
                    off+=len_data_chunked;
                    len_data_chunked=0;
                    flip=true;
                    continue;
                }
            }
        }        
        return false; // finish
        
        /*
        //https://datatracker.ietf.org/doc/html/rfc9112#field.transfer-encoding
        /////////////////
        Transfer-Encoding: chunked
        3 chunks of length 4, 6 and 14 (hexadecimal "E" or "e"):
            4\r\n        (bytes to send)
            Wiki\r\n     (data)
            6\r\n        (bytes to send)
            pedia \r\n   (data)
            E\r\n        (bytes to send)
            in \r\n
            \r\n
            chunks.\r\n  (data)
            0\r\n        (final byte - 0)
            \r\n         (end message)                                    
        */
    }
    
    public int hex_string_to_int(String a){
System.out.println("AA" + a);
        int retorno=0;
        int lvl=1;
        while(a.length()>0){
            int len=a.length();            
            int p=Util.hex_string.indexOf(a.substring(len-1,len));
            if ( p == -1 )
                Util.erroFatal(200);
            retorno+=p*lvl;
            a=a.substring(0, len-1);
            lvl*=16;
        }        
System.out.println("BB" + retorno);        
        return retorno+2;
    }
    
    public void sedBasic(String [] args)
    {
        String line;
        while ( (line=readLine()) != null ) {
            for ( int i=1;i<args.length;i+=2 ){
                if ( args[i].length() == 0 ){
                    System.err.println("Erro nos parametros!!");
                    System.exit(1);
                }
                line=line.replace(args[i],args[i+1]);
            }
            System.out.println(line);
        }
    }
    
    ArrayList<Byte> sed_agulha1=new ArrayList<Byte>();
    int sed_agulha1_count=0;
    ArrayList<Byte> sed_agulha2=new ArrayList<Byte>(); // substituir in
    int sed_agulha2_count=0;
    ArrayList<Byte> sed_agulha3=new ArrayList<Byte>(); // substituir out
    int sed_agulha3_count=0;
    public void sed(String [] args)
    {     
        byte [] in_=args[1].getBytes();
        for ( int i=0;i<in_.length;i++ )
            sed_agulha2.add(in_[i]);   
        sed_agulha2=codificacaoBarra(sed_agulha2);
        sed_agulha2_count=sed_agulha2.size();
        
        byte [] out_=args[2].getBytes();
        for ( int i=0;i<out_.length;i++ )
            sed_agulha3.add(out_[i]);        
        sed_agulha3=codificacaoBarra(sed_agulha3);
        sed_agulha3_count=sed_agulha3.size();
        
        byte[] entrada_ = new byte[1];
        while ( read1Byte(entrada_) ){
            // insere lido depois trata!!            
            sed_agulha1.add(entrada_[0]);
            sed_agulha1_count++;
            
            if ( sed_agulha1_count < sed_agulha2_count )
                continue;
            
            if ( sed_agulha1_count > sed_agulha2_count ){
                write1Byte(sed_agulha1.get(0));
                sed_agulha1.remove(0);
                sed_agulha1_count--;
            }
            
            if ( sed_agulhas_iguais() ){
                for ( int i=0;i<sed_agulha3_count;i++ )
                    write1Byte(sed_agulha3.get(i));
                sed_agulha1=new ArrayList<Byte>();
                sed_agulha1_count=0;
            }
            write1ByteFlush();// 0 cache
        }
        while( sed_agulha1_count>0 ){
            write1Byte(sed_agulha1.get(0));
            sed_agulha1.remove(0);
            sed_agulha1_count--;            
            write1ByteFlush();// 0 cache
        }
        write1ByteFlush();
    }
    
    public ArrayList<Byte> codificacaoBarra(ArrayList<Byte> a)
    {
        // transforma 97(A)   92(\)   110(n)   97(A)
        // em         97(A)   10(\n)   97(A)
        
        //    BARRA_R=13;     // \r
        //    CHAR_R=114;     // r
        //    BARRA_N=10;     // \n
        //    CHAR_N=110;     // n
        //    
        //    BARRA_0=0;      // \0
        //    CHAR_0=48;      // 0
        //    BARRA_A=7;      // \a
        //    CHAR_A=97;      // a
        //    BARRA_B=8;      // \b
        //    CHAR_B=98;      // b
        //    BARRA_T=9;      // \t
        //    CHAR_T=116;     // t
        //    BARRA_V=11;     // \v
        //    CHAR_V=118;     // v
        //    BARRA_F=12;     // \f
        //    CHAR_F=102;     // f
        //    CHAR_BARRA=92; // \\ => \
        
        ArrayList<Byte> lista=new ArrayList<Byte>();        
        int tail=-1;
        int entrada=-1;
        
        for ( int i=0;i<a.size();i++ ){
            entrada=byte_to_int_java(a.get(i),false);
            if ( tail == -1 ){
                tail=entrada;
                continue;
            }            
            if ( tail == CHAR_BARRA && entrada == CHAR_R ){ lista.add( Byte.valueOf( (byte)BARRA_R ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_N ){ lista.add( Byte.valueOf( (byte)BARRA_N ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_0 ){ lista.add( Byte.valueOf( (byte)BARRA_0 ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_A ){ lista.add( Byte.valueOf( (byte)BARRA_A ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_B ){ lista.add( Byte.valueOf( (byte)BARRA_B ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_T ){ lista.add( Byte.valueOf( (byte)BARRA_T ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_V ){ lista.add( Byte.valueOf( (byte)BARRA_V ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_F ){ lista.add( Byte.valueOf( (byte)BARRA_F ) ); tail=-1; continue; }
            if ( tail == CHAR_BARRA && entrada == CHAR_BARRA ){ lista.add( Byte.valueOf( (byte)CHAR_BARRA ) ); tail=-1; continue; }
            
            lista.add( Byte.valueOf( (byte)tail ) );
            tail=entrada;
        }
        if ( tail != -1 ){
            // condição comentada por nao ser possivel atingir
            // nao remover o comentario!!
            //if ( tail == CHAR_BARRA ){
            //    System.err.println("Erro, parametro inválido: "+a.toString());
            //    System.exit(1);
            //}
            lista.add( Byte.valueOf( (byte)tail ) );
        }
        
        return lista;
    }
    
    public boolean sed_agulhas_iguais(){
        if ( sed_agulha1_count != sed_agulha2_count ){
            System.err.println("Erro inesperado!");
            System.exit(1);
        }
        for(int i=0;i<sed_agulha1_count;i++)
            if ( (int)sed_agulha1.get(i) != (int)sed_agulha2.get(i) )
                return false;
        return true;
    }
    
    public void n() // \n
    {
        // modifica arquivo \r\n para \n(se ja tiver \n nao tem problema)
        try {
            boolean tail_use=false;            
            byte tail=0;
            byte[] entrada_ = new byte[1];
            byte entrada=0;
            while ( read1Byte(entrada_) ){
                entrada=entrada_[0];
                if ( ! tail_use ){
                    tail_use=true;
                    tail=entrada;
                    continue;
                }
                if ( entrada == BARRA_N && tail == BARRA_R ){
                    tail=entrada;
                    continue;
                }
                write1Byte(tail);
                tail=entrada;
            }
            if ( tail_use )
                write1Byte(tail);
            write1ByteFlush();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void rn() // \n\r
    {
        // modifica arquivo \n para \r\n(se ja tiver \r\n nao tem problema)
        try {
            boolean tail_use=false;            
            byte tail=0;
            byte[] entrada_ = new byte[1];
            byte entrada=0;
            while ( read1Byte(entrada_) ){
                entrada=entrada_[0];
                if ( ! tail_use ){
                    tail_use=true;
                    tail=entrada;
                    continue;
                }
                if ( entrada == BARRA_N && tail == BARRA_R ){
                    write1Byte(tail);
                    tail=entrada;
                    continue;
                }
                if ( entrada == BARRA_N && tail != BARRA_R ){
                    write1Byte(tail);
                    write1Byte(BARRA_R);
                    tail=entrada;
                    continue;
                }                
                write1Byte(tail);
                tail=entrada;
            }
            if ( tail_use )
                write1Byte(tail);
            write1ByteFlush();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
        
    public Boolean checkWindows(){
        return new File("c:/").exists();
    }
    
    public void bytesToInts(boolean dif_128)
    {      
        try {
            byte[] entrada_ = new byte[1];
            while ( read1Byte(entrada_) ){
                System.out.println( byte_to_int_java(entrada_[0],dif_128) );
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }        
    }
        
    public void intsToBytes(String [] args)
    {
        String line=null;
        int valor=0;        
        if ( args.length == 1 ){ // stdin
            while ( (line=readLine()) != null ) {
                try{
                    valor=Integer.parseInt(line);
                }catch(Exception ex){
                    System.out.println("\nErro, valor invalido: "+line);
                    System.exit(1);
                }
                write1Byte(valor);            
                write1ByteFlush();//0 cache
            } 
            closeLine();
        }else{ // parametros
            for ( int i=1;i<args.length;i++ ){
                try{
                    valor=Integer.parseInt(args[i]);
                }catch(Exception ex){
                    System.out.println("\nErro, valor invalido: "+line);
                    System.exit(valor);
                }
                write1Byte(valor);            
                write1ByteFlush();//0 cache
            }                    
        }
        write1ByteFlush();
    }

    boolean od_b=false;
    boolean od_c=false;
    boolean od_r=false;
    String parm_od="";
    public void init_od(String parm)
    {
        parm_od=parm;
        od_b=parm.contains("b");
        od_c=parm.contains("c");
        od_r=parm.contains("r");
        if ( ! od_b && ! od_c && ! od_r ){
            od_r=true;
            parm_od="r";
        }
    }
        
    StringBuilder sb_b=new StringBuilder();
    StringBuilder sb_c=new StringBuilder();
    StringBuilder sb_r=new StringBuilder();
    int count_all_od=0;
    int count_16_od=0;
    public void write1od(int i)
    {
        if ( od_b )
            sb_b.append(OD_BC_B[i]);
        if ( od_c )
            sb_c.append(OD_BC_C[i]);
        
        sb_r.append(OD_BC_R[i]);
        
        count_16_od++;
        count_all_od++;
        if ( count_16_od >= 16){            
            writeOd();
            sb_b=new StringBuilder();
            sb_c=new StringBuilder();
            sb_r=new StringBuilder();      
            count_16_od=0;
        }
    }
        
    String tail_od="";
    boolean isPrintedRepeat=false;
    public void writeOd()
    {
        String compare=sb_r.toString();        
        // trata repetição
        if ( compare.equals(tail_od) ){
            if ( ! isPrintedRepeat ){
                isPrintedRepeat=true;
                System.out.println("*");                
            }
            return;
        }else{
            tail_od=compare;
            isPrintedRepeat=false;
        }
        
        for ( int i=0;i<parm_od.length();i++ ){
            writeCarroOd(i==0);
            if ( parm_od.substring(i,i+1).equals("b") ){
                System.out.println(sb_b.toString());
                continue;
            }
            if ( parm_od.substring(i,i+1).equals("c") ){
                System.out.println(sb_c.toString());
                continue;
            }
            if ( parm_od.substring(i,i+1).equals("r") ){
                System.out.println(sb_r.toString());
                continue;
            }
            System.out.println("Erro, parametro nao tratado: "+parm_od);
            System.exit(1);
        }
    }
    
    public void writeCarroOd(boolean isPrint)
    {
        if ( isPrint ){            
            System.out.print( lpad( Integer.toOctalString(count_all_od-count_16_od).trim() ,7,"0") );        
        }else{
            System.out.print("        ");
        }
    }
    
    public void writeodFlush()
    {
        if ( count_16_od > 0 )
            writeOd();
        count_16_od=0;
        writeCarroOd(true);
        System.out.println();
    }
    
    public void od(String parm)
    {
        init_od(parm);
        try {
            int i=0;
            byte[] entrada_ = new byte[1];
            while ( read1Byte(entrada_) ){
                i=byte_to_int_java(entrada_[0],false);
                write1od(i);                
            }
            writeodFlush();
        }catch(Exception e){
            System.out.println(e.toString());
        }        
    }
    
    public void touch(String [] args){
        long dif_segundos=0;
        long current_milisegundos=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");  
        Date dataCurrent=null;
        try {
            if ( args.length == 2 ){
                touch(new File(args[1]),current_milisegundos,0);
                return;
            }
            if ( args.length == 3 ){
                if ( args[2].length() == 14 ){ //data 20210128235959
                    dataCurrent=format.parse(args[2]);  
                    current_milisegundos=dataCurrent.getTime();
                }else{
                    dif_segundos=Long.parseLong(args[2]); // 3600
                }
                touch(new File(args[1]),current_milisegundos,dif_segundos);                                    
                return;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.exit(1);
        }
        return;
    }
    
    public void rm(String [] args){
        boolean recursiveMode = false;
        for ( int i=1;i<args.length;i++ ){
            try{                
                if(args[i].equals("-R")){
                    recursiveMode=true;
                    continue;
                }
                if(args[i].equals(".") || args[i].equals("..")){
                    System.out.println("Error, diretorios \".\" e \"..\" nao sao permitidos.");
                    errorRmPrinted = true;
                    continue;
                }
                File f=new File(args[i]);
                if ( f.exists() == false ){
                    System.out.println("Error, " + args[i] + " nao encontrado.");
                    errorRmPrinted = true;
                }else
                    rm(new File(args[i]), recursiveMode);
                
            }catch(Exception e){
                System.out.println(e.toString());
                errorRmPrinted = true;
            }        
        }
        if ( errorRmPrinted )
            System.exit(1);
    }

    boolean errorRmPrinted = false;
    boolean errorRRmPrinted = false;
    public void rm(File f, boolean recursiveMode){
        try{
            if( f.isDirectory() ){
                if ( recursiveMode ){
                    File [] files=f.listFiles();
                    for( int i=0;i<files.length;i++ ){
                        rm(files[i], recursiveMode);
                    }
                    f.delete();
                }else{
                    if ( errorRRmPrinted == false ){
                        errorRRmPrinted = true;
                        errorRmPrinted = true;
                        System.out.println("Error, use -R para pasta");
                    }
                }
            }else{
                f.delete();
            }
        }catch(Exception e){
            System.out.println(e.toString());
            errorRmPrinted = true;
        }        
    }
    
    boolean errorCpPrinted = false;
    public void cp(File f1, File f2, boolean recursiveMode, boolean isFirst){
        if ( errorCpPrinted )
            return;
        try{
            if ( f1.isDirectory() && !recursiveMode ){
                System.out.println("Error, use -R para copia de pasta");
                errorCpPrinted = true;
                return;
            }
            if ( f1.getAbsolutePath().toUpperCase().equals(f2.getAbsolutePath().toUpperCase()) ){
                System.out.println("Error, origem igual ao destino");
                errorCpPrinted = true;
                return;
            }        
            if ( f1.isDirectory() && f2.exists() && !f2.isDirectory() ){
                System.out.println("Error, incompatibilidade.. [" + f1.getAbsolutePath() + "] é um diretório e [" + f2.getAbsolutePath() + "] não é um diretório.");
                errorCpPrinted = true;
                return;
            }
            if ( !f1.isDirectory() && f2.exists() && f2.isDirectory() ){
                System.out.println("Error, incompatibilidade.. [" + f1.getAbsolutePath() + "] não é um diretório e [" + f2.getAbsolutePath() + "] é um diretório.");
                errorCpPrinted = true;
                return;
            }
            if ( isFirst ){
                if( f1.isDirectory() ){
                    // validation recursivo invalido
                    String sep=f1.getAbsolutePath().contains("\\")?"\\":"/";
                    String p2=f2.getAbsolutePath().toUpperCase();
                    String p1=f1.getAbsolutePath().toUpperCase();
                    if(!p2.endsWith(sep))
                        p2+=sep;
                    if(!p1.endsWith(sep))
                        p1+=sep;
                    if ( p2.indexOf(p1) == 0 ){
                        System.out.println("Error, caminho recursivo invalido(infinito)");
                        errorCpPrinted = true;
                        return;
                    }
                    // fim validation recursivo invalido
                    if( f2.exists() ){
                        File f_=new File(f2.getAbsolutePath()+"/"+f1.getName());
                        f_.mkdir();
                        cp(f1, f_, recursiveMode, false);
                        return;
                    }
                    f2.mkdir();
                    cp(f1, f2, recursiveMode, false);
                    return;                    
                }else{
                    if ( f2.exists() && f2.isDirectory() ){
                        System.out.println("Error, incompatibilidade.. [" + f1.getAbsolutePath() + "] é um arquivo e [" + f2.getAbsolutePath() + "] é um diretório.");
                        errorCpPrinted = true;
                        return;
                    }
                    Files.copy(f1.toPath(), f2.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }  
            if ( f1.isDirectory() ){                
                File [] files=f1.listFiles();
                for( int i=0;i<files.length;i++ ){
                    File f_=new File(f2.getAbsolutePath()+"/"+files[i].getName());
                    if(files[i].isDirectory()){
                        if(!f_.exists())
                            f_.mkdir();
                        cp(files[i], f_, recursiveMode, false);
                    }else{
                        if ( f_.exists() && f_.isDirectory() ){
                            System.out.println("Error, incompatibilidade.. [" + files[i].getAbsolutePath() + "] é um arquivo e [" + f_.getAbsolutePath() + "] é um diretório.");
                            errorCpPrinted = true;
                            return;
                        }
                        Files.copy(files[i].toPath(), f_.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }                
            }else{
                if ( f2.exists() && f2.isDirectory() ){
                    System.out.println("Error, incompatibilidade.. [" + f1.getAbsolutePath() + "] é um arquivo e [" + f2.getAbsolutePath() + "] é um diretório.");
                    errorCpPrinted = true;
                    return;
                }
                Files.copy(f1.toPath(), f2.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }                
        }catch(Exception e){
            System.out.println(e.toString());
            errorCpPrinted = true;
        }        
    }
    
    public void mkdir(File f){
        if(f.exists()){
            System.out.println("pasta "+f.getAbsolutePath() + " ja existe");
            System.exit(1);
        }
        try{
            f.mkdir();
        }catch(Exception e){
            System.out.println(e.toString());
        }    
    }
            
    public void touch(File file, long current_milisegundos, long dif_segundos) throws Exception{        
        if (!file.exists())
           new FileOutputStream(file).close();
        file.setLastModified(current_milisegundos + (dif_segundos*1000) );
    }
    
    private boolean isSuportIconv(String a) {
        for ( int i=0;i<suportIconv.length;i++ )
            if ( a.equals(suportIconv[i]))
                return true;
        return false;
    }
    
    private void iconvUTF8ToWindows(String caminho) {
        // existem sequencias irreversiveis que nao suprimidar na conversao padrao mas aqui nao, ex: 226 128 147
        try {
            boolean tail_use=false;            
            int tail=0;
            byte[] entrada_ = new byte[1];
            int entrada=0;
            if ( caminho != null && ! caminho.equals("") )
                readBytes(caminho);
            while ( read1Byte(entrada_) ){
                entrada=byte_to_int_java(entrada_[0],false);
                if ( ! tail_use ){
                    tail_use=true;
                    tail=entrada;
                    continue;
                }
                if ( (tail == 194 || tail == 195) && (entrada < 128 || entrada >= 192 ) ){
                    System.out.println(erroSequenciaIlegal);
                    System.exit(1);
                }
                if ( tail == 194 ){
                    write1Byte(entrada);
                    tail_use=false;
                    continue;
                }
                if ( tail == 195 ){
                    write1Byte(entrada+64);                    
                    tail_use=false;
                    continue;
                }
                write1Byte(tail);
                tail=entrada;
            }
            if ( tail_use )
                write1Byte(tail);
            write1ByteFlush();
            closeBytes();
        }catch(Exception e){
            System.out.println(e.toString());
            System.exit(1);
        }
    }
    
    private void iconvWindowsToUTF8(String caminho) {
        try {
            byte[] entrada_ = new byte[1];
            int entrada=0;
            if ( caminho != null && ! caminho.equals("") )
                readBytes(caminho);
            while ( read1Byte(entrada_) ){
                entrada=byte_to_int_java(entrada_[0],false);
                if ( entrada >= 128 && entrada < 192 ){
                    write1Byte(194);
                    write1Byte(entrada);
                    continue;
                }
                if ( entrada >= 192 ){
                    write1Byte(195);
                    write1Byte(entrada-64);
                    continue;
                }
                write1Byte(entrada);                
            }
            write1ByteFlush();
            closeBytes();
        }catch(Exception e){
            System.out.println(e.toString());
            System.exit(1);
        }
    }
    
    /*
    esteiras(normalização em "ISO-8859-1"):
    
      11 -> remove BOM UTF-8
      12 -> remove BOM UCS-2LE
    
      21 -> normalizando ISO-8859-1 vindo de UTF-8
      22 -> normalizando ISO-8859-1 vindo de UCS-2LE
    
      31 -> codificando para UTF-8
      32 -> codificando para UCS-2LE
    
      41 -> colocando BOM UTF-8
      42 -> colocando BOM UCS-2LE
    
      50 -> finalizando
    */    
    ArrayList<Integer> esteiras=new ArrayList<Integer>();
    private void iconv(String tipoOrigem, String tipoOrigemPuro, String tipoDestino, String tipoDestinoPuro, String caminho) throws Exception {        
        
        if ( caminho != null && ! caminho.equals("") )
            readBytes(caminho);
        
        // tirando BOM
        if ( tipoOrigem.equals("UTF-8BOM"))
            esteiras.add(11);
        if ( tipoOrigem.equals("UCS-2LEBOM"))
            esteiras.add(12);
        
        // decodificando e codificando
        if ( ! tipoOrigemPuro.equals(tipoDestinoPuro) ){
            if (tipoOrigemPuro.equals("UTF-8"))
                esteiras.add(21);
            if (tipoOrigemPuro.equals("UCS-2LE"))
                esteiras.add(22);
            if (tipoDestinoPuro.equals("UTF-8") )
                esteiras.add(31);
            if (tipoDestinoPuro.equals("UCS-2LE") )
                esteiras.add(32);
        }        
        
        // colocando BOM
        if ( tipoDestino.equals("UTF-8BOM"))
            esteiras.add(41);
        if ( tipoDestino.equals("UCS-2LEBOM"))
            esteiras.add(42);
        
        // finish
        esteiras.add(50);
        
        byte[] entrada_ = new byte[1];
        int entrada=0;
        while ( read1Byte(entrada_) ){
            entrada=byte_to_int_java(entrada_[0],false);
            nextEsteira(entrada,-1);
        }
        
        nextEsteira(-1,-1);// comando para liberar os dados nas agulhas
        
        write1ByteFlush();
        closeBytes();
    }

    public void nextEsteira(int entrada,int seqEsteira){
        
        // proxima esteira
        seqEsteira++;        
        int esteira=esteiras.get(seqEsteira);

        // 11 -> remove BOM UTF-8
        if ( esteira == 11 ){
            esteiraRemoveBOM(entrada,seqEsteira,BOM_UTF_8);
            return;
        }
        // 12 -> remove BOM UCS-2LE
        if ( esteira == 12 ){
            esteiraRemoveBOM(entrada,seqEsteira,BOM_UCS_2LE);
            return;
        }
        // 21 -> decode UTF-8
        if ( esteira == 21 ){
            esteiraDecode_UTF_8(entrada,seqEsteira);
            return;
        }
        // 22 -> decode UCS-2LE
        if ( esteira == 22 ){
            esteiraDecode_UCS_2LE(entrada,seqEsteira);
            return;
        }
        // 31 -> encode UTF-8
        if ( esteira == 31 ){
            esteiraEncode_UTF_8(entrada,seqEsteira);
            return;
        }
        // 32 -> encode UCS-2LE
        if ( esteira == 32 ){
            esteiraEncode_UCS_2LE(entrada,seqEsteira);
            return;
        }
        // 41 -> add BOM UTF-8
        if ( esteira == 41 ){
            esteiraAddBOM(entrada,seqEsteira,BOM_UTF_8);
            return;
        }
        // 42 -> add BOM UCS-2LE
        if ( esteira == 42 ){
            esteiraAddBOM(entrada,seqEsteira,BOM_UCS_2LE);
            return;
        }
        // 50 -> finish
        if ( esteira == 50 ){
            esteiraFinish(entrada,seqEsteira);
            return;
        }        
    }
    
    int esteiraRemoveBOM_count=0;
    int esteiraRemoveBOM_max=-1;
    public void esteiraRemoveBOM(int entrada,int seqEsteira,int [] seqBOM){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            if ( esteiraRemoveBOM_count < esteiraRemoveBOM_max ){
                System.out.println(erroSequenciaIlegal);
                System.exit(1);                
            }
            nextEsteira(entrada, seqEsteira); // liberando o que sobrou na agulha(tail) para os proximos
            return;
        }        
        if ( esteiraRemoveBOM_max == -1 )
            esteiraRemoveBOM_max=seqBOM.length;
        if ( esteiraRemoveBOM_count < esteiraRemoveBOM_max ){
            if ( entrada != seqBOM[esteiraRemoveBOM_count]){
                System.out.println(erroSequenciaIlegal);
                System.exit(1);                
            }
            esteiraRemoveBOM_count++;
            return;
        }
        nextEsteira(entrada, seqEsteira);
    }
    
    boolean decode_UTF_8_tail_use=false;
    int decode_UTF_8_tail=-1;
    public void esteiraDecode_UTF_8(int entrada,int seqEsteira){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            if ( decode_UTF_8_tail_use ){
                nextEsteira(decode_UTF_8_tail, seqEsteira);
                decode_UTF_8_tail_use=false;
            }
            nextEsteira(entrada, seqEsteira); // liberando o que sobrou na agulha(tail) para os proximos
            return;
        }
        if ( ! decode_UTF_8_tail_use ){
            decode_UTF_8_tail_use=true;
            decode_UTF_8_tail=entrada;
            return;
        }
        if ( (decode_UTF_8_tail == 194 || decode_UTF_8_tail == 195) && (entrada < 128 || entrada >= 192 ) ){
            System.out.println(erroSequenciaIlegal);
            System.exit(1);
        }
        if ( decode_UTF_8_tail == 194 ){
            nextEsteira(entrada, seqEsteira);
            decode_UTF_8_tail_use=false;
            return;
        }
        if ( decode_UTF_8_tail == 195 ){
            nextEsteira(entrada+64, seqEsteira);
            decode_UTF_8_tail_use=false;
            return;
        }
        nextEsteira(decode_UTF_8_tail, seqEsteira);
        decode_UTF_8_tail=entrada;
    }
    
    boolean decode_UCS_2LE_entrada_par=true;
    public void esteiraDecode_UCS_2LE(int entrada,int seqEsteira){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            if ( ! decode_UCS_2LE_entrada_par ){
                System.out.println(erroSequenciaIlegal);
                System.exit(1);
            }
            nextEsteira(entrada, seqEsteira); // liberando o que sobrou na agulha(tail) para os proximos
            return;
        }        
        decode_UCS_2LE_entrada_par=!decode_UCS_2LE_entrada_par;
        if ( decode_UCS_2LE_entrada_par ){
            if ( entrada == 0 )
                return;
            System.out.println(erroSequenciaIlegal);
            System.exit(1);
        }
        nextEsteira(entrada, seqEsteira);
    }
            
    public void esteiraEncode_UTF_8(int entrada,int seqEsteira){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            nextEsteira(entrada, seqEsteira); // liberando o que sobrou na agulha(tail) para os proximos
            return;
        }        
        if ( entrada >= 128 && entrada < 192 ){
            nextEsteira(194, seqEsteira);
            nextEsteira(entrada, seqEsteira);
            return;
        }
        if ( entrada >= 192 ){
            nextEsteira(195, seqEsteira);
            nextEsteira(entrada-64, seqEsteira);
            return;
        }
        nextEsteira(entrada, seqEsteira);
    }
    
    public void esteiraEncode_UCS_2LE(int entrada,int seqEsteira){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            nextEsteira(entrada, seqEsteira); // liberando o que sobrou na agulha(tail) para os proximos
            return;
        }        
        nextEsteira(entrada, seqEsteira);
        nextEsteira(0, seqEsteira);
    }
    
    boolean esteiraAddBOM_isAdded=false;
    public void esteiraAddBOM(int entrada,int seqEsteira,int [] seqBOM){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            nextEsteira(entrada, seqEsteira); // liberando o que sobrou na agulha(tail) para os proximos
            return;
        }        
        if ( ! esteiraAddBOM_isAdded ){
            for(int i=0;i<seqBOM.length;i++)
                nextEsteira(seqBOM[i], seqEsteira);
            esteiraAddBOM_isAdded=true;
        }
        nextEsteira(entrada, seqEsteira);
    }
    
    public void esteiraFinish(int entrada,int seqEsteira){
        if ( entrada == -1 ){ // liberando o que sobrou na agulha(tail)
            return;
        }        
        write1Byte(entrada);
    }
    
    public void tee(String caminho)
    {
        try{
            FileOutputStream out=new FileOutputStream(caminho);                        
            int len;
            byte[] buf = new byte[BUFFER_SIZE];
            while( (len=readBytes(buf)) > -1){
                out.write(buf, 0, len);
                out.flush();
                System.out.write(buf, 0, len);
                System.out.flush();
            }
            out.flush();            
            out.close();
            System.out.flush();
            closeBytes();
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
        }
    }
    
    public void uniq_quebra(boolean quebra){
        String tail=null;
        String line=null;
        try {
            long count=0;
            while ( (line=readLine()) != null ){
                if ( tail == null ){
                    count++;
                    tail=line;
                    continue;
                }
                if ( !line.equals(tail) ){
                    if ( quebra ){
                        System.out.print(count);
                        System.out.print(" ");
                    }
                    System.out.println(tail);
                    count=0;
                }
                tail=line;
                count++;
            }
            if ( tail != null ){
                if ( quebra ){
                    System.out.print(count);
                    System.out.print(" ");
                }
                System.out.println(tail);
            }
            closeLine();
        }catch(Exception e){
            System.err.println(e.toString());
            System.exit(1);
        }
    }
    
    public void seqDate(String a, String b) throws Exception{
        String base1="yyyy-MM-dd";
        String base2="dd/MM/yyyy";
        String base=a.split("-").length>1?base1:base2;
        
        java.util.Date d1 = new java.text.SimpleDateFormat(base, java.util.Locale.ENGLISH).parse(a);       
        java.util.Date d2 = new java.text.SimpleDateFormat(base, java.util.Locale.ENGLISH).parse(b);       
        String s1 = new java.text.SimpleDateFormat(base).format(d1);
        String s2 = new java.text.SimpleDateFormat(base).format(d2);
        java.util.Calendar c = java.util.Calendar.getInstance(); 
        
        int v = s1.compareTo(s2);        
        if ( a.split("/").length>1 ){
            String s1_ = new java.text.SimpleDateFormat(base1).format(d1);
            String s2_ = new java.text.SimpleDateFormat(base1).format(d2);
            v = s1.compareTo(s2);
        }
        
        if ( v == 0 ){
            System.out.println(s1);
            return;
        }
        int inc=v>0?-1:1;
        while(true){
            System.out.println(s1);                
            if ( s1.equals(s2) )
                break;
            c.setTime(d1); 
            c.add(java.util.Calendar.DATE, inc);
            d1 = c.getTime();
            s1 = new java.text.SimpleDateFormat(base).format(d1);
        }
              
    }
    
    public void add(String a) throws Exception{
        String base1="yyyy-MM-dd";
        String base2="dd/MM/yyyy";
        String base=a.split("-").length>1?base1:base2;

        java.util.Date d1 = new java.text.SimpleDateFormat(base, java.util.Locale.ENGLISH).parse(a);       
        String s1 = new java.text.SimpleDateFormat(base).format(d1);
        java.util.Calendar c = java.util.Calendar.getInstance(); 

        c.setTime(d1); 
        c.add(java.util.Calendar.DATE, 1);
        d1 = c.getTime();
        s1 = new java.text.SimpleDateFormat(base).format(d1);
        System.out.println(s1);
    }
        
    public void seq(int a,int b,int len){
        int inc=1;
        if ( a > b )
            inc=-1;
        while(true){
            System.out.println( lpad(a,len,"0") );
            if ( a == b )
                break;
            a+=inc;
        }
    }
    
    public void awk_print(String [] args)
    {
        ArrayList<Integer> lista=new ArrayList<Integer>();
        int [] elem;
        String [] partes;
        
        try{
            for ( int i=2;i<args.length;i++ ){
                partes=args[i].split(",");
                for ( int j=0;j<partes.length;j++ ){
                    if ( j > 0 )
                        lista.add(500); // " "
                    lista.add(Integer.parseInt(partes[j]));
                }
            }
        }catch(Exception e){
            comando_invalido(args);
            return;
        }
        
        // list command print
        elem=new int[lista.size()];
        for ( int i=0;i<lista.size();i++ )
            elem[i]=lista.get(i);
        
        try {
            String line=null;
            while ( (line=readLine()) != null ) {      
                // partes de linha
                partes=line.replaceAll("\t"," ").replaceAll("\r"," ").split(" ");
                for ( int i=0;i<elem.length;i++ ){                    
                    if ( elem[i] == 0 )
                    {
                        for ( int j=0;j<partes.length;j++ ){
                            if ( j != 0 )
                                System.out.print(" ");
                            System.out.print(partes[j]);
                        }
                        continue;
                    }
                    if ( elem[i] == 500 )
                    {
                        System.out.print(" ");
                        continue;
                    }
                    int p=elem[i];     
                    if ( p > 0 ) // posição iniciada de 0
                        p-=1;
                    else
                        p+=partes.length; // correção do pedido negativo print -1 equivale a print 3 caso o total de palavras seja 4, ou seja, -1 significa o ultimo
                    
                    if ( p < 0 ) continue;
                    if ( p >= partes.length ) continue;                    
                    System.out.print(partes[p]);
                }
                System.out.println("");                
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void awk_start_end(String [] args)
    {
        String [] negativaStartEnd=getNegativaStartEnd(args);
        if ( negativaStartEnd == null )
        {
            comando_invalido(args);
            return;
        }
        String negativa=negativaStartEnd[0]; // S/N
        String start=negativaStartEnd[1]; // ".." ou null
        String end=negativaStartEnd[2]; // ".." ou null
        
        int status=0; // 0 -> fora, 1 -> dentro do range
        if ( start == null )
            status=1;

        try {
            String line=null;
            while ( (line=readLine()) != null ) {
                if ( start != null && status == 0 && line.contains(start) )
                    status=1;
                
                if ( 
                    (negativa.equals("S") && status == 0)
                    || (negativa.equals("N") && status == 1)
                )
                    System.out.println(line);
                
                if ( end != null && status == 1 && line.contains(end) )
                    status=0;   
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
            
    public void dev_null()
    {
        try{            
            InputStream inputStream_pipe=System.in;
            int BUFFER_SIZE=1024;
            byte[] buf = new byte[BUFFER_SIZE];
            while( inputStream_pipe.read(buf,0,BUFFER_SIZE) > 0 ){}
            System.out.flush();
            System.out.close();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void dev_in()
    {
        int BUFFER_SIZE=1024;
        String s="";
        for ( int i=0;i<BUFFER_SIZE/2;i++ )
            s+="0\n";
        byte[] buf = s.getBytes();
        while(true)
            System.out.write(buf, 0, BUFFER_SIZE);
    }

    public byte[] base64_B_B(byte[] txt,boolean encoding) throws Exception{ // byte in byte out
        ByteArrayInputStream bais=new ByteArrayInputStream(txt);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        base64(bais,baos,encoding);
        return baos.toByteArray();
    }
    
    public String base64_S_S(String txt,boolean encoding) throws Exception{ // String in String out
        return new String(base64_B_B(txt.getBytes(),encoding));
    }

    public String base64_B_S(byte[] txt,boolean encoding) throws Exception{ // byte in String out
        return new String(base64_B_B(txt,encoding));
    }
    
    public byte[] base64_S_B(String txt,boolean encoding) throws Exception{ // String in byte out
        return base64_B_B(txt.getBytes(),encoding);
    }
    
    public String base64(String txt,boolean encoding) throws Exception{        
        return base64_S_S(txt,encoding);
    }
    
    public void aes(String senha,boolean encoding,String md,byte[] salt){        
        try{
            if ( encoding )
                new AES().encrypt(System.in,System.out,senha,md,salt);
            else
                new AES().decrypt(System.in,System.out,senha,md);
        }catch(Exception e){
            System.err.println(e.toString());
            System.err.println(erroSequenciaIlegal);
            System.exit(1);
        }
    }
    
    private static String bytesToHex(byte[] a){
        StringBuilder sb = new StringBuilder();
        for (byte b : a) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    private static byte[] hexTobytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public void base64(InputStream pipe_in,OutputStream pipe_out,boolean encoding) throws Exception{        
        // ex: base64(System.in,System.out,true);
        if ( encoding )
            base64encode(pipe_in,pipe_out);
        else
            base64decode(pipe_in,pipe_out);                    
    }
    
    public void base64encode(InputStream pipe_in,OutputStream pipe_out) throws Exception{        
        int BUFFER_SIZE_ = 1;
        byte [] buf=new byte[BUFFER_SIZE_];
        int len=-1;
        int entrada=-1;
        int agulha=0;
        int agulha_count=0;
        int indexPadding=61; // "="
        while(true){
            while( (len=pipe_in.read(buf,0,BUFFER_SIZE_)) == 0 ){}
            if ( len == -1 ){
                if ( agulha_count == 4 ){
                    pipe_out.write( indexBase64[ agulha<<2 ] );
                    pipe_out.write( indexPadding );
                }
                if ( agulha_count == 2 ){
                    pipe_out.write( indexBase64[ agulha<<4 ] );
                    pipe_out.write( indexPadding );
                    pipe_out.write( indexPadding );
                }  
                break;
            }
            entrada=byte_to_int_java(buf[0],false);
            agulha=(agulha<<8)|entrada;
            agulha_count+=8;
            while(agulha_count>=6){
                if ( agulha_count == 6 ){
                    pipe_out.write( indexBase64[ agulha ] );
                    agulha=0;
                    agulha_count-=6;
                    continue;
                }
                if ( agulha_count == 8 ){
                    pipe_out.write( indexBase64[ (agulha & V_0b11111100)>>2 ] );
                    agulha&=V_0b00000011;
                    agulha_count-=6;
                    continue;
                }
                if ( agulha_count == 10 ){
                    pipe_out.write( indexBase64[ (agulha & V_0b1111110000)>>4 ] );
                    agulha&=V_0b0000001111;
                    agulha_count-=6;
                    continue;
                }
                if ( agulha_count == 12 ){
                    pipe_out.write( indexBase64[ (agulha & V_0b111111000000)>>6 ] );
                    agulha&=V_0b000000111111;
                    agulha_count-=6;
                    continue;
                }
            }
        }    
        pipe_out.flush();
    }
    
    public void base64decode(InputStream pipe_in,OutputStream pipe_out) throws Exception{        
        int BUFFER_SIZE_ = 1;
        byte [] buf=new byte[BUFFER_SIZE_];
        int len=-1;
        int entrada=-1;
        int agulha=0;
        int agulha_count=0;        
        int padding_count=0;
        while(true){
            while( (len=pipe_in.read(buf,0,BUFFER_SIZE_)) == 0 ){}
            if ( len == -1 ){
                if ( agulha_count == 0 && padding_count == 0 && agulha == 0 ){
                    break;
                }
                if ( agulha_count == 4 && padding_count == 2 && agulha == 0 ){
                    break;
                }
                if ( agulha_count == 2 && padding_count == 1 && agulha == 0 ){
                    break;
                }
                System.err.println(erroSequenciaIlegal);
                System.exit(1);
                break;
            }
            entrada=byte_to_int_java(buf[0],false);
            // suprimindo \r\n
            if ( entrada == 10 || entrada == 13 || entrada == 32 )
                continue;
            entrada=txtBase64.indexOf((char)entrada);
            if ( entrada == -1 ){
                System.err.println(erroSequenciaIlegal);
                System.exit(1);
            }
            if ( entrada == 64 ){
                padding_count++;
                continue;
            }            
            agulha=(agulha<<6)|entrada;
            agulha_count+=6;
            while(agulha_count>=8){
                if ( agulha_count == 8 ){
                    pipe_out.write( agulha );
                    agulha=0;
                    agulha_count-=8;
                    continue;
                }
                if ( agulha_count == 10 ){
                    pipe_out.write( (agulha & V_0b1111111100)>>2 );
                    agulha&=V_0b0000000011;
                    agulha_count-=8;
                    continue;
                }
                if ( agulha_count == 12 ){
                    pipe_out.write( (agulha & V_0b111111110000)>>4 );
                    agulha&=V_0b000000001111;
                    agulha_count-=8;
                    continue;
                }
            }
        }    
        pipe_out.flush();        
    }

    public void comando_invalido(String[] args) {
        //Comando inválido
        System.err.print("Comando inválido: [y");
        for ( int i=0;i<args.length;i++ )
            System.err.print(" "+args[i]);
        System.err.println("]");
    }

    public String[] sliceParm(int n, String[] args) {
        String [] retorno=new String[args.length-n];
        for ( int i=n;i<args.length;i++ )
            retorno[i-n]=args[i];
        return retorno;
    }

    public void createjobexecute(String conn) throws Exception {
        String line;
        String SQL="";
        while ( (line=readLine()) != null )
            SQL+=line+"\n";
        closeLine();
        
        System.out.print("jobexecute "); // funciona como orientador, não tem função prática
        System.out.println( 
            base64(
                "jobexecute\n"
                + "-conn\n"
                + conn+"\n"
                + "SQL\n"
                + SQL
                ,true
            )
        );
    }

    public void createjobcarga(String connIn, String fileCSV, String connOut, String outTable, String trunc, String app) throws Exception {
        String line;
        String SQL="";
        while ( (line=readLine()) != null )
            SQL+=line+"\n";
        closeLine();
        
        System.out.print("jobcarga "+outTable+" "); // funciona como orientador, não tem função prática
        System.out.println(
            base64(
                "jobcarga\n"
                + "-connIn\n"
                + connIn+"\n"
                + "-fileCSV\n"
                + fileCSV+"\n"
                + "-connOut\n"
                + connOut+"\n"
                + "-outTable\n"
                + outTable+"\n"
                + "trunc\n"
                + trunc+"\n"
                + "SQL\n"
                + SQL
                ,true
            )
        );
    }

    public void carga(final String connIn,final String fileCSV,final String connOut,final String outTable,final String trunc){
        final String nemVouExplicar=trunc.equals("CREATETABLE")?connOut:"";
        if ( outTable.trim().equals("") )
        {
            System.err.println("Erro, outTable não preenchido!");
            return;
        }
        if ( ! trunc.equals("S") && ! trunc.equals("N") && ! trunc.equals("CREATETABLE") )
        {
            System.err.println("Erro, inesperado!");
            return;
        }
            
        try{
            final PipedInputStream pipedInputStream=new PipedInputStream();
            final PipedOutputStream pipedOutputStream=new PipedOutputStream();
            
            // construção da variavel select(o select pode ser customizado)
            // em CSV nao tem select
            String select_="";  
            if ( !connIn.equals("") ){
                String line;
                while( (line=readLine()) != null )
                    select_+=line+"\n";
                closeLine();
                
                select_=removePontoEVirgual(select_);            
            }
            final String select=select_;
            
            pipedInputStream.connect(pipedOutputStream);

            if ( trunc.equals("S") && ! execute(connOut, "truncate table "+outTable) )
                return;
            if ( trunc.equals("CREATETABLE") && !connIn.equals("") )
            {
                String tabela=getTableByParm(select);
                if ( tabela.equals("") ){
                    System.err.println("Erro, não foi possível encontrar o nome da tabela");
                    System.exit(1);
                }
                String create=getcreate(connIn,tabela,outTable);
                if ( create.contains("USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS") )
                {
                    System.err.println("Erro, não foi possível pegar o metadata a partir de "+tabela+" segue comando: "+create);
                    System.exit(1);
                }
                if ( create.equals("") ){
                    System.err.println("Erro, não foi possível pegar o metadata a partir de "+tabela);
                    System.exit(1);
                }
                if ( !connIn.equals("") )
                {
                    if ( ! execute(connOut, create) )
                        return;                    
                }else{
                    // será feito pelo nemVouExplicar
                }
            }
            
            Thread pipeWriter=new Thread(new Runnable() {
                public void run() {
                    selectInsert(connIn,fileCSV,select,pipedOutputStream,outTable,nemVouExplicar);
                }
            });
            
            Thread pipeReader=new Thread(new Runnable() {
                public void run() {
                    executeInsert(connOut, pipedInputStream);
                }
            });

            pipeWriter.start();
            pipeReader.start();
            
            pipeWriter.join();
            pipeReader.join();
            
            pipedOutputStream.flush();
            pipedOutputStream.close();            
            pipedInputStream.close();        
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
        }
    }

    public void executejob() {
        try{
            String line;
            String hash="";
            String [] partes;
            ArrayList<Thread> threads = new ArrayList<Thread>();
            String value_="";
            String [] sub_linesjob;

            while ( (line=readLine()) != null ){
                line=line.trim();
                if ( line.equals("") ) continue;
                if ( line.contains(" ") ){
                    partes=line.split(" ");


                    // jobcarga
                    if ( partes.length == 3 && partes[0].equals("jobcarga") )
                        hash=partes[2];
                    // jobexecute
                    if ( hash.equals("") && partes.length == 2 && partes[0].equals("jobexecute") )
                        hash=partes[1];

                    if ( hash.equals("") )
                    {
                        System.err.println("Erro, comando inválido:" + line);
                        return;
                    }

                    value_=base64(hash,false);

                    if ( value_ == null )
                    {
                        System.err.println("Erro, comando inválido:" + line);
                        return;
                    }

                    final ArrayList<String> instrucoes = new ArrayList<String>();
                    String SQL="";
                    sub_linesjob=value_.split("\n");

                    for ( int i=0;i<sub_linesjob.length; )
                    {
                        if ( sub_linesjob[i].equals("SQL") ){
                            instrucoes.add(sub_linesjob[i]);
                            i++;
                            for ( ;i<sub_linesjob.length; ){
                                SQL+=sub_linesjob[i]+"\n";
                                i++;
                            }
                            instrucoes.add(SQL);
                        }else{
                            instrucoes.add(sub_linesjob[i]);
                        }
                        i++;
                    }

                    /*
                    "jobcarga\n"
                    + "-connIn\n"
                    + connIn+"\n"
                    + "-connOut\n"
                    + connOut+"\n"
                    + "-outTable\n"
                    + outTable+"\n"
                    + "trunc\n"
                    + trunc+"\n"
                    + "SQL\n"
                    + SQL
                    */                        

                    if ( instrucoes.size() == 11
                        && instrucoes.get(0).equals("jobcarga")
                        && instrucoes.get(1).equals("-connIn")
                        && instrucoes.get(3).equals("-fileCSV")
                        && instrucoes.get(5).equals("-connOut")
                        && instrucoes.get(7).equals("-outTable")
                        && instrucoes.get(9).equals("trunc")
                        && instrucoes.get(11).equals("SQL")
                        && ! instrucoes.get(12).equals("")
                    ){
                        threads.add(
                            new Thread(new Runnable() {
                                public void run() {
                                    carga(
                                        instrucoes.get(2)
                                        ,instrucoes.get(4)
                                        ,instrucoes.get(6)
                                        ,instrucoes.get(8)
                                        ,instrucoes.get(10)
                                    );
                                }
                            })
                        );
                        continue;
                    }

                    /*
                    "jobexecute\n"
                    + "-conn\n"
                    + conn+"\n"
                    + "SQL\n"
                    + SQL
                    */                        
                    if ( instrucoes.size() == 5
                        && instrucoes.get(0).equals("jobexecute")
                        && instrucoes.get(1).equals("-conn")
                        && instrucoes.get(3).equals("SQL")
                        && ! instrucoes.get(4).equals("")
                    ){
                        threads.add(
                            new Thread(new Runnable() {
                                public void run() {
                                    execute(
                                        instrucoes.get(2)
                                        ,instrucoes.get(4)
                                    );
                                }
                            })
                        );
                        continue;
                    }
                    System.err.println("Erro, comando inválido:" + line);
                    return;
                }
                for ( int i=0;i<threads.size();i++ )
                    threads.get(i).start();
                for ( int i=0;i<threads.size();i++ )
                    threads.get(i).join();
            }
            closeLine();
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
        }
    }
    
    public static String formatacaoInsertClobComAspetas(String _text)
    {
        String retorno = "";
        int len = 0;
        while (_text.length() > 0 )
        {
            if (_text.length() > 3000)
                len = 3000;
            else
                len = _text.length();

            retorno +=
                (retorno.equals("") ? "" : " || ")
                + " to_clob('" + _text.substring(0, len).replace("'", "''") + "') ";
            if (_text.length() <= 3000)
                _text = "";
            else
                _text = _text.substring(len);
        }
        return retorno;
    }    

    private String getcreate(String connIn, String tabela, String outTable) {
        Connection con=null;
        Statement stmt=null;
        ResultSet rs=null;
        
        String schema="";
        String tabela_=tabela;
        
        if ( tabela.contains(".") ){
            schema=tabela.split("\\.")[0];
            tabela=tabela.split("\\.")[1];
        }
        
        String SQL=lendo_arquivo_pacote("/y/sql_get_ddl_createtable").replace("[TABELA]",tabela).replace("[SCHEMA]",schema);

        try{    
            String retorno="";
            con = getcon(connIn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return "";
            }
            stmt = con.createStatement();
            rs=null;
            rs=stmt.executeQuery(SQL);
            if ( rs.next() ){
                retorno=rs.getString("TXT");
            }    
            close(rs,stmt,con);
            
            if ( ! retorno.equals("") ){
                retorno=removePontoEVirgual(retorno);
                retorno=retorno.trim();                
                String [] partes=retorno.split("\n");
                retorno="CREATE TABLE "+outTable+"\n";
                for ( int i=1;i<partes.length;i++ )
                    retorno+=partes[i]+"\n";
                if ( retorno.contains("USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS") )
                    return tryFixCreate(retorno);
                return retorno;
            }
        }
        catch(Exception e)
        {
            if ( e.toString().contains("ORA-31603") )
            {
                System.err.println("Erro, a tabela "+tabela_+" não foi encontrada!");
                close(rs,stmt,con);
                System.exit(1);
            }
            close(rs,stmt,con);
            return "";
        }        
        close(rs,stmt,con);
        return "";
    }

    private String tryFixCreate(String txt) {
        String retorno="";
        String [] partes=txt.split("\n");
        retorno=partes[0]+"\n";
        retorno+=partes[1]+"\n";
        for ( int i=2;i<partes.length;i++ )
        {
            if ( ! partes[i].trim().startsWith("\"") )
                break;
            retorno+=partes[i]+"\n";
        }
        retorno=retorno.trim();
        retorno=retorno.substring(0,retorno.length()-1)+")";
        return retorno;
    }

    private void close(ResultSet rs, Statement stmt, Connection con) {
        try{ 
            rs.close();
        }catch(Exception e){}
        try{ 
            stmt.close();
        }catch(Exception e){}
        try{ 
            con.close();
        }catch(Exception e){}
    }

    private void try_finish_and_count(long count) {
        // grava em arquivo uma sinalização de sim e count
        String caminho_status_fim=System.getenv("STATUS_FIM_Y");
        String caminho_count=System.getenv("COUNT_Y");
        if ( caminho_status_fim != null && ! caminho_status_fim.equals("") ){
            salvando_file("FIM\n",new File(caminho_status_fim));
        }
        if ( caminho_count != null && ! caminho_count.equals("") ){
            salvando_file(count+"\n",new File(caminho_count));
        }
    }

    public String somente_banco(String a){
        String [] linhas=lendo_arquivo_pacote(a).split("\n");
        String result="";
        int tag=0;
        for ( int i=0;i<linhas.length;i++ ){
            if ( linhas[i].startsWith(" ") && linhas[i].trim().startsWith("[y ") && !linhas[i].trim().startsWith("[y banco ") ) continue;
            if ( linhas[i].startsWith("[y ") ){
                if ( linhas[i].startsWith("[y banco ") )
                    tag=1;
                else
                    tag=2;
            }
            
            if ( tag > 0 && linhas[i].trim().equals("") )
                tag=0;
            
            if ( tag == 2 ) continue;
            
            result+=linhas[i]+"\n";
        }
        return result;
    }
    
    public String helplike(String txt){
        String retorno=helplikecase(txt,true);
        if ( retorno.equals("") )
            retorno=helplikecase(txt,false);
        if ( retorno.equals("") )
            return null;
        return retorno;
    }
    
    public String helplikecase(String txt, boolean case_){
        String [] linhas=somente_detalhado().split("\n");
        String result="";
        boolean achou=false;
        for ( int i=0;i<linhas.length;i++ ){
            if ( linhas[i].startsWith("[") ){
                achou=false;
                if ( 
                    (case_ && linhas[i].equals("[y "+txt+"]"))
                    || (!case_ && linhas[i].toUpperCase().contains(txt.toUpperCase()))
                )
                    achou=true;
            }
            if ( achou )
                result+=linhas[i]+"\n";
        }
        return result;
    }
    
    public String somente_detalhado(){
        String [] linhas=lendo_arquivo_pacote("/y/manual").split("\n");
        String result="";
        int count=0;
        for ( int i=0;i<linhas.length;i++ ){
            if ( count == 2 )
                result+=linhas[i]+"\n";
            if ( linhas[i].trim().equals("") )
                count++;
            if ( count == 3 )
                break;
        }
        return result;
    }
    
    public String somente_mini(String a){
        String [] linhas=lendo_arquivo_pacote(a).split("\n");
        String result="";
        for ( int i=0;i<linhas.length;i++ ){
            result+=linhas[i]+"\n";
            if ( linhas[i].trim().equals("") )
                break;
        }
        return result;
    }
        
    public String lendo_arquivo_pacote(String caminho){
        // System.out.println(
        //   lendo_arquivo_pacote("/y/manual")
        // );
        String result="";
        try{
            readLine(getClass().getResourceAsStream(caminho));
            String strLine;
            while ((strLine = readLine()) != null)
                result+=strLine+"\n";
            closeLine();
            return result;
        }catch (Exception e){}
        return new Arquivos().lendo_arquivo_pacote(caminho);
    }

    public void MetodoGaranteAPermanenciaDeAlgunsImportsJava()
    {
        FilterOutputStream a;
        IOException b;
        OutputStream c;
        ByteBuffer d;
        Charset e;
        Arrays f;
        Comparator g;
    }
    
    private String [] getCamposCSV(String txt) {
        // modelos
        // HEADER_CAMPO1;BB;CC;3;4;5;
        // HEADER_CAMPO1;BB;CC;3;4;5
        
        txt=txt.trim();
        if ( txt.endsWith(sepCSV) )
            txt=txt.substring(0, txt.length()-1);
        return txt.replace("\"","").split( sepCSV.equals("|")?"\\|":sepCSV ); // split nao funciona com |, tem que usar \\|
    }

    private void readColunaCSV(String line) {
        ponteiroLinhaCSV=0;
        linhaCSV=line;
    }
    
    private String readColunaCSV() {
        if ( linhaCSV.length() == 0 )
            return null;
        if ( ponteiroLinhaCSV == -1 )
            return null;
        if ( ponteiroLinhaCSV >= linhaCSV.length() )
            return null;
        if ( linhaCSV.substring(ponteiroLinhaCSV, ponteiroLinhaCSV+1).equals("\"") )
        {
            return readColunaCSVComplexa();
        }else{
            return readColunaCSVSimples();
        }
        
        // linhaCSV
        /*
        HEADER_CAMPO1;BB;CC;3;4;5;
        11;;";;""""""11';;";55;55;55
        11;;";;""""""11';;";55;55;55
        11;;";;""""""11';;";55;55;55;
        33;44
        33;44
        33;44;44;44;44;44;44;44;44;44;44;44;44;44;44;44
        33;44;44;44;44;44;44;44;44;44;44;44;44;44;44;44        
        */
        
    }
    
    private String readColunaCSVComplexa() { // exmeplo ";;""""""11';;"        
        if ( ponteiroLinhaCSV >= linhaCSV.length()-2 )
            return null;
        int ini=ponteiroLinhaCSV+1;
        int fim=-1;
        int pos=ponteiroLinhaCSV+1; // olhando adiantado
        int pos_=-1;
        while(true)
        {    
            pos_=linhaCSV.indexOf("\"",pos);
            if ( pos_ == -1 )
            {
                System.err.println("Erro: CSV inválido, linha inconsistente: "+linhaCSV);
                System.exit(1);
            }
            if ( linhaCSV.indexOf("\"",pos_+1) == pos_+1 ){
                pos=pos_+2;
                continue;
            }
            fim=pos_;
            ponteiroLinhaCSV=pos_+2;
            break;
        }
        return linhaCSV.substring(ini,fim).replace("\r","").replace("\n","");
    }
    
    private String readColunaCSVSimples() {
        if ( linhaCSV.indexOf(sepCSV,ponteiroLinhaCSV) == ponteiroLinhaCSV )
        {
            ponteiroLinhaCSV++;
            return "";
        }
        
        int pos=linhaCSV.indexOf(sepCSV,ponteiroLinhaCSV+1);
        int ini=ponteiroLinhaCSV;
        int fim=-1;
        if ( pos == -1 )
        {
            fim=linhaCSV.length();
            ponteiroLinhaCSV=-1;
        }else{
            fim=pos;
            ponteiroLinhaCSV=pos+1;
        }
        return linhaCSV.substring(ini,fim).replace("\r","").replace("\n","");
    }

    private String getCreateByCamposCSV(String[] camposCSV, String table) {
        String result="CREATE TABLE "+table+" (";
        for ( int i=0;i<camposCSV.length;i++ ){
            result+=" \""+camposCSV[i]+"\" varchar2(4000)";
            if ( i != camposCSV.length-1 )
                result+=",";
        }
        result+=")";
        return result;
    }

    private void scp(String[] args) {        
        // créditos
        // https://github.com/is/jsch/tree/master/examples
        int port=22;
        if ( args.length != 3 && args.length != 4 )
        {
            comando_invalido(args);
            return;
        }
        if ( 
            ( args[1].contains("@") && args[2].contains("@") )
            || ( !args[1].contains("@") && !args[2].contains("@") )
        ){
            comando_invalido(args);
            return;
        }
        if ( args.length == 4 )
        {
            try{
                port=Integer.parseInt(args[3]);
            }catch(Exception e){
                comando_invalido(args);
                return;
            }            
        }
        String[] senha=new String[]{""};
        pedeSenhaCasoNaoTenha(args,senha);
        if ( args[1].contains("@") )
            new JSchCustom().scpFrom(new String[]{args[1],args[2]},senha[0],port);                    
        else
            new JSchCustom().scpTo(new String[]{args[1],args[2]},senha[0],port);                    
        System.exit(0);
    }
    
    private void execSsh(String[] args) {        
        // créditos
        // https://github.com/is/jsch/tree/master/examples
        int port=22;
        if ( args.length != 3 && args.length != 4 )
        {
            comando_invalido(args);
            return;
        }
        if ( !args[1].contains("@") )
        {
            comando_invalido(args);
            return;
        }
        if ( args.length == 4 )
        {
            try{
                port=Integer.parseInt(args[3]);
            }catch(Exception e){
                comando_invalido(args);
                return;
            }            
        }
        String[] senha=new String[]{""};
        pedeSenhaCasoNaoTenha(args,senha);
        new JSchCustom().execSsh(new String[]{args[1],args[2]},senha[0],port);
        System.exit(0);
    }
    
    private void ssh(String[] args) {        
        // créditos
        // https://github.com/is/jsch/tree/master/examples
        int port=22;
        if ( args.length != 2 && args.length != 3 )
        {
            comando_invalido(args);
            return;
        }
        if ( !args[1].contains("@") )
        {
            comando_invalido(args);
            return;
        }
        if ( args.length == 3 )
        {
            try{
                port=Integer.parseInt(args[2]);
            }catch(Exception e){
                comando_invalido(args);
                return;
            }            
        }
        String[] senha=new String[]{""};
        pedeSenhaCasoNaoTenha(args,senha);
        new JSchCustom().ssh(new String[]{args[1]},senha[0],port);
        System.exit(0);
    }
    
    private void sshinfo(String host_,String port_){
        String host = "localhost";
        int port = 22;
        if ( host_ != null )
            host = host_;
        if ( port_ != null )
            port = Integer.parseInt(port_);
        try{
            InetAddress serverAddress = InetAddress.getByName(host);
            Socket socket_ = new Socket(host, port);
            BufferedInputStream _instream = new BufferedInputStream(socket_.getInputStream());
            StringBuffer received = new StringBuffer();
            int b;
            while( (b = _instream.read()) != '\n' )
                received.append((char) b);
            System.out.println(received.toString());
            _instream.close();
            socket_.close();
        }catch(Exception e){
            System.err.println(e.toString());
            System.exit(1);
        }
    }
    
    private void sftp(String[] args) {
        // créditos
        // https://github.com/is/jsch/tree/master/examples
        int port=22;
        if ( args.length != 2 && args.length != 3 )
        {
            comando_invalido(args);
            return;
        }
        if ( !args[1].contains("@") )
        {
            comando_invalido(args);
            return;
        }
        if ( args.length == 3 )
        {
            try{
                port=Integer.parseInt(args[2]);
            }catch(Exception e){
                comando_invalido(args);
                return;
            }            
        }        
        String[] senha=new String[]{""};
        pedeSenhaCasoNaoTenha(args,senha);
        new JSchCustom().sftp(new String[]{args[1]},senha[0],port);
        System.exit(0);
    }
    
    public void pedeSenhaCasoNaoTenha(String [] args,String [] senha){
        // ywanes@desktop's password:
        // String password = new String(console.readPassword("Password: "));
        for( int i=0;i<args.length;i++ ){
            if( args[i].contains("@") ){                
                if (  args[i].startsWith("@") || args[i].endsWith("@") ){
                    System.out.println("Error command");
                    System.exit(1);                    
                }
                if ( args[i].contains(",") ){
                    int p_virgula=args[i].indexOf(",");
                    int p_ultima_arroba=args[i].length()-1;
                    while(!args[i].substring(p_ultima_arroba,p_ultima_arroba+1).equals("@"))
                        p_ultima_arroba--;
                    String user=args[i].substring(0,p_virgula);
                    senha[0]=args[i].substring(p_virgula+1,p_ultima_arroba);
                    String host=args[i].substring(p_ultima_arroba+1,args[i].length());
                    args[i]=user+"@"+host;
                }else{
                    java.io.Console console=System.console();
                    if ( console == null ){
                        System.out.println("Error, input nao suportado nesse ambiente, rodando no netbeans?...");
                        System.exit(1);
                    }
                    
                    String user_server_print=args[i];
                    if ( user_server_print.contains(":") )
                        user_server_print=user_server_print.split(":")[0];
                    
                    String password=null;
                    char [] passChar = System.console().readPassword(user_server_print+"'s password: ");
                    if ( passChar != null )
                        password = new String(passChar);
                    
                    if ( password == null || password.trim().equals("") ){
                        System.out.println("Error, not input found");
                        System.exit(1);
                    }
                    senha[0]=password;
                }
                break;
            }
        }
    }
    
    private void serverRouter(String[] args) {
        if ( args.length == 5 ){
            new Ponte().serverRouter(args[1],Integer.parseInt(args[2]),args[3],Integer.parseInt(args[4]),"");
            return;
        }
        if ( args.length == 6 && ( 
                args[5].equals("show") 
                || args[5].equals("showOnlySend") 
                || args[5].equals("showOnlyReceive") 
                || args[5].equals("showSimple") 
        ) ){
            new Ponte().serverRouter(args[1],Integer.parseInt(args[2]),args[3],Integer.parseInt(args[4]),args[5]);
            return;
        }
        comando_invalido(args);
        System.exit(0);
    }

    public static int byte_to_int_java(byte a,boolean dif_128) {
        // os bytes em java vem 0..127 e -128..-1 totalizando 256
        // implementacao manual de Byte.toUnsignedInt(a)
        // dif_128 true  => -128..127
        // dif_128 false =>    0..255
        int i=(int)a;
        if ( !dif_128 && i < 0 )
            i+=256;
        return i;
    }

    ArrayList<String> xlsxToCSV_nomes=null;
    ArrayList<String> shared=null;
    private void xlsxToCSV(String caminhoXlsx, boolean mostraEstrutura, boolean listaAbas, int numeroAba, String nomeAba, OutputStream out, boolean suprimeHeader) throws Exception {

        //"C:\\Users\\ywanes\\Documents\\teste.xlsx"
        //xlsxToCSV arquivo.xlsx mostraEstrutura
        //xlsxToCSV arquivo.xlsx listaAbas
        //xlsxToCSV arquivo.xlsx numeroAba 1
        //xlsxToCSV arquivo.xlsx nomeAba Planilha1
        
        try{            
            ZipFile zipFile = new ZipFile(caminhoXlsx);
            Enumeration<? extends ZipEntry> enumeration_entries = zipFile.entries();            
    
            InputStream is=null;            
            String caminho="";
            XML xmlShared=null;
            XML xmlNomes=null;
            int sheet_count=0;
            boolean exportSheetCSV=false;
            int sheetEncontrados=0;
            //XML xml=null;
            //String atributo_t=null;
            
            ArrayList<ZipEntry> entries=new ArrayList<ZipEntry>();
            while(enumeration_entries.hasMoreElements()){
                ZipEntry z=enumeration_entries.nextElement();
                if ( z.getName().equals("xl/sharedStrings.xml") ) // "xl/sharedStrings.xml" precisa ser carregado antes das "xl/worksheets/"
                    entries.add(0,z);
                else
                    entries.add(z);
            }
            
            for ( ZipEntry entry : entries){
                caminho=entry.getName();            
                if ( caminho.equals("xl/sharedStrings.xml") || caminho.equals("xl/workbook.xml") || caminho.startsWith("xl/worksheets/") ){
                    // xl/worksheets/sheet1.xml
                    // xl/worksheets/sheet2.xml
                    // xl/sharedStrings.xml
                    // xl/worksheets/_rels/sheet2.xml.rels
                    // xl/workbook.xml

                    if ( caminho.equals("xl/workbook.xml") && xlsxToCSV_nomes != null )
                        continue; // conteudo ja carregado.
                    if ( caminho.equals("xl/sharedStrings.xml") && shared != null )
                        continue; // conteudo ja carregado.
                                        
                    if ( caminho.startsWith("xl/worksheets/") && caminho.endsWith("xml") && listaAbas )
                        continue;
                    
                    exportSheetCSV=false;
                    if ( caminho.startsWith("xl/worksheets/") && caminho.endsWith("xml") && !mostraEstrutura && !listaAbas ){                        
                        if ( xlsxToCSV_nomes.size() == 0 )
                            erroFatal(99);                    
                        if ( numeroAba == -1 && nomeAba.equals(xlsxToCSV_nomes.get(sheet_count)) ){
                            exportSheetCSV=true;
                        }
                        if ( numeroAba != -1 && numeroAba == sheet_count+1 ){
                            exportSheetCSV=true;
                        }                        
                        sheet_count++;
                        if ( ! exportSheetCSV )
                            continue;
                        sheetEncontrados++;
                    }
                    
                    is = zipFile.getInputStream(entry);
                    XML.loadIs(is,mostraEstrutura,false,caminho,exportSheetCSV,out,suprimeHeader);
                    is.close();
                    
                    // carrega lista de abas
                    if ( caminho.equals("xl/workbook.xml") && !mostraEstrutura  ){
                        xlsxToCSV_nomes=new ArrayList<String>();
                        xmlNomes=XML.getXML();
                        for ( XML item1 : xmlNomes.getFilhos())
                            if ( item1.getTag().equals("sheets") )
                                for ( XML item2 : item1.getFilhos())
                                    xlsxToCSV_nomes.add(item2.getAtributo("name"));
                    }
                    

                    // carrega dicionario de palavras compartilhadas
                    if ( caminho.equals("xl/sharedStrings.xml") && !mostraEstrutura && !listaAbas ){
                        shared=new ArrayList<String>();  
                        xmlShared=XML.getXML();
                        for ( XML item1 : xmlShared.getFilhos()){
                            for ( XML item2 : item1.getFilhos()){
                                if(item2.getTag().equals("t")){
                                    shared.add(item2.getValue());
                                }
                            }
                        }
                        XML.shared=shared;
                    }
                }
            }

            if ( !mostraEstrutura && !listaAbas && sheetEncontrados==0 ){
                if ( numeroAba != -1 )
                    System.err.println("Erro, numeroAba: "+numeroAba+" não encontrada!");
                else
                    System.err.println("Erro, nomeAba: "+nomeAba+" não encontrada!");
                System.exit(1);
            }
            
        }catch(Exception e){
            System.err.println("Erro "+e.toString());
            System.exit(1);
        }
        // finalizacao será feita no chamador de xlsxToCSV
        /*if ( out != null ){
            out.flush();
            out.close();
        }*/
    }


    public static byte[] readAllBytes(String path) throws IOException {
        java.io.RandomAccessFile f = new java.io.RandomAccessFile(new File(path), "r");
        if (f.length() > Integer.MAX_VALUE)
            throw new IOException("File is too large");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);        
        if (f.getFilePointer() != f.length())
            throw new IOException("File length changed while reading");
        return b;
    }    



    private void copiaByStream(InputStream pipe_in, OutputStream pipe_out) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];            
        int len;
        while ((len = pipe_in.read(buf)) > -1)
            pipe_out.write(buf, 0, len);
        pipe_out.flush();
        pipe_out.close();
    }

    private void interpretaSqlParaSelectCSV(String sqlText) throws Exception {
        String sqlTextBKP=sqlText;
        
        if ( ! sqlText.startsWith("select ") )
            throw_erroDeInterpretacaoDeSQL("ORAZ: 01 - Não foi possível interpretar o SQL: "+sqlTextBKP);
        
        // remove "select "
        sqlText=sqlText.substring("select ".length());
        
        if ( ! sqlText.endsWith(" from this") )
            throw_erroDeInterpretacaoDeSQL("ORAZ: 02 - Não foi possível interpretar o SQL: "+sqlTextBKP);

        // remove 
        sqlText=sqlText.substring(0,sqlText.length()-" from this".length());

        sqlText=sqlText.trim();
        
        if ( sqlText.equals("") )
            throw_erroDeInterpretacaoDeSQL("ORAZ: 03 - Não foi possível interpretar o SQL: "+sqlTextBKP);
        
        if ( sqlText.equals("*") ){
            selectCSV_camposNameSaida=selectCSV_camposName;
            selectCSV_camposNameSaidaAlias=selectCSV_camposName;
            selectCSV_tratativasWhere=new String[]{};
            return;
        }
            
        String [] partes=sqlText.split(",");
        for ( int i=0;i<partes.length;i++ )
            partes[i]=partes[i].trim();
        selectCSV_camposNameSaida=partes;
        selectCSV_camposNameSaidaAlias=partes;
        selectCSV_tratativasWhere=new String[]{};
    }

    private void processaRegistroSqlParaSelectCSV(OutputStream out) throws Exception {
        StringBuilder sb=new StringBuilder();
        boolean achou=false;
        
        for ( int i=0;i<selectCSV_camposNameSaida.length;i++ ){        
            sb.append("\"");
            achou=false;
            for ( int j=0;j<selectCSV_camposName.length;j++ ){        
                if ( selectCSV_camposNameSaida[i].equals(selectCSV_camposName[j]) ){
                    achou=true;
                    if ( selectCSV_camposValue[j] != null )
                        sb.append(selectCSV_camposValue[j]);
                    break;
                }
            }
            if ( ! achou )
                throw_erroDeInterpretacaoDeSQL("ORAZ: 99 - Não foi possível interpretar o campo: "+selectCSV_camposNameSaida[i]);
            
            if ( i < selectCSV_camposNameSaida.length-1 )
                sb.append("\";");
            else
                sb.append("\"");
        }
        
        // implementar where depois
        
        sb.append("\n");
        out.write(sb.toString().getBytes());
    }

    private void throw_erroDeInterpretacaoDeSQL(String string) throws Exception {
        throw new Exception(string);
    }

    private String get_abaSequencial(ArrayList<String> bkp_lista) {
        if ( bkp_lista.size() > 1 && bkp_lista.get(0).length() > 3 && bkp_lista.get(0).endsWith("(1)") ){
            String tmp=bkp_lista.get(0).substring(0,bkp_lista.get(0).length()-3);
            for ( int i=1;i<bkp_lista.size();i++ ){
                if ( ! bkp_lista.get(i).equals(tmp+"("+(i+1)+")") )
                    return null;
            }
            return tmp;
        }
        return null;
    }

    // tipos de retorno
    // file1|D:\pasta1\file1.class
    // pacote1.file1|D:\pacote1\file1.class
    private ArrayList<String> nav_custom(String dir){
        // dir possiveis
        // .
        // pasta
        // pasta\\
        // d:\\aa
        if ( dir.equals("") ) dir=".";
        dir=new File(dir).getAbsolutePath();
        String sep="/";
        if ( dir.contains("\\") )
            sep="\\";
        if ( dir.endsWith(sep+".") )
            dir=dir.substring(0,dir.length()-2);
        return nav_custom(dir,"",sep);
    }

    private ArrayList<String> nav_custom(String dir1, String dir2, String sep) {
        ArrayList<String> retorno=new ArrayList<String>();
        
        for ( File item : new java.io.File(dir1+dir2).listFiles() ){
            if ( !item.isFile() ) continue;
            if ( !item.getAbsolutePath().endsWith(".class") ) continue;
            String tmp=item.getAbsolutePath().substring(dir1.length()+1);
            tmp=tmp.substring(0,tmp.length()-".class".length());
            tmp=tmp.replace(sep,".");
            retorno.add(
                tmp
                +"|"
                +item.getAbsolutePath()
            );
        }
        
        for ( File item : new java.io.File(dir1+dir2).listFiles() ){
            if ( !item.isDirectory() ) continue;
            retorno.addAll(nav_custom(dir1,dir2+sep+item.getName(),sep));
        }
        return retorno;
    }    

    private void wget(String[] args) {
        args=sliceParm(1,args);
        if ( args.length == 0 )
            args=new String[]{"-h"};
        Wget w=new Wget();
        for ( String comando : args)
            w.comando(comando);
        try {
            w.start_motor();
        } catch (Exception ex) {
            System.err.println("Error: "+ex.toString());
        }
    }

    private void find(String path, Boolean superficial, float mtime){
        String sep=System.getProperty("user.dir").contains("/")?"/":"\\";
        File f=null;
        if (path == null){
            f=new File(System.getProperty("user.dir"));
            path="";
        }else{
            try{
                f=new File(path);
            }catch(Exception e){
                System.out.println(e.toString());
                System.exit(1);
            }
        }
        if ( !f.exists() ){
            System.err.println("Error: \""+path+"\" not found!");
            System.exit(1);
        }
        if ( !f.isDirectory() )
            showfind(path, mtime);
        else
            if ( f.isDirectory())
                find_nav(f, sep, path, superficial, mtime);
    }
    
    private void find_nav(File f, String sep, String hist, Boolean superficial, float mtime){
        if (superficial || hist.equals("") || hist.equals(".") || hist.equals("/") || (hist.contains(":") && hist.length() <= 3) ){
            // faz nada
        }else{
            showfind(hist, mtime);
            hist+=sep;
        }
        try{
            File [] files = f.listFiles();
            for ( int i=0;i<files.length;i++ )
                if ( !files[i].isDirectory() )
                    if ( superficial )
                        showfind(files[i].getName(), mtime);
                    else
                        showfind(hist+files[i].getName(), mtime);
            for ( int i=0;i<files.length;i++ )
                if ( files[i].isDirectory() )
                    if ( superficial )
                        showfind(files[i].getName(), mtime);
                    else
                        find_nav(files[i], sep, hist+files[i].getName(), superficial, mtime);
        }catch(Exception e){}
    }
    
    long findnow = 0;
    private void showfind(String a, float mtime){
        if ( mtime == 0 ){
            System.out.println(a);
        }else{
            if ( findnow == 0 ){
                findnow = java.util.Calendar.getInstance().getTime().getTime();
            }
            long b = new java.util.Date(new File(a).lastModified()).getTime();
            long diffMili = Math.abs(findnow - b);
            if (
                (mtime > 0 && diffMili >= mtime)
                || (mtime < 0 && mtime*-1 >= diffMili)
            ){
                System.out.println(a);
            }
        }                
    }
    
    private void sleep(Float a){
        try { Thread.sleep((int)(a*1000)); } catch (Exception ee) {}
    }
    
    private void split(String bytes, String lines, String prefix, String parm){
        int q_bytes=0;
        int q_lines=0;
        
        if ( !parm.equals("") && ( !new File(parm).exists() || !new File(parm).isFile()) )
        {
            System.err.println(parm + " nao é um arquivo valido!");
            System.exit(1);            
        }            
        
        if ( !bytes.equals("") ){
            try{
                q_bytes=Integer.parseInt(bytes);
                if ( q_bytes <= 0 )
                    throw new Exception();
            }catch(Exception e){
                System.err.println("numero invalido = "+ bytes);
                System.exit(1);                            
            }
        }

        if ( !lines.equals("") ){
            try{
                q_lines=Integer.parseInt(lines);
                if ( q_lines <= 0 )
                    throw new Exception();
            }catch(Exception e){
                System.err.println("numero invalido = "+ lines);
                System.exit(1);                            
            }
        }
        
        InputStream is=null;
        OutputStream os=null;
        try{
            if (parm.equals(""))
                is=System.in;
            else
                is=new FileInputStream(parm);
            
            int BUFFER_SIZE=1024;
            byte[] buf = new byte[BUFFER_SIZE];
            int count=0;
            int len=0;
            int p=0;
            String name="";
            byte[] n_="\n".getBytes();
			
            while( (len=is.read(buf,0,BUFFER_SIZE)) > 0 ){
                p=0;                                
                if ( q_bytes > 0 ){ // bytes
                    while(p < len){						
                        if ( os == null ){
                            name=getNameSplit(prefix);
                            os = new FileOutputStream(name);
                        }
                        if ( count >= q_bytes ){
                            os.flush();
                            os.close();
                            name=getNameSplit(prefix);
                            os = new FileOutputStream(name);
                            count=0;
                        }                 
                        if ( len-p <= q_bytes-count ){
                            int aux=len-p;
                            os.write(buf, p, aux);
                            count+=aux;
                            p+=aux;
                        }else{
                            int aux=q_bytes-count;
                            os.write(buf, p, aux);
                            count+=aux;
                            p+=aux;                            
                        }
                    }
                }else{ // lines
                    while(p < len){						
                        if ( os == null ){
                            name=getNameSplit(prefix);
                            os = new FileOutputStream(name);
                        }
                        if ( count >= q_lines ){
                            os.flush();
                            os.close();
                            name=getNameSplit(prefix);
                            os = new FileOutputStream(name);
                            count=0;
                        }    
                        int aux=0;
                        while(p < len && count < q_lines){
                            if (buf[p] == n_[0])
                                count++;
                            aux++;
                            p++;                            
                        }
                        os.write(buf, p-aux, aux);
                    }
                }
            }
        }catch(Exception e){
            System.err.println(e.toString());
            System.exit(1);
        }
        try{
            is.close();
        }catch(Exception e){}
        try{
            os.flush();
            os.close();
        }catch(Exception e){}
    }
        
    public String [] getBytesLinesPrefixParm(String [] args){
        String bytes="";        
        String lines="";
        String prefix="";
        String parm="";
        
        if ( args.length > 0 && args[0].equals("split") )
            args=sliceParm(1,args);
        
        if ( args.length > 0 && args[0].startsWith("--bytes=") )
        {
            bytes=args[0].split("=")[1];
            args=sliceParm(1,args);
        }

        if ( args.length > 1 && args[0].startsWith("-b") )
        {
            bytes=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 && args[0].startsWith("--lines=") )
        {
            lines=args[0].split("=")[1];
            args=sliceParm(1,args);
        }

        if ( args.length > 1 && args[0].startsWith("-l") )
        {
            lines=args[1];
            args=sliceParm(2,args);
        }

        if ( args.length > 0 && args[0].startsWith("--prefix=") )
        {
            prefix=args[0].split("=")[1];
            args=sliceParm(1,args);
        }

        if ( args.length > 1 && args[0].startsWith("-p") )
        {
            prefix=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 0 )
        {
            parm=args[0];
            args=sliceParm(1,args);
        }
        
        if ( args.length > 0 || lines.equals("") == bytes.equals("") )
            return null;

        return new String[]{bytes, lines, prefix, parm};
    }

    private void regua(int p){
        String s="";
        if( p < 0 ) 
            return;
        if ( p > 300 )
            p=300;
        for ( int i=5;i<=p;i+=5 ){
            if(i%10==5)
                s+="....+";
            else{
                String t=""+i;
                while (t.length()<5)
                    t="."+t;
                s+=t;
            }
        }
        System.out.println(s);
    }

    private void link(String fonte, String new_){
        if ( new File(new_).exists() ){
            System.out.println("Error - Ja existe o elemento " + new_ + "!");
            System.exit(1);        
        }else{
            String sep=System.getProperty("user.dir").contains("/")?"/":"\\";
            File [] files = new File(System.getProperty("user.dir")+sep+new_).getParentFile().listFiles();
            for ( int i=0;i<files.length;i++ ){
                if ( files[i].getName().equals(new_)){
                    System.out.println("Error - Ja existe o elemento " + new_ + "!");
                    System.exit(1);                            
                }
            }
        }
        if ( !new File(fonte).exists() ){
            System.out.println("Warning: O Elemento de origem " + fonte + " nao foi encontrado, mesmo assim o linked deve ser criado!");
        }
            
        String command="";
        if ( System.getProperty("user.dir").contains("/") )
            command = "ln -s " + fonte + " " + new_;
        else
            command = "cmd /c mklink /j " + new_ + " " + fonte;
        
        try{
            System.out.println("running command " + command);
            Runtime.getRuntime().exec(command);
            System.out.println("finish!");
        }catch(Exception e){
            System.out.println("Error...");
            System.out.println(e.toString());
            System.exit(1);
        }            
    }
    
    private void os() {
        boolean show=false;
        
        for ( String command : new String[]{
            "cmd /c wmic os get BootDevice,BuildNumber,Caption,OSArchitecture,RegisteredUser,Version",
            "system_profiler SPSoftwareDataType",
            "oslevel;cat /proc/version",
            "lsb_release -a;cat /proc/version",
            "cat /etc/os-release;cat /proc/version",
        } ){
            try {
                String [] command_p = command.split(";");
                boolean error=false;
                for ( int i=0;i<command_p.length;i++ ){
                    Process proc;
                    proc = Runtime.getRuntime().exec(command_p[i]);
                    int len=0;
                    byte[] b=new byte[1024];
                    boolean ok=false;                    
                    while ( (len=proc.getInputStream().read(b, 0, b.length)) != -1 ){
                        System.out.write(b, 0, len);
                        ok=true;
                    }
                    while ( (len=proc.getErrorStream().read(b, 0, b.length)) != -1 ){
                        error=true;
                    }           
                    if ( !ok ){
                        System.err.println("Erro fatal 99!");
                        System.exit(1);
                    }
                    if ( error ) break;
                }
                if ( error ) continue;
                show=true;
                    break;
                        
            } catch (Exception ex) {
                continue;
            }        
        }
        if ( !show )
            System.out.println("Nenhum sistema foi detectado!");

    }
    
    private void date(String [] args){
        String parm="+%d/%m/%Y %H:%M:%S";
        if ( args.length > 1 ){
            parm=args[1];
            for ( int i=2;i<args.length;i++ )
                parm+=" "+args[i];
        }
        if(parm.startsWith("+"))
            parm=parm.substring(1);
        Date d = new Date();
        String w="";
        for(int i=0;i<parm.length();i++){
            w+=parm.substring(i, i+1);
            if(w.equals("%"))
                continue;
            if(w.equals("%d")){
                System.out.print(new SimpleDateFormat("dd").format(d));
                w="";
                continue;
            }
            if(w.equals("%m")){
                System.out.print(new SimpleDateFormat("MM").format(d));
                w="";
                continue;
            }
            if(w.equals("%Y")){
                System.out.print(new SimpleDateFormat("yyyy").format(d));
                w="";
                continue;
            }
            if(w.equals("%H")){
                System.out.print(new SimpleDateFormat("HH").format(d));
                w="";
                continue;
            }
            if(w.equals("%M")){
                System.out.print(new SimpleDateFormat("mm").format(d));
                w="";
                continue;
            }
            if(w.equals("%S")){
                System.out.print(new SimpleDateFormat("ss").format(d));
                w="";
                continue;
            }
            if(w.equals("%N")){
                System.out.print(new SimpleDateFormat("SSS").format(d));
                w="";
                continue;
            }
            if(w.equals("%Z")){
                System.out.print(new SimpleDateFormat("X").format(d));
                w="";
                continue;
            }
            if(w.equals("%s")){
                System.out.print(epoch(d));
                w="";
                continue;
            }
            System.out.print(w);
            w="";
        }
        System.out.println();
    }
    
    private void uptime(boolean ms){
        boolean show=false;
        
        String [] command = new String[]{
            "cmd /c wmic path Win32_OperatingSystem get LastBootUpTime, LocalDateTime",
            "cat /proc/uptime",
            "date +%s",
            "sysctl -n kern.boottime",
        };
        String [] index_command = new String[]{
            "windows",
            "linux",
            "aux_mac",
            "mac",
        };      
        String s1_aux="";  
        for ( int i=0;i<command.length;i++ ){
            try {          
                boolean error=false;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Process proc = Runtime.getRuntime().exec(command[i]);
                int len=0;
                byte[] b=new byte[1024];
                boolean ok=false;                    
                while ( (len=proc.getInputStream().read(b, 0, b.length)) != -1 ){
                    baos.write(b, 0, len);
                    ok=true;
                }
                while ( (len=proc.getErrorStream().read(b, 0, b.length)) != -1 ){
                    baos.write(b, 0, len);
                    error=true;
                }          
                String s = baos.toString("UTF-8");
                
                if ( !ok ){
                    if ( s.contains("No such file") )
                        continue;
                    if ( s.contains("Permission denied") ){                        
                        System.err.println("Permission denied!");
                        System.exit(1);
                    }
                    System.err.println("Erro fatal 99!");
                    System.exit(1);
                }
                if ( error ) continue;
                
                long seconds=-1;
                if ( index_command[i].equals("windows") ){
                    s = s.split("\r\n")[1];
                    String s1=s.split(" ")[0];
                    String s2=s.split(" ")[2];
                    if ( s1.split("\\.").length > 1 )
                        s1=s1.split("\\.")[0];
                    if ( s2.split("\\.").length > 1 )
                        s2=s2.split("\\.")[0];
                    seconds=new SimpleDateFormat("yyyyMMddHHmmss").parse(s2).getTime() - new SimpleDateFormat("yyyyMMddHHmmss").parse(s1).getTime();                    
                    seconds/=1000;
                }
                if ( index_command[i].equals("linux") ){
                    s = s.split("\r\n")[0];
                    String s1=s.split(" ")[0];
                    String s2=s.split(" ")[1];
                    if ( s1.split("\\.").length > 1 )
                        s1=s1.split("\\.")[0];
                    if ( s2.split("\\.").length > 1 )
                        s2=s2.split("\\.")[0];
                    seconds=Long.parseLong(s1)-Long.parseLong(s2);                    
                }
                if ( index_command[i].equals("aux_mac") ){
                    s = s.split("\r\n")[0];
                    s1_aux = s.replace("\r","").replace("\n","");
                    continue;
                }
                if ( index_command[i].equals("mac") ){
                    s = s.split("\r\n")[0];
                    String s1=s1_aux;
                    String s2=s.replace(",","").split(" ")[3];
                    seconds=Long.parseLong(s1)-Long.parseLong(s2);                    
                }
                if ( ms ){
                    System.out.println(seconds+" seconds");
                }else{
                    long second = 1;
                    long minute = 60*second;
                    long hour = 60*minute;
                    long day = 24*hour;
                    long minutes=0;
                    long hours=0;
                    long days=0;
                    while(seconds >= day){
                        seconds-=day;
                        days++;
                    }
                    while(seconds >= hour){
                        seconds-=hour;
                        hours++;
                    }
                    while(seconds >= minute){
                        seconds-=minute;
                        minutes++;
                    }
                    s="up ";
                    if ( !s.equals("up ") || days > 0 ){
                        s+=" "+days+" days,";
                    }
                    if ( !s.equals("up ") || hours > 0 ){
                        s+=" "+hours+" hours,";
                    }
                    if ( !s.equals("up ") || minute > 0 ){
                        s+=" "+minutes+" minutes,";
                    }
                    if ( !s.equals("up ") || second > 0 ){
                        s+=" "+seconds+" seconds";
                    }
                    System.out.println(s);
                }
                show=true;
                break;                        
            } catch (Exception ex) {
                continue;        
            }        
        }
        if ( !show )
            System.out.println("Falha ao obter uptime");
    }
    
    private void cronometro(String parm){
        if ( parm == null ){
            try{
                System.out.print("startado. pressione enter para mais flags.");
                InputStream inputStream_pipe=System.in;
                int BUFFER_SIZE=1024;
                byte[] buf = new byte[BUFFER_SIZE];
                int len=0;
                ArrayList<Long> lista=new ArrayList<>();
                lista.add(epochmili(null));
                while( (len=inputStream_pipe.read(buf,0,BUFFER_SIZE)) > 0 ){
                    lista.add(epochmili(null));
                    Long [] elem=new Long[lista.size()];
                    for ( int i=0;i<lista.size();i++ )
                        elem[i]=(Long)lista.get(i);
                    String s="";
                    for ( int i=1;i<lista.size();i++ ){
                        if( i == 1 ){
                            s=(elem[i]-elem[i-1])+" mili";
                        }else{
                            s=(elem[i]-elem[i-1]) + " mili - " + (elem[i]-elem[0]) + " mili total";                                
                        }
                    }                        
                    System.out.print(s);
                }
                System.out.flush();
                System.out.close();
            }catch(
                Exception e){System.out.println("Erro fatal!");
            };            
        }else{
            if ( parm.equals("start") )
                if ( ! salvando_file(epochmili(null)+"\n",new File(".cron_flag")) )
                    System.out.println("Erro, nao foi possivel gravar uma flag!");
            if ( parm.equals("flag") )
                if ( ! salvando_file(epochmili(null)+"\n",new File(".cron_flag"),true) )
                    System.out.println("Erro, nao foi possivel gravar uma flag!");
            if ( parm.equals("end") ){
                File f=new File(".cron_flag");
                if ( f.exists() ){
                    String s=lendo_arquivo(".cron_flag")+"\n"+epochmili(null);
                    String [] partes=s.split("\n");
                    Long [] elem=new Long[partes.length];
                    for ( int i=0;i<partes.length;i++ )
                        elem[i]=Long.parseLong(partes[i]);
                    for ( int i=1;i<partes.length;i++ ){
                        if( i == 1 ){
                            System.out.println((elem[i]-elem[i-1])+" mili");
                        }else{
                            System.out.println((elem[i]-elem[i-1]) + " mili - " + (elem[i]-elem[0]) + " mili total");
                        }
                    }
                    if ( ! new File(".cron_flag").delete() )
                        System.out.println("Erro, nao foi possivel apagar a flag!");
                }else
                    System.out.println("Erro, nao foi possivel ler a flag!");
            }
        }
    }
    
    private boolean tipo_cadastrado(int a) {
        return true; 
        // controle desabilitado - ja foram feitos muitos testes... 
        // return a == -9 || a == -5 || a == -3 || a == -1 || a == 1 || a == 2 || a == 3 || a == 4 || a == 12 || a == 93 || a == 2005;                
    }
    
    private boolean tipo_numerico(int a) {
        return a == 2 || a == 3 || a == 4;
    }

    ArrayList<Integer> pivoName=new ArrayList<Integer>();    
    int pivoNameP1=1;    
    private String getNameSplit(String prefix) {
        if ( pivoName.size() == 0 ){
            pivoName.add(120);
            pivoName.add(97);
            pivoName.add(97);
        }
        String result=prefix;
        int len=pivoName.size();        
        for ( int i=0;i<len;i++ )
            result+=(char)(int)pivoName.get(i);        
        pivoName.set(len-1,pivoName.get(len-1)+1);
        for ( int i=pivoName.size()-1;i>0;i-- ){
            if ( pivoName.get(i) > 122 ){
                pivoName.set(i, 97);
                pivoName.set(i-1,pivoName.get(i-1)+1);
            }
        }
        if (pivoName.get(pivoNameP1) >= 122){
            pivoNameP1++;
            pivoName.add(97);
            pivoName.add(97);            
        }
        return result;
    }

    private String[] arrayList_to_array(ArrayList a){
        String[] args=new String[a.size()];
        for(int i=0;i<a.size();i++)
            args[i]=a.get(i).toString();
        return args;        
    }

    private boolean bind_isSep(String parm){    
        return parm.startsWith("/") || parm.startsWith("\\");
    }
    
    private String[] bind_asterisk(String[] args) {        
        ArrayList lista=new ArrayList<String>();
        for(int i=0;i<args.length;i++)
            lista.addAll(bind_asterisk_parm(args[i]));
        return arrayList_to_array(lista);
    }
   
    ArrayList bind_nav;
    private ArrayList bind_asterisk_parm(String parm){
        bind_nav=new ArrayList();
        if ( ! parm.contains("*") || parm.equals("") ){
            bind_nav.add(parm);
            return bind_nav;
        }
        String [] partesA=bind_asterisk_getPartes(parm);
        String [] partesB=bind_asterisk_getPartes(parm);
        File f;
        if(parm.length()>1){
            if( partesA[0].equals("/") )
                bind_asterisk_nav(new File("/"), partesA, partesB, 1);
            else
                if( partesA[0].contains(":") )
                    bind_asterisk_nav(new File(partesA[0]+"\\"), partesA, partesB, 2);
                else
                    if(partesA[0].contains("*"))
                        bind_asterisk_nav(new File("."), partesA, partesB, 0);
                    else
                        bind_asterisk_nav(new File(partesA[0]), partesA, partesB, 2);
        }else
            bind_asterisk_nav(new File("."), partesA, partesB, 0);
        if ( bind_nav.size() == 0 )
            bind_nav.add(parm);
        return bind_nav;
    }    
        
    private void bind_asterisk_nav(File f, String [] partesA, String [] partesB, int p){
        if ( ! f.exists() )
            return;
        File [] files=f.listFiles();
        if ( files == null )
            return;
        for( int i=0;i<files.length;i++ ){
            String result_match=bind_match(partesA[p],files[i].getName());
            if ( result_match != null ){
                partesB[p]=result_match;
                if(p+2>=partesA.length){
                    bind_processa(partesB);
                }else{
                    bind_asterisk_nav(files[i], partesA, partesB, p+2);
                }
            }
        }
    }
    
    private String bind_match(String digitado, String presenteNoLocal){
        if(digitado.equals(presenteNoLocal))
            return digitado;
        if(digitado.contains("*")){
            boolean result=bind_match(digitado, 0, presenteNoLocal, 0);
            if (result){
                return presenteNoLocal;
            }
        }
        return null;
    }
    
    private boolean bind_match(String digitado, int pos1, String presenteNoLocal, int pos2){
        if ( pos1 == digitado.length() && pos2 == presenteNoLocal.length())
            return true;
        if ( pos1 >= digitado.length() || pos2 >= presenteNoLocal.length())
            return false;
        if ( digitado.substring(pos1,pos1+1).equals("*") ){
            for(int i=0;i<presenteNoLocal.length()-pos2+1;i++){
                boolean result=bind_match(digitado, pos1+1, presenteNoLocal, pos2+i);
                if(result)
                    return true;
            }
        }else{
            if ( digitado.substring(pos1,pos1+1).equals(presenteNoLocal.substring(pos2,pos2+1)) ){
                return bind_match(digitado, pos1+1, presenteNoLocal, pos2+1);
            }
        }
        return false;
    }
    
    private void bind_processa(String [] partes){
        String s="";
        for(int i=0;i<partes.length;i++)
            s+=partes[i];
        bind_nav.add(s);
    }
    
    private String[] bind_asterisk_getPartes(String parm){    
        ArrayList lista=new ArrayList();
        String tail=parm.substring(0, 1);
        for(int i=1;i<parm.length();i++){
            String p=parm.substring(i, i+1);
            if ( bind_isSep(tail) != bind_isSep(p) ){
                lista.add(tail);
                tail=p;
            }else
                tail+=p;
        }
        lista.add(tail);
        return arrayList_to_array(lista);
    }
    
    private long epoch(Date d) {
        return Long.parseLong((epochmili(d)+"").substring(0,10));                
    }
    
    private long epochmili(Date d){
        if ( d == null )
            d = new Date();
        return d.toInstant().toEpochMilli();
    }
}

class Util{
    public static String hex_string="0123456789ABCDEF";
    public static String lendo_arquivo(String caminho) {
        String result="";
        String strLine;
        try{
            readLine(caminho);
            while ((strLine = readLine()) != null)   {
                if ( result.equals("") )
                    result+=strLine;
                else
                    result+="\n"+strLine;
            }
            closeLine();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return result;
    }
    
    static void testOn() {
        try{
            inputStream_pipe=new FileInputStream("c:/tmp/file.json");
        }catch(Exception e){
            erroFatal(404);
        }
    }
    public static String sepCSV=getSeparadorCSV(); // ";";
    public static int BUFFER_SIZE=1024;
    
    private static String getSeparadorCSV(){
        String sep_=System.getenv("CSV_SEP_Y");
        if ( sep_ == null || sep_.trim().equals("") )
            sep_=";";
        return sep_;
    }
    
    public static void readLine(String caminho) throws Exception{
        readLine(new FileInputStream(new File(caminho)));
    }
    
    public static java.util.Scanner scanner_pipe=null;
    public static void readLine(InputStream in){        
        readLine(in,null,null);
    }    
    
    public static void readLine(InputStream in,String encoding,String delimiter){
        if ( delimiter == null )
            delimiter="\n";
        if ( encoding == null )
            scanner_pipe=new java.util.Scanner(in);
        else
            scanner_pipe=new java.util.Scanner(in,encoding);
        scanner_pipe.useDelimiter(delimiter);        
    }    
    
    public static String readLine(){
        try{            
            if ( scanner_pipe == null ){
                readLine(System.in);
            }
            if ( scanner_pipe.hasNext() )
                return scanner_pipe.next().replace("\r","");
            else
                return null;            
        }catch(java.util.NoSuchElementException no) {
            return null;
        }catch(Exception e){
            System.err.println("NOK: "+e.toString());
        }
        return null;
    }

    public static void closeLine(){
        try{
            scanner_pipe.close();            
        }catch(Exception e){}
        scanner_pipe=null;
    }
    
    public static java.util.Scanner scanner_pipeB=null;
    public void readLineB(String caminho) throws Exception{
        readLineB(new FileInputStream(new File(caminho)));
    }
    
    public void readLineB(InputStream in){
        scanner_pipeB=new java.util.Scanner(in);
        scanner_pipeB.useDelimiter("\n");
    }
        
    public String readLineB(){        
        try{
            if ( scanner_pipeB == null )
                readLineB(System.in);
            if ( scanner_pipeB.hasNext() )
                return scanner_pipeB.next();
            else
                return null;
        }catch(java.util.NoSuchElementException no) {
            return null;
        }catch(Exception e){
            System.err.println("NOK: "+e.toString());
        }
        return null;
    }
    
    public void closeLineB(){
        try{
            scanner_pipeB.close();
        }catch(Exception e){}
        scanner_pipeB=null;
    }
    
    public static InputStream inputStream_pipe=null;    
    
    public void readBytes(String caminho) throws Exception{
        readBytes(new File(caminho));
    }
    public void readBytes(File file) throws Exception{
        readBytesInit();
        inputStream_pipe=new FileInputStream(file);
    }
    
    public int readBytes(byte[] buf){
        return readBytes(buf,0,BUFFER_SIZE);
    }
    
    int read1Byte_n=-1;
    int read1Byte_len=-1;
    public void readBytesInit(){
        read1Byte_n=-1;
        read1Byte_len=-1;
    }
    
    public int readBytes(byte[] buf,int off,int len){
        try{
            if ( inputStream_pipe == null ){
                readBytesInit();
                inputStream_pipe=System.in;
            }
            int retorno=-1;
            while( (retorno=inputStream_pipe.read(buf,off,len)) == 0 ){
            }
            return retorno;
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
            System.exit(1);
        }
        return -1;
    }
    
    byte[] read1ByteBuff = new byte[BUFFER_SIZE];
    public boolean read1Byte(byte [] b){
        if ( inputStream_pipe == null ){
            readBytesInit();
            inputStream_pipe=System.in;
        }   
        if ( read1Byte_n == -1 || read1Byte_n >= read1Byte_len ){
            read1Byte_n=0;
            read1Byte_len=readBytes(read1ByteBuff);            
        } 
        if ( read1Byte_n < read1Byte_len ){
            b[0]=read1ByteBuff[read1Byte_n];
            read1Byte_n++;
            return true;
        }
        return false;
    }
    
    public void closeBytes(){
        try{
            inputStream_pipe.close();            
        }catch(Exception e){}
        inputStream_pipe=null;
    }
    
    public void write1Byte(int b){
        write1Byte(new byte[]{(byte)b});
    }

    // write1Byte
    byte[] write1ByteBuff = new byte[BUFFER_SIZE];
    int write1Byte_n=0;
    public void write1Byte(byte [] b){
        if ( write1Byte_n >= BUFFER_SIZE ){
            System.out.write(write1ByteBuff, 0, BUFFER_SIZE);
            write1Byte_n=0;            
        }
        write1ByteBuff[write1Byte_n]=b[0];
        write1Byte_n++;
    }
    
    public void write1ByteFlush(){
        if ( write1Byte_n > 0 ){
            System.out.write(write1ByteBuff, 0, write1Byte_n);
            write1Byte_n=0;
        }
    }    
    
    public static void erroFatal(int n) {
        System.out.println("Erro Fatal! "+n);
        System.err.println("Erro Fatal! "+n);
        System.exit(1);
    }      
}


class JSON extends Util{
    boolean literal=false;   
    String command="";
    boolean mostraTabela=false;
    boolean mostraEstrutura=false;
    boolean mostraEstruturaObs=false;
    boolean list_on=false;
    String filter_for="";
    String filter_forB="";
    boolean filter_on=false;
    boolean filter_onB=false;
    String unico_campo="";
    String [] campos= new String[99];
    int count_campos=0;
    boolean finish_add_campos=false;
    public boolean go(String command, boolean mostraTabela, boolean mostraEstrutura, boolean mostraEstruturaObs, boolean list_on){ // "[elem['id'] for elem in data['items']]"        
        this.command=command;
        this.mostraTabela=mostraTabela;
        this.mostraEstrutura=mostraEstrutura;
        this.mostraEstruturaObs=mostraEstruturaObs;  
        this.list_on=list_on;
        if ( setFilter() ){
        }else{
            return false;
        }
        byte[] entrada_ = new byte[1];
        while ( read1Byte(entrada_) ){
            String t=new String(entrada_);            
            if ( t.equals("\"") ){
                literal=!literal;
                if ( literal )
                    pai="";
            }
            if ( !literal && t.equals(" ") ){
                continue;
            }
            if ( t.equals("\t") || t.equals("\r") || t.equals("\n") ){
                continue;
            }
            if ( literal && !t.equals("\"") && !t.equals(" ") ){
                pai+=t;
            }
            next(t);
            if ( !literal && t.equals(":")){
                next(" ");            
            }
        }   
        nextflush();
        return true;
    }
    
    /*
      Define o filtro, ex data['items']['itemsB']
    */
    private boolean setFilter(){ // "[elem['id'] for elem in data['items']['itemsB']]"
        if ( command.startsWith("[") && command.endsWith("]") ) // "[elem['id'] for elem in data['items']]" -> "elem['id'] for elem in data['items']['itemsB']"
            command=command.substring(1,command.length()-1); 
        else
            return false;
        String [] partes=command.split(" for elem in ");
        if ( partes.length == 2 ){
            String a=partes[0];
            String b=partes[1];
            partes=a.split("'"); // elem['id']
            if ( partes.length == 3 ){
                unico_campo=partes[1];
            }
            if ( b.startsWith("data") ){ // data['items']['itemsB'] -> _.items.itemsB._
                                         // data -> _
                //partes=b.substring(4).replace("[", "").replace("]", "").split("'");
                partes=b.substring(4).replace("]","],").split(",");
                filter_for="_";
                for ( String parte : partes ){
                    if ( parte.equals("") )
                        continue;
                    if ( parte.startsWith("[") && parte.endsWith("]") )
                        parte=parte.substring(1, parte.length()-1);
                    else
                        return false;
                    if ( 
                        (parte.startsWith("'") && !parte.endsWith("'"))
                        || (!parte.startsWith("'") && parte.endsWith("'"))
                    )
                        return false;
                    parte=parte.replace("'","");
                    filter_for+="."+parte;
                }
                filter_forB=filter_for;
                filter_for+="._";
            }else
                return false;
        }    
        return true;
    }
    
    private void indexacao() {     
        for ( int i=0;i<count_pilha*2;i++ )
            outindex(" ");
    }
    
    String [] pilha=new String [999];
    String [] pilha_pai=new String [999];
    String pai="";
    
    int count_pilha=0;
    String level_in="[{";
    String level_out="]}";
    int seq=0;
    /*
      tratando byte a byte
    */
    private void next(String t) {
        if ( level_in.contains(t) ){
            seq=0;
            int aux=level_in.indexOf(t);
            pilha[count_pilha]=level_out.substring(aux, aux+1);
            pilha_pai[count_pilha]=pai;
            pai="";
            count_pilha++;
            out(t);
            outwrite();                
            obs();
            indexacao();
            return;
        }else{
            if ( level_out.contains(t) ){
                if (count_pilha==0 || !pilha[count_pilha-1].equals(t)){
                    erroFatal(99);
                }
                count_pilha--;     
                outwrite();
                obs();
                indexacao();
                out(t);
                return;
            }
        }
        if ( t.equals(",")){
            pai="";
            out(t);
            outwrite();
            obs();
            indexacao();
            return;
        }
        out(t);
    }   
    /*
      finalizar processo.
    */   
    private void nextflush(){
        outwrite();
        if ( !command.equals("") ){
            if ( !finish_add_campos  )
                print_header();
            if ( !detail.equals("") )
                System.out.println(detail);
        }
    }
    
    String [] tabelas=new String[99];
    int count_tabelas=0;    
    private void obs(){
        String obs="";
        String tabela="data";
        for ( int i=0;i<count_pilha;i++ ){
            if ( i > 0 )
                obs+=".";
            obs+=pilha_pai[i].equals("")?"_":pilha_pai[i];
            if ( i > 0 && i < count_pilha-1)
                tabela+="['"+pilha_pai[i]+"']";
        }
        if ( mostraEstruturaObs )
            System.out.println(obs);
        if ( mostraTabela ){
            if ( obs.endsWith("._") && !obs.contains("._.") ){
                tabela="[elem for elem in " + tabela + "]";
                if ( !contemNaTabela(tabela) && count_tabelas < 50 ){
                    tabelas[count_tabelas++]=tabela;
                    System.out.println(tabela);
                }
            }           
        }
        filter_on=obs.equals(filter_for); // list of obj -> [{"b":1},{"b":2}] ou [[1,2],[3,4]]
        filter_onB=obs.equals(filter_forB); // list of value -> [1,2]
    } 
    private boolean contemNaTabela(String a){
        for ( int i=0;i<count_tabelas;i++ )
            if ( tabelas[i].equals(a) )
                return true;
        return false;
    }                    
    
    String out="";
    String out_mostra="";
    private void out(String a){
        out+=a;
        out_mostra+=a;
    }
    private void outindex(String a){
        out_mostra+=a;
    }
    String detail="";
    String key="";
    String value="";    
    boolean unica_verificacao=false;
    private void outwrite(){
        if ( mostraEstrutura || mostraEstruturaObs )
            System.out.println(out_mostra);
        if ( !unica_verificacao && !command.equals("") && filter_onB ){
            unica_verificacao=true;
            if ( !out.startsWith("{") && !out.startsWith("[") ){
                filter_on=filter_onB;
                filter_for=filter_forB;
            }
        }
        if ( !command.equals("") && filter_on && get_KeyValue() ){
            // add campo
            if ( !finish_add_campos ){
                if ( value.equals("{") || value.equals("[") ){
                    finish_add_campos=true;
                    print_header();                    
                }else{
                    if ( !contem(key) ){
                        if ( unico_campo.equals("") ){
                            campos[count_campos++]=key;
                        }
                        if ( !unico_campo.equals("") && unico_campo.equals(key) ){
                            campos[count_campos++]=key;
                        }
                    }else{
                        finish_add_campos=true;
                        print_header();
                    }
                }
            }
            if ( finish_add_campos && campos[0].equals(key)){
                if ( list_on ){
                    //pass
                }else{
                    System.out.println(detail);
                    detail="";                                        
                }
            }
            if ( contem(key) ){
                if ( !detail.equals("") )
                    if ( list_on )
                        detail+=", ";
                    else
                        detail+=sepCSV;
                detail+="\""+value+"\"";
            }
        }
        
        out="";
        out_mostra="";
    }
    
    private void print_header(){
        if ( list_on == false ){
            for ( int i=0;i<count_campos;i++ ){
                if ( i != 0 )
                    System.out.print(sepCSV);
                System.out.print("\""+campos[i]+"\"");
            }
            System.out.println();        
        }
    }
    
    private boolean contem(String a){
        for ( int i=0;i<count_campos;i++ )
            if ( campos[i].equals(a) )
                return true;
        return false;
    }
    
    private boolean get_KeyValue(){
        String a=out;
        key="?";
        value="?";
        if ( a.endsWith("{") || a.endsWith("[") || a.equals("}") || a.equals("]") || a.equals("},") || a.equals("],") ){
            return false;
        }
        a=tiraVirgula(a);
        int p=a.indexOf(": ");
        if ( p > 0 ){
            key=tiraAspasPontas(a.substring(0, p-1));
            value=tiraAspasPontas(a.substring(p+2,a.length()));
        }else{
            key="f"+(++seq)+"_";
            value=tiraAspasPontas(a);
        }   
        value=value.replace("\"", "\"\"");
        return true;
    }
    private String tiraVirgula(String a){
        if ( a.endsWith(",") )
            return a.substring(0,a.length()-1);         
        return a;
    }
    private String tiraAspasPontas(String a){
        if ( a.startsWith("\"") )
            a = a.substring(1, a.length());
        if ( a.endsWith("\"") )
            a = a.substring(0, a.length()-1);
        return a;
    }
}

class Ponte {
    //exemplo
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"");                
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"show");                
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"showOnlySend");                
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"showOnlyReceive");                
    
    public static boolean displayIda=false;
    public static boolean displayVolta=false;
    public static boolean displaySimple=false;

    public void serverRouter(final String host0,final int port0,final String host1,final  int port1,final String typeShow){
        Ambiente ambiente=null;
        try{
            ambiente=new Ambiente(host0,port0);
        }catch(Exception e){
            System.out.println("Nao foi possível utilizar a porta "+port0+" - "+e.toString());
            System.exit(1);
        }     
        System.out.println("ServerRouter criado.");
        System.out.println("obs: A ponte só estabelece conexão com o destino quando detectar o início da origem");
        if ( typeShow.equals("show") || typeShow.equals("showOnlySend") || typeShow.equals("showSimple"))
            displayIda=true;
        if ( typeShow.equals("show") || typeShow.equals("showOnlyReceive") || typeShow.equals("showSimple"))
            displayVolta=true;
        if ( typeShow.equals("showSimple") )
            displaySimple=true;
        while(true){
            try{
                final Socket credencialSocket=ambiente.getCredencialSocket();
                new Thread(){
                    public void run(){
                        ponte0(credencialSocket,host1,port1);
                    }
                }.start();   
            }catch(Exception e){
                System.out.println("FIM");
                break;
            }
        }
    }

    private void ponte0(Socket credencialSocket, String host1, int port1) {
        String id=padLeftZeros(new Random().nextInt(100000)+"",6);
        // formatando ip
        String ip_origem=credencialSocket.getRemoteSocketAddress().toString().substring(1);
        if ( ip_origem.contains(":") ){
            int p=ip_origem.length()-1;
            while(ip_origem.charAt(p--) != ':'){}
            ip_origem=ip_origem.substring(0,p+1);
        }
        System.out.println("iniciando ponte id "+id+" - ip origem "+ip_origem);
        Origem origem=null;
        try{
            Destino destino=new Destino(host1,port1);                    
            origem=new Origem(credencialSocket,id);
            origem.referencia(destino);
            destino.referencia(origem);
            origem.start(); // destino é startado no meio do start da origem;
        }catch(Exception e){
            System.out.println("termino inexperado de ponte id "+id+" - "+e.toString());
            origem.destroy();
        }
        System.out.println("finalizando ponte id "+id);
    }

    private class Destino {   // |   |<->|
        OutputStreamCustom os=null;
        Origem origem=null;
        String host1;
        int port1;        
        private Destino(String host1, int port1) {
            this.host1=host1;
            this.port1=port1;
        }
        private void referencia(Origem origem) {
            this.origem=origem;
        }
        private void start() throws Exception {
            Socket socket=new Socket(host1, port1);                                                
            final InputStream is=socket.getInputStream();           //  |   |<- |             
            os=new OutputStreamCustom(socket.getOutputStream());    //  |   | ->|
            new Thread(){
                public void run(){
                    int len=0;   
                    byte[] buffer = new byte[2048];
                    try{
                        while( (len=is.read(buffer)) > -1 )
                            origem.volta(len,buffer);
                        System.out.println("desconectou"); 
                    }catch(Exception e){
                        System.out.println("desconectou destino "+e.toString());
                    }
                    origem.destroy(); 
                }
            }.start();                                    
        }

        private void ida(byte[] buffer,int len, String ponteID) throws Exception {   // |   | ->|          
            os.write(OutputStreamCustom.IDA,buffer,0,len,ponteID);
        }
    }

    private class Origem {    // |<->|   |
        String ponteID="";
        Socket socket=null;
        OutputStreamCustom os=null;
        Destino destino=null;
        int port0;
        
        private Origem(Socket credencialSocket,String ponteID) {            
            socket=credencialSocket;
            this.ponteID=ponteID;
        }
        private void referencia(Destino destino) {
            this.destino=destino;
        }

        private void start() throws Exception {
            // start destino
            destino.start();
            // start origem
            int len=0;
            byte[] buffer = new byte[2048];            
            InputStream is=null;
            os=null;
            BufferedInputStream bis=null;                            
            is=socket.getInputStream();                            //  | ->|   |
            os=new OutputStreamCustom(socket.getOutputStream());   //  |<- |   |         
            bis=new BufferedInputStream(is);                            
            while( (len=bis.read(buffer)) != -1 )
                destino.ida(buffer,len,ponteID);
            try{ bis.close(); }catch(Exception e){}
            try{ is.close(); }catch(Exception e){}
        }

        private void volta(int len,byte[] buffer) throws Exception { // |<- |   |
            os.write(OutputStreamCustom.VOLTA,buffer,0,len,ponteID);
        }

        private void destroy() {
            try{                
                socket.close();
            }catch(Exception e){}
        }
    }

    public static class OutputStreamCustom{ 
        public static int IDA=1;   //  |   | ->|
        public static int VOLTA=2; //  |<- |   |

        private OutputStream os=null;
        private OutputStreamCustom(OutputStream os) {
            this.os=os;
        }
        
        public void write(int sentido_, byte[] buffer, int off, int len, String ponteID) throws IOException {
            // implementar aqui o controle geral
            //  if (sentido_ == IDA)     |   | ->|
            //  if (sentido_ == VOLTA)   |<- |   |
            os.write(buffer, off, len);
            mostra_(sentido_,buffer,len,ponteID);
        }

        private void mostra_(int sentido_, byte[] buffer, int len, String ponteID){
            if (sentido_ == IDA){
                if (displayIda)
                    mostra("->",buffer,len,ponteID);
                if (displaySimple)
                    System.out.println("1 "+ponteID+" "+cleanTextContent(new String(buffer,0,len)));
            }
            if (sentido_ == VOLTA){
                if (displayVolta)
                    mostra("<-",buffer,len,ponteID);
                if (displaySimple)
                    System.out.println("2 "+ponteID+" "+cleanTextContent(new String(buffer,0,len))); 
            }
        }

        private void mostra(String direcao, byte[] buffer, int len, String ponteID) {
            // INT
            System.out.println(
                direcao+"(id "+ponteID+" int):"
                +getInts(buffer,len)
            );

            // STR
            for (String parte : new String(buffer,0,len).split("\n") )                
                System.out.println(direcao+"(id "+ponteID+" str):"+parte);            
        }

        private String cleanTextContent(String text) 
        {
            // strips off all non-ASCII characters
            text = text.replaceAll("[^\\x00-\\x7F]", " ");
            // erases all the ASCII control characters
            text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ");
            // removes non-printable characters from Unicode
            text = text.replaceAll("\\p{C}", " ");
            return text.replace("$"," ").trim();
        }
        
        private String getInts(byte[] buffer,int len) {            
            int count=0;
            StringBuilder sb=new StringBuilder();
            for (byte b : buffer){
                sb.append(Y.OD_BC_R[Y.byte_to_int_java(b,false)]);
                if ( ++count >= len )
                    break;
            }
            return sb.toString();
        }

        private String getHexs(byte[] buffer,int len) {            
            int count=0;
            StringBuilder sb=new StringBuilder();
            for (byte b : buffer){
                sb.append(String.format("%02X",Y.byte_to_int_java(b,false)));
                if ( ++count >= len )
                    break;
            }
            return sb.toString();
        }

    }

    class Ambiente {
        ServerSocket serverSocket=null;
        private Ambiente(String host0,int port0) throws Exception {
            // exemplo host0 -> "192.168.0.100"
            if ( host0 == null || host0.equals("localhost") ){
                try{
                    host0=InetAddress.getLocalHost().getHostName();
                }catch(Exception e){
                    System.out.println("warning: procurando ip ...");
                    host0=getListaIPs().get(0);
                    System.out.println("warning: ip localizado -> "+host0);
                }                    
            }
            
            serverSocket = new ServerSocket(port0, 1,InetAddress.getByName(host0));
        }
        private Socket getCredencialSocket() throws Exception {
            return serverSocket.accept();
        }

    }

    public static ArrayList<String> getListaIPs()
    {     
        ArrayList<String> lista=new ArrayList<String>();
        ArrayList<String> lista2=new ArrayList<String>();

        try {
            java.util.Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                java.net.NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;
                java.util.Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if ( addr.getHostAddress().startsWith("192.168.0.") || addr.getHostAddress().startsWith("192.168.1.") )
                        lista.add(addr.getHostAddress());                       
                    else
                        lista2.add(addr.getHostAddress());                       
                }
            }
        } catch (java.net.SocketException e) {
            throw new RuntimeException(e);
        } 
        lista.addAll(lista2);        
        return lista;
    }
    
    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}  

class XML extends Util{
    public static String linhasExcel="0123456789";    
    public static int linhasExcel_len=linhasExcel.length();    
    public static String colunasExcel="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static int colunasExcel_len=colunasExcel.length();    
    public static String sepCSV=";";
    
    private static void resetControleTags(){
        controleTags_pilha=new ArrayList<String>();
        controleTags_tags=new ArrayList<String>();
        controleTags_nivelTail=-1;
        pivo_t=null;
        pivo_r=null;
        pivo_txt=null;
    }
    
    private static ArrayList<String> controleTags_pilha=new ArrayList<String>();
    private static ArrayList<String> controleTags_tags=new ArrayList<String>();
    private static int controleTags_nivelTail=-1;
    private static String pivo_t=null;
    private static String pivo_r=null;
    private static String pivo_s=null;
    private static String pivo_txt=null;
    public static ArrayList<String> shared=null;
    private static void controleTags(String txt,int nivel,boolean exportSheetCSV){
        nivel=nivel-1;// lista iniciada com 0
        pivo_txt=null;
        
        if ( nivel == controleTags_nivelTail+1 ){
            if ( ! txt.trim().startsWith("<") ){
                // pivo_txt
                if ( exportSheetCSV 
                    && controleTags_pilha.size() == 5
                    && controleTags_pilha.get(0).equals("worksheet")
                    && controleTags_pilha.get(1).equals("sheetData")
                    && controleTags_pilha.get(2).equals("row")
                    && controleTags_pilha.get(3).equals("c")
                    && controleTags_pilha.get(4).equals("v")
                ){
                    pivo_txt=txt;                    
                }
                return;
            }
            if ( txt.trim().startsWith("</") ){
                erroFatal(54);
            }
            controleTags_pilha.add(getTagFromLine(txt));
            controleTags_nivelTail=nivel;
            controleTagsUpdate();
            // pivo_t/pivo_r/pivo_r
            if ( exportSheetCSV 
                && controleTags_pilha.size() == 4
                && controleTags_pilha.get(0).equals("worksheet")
                && controleTags_pilha.get(1).equals("sheetData")
                && controleTags_pilha.get(2).equals("row")
                && controleTags_pilha.get(3).equals("c")                
            ){                
                pivo_t=null;
                pivo_r=null;
                pivo_s=null;
                HashMap att=getAtributosFromLine(txt);
                if (att.containsKey("t"))
                    pivo_t=att.get("t").toString();
                if (att.containsKey("r"))
                    pivo_r=att.get("r").toString();
                if (att.containsKey("s"))
                    pivo_s=att.get("s").toString();
            }            
            return;
        }
        if ( nivel == controleTags_nivelTail ){
            if ( ! txt.trim().startsWith("<") ) return;
            if ( txt.trim().startsWith("</") ) return;
            // remove a ultima posicao
            controleTags_pilha.remove(controleTags_pilha.size()-1); 
            controleTags_pilha.add(getTagFromLine(txt));
            controleTags_nivelTail=nivel;
            controleTagsUpdate();
            // pivo_t/pivo_r/pivo_s
            if ( exportSheetCSV 
                && controleTags_pilha.size() == 4
                && controleTags_pilha.get(0).equals("worksheet")
                && controleTags_pilha.get(1).equals("sheetData")
                && controleTags_pilha.get(2).equals("row")
                && controleTags_pilha.get(3).equals("c")                
            ){                
                pivo_t=null;
                pivo_r=null;
                pivo_s=null;
                HashMap att=getAtributosFromLine(txt);
                if (att.containsKey("t"))
                    pivo_t=att.get("t").toString();
                if (att.containsKey("r"))
                    pivo_r=att.get("r").toString();
                if (att.containsKey("s"))
                    pivo_s=att.get("s").toString();
            }                        
            return;
        }

        if ( nivel == controleTags_nivelTail-1 ){
            if ( ! txt.trim().startsWith("<") )
                return;                
            if ( txt.trim().startsWith("</") ){
                controleTags_pilha.remove(controleTags_pilha.size()-1); 
                controleTags_nivelTail=nivel;
                return;
            }
            // remove as duas ultimas posicoes
            controleTags_pilha.remove(controleTags_pilha.size()-1);
            controleTags_pilha.remove(controleTags_pilha.size()-1); 
            controleTags_pilha.add(getTagFromLine(txt));
            controleTags_nivelTail=nivel;
            controleTagsUpdate();
            return;
        }
    }
    
    private static void controleTagsUpdate(){
        String tag="";
        for ( int j=0;j<controleTags_pilha.size();j++ )
            tag+="/"+controleTags_pilha.get(j);
        boolean achou=false;
        for ( int j=0;j<controleTags_tags.size();j++ ){
            if ( controleTags_tags.get(j).equals(tag) ){
                achou=true;
                break;
            }
        }
        if ( ! achou )
            controleTags_tags.add(tag);
    }

    // faz arredondamento e tira notação cientifica    
    // de   5.8000000000000003E-2
    // para 0.058
    // de 0.99409999999999998
    // para 0.9941
    private static String arredondamentoNumber(String txt) {        
        
        boolean analise=false;
        if ( txt.equals("6.7100000000000007E-2") ) // 0.060
            analise=true;
        
        
        int flutuacao=0;
        String tmp="";
        String a="";
        String b="";
        boolean achou=false;
        
        if ( txt.contains("E") ){
            tmp=txt.split("E")[1];
            txt=txt.split("E")[0];
            flutuacao=Integer.parseInt(tmp);
        }
        
        a=txt.split("\\.")[0];
        b=txt.split("\\.")[1];
        
        if ( b.length() >= 15 ){
            // arredondamento ...999X
            // de 0.99409999999999998
            // para 0.9941
            if ( 
                b.substring(b.length()-2,b.length()-1).equals("9") 
                && b.substring(b.length()-3,b.length()-2).equals("9") 
                && b.substring(b.length()-4,b.length()-3).equals("9") 
            ){
                for( int i=b.length()-5;i>=0;i-- ){
                    if ( ! b.substring(i,i+1).equals("9") ){
                        b=b.substring(0,i)+(Integer.parseInt(b.substring(i,i+1))+1);
                        achou=true;
                        break;
                    }
                }
                if ( ! achou ){
                    b="0";
                    a=(Integer.parseInt(a)+1)+"";
                }
            }else{
                // convert 058000000000000003 em 058
                if ( 
                    b.substring(b.length()-2,b.length()-1).equals("0") 
                    && b.substring(b.length()-3,b.length()-2).equals("0") 
                    && b.substring(b.length()-4,b.length()-3).equals("0") 
                ){
                    for( int i=b.length()-5;i>=0;i-- ){
                        if ( ! b.substring(i,i+1).equals("0") ){
                            b=b.substring(0,i)+b.substring(i,i+1);
                            achou=true;
                            break;
                        }
                    }
                }
                if ( ! achou ){
                    b="0";
                }
            }
        }
                
        while(flutuacao>0){
            if ( b.length() == 0 ){
                a+="0";
            }else{
                a+=b.substring(0,1);
                b=b.substring(1);
            }
            flutuacao--;
        }
        while(flutuacao<0){
            b=a+b;
            a="0";
            flutuacao++;
        }    
        if ( b.equals("0") )
            return a;
        return a+"."+b;
    }
    
    private HashMap atributos=new HashMap();
    private String value=null;
    private ArrayList<XML> filhos=new ArrayList<XML>();
    private String tag=null;    
    
        
    public static ArrayList<String> listaTxt=null;
    public static ArrayList<Integer> listaNivel=null;
    public static void loadIs(InputStream is,boolean mostraEstrutura,boolean mostraTags,String caminho, boolean exportSheetCSV,OutputStream out, boolean suprimeHeader) throws Exception {
        resetLista();
        
        Y.readLine(is,"UTF-8",">");        
        StringBuilder sb=new StringBuilder();
        String txt=null;
        boolean first=true;
        
        String entrada=null;
        String tail=null;
        sb=new StringBuilder();
        int len=0;
        int nivel=1;
        boolean tag_in=false;
        boolean tag_finish=false;
        boolean tag_value=false;
        boolean tail_tag_abertura=false; //
        
        if ( exportSheetCSV )
            processaCelulaInit();
        
        if ( mostraEstrutura && caminho != null )
            System.out.println("\n=> "+caminho);        
        
        resetControleTags();
        
        while ( (txt=Y.readLine()) != null ){                        
            txt=txt.replace("\n","")+">";
            len=txt.length();
            

            if ( first ){
                first=false;
                int detectBOM=txt.indexOf("<?xml");
                if ( detectBOM > -1 && detectBOM <= 3 )
                    continue;
            }

            for( int i=0;i<len;i++ ){
                entrada=txt.substring(i,i+1);
                
                if ( nivel < 1 ){                    
                    erroFatal(1);
                }     
                if ( tag_in && tag_value ){
                    erroFatal(2);
                }
                if ( tail == null ){
                    tail=entrada;
                    continue;
                }
                if( !tag_in && !tag_value && tail.equals("<") && entrada.equals("/") ){ // tag de fechamento
                    sb.append(tail);
                    sb.append(entrada);
                    tail=null;
                    tag_in=true;
                    tag_finish=true;
                    nivel--;
                    continue;
                }
                if( !tag_in && !tag_value && tail.equals("<") ){ // tag de abertura ou tag unica
                    sb.append(tail);                
                    tail=entrada;
                    tag_in=true;                
                    continue;
                }
                if ( tag_in && tail.equals("/") && entrada.equals(">") ){ // tag unica            
                    if ( tag_finish ){ // nao pode haver finish com tag unica
                        erroFatal(3);
                    }
                    sb.append(tail);
                    sb.append(entrada);
                    tail=null;
                    tag_in=false;
                    tag_finish=false; // segurança
                    addLista(sb.toString(),nivel,mostraEstrutura,exportSheetCSV,out,suprimeHeader);
                    sb=new StringBuilder();
                    tail_tag_abertura=false;
                    continue;                
                }
                if ( tag_in && entrada.equals(">") ){ // tag abertura ou tag fechamento
                    sb.append(tail);
                    sb.append(entrada);
                    tail=null;
                    tag_in=false;                
                    addLista(sb.toString(),nivel,mostraEstrutura,exportSheetCSV,out,suprimeHeader);
                    sb=new StringBuilder();
                    if ( tag_finish ){
                        //nivel--; foi decrementado em outro local
                        tail_tag_abertura=false;
                    }else{
                        nivel++;
                        tail_tag_abertura=true;
                    }
                    tag_finish=false;
                    continue;                
                }
                if ( !tag_in && !tag_value && entrada.equals("<") ){ // inicio e fim de value bem rapido ex: ...>5<...
                    if ( ! tail_tag_abertura ){
                        // nao eh possivel iniciar value
                        tail=entrada;
                        continue;
                    }
                    tag_value=true;                
                    sb.append(tail);                    
                    tail=entrada;
                    addLista(sb.toString(),nivel,mostraEstrutura,exportSheetCSV,out,suprimeHeader);
                    sb=new StringBuilder();
                    tag_value=false;
                    tail_tag_abertura=false;
                    continue;
                }
                if ( !tag_in && !tag_value ){ // iniciando value
                    if ( ! tail_tag_abertura ){
                        // nao eh possivel iniciar value
                        tail=entrada;
                        continue;
                    }                
                    tag_value=true;                
                    sb.append(tail);                    
                    tail=entrada;
                    continue;
                }
                if( tag_value && entrada.equals("<") ){ // finalizando processamento de value
                    tag_value=false;
                    sb.append(tail);                    
                    tail=entrada;
                    addLista(sb.toString(),nivel,mostraEstrutura,exportSheetCSV,out,suprimeHeader);
                    sb=new StringBuilder();
                    continue;            
                }
                sb.append(tail);                    
                tail=entrada;
                continue;            
            }            
        }
        Y.closeLine();
        
        if ( exportSheetCSV )
            processaCelulaFlush(out);
        
        if ( tail != null ){
            erroFatal(4);
        }        
        if ( mostraTags )
            for (int i=0;i<controleTags_tags.size();i++ )            
                System.out.println(controleTags_tags.get(i));
    }
        
    private static void resetLista() {
        listaTxt=new ArrayList<String>();
        listaNivel=new ArrayList<Integer>();
    }
    
    private static void addLista(String txt, int nivel, boolean mostraEstrutura, boolean exportSheetCSV,OutputStream out, boolean suprimeHeader) throws Exception{
        if ( mostraEstrutura ){
            if ( ! txt.trim().equals("") ){
                for (int j=0;j<nivel-1;j++ )
                    System.out.print("\t");
                System.out.println(txt);
            }
        }
        controleTags(txt.trim(),nivel,exportSheetCSV);
        
        if ( exportSheetCSV ){
            if ( pivo_txt != null ){
                if ( pivo_t != null && pivo_t.equals("s") ){
                    processaCelula(
                        pivo_r
                        ,shared.get(Integer.parseInt(pivo_txt))
                        ,out
                        ,suprimeHeader
                    );
                }else{
                    if ( pivo_s != null && pivo_s.equals("2") && ( pivo_txt.contains("E") || ( txt.contains(".") && txt.split("\\.")[1].length() >= 15 ) ) ){ 
                        processaCelula(
                            pivo_r
                            ,arredondamentoNumber(pivo_txt)
                            ,out
                            ,suprimeHeader
                        );
                    }else{
                        processaCelula(
                            pivo_r
                            ,pivo_txt
                            ,out
                            ,suprimeHeader
                        );
                    }
                }
            }
        }else{
            // processamento cache
            listaTxt.add(txt.trim());
            listaNivel.add(nivel);        
        }
    }
    
    private static StringBuilder processaCelula_sb=new StringBuilder();
    private static int processaCelula_tail_linha=-1;
    private static int processaCelula_tail_coluna=-1;
    private static int processaCelula_max_tail_coluna=-1;
    private static int countWrited=0;
    private static void processaCelulaInit(){
        processaCelula_sb=new StringBuilder();
        processaCelula_tail_linha=-1;
        processaCelula_tail_coluna=-1;
        processaCelula_max_tail_coluna=-1;
        countWrited=0;
    }
    
    private static void processaCelula(String localCelula, String valor, OutputStream out, boolean suprimeHeader) throws Exception {
        //public static String linhasExcel="0123456789";    
        //public static String colunasExcel="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int linha=0;
        int linha_exp=0;
        int coluna=0;
        int coluna_exp=0;    
        
        int len=localCelula.length();
        String entrada="";
        int pos=0;
        
        if ( valor == null )
            valor="";
        valor=valor.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("\"","\"\"");
        
        for ( int i=len-1;i>=0;i-- ){ // obs: no excel a primeira linha é 1. A primeira coluna é A(aqui representada com 0)
            entrada=localCelula.substring(i,i+1);
            pos=linhasExcel.indexOf(entrada);
            if ( pos != -1 ){ // linha
                linha+=Math.pow(linhasExcel_len, linha_exp++)*pos;
            }else{ // coluna
                pos=colunasExcel.indexOf(entrada);
                if ( pos != -1 ){ 
                    coluna+=Math.pow(colunasExcel_len, coluna_exp++)*pos;
                }else{
                    erroFatal(15);
                }
            }
        }

        if ( processaCelula_tail_linha == -1 ){
            processaCelula_tail_linha=linha;
            processaCelula_tail_coluna=-1;
            if ( processaCelula_tail_coluna > processaCelula_max_tail_coluna )
                processaCelula_max_tail_coluna=coluna;        
        }else{
            if ( processaCelula_tail_linha > linha ){
                erroFatal(14);
            }
            while(processaCelula_tail_linha < linha){
                while(processaCelula_tail_coluna<processaCelula_max_tail_coluna){
                    processaCelula_sb.append("\"\"");
                    processaCelula_sb.append(sepCSV);
                    processaCelula_tail_coluna++;
                }
                processaCelula_tail_coluna=-1;
                countWrited++;
                if ( suprimeHeader && countWrited == 1 ){
                    // suprime
                }else{
                    out.write(processaCelula_sb.toString().getBytes());
                    out.write("\n".getBytes());
                }
                processaCelula_sb=new StringBuilder();
                processaCelula_tail_linha++;
            }
        }
        while(processaCelula_tail_coluna<coluna-1){
            processaCelula_sb.append("\"\"");
            processaCelula_sb.append(sepCSV);
            processaCelula_tail_coluna++;
        }
        
        processaCelula_sb.append("\"");
        processaCelula_sb.append(valor);
        processaCelula_sb.append("\"");
        processaCelula_sb.append(sepCSV);
        
        processaCelula_tail_linha=linha;
        processaCelula_tail_coluna=coluna;
        if ( processaCelula_tail_coluna > processaCelula_max_tail_coluna ){
            processaCelula_max_tail_coluna=coluna;        
        }
    }

    private static void processaCelulaFlush(OutputStream out) throws Exception{
        if ( processaCelula_sb.length() > 0 ){
            while(processaCelula_tail_coluna<processaCelula_max_tail_coluna){
                processaCelula_sb.append("\"\"");
                processaCelula_sb.append(sepCSV);
                processaCelula_tail_coluna++;
            }
            out.write(processaCelula_sb.toString().getBytes());
            out.write("\n".getBytes());
        }
    }
    
    public static XML getXML(){
        return ((ArrayList<XML>)getXML(1,0,listaTxt.size()-1)).get(0);
    }    
    
    public static Object getXML(int nivel,int ini,int fim){
        // retorno pode ser ArrayList<XML> ou String
        // nivel inicia em 1
        // int inicia em 0        
        String tag="";
        String tipoTag=""; // inicio fim unica
        if ( ini == fim && !listaTxt.get(ini).startsWith("<") )
            return listaTxt.get(ini);        
        ArrayList<XML> lista=new ArrayList<XML>();
        XML xml=null;        
        int inicio_n=0;
        String inicio_tag="";
        for(int i=ini;i<=fim;i++ ){
            if ( listaNivel.get(i) == nivel && xml == null ){
                if ( listaTxt.get(i).trim().equals("") )
                    continue;
                xml=new XML();
                tag=XML.getTagFromLine(listaTxt.get(i));
                tipoTag=XML.getTipoTagFromLine(listaTxt.get(i));
                if ( tipoTag.equals("fim") )
                    erroFatal(8);
                xml.addAtributoAll(XML.getAtributosFromLine(listaTxt.get(i)));
                if ( tipoTag.equals("unica") ){
                    xml.setTag(tag);
                    lista.add(xml);
                    xml=null;                    
                }else{
                    inicio_n=i;
                    inicio_tag=tag;
                    xml.setTag(tag);
                }
                continue;
            }
            if ( listaNivel.get(i) == nivel && xml != null ){
                tag=XML.getTagFromLine(listaTxt.get(i));
                tipoTag=XML.getTipoTagFromLine(listaTxt.get(i));
                if ( !tipoTag.equals("fim") )
                    erroFatal(9);                
                if ( ! tag.equals(inicio_tag) )
                    erroFatal(10);
                if ( (i-inicio_n) > 1 ){
                    Object interno=getXML(nivel+1,inicio_n+1,i-1);
                    try{
                        xml.addFilhos((ArrayList<XML>)interno);
                    }catch(Exception e){
                        xml.value=(String)interno;
                    }
                }
                lista.add(xml);
                xml=null;                
            }
        }
        return lista;
    }
    
    private static String getTagFromLine(String line) {
        String tag=line.replace("<","").replace(">","").replace("/","").split(" ")[0];
        if ( tag.length() == 0 )
            erroFatal(12);
        return tag;
    }

    private static String getTipoTagFromLine(String line) {
        String tipoTag=null;        
        if ( line.startsWith("</") )
            tipoTag="fim";
        else{            
            if ( line.endsWith("/>") )
                tipoTag="unica";
            else{
                if ( !line.startsWith("<") || !line.endsWith(">") )
                    erroFatal(11);
                tipoTag="inicio";
            }
        }
        return tipoTag;
    }

    private static HashMap getAtributosFromLine(String a) {
        ArrayList<String> partes=new ArrayList<String>();
        a=a.replace("<","").replace(">","").replace("/","");
        int len=a.length();
        boolean aspas_in=false;
        String entrada="";
        StringBuilder sb=new StringBuilder();
        HashMap retorno=new HashMap();
        
        for ( int i=0;i<len;i++ ){
            entrada=a.substring(i,i+1);
            if(entrada.equals("\""))
                aspas_in=!aspas_in;
            if ( !aspas_in && entrada.equals(" ") ){
                partes.add(sb.toString());
                sb=new StringBuilder();
                continue;
            }
            sb.append(entrada);
        }
        if ( sb.toString().length() > 0 )
            partes.add(sb.toString());
        
        String k="";
        String v="";
        for(int i=1;i<partes.size();i++){
            try{
                k=partes.get(i).split("=")[0];
                v=partes.get(i).split("=")[1].replace("\"","");
                if ( k.length() > 0 && v.length() > 0 ){
                    retorno.put(k, v);
                }
            }catch(Exception e){}
        }
        return retorno;
    }

    public void setTag(String tag){
        this.tag=tag;
    }
    
    public String getTag(){
        return tag;
    }
    
    public String getAtributo(String a){
        return (String)atributos.get(a);
    }
    
    public void addAtributoAll(HashMap a) {
        atributos.putAll(a);
    }    
    
    public List<String> getAtributosNames() {
        return new ArrayList<String>(atributos.keySet());
    }
    
    public void setValue(String value){
        this.value=value;
    }
    
    public String getValue(){
        return value;
    }
    
    public void addFilhos(ArrayList<XML> a) {
        filhos.addAll(a);
    }
    
    public ArrayList<XML> getFilhos(){
        return filhos;
    }
      
}


/* class AES */ // echo TXT | openssl aes-256-cbc -base64 -pass pass:SENHA -md md5 -e
/* class AES */ // y echo PPP | openssl aes-256-cbc -md md5 -k SENHA -e | y base64
/* class AES */ // ===> ATENCAO, só é compativel com o openssl com o parametro -md md5
/* class AES */ // creditos: https://github.com/chmduquesne/minibackup/blob/master/samples/OpensslAES.java
/* class AES */ // new AES().encrypt(bytes,password);
/* class AES */ // new AES().decrypt(bytes,password);
/* class AES */ class AES{ byte [] deriveKeyAndIV(byte[] password, String md, byte[] salt) throws Exception{        if ( md == null || md.equals("") ) md="MD5"; byte[] res = new byte[48]; final java.security.MessageDigest md5 = java.security.MessageDigest.getInstance(md); md5.update(password); md5.update(salt); byte[] hash1 = md5.digest(); md5.reset(); md5.update(hash1); md5.update(password); md5.update(salt); byte[] hash2 = md5.digest(); md5.reset(); md5.update(hash2); md5.update(password); md5.update(salt); byte[] hash3 = md5.digest(); if ( md == null || md.equals("MD5")){ System.arraycopy(hash1, 0, res, 0, 16); System.arraycopy(hash2, 0, res, 16, 16); System.arraycopy(hash3, 0, res, 32, 16); }else{ System.arraycopy(hash1, 0, res, 0, 32); System.arraycopy(hash2, 0, res, 32, 16); } return res; } public void encrypt(java.io.InputStream pipe_in, java.io.OutputStream pipe_out, String senha, String md, byte[] salt) throws Exception { byte[] salt_ = new byte[8]; java.security.SecureRandom sr = new java.security.SecureRandom(); sr.nextBytes(salt_); if ( salt==null ) salt=salt_; byte[] keyAndIV = deriveKeyAndIV(senha.getBytes(),md,salt); byte[] key = java.util.Arrays.copyOfRange(keyAndIV, 0, 32); byte[] iv = java.util.Arrays.copyOfRange(keyAndIV, 32, 48); javax.crypto.spec.SecretKeySpec skeySpec = new javax.crypto.spec.SecretKeySpec(key, "AES"); javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv); javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding"); cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, skeySpec, ivspec); int BUFFER_SIZE=1024; byte[] buff=new byte[BUFFER_SIZE]; int len=0; pipe_out.write("Salted__".getBytes()); pipe_out.write(salt); while ( (len=pipe_in.read(buff,0,BUFFER_SIZE)) > 0 ) pipe_out.write( cipher.update(buff,0,len) ); pipe_out.write(cipher.doFinal()); pipe_out.flush();        } public byte[] encrypt(byte[] data, String senha, String md, byte[] salt) throws Exception{ java.io.ByteArrayInputStream bais=new java.io.ByteArrayInputStream(data); java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream(); encrypt(bais,baos,senha,md,salt); return baos.toByteArray(); } public void decrypt(java.io.InputStream pipe_in,java.io.OutputStream pipe_out,String senha,String md) throws Exception { int p=0; p=pipe_in.read(new byte[8]); if ( p != 8 ){ System.err.println("Erro fatal 0!"); System.exit(1); } byte[] salt=new byte[8]; p=pipe_in.read(salt); if ( p != 8 ){ System.err.println("Erro fatal 0!"); System.exit(1); }        byte[] keyAndIV=deriveKeyAndIV(senha.getBytes(),md,salt); byte[] key=java.util.Arrays.copyOfRange(keyAndIV, 0, 32); byte[] iv=java.util.Arrays.copyOfRange(keyAndIV, 32, 48); javax.crypto.spec.SecretKeySpec skeySpec = new javax.crypto.spec.SecretKeySpec(key, "AES"); javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv); javax.crypto.Cipher cipher; cipher=javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding"); cipher.init(javax.crypto.Cipher.DECRYPT_MODE, skeySpec, ivspec); int BUFFER_SIZE=1024; byte[] buff=new byte[BUFFER_SIZE]; int len=0; while ( (len=pipe_in.read(buff,0,BUFFER_SIZE)) > 0 ) pipe_out.write( cipher.update(buff,0,len) ); pipe_out.write(cipher.doFinal()); pipe_out.flush(); } public byte[] decrypt(byte[] data, String senha, String md) throws Exception{ java.io.ByteArrayInputStream bais=new java.io.ByteArrayInputStream(data); java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream(); decrypt(bais,baos,senha,md); return baos.toByteArray(); } private static String bytesToHex(byte[] a){ StringBuilder sb = new StringBuilder(); for (byte b : a) { sb.append(String.format("%02X", b)); } return sb.toString(); } private static byte[] hexTobytes(String s) { int len = s.length(); byte[] data = new byte[len / 2]; for (int i = 0; i < len; i += 2) { data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16)); } return data;}}


/* class Utilonsole */ // String senha=Utilonsole.getPasswordConsole("Digite a senha: ");
/* class Utilonsole */ // String texto=Utilonsole.getTextConsole("Digite o texto: ");
/* class Utilonsole */ class Utilonsole{ public static String getPasswordConsole(String txt) { String retorno=""; java.io.Console console=System.console(); if ( console == null ){ System.out.println("Error, input nao suportado nesse ambiente, rodando no netbeans?..."); System.exit(1); } char [] passChar = System.console().readPassword(txt); if ( passChar != null ) retorno=new String(passChar); if ( retorno == null ){ System.out.println("Error, not input found"); System.exit(1); } return retorno;}public static String getTextConsole(String txt) { String retorno=""; java.io.Console console=System.console(); if ( console == null ){ System.out.println("Error, input nao suportado nesse ambiente, rodando no netbeans?..."); System.exit(1); } System.out.print(txt);retorno=System.console().readLine();if ( retorno == null ){ System.out.println("Error, not input found"); System.exit(1); } return retorno;}}


/* class JSchCustom */ class JSchCustom { void scpFrom(String[] args, String senha, int port) { ScpFrom.custom(args, senha, port); } void scpTo(String[] args, String senha, int port) { ScpTo.custom(args, senha, port); } void execSsh(String[] args, String senha, int port) { ExecSsh.custom(args, senha, port); } void ssh(String[] args, String senha, int port) { Ssh.custom(args, senha, port); } void sftp(String[] args, String senha, int port) { Sftp.custom(args, senha, port); } } class ScpFrom { public static void custom(String[] arg, String senha, int port) { if (arg.length != 2 || !arg[0].contains("@")) { System.err.println("usage: y scp user,pass@remotehost:file1 file2"); System.exit(-1); } FileOutputStream fos = null; try { String user = arg[0].substring(0, arg[0].indexOf('@')); arg[0] = arg[0].substring(arg[0].indexOf('@') + 1); String host = arg[0].substring(0, arg[0].indexOf(':'));  String rfile = arg[0].substring(arg[0].indexOf(':') + 1); String lfile = arg[1]; String prefix = null; if (new File(lfile).isDirectory()) { prefix = lfile + File.separator; } JSch jsch = new JSch(); Session session = jsch.getSession(user, host, port); UserInfo ui = new MyUserInfo(senha); session.setUserInfo(ui); session.connect(); String command = "scp -f " + rfile; Channel channel = session.openChannel("exec"); ((ChannelExec) channel).setCommand(command); OutputStream out = channel.getOutputStream(); InputStream in = channel.getInputStream(); channel.connect(); byte[] buf = new byte[1024]; buf[0] = 0; out.write(buf, 0, 1); out.flush(); while (true) { int c = checkAck( in ); if (c != 'C') { break; }  in.read(buf, 0, 5); long filesize = 0L; while (true) { if ( in .read(buf, 0, 1) < 0) { break; } if (buf[0] == ' ') break; filesize = filesize * 10L + (long)(buf[0] - '0'); } String file = null; for (int i = 0;; i++) { in .read(buf, i, 1); if (buf[i] == (byte) 0x0a) { file = new String(buf, 0, i); break; } } buf[0] = 0; out.write(buf, 0, 1); out.flush(); fos = new FileOutputStream(prefix == null ? lfile : prefix + file); int foo; while (true) { if (buf.length < filesize) foo = buf.length; else foo = (int) filesize; foo = in .read(buf, 0, foo); if (foo < 0) { break; } fos.write(buf, 0, foo); filesize -= foo; if (filesize == 0L) break; } fos.close(); fos = null; if (checkAck( in ) != 0) { System.exit(0); } buf[0] = 0; out.write(buf, 0, 1); out.flush(); } session.disconnect(); System.exit(0); } catch (Exception e) { System.out.println(e); try { if (fos != null) fos.close(); } catch (Exception ee) {} } } static int checkAck(InputStream in ) throws IOException { int b = in .read(); if (b == 0) return b; if (b == -1) return b; if (b == 1 || b == 2) { StringBuffer sb = new StringBuffer(); int c; do { c = in .read(); sb.append((char) c); } while (c != '\n'); if (b == 1) { System.out.print(sb.toString()); } if (b == 2) { System.out.print(sb.toString()); } } return b; } public static class MyUserInfo implements UserInfo, UIKeyboardInteractive { String passwd; String senha; private MyUserInfo(String senha) { this.senha = senha; } public String getPassword() { return passwd; } public boolean promptYesNo(String str) { return true; } JTextField passwordField = (JTextField) new JPasswordField(20); public String getPassphrase() { return null; } public boolean promptPassphrase(String message) { return true; } public boolean promptPassword(String message) { passwd = senha; return true; } public void showMessage(String message) { System.err.println("nao implementado! cod 1"); System.exit(1); } 
/* class JSchCustom */ final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0); private Container panel; public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) { return null; } } } class ScpTo { public static void custom(String[] arg, String senha, int port) { if (arg.length != 2 || !arg[1].contains("@")) { System.err.println("usage: y scp file1 user,pass@remotehost:file2"); System.exit(-1); } FileInputStream fis = null; try { String lfile = arg[0]; String user = arg[1].substring(0, arg[1].indexOf('@')); arg[1] = arg[1].substring(arg[1].indexOf('@') + 1); String host = arg[1].substring(0, arg[1].indexOf(':')); String rfile = arg[1].substring(arg[1].indexOf(':') + 1); JSch jsch = new JSch(); Session session = jsch.getSession(user, host, 22); UserInfo ui = new MyUserInfo(senha); session.setUserInfo(ui); session.connect(); boolean ptimestamp = true; String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rfile; Channel channel = session.openChannel("exec"); ((ChannelExec) channel).setCommand(command); OutputStream out = channel.getOutputStream(); InputStream in = channel.getInputStream(); channel.connect(); if (checkAck( in ) != 0) { System.exit(0); } File _lfile = new File(lfile); if (ptimestamp) { command = "T" + (_lfile.lastModified() / 1000) + " 0"; command += (" " + (_lfile.lastModified() / 1000) + " 0\n"); out.write(command.getBytes()); out.flush(); if (checkAck( in ) != 0) { System.exit(0); } } long filesize = _lfile.length(); command = "C0644 " + filesize + " "; if (lfile.lastIndexOf('/') > 0) { command += lfile.substring(lfile.lastIndexOf('/') + 1); } else { command += lfile; } command += "\n"; out.write(command.getBytes()); out.flush(); if (checkAck( in ) != 0) { System.exit(0); } fis = new FileInputStream(lfile); byte[] buf = new byte[1024]; while (true) { int len = fis.read(buf, 0, buf.length); if (len <= 0) break; out.write(buf, 0, len); } fis.close(); fis = null; buf[0] = 0; out.write(buf, 0, 1); out.flush(); if (checkAck( in ) != 0) { System.exit(0); } out.close(); channel.disconnect(); session.disconnect(); System.exit(0); } catch (Exception e) { System.out.println(e); try { if (fis != null) fis.close(); } catch (Exception ee) {} } } static int checkAck(InputStream in ) throws IOException { int b = in .read(); if (b == 0) return b; if (b == -1) return b; if (b == 1 || b == 2) { StringBuffer sb = new StringBuffer(); int c; do { c = in .read(); sb.append((char) c); } while (c != '\n'); if (b == 1) { System.out.print(sb.toString()); } if (b == 2) { System.out.print(sb.toString()); } } return b; } public static class MyUserInfo implements UserInfo, UIKeyboardInteractive { String passwd; String senha; private MyUserInfo(String senha) { this.senha = senha; } public String getPassword() { return passwd; } public boolean promptYesNo(String str) { return true; } JTextField passwordField = (JTextField) new JPasswordField(20); public String getPassphrase() { return null; } public boolean promptPassphrase(String message) { return true; } public boolean promptPassword(String message) { passwd = senha; return true; } public void showMessage(String message) { System.err.println("nao implementado! cod 3"); System.exit(1); } final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0); 
/* class JSchCustom */ private Container panel; public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) { return null; } } } class ExecSsh { public static void custom(String[] arg, String senha, int port) { try { JSch jsch = new JSch(); if (arg.length != 2 || !arg[0].contains("@")) { System.err.println("usage: y execSsh user,pass@remotehost command"); System.exit(-1); } String user = arg[0].split("@")[0]; String host = arg[0].split("@")[1]; String command = arg[1]; Session session = jsch.getSession(user, host, port); UserInfo ui = new MyUserInfo(senha); session.setUserInfo(ui); session.connect(); Channel channel = session.openChannel("exec"); ((ChannelExec) channel).setCommand(command); channel.setInputStream(null); ((ChannelExec) channel).setErrStream(System.err); InputStream in = channel.getInputStream(); channel.connect(); byte[] tmp = new byte[1024]; while (true) { while ( in .available() > 0) { int i = in .read(tmp, 0, 1024); if (i < 0) break; System.out.print(new String(tmp, 0, i)); } if (channel.isClosed()) { if ( in .available() > 0) continue; break; } try { Thread.sleep(1000); } catch (Exception ee) {} } channel.disconnect(); session.disconnect(); } catch (Exception e) { System.out.println(e); } } public static class MyUserInfo implements UserInfo, UIKeyboardInteractive { String passwd; String senha; private MyUserInfo(String senha) { this.senha = senha; } public String getPassword() { return passwd; } public boolean promptYesNo(String str) { return true; } JTextField passwordField = (JTextField) new JPasswordField(20); public String getPassphrase() { return null; } public boolean promptPassphrase(String message) { return true; } public boolean promptPassword(String message) { passwd = senha; return true; } public void showMessage(String message) { System.err.println("nao implementado! cod 5"); System.exit(1); } final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0); private Container panel; public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) { return null; } } } class Ssh { public static void custom(String[] arg, String senha, int port) { Channel channel = null; try { JSch jsch = new JSch(); if (arg.length != 1 || !arg[0].contains("@")) { System.err.println("usage: y ssh user,pass@remotehost"); System.exit(-1); } String user = arg[0].split("@")[0]; String host = arg[0].split("@")[1]; Session session = jsch.getSession(user, host, port); session.setPassword(senha); UserInfo ui = new MyUserInfo() { public void showMessage(String message) { JOptionPane.showMessageDialog(null, message); } public boolean promptYesNo(String message) { return true; } }; session.setUserInfo(ui); session.connect(30000); channel = session.openChannel("shell"); channel.setInputStream(System.in); channel.setOutputStream(System.out); channel.connect(3 * 1000); } catch (Exception e) { System.out.println(e); } while (channel != null && !channel.isEOF()) {} } public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive { public String getPassword() { return null; } public boolean promptYesNo(String str) { return false; } public String getPassphrase() { return null; } public boolean promptPassphrase(String message) { return false; } 
/* class JSchCustom */ public boolean promptPassword(String message) { return false; } public void showMessage(String message) {} public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) { return null; } } } class Sftp { public static void custom(String[] arg, String senha, int port) { try { JSch jsch = new JSch(); if (arg.length != 1 || !arg[0].contains("@")) { System.err.println("usage: y sftp user,pass@remotehost"); System.err.println("usage: y sftp user,pass@remotehost 22"); System.exit(-1); } String user = arg[0].split("@")[0]; String host = arg[0].split("@")[1]; Session session = jsch.getSession(user, host, port); UserInfo ui = new MyUserInfo(senha); session.setUserInfo(ui); session.connect(); Channel channel = session.openChannel("sftp"); channel.connect(); ChannelSftp c = (ChannelSftp) channel; java.io.InputStream in = System.in; java.io.PrintStream out = System.out; java.util.Vector cmds = new java.util.Vector(); byte[] buf = new byte[1024]; int i; String str; int level = 0; while (true) { out.print("sftp> "); cmds.removeAllElements(); i = in .read(buf, 0, 1024); if (i <= 0) break; i--; if (i > 0 && buf[i - 1] == 0x0d) i--; int s = 0; for (int ii = 0; ii < i; ii++) { if (buf[ii] == ' ') { if (ii - s > 0) { cmds.addElement(new String(buf, s, ii - s)); } while (ii < i) { if (buf[ii] != ' ') break; ii++; } s = ii; } } if (s < i) { cmds.addElement(new String(buf, s, i - s)); } if (cmds.size() == 0) continue; String cmd = (String) cmds.elementAt(0); if (cmd.equals("quit")) { c.quit(); break; } if (cmd.equals("exit")) { c.exit(); break; } if (cmd.equals("rekey")) { session.rekey(); continue; } if (cmd.equals("compression")) { if (cmds.size() < 2) { out.println("compression level: " + level); continue; } try { level = Integer.parseInt((String) cmds.elementAt(1)); if (level == 0) { session.setConfig("compression.s2c", "none"); session.setConfig("compression.c2s", "none"); } else { session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none"); session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none"); } } catch (Exception e) {} session.rekey(); continue; } if (cmd.equals("cd") || cmd.equals("lcd")) { if (cmds.size() < 2) continue; String path = (String) cmds.elementAt(1); try { if (cmd.equals("cd")) c.cd(path); else c.lcd(path); } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("rm") || cmd.equals("rmdir") || cmd.equals("mkdir")) { if (cmds.size() < 2) continue; String path = (String) cmds.elementAt(1); try { if (cmd.equals("rm")) c.rm(path); else if (cmd.equals("rmdir")) c.rmdir(path); else c.mkdir(path); } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("chgrp") || cmd.equals("chown") || cmd.equals("chmod")) { if (cmds.size() != 3) continue; String path = (String) cmds.elementAt(2); int foo = 0; if (cmd.equals("chmod")) { byte[] bar = ((String) cmds.elementAt(1)).getBytes(); int k; for (int j = 0; j < bar.length; j++) { k = bar[j]; if (k < '0' || k > '7') { foo = -1; break; } foo <<= 3; foo |= (k - '0'); } if (foo == -1) continue; } else { try { foo = Integer.parseInt((String) cmds.elementAt(1)); } catch (Exception e) { continue; } } try { if (cmd.equals("chgrp")) { c.chgrp(foo, path); } else if (cmd.equals("chown")) { c.chown(foo, path); } else if (cmd.equals("chmod")) { c.chmod(foo, path); } } catch (SftpException e) { System.out.println(e.toString()); } continue; } 
/* class JSchCustom */ if (cmd.equals("pwd") || cmd.equals("lpwd")) { str = (cmd.equals("pwd") ? "Remote" : "Local"); str += " working directory: "; if (cmd.equals("pwd")) str += c.pwd(); else str += c.lpwd(); out.println(str); continue; } if (cmd.equals("ls") || cmd.equals("dir")) { String path = "."; if (cmds.size() == 2) path = (String) cmds.elementAt(1); try { java.util.Vector vv = c.ls(path); if (vv != null) { for (int ii = 0; ii < vv.size(); ii++) { Object obj = vv.elementAt(ii); if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) { out.println(((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getLongname()); } } } } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("lls") || cmd.equals("ldir")) { String path = "."; if (cmds.size() == 2) path = (String) cmds.elementAt(1); try { java.io.File file = new java.io.File(path); if (!file.exists()) { out.println(path + ": No such file or directory"); continue; } if (file.isDirectory()) { String[] list = file.list(); for (int ii = 0; ii < list.length; ii++) { out.println(list[ii]); } continue; } out.println(path); } catch (Exception e) { System.out.println(e); } continue; } if (cmd.equals("get") || cmd.equals("get-resume") || cmd.equals("get-append") || cmd.equals("put") || cmd.equals("put-resume") || cmd.equals("put-append")) { if (cmds.size() != 2 && cmds.size() != 3) continue; String p1 = (String) cmds.elementAt(1); String p2 = "."; if (cmds.size() == 3) p2 = (String) cmds.elementAt(2); try { SftpProgressMonitor monitor = new MyProgressMonitor(); if (cmd.startsWith("get")) { int mode = ChannelSftp.OVERWRITE; if (cmd.equals("get-resume")) { mode = ChannelSftp.RESUME; } else if (cmd.equals("get-append")) { mode = ChannelSftp.APPEND; } c.get(p1, p2, monitor, mode); } else { int mode = ChannelSftp.OVERWRITE; if (cmd.equals("put-resume")) { mode = ChannelSftp.RESUME; } else if (cmd.equals("put-append")) { mode = ChannelSftp.APPEND; } c.put(p1, p2, monitor, mode); } } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("ln") || cmd.equals("symlink") || cmd.equals("rename") || cmd.equals("hardlink")) { if (cmds.size() != 3) continue; String p1 = (String) cmds.elementAt(1); String p2 = (String) cmds.elementAt(2); try { if (cmd.equals("hardlink")) { c.hardlink(p1, p2); } else if (cmd.equals("rename")) c.rename(p1, p2); else c.symlink(p1, p2); } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("df")) { if (cmds.size() > 2) continue; String p1 = cmds.size() == 1 ? "." : (String) cmds.elementAt(1); SftpStatVFS stat = c.statVFS(p1); long size = stat.getSize(); long used = stat.getUsed(); long avail = stat.getAvailForNonRoot(); long root_avail = stat.getAvail(); long capacity = stat.getCapacity(); System.out.println("Size: " + size); System.out.println("Used: " + used); System.out.println("Avail: " + avail); System.out.println("(root): " + root_avail); System.out.println("%Capacity: " + capacity); continue; } if (cmd.equals("stat") || cmd.equals("lstat")) { if (cmds.size() != 2) continue; String p1 = (String) cmds.elementAt(1); SftpATTRS attrs = null; try { if (cmd.equals("stat")) attrs = c.stat(p1); else attrs = c.lstat(p1); } catch (SftpException e) { System.out.println(e.toString()); } if (attrs != null) { out.println(attrs); } else {} continue; } if (cmd.equals("readlink")) { if (cmds.size() != 2) continue; String p1 = (String) cmds.elementAt(1); String filename = null; 
/* class JSchCustom */ try { filename = c.readlink(p1); out.println(filename); } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("realpath")) { if (cmds.size() != 2) continue; String p1 = (String) cmds.elementAt(1); String filename = null; try { filename = c.realpath(p1); out.println(filename); } catch (SftpException e) { System.out.println(e.toString()); } continue; } if (cmd.equals("version")) { out.println("SFTP protocol version " + c.version()); continue; } if (cmd.equals("help") || cmd.equals("?")) { out.println(help); continue; } out.println("unimplemented command: " + cmd); } session.disconnect(); } catch (Exception e) { System.out.println(e); } System.exit(0); } private static String help = "      Available commands:\n" + "      * means unimplemented command.\n" + "cd path                       Change remote directory to 'path'\n" + "lcd path                      Change local directory to 'path'\n" + "chgrp grp path                Change group of file 'path' to 'grp'\n" + "chmod mode path               Change permissions of file 'path' to 'mode'\n" + "chown own path                Change owner of file 'path' to 'own'\n" + "df [path]                     Display statistics for current directory or\n" + "                              filesystem containing 'path'\n" + "help                          Display this help text\n" + "get remote-path [local-path]  Download file\n" + "get-resume remote-path [local-path]  Resume to download file.\n" + "get-append remote-path [local-path]  Append remote file to local file\n" + "hardlink oldpath newpath      Hardlink remote file\n" + "*lls [ls-options [path]]      Display local directory listing\n" + "ln oldpath newpath            Symlink remote file\n" + "*lmkdir path                  Create local directory\n" + "lpwd                          Print local working directory\n" + "ls [path]                     Display remote directory listing\n" + "*lumask umask                 Set local umask to 'umask'\n" + "mkdir path                    Create remote directory\n" + "put local-path [remote-path]  Upload file\n" + "put-resume local-path [remote-path]  Resume to upload file\n" + "put-append local-path [remote-path]  Append local file to remote file.\n" + "pwd                           Display remote working directory\n" + "stat path                     Display info about path\n" + "exit                          Quit sftp\n" + "quit                          Quit sftp\n" + "rename oldpath newpath        Rename remote file\n" + "rmdir path                    Remove remote directory\n" + "rm path                       Delete remote file\n" + "symlink oldpath newpath       Symlink remote file\n" + "readlink path                 Check the target of a symbolic link\n" + "realpath path                 Canonicalize the path\n" + "rekey                         Key re-exchanging\n" + "compression level             Packet compression will be enabled\n" + "version                       Show SFTP version\n" + "?                             Synonym for help"; public static class MyProgressMonitor implements SftpProgressMonitor { ProgressMonitor monitor; long count = 0; long max = 0; public void init(int op, String src, String dest, long max) { this.max = max; monitor = new ProgressMonitor(null, ((op == SftpProgressMonitor.PUT) ? "put" : "get") + ": " + src, "", 0, (int) max); count = 0; percent = -1; monitor.setProgress((int) this.count); 
/* class JSchCustom */ monitor.setMillisToDecideToPopup(1000); } private long percent = -1; public boolean count(long count) { this.count += count; if (percent >= this.count * 100 / max) { return true; } percent = this.count * 100 / max; monitor.setNote("Completed " + this.count + "(" + percent + "%) out of " + max + "."); monitor.setProgress((int) this.count); return !(monitor.isCanceled()); } public void end() { monitor.close(); } } public static class MyUserInfo implements UserInfo, UIKeyboardInteractive { String passwd; String senha; private MyUserInfo(String senha) { this.senha = senha; } public String getPassword() { return passwd; } public boolean promptYesNo(String str) { return true; } JTextField passwordField = (JTextField) new JPasswordField(20); public String getPassphrase() { return null; } public boolean promptPassphrase(String message) { return true; } public boolean promptPassword(String message) { passwd = senha; return true; } public void showMessage(String message) { System.err.println("nao implementado! cod 7"); System.exit(1); } final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0); private Container panel; public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) { return null; } } } 

/* class texto_longo */ class texto_longo{
/* class texto_longo */     public static String get_html_virtual_playlist(){
/* class texto_longo */         String faixas="";
/* class texto_longo */         File [] f=new File(".").listFiles();
/* class texto_longo */         for ( int i=0;i<f.length;i++ ){
/* class texto_longo */             if ( f[i].isFile() && ! f[i].getName().endsWith(".bat") && ! f[i].getName().endsWith(".cfg") ){
/* class texto_longo */                 faixas += "<tr><td style=\"display: inline-block; cursor: pointer; color: white;width: 800px;\" onclick=\"click_faixa(this,'humanClick')\">\n" + f[i].getName() + "</td></tr>\n";
/* class texto_longo */                 File f2=new File(f[i].getName()+".cfg");
/* class texto_longo */                 if ( f2.exists() && f2.isFile() ){
/* class texto_longo */                     String [] partes=Util.lendo_arquivo(f[i].getName()+".cfg").split("\n");
/* class texto_longo */                     for ( int j=0;j<partes.length;j++ ){
/* class texto_longo */                         faixas += "<tr><td style=\"display: inline-block; cursor: pointer; color: white;width: 800px;\" onclick=\"click_faixa(this,'humanClick')\">\n" + partes[j] + "</td></tr>\n";
/* class texto_longo */                     }
/* class texto_longo */                 }
/* class texto_longo */             }
/* class texto_longo */         };
/* class texto_longo */         return "<html>\n" +
/* class texto_longo */         "<head>\n" +
/* class texto_longo */         "<body onload=\"preparacao();\">\n" +
/* class texto_longo */         "<meta charset=\"utf-8\">\n" +
/* class texto_longo */         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
/* class texto_longo */         "<div id=\"master\"></div>\n" +
/* class texto_longo */         "<a style=\"color: #777; font-size: 8px;\">&nbsp;&nbsp;click no corpo da pagina para pausar/despausar</a></br></br>\n" +
/* class texto_longo */         "<div><style>.bordered {border: solid #177 3px;border-radius: 6px;}.bordered tr:hover {background: #999;}.bordered td, .bordered th {border-left: 2px solid #177;border-top: 2px solid #177;padding: 10px;}audio::-webkit-media-controls-panel{background-color: #777;}</style>\n" +
/* class texto_longo */         "<table id=\"tablebase\" class=\"bordered\" style=\"font-family:Verdana,sans-serif;font-size:10px;border-spacing: 0;\"><tbody>\n" +
/* class texto_longo */         faixas +
/* class texto_longo */         "</tbody></table></div>\n" +
/* class texto_longo */         "<script type=\"text/javascript\">\n" +
/* class texto_longo */         "function remove_playlist(){\n" +
/* class texto_longo */         "  document.getElementById('master').innerHTML='';\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function create_playlist(){\n" +
/* class texto_longo */         "  document.getElementById('master').innerHTML=`\n" +
/* class texto_longo */         "    <audio id='p' controls='controls' preload='metadata'>\n" +
/* class texto_longo */         "	<source src='' type='audio/mp3' />\n" +
/* class texto_longo */         "	seu navegador não suporta HTML5\n" +
/* class texto_longo */         "	</audio>\n" +
/* class texto_longo */         "  `;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function pause(){\n" +
/* class texto_longo */         "  document.getElementById('p').pause();\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function play(){\n" +
/* class texto_longo */         "  var playPromise=document.getElementById('p').play();\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function vai_pro_fim_da_faixa(){\n" +
/* class texto_longo */         "  var d=document.getElementById('p').duration-1;\n" +
/* class texto_longo */         "  document.getElementById('p').currentTime=d;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function first(){\n" +
/* class texto_longo */         "  var t=document.getElementById('tablebase').children[0];\n" +
/* class texto_longo */         "  e=t.children[0].children[0];\n" +
/* class texto_longo */         "  var limit=500;\n" +
/* class texto_longo */         "  e=dir(e) || first(e);\n" +
/* class texto_longo */         "  while( limit-- > 0 && isChildrenMusicNotActive(e) )\n" +
/* class texto_longo */         "    e=dir(e) || first();\n" +
/* class texto_longo */         "  return e;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function troca_de_faixa(){  \n" +
/* class texto_longo */         "  var t=document.getElementById('tablebase').children[0];\n" +
/* class texto_longo */         "  var e=get_playing() || first();\n" +
/* class texto_longo */         "  var limit=500;\n" +
/* class texto_longo */         "  e=dir(e) || first(e);\n" +
/* class texto_longo */         "  while( limit-- > 0 && isChildrenMusicNotActive(e) )\n" +
/* class texto_longo */         "    e=dir(e) || first();\n" +
/* class texto_longo */         "  if ( limit <= 0 ){\n" +
/* class texto_longo */         "	console.log('Erro Fatal Loop.');\n" +
/* class texto_longo */         "	return;\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  click_faixa(e);\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function preparacao(){\n" +
/* class texto_longo */         "  document.body.style.backgroundColor = '#000';\n" +
/* class texto_longo */         "  create_playlist();\n" +
/* class texto_longo */         "  add_listener();\n" +
/* class texto_longo */         "  var t=document.getElementById('tablebase').children[0];\n" +
/* class texto_longo */         "  trySetParm(window.location.href);\n" +
/* class texto_longo */         "  click_faixa(t.children[0].children[0]);\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function trySetParm(url){\n" +
/* class texto_longo */         "  if ( url.indexOf('?')  > -1 && url.split('?')[1].trim().length > 0 ){\n" +
/* class texto_longo */         "    url=decodeURI(url.split('?')[1].trim())\n" +
/* class texto_longo */         "    var t=document.getElementById('tablebase').children[0];\n" +
/* class texto_longo */         "    e=t.children[0].children[0];\n" +
/* class texto_longo */         "    var limit=500;\n" +
/* class texto_longo */         "    while( limit-- > 0 && e != null){\n" +
/* class texto_longo */         "	  if ( isChildrenMusic(e) && e.innerText.trim().substring('+ 00:00:00 '.length) == url ){\n" +
/* class texto_longo */         "	    symbol_click(e);\n" +
/* class texto_longo */         "	    break;\n" +
/* class texto_longo */         "	  }\n" +
/* class texto_longo */         "	  e=dir(e);\n" +
/* class texto_longo */         "	}\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function pause_play(){\n" +
/* class texto_longo */         "  if ( document.getElementById('p').paused )\n" +
/* class texto_longo */         "    play();\n" +
/* class texto_longo */         "  else\n" +
/* class texto_longo */         "    pause();\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function add_listener(){\n" +
/* class texto_longo */         "  document.getElementById('p').onended = function(){\n" +
/* class texto_longo */         "    troca_de_faixa();\n" +
/* class texto_longo */         "  };\n" +
/* class texto_longo */         "  document.addEventListener('click', function(e) {  \n" +
/* class texto_longo */         "    if ( e.target.tagName == 'BODY' || e.target.tagName == 'DIV' ){\n" +
/* class texto_longo */         "	  pause_play();  	  \n" +
/* class texto_longo */         "	}else{\n" +
/* class texto_longo */         "	  if ( transfer_e != null ){\n" +
/* class texto_longo */         "	    click_faixa(transfer_e,true,e.x);\n" +
/* class texto_longo */         "		transfer_e=null;\n" +
/* class texto_longo */         "	  }\n" +
/* class texto_longo */         "	}\n" +
/* class texto_longo */         "  },false);  \n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function limpa_click_faixa(){\n" +
/* class texto_longo */         "  var t=document.getElementById('tablebase').children[0];\n" +
/* class texto_longo */         "  for ( var i=0;i<t.children.length;i++ )\n" +
/* class texto_longo */         "    t.children[i].children[0].style.background='';\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function isChildrenMusic(e){\n" +
/* class texto_longo */         "  return e.innerText.trim().indexOf('+') == 0 || e.innerText.indexOf('-') == 0;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function isMasterMusic(e){\n" +
/* class texto_longo */         "  return !isChildrenMusic(e);\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function isChildrenMusicActive(e){\n" +
/* class texto_longo */         "  return e.innerText.trim().indexOf('+') == 0;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function isChildrenMusicNotActive(e){\n" +
/* class texto_longo */         "  return e.innerText.trim().indexOf('-') == 0;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function esq(e){\n" +
/* class texto_longo */         "  if ( e.parentElement.previousElementSibling != null )\n" +
/* class texto_longo */         "	return e.parentElement.previousElementSibling.children[0];\n" +
/* class texto_longo */         "  return null;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function dir(e){\n" +
/* class texto_longo */         "  if ( e.parentElement.nextElementSibling != null )\n" +
/* class texto_longo */         "	return e.parentElement.nextElementSibling.children[0];\n" +
/* class texto_longo */         "  return null;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function getNameHierarchy(e){\n" +
/* class texto_longo */         "  if ( e.innerText == null )\n" +
/* class texto_longo */         "    return 'null';\n" +
/* class texto_longo */         "  if ( isChildrenMusic(e) )\n" +
/* class texto_longo */         "    return getNameHierarchy(esq(e));\n" +
/* class texto_longo */         "  return e.innerText.trim(); \n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function getFaixaChildrenToSeconds(p){\n" +
/* class texto_longo */         "  p=p.split(' ')[1].split(':');\n" +
/* class texto_longo */         "  return parseInt(p[2])+parseInt(p[1])*60+parseInt(p[0])*60*60;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function getStart(e){\n" +
/* class texto_longo */         "  if ( e.innerText.trim().split(' ')[1].length != 8 ){\n" +
/* class texto_longo */         "    console.log('Alert, formato invalido ' + e.innerText.trim().split(' ')[1] + '. exemplo 00:37:46');\n" +
/* class texto_longo */         "	return 0;\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  return getFaixaChildrenToSeconds(e.innerText.trim());\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function QtyChildrenByChildren(e){\n" +
/* class texto_longo */         "  var qty=1;\n" +
/* class texto_longo */         "  while( esq(e) != null && isChildrenMusic(esq(e)) )\n" +
/* class texto_longo */         "    e=esq(e);\n" +
/* class texto_longo */         "  while( dir(e) != null && isChildrenMusic(dir(e)) ){\n" +
/* class texto_longo */         "	qty++;\n" +
/* class texto_longo */         "    e=dir(e);\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  return qty;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function QtyChildrenActiveByChildren(e){\n" +
/* class texto_longo */         "  var qty=0;\n" +
/* class texto_longo */         "  while( esq(e) != null && isChildrenMusic(esq(e)) )\n" +
/* class texto_longo */         "    e=esq(e);\n" +
/* class texto_longo */         "  if ( isChildrenMusicActive(e) )\n" +
/* class texto_longo */         "	qty++;\n" +
/* class texto_longo */         "  while( dir(e) != null && isChildrenMusic(dir(e)) ){\n" +
/* class texto_longo */         "    e=dir(e);\n" +
/* class texto_longo */         "    if ( isChildrenMusicActive(e) )\n" +
/* class texto_longo */         "	  qty++;\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  return qty;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function setForActive(e){\n" +
/* class texto_longo */         "  e.innerText='+'+e.innerText.trim().substr(1);\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function setForNotActive(e){\n" +
/* class texto_longo */         "  e.innerText='-'+e.innerText.trim().substr(1);\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function setAllForActive(e){\n" +
/* class texto_longo */         "  while( esq(e) != null && isChildrenMusic(esq(e)) )\n" +
/* class texto_longo */         "    e=esq(e);\n" +
/* class texto_longo */         "  setForActive(e);\n" +
/* class texto_longo */         "  while( dir(e) != null && isChildrenMusic(dir(e)) ){\n" +
/* class texto_longo */         "    e=dir(e);\n" +
/* class texto_longo */         "	setForActive(e);\n" +
/* class texto_longo */         "  }	\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function setAllForNotActive(e){\n" +
/* class texto_longo */         "  while( esq(e) != null && isChildrenMusic(esq(e)) )\n" +
/* class texto_longo */         "    e=esq(e);\n" +
/* class texto_longo */         "  setForNotActive(e);\n" +
/* class texto_longo */         "  while( dir(e) != null && isChildrenMusic(dir(e)) ){\n" +
/* class texto_longo */         "    e=dir(e);\n" +
/* class texto_longo */         "	setForNotActive(e);\n" +
/* class texto_longo */         "  }		\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "var transfer_e=null;\n" +
/* class texto_longo */         "function click_faixa(e,humanClick,humanClick_x){ // click td\n" +
/* class texto_longo */         "  if ( humanClick && humanClick_x == null ){\n" +
/* class texto_longo */         "    transfer_e=e;\n" +
/* class texto_longo */         "    return;\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  if ( humanClick != null && isChildrenMusic(e) && humanClick_x <= 35 ){\n" +
/* class texto_longo */         "    symbol_click(e);\n" +
/* class texto_longo */         "	return;\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  // this is root and exists children +\n" +
/* class texto_longo */         "  if ( isMasterMusic(e) && dir(e) != null && isChildrenMusic(dir(e)) ){    \n" +
/* class texto_longo */         "    click_faixa(dir(e));\n" +
/* class texto_longo */         "	return;\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  limpa_click_faixa();\n" +
/* class texto_longo */         "  e.style.background='#999';  \n" +
/* class texto_longo */         "  document.getElementById('p').src=getNameHierarchy(e);\n" +
/* class texto_longo */         "  document.getElementById('p').currentTime=getStart(e);  \n" +
/* class texto_longo */         "  play();  \n" +
/* class texto_longo */         "  if ( humanClick == null )  \n" +
/* class texto_longo */         "    e.scrollIntoView(false);  \n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function symbol_click(e){\n" +
/* class texto_longo */         "  var qty=QtyChildrenByChildren(e);\n" +
/* class texto_longo */         "  var qtyActive=QtyChildrenActiveByChildren(e);\n" +
/* class texto_longo */         "  var qtyNotActive=qty-qtyActive;\n" +
/* class texto_longo */         "  var isActive=isChildrenMusicActive(e);\n" +
/* class texto_longo */         "  if ( isActive ){\n" +
/* class texto_longo */         "    if ( qty == 1 )\n" +
/* class texto_longo */         "  	  return;\n" +
/* class texto_longo */         "    if ( qtyActive == 1 ){\n" +
/* class texto_longo */         "  	  setAllForActive(e);\n" +
/* class texto_longo */         "  	  return;\n" +
/* class texto_longo */         "    }\n" +
/* class texto_longo */         "    if ( qtyActive == qty ){\n" +
/* class texto_longo */         "  	  setAllForNotActive(e);\n" +
/* class texto_longo */         "  	  setForActive(e);\n" +
/* class texto_longo */         "  	  return;\n" +
/* class texto_longo */         "    }\n" +
/* class texto_longo */         "  setForNotActive(e);\n" +
/* class texto_longo */         "}else{\n" +
/* class texto_longo */         "  setForActive(e);\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function tryUpdateStateChildren(){\n" +
/* class texto_longo */         "  if ( document.getElementById('p') == null || document.getElementById('p').currentTime == null )\n" +
/* class texto_longo */         "    return;\n" +
/* class texto_longo */         "  e=get_playing() || first();\n" +
/* class texto_longo */         "  while ( isChildrenMusic(e) && esq(e) != null && isChildrenMusic(esq(e)) && document.getElementById('p').currentTime < getFaixaChildrenToSeconds(e.innerText.trim()) ){\n" +
/* class texto_longo */         "	e.style.background='';  \n" +
/* class texto_longo */         "	esq(e).style.background='#999';  \n" +
/* class texto_longo */         "	e=esq(e);\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  while ( isChildrenMusic(e) && dir(e) != null && isChildrenMusic(dir(e)) && document.getElementById('p').currentTime >= getFaixaChildrenToSeconds(dir(e).innerText.trim()) ){\n" +
/* class texto_longo */         "	e.style.background='';  \n" +
/* class texto_longo */         "	dir(e).style.background='#999';  \n" +
/* class texto_longo */         "	e=dir(e);\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "  if ( isChildrenMusicNotActive(e) ){\n" +
/* class texto_longo */         "	troca_de_faixa();\n" +
/* class texto_longo */         "  }\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "function get_playing(){\n" +
/* class texto_longo */         "  var t=document.getElementById('tablebase').children[0];\n" +
/* class texto_longo */         "  for ( var i=0;i<t.children.length;i++ )\n" +
/* class texto_longo */         "    if ( t.children[i].children[0].style.background != '')\n" +
/* class texto_longo */         "	  return t.children[i].children[0];\n" +
/* class texto_longo */         "  return null;\n" +
/* class texto_longo */         "}\n" +
/* class texto_longo */         "setInterval(tryUpdateStateChildren, 100);\n" +
/* class texto_longo */         "</script>\n" +
/* class texto_longo */         "</body></head></html>\n";
/* class texto_longo */     }
/* class texto_longo */ }

/* class HttpServer */ // parametros
/* class HttpServer */ // new HttpServer(...)
/* class HttpServer */ // host(pode ser ""), titulo_url, titulo, port, dir, endsWiths(ex: "","jar,zip"), ips_banidos(ex: "","8.8.8.8,4.4.4.4")
/* class HttpServer */ class HttpServer {
/* class HttpServer */     String host, titulo_url, titulo, dir, nav, endsWiths, ips_banidos;
/* class HttpServer */     boolean index_playlist=false;
/* class HttpServer */     int port;
/* class HttpServer */     Socket socket = null;
/* class HttpServer */     public HttpServer(String[] args, boolean index_playlist) {
/* class HttpServer */         host = args[0];
/* class HttpServer */         if (args[0] == null || args[0].equals("localhost")) try {
/* class HttpServer */             host = InetAddress.getLocalHost().getHostName();
/* class HttpServer */         } catch (Exception e) {}
/* class HttpServer */         titulo_url = args[1];
/* class HttpServer */         titulo = args[2];
/* class HttpServer */         port = Integer.parseInt(args[3]);
/* class HttpServer */         dir = args[4].trim();
/* class HttpServer */         if (!dir.endsWith("/")) dir += "/";
/* class HttpServer */         endsWiths = args[5];
/* class HttpServer */         ips_banidos = args[6];
/* class HttpServer */         this.index_playlist = index_playlist;
/* class HttpServer */         try {
/* class HttpServer */             serve();
/* class HttpServer */         } catch (Exception e) {
/* class HttpServer */             System.err.println(e.toString());
/* class HttpServer */             System.exit(1);
/* class HttpServer */         }
/* class HttpServer */     }
/* class HttpServer */     public void serve() throws Exception {
/* class HttpServer */         ServerSocket serverSocket = null;
/* class HttpServer */         String origem = "";
/* class HttpServer */         try {
/* class HttpServer */             serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
/* class HttpServer */             if (host.contains(":")) System.out.println("Service opened: http://[" + host + "]:" + port + "/" + titulo_url);
/* class HttpServer */             else System.out.println("Service opened: http://" + host + ":" + port + "/" + titulo_url);
/* class HttpServer */             System.out.println("path work: " + dir);
/* class HttpServer */         } catch (Exception e) {
/* class HttpServer */             throw new Exception("erro na inicialização: " + e.toString());
/* class HttpServer */         }
/* class HttpServer */         while (true) {
/* class HttpServer */             try {
/* class HttpServer */                 socket = serverSocket.accept();
/* class HttpServer */                 origem = socket.getRemoteSocketAddress().toString();
/* class HttpServer */                 if (origem.length() > 2 && origem.startsWith("/")) origem = origem.substring(1);
/* class HttpServer */                 if (origem.indexOf(":") != -1) origem = origem.substring(0, origem.indexOf(":"));
/* class HttpServer */                 System.out.println("Conexao de origem: " + origem + ", data:" + (new Date()));
/* class HttpServer */                 if (ips_banidos.length() > 0 && ("," + ips_banidos + ",").contains("," + origem + ",")) {
/* class HttpServer */                     System.out.println("Acesso recusado para o ip banido: " + origem);
/* class HttpServer */                     continue;
/* class HttpServer */                 }
/* class HttpServer */                 new ClientThread(socket, titulo_url, titulo, dir, endsWiths, index_playlist);
/* class HttpServer */             } catch (Exception e) {
/* class HttpServer */                 System.out.println("Erro ao executar servidor:" + e.toString());
/* class HttpServer */             }
/* class HttpServer */         }
/* class HttpServer */     }
/* class HttpServer */ }
/* class HttpServer */ class ClientThread {
/* class HttpServer */     String method, uri, protocol, titulo_url, titulo, dir, endsWiths;
/* class HttpServer */     long range=-1;
/* class HttpServer */     String nav;
/* class HttpServer */     InputStream input = null;
/* class HttpServer */     OutputStream output = null;
/* class HttpServer */     char[] buffer = new char[2048];
/* class HttpServer */     Writer writer;
/* class HttpServer */     InputStreamReader isr = null;
/* class HttpServer */     Reader reader;
/* class HttpServer */     boolean index_playlist=false;
/* class HttpServer */     public ClientThread(final Socket socket, String titulo_url, String titulo, String dir, String endsWiths, boolean index_playlist) {
/* class HttpServer */         this.titulo_url = titulo_url;
/* class HttpServer */         this.titulo = titulo;
/* class HttpServer */         this.dir = dir;
/* class HttpServer */         this.endsWiths = endsWiths;
/* class HttpServer */         this.index_playlist = index_playlist;
/* class HttpServer */         new Thread() {
/* class HttpServer */             public void run() {
/* class HttpServer */                 try {
/* class HttpServer */                     input = socket.getInputStream();
/* class HttpServer */                     output = socket.getOutputStream();
/* class HttpServer */                     if (input != null) {
/* class HttpServer */                         isr = new InputStreamReader(input);
/* class HttpServer */                         reader = new BufferedReader(isr);
/* class HttpServer */                         writer = new StringWriter();
/* class HttpServer */                         lendo();
/* class HttpServer */                         gravando();
/* class HttpServer */                         socket.close();
/* class HttpServer */                         writer.close();
/* class HttpServer */                         reader.close();
/* class HttpServer */                         isr.close();
/* class HttpServer */                     }
/* class HttpServer */                 } catch (Exception e) {
/* class HttpServer */                     System.out.println("----------> Erro ao executar servidor:" + e.toString());
/* class HttpServer */                 }
/* class HttpServer */             }
/* class HttpServer */         }.start();
/* class HttpServer */     }
/* class HttpServer */     private void lendo() throws Exception {
/* class HttpServer */         try {
/* class HttpServer */             int i = reader.read(buffer);
/* class HttpServer */             if (i == -1) return;
/* class HttpServer */             writer.write(buffer, 0, i);
/* class HttpServer */             BufferedReader br = new BufferedReader(new StringReader(writer.toString()));
/* class HttpServer */             String line = null;
/* class HttpServer */             int lineNumber = 0;
/* class HttpServer */             this.range = -1;
/* class HttpServer */             while ((line = br.readLine()) != null) {
/* class HttpServer */                 System.out.println("<---|    " + line.replace("\n","\n          "));
/* class HttpServer */                 if (lineNumber == 0 && line.split(" ").length == 3) {
/* class HttpServer */                     this.method = line.split(" ")[0];
/* class HttpServer */                     this.uri = line.split(" ")[1];
/* class HttpServer */                     if ( this.uri.indexOf("?") > -1 )
/* class HttpServer */                         this.uri = this.uri.split("\\?")[0];
/* class HttpServer */                     this.protocol = line.split(" ")[2];
/* class HttpServer */                 }
/* class HttpServer */                 if (line.startsWith("Range: bytes=") && line.endsWith("-") )
/* class HttpServer */                   this.range = Long.parseLong(line.split("=")[1].replace("-", ""));
/* class HttpServer */                 lineNumber++;
/* class HttpServer */             }
/* class HttpServer */             System.out.println("    |");
/* class HttpServer */         } catch (IOException e) {
/* class HttpServer */             throw new Exception("Erro ao converter stream para string:" + e.toString());
/* class HttpServer */         }
/* class HttpServer */     }
/* class HttpServer */     private void gravando() throws Exception {
/* class HttpServer */         StringBuilder sb = new StringBuilder();
/* class HttpServer */         if (method.equals("OPTIONS")) {
/* class HttpServer */             for (String line: new String[] {
/* class HttpServer */                     "HTTP/1.1 501 Not Implemented\r\n" + "\r\n"
/* class HttpServer */                 }) {
/* class HttpServer */                 sb.append(line);
/* class HttpServer */                 System.out.println("    |---> " + line.replace("\n","\n          "));
/* class HttpServer */             }
/* class HttpServer */             System.out.println("    |");
/* class HttpServer */             output.write(sb.toString().getBytes());
/* class HttpServer */             return;
/* class HttpServer */         }
/* class HttpServer */         if (uri.equals("/") && nav == null && index_playlist){
/* class HttpServer */             // verifica arquivos locais
/* class HttpServer */             File [] f=new File(".").listFiles();
/* class HttpServer */             boolean existe=false;
/* class HttpServer */             for ( int i=0;i<f.length;i++ ){
/* class HttpServer */                 if ( f[i].isFile() ){
/* class HttpServer */                     existe=true;
/* class HttpServer */                     break;
/* class HttpServer */                 }
/* class HttpServer */             }
/* class HttpServer */             if ( existe ){
/* class HttpServer */                 sb = new StringBuilder();
/* class HttpServer */                 for (String line: new String[] {
/* class HttpServer */                         "HTTP/1.1 200 OK\r\n",
/* class HttpServer */                         "Content-Type: text/html; charset=UTF-8\r\n",
/* class HttpServer */                         "\r\n",
/* class HttpServer */                         texto_longo.get_html_virtual_playlist()
/* class HttpServer */                     }) {
/* class HttpServer */                     sb.append(line);
/* class HttpServer */                     System.out.println("    |---> " + line.replace("\n","\n          "));
/* class HttpServer */                 }
/* class HttpServer */                 System.out.println("    |");
/* class HttpServer */                 output.write(sb.toString().getBytes());
/* class HttpServer */                 return;
/* class HttpServer */             }else{    
/* class HttpServer */                 System.out.println("Error, nao foi possivel encontrar nenhum arquivo no diretorio atual!");
/* class HttpServer */                 return;
/* class HttpServer */             }
/* class HttpServer */         }
/* class HttpServer */         sb = new StringBuilder();
/* class HttpServer */         nav = dir + uri.replace("//", "/").trim();
/* class HttpServer */         nav = nav.replace("//", "/").replace("%20", " ");
/* class HttpServer */         if (!new File(nav).isFile()) {
/* class HttpServer */             nav += "/";
/* class HttpServer */             int c = 9;
/* class HttpServer */             while (nav.contains("//") && c-- > 0) nav = nav.replace("//", "/");
/* class HttpServer */             for (
/* class HttpServer */                 String index: new String[] {
/* class HttpServer */                     "index.html",
/* class HttpServer */                     "index.htm"
/* class HttpServer */                 }) {
/* class HttpServer */                 if (new File(nav + index).exists()) {
/* class HttpServer */                     nav += index;
/* class HttpServer */                     break;
/* class HttpServer */                 }
/* class HttpServer */             }
/* class HttpServer */         }
/* class HttpServer */         if (uri.equals("/" + titulo_url)) {
/* class HttpServer */             sb = new StringBuilder();
/* class HttpServer */             for (String line: new String[] {
/* class HttpServer */                     "HTTP/1.1 200 OK\r\n",
/* class HttpServer */                     "Content-Type: text/html; charset=UTF-8\r\n",
/* class HttpServer */                     "\r\n",
/* class HttpServer */                     "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n",
/* class HttpServer */                     "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n",
/* class HttpServer */                     "<meta charset='UTF-8' http-equiv='X-UA-Compatible' content='IE=9'>\n",
/* class HttpServer */                     "<br>\n",
/* class HttpServer */                     "&nbsp;" + titulo + "<br>\n"
/* class HttpServer */                 }) {
/* class HttpServer */                 sb.append(line);
/* class HttpServer */                 System.out.println("    |---> " + line.replace("\n","\n          "));
/* class HttpServer */             }
/* class HttpServer */             File[] files = new File(dir).listFiles();
/* class HttpServer */             Arrays.sort(files, new Comparator < File > () {
/* class HttpServer */                 public int compare(File f1, File f2) {
/* class HttpServer */                     if (f1.lastModified() < f2.lastModified()) return 1;
/* class HttpServer */                     if (f1.lastModified() > f2.lastModified()) return -1;
/* class HttpServer */                     return 0;
/* class HttpServer */                 }
/* class HttpServer */             });
/* class HttpServer */             sb.append("<style>.bordered {border: solid #ccc 3px;border-radius: 6px;}.bordered tr:hover {background: #fbf8e9;}.bordered td, .bordered th {border-left: 2px solid #ccc;border-top: 2px solid #ccc;padding: 10px;}</style>");
/* class HttpServer */             System.out.println("<style>.bordered {border: solid #ccc 3px;border-radius: 6px;}.bordered tr:hover {background: #fbf8e9;}.bordered td, .bordered th {border-left: 2px solid #ccc;border-top: 2px solid #ccc;padding: 10px;}</style>");
/* class HttpServer */             sb.append("<table id='tablebase' class='bordered' style='font-family:Verdana,sans-serif;font-size:10px;border-spacing: 0;'>");
/* class HttpServer */             System.out.println("<table id='tablebase' class='bordered' style='font-family:Verdana,sans-serif;font-size:10px;border-spacing: 0;'>");
/* class HttpServer */             for (File p: files) {
/* class HttpServer */                 if (!p.isFile()) continue;
/* class HttpServer */                 if (!endsWith_OK(p.getName(), endsWiths)) continue;
/* class HttpServer */                 sb.append("<tr><td>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(p.lastModified())).toString() + "</td><td>" + "<a href='" + p.getName() + "'>" + p.getName() + "</a></td></tr>\n");
/* class HttpServer */                 System.out.println("<tr><td>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(p.lastModified())).toString() + "</td><td>" + "<a href='" + p.getName() + "'>" + p.getName() + "</a></td></tr>\n");
/* class HttpServer */             }
/* class HttpServer */             sb.append("</table></html>");
/* class HttpServer */             System.out.println("</table></html>");
/* class HttpServer */             System.out.println("    |");
/* class HttpServer */             output.write(sb.toString().getBytes());
/* class HttpServer */             return;
/* class HttpServer */         }
/* class HttpServer */         if ( ! uri.equals("/favicon.ico"))
/* class HttpServer */             System.out.println("nav: " + nav + ";uri: " + uri);
/* class HttpServer */         if (new File(nav).exists() && new File(nav).isFile() && endsWith_OK(nav, endsWiths)) {
/* class HttpServer */             long lenFile = -1;
/* class HttpServer */             if ( range > -1 ){
/* class HttpServer */                 lenFile = new File(nav).length();
/* class HttpServer */                 if ( range >= lenFile)
/* class HttpServer */                     range = -1;
/* class HttpServer */             }
/* class HttpServer */             if ( range > -1){
/* class HttpServer */                 String rangeFormat = "accept-ranges: bytes\r\nContent-Length: " + lenFile + "\r\nContent-Range: bytes " + range + "-" + (lenFile-1) + "/" + lenFile + "\r\n";
/* class HttpServer */                 for (String line: new String[] {
/* class HttpServer */                         "HTTP/1.1 206 OK\r\n" + "Content-Type: " + getContentType(nav) + "\r\ncharset=UTF-8\r\n" + rangeFormat + "\r\n"
/* class HttpServer */                     }) {
/* class HttpServer */                     sb.append(line);
/* class HttpServer */                     System.out.println("    |---> " + line.replace("\n","\n          "));
/* class HttpServer */                 }
/* class HttpServer */             }else{  
/* class HttpServer */                 for (String line: new String[] {
/* class HttpServer */                         "HTTP/1.1 200 OK\r\n" + "Content-Type: " + getContentType(nav) + "\r\ncharset=UTF-8\r\n" + "\r\n"
/* class HttpServer */                     }) {
/* class HttpServer */                     sb.append(line);
/* class HttpServer */                     System.out.println("    |---> " + line.replace("\n","\n          "));
/* class HttpServer */                 }
/* class HttpServer */             }    
/* class HttpServer */             System.out.println("    |");
/* class HttpServer */             output.write(sb.toString().getBytes());
/* class HttpServer */             try {
/* class HttpServer */                 System.out.println("iniciando leitura do arquivo: " + nav);
/* class HttpServer */                 transf_bytes(output, nav, range);
/* class HttpServer */                 System.out.println("finalizando leitura do arquivo: " + nav);
/* class HttpServer */                 return;
/* class HttpServer */             } catch (Exception e) {
/* class HttpServer */                 if ( e.toString().contains("Software caused connection abort: socket write error") ){}else{
/* class HttpServer */                     System.out.println("erro 404, não foi possivel ler o arquivo: " + nav);
/* class HttpServer */                 }
/* class HttpServer */                 return;
/* class HttpServer */             }
/* class HttpServer */         } else {
/* class HttpServer */             if (uri.equals("/favicon.ico")) {
/* class HttpServer */                 return;
/* class HttpServer */             }else{
/* class HttpServer */                 System.out.println("nao encontrou o arquivo: " + nav);
/* class HttpServer */             }
/* class HttpServer */         } /* ERROR 404 */
/* class HttpServer */         sb = new StringBuilder();
/* class HttpServer */         for (String line: new String[] {
/* class HttpServer */                 "HTTP/1.1 200 OK\r\n",
/* class HttpServer */                 "Content-Type: text/html; charset=UTF-8\r\n",
/* class HttpServer */                 "\r\n",
/* class HttpServer */                 "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"/>\n" + "<title>404 - File or directory not found.</title>\n" + "<style type=\"text/css\">\n" + "<!--\n" + "body{margin:0;font-size:.7em;font-family:Verdana, Arial, Helvetica, sans-serif;background:#EEEEEE;}\n" + "fieldset{padding:0 15px 10px 15px;} \n" +
/* class HttpServer */                 "h1{font-size:2.4em;margin:0;color:#FFF;}\n" + "h2{font-size:1.7em;margin:0;color:#CC0000;} \n" + "h3{font-size:1.2em;margin:10px 0 0 0;color:#000000;} \n" + "#header{width:96%;margin:0 0 0 0;padding:6px 2% 6px 2%;font-family:\"trebuchet MS\", Verdana, sans-serif;color:#FFF;\n" + "background-color:#555555;}\n" + "#content{margin:0 0 0 2%;position:relative;}\n" + ".content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;}\n" + "-->\n" + "</style>\n" + "</head>\n" + "<body>\n" + "<div id=\"header\"><h1>Server Error</h1></div>\n" + "<div id=\"content\">\n" + " <div class=\"content-container\"><fieldset>\n" + "  <h2>404 - File or directory not found.</h2>\n" + "  <h3>The resource you are looking for might have been removed, had its name changed, or is temporarily unavailable.</h3>\n" + " </fieldset></div>\n" + "</div>\n" + "</body>\n" + "</html>"
/* class HttpServer */             }) {
/* class HttpServer */             sb.append(line);
/* class HttpServer */             System.out.println("    |---> " + line.replace("\n","\n          "));
/* class HttpServer */         }
/* class HttpServer */         System.out.println("    |");
/* class HttpServer */         output.write(sb.toString().getBytes());
/* class HttpServer */     }
/* class HttpServer */     private String getContentType(String caminho) {
/* class HttpServer */         if (caminho.endsWith(".html") || caminho.endsWith(".htm")) return "text/html";
/* class HttpServer */         if (caminho.endsWith(".css")) return "text/css";
/* class HttpServer */         if (caminho.endsWith(".png") || caminho.endsWith(".ico") || caminho.endsWith(".jpg")) return "image/png";
/* class HttpServer */         if (caminho.endsWith(".mkv")) return "video/webm";
/* class HttpServer */         return "application/octet-stream";
/* class HttpServer */     }
/* class HttpServer */     public ArrayList < String > lendo_arquivo_display(String caminho) throws Exception {
/* class HttpServer */         ArrayList < String > result = new ArrayList < String > ();
/* class HttpServer */         String strLine;
/* class HttpServer */         try {
/* class HttpServer */             FileReader rf = new FileReader(caminho);
/* class HttpServer */             BufferedReader in = new BufferedReader(rf);
/* class HttpServer */             while ((strLine = in .readLine()) != null) result.add(strLine); in .close();
/* class HttpServer */             rf.close();
/* class HttpServer */         } catch (Exception e) {
/* class HttpServer */             throw new Exception("nao foi possivel encontrar o arquivo " + caminho);
/* class HttpServer */         }
/* class HttpServer */         return result;
/* class HttpServer */     }
/* class HttpServer */     private void transf_bytes(OutputStream output, String nav, long resume) throws Exception {
/* class HttpServer */         int count;
/* class HttpServer */         DataInputStream dis = new DataInputStream(new FileInputStream(nav));
/* class HttpServer */         byte[] buffer = new byte[8192];
/* class HttpServer */         if ( resume > 0 ) 
/* class HttpServer */             dis.skip(resume);
/* class HttpServer */         while ((count = dis.read(buffer)) > 0) output.write(buffer, 0, count);
/* class HttpServer */     }
/* class HttpServer */     private boolean endsWith_OK(String url, String ends) {
/* class HttpServer */         if (ends.equals("")) return true;
/* class HttpServer */         String[] partes = ends.split(",");
/* class HttpServer */         for (int i = 0; i < partes.length; i++)
/* class HttpServer */             if (url.endsWith("." + partes[i])) return true;
/* class HttpServer */         return false;
/* class HttpServer */     }
/* class HttpServer */ }

/* class Wget */ //String [] args2 = {"-h"};               
/* class Wget */ //String [] args2 = {"-ban","%d0","-only_before","-list_mp3","-list_diretories","http://195.122.253.112/public/mp3/"};        
/* class Wget */ //String [] args2 = {"-list_files_and_diretories","http://www.dcbasso.rsn86.com/arquivos/Musicas/"};    
/* class Wget */ //String [] args2 = {"-accep_escape_host_mp3","-list_files_and_diretories","http://openwebindex.com/mp3/music/"};    
/* class Wget */ //String [] args2 = {"-accep_escape_host_mp3","-list_mp3","-list_diretories","http://www.brain-magazine.com"};    
/* class Wget */ //String [] args2 = {"-only_before","-list_mp3","-list_diretories","http://moransa.com/music/"};        
/* class Wget */ //String [] args2 = {"-r","http://www.blesscosmetics.com.br/","-list_files","-list_diretories","-output|C:\\Users\\ywanes\\Documents"};               
/* class Wget */ //String [] args2 = {"-r","http://www.blesscosmetics.com.br/","-output|C:\\Users\\ywanes\\Documents"};               
/* class Wget */ //String [] args2 = {"-r","http://www.naosalvo.com.br/","-output|C:\\Users\\ywanes\\Documents"};               
/* class Wget */ //String [] args2 = {"http://moransa.com/music/Guns%20N'%20Roses%20-%20Appetite%20for%20Destruction/","-only_before","-output|C:\\Users\\ywanes\\Documents","-tipo|.ini|.jpg"};               
/* class Wget */ //String [] args2 = {"-list_mp3","-only_before","http://jeankulle.free.fr/ftp/sons/"};
/* class Wget */ //String [] args2 = {"-list_mp3","-only_before","http://percyvanrijn.com/music/"};        
/* class Wget */ //String [] args2 = {"-list_mp3","-only_before","https://notendur.hi.is/gvr/music/Big%20Whiskey%20and%20the%20GrooGrux%20King/"};                                
/* class Wget */ class Wget {  public int cont; public boolean list_mp3=false; public String motor="";  Hashtable hashtable = new Hashtable(); int hash_cont=0; private boolean list_files=false; private boolean list_diretorios=false; public ArrayList<String> pilha=new ArrayList<String>(); private boolean accep_escape_host_mp3=false; private boolean only_before=false; private String string_only_before=null; private boolean ban=false; private String string_ban=null; private boolean recursive=false; private String string_output_dir=""; public String sep=""; private String tipo=""; public String proxy=""; public boolean legend=false;   public String parametros [][] = { {"-output|","pasta de arquivo de saida ex:-output|c:\\user\\usuario\\Documents"}, { "http://site.com.br","site da utilizacao"}, {"-r","recursivo, download do site todo"}, {"-h","help, mostra parametros"}, {"-list_mp3","listar as mp3s"}, {"-list_files","listar os arquivos"}, {"-list_diretories","listar os diretorios"}, {"-accep_escape_host_mp3","aceitar navegação de outro host por url mp3"}, {"-tipo|.mp3|.wma","baixar somente arquivos com estas extensoes"}, { "-only_before","somente navegação da url de uma ponto pra frente"} , { "-proxy|","ex: -proxy|endereco-proxy|80"}, {"-ban","palava ou caracter banido, ex -ban %d0"}, {"-legend","colocando 1 arquivo A, 2 arquivo B ... "}, };   String parametros() { String retorno=""; for ( int i=0;i<parametros.length;i++){ retorno+=parametros[i][0]+" => "+parametros[i][1]+"\n"; } return retorno; }  void comando(String comando) { if ( comando.startsWith("-output|")){ string_output_dir=comando.split("\\|")[1]; return; } if ( comando.startsWith("-tipo|")){ tipo=comando.replace("-tipo\\|",""); return; } if ( ban && string_ban == null ){ string_ban=comando; return; } if ( comando.equals("-ban") ){ ban=true; return; } if ( comando.equals("-r") ){ recursive=true; only_before=true; return; } if ( comando.equals("-list_mp3") ){ list_mp3=true; return; } if ( comando.equals("-list_files") ){ list_files=true; return; } if ( comando.equals("-list_diretories") ){ list_diretorios=true;             return; } if ( comando.equals("-accep_escape_host_mp3") ){ accep_escape_host_mp3=true; return; } if ( comando.equals("-only_before") ){ only_before=true; return; } if ( comando.equals("-legend") ){ legend=true; return; } if ( comando.startsWith("-proxy|") ){ comando=comando.replace("-proxy|",""); if ( comando.split("\\|").length != 2 ){ throw new Error("Comando "+comando+" invalido "+comando.split("|").length+" "+comando); } proxy=comando; return; }  if ( comando.contains("-h") ){       System.out.println("Parametros:"); System.out.println(parametros()); System.exit(0); }  if ( ! comando.startsWith("-")){ fix_barra_url(comando); return; } throw new Error("Comando "+comando+" invalido"); }  public void start_motor() throws Exception{ if ( ! motor.equals("")){ cont=0; if ( recursive || ! tipo.equals("")){ if (string_output_dir.equals("")){ throw new Exception("erro, falta do parametro -output|[dir], necessario com o parametro -r ou -tipo ");                 }else{ if ( ! new File(string_output_dir).exists() ){ throw new Exception("erro, o diretorio "+string_output_dir + " nao existe.");                 }else{ grava(get_raiz(motor),"dir"); } } } motor(motor);             } }  public void motor(String url) throws Exception{ if ( url_invalida(url) ){ return; } if ( only_before ){ if (  string_only_before == null ){ string_only_before=url; 
/* class Wget */ }else{ if ( ! url.contains(string_only_before)){ return; } }             } if ( url.contains("#")){ url=fix_sharp(url); } if ( url.contains("/../") ){ url=fix_dotdot(url); }    if ( ! url.endsWith("/") && ! get_life(url).contains(".")){ url+="/"; }     if ( url_de_request(url) ){  if ( list_diretorios && url.endsWith("/")){ if ( legend ){ System.out.println("pasta                  "+url); }else{ System.out.println(url); } } String html = getcode(url);   if ( ! html.equals("")){ boolean tmp_tipo=false; if ( ! tipo.equals("")){ for ( String ext : tipo.split("\\|")){ if ( url.toLowerCase().endsWith(ext)){ tmp_tipo=true; } } } if ( recursive || tmp_tipo ){ grava(url,"file"); } for ( String parametro : gethref(html) ){    if ( ! parametro.equals("") && ! parametro.contains("?") && ! parametro.contains(" ")){ if ( ban && string_ban != null && parametro.contains(string_ban)){ }else{ motor(monta_url(url,parametro)); } } } }             }else{ if (  (list_mp3 && url.toLowerCase().endsWith(".mp3")) || list_files                     ){ if ( legend ){ System.out.println(++cont+"           arquivo    "+url); }else{ System.out.println(url); } }   boolean tmp_tipo=false; if ( ! tipo.equals("")){ for ( String ext : tipo.split("\\|")){ if ( url.toLowerCase().endsWith(ext)){ tmp_tipo=true; } } } if ( recursive || tmp_tipo ){ grava(url,"file"); }             } }  public boolean url_invalida(String url) throws Exception{ if ( url.length() > 1000){ throw new Exception("erro inesperado ! "+url); }  url=url.trim();  if ( hashtable.contains(new String(url)) ){ return true;             } hashtable.put(hash_cont++,url);  if ( url.contains(";") && ! url.contains("&amp;")){ return true; }     return false; }  public ArrayList<String> gethref(String texto){ ArrayList<String> lista=new ArrayList<String>(); Matcher m; m = Pattern.compile(" href=\"([^\"])*").matcher(texto);  while ( m.find() ){ lista.add(m.group().substring(7)); } m = Pattern.compile(" HREF=\"([^\"])*").matcher(texto);  while ( m.find() ){ lista.add(m.group().substring(7)); } m = Pattern.compile(" href='([^'])*").matcher(texto);  while ( m.find() ){ lista.add(m.group().substring(7)); } m = Pattern.compile(" HREF='([^'])*").matcher(texto);  while ( m.find() ){ lista.add(m.group().substring(7)); } m = Pattern.compile(" src='([^'])*").matcher(texto);  while ( m.find() ){ lista.add(m.group().substring(6)); } m = Pattern.compile(" SRC='([^'])*").matcher(texto);  while ( m.find() ){ lista.add(m.group().substring(6)); } return lista; }  public String getcode(String url){  String texto=""; String inputLine="";         HttpURLConnection httpcon=null; BufferedReader in=null;  try{ URLConnection con=null; if ( proxy.equals("")){ con=new URL(url).openConnection();                 }else{ con=new URL(url).openConnection( new Proxy(Proxy.Type.HTTP, new InetSocketAddress( proxy.split("\\|")[0], Integer.parseInt(proxy.split("\\|")[1]))));                 }  con.setUseCaches(false);   (httpcon = (HttpURLConnection) con).addRequestProperty("User-Agent", "Mozilla/4.76"); if ( httpcon.getResponseCode() != 503 ){ in = new BufferedReader(new InputStreamReader(httpcon.getInputStream())); while ((inputLine = in.readLine()) != null) texto+=inputLine; in.close(); return texto; } return ""; }catch (Exception e){ if ( true )return ""; }  return texto; }  public boolean mp3_realmente(String url){ String inputLine;   int cont=0;  try{ URL UrL=new URL(url); URLConnection con; con=UrL.openConnection(); HttpURLConnection httpcon = (HttpURLConnection) con;  httpcon.addRequestProperty("User-Agent", "Mozilla/6.0");  
/* class Wget */ BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream())); cont=0; while ((inputLine = in.readLine()) != null && cont < 50){ cont++; } in.close(); }catch (Exception e){} if ( cont == 50 ){ return true; } return false; }  public void fix_barra_url(String comando){         if ( ! comando.toLowerCase().startsWith("http://") && ! comando.toLowerCase().startsWith("https://") ){ comando="http://"+comando; } if ( quantidade_de_barra(comando) == 0){ comando+="/";     } motor=comando;         }  private String fix_dotdot(String url2) { boolean final_com_barra=url2.endsWith("/"); reset_pilha(); for ( String pasta : url2.split("/")){ if ( pasta.equals("..") ){ resempilha(); }else{ empilha(pasta); } } String retorno=""; for ( String pasta : pilha ){ retorno+=pasta+"/"; } if ( ! final_com_barra ){ retorno=retorno.substring(0,retorno.length()-1); } return retorno; }  private void reset_pilha() { pilha=new ArrayList<String>(); }  private void resempilha() { if ( pilha.size() > 0 ){ pilha.remove(pilha.size()-1); } }  private void empilha(String pasta) { pilha.add(pasta); }  private String tira_file_da_url(String url2) { while ( ! url2.endsWith("/")){ url2=url2.substring(0,url2.length()-1); } return url2;      }  private String fix_sharp(String incremento) { if ( incremento.contains("#") ){ boolean ignore=false; String incremento_aux=""; for ( int i=0;i<incremento.length() && ! ignore;i++){ if ( incremento.substring(i,i+1).equals("#")){ ignore=true; }else{ incremento_aux+=incremento.substring(i,i+1); }                 } return incremento_aux; }         return incremento; }  private int quantidade_de_barra(String comando) { comando=tira_http(comando); int cont=0; for ( int i=0;i<comando.length();i++){ if ( comando.substring(i,i+1).equals("/")){ cont++; } } return cont; }  private boolean url_de_request(String url) { url=url.toLowerCase(); if ( url.endsWith("/") || url.contains(".asp")  || url.contains(".php") || url.contains(".html") || url.contains(".htm") || url.contains(".apsx") ){ return true; } if ( pasta(url) ){ return true; } return false; }  private boolean pasta(String url) { url=url.replace("http://",""); String u=""; for ( String p : url.split("/")) { u=p; } if ( ! u.contains(".")){ return true; } return false; }  private String get_raiz(String url) { url=tira_http(url); return "http://"+url.split("/")[0]; }  private String tira_http(String comando) { return comando.replace("http://",""); }  private String monta_url(String url, String parametro) { if ( parametro.startsWith("http://")){                         return parametro; }else{ if ( parametro.startsWith("/")){ return get_raiz(url)+parametro; }else{ if ( ! parametro.contains(".") && ! parametro.endsWith("/")){ parametro+="/"; } if ( url.endsWith("/")){ return url+parametro; }else{ if ( ! tira_file(url).equals("") ){ return tira_file(url)+parametro; } return url+"/"+parametro; } } } }  private String tira_file(String url) { url=tira_http(url); String tmp="",retorno="http://";         for ( String pasta : url.split("/")){ if ( ! tmp.equals("")){ if ( pasta.contains(".")){ return retorno; }                     }else{ tmp=pasta; } retorno+=pasta+"/"; } return ""; }  private void grava(String conteudo,String tipo) { if ( string_output_dir.contains("\\")){ sep="\\"; }else{ sep="/"; }  if ( tipo.equals("dir")){ conteudo=string_output_dir+sep+tira_http(conteudo);         if ( ! new File(conteudo).exists() ){ new File(conteudo).mkdir(); } }else{     download(conteudo); } }  private void download(String conteudo) {                 
/* class Wget */ String path=string_output_dir+sep; String aux=""; for ( String pasta : tira_http(tira_file_da_url(conteudo)).split("/")){ path+=pasta+sep; aux=path.replace("%20"," "); if ( ! new File(aux).exists() ){ new File(aux).mkdir(); } }     String file=get_life(conteudo); if ( ! conteudo.contains("?")){ System.out.println("Salvando: "+conteudo); if ( file.equals("")){ file="index.html"; if ( ! new File(path+file).exists() ){                 String html = getcode(conteudo);             try{ aux=(path+file).replace("%20"," "); FileWriter fstream = new FileWriter(aux); BufferedWriter out = new BufferedWriter(fstream); out.write(html); out.close(); }catch (Exception e){ System.err.println("Error: " + e.getMessage()); } } }else{ aux=(path+file).replace("%20"," "); if ( ! new File(aux).exists() ){ new UrlDownload().fileDownload(conteudo,path,proxy); } } } }  private String get_life(String url) { url=tira_http(url); String tmp="",retorno="http://";         for ( String pasta : url.split("/")){ if ( ! tmp.equals("")){ if ( pasta.contains(".")){ return pasta; }                     }else{ tmp=pasta; } retorno+=pasta+"/"; } return "";         }  public static class UrlDownload { final static int size=1024; public static void fileUrl(String fAddress, String localFileName, String destinationDir,String proxy) { String aux=""; if ((new File(destinationDir+"\\"+localFileName)).exists()) { System.out.println("Arquivo " + localFileName + " ja exite."); return; }else{ OutputStream outStream = null; URLConnection  uCon = null; InputStream is = null; try { URL Url; byte[] buf; int ByteRead,ByteWritten=0;                 aux=(destinationDir+"\\"+localFileName).replace("%20"," "); outStream = new BufferedOutputStream(new FileOutputStream(aux)); if ( proxy.equals("")){ uCon=new URL(fAddress).openConnection(); }else{ uCon=new URL(fAddress).openConnection( new Proxy(Proxy.Type.HTTP, new InetSocketAddress( proxy.split("\\|")[0], Integer.parseInt(proxy.split("\\|")[1]))));                 }  is = uCon.getInputStream(); buf = new byte[size];  while ((ByteRead = is.read(buf)) != -1) { outStream.write(buf, 0, ByteRead); ByteWritten += ByteRead; } is.close(); outStream.close(); }catch (Exception e) { } } }  public static void fileDownload(String fAddress, String destinationDir,String proxy) { int slashIndex =fAddress.lastIndexOf('/'); int periodIndex =fAddress.lastIndexOf('.'); String fileName=fAddress.substring(slashIndex + 1); if (periodIndex >=1 &&  slashIndex >= 0 && slashIndex < fAddress.length()-1) { if(fileName.contains("?")){ String tmp []=fileName.split("="); fileName=tmp[0]; fileName=fileName.substring(0, fileName.length()-2); } fileUrl(fAddress,fileName,destinationDir,proxy); }else{ System.err.println("path or file name."); } } } } 


/* class Tar  */ // credits: https://github.com/kamranzafar/jtar/blob/master/src/test/java/org/kamranzafar/jtar/JTarTest.java 
/* class Tar  */ // tar("in"); 
/* class Tar  */ // tar("test.tar", "in"); 
/* class Tar  */ // untar("test.tar", null); 
/* class Tar  */ // untar("test.tar", "in/in2/only"); 
/* class Tar  */ // tarlist("test.tar"); 
/* class Tar  */ class Tar { String sep=System.getProperty("user.dir").contains("/")?"/":"\\"; public void tar(String target, String source) throws Exception{ String dir = System.getProperty("user.dir"); TarOutputStream out = null; if ( target == null ) out = new TarOutputStream(new BufferedOutputStream(System.out)); else out = new TarOutputStream(new BufferedOutputStream(new FileOutputStream(dir+sep+target))); tarRun(null, dir+sep+source, out, target == null); out.close();         } public void untar(String source, String only) throws Exception { String dir = System.getProperty("user.dir"); File zf = new File(dir+sep+source); TarInputStream tis = null; if ( source == null ) tis = new TarInputStream(new BufferedInputStream(System.in)); else tis = new TarInputStream(new BufferedInputStream(new FileInputStream(zf))); untarRun(tis, dir, only, source == null); tis.close(); } public void tarlist(String source) throws IOException { String dir = System.getProperty("user.dir"); File zf = new File(dir+sep+source); TarInputStream tis = new TarInputStream(new BufferedInputStream(new FileInputStream(zf))); TarEntry entry; while ((entry = tis.getNextEntry()) != null) System.out.println(entry.getName()); } public void tarRun(String parent, String path, TarOutputStream out, Boolean targetNull) throws Exception { final int BUFFER = 2048; BufferedInputStream origin = null; File f = new File(path); String [] files = new String[]{}; if ( f.isDirectory() ){ if ( parent == null && !targetNull ) System.out.println("a " + f.getName()); files = f.list(); }else if ( f.isFile() ) files = new String[]{f.getName()}; else throw new Exception(f.getName() + " not found!"); parent = ((parent == null) ? (f.isFile()) ? "" : f.getName() + "/" : parent + f.getName() + "/"); for (int i = 0; i < files.length; i++) { if ( !targetNull ) System.out.println("a " + parent + files[i]); File fe = f; byte data[] = new byte[BUFFER]; if (f.isDirectory()) { fe = new File(f, files[i]); } if (fe.isDirectory()) { String[] fl = fe.list(); if (fl != null && fl.length != 0) { tarRun(parent, fe.getPath(), out, targetNull); } else { TarEntry entry = new TarEntry(fe, parent + files[i] + "/"); out.putNextEntry(entry); } continue; } FileInputStream fi = new FileInputStream(fe); origin = new BufferedInputStream(fi); TarEntry entry = new TarEntry(fe, parent + files[i]); out.putNextEntry(entry); int count; while ((count = origin.read(data)) != -1) { out.write(data, 0, count); } out.flush(); origin.close(); } }  private void untarRun(TarInputStream tis, String destFolder, String only, Boolean sourceNull) throws IOException { final int BUFFER = 2048; BufferedOutputStream dest = null;  TarEntry entry; while ((entry = tis.getNextEntry()) != null) { if ( !sourceNull && only != null && ! entry.getName().equals(only) ) continue; if ( !sourceNull ) System.out.println("x " + entry.getName()); int count; byte data[] = new byte[BUFFER];  if (entry.isDirectory()) { new File(destFolder + sep + entry.getName()).mkdirs(); continue; } else { int di = entry.getName().lastIndexOf('/'); if (di != -1) { new File(destFolder + sep + entry.getName().substring(0, di)).mkdirs(); } }  FileOutputStream fos = new FileOutputStream(destFolder + sep + entry.getName()); dest = new BufferedOutputStream(fos);  while ((count = tis.read(data)) != -1) { dest.write(data, 0, count); }  dest.flush(); dest.close(); } }  }  class TarOutputStream extends OutputStream { private final OutputStream out; private long bytesWritten; private long currentFileSize; private TarEntry currentEntry;  
/* class Tar  */ public TarOutputStream(OutputStream out) { this.out = out; bytesWritten = 0; currentFileSize = 0; }  public TarOutputStream(final File fout) throws FileNotFoundException { this.out = new BufferedOutputStream(new FileOutputStream(fout)); bytesWritten = 0; currentFileSize = 0; }  public TarOutputStream(final File fout, final boolean append) throws IOException { @SuppressWarnings("resource") RandomAccessFile raf = new RandomAccessFile(fout, "rw"); final long fileSize = fout.length(); if (append && fileSize > TarConstants.EOF_BLOCK) { raf.seek(fileSize - TarConstants.EOF_BLOCK); } out = new BufferedOutputStream(new FileOutputStream(raf.getFD())); }  @Override public void close() throws IOException { closeCurrentEntry(); write( new byte[TarConstants.EOF_BLOCK] ); out.close(); }  @Override public void write(int b) throws IOException { out.write( b ); bytesWritten += 1;  if (currentEntry != null) { currentFileSize += 1; } }  @Override public void write(byte[] b, int off, int len) throws IOException { if (currentEntry != null && !currentEntry.isDirectory()) { if (currentEntry.getSize() < currentFileSize + len) { throw new IOException( "The current entry[" + currentEntry.getName() + "] size[" + currentEntry.getSize() + "] is smaller than the bytes[" + ( currentFileSize + len ) + "] being written." ); } }  out.write( b, off, len );  bytesWritten += len;  if (currentEntry != null) { currentFileSize += len; }         }  public void putNextEntry(TarEntry entry) throws IOException { closeCurrentEntry();  byte[] header = new byte[TarConstants.HEADER_BLOCK]; entry.writeEntryHeader( header );  write( header );  currentEntry = entry; }  protected void closeCurrentEntry() throws IOException { if (currentEntry != null) { if (currentEntry.getSize() > currentFileSize) { throw new IOException( "The current entry[" + currentEntry.getName() + "] of size[" + currentEntry.getSize() + "] has not been fully written." ); }  currentEntry = null; currentFileSize = 0;  pad(); } }  protected void pad() throws IOException { if (bytesWritten > 0) { int extra = (int) ( bytesWritten % TarConstants.DATA_BLOCK );  if (extra > 0) { write( new byte[TarConstants.DATA_BLOCK - extra] ); } } } }  class TarEntry { protected File file; protected TarHeader header;  private TarEntry() { this.file = null; header = new TarHeader(); }  public TarEntry(File file, String entryName) { this(); this.file = file; this.extractTarHeader(entryName); }  public TarEntry(byte[] headerBuf) { this(); this.parseTarHeader(headerBuf); }  public TarEntry(TarHeader header) { this.file = null; this.header = header; }  @Override public boolean equals(Object it) { if (!(it instanceof TarEntry)) { return false; } return header.name.toString().equals( ((TarEntry) it).header.name.toString()); }  @Override public int hashCode() { return header.name.hashCode(); }  public boolean isDescendent(TarEntry desc) { return desc.header.name.toString().startsWith(header.name.toString()); }  public TarHeader getHeader() { return header; }  public String getName() { String name = header.name.toString(); if (header.namePrefix != null && !header.namePrefix.toString().equals("")) { name = header.namePrefix.toString() + "/" + name; }  return name; }  public void setName(String name) { header.name = new StringBuffer(name); }  public int getUserId() { return header.userId; }  public void setUserId(int userId) { header.userId = userId; }  public int getGroupId() { return header.groupId; }  public void setGroupId(int groupId) { header.groupId = groupId; }  
/* class Tar  */ public String getUserName() { return header.userName.toString(); }  public void setUserName(String userName) { header.userName = new StringBuffer(userName); }  public String getGroupName() { return header.groupName.toString(); }  public void setGroupName(String groupName) { header.groupName = new StringBuffer(groupName); }  public void setIds(int userId, int groupId) { this.setUserId(userId); this.setGroupId(groupId); }  public void setModTime(long time) { header.modTime = time / 1000; }  public void setModTime(Date time) { header.modTime = time.getTime() / 1000; }  public Date getModTime() { return new Date(header.modTime * 1000); }  public File getFile() { return this.file; }  public long getSize() { return header.size; }  public void setSize(long size) { header.size = size; }  public boolean isDirectory() { if (this.file != null) return this.file.isDirectory();  if (header != null) { if (header.linkFlag == TarHeader.LF_DIR) return true;  if (header.name.toString().endsWith("/")) return true; }  return false; }  public void extractTarHeader(String entryName) { int permissions = PermissionUtils.permissions(file); header = TarHeader.createHeader(entryName, file.length(), file.lastModified() / 1000, file.isDirectory(), permissions); }  public long computeCheckSum(byte[] buf) { long sum = 0;  for (int i = 0; i < buf.length; ++i) { sum += 255 & buf[i]; }  return sum; }  public void writeEntryHeader(byte[] outbuf) { int offset = 0;  offset = TarHeader.getNameBytes(header.name, outbuf, offset, TarHeader.NAMELEN); offset = Octal.getOctalBytes(header.mode, outbuf, offset, TarHeader.MODELEN); offset = Octal.getOctalBytes(header.userId, outbuf, offset, TarHeader.UIDLEN); offset = Octal.getOctalBytes(header.groupId, outbuf, offset, TarHeader.GIDLEN);  long size = header.size;  offset = Octal.getLongOctalBytes(size, outbuf, offset, TarHeader.SIZELEN); offset = Octal.getLongOctalBytes(header.modTime, outbuf, offset, TarHeader.MODTIMELEN);  int csOffset = offset; for (int c = 0; c < TarHeader.CHKSUMLEN; ++c) outbuf[offset++] = (byte) ' ';  outbuf[offset++] = header.linkFlag;  offset = TarHeader.getNameBytes(header.linkName, outbuf, offset, TarHeader.NAMELEN); offset = TarHeader.getNameBytes(header.magic, outbuf, offset, TarHeader.USTAR_MAGICLEN); offset = TarHeader.getNameBytes(header.userName, outbuf, offset, TarHeader.USTAR_USER_NAMELEN); offset = TarHeader.getNameBytes(header.groupName, outbuf, offset, TarHeader.USTAR_GROUP_NAMELEN); offset = Octal.getOctalBytes(header.devMajor, outbuf, offset, TarHeader.USTAR_DEVLEN); offset = Octal.getOctalBytes(header.devMinor, outbuf, offset, TarHeader.USTAR_DEVLEN); offset = TarHeader.getNameBytes(header.namePrefix, outbuf, offset, TarHeader.USTAR_FILENAME_PREFIX);  for (; offset < outbuf.length;) outbuf[offset++] = 0;  long checkSum = this.computeCheckSum(outbuf);  Octal.getCheckSumOctalBytes(checkSum, outbuf, csOffset, TarHeader.CHKSUMLEN); }  public void parseTarHeader(byte[] bh) { int offset = 0;  header.name = TarHeader.parseName(bh, offset, TarHeader.NAMELEN); offset += TarHeader.NAMELEN;  header.mode = (int) Octal.parseOctal(bh, offset, TarHeader.MODELEN); offset += TarHeader.MODELEN;  header.userId = (int) Octal.parseOctal(bh, offset, TarHeader.UIDLEN); offset += TarHeader.UIDLEN;  header.groupId = (int) Octal.parseOctal(bh, offset, TarHeader.GIDLEN); offset += TarHeader.GIDLEN;  header.size = Octal.parseOctal(bh, offset, TarHeader.SIZELEN); offset += TarHeader.SIZELEN;  
/* class Tar  */ header.modTime = Octal.parseOctal(bh, offset, TarHeader.MODTIMELEN); offset += TarHeader.MODTIMELEN;  header.checkSum = (int) Octal.parseOctal(bh, offset, TarHeader.CHKSUMLEN); offset += TarHeader.CHKSUMLEN;  header.linkFlag = bh[offset++];  header.linkName = TarHeader.parseName(bh, offset, TarHeader.NAMELEN); offset += TarHeader.NAMELEN;  header.magic = TarHeader.parseName(bh, offset, TarHeader.USTAR_MAGICLEN); offset += TarHeader.USTAR_MAGICLEN;  header.userName = TarHeader.parseName(bh, offset, TarHeader.USTAR_USER_NAMELEN); offset += TarHeader.USTAR_USER_NAMELEN;  header.groupName = TarHeader.parseName(bh, offset, TarHeader.USTAR_GROUP_NAMELEN); offset += TarHeader.USTAR_GROUP_NAMELEN;  header.devMajor = (int) Octal.parseOctal(bh, offset, TarHeader.USTAR_DEVLEN); offset += TarHeader.USTAR_DEVLEN;  header.devMinor = (int) Octal.parseOctal(bh, offset, TarHeader.USTAR_DEVLEN); offset += TarHeader.USTAR_DEVLEN;  header.namePrefix = TarHeader.parseName(bh, offset, TarHeader.USTAR_FILENAME_PREFIX); } }  class TarConstants { public static final int EOF_BLOCK = 1024; public static final int DATA_BLOCK = 512; public static final int HEADER_BLOCK = 512; }  class TarHeader { public static final int NAMELEN = 100; public static final int MODELEN = 8; public static final int UIDLEN = 8; public static final int GIDLEN = 8; public static final int SIZELEN = 12; public static final int MODTIMELEN = 12; public static final int CHKSUMLEN = 8; public static final byte LF_OLDNORM = 0; public static final byte LF_NORMAL = (byte) '0'; public static final byte LF_LINK = (byte) '1'; public static final byte LF_SYMLINK = (byte) '2'; public static final byte LF_CHR = (byte) '3'; public static final byte LF_BLK = (byte) '4'; public static final byte LF_DIR = (byte) '5'; public static final byte LF_FIFO = (byte) '6'; public static final byte LF_CONTIG = (byte) '7';   public static final String USTAR_MAGIC = "ustar";  public static final int USTAR_MAGICLEN = 8; public static final int USTAR_USER_NAMELEN = 32; public static final int USTAR_GROUP_NAMELEN = 32; public static final int USTAR_DEVLEN = 8; public static final int USTAR_FILENAME_PREFIX = 155;  public StringBuffer name; public int mode; public int userId; public int groupId; public long size; public long modTime; public int checkSum; public byte linkFlag; public StringBuffer linkName; public StringBuffer magic; public StringBuffer userName; public StringBuffer groupName; public int devMajor; public int devMinor; public StringBuffer namePrefix;  public TarHeader() { this.magic = new StringBuffer(TarHeader.USTAR_MAGIC);  this.name = new StringBuffer(); this.linkName = new StringBuffer();  String user = System.getProperty("user.name", "");  if (user.length() > 31) user = user.substring(0, 31);  this.userId = 0; this.groupId = 0; this.userName = new StringBuffer(user); this.groupName = new StringBuffer(""); this.namePrefix = new StringBuffer(); }  public static StringBuffer parseName(byte[] header, int offset, int length) { StringBuffer result = new StringBuffer(length);  int end = offset + length; for (int i = offset; i < end; ++i) { if (header[i] == 0) break; result.append((char) header[i]); }  return result; }  public static int getNameBytes(StringBuffer name, byte[] buf, int offset, int length) { int i;  for (i = 0; i < length && i < name.length(); ++i) { buf[offset + i] = (byte) name.charAt(i); }  for (; i < length; ++i) { buf[offset + i] = 0; }  return offset + length; }  
/* class Tar  */ public static TarHeader createHeader(String entryName, long size, long modTime, boolean dir, int permissions) { String name = entryName; name = TarUtils.trim(name.replace(File.separatorChar, '/'), '/');  TarHeader header = new TarHeader(); header.linkName = new StringBuffer(""); header.mode = permissions;  if (name.length() > 100) { header.namePrefix = new StringBuffer(name.substring(0, name.lastIndexOf('/'))); header.name = new StringBuffer(name.substring(name.lastIndexOf('/') + 1)); } else { header.name = new StringBuffer(name); } if (dir) { header.linkFlag = TarHeader.LF_DIR; if (header.name.charAt(header.name.length() - 1) != '/') { header.name.append("/"); } header.size = 0; } else { header.linkFlag = TarHeader.LF_NORMAL; header.size = size; }  header.modTime = modTime; header.checkSum = 0; header.devMajor = 0; header.devMinor = 0;  return header; } }  class PermissionUtils { private static enum StandardFilePermission { EXECUTE(0110), WRITE(0220), READ(0440);  private int mode;  private StandardFilePermission(int mode) { this.mode = mode; } }  private static Map<PosixFilePermission, Integer> posixPermissionToInteger = new HashMap<>();  static { posixPermissionToInteger.put(PosixFilePermission.OWNER_EXECUTE, 0100); posixPermissionToInteger.put(PosixFilePermission.OWNER_WRITE, 0200); posixPermissionToInteger.put(PosixFilePermission.OWNER_READ, 0400);  posixPermissionToInteger.put(PosixFilePermission.GROUP_EXECUTE, 0010); posixPermissionToInteger.put(PosixFilePermission.GROUP_WRITE, 0020); posixPermissionToInteger.put(PosixFilePermission.GROUP_READ, 0040);  posixPermissionToInteger.put(PosixFilePermission.OTHERS_EXECUTE, 0001); posixPermissionToInteger.put(PosixFilePermission.OTHERS_WRITE, 0002); posixPermissionToInteger.put(PosixFilePermission.OTHERS_READ, 0004); }  public static int permissions(File f) { if(f == null) { throw new NullPointerException("File is null."); } if(!f.exists()) { throw new IllegalArgumentException("File " + f + " does not exist."); }  return isPosix ? posixPermissions(f) : standardPermissions(f); }  private static final boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");  private static int posixPermissions(File f) { int number = 0; try { Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(f.toPath()); for (Map.Entry<PosixFilePermission, Integer> entry : posixPermissionToInteger.entrySet()) { if (permissions.contains(entry.getKey())) { number += entry.getValue(); } } } catch (IOException e) { throw new RuntimeException(e); } return number; }  private static Set<StandardFilePermission> readStandardPermissions(File f) { Set<StandardFilePermission> permissions = new HashSet<>(); if(f.canExecute()) { permissions.add(StandardFilePermission.EXECUTE); } if(f.canWrite()) { permissions.add(StandardFilePermission.WRITE); } if(f.canRead()) { permissions.add(StandardFilePermission.READ); } return permissions; }  private static Integer standardPermissions(File f) { int number = 0; Set<StandardFilePermission> permissions = readStandardPermissions(f); for (StandardFilePermission permission : permissions) { number += permission.mode; } return number; } }  class Octal { public static long parseOctal(byte[] header, int offset, int length) { long result = 0; boolean stillPadding = true;  int end = offset + length; for (int i = offset; i < end; ++i) { if (header[i] == 0) break;  if (header[i] == (byte) ' ' || header[i] == '0') { 
/* class Tar  */ if (stillPadding) continue;  if (header[i] == (byte) ' ') break; }  stillPadding = false;  result = ( result << 3 ) + ( header[i] - '0' ); }  return result; }  public static int getOctalBytes(long value, byte[] buf, int offset, int length) { int idx = length - 1;  buf[offset + idx] = 0; --idx; buf[offset + idx] = (byte) ' '; --idx;  if (value == 0) { buf[offset + idx] = (byte) '0'; --idx; } else { for (long val = value; idx >= 0 && val > 0; --idx) { buf[offset + idx] = (byte) ( (byte) '0' + (byte) ( val & 7 ) ); val = val >> 3; } }  for (; idx >= 0; --idx) { buf[offset + idx] = (byte) '0'; }  return offset + length; } public static int getCheckSumOctalBytes(long value, byte[] buf, int offset, int length) { getOctalBytes( value, buf, offset, length ); buf[offset + length - 1] = (byte) ' '; buf[offset + length - 2] = 0; return offset + length; }  public static int getLongOctalBytes(long value, byte[] buf, int offset, int length) { byte[] temp = new byte[length + 1]; getOctalBytes( value, temp, 0, length + 1 ); System.arraycopy( temp, 0, buf, offset, length ); return offset + length; }  }  class TarUtils { public static long calculateTarSize(File path) { return tarSize(path) + TarConstants.EOF_BLOCK; }  private static long tarSize(File dir) { long size = 0;  if (dir.isFile()) { return entrySize(dir.length()); } else { File[] subFiles = dir.listFiles();  if (subFiles != null && subFiles.length > 0) { for (File file : subFiles) { if (file.isFile()) { size += entrySize(file.length()); } else { size += tarSize(file); } } } else { return TarConstants.HEADER_BLOCK; } }  return size; }  private static long entrySize(long fileSize) { long size = 0; size += TarConstants.HEADER_BLOCK; size += fileSize;  long extra = size % TarConstants.DATA_BLOCK;  if (extra > 0) { size += (TarConstants.DATA_BLOCK - extra); }  return size; }  public static String trim(String s, char c) { StringBuffer tmp = new StringBuffer(s); for (int i = 0; i < tmp.length(); i++) { if (tmp.charAt(i) != c) { break; } else { tmp.deleteCharAt(i); } }  for (int i = tmp.length() - 1; i >= 0; i--) { if (tmp.charAt(i) != c) { break; } else { tmp.deleteCharAt(i); } }  return tmp.toString(); } }  class TarInputStream extends FilterInputStream {  private static final int SKIP_BUFFER_SIZE = 2048; private TarEntry currentEntry; private long currentFileSize; private long bytesRead; private boolean defaultSkip = false;  public TarInputStream(InputStream in) { super(in); currentFileSize = 0; bytesRead = 0; }  @Override public boolean markSupported() { return false; }  @Override public synchronized void mark(int readlimit) { }  @Override public synchronized void reset() throws IOException { throw new IOException("mark/reset not supported"); }  @Override public int read() throws IOException { byte[] buf = new byte[1];  int res = this.read(buf, 0, 1);  if (res != -1) { return 0xFF & buf[0]; }  return res; }  @Override public int read(byte[] b, int off, int len) throws IOException { if (currentEntry != null) { if (currentFileSize == currentEntry.getSize()) { return -1; } else if ((currentEntry.getSize() - currentFileSize) < len) { len = (int) (currentEntry.getSize() - currentFileSize); } }  int br = super.read(b, off, len);  if (br != -1) { if (currentEntry != null) { currentFileSize += br; }  bytesRead += br; }  return br; }  public TarEntry getNextEntry() throws IOException { closeCurrentEntry();  byte[] header = new byte[TarConstants.HEADER_BLOCK]; 
/* class Tar  */ byte[] theader = new byte[TarConstants.HEADER_BLOCK]; int tr = 0;  while (tr < TarConstants.HEADER_BLOCK) { int res = read(theader, 0, TarConstants.HEADER_BLOCK - tr);  if (res < 0) { break; }  System.arraycopy(theader, 0, header, tr, res); tr += res; }  boolean eof = true; for (byte b : header) { if (b != 0) { eof = false; break; } }  if (!eof) { currentEntry = new TarEntry(header); }  return currentEntry; }  public long getCurrentOffset() { return bytesRead; }  protected void closeCurrentEntry() throws IOException { if (currentEntry != null) { if (currentEntry.getSize() > currentFileSize) { long bs = 0; while (bs < currentEntry.getSize() - currentFileSize) { long res = skip(currentEntry.getSize() - currentFileSize - bs);  if (res == 0 && currentEntry.getSize() - currentFileSize > 0) { throw new IOException("Possible tar file corruption"); }  bs += res; } }  currentEntry = null; currentFileSize = 0L; skipPad(); } }  protected void skipPad() throws IOException { if (bytesRead > 0) { int extra = (int) (bytesRead % TarConstants.DATA_BLOCK);  if (extra > 0) { long bs = 0; while (bs < TarConstants.DATA_BLOCK - extra) { long res = skip(TarConstants.DATA_BLOCK - extra - bs); bs += res; } } } }  @Override public long skip(long n) throws IOException { if (defaultSkip) { long bs = super.skip(n); bytesRead += bs;  return bs; }  if (n <= 0) { return 0; }  long left = n; byte[] sBuff = new byte[SKIP_BUFFER_SIZE];  while (left > 0) { int res = read(sBuff, 0, (int) (left < SKIP_BUFFER_SIZE ? left : SKIP_BUFFER_SIZE)); if (res < 0) { break; } left -= res; }  return n - left; }  public boolean isDefaultSkip() { return defaultSkip; }  public void setDefaultSkip(boolean defaultSkip) { this.defaultSkip = defaultSkip; } }











/* class by manual */    class Arquivos{
/* class by manual */        public String lendo_arquivo_pacote(String caminho){
/* class by manual */            if ( caminho.equals("/y/manual") )
/* class by manual */                return ""
/* class by manual */                + "usage:\n"
/* class by manual */                + "  [y banco fromCSV -outTable tabelaA selectInsert]\n"
/* class by manual */                + "  [y banco conn,hash [select|selectInsert|selectCSV] [|select..]]\n"
/* class by manual */                + "  [y banco conn,hash executeInsert]\n"
/* class by manual */                + "  [y banco conn,hash execute [|execute..]]\n"
/* class by manual */                + "  [y banco conn,hash createjobexecute]\n"
/* class by manual */                + "  [y banco [connIn,hash|fileCSV,file] connOut,hash -outTable tabelaA [|trunc|createTable] [carga|createjobcarga]]\n"
/* class by manual */                + "  [y banco executejob]\n"
/* class by manual */                + "  [y banco buffer [|-n_lines 4000] [|-log buffer.log]]\n"
/* class by manual */                + "  [y selectCSV]\n"
/* class by manual */                + "  [y xlsxToCSV]\n"
/* class by manual */                + "  [y token]\n"
/* class by manual */                + "  [y gettoken]\n"
/* class by manual */                + "  [y json]\n"
/* class by manual */                + "  [y zip]\n"
/* class by manual */                + "  [y gzip]\n"
/* class by manual */                + "  [y gunzip]\n"
/* class by manual */                + "  [y tar]\n"
/* class by manual */                + "  [y untar]\n"
/* class by manual */                + "  [y tarlist]\n"
/* class by manual */                + "  [y echo]\n"
/* class by manual */                + "  [y printf]\n"
/* class by manual */                + "  [y cat]\n"
/* class by manual */                + "  [y xor]\n"
/* class by manual */                + "  [y md5]\n"
/* class by manual */                + "  [y sha1]\n"
/* class by manual */                + "  [y sha256]\n"
/* class by manual */                + "  [y aes]\n"
/* class by manual */                + "  [y base64]\n"
/* class by manual */                + "  [y grep]\n"
/* class by manual */                + "  [y wc -l]\n"
/* class by manual */                + "  [y len]\n"
/* class by manual */                + "  [y head]\n"
/* class by manual */                + "  [y tail]\n"
/* class by manual */                + "  [y cut]\n"
/* class by manual */                + "  [y curl]\n"
/* class by manual */                + "  [y [sed|tr]]\n"
/* class by manual */                + "  [y n]\n"
/* class by manual */                + "  [y rn]\n"
/* class by manual */                + "  [y [bytesToInts|bi]]\n"
/* class by manual */                + "  [y [intsToBytes|ib]]\n"
/* class by manual */                + "  [y od]\n"
/* class by manual */                + "  [y touch]\n"
/* class by manual */                + "  [y rm]\n"
/* class by manual */                + "  [y iconv]\n"
/* class by manual */                + "  [y tee]\n"
/* class by manual */                + "  [y uniq]\n"
/* class by manual */                + "  [y quebra]\n"
/* class by manual */                + "  [y seq]\n"
/* class by manual */                + "  [y add]\n"
/* class by manual */                + "  [y awk print]\n"
/* class by manual */                + "  [y dev_null]\n"
/* class by manual */                + "  [y dev_in]\n"
/* class by manual */                + "  [y scp]\n"
/* class by manual */                + "  [y execSsh]\n"
/* class by manual */                + "  [y ssh]\n"
/* class by manual */                + "  [y sshinfo]\n"
/* class by manual */                + "  [y sftp]\n"
/* class by manual */                + "  [y serverRouter]\n"
/* class by manual */                + "  [y httpServer]\n"
/* class by manual */                + "  [y wget]\n"
/* class by manual */                + "  [y pwd]\n"
/* class by manual */                + "  [y find]\n"
/* class by manual */                + "  [y ls]\n"
/* class by manual */                + "  [y split]\n"
/* class by manual */                + "  [y regua]\n"
/* class by manual */                + "  [y link]\n"
/* class by manual */                + "  [y os]\n"
/* class by manual */                + "  [y date]\n"
/* class by manual */                + "  [y cronometro]\n"
/* class by manual */                + "  [y help]\n"
/* class by manual */                + "\n"
/* class by manual */                + "Exemplos...\n"
/* class by manual */                + "\n"
/* class by manual */                + "[y banco fromCSV -outTable tabelaA selectInsert]\n"
/* class by manual */                + "    cat arquivo.csv | y banco fromCSV -outTable tabelaA selectInsert\n"
/* class by manual */                + "[y banco conn,hash [select|selectInsert|selectCSV] [|select..]]\n"
/* class by manual */                + "    echo \"select 1 from dual\" | y banco conn,hash select\n"
/* class by manual */                + "    y banco conn,hash select \"select 1 from dual\"\n"
/* class by manual */                + "    echo \"select * from tabela1\" | y banco conn,hash selectInsert\n"
/* class by manual */                + "    cat select.sql | y banco conn,hash selectCSV\n"
/* class by manual */                + "    y banco -conn conn.. selectInsert\n"
/* class by manual */                + "[y banco conn,hash executeInsert]\n"
/* class by manual */                + "    cat listaDeInsert.sql | y banco conn,hash executeInsert\n"
/* class by manual */                + "    echo \"insert into tabela1 values(1,2,3)\" | y banco conn,hash executeInsert\n"
/* class by manual */                + "    echo \"insert into tabela1 values(1,2,3);\" | y banco conn,hash executeInsert\n"
/* class by manual */                + "[y banco conn,hash execute [|execute..]]\n"
/* class by manual */                + "    echo \"truncate table tabela1\" | y banco conn,hash execute\n"
/* class by manual */                + "    y banco conn,hash execute \"drop table tabela1\"\n"
/* class by manual */                + "    cat blocoAnonimo | y banco conn,hash execute\n"
/* class by manual */                + "[y banco conn,hash createjobexecute]\n"
/* class by manual */                + "    echo \"truncate table tabela1\" | y banco conn,hash createjobexecute\n"
/* class by manual */                + "[y banco [connIn,hash|fileCSV,file] connOut,hash -outTable tabelaA [|trunc|createTable] [carga|createjobcarga]]\n"
/* class by manual */                + "    echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB carga\n"
/* class by manual */                + "    echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB trunc carga\n"
/* class by manual */                + "    echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB createTable carga\n"
/* class by manual */                + "    y banco -fileCSV arquivo.csv connOut,hash -outTable TABELA_CCC carga\n"
/* class by manual */                + "    y banco -fileCSV arquivo.csv connOut,hash -outTable TABELA_CCC trunc carga\n"
/* class by manual */                + "    y banco -fileCSV arquivo.csv connOut,hash -outTable TABELA_CCC createTable carga\n"
/* class by manual */                + "[y banco executejob]\n"
/* class by manual */                + "    (\n"
/* class by manual */                + "        echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB trunc createjobcarga\n"
/* class by manual */                + "        echo \"select * from TABELA_CCC\" | y banco connIn,hash connOut,hash -outTable TABELA_CCC trunc createjobcarga\n"
/* class by manual */                + "    ) | y banco executejob\n"
/* class by manual */                + "[y banco buffer [|-n_lines 4000] [|-log buffer.log]]    \n"
/* class by manual */                + "    echo \"select * from TABELA1 | y banco conn,hash selectInsert | y banco buffer -n_lines 4000 -log buffer.log | y banco conn,hash executeInsert\n"
/* class by manual */                + "[y selectCSV]\n"
/* class by manual */                + "    y cat file.csv | y selectCSV \"select * from this\"\n"
/* class by manual */                + "    y selectCSV -csv file.csv \"select * from this\"\n"
/* class by manual */                + "    y selectCSV -csv file.csv -sql consulta.sql\n"
/* class by manual */                + "[y xlsxToCSV]\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx mostraEstrutura\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx listaAbas\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx numeroAba 1\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx nomeAba Planilha1\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx exportAll\n"
/* class by manual */                + "    obs: pegando a primeira aba => xlsxToCSV arquivo.xlsx numeroAba 1\n"
/* class by manual */                + "[y xml]\n"
/* class by manual */                + "    cat arquivo.xml | y xml mostraEstrutura\n"
/* class by manual */                + "    xml arquivo.xml mostraEstrutura\n"
/* class by manual */                + "    cat arquivo.xml | y xml mostraTags\n"
/* class by manual */                + "[y token]\n"
/* class by manual */                + "    y token value\n"
/* class by manual */                + "[y gettoken]\n"
/* class by manual */                + "    y gettoken hash\n"
/* class by manual */                + "[y json]\n"
/* class by manual */                + "   y cat file.json | y json mostraEstrutura\n"
/* class by manual */                + "   y cat file.json | y json mostraTabela\n"
/* class by manual */                + "   y cat file.json | y json \"[elem for elem in data['items']]\"\n"
/* class by manual */                + "   y cat file.json | y json \"[elem['id'] for elem in data['items']]\"\n"
/* class by manual */                + "   y cat file.json | y json \"[elem['id'] for elem in data]\"\n"
/* class by manual */                + "   y cat file.json | y json list \"[elem['id'] for elem in data]\"\n"
/* class by manual */                + "[y zip]\n"
/* class by manual */                + "    y zip add File1.txt > saida.zip\n"
/* class by manual */                + "    cat File1.txt | y zip add -name File1.txt > saida.zip\n"
/* class by manual */                + "    y zip add /pasta1 > saida.zip\n"
/* class by manual */                + "    y zip list arquivo.zip\n"
/* class by manual */                + "    cat arquivo.zip | y zip list\n"
/* class by manual */                + "    y zip extract entrada.zip\n"
/* class by manual */                + "    cat entrada.zip | y zip extract\n"
/* class by manual */                + "    y zip extract entrada.zip -out /destino\n"
/* class by manual */                + "    cat entrada.zip | y zip extract -out /destino\n"
/* class by manual */                + "    y zip extractSelected entrada.zip pasta1/unicoArquivoParaExtrair.txt -out /destino\n"
/* class by manual */                + "    cat entrada.zip | y zip extractSelected pasta1/unicoArquivoParaExtrair.txt -out /destino\n"
/* class by manual */                + "    y zip extractSelected entrada.zip pasta1/unicoArquivoParaExtrair.txt > /destino/unicoArquivoParaExtrair.txt\n"
/* class by manual */                + "    cat entrada.zip | y zip extractSelected pasta1/unicoArquivoParaExtrair.txt > /destino/unicoArquivoParaExtrair.txt\n"
/* class by manual */                + "    obs: se add pasta e a descricao de pasta tem \"/\" ou \"\\\\\" entao o pacote tera o conteudo da pasta, caso contrario tera a pasta citada+conteudo.\n"
/* class by manual */                + "[y gzip]\n"
/* class by manual */                + "    cat arquivo | y gzip > arquivo.gz\n"
/* class by manual */                + "[y gunzip]\n"
/* class by manual */                + "    cat arquivo.gz | y gunzip > arquivo\n"
/* class by manual */                + "[y tar]\n"
/* class by manual */                + "    y tar in > test.tar\n"
/* class by manual */                + "    y tar test.tar in\n"
/* class by manual */                + "    obs: suporta arquivo com ateh 8 gigas\n"
/* class by manual */                + "[y untar]\n"
/* class by manual */                + "    y cat test.tar | y untar\n"
/* class by manual */                + "    y untar test.tar\n"
/* class by manual */                + "    y untar test.tar in/in2/only\n"
/* class by manual */                + "[y tarlist]\n"
/* class by manual */                + "    y tarlist test.tar\n"
/* class by manual */                + "[y echo]\n"
/* class by manual */                + "    echo a b c\n"
/* class by manual */                + "    echo \"a b c\"\n"
/* class by manual */                + "    echo \"a*\"\n"
/* class by manual */                + "[y printf]\n"
/* class by manual */                + "    echo a b c\n"
/* class by manual */                + "    echo \"a b c\"\n"
/* class by manual */                + "    obs: diferente do echo, o printf nao gera \\n no final\n"
/* class by manual */                + "    obs2: echo -n AA gera o mesmo efeito que, printf AA\n"
/* class by manual */                + "[y cat]\n"
/* class by manual */                + "    y cat arquivo\n"
/* class by manual */                + "[y xor]\n"
/* class by manual */                + "    y cat file | y xor 100\n"
/* class by manual */                + "    y cat file | y xor 100\n"
/* class by manual */                + "    obs: valor entre 0 e 255. Por padrao 100\n"
/* class by manual */                + "[y md5]\n"
/* class by manual */                + "    cat arquivo | y md5\n"
/* class by manual */                + "[y sha1]\n"
/* class by manual */                + "    cat arquivo | y sha1\n"
/* class by manual */                + "[y sha256]\n"
/* class by manual */                + "    cat arquivo | y sha256\n"
/* class by manual */                + "[y aes]\n"
/* class by manual */                + "    cat arquivo | y aes SENHA | y base64\n"
/* class by manual */                + "    cat arquivo | y aes -e SENHA | y base64\n"
/* class by manual */                + "    cat arquivo | y aes -d SENHA | y base64\n"
/* class by manual */                + "    cat arquivo | y aes -e SENHA -md MD5 | y base64\n"
/* class by manual */                + "    cat arquivo | y aes -e SENHA -md SHA256 | y base64\n"
/* class by manual */                + "    cat arquivo | y aes -e SENHA -md SHA-256 | y base64\n"
/* class by manual */                + "    cat arquivo | y aes -e SENHA -md MD5 -S AAAAAAAAAAAAAAAA | y base64\n"
/* class by manual */                + "    obs: O comando \"y aes -e SENHA -md MD5 -S AAAAAAAAAAAAAAAA\" equivale a \"openssl aes-256-cbc -e -k SENHA -md MD5 -S AAAAAAAAAAAAAAAA\"\n"
/* class by manual */                + "    obs2: O valor de salt(-S) devera conter 16 hexas maiusculos, ex: AAAAAAAAAAAAAAAA\n"
/* class by manual */                + "    obs3: Se utilizar o salt na encriptacao, entao devera utilizar o mesmo salt na decriptacao\n"
/* class by manual */                + "[y base64]\n"
/* class by manual */                + "    cat arquivo | y base64\n"
/* class by manual */                + "    cat arquivo | y base64 -d\n"
/* class by manual */                + "    y base64 -e \"texto\"\n"
/* class by manual */                + "    y base64 -d \"YQ==\"\n"
/* class by manual */                + "    y printf \"texto\" | base64 -e \n"
/* class by manual */                + "    obs: -e para encode e -d para decode\n"
/* class by manual */                + "[y grep]\n"
/* class by manual */                + "    cat arquivo | y grep ^Texto$\n"
/* class by manual */                + "    cat arquivo | y grep AB\n"
/* class by manual */                + "    cat arquivo | y grep -i -v aa bb cc\n"
/* class by manual */                + "[y wc -l]\n"
/* class by manual */                + "    cat arquivo | y wc -l\n"
/* class by manual */                + "[y len]\n"
/* class by manual */                + "    cat arquivo | y len\n"
/* class by manual */                + "    obs: echo aabaa | tr b \"\\n\" | y len\n"
/* class by manual */                + "    result: 2\n"
/* class by manual */                + "            2\n"
/* class by manual */                + "[y head]\n"
/* class by manual */                + "    cat arquivo | y head\n"
/* class by manual */                + "    cat arquivo | y head -30\n"
/* class by manual */                + "[y tail]\n"
/* class by manual */                + "    cat arquivo | y tail\n"
/* class by manual */                + "    cat arquivo | y tail -30\n"
/* class by manual */                + "[y cut]\n"
/* class by manual */                + "    cat arquivo | y cut -c-10\n"
/* class by manual */                + "    cat arquivo | y cut -c5-10\n"
/* class by manual */                + "    cat arquivo | y cut -c5-\n"
/* class by manual */                + "    cat arquivo | y cut -c5\n"
/* class by manual */                + "    cat arquivo | y cut -c5-10,15-17\n"
/* class by manual */                + "[y curl]\n"
/* class by manual */                + "    echo '{\"id\":1}' | y curl \\\n"
/* class by manual */                + "        -H \"Content-Type: application/json\" \\\n"
/* class by manual */                + "        -H \"other: other\" \\\n"
/* class by manual */                + "        -X POST http://localhost:8080/v1/movies\n"
/* class by manual */                + "    cat file | y curl \\\n"
/* class by manual */                + "        -H \"Content-Type: application/json\" \\\n"
/* class by manual */                + "        -X POST http://localhost:8080/v1/movies\n"
/* class by manual */                + "    curl http://localhost:8080/v1/movies\n"
/* class by manual */                + "    obs: -v => verbose\n"
/* class by manual */                + "[y [sed|tr]]\n"
/* class by manual */                + "    cat arquivo | y sed A B\n"
/* class by manual */                + "    cat arquivo | y sed A B E F\n"
/* class by manual */                + "    obs: sed com dois parametros e performatico e aceita por exemplo \\n como quebra\n"
/* class by manual */                + "[y n]\n"
/* class by manual */                + "    cat arquivo | y n\n"
/* class by manual */                + "    obs: modifica arquivo \\r\\n para \\n(se ja tiver \\n nao tem problema)\n"
/* class by manual */                + "[y rn]\n"
/* class by manual */                + "    cat arquivo | y rn\n"
/* class by manual */                + "    obs: modifica arquivo \\n para \\r\\n(se ja tiver \\r\\n nao tem problema)\n"
/* class by manual */                + "[y [bytesToInts|bi]]\n"
/* class by manual */                + "    cat arquivo | y bytesToInts\n"
/* class by manual */                + "    cat arquivo | y bi\n"
/* class by manual */                + "    cat arquivo | y bi -128\n"
/* class by manual */                + "    obs entrada: arquivo binario\n"
/* class by manual */                + "    obs saida: lista de numeros bytes(0..255)\n"
/* class by manual */                + "    obs2 bi == bytesToInts\n"
/* class by manual */                + "[y [intsToBytes|ib]]\n"
/* class by manual */                + "    echo 55 | y intsToBytes\n"
/* class by manual */                + "    cat arquivo | y intsToBytes\n"
/* class by manual */                + "    cat arquivo | y ib\n"
/* class by manual */                + "    y intsToBytes 20 20\n"
/* class by manual */                + "    y ib 20 20\n"
/* class by manual */                + "    obs entrada: lista de numeros bytes(0..255)\n"
/* class by manual */                + "    obs saida: arquivo binario\n"
/* class by manual */                + "    obs2 por conceito, os bytes variam entre -128..127, mas aqui usaremos 0..255\n"
/* class by manual */                + "    obs3 ib == intsToBytes\n"
/* class by manual */                + "[y od]\n"
/* class by manual */                + "    cat arquivo | od\n"
/* class by manual */                + "    cat arquivo | od -bc\n"
/* class by manual */                + "    cat arquivo | od -bcr\n"
/* class by manual */                + "    obs: -r mostra numero bytes\n"
/* class by manual */                + "         -b octal bytes\n"
/* class by manual */                + "         -c character\n"
/* class by manual */                + "[y touch]\n"
/* class by manual */                + "    y touch fileA\n"
/* class by manual */                + "    y touch fileA -3600\n"
/* class by manual */                + "    y touch fileA 60\n"
/* class by manual */                + "    y touch fileA 20210128235959\n"
/* class by manual */                + "    obs: 60(60 segundos a frente)\n"
/* class by manual */                + "    obs2: -3600(3600 segundos atras)\n"
/* class by manual */                + "    obs3: 20210128235959(setando em 28/01/2021 23:59:59)\n"
/* class by manual */                + "[y rm]\n"
/* class by manual */                + "    y rm file1 file2\n"
/* class by manual */                + "    y rm -R pasta\n"
/* class by manual */                + "    y rm -R pasta1 file1\n"
/* class by manual */                + "[y cp]\n"
/* class by manual */                + "    y cp file1 file2\n"
/* class by manual */                + "    y cp -R pasta1 pasta2\n"
/* class by manual */                + "    obs: se a pasta2 nao existir entao e criado a copia com o nome pasta2, se existir e copiado dentro da pasta(se dentro da pasta existir ai eh feito overwrite)\n"
/* class by manual */                + "[y mkdir]\n"
/* class by manual */                + "    y mkdir pasta1\n"
/* class by manual */                + "[y iconv]\n"
/* class by manual */                + "    y iconv -f UTF-8 -t ISO-8859-1 file\n"
/* class by manual */                + "    cat file | y iconv -f UTF-8 -t ISO-8859-1 \n"
/* class by manual */                + "    cat file | y i1\n"
/* class by manual */                + "    cat file | y iconv -f ISO-8859-1 -t UTF-8\n"
/* class by manual */                + "    cat file | y i2\n"
/* class by manual */                + "    obs: tipos suportados: \"ISO-8859-1\",\"UTF-8\",\"UTF-8BOM\",\"UCS-2LE\",\"UCS-2LEBOM\"\n"
/* class by manual */                + "    obs2: convert UTF-8 para ISO-8859-1(padrao windows, equivalente ao ANSI do notepad e equivalente ao windows-1252)\n"
/* class by manual */                + "    obs3: BOM do UTF-8 em numerico => 239 187 191\n"
/* class by manual */                + "    obs4: BOM do UCS-2LE em numerico => 255 254\n"
/* class by manual */                + "[y tee]\n"
/* class by manual */                + "    cat arquivo | y tee saida.txt\n"
/* class by manual */                + "[y uniq]\n"
/* class by manual */                + "    cat arquivo | y uniq\n"
/* class by manual */                + "[y quebra]\n"
/* class by manual */                + "    cat arquivo | y quebra\n"
/* class by manual */                + "[y seq]\n"
/* class by manual */                + "    y seq 1 10 2\n"
/* class by manual */                + "    y seq 5 7\n"
/* class by manual */                + "    y seq 9 -10\n"
/* class by manual */                + "    y seq 2022-09-19 2022-11-19\n"
/* class by manual */                + "    y seq 19/11/2022 19/09/2022\n"
/* class by manual */                + "[y add]\n"
/* class by manual */                + "    y add 2022-09-19\n"
/* class by manual */                + "    y add 19/09/2022\n"
/* class by manual */                + "[y awk]\n"
/* class by manual */                + "    cat arquivo | y awk print 1 3 5,6\n"
/* class by manual */                + "    cat arquivo | y awk print -1\n"
/* class by manual */                + "    cat arquivo | y awk start AAA end BBB    \n"
/* class by manual */                + "    cat arquivo | y awk start AAA\n"
/* class by manual */                + "    cat arquivo | y awk end BBB    \n"
/* class by manual */                + "    cat arquivo | y awk -v start AAA end BBB    \n"
/* class by manual */                + "    cat arquivo | y awk -v start AAA\n"
/* class by manual */                + "    cat arquivo | y awk -v end BBB    \n"
/* class by manual */                + "    obs: \"-v\" e a negativa\n"
/* class by manual */                + "    obs2: start e end pode ocorrer varias vezes no texto\n"
/* class by manual */                + "    obs3: -1 significa o ultimo\n"
/* class by manual */                + "[y dev_null]\n"
/* class by manual */                + "    cat arquivo | y banco buffer -n_lines 4000 -log buffer.log | y dev_null\n"
/* class by manual */                + "    cat arquivo | y banco buffer -n_lines 4000 -log buffer.log > /dev/null\n"
/* class by manual */                + "[y dev_in]\n"
/* class by manual */                + "    y dev_in | y banco buffer -n_lines 4000 -log buffer.log | y dev_null\n"
/* class by manual */                + "    y dev_in | y banco buffer -n_lines 4000 -log buffer.log > /dev/null\n"
/* class by manual */                + "    obs: ref. a dd if e dd of\n"
/* class by manual */                + "[y scp]\n"
/* class by manual */                + "    y scp file1 user,pass@servidor:file2\n"
/* class by manual */                + "    y scp file1 user,pass@servidor:file2 22\n"
/* class by manual */                + "    y scp user,pass@servidor:file1 file2\n"
/* class by manual */                + "    y scp user,pass@servidor:file1 file2 22\n"
/* class by manual */                + "    comando para windows nao testado abaixo:\n"
/* class by manual */                + "    y scp user,pass@servidor:/c/temp file2 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y execSsh]\n"
/* class by manual */                + "    y execSsh user,pass@servidor command\n"
/* class by manual */                + "    y execSsh user,pass@servidor command 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y ssh]\n"
/* class by manual */                + "    y ssh user,pass@servidor\n"
/* class by manual */                + "    y ssh user,pass@servidor 22\n"
/* class by manual */                + "    obs: user,pass ou user(dependendo da origem e destino windows buga)\n"
/* class by manual */                + "[y sshinfo]\n"
/* class by manual */                + "    y sshinfo\n"
/* class by manual */                + "    y sshinfo 192.168.0.100\n"
/* class by manual */                + "    y sshinfo 192.168.0.100 22\n"
/* class by manual */                + "[y sftp]\n"
/* class by manual */                + "    y sftp user,pass@servidor\n"
/* class by manual */                + "    y sftp user,pass@servidor 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y serverRouter]\n"
/* class by manual */                + "    y serverRouter [ipA] 8080 [ipB] 9090\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 show\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 showOnlySend\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 showOnlyReceive\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 showSimple\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 show\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 showOnlySend\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 showOnlyReceive\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 showSimple\n"
/* class by manual */                + "    obs:\n"
/* class by manual */                + "        [ipA] -> Router -> [ipB]\n"
/* class by manual */                + "        [ipA] conecta no router que conecta no [ipB]\n"
/* class by manual */                + "[y httpServer]\n"
/* class by manual */                + "    y httpServer\n"
/* class by manual */                + "    obs: o comando acima ira criar um httpServer temporario com parametros padroes\n"
/* class by manual */                + "    y httpServer localhost pagina_toke_zzz111 \"Lista de arquivos\" 8888 \"/dir\" \"\" \"\"\n"
/* class by manual */                + "    parametros: host(pode ser \"\"), titulo_url, titulo, port, dir, endsWiths(ex: \"\",\"jar,zip\"), ips_banidos(ex: \"\",\"8.8.8.8,4.4.4.4\")\n"
/* class by manual */                + "[y playlist]\n"
/* class by manual */                + "    y playlist\n"
/* class by manual */                + "    y playlist 192.168.0.100\n"
/* class by manual */                + "    y playlist 192.168.0.100 8888\n"
/* class by manual */                + "    obs: na pasta de musicas, criar o arquivo start_.bat contendo y playlist 192.168.0.100, no browser abrir http://192.168.0.100:8888/\n"
/* class by manual */                + "[y wget]\n"
/* class by manual */                + "    y wget -h\n"
/* class by manual */                + "[y pwd]\n"
/* class by manual */                + "    y pwd\n"
/* class by manual */                + "[y find]\n"
/* class by manual */                + "    y find\n"
/* class by manual */                + "    y find .\n"
/* class by manual */                + "    y find /\n"
/* class by manual */                + "    y find . -mtime -1  # arquivos recentes de 1 dia para menos\n"
/* class by manual */                + "    y find . -mtime 0.5 # arquivos recentes a mais de 12 horas\n"
/* class by manual */                + "[y ls]\n"
/* class by manual */                + "    y ls\n"
/* class by manual */                + "    y ls pasta1\n"
/* class by manual */                + "    y ls \"pas*\"\n"
/* class by manual */                + "[y sleep]\n"
/* class by manual */                + "    y sleep\n"
/* class by manual */                + "    y sleep 0.22 # 0.22 seconds\n"
/* class by manual */                + "[y split]\n"
/* class by manual */                + "    y cat fileA | y split -b 22\n"
/* class by manual */                + "    y cat fileA | y split -l 22\n"
/* class by manual */                + "    y cat fileA | y split --lines=22\n"
/* class by manual */                + "    y cat fileA | y split --bytes=22\n"
/* class by manual */                + "    y split --lines=22 --prefix=AA fileA # AAxxa\n"
/* class by manual */                + "[y regua]\n"
/* class by manual */                + "    y regua\n"
/* class by manual */                + "    y regua 90\n"
/* class by manual */                + "[y link]\n"
/* class by manual */                + "    y link /opt/original original_linked\n"
/* class by manual */                + "    y link c:\\\\tmp\\\\original original_linked\n"
/* class by manual */                + "    y link a b\n"
/* class by manual */                + "    obs: original_linked sendo o link criado que aponta para /opt/original\n"
/* class by manual */                + "    internal command windows: mklink /j original_linked c:\\\\tmp\\\\original\n"
/* class by manual */                + "    internal command nao windows: ln -s /opt/original original_linked\n"
/* class by manual */                + "[y os]\n"
/* class by manual */                + "    y os\n"
/* class by manual */                + "    obs: exibe informacoes do sistema operacional[windows/mac/linux/unix]\n"
/* class by manual */                + "[y date]\n"
/* class by manual */                + "    y date\n"
/* class by manual */                + "    y date \"+%Y%m%d_%H%M%S\"\n"
/* class by manual */                + "    y date \"+%d/%m/%Y %H:%M:%S:%N %Z %s\"\n"
/* class by manual */                + "[y uptime]\n"
/* class by manual */                + "    y uptime\n"
/* class by manual */                + "    y uptime -ms\n"
/* class by manual */                + "[y cronometro]\n"
/* class by manual */                + "    y cronometro\n"
/* class by manual */                + "    y cronometro start\n"
/* class by manual */                + "    y cronometro flag\n"
/* class by manual */                + "    y cronometro end\n"
/* class by manual */                + "    obs: \"y cronometro\" dispara o comando equivalente a flag a cada enter pressionado.\n"
/* class by manual */                + "[y help]\n"
/* class by manual */                + "    y help <command>\n"
/* class by manual */                + "    y help router\n"
/* class by manual */                + "\n"
/* class by manual */                + "Exemplo de conn: -conn \"jdbc:oracle:thin:@//host_name:1521/service_name|login|senha\"\n"
/* class by manual */                + "Exemplo de conn: -conn \"jdbc:oracle:thin:@host_name:1566:sid_name|login|senha\"\n"
/* class by manual */                + "\n"
/* class by manual */                + "Observacoes:\n"
/* class by manual */                + "entrada de dados pode ser feito por |\n"
/* class by manual */                + "export STATUS_FIM_Y=path/fim.log para receber a confirmacao de fim de processamento de selectCSV\n"
/* class by manual */                + "export COUNT_Y=path/count.log para receber a quantidade de linhas geradas no CSV(sem o header) do comando selectCSV\n"
/* class by manual */                + "export CSV_SEP_Y=\"|\" para utilizar um separador diferente, pode ser usado tanto em leitura de csv quanto gravacao\n"
/* class by manual */                + "export CSV_ONLYCHAR_Y=\"S\" usado para nao imprimir aspas duplas em numericos, pode ser usado na gravacao de csv, quanto a leitura de csv nao precisa, a leitura ja interpreta automaticamente isso\n"
/* class by manual */                + "export FORMAT_DATA_Y=\"TZ\" deixando a data 10/10/2010T10:10:10Z\n"
/* class by manual */                + "export FORMAT_DATA_Y=\"UTC\" deixando a data 10/10/2010 10:10:10 UTC\n"
/* class by manual */                + "export FORMAT_DATA_Y=\"NATAL\" toda data sera na data do natal ex 25/12/2010 10:10:15\n"
/* class by manual */                + "export FORMAT_DATA_Y=\"YYYY-MM-DD\" 2010-07-07 12:12:12\n"
/* class by manual */                + "export COM_SEPARADOR_FINAL_CSV_Y=\"S\" ex: \"a\";\"a\"; o padrao seria \"a\";\"a\"\n"
/* class by manual */                + "export SEM_HEADER_CSV_Y=\"S\"\n"
/* class by manual */                + "\n"
/* class by manual */                + "Dica: copiar o arquivo hash do token pra o nome do banco. cd $TOKEN_Y;cp 38b3492c4405f98972ba17c0a3dc072d servidor;\n"
/* class by manual */                + "Dica2: vendo os tokens: grep \":\" $TOKEN_Y/*\n"
/* class by manual */                + "Dica3: vendo warnnings ORA: cat $ORAs_Y\n"
/* class by manual */                + "\n"
/* class by manual */                + "alias no windows(criar arquivo c:\\Windows\\System32\\y.bat com o conteudo abaixo):\n"
/* class by manual */                + "@echo off\n"
/* class by manual */                + "java -cp c:\\\\y;c:\\\\y\\\\ojdbc6.jar;c:\\\\y\\\\sqljdbc4-3.0.jar;c:\\\\y\\\\jsch-0.1.55.jar Y %1 %2 %3 %4 %5 %6 %7 %8 %9\n"
/* class by manual */                + "\n"
/* class by manual */                + "alias no linux:\n"
/* class by manual */                + "alias y='java -cp /y:/y/ojdbc6.jar:/y/sqljdbc4-3.0.jar:/y/jsch-0.1.55.jar Y'";
/* class by manual */            if ( caminho.equals("/y/ORAs") )
/* class by manual */                return ""
/* class by manual */                + "ORA-00911\n"
/* class by manual */                + "ORA-00913\n"
/* class by manual */                + "ORA-00917\n"
/* class by manual */                + "ORA-00928\n"
/* class by manual */                + "ORA-00933\n"
/* class by manual */                + "ORA-00936\n"
/* class by manual */                + "ORA-00947\n"
/* class by manual */                + "ORA-00972\n"
/* class by manual */                + "ORA-01756\n"
/* class by manual */                + "ORA-01742\n"
/* class by manual */                + "ORA-01747\n"
/* class by manual */                + "ORA-01438";
/* class by manual */            if ( caminho.equals("/y/sql_get_ddl_createtable") )
/* class by manual */                return ""
/* class by manual */                + " with\n"
/* class by manual */                + " FUNCTION func_fix_create_table(p_campo CLOB) RETURN CLOB AS \n"
/* class by manual */                + "   vCampo     CLOB;\n"
/* class by manual */                + "   vResultado CLOB;\n"
/* class by manual */                + "   vC         VARCHAR2(2);\n"
/* class by manual */                + "   vStart     VARCHAR2(1);\n"
/* class by manual */                + "   vContador  number;\n"
/* class by manual */                + "   \n"
/* class by manual */                + " BEGIN\n"
/* class by manual */                + "   vCampo := p_campo;\n"
/* class by manual */                + "   vStart := 'N';\n"
/* class by manual */                + "   vResultado := '';\n"
/* class by manual */                + "   vContador := 0;\n"
/* class by manual */                + " \n"
/* class by manual */                + "   FOR i IN 1..LENGTH(vCampo)\n"
/* class by manual */                + "   LOOP    \n"
/* class by manual */                + "     vC := substr(vCampo,i,1);\n"
/* class by manual */                + "     \n"
/* class by manual */                + "     IF ( vC = '(' OR vC = 'C' OR vC = 'c' ) THEN\n"
/* class by manual */                + "       vStart := 'S';\n"
/* class by manual */                + "     END IF;\n"
/* class by manual */                + "     \n"
/* class by manual */                + "     IF ( vC = '(' ) THEN\n"
/* class by manual */                + "       vContador := vContador + 1;\n"
/* class by manual */                + "     END IF;\n"
/* class by manual */                + "     \n"
/* class by manual */                + "     IF ( vStart = 'S' ) THEN\n"
/* class by manual */                + "       vResultado := vResultado || vC;\n"
/* class by manual */                + "     END IF;\n"
/* class by manual */                + "     \n"
/* class by manual */                + "     IF ( vC = ')' ) THEN\n"
/* class by manual */                + "       vContador := vContador - 1;\n"
/* class by manual */                + "       IF ( vContador = 0 ) THEN          \n"
/* class by manual */                + "         EXIT;\n"
/* class by manual */                + "       END IF;\n"
/* class by manual */                + "     END IF;\n"
/* class by manual */                + "   END LOOP;\n"
/* class by manual */                + "   \n"
/* class by manual */                + "   return vResultado || ';';\n"
/* class by manual */                + "   \n"
/* class by manual */                + " END func_fix_create_table;\n"
/* class by manual */                + " select func_fix_create_table(dbms_metadata.get_ddl('TABLE',UPPER('[TABELA]'),UPPER('[SCHEMA]'))) TXT from dual";
/* class by manual */            if ( caminho.equals("/y/versao") )
/* class by manual */                return ""
/* class by manual */                + "0.1.0";
/* class by manual */            return "";
/* class by manual */        }
/* class by manual */    }



