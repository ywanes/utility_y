package y;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Y {    
    //public static String env=null;
    public static String env="c:\\tmp";

    public static java.util.Scanner scanner_pipe=null;
    public static int n_lines_buffer_DEFAULT=500;    
    
    public static void main(String[] args) {

/*

# 34k linhas/s                
# y banco $conn execute "create table a ( C1 varchar2(3000), C2 varchar2(3000), C3 varchar2(3000) )"
y banco conn,desktop execute "drop table b"
y banco conn,desktop execute "create table b as select * from a where rownum <= 1"
y banco conn,desktop selectInsert "select b.* from b,(select level from dual connect by level <= 160000)" | y banco buffer -n_line 500 -log buffer.log | y banco conn,desktop insert
cat buffer.log
# y banco conn,desktop select "select count(1) from b"

        
*/
        new Y().go(args);
    }

    
    public void go(String[] args){
        String version="0.1.0";
        String env="TOKEN_Y";
        String [] programas=new String[]{"y banco","y token","y gettoken","y gzip","y gunzip","y echo","y cat","y md5","y sha1","y sha256","y grep","y wc -l","y sed","y awk print","y dev_null","y help"};
        if ( args.length == 0 ){
            System.out.println(apresentacao(programas));
            return;
        }
        if ( args[0].equals("banco") ){
            String msg_usage="usage: "
            + "\n  [y banco -conn ... select]"
            + "\n  [y banco -conn ... select select..]"
            + "\n  [y banco conn,hash select]"
            + "\n  [y banco conn,hash select select..]"
            + "\n  [y banco conn,hash selectInsert]"
            + "\n  [y banco conn,hash selectInsert select..]"
            + "\n  [y banco conn,hash selectCSV]"
            + "\n  [y banco conn,hash selectCSV select..]"
            + "\n  [y banco conn,hash insert]"
            + "\n  [y banco conn,hash execute]"
            + "\n  [y banco conn,hash execute execute..]"
            + "\n  [y banco buffer]"
            + "\n  [y banco buffer -n_lines 500]"
            + "\n  [y banco buffer -log buffer.log]"
            + "\n  [y banco buffer -n_line 500 -log buffer.log]"
            + "\n  Ex: -conn \"jdbc:oracle:thin:@//host_name:1521/service_name|login|senha\""
            + "\n  Ex2: -conn \"jdbc:oracle:thin:@host_name:1566:sid_name|login|senha\""
            + "\n  Obs: entrada de dados pode ser feito por |"
            + "\n  Dica: copiar o arquivo hash do token pra o nome do banco. cd $TOKEN_Y;cp 38b3492c4405f98972ba17c0a3dc072d servidor;"
            + "\n  Dica2: vendo os tokens: grep \":\" $TOKEN_Y/*";
            
            
            if ( args.length == 1 ){
                System.out.println(msg_usage);
                return;
            }
            String conn=null;
            String app="";
            String parm="";

            // comandos iniciados com conn, ou -conn
            if ( 
                args[1].equals("-conn") 
                || ( args[1].startsWith("conn,") && ! args[1].equals("conn,") ) 
            ){
                // PREPARAÇÂO
                // pegando conn com os parametros args[1] e args[1][2]
                if ( args[1].equals("-conn") )
                {
                    if ( args.length == 4 || args.length == 5 ){
                        if ( args.length == 4 ){
                            conn=args[2];
                            app=args[3];                    
                        }
                        if ( args.length == 5 ){
                            conn=args[2];
                            app=args[3];
                            parm=args[4];
                        }
                    }else{
                        System.out.println(msg_usage);
                        return;
                    }
                }

                // PREPARAÇÂO
                // pegando conn com o parametro args[1]
                if ( args[1].startsWith("conn,") && ! args[1].equals("conn,") )
                {
                    if ( args.length == 3 || args.length == 4 ){
                        if ( args.length == 3 ){
                            app=args[2];                    
                        }
                        if ( args.length == 4 ){
                            app=args[2];
                            parm=args[3];
                        }
                    }else{
                        System.out.println(msg_usage);
                        return;
                    }
                    String value=gettoken(args[1].split(",")[1]);
                    if ( value == null )
                    {
                        System.out.println("Não foi possível encontrar o token "+args[1].split(",")[1]);
                        return;
                    }
                    conn=value;
                }

                // comandos app
                if ( app.equals("select") ){
                    select(conn,parm);
                    return;
                }
                if ( app.equals("selectInsert") ){
                    selectInsert(conn,parm);
                    return;
                }
                if ( app.equals("selectCSV") ){
                    selectCSV(conn,parm);
                    return;
                }
                if ( app.equals("insert") ){
                    insert(conn);
                    return;
                }
                if ( app.equals("execute") ){
                    execute(conn,parm);
                    return;
                }
            }
            
            // buffer
            if ( args[1].equals("buffer") 
                && (
                    ( args.length == 2 )
                    || ( args.length == 4 && args[2].equals("-n_lines") )
                    || ( args.length == 4 && args[2].equals("-log") )
                    || ( args.length == 6 && args[2].equals("-n_line") && args[4].equals("-log") )
                )
            ){
                buffer(args);
                return;
            }
            
            //Comando inválido
            System.out.print("Comando inválido: [y");
            for ( int i=0;i<args.length;i++ )
                System.out.print(" "+args[i]);
            System.out.println("]");

            return;
        }
        if ( args[0].equals("token") ){
            if ( args.length == 1 ){
                System.out.println("usage:"
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
                System.out.println("Não foi possível utilizar a pasta "+dir_token);
                return;
            }
            System.out.println(hash);
            return;
        }
        if ( args[0].equals("gettoken") ){
            if ( args.length == 1 ){
                System.out.println("usage:"
                + "\n  [y token value]"
                + "\n  return hash"
                + "\n  [y gettoken hash]"
                + "\n  return value");
                return;
            }
            String value=gettoken(args[1]);
            if ( value == null )
            {
                System.out.println("Não foi possível encontrar o token "+args[1]);
                return;
            }
            System.out.println(value);
            return;
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
        if ( args[0].equals("help") || args[0].equals("-help") || args[0].equals("--help") ){
            System.out.println("Utilitário Y versão:"+version+"\n"+apresentacao(programas));
            return;
        }
        if ( args[0].equals("grep") && args.length == 2 ){
            grep(args[1]);
            return;
        }        
        if ( args.length == 2 && args[0].equals("wc") && args[1].equals("-l") ){
            wc_l();
            return;
        }        
        if ( args[0].equals("sed") && args.length == 3 ){
            sed(args[1],args[2]);
            return;
        }
        if ( args.length >= 3 && args[0].equals("awk") && args[1].equals("print") ){
            awk(args);
            return;
        }
        if ( args[0].equals("dev_null") ){
            dev_null();
            return;
        }

        //Comando inválido
        System.out.print("Comando inválido: [y");
        for ( int i=0;i<args.length;i++ )
            System.out.print(" "+args[i]);
        System.out.println("]");

        return;
    }

    public void select(String conn,String parm){
        try{            
            Connection con = getcon(conn);
            if ( con == null ){
                System.out.println("Não foi possível se conectar!!" );
                return;
            }

            if ( parm.equals("") ){
                String linha;
                while( (linha=read()) != null )
                    parm+="\n"+linha;
                parm=removePontoEVirgual(parm);
            }

            Statement stmt = con.createStatement();
            ResultSet rs=null;
            ResultSetMetaData rsmd;
            ArrayList<String> campos=new ArrayList<String>();
            ArrayList<Integer> tipos=new ArrayList<Integer>();
            StringBuilder sb=null;
            String tmp="";
            String table="";
            String parmTratado=removePontoEVirgual(parm);

            rs=stmt.executeQuery(parmTratado);
            rsmd=rs.getMetaData();
            table=getTableByParm(parmTratado);

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
                        tmp=tmp.replace("'","''");
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
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("Erro: "+e.toString()+" -> "+parm);
        }        
    }

    public void selectInsert(String conn,String parm){
        int countCommit=0;
        try{
            Connection con = getcon(conn);
            if ( con == null ){
                System.out.println("Não foi possível se conectar!!" );
                return;
            }

            if ( parm.equals("") ){
                String linha;
                while( (linha=read()) != null )
                    parm+="\n"+linha;
                parm=removePontoEVirgual(parm);
            }

            Statement stmt = con.createStatement();
            ResultSet rs=null;
            ResultSetMetaData rsmd;
            ArrayList<String> campos=new ArrayList<String>();
            ArrayList<Integer> tipos=new ArrayList<Integer>();
            StringBuilder sb=null;
            String tmp="";
            String table="";
            String parmTratado=removePontoEVirgual(parm);

            rs=stmt.executeQuery(parmTratado);
            rsmd=rs.getMetaData();
            table=getTableByParm(parmTratado);

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
                            tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+" "+tmp.substring(11, 19);
                        tmp=tmp.replace("'","''");
                        if ( i == campos.size()-1 ){
                            sb.append("'");
                            sb.append(tmp);
                            sb.append("'");
                        }else{
                            sb.append("'");
                            sb.append(tmp);
                            sb.append("',");
                        }
                        continue;
                    }
                    throw new Exception("tipo desconhecido:"+tipos.get(i) + " -> " + rs.getString(campos.get(i)) );
                }
                System.out.println("insert into "+table+" values("+ sb.toString()+");");
                if ( countCommit++ >= 10000 ){
                    System.out.println("commit;");
                    countCommit=0;
                }
            }
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("Erro: "+e.toString()+" -> "+parm);
        }        
    }

    public void selectCSV(String conn,String parm){
        try{
            Connection con = getcon(conn);
            if ( con == null ){
                System.out.println("Não foi possível se conectar!!" );
                return;
            }

            if ( parm.equals("") ){
                String linha;
                while( (linha=read()) != null )
                    parm+="\n"+linha;
                parm=removePontoEVirgual(parm);
            }

            Statement stmt = con.createStatement();
            ResultSet rs=null;
            ResultSetMetaData rsmd;
            ArrayList<String> campos=new ArrayList<String>();
            ArrayList<Integer> tipos=new ArrayList<Integer>();
            StringBuilder sb=null;
            String tmp="";
            String table="";
            String parmTratado=removePontoEVirgual(parm);
            String header="";
            String first_detail="";
            boolean first=true;

            rs=stmt.executeQuery(parmTratado);
            rsmd=rs.getMetaData();
            table=getTableByParm(parmTratado);

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
                            header+="\""+campos.get(i)+"\";";
                            first_detail+="\"\";";
                        }else
                            sb.append("\"\";");
                        continue;
                    }
                    if ( tipos.get(i) == -3 || tipos.get(i) == 2 || tipos.get(i) == 12 || tipos.get(i) == -9
                        || tipos.get(i) == 1 || tipos.get(i) == 2005 || tipos.get(i) == -1 || tipos.get(i) == 93 )
                    {
                        if ( tipos.get(i) == 2 && tmp.startsWith("."))
                            tmp="0"+tmp;
                        if ( tipos.get(i) == 93 ) // DATA
                            tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+" "+tmp.substring(11, 19);
                        tmp=tmp.replace("\"","").replace("\n","");

                        if ( first )
                        {
                            header+="\""+campos.get(i)+"\";";
                            first_detail+="\""+tmp+"\";";
                        }else{
                            sb.append("\"");
                            sb.append(tmp);
                            sb.append("\";");
                        }

                        continue;
                    }
                    throw new Exception("tipo desconhecido:"+tipos.get(i) + " -> " + rs.getString(campos.get(i)) );
                }

                if ( first ){
                    first=false;
                    System.out.println(header);
                    System.out.println(first_detail);
                    continue;
                }

                System.out.println(sb.toString());                
            }        
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception e){
            System.out.println("Erro: "+e.toString()+" -> "+parm);
        }        
    }

    public void insert(String conn){        
        // status
        // 1 - lendo uma sequencia de linhas
        // 2 - NÃO lendo uma sequencia de linhas
        boolean reading=false;
        boolean par=true;
        String linha="";
        StringBuilder sb=null;
        boolean ok=true;
        
        int agulha=0;
        int limiteAgulha=1000; // 53k linhas/s
        
        String ii;
        StringBuilder all=new StringBuilder(" insert all");
        
        try{
            Connection con = getcon(conn);
            if ( con == null ){
                System.out.println("Não foi possível se conectar!!" );
                return;
            }
            con.setAutoCommit(false);
            
            Statement stmt = con.createStatement();
            
            while ( (linha=read()) != null ){
                if ( par && linha.trim().equals("") )
                    continue;
                if ( par ){
                    if ( linha.trim().startsWith("commit") || linha.trim().startsWith("COMMIT") )
                    {
                        try{  
                            ii=removePontoEVirgual(linha);
                            stmt.execute(ii);
                            /*
                            if ( ++agulha >= limiteAgulha )
                            {
                                stmt.addBatch(ii);
                                stmt.executeBatch();
                                agulha=0;
                            }else{
                                stmt.addBatch(ii);                                
                            }
                            */
                        }catch(Exception e){
                            System.out.println("Erro: "+e.toString()+" -> "+linha);
                            ok=false;
                        }
                        continue;
                    }
                    if ( startingInsert(linha) )
                    {
                        if ( par=countParAspeta(par,linha) ){
                            try{
                                ii=removePontoEVirgual(linha);                                
                                /*
                                if ( ++agulha >= limiteAgulha )
                                {
                                    stmt.addBatch(ii);
                                    stmt.executeBatch();
                                    agulha=0;
                                }else{
                                    stmt.addBatch(ii);                                
                                }
                                */
                                if ( ++agulha >= limiteAgulha ){
                                    all.append(ii.substring(6));
                                    all.append(" select * from dual");
                                    stmt.execute(all.toString());
                                    all=null;
                                    all=new StringBuilder(" insert all");
                                    agulha=0;
                                }else{
                                    all.append(ii.substring(6));
                                }
                                
                            }catch(Exception e){
                                System.out.println("Erro: "+e.toString()+" -> "+linha);
                                ok=false;
                            }
                            continue;
                        }else{
                            sb=null;// forçando limpeza de memoria
                            sb=new StringBuilder(linha);
                        }
                        continue;
                    }
                    throw new Exception("Erro, linha inesperada:" +linha);
                }else{
                    if ( par=countParAspeta(par,linha) ){
                        try{
                            sb.append("\n");
                            sb.append(removePontoEVirgual(linha));
                            ii=sb.toString();
                            /*
                            if ( ++agulha >= limiteAgulha )
                            {
                                stmt.addBatch(ii);
                                stmt.executeBatch();
                                agulha=0;
                            }else{
                                stmt.addBatch(ii);                                
                            }
                            */
                            if ( ++agulha >= limiteAgulha ){
                                all.append(ii.substring(6));
                                all.append(" select * from dual");
                                stmt.execute(all.toString());
                                all=null;
                                all=new StringBuilder(" insert all");
                                agulha=0;
                            }else{
                                all.append(ii.substring(6));
                            }
                            
                        }catch(Exception e){
                            System.out.println("Erro: "+e.toString()+" -> "+linha);
                            ok=false;
                        }
                        continue;
                    }else{
                        sb.append("\n");
                        sb.append(linha);
                    }
                }
            }
            /*
            if ( agulha > 0 ){
                stmt.executeBatch();
                agulha=0;
            }
            */
            if ( agulha > 0 ){
                all.append(" select * from dual");
                stmt.execute(all.toString());
                all=null;
                agulha=0;
            }
            stmt.close();
            con.close();
        }catch(Exception e){
            System.out.println("Erro: "+e.toString());
            ok=false;
        }   
        if ( ok )
            System.out.println("OK");
    }

    public void execute(String conn,String parm){
        try{
            Connection con = getcon(conn);
            if ( con == null ){
                System.out.println("Não foi possível se conectar!!" );
                return;
            }

            if ( parm.equals("") ){
                String linha;
                while( (linha=read()) != null )
                    parm+="\n"+linha;
                parm=removePontoEVirgual(parm);
            }

            Statement stmt = con.createStatement();
            stmt.execute(removePontoEVirgual(parm));
            stmt.close();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("Erro: "+e.toString()+" -> "+parm);
        }        
    }

    public void buffer(String [] args){
        if ( args.length == 2 ){
            buffer(n_lines_buffer_DEFAULT,null);
        }
        if ( args.length == 4 && args[2].equals("-n_lines") ){
            buffer(tryConvertNumberPositiveByString(n_lines_buffer_DEFAULT,args[3]),null);
        }
        if ( args.length == 4 && args[2].equals("-log") ){
            buffer(n_lines_buffer_DEFAULT,args[3]);
        }
        if ( args.length == 6 && args[2].equals("-n_line") && args[4].equals("-log") ){
            buffer(tryConvertNumberPositiveByString(n_lines_buffer_DEFAULT,args[3]),args[5]);
        }
    }
            
    public void buffer(int n_lines_buffer,String caminhoLog_){
        try{     
            List<String> lista=Collections.synchronizedList(new ArrayList<String>());
            
            boolean [] finishIn=new boolean[]{false};
            long [] countLinhas=new long []{0,0,0};
            String [] caminhoLog=new String[]{caminhoLog_};
            PrintWriter [] out=new PrintWriter[1];

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
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                            long count;

                            out[0].println(formatter.format(new Date()) + " - start");
                            out[0].flush();
                            while(true)
                            {
                                time2=System.currentTimeMillis();
                                if ( countLinhas[2] == 1 )
                                    break;
                                if ( time2 >= time1+1000 && countLinhas[1] == 0 ){
                                    time1=time2;
                                    count=countLinhas[0];
                                    countLinhas[1]=count;
                                    out[0].println( formatter.format(new Date()) + " - linhas por segundo: "+count+" buffer: "+lista.size());
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
                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    scanner.useDelimiter("\n");

                    while( true ){
                        if ( lista.size() < n_lines_buffer )
                        {
                            if ( scanner.hasNext() ){
                                lista.add(scanner.next());
                            }else{
                                finishIn[0]=true;
                                break;
                            }
                        }
                    }
                }
            }.start();        
            
            // saida
            while( true ){
                if ( finishIn[0] ){
                    while(lista.size() > 0){
                        System.out.println(lista.get(0));
                        lista.remove(0);
                        if ( caminhoLog[0] != null )
                            contabiliza(countLinhas);
                    }
                    countLinhas[2]=1;
                    break;
                }
                if (lista.size() > 0){
                    System.out.println(lista.get(0));
                    lista.remove(0);
                    if ( caminhoLog[0] != null )
                        contabiliza(countLinhas);
                }  
            }

            // fechando arquivo
            if ( caminhoLog[0] != null )
            {
                try{
                    out[0].close();                    
                }catch(Exception e){}
            }
            
        }catch(Exception e){}
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
            System.out.println("Para usar o token é necessário ter a variável de ambiente TOKEN_Y definida, ex export TOKEN_Y=/home/user/.token_y");
            return false;
        }
        File f = new File(dir_token);
        if ( ! f.exists() ){
            if ( ! f.mkdir() ){
                System.out.println("Não foi possível utilizar/criar a pasta "+dir_token);
                return false;
            }
        }
        if ( ! f.isDirectory() ){
            System.out.println("O caminho "+dir_token+" não é um diretório");
            return false;

        }
        return true;
    }
    public String gravado_token(String dir_token,String value){
        dir_token=fix_caminho(dir_token);
        String md5=getMD5_SHA1_FILE(value,"MD5");
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
    public static boolean salvando_file(String texto, File arquivo) {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(arquivo));
            out.write(texto);
            out.close();
            return true;
        }catch(Exception e){}        
        return false; 
    }

    public static String lendo_arquivo(String caminho) {
        String result="";
        String strLine;
        try{
            BufferedReader in=new BufferedReader(new FileReader(caminho));
            while ((strLine = in.readLine()) != null)   {
                if ( result.equals("") )
                    result+=strLine;
                else
                    result+="\n"+strLine;
            }
        }catch (Exception e){}
        return result;
    }

    public String fix_caminho(String caminho){
        if ( ! caminho.endsWith("/") && caminho.contains("/") )
            return caminho+"/";
        if ( ! caminho.endsWith("\\") && caminho.contains("\\") )
            return caminho+"\\";
        return caminho;
    }
    public String getenv(){
        if ( env != null && new File(env).exists() )
            return env;
        return System.getenv("TOKEN_Y");
    }

    public String getTableByParm(String parm){
        String retorno="";
        try{
            retorno=parm.toUpperCase().replace(")"," ").replace("\n"," ").replace(","," ").split("FROM ")[1].split(" ")[0].trim();
            if ( retorno.length() == 0 )
                return "DUAL";
        }catch(Exception e){
            return "DUAL";
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

    public String getMD5_SHA1_FILE(File file,String tipo){
        InputStream data=null;
        String md5="";
        int STREAM_BUFFER_LENGTH = 1024;
        try {
            data=new FileInputStream(file);
            MessageDigest digest=MessageDigest.getInstance(tipo);
            byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
            int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
            while (read > -1) {
                digest.update(buffer, 0, read);
                read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
            }
            md5=new String(encodeHex(digest.digest()));
            data.close();
        } catch (Exception ex) {}
        return md5;
    }

    public String getMD5_SHA1_FILE(String txt,String tipo){
        try {
            byte[] bytesOfMessage = txt.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");            
            return new String(encodeHex(md.digest(bytesOfMessage)));                
        } catch (Exception ex) {}
        return null;
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
            System.out.println("Erro na conexão: Login e senha não encontrado!");
            return null;
        }else{
            String par = stringcon.split("\\|")[0];
            String user = stringcon.split("\\|")[1];
            String pass = stringcon.split("\\|")[2];
            try {
                return DriverManager.getConnection(par, user, pass);
            } catch (Exception x) {
                System.out.println("Erro na conexão:"+x.toString());
            }
        }
        return null;
    }

    public String read(){
        try{
            if ( scanner_pipe == null ){
                scanner_pipe=new java.util.Scanner(System.in);
                scanner_pipe.useDelimiter("\n");
            }
            if ( scanner_pipe.hasNextLine() )
                return scanner_pipe.nextLine();
            else
                return null;
        }catch(java.util.NoSuchElementException no) {
            return null;
        }catch(Exception e){
            System.out.println("NOK: "+e.toString());
        }
        return null;
    }
    
    public void gzip()
    {
        try{
            int BUFFER_SIZE = 512;
            byte[] buf = new byte[BUFFER_SIZE];
            java.util.zip.GZIPOutputStream out = new java.util.zip.GZIPOutputStream(System.out);
            int len;
            while ((len = System.in.read(buf)) > -1)
                out.write(buf, 0, len);
            out.finish();        
        }catch(Exception e){}
    }
    
    public void gunzip()
    {
        try{
            int BUFFER_SIZE = 512;
            byte[] buf = new byte[BUFFER_SIZE];
            java.util.zip.GZIPInputStream out = new java.util.zip.GZIPInputStream(System.in);
            int len;
            while ((len = out.read(buf)) > -1)
                System.out.write(buf, 0, len);            
        }catch(Exception e){}
    }

    public void echo(String [] args)
    {
        if ( args.length > 1 )
            System.out.print(args[1]);
        for ( int i=2;i<args.length;i++ )
            System.out.print(" "+args[i]);
        System.out.println("");
    }

    public void cat(String [] caminhos)
    {
        
        try{
            for ( int i=1;i<caminhos.length;i++ )
            {
                if ( ! new File(caminhos[i]).exists() ){
                    System.out.println("Erro, este arquivo não existe: "+caminhos[i]);
                    return;
                }
            }
            for ( int i=1;i<caminhos.length;i++ )
            {
                int BUFFER_SIZE = 512;
                byte[] buf = new byte[BUFFER_SIZE];            
                FileInputStream fis = new FileInputStream(caminhos[i]);
                int len;
                while ((len = fis.read(buf)) > -1)
                    System.out.write(buf, 0, len);            
                fis.close();
            }
        }catch(Exception e){
            System.out.println("Erro, "+e.toString());
        }
    }

    public void digest(String tipo){        
        try {
            MessageDigest digest=MessageDigest.getInstance(tipo);
            int BUFFER_SIZE = 1024;
            byte[] buf = new byte[BUFFER_SIZE];            
            int len;
            while( (len=System.in.read(buf, 0, BUFFER_SIZE)) > -1 )
                digest.update(buf, 0, len);
            System.out.println(new String(encodeHex(digest.digest())));
        } catch (Exception ex) {}
    }

    public int tryConvertNumberPositiveByString(int n_lines_buffer,String value){
        try{
            int tmp=Integer.parseInt(value);
            if ( tmp >= 0 )
                return tmp;
        }catch(Exception e){}
        return n_lines_buffer;
    }
    
    public void grep(String grep)
    {
        boolean first=false;
        boolean tail=false;
        
        if ( grep.startsWith("^") ){
            first=true;
            grep=grep.substring(1);
        }
        if ( grep.endsWith("$") ){
            tail=true;
            grep=grep.substring(0,grep.length()-2);
        }        
        try {
            String line=null;
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            scanner.useDelimiter("\n");
            while ( scanner.hasNext() && (line=scanner.next()) != null ) {
                if ( ! first && ! tail && line.contains(grep) ){
                    System.out.println(line);
                    continue;
                }
                if ( first && ! tail && line.startsWith(grep) ){
                    System.out.println(line);
                    continue;
                }
                if ( ! first && tail && line.endsWith(grep) ){
                    System.out.println(line);
                    continue;
                }
            }
        }catch(Exception e){}
    }
    public void wc_l()
    {
        try {
            long count=0;
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            scanner.useDelimiter("\n");
            while ( scanner.hasNextLine() ){
                scanner.nextLine();
                count++;
            }
            System.out.println(count);
        }catch(Exception e){
        }
    }

    public void sed(String sedA,String sedB)
    {
        try {
            String line=null;
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            scanner.useDelimiter("\n");
            while ( scanner.hasNext() && (line=scanner.next()) != null ) {
                System.out.println(line.replaceAll(sedA, sedB));
            }
        }catch(Exception e){}
    }
        
    public void awk(String [] args)
    {
        // pendente
    }
    
    public void dev_null()
    {
        try{
            int BUFFER_SIZE = 512;
            byte[] buf = new byte[BUFFER_SIZE];
            while(System.in.read(buf) > -1){}
        }catch(Exception e){}
    }
    
}


