
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class GeraTextoClasseArquivos {
    public static void main(String[] args) {
        new GeraTextoClasseArquivos().go();
    }

    // metodo feito para gerar texto de manual para colocar no final de Y.java
    public void go(){
        String prefixo="/* class by manual */    ";
        System.out.println(prefixo+"class Arquivos{");
        System.out.println(prefixo+"    public String lendo_arquivo_pacote(String caminho){");
        for ( String item : getResourceFiles("/") )
        {
            if ( item.contains(".") ) continue;
            if ( item.equals("y") ) continue;
            System.out.println(prefixo+"        if ( caminho.equals(\"/y/"+item+"\") )");
            System.out.println(prefixo+"            return \"\"");
            String [] linhas=lendo_arquivo_pacote("/"+item).split("\n");
            for ( int i=0;i<linhas.length;i++ )
                if ( i == linhas.length -1 )
                    System.out.println(prefixo+"            + \""+removerAcentos(linhas[i].replace("\\","\\\\").replace("\"","\\\""))+"\";");
                else
                    System.out.println(prefixo+"            + \""+removerAcentos(linhas[i].replace("\\","\\\\").replace("\"","\\\""))+"\\n\"");
        }
        
        System.out.println(prefixo+"        return \"\";");
        System.out.println(prefixo+"    }");
        System.out.println(prefixo+"}");
        System.out.println("");
        System.out.println("");
    }

            
    private List<String> getResourceFiles(String path){
        List<String> filenames = new ArrayList<>();
        try{
            InputStream in = getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }catch(Exception e){}
        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }
    
    private ClassLoader getContextClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
    
    public String lendo_arquivo_pacote(String caminho){
        InputStream fstream=getClass().getResourceAsStream(caminho);
        // System.out.println(
        //   lendo_arquivo_pacote("/y/manual")
        // );
        String result="";
        try{
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
                result+=strLine+"\n";
            in.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return result;
    }
    
    public static String removerAcentos(String str){
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
    
}
