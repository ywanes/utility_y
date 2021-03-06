//
// curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/dist/lib/ojdbc6.jar > ojdbc6.jar
// curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/dist/lib/jsch-0.1.55.jar > jsch-0.1.55.jar
// curl https://raw.githubusercontent.com/ywanes/utility_y/master/y/src/Y.java > Y.java
// javac -encoding UTF-8 -cp .:ojdbc6.jar:jsch-0.1.55.jar Y.java
// alias y='java -cp /y:/y/ojdbc6.jar:/y/jsch-0.1.55.jar Y'
// crétidos "ssh/scp/sftp/sshExec" https://ufpr.dl.sourceforge.net/project/jsch/jsch.jar/0.1.55/jsch-0.1.55.jar 
// crétidos https://github.com/is/jsch/tree/master/examples
//

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


public class Y {    
    //public static String local_env=null;
    public static String local_env="c:\\tmp";

    public static int BUFFER_SIZE=1024;
    public static String linhaCSV=null;
    public static int ponteiroLinhaCSV=0;    
    public static String sepCSV=getSeparadorCSV(); // ";";
    public static int n_lines_buffer_DEFAULT=500;        
    public String [] ORAs=new String[]{};
    public String [] suportIconv=new String[]{"ISO-8859-1","UTF-8","UTF-8BOM","UCS-2LE","UCS-2LEBOM"};
    public int [] BOM_UTF_8=new int[]{239,187,191};    
    public int [] BOM_UCS_2LE=new int[]{255,254};        
    public String erroSequenciaIlegal="Erro, sequencia ilegal!";
    
    // octal bytes
    public static String [] OD_BC_B=new String[]{" 000"," 001"," 002"," 003"," 004"," 005"," 006"," 007"," 010"," 011"," 012"," 013"," 014"," 015"," 016"," 017"," 020"," 021"," 022"," 023"," 024"," 025"," 026"," 027"," 030"," 031"," 032"," 033"," 034"," 035"," 036"," 037"," 040"," 041"," 042"," 043"," 044"," 045"," 046"," 047"," 050"," 051"," 052"," 053"," 054"," 055"," 056"," 057"," 060"," 061"," 062"," 063"," 064"," 065"," 066"," 067"," 070"," 071"," 072"," 073"," 074"," 075"," 076"," 077"," 100"," 101"," 102"," 103"," 104"," 105"," 106"," 107"," 110"," 111"," 112"," 113"," 114"," 115"," 116"," 117"," 120"," 121"," 122"," 123"," 124"," 125"," 126"," 127"," 130"," 131"," 132"," 133"," 134"," 135"," 136"," 137"," 140"," 141"," 142"," 143"," 144"," 145"," 146"," 147"," 150"," 151"," 152"," 153"," 154"," 155"," 156"," 157"," 160"," 161"," 162"," 163"," 164"," 165"," 166"," 167"," 170"," 171"," 172"," 173"," 174"," 175"," 176"," 177"," 200"," 201"," 202"," 203"," 204"," 205"," 206"," 207"," 210"," 211"," 212"," 213"," 214"," 215"," 216"," 217"," 220"," 221"," 222"," 223"," 224"," 225"," 226"," 227"," 230"," 231"," 232"," 233"," 234"," 235"," 236"," 237"," 240"," 241"," 242"," 243"," 244"," 245"," 246"," 247"," 250"," 251"," 252"," 253"," 254"," 255"," 256"," 257"," 260"," 261"," 262"," 263"," 264"," 265"," 266"," 267"," 270"," 271"," 272"," 273"," 274"," 275"," 276"," 277"," 300"," 301"," 302"," 303"," 304"," 305"," 306"," 307"," 310"," 311"," 312"," 313"," 314"," 315"," 316"," 317"," 320"," 321"," 322"," 323"," 324"," 325"," 326"," 327"," 330"," 331"," 332"," 333"," 334"," 335"," 336"," 337"," 340"," 341"," 342"," 343"," 344"," 345"," 346"," 347"," 350"," 351"," 352"," 353"," 354"," 355"," 356"," 357"," 360"," 361"," 362"," 363"," 364"," 365"," 366"," 367"," 370"," 371"," 372"," 373"," 374"," 375"," 376"," 377"};
    // caracteres
    public static String [] OD_BC_C=new String[]{"  \\0"," 001"," 002"," 003"," 004"," 005"," 006","  \\a","  \\b","  \\t","  \\n","  \\v","  \\f","  \\r"," 016"," 017"," 020"," 021"," 022"," 023"," 024"," 025"," 026"," 027"," 030"," 031"," 032"," 033"," 034"," 035"," 036"," 037","    ","   !","   \"","   #","   $","   %","   &","   '","   (","   )","   *","   +","   ,","   -","   .","   /","   0","   1","   2","   3","   4","   5","   6","   7","   8","   9","   :","   ;","   <","   =","   >","   ?","   @","   A","   B","   C","   D","   E","   F","   G","   H","   I","   J","   K","   L","   M","   N","   O","   P","   Q","   R","   S","   T","   U","   V","   W","   X","   Y","   Z","   [","   \\","   ]","   ^","   _","   `","   a","   b","   c","   d","   e","   f","   g","   h","   i","   j","   k","   l","   m","   n","   o","   p","   q","   r","   s","   t","   u","   v","   w","   x","   y","   z","   {","   |","   }","   ~"," 177"," 200"," 201"," 202"," 203"," 204"," 205"," 206"," 207"," 210"," 211"," 212"," 213"," 214"," 215"," 216"," 217"," 220"," 221"," 222"," 223"," 224"," 225"," 226"," 227"," 230"," 231"," 232"," 233"," 234"," 235"," 236"," 237"," 240"," 241"," 242"," 243"," 244"," 245"," 246"," 247"," 250"," 251"," 252"," 253"," 254"," 255"," 256"," 257"," 260"," 261"," 262"," 263"," 264"," 265"," 266"," 267"," 270"," 271"," 272"," 273"," 274"," 275"," 276"," 277"," 300"," 301"," 302"," 303"," 304"," 305"," 306"," 307"," 310"," 311"," 312"," 313"," 314"," 315"," 316"," 317"," 320"," 321"," 322"," 323"," 324"," 325"," 326"," 327"," 330"," 331"," 332"," 333"," 334"," 335"," 336"," 337"," 340"," 341"," 342"," 343"," 344"," 345"," 346"," 347"," 350"," 351"," 352"," 353"," 354"," 355"," 356"," 357"," 360"," 361"," 362"," 363"," 364"," 365"," 366"," 367"," 370"," 371"," 372"," 373"," 374"," 375"," 376"," 377"};
    // 0...256
    public static String [] OD_BC_R=new String[]{"   0","   1","   2","   3","   4","   5","   6","   7","   8","   9","  10","  11","  12","  13","  14","  15","  16","  17","  18","  19","  20","  21","  22","  23","  24","  25","  26","  27","  28","  29","  30","  31","  32","  33","  34","  35","  36","  37","  38","  39","  40","  41","  42","  43","  44","  45","  46","  47","  48","  49","  50","  51","  52","  53","  54","  55","  56","  57","  58","  59","  60","  61","  62","  63","  64","  65","  66","  67","  68","  69","  70","  71","  72","  73","  74","  75","  76","  77","  78","  79","  80","  81","  82","  83","  84","  85","  86","  87","  88","  89","  90","  91","  92","  93","  94","  95","  96","  97","  98","  99"," 100"," 101"," 102"," 103"," 104"," 105"," 106"," 107"," 108"," 109"," 110"," 111"," 112"," 113"," 114"," 115"," 116"," 117"," 118"," 119"," 120"," 121"," 122"," 123"," 124"," 125"," 126"," 127"," 128"," 129"," 130"," 131"," 132"," 133"," 134"," 135"," 136"," 137"," 138"," 139"," 140"," 141"," 142"," 143"," 144"," 145"," 146"," 147"," 148"," 149"," 150"," 151"," 152"," 153"," 154"," 155"," 156"," 157"," 158"," 159"," 160"," 161"," 162"," 163"," 164"," 165"," 166"," 167"," 168"," 169"," 170"," 171"," 172"," 173"," 174"," 175"," 176"," 177"," 178"," 179"," 180"," 181"," 182"," 183"," 184"," 185"," 186"," 187"," 188"," 189"," 190"," 191"," 192"," 193"," 194"," 195"," 196"," 197"," 198"," 199"," 200"," 201"," 202"," 203"," 204"," 205"," 206"," 207"," 208"," 209"," 210"," 211"," 212"," 213"," 214"," 215"," 216"," 217"," 218"," 219"," 220"," 221"," 222"," 223"," 224"," 225"," 226"," 227"," 228"," 229"," 230"," 231"," 232"," 233"," 234"," 235"," 236"," 237"," 238"," 239"," 240"," 241"," 242"," 243"," 244"," 245"," 246"," 247"," 248"," 249"," 250"," 251"," 252"," 253"," 254"," 255"};
    // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=
    public static int [] indexBase64 = new int []{65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,48,49,50,51,52,53,54,55,56,57,43,47};
    // ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=
    public static String txtBase64="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    public static String linhasExcel="0123456789";    
    public static int linhasExcel_len=linhasExcel.length();    
    public static String colunasExcel="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static int colunasExcel_len=colunasExcel.length();    
    
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
        
        //args=new String[]{"xlsxToCSV","C:\\tmp\\aa\\a.xlsx","exportAll"};                                        
        new Y().go(args);
    }
        
    public void go(String[] args){        
        try_load_ORAs();

        if ( args.length == 0 ){
            System.err.println(      
                lendo_arquivo_pacote("/y/manual_mini")
            );
            return;
        }
        if ( args[0].equals("banco") ){            
            if ( args.length == 1 ){
                System.err.println(
                    lendo_arquivo_pacote("/y/manual")
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
        if ( args[0].equals("xlsxToCSV") && args.length >= 3 ){
            //args=new String[]{"xlsxToCSV","teste.xlsx","mostraEstrutura"};
            //args=new String[]{"xlsxToCSV","teste.xlsx","listaAbas"};
            //args=new String[]{"xlsxToCSV","teste.xlsx","numeroAba","1"};
            //args=new String[]{"xlsxToCSV","teste.xlsx","nomeAba","A"};

            try{
                if ( new File(args[1]).exists() ){
                    if ( args.length == 3 && args[2].equals("mostraEstrutura") ){
                        xlsxToCSV(args[1],true,false,-1,"",System.out);
                        return;
                    }
                    if ( args.length == 3 && args[2].equals("listaAbas") ){
                        xlsxToCSV(args[1],false,true,-1,"",System.out);
                        for ( int i=0;i<xlsxToCSV_nomes.size();i++ )
                            System.out.println(xlsxToCSV_nomes.get(i));
                        return;
                    }
                    if ( args.length == 3 && args[2].equals("exportAll") ){
                        xlsxToCSV(args[1],false,true,-1,"",null);
                        ArrayList<String> bkp_lista=xlsxToCSV_nomes;
                        for ( int i=0;i<bkp_lista.size();i++ ){
                            System.out.println("exportando("+(i+1)+"/"+bkp_lista.size()+") arquivo: "+bkp_lista.get(i)+".csv");
                            xlsxToCSV(args[1],false,false,-1,bkp_lista.get(i),new FileOutputStream(bkp_lista.get(i)+".csv"));
                        }
                        return;
                    }
                    if ( args.length == 4 && args[2].equals("numeroAba") ){
                        try{
                            xlsxToCSV(args[1],false,false,Integer.parseInt(args[3]),"",System.out);
                            return;
                        }catch(Exception e){}
                    }
                    if ( args.length == 4 && args[2].equals("nomeAba") && args[3].length() > 0 ){
                        xlsxToCSV(args[1],false,false,-1,args[3],System.out);
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
                    XML.loadIs(System.in,true,false,null);
                    return;
                }            
                if ( args.length == 2 && args[1].equals("mostraTags") ){
                    XML.loadIs(System.in,false,true,null);
                    return;
                }            
                if ( args.length == 3 && new File(args[1]).exists() && args[2].equals("mostraEstrutura") ){
                    FileInputStream is = new FileInputStream(args[1]);
                    XML.loadIs(is,true,false,null);
                    is.close();                
                    return;
                }            
                if ( args.length == 3 && new File(args[1]).exists() && args[2].equals("mostraTags") ){
                    FileInputStream is = new FileInputStream(args[1]);
                    XML.loadIs(is,false,true,null);
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
            String hash=gravado_token(dir_token,value);
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
        if ( args[0].equals("echo") ){
            echo(args);
            return;
        }      
        if ( args[0].equals("printf") ){
            printf(args);
            return;
        }              
        if ( args[0].equals("cat") && args.length >= 2 ){
            cat(args);
            return;
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
        if ( args[0].equals("sed") && args.length == 3 && args[1].length() > 0 ){
            sed(args);
            return;
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
            bytesToInts();
            return;
        }       
        if ( args[0].equals("intsToBytes") || args[0].equals("ib") ){
            intsToBytes(args);
            return;
        }    
        if ( args[0].equals("od") 
            && ( 
                args.length == 1 
                || (args.length == 2 && args[1].startsWith("-") && args[1].length() > 1)
                )
            ){
            if ( args.length == 1 )
                od("");
            else
                od(args[1].substring(1));
            return;
        }          
        if ( args[0].equals("touch") && (args.length == 2 || args.length == 3) ){
            touch(args);
            return;
        }
        if ( args[0].equals("M") )
        {
            if ( args.length == 1 ){
                System.out.println("Parametro inválido!");
                System.out.println("Modelo:");
                System.out.println("y M ClassePrincipal Caminho Senha");
                return;
            }    
            if ( args.length == 4 ){
                String txt="";        
                boolean principal_encontrado=false;
                String classe="";
                byte[] data=null;

                //String senha=Utilonsole.getPasswordConsole("Digite a senha: ");
                //if ( senha == null || senha.length() == 0 ){
                //    System.out.println("Erro, nenhuma senha digitada!");
                //    System.exit(1);
                //}
                //String principal=Utilonsole.getTextConsole("Digite o classe principal: ");
                //if ( principal == null || principal.length() == 0 ){
                //    System.out.println("Erro, nenhuma classe digitada!");
                //    System.exit(1);
                //}
                //String dir=Utilonsole.getTextConsole("Digite o caminho(para o caminho digite enter): ");
                //if ( dir != null && dir.length() > 0 && ! new java.io.File(dir).exists() ){
                //    System.out.println("Erro, caminho inexistente!");
                //    System.exit(1);
                //}
                
                String principal=args[1];
                String dir=args[2];
                String senha=args[3];

                // chamada principal
                txt=","+principal;

                if ( dir.equals("") ) dir=".";
                java.io.File[] files=new java.io.File(dir).listFiles();
                for ( int i=0;i<files.length;i++ ){
                    if ( !files[i].isFile() ) continue;
                    if ( !files[i].getAbsolutePath().endsWith(".class") ) continue;                        
                    classe=files[i].getName().substring(0,files[i].getName().length()-6);
                    if ( classe.equals(principal) )
                        principal_encontrado=true;
                    try{
                        data=readAllBytes( files[i].getAbsolutePath() );
                    }catch(Exception e){
                        System.out.println("Erro na leitrua do arquivo: "+files[i].getAbsolutePath()+". "+e.toString());
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
                    System.out.println("Erro interno!");
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
                System.out.println("            String M=M_Utilonsole.getPasswordConsole(\"Digite a senha: \");");
                System.out.println("            if ( M == null || M.length() == 0 ){");
                System.out.println("                System.out.println(\"Erro, nenhuma senha digitada!\");");
                System.out.println("                System.exit(1);");
                System.out.println("            }");
                System.out.println("            M=M_Base64.base64(new M_AES().encrypt(M.getBytes(),\"\",null,null),true);");
                System.out.println("            System.out.println(\"digite o comando export abaixo para ambientes nao windows:\");");
                System.out.println("            System.out.println(\"export M=\"+M);            ");
                System.out.println("            System.out.println(\"digite o comando export abaixo para ambientes windows:\");");
                System.out.println("            System.out.println(\"set M=\"+M);");
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
                System.out.println("// String senha=M_Utilonsole.getPasswordConsole(\"Digite a senha: \");");
                System.out.println("// String texto=M_Utilonsole.getTextConsole(\"Digite o texto: \");");
                System.out.println("class M_Utilonsole{ public static String getPasswordConsole(String txt) { String retorno=\"\"; java.io.Console console=System.console(); if ( console == null ){ System.out.println(\"Error, input nao suportado nesse ambiente, rodando no netbeans?...\"); System.exit(1); } char [] passChar = System.console().readPassword(txt); if ( passChar != null ) retorno=new String(passChar); if ( retorno == null ){ System.out.println(\"Error, not input found\"); System.exit(1); } return retorno;}public static String getTextConsole(String txt) { String retorno=\"\"; java.io.Console console=System.console(); if ( console == null ){ System.out.println(\"Error, input nao suportado nesse ambiente, rodando no netbeans?...\"); System.exit(1); } System.out.print(txt);retorno=System.console().readLine();if ( retorno == null ){ System.out.println(\"Error, not input found\"); System.exit(1); } return retorno;}}");
                System.out.println("//M_Loader");
                System.out.println("//[classe principal],[load classA]  ,[load classB] ");
                System.out.println("//,classA           ,classA,dados...,classB,dados...");
                System.out.println("class M_Loader{ public static boolean loader(String senha,String[] args) throws Exception { try{ java.util.HashMap classes=new java.util.HashMap(); String base=M_Dados.get(); String txt=new String( new M_AES().decrypt( M_Base64.base64(base,false) ,senha,null) ); if (!txt.startsWith(\",\")){ throw new Exception(\"Erro fatal!\"); }else{ txt=txt.substring(1); } String partes[]=txt.split(\",\"); String id=null; String principal=null; for ( int i=0;i<partes.length;i++ ){ if ( principal == null ){ principal=partes[i]; continue; } if ( id == null ){ id=partes[i]; continue; } classes.put(id,partes[i]); id=null; } ClassLoader classLoader=new ClassLoader() {            @Override protected Class<?> findClass(String name) throws ClassNotFoundException { if ( classes.containsKey(name) ){ try { byte[] data=M_Base64.base64((String)classes.get(name),false); return defineClass(name,data,0,data.length);        } catch (Exception e) { System.err.println(\"Erro no carregamento da classe \"+name); System.exit(1); } } return super.findClass(name); } }; Class c=classLoader.loadClass(principal); java.lang.reflect.Method method=c.getDeclaredMethod(\"main\", new Class[]{String[].class} ); method.invoke(null, new Object[]{ args } ); }catch(Exception e){ return false;} return true;} }");
                
                int len=txt.length();
                System.out.println("//M_Dados");
                System.out.println("class M_Dados {");
                System.out.println("    public static String get() {");
                System.out.println("        StringBuilder sb=new StringBuilder();");
                for ( int i=0;i<len;i+=200 ){            
                    if ( i+200 > len )
                        System.out.println("        sb.append(\""+txt.substring(i,len)+"\");");
                    else
                        System.out.println("        sb.append(\""+txt.substring(i,i+200)+"\");");
                }
                System.out.println("        return sb.toString();");
                System.out.println("    }");
                System.out.println("}");
                return;
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
                    seq(Integer.parseInt(args[1]),Integer.parseInt(args[2]),0);
                    return;
                }
                if ( args.length == 4 ){
                    seq(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                    return;
                }
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
        if ( args[0].equals("sftp") ){
            sftp(args);
            return;
        }        
        if ( args[0].equals("serverRouter"))
        {
            serverRouter(args);
            return;            
        }
        if ( args[0].equals("TESTEserver"))
        {
            TESTEserver(args);
            return;            
        }
        if ( args[0].equals("TESTEclient"))
        {
            TESTEclient(args);
            return;            
        }
        if ( args.length == 8 && args[0].equals("httpServer"))
        {
            new HttpServer(new String[]{args[1],args[2],args[3],args[4],args[5],args[6],args[7]});
            return;            
        }        
        if ( args[0].equals("help") || args[0].equals("-help") || args[0].equals("--help") ){
            System.err.println(
                "Utilitário Y versão:" + lendo_arquivo_pacote("/y/versao") + "\n"
                + lendo_arquivo_pacote("/y/manual")
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
        
    public String [] get_v_i_txt(String [] args){
        String v="N";
        String i="N";
        String txt="";
        
        if ( args.length > 0 && args[0].equals("grep") )
            args=sliceParm(1,args);
        
        if ( args.length > 1 && args[0].equals("-v") ){
            v="S";
            args=sliceParm(1,args);
        }
        if ( args.length > 1 && args[0].equals("-i") ){
            i="S";
            args=sliceParm(1,args);
        }        
        if ( args.length > 1 && args[0].equals("-v") ){ // repetido de proposito, tratando [-v -i] e [-i -v]
            v="S";
            args=sliceParm(1,args);
        }

        if ( args.length == 1 ){
            txt=args[0];
            args=sliceParm(1,args);
        }
        
        if ( txt.equals("") )
            return null;
        return new String[]{v,i,txt};
    }
    
    
    public String [] get_senha_isEncoding_md_salt(String [] args){
        String senha=null;
        String isEncoding="S";
        String md=null;
        String salt=null;
        
        if ( args.length > 0 && args[0].equals("aes") )
            args=sliceParm(1,args);
        
        if ( args.length > 0 ){
            senha=args[0];
            args=sliceParm(1,args);
        }
        
        if ( args.length > 0 && ( args[0].equals("-e") || args[0].equals("-d") ) ){
            if ( args[0].equals("-e") )
                isEncoding="S";
            if ( args[0].equals("-d") )
                isEncoding="N";
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
        
        if ( args.length > 0 && args[0].equals("awk") )
            args=sliceParm(1,args);
        
        if ( args.length > 0 && args[0].equals("-v") ){
            negativa="S";        
            args=sliceParm(1,args);
        }

        if ( args.length > 1 && args[0].equals("start") )
        {
            start=args[1];
            args=sliceParm(2,args);
        }
        
        if ( args.length > 1 && args[0].equals("end") )
        {
            end=args[1];
            args=sliceParm(2,args);
        }

        if ( args.length == 0 ){
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
                    if ( tipos.get(i) == -3 || tipos.get(i) == 2 || tipos.get(i) == 12 || tipos.get(i) == -9
                        || tipos.get(i) == 1 || tipos.get(i) == 2005 || tipos.get(i) == -1 || tipos.get(i) == 93 )
                    {
                        if ( tipos.get(i) == 2 && tmp.startsWith("."))
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+" "+tmp.substring(11, 19);
                        //tmp=tmp.replace("'","''");
                        if ( i == campos.size()-1 ){
                            sb.append(tmp);
                        }else{
                            sb.append(tmp);
                            sb.append("\t");
                        }
                        continue;
                    }
                    close(rs,stmt,con);
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
                    if ( tipos.get(i) == -3 || tipos.get(i) == 2 || tipos.get(i) == 12 || tipos.get(i) == -9
                        || tipos.get(i) == 1 || tipos.get(i) == 2005 || tipos.get(i) == -1 || tipos.get(i) == 93 )
                    {
                        if ( tipos.get(i) == 2 && tmp.startsWith("."))
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            tmp="to_date('"+tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+" "+tmp.substring(11, 19)+"','DD/MM/YYYY HH24:MI:SS')";
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

    public void selectCSV(String conn,String parm){
        
        boolean onlychar=false;
        String onlychar_=System.getenv("CSV_ONLYCHAR_Y");
        if ( onlychar_ != null && onlychar_.equals("S") )
            onlychar=true;
        
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
                            header+="\""+campos.get(i)+"\""+sepCSV;
                            first_detail+="\"\""+sepCSV;
                        }else{
                            sb.append("\"\"");
                            sb.append(sepCSV);
                        }
                        continue;
                    }
                    if ( tipos.get(i) == -3 || tipos.get(i) == 2 || tipos.get(i) == 12 || tipos.get(i) == -9
                        || tipos.get(i) == 1 || tipos.get(i) == 2005 || tipos.get(i) == -1 || tipos.get(i) == 93 )
                    {
                        if ( tipos.get(i) == 2 && tmp.startsWith(".") )
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+" "+tmp.substring(11, 19);
                        // o csv suporta ".."".." mas para ficar mais simples o comando abaixo tira o "
                        tmp=tmp.replace("\"","").replace("\n","");
                        tmp=tmp.trim();
                        
                        if ( first )
                        {
                            header+="\""+campos.get(i)+"\""+sepCSV;
                            first_detail+="\""+tmp+"\""+sepCSV;
                        }else{
                            // nao imprime delimitador em onlychar e tipos.get(i) == 2
                            if ( !onlychar || tipos.get(i) != 2 )
                                sb.append("\"");
                            sb.append(tmp);
                            if ( !onlychar || tipos.get(i) != 2 )
                                sb.append("\"");
                            sb.append(sepCSV);
                        }

                        continue;
                    }
                    close(rs,stmt,con);
                    throw new Exception("tipo desconhecido:"+tipos.get(i) + " -> " + rs.getString(campos.get(i)) );
                }

                if ( first ){
                    first=false;
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

            while( (line=readlineB()) != null ){
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
        for ( int i=0;i<=length-len_input;i++ )
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
    public String gravado_token(String dir_token,String value){
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
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(arquivo));
            out.write(texto);
            out.flush();
            out.close();
            return true;
        }catch(Exception e){
            System.err.println(e.toString());
        }        
        return false; 
    }

    public String lendo_arquivo(String caminho) {
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
        if ( stringcon.split("\\|").length != 3){
            System.err.println("Erro na conexão: Login e senha não encontrado!");
            return null;
        }else{
            String par = stringcon.split("\\|")[0];
            String user = stringcon.split("\\|")[1];
            String pass = stringcon.split("\\|")[2];
            try {
                return DriverManager.getConnection(par, user, pass);
            } catch (Exception x) {
                System.err.println("Erro na conexão:"+x.toString());
            }
        }
        return null;
    }

    public static void readLine(String caminho) throws Exception{
        readLine(new FileInputStream(new File(caminho)));
    }
    
    public static java.util.Scanner scanner_pipe=null;
    public static void readLine(InputStream in){        
        readLine(in,null);
    }    
    
    public static void readLine(InputStream in,String encoding){
        if ( encoding == null )
            scanner_pipe=new java.util.Scanner(in);
        else
            scanner_pipe=new java.util.Scanner(in,encoding);
        scanner_pipe.useDelimiter("\n");
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
        
    public String readlineB(){        
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
            while( (retorno=inputStream_pipe.read(buf,off,len)) == 0 ){}
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
        System.out.write(write1ByteBuff, 0, write1Byte_n);
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
        printf(args);
        System.out.println("");
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
        boolean first=false;
        boolean tail=false;
        
        String [] get_v_i_txt=get_v_i_txt(args);
        if ( get_v_i_txt == null ){
            comando_invalido(args);
            return;
        }
        boolean v_=get_v_i_txt[0].equals("S");
        boolean i_=get_v_i_txt[1].equals("S");        
        String txt=get_v_i_txt[2];        
        
        boolean print=false;
        String line=null;
        String line_="";
        
        if ( txt.startsWith("^") ){
            first=true;
            txt=txt.substring(1);
        }
        if ( txt.endsWith("$") ){
            tail=true;
            txt=txt.substring(0,txt.length()-2);
        }     
        if ( i_ )
            txt=txt.toUpperCase();
        
        try {            
            while ( (line_=line=readLine()) != null ) {
                if ( i_ )
                    line_=line_.toUpperCase();
                if ( !print && !first && !tail && line_.contains(txt) )
                    print=true;                
                if ( !print && first && !tail && line_.startsWith(txt) )
                    print=true;                
                if ( !print && !first && tail && line_.endsWith(txt) )
                    print=true;                
                if ( !print && first && tail && line_.startsWith(txt) && line_.endsWith(txt) )
                    print=true;  
                
                if ( v_ )
                    print=!print;
                
                if ( print )
                    System.out.println(line);
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public void wc_l()
    {
        try {
            long count=0;
            while ( (readLine()) != null )
                count++;
            closeLine();
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
        }
        while( sed_agulha1_count>0 ){
            write1Byte(sed_agulha1.get(0));
            sed_agulha1.remove(0);
            sed_agulha1_count--;            
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
            entrada=byte_to_int_java(a.get(i));
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
    
    public void bytesToInts()
    {      
        try {
            byte[] entrada_ = new byte[1];
            while ( read1Byte(entrada_) ){
                System.out.println( byte_to_int_java(entrada_[0]) );
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
            System.out.print("       ");
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
                i=byte_to_int_java(entrada_[0]);
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
                entrada=byte_to_int_java(entrada_[0]);
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
                entrada=byte_to_int_java(entrada_[0]);
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
            entrada=byte_to_int_java(entrada_[0]);
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
                System.out.write(buf, 0, len);
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
        String negativa=args[0]; // S/N
        String start=args[1]; // ".." ou null
        String end=args[2]; // ".." ou null
        
        int status=0; // 0 -> fora, 1 -> dentro do range
        
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
            byte[] buf = new byte[BUFFER_SIZE];
            while(readBytes(buf) > -1){}
            closeBytes();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void dev_in()
    {
        while(true)
            System.out.println(0);
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
            entrada=byte_to_int_java(buf[0]);
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
            entrada=byte_to_int_java(buf[0]);
            // suprimindo \r\n
            if ( entrada == 10 || entrada == 13 )
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

    public String lendo_arquivo_pacote(String caminho){
        // System.out.println(
        //   lendo_arquivo_pacote("/y/manual_mini")
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

    private static String getSeparadorCSV(){
        String sep_=System.getenv("CSV_SEP_Y");
        if ( sep_ == null || sep_.trim().equals("") )
            sep_=";";
        return sep_;
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
        if ( args.length != 3)
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
        if ( 
            ( args[1].contains("@") && senhaComArroba(args[1]) )
            || ( args[2].contains("@") && senhaComArroba(args[2]) )
        ){
            System.err.print("Comando inválido: A aplicação não suporta senha com arroba!");
            return;
        }
        pedeSenhaCasoNaoTenha(args);
        if ( args[1].contains("@") )
            new JSchCustom().scpFrom(new String[]{args[1],args[2]});                    
        else
            new JSchCustom().scpTo(new String[]{args[1],args[2]});                    
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
        if ( senhaComArroba(args[1]) ){
            System.err.print("Comando inválido: A aplicação não suporta senha com arroba!");
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
        pedeSenhaCasoNaoTenha(args);
        new JSchCustom().execSsh(new String[]{args[1],args[2]},port);
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
        if ( senhaComArroba(args[1]) ){
            System.err.print("Comando inválido: A aplicação não suporta senha com arroba!");
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
        pedeSenhaCasoNaoTenha(args);
        new JSchCustom().ssh(new String[]{args[1]},port);
        System.exit(0);
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
        if ( senhaComArroba(args[1]) ){
            System.err.print("Comando inválido: A aplicação não suporta senha com arroba!");
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
        pedeSenhaCasoNaoTenha(args);
        new JSchCustom().sftp(new String[]{args[1]},port);
        System.exit(0);
    }
    
    public void pedeSenhaCasoNaoTenha(String [] args){
        // ywanes@desktop's password:
        // String password = new String(console.readPassword("Password: "));
        for( int i=0;i<args.length;i++ ){
            if( args[i].contains("@") ){                
                if (  args[i].startsWith("@") || args[i].endsWith("@") ){
                    System.out.println("Error command");
                    System.exit(1);                    
                }
                if ( ! args[i].contains(",") ){
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
                    if ( password.contains("@") ){
                        System.out.println("Comando inválido: A aplicação não suporta senha com arroba!");
                        System.exit(1);
                    }
                    args[i]=args[i].split("@")[0]+","+password+"@"+args[i].split("@")[1];
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
        if ( args.length == 6 && ( args[5].equals("show") || args[5].equals("showOnlySend") || args[5].equals("showOnlyReceive") ) ){
            new Ponte().serverRouter(args[1],Integer.parseInt(args[2]),args[3],Integer.parseInt(args[4]),args[5]);
            return;
        }
        comando_invalido(args);
        System.exit(0);
    }

    private void TESTEserver(String[] args) {
        if ( args.length == 2 ){
            new Ponte().TESTEserver(null,Integer.parseInt(args[1]));
            return;
        }
        if ( args.length == 3 ){
            new Ponte().TESTEserver(args[1],Integer.parseInt(args[2]));
            return;
        }
        comando_invalido(args);
        System.exit(0);
    }

    private void TESTEclient(String[] args) {
        if ( args.length == 3 ){
            new Ponte().TESTEclient(args[1],Integer.parseInt(args[2]));
            return;
        }
        comando_invalido(args);
        System.exit(0);
    }

    private boolean senhaComArroba(String txt) {
        // verifica se txt tem uma quantidade de @ diferente de 1
        return txt.length() != (txt.replace("@","").length()+1);
    }

    public static int byte_to_int_java(byte a) {
        // os bytes em java vem 0..127 e -128..-1 totalizando 256
        // implementacao manual de Byte.toUnsignedInt(a)
        int i=(int)a;
        if ( i < 0 )
            i+=256;
        return i;
    }

    ArrayList<String> xlsxToCSV_nomes=null;
    ArrayList<String> shared=null;            
    private void xlsxToCSV(String caminhoXlsx, boolean mostraEstrutura, boolean listaAbas, int numeroAba, String nomeAba, OutputStream out) throws Exception {
        //"C:\\Users\\ywanes\\Documents\\teste.xlsx"
        //xlsxToCSV arquivo.xlsx mostraEstrutura
        //xlsxToCSV arquivo.xlsx listaAbas
        //xlsxToCSV arquivo.xlsx numeroAba 1
        //xlsxToCSV arquivo.xlsx nomeAba Planilha1
        
        try{            
            ZipFile zipFile = new ZipFile(caminhoXlsx);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();            
    
            InputStream is=null;            
            String caminho="";
            XML xml=null;
            XML xmlShared=null;
            XML xmlNomes=null;
            int sheet_count=0;
            String atributo_t=null;

            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();            
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
                    
                    is = zipFile.getInputStream(entry);
                    XML.loadIs(is,mostraEstrutura,false,caminho);
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
                    
                    // coloca em cache o xml consultado
                    if ( caminho.startsWith("xl/worksheets/") && caminho.endsWith("xml") && !mostraEstrutura && !listaAbas ){                        
                        if ( xlsxToCSV_nomes.size() == 0 )
                            XML.ErroFatal(99);                    
                        if ( numeroAba == -1 && nomeAba.equals(xlsxToCSV_nomes.get(sheet_count)) ){
                            xml=XML.getXML();
                        }
                        if ( numeroAba != -1 && numeroAba == sheet_count+1 ){
                            xml=XML.getXML();
                        }                        
                        sheet_count++;
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
                    }

                }
            }

            // processa xml selecionado com ajuda do dicionario
            if ( !mostraEstrutura && !listaAbas ){
                if ( xml == null ){
                    if ( numeroAba != -1 )
                        System.err.println("Erro, numeroAba: "+numeroAba+" não encontrada!");
                    else
                        System.err.println("Erro, nomeAba: "+nomeAba+" não encontrada!");
                    System.exit(1);
                }else{
                    processaCelulaInit();
                    for ( XML item1 : xml.getFilhos()){
                        if ( item1.getTag().equals("sheetData") ){
                            for ( XML item2 : item1.getFilhos()){
                                if ( item2.getTag().equals("row") ){
                                    for ( XML item3 : item2.getFilhos()){
                                        if ( item3.getTag().equals("c") ){
                                            for ( XML item4 : item3.getFilhos()){
                                                if ( item4.getTag().equals("v") ){
                                                    atributo_t=item3.getAtributo("t");
                                                    if ( atributo_t != null && atributo_t.equals("s") ){
                                                        processaCelula(
                                                            item3.getAtributo("r")
                                                            ,shared.get(Integer.parseInt(item4.getValue()))
                                                            ,out
                                                        );
                                                    }else{
                                                        processaCelula(
                                                            item3.getAtributo("r")
                                                            ,item4.getValue()
                                                            ,out
                                                        );
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }  
                    processaCelulaFlush(out);
                }
            }            
        }catch(Exception e){
            System.err.println("Erro "+e.toString());
            System.exit(1);
        }
        if ( out != null ){
            out.flush();
            out.close();
        }
    }

    private StringBuilder processaCelula_sb=new StringBuilder();
    private int processaCelula_tail_linha=-1;
    private int processaCelula_tail_coluna=-1;
    private int processaCelula_max_tail_coluna=-1;
    private void processaCelulaInit(){
        processaCelula_sb=new StringBuilder();
        processaCelula_tail_linha=-1;
        processaCelula_tail_coluna=-1;
        processaCelula_max_tail_coluna=-1;
    }
    
    private void processaCelula(String local, String valor, OutputStream out) throws Exception {
        //public static String linhasExcel="0123456789";    
        //public static String colunasExcel="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int linha=0;
        int linha_exp=0;
        int coluna=0;
        int coluna_exp=0;    
        
        int len=local.length();
        String entrada="";
        int pos=0;
        
        if ( valor == null )
            valor="";
        valor=valor.replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("\"","\"\"");
        
        for ( int i=len-1;i>=0;i-- ){ // obs: no excel a primeira linha é 1. A primeira coluna é A(aqui representada com 0)
            entrada=local.substring(i,i+1);
            pos=linhasExcel.indexOf(entrada);
            if ( pos != -1 ){ // linha
                linha+=Math.pow(linhasExcel_len, linha_exp++)*pos;
            }else{ // coluna
                pos=colunasExcel.indexOf(entrada);
                if ( pos != -1 ){ 
                    coluna+=Math.pow(colunasExcel_len, coluna_exp++)*pos;
                }else{
                    XML.ErroFatal(15);
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
                XML.ErroFatal(14);
            }
            while(processaCelula_tail_linha < linha){
                while(processaCelula_tail_coluna<processaCelula_max_tail_coluna){
                    processaCelula_sb.append("\"\"");
                    processaCelula_sb.append(sepCSV);
                    processaCelula_tail_coluna++;
                }
                processaCelula_tail_coluna=-1;
                out.write(processaCelula_sb.toString().getBytes());
                out.write("\n".getBytes());
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
        if ( processaCelula_tail_coluna > processaCelula_max_tail_coluna )
            processaCelula_max_tail_coluna=coluna;        
    }

    private void processaCelulaFlush(OutputStream out) throws Exception{
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
    
}

class Ponte {
    //exemplo
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"");                
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"show");                
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"showOnlySend");                
    //new Ponte().serverRouter("192.168.0.100",8080,"192.168.0.200",9090,"showOnlyReceive");                
    
    // teste server
    //new Ponte().TESTEserver("9090");                        
    // teste client
    //new Ponte().TESTEclient("localhost","8080");

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
        while(true){
            try{
                final Socket credencialSocket=ambiente.getCredencialSocket();
                new Thread(){
                    public void run(){
                        ponte0(credencialSocket,host1,port1,typeShow);
                    }
                }.start();   
            }catch(Exception e){
                System.out.println("FIM");
                break;
            }
        }
    }

    private void ponte0(Socket credencialSocket, String host1, int port1,String typeShow) {
        String id=padLeftZeros(new Random().nextInt(100000)+"",6);
        System.out.println("iniciando ponte id "+id);
        Origem origem=null;
        try{
            Destino destino=new Destino(host1,port1);                    
            origem=new Origem(credencialSocket,id,typeShow);
            origem.referencia(destino);
            destino.referencia(origem);
            origem.start(); // destino é startado no meio do start da origem;
        }catch(Exception e){
            System.out.println("termino inexperado de ponte id "+id+" - "+e.toString());
            origem.destroy();
        }
        System.out.println("finalizando ponte id "+id);
    }

    public static class OutputStreamCustom{ 
        public static int IDA=1;
        public static int VOLTA=2;

        private OutputStream os=null;
        private OutputStreamCustom(OutputStream os) {
            this.os=os;
        }
        
        public void write(int sentido_, byte[] buffer, int off, int len) throws IOException {
            os.write(buffer, off, len);
        }
    }

    private class Destino {
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
            final InputStream is=socket.getInputStream();                        
            os=new OutputStreamCustom(socket.getOutputStream());
            new Thread(){
                public void run(){
                    int len=0;   
                    byte[] buffer = new byte[2048];
                    try{
                        while( (len=is.read(buffer)) > -1 )
                            origem.volta(len,buffer);
                    }catch(Exception e){
                        System.out.println("desconectou destino "+e.toString());
                    }
                }
            }.start();                                    
        }

        private void ida(byte[] buffer,int len) throws Exception {            
            os.write(OutputStreamCustom.IDA,buffer,0,len);
        }
    }

    private class Origem {    
        String ponteID="";
        Socket socket=null;
        OutputStreamCustom os=null;
        Destino destino=null;
        boolean displayIda=false;
        boolean displayVolta=false;
        int port0;
        
        private Origem(Socket credencialSocket,String ponteID,String typeShow) {            
            socket=credencialSocket;
            this.ponteID=ponteID;
            if ( typeShow.equals("show") || typeShow.equals("showOnlySend"))
                displayIda=true;
            if ( typeShow.equals("show") || typeShow.equals("showOnlyReceive"))
                displayVolta=true;
        }
        private void referencia(Destino destino) {
            this.destino=destino;
        }

        private void start() throws Exception {
            // start destino
            destino.start();
            int len=0;
            byte[] buffer = new byte[2048];            
            InputStream is=null;
            os=null;
            BufferedInputStream bis=null;                            
            is=socket.getInputStream();
            os=new OutputStreamCustom(socket.getOutputStream());            
            bis=new BufferedInputStream(is);                            
            while( (len=bis.read(buffer)) != -1 ){
                // local de filtro ida
                if (displayIda)
                    mostra(len,"->",ponteID,buffer);
                destino.ida(buffer,len);
            }            
            try{ bis.close(); }catch(Exception e){}
            try{ is.close(); }catch(Exception e){}
        }

        private void volta(int len,byte[] buffer) throws Exception {
            // local de filtro volta
            if (displayVolta)
                mostra(len,"<-",ponteID,buffer);
            os.write(OutputStreamCustom.VOLTA,buffer,0,len);
        }

        private void destroy() {
            try{                
                socket.close();
            }catch(Exception e){}
        }

        private void mostra(int len,String direcao, String ponteID, byte[] buffer) {
            // INT
            System.out.println(
                direcao+"(id "+ponteID+" int):"
                +getInts(buffer,len)
            );
             
            // STR
            for (String parte : new String(buffer,0,len).split("\n") )                
                System.out.println(direcao+"(id "+ponteID+" str):"+parte);            
        }

        private String getInts(byte[] buffer,int len) {            
            int count=0;
            StringBuilder sb=new StringBuilder();
            for (byte b : buffer){
                sb.append(Y.OD_BC_R[Y.byte_to_int_java(b)]);
                if ( ++count >= len )
                    break;
            }
            return sb.toString();
        }

        private String getHexs(byte[] buffer,int len) {            
            int count=0;
            StringBuilder sb=new StringBuilder();
            for (byte b : buffer){
                sb.append(String.format("%02X",Y.byte_to_int_java(b)));
                if ( ++count >= len )
                    break;
            }
            return sb.toString();
        }

    }

    // preparando para receber varias conexoes
    public void TESTEserver(String host,int port){
        try{
            
            // exemplo host0 -> "192.168.0.100"
            if ( host == null || host.equals("localhost") ){
                try{
                    host=InetAddress.getLocalHost().getHostName();
                }catch(Exception e){
                    System.out.println("warning: procurando ip ...");
                    host=getListaIPs().get(0);
                    System.out.println("warning: ip localizado -> "+host);
                }                    
            }
            
            ServerSocket serverSocket = new ServerSocket(port, 1,InetAddress.getByName(host));
            System.out.println("servidor porta "+port+" criado.");
            while (true) {
                final Socket socket=serverSocket.accept();
                System.out.println("recebendo conexao..");
                new Thread(){
                    public void run(){
                        try {
                            TESTEserver0(socket);
                        } catch (Exception e) {
                            System.out.println("Erro ao executar servidor:" + e.toString());
                        }
                        System.out.println("finalizando conexao..");
                    }
                }.start();
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    // operando uma unica comunicação
    private void TESTEserver0(Socket socket) throws Exception{
        int len=0;
        byte[] buffer = new byte[2048];            
        InputStream is=null;
        BufferedInputStream bis=null;
        is = socket.getInputStream();
        bis=new BufferedInputStream(is);            
        while( (len=bis.read (buffer)) != -1 )
        {
            System.out.println(
                new String(buffer)
            );
        }
        try{ bis.close(); }catch(Exception e){}
        try{ is.close(); }catch(Exception e){}
    }

    public void TESTEclient(String host, int port){
        try{
            System.out.println("cliente iniciado.");
            OutputStream os=null;

            Socket socket=new Socket(host, port);
            os=socket.getOutputStream();
            os.write(new byte[]{1,2,3,70});
            try {Thread.sleep(3000);}catch (Exception e) { }        
            os.write(new byte[]{1,2,3,70});
            try {Thread.sleep(3000);}catch (Exception e) { }        
            os.write(new byte[]{1,2,3,70});
            try {Thread.sleep(3000);}catch (Exception e) { }        
            os.write(new byte[]{1,2,3,70});
            try {Thread.sleep(3000);}catch (Exception e) { }        
            os.write(new byte[]{1,2,3,70});
            try {Thread.sleep(3000);}catch (Exception e) { }        
            try{ os.close(); }catch(Exception e){}
        }catch(Exception e){
            System.out.println(e.toString());
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

class XML{
    private static void mostraEstrutura(ArrayList<String> listaTxt, ArrayList<Integer> listaNivel,String caminho) {
        int len=listaTxt.size();
        int nivel=0;
        System.out.println();
        if ( caminho != null )
            System.out.println("=> "+caminho);        
        for (int i=0;i<len;i++ ){
            nivel=listaNivel.get(i)-1;
            for (int j=0;j<nivel;j++ )
                System.out.print("\t");
            System.out.println(listaTxt.get(i));
        }
    }
    
    private static void mostraTags(ArrayList<String> listaTxt, ArrayList<Integer> listaNivel,String caminho) {
        int len=listaTxt.size();
        int nivel=0;
        int nivelTail=-1;
        ArrayList<String> pilha=new ArrayList<String>();
        ArrayList<String> tags=new ArrayList<String>();
        String tag="";
        if ( caminho != null )
            System.out.println("=> "+caminho);        
        for (int i=0;i<len;i++ ){
            nivel=listaNivel.get(i)-1;
            if ( nivel == nivelTail+1 ){
                if ( ! listaTxt.get(i).trim().startsWith("<") ) continue;
                if ( listaTxt.get(i).trim().startsWith("</") ){
                    ErroFatal(54);
                }
                pilha.add(getTagFromLine(listaTxt.get(i)));
                nivelTail=nivel;
                tag="";
                for ( int j=0;j<pilha.size();j++ )
                    tag+="/"+pilha.get(j);
                boolean achou=false;
                for ( int j=0;j<tags.size();j++ ){
                    if ( tags.get(j).equals(tag) ){
                        achou=true;
                        break;
                    }
                }
                if ( ! achou )
                    tags.add(tag);
                continue;
            }
            if ( nivel == nivelTail ){
                if ( ! listaTxt.get(i).trim().startsWith("<") ) continue;
                if ( listaTxt.get(i).trim().startsWith("</") ) continue;
                // remove a ultima posicao
                pilha.remove(pilha.size()-1); 
                pilha.add(getTagFromLine(listaTxt.get(i)));
                nivelTail=nivel;
                tag="";
                for ( int j=0;j<pilha.size();j++ )
                    tag+="/"+pilha.get(j);
                boolean achou=false;
                for ( int j=0;j<tags.size();j++ ){
                    if ( tags.get(j).equals(tag) ){
                        achou=true;
                        break;
                    }
                }
                if ( ! achou )
                    tags.add(tag);
                continue;
            }
            if ( nivel == nivelTail-1 ){
                if ( ! listaTxt.get(i).trim().startsWith("<") ) continue;
                if ( listaTxt.get(i).trim().startsWith("</") ){
                    pilha.remove(pilha.size()-1); 
                    nivelTail=nivel;
                    continue;
                }
                // remove as duas ultimas posicoes
                pilha.remove(pilha.size()-1);
                pilha.remove(pilha.size()-1); 
                pilha.add(getTagFromLine(listaTxt.get(i)));
                nivelTail=nivel;
                tag="";
                for ( int j=0;j<pilha.size();j++ )
                    tag+="/"+pilha.get(j);
                boolean achou=false;
                for ( int j=0;j<tags.size();j++ ){
                    if ( tags.get(j).equals(tag) ){
                        achou=true;
                        break;
                    }
                }
                if ( ! achou )
                    tags.add(tag);
                continue;
            }
            ErroFatal(44);
        }
        for (int i=0;i<tags.size();i++ )            
            System.out.println(tags.get(i));
    }

    private HashMap atributos=new HashMap();
    private String value=null;
    private ArrayList<XML> filhos=new ArrayList<XML>();
    private String tag=null;    
    
    private static ArrayList<String> listaTxt=null;
    private static ArrayList<Integer> listaNivel=null;
    public static void loadIs(InputStream is,boolean mostraEstrutura,boolean mostraTags,String caminho) {
        listaTxt=new ArrayList<String>();
        listaNivel=new ArrayList<Integer>();
        
        Y.readLine(is,"UTF-8");
        StringBuilder sb=new StringBuilder();
        String line="";
        String txt=null;
        boolean first=true;
        while ( (line=Y.readLine()) != null ){            
            if ( first ){
                first=false;
                int detectBOM=line.indexOf("<?xml");
                if ( detectBOM >=1 && detectBOM <= 3 )
                    line=line.substring(detectBOM);
                if ( line.startsWith("<?xml") ){
                    int tmp=line.indexOf("?>");
                    if ( tmp > -1 ){
                        line=line.substring(tmp+2);
                    }else{
                        ErroFatal(44);
                    }
                }
            }
            sb.append(line);            
        }
        Y.closeLine();
        txt=sb.toString();
        
        String entrada=null;
        String tail=null;
        sb=new StringBuilder();
        int len=txt.length();
        int nivel=1;
        boolean tag_in=false;
        boolean tag_finish=false;
        boolean tag_value=false;
        boolean tail_tag_abertura=false; //
        
        // tag de abertura   <a>
        // tag de fechamento </a>
        // tag unica         <a/>
        
        for( int i=0;i<len;i++ ){
            entrada=txt.substring(i,i+1);
            if ( nivel < 1 ){
                ErroFatal(1);
            }     
            if ( tag_in && tag_value ){
                ErroFatal(2);
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
                    ErroFatal(3);
                }
                sb.append(tail);
                sb.append(entrada);
                tail=null;
                tag_in=false;
                tag_finish=false; // segurança
                listaTxt.add(sb.toString());
                listaNivel.add(nivel);
                sb=new StringBuilder();
                tail_tag_abertura=false;
                continue;                
            }
            if ( tag_in && entrada.equals(">") ){ // tag abertura ou tag fechamento
                sb.append(tail);
                sb.append(entrada);
                tail=null;
                tag_in=false;                
                listaTxt.add(sb.toString());
                listaNivel.add(nivel);
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
                listaTxt.add(sb.toString());
                listaNivel.add(nivel);
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
                listaTxt.add(sb.toString());
                listaNivel.add(nivel);
                sb=new StringBuilder();
                continue;            
            }
            sb.append(tail);                    
            tail=entrada;
            continue;            
        }
        if ( tail != null ){
            ErroFatal(4);
        }        
        if ( mostraEstrutura )
            mostraEstrutura(listaTxt,listaNivel,caminho);
        if ( mostraTags )
            mostraTags(listaTxt,listaNivel,caminho);
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
                    ErroFatal(8);
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
                    ErroFatal(9);                
                if ( ! tag.equals(inicio_tag) )
                    ErroFatal(10);
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
            ErroFatal(12);
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
                    ErroFatal(11);
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
    
    public static void ErroFatal(int n) {
        System.err.println("Erro Fatal! "+n);
        System.exit(1);
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


/* class JSchCustom */ class JSchCustom{void scpFrom(String[] args) {ScpFrom.custom(args);}void scpTo(String[] args) {     ScpTo.custom(args);     }      void execSsh(String[] args,int port) {         ExecSsh.custom(args,port);     }void ssh(String[] args,int port) {         Ssh.custom(args,port);     }      void sftp(String[] args,int port) {         Sftp.custom(args,port);     } }  class ScpFrom{    public static void custom(String[] arg){     if(arg.length!=2 || !arg[0].contains(",") || !arg[0].contains("@")){       System.err.println("usage: y scp user,pass@remotehost:file1 file2");       System.exit(-1);     }                FileOutputStream fos=null;     try{        String senha=arg[0].split("@")[0].split(",")[1];       arg=new String[]{arg[0].split("@")[0].split(",")[0]+"@"+arg[0].split("@")[1],arg[1]};                String user=arg[0].substring(0, arg[0].indexOf('@'));       arg[0]=arg[0].substring(arg[0].indexOf('@')+1);       String host=arg[0].substring(0, arg[0].indexOf(':'));       String rfile=arg[0].substring(arg[0].indexOf(':')+1);       String lfile=arg[1];        String prefix=null;       if(new File(lfile).isDirectory()){         prefix=lfile+File.separator;       }        JSch jsch=new JSch();       Session session=jsch.getSession(user, host, 22);        UserInfo ui=new MyUserInfo(senha);       session.setUserInfo(ui);       session.connect();        String command="scp -f "+rfile;       Channel channel=session.openChannel("exec");       ((ChannelExec)channel).setCommand(command);        OutputStream out=channel.getOutputStream();       InputStream in=channel.getInputStream();        channel.connect();        byte[] buf=new byte[1024];        buf[0]=0; out.write(buf, 0, 1); out.flush();        while(true){ 	int c=checkAck(in);         if(c!='C'){ 	  break; 	}          in.read(buf, 0, 5);          long filesize=0L;         while(true){           if(in.read(buf, 0, 1)<0){             break;            }           if(buf[0]==' ')break;           filesize=filesize*10L+(long)(buf[0]-'0');         }          String file=null;         for(int i=0;;i++){           in.read(buf, i, 1);           if(buf[i]==(byte)0x0a){             file=new String(buf, 0, i);             break;   	  }         }          buf[0]=0; out.write(buf, 0, 1); out.flush();          fos=new FileOutputStream(prefix==null ? lfile : prefix+file);         int foo;         while(true){           if(buf.length<filesize) foo=buf.length; 	  else foo=(int)filesize;           foo=in.read(buf, 0, foo);           if(foo<0){             break;           }           fos.write(buf, 0, foo);           filesize-=foo;           if(filesize==0L) break;         }         fos.close();         fos=null;  	if(checkAck(in)!=0){ 	  System.exit(0); 	}          buf[0]=0; out.write(buf, 0, 1); out.flush();       }        session.disconnect();        System.exit(0);     }     catch(Exception e){       System.out.println(e);       try{if(fos!=null)fos.close();}catch(Exception ee){}     }   }    static int checkAck(InputStream in) throws IOException{     int b=in.read();     if(b==0) return b;     if(b==-1) return b;      if(b==1 || b==2){       StringBuffer sb=new StringBuffer();       int c;       do { 	c=in.read(); 	sb.append((char)c);       }       while(c!='\n');       if(b==1){  	System.out.print(sb.toString());       }       if(b==2){  	System.out.print(sb.toString());       }     }     return b;   }      public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{     String passwd;     String senha;              private MyUserInfo(String senha) {             this.senha=senha;         }              public String getPassword(){ return passwd;}public boolean promptYesNo(String str){return true;}JTextField passwordField=(JTextField)new JPasswordField(20);public String getPassphrase(){ return null; } 
/* class JSchCustom */ public boolean promptPassphrase(String message){ return true; }          public boolean promptPassword(String message){         passwd=senha;         return true;     }          public void showMessage(String message){         System.err.println("nao implementado!");         System.exit(1);     }          final GridBagConstraints gbc =        new GridBagConstraints(0,0,1,1,1,1,                              GridBagConstraints.NORTHWEST,                              GridBagConstraints.NONE,                              new Insets(0,0,0,0),0,0);     private Container panel;     public String[] promptKeyboardInteractive(String destination,                                               String name,                                               String instruction,                                               String[] prompt,                                               boolean[] echo){         System.err.println("nao implementado!");         System.exit(1);         return null;     }   } }   class ScpTo{        public static void custom(String[] arg){     if(arg.length!=2 || !arg[1].contains(",") || !arg[1].contains("@") ){       System.err.println("usage: y scp file1 user,pass@remotehost:file2");       System.exit(-1);     }            FileInputStream fis=null;     try{        String senha=arg[1].split("@")[0].split(",")[1];       arg=new String[]{arg[0],arg[1].split("@")[0].split(",")[0]+"@"+arg[1].split("@")[1]};              String lfile=arg[0];       String user=arg[1].substring(0, arg[1].indexOf('@'));       arg[1]=arg[1].substring(arg[1].indexOf('@')+1);       String host=arg[1].substring(0, arg[1].indexOf(':'));       String rfile=arg[1].substring(arg[1].indexOf(':')+1);        JSch jsch=new JSch();       Session session=jsch.getSession(user, host, 22);        UserInfo ui=new MyUserInfo(senha);       session.setUserInfo(ui);       session.connect();        boolean ptimestamp = true;        String command="scp " + (ptimestamp ? "-p" :"") +" -t "+rfile;       Channel channel=session.openChannel("exec");       ((ChannelExec)channel).setCommand(command);        OutputStream out=channel.getOutputStream();       InputStream in=channel.getInputStream();        channel.connect();        if(checkAck(in)!=0){ 	System.exit(0);       }        File _lfile = new File(lfile);        if(ptimestamp){         command="T"+(_lfile.lastModified()/1000)+" 0";         command+=(" "+(_lfile.lastModified()/1000)+" 0\n");          out.write(command.getBytes()); out.flush();         if(checkAck(in)!=0){   	  System.exit(0);         }       }        long filesize=_lfile.length();       command="C0644 "+filesize+" ";       if(lfile.lastIndexOf('/')>0){         command+=lfile.substring(lfile.lastIndexOf('/')+1);       }       else{         command+=lfile;       }       command+="\n";       out.write(command.getBytes()); out.flush();       if(checkAck(in)!=0){ 	System.exit(0);       }        fis=new FileInputStream(lfile);       byte[] buf=new byte[1024];       while(true){         int len=fis.read(buf, 0, buf.length); 	if(len<=0) break;         out.write(buf, 0, len);        }       fis.close();       fis=null;       buf[0]=0; out.write(buf, 0, 1); out.flush();       if(checkAck(in)!=0){ 	System.exit(0);       }       out.close();        channel.disconnect();       session.disconnect();        System.exit(0);     }     catch(Exception e){       System.out.println(e);       try{if(fis!=null)fis.close();}catch(Exception ee){}     }   }    static int checkAck(InputStream in) throws IOException{     int b=in.read();     if(b==0) return b;     if(b==-1) return b;      if(b==1 || b==2){       StringBuffer sb=new StringBuffer();       int c;       do { 	c=in.read(); 	sb.append((char)c);       }       while(c!='\n');       if(b==1){  	System.out.print(sb.toString());       }       if(b==2){
/* class JSchCustom */ System.out.print(sb.toString());       }     }     return b;   }      public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{     String passwd;     String senha;              private MyUserInfo(String senha) {             this.senha=senha;         }              public String getPassword(){ return passwd; }     public boolean promptYesNo(String str){        return true;     }          JTextField passwordField=(JTextField)new JPasswordField(20);      public String getPassphrase(){ return null; }     public boolean promptPassphrase(String message){ return true; }          public boolean promptPassword(String message){         passwd=senha;         return true;     }          public void showMessage(String message){         System.err.println("nao implementado!");         System.exit(1);     }          final GridBagConstraints gbc =        new GridBagConstraints(0,0,1,1,1,1,                              GridBagConstraints.NORTHWEST,                              GridBagConstraints.NONE,                              new Insets(0,0,0,0),0,0);     private Container panel;     public String[] promptKeyboardInteractive(String destination,                                               String name,                                               String instruction,                                               String[] prompt,                                               boolean[] echo){         System.err.println("nao implementado!");         System.exit(1);         return null;     }   } }  class ExecSsh{       public static void custom(String[] arg,int port){     try{              JSch jsch=new JSch();                      if(arg.length!=2 || !arg[0].contains(",") || !arg[0].contains("@")){         System.err.println("usage: y execSsh user,pass@remotehost command");         System.exit(-1);       }                    String user=arg[0].split("@")[0].split(",")[0];       String senha=arg[0].split("@")[0].split(",")[1];       String host=arg[0].split("@")[1];       String command=arg[1];       Session session=jsch.getSession(user, host, port);       UserInfo ui=new MyUserInfo(senha);       session.setUserInfo(ui);       session.connect();       Channel channel=session.openChannel("exec");       ((ChannelExec)channel).setCommand(command);       channel.setInputStream(null);       ((ChannelExec)channel).setErrStream(System.err);       InputStream in=channel.getInputStream();       channel.connect();        byte[] tmp=new byte[1024];       while(true){         while(in.available()>0){           int i=in.read(tmp, 0, 1024);           if(i<0)break;           System.out.print(new String(tmp, 0, i));         }         if(channel.isClosed()){           if(in.available()>0) continue;           System.out.println("exit-status: "+channel.getExitStatus());           break;         }         try{Thread.sleep(1000);}catch(Exception ee){}       }       channel.disconnect();       session.disconnect();     }     catch(Exception e){       System.out.println(e);     }   }      public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{     String passwd;     String senha;              private MyUserInfo(String senha) {             this.senha=senha;         }              public String getPassword(){ return passwd; }     public boolean promptYesNo(String str){        return true;     }          JTextField passwordField=(JTextField)new JPasswordField(20);      public String getPassphrase(){ return null; }     public boolean promptPassphrase(String message){ return true; }          public boolean promptPassword(String message){         passwd=senha;         return true;     }          public void showMessage(String message){         System.err.println("nao implementado!");         System.exit(1);     }          final GridBagConstraints gbc =        new GridBagConstraints(0,0,1,1,1,1,
/* class JSchCustom */ GridBagConstraints.NORTHWEST,                              GridBagConstraints.NONE,                              new Insets(0,0,0,0),0,0);     private Container panel;     public String[] promptKeyboardInteractive(String destination,                                               String name,                                               String instruction,                                               String[] prompt,                                               boolean[] echo){         System.err.println("nao implementado!");         System.exit(1);         return null;     }   } }  class Ssh{   public static void custom(String[] arg,int port){     Channel channel=null;          try{       JSch jsch=new JSch();        if(arg.length!=1 || !arg[0].contains(",") || !arg[0].contains("@")){         System.err.println("usage: y ssh user,pass@remotehost");         System.exit(-1);       }              String user=arg[0].split("@")[0].split(",")[0];       String senha=arg[0].split("@")[0].split(",")[1];       String host=arg[0].split("@")[1];              Session session=jsch.getSession(user, host, port);        session.setPassword(senha);        UserInfo ui = new MyUserInfo(){         public void showMessage(String message){           JOptionPane.showMessageDialog(null, message);         }         public boolean promptYesNo(String message){return true;}       };        session.setUserInfo(ui);       session.connect(30000);       channel=session.openChannel("shell");       channel.setInputStream(System.in);       channel.setOutputStream(System.out);       channel.connect(3*1000);     }     catch(Exception e){       System.out.println(e);     }          while (channel != null && !channel.isEOF()){}   }    public static abstract class MyUserInfo                           implements UserInfo, UIKeyboardInteractive{     public String getPassword(){ return null; }     public boolean promptYesNo(String str){ return false; }     public String getPassphrase(){ return null; }     public boolean promptPassphrase(String message){ return false; }     public boolean promptPassword(String message){ return false; }     public void showMessage(String message){ }     public String[] promptKeyboardInteractive(String destination,                                               String name,                                               String instruction,                                               String[] prompt,                                               boolean[] echo){       return null;     }   } }  class Sftp{   public static void custom(String[] arg,int port){      try{       JSch jsch=new JSch();       if(arg.length == 1)           arg=new String[]{arg[0],port+""};              if(arg.length!=2 || !arg[0].contains(",") || !arg[0].contains("@") ){         System.err.println("usage: y sftp user,pass@remotehost");         System.err.println("usage: y sftp user,pass@remotehost 22");         System.exit(-1);       }                    try{         port=Integer.parseInt(arg[1]);       }catch(Exception e){         System.err.println("usage: y sftp user,pass@remotehost");         System.err.println("usage: y sftp user,pass@remotehost 22");         System.exit(-1);       }            String senha=arg[0].split("@")[0].split(",")[1];       arg=new String[]{arg[0].split("@")[0].split(",")[0]+"@"+arg[0].split("@")[1]};              String user=arg[0].split("@")[0];       String host=arg[0].split("@")[1];              Session session=jsch.getSession(user, host, port);        UserInfo ui=new MyUserInfo(senha);       session.setUserInfo(ui);        session.connect();        Channel channel=session.openChannel("sftp");       channel.connect();       ChannelSftp c=(ChannelSftp)channel;        java.io.InputStream in=System.in;       java.io.PrintStream out=System.out;        
/* class JSchCustom */ java.util.Vector cmds=new java.util.Vector();       byte[] buf=new byte[1024];       int i;       String str;       int level=0;        while(true){         out.print("sftp> "); 	cmds.removeAllElements();         i=in.read(buf, 0, 1024); 	if(i<=0)break;          i--;         if(i>0 && buf[i-1]==0x0d)i--; 	int s=0; 	for(int ii=0; ii<i; ii++){           if(buf[ii]==' '){             if(ii-s>0){ cmds.addElement(new String(buf, s, ii-s)); } 	    while(ii<i){if(buf[ii]!=' ')break; ii++;} 	    s=ii; 	  } 	} 	if(s<i){ cmds.addElement(new String(buf, s, i-s)); } 	if(cmds.size()==0)continue;  	String cmd=(String)cmds.elementAt(0); 	if(cmd.equals("quit")){           c.quit(); 	  break; 	} 	if(cmd.equals("exit")){           c.exit(); 	  break; 	}  	if(cmd.equals("rekey")){  	  session.rekey();  	  continue;  	}  	if(cmd.equals("compression")){           if(cmds.size()<2){ 	    out.println("compression level: "+level);             continue; 	  } 	  try{ 	    level=Integer.parseInt((String)cmds.elementAt(1)); 	    if(level==0){ 	      session.setConfig("compression.s2c", "none"); 	      session.setConfig("compression.c2s", "none"); 	    } 	    else{               session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");               session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none"); 	    } 	  } 	  catch(Exception e){}           session.rekey();  	  continue; 	} 	if(cmd.equals("cd") || cmd.equals("lcd")){           if(cmds.size()<2) continue; 	  String path=(String)cmds.elementAt(1); 	  try{ 	    if(cmd.equals("cd")) c.cd(path); 	    else c.lcd(path); 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("rm") || cmd.equals("rmdir") || cmd.equals("mkdir")){           if(cmds.size()<2) continue; 	  String path=(String)cmds.elementAt(1); 	  try{ 	    if(cmd.equals("rm")) c.rm(path); 	    else if(cmd.equals("rmdir")) c.rmdir(path); 	    else c.mkdir(path); 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("chgrp") || cmd.equals("chown") || cmd.equals("chmod")){           if(cmds.size()!=3) continue; 	  String path=(String)cmds.elementAt(2); 	  int foo=0; 	  if(cmd.equals("chmod")){             byte[] bar=((String)cmds.elementAt(1)).getBytes();             int k;             for(int j=0; j<bar.length; j++){               k=bar[j]; 	      if(k<'0'||k>'7'){foo=-1; break;}   	      foo<<=3; 	      foo|=(k-'0'); 	    } 	    if(foo==-1)continue; 	  } 	  else{   	    try{foo=Integer.parseInt((String)cmds.elementAt(1));} 	    catch(Exception e){continue;} 	  } 	  try{ 	    if(cmd.equals("chgrp")){ c.chgrp(foo, path); } 	    else if(cmd.equals("chown")){ c.chown(foo, path); } 	    else if(cmd.equals("chmod")){ c.chmod(foo, path); } 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("pwd") || cmd.equals("lpwd")){            str=(cmd.equals("pwd")?"Remote":"Local"); 	   str+=" working directory: ";           if(cmd.equals("pwd")) str+=c.pwd(); 	  else str+=c.lpwd(); 	  out.println(str); 	  continue; 	} 	if(cmd.equals("ls") || cmd.equals("dir")){ 	  String path="."; 	  if(cmds.size()==2) path=(String)cmds.elementAt(1); 	  try{ 	    java.util.Vector vv=c.ls(path); 	    if(vv!=null){ 	      for(int ii=0; ii<vv.size(); ii++){                 Object obj=vv.elementAt(ii);                 if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){                   out.println(((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getLongname());
/* class JSchCustom */ }}}}catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("lls") || cmd.equals("ldir")){ 	  String path="."; 	  if(cmds.size()==2) path=(String)cmds.elementAt(1); 	  try{ 	    java.io.File file=new java.io.File(path); 	    if(!file.exists()){ 	      out.println(path+": No such file or directory");               continue;              } 	    if(file.isDirectory()){ 	      String[] list=file.list(); 	      for(int ii=0; ii<list.length; ii++){ 		out.println(list[ii]); 	      } 	      continue; 	    } 	    out.println(path); 	  } 	  catch(Exception e){ 	    System.out.println(e); 	  } 	  continue; 	} 	if(cmd.equals("get") ||  	   cmd.equals("get-resume") || cmd.equals("get-append") ||  	   cmd.equals("put") ||  	   cmd.equals("put-resume") || cmd.equals("put-append") 	   ){ 	  if(cmds.size()!=2 && cmds.size()!=3) continue; 	  String p1=(String)cmds.elementAt(1); 	  String p2="."; 	  if(cmds.size()==3)p2=(String)cmds.elementAt(2); 	  try{ 	    SftpProgressMonitor monitor=new MyProgressMonitor(); 	    if(cmd.startsWith("get")){ 	      int mode=ChannelSftp.OVERWRITE; 	      if(cmd.equals("get-resume")){ mode=ChannelSftp.RESUME; } 	      else if(cmd.equals("get-append")){ mode=ChannelSftp.APPEND; }  	      c.get(p1, p2, monitor, mode); 	    } 	    else{  	      int mode=ChannelSftp.OVERWRITE; 	      if(cmd.equals("put-resume")){ mode=ChannelSftp.RESUME; } 	      else if(cmd.equals("put-append")){ mode=ChannelSftp.APPEND; }  	      c.put(p1, p2, monitor, mode);  	    } 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("ln") || cmd.equals("symlink") ||            cmd.equals("rename") || cmd.equals("hardlink")){           if(cmds.size()!=3) continue; 	  String p1=(String)cmds.elementAt(1); 	  String p2=(String)cmds.elementAt(2); 	  try{ 	    if(cmd.equals("hardlink")){  c.hardlink(p1, p2); } 	    else if(cmd.equals("rename")) c.rename(p1, p2); 	    else c.symlink(p1, p2); 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("df")){           if(cmds.size()>2) continue;           String p1 = cmds.size()==1 ? ".": (String)cmds.elementAt(1);           SftpStatVFS stat = c.statVFS(p1);            long size = stat.getSize();           long used = stat.getUsed();           long avail = stat.getAvailForNonRoot();           long root_avail = stat.getAvail();           long capacity = stat.getCapacity();            System.out.println("Size: "+size);           System.out.println("Used: "+used);           System.out.println("Avail: "+avail);           System.out.println("(root): "+root_avail);           System.out.println("%Capacity: "+capacity);            continue;         } 	if(cmd.equals("stat") || cmd.equals("lstat")){           if(cmds.size()!=2) continue; 	  String p1=(String)cmds.elementAt(1); 	  SftpATTRS attrs=null; 	  try{ 	    if(cmd.equals("stat")) attrs=c.stat(p1); 	    else attrs=c.lstat(p1); 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  if(attrs!=null){             out.println(attrs); 	  } 	  else{ 	  } 	  continue; 	} 	if(cmd.equals("readlink")){           if(cmds.size()!=2) continue; 	  String p1=(String)cmds.elementAt(1); 	  String filename=null; 	  try{ 	    filename=c.readlink(p1);             out.println(filename); 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("realpath")){           if(cmds.size()!=2) continue; 	  String p1=(String)cmds.elementAt(1); 	  String filename=null;
/* class JSchCustom */ try{filename=c.realpath(p1);             out.println(filename); 	  } 	  catch(SftpException e){ 	    System.out.println(e.toString()); 	  } 	  continue; 	} 	if(cmd.equals("version")){ 	  out.println("SFTP protocol version "+c.version()); 	  continue; 	} 	if(cmd.equals("help") || cmd.equals("?")){ 	  out.println(help); 	  continue; 	}         out.println("unimplemented command: "+cmd);       }       session.disconnect();     }     catch(Exception e){       System.out.println(e);     }     System.exit(0);   }     private static String help = "      Available commands:\n"+ "      * means unimplemented command.\n"+ "cd path                       Change remote directory to 'path'\n"+ "lcd path                      Change local directory to 'path'\n"+ "chgrp grp path                Change group of file 'path' to 'grp'\n"+ "chmod mode path               Change permissions of file 'path' to 'mode'\n"+ "chown own path                Change owner of file 'path' to 'own'\n"+ "df [path]                     Display statistics for current directory or\n"+ "                              filesystem containing 'path'\n"+ "help                          Display this help text\n"+ "get remote-path [local-path]  Download file\n"+ "get-resume remote-path [local-path]  Resume to download file.\n"+ "get-append remote-path [local-path]  Append remote file to local file\n"+ "hardlink oldpath newpath      Hardlink remote file\n"+ "*lls [ls-options [path]]      Display local directory listing\n"+ "ln oldpath newpath            Symlink remote file\n"+ "*lmkdir path                  Create local directory\n"+ "lpwd                          Print local working directory\n"+ "ls [path]                     Display remote directory listing\n"+ "*lumask umask                 Set local umask to 'umask'\n"+ "mkdir path                    Create remote directory\n"+ "put local-path [remote-path]  Upload file\n"+ "put-resume local-path [remote-path]  Resume to upload file\n"+ "put-append local-path [remote-path]  Append local file to remote file.\n"+ "pwd                           Display remote working directory\n"+ "stat path                     Display info about path\n"+ "exit                          Quit sftp\n"+ "quit                          Quit sftp\n"+ "rename oldpath newpath        Rename remote file\n"+ "rmdir path                    Remove remote directory\n"+ "rm path                       Delete remote file\n"+ "symlink oldpath newpath       Symlink remote file\n"+ "readlink path                 Check the target of a symbolic link\n"+ "realpath path                 Canonicalize the path\n"+ "rekey                         Key re-exchanging\n"+ "compression level             Packet compression will be enabled\n"+ "version                       Show SFTP version\n"+ "?                             Synonym for help";               public static class MyProgressMonitor implements SftpProgressMonitor{     ProgressMonitor monitor;     long count=0;     long max=0;     public void init(int op, String src, String dest, long max){       this.max=max;       monitor=new ProgressMonitor(null,                                    ((op==SftpProgressMonitor.PUT)?                                     "put" : "get")+": "+src,                                    "",  0, (int)max);       count=0;       percent=-1;       monitor.setProgress((int)this.count);       monitor.setMillisToDecideToPopup(1000);     }     private long percent=-1;     public boolean count(long count){       this.count+=count;        if(percent>=this.count*100/max){ return true; }       percent=this.count*100/max;        monitor.setNote("Completed "+this.count+"("+percent+"%) out of "+max+".");            monitor.setProgress((int)this.count);        return !(monitor.isCanceled());     }     public void end(){       
/* class JSchCustom */ monitor.close();     }   }        public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{     String passwd;     String senha;              private MyUserInfo(String senha) {             this.senha=senha;         }              public String getPassword(){ return passwd; }     public boolean promptYesNo(String str){        return true;     }          JTextField passwordField=(JTextField)new JPasswordField(20);      public String getPassphrase(){ return null; }     public boolean promptPassphrase(String message){ return true; }          public boolean promptPassword(String message){         passwd=senha;         return true;     }          public void showMessage(String message){         System.err.println("nao implementado!");         System.exit(1);     }          final GridBagConstraints gbc =        new GridBagConstraints(0,0,1,1,1,1,                              GridBagConstraints.NORTHWEST,                              GridBagConstraints.NONE,                              new Insets(0,0,0,0),0,0);     private Container panel;     public String[] promptKeyboardInteractive(String destination,                                               String name,                                               String instruction,                                               String[] prompt,                                               boolean[] echo){         System.err.println("nao implementado!");         System.exit(1);         return null;     }   } } 


/* class HttpServer */ // parametros
/* class HttpServer */ // new HttpServer(...)
/* class HttpServer */ // host(pode ser ""), titulo_url, titulo, port, dir, endsWiths(ex: "","jar,zip"), ips_banidos(ex: "","8.8.8.8,4.4.4.4")
/* class HttpServer */ class HttpServer {String host,titulo_url,titulo,dir,nav,endsWiths,ips_banidos;int port;Socket socket = null;public HttpServer(String[] args){    host=args[0];    if ( args[0] == null || args[0].equals("localhost") )try{host=InetAddress.getLocalHost().getHostName();}catch(Exception e){}        titulo_url = args[1];titulo = args[2];port = Integer.parseInt(args[3]);dir = args[4].trim();if ( ! dir.endsWith("/") ) dir+="/";endsWiths = args[5];ips_banidos = args[6];try{serve();}catch(Exception e){System.err.println(e.toString());System.exit(1);}}public void serve() throws Exception {ServerSocket serverSocket = null;String origem="";try {serverSocket = new ServerSocket(port, 1,InetAddress.getByName(host));System.out.println("Service opened: http://"+host+":"+port+"/"+titulo_url);System.out.println("path work:"+dir);} catch (Exception e) {throw new Exception("erro na inicialização: "+e.toString());}while(true) {try {socket = serverSocket.accept();origem = socket.getRemoteSocketAddress().toString();if ( origem.length() > 2 && origem.startsWith("/") )origem=origem.substring(1);if ( origem.indexOf(":") != -1 )origem=origem.substring(0,origem.indexOf(":"));System.out.println("Conexao de origem: "+origem+", data:"+(new Date()));if ( ips_banidos.length() > 0 && (","+ips_banidos+",").contains(","+origem+",") ){System.out.println("Acesso recusado para o ip banido: "+origem);continue;}new ClientThread(socket,titulo_url,titulo,dir,endsWiths);} catch (Exception e) {System.out.println("Erro ao executar servidor:" + e.toString());}}}}class ClientThread {String method,uri,protocol,titulo_url,titulo,dir,endsWiths;String nav;InputStream input = null;OutputStream output = null;char[] buffer = new char[2048];Writer writer;InputStreamReader isr=null;Reader reader;public ClientThread(final Socket socket,String titulo_url, String titulo, String dir,String endsWiths) {this.titulo_url=titulo_url;this.titulo=titulo;this.dir=dir;this.endsWiths=endsWiths;new Thread(){public void run(){try {input = socket.getInputStream();output = socket.getOutputStream();if ( input != null ){isr = new InputStreamReader(input);reader = new BufferedReader(isr);writer = new StringWriter();lendo();gravando();socket.close();writer.close();reader.close();isr.close();}} catch (Exception e) {System.out.println("----------> Erro ao executar servidor:" + e.toString());}}}.start();}private void lendo() throws Exception {try {int i = reader.read(buffer);if ( i == -1 ) return;writer.write(buffer, 0, i);BufferedReader br = new BufferedReader(new StringReader(writer.toString()));String line = null;int lineNumber = 0;while ((line = br.readLine()) != null) {System.out.println("<---|    " + line);if (lineNumber == 0 && line.split(" ").length == 3 ) {this.method     = line.split(" ")[0];this.uri        = line.split(" ")[1];this.protocol   = line.split(" ")[2];}lineNumber++;}System.out.println("     |    ");} catch (IOException e) {throw new Exception("Erro ao converter stream para string:" + e.toString());}}private void gravando() throws Exception {StringBuilder sb = new StringBuilder();if ( method.equals("OPTIONS") ){for ( String line : new String[]{"HTTP/1.1 501 Not Implemented\r\n"+ "\r\n"}){sb.append(line);System.out.println("    |---> " + line.replace("\r\n",""));}System.out.println("   |    ");output.write(sb.toString().getBytes());return;}sb = new StringBuilder();nav=dir+uri.replace("//","/").trim();nav=nav.replace("//","/").replace("%20"," ");if ( ! new File(nav).isFile() ){nav+="/";int c=9;while ( nav.contains("//") && c-->0 )nav=nav.replace("//","/");for ( 
/* class HttpServer */ String index : new String[]{"index.html","index.htm"} ){if ( new File(nav+index).exists() ){nav+=index;break;}}}if ( uri.equals("/"+titulo_url) ){sb = new StringBuilder(); for ( String line : new String[]{"HTTP/1.1 200 OK\r\n","Content-Type: text/html; charset=UTF-8\r\n","\r\n","<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n","<html xmlns=\"http://www.w3.org/1999/xhtml\">\n","<meta charset='UTF-8' http-equiv='X-UA-Compatible' content='IE=9'>\n","<br>\n","&nbsp;"+titulo+"<br>\n"}){sb.append(line);System.out.println("    |---> " + line.replace("\r\n",""));}File[] files = new File(dir).listFiles();Arrays.sort(files, new Comparator<File>() {public int compare(File f1, File f2) {return f1.lastModified()<f2.lastModified()?1:-1;}});sb.append("<style>.bordered {border: solid #ccc 3px;border-radius: 6px;}.bordered tr:hover {background: #fbf8e9;}.bordered td, .bordered th {border-left: 2px solid #ccc;border-top: 2px solid #ccc;padding: 10px;}</style>");System.out.println("<style>.bordered {border: solid #ccc 3px;border-radius: 6px;}.bordered tr:hover {background: #fbf8e9;}.bordered td, .bordered th {border-left: 2px solid #ccc;border-top: 2px solid #ccc;padding: 10px;}</style>");sb.append("<table id='tablebase' class='bordered' style='font-family:Verdana,sans-serif;font-size:10px;border-spacing: 0;'>");System.out.println("<table id='tablebase' class='bordered' style='font-family:Verdana,sans-serif;font-size:10px;border-spacing: 0;'>");for ( File p : files){if ( ! p.isFile() ) continue;if ( ! endsWith_OK(p.getName(),endsWiths) ) continue;sb.append("<tr><td>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(p.lastModified())).toString() + "</td><td>" + "<a href='" + p.getName() + "'>" + p.getName() + "</a></td></tr>\n");System.out.println("<tr><td>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(p.lastModified())).toString() + "</td><td>" + "<a href='" + p.getName() + "'>" + p.getName() + "</a></td></tr>\n");}sb.append("</table></html>");System.out.println("</table></html>");System.out.println("   |    ");output.write(sb.toString().getBytes());return;}System.out.println("nav: "+nav+";uri: "+uri);if ( new File(nav).exists() && new File(nav).isFile() && endsWith_OK(nav,endsWiths) ){for ( String line : new String[]{"HTTP/1.1 200 OK\r\n"+ "Content-Type: "+ getContentType(nav)+ "; charset=UTF-8\r\n"+ "\r\n"}){sb.append(line);System.out.println("    |---> " + line.replace("\r\n",""));}System.out.println("   |    ");output.write(sb.toString().getBytes());try{System.out.println("iniciando leitura do arquivo: " + nav);transf_bytes(output,nav);System.out.println("finalizando leitura do arquivo: " + nav);return;}catch(Exception e){System.out.println("erro 404, não foi possivel ler o arquivo: " + nav);}}else{System.out.println("nao encontrou o arquivo: " + nav);if ( uri.equals("/favicon.ico") ){return;}}/* ERROR 404 */sb = new StringBuilder();for ( String line : new String[]{"HTTP/1.1 200 OK\r\n","Content-Type: text/html; charset=UTF-8\r\n","\r\n","<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +"<head>\n" +"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"/>\n" +"<title>404 - File or directory not found.</title>\n" +"<style type=\"text/css\">\n" +"<!--\n" +"body{margin:0;font-size:.7em;font-family:Verdana, Arial, Helvetica, sans-serif;background:#EEEEEE;}\n" +"fieldset{padding:0 15px 10px 15px;} \n" 
/* class HttpServer */ +"h1{font-size:2.4em;margin:0;color:#FFF;}\n" +"h2{font-size:1.7em;margin:0;color:#CC0000;} \n" +"h3{font-size:1.2em;margin:10px 0 0 0;color:#000000;} \n"  +"#header{width:96%;margin:0 0 0 0;padding:6px 2% 6px 2%;font-family:\"trebuchet MS\", Verdana, sans-serif;color:#FFF;\n" +"background-color:#555555;}\n" +"#content{margin:0 0 0 2%;position:relative;}\n" +".content-container{background:#FFF;width:96%;margin-top:8px;padding:10px;position:relative;}\n" +"-->\n" +"</style>\n" +"</head>\n" +"<body>\n" +"<div id=\"header\"><h1>Server Error</h1></div>\n" +"<div id=\"content\">\n" +" <div class=\"content-container\"><fieldset>\n" +"  <h2>404 - File or directory not found.</h2>\n" +"  <h3>The resource you are looking for might have been removed, had its name changed, or is temporarily unavailable.</h3>\n" +" </fieldset></div>\n" +"</div>\n" +"</body>\n" +"</html>"}){sb.append(line);System.out.println("    |---> " + line.replace("\r\n",""));}System.out.println("   |    ");output.write(sb.toString().getBytes());}private String getContentType(String caminho) {if ( caminho.endsWith(".html") || caminho.endsWith(".htm") )return "text/html";if ( caminho.endsWith(".css") )return "text/css";if ( caminho.endsWith(".png") || caminho.endsWith(".ico") || caminho.endsWith(".jpg") )return "image/png";return "application/octet-stream";}public byte[] lendo_arquivo(String caminho) throws Exception {FileInputStream fis=null;File file = new File(caminho);byte[] bFile = new byte[(int) file.length()];fis = new FileInputStream(file);fis.read(bFile);fis.close();return bFile;}public ArrayList<String> lendo_arquivo_display(String caminho) throws Exception {ArrayList<String> result=new ArrayList<String>();String strLine;try{FileReader rf=new FileReader(caminho);BufferedReader in=new BufferedReader(rf);while ((strLine = in.readLine()) != null)result.add(strLine);in.close();rf.close();}catch (Exception e){throw new Exception("nao foi possivel encontrar o arquivo "+caminho);}return result;}private void transf_bytes(OutputStream output, String nav) throws Exception {int count;DataInputStream dis=new DataInputStream(new FileInputStream(nav));byte[] buffer = new byte[8192];while ((count = dis.read(buffer)) > 0)output.write(buffer, 0, count);}private boolean endsWith_OK(String url,String ends){if ( ends.equals("") ) return true;String [] partes = ends.split(",");for ( int i=0;i<partes.length;i++ )if ( url.endsWith("."+partes[i]) )return true;return false;}}




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
/* class by manual */                + "  [y banco buffer [|-n_lines 500] [|-log buffer.log]]\n"
/* class by manual */                + "  [y xlsxToCSV]\n"
/* class by manual */                + "  [y token]\n"
/* class by manual */                + "  [y gettoken]\n"
/* class by manual */                + "  [y zip]\n"
/* class by manual */                + "  [y gzip]\n"
/* class by manual */                + "  [y gunzip]\n"
/* class by manual */                + "  [y echo]\n"
/* class by manual */                + "  [y printf]\n"
/* class by manual */                + "  [y cat]\n"
/* class by manual */                + "  [y md5]\n"
/* class by manual */                + "  [y sha1]\n"
/* class by manual */                + "  [y sha256]\n"
/* class by manual */                + "  [y aes]\n"
/* class by manual */                + "  [y base64]\n"
/* class by manual */                + "  [y grep]\n"
/* class by manual */                + "  [y wc -l]\n"
/* class by manual */                + "  [y head]\n"
/* class by manual */                + "  [y tail]\n"
/* class by manual */                + "  [y cut]\n"
/* class by manual */                + "  [y sed]\n"
/* class by manual */                + "  [y n]\n"
/* class by manual */                + "  [y rn]\n"
/* class by manual */                + "  [y [bytesToInts|bi]]\n"
/* class by manual */                + "  [y [intsToBytes|ib]]\n"
/* class by manual */                + "  [y od]\n"
/* class by manual */                + "  [y touch]\n"
/* class by manual */                + "  [y iconv]\n"
/* class by manual */                + "  [y tee]\n"
/* class by manual */                + "  [y uniq]\n"
/* class by manual */                + "  [y quebra]\n"
/* class by manual */                + "  [y seq]\n"
/* class by manual */                + "  [y awk print]\n"
/* class by manual */                + "  [y dev_null]\n"
/* class by manual */                + "  [y dev_in]\n"
/* class by manual */                + "  [y scp]\n"
/* class by manual */                + "  [y execSsh]\n"
/* class by manual */                + "  [y ssh]\n"
/* class by manual */                + "  [y sftp]\n"
/* class by manual */                + "  [y serverRouter]\n"
/* class by manual */                + "  [y httpServer]\n"
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
/* class by manual */                + "[y banco buffer [|-n_lines 500] [|-log buffer.log]]    \n"
/* class by manual */                + "    echo \"select * from TABELA1 | y banco conn,hash selectInsert | y banco buffer -n_lines 500 -log buffer.log | y banco conn,hash executeInsert\n"
/* class by manual */                + "[y xlsxToCSV]\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx mostraEstrutura\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx listaAbas\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx numeroAba 1\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx nomeAba Planilha1\n"
/* class by manual */                + "    xlsxToCSV arquivo.xlsx exportAll\n"
/* class by manual */                + "    obs: pegando a primeira aba => xlsxToCSV arquivo.xlsx numeroAba 1\n"
/* class by manual */                + "[y xml]\n"
/* class by manual */                + "    cat arquivo.xml | mostraEstrutura\n"
/* class by manual */                + "    xml arquivo.xml mostraEstrutura\n"
/* class by manual */                + "    cat arquivo.xml | mostraTags\n"
/* class by manual */                + "[y token]\n"
/* class by manual */                + "    y token value\n"
/* class by manual */                + "[y gettoken]\n"
/* class by manual */                + "    y gettoken hash\n"
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
/* class by manual */                + "[y echo]\n"
/* class by manual */                + "    echo a b c\n"
/* class by manual */                + "    echo \"a b c\"\n"
/* class by manual */                + "[y printf]\n"
/* class by manual */                + "    echo a b c\n"
/* class by manual */                + "    echo \"a b c\"\n"
/* class by manual */                + "    obs: diferente do echo, o printf nao gera \\n no final\n"
/* class by manual */                + "[y cat]\n"
/* class by manual */                + "    y cat arquivo\n"
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
/* class by manual */                + "[y wc -l]\n"
/* class by manual */                + "    cat arquivo | y wc -l\n"
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
/* class by manual */                + "[y sed]\n"
/* class by manual */                + "    cat arquivo | y sed A B    \n"
/* class by manual */                + "[y n]\n"
/* class by manual */                + "    cat arquivo | y n\n"
/* class by manual */                + "    obs: modifica arquivo \\r\\n para \\n(se ja tiver \\n nao tem problema)\n"
/* class by manual */                + "[y rn]\n"
/* class by manual */                + "    cat arquivo | y rn\n"
/* class by manual */                + "    obs: modifica arquivo \\n para \\r\\n(se ja tiver \\r\\n nao tem problema)\n"
/* class by manual */                + "[y [bytesToInts|bi]]\n"
/* class by manual */                + "    cat arquivo | y bytesToInts\n"
/* class by manual */                + "    cat arquivo | y bi\n"
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
/* class by manual */                + "[y touch]\n"
/* class by manual */                + "    y touch fileA\n"
/* class by manual */                + "    y touch fileA -3600\n"
/* class by manual */                + "    y touch fileA 60\n"
/* class by manual */                + "    y touch fileA 20210128235959\n"
/* class by manual */                + "    obs: 60(60 segundos a frente)\n"
/* class by manual */                + "    obs2: -3600(3600 segundos atras)\n"
/* class by manual */                + "    obs3: 20210128235959(setando em 28/01/2021 23:59:59)\n"
/* class by manual */                + "[y iconv]\n"
/* class by manual */                + "    y iconv -f UTF-8 -t ISO-8859-1 file\n"
/* class by manual */                + "    cat file | y iconv -f UTF-8 -t ISO-8859-1 \n"
/* class by manual */                + "    cat file | y iconv -f ISO-8859-1 -t UTF-8\n"
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
/* class by manual */                + "    cat arquivo | y banco buffer -n_lines 500 -log buffer.log | y dev_null\n"
/* class by manual */                + "[y dev_in]\n"
/* class by manual */                + "    y dev_in | y banco buffer -n_lines 500 -log buffer.log | y dev_null\n"
/* class by manual */                + "[y scp]\n"
/* class by manual */                + "    y scp file1 user,pass@servidor:file2\n"
/* class by manual */                + "    y scp file1 user,pass@servidor:file2 22\n"
/* class by manual */                + "    y scp user,pass@servidor:file1 file2\n"
/* class by manual */                + "    y scp user,pass@servidor:file1 file2 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y execSsh]\n"
/* class by manual */                + "    y execSsh user,pass@servidor command\n"
/* class by manual */                + "    y execSsh user,pass@servidor command 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y ssh]\n"
/* class by manual */                + "    y ssh user,pass@servidor\n"
/* class by manual */                + "    y ssh user,pass@servidor 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y sftp]\n"
/* class by manual */                + "    y sftp user,pass@servidor\n"
/* class by manual */                + "    y sftp user,pass@servidor 22\n"
/* class by manual */                + "    obs: user,pass ou user\n"
/* class by manual */                + "[y serverRouter]\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 show\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 showOnlySend\n"
/* class by manual */                + "    y serverRouter 192.168.0.100 8080 localhost 9090 showOnlyReceive\n"
/* class by manual */                + "\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 show\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 showOnlySend\n"
/* class by manual */                + "    y serverRouter localhost 8080 localhost 9090 showOnlyReceive\n"
/* class by manual */                + "    obs:\n"
/* class by manual */                + "        192.168.0.100 -> ip a se conectar(se colocar localhost ele vai tentar pegar o ip correto)\n"
/* class by manual */                + "        8080 -> porta para conectar no router\n"
/* class by manual */                + "        localhost -> local que o serverRouter conecta(use nome da maquina ou ip)\n"
/* class by manual */                + "        9090 -> porta que o serverRouter conecta\n"
/* class by manual */                + "[y TESTEserver]\n"
/* class by manual */                + "    y TESTEserver 9090\n"
/* class by manual */                + "    y TESTEserver 192.168.0.100 9090\n"
/* class by manual */                + "[y TESTEclient]\n"
/* class by manual */                + "    y TESTEclient localhost 8080\n"
/* class by manual */                + "[y httpServer]\n"
/* class by manual */                + "    y httpServer localhost pagina_toke_zzz111 \"Lista de arquivos\" 8888 \"/dir\" \"\" \"\"\n"
/* class by manual */                + "    parametros: host(pode ser \"\"), titulo_url, titulo, port, dir, endsWiths(ex: \"\",\"jar,zip\"), ips_banidos(ex: \"\",\"8.8.8.8,4.4.4.4\")\n"
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
/* class by manual */                + "\n"
/* class by manual */                + "Dica: copiar o arquivo hash do token pra o nome do banco. cd $TOKEN_Y;cp 38b3492c4405f98972ba17c0a3dc072d servidor;\n"
/* class by manual */                + "Dica2: vendo os tokens: grep \":\" $TOKEN_Y/*\n"
/* class by manual */                + "Dica3: vendo warnnings ORA: cat $ORAs_Y\n"
/* class by manual */                + "\n"
/* class by manual */                + "alias no windows(criar arquivo c:\\Windows\\System32\\y.bat com o conteudo abaixo):\n"
/* class by manual */                + "@echo off\n"
/* class by manual */                + "java -cp c:\\\\y;c:\\\\y\\\\ojdbc6.jar;c:\\\\y\\\\jsch-0.1.55.jar Y %1 %2 %3 %4 %5 %6 %7 %8 %9\n"
/* class by manual */                + "\n"
/* class by manual */                + "alias no linux:\n"
/* class by manual */                + "alias y='java -cp /y:/y/ojdbc6.jar:/y/jsch-0.1.55.jar Y'";
/* class by manual */            if ( caminho.equals("/y/manual_mini") )
/* class by manual */                return ""
/* class by manual */                + "usage:\n"
/* class by manual */                + "  [y banco fromCSV -outTable tabelaA selectInsert]\n"
/* class by manual */                + "  [y banco conn,hash [select|selectInsert|selectCSV] [|select..]]\n"
/* class by manual */                + "  [y banco conn,hash executeInsert]\n"
/* class by manual */                + "  [y banco conn,hash execute [|execute..]]\n"
/* class by manual */                + "  [y banco conn,hash createjobexecute]\n"
/* class by manual */                + "  [y banco [connIn,hash|fileCSV,file] connOut,hash -outTable tabelaA [|trunc|createTable] [carga|createjobcarga]]\n"
/* class by manual */                + "  [y banco executejob]\n"
/* class by manual */                + "  [y banco buffer [|-n_lines 500] [|-log buffer.log]]\n"
/* class by manual */                + "  [y xlsxToCSV]\n"
/* class by manual */                + "  [y token]\n"
/* class by manual */                + "  [y gettoken]\n"
/* class by manual */                + "  [y zip]\n"
/* class by manual */                + "  [y gzip]\n"
/* class by manual */                + "  [y gunzip]\n"
/* class by manual */                + "  [y echo]\n"
/* class by manual */                + "  [y printf]\n"
/* class by manual */                + "  [y cat]\n"
/* class by manual */                + "  [y md5]\n"
/* class by manual */                + "  [y sha1]\n"
/* class by manual */                + "  [y sha256]\n"
/* class by manual */                + "  [y aes]\n"
/* class by manual */                + "  [y base64]\n"
/* class by manual */                + "  [y grep]\n"
/* class by manual */                + "  [y wc -l]\n"
/* class by manual */                + "  [y head]\n"
/* class by manual */                + "  [y tail]\n"
/* class by manual */                + "  [y cut]\n"
/* class by manual */                + "  [y sed]\n"
/* class by manual */                + "  [y n]\n"
/* class by manual */                + "  [y rn]\n"
/* class by manual */                + "  [y [bytesToInts|bi]]\n"
/* class by manual */                + "  [y [intsToBytes|ib]]\n"
/* class by manual */                + "  [y od]\n"
/* class by manual */                + "  [y touch]\n"
/* class by manual */                + "  [y iconv]\n"
/* class by manual */                + "  [y tee]\n"
/* class by manual */                + "  [y uniq]\n"
/* class by manual */                + "  [y quebra]\n"
/* class by manual */                + "  [y seq]\n"
/* class by manual */                + "  [y awk print]\n"
/* class by manual */                + "  [y dev_null]\n"
/* class by manual */                + "  [y dev_in]\n"
/* class by manual */                + "  [y scp]\n"
/* class by manual */                + "  [y execSsh]\n"
/* class by manual */                + "  [y ssh]\n"
/* class by manual */                + "  [y sftp]\n"
/* class by manual */                + "  [y serverRouter]\n"
/* class by manual */                + "  [y httpServer]\n"
/* class by manual */                + "  [y help]";
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


