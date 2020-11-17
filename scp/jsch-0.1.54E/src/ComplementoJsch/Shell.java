package ComplementoJsch;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;

public class Shell{
  public static void custom(String[] arg){
    Channel channel=null;
    
    try{
      JSch jsch=new JSch();

      if(arg.length!=1 || !arg[0].contains(",") || !arg[0].contains("@")){
        System.err.println("usage: y ssh user,pass@remotehost");
        System.exit(-1);
      }      

      String user=arg[0].split("@")[0].split(",")[0];
      String senha=arg[0].split("@")[0].split(",")[1];
      String host=arg[0].split("@")[1];
      
      Session session=jsch.getSession(user, host, 22);

      session.setPassword(senha);

      UserInfo ui = new MyUserInfo(){
        public void showMessage(String message){
          JOptionPane.showMessageDialog(null, message);
        }
        public boolean promptYesNo(String message){return true;}
      };

      session.setUserInfo(ui);

      //session.connect();
      session.connect(30000);   // making a connection with timeout.

      channel=session.openChannel("shell");

      channel.setInputStream(System.in);
      channel.setOutputStream(System.out);

      //channel.connect();
      channel.connect(3*1000);
    }
    catch(Exception e){
      System.out.println(e);
    }
    
    while (channel != null && !channel.isEOF()){}
  }

  public static abstract class MyUserInfo
                          implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return null; }
    public boolean promptYesNo(String str){ return false; }
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return false; }
    public boolean promptPassword(String message){ return false; }
    public void showMessage(String message){ }
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      return null;
    }
  }
}
