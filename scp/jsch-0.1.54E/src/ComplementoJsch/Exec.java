package ComplementoJsch;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate remote exec.
 *  $ CLASSPATH=.:../build javac Exec.java 
 *  $ CLASSPATH=.:../build java Exec
 * You will be asked username, hostname, displayname, passwd and command.
 * If everything works fine, given command will be invoked 
 * on the remote side and outputs will be printed out.
 *
 */
import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Exec{
  public static void custom(String[] arg){
    try{
      
      JSch jsch=new JSch();        
      
      if(arg.length!=2 || !arg[0].contains(",") || !arg[0].contains("@")){
        System.err.println("usage: y execSsh user,pass@remotehost command");
        System.exit(-1);
      }      
      
      String user=arg[0].split("@")[0].split(",")[0];
      String senha=arg[0].split("@")[0].split(",")[1];
      String host=arg[0].split("@")[1];
      String command=arg[1];
      
      /*
      String host=null;
      if(arg.length>0){
        host=arg[0];
      }
      else{
        host=JOptionPane.showInputDialog("Enter username@hostname",
                                         System.getProperty("user.name")+
                                         "@localhost"); 
      }
      String user=host.substring(0, host.indexOf('@'));
      host=host.substring(host.indexOf('@')+1);
      */
      
      Session session=jsch.getSession(user, host, 22);
      
      /*
      String xhost="127.0.0.1";
      int xport=0;
      String display=JOptionPane.showInputDialog("Enter display name", 
                                                 xhost+":"+xport);
      xhost=display.substring(0, display.indexOf(':'));
      xport=Integer.parseInt(display.substring(display.indexOf(':')+1));
      session.setX11Host(xhost);
      session.setX11Port(xport+6000);
      */

      // username and password will be given via UserInfo interface.
      UserInfo ui=new MyUserInfo(senha);
      session.setUserInfo(ui);
      session.connect();

      //String command=JOptionPane.showInputDialog("Enter command", 
      //                                           "set|grep SSH");

      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);

      // X Forwarding
      // channel.setXForwarding(true);

      //channel.setInputStream(System.in);
      channel.setInputStream(null);

      //channel.setOutputStream(System.out);

      //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
      //((ChannelExec)channel).setErrStream(fos);
      ((ChannelExec)channel).setErrStream(System.err);

      InputStream in=channel.getInputStream();

      channel.connect();

      byte[] tmp=new byte[1024];
      while(true){
        while(in.available()>0){
          int i=in.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          if(in.available()>0) continue;
          System.out.println("exit-status: "+channel.getExitStatus());
          break;
        }
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
      channel.disconnect();
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
  }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    String passwd;
    String senha;
    
        private MyUserInfo(String senha) {
            this.senha=senha;
        }
        
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){
       return true;
    }
    
    JTextField passwordField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    
    public boolean promptPassword(String message){
        passwd=senha;
        return true;
    }
    
    public void showMessage(String message){
        System.err.println("nao implementado!");
        System.exit(1);
    }
    
    final GridBagConstraints gbc = 
      new GridBagConstraints(0,0,1,1,1,1,
                             GridBagConstraints.NORTHWEST,
                             GridBagConstraints.NONE,
                             new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
        System.err.println("nao implementado!");
        System.exit(1);
        return null;
    }
  }
}
