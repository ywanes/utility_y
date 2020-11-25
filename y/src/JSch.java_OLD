import java.awt.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.*;
import java.net.Socket;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;

public class JSch{
  /**
   * The version number.
   */
  public static final String VERSION  = "0.1.54";

  static java.util.Hashtable config=new java.util.Hashtable();
  static{
    config.put("kex", "ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha256,diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1");
    config.put("server_host_key", "ssh-rsa,ssh-dss,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");
    config.put("cipher.s2c",
               "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc");
    config.put("cipher.c2s",
               "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc");

    config.put("mac.s2c", "hmac-md5,hmac-sha1,hmac-sha2-256,hmac-sha1-96,hmac-md5-96");
    config.put("mac.c2s", "hmac-md5,hmac-sha1,hmac-sha2-256,hmac-sha1-96,hmac-md5-96");
    config.put("compression.s2c", "none");
    config.put("compression.c2s", "none");

    config.put("lang.s2c", "");
    config.put("lang.c2s", "");

    config.put("compression_level", "6");

    config.put("diffie-hellman-group-exchange-sha1",
                                "com.jcraft.jsch.DHGEX");
    config.put("diffie-hellman-group1-sha1",
                                "com.jcraft.jsch.DHG1");
    config.put("diffie-hellman-group14-sha1",
               "com.jcraft.jsch.DHG14");    // available since JDK8.
    config.put("diffie-hellman-group-exchange-sha256",
               "com.jcraft.jsch.DHGEX256"); // available since JDK1.4.2.
                                            // On JDK8, 2048bits will be used.
    config.put("ecdsa-sha2-nistp256", "com.jcraft.jsch.jce.SignatureECDSA");
    config.put("ecdsa-sha2-nistp384", "com.jcraft.jsch.jce.SignatureECDSA");
    config.put("ecdsa-sha2-nistp521", "com.jcraft.jsch.jce.SignatureECDSA");

    config.put("ecdh-sha2-nistp256", "com.jcraft.jsch.DHEC256");
    config.put("ecdh-sha2-nistp384", "com.jcraft.jsch.DHEC384");
    config.put("ecdh-sha2-nistp521", "com.jcraft.jsch.DHEC521");

    config.put("ecdh-sha2-nistp", "com.jcraft.jsch.jce.ECDHN");

    config.put("dh",            "com.jcraft.jsch.jce.DH");
    config.put("3des-cbc",      "com.jcraft.jsch.jce.TripleDESCBC");
    config.put("blowfish-cbc",  "com.jcraft.jsch.jce.BlowfishCBC");
    config.put("hmac-sha1",     "com.jcraft.jsch.jce.HMACSHA1");
    config.put("hmac-sha1-96",  "com.jcraft.jsch.jce.HMACSHA196");
    config.put("hmac-sha2-256",  "com.jcraft.jsch.jce.HMACSHA256");
    // The "hmac-sha2-512" will require the key-length 2048 for DH,
    // but Sun's JCE has not allowed to use such a long key.
    //config.put("hmac-sha2-512",  "com.jcraft.jsch.jce.HMACSHA512");
    config.put("hmac-md5",      "com.jcraft.jsch.jce.HMACMD5");
    config.put("hmac-md5-96",   "com.jcraft.jsch.jce.HMACMD596");
    config.put("sha-1",         "com.jcraft.jsch.jce.SHA1");
    config.put("sha-256",         "com.jcraft.jsch.jce.SHA256");
    config.put("sha-384",         "com.jcraft.jsch.jce.SHA384");
    config.put("sha-512",         "com.jcraft.jsch.jce.SHA512");
    config.put("md5",           "com.jcraft.jsch.jce.MD5");
    config.put("signature.dss", "com.jcraft.jsch.jce.SignatureDSA");
    config.put("signature.rsa", "com.jcraft.jsch.jce.SignatureRSA");
    config.put("signature.ecdsa", "com.jcraft.jsch.jce.SignatureECDSA");
    config.put("keypairgen.dsa",   "com.jcraft.jsch.jce.KeyPairGenDSA");
    config.put("keypairgen.rsa",   "com.jcraft.jsch.jce.KeyPairGenRSA");
    config.put("keypairgen.ecdsa", "com.jcraft.jsch.jce.KeyPairGenECDSA");
    //config.put("random",        "com.jcraft.jsch.jce.Random");
    config.put("random",        "Random2");

    config.put("none",           "com.jcraft.jsch.CipherNone");

    config.put("aes128-cbc",    "com.jcraft.jsch.jce.AES128CBC");
    config.put("aes192-cbc",    "com.jcraft.jsch.jce.AES192CBC");
    config.put("aes256-cbc",    "com.jcraft.jsch.jce.AES256CBC");

    config.put("aes128-ctr",    "com.jcraft.jsch.jce.AES128CTR");
    config.put("aes192-ctr",    "com.jcraft.jsch.jce.AES192CTR");
    config.put("aes256-ctr",    "com.jcraft.jsch.jce.AES256CTR");
    config.put("3des-ctr",      "com.jcraft.jsch.jce.TripleDESCTR");
    config.put("arcfour",      "com.jcraft.jsch.jce.ARCFOUR");
    config.put("arcfour128",      "com.jcraft.jsch.jce.ARCFOUR128");
    config.put("arcfour256",      "com.jcraft.jsch.jce.ARCFOUR256");

    config.put("userauth.none",    "com.jcraft.jsch.UserAuthNone");
    config.put("userauth.password",    "com.jcraft.jsch.UserAuthPassword");
    config.put("userauth.keyboard-interactive",    "com.jcraft.jsch.UserAuthKeyboardInteractive");
    config.put("userauth.publickey",    "com.jcraft.jsch.UserAuthPublicKey");
    config.put("userauth.gssapi-with-mic",    "com.jcraft.jsch.UserAuthGSSAPIWithMIC");
    config.put("gssapi-with-mic.krb5",    "com.jcraft.jsch.jgss.GSSContextKrb5");

    config.put("zlib",             "com.jcraft.jsch.jcraft.Compression");
    config.put("zlib@openssh.com", "com.jcraft.jsch.jcraft.Compression");

    config.put("pbkdf", "com.jcraft.jsch.jce.PBKDF");

    config.put("StrictHostKeyChecking",  "ask");
    config.put("HashKnownHosts",  "no");

    config.put("PreferredAuthentications", "gssapi-with-mic,publickey,keyboard-interactive,password");

    config.put("CheckCiphers", "aes256-ctr,aes192-ctr,aes128-ctr,aes256-cbc,aes192-cbc,aes128-cbc,3des-ctr,arcfour,arcfour128,arcfour256");
    config.put("CheckKexes", "diffie-hellman-group14-sha1,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521");
    config.put("CheckSignatures", "ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521");

    config.put("MaxAuthTries", "6");
    config.put("ClearAllForwardings", "no");
  }

    static void ScpFrom(String[] args) {
        ScpFrom.custom(args);
    }

    static void ScpTo(String[] args) {
        ScpTo.custom(args);
    }

    static void Exec(String[] args) {
        Exec.custom(args);
    }

    static void Shell(String[] args) {
        Shell.custom(args);
    }

    static void Sftp(String[] args) {
        Sftp.custom(args);
    }

  private java.util.Vector sessionPool = new java.util.Vector();

  private IdentityRepository defaultIdentityRepository =
    new LocalIdentityRepository(this);

  private IdentityRepository identityRepository = defaultIdentityRepository;

  private ConfigRepository configRepository = null;

  /**
   * Sets the <code>identityRepository</code>, which will be referred
   * in the public key authentication.
   *
   * @param identityRepository if <code>null</code> is given,
   * the default repository, which usually refers to ~/.ssh/, will be used.
   *
   * @see #getIdentityRepository()
   */
  public synchronized void setIdentityRepository(IdentityRepository identityRepository){
    if(identityRepository == null){
      this.identityRepository = defaultIdentityRepository;
    }
    else{
      this.identityRepository = identityRepository;
    }
  }

  public synchronized IdentityRepository getIdentityRepository(){
    return this.identityRepository;
  }

  public ConfigRepository getConfigRepository() {
    return this.configRepository;
  }

  public void setConfigRepository(ConfigRepository configRepository) {
    this.configRepository = configRepository;
  }

  private HostKeyRepository known_hosts=null;

  private static final Logger DEVNULL=new Logger(){
      public boolean isEnabled(int level){return false;}
      public void log(int level, String message){}
    };
  static Logger logger=DEVNULL;

  public JSch(){
    /*
    // The JCE of Sun's Java5 on Mac OS X has the resource leak bug
    // in calculating HMAC, so we need to use our own implementations.
    try{
      String osname=(String)(System.getProperties().get("os.name"));
      if(osname!=null && osname.equals("Mac OS X")){
        config.put("hmac-sha1",     "com.jcraft.jsch.jcraft.HMACSHA1");
        config.put("hmac-md5",      "com.jcraft.jsch.jcraft.HMACMD5");
        config.put("hmac-md5-96",   "com.jcraft.jsch.jcraft.HMACMD596");
        config.put("hmac-sha1-96",  "com.jcraft.jsch.jcraft.HMACSHA196");
      }
    }
    catch(Exception e){
    }
    */
  }

  /**
   * Instantiates the <code>Session</code> object with
   * <code>host</code>.  The user name and port number will be retrieved from
   * ConfigRepository.  If user name is not given,
   * the system property "user.name" will be referred.
   *
   * @param host hostname
   *
   * @throws JSchException
   *         if <code>username</code> or <code>host</code> are invalid.
   *
   * @return the instance of <code>Session</code> class.
   *
   * @see #getSession(String username, String host, int port)
   * @see com.jcraft.jsch.Session
   * @see com.jcraft.jsch.ConfigRepository
   */
  public Session getSession(String host)
     throws JSchException {
    return getSession(null, host, 22);
  }

  /**
   * Instantiates the <code>Session</code> object with
   * <code>username</code> and <code>host</code>.
   * The TCP port 22 will be used in making the connection.
   * Note that the TCP connection must not be established
   * until Session#connect().
   *
   * @param username user name
   * @param host hostname
   *
   * @throws JSchException
   *         if <code>username</code> or <code>host</code> are invalid.
   *
   * @return the instance of <code>Session</code> class.
   *
   * @see #getSession(String username, String host, int port)
   * @see com.jcraft.jsch.Session
   */
  public Session getSession(String username, String host)
     throws JSchException {
    return getSession(username, host, 22);
  }

  /**
   * Instantiates the <code>Session</code> object with given
   * <code>username</code>, <code>host</code> and <code>port</code>.
   * Note that the TCP connection must not be established
   * until Session#connect().
   *
   * @param username user name
   * @param host hostname
   * @param port port number
   *
   * @throws JSchException
   *         if <code>username</code> or <code>host</code> are invalid.
   *
   * @return the instance of <code>Session</code> class.
   *
   * @see #getSession(String username, String host, int port)
   * @see com.jcraft.jsch.Session
   */
  public Session getSession(String username, String host, int port) throws JSchException {
    if(host==null){
      throw new JSchException("host must not be null.");
    }
    Session s = new Session(this, username, host, port);
    return s;
  }

  protected void addSession(Session session){
    synchronized(sessionPool){
      sessionPool.addElement(session);
    }
  }

  protected boolean removeSession(Session session){
    synchronized(sessionPool){
      return sessionPool.remove(session);
    }
  }

  /**
   * Sets the hostkey repository.
   *
   * @param hkrepo
   *
   * @see com.jcraft.jsch.HostKeyRepository
   * @see com.jcraft.jsch.KnownHosts
   */
  public void setHostKeyRepository(HostKeyRepository hkrepo){
    known_hosts=hkrepo;
  }

  /**
   * Sets the instance of <code>KnownHosts</code>, which refers
   * to <code>filename</code>.
   *
   * @param filename filename of known_hosts file.
   *
   * @throws JSchException
   *         if the given filename is invalid.
   *
   * @see com.jcraft.jsch.KnownHosts
   */
  public void setKnownHosts(String filename) throws JSchException{
    if(known_hosts==null) known_hosts=new KnownHosts(this);
    if(known_hosts instanceof KnownHosts){
      synchronized(known_hosts){
        ((KnownHosts)known_hosts).setKnownHosts(filename);
      }
    }
  }

  /**
   * Sets the instance of <code>KnownHosts</code> generated with
   * <code>stream</code>.
   *
   * @param stream the instance of InputStream from known_hosts file.
   *
   * @throws JSchException
   *         if an I/O error occurs.
   *
   * @see com.jcraft.jsch.KnownHosts
   */
  public void setKnownHosts(InputStream stream) throws JSchException{
    if(known_hosts==null) known_hosts=new KnownHosts(this);
    if(known_hosts instanceof KnownHosts){
      synchronized(known_hosts){
        ((KnownHosts)known_hosts).setKnownHosts(stream);
      }
    }
  }

  /**
   * Returns the current hostkey repository.
   * By the default, this method will the instance of <code>KnownHosts</code>.
   *
   * @return current hostkey repository.
   *
   * @see com.jcraft.jsch.HostKeyRepository
   * @see com.jcraft.jsch.KnownHosts
   */
  public HostKeyRepository getHostKeyRepository(){
    if(known_hosts==null) known_hosts=new KnownHosts(this);
    return known_hosts;
  }

  /**
   * Sets the private key, which will be referred in
   * the public key authentication.
   *
   * @param prvkey filename of the private key.
   *
   * @throws JSchException if <code>prvkey</code> is invalid.
   *
   * @see #addIdentity(String prvkey, String passphrase)
   */
  public void addIdentity(String prvkey) throws JSchException{
    addIdentity(prvkey, (byte[])null);
  }

  /**
   * Sets the private key, which will be referred in
   * the public key authentication.
   * Before registering it into identityRepository,
   * it will be deciphered with <code>passphrase</code>.
   *
   * @param prvkey filename of the private key.
   * @param passphrase passphrase for <code>prvkey</code>.
   *
   * @throws JSchException if <code>passphrase</code> is not right.
   *
   * @see #addIdentity(String prvkey, byte[] passphrase)
   */
  public void addIdentity(String prvkey, String passphrase) throws JSchException{
    byte[] _passphrase=null;
    if(passphrase!=null){
      _passphrase=Util.str2byte(passphrase);
    }
    addIdentity(prvkey, _passphrase);
    if(_passphrase!=null)
      Util.bzero(_passphrase);
  }

  /**
   * Sets the private key, which will be referred in
   * the public key authentication.
   * Before registering it into identityRepository,
   * it will be deciphered with <code>passphrase</code>.
   *
   * @param prvkey filename of the private key.
   * @param passphrase passphrase for <code>prvkey</code>.
   *
   * @throws JSchException if <code>passphrase</code> is not right.
   *
   * @see #addIdentity(String prvkey, String pubkey, byte[] passphrase)
   */
  public void addIdentity(String prvkey, byte[] passphrase) throws JSchException{
    Identity identity=IdentityFile.newInstance(prvkey, null, this);
    addIdentity(identity, passphrase);
  }

  /**
   * Sets the private key, which will be referred in
   * the public key authentication.
   * Before registering it into identityRepository,
   * it will be deciphered with <code>passphrase</code>.
   *
   * @param prvkey filename of the private key.
   * @param pubkey filename of the public key.
   * @param passphrase passphrase for <code>prvkey</code>.
   *
   * @throws JSchException if <code>passphrase</code> is not right.
   */
  public void addIdentity(String prvkey, String pubkey, byte[] passphrase) throws JSchException{
    Identity identity=IdentityFile.newInstance(prvkey, pubkey, this);
    addIdentity(identity, passphrase);
  }

  /**
   * Sets the private key, which will be referred in
   * the public key authentication.
   * Before registering it into identityRepository,
   * it will be deciphered with <code>passphrase</code>.
   *
   * @param name name of the identity to be used to
                 retrieve it in the identityRepository.
   * @param prvkey private key in byte array.
   * @param pubkey public key in byte array.
   * @param passphrase passphrase for <code>prvkey</code>.
   *
   */
  public void addIdentity(String name, byte[]prvkey, byte[]pubkey, byte[] passphrase) throws JSchException{
    Identity identity=IdentityFile.newInstance(name, prvkey, pubkey, this);
    addIdentity(identity, passphrase);
  }

  /**
   * Sets the private key, which will be referred in
   * the public key authentication.
   * Before registering it into identityRepository,
   * it will be deciphered with <code>passphrase</code>.
   *
   * @param identity private key.
   * @param passphrase passphrase for <code>identity</code>.
   *
   * @throws JSchException if <code>passphrase</code> is not right.
   */
  public void addIdentity(Identity identity, byte[] passphrase) throws JSchException{
    if(passphrase!=null){
      try{
        byte[] goo=new byte[passphrase.length];
        System.arraycopy(passphrase, 0, goo, 0, passphrase.length);
        passphrase=goo;
        identity.setPassphrase(passphrase);
      }
      finally{
        Util.bzero(passphrase);
      }
    }

    if(identityRepository instanceof LocalIdentityRepository){
      ((LocalIdentityRepository)identityRepository).add(identity);
    }
    else if(identity instanceof IdentityFile && !identity.isEncrypted()) {
      identityRepository.add(((IdentityFile)identity).getKeyPair().forSSHAgent());
    }
    else {
      synchronized(this){
        if(!(identityRepository instanceof IdentityRepository.Wrapper)){
          setIdentityRepository(new IdentityRepository.Wrapper(identityRepository));
        }
      }
      ((IdentityRepository.Wrapper)identityRepository).add(identity);
    }
  }

  /**
   * @deprecated use #removeIdentity(Identity identity)
   */
  public void removeIdentity(String name) throws JSchException{
    Vector identities = identityRepository.getIdentities();
    for(int i=0; i<identities.size(); i++){
      Identity identity=(Identity)(identities.elementAt(i));
      if(!identity.getName().equals(name))
        continue;
      if(identityRepository instanceof LocalIdentityRepository){
        ((LocalIdentityRepository)identityRepository).remove(identity);
      }
      else
        identityRepository.remove(identity.getPublicKeyBlob());
    }
  }

  /**
   * Removes the identity from identityRepository.
   *
   * @param identity the indentity to be removed.
   *
   * @throws JSchException if <code>identity</code> is invalid.
   */
  public void removeIdentity(Identity identity) throws JSchException{
    identityRepository.remove(identity.getPublicKeyBlob());
  }

  /**
   * Lists names of identities included in the identityRepository.
   *
   * @return names of identities
   *
   * @throws JSchException if identityReposory has problems.
   */
  public Vector getIdentityNames() throws JSchException{
    Vector foo=new Vector();
    Vector identities = identityRepository.getIdentities();
    for(int i=0; i<identities.size(); i++){
      Identity identity=(Identity)(identities.elementAt(i));
      foo.addElement(identity.getName());
    }
    return foo;
  }

  /**
   * Removes all identities from identityRepository.
   *
   * @throws JSchException if identityReposory has problems.
   */
  public void removeAllIdentity() throws JSchException{
    identityRepository.removeAll();
  }

  /**
   * Returns the config value for the specified key.
   *
   * @param key key for the configuration.
   * @return config value
   */
  public static String getConfig(String key){
    synchronized(config){
      return (String)(config.get(key));
    }
  }

  /**
   * Sets or Overrides the configuration.
   *
   * @param newconf configurations
   */
  public static void setConfig(java.util.Hashtable newconf){
    synchronized(config){
      for(java.util.Enumeration e=newconf.keys() ; e.hasMoreElements() ;) {
        String key=(String)(e.nextElement());
        config.put(key, (String)(newconf.get(key)));
      }
    }
  }

  /**
   * Sets or Overrides the configuration.
   *
   * @param key key for the configuration
   * @param value value for the configuration
   */
  public static void setConfig(String key, String value){
    config.put(key, value);
  }

  /**
   * Sets the logger
   *
   * @param logger logger
   *
   * @see com.jcraft.jsch.Logger
   */
  public static void setLogger(Logger logger){
    if(logger==null) logger=DEVNULL;
    JSch.logger=logger;
  }

  static Logger getLogger(){
    return logger;
  }
}

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


class Buffer{
  final byte[] tmp=new byte[4];
  byte[] buffer;
  int index;
  int s;
  public Buffer(int size){
    buffer=new byte[size];
    index=0;
    s=0;
  }
  public Buffer(byte[] buffer){
    this.buffer=buffer;
    index=0;
    s=0;
  }
  public Buffer(){ this(1024*10*2); }
  public void putByte(byte foo){
    buffer[index++]=foo;
  }
  public void putByte(byte[] foo) {
    putByte(foo, 0, foo.length);
  }
  public void putByte(byte[] foo, int begin, int length) {
    System.arraycopy(foo, begin, buffer, index, length);
    index+=length;
  }
  public void putString(byte[] foo){
    putString(foo, 0, foo.length);
  }
  public void putString(byte[] foo, int begin, int length) {
    putInt(length);
    putByte(foo, begin, length);
  }
  public void putInt(int val) {
    tmp[0]=(byte)(val >>> 24);
    tmp[1]=(byte)(val >>> 16);
    tmp[2]=(byte)(val >>> 8);
    tmp[3]=(byte)(val);
    System.arraycopy(tmp, 0, buffer, index, 4);
    index+=4;
  }
  public void putLong(long val) {
    tmp[0]=(byte)(val >>> 56);
    tmp[1]=(byte)(val >>> 48);
    tmp[2]=(byte)(val >>> 40);
    tmp[3]=(byte)(val >>> 32);
    System.arraycopy(tmp, 0, buffer, index, 4);
    tmp[0]=(byte)(val >>> 24);
    tmp[1]=(byte)(val >>> 16);
    tmp[2]=(byte)(val >>> 8);
    tmp[3]=(byte)(val);
    System.arraycopy(tmp, 0, buffer, index+4, 4);
    index+=8;
  }
  void skip(int n) {
    index+=n;
  }
  void putPad(int n) {
    while(n>0){
      buffer[index++]=(byte)0;
      n--;
    }
  }
  public void putMPInt(byte[] foo){
    int i=foo.length;
    if((foo[0]&0x80)!=0){
      i++;
      putInt(i);
      putByte((byte)0);
    }
    else{
      putInt(i);
    }
    putByte(foo);
  }
  public int getLength(){
    return index-s;
  }
  public int getOffSet(){
    return s;
  }
  public void setOffSet(int s){
    this.s=s;
  }
  public long getLong(){
    long foo = getInt()&0xffffffffL;
    foo = ((foo<<32)) | (getInt()&0xffffffffL);
    return foo;
  }
  public int getInt(){
    int foo = getShort();
    foo = ((foo<<16)&0xffff0000) | (getShort()&0xffff);
    return foo;
  }
  public long getUInt(){
    long foo = 0L;
    long bar = 0L;
    foo = getByte();
    foo = ((foo<<8)&0xff00)|(getByte()&0xff);
    bar = getByte();
    bar = ((bar<<8)&0xff00)|(getByte()&0xff);
    foo = ((foo<<16)&0xffff0000) | (bar&0xffff);
    return foo;
  }
  int getShort() {
    int foo = getByte();
    foo = ((foo<<8)&0xff00)|(getByte()&0xff);
    return foo;
  }
  public int getByte() {
    return (buffer[s++]&0xff);
  }
  public void getByte(byte[] foo) {
    getByte(foo, 0, foo.length);
  }
  void getByte(byte[] foo, int start, int len) {
    System.arraycopy(buffer, s, foo, start, len);
    s+=len;
  }
  public int getByte(int len) {
    int foo=s;
    s+=len;
    return foo;
  }
  public byte[] getMPInt() {
    int i=getInt();  // uint32
    if(i<0 ||  // bigger than 0x7fffffff
       i>8*1024){
      // TODO: an exception should be thrown.
      i = 8*1024; // the session will be broken, but working around OOME.
    }
    byte[] foo=new byte[i];
    getByte(foo, 0, i);
    return foo;
  }
  public byte[] getMPIntBits() {
    int bits=getInt();
    int bytes=(bits+7)/8;
    byte[] foo=new byte[bytes];
    getByte(foo, 0, bytes);
    if((foo[0]&0x80)!=0){
      byte[] bar=new byte[foo.length+1];
      bar[0]=0; // ??
      System.arraycopy(foo, 0, bar, 1, foo.length);
      foo=bar;
    }
    return foo;
  }
  public byte[] getString() {
    int i = getInt();  // uint32
    if(i<0 ||  // bigger than 0x7fffffff
       i>256*1024){
      // TODO: an exception should be thrown.
      i = 256*1024; // the session will be broken, but working around OOME.
    }
    byte[] foo=new byte[i];
    getByte(foo, 0, i);
    return foo;
  }
  byte[] getString(int[]start, int[]len) {
    int i=getInt();
    start[0]=getByte(i);
    len[0]=i;
    return buffer;
  }
  public void reset(){
    index=0;
    s=0;
  }
  public void shift(){
    if(s==0)return;
    System.arraycopy(buffer, s, buffer, 0, index-s);
    index=index-s;
    s=0;
  }
  void rewind(){
    s=0;
  }

  byte getCommand(){
    return buffer[5];
  }

  void checkFreeSize(int n){
    int size = index+n+Session.buffer_margin;
    if(buffer.length<size){
      int i = buffer.length*2;
      if(i<size) i = size;
      byte[] tmp = new byte[i];
      System.arraycopy(buffer, 0, tmp, 0, index);
      buffer = tmp;
    }
  }

  byte[][] getBytes(int n, String msg) throws JSchException {
    byte[][] tmp = new byte[n][];
    for(int i = 0; i < n; i++){
      int j = getInt();
      if(getLength() < j){
        throw new JSchException(msg);
      }
      tmp[i] = new byte[j];
      getByte(tmp[i]);
    }
    return tmp;
  }

  /*
  static Buffer fromBytes(byte[]... args){
    int length = args.length*4;
    for(int i = 0; i < args.length; i++){
      length += args[i].length;
    }
    Buffer buf = new Buffer(length);
    for(int i = 0; i < args.length; i++){
      buf.putString(args[i]);
    }
    return buf;
  }
  */

  static Buffer fromBytes(byte[][] args){
    int length = args.length*4;
    for(int i = 0; i < args.length; i++){
      length += args[i].length;
    }
    Buffer buf = new Buffer(length);
    for(int i = 0; i < args.length; i++){
      buf.putString(args[i]);
    }
    return buf;
  }


/*
  static String[] chars={
    "0","1","2","3","4","5","6","7","8","9", "a","b","c","d","e","f"
  };
  static void dump_buffer(){
    int foo;
    for(int i=0; i<tmp_buffer_index; i++){
        foo=tmp_buffer[i]&0xff;
        System.err.print(chars[(foo>>>4)&0xf]);
        System.err.print(chars[foo&0xf]);
        if(i%16==15){
          System.err.println("");
          continue;
        }
        if(i>0 && i%2==1){
          System.err.print(" ");
        }
    }
    System.err.println("");
  }
  static void dump(byte[] b){
    dump(b, 0, b.length);
  }
  static void dump(byte[] b, int s, int l){
    for(int i=s; i<s+l; i++){
      System.err.print(Integer.toHexString(b[i]&0xff)+":");
    }
    System.err.println("");
  }
*/

}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2006-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelAgentForwarding extends Channel{

  static private final int LOCAL_WINDOW_SIZE_MAX=0x20000;
  static private final int LOCAL_MAXIMUM_PACKET_SIZE=0x4000;

  private final byte SSH_AGENTC_REQUEST_RSA_IDENTITIES = 1;
  private final byte SSH_AGENT_RSA_IDENTITIES_ANSWER = 2;
  private final byte SSH_AGENTC_RSA_CHALLENGE = 3;
  private final byte SSH_AGENT_RSA_RESPONSE = 4;
  private final byte SSH_AGENT_FAILURE = 5;
  private final byte SSH_AGENT_SUCCESS = 6;
  private final byte SSH_AGENTC_ADD_RSA_IDENTITY        = 7;
  private final byte SSH_AGENTC_REMOVE_RSA_IDENTITY = 8;
  private final byte SSH_AGENTC_REMOVE_ALL_RSA_IDENTITIES = 9;

  private final byte SSH2_AGENTC_REQUEST_IDENTITIES=11;
  private final byte SSH2_AGENT_IDENTITIES_ANSWER=12;
  private final byte SSH2_AGENTC_SIGN_REQUEST=13;
  private final byte SSH2_AGENT_SIGN_RESPONSE=14;
  private final byte SSH2_AGENTC_ADD_IDENTITY=17;
  private final byte SSH2_AGENTC_REMOVE_IDENTITY=18;
  private final byte SSH2_AGENTC_REMOVE_ALL_IDENTITIES=19;
  private final byte SSH2_AGENT_FAILURE=30;

  boolean init=true;

  private Buffer rbuf=null;
  private Buffer wbuf=null;
  private Packet packet=null;
  private Buffer mbuf=null;

  ChannelAgentForwarding(){
    super();

    setLocalWindowSizeMax(LOCAL_WINDOW_SIZE_MAX);
    setLocalWindowSize(LOCAL_WINDOW_SIZE_MAX);
    setLocalPacketSize(LOCAL_MAXIMUM_PACKET_SIZE);

    type=Util.str2byte("auth-agent@openssh.com");
    rbuf=new Buffer();
    rbuf.reset();
    //wbuf=new Buffer(rmpsize);
    //packet=new Packet(wbuf);
    mbuf=new Buffer();
    connected=true;
  }

  public void run(){
    try{
      sendOpenConfirmation();
    }
    catch(Exception e){
      close=true;
      disconnect();
    }
  }

  void write(byte[] foo, int s, int l) throws java.io.IOException {

    if(packet==null){
      wbuf=new Buffer(rmpsize);
      packet=new Packet(wbuf);
    }

    rbuf.shift();
    if(rbuf.buffer.length<rbuf.index+l){
      byte[] newbuf=new byte[rbuf.s+l];
      System.arraycopy(rbuf.buffer, 0, newbuf, 0, rbuf.buffer.length);
      rbuf.buffer=newbuf;
    }

    rbuf.putByte(foo, s, l);

    int mlen=rbuf.getInt();
    if(mlen>rbuf.getLength()){
      rbuf.s-=4;
      return;
    }

    int typ=rbuf.getByte();

    Session _session=null;
    try{
      _session=getSession();
    }
    catch(JSchException e){
      throw new java.io.IOException(e.toString());
    }

    IdentityRepository irepo = _session.getIdentityRepository();
    UserInfo userinfo=_session.getUserInfo();

    mbuf.reset();

    if(typ==SSH2_AGENTC_REQUEST_IDENTITIES){
      mbuf.putByte(SSH2_AGENT_IDENTITIES_ANSWER);
      Vector identities = irepo.getIdentities();
      synchronized(identities){
        int count=0;
        for(int i=0; i<identities.size(); i++){
          Identity identity=(Identity)(identities.elementAt(i));
          if(identity.getPublicKeyBlob()!=null)
            count++;
        }
        mbuf.putInt(count);
        for(int i=0; i<identities.size(); i++){
          Identity identity=(Identity)(identities.elementAt(i));
          byte[] pubkeyblob=identity.getPublicKeyBlob();
          if(pubkeyblob==null)
            continue;
          mbuf.putString(pubkeyblob);
          mbuf.putString(Util.empty);
        }
      }
    }
    else if(typ==SSH_AGENTC_REQUEST_RSA_IDENTITIES) {
      mbuf.putByte(SSH_AGENT_RSA_IDENTITIES_ANSWER);
      mbuf.putInt(0);
    }
    else if(typ==SSH2_AGENTC_SIGN_REQUEST){
      byte[] blob=rbuf.getString();
      byte[] data=rbuf.getString();
      int flags=rbuf.getInt();

//      if((flags & 1)!=0){ //SSH_AGENT_OLD_SIGNATURE // old OpenSSH 2.0, 2.1
//        datafellows = SSH_BUG_SIGBLOB;
//      }

      Vector identities = irepo.getIdentities();
      Identity identity = null;
      synchronized(identities){
        for(int i=0; i<identities.size(); i++){
          Identity _identity=(Identity)(identities.elementAt(i));
          if(_identity.getPublicKeyBlob()==null)
            continue;
          if(!Util.array_equals(blob, _identity.getPublicKeyBlob())){
            continue;
          }
          if(_identity.isEncrypted()){
            if(userinfo==null)
              continue;
            while(_identity.isEncrypted()){
              if(!userinfo.promptPassphrase("Passphrase for "+_identity.getName())){
                break;
              }

              String _passphrase=userinfo.getPassphrase();
              if(_passphrase==null){
                break;
              }

              byte[] passphrase=Util.str2byte(_passphrase);
              try{
                if(_identity.setPassphrase(passphrase)){
                  break;
                }
              }
              catch(JSchException e){
                break;
              }
            }
          }

          if(!_identity.isEncrypted()){
            identity=_identity;
            break;
          }
        }
      }

      byte[] signature=null;

      if(identity!=null){
        signature=identity.getSignature(data);
      }

      if(signature==null){
        mbuf.putByte(SSH2_AGENT_FAILURE);
      }
      else{
        mbuf.putByte(SSH2_AGENT_SIGN_RESPONSE);
        mbuf.putString(signature);
      }
    }
    else if(typ==SSH2_AGENTC_REMOVE_IDENTITY){
      byte[] blob=rbuf.getString();
      irepo.remove(blob);
      mbuf.putByte(SSH_AGENT_SUCCESS);
    }
    else if(typ==SSH_AGENTC_REMOVE_ALL_RSA_IDENTITIES){
      mbuf.putByte(SSH_AGENT_SUCCESS);
    }
    else if(typ==SSH2_AGENTC_REMOVE_ALL_IDENTITIES){
      irepo.removeAll();
      mbuf.putByte(SSH_AGENT_SUCCESS);
    }
    else if(typ==SSH2_AGENTC_ADD_IDENTITY){
      int fooo = rbuf.getLength();
      byte[] tmp = new byte[fooo];
      rbuf.getByte(tmp);
      boolean result = irepo.add(tmp);
      mbuf.putByte(result ? SSH_AGENT_SUCCESS : SSH_AGENT_FAILURE);
    }
    else {
      rbuf.skip(rbuf.getLength()-1);
      mbuf.putByte(SSH_AGENT_FAILURE);
    }

    byte[] response = new byte[mbuf.getLength()];
    mbuf.getByte(response);
    send(response);
  }

  private void send(byte[] message){
    packet.reset();
    wbuf.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
    wbuf.putInt(recipient);
    wbuf.putInt(4+message.length);
    wbuf.putString(message);

    try{
      getSession().write(packet, this, 4+message.length);
    }
    catch(Exception e){
    }
  }

  void eof_remote(){
    super.eof_remote();
    eof();
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelDirectTCPIP extends Channel{

  static private final int LOCAL_WINDOW_SIZE_MAX=0x20000;
  static private final int LOCAL_MAXIMUM_PACKET_SIZE=0x4000;
  static private final byte[] _type = Util.str2byte("direct-tcpip");
  String host;
  int port;

  String originator_IP_address="127.0.0.1";
  int originator_port=0;

  ChannelDirectTCPIP(){
    super();
    type = _type;
    setLocalWindowSizeMax(LOCAL_WINDOW_SIZE_MAX);
    setLocalWindowSize(LOCAL_WINDOW_SIZE_MAX);
    setLocalPacketSize(LOCAL_MAXIMUM_PACKET_SIZE);
  }

  void init (){
    io=new IO();
  }

  public void connect(int connectTimeout) throws JSchException{
    this.connectTimeout=connectTimeout;
    try{
      Session _session=getSession();
      if(!_session.isConnected()){
        throw new JSchException("session is down");
      }

      if(io.in!=null){
        thread=new Thread(this);
        thread.setName("DirectTCPIP thread "+_session.getHost());
        if(_session.daemon_thread){
          thread.setDaemon(_session.daemon_thread);
        }
        thread.start();
      }
      else {
        sendChannelOpen();
      }
    }
    catch(Exception e){
      io.close();
      io=null;
      Channel.del(this);
      if (e instanceof JSchException) {
        throw (JSchException) e;
      }
    }
  }

  public void run(){

    try{
      sendChannelOpen();

      Buffer buf=new Buffer(rmpsize);
      Packet packet=new Packet(buf);
      Session _session=getSession();
      int i=0;

      while(isConnected() &&
            thread!=null &&
            io!=null &&
            io.in!=null){
        i=io.in.read(buf.buffer,
                     14,
                     buf.buffer.length-14
                     -Session.buffer_margin
                     );
        if(i<=0){
          eof();
          break;
        }
        packet.reset();
        buf.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
        buf.putInt(recipient);
        buf.putInt(i);
        buf.skip(i);
        synchronized(this){
          if(close)
            break;
          _session.write(packet, this, i);
        }
      }
    }
    catch(Exception e){
      // Whenever an exception is thrown by sendChannelOpen(),
      // 'connected' is false.
      if(!connected){
        connected=true;
      }
      disconnect();
      return;
    }

    eof();
    disconnect();
  }

  public void setInputStream(InputStream in){
    io.setInputStream(in);
  }
  public void setOutputStream(OutputStream out){
    io.setOutputStream(out);
  }

  public void setHost(String host){this.host=host;}
  public void setPort(int port){this.port=port;}
  public void setOrgIPAddress(String foo){this.originator_IP_address=foo;}
  public void setOrgPort(int foo){this.originator_port=foo;}

  protected Packet genChannelOpenPacket(){
    Buffer buf = new Buffer(50 + // 6 + 4*8 + 12
                            host.length() + originator_IP_address.length() +
                            Session.buffer_margin);
    Packet packet = new Packet(buf);
    // byte   SSH_MSG_CHANNEL_OPEN(90)
    // string channel type         //
    // uint32 sender channel       // 0
    // uint32 initial window size  // 0x100000(65536)
    // uint32 maxmum packet size   // 0x4000(16384)
    packet.reset();
    buf.putByte((byte)90);
    buf.putString(this.type);
    buf.putInt(id);
    buf.putInt(lwsize);
    buf.putInt(lmpsize);
    buf.putString(Util.str2byte(host));
    buf.putInt(port);
    buf.putString(Util.str2byte(originator_IP_address));
    buf.putInt(originator_port);
    return packet;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelExec extends ChannelSession{

  byte[] command=new byte[0];

  public void start() throws JSchException{
    Session _session=getSession();
    try{
      sendRequests();
      Request request=new RequestExec(command);
      request.request(_session, this);
    }
    catch(Exception e){
      if(e instanceof JSchException) throw (JSchException)e;
      if(e instanceof Throwable)
        throw new JSchException("ChannelExec", (Throwable)e);
      throw new JSchException("ChannelExec");
    }

    if(io.in!=null){
      thread=new Thread(this);
      thread.setName("Exec thread "+_session.getHost());
      if(_session.daemon_thread){
        thread.setDaemon(_session.daemon_thread);
      }
      thread.start();
    }
  }

  public void setCommand(String command){
    this.command=Util.str2byte(command);
  }
  public void setCommand(byte[] command){
    this.command=command;
  }

  void init() throws JSchException {
    io.setInputStream(getSession().in);
    io.setOutputStream(getSession().out);
  }

  public void setErrStream(java.io.OutputStream out){
    setExtOutputStream(out);
  }
  public void setErrStream(java.io.OutputStream out, boolean dontclose){
    setExtOutputStream(out, dontclose);
  }
  public java.io.InputStream getErrStream() throws java.io.IOException {
    return getExtInputStream();
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelForwardedTCPIP extends Channel{

  private static Vector pool = new Vector();

  static private final int LOCAL_WINDOW_SIZE_MAX=0x20000;
//static private final int LOCAL_WINDOW_SIZE_MAX=0x100000;
  static private final int LOCAL_MAXIMUM_PACKET_SIZE=0x4000;

  static private final int TIMEOUT=10*1000;

  private Socket socket=null;
  private ForwardedTCPIPDaemon daemon=null;
  private Config config = null;

  ChannelForwardedTCPIP(){
    super();
    setLocalWindowSizeMax(LOCAL_WINDOW_SIZE_MAX);
    setLocalWindowSize(LOCAL_WINDOW_SIZE_MAX);
    setLocalPacketSize(LOCAL_MAXIMUM_PACKET_SIZE);
    io=new IO();
    connected=true;
  }

  public void run(){
    try{
      if(config instanceof ConfigDaemon){
        ConfigDaemon _config = (ConfigDaemon)config;
        Class c=Class.forName(_config.target);
        daemon=(ForwardedTCPIPDaemon)c.newInstance();

        PipedOutputStream out=new PipedOutputStream();
        io.setInputStream(new PassiveInputStream(out
                                                 , 32*1024
                                                 ), false);

        daemon.setChannel(this, getInputStream(), out);
        daemon.setArg(_config.arg);
        new Thread(daemon).start();
      }
      else{
        ConfigLHost _config = (ConfigLHost)config;
        socket=(_config.factory==null) ?
           Util.createSocket(_config.target, _config.lport, TIMEOUT) :
          _config.factory.createSocket(_config.target, _config.lport);
        socket.setTcpNoDelay(true);
        io.setInputStream(socket.getInputStream());
        io.setOutputStream(socket.getOutputStream());
      }
      sendOpenConfirmation();
    }
    catch(Exception e){
      sendOpenFailure(SSH_OPEN_ADMINISTRATIVELY_PROHIBITED);
      close=true;
      disconnect();
      return;
    }

    thread=Thread.currentThread();
    Buffer buf=new Buffer(rmpsize);
    Packet packet=new Packet(buf);
    int i=0;
    try{
      Session _session = getSession();
      while(thread!=null &&
            io!=null &&
            io.in!=null){
        i=io.in.read(buf.buffer,
                     14,
                     buf.buffer.length-14
                     -Session.buffer_margin
                     );
        if(i<=0){
          eof();
          break;
        }
        packet.reset();
        buf.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
        buf.putInt(recipient);
        buf.putInt(i);
        buf.skip(i);
        synchronized(this){
          if(close)
            break;
          _session.write(packet, this, i);
        }
      }
    }
    catch(Exception e){
      //System.err.println(e);
    }
    //thread=null;
    //eof();
    disconnect();
  }

  void getData(Buffer buf){
    setRecipient(buf.getInt());
    setRemoteWindowSize(buf.getUInt());
    setRemotePacketSize(buf.getInt());
    byte[] addr=buf.getString();
    int port=buf.getInt();
    byte[] orgaddr=buf.getString();
    int orgport=buf.getInt();

    /*
    System.err.println("addr: "+Util.byte2str(addr));
    System.err.println("port: "+port);
    System.err.println("orgaddr: "+Util.byte2str(orgaddr));
    System.err.println("orgport: "+orgport);
    */

    Session _session=null;
    try{
      _session=getSession();
    }
    catch(JSchException e){
      // session has been already down.
    }

    this.config = getPort(_session, Util.byte2str(addr), port);
    if(this.config == null)
      this.config = getPort(_session, null, port);

    if(this.config == null){
      if(JSch.getLogger().isEnabled(Logger.ERROR)){
        JSch.getLogger().log(Logger.ERROR,
                             "ChannelForwardedTCPIP: "+Util.byte2str(addr)+":"+port+" is not registered.");
      }
    }
  }

  private static Config getPort(Session session, String address_to_bind, int rport){
    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        Config bar = (Config)(pool.elementAt(i));
        if(bar.session != session) continue;
        if(bar.rport != rport) {
          if(bar.rport != 0 || bar.allocated_rport != rport)
            continue;
        }
        if(address_to_bind != null &&
           !bar.address_to_bind.equals(address_to_bind)) continue;
        return bar;
      }
      return null;
    }
  }

  static String[] getPortForwarding(Session session){
    Vector foo = new Vector();
    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        Config config = (Config)(pool.elementAt(i));
        if(config instanceof ConfigDaemon)
          foo.addElement(config.allocated_rport+":"+config.target+":");
        else
          foo.addElement(config.allocated_rport+":"+config.target+":"+((ConfigLHost)config).lport);
      }
    }
    String[] bar=new String[foo.size()];
    for(int i=0; i<foo.size(); i++){
      bar[i]=(String)(foo.elementAt(i));
    }
    return bar;
  }

  static String normalize(String address){
    if(address==null){ return "localhost"; }
    else if(address.length()==0 || address.equals("*")){ return ""; }
    else{ return address; }
  }

  static void addPort(Session session, String _address_to_bind,
                      int port, int allocated_port, String target, int lport, SocketFactory factory) throws JSchException{
    String address_to_bind=normalize(_address_to_bind);
    synchronized(pool){
      if(getPort(session, address_to_bind, port)!=null){
        throw new JSchException("PortForwardingR: remote port "+port+" is already registered.");
      }
      ConfigLHost config = new ConfigLHost();
      config.session = session;
      config.rport = port;
      config.allocated_rport = allocated_port;
      config.target = target;
      config.lport =lport;
      config.address_to_bind = address_to_bind;
      config.factory = factory;
      pool.addElement(config);
    }
  }
  static void addPort(Session session, String _address_to_bind,
                      int port, int allocated_port, String daemon, Object[] arg) throws JSchException{
    String address_to_bind=normalize(_address_to_bind);
    synchronized(pool){
      if(getPort(session, address_to_bind, port)!=null){
        throw new JSchException("PortForwardingR: remote port "+port+" is already registered.");
      }
      ConfigDaemon config = new ConfigDaemon();
      config.session = session;
      config.rport = port;
      config.allocated_rport = port;
      config.target = daemon;
      config.arg = arg;
      config.address_to_bind = address_to_bind;
      pool.addElement(config);
    }
  }
  static void delPort(ChannelForwardedTCPIP c){
    Session _session=null;
    try{
      _session=c.getSession();
    }
    catch(JSchException e){
      // session has been already down.
    }
    if(_session!=null && c.config!=null)
      delPort(_session, c.config.rport);
  }
  static void delPort(Session session, int rport){
    delPort(session, null, rport);
  }
  static void delPort(Session session, String address_to_bind, int rport){
    synchronized(pool){
      Config foo = getPort(session, normalize(address_to_bind), rport);
      if(foo == null)
        foo = getPort(session, null, rport);
      if(foo==null) return;
      pool.removeElement(foo);
      if(address_to_bind==null){
        address_to_bind=foo.address_to_bind;
      }
      if(address_to_bind==null){
        address_to_bind="0.0.0.0";
      }
    }

    Buffer buf=new Buffer(100); // ??
    Packet packet=new Packet(buf);

    try{
      // byte SSH_MSG_GLOBAL_REQUEST 80
      // string "cancel-tcpip-forward"
      // boolean want_reply
      // string  address_to_bind (e.g. "127.0.0.1")
      // uint32  port number to bind
      packet.reset();
      buf.putByte((byte) 80/*SSH_MSG_GLOBAL_REQUEST*/);
      buf.putString(Util.str2byte("cancel-tcpip-forward"));
      buf.putByte((byte)0);
      buf.putString(Util.str2byte(address_to_bind));
      buf.putInt(rport);
      session.write(packet);
    }
    catch(Exception e){
//    throw new JSchException(e.toString());
    }
  }
  static void delPort(Session session){
    int[] rport=null;
    int count=0;
    synchronized(pool){
      rport=new int[pool.size()];
      for(int i=0; i<pool.size(); i++){
        Config config = (Config)(pool.elementAt(i));
        if(config.session == session) {
          rport[count++]=config.rport; // ((Integer)bar[1]).intValue();
        }
      }
    }
    for(int i=0; i<count; i++){
      delPort(session, rport[i]);
    }
  }

  public int getRemotePort(){return (config!=null ? config.rport: 0);}
  private void setSocketFactory(SocketFactory factory){
    if(config!=null && (config instanceof ConfigLHost) )
      ((ConfigLHost)config).factory = factory;
  }
  static abstract class Config {
    Session session;
    int rport;
    int allocated_rport;
    String address_to_bind;
    String target;
  }

  static class ConfigDaemon extends Config {
    Object[] arg;
  }

  static class ConfigLHost extends Config {
    int lport;
    SocketFactory factory;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/





abstract class Channel implements Runnable{

  static final int SSH_MSG_CHANNEL_OPEN_CONFIRMATION=      91;
  static final int SSH_MSG_CHANNEL_OPEN_FAILURE=           92;
  static final int SSH_MSG_CHANNEL_WINDOW_ADJUST=          93;

  static final int SSH_OPEN_ADMINISTRATIVELY_PROHIBITED=    1;
  static final int SSH_OPEN_CONNECT_FAILED=                 2;
  static final int SSH_OPEN_UNKNOWN_CHANNEL_TYPE=           3;
  static final int SSH_OPEN_RESOURCE_SHORTAGE=              4;

  static int index=0;
  private static java.util.Vector pool=new java.util.Vector();
  static Channel getChannel(String type){
    if(type.equals("session")){
      return new ChannelSession();
    }
    if(type.equals("shell")){
      return new ChannelShell();
    }
    if(type.equals("exec")){
      return new ChannelExec();
    }
    if(type.equals("x11")){
      return new ChannelX11();
    }
    if(type.equals("auth-agent@openssh.com")){
      return new ChannelAgentForwarding();
    }
    if(type.equals("direct-tcpip")){
      return new ChannelDirectTCPIP();
    }
    if(type.equals("forwarded-tcpip")){
      return new ChannelForwardedTCPIP();
    }
    if(type.equals("sftp")){
      return new ChannelSftp();
    }
    if(type.equals("subsystem")){
      return new ChannelSubsystem();
    }
    return null;
  }
  static Channel getChannel(int id, Session session){
    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        Channel c=(Channel)(pool.elementAt(i));
        if(c.id==id && c.session==session) return c;
      }
    }
    return null;
  }
  static void del(Channel c){
    synchronized(pool){
      pool.removeElement(c);
    }
  }

  int id;
  volatile int recipient=-1;
  protected byte[] type=Util.str2byte("foo");
  volatile int lwsize_max=0x100000;
  volatile int lwsize=lwsize_max;     // local initial window size
  volatile int lmpsize=0x4000;     // local maximum packet size

  volatile long rwsize=0;         // remote initial window size
  volatile int rmpsize=0;        // remote maximum packet size

  IO io=null;
  Thread thread=null;

  volatile boolean eof_local=false;
  volatile boolean eof_remote=false;

  volatile boolean close=false;
  volatile boolean connected=false;
  volatile boolean open_confirmation=false;

  volatile int exitstatus=-1;

  volatile int reply=0;
  volatile int connectTimeout=0;

  private Session session;

  int notifyme=0;

  Channel(){
    synchronized(pool){
      id=index++;
      pool.addElement(this);
    }
  }
  synchronized void setRecipient(int foo){
    this.recipient=foo;
    if(notifyme>0)
      notifyAll();
  }
  int getRecipient(){
    return recipient;
  }

  void init() throws JSchException {
  }

  public void connect() throws JSchException{
    connect(0);
  }

  public void connect(int connectTimeout) throws JSchException{
    this.connectTimeout=connectTimeout;
    try{
      sendChannelOpen();
      start();
    }
    catch(Exception e){
      connected=false;
      disconnect();
      if(e instanceof JSchException)
        throw (JSchException)e;
      throw new JSchException(e.toString(), e);
    }
  }

  public void setXForwarding(boolean foo){
  }

  public void start() throws JSchException{}

  public boolean isEOF() {return eof_remote;}

  void getData(Buffer buf){
    setRecipient(buf.getInt());
    setRemoteWindowSize(buf.getUInt());
    setRemotePacketSize(buf.getInt());
  }

  public void setInputStream(InputStream in){
    io.setInputStream(in, false);
  }
  public void setInputStream(InputStream in, boolean dontclose){
    io.setInputStream(in, dontclose);
  }
  public void setOutputStream(OutputStream out){
    io.setOutputStream(out, false);
  }
  public void setOutputStream(OutputStream out, boolean dontclose){
    io.setOutputStream(out, dontclose);
  }
  public void setExtOutputStream(OutputStream out){
    io.setExtOutputStream(out, false);
  }
  public void setExtOutputStream(OutputStream out, boolean dontclose){
    io.setExtOutputStream(out, dontclose);
  }
  public InputStream getInputStream() throws IOException {
    int max_input_buffer_size = 32*1024;
    try {
      max_input_buffer_size =
        Integer.parseInt(getSession().getConfig("max_input_buffer_size"));
    }
    catch(Exception e){}
    PipedInputStream in =
      new MyPipedInputStream(
                             32*1024,  // this value should be customizable.
                             max_input_buffer_size
                             );
    boolean resizable = 32*1024<max_input_buffer_size;
    io.setOutputStream(new PassiveOutputStream(in, resizable), false);
    return in;
  }
  public InputStream getExtInputStream() throws IOException {
    int max_input_buffer_size = 32*1024;
    try {
      max_input_buffer_size =
        Integer.parseInt(getSession().getConfig("max_input_buffer_size"));
    }
    catch(Exception e){}
    PipedInputStream in =
      new MyPipedInputStream(
                             32*1024,  // this value should be customizable.
                             max_input_buffer_size
                             );
    boolean resizable = 32*1024<max_input_buffer_size;
    io.setExtOutputStream(new PassiveOutputStream(in, resizable), false);
    return in;
  }
  public OutputStream getOutputStream() throws IOException {

    final Channel channel=this;
    OutputStream out=new OutputStream(){
        private int dataLen=0;
        private Buffer buffer=null;
        private Packet packet=null;
        private boolean closed=false;
        private synchronized void init() throws java.io.IOException{
          buffer=new Buffer(rmpsize);
          packet=new Packet(buffer);

          byte[] _buf=buffer.buffer;
          if(_buf.length-(14+0)-Session.buffer_margin<=0){
            buffer=null;
            packet=null;
            throw new IOException("failed to initialize the channel.");
          }

        }
        byte[] b=new byte[1];
        public void write(int w) throws java.io.IOException{
          b[0]=(byte)w;
          write(b, 0, 1);
        }
        public void write(byte[] buf, int s, int l) throws java.io.IOException{
          if(packet==null){
            init();
          }

          if(closed){
            throw new java.io.IOException("Already closed");
          }

          byte[] _buf=buffer.buffer;
          int _bufl=_buf.length;
          while(l>0){
            int _l=l;
            if(l>_bufl-(14+dataLen)-Session.buffer_margin){
              _l=_bufl-(14+dataLen)-Session.buffer_margin;
            }

            if(_l<=0){
              flush();
              continue;
            }

            System.arraycopy(buf, s, _buf, 14+dataLen, _l);
            dataLen+=_l;
            s+=_l;
            l-=_l;
          }
        }

        public void flush() throws java.io.IOException{
          if(closed){
            throw new java.io.IOException("Already closed");
          }
          if(dataLen==0)
            return;
          packet.reset();
          buffer.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
          buffer.putInt(recipient);
          buffer.putInt(dataLen);
          buffer.skip(dataLen);
          try{
            int foo=dataLen;
            dataLen=0;
            synchronized(channel){
              if(!channel.close)
                getSession().write(packet, channel, foo);
            }
          }
          catch(Exception e){
            close();
            throw new java.io.IOException(e.toString());
          }

        }
        public void close() throws java.io.IOException{
          if(packet==null){
            try{
              init();
            }
            catch(java.io.IOException e){
              // close should be finished silently.
              return;
            }
          }
          if(closed){
            return;
          }
          if(dataLen>0){
            flush();
          }
          channel.eof();
          closed=true;
        }
      };
    return out;
  }

  class MyPipedInputStream extends PipedInputStream{
    private int BUFFER_SIZE = 1024;
    private int max_buffer_size = BUFFER_SIZE;
    MyPipedInputStream() throws IOException{ super(); }
    MyPipedInputStream(int size) throws IOException{
      super();
      buffer=new byte[size];
      BUFFER_SIZE = size;
      max_buffer_size = size;
    }
    MyPipedInputStream(int size, int max_buffer_size) throws IOException{
      this(size);
      this.max_buffer_size = max_buffer_size;
    }
    MyPipedInputStream(PipedOutputStream out) throws IOException{ super(out); }
    MyPipedInputStream(PipedOutputStream out, int size) throws IOException{
      super(out);
      buffer=new byte[size];
      BUFFER_SIZE=size;
    }

    /*
     * TODO: We should have our own Piped[I/O]Stream implementation.
     * Before accepting data, JDK's PipedInputStream will check the existence of
     * reader thread, and if it is not alive, the stream will be closed.
     * That behavior may cause the problem if multiple threads make access to it.
     */
    public synchronized void updateReadSide() throws IOException {
      if(available() != 0){ // not empty
        return;
      }
      in = 0;
      out = 0;
      buffer[in++] = 0;
      read();
    }

    private int freeSpace(){
      int size = 0;
      if(out < in) {
        size = buffer.length-in;
      }
      else if(in < out){
        if(in == -1) size = buffer.length;
        else size = out - in;
      }
      return size;
    }
    synchronized void checkSpace(int len) throws IOException {
      int size = freeSpace();
      if(size<len){
        int datasize=buffer.length-size;
        int foo = buffer.length;
        while((foo - datasize) < len){
          foo*=2;
        }

        if(foo > max_buffer_size){
          foo = max_buffer_size;
        }
        if((foo - datasize) < len) return;

        byte[] tmp = new byte[foo];
        if(out < in) {
          System.arraycopy(buffer, 0, tmp, 0, buffer.length);
        }
        else if(in < out){
          if(in == -1) {
          }
          else {
            System.arraycopy(buffer, 0, tmp, 0, in);
            System.arraycopy(buffer, out,
                             tmp, tmp.length-(buffer.length-out),
                             (buffer.length-out));
            out = tmp.length-(buffer.length-out);
          }
        }
        else if(in == out){
          System.arraycopy(buffer, 0, tmp, 0, buffer.length);
          in=buffer.length;
        }
        buffer=tmp;
      }
      else if(buffer.length == size && size > BUFFER_SIZE) {
        int  i = size/2;
        if(i<BUFFER_SIZE) i = BUFFER_SIZE;
        byte[] tmp = new byte[i];
        buffer=tmp;
      }
    }
  }
  void setLocalWindowSizeMax(int foo){ this.lwsize_max=foo; }
  void setLocalWindowSize(int foo){ this.lwsize=foo; }
  void setLocalPacketSize(int foo){ this.lmpsize=foo; }
  synchronized void setRemoteWindowSize(long foo){ this.rwsize=foo; }
  synchronized void addRemoteWindowSize(long foo){
    this.rwsize+=foo;
    if(notifyme>0)
      notifyAll();
  }
  void setRemotePacketSize(int foo){ this.rmpsize=foo; }

  public void run(){
  }

  void write(byte[] foo) throws IOException {
    write(foo, 0, foo.length);
  }
  void write(byte[] foo, int s, int l) throws IOException {
    try{
      io.put(foo, s, l);
    }catch(NullPointerException e){}
  }
  void write_ext(byte[] foo, int s, int l) throws IOException {
    try{
      io.put_ext(foo, s, l);
    }catch(NullPointerException e){}
  }

  void eof_remote(){
    eof_remote=true;
    try{
      io.out_close();
    }
    catch(NullPointerException e){}
  }

  void eof(){
    if(eof_local)return;
    eof_local=true;

    int i = getRecipient();
    if(i == -1) return;

    try{
      Buffer buf=new Buffer(100);
      Packet packet=new Packet(buf);
      packet.reset();
      buf.putByte((byte)Session.SSH_MSG_CHANNEL_EOF);
      buf.putInt(i);
      synchronized(this){
        if(!close)
          getSession().write(packet);
      }
    }
    catch(Exception e){
      //System.err.println("Channel.eof");
      //e.printStackTrace();
    }
    /*
    if(!isConnected()){ disconnect(); }
    */
  }

  /*
  http://www1.ietf.org/internet-drafts/draft-ietf-secsh-connect-24.txt

5.3  Closing a Channel
  When a party will no longer send more data to a channel, it SHOULD
   send SSH_MSG_CHANNEL_EOF.

            byte      SSH_MSG_CHANNEL_EOF
            uint32    recipient_channel

  No explicit response is sent to this message.  However, the
   application may send EOF to whatever is at the other end of the
  channel.  Note that the channel remains open after this message, and
   more data may still be sent in the other direction.  This message
   does not consume window space and can be sent even if no window space
   is available.

     When either party wishes to terminate the channel, it sends
     SSH_MSG_CHANNEL_CLOSE.  Upon receiving this message, a party MUST
   send back a SSH_MSG_CHANNEL_CLOSE unless it has already sent this
   message for the channel.  The channel is considered closed for a
     party when it has both sent and received SSH_MSG_CHANNEL_CLOSE, and
   the party may then reuse the channel number.  A party MAY send
   SSH_MSG_CHANNEL_CLOSE without having sent or received
   SSH_MSG_CHANNEL_EOF.

            byte      SSH_MSG_CHANNEL_CLOSE
            uint32    recipient_channel

   This message does not consume window space and can be sent even if no
   window space is available.

   It is recommended that any data sent before this message is delivered
     to the actual destination, if possible.
  */

  void close(){
    if(close)return;
    close=true;
    eof_local=eof_remote=true;

    int i = getRecipient();
    if(i == -1) return;

    try{
      Buffer buf=new Buffer(100);
      Packet packet=new Packet(buf);
      packet.reset();
      buf.putByte((byte)Session.SSH_MSG_CHANNEL_CLOSE);
      buf.putInt(i);
      synchronized(this){
        getSession().write(packet);
      }
    }
    catch(Exception e){
      //e.printStackTrace();
    }
  }
  public boolean isClosed(){
    return close;
  }
  static void disconnect(Session session){
    Channel[] channels=null;
    int count=0;
    synchronized(pool){
      channels=new Channel[pool.size()];
      for(int i=0; i<pool.size(); i++){
        try{
          Channel c=((Channel)(pool.elementAt(i)));
          if(c.session==session){
            channels[count++]=c;
          }
        }
        catch(Exception e){
        }
      }
    }
    for(int i=0; i<count; i++){
      channels[i].disconnect();
    }
  }

  public void disconnect(){
    //System.err.println(this+":disconnect "+io+" "+connected);
    //Thread.dumpStack();

    try{

      synchronized(this){
        if(!connected){
          return;
        }
        connected=false;
      }

      close();

      eof_remote=eof_local=true;

      thread=null;

      try{
        if(io!=null){
          io.close();
        }
      }
      catch(Exception e){
        //e.printStackTrace();
      }
      // io=null;
    }
    finally{
      Channel.del(this);
    }
  }

  public boolean isConnected(){
    Session _session=this.session;
    if(_session!=null){
      return _session.isConnected() && connected;
    }
    return false;
  }

  public void sendSignal(String signal) throws Exception {
    RequestSignal request=new RequestSignal();
    request.setSignal(signal);
    request.request(getSession(), this);
  }

//  public String toString(){
//      return "Channel: type="+new String(type)+",id="+id+",recipient="+recipient+",window_size="+window_size+",packet_size="+packet_size;
//  }

/*
  class OutputThread extends Thread{
    Channel c;
    OutputThread(Channel c){ this.c=c;}
    public void run(){c.output_thread();}
  }
*/

  class PassiveInputStream extends MyPipedInputStream{
    PipedOutputStream out;
    PassiveInputStream(PipedOutputStream out, int size) throws IOException{
      super(out, size);
      this.out=out;
    }
    PassiveInputStream(PipedOutputStream out) throws IOException{
      super(out);
      this.out=out;
    }
    public void close() throws IOException{
      if(out!=null){
        this.out.close();
      }
      out=null;
    }
  }
  class PassiveOutputStream extends PipedOutputStream{
    private MyPipedInputStream _sink=null;
    PassiveOutputStream(PipedInputStream in,
                        boolean resizable_buffer) throws IOException{
      super(in);
      if(resizable_buffer && (in instanceof MyPipedInputStream)) {
        this._sink=(MyPipedInputStream)in;
      }
    }
    public void write(int b) throws IOException {
      if(_sink != null) {
        _sink.checkSpace(1);
      }
      super.write(b);
    }
    public void write(byte[] b, int off, int len) throws IOException {
      if(_sink != null) {
        _sink.checkSpace(len);
      }
      super.write(b, off, len);
    }
  }

  void setExitStatus(int status){ exitstatus=status; }
  public int getExitStatus(){ return exitstatus; }

  void setSession(Session session){
    this.session=session;
  }

  public Session getSession() throws JSchException{
    Session _session=session;
    if(_session==null){
      throw new JSchException("session is not available");
    }
    return _session;
  }
  public int getId(){ return id; }

  protected void sendOpenConfirmation() throws Exception{
    Buffer buf=new Buffer(100);
    Packet packet=new Packet(buf);
    packet.reset();
    buf.putByte((byte)SSH_MSG_CHANNEL_OPEN_CONFIRMATION);
    buf.putInt(getRecipient());
    buf.putInt(id);
    buf.putInt(lwsize);
    buf.putInt(lmpsize);
    getSession().write(packet);
  }

  protected void sendOpenFailure(int reasoncode){
    try{
      Buffer buf=new Buffer(100);
      Packet packet=new Packet(buf);
      packet.reset();
      buf.putByte((byte)SSH_MSG_CHANNEL_OPEN_FAILURE);
      buf.putInt(getRecipient());
      buf.putInt(reasoncode);
      buf.putString(Util.str2byte("open failed"));
      buf.putString(Util.empty);
      getSession().write(packet);
    }
    catch(Exception e){
    }
  }

  protected Packet genChannelOpenPacket(){
    Buffer buf=new Buffer(100);
    Packet packet=new Packet(buf);
    // byte   SSH_MSG_CHANNEL_OPEN(90)
    // string channel type         //
    // uint32 sender channel       // 0
    // uint32 initial window size  // 0x100000(65536)
    // uint32 maxmum packet size   // 0x4000(16384)
    packet.reset();
    buf.putByte((byte)90);
    buf.putString(this.type);
    buf.putInt(this.id);
    buf.putInt(this.lwsize);
    buf.putInt(this.lmpsize);
    return packet;
  }

  protected void sendChannelOpen() throws Exception {
    Session _session=getSession();
    if(!_session.isConnected()){
      throw new JSchException("session is down");
    }

    Packet packet = genChannelOpenPacket();
    _session.write(packet);

    int retry=2000;
    long start=System.currentTimeMillis();
    long timeout=connectTimeout;
    if(timeout!=0L) retry = 1;
    synchronized(this){
      while(this.getRecipient()==-1 &&
            _session.isConnected() &&
             retry>0){
        if(timeout>0L){
          if((System.currentTimeMillis()-start)>timeout){
            retry=0;
            continue;
          }
        }
        try{
          long t = timeout==0L ? 10L : timeout;
          this.notifyme=1;
          wait(t);
        }
        catch(java.lang.InterruptedException e){
        }
        finally{
          this.notifyme=0;
        }
        retry--;
      }
    }
    if(!_session.isConnected()){
      throw new JSchException("session is down");
    }
    if(this.getRecipient()==-1){  // timeout
      throw new JSchException("channel is not opened.");
    }
    if(this.open_confirmation==false){  // SSH_MSG_CHANNEL_OPEN_FAILURE
      throw new JSchException("channel is not opened.");
    }
    connected=true;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelSession extends Channel{
  private static byte[] _session=Util.str2byte("session");

  protected boolean agent_forwarding=false;
  protected boolean xforwading=false;
  protected Hashtable env=null;

  protected boolean pty=false;

  protected String ttype="vt100";
  protected int tcol=80;
  protected int trow=24;
  protected int twp=640;
  protected int thp=480;
  protected byte[] terminal_mode=null;

  ChannelSession(){
    super();
    type=_session;
    io=new IO();
  }

  /**
   * Enable the agent forwarding.
   *
   * @param enable
   */
  public void setAgentForwarding(boolean enable){
    agent_forwarding=enable;
  }

  /**
   * Enable the X11 forwarding.
   * Refer to RFC4254 6.3.1. Requesting X11 Forwarding.
   *
   * @param enable
   */
  public void setXForwarding(boolean enable){
    xforwading=enable;
  }

  /**
   * @deprecated Use {@link #setEnv(String, String)} or {@link #setEnv(byte[], byte[])} instead.
   * @see #setEnv(String, String)
   * @see #setEnv(byte[], byte[])
   */
  public void setEnv(Hashtable env){
    synchronized(this){
      this.env=env;
    }
  }

  /**
   * Set the environment variable.
   * If <code>name</code> and <code>value</code> are needed to be passed
   * to the remote in your favorite encoding,
   * use {@link #setEnv(byte[], byte[])}.
   * Refer to RFC4254 6.4 Environment Variable Passing.
   *
   * @param name A name for environment variable.
   * @param value A value for environment variable.
   */
  public void setEnv(String name, String value){
    setEnv(Util.str2byte(name), Util.str2byte(value));
  }

  /**
   * Set the environment variable.
   * Refer to RFC4254 6.4 Environment Variable Passing.
   *
   * @param name A name of environment variable.
   * @param value A value of environment variable.
   * @see #setEnv(String, String)
   */
  public void setEnv(byte[] name, byte[] value){
    synchronized(this){
      getEnv().put(name, value);
    }
  }

  private Hashtable getEnv(){
    if(env==null)
      env=new Hashtable();
    return env;
  }

  /**
   * Allocate a Pseudo-Terminal.
   * Refer to RFC4254 6.2. Requesting a Pseudo-Terminal.
   *
   * @param enable
   */
  public void setPty(boolean enable){
    pty=enable;
  }

  /**
   * Set the terminal mode.
   *
   * @param terminal_mode
   */
  public void setTerminalMode(byte[] terminal_mode){
    this.terminal_mode=terminal_mode;
  }

  /**
   * Change the window dimension interactively.
   * Refer to RFC4254 6.7. Window Dimension Change Message.
   *
   * @param col terminal width, columns
   * @param row terminal height, rows
   * @param wp terminal width, pixels
   * @param hp terminal height, pixels
   */
  public void setPtySize(int col, int row, int wp, int hp){
    setPtyType(this.ttype, col, row, wp, hp);
    if(!pty || !isConnected()){
      return;
    }
    try{
      RequestWindowChange request=new RequestWindowChange();
      request.setSize(col, row, wp, hp);
      request.request(getSession(), this);
    }
    catch(Exception e){
      //System.err.println("ChannelSessio.setPtySize: "+e);
    }
  }

  /**
   * Set the terminal type.
   * This method is not effective after Channel#connect().
   *
   * @param ttype terminal type(for example, "vt100")
   * @see #setPtyType(String, int, int, int, int)
   */
  public void setPtyType(String ttype){
    setPtyType(ttype, 80, 24, 640, 480);
  }

  /**
   * Set the terminal type.
   * This method is not effective after Channel#connect().
   *
   * @param ttype terminal type(for example, "vt100")
   * @param col terminal width, columns
   * @param row terminal height, rows
   * @param wp terminal width, pixels
   * @param hp terminal height, pixels
   */
  public void setPtyType(String ttype, int col, int row, int wp, int hp){
    this.ttype=ttype;
    this.tcol=col;
    this.trow=row;
    this.twp=wp;
    this.thp=hp;
  }

  protected void sendRequests() throws Exception{
    Session _session=getSession();
    Request request;
    if(agent_forwarding){
      request=new RequestAgentForwarding();
      request.request(_session, this);
    }

    if(xforwading){
      request=new RequestX11();
      request.request(_session, this);
    }

    if(pty){
      request=new RequestPtyReq();
      ((RequestPtyReq)request).setTType(ttype);
      ((RequestPtyReq)request).setTSize(tcol, trow, twp, thp);
      if(terminal_mode!=null){
        ((RequestPtyReq)request).setTerminalMode(terminal_mode);
      }
      request.request(_session, this);
    }

    if(env!=null){
      for(Enumeration _env=env.keys(); _env.hasMoreElements();){
        Object name=_env.nextElement();
        Object value=env.get(name);
        request=new RequestEnv();
        ((RequestEnv)request).setEnv(toByteArray(name),
                                     toByteArray(value));
        request.request(_session, this);
      }
    }
  }

  private byte[] toByteArray(Object o){
    if(o instanceof String){
      return Util.str2byte((String)o);
    }
    return (byte[])o;
  }

  public void run(){
    //System.err.println(this+":run >");

    Buffer buf=new Buffer(rmpsize);
    Packet packet=new Packet(buf);
    int i=-1;
    try{
      while(isConnected() &&
            thread!=null &&
            io!=null &&
            io.in!=null){
        i=io.in.read(buf.buffer,
                     14,
                     buf.buffer.length-14
                     -Session.buffer_margin
                     );
        if(i==0)continue;
        if(i==-1){
          eof();
          break;
        }
        if(close)break;
        //System.out.println("write: "+i);
        packet.reset();
        buf.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
        buf.putInt(recipient);
        buf.putInt(i);
        buf.skip(i);
        getSession().write(packet, this, i);
      }
    }
    catch(Exception e){
      //System.err.println("# ChannelExec.run");
      //e.printStackTrace();
    }
    Thread _thread=thread;
    if(_thread!=null){
      synchronized(_thread){ _thread.notifyAll(); }
    }
    thread=null;
    //System.err.println(this+":run <");
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/





class ChannelSftp extends ChannelSession{

  static private final int LOCAL_MAXIMUM_PACKET_SIZE=32*1024;
  static private final int LOCAL_WINDOW_SIZE_MAX=(64*LOCAL_MAXIMUM_PACKET_SIZE);

  private static final byte SSH_FXP_INIT=               1;
  private static final byte SSH_FXP_VERSION=            2;
  private static final byte SSH_FXP_OPEN=               3;
  private static final byte SSH_FXP_CLOSE=              4;
  private static final byte SSH_FXP_READ=               5;
  private static final byte SSH_FXP_WRITE=              6;
  private static final byte SSH_FXP_LSTAT=              7;
  private static final byte SSH_FXP_FSTAT=              8;
  private static final byte SSH_FXP_SETSTAT=            9;
  private static final byte SSH_FXP_FSETSTAT=          10;
  private static final byte SSH_FXP_OPENDIR=           11;
  private static final byte SSH_FXP_READDIR=           12;
  private static final byte SSH_FXP_REMOVE=            13;
  private static final byte SSH_FXP_MKDIR=             14;
  private static final byte SSH_FXP_RMDIR=             15;
  private static final byte SSH_FXP_REALPATH=          16;
  private static final byte SSH_FXP_STAT=              17;
  private static final byte SSH_FXP_RENAME=            18;
  private static final byte SSH_FXP_READLINK=          19;
  private static final byte SSH_FXP_SYMLINK=           20;
  private static final byte SSH_FXP_STATUS=           101;
  private static final byte SSH_FXP_HANDLE=           102;
  private static final byte SSH_FXP_DATA=             103;
  private static final byte SSH_FXP_NAME=             104;
  private static final byte SSH_FXP_ATTRS=            105;
  private static final byte SSH_FXP_EXTENDED=         (byte)200;
  private static final byte SSH_FXP_EXTENDED_REPLY=   (byte)201;

  // pflags
  private static final int SSH_FXF_READ=           0x00000001;
  private static final int SSH_FXF_WRITE=          0x00000002;
  private static final int SSH_FXF_APPEND=         0x00000004;
  private static final int SSH_FXF_CREAT=          0x00000008;
  private static final int SSH_FXF_TRUNC=          0x00000010;
  private static final int SSH_FXF_EXCL=           0x00000020;

  private static final int SSH_FILEXFER_ATTR_SIZE=         0x00000001;
  private static final int SSH_FILEXFER_ATTR_UIDGID=       0x00000002;
  private static final int SSH_FILEXFER_ATTR_PERMISSIONS=  0x00000004;
  private static final int SSH_FILEXFER_ATTR_ACMODTIME=    0x00000008;
  private static final int SSH_FILEXFER_ATTR_EXTENDED=     0x80000000;

  public static final int SSH_FX_OK=                            0;
  public static final int SSH_FX_EOF=                           1;
  public static final int SSH_FX_NO_SUCH_FILE=                  2;
  public static final int SSH_FX_PERMISSION_DENIED=             3;
  public static final int SSH_FX_FAILURE=                       4;
  public static final int SSH_FX_BAD_MESSAGE=                   5;
  public static final int SSH_FX_NO_CONNECTION=                 6;
  public static final int SSH_FX_CONNECTION_LOST=               7;
  public static final int SSH_FX_OP_UNSUPPORTED=                8;
/*
   SSH_FX_OK
      Indicates successful completion of the operation.
   SSH_FX_EOF
     indicates end-of-file condition; for SSH_FX_READ it means that no
       more data is available in the file, and for SSH_FX_READDIR it
      indicates that no more files are contained in the directory.
   SSH_FX_NO_SUCH_FILE
      is returned when a reference is made to a file which should exist
      but doesn't.
   SSH_FX_PERMISSION_DENIED
      is returned when the authenticated user does not have sufficient
      permissions to perform the operation.
   SSH_FX_FAILURE
      is a generic catch-all error message; it should be returned if an
      error occurs for which there is no more specific error code
      defined.
   SSH_FX_BAD_MESSAGE
      may be returned if a badly formatted packet or protocol
      incompatibility is detected.
   SSH_FX_NO_CONNECTION
      is a pseudo-error which indicates that the client has no
      connection to the server (it can only be generated locally by the
      client, and MUST NOT be returned by servers).
   SSH_FX_CONNECTION_LOST
      is a pseudo-error which indicates that the connection to the
      server has been lost (it can only be generated locally by the
      client, and MUST NOT be returned by servers).
   SSH_FX_OP_UNSUPPORTED
      indicates that an attempt was made to perform an operation which
      is not supported for the server (it may be generated locally by
      the client if e.g.  the version number exchange indicates that a
      required feature is not supported by the server, or it may be
      returned by the server if the server does not implement an
      operation).
*/
  private static final int MAX_MSG_LENGTH = 256* 1024;

  public static final int OVERWRITE=0;
  public static final int RESUME=1;
  public static final int APPEND=2;

  private boolean interactive=false;
  private int seq=1;
  private int[] ackid=new int[1];

  private Buffer buf;
  private Packet packet;

  // The followings will be used in file uploading.
  private Buffer obuf;
  private Packet opacket;

  private int client_version=3;
  private int server_version=3;
  private String version=String.valueOf(client_version);

  private java.util.Hashtable extensions=null;
  private InputStream io_in=null;

  private boolean extension_posix_rename = false;
  private boolean extension_statvfs = false;
  // private boolean extension_fstatvfs = false;
  private boolean extension_hardlink = false;

/*
10. Changes from previous protocol versions
  The SSH File Transfer Protocol has changed over time, before it's
   standardization.  The following is a description of the incompatible
   changes between different versions.
10.1 Changes between versions 3 and 2
   o  The SSH_FXP_READLINK and SSH_FXP_SYMLINK messages were added.
   o  The SSH_FXP_EXTENDED and SSH_FXP_EXTENDED_REPLY messages were added.
   o  The SSH_FXP_STATUS message was changed to include fields `error
      message' and `language tag'.
10.2 Changes between versions 2 and 1
   o  The SSH_FXP_RENAME message was added.
10.3 Changes between versions 1 and 0
   o  Implementation changes, no actual protocol changes.
*/

  private static final String file_separator=java.io.File.separator;
  private static final char file_separatorc=java.io.File.separatorChar;
  private static boolean fs_is_bs=(byte)java.io.File.separatorChar == '\\';

  private String cwd;
  private String home;
  private String lcwd;

  private static final String UTF8="UTF-8";
  private String fEncoding=UTF8;
  private boolean fEncoding_is_utf8=true;

  private RequestQueue rq = new RequestQueue(16);

  /**
   * Specify how many requests may be sent at any one time.
   * Increasing this value may slightly improve file transfer speed but will
   * increase memory usage.  The default is 16 requests.
   *
   * @param bulk_requests how many requests may be outstanding at any one time.
   */
  public void setBulkRequests(int bulk_requests) throws JSchException {
    if(bulk_requests>0)
      rq = new RequestQueue(bulk_requests);
    else
      throw new JSchException("setBulkRequests: "+
                              bulk_requests+" must be greater than 0.");
  }

  /**
   * This method will return the value how many requests may be
   * sent at any one time.
   *
   * @return how many requests may be sent at any one time.
   */
  public int getBulkRequests(){
    return rq.size();
  }

  public ChannelSftp(){
    super();
    setLocalWindowSizeMax(LOCAL_WINDOW_SIZE_MAX);
    setLocalWindowSize(LOCAL_WINDOW_SIZE_MAX);
    setLocalPacketSize(LOCAL_MAXIMUM_PACKET_SIZE);
  }

  void init(){
  }

  public void start() throws JSchException{
    try{

      PipedOutputStream pos=new PipedOutputStream();
      io.setOutputStream(pos);
      PipedInputStream pis=new MyPipedInputStream(pos, rmpsize);
      io.setInputStream(pis);

      io_in=io.in;

      if(io_in==null){
        throw new JSchException("channel is down");
      }

      Request request=new RequestSftp();
      request.request(getSession(), this);

      /*
      System.err.println("lmpsize: "+lmpsize);
      System.err.println("lwsize: "+lwsize);
      System.err.println("rmpsize: "+rmpsize);
      System.err.println("rwsize: "+rwsize);
      */

      buf=new Buffer(lmpsize);
      packet=new Packet(buf);

      obuf=new Buffer(rmpsize);
      opacket=new Packet(obuf);

      int i=0;
      int length;
      int type;
      byte[] str;

      // send SSH_FXP_INIT
      sendINIT();

      // receive SSH_FXP_VERSION
      Header header=new Header();
      header=header(buf, header);
      length=header.length;
      if(length > MAX_MSG_LENGTH){
        throw new SftpException(SSH_FX_FAILURE,
                                "Received message is too long: " + length);
      }
      type=header.type;             // 2 -> SSH_FXP_VERSION
      server_version=header.rid;
      //System.err.println("SFTP protocol server-version="+server_version);
      extensions=new java.util.Hashtable();
      if(length>0){
        // extension data
        fill(buf, length);
        byte[] extension_name=null;
        byte[] extension_data=null;
        while(length>0){
          extension_name=buf.getString();
          length-=(4+extension_name.length);
          extension_data=buf.getString();
          length-=(4+extension_data.length);
          extensions.put(Util.byte2str(extension_name),
                         Util.byte2str(extension_data));
        }
      }

      if(extensions.get("posix-rename@openssh.com")!=null &&
         extensions.get("posix-rename@openssh.com").equals("1")){
        extension_posix_rename = true;
      }

      if(extensions.get("statvfs@openssh.com")!=null &&
         extensions.get("statvfs@openssh.com").equals("2")){
        extension_statvfs = true;
      }

      /*
      if(extensions.get("fstatvfs@openssh.com")!=null &&
         extensions.get("fstatvfs@openssh.com").equals("2")){
        extension_fstatvfs = true;
      }
      */

      if(extensions.get("hardlink@openssh.com")!=null &&
         extensions.get("hardlink@openssh.com").equals("1")){
        extension_hardlink = true;
      }

      lcwd=new File(".").getCanonicalPath();
    }
    catch(Exception e){
      //System.err.println(e);
      if(e instanceof JSchException) throw (JSchException)e;
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }
  }

  public void quit(){ disconnect();}
  public void exit(){ disconnect();}
  public void lcd(String path) throws SftpException{
    path=localAbsolutePath(path);
    if((new File(path)).isDirectory()){
      try{
        path=(new File(path)).getCanonicalPath();
      }
      catch(Exception e){}
      lcwd=path;
      return;
    }
    throw new SftpException(SSH_FX_NO_SUCH_FILE, "No such directory");
  }

  public void cd(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);
      path=isUnique(path);

      byte[] str=_realpath(path);
      SftpATTRS attr=_stat(str);

      if((attr.getFlags()&SftpATTRS.SSH_FILEXFER_ATTR_PERMISSIONS)==0){
        throw new SftpException(SSH_FX_FAILURE,
                                "Can't change directory: "+path);
      }
      if(!attr.isDir()){
        throw new SftpException(SSH_FX_FAILURE,
                                "Can't change directory: "+path);
      }

      setCwd(Util.byte2str(str, fEncoding));
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void put(String src, String dst) throws SftpException{
    put(src, dst, null, OVERWRITE);
  }
  public void put(String src, String dst, int mode) throws SftpException{
    put(src, dst, null, mode);
  }
  public void put(String src, String dst,
                  SftpProgressMonitor monitor) throws SftpException{
    put(src, dst, monitor, OVERWRITE);
  }

  /**
   * Sends data from <code>src</code> file to <code>dst</code> file.
   * The <code>mode</code> should be <code>OVERWRITE</code>,
   * <code>RESUME</code> or <code>APPEND</code>.
   *
   * @param src source file
   * @param dst destination file
   * @param monitor progress monitor
   * @param mode how data should be added to dst
   */
  public void put(String src, String dst,
                  SftpProgressMonitor monitor, int mode) throws SftpException{

    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      src=localAbsolutePath(src);
      dst=remoteAbsolutePath(dst);

      Vector v=glob_remote(dst);
      int vsize=v.size();
      if(vsize!=1){
        if(vsize==0){
          if(isPattern(dst))
            throw new SftpException(SSH_FX_FAILURE, dst);
          else
            dst=Util.unquote(dst);
        }
        throw new SftpException(SSH_FX_FAILURE, v.toString());
      }
      else{
        dst=(String)(v.elementAt(0));
      }

      boolean isRemoteDir=isRemoteDir(dst);

      v=glob_local(src);
      vsize=v.size();

      StringBuffer dstsb=null;
      if(isRemoteDir){
        if(!dst.endsWith("/")){
            dst+="/";
        }
        dstsb=new StringBuffer(dst);
      }
      else if(vsize>1){
        throw new SftpException(SSH_FX_FAILURE,
                                "Copying multiple files, but the destination is missing or a file.");
      }

      for(int j=0; j<vsize; j++){
        String _src=(String)(v.elementAt(j));
        String _dst=null;
        if(isRemoteDir){
          int i=_src.lastIndexOf(file_separatorc);
          if(fs_is_bs){
            int ii=_src.lastIndexOf('/');
            if(ii!=-1 && ii>i)
              i=ii;
          }
          if(i==-1) dstsb.append(_src);
          else dstsb.append(_src.substring(i + 1));
          _dst=dstsb.toString();
          dstsb.delete(dst.length(), _dst.length());
        }
        else{
          _dst=dst;
        }
        //System.err.println("_dst "+_dst);

        long size_of_dst=0;
        if(mode==RESUME){
          try{
            SftpATTRS attr=_stat(_dst);
            size_of_dst=attr.getSize();
          }
          catch(Exception eee){
            //System.err.println(eee);
          }
          long size_of_src=new File(_src).length();
          if(size_of_src<size_of_dst){
            throw new SftpException(SSH_FX_FAILURE,
                                    "failed to resume for "+_dst);
          }
          if(size_of_src==size_of_dst){
            return;
          }
        }

        if(monitor!=null){
          monitor.init(SftpProgressMonitor.PUT, _src, _dst,
                       (new File(_src)).length());
          if(mode==RESUME){
            monitor.count(size_of_dst);
          }
        }
        FileInputStream fis=null;
        try{
          fis=new FileInputStream(_src);
          _put(fis, _dst, monitor, mode);
        }
        finally{
          if(fis!=null) {
            fis.close();
          }
        }
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, e.toString(), (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, e.toString());
    }
  }
  public void put(InputStream src, String dst) throws SftpException{
    put(src, dst, null, OVERWRITE);
  }
  public void put(InputStream src, String dst, int mode) throws SftpException{
    put(src, dst, null, mode);
  }
  public void put(InputStream src, String dst,
                  SftpProgressMonitor monitor) throws SftpException{
    put(src, dst, monitor, OVERWRITE);
  }

  /**
   * Sends data from the input stream <code>src</code> to <code>dst</code> file.
   * The <code>mode</code> should be <code>OVERWRITE</code>,
   * <code>RESUME</code> or <code>APPEND</code>.
   *
   * @param src input stream
   * @param dst destination file
   * @param monitor progress monitor
   * @param mode how data should be added to dst
   */
  public void put(InputStream src, String dst,
                  SftpProgressMonitor monitor, int mode) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      dst=remoteAbsolutePath(dst);

      Vector v=glob_remote(dst);
      int vsize=v.size();
      if(vsize!=1){
        if(vsize==0){
          if(isPattern(dst))
            throw new SftpException(SSH_FX_FAILURE, dst);
          else
            dst=Util.unquote(dst);
        }
        throw new SftpException(SSH_FX_FAILURE, v.toString());
      }
      else{
        dst=(String)(v.elementAt(0));
      }

      if(monitor!=null){
        monitor.init(SftpProgressMonitor.PUT,
                     "-", dst,
                     SftpProgressMonitor.UNKNOWN_SIZE);
      }

      _put(src, dst, monitor, mode);
    }
    catch(Exception e){
      if(e instanceof SftpException) {
        if(((SftpException)e).id == SSH_FX_FAILURE &&
           isRemoteDir(dst)) {
          throw new SftpException(SSH_FX_FAILURE, dst+" is a directory");
        }
        throw (SftpException)e;
      }
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, e.toString(), (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, e.toString());
    }
  }

  public void _put(InputStream src, String dst,
                   SftpProgressMonitor monitor, int mode) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      byte[] dstb=Util.str2byte(dst, fEncoding);
      long skip=0;
      if(mode==RESUME || mode==APPEND){
        try{
          SftpATTRS attr=_stat(dstb);
          skip=attr.getSize();
        }
        catch(Exception eee){
          //System.err.println(eee);
        }
      }
      if(mode==RESUME && skip>0){
        long skipped=src.skip(skip);
        if(skipped<skip){
          throw new SftpException(SSH_FX_FAILURE, "failed to resume for "+dst);
        }
      }

      if(mode==OVERWRITE){ sendOPENW(dstb); }
      else{ sendOPENA(dstb); }

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_STATUS && type!=SSH_FXP_HANDLE){
        throw new SftpException(SSH_FX_FAILURE, "invalid type="+type);
      }
      if(type==SSH_FXP_STATUS){
        int i=buf.getInt();
        throwStatusError(buf, i);
      }
      byte[] handle=buf.getString();         // handle
      byte[] data=null;

      boolean dontcopy=true;

      if(!dontcopy){  // This case will not work anymore.
        data=new byte[obuf.buffer.length
                      -(5+13+21+handle.length+Session.buffer_margin
                        )
        ];
      }

      long offset=0;
      if(mode==RESUME || mode==APPEND){
        offset+=skip;
      }

      int startid=seq;
      int ackcount=0;
      int _s=0;
      int _datalen=0;

      if(!dontcopy){  // This case will not work anymore.
        _datalen=data.length;
      }
      else{
        data=obuf.buffer;
        _s=5+13+21+handle.length;
        _datalen=obuf.buffer.length-_s-Session.buffer_margin;
      }

      int bulk_requests = rq.size();

      while(true){
        int nread=0;
        int count=0;
        int s=_s;
        int datalen=_datalen;

        do{
          nread=src.read(data, s, datalen);
          if(nread>0){
            s+=nread;
            datalen-=nread;
            count+=nread;
          }
        }
        while(datalen>0 && nread>0);
        if(count<=0)break;

        int foo=count;
        while(foo>0){
          if((seq-1)==startid ||
             ((seq-startid)-ackcount)>=bulk_requests){
            while(((seq-startid)-ackcount)>=bulk_requests){
              if(checkStatus(ackid, header)){
                int _ackid = ackid[0];
                if(startid>_ackid || _ackid>seq-1){
                  if(_ackid==seq){
                    System.err.println("ack error: startid="+startid+" seq="+seq+" _ackid="+_ackid);
                  }
                  else{
                    throw new SftpException(SSH_FX_FAILURE, "ack error: startid="+startid+" seq="+seq+" _ackid="+_ackid);
                  }
                }
                ackcount++;
              }
              else{
                break;
              }
            }
          }
          if(dontcopy){
            foo-=sendWRITE(handle, offset, data, 0, foo);
            if(data!=obuf.buffer){
              data=obuf.buffer;
              _datalen=obuf.buffer.length-_s-Session.buffer_margin;
            }
          }
          else {
            foo-=sendWRITE(handle, offset, data, _s, foo);
          }
        }
        offset+=count;
        if(monitor!=null && !monitor.count(count)){
          break;
        }
      }
      int _ackcount=seq-startid;
      while(_ackcount>ackcount){
        if(!checkStatus(null, header)){
          break;
        }
        ackcount++;
      }
      if(monitor!=null)monitor.end();
      _sendCLOSE(handle, header);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, e.toString(), (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, e.toString());
    }
  }

  public OutputStream put(String dst) throws SftpException{
    return put(dst, (SftpProgressMonitor)null, OVERWRITE);
  }
  public OutputStream put(String dst, final int mode) throws SftpException{
    return put(dst, (SftpProgressMonitor)null, mode);
  }
  public OutputStream put(String dst, final SftpProgressMonitor monitor, final int mode) throws SftpException{
    return put(dst, monitor, mode, 0);
  }

  /**
   * Sends data from the output stream to <code>dst</code> file.
   * The <code>mode</code> should be <code>OVERWRITE</code>,
   * <code>RESUME</code> or <code>APPEND</code>.
   *
   * @param dst destination file
   * @param monitor progress monitor
   * @param mode how data should be added to dst
   * @param offset data will be added at offset
   * @return output stream, which accepts data to be transferred.
   */
  public OutputStream put(String dst, final SftpProgressMonitor monitor, final int mode, long offset) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      dst=remoteAbsolutePath(dst);
      dst=isUnique(dst);

      if(isRemoteDir(dst)){
        throw new SftpException(SSH_FX_FAILURE, dst+" is a directory");
      }

      byte[] dstb=Util.str2byte(dst, fEncoding);

      long skip=0;
      if(mode==RESUME || mode==APPEND){
        try{
          SftpATTRS attr=_stat(dstb);
          skip=attr.getSize();
        }
        catch(Exception eee){
          //System.err.println(eee);
        }
      }

      if(monitor!=null){
        monitor.init(SftpProgressMonitor.PUT,
                     "-", dst,
                     SftpProgressMonitor.UNKNOWN_SIZE);
      }

      if(mode==OVERWRITE){ sendOPENW(dstb); }
      else{ sendOPENA(dstb); }

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_STATUS && type!=SSH_FXP_HANDLE){
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      if(type==SSH_FXP_STATUS){
        int i=buf.getInt();
        throwStatusError(buf, i);
      }
      final byte[] handle=buf.getString();         // handle

      if(mode==RESUME || mode==APPEND){
        offset+=skip;
      }

      final long[] _offset=new long[1];
      _offset[0]=offset;
      OutputStream out = new OutputStream(){
        private boolean init=true;
        private boolean isClosed=false;
        private int[] ackid=new int[1];
        private int startid=0;
        private int _ackid=0;
        private int ackcount=0;
        private int writecount=0;
        private Header header=new Header();

        public void write(byte[] d) throws java.io.IOException{
          write(d, 0, d.length);
        }

        public void write(byte[] d, int s, int len) throws java.io.IOException{
          if(init){
            startid=seq;
            _ackid=seq;
            init=false;
          }

          if(isClosed){
            throw new IOException("stream already closed");
          }

          try{
            int _len=len;
            while(_len>0){
              int sent=sendWRITE(handle, _offset[0], d, s, _len);
              writecount++;
              _offset[0]+=sent;
              s+=sent;
              _len-=sent;
              if((seq-1)==startid ||
                 io_in.available()>=1024){
                while(io_in.available()>0){
                  if(checkStatus(ackid, header)){
                    _ackid=ackid[0];
                    if(startid>_ackid || _ackid>seq-1){
                      throw new SftpException(SSH_FX_FAILURE, "");
                    }
                    ackcount++;
                  }
                  else{
                    break;
                  }
                }
              }
            }
            if(monitor!=null && !monitor.count(len)){
              close();
              throw new IOException("canceled");
            }
          }
          catch(IOException e){ throw e; }
          catch(Exception e){ throw new IOException(e.toString());  }
        }

        byte[] _data=new byte[1];
        public void write(int foo) throws java.io.IOException{
          _data[0]=(byte)foo;
          write(_data, 0, 1);
        }

        public void flush() throws java.io.IOException{

          if(isClosed){
            throw new IOException("stream already closed");
          }

          if(!init){
            try{
              while(writecount>ackcount){
                if(!checkStatus(null, header)){
                  break;
                }
                ackcount++;
              }
            }
            catch(SftpException e){
              throw new IOException(e.toString());
            }
          }
        }

        public void close() throws java.io.IOException{
          if(isClosed){
            return;
          }
          flush();
          if(monitor!=null)monitor.end();
          try{ _sendCLOSE(handle, header); }
          catch(IOException e){ throw e; }
          catch(Exception e){
            throw new IOException(e.toString());
          }
          isClosed=true;
        }
      };
      return out;
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void get(String src, String dst) throws SftpException{
    get(src, dst, null, OVERWRITE);
  }
  public void get(String src, String dst,
                  SftpProgressMonitor monitor) throws SftpException{
    get(src, dst, monitor, OVERWRITE);
  }
  public void get(String src, String dst,
                  SftpProgressMonitor monitor, int mode) throws SftpException{
    // System.out.println("get: "+src+" "+dst);

    boolean _dstExist = false;
    String _dst=null;
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      src=remoteAbsolutePath(src);
      dst=localAbsolutePath(dst);

      Vector v=glob_remote(src);
      int vsize=v.size();
      if(vsize==0){
        throw new SftpException(SSH_FX_NO_SUCH_FILE, "No such file");
      }

      File dstFile=new File(dst);
      boolean isDstDir=dstFile.isDirectory();
      StringBuffer dstsb=null;
      if(isDstDir){
        if(!dst.endsWith(file_separator)){
          dst+=file_separator;
        }
        dstsb=new StringBuffer(dst);
      }
      else if(vsize>1){
        throw new SftpException(SSH_FX_FAILURE,
                                "Copying multiple files, but destination is missing or a file.");
      }

      for(int j=0; j<vsize; j++){
        String _src=(String)(v.elementAt(j));
        SftpATTRS attr=_stat(_src);
        if(attr.isDir()){
          throw new SftpException(SSH_FX_FAILURE,
                                  "not supported to get directory "+_src);
        }

        _dst=null;
        if(isDstDir){
          int i=_src.lastIndexOf('/');
          if(i==-1) dstsb.append(_src);
          else dstsb.append(_src.substring(i + 1));
          _dst=dstsb.toString();
          if(_dst.indexOf("..")!=-1){
            String dstc = (new java.io.File(dst)).getCanonicalPath();
            String _dstc = (new java.io.File(_dst)).getCanonicalPath();
            if(!(_dstc.length()>dstc.length() &&
                 _dstc.substring(0, dstc.length()+1).equals(dstc+file_separator))){
              throw new SftpException(SSH_FX_FAILURE,
                                      "writing to an unexpected file "+_src);
            }
          }
          dstsb.delete(dst.length(), _dst.length());
        }
        else{
          _dst=dst;
        }

        File _dstFile=new File(_dst);
        if(mode==RESUME){
          long size_of_src=attr.getSize();
          long size_of_dst=_dstFile.length();
          if(size_of_dst>size_of_src){
            throw new SftpException(SSH_FX_FAILURE,
                                    "failed to resume for "+_dst);
          }
          if(size_of_dst==size_of_src){
            return;
          }
        }

        if(monitor!=null){
          monitor.init(SftpProgressMonitor.GET, _src, _dst, attr.getSize());
          if(mode==RESUME){
            monitor.count(_dstFile.length());
          }
        }

        FileOutputStream fos=null;
        _dstExist = _dstFile.exists();
        try{
          if(mode==OVERWRITE){
            fos=new FileOutputStream(_dst);
          }
          else{
            fos=new FileOutputStream(_dst, true); // append
          }
          // System.err.println("_get: "+_src+", "+_dst);
          _get(_src, fos, monitor, mode, new File(_dst).length());
        }
        finally{
          if(fos!=null){
            fos.close();
          }
        }
      }
    }
    catch(Exception e){
      if(!_dstExist && _dst!=null){
        File _dstFile = new File(_dst);
        if(_dstFile.exists() && _dstFile.length()==0){
          _dstFile.delete();
        }
      }
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }
  public void get(String src, OutputStream dst) throws SftpException{
    get(src, dst, null, OVERWRITE, 0);
  }
  public void get(String src, OutputStream dst,
                  SftpProgressMonitor monitor) throws SftpException{
    get(src, dst, monitor, OVERWRITE, 0);
  }
  public void get(String src, OutputStream dst,
                   SftpProgressMonitor monitor, int mode, long skip) throws SftpException{
//System.err.println("get: "+src+", "+dst);
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      src=remoteAbsolutePath(src);
      src=isUnique(src);

      if(monitor!=null){
        SftpATTRS attr=_stat(src);
        monitor.init(SftpProgressMonitor.GET, src, "??", attr.getSize());
        if(mode==RESUME){
          monitor.count(skip);
        }
      }
      _get(src, dst, monitor, mode, skip);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  private void _get(String src, OutputStream dst,
                    SftpProgressMonitor monitor, int mode, long skip) throws SftpException{
    //System.err.println("_get: "+src+", "+dst);

    byte[] srcb=Util.str2byte(src, fEncoding);
    try{
      sendOPENR(srcb);

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_STATUS && type!=SSH_FXP_HANDLE){
        throw new SftpException(SSH_FX_FAILURE, "");
      }

      if(type==SSH_FXP_STATUS){
        int i=buf.getInt();
        throwStatusError(buf, i);
      }

      byte[] handle=buf.getString();         // filename

      long offset=0;
      if(mode==RESUME){
        offset+=skip;
      }

      int request_max=1;
      rq.init();
      long request_offset=offset;

      int request_len = buf.buffer.length-13;
      if(server_version==0){ request_len=1024; }

      loop:
      while(true){

        while(rq.count() < request_max){
          sendREAD(handle, request_offset, request_len, rq);
          request_offset += request_len;
        }

        header=header(buf, header);
        length=header.length;
        type=header.type;

        RequestQueue.Request rr = null;
        try{
          rr = rq.get(header.rid);
        }
        catch(RequestQueue.OutOfOrderException e){
          request_offset = e.offset;
          skip(header.length);
          rq.cancel(header, buf);
          continue;
        }

        if(type==SSH_FXP_STATUS){
          fill(buf, length);
          int i=buf.getInt();
          if(i==SSH_FX_EOF){
            break loop;
          }
          throwStatusError(buf, i);
        }

        if(type!=SSH_FXP_DATA){
          break loop;
        }

        buf.rewind();
        fill(buf.buffer, 0, 4); length-=4;
        int length_of_data = buf.getInt();   // length of data

        /**
         Since sftp protocol version 6, "end-of-file" has been defined,

           byte   SSH_FXP_DATA
           uint32 request-id
           string data
           bool   end-of-file [optional]

         but some sftpd server will send such a field in the sftp protocol 3 ;-(
         */
        int optional_data = length - length_of_data;

        int foo = length_of_data;
        while(foo>0){
          int bar=foo;
          if(bar>buf.buffer.length){
            bar=buf.buffer.length;
          }
          int data_len = io_in.read(buf.buffer, 0, bar);
          if(data_len<0){
            break loop;
          }

          dst.write(buf.buffer, 0, data_len);

          offset+=data_len;
          foo-=data_len;

          if(monitor!=null){
            if(!monitor.count(data_len)){
              skip(foo);
              if(optional_data>0){
                skip(optional_data);
              }
              break loop;
            }
          }

        }
        //System.err.println("length: "+length);  // length should be 0

        if(optional_data>0){
          skip(optional_data);
        }

        if(length_of_data<rr.length){  //
          rq.cancel(header, buf);
          sendREAD(handle, rr.offset+length_of_data, (int)(rr.length-length_of_data), rq);
          request_offset=rr.offset+rr.length;
        }

        if(request_max < rq.size()){
          request_max++;
        }
      }
      dst.flush();

      if(monitor!=null)monitor.end();

      rq.cancel(header, buf);

      _sendCLOSE(handle, header);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }


  private class RequestQueue {
    class OutOfOrderException extends Exception {
      long offset;
      OutOfOrderException(long offset){
        this.offset=offset;
      }
    }
    class Request {
      int id;
      long offset;
      long length;
    }

    Request[] rrq=null;
    int head, count;
    RequestQueue(int size){
      rrq = new Request[size];
      for(int i=0; i<rrq.length; i++){
        rrq[i]=new Request();
      }
      init();
    }

    void init(){
      head=count=0;
    }

    void add(int id, long offset, int length){
      if(count == 0) head = 0;
      int tail = head + count;
      if(tail>=rrq.length) tail -= rrq.length;
      rrq[tail].id=id;
      rrq[tail].offset=offset;
      rrq[tail].length=length;
      count++;
    }

    Request get(int id) throws OutOfOrderException, SftpException {
      count -= 1;
      int i = head;
      head++;
      if(head==rrq.length) head=0;
      if(rrq[i].id != id){
        long offset = getOffset();
        boolean find = false;
        for(int j = 0; j<rrq.length; j++){
          if(rrq[j].id == id){
            find = true;
            rrq[j].id = 0;
            break;
          }
        }
        if(find)
          throw new OutOfOrderException(offset);
        throw new SftpException(SSH_FX_FAILURE,
                                "RequestQueue: unknown request id "+id);
      }
      rrq[i].id = 0;
      return rrq[i];
    }

    int count() {
      return count;
    }

    int size() {
      return rrq.length;
    }

    void cancel(Header header, Buffer buf) throws IOException {
      int _count = count;
      for(int i=0; i<_count; i++){
        header=header(buf, header);
        int length=header.length;
        for(int j=0; j<rrq.length; j++){
          if(rrq[j].id == header.rid){
            rrq[j].id=0;
            break;
          }
        }
        skip(length);
      }
      init();
    }

    long getOffset(){
      long result = Long.MAX_VALUE;

      for(int i=0; i<rrq.length; i++){
        if(rrq[i].id == 0)
          continue;
        if(result>rrq[i].offset)
          result=rrq[i].offset;
      }

      return result;
    }
  }

  public InputStream get(String src) throws SftpException{
    return get(src, null, 0L);
  }
  public InputStream get(String src, SftpProgressMonitor monitor) throws SftpException{
    return get(src, monitor, 0L);
  }

  /**
   * @deprecated  This method will be deleted in the future.
   */
  public InputStream get(String src, int mode) throws SftpException{
    return get(src, null, 0L);
  }
  /**
   * @deprecated  This method will be deleted in the future.
   */
  public InputStream get(String src, final SftpProgressMonitor monitor, final int mode) throws SftpException{
    return get(src, monitor, 0L);
  }
  public InputStream get(String src, final SftpProgressMonitor monitor, final long skip) throws SftpException{

    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      src=remoteAbsolutePath(src);
      src=isUnique(src);

      byte[] srcb=Util.str2byte(src, fEncoding);

      SftpATTRS attr=_stat(srcb);
      if(monitor!=null){
        monitor.init(SftpProgressMonitor.GET, src, "??", attr.getSize());
      }

      sendOPENR(srcb);

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_STATUS && type!=SSH_FXP_HANDLE){
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      if(type==SSH_FXP_STATUS){
        int i=buf.getInt();
        throwStatusError(buf, i);
      }

      final byte[] handle=buf.getString();         // handle

      rq.init();

      java.io.InputStream in=new java.io.InputStream(){
           long offset=skip;
           boolean closed=false;
           int rest_length=0;
           byte[] _data=new byte[1];
           byte[] rest_byte=new byte[1024];
           Header header=new Header();
           int request_max=1;
           long request_offset=offset;

           public int read() throws java.io.IOException{
             if(closed)return -1;
             int i=read(_data, 0, 1);
             if (i==-1) { return -1; }
             else {
               return _data[0]&0xff;
             }
           }
           public int read(byte[] d) throws java.io.IOException{
             if(closed)return -1;
             return read(d, 0, d.length);
           }
           public int read(byte[] d, int s, int len) throws java.io.IOException{
             if(closed)return -1;
             if(d==null){throw new NullPointerException();}
             if(s<0 || len <0 || s+len>d.length){
               throw new IndexOutOfBoundsException();
             }
             if(len==0){ return 0; }

             if(rest_length>0){
               int foo=rest_length;
               if(foo>len) foo=len;
               System.arraycopy(rest_byte, 0, d, s, foo);
               if(foo!=rest_length){
                 System.arraycopy(rest_byte, foo,
                                  rest_byte, 0, rest_length-foo);
               }

               if(monitor!=null){
                 if(!monitor.count(foo)){
                   close();
                   return -1;
                 }
               }

               rest_length-=foo;
               return foo;
             }

             if(buf.buffer.length-13<len){
               len=buf.buffer.length-13;
             }
             if(server_version==0 && len>1024){
               len=1024;
             }

             if(rq.count()==0
                || true // working around slow transfer speed for
                        // some sftp servers including Titan FTP.
               ) {
               int request_len = buf.buffer.length-13;
               if(server_version==0){ request_len=1024; }

               while(rq.count() < request_max){
                 try{
                   sendREAD(handle, request_offset, request_len, rq);
                 }
                 catch(Exception e){ throw new IOException("error"); }
                 request_offset += request_len;
               }
             }

             header=header(buf, header);
             rest_length=header.length;
             int type=header.type;
             int id=header.rid;

             RequestQueue.Request rr = null;
             try{
               rr = rq.get(header.rid);
             }
             catch(RequestQueue.OutOfOrderException e){
               request_offset = e.offset;
               skip(header.length);
               rq.cancel(header, buf);
               return 0;
             }
             catch(SftpException e){
               throw new IOException("error: "+e.toString());
             }

             if(type!=SSH_FXP_STATUS && type!=SSH_FXP_DATA){
               throw new IOException("error");
             }
             if(type==SSH_FXP_STATUS){
               fill(buf, rest_length);
               int i=buf.getInt();
               rest_length=0;
               if(i==SSH_FX_EOF){
                 close();
                 return -1;
               }
               //throwStatusError(buf, i);
               throw new IOException("error");
             }

             buf.rewind();
             fill(buf.buffer, 0, 4);
             int length_of_data = buf.getInt(); rest_length-=4;

             /**
              Since sftp protocol version 6, "end-of-file" has been defined,

                byte   SSH_FXP_DATA
                uint32 request-id
                string data
                bool   end-of-file [optional]

              but some sftpd server will send such a field in the sftp protocol 3 ;-(
              */
             int optional_data = rest_length - length_of_data;

             offset += length_of_data;
             int foo = length_of_data;
             if(foo>0){
               int bar=foo;
               if(bar>len){
                 bar=len;
               }
               int i=io_in.read(d, s, bar);
               if(i<0){
                 return -1;
               }
               foo-=i;
               rest_length=foo;

               if(foo>0){
                 if(rest_byte.length<foo){
                   rest_byte=new byte[foo];
                 }
                 int _s=0;
                 int _len=foo;
                 int j;
                 while(_len>0){
                   j=io_in.read(rest_byte, _s, _len);
                   if(j<=0)break;
                   _s+=j;
                   _len-=j;
                 }
               }

               if(optional_data>0){
                 io_in.skip(optional_data);
               }

               if(length_of_data<rr.length){  //
                 rq.cancel(header, buf);
                 try {
                   sendREAD(handle,
                            rr.offset+length_of_data,
                            (int)(rr.length-length_of_data), rq);
                 }
                 catch(Exception e){ throw new IOException("error"); }
                 request_offset=rr.offset+rr.length;
               }

               if(request_max < rq.size()){
                 request_max++;
               }

               if(monitor!=null){
                 if(!monitor.count(i)){
                   close();
                   return -1;
                 }
               }

               return i;
             }
             return 0; // ??
           }
           public void close() throws IOException{
             if(closed)return;
             closed=true;
             if(monitor!=null)monitor.end();
             rq.cancel(header, buf);
             try{_sendCLOSE(handle, header);}
             catch(Exception e){throw new IOException("error");}
           }
         };
       return in;
     }
     catch(Exception e){
       if(e instanceof SftpException) throw (SftpException)e;
       if(e instanceof Throwable)
         throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
       throw new SftpException(SSH_FX_FAILURE, "");
     }
   }

   public java.util.Vector ls(String path) throws SftpException{
     final java.util.Vector v = new Vector();
     LsEntrySelector selector = new LsEntrySelector(){
       public int select(LsEntry entry){
         v.addElement(entry);
         return CONTINUE;
       }
     };
     ls(path, selector);
     return v;
   }

  /**
   * List files specified by the remote <code>path</code>.
   * Each files and directories will be passed to
   * <code>LsEntrySelector#select(LsEntry)</code> method, and if that method
   * returns <code>LsEntrySelector#BREAK</code>, the operation will be
   * canceled immediately.
   *
   * @see ChannelSftp.LsEntrySelector
   * @since 0.1.47
   */
   public void ls(String path, LsEntrySelector selector) throws SftpException{
     //System.out.println("ls: "+path);
     try{
       ((MyPipedInputStream)io_in).updateReadSide();

       path=remoteAbsolutePath(path);
       byte[] pattern=null;
       java.util.Vector v=new java.util.Vector();

       int foo=path.lastIndexOf('/');
       String dir=path.substring(0, ((foo==0)?1:foo));
       String _pattern=path.substring(foo+1);
       dir=Util.unquote(dir);

       // If pattern has included '*' or '?', we need to convert
       // to UTF-8 string before globbing.
       byte[][] _pattern_utf8=new byte[1][];
       boolean pattern_has_wildcard=isPattern(_pattern, _pattern_utf8);

       if(pattern_has_wildcard){
         pattern=_pattern_utf8[0];
       }
       else{
         String upath=Util.unquote(path);
         //SftpATTRS attr=_lstat(upath);
         SftpATTRS attr=_stat(upath);
         if(attr.isDir()){
           pattern=null;
           dir=upath;
         }
         else{
           /*
             // If we can generage longname by ourself,
             // we don't have to use openDIR.
           String filename=Util.unquote(_pattern);
           String longname=...
           v.addElement(new LsEntry(filename, longname, attr));
           return v;
           */

           if(fEncoding_is_utf8){
             pattern=_pattern_utf8[0];
             pattern=Util.unquote(pattern);
           }
           else{
             _pattern=Util.unquote(_pattern);
             pattern=Util.str2byte(_pattern, fEncoding);
           }

         }
       }

       sendOPENDIR(Util.str2byte(dir, fEncoding));

       Header header=new Header();
       header=header(buf, header);
       int length=header.length;
       int type=header.type;

       fill(buf, length);

       if(type!=SSH_FXP_STATUS && type!=SSH_FXP_HANDLE){
         throw new SftpException(SSH_FX_FAILURE, "");
       }
       if(type==SSH_FXP_STATUS){
         int i=buf.getInt();
         throwStatusError(buf, i);
       }

       int cancel = LsEntrySelector.CONTINUE;
       byte[] handle=buf.getString();         // handle

       while(cancel==LsEntrySelector.CONTINUE){

         sendREADDIR(handle);

         header=header(buf, header);
         length=header.length;
         type=header.type;
         if(type!=SSH_FXP_STATUS && type!=SSH_FXP_NAME){
           throw new SftpException(SSH_FX_FAILURE, "");
         }
         if(type==SSH_FXP_STATUS){
           fill(buf, length);
           int i=buf.getInt();
           if(i==SSH_FX_EOF)
             break;
           throwStatusError(buf, i);
         }

         buf.rewind();
         fill(buf.buffer, 0, 4); length-=4;
         int count=buf.getInt();

         byte[] str;
         int flags;

         buf.reset();
         while(count>0){
           if(length>0){
             buf.shift();
             int j=(buf.buffer.length>(buf.index+length)) ?
               length :
               (buf.buffer.length-buf.index);
             int i=fill(buf.buffer, buf.index, j);
             buf.index+=i;
             length-=i;
           }
           byte[] filename=buf.getString();
           byte[] longname=null;
           if(server_version<=3){
             longname=buf.getString();
           }
           SftpATTRS attrs=SftpATTRS.getATTR(buf);

           if(cancel==LsEntrySelector.BREAK){
             count--;
             continue;
           }

           boolean find=false;
           String f=null;
           if(pattern==null){
             find=true;
           }
           else if(!pattern_has_wildcard){
             find=Util.array_equals(pattern, filename);
           }
           else{
             byte[] _filename=filename;
             if(!fEncoding_is_utf8){
               f=Util.byte2str(_filename, fEncoding);
               _filename=Util.str2byte(f, UTF8);
             }
             find=Util.glob(pattern, _filename);
           }

           if(find){
             if(f==null){
               f=Util.byte2str(filename, fEncoding);
             }
             String l=null;
             if(longname==null){
               // TODO: we need to generate long name from attrs
               //       for the sftp protocol 4(and later).
               l=attrs.toString()+" "+f;
             }
             else{
               l=Util.byte2str(longname, fEncoding);
             }

             cancel = selector.select(new LsEntry(f, l, attrs));
           }

           count--;
         }
       }
       _sendCLOSE(handle, header);

       /*
       if(v.size()==1 && pattern_has_wildcard){
         LsEntry le=(LsEntry)v.elementAt(0);
         if(le.getAttrs().isDir()){
           String f=le.getFilename();
           if(isPattern(f)){
             f=Util.quote(f);
           }
           if(!dir.endsWith("/")){
             dir+="/";
           }
           v=null;
           return ls(dir+f);
         }
       }
       */

     }
     catch(Exception e){
       if(e instanceof SftpException) throw (SftpException)e;
       if(e instanceof Throwable)
         throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
       throw new SftpException(SSH_FX_FAILURE, "");
     }
   }

   public String readlink(String path) throws SftpException{
     try{
       if(server_version<3){
         throw new SftpException(SSH_FX_OP_UNSUPPORTED,
                                 "The remote sshd is too old to support symlink operation.");
       }

       ((MyPipedInputStream)io_in).updateReadSide();

       path=remoteAbsolutePath(path);

       path=isUnique(path);

       sendREADLINK(Util.str2byte(path, fEncoding));

       Header header=new Header();
       header=header(buf, header);
       int length=header.length;
       int type=header.type;

       fill(buf, length);

       if(type!=SSH_FXP_STATUS && type!=SSH_FXP_NAME){
         throw new SftpException(SSH_FX_FAILURE, "");
       }
       if(type==SSH_FXP_NAME){
         int count=buf.getInt();       // count
         byte[] filename=null;
         for(int i=0; i<count; i++){
           filename=buf.getString();
           if(server_version<=3){
             byte[] longname=buf.getString();
           }
           SftpATTRS.getATTR(buf);
         }
         return Util.byte2str(filename, fEncoding);
       }

       int i=buf.getInt();
       throwStatusError(buf, i);
     }
     catch(Exception e){
       if(e instanceof SftpException) throw (SftpException)e;
       if(e instanceof Throwable)
         throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
       throw new SftpException(SSH_FX_FAILURE, "");
     }
     return null;
   }

   public void symlink(String oldpath, String newpath) throws SftpException{
     if(server_version<3){
       throw new SftpException(SSH_FX_OP_UNSUPPORTED,
                               "The remote sshd is too old to support symlink operation.");
     }

     try{
       ((MyPipedInputStream)io_in).updateReadSide();

       String _oldpath=remoteAbsolutePath(oldpath);
       newpath=remoteAbsolutePath(newpath);

       _oldpath=isUnique(_oldpath);
       if(oldpath.charAt(0)!='/'){ // relative path
         String cwd=getCwd();
         oldpath=_oldpath.substring(cwd.length()+(cwd.endsWith("/")?0:1));
       }
       else {
         oldpath=_oldpath;
       }

       if(isPattern(newpath)){
         throw new SftpException(SSH_FX_FAILURE, newpath);
       }
       newpath=Util.unquote(newpath);

       sendSYMLINK(Util.str2byte(oldpath, fEncoding),
                   Util.str2byte(newpath, fEncoding));

       Header header=new Header();
       header=header(buf, header);
       int length=header.length;
       int type=header.type;

       fill(buf, length);

       if(type!=SSH_FXP_STATUS){
         throw new SftpException(SSH_FX_FAILURE, "");
       }

       int i=buf.getInt();
       if(i==SSH_FX_OK) return;
       throwStatusError(buf, i);
     }
     catch(Exception e){
       if(e instanceof SftpException) throw (SftpException)e;
       if(e instanceof Throwable)
         throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
       throw new SftpException(SSH_FX_FAILURE, "");
     }
   }

   public void hardlink(String oldpath, String newpath) throws SftpException{
     if(!extension_hardlink){
       throw new SftpException(SSH_FX_OP_UNSUPPORTED,
                               "hardlink@openssh.com is not supported");
     }

     try{
       ((MyPipedInputStream)io_in).updateReadSide();

       String _oldpath=remoteAbsolutePath(oldpath);
       newpath=remoteAbsolutePath(newpath);

       _oldpath=isUnique(_oldpath);
       if(oldpath.charAt(0)!='/'){ // relative path
         String cwd=getCwd();
         oldpath=_oldpath.substring(cwd.length()+(cwd.endsWith("/")?0:1));
       }
       else {
         oldpath=_oldpath;
       }

       if(isPattern(newpath)){
         throw new SftpException(SSH_FX_FAILURE, newpath);
       }
       newpath=Util.unquote(newpath);

       sendHARDLINK(Util.str2byte(oldpath, fEncoding),
                   Util.str2byte(newpath, fEncoding));

       Header header=new Header();
       header=header(buf, header);
       int length=header.length;
       int type=header.type;

       fill(buf, length);

       if(type!=SSH_FXP_STATUS){
         throw new SftpException(SSH_FX_FAILURE, "");
       }

       int i=buf.getInt();
       if(i==SSH_FX_OK) return;
       throwStatusError(buf, i);
     }
     catch(Exception e){
       if(e instanceof SftpException) throw (SftpException)e;
       if(e instanceof Throwable)
         throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
       throw new SftpException(SSH_FX_FAILURE, "");
     }
   }

   public void rename(String oldpath, String newpath) throws SftpException{
     if(server_version<2){
       throw new SftpException(SSH_FX_OP_UNSUPPORTED,
                               "The remote sshd is too old to support rename operation.");
     }

     try{
       ((MyPipedInputStream)io_in).updateReadSide();

       oldpath=remoteAbsolutePath(oldpath);
       newpath=remoteAbsolutePath(newpath);

       oldpath=isUnique(oldpath);

       Vector v=glob_remote(newpath);
       int vsize=v.size();
       if(vsize>=2){
         throw new SftpException(SSH_FX_FAILURE, v.toString());
       }
       if(vsize==1){
         newpath=(String)(v.elementAt(0));
       }
       else{  // vsize==0
         if(isPattern(newpath))
           throw new SftpException(SSH_FX_FAILURE, newpath);
         newpath=Util.unquote(newpath);
       }

       sendRENAME(Util.str2byte(oldpath, fEncoding),
                  Util.str2byte(newpath, fEncoding));

       Header header=new Header();
       header=header(buf, header);
       int length=header.length;
       int type=header.type;

       fill(buf, length);

       if(type!=SSH_FXP_STATUS){
         throw new SftpException(SSH_FX_FAILURE, "");
       }

       int i=buf.getInt();
       if(i==SSH_FX_OK) return;
       throwStatusError(buf, i);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }
  public void rm(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();

      Header header=new Header();

      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));
        sendREMOVE(Util.str2byte(path, fEncoding));

        header=header(buf, header);
        int length=header.length;
        int type=header.type;

        fill(buf, length);

        if(type!=SSH_FXP_STATUS){
          throw new SftpException(SSH_FX_FAILURE, "");
        }
        int i=buf.getInt();
        if(i!=SSH_FX_OK){
          throwStatusError(buf, i);
        }
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  private boolean isRemoteDir(String path){
    try{
      sendSTAT(Util.str2byte(path, fEncoding));

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_ATTRS){
        return false;
      }
      SftpATTRS attr=SftpATTRS.getATTR(buf);
      return attr.isDir();
    }
    catch(Exception e){}
    return false;
  }

  public void chgrp(int gid, String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();
      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));

        SftpATTRS attr=_stat(path);

        attr.setFLAGS(0);
        attr.setUIDGID(attr.uid, gid);
        _setStat(path, attr);
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void chown(int uid, String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();
      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));

        SftpATTRS attr=_stat(path);

        attr.setFLAGS(0);
        attr.setUIDGID(uid, attr.gid);
        _setStat(path, attr);
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void chmod(int permissions, String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();
      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));

        SftpATTRS attr=_stat(path);

        attr.setFLAGS(0);
        attr.setPERMISSIONS(permissions);
        _setStat(path, attr);
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void setMtime(String path, int mtime) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();
      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));

        SftpATTRS attr=_stat(path);

        attr.setFLAGS(0);
        attr.setACMODTIME(attr.getATime(), mtime);
        _setStat(path, attr);
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void rmdir(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();

      Header header=new Header();

      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));
        sendRMDIR(Util.str2byte(path, fEncoding));

        header=header(buf, header);
        int length=header.length;
        int type=header.type;

        fill(buf, length);

        if(type!=SSH_FXP_STATUS){
          throw new SftpException(SSH_FX_FAILURE, "");
        }

        int i=buf.getInt();
        if(i!=SSH_FX_OK){
          throwStatusError(buf, i);
        }
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public void mkdir(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      sendMKDIR(Util.str2byte(path, fEncoding), null);

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_STATUS){
        throw new SftpException(SSH_FX_FAILURE, "");
      }

      int i=buf.getInt();
      if(i==SSH_FX_OK) return;
      throwStatusError(buf, i);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public SftpATTRS stat(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);
      path=isUnique(path);

      return _stat(path);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    //return null;
  }

  private SftpATTRS _stat(byte[] path) throws SftpException{
    try{

      sendSTAT(path);

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_ATTRS){
        if(type==SSH_FXP_STATUS){
          int i=buf.getInt();
          throwStatusError(buf, i);
        }
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      SftpATTRS attr=SftpATTRS.getATTR(buf);
      return attr;
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    //return null;
  }

  private SftpATTRS _stat(String path) throws SftpException{
    return _stat(Util.str2byte(path, fEncoding));
  }

  public SftpStatVFS statVFS(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);
      path=isUnique(path);

      return _statVFS(path);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    //return null;
  }

  private SftpStatVFS _statVFS(byte[] path) throws SftpException{
    if(!extension_statvfs){
      throw new SftpException(SSH_FX_OP_UNSUPPORTED,
                              "statvfs@openssh.com is not supported");
    }

    try{

      sendSTATVFS(path);

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type != (SSH_FXP_EXTENDED_REPLY&0xff)){
        if(type==SSH_FXP_STATUS){
          int i=buf.getInt();
          throwStatusError(buf, i);
        }
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      else {
        SftpStatVFS stat = SftpStatVFS.getStatVFS(buf);
        return stat;
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    //return null;
  }

  private SftpStatVFS _statVFS(String path) throws SftpException{
    return _statVFS(Util.str2byte(path, fEncoding));
  }

  public SftpATTRS lstat(String path) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);
      path=isUnique(path);

      return _lstat(path);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  private SftpATTRS _lstat(String path) throws SftpException{
    try{
      sendLSTAT(Util.str2byte(path, fEncoding));

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_ATTRS){
        if(type==SSH_FXP_STATUS){
          int i=buf.getInt();
          throwStatusError(buf, i);
        }
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      SftpATTRS attr=SftpATTRS.getATTR(buf);
      return attr;
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  private byte[] _realpath(String path) throws SftpException, IOException, Exception{
    sendREALPATH(Util.str2byte(path, fEncoding));

    Header header=new Header();
    header=header(buf, header);
    int length=header.length;
    int type=header.type;

    fill(buf, length);

    if(type!=SSH_FXP_STATUS && type!=SSH_FXP_NAME){
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    int i;
    if(type==SSH_FXP_STATUS){
      i=buf.getInt();
      throwStatusError(buf, i);
    }
    i=buf.getInt();   // count

    byte[] str=null;
    while(i-->0){
      str=buf.getString();  // absolute path;
      if(server_version<=3){
        byte[] lname=buf.getString();  // long filename
      }
      SftpATTRS attr=SftpATTRS.getATTR(buf);  // dummy attribute
    }
    return str;
  }

  public void setStat(String path, SftpATTRS attr) throws SftpException{
    try{
      ((MyPipedInputStream)io_in).updateReadSide();

      path=remoteAbsolutePath(path);

      Vector v=glob_remote(path);
      int vsize=v.size();
      for(int j=0; j<vsize; j++){
        path=(String)(v.elementAt(j));
        _setStat(path, attr);
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }
  private void _setStat(String path, SftpATTRS attr) throws SftpException{
    try{
      sendSETSTAT(Util.str2byte(path, fEncoding), attr);

      Header header=new Header();
      header=header(buf, header);
      int length=header.length;
      int type=header.type;

      fill(buf, length);

      if(type!=SSH_FXP_STATUS){
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      int i=buf.getInt();
      if(i!=SSH_FX_OK){
        throwStatusError(buf, i);
      }
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public String pwd() throws SftpException{ return getCwd(); }
  public String lpwd(){ return lcwd; }
  public String version(){ return version; }
  public String getHome() throws SftpException {
    if(home==null){
      try{
        ((MyPipedInputStream)io_in).updateReadSide();

        byte[] _home=_realpath("");
        home=Util.byte2str(_home, fEncoding);
      }
      catch(Exception e){
        if(e instanceof SftpException) throw (SftpException)e;
        if(e instanceof Throwable)
          throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
        throw new SftpException(SSH_FX_FAILURE, "");
      }
    }
    return home;
  }

  private String getCwd() throws SftpException{
    if(cwd==null)
      cwd=getHome();
    return cwd;
  }

  private void setCwd(String cwd){
    this.cwd=cwd;
  }

  private void read(byte[] buf, int s, int l) throws IOException, SftpException{
    int i=0;
    while(l>0){
      i=io_in.read(buf, s, l);
      if(i<=0){
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      s+=i;
      l-=i;
    }
  }

  private boolean checkStatus(int[] ackid, Header header) throws IOException, SftpException{
    header=header(buf, header);
    int length=header.length;
    int type=header.type;
    if(ackid!=null)
      ackid[0]=header.rid;

    fill(buf, length);

    if(type!=SSH_FXP_STATUS){
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    int i=buf.getInt();
    if(i!=SSH_FX_OK){
      throwStatusError(buf, i);
    }
    return true;
  }
  private boolean _sendCLOSE(byte[] handle, Header header) throws Exception{
    sendCLOSE(handle);
    return checkStatus(null, header);
  }

  private void sendINIT() throws Exception{
    packet.reset();
    putHEAD(SSH_FXP_INIT, 5);
    buf.putInt(3);                // version 3
    getSession().write(packet, this, 5+4);
  }

  private void sendREALPATH(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_REALPATH, path);
  }
  private void sendSTAT(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_STAT, path);
  }
  private void sendSTATVFS(byte[] path) throws Exception{
    sendPacketPath((byte)0, path, "statvfs@openssh.com");
  }
  /*
  private void sendFSTATVFS(byte[] handle) throws Exception{
    sendPacketPath((byte)0, handle, "fstatvfs@openssh.com");
  }
  */
  private void sendLSTAT(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_LSTAT, path);
  }
  private void sendFSTAT(byte[] handle) throws Exception{
    sendPacketPath(SSH_FXP_FSTAT, handle);
  }
  private void sendSETSTAT(byte[] path, SftpATTRS attr) throws Exception{
    packet.reset();
    putHEAD(SSH_FXP_SETSTAT, 9+path.length+attr.length());
    buf.putInt(seq++);
    buf.putString(path);             // path
    attr.dump(buf);
    getSession().write(packet, this, 9+path.length+attr.length()+4);
  }
  private void sendREMOVE(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_REMOVE, path);
  }
  private void sendMKDIR(byte[] path, SftpATTRS attr) throws Exception{
    packet.reset();
    putHEAD(SSH_FXP_MKDIR, 9+path.length+(attr!=null?attr.length():4));
    buf.putInt(seq++);
    buf.putString(path);             // path
    if(attr!=null) attr.dump(buf);
    else buf.putInt(0);
    getSession().write(packet, this, 9+path.length+(attr!=null?attr.length():4)+4);
  }
  private void sendRMDIR(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_RMDIR, path);
  }
  private void sendSYMLINK(byte[] p1, byte[] p2) throws Exception{
    sendPacketPath(SSH_FXP_SYMLINK, p1, p2);
  }
  private void sendHARDLINK(byte[] p1, byte[] p2) throws Exception{
    sendPacketPath((byte)0, p1, p2, "hardlink@openssh.com");
  }
  private void sendREADLINK(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_READLINK, path);
  }
  private void sendOPENDIR(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_OPENDIR, path);
  }
  private void sendREADDIR(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_READDIR, path);
  }
  private void sendRENAME(byte[] p1, byte[] p2) throws Exception{
    sendPacketPath(SSH_FXP_RENAME, p1, p2,
                   extension_posix_rename ? "posix-rename@openssh.com" : null);
  }
  private void sendCLOSE(byte[] path) throws Exception{
    sendPacketPath(SSH_FXP_CLOSE, path);
  }
  private void sendOPENR(byte[] path) throws Exception{
    sendOPEN(path, SSH_FXF_READ);
  }
  private void sendOPENW(byte[] path) throws Exception{
    sendOPEN(path, SSH_FXF_WRITE|SSH_FXF_CREAT|SSH_FXF_TRUNC);
  }
  private void sendOPENA(byte[] path) throws Exception{
    sendOPEN(path, SSH_FXF_WRITE|/*SSH_FXF_APPEND|*/SSH_FXF_CREAT);
  }
  private void sendOPEN(byte[] path, int mode) throws Exception{
    packet.reset();
    putHEAD(SSH_FXP_OPEN, 17+path.length);
    buf.putInt(seq++);
    buf.putString(path);
    buf.putInt(mode);
    buf.putInt(0);           // attrs
    getSession().write(packet, this, 17+path.length+4);
  }
  private void sendPacketPath(byte fxp, byte[] path) throws Exception{
    sendPacketPath(fxp, path, (String)null);
  }
  private void sendPacketPath(byte fxp, byte[] path, String extension) throws Exception{
    packet.reset();
    int len = 9+path.length;
    if(extension == null) {
      putHEAD(fxp, len);
      buf.putInt(seq++);
    }
    else {
      len+=(4+extension.length());
      putHEAD(SSH_FXP_EXTENDED, len);
      buf.putInt(seq++);
      buf.putString(Util.str2byte(extension));
    }
    buf.putString(path);             // path
    getSession().write(packet, this, len+4);
  }

  private void sendPacketPath(byte fxp, byte[] p1, byte[] p2) throws Exception{
    sendPacketPath(fxp, p1, p2, null);
  }
  private void sendPacketPath(byte fxp, byte[] p1, byte[] p2, String extension) throws Exception{
    packet.reset();
    int len = 13+p1.length+p2.length;
    if(extension==null){
      putHEAD(fxp, len);
      buf.putInt(seq++);
    }
    else {
      len+=(4+extension.length());
      putHEAD(SSH_FXP_EXTENDED, len);
      buf.putInt(seq++);
      buf.putString(Util.str2byte(extension));
    }
    buf.putString(p1);
    buf.putString(p2);
    getSession().write(packet, this, len+4);
  }

  private int sendWRITE(byte[] handle, long offset,
                        byte[] data, int start, int length) throws Exception{
    int _length=length;
    opacket.reset();
    if(obuf.buffer.length<obuf.index+13+21+handle.length+length+Session.buffer_margin){
      _length=obuf.buffer.length-(obuf.index+13+21+handle.length+Session.buffer_margin);
      // System.err.println("_length="+_length+" length="+length);
    }

    putHEAD(obuf, SSH_FXP_WRITE, 21+handle.length+_length);       // 14
    obuf.putInt(seq++);                                      //  4
    obuf.putString(handle);                                  //  4+handle.length
    obuf.putLong(offset);                                    //  8
    if(obuf.buffer!=data){
      obuf.putString(data, start, _length);                    //  4+_length
    }
    else{
      obuf.putInt(_length);
      obuf.skip(_length);
    }
    getSession().write(opacket, this, 21+handle.length+_length+4);
    return _length;
  }
  private void sendREAD(byte[] handle, long offset, int length) throws Exception{
    sendREAD(handle, offset, length, null);
  }
  private void sendREAD(byte[] handle, long offset, int length,
                        RequestQueue rrq) throws Exception{
    packet.reset();
    putHEAD(SSH_FXP_READ, 21+handle.length);
    buf.putInt(seq++);
    buf.putString(handle);
    buf.putLong(offset);
    buf.putInt(length);
    getSession().write(packet, this, 21+handle.length+4);
    if(rrq!=null){
      rrq.add(seq-1, offset, length);
    }
  }

  private void putHEAD(Buffer buf, byte type, int length) throws Exception{
    buf.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
    buf.putInt(recipient);
    buf.putInt(length+4);
    buf.putInt(length);
    buf.putByte(type);
  }

  private void putHEAD(byte type, int length) throws Exception{
    putHEAD(buf, type, length);
  }

  private Vector glob_remote(String _path) throws Exception{
    Vector v=new Vector();
    int i=0;

    int foo=_path.lastIndexOf('/');
    if(foo<0){  // it is not absolute path.
      v.addElement(Util.unquote(_path));
      return v;
    }

    String dir=_path.substring(0, ((foo==0)?1:foo));
    String _pattern=_path.substring(foo+1);

    dir=Util.unquote(dir);

    byte[] pattern=null;
    byte[][] _pattern_utf8=new byte[1][];
    boolean pattern_has_wildcard=isPattern(_pattern, _pattern_utf8);

    if(!pattern_has_wildcard){
      if(!dir.equals("/"))
        dir+="/";
      v.addElement(dir+Util.unquote(_pattern));
      return v;
    }

    pattern=_pattern_utf8[0];

    sendOPENDIR(Util.str2byte(dir, fEncoding));

    Header header=new Header();
    header=header(buf, header);
    int length=header.length;
    int type=header.type;

    fill(buf, length);

    if(type!=SSH_FXP_STATUS && type!=SSH_FXP_HANDLE){
      throw new SftpException(SSH_FX_FAILURE, "");
    }
    if(type==SSH_FXP_STATUS){
      i=buf.getInt();
      throwStatusError(buf, i);
    }

    byte[] handle=buf.getString();         // filename
    String pdir=null;                      // parent directory

    while(true){
      sendREADDIR(handle);
      header=header(buf, header);
      length=header.length;
      type=header.type;

      if(type!=SSH_FXP_STATUS && type!=SSH_FXP_NAME){
        throw new SftpException(SSH_FX_FAILURE, "");
      }
      if(type==SSH_FXP_STATUS){
        fill(buf, length);
        break;
      }

      buf.rewind();
      fill(buf.buffer, 0, 4); length-=4;
      int count=buf.getInt();

      byte[] str;
      int flags;

      buf.reset();
      while(count>0){
        if(length>0){
          buf.shift();
          int j=(buf.buffer.length>(buf.index+length)) ? length : (buf.buffer.length-buf.index);
          i=io_in.read(buf.buffer, buf.index, j);
          if(i<=0)break;
          buf.index+=i;
          length-=i;
        }

        byte[] filename=buf.getString();
        //System.err.println("filename: "+new String(filename));
        if(server_version<=3){
          str=buf.getString();  // longname
        }
        SftpATTRS attrs=SftpATTRS.getATTR(buf);

        byte[] _filename=filename;
        String f=null;
        boolean found=false;

        if(!fEncoding_is_utf8){
          f=Util.byte2str(filename, fEncoding);
          _filename=Util.str2byte(f, UTF8);
        }
        found=Util.glob(pattern, _filename);

        if(found){
          if(f==null){
            f=Util.byte2str(filename, fEncoding);
          }
          if(pdir==null){
            pdir=dir;
            if(!pdir.endsWith("/")){
              pdir+="/";
            }
          }
          v.addElement(pdir+f);
        }
        count--;
      }
    }
    if(_sendCLOSE(handle, header))
      return v;
    return null;
  }

  private boolean isPattern(byte[] path){
    int length=path.length;
    int i=0;
    while(i<length){
      if(path[i]=='*' || path[i]=='?')
        return true;
      if(path[i]=='\\' && (i+1)<length)
        i++;
      i++;
    }
    return false;
  }

  private Vector glob_local(String _path) throws Exception{
//System.err.println("glob_local: "+_path);
    Vector v=new Vector();
    byte[] path=Util.str2byte(_path, UTF8);
    int i=path.length-1;
    while(i>=0){
      if(path[i]!='*' && path[i]!='?'){
        i--;
        continue;
      }
      if(!fs_is_bs &&
         i>0 && path[i-1]=='\\'){
        i--;
        if(i>0 && path[i-1]=='\\'){
          i--;
          i--;
          continue;
        }
      }
      break;
    }

    if(i<0){ v.addElement(fs_is_bs ? _path : Util.unquote(_path)); return v;}

    while(i>=0){
      if(path[i]==file_separatorc ||
         (fs_is_bs && path[i]=='/')){ // On Windows, '/' is also the separator.
        break;
      }
      i--;
    }

    if(i<0){ v.addElement(fs_is_bs ? _path : Util.unquote(_path)); return v;}

    byte[] dir;
    if(i==0){dir=new byte[]{(byte)file_separatorc};}
    else{
      dir=new byte[i];
      System.arraycopy(path, 0, dir, 0, i);
    }

    byte[] pattern=new byte[path.length-i-1];
    System.arraycopy(path, i+1, pattern, 0, pattern.length);

//System.err.println("dir: "+new String(dir)+" pattern: "+new String(pattern));
    try{
      String[] children=(new File(Util.byte2str(dir, UTF8))).list();
      String pdir=Util.byte2str(dir)+file_separator;
      for(int j=0; j<children.length; j++){
//System.err.println("children: "+children[j]);
        if(Util.glob(pattern, Util.str2byte(children[j], UTF8))){
          v.addElement(pdir+children[j]);
        }
      }
    }
    catch(Exception e){
    }
    return v;
  }

  private void throwStatusError(Buffer buf, int i) throws SftpException{
    if(server_version>=3 &&   // WindRiver's sftp will send invalid
       buf.getLength()>=4){   // SSH_FXP_STATUS packet.
      byte[] str=buf.getString();
      //byte[] tag=buf.getString();
      throw new SftpException(i, Util.byte2str(str, UTF8));
    }
    else{
      throw new SftpException(i, "Failure");
    }
  }

  private static boolean isLocalAbsolutePath(String path){
    return (new File(path)).isAbsolute();
  }

  public void disconnect(){
    super.disconnect();
  }

  private boolean isPattern(String path, byte[][] utf8){
    byte[] _path=Util.str2byte(path, UTF8);
    if(utf8!=null)
      utf8[0]=_path;
    return isPattern(_path);
  }

  private boolean isPattern(String path){
    return isPattern(path, null);
  }

  private void fill(Buffer buf, int len)  throws IOException{
    buf.reset();
    fill(buf.buffer, 0, len);
    buf.skip(len);
  }

  private int fill(byte[] buf, int s, int len) throws IOException{
    int i=0;
    int foo=s;
    while(len>0){
      i=io_in.read(buf, s, len);
      if(i<=0){
        throw new IOException("inputstream is closed");
        //return (s-foo)==0 ? i : s-foo;
      }
      s+=i;
      len-=i;
    }
    return s-foo;
  }
  private void skip(long foo) throws IOException{
    while(foo>0){
      long bar=io_in.skip(foo);
      if(bar<=0)
        break;
      foo-=bar;
    }
  }

  class Header{
    int length;
    int type;
    int rid;
  }
  private Header header(Buffer buf, Header header) throws IOException{
    buf.rewind();
    int i=fill(buf.buffer, 0, 9);
    header.length=buf.getInt()-5;
    header.type=buf.getByte()&0xff;
    header.rid=buf.getInt();
    return header;
  }

  private String remoteAbsolutePath(String path) throws SftpException{
    if(path.charAt(0)=='/') return path;
    String cwd=getCwd();
//    if(cwd.equals(getHome())) return path;
    if(cwd.endsWith("/")) return cwd+path;
    return cwd+"/"+path;
  }

  private String localAbsolutePath(String path){
    if(isLocalAbsolutePath(path)) return path;
    if(lcwd.endsWith(file_separator)) return lcwd+path;
    return lcwd+file_separator+path;
  }

  /**
   * This method will check if the given string can be expanded to the
   * unique string.  If it can be expanded to mutiple files, SftpException
   * will be thrown.
   * @return the returned string is unquoted.
   */
  private String isUnique(String path) throws SftpException, Exception{
    Vector v=glob_remote(path);
    if(v.size()!=1){
      throw new SftpException(SSH_FX_FAILURE, path+" is not unique: "+v.toString());
    }
    return (String)(v.elementAt(0));
  }

  public int getServerVersion() throws SftpException{
    if(!isConnected()){
      throw new SftpException(SSH_FX_FAILURE, "The channel is not connected.");
    }
    return server_version;
  }

  public void setFilenameEncoding(String encoding) throws SftpException{
    int sversion=getServerVersion();
    if(3 <= sversion && sversion <= 5 &&
       !encoding.equals(UTF8)){
      throw new SftpException(SSH_FX_FAILURE,
                              "The encoding can not be changed for this sftp server.");
    }
    if(encoding.equals(UTF8)){
      encoding=UTF8;
    }
    fEncoding=encoding;
    fEncoding_is_utf8=fEncoding.equals(UTF8);
  }

  public String getExtension(String key){
    if(extensions==null)
      return null;
    return (String)extensions.get(key);
  }

  public String realpath(String path) throws SftpException{
    try{
      byte[] _path=_realpath(remoteAbsolutePath(path));
      return Util.byte2str(_path, fEncoding);
    }
    catch(Exception e){
      if(e instanceof SftpException) throw (SftpException)e;
      if(e instanceof Throwable)
        throw new SftpException(SSH_FX_FAILURE, "", (Throwable)e);
      throw new SftpException(SSH_FX_FAILURE, "");
    }
  }

  public class LsEntry implements Comparable{
    private  String filename;
    private  String longname;
    private  SftpATTRS attrs;
    LsEntry(String filename, String longname, SftpATTRS attrs){
      setFilename(filename);
      setLongname(longname);
      setAttrs(attrs);
    }
    public String getFilename(){return filename;};
    void setFilename(String filename){this.filename = filename;};
    public String getLongname(){return longname;};
    void setLongname(String longname){this.longname = longname;};
    public SftpATTRS getAttrs(){return attrs;};
    void setAttrs(SftpATTRS attrs) {this.attrs = attrs;};
    public String toString(){ return longname; }
    public int compareTo(Object o) throws ClassCastException{
      if(o instanceof LsEntry){
        return filename.compareTo(((LsEntry)o).getFilename());
      }
      throw new ClassCastException("a decendent of LsEntry must be given.");
    }
  }

  /**
   * This interface will be passed as an argument for <code>ls</code> method.
   *
   * @see ChannelSftp.LsEntry
   * @see #ls(String, ChannelSftp.LsEntrySelector)
   * @since 0.1.47
   */
  public interface LsEntrySelector {
    public final int CONTINUE = 0;
    public final int BREAK = 1;

    /**
     * <p> The <code>select</code> method will be invoked in <code>ls</code>
     * method for each file entry. If this method returns BREAK,
     * <code>ls</code> will be canceled.
     *
     * @param entry one of entry from ls
     * @return if BREAK is returned, the 'ls' operation will be canceled.
     */
    public int select(LsEntry entry);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelShell extends ChannelSession{

  ChannelShell(){
    super();
    pty=true;
  }

  public void start() throws JSchException{
    Session _session=getSession();
    try{
      sendRequests();

      Request request=new RequestShell();
      request.request(_session, this);
    }
    catch(Exception e){
      if(e instanceof JSchException) throw (JSchException)e;
      if(e instanceof Throwable)
        throw new JSchException("ChannelShell", (Throwable)e);
      throw new JSchException("ChannelShell");
    }

    if(io.in!=null){
      thread=new Thread(this);
      thread.setName("Shell for "+_session.host);
      if(_session.daemon_thread){
        thread.setDaemon(_session.daemon_thread);
      }
      thread.start();
    }
  }

  void init() throws JSchException {
    io.setInputStream(getSession().in);
    io.setOutputStream(getSession().out);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2005-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class ChannelSubsystem extends ChannelSession{
  boolean xforwading=false;
  boolean pty=false;
  boolean want_reply=true;
  String subsystem="";
  public void setXForwarding(boolean foo){ xforwading=foo; }
  public void setPty(boolean foo){ pty=foo; }
  public void setWantReply(boolean foo){ want_reply=foo; }
  public void setSubsystem(String foo){ subsystem=foo; }
  public void start() throws JSchException{
    Session _session=getSession();
    try{
      Request request;
      if(xforwading){
        request=new RequestX11();
        request.request(_session, this);
      }
      if(pty){
        request=new RequestPtyReq();
        request.request(_session, this);
      }
      request=new RequestSubsystem();
      ((RequestSubsystem)request).request(_session, this, subsystem, want_reply);
    }
    catch(Exception e){
      if(e instanceof JSchException){ throw (JSchException)e; }
      if(e instanceof Throwable)
        throw new JSchException("ChannelSubsystem", (Throwable)e);
      throw new JSchException("ChannelSubsystem");
    }
    if(io.in!=null){
      thread=new Thread(this);
      thread.setName("Subsystem for "+_session.host);
      if(_session.daemon_thread){
        thread.setDaemon(_session.daemon_thread);
      }
      thread.start();
    }
  }

  void init() throws JSchException {
    io.setInputStream(getSession().in);
    io.setOutputStream(getSession().out);
  }

  public void setErrStream(java.io.OutputStream out){
    setExtOutputStream(out);
  }
  public java.io.InputStream getErrStream() throws java.io.IOException {
    return getExtInputStream();
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ChannelX11 extends Channel{

  static private final int LOCAL_WINDOW_SIZE_MAX=0x20000;
  static private final int LOCAL_MAXIMUM_PACKET_SIZE=0x4000;

  static private final int TIMEOUT=10*1000;

  private static String host="127.0.0.1";
  private static int port=6000;

  private boolean init=true;

  static byte[] cookie=null;
  private static byte[] cookie_hex=null;

  private static java.util.Hashtable faked_cookie_pool=new java.util.Hashtable();
  private static java.util.Hashtable faked_cookie_hex_pool=new java.util.Hashtable();

  private static byte[] table={0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,
                               0x61,0x62,0x63,0x64,0x65,0x66};

  private Socket socket = null;

  static int revtable(byte foo){
    for(int i=0; i<table.length; i++){
      if(table[i]==foo)return i;
    }
    return 0;
  }
  static void setCookie(String foo){
    cookie_hex=Util.str2byte(foo);
    cookie=new byte[16];
    for(int i=0; i<16; i++){
        cookie[i]=(byte)(((revtable(cookie_hex[i*2])<<4)&0xf0) |
                         ((revtable(cookie_hex[i*2+1]))&0xf));
    }
  }
  static void setHost(String foo){ host=foo; }
  static void setPort(int foo){ port=foo; }
  static byte[] getFakedCookie(Session session){
    synchronized(faked_cookie_hex_pool){
      byte[] foo=(byte[])faked_cookie_hex_pool.get(session);
      if(foo==null){
        Random random=Session.random;
        foo=new byte[16];
        synchronized(random){
          random.fill(foo, 0, 16);
        }
/*
System.err.print("faked_cookie: ");
for(int i=0; i<foo.length; i++){
    System.err.print(Integer.toHexString(foo[i]&0xff)+":");
}
System.err.println("");
*/
        faked_cookie_pool.put(session, foo);
        byte[] bar=new byte[32];
        for(int i=0; i<16; i++){
          bar[2*i]=table[(foo[i]>>>4)&0xf];
          bar[2*i+1]=table[(foo[i])&0xf];
        }
        faked_cookie_hex_pool.put(session, bar);
        foo=bar;
      }
      return foo;
    }
  }

  static void removeFakedCookie(Session session){
    synchronized(faked_cookie_hex_pool){
      faked_cookie_hex_pool.remove(session);
      faked_cookie_pool.remove(session);
    }
  }

  ChannelX11(){
    super();

    setLocalWindowSizeMax(LOCAL_WINDOW_SIZE_MAX);
    setLocalWindowSize(LOCAL_WINDOW_SIZE_MAX);
    setLocalPacketSize(LOCAL_MAXIMUM_PACKET_SIZE);

    type=Util.str2byte("x11");

    connected=true;
    /*
    try{
      socket=Util.createSocket(host, port, TIMEOUT);
      socket.setTcpNoDelay(true);
      io=new IO();
      io.setInputStream(socket.getInputStream());
      io.setOutputStream(socket.getOutputStream());
    }
    catch(Exception e){
      //System.err.println(e);
    }
    */
  }

  public void run(){

    try{
      socket=Util.createSocket(host, port, TIMEOUT);
      socket.setTcpNoDelay(true);
      io=new IO();
      io.setInputStream(socket.getInputStream());
      io.setOutputStream(socket.getOutputStream());
      sendOpenConfirmation();
    }
    catch(Exception e){
      sendOpenFailure(SSH_OPEN_ADMINISTRATIVELY_PROHIBITED);
      close=true;
      disconnect();
      return;
    }

    thread=Thread.currentThread();
    Buffer buf=new Buffer(rmpsize);
    Packet packet=new Packet(buf);
    int i=0;
    try{
      while(thread!=null &&
            io!=null &&
            io.in!=null){
        i=io.in.read(buf.buffer,
                     14,
                     buf.buffer.length-14-Session.buffer_margin);
        if(i<=0){
          eof();
          break;
        }
        if(close)break;
        packet.reset();
        buf.putByte((byte)Session.SSH_MSG_CHANNEL_DATA);
        buf.putInt(recipient);
        buf.putInt(i);
        buf.skip(i);
        getSession().write(packet, this, i);
      }
    }
    catch(Exception e){
      //System.err.println(e);
    }
    disconnect();
  }

  private byte[] cache=new byte[0];
  private byte[] addCache(byte[] foo, int s, int l){
    byte[] bar=new byte[cache.length+l];
    System.arraycopy(foo, s, bar, cache.length, l);
    if(cache.length>0)
      System.arraycopy(cache, 0, bar, 0, cache.length);
    cache=bar;
    return cache;
  }

  void write(byte[] foo, int s, int l) throws java.io.IOException {
    //if(eof_local)return;

    if(init){

      Session _session=null;
      try{
        _session=getSession();
      }
      catch(JSchException e){
        throw new java.io.IOException(e.toString());
      }

      foo=addCache(foo, s, l);
      s=0;
      l=foo.length;

      if(l<9)
        return;

      int plen=(foo[s+6]&0xff)*256+(foo[s+7]&0xff);
      int dlen=(foo[s+8]&0xff)*256+(foo[s+9]&0xff);

      if((foo[s]&0xff)==0x42){
      }
      else if((foo[s]&0xff)==0x6c){
         plen=((plen>>>8)&0xff)|((plen<<8)&0xff00);
         dlen=((dlen>>>8)&0xff)|((dlen<<8)&0xff00);
      }
      else{
          // ??
      }

      if(l<12+plen+((-plen)&3)+dlen)
        return;

      byte[] bar=new byte[dlen];
      System.arraycopy(foo, s+12+plen+((-plen)&3), bar, 0, dlen);
      byte[] faked_cookie=null;

      synchronized(faked_cookie_pool){
        faked_cookie=(byte[])faked_cookie_pool.get(_session);
      }

      /*
System.err.print("faked_cookie: ");
for(int i=0; i<faked_cookie.length; i++){
    System.err.print(Integer.toHexString(faked_cookie[i]&0xff)+":");
}
System.err.println("");
System.err.print("bar: ");
for(int i=0; i<bar.length; i++){
    System.err.print(Integer.toHexString(bar[i]&0xff)+":");
}
System.err.println("");
      */

      if(equals(bar, faked_cookie)){
        if(cookie!=null)
          System.arraycopy(cookie, 0, foo, s+12+plen+((-plen)&3), dlen);
      }
      else{
          //System.err.println("wrong cookie");
          thread=null;
          eof();
          io.close();
          disconnect();
      }
      init=false;
      io.put(foo, s, l);
      cache=null;
      return;
    }
    io.put(foo, s, l);
  }

  private static boolean equals(byte[] foo, byte[] bar){
    if(foo.length!=bar.length)return false;
    for(int i=0; i<foo.length; i++){
      if(foo[i]!=bar[i])return false;
    }
    return true;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Cipher{
  static int ENCRYPT_MODE=0;
  static int DECRYPT_MODE=1;
  int getIVSize();
  int getBlockSize();
  void init(int mode, byte[] key, byte[] iv) throws Exception;
  void update(byte[] foo, int s1, int len, byte[] bar, int s2) throws Exception;
  boolean isCBC();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class CipherNone implements Cipher{
  private static final int ivsize=8;
  private static final int bsize=16;
  public int getIVSize(){return ivsize;}
  public int getBlockSize(){return bsize;}
  public void init(int mode, byte[] key, byte[] iv) throws Exception{
  }
  public void update(byte[] foo, int s1, int len, byte[] bar, int s2) throws Exception{
  }
  public boolean isCBC(){return false; }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Compression{
  static public final int INFLATER=0;
  static public final int DEFLATER=1;
  void init(int type, int level);
  byte[] compress(byte[] buf, int start, int[] len);
  byte[] uncompress(byte[] buf, int start, int[] len);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2013-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface ConfigRepository {

  public Config getConfig(String host);

  public interface Config {
    public String getHostname();
    public String getUser();
    public int getPort();
    public String getValue(String key);
    public String[] getValues(String key);
  }

  static final Config defaultConfig = new Config() {
    public String getHostname() {return null;}
    public String getUser() {return null;}
    public int getPort() {return -1;}
    public String getValue(String key) {return null;}
    public String[] getValues(String key) {return null;}
  };

  static final ConfigRepository nullConfig = new ConfigRepository(){
    public Config getConfig(String host) { return defaultConfig; }
  };
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHEC256 extends DHECN {
  public DHEC256(){
    sha_name="sha-256";
    key_size=256;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHEC384 extends DHECN {
  public DHEC384(){
    sha_name="sha-384";
    key_size=384;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHEC521 extends DHECN {
  public DHEC521(){
    sha_name="sha-512";
    key_size=521;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



abstract class DHECN extends KeyExchange{

  private static final int SSH_MSG_KEX_ECDH_INIT =                 30;
  private static final int SSH_MSG_KEX_ECDH_REPLY=                 31;
  private int state;

  byte[] Q_C;

  byte[] V_S;
  byte[] V_C;
  byte[] I_S;
  byte[] I_C;

  byte[] e;

  private Buffer buf;
  private Packet packet;

  private ECDH ecdh;

  protected String sha_name;
  protected int key_size;

  public void init(Session session,
                   byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception{
    this.session=session;
    this.V_S=V_S;
    this.V_C=V_C;
    this.I_S=I_S;
    this.I_C=I_C;

    try{
      Class c=Class.forName(session.getConfig(sha_name));
      sha=(HASH)(c.newInstance());
      sha.init();
    }
    catch(Exception e){
      System.err.println(e);
    }

    buf=new Buffer();
    packet=new Packet(buf);

    packet.reset();
    buf.putByte((byte)SSH_MSG_KEX_ECDH_INIT);

    try{
      Class c=Class.forName(session.getConfig("ecdh-sha2-nistp"));
      ecdh=(ECDH)(c.newInstance());
      ecdh.init(key_size);

      Q_C = ecdh.getQ();
      buf.putString(Q_C);
    }
    catch(Exception e){
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }

    if(V_S==null){  // This is a really ugly hack for Session.checkKexes ;-(
      return;
    }

    session.write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_KEX_ECDH_INIT sent");
      JSch.getLogger().log(Logger.INFO,
                           "expecting SSH_MSG_KEX_ECDH_REPLY");
    }

    state=SSH_MSG_KEX_ECDH_REPLY;
  }

  public boolean next(Buffer _buf) throws Exception{
    int i,j;
    switch(state){
    case SSH_MSG_KEX_ECDH_REPLY:
      // The server responds with:
      // byte     SSH_MSG_KEX_ECDH_REPLY
      // string   K_S, server's public host key
      // string   Q_S, server's ephemeral public key octet string
      // string   the signature on the exchange hash
      j=_buf.getInt();
      j=_buf.getByte();
      j=_buf.getByte();
      if(j!=31){
        System.err.println("type: must be 31 "+j);
        return false;
      }

      K_S=_buf.getString();

      byte[] Q_S=_buf.getString();

      byte[][] r_s = KeyPairECDSA.fromPoint(Q_S);

      // RFC 5656,
      // 4. ECDH Key Exchange
      //   All elliptic curve public keys MUST be validated after they are
      //   received.  An example of a validation algorithm can be found in
      //   Section 3.2.2 of [SEC1].  If a key fails validation,
      //   the key exchange MUST fail.
      if(!ecdh.validate(r_s[0], r_s[1])){
        return false;
      }

      K = ecdh.getSecret(r_s[0], r_s[1]);
      K=normalize(K);

      byte[] sig_of_H=_buf.getString();

      //The hash H is computed as the HASH hash of the concatenation of the
      //following:
      // string   V_C, client's identification string (CR and LF excluded)
      // string   V_S, server's identification string (CR and LF excluded)
      // string   I_C, payload of the client's SSH_MSG_KEXINIT
      // string   I_S, payload of the server's SSH_MSG_KEXINIT
      // string   K_S, server's public host key
      // string   Q_C, client's ephemeral public key octet string
      // string   Q_S, server's ephemeral public key octet string
      // mpint    K,   shared secret

      // This value is called the exchange hash, and it is used to authenti-
      // cate the key exchange.
      buf.reset();
      buf.putString(V_C); buf.putString(V_S);
      buf.putString(I_C); buf.putString(I_S);
      buf.putString(K_S);
      buf.putString(Q_C); buf.putString(Q_S);
      buf.putMPInt(K);
      byte[] foo=new byte[buf.getLength()];
      buf.getByte(foo);

      sha.update(foo, 0, foo.length);
      H=sha.digest();

      i=0;
      j=0;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      String alg=Util.byte2str(K_S, i, j);
      i+=j;

      boolean result = verify(alg, K_S, i, sig_of_H);

      state=STATE_END;
      return result;
    }
    return false;
  }

  public int getState(){return state; }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHG14 extends KeyExchange{

  static final byte[] g={ 2 };
  static final byte[] p={
(byte)0x00,
(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
(byte)0xC9,(byte)0x0F,(byte)0xDA,(byte)0xA2,(byte)0x21,(byte)0x68,(byte)0xC2,(byte)0x34,
(byte)0xC4,(byte)0xC6,(byte)0x62,(byte)0x8B,(byte)0x80,(byte)0xDC,(byte)0x1C,(byte)0xD1,
(byte)0x29,(byte)0x02,(byte)0x4E,(byte)0x08,(byte)0x8A,(byte)0x67,(byte)0xCC,(byte)0x74,
(byte)0x02,(byte)0x0B,(byte)0xBE,(byte)0xA6,(byte)0x3B,(byte)0x13,(byte)0x9B,(byte)0x22,
(byte)0x51,(byte)0x4A,(byte)0x08,(byte)0x79,(byte)0x8E,(byte)0x34,(byte)0x04,(byte)0xDD,
(byte)0xEF,(byte)0x95,(byte)0x19,(byte)0xB3,(byte)0xCD,(byte)0x3A,(byte)0x43,(byte)0x1B,
(byte)0x30,(byte)0x2B,(byte)0x0A,(byte)0x6D,(byte)0xF2,(byte)0x5F,(byte)0x14,(byte)0x37,
(byte)0x4F,(byte)0xE1,(byte)0x35,(byte)0x6D,(byte)0x6D,(byte)0x51,(byte)0xC2,(byte)0x45,
(byte)0xE4,(byte)0x85,(byte)0xB5,(byte)0x76,(byte)0x62,(byte)0x5E,(byte)0x7E,(byte)0xC6,
(byte)0xF4,(byte)0x4C,(byte)0x42,(byte)0xE9,(byte)0xA6,(byte)0x37,(byte)0xED,(byte)0x6B,
(byte)0x0B,(byte)0xFF,(byte)0x5C,(byte)0xB6,(byte)0xF4,(byte)0x06,(byte)0xB7,(byte)0xED,
(byte)0xEE,(byte)0x38,(byte)0x6B,(byte)0xFB,(byte)0x5A,(byte)0x89,(byte)0x9F,(byte)0xA5,
(byte)0xAE,(byte)0x9F,(byte)0x24,(byte)0x11,(byte)0x7C,(byte)0x4B,(byte)0x1F,(byte)0xE6,
(byte)0x49,(byte)0x28,(byte)0x66,(byte)0x51,(byte)0xEC,(byte)0xE4,(byte)0x5B,(byte)0x3D,
(byte)0xC2,(byte)0x00,(byte)0x7C,(byte)0xB8,(byte)0xA1,(byte)0x63,(byte)0xBF,(byte)0x05,
(byte)0x98,(byte)0xDA,(byte)0x48,(byte)0x36,(byte)0x1C,(byte)0x55,(byte)0xD3,(byte)0x9A,
(byte)0x69,(byte)0x16,(byte)0x3F,(byte)0xA8,(byte)0xFD,(byte)0x24,(byte)0xCF,(byte)0x5F,
(byte)0x83,(byte)0x65,(byte)0x5D,(byte)0x23,(byte)0xDC,(byte)0xA3,(byte)0xAD,(byte)0x96,
(byte)0x1C,(byte)0x62,(byte)0xF3,(byte)0x56,(byte)0x20,(byte)0x85,(byte)0x52,(byte)0xBB,
(byte)0x9E,(byte)0xD5,(byte)0x29,(byte)0x07,(byte)0x70,(byte)0x96,(byte)0x96,(byte)0x6D,
(byte)0x67,(byte)0x0C,(byte)0x35,(byte)0x4E,(byte)0x4A,(byte)0xBC,(byte)0x98,(byte)0x04,
(byte)0xF1,(byte)0x74,(byte)0x6C,(byte)0x08,(byte)0xCA,(byte)0x18,(byte)0x21,(byte)0x7C,
(byte)0x32,(byte)0x90,(byte)0x5E,(byte)0x46,(byte)0x2E,(byte)0x36,(byte)0xCE,(byte)0x3B,
(byte)0xE3,(byte)0x9E,(byte)0x77,(byte)0x2C,(byte)0x18,(byte)0x0E,(byte)0x86,(byte)0x03,
(byte)0x9B,(byte)0x27,(byte)0x83,(byte)0xA2,(byte)0xEC,(byte)0x07,(byte)0xA2,(byte)0x8F,
(byte)0xB5,(byte)0xC5,(byte)0x5D,(byte)0xF0,(byte)0x6F,(byte)0x4C,(byte)0x52,(byte)0xC9,
(byte)0xDE,(byte)0x2B,(byte)0xCB,(byte)0xF6,(byte)0x95,(byte)0x58,(byte)0x17,(byte)0x18,
(byte)0x39,(byte)0x95,(byte)0x49,(byte)0x7C,(byte)0xEA,(byte)0x95,(byte)0x6A,(byte)0xE5,
(byte)0x15,(byte)0xD2,(byte)0x26,(byte)0x18,(byte)0x98,(byte)0xFA,(byte)0x05,(byte)0x10,
(byte)0x15,(byte)0x72,(byte)0x8E,(byte)0x5A,(byte)0x8A,(byte)0xAC,(byte)0xAA,(byte)0x68,
(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF
};

  private static final int SSH_MSG_KEXDH_INIT=                     30;
  private static final int SSH_MSG_KEXDH_REPLY=                    31;

  private int state;

  DH dh;

  byte[] V_S;
  byte[] V_C;
  byte[] I_S;
  byte[] I_C;

  byte[] e;

  private Buffer buf;
  private Packet packet;

  public void init(Session session,
                   byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception{
    this.session=session;
    this.V_S=V_S;
    this.V_C=V_C;
    this.I_S=I_S;
    this.I_C=I_C;

    try{
      Class c=Class.forName(session.getConfig("sha-1"));
      sha=(HASH)(c.newInstance());
      sha.init();
    }
    catch(Exception e){
      System.err.println(e);
    }

    buf=new Buffer();
    packet=new Packet(buf);

    try{
      Class c=Class.forName(session.getConfig("dh"));
      dh=(DH)(c.newInstance());
      dh.init();
    }
    catch(Exception e){
      //System.err.println(e);
      throw e;
    }

    dh.setP(p);
    dh.setG(g);
    // The client responds with:
    // byte  SSH_MSG_KEXDH_INIT(30)
    // mpint e <- g^x mod p
    //         x is a random number (1 < x < (p-1)/2)

    e=dh.getE();
    packet.reset();
    buf.putByte((byte)SSH_MSG_KEXDH_INIT);
    buf.putMPInt(e);

    if(V_S==null){  // This is a really ugly hack for Session.checkKexes ;-(
      return;
    }

    session.write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_KEXDH_INIT sent");
      JSch.getLogger().log(Logger.INFO,
                           "expecting SSH_MSG_KEXDH_REPLY");
    }

    state=SSH_MSG_KEXDH_REPLY;
  }

  public boolean next(Buffer _buf) throws Exception{
    int i,j;

    switch(state){
    case SSH_MSG_KEXDH_REPLY:
      // The server responds with:
      // byte      SSH_MSG_KEXDH_REPLY(31)
      // string    server public host key and certificates (K_S)
      // mpint     f
      // string    signature of H
      j=_buf.getInt();
      j=_buf.getByte();
      j=_buf.getByte();
      if(j!=31){
        System.err.println("type: must be 31 "+j);
        return false;
      }

      K_S=_buf.getString();

      byte[] f=_buf.getMPInt();
      byte[] sig_of_H=_buf.getString();

      dh.setF(f);

      dh.checkRange();

      K=normalize(dh.getK());

      //The hash H is computed as the HASH hash of the concatenation of the
      //following:
      // string    V_C, the client's version string (CR and NL excluded)
      // string    V_S, the server's version string (CR and NL excluded)
      // string    I_C, the payload of the client's SSH_MSG_KEXINIT
      // string    I_S, the payload of the server's SSH_MSG_KEXINIT
      // string    K_S, the host key
      // mpint     e, exchange value sent by the client
      // mpint     f, exchange value sent by the server
      // mpint     K, the shared secret
      // This value is called the exchange hash, and it is used to authenti-
      // cate the key exchange.
      buf.reset();
      buf.putString(V_C); buf.putString(V_S);
      buf.putString(I_C); buf.putString(I_S);
      buf.putString(K_S);
      buf.putMPInt(e); buf.putMPInt(f);
      buf.putMPInt(K);
      byte[] foo=new byte[buf.getLength()];
      buf.getByte(foo);
      sha.update(foo, 0, foo.length);
      H=sha.digest();
      //System.err.print("H -> "); //dump(H, 0, H.length);

      i=0;
      j=0;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      String alg=Util.byte2str(K_S, i, j);
      i+=j;

      boolean result = verify(alg, K_S, i, sig_of_H);

      state=STATE_END;
      return result;
    }
    return false;
  }

  public int getState(){return state; }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHG1 extends KeyExchange{

  static final byte[] g={ 2 };
  static final byte[] p={
(byte)0x00,
(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
(byte)0xC9,(byte)0x0F,(byte)0xDA,(byte)0xA2,(byte)0x21,(byte)0x68,(byte)0xC2,(byte)0x34,
(byte)0xC4,(byte)0xC6,(byte)0x62,(byte)0x8B,(byte)0x80,(byte)0xDC,(byte)0x1C,(byte)0xD1,
(byte)0x29,(byte)0x02,(byte)0x4E,(byte)0x08,(byte)0x8A,(byte)0x67,(byte)0xCC,(byte)0x74,
(byte)0x02,(byte)0x0B,(byte)0xBE,(byte)0xA6,(byte)0x3B,(byte)0x13,(byte)0x9B,(byte)0x22,
(byte)0x51,(byte)0x4A,(byte)0x08,(byte)0x79,(byte)0x8E,(byte)0x34,(byte)0x04,(byte)0xDD,
(byte)0xEF,(byte)0x95,(byte)0x19,(byte)0xB3,(byte)0xCD,(byte)0x3A,(byte)0x43,(byte)0x1B,
(byte)0x30,(byte)0x2B,(byte)0x0A,(byte)0x6D,(byte)0xF2,(byte)0x5F,(byte)0x14,(byte)0x37,
(byte)0x4F,(byte)0xE1,(byte)0x35,(byte)0x6D,(byte)0x6D,(byte)0x51,(byte)0xC2,(byte)0x45,
(byte)0xE4,(byte)0x85,(byte)0xB5,(byte)0x76,(byte)0x62,(byte)0x5E,(byte)0x7E,(byte)0xC6,
(byte)0xF4,(byte)0x4C,(byte)0x42,(byte)0xE9,(byte)0xA6,(byte)0x37,(byte)0xED,(byte)0x6B,
(byte)0x0B,(byte)0xFF,(byte)0x5C,(byte)0xB6,(byte)0xF4,(byte)0x06,(byte)0xB7,(byte)0xED,
(byte)0xEE,(byte)0x38,(byte)0x6B,(byte)0xFB,(byte)0x5A,(byte)0x89,(byte)0x9F,(byte)0xA5,
(byte)0xAE,(byte)0x9F,(byte)0x24,(byte)0x11,(byte)0x7C,(byte)0x4B,(byte)0x1F,(byte)0xE6,
(byte)0x49,(byte)0x28,(byte)0x66,(byte)0x51,(byte)0xEC,(byte)0xE6,(byte)0x53,(byte)0x81,
(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF
};

  private static final int SSH_MSG_KEXDH_INIT=                     30;
  private static final int SSH_MSG_KEXDH_REPLY=                    31;

  private int state;

  DH dh;

  byte[] V_S;
  byte[] V_C;
  byte[] I_S;
  byte[] I_C;

  byte[] e;

  private Buffer buf;
  private Packet packet;

  public void init(Session session,
                   byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception{
    this.session=session;
    this.V_S=V_S;
    this.V_C=V_C;
    this.I_S=I_S;
    this.I_C=I_C;

    try{
      Class c=Class.forName(session.getConfig("sha-1"));
      sha=(HASH)(c.newInstance());
      sha.init();
    }
    catch(Exception e){
      System.err.println(e);
    }

    buf=new Buffer();
    packet=new Packet(buf);

    try{
      Class c=Class.forName(session.getConfig("dh"));
      dh=(DH)(c.newInstance());
      dh.init();
    }
    catch(Exception e){
      //System.err.println(e);
      throw e;
    }

    dh.setP(p);
    dh.setG(g);

    // The client responds with:
    // byte  SSH_MSG_KEXDH_INIT(30)
    // mpint e <- g^x mod p
    //         x is a random number (1 < x < (p-1)/2)

    e=dh.getE();

    packet.reset();
    buf.putByte((byte)SSH_MSG_KEXDH_INIT);
    buf.putMPInt(e);
    session.write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_KEXDH_INIT sent");
      JSch.getLogger().log(Logger.INFO,
                           "expecting SSH_MSG_KEXDH_REPLY");
    }

    state=SSH_MSG_KEXDH_REPLY;
  }

  public boolean next(Buffer _buf) throws Exception{
    int i,j;

    switch(state){
    case SSH_MSG_KEXDH_REPLY:
      // The server responds with:
      // byte      SSH_MSG_KEXDH_REPLY(31)
      // string    server public host key and certificates (K_S)
      // mpint     f
      // string    signature of H
      j=_buf.getInt();
      j=_buf.getByte();
      j=_buf.getByte();
      if(j!=31){
        System.err.println("type: must be 31 "+j);
        return false;
      }

      K_S=_buf.getString();

      byte[] f=_buf.getMPInt();
      byte[] sig_of_H=_buf.getString();

      dh.setF(f);

      dh.checkRange();

      K=normalize(dh.getK());

      //The hash H is computed as the HASH hash of the concatenation of the
      //following:
      // string    V_C, the client's version string (CR and NL excluded)
      // string    V_S, the server's version string (CR and NL excluded)
      // string    I_C, the payload of the client's SSH_MSG_KEXINIT
      // string    I_S, the payload of the server's SSH_MSG_KEXINIT
      // string    K_S, the host key
      // mpint     e, exchange value sent by the client
      // mpint     f, exchange value sent by the server
      // mpint     K, the shared secret
      // This value is called the exchange hash, and it is used to authenti-
      // cate the key exchange.
      buf.reset();
      buf.putString(V_C); buf.putString(V_S);
      buf.putString(I_C); buf.putString(I_S);
      buf.putString(K_S);
      buf.putMPInt(e); buf.putMPInt(f);
      buf.putMPInt(K);
      byte[] foo=new byte[buf.getLength()];
      buf.getByte(foo);
      sha.update(foo, 0, foo.length);
      H=sha.digest();
      //System.err.print("H -> "); //dump(H, 0, H.length);

      i=0;
      j=0;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      String alg=Util.byte2str(K_S, i, j);
      i+=j;

      boolean result = verify(alg, K_S, i, sig_of_H);

      state=STATE_END;
      return result;
    }
    return false;
  }

  public int getState(){return state; }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHGEX256 extends DHGEX {
  DHGEX256(){
    hash="sha-256";
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class DHGEX extends KeyExchange{

  private static final int SSH_MSG_KEX_DH_GEX_GROUP=               31;
  private static final int SSH_MSG_KEX_DH_GEX_INIT=                32;
  private static final int SSH_MSG_KEX_DH_GEX_REPLY=               33;
  private static final int SSH_MSG_KEX_DH_GEX_REQUEST=             34;

  static int min=1024;
  static int preferred=1024;
  int max=1024;

  private int state;

  DH dh;

  byte[] V_S;
  byte[] V_C;
  byte[] I_S;
  byte[] I_C;

  private Buffer buf;
  private Packet packet;

  private byte[] p;
  private byte[] g;
  private byte[] e;

  protected String hash="sha-1";

  public void init(Session session,
                   byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception{
    this.session=session;
    this.V_S=V_S;
    this.V_C=V_C;
    this.I_S=I_S;
    this.I_C=I_C;

    try{
      Class c=Class.forName(session.getConfig(hash));
      sha=(HASH)(c.newInstance());
      sha.init();
    }
    catch(Exception e){
      System.err.println(e);
    }

    buf=new Buffer();
    packet=new Packet(buf);

    try{
      Class c=Class.forName(session.getConfig("dh"));
      // Since JDK8, SunJCE has lifted the keysize restrictions
      // from 1024 to 2048 for DH.
      preferred = max = check2048(c, max);
      dh=(DH)(c.newInstance());
      dh.init();
    }
    catch(Exception e){
      throw e;
    }

    packet.reset();
    buf.putByte((byte)SSH_MSG_KEX_DH_GEX_REQUEST);
    buf.putInt(min);
    buf.putInt(preferred);
    buf.putInt(max);
    session.write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_KEX_DH_GEX_REQUEST("+min+"<"+preferred+"<"+max+") sent");
      JSch.getLogger().log(Logger.INFO,
                           "expecting SSH_MSG_KEX_DH_GEX_GROUP");
    }

    state=SSH_MSG_KEX_DH_GEX_GROUP;
  }

  public boolean next(Buffer _buf) throws Exception{
    int i,j;
    switch(state){
    case SSH_MSG_KEX_DH_GEX_GROUP:
      // byte  SSH_MSG_KEX_DH_GEX_GROUP(31)
      // mpint p, safe prime
      // mpint g, generator for subgroup in GF (p)
      _buf.getInt();
      _buf.getByte();
      j=_buf.getByte();
      if(j!=SSH_MSG_KEX_DH_GEX_GROUP){
        System.err.println("type: must be SSH_MSG_KEX_DH_GEX_GROUP "+j);
        return false;
      }

      p=_buf.getMPInt();
      g=_buf.getMPInt();

      dh.setP(p);
      dh.setG(g);
      // The client responds with:
      // byte  SSH_MSG_KEX_DH_GEX_INIT(32)
      // mpint e <- g^x mod p
      //         x is a random number (1 < x < (p-1)/2)

      e=dh.getE();

      packet.reset();
      buf.putByte((byte)SSH_MSG_KEX_DH_GEX_INIT);
      buf.putMPInt(e);
      session.write(packet);

      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "SSH_MSG_KEX_DH_GEX_INIT sent");
        JSch.getLogger().log(Logger.INFO,
                             "expecting SSH_MSG_KEX_DH_GEX_REPLY");
      }

      state=SSH_MSG_KEX_DH_GEX_REPLY;
      return true;
      //break;

    case SSH_MSG_KEX_DH_GEX_REPLY:
      // The server responds with:
      // byte      SSH_MSG_KEX_DH_GEX_REPLY(33)
      // string    server public host key and certificates (K_S)
      // mpint     f
      // string    signature of H
      j=_buf.getInt();
      j=_buf.getByte();
      j=_buf.getByte();
      if(j!=SSH_MSG_KEX_DH_GEX_REPLY){
        System.err.println("type: must be SSH_MSG_KEX_DH_GEX_REPLY "+j);
        return false;
      }

      K_S=_buf.getString();

      byte[] f=_buf.getMPInt();
      byte[] sig_of_H=_buf.getString();

      dh.setF(f);

      dh.checkRange();

      K=normalize(dh.getK());

      //The hash H is computed as the HASH hash of the concatenation of the
      //following:
      // string    V_C, the client's version string (CR and NL excluded)
      // string    V_S, the server's version string (CR and NL excluded)
      // string    I_C, the payload of the client's SSH_MSG_KEXINIT
      // string    I_S, the payload of the server's SSH_MSG_KEXINIT
      // string    K_S, the host key
      // uint32    min, minimal size in bits of an acceptable group
      // uint32   n, preferred size in bits of the group the server should send
      // uint32    max, maximal size in bits of an acceptable group
      // mpint     p, safe prime
      // mpint     g, generator for subgroup
      // mpint     e, exchange value sent by the client
      // mpint     f, exchange value sent by the server
      // mpint     K, the shared secret
      // This value is called the exchange hash, and it is used to authenti-
      // cate the key exchange.

      buf.reset();
      buf.putString(V_C); buf.putString(V_S);
      buf.putString(I_C); buf.putString(I_S);
      buf.putString(K_S);
      buf.putInt(min); buf.putInt(preferred); buf.putInt(max);
      buf.putMPInt(p); buf.putMPInt(g); buf.putMPInt(e); buf.putMPInt(f);
      buf.putMPInt(K);

      byte[] foo=new byte[buf.getLength()];
      buf.getByte(foo);
      sha.update(foo, 0, foo.length);

      H=sha.digest();

      // System.err.print("H -> "); dump(H, 0, H.length);

      i=0;
      j=0;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      String alg=Util.byte2str(K_S, i, j);
      i+=j;

      boolean result = verify(alg, K_S, i, sig_of_H);

      state=STATE_END;
      return result;
    }
    return false;
  }

  public int getState(){return state; }

  protected int check2048(Class c, int _max) throws Exception {
    DH dh=(DH)(c.newInstance());
    dh.init();
    byte[] foo = new byte[257];
    foo[1]=(byte)0xdd;
    foo[256]=0x73;
    dh.setP(foo);
    byte[] bar = {(byte)0x02};
    dh.setG(bar);
    try {
      dh.getE();
      _max=2048;
    }
    catch(Exception e){ }
    return _max;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface DH{
  void init() throws Exception;
  void setP(byte[] p);
  void setG(byte[] g);
  byte[] getE() throws Exception;
  void setF(byte[] f);
  byte[] getK() throws Exception;

  // checkRange() will check if e and f are in [1,p-1]
  // as defined at https://tools.ietf.org/html/rfc4253#section-8
  void checkRange() throws Exception;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface ECDH {
  void init(int size) throws Exception;
  byte[] getSecret(byte[] r, byte[] s) throws Exception;
  byte[] getQ() throws Exception;
  boolean validate(byte[] r, byte[] s) throws Exception;
}


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



class Exec{
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
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface ForwardedTCPIPDaemon extends Runnable{
  void setChannel(ChannelForwardedTCPIP channel, InputStream in, OutputStream out);
  void setArg(Object[] arg);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2004-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface GSSContext{
  public void create(String user, String host) throws JSchException;
  public boolean isEstablished();
  public byte[] init(byte[] token, int s, int l) throws JSchException;
  public byte[] getMIC(byte[] message, int s, int l);
  public void dispose();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface HASH{
  void init() throws Exception;
  int getBlockSize();
  void update(byte[] foo, int start, int len) throws Exception;
  byte[] digest() throws Exception;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class HostKey{

  private static final byte[][] names = {
    Util.str2byte("ssh-dss"),
    Util.str2byte("ssh-rsa"),
    Util.str2byte("ecdsa-sha2-nistp256"),
    Util.str2byte("ecdsa-sha2-nistp384"),
    Util.str2byte("ecdsa-sha2-nistp521")
  };

  protected static final int GUESS=0;
  public static final int SSHDSS=1;
  public static final int SSHRSA=2;
  public static final int ECDSA256=3;
  public static final int ECDSA384=4;
  public static final int ECDSA521=5;
  static final int UNKNOWN=6;

  protected String marker;
  protected String host;
  protected int type;
  protected byte[] key;
  protected String comment;

  public HostKey(String host, byte[] key) throws JSchException {
    this(host, GUESS, key);
  }

  public HostKey(String host, int type, byte[] key) throws JSchException {
    this(host, type, key, null);
  }
  public HostKey(String host, int type, byte[] key, String comment) throws JSchException {
    this("", host, type, key, comment);
  }
  public HostKey(String marker, String host, int type, byte[] key, String comment) throws JSchException {
    this.marker=marker;
    this.host=host;
    if(type==GUESS){
      if(key[8]=='d'){ this.type=SSHDSS; }
      else if(key[8]=='r'){ this.type=SSHRSA; }
      else if(key[8]=='a' && key[20]=='2'){ this.type=ECDSA256; }
      else if(key[8]=='a' && key[20]=='3'){ this.type=ECDSA384; }
      else if(key[8]=='a' && key[20]=='5'){ this.type=ECDSA521; }
      else { throw new JSchException("invalid key type");}
    }
    else{
      this.type=type;
    }
    this.key=key;
    this.comment=comment;
  }

  public String getHost(){ return host; }
  public String getType(){
    if(type==SSHDSS ||
       type==SSHRSA ||
       type==ECDSA256 ||
       type==ECDSA384 ||
       type==ECDSA521){
      return Util.byte2str(names[type-1]);
    }
    return "UNKNOWN";
  }
  protected static int name2type(String name){
    for(int i = 0; i < names.length; i++){
      if(Util.byte2str(names[i]).equals(name)){
        return i + 1;
      }
    }
    return UNKNOWN;
  }
  public String getKey(){
    return Util.byte2str(Util.toBase64(key, 0, key.length));
  }
  public String getFingerPrint(JSch jsch){
    HASH hash=null;
    try{
      Class c=Class.forName(jsch.getConfig("md5"));
      hash=(HASH)(c.newInstance());
    }
    catch(Exception e){ System.err.println("getFingerPrint: "+e); }
    return Util.getFingerPrint(hash, key);
  }
  public String getComment(){ return comment; }
  public String getMarker(){ return marker; }

  boolean isMatched(String _host){
    return isIncluded(_host);
  }

  private boolean isIncluded(String _host){
    int i=0;
    String hosts=this.host;
    int hostslen=hosts.length();
    int hostlen=_host.length();
    int j;
    while(i<hostslen){
      j=hosts.indexOf(',', i);
      if(j==-1){
       if(hostlen!=hostslen-i) return false;
       return hosts.regionMatches(true, i, _host, 0, hostlen);
      }
      if(hostlen==(j-i)){
        if(hosts.regionMatches(true, i, _host, 0, hostlen)) return true;
      }
      i=j+1;
    }
    return false;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2004-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface HostKeyRepository{
  final int OK=0;
  final int NOT_INCLUDED=1;
  final int CHANGED=2;

  /**
   * Checks if <code>host</code> is included with the <code>key</code>.
   *
   * @return #NOT_INCLUDED, #OK or #CHANGED
   * @see #NOT_INCLUDED
   * @see #OK
   * @see #CHANGED
   */
  int check(String host, byte[] key);

  /**
   * Adds a host key <code>hostkey</code>
   *
   * @param hostkey a host key to be added
   * @param ui a user interface for showing messages or promping inputs.
   * @see UserInfo
   */
  void add(HostKey hostkey, UserInfo ui);

  /**
   * Removes a host key if there exists mached key with
   * <code>host</code>, <code>type</code>.
   *
   * @see #remove(String host, String type, byte[] key)
   */
  void remove(String host, String type);

  /**
   * Removes a host key if there exists a matched key with
   * <code>host</code>, <code>type</code> and <code>key</code>.
   */
  void remove(String host, String type, byte[] key);

  /**
   * Returns id of this repository.
   *
   * @return identity in String
   */
  String getKnownHostsRepositoryID();

  /**
   * Retuns a list for host keys managed in this repository.
   *
   * @see #getHostKey(String host, String type)
   */
  HostKey[] getHostKey();

  /**
   * Retuns a list for host keys managed in this repository.
   *
   * @param host a hostname used in searching host keys.
   *        If <code>null</code> is given, every host key will be listed.
   * @param type a key type used in searching host keys,
   *        and it should be "ssh-dss" or "ssh-rsa".
   *        If <code>null</code> is given, a key type type will not be ignored.
   */
  HostKey[] getHostKey(String host, String type);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class IdentityFile implements Identity{
  private JSch jsch;
  private KeyPair kpair;
  private String identity;

  static IdentityFile newInstance(String prvfile, String pubfile, JSch jsch) throws JSchException{
    KeyPair kpair = KeyPair.load(jsch, prvfile, pubfile);
    return new IdentityFile(jsch, prvfile, kpair);
  }

  static IdentityFile newInstance(String name, byte[] prvkey, byte[] pubkey, JSch jsch) throws JSchException{

    KeyPair kpair = KeyPair.load(jsch, prvkey, pubkey);
    return new IdentityFile(jsch, name, kpair);
  }

  private IdentityFile(JSch jsch, String name, KeyPair kpair) throws JSchException{
    this.jsch = jsch;
    this.identity = name;
    this.kpair = kpair;
  }

  /**
   * Decrypts this identity with the specified pass-phrase.
   * @param passphrase the pass-phrase for this identity.
   * @return <tt>true</tt> if the decryption is succeeded
   * or this identity is not cyphered.
   */
  public boolean setPassphrase(byte[] passphrase) throws JSchException{
    return kpair.decrypt(passphrase);
  }

  /**
   * Returns the public-key blob.
   * @return the public-key blob
   */
  public byte[] getPublicKeyBlob(){
    return kpair.getPublicKeyBlob();
  }

  /**
   * Signs on data with this identity, and returns the result.
   * @param data data to be signed
   * @return the signature
   */
  public byte[] getSignature(byte[] data){
    return kpair.getSignature(data);
  }

  /**
   * @deprecated This method should not be invoked.
   * @see #setPassphrase(byte[] passphrase)
   */
  public boolean decrypt(){
    throw new RuntimeException("not implemented");
  }

  /**
   * Returns the name of the key algorithm.
   * @return "ssh-rsa" or "ssh-dss"
   */
  public String getAlgName(){
    return new String(kpair.getKeyTypeName());
  }

  /**
   * Returns the name of this identity.
   * It will be useful to identify this object in the {@link IdentityRepository}.
   */
  public String getName(){
    return identity;
  }

  /**
   * Returns <tt>true</tt> if this identity is cyphered.
   * @return <tt>true</tt> if this identity is cyphered.
   */
  public boolean isEncrypted(){
    return kpair.isEncrypted();
  }

  /**
   * Disposes internally allocated data, like byte array for the private key.
   */
  public void clear(){
    kpair.dispose();
    kpair = null;
  }

  /**
   * Returns an instance of {@link KeyPair} used in this {@link Identity}.
   * @return an instance of {@link KeyPair} used in this {@link Identity}.
   */
  public KeyPair getKeyPair(){
    return kpair;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Identity{

  /**
   * Decrypts this identity with the specified pass-phrase.
   * @param passphrase the pass-phrase for this identity.
   * @return <tt>true</tt> if the decryption is succeeded
   * or this identity is not cyphered.
   */
  public boolean setPassphrase(byte[] passphrase) throws JSchException;

  /**
   * Returns the public-key blob.
   * @return the public-key blob
   */
  public byte[] getPublicKeyBlob();

  /**
   * Signs on data with this identity, and returns the result.
   * @param data data to be signed
   * @return the signature
   */
  public byte[] getSignature(byte[] data);

  /**
   * @deprecated The decryption should be done automatically in #setPassphase(byte[] passphrase)
   * @see #setPassphrase(byte[] passphrase)
   */
  public boolean decrypt();

  /**
   * Returns the name of the key algorithm.
   * @return "ssh-rsa" or "ssh-dss"
   */
  public String getAlgName();

  /**
   * Returns the name of this identity.
   * It will be useful to identify this object in the {@link IdentityRepository}.
   */
  public String getName();

  /**
   * Returns <tt>true</tt> if this identity is cyphered.
   * @return <tt>true</tt> if this identity is cyphered.
   */
  public boolean isEncrypted();

  /**
   * Disposes internally allocated data, like byte array for the private key.
   */
  public void clear();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2012-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




interface IdentityRepository {
  public static final int UNAVAILABLE=0;
  public static final int NOTRUNNING=1;
  public static final int RUNNING=2;
  public String getName();
  public int getStatus();
  public Vector getIdentities();
  public boolean add(byte[] identity);
  public boolean remove(byte[] blob);
  public void removeAll();

  /**
   * JSch will accept ciphered keys, but some implementations of
   * IdentityRepository can not.  For example, IdentityRepository for
   * ssh-agent and pageant only accept plain keys.  The following class has
   * been introduced to cache ciphered keys for them, and pass them
   * whenever they are de-ciphered.
   */
  static class Wrapper implements IdentityRepository {
    private IdentityRepository ir;
    private Vector cache = new Vector();
    private boolean keep_in_cache = false;
    Wrapper(IdentityRepository ir){
      this(ir, false);
    }
    Wrapper(IdentityRepository ir, boolean keep_in_cache){
      this.ir = ir;
      this.keep_in_cache = keep_in_cache;
    }
    public String getName() {
      return ir.getName();
    }
    public int getStatus() {
      return ir.getStatus();
    }
    public boolean add(byte[] identity) {
      return ir.add(identity);
    }
    public boolean remove(byte[] blob) {
      return ir.remove(blob);
    }
    public void removeAll() {
      cache.removeAllElements();
      ir.removeAll();
    }
    public Vector getIdentities() {
      Vector result = new Vector();
      for(int i = 0; i< cache.size(); i++){
        Identity identity = (Identity)(cache.elementAt(i));
        result.add(identity);
      }
      Vector tmp = ir.getIdentities();
      for(int i = 0; i< tmp.size(); i++){
        result.add(tmp.elementAt(i));
      }
      return result;
    }
    void add(Identity identity) {
      if(!keep_in_cache &&
         !identity.isEncrypted() && (identity instanceof IdentityFile)) {
        try {
          ir.add(((IdentityFile)identity).getKeyPair().forSSHAgent());
        }
        catch(JSchException e){
          // an exception will not be thrown.
        }
      }
      else
        cache.addElement(identity);
    }
    void check() {
      if(cache.size() > 0){
        Object[] identities = cache.toArray();
        for(int i = 0; i < identities.length; i++){
          Identity identity = (Identity)(identities[i]);
          cache.removeElement(identity);
          add(identity);
        }
      }
    }
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class IO{
  InputStream in;
  OutputStream out;
  OutputStream out_ext;

  private boolean in_dontclose=false;
  private boolean out_dontclose=false;
  private boolean out_ext_dontclose=false;

  void setOutputStream(OutputStream out){ this.out=out; }
  void setOutputStream(OutputStream out, boolean dontclose){
    this.out_dontclose=dontclose;
    setOutputStream(out);
  }
  void setExtOutputStream(OutputStream out){ this.out_ext=out; }
  void setExtOutputStream(OutputStream out, boolean dontclose){
    this.out_ext_dontclose=dontclose;
    setExtOutputStream(out);
  }
  void setInputStream(InputStream in){ this.in=in; }
  void setInputStream(InputStream in, boolean dontclose){
    this.in_dontclose=dontclose;
    setInputStream(in);
  }

  public void put(Packet p) throws IOException, java.net.SocketException {
    out.write(p.buffer.buffer, 0, p.buffer.index);
    out.flush();
  }
  void put(byte[] array, int begin, int length) throws IOException {
    out.write(array, begin, length);
    out.flush();
  }
  void put_ext(byte[] array, int begin, int length) throws IOException {
    out_ext.write(array, begin, length);
    out_ext.flush();
  }

  int getByte() throws IOException {
    return in.read();
  }

  void getByte(byte[] array) throws IOException {
    getByte(array, 0, array.length);
  }

  void getByte(byte[] array, int begin, int length) throws IOException {
    do{
      int completed = in.read(array, begin, length);
      if(completed<0){
        throw new IOException("End of IO Stream Read");
      }
      begin+=completed;
      length-=completed;
    }
    while (length>0);
  }

  void out_close(){
    try{
      if(out!=null && !out_dontclose) out.close();
      out=null;
    }
    catch(Exception ee){}
  }

  public void close(){
    try{
      if(in!=null && !in_dontclose) in.close();
      in=null;
    }
    catch(Exception ee){}

    out_close();

    try{
      if(out_ext!=null && !out_ext_dontclose) out_ext.close();
      out_ext=null;
    }
    catch(Exception ee){}
  }

  /*
  public void finalize() throws Throwable{
    try{
      if(in!=null) in.close();
    }
    catch(Exception ee){}
    try{
      if(out!=null) out.close();
    }
    catch(Exception ee){}
    try{
      if(out_ext!=null) out_ext.close();
    }
    catch(Exception ee){}
  }
  */
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class JSchAuthCancelException extends JSchException{
  //private static final long serialVersionUID=3204965907117900987L;
  String method;
  JSchAuthCancelException () {
    super();
  }
  JSchAuthCancelException (String s) {
    super(s);
    this.method=s;
  }
  public String getMethod(){
    return method;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class JSchException extends Exception{
  //private static final long serialVersionUID=-1319309923966731989L;
  private Throwable cause=null;
  public JSchException () {
    super();
  }
  public JSchException (String s) {
    super(s);
  }
  public JSchException (String s, Throwable e) {
    super(s);
    this.cause=e;
  }
  public Throwable getCause(){
    return this.cause;
  }
}


/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class JSchPartialAuthException extends JSchException{
  //private static final long serialVersionUID=-378849862323360367L;
  String methods;
  public JSchPartialAuthException () {
    super();
  }
  public JSchPartialAuthException (String s) {
    super(s);
    this.methods=s;
  }
  public String getMethods(){
    return methods;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



abstract class KeyExchange{

  static final int PROPOSAL_KEX_ALGS=0;
  static final int PROPOSAL_SERVER_HOST_KEY_ALGS=1;
  static final int PROPOSAL_ENC_ALGS_CTOS=2;
  static final int PROPOSAL_ENC_ALGS_STOC=3;
  static final int PROPOSAL_MAC_ALGS_CTOS=4;
  static final int PROPOSAL_MAC_ALGS_STOC=5;
  static final int PROPOSAL_COMP_ALGS_CTOS=6;
  static final int PROPOSAL_COMP_ALGS_STOC=7;
  static final int PROPOSAL_LANG_CTOS=8;
  static final int PROPOSAL_LANG_STOC=9;
  static final int PROPOSAL_MAX=10;

  //static String kex_algs="diffie-hellman-group-exchange-sha1"+
  //                       ",diffie-hellman-group1-sha1";

//static String kex="diffie-hellman-group-exchange-sha1";
  static String kex="diffie-hellman-group1-sha1";
  static String server_host_key="ssh-rsa,ssh-dss";
  static String enc_c2s="blowfish-cbc";
  static String enc_s2c="blowfish-cbc";
  static String mac_c2s="hmac-md5";     // hmac-md5,hmac-sha1,hmac-ripemd160,
                                        // hmac-sha1-96,hmac-md5-96
  static String mac_s2c="hmac-md5";
//static String comp_c2s="none";        // zlib
//static String comp_s2c="none";
  static String lang_c2s="";
  static String lang_s2c="";

  public static final int STATE_END=0;

  protected Session session=null;
  protected HASH sha=null;
  protected byte[] K=null;
  protected byte[] H=null;
  protected byte[] K_S=null;

  public abstract void init(Session session,
                            byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception;
  public abstract boolean next(Buffer buf) throws Exception;

  public abstract int getState();

  protected final int RSA=0;
  protected final int DSS=1;
  protected final int ECDSA=2;
  private int type=0;
  private String key_alg_name = "";

  public String getKeyType() {
    if(type==DSS) return "DSA";
    if(type==RSA) return "RSA";
    return "ECDSA";
  }

  public String getKeyAlgorithName() {
    return key_alg_name;
  }

  protected static String[] guess(byte[]I_S, byte[]I_C){
    String[] guess=new String[PROPOSAL_MAX];
    Buffer sb=new Buffer(I_S); sb.setOffSet(17);
    Buffer cb=new Buffer(I_C); cb.setOffSet(17);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      for(int i=0; i<PROPOSAL_MAX; i++){
        JSch.getLogger().log(Logger.INFO,
                             "kex: server: "+Util.byte2str(sb.getString()));
      }
      for(int i=0; i<PROPOSAL_MAX; i++){
        JSch.getLogger().log(Logger.INFO,
                             "kex: client: "+Util.byte2str(cb.getString()));
      }
      sb.setOffSet(17);
      cb.setOffSet(17);
    }

    for(int i=0; i<PROPOSAL_MAX; i++){
      byte[] sp=sb.getString();  // server proposal
      byte[] cp=cb.getString();  // client proposal
      int j=0;
      int k=0;

      loop:
      while(j<cp.length){
        while(j<cp.length && cp[j]!=',')j++;
        if(k==j) return null;
        String algorithm=Util.byte2str(cp, k, j-k);
        int l=0;
        int m=0;
        while(l<sp.length){
          while(l<sp.length && sp[l]!=',')l++;
          if(m==l) return null;
          if(algorithm.equals(Util.byte2str(sp, m, l-m))){
            guess[i]=algorithm;
            break loop;
          }
          l++;
          m=l;
        }
        j++;
        k=j;
      }
      if(j==0){
        guess[i]="";
      }
      else if(guess[i]==null){
        return null;
      }
    }

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "kex: server->client"+
                           " "+guess[PROPOSAL_ENC_ALGS_STOC]+
                           " "+guess[PROPOSAL_MAC_ALGS_STOC]+
                           " "+guess[PROPOSAL_COMP_ALGS_STOC]);
      JSch.getLogger().log(Logger.INFO,
                           "kex: client->server"+
                           " "+guess[PROPOSAL_ENC_ALGS_CTOS]+
                           " "+guess[PROPOSAL_MAC_ALGS_CTOS]+
                           " "+guess[PROPOSAL_COMP_ALGS_CTOS]);
    }

    return guess;
  }

  public String getFingerPrint(){
    HASH hash=null;
    try{
      Class c=Class.forName(session.getConfig("md5"));
      hash=(HASH)(c.newInstance());
    }
    catch(Exception e){ System.err.println("getFingerPrint: "+e); }
    return Util.getFingerPrint(hash, getHostKey());
  }
  byte[] getK(){ return K; }
  byte[] getH(){ return H; }
  HASH getHash(){ return sha; }
  byte[] getHostKey(){ return K_S; }

  /*
   * It seems JCE included in Oracle's Java7u6(and later) has suddenly changed
   * its behavior.  The secrete generated by KeyAgreement#generateSecret()
   * may start with 0, even if it is a positive value.
   */
  protected byte[] normalize(byte[] secret) {
    if(secret.length > 1 &&
       secret[0] == 0 && (secret[1]&0x80) == 0) {
      byte[] tmp=new byte[secret.length-1];
      System.arraycopy(secret, 1, tmp, 0, tmp.length);
      return normalize(tmp);
    }
    else {
      return secret;
    }
  }

  protected boolean verify(String alg, byte[] K_S, int index,
                           byte[] sig_of_H) throws Exception {
    int i,j;

    i=index;
    boolean result=false;

    if(alg.equals("ssh-rsa")){
      byte[] tmp;
      byte[] ee;
      byte[] n;

      type=RSA;
      key_alg_name=alg;

      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      ee=tmp;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      n=tmp;

      SignatureRSA sig=null;
      try{
        Class c=Class.forName(session.getConfig("signature.rsa"));
        sig=(SignatureRSA)(c.newInstance());
        sig.init();
      }
      catch(Exception e){
        System.err.println(e);
      }
      sig.setPubKey(ee, n);
      sig.update(H);
      result=sig.verify(sig_of_H);

      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "ssh_rsa_verify: signature "+result);
      }
    }
    else if(alg.equals("ssh-dss")){
      byte[] q=null;
      byte[] tmp;
      byte[] p;
      byte[] g;
      byte[] f;

      type=DSS;
      key_alg_name=alg;

      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
          ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      p=tmp;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      q=tmp;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
          ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      g=tmp;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      f=tmp;

      SignatureDSA sig=null;
      try{
        Class c=Class.forName(session.getConfig("signature.dss"));
        sig=(SignatureDSA)(c.newInstance());
        sig.init();
      }
      catch(Exception e){
        System.err.println(e);
      }
      sig.setPubKey(f, p, q, g);
      sig.update(H);
      result=sig.verify(sig_of_H);

      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "ssh_dss_verify: signature "+result);
      }
    }
    else if(alg.equals("ecdsa-sha2-nistp256") ||
            alg.equals("ecdsa-sha2-nistp384") ||
            alg.equals("ecdsa-sha2-nistp521")) {
      byte[] tmp;
      byte[] r;
      byte[] s;

      // RFC 5656,
      type=ECDSA;
      key_alg_name=alg;

      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      tmp=new byte[j]; System.arraycopy(K_S, i, tmp, 0, j); i+=j;
      j=((K_S[i++]<<24)&0xff000000)|((K_S[i++]<<16)&0x00ff0000)|
        ((K_S[i++]<<8)&0x0000ff00)|((K_S[i++])&0x000000ff);
      i++;
      tmp=new byte[(j-1)/2];
      System.arraycopy(K_S, i, tmp, 0, tmp.length); i+=(j-1)/2;
      r=tmp;
      tmp=new byte[(j-1)/2];
      System.arraycopy(K_S, i, tmp, 0, tmp.length); i+=(j-1)/2;
      s=tmp;

      SignatureECDSA sig=null;
      try{
        Class c=Class.forName(session.getConfig("signature.ecdsa"));
        sig=(SignatureECDSA)(c.newInstance());
        sig.init();
      }
      catch(Exception e){
        System.err.println(e);
      }

      sig.setPubKey(r, s);

      sig.update(H);

      result=sig.verify(sig_of_H);
    }
    else{
      System.err.println("unknown alg");
    }

    return result;
  }

}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class KeyPairDSA extends KeyPair{
  private byte[] P_array;
  private byte[] Q_array;
  private byte[] G_array;
  private byte[] pub_array;
  private byte[] prv_array;

  //private int key_size=0;
  private int key_size=1024;

  public KeyPairDSA(JSch jsch){
    this(jsch, null, null, null, null, null);
  }

  public KeyPairDSA(JSch jsch,
                    byte[] P_array,
                    byte[] Q_array,
                    byte[] G_array,
                    byte[] pub_array,
                    byte[] prv_array){
    super(jsch);
    this.P_array = P_array;
    this.Q_array = Q_array;
    this.G_array = G_array;
    this.pub_array = pub_array;
    this.prv_array = prv_array;
    if(P_array!=null)
      key_size = (new java.math.BigInteger(P_array)).bitLength();
  }

  void generate(int key_size) throws JSchException{
    this.key_size=key_size;
    try{
      Class c=Class.forName(jsch.getConfig("keypairgen.dsa"));
      KeyPairGenDSA keypairgen=(KeyPairGenDSA)(c.newInstance());
      keypairgen.init(key_size);
      P_array=keypairgen.getP();
      Q_array=keypairgen.getQ();
      G_array=keypairgen.getG();
      pub_array=keypairgen.getY();
      prv_array=keypairgen.getX();

      keypairgen=null;
    }
    catch(Exception e){
      //System.err.println("KeyPairDSA: "+e);
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }
  }

  private static final byte[] begin=Util.str2byte("-----BEGIN DSA PRIVATE KEY-----");
  private static final byte[] end=Util.str2byte("-----END DSA PRIVATE KEY-----");

  byte[] getBegin(){ return begin; }
  byte[] getEnd(){ return end; }

  byte[] getPrivateKey(){
    int content=
      1+countLength(1) + 1 +                           // INTEGER
      1+countLength(P_array.length) + P_array.length + // INTEGER  P
      1+countLength(Q_array.length) + Q_array.length + // INTEGER  Q
      1+countLength(G_array.length) + G_array.length + // INTEGER  G
      1+countLength(pub_array.length) + pub_array.length + // INTEGER  pub
      1+countLength(prv_array.length) + prv_array.length;  // INTEGER  prv

    int total=
      1+countLength(content)+content;   // SEQUENCE

    byte[] plain=new byte[total];
    int index=0;
    index=writeSEQUENCE(plain, index, content);
    index=writeINTEGER(plain, index, new byte[1]);  // 0
    index=writeINTEGER(plain, index, P_array);
    index=writeINTEGER(plain, index, Q_array);
    index=writeINTEGER(plain, index, G_array);
    index=writeINTEGER(plain, index, pub_array);
    index=writeINTEGER(plain, index, prv_array);
    return plain;
  }

  boolean parse(byte[] plain){
    try{

      if(vendor==VENDOR_FSECURE){
        if(plain[0]!=0x30){              // FSecure
          Buffer buf=new Buffer(plain);
          buf.getInt();
          P_array=buf.getMPIntBits();
          G_array=buf.getMPIntBits();
          Q_array=buf.getMPIntBits();
          pub_array=buf.getMPIntBits();
          prv_array=buf.getMPIntBits();
          if(P_array!=null)
            key_size = (new java.math.BigInteger(P_array)).bitLength();
          return true;
        }
        return false;
      }
      else if(vendor==VENDOR_PUTTY){
        Buffer buf=new Buffer(plain);
        buf.skip(plain.length);

        try {
          byte[][] tmp = buf.getBytes(1, "");
          prv_array = tmp[0];
        }
        catch(JSchException e){
          return false;
        }

        return true;
      }

      int index=0;
      int length=0;

      if(plain[index]!=0x30)return false;
      index++; // SEQUENCE
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      if(plain[index]!=0x02)return false;
      index++; // INTEGER
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      P_array=new byte[length];
      System.arraycopy(plain, index, P_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      Q_array=new byte[length];
      System.arraycopy(plain, index, Q_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      G_array=new byte[length];
      System.arraycopy(plain, index, G_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      pub_array=new byte[length];
      System.arraycopy(plain, index, pub_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      prv_array=new byte[length];
      System.arraycopy(plain, index, prv_array, 0, length);
      index+=length;

      if(P_array!=null)
        key_size = (new java.math.BigInteger(P_array)).bitLength();
    }
    catch(Exception e){
      //System.err.println(e);
      //e.printStackTrace();
      return false;
    }
    return true;
  }

  public byte[] getPublicKeyBlob(){
    byte[] foo=super.getPublicKeyBlob();
    if(foo!=null) return foo;

    if(P_array==null) return null;
    byte[][] tmp = new byte[5][];
    tmp[0] = sshdss;
    tmp[1] = P_array;
    tmp[2] = Q_array;
    tmp[3] = G_array;
    tmp[4] = pub_array;
    return Buffer.fromBytes(tmp).buffer;
  }

  private static final byte[] sshdss=Util.str2byte("ssh-dss");
  byte[] getKeyTypeName(){return sshdss;}
  public int getKeyType(){return DSA;}

  public int getKeySize(){
    return key_size;
  }

  public byte[] getSignature(byte[] data){
    try{
      Class c=Class.forName((String)jsch.getConfig("signature.dss"));
      SignatureDSA dsa=(SignatureDSA)(c.newInstance());
      dsa.init();
      dsa.setPrvKey(prv_array, P_array, Q_array, G_array);

      dsa.update(data);
      byte[] sig = dsa.sign();
      byte[][] tmp = new byte[2][];
      tmp[0] = sshdss;
      tmp[1] = sig;
      return Buffer.fromBytes(tmp).buffer;
    }
    catch(Exception e){
      //System.err.println("e "+e);
    }
    return null;
  }

  public Signature getVerifier(){
    try{
      Class c=Class.forName((String)jsch.getConfig("signature.dss"));
      SignatureDSA dsa=(SignatureDSA)(c.newInstance());
      dsa.init();

      if(pub_array == null && P_array == null && getPublicKeyBlob()!=null){
        Buffer buf = new Buffer(getPublicKeyBlob());
        buf.getString();
        P_array = buf.getString();
        Q_array = buf.getString();
        G_array = buf.getString();
        pub_array = buf.getString();
      }

      dsa.setPubKey(pub_array, P_array, Q_array, G_array);
      return dsa;
    }
    catch(Exception e){
      //System.err.println("e "+e);
    }
    return null;
  }

  static KeyPair fromSSHAgent(JSch jsch, Buffer buf) throws JSchException {

    byte[][] tmp = buf.getBytes(7, "invalid key format");

    byte[] P_array = tmp[1];
    byte[] Q_array = tmp[2];
    byte[] G_array = tmp[3];
    byte[] pub_array = tmp[4];
    byte[] prv_array = tmp[5];
    KeyPairDSA kpair = new KeyPairDSA(jsch,
                                      P_array, Q_array, G_array,
                                      pub_array, prv_array);
    kpair.publicKeyComment = new String(tmp[6]);
    kpair.vendor=VENDOR_OPENSSH;
    return kpair;
  }

  public byte[] forSSHAgent() throws JSchException {
    if(isEncrypted()){
      throw new JSchException("key is encrypted.");
    }
    Buffer buf = new Buffer();
    buf.putString(sshdss);
    buf.putString(P_array);
    buf.putString(Q_array);
    buf.putString(G_array);
    buf.putString(pub_array);
    buf.putString(prv_array);
    buf.putString(Util.str2byte(publicKeyComment));
    byte[] result = new byte[buf.getLength()];
    buf.getByte(result, 0, result.length);
    return result;
  }

  public void dispose(){
    super.dispose();
    Util.bzero(prv_array);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class KeyPairECDSA extends KeyPair{

  private static byte[][] oids = {
    {(byte)0x06, (byte)0x08, (byte)0x2a, (byte)0x86, (byte)0x48, // 256
     (byte)0xce, (byte)0x3d, (byte)0x03, (byte)0x01, (byte)0x07},
    {(byte)0x06, (byte)0x05, (byte)0x2b, (byte)0x81, (byte)0x04, // 384
     (byte)0x00, (byte)0x22},
    {(byte)0x06, (byte)0x05, (byte)0x2b, (byte)0x81, (byte)0x04, //521
     (byte)0x00, (byte)0x23},
  };

  private static String[] names = {
    "nistp256", "nistp384", "nistp521"
  };

  private byte[] name=Util.str2byte(names[0]);
  private byte[] r_array;
  private byte[] s_array;
  private byte[] prv_array;

  private int key_size=256;

  public KeyPairECDSA(JSch jsch){
    this(jsch, null, null, null, null);
  }

  public KeyPairECDSA(JSch jsch,
                      byte[] name,
                      byte[] r_array,
                      byte[] s_array,
                      byte[] prv_array){
    super(jsch);
    if(name!=null)
      this.name = name;
    this.r_array = r_array;
    this.s_array = s_array;
    this.prv_array = prv_array;
    if(prv_array!=null)
      key_size = prv_array.length>=64 ? 521 :
                  (prv_array.length>=48 ? 384 : 256);
  }

  void generate(int key_size) throws JSchException{
    this.key_size=key_size;
    try{
      Class c=Class.forName(jsch.getConfig("keypairgen.ecdsa"));
      KeyPairGenECDSA keypairgen=(KeyPairGenECDSA)(c.newInstance());
      keypairgen.init(key_size);
      prv_array=keypairgen.getD();
      r_array=keypairgen.getR();
      s_array=keypairgen.getS();
      name=Util.str2byte(names[prv_array.length>=64 ? 2 :
                               (prv_array.length>=48 ? 1 : 0)]);
      keypairgen=null;
    }
    catch(Exception e){
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }
  }

  private static final byte[] begin =
    Util.str2byte("-----BEGIN EC PRIVATE KEY-----");
  private static final byte[] end =
    Util.str2byte("-----END EC PRIVATE KEY-----");

  byte[] getBegin(){ return begin; }
  byte[] getEnd(){ return end; }

  byte[] getPrivateKey(){

    byte[] tmp = new byte[1]; tmp[0]=1;

    byte[] oid = oids[
                      (r_array.length>=64) ? 2 :
                       ((r_array.length>=48) ? 1 : 0)
                     ];

    byte[] point = toPoint(r_array, s_array);

    int bar = ((point.length+1)&0x80)==0 ? 3 : 4;
    byte[] foo = new byte[point.length+bar];
    System.arraycopy(point, 0, foo, bar, point.length);
    foo[0]=0x03;                     // BITSTRING
    if(bar==3){
      foo[1]=(byte)(point.length+1);
    }
    else {
      foo[1]=(byte)0x81;
      foo[2]=(byte)(point.length+1);
    }
    point = foo;

    int content=
      1+countLength(tmp.length) + tmp.length +
      1+countLength(prv_array.length) + prv_array.length +
      1+countLength(oid.length) + oid.length +
      1+countLength(point.length) + point.length;

    int total=
      1+countLength(content)+content;   // SEQUENCE

    byte[] plain=new byte[total];
    int index=0;
    index=writeSEQUENCE(plain, index, content);
    index=writeINTEGER(plain, index, tmp);
    index=writeOCTETSTRING(plain, index, prv_array);
    index=writeDATA(plain, (byte)0xa0, index, oid);
    index=writeDATA(plain, (byte)0xa1, index, point);

    return plain;
  }

  boolean parse(byte[] plain){
    try{

      if(vendor==VENDOR_FSECURE){
        /*
        if(plain[0]!=0x30){              // FSecure
          return true;
        }
        return false;
        */
        return false;
      }
      else if(vendor==VENDOR_PUTTY){
        /*
        Buffer buf=new Buffer(plain);
        buf.skip(plain.length);

        try {
          byte[][] tmp = buf.getBytes(1, "");
          prv_array = tmp[0];
        }
        catch(JSchException e){
          return false;
        }

        return true;
        */
        return false;
      }

      int index=0;
      int length=0;

      if(plain[index]!=0x30)return false;
      index++; // SEQUENCE
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      if(plain[index]!=0x02)return false;
      index++; // INTEGER

      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      index+=length;
      index++;   // 0x04

      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      prv_array=new byte[length];
      System.arraycopy(plain, index, prv_array, 0, length);

      index+=length;

      index++;  // 0xa0

      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      byte[] oid_array=new byte[length];
      System.arraycopy(plain, index, oid_array, 0, length);
      index+=length;

      for(int i = 0; i<oids.length; i++){
        if(Util.array_equals(oids[i], oid_array)){
          name = Util.str2byte(names[i]);
          break;
        }
      }

      index++;  // 0xa1

      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      byte[] Q_array=new byte[length];
      System.arraycopy(plain, index, Q_array, 0, length);
      index+=length;

      byte[][] tmp = fromPoint(Q_array);
      r_array = tmp[0];
      s_array = tmp[1];

      if(prv_array!=null)
        key_size = prv_array.length>=64 ? 521 :
                    (prv_array.length>=48 ? 384 : 256);
    }
    catch(Exception e){
      //System.err.println(e);
      //e.printStackTrace();
      return false;
    }
    return true;
  }

  public byte[] getPublicKeyBlob(){
    byte[] foo = super.getPublicKeyBlob();

    if(foo!=null) return foo;

    if(r_array==null) return null;

    byte[][] tmp = new byte[3][];
    tmp[0] = Util.str2byte("ecdsa-sha2-"+new String(name));
    tmp[1] = name;
    tmp[2] = new byte[1+r_array.length+s_array.length];
    tmp[2][0] = 4;   // POINT_CONVERSION_UNCOMPRESSED
    System.arraycopy(r_array, 0, tmp[2], 1, r_array.length);
    System.arraycopy(s_array, 0, tmp[2], 1+r_array.length, s_array.length);

    return Buffer.fromBytes(tmp).buffer;
  }

  byte[] getKeyTypeName(){
    return Util.str2byte("ecdsa-sha2-"+new String(name));
  }
  public int getKeyType(){
    return ECDSA;
  }
  public int getKeySize(){
    return key_size;
  }

  public byte[] getSignature(byte[] data){
    try{
      Class c=Class.forName((String)jsch.getConfig("signature.ecdsa"));
      SignatureECDSA ecdsa=(SignatureECDSA)(c.newInstance());
      ecdsa.init();
      ecdsa.setPrvKey(prv_array);

      ecdsa.update(data);
      byte[] sig = ecdsa.sign();

      byte[][] tmp = new byte[2][];
      tmp[0] = Util.str2byte("ecdsa-sha2-"+new String(name));
      tmp[1] = sig;
      return Buffer.fromBytes(tmp).buffer;
    }
    catch(Exception e){
      //System.err.println("e "+e);
    }
    return null;
  }

  public Signature getVerifier(){
    try{
      Class c=Class.forName((String)jsch.getConfig("signature.ecdsa"));
      final SignatureECDSA ecdsa=(SignatureECDSA)(c.newInstance());
      ecdsa.init();

      if(r_array == null && s_array == null && getPublicKeyBlob()!=null){
        Buffer buf = new Buffer(getPublicKeyBlob());
        buf.getString();    // ecdsa-sha2-nistp256
        buf.getString();    // nistp256
        byte[][] tmp = fromPoint(buf.getString());
        r_array = tmp[0];
        s_array = tmp[1];
      }
      ecdsa.setPubKey(r_array, s_array);
      return ecdsa;
    }
    catch(Exception e){
      //System.err.println("e "+e);
    }
    return null;
  }

  static KeyPair fromSSHAgent(JSch jsch, Buffer buf) throws JSchException {

    byte[][] tmp = buf.getBytes(5, "invalid key format");

    byte[] name = tmp[1];       // nistp256
    byte[][] foo = fromPoint(tmp[2]);
    byte[] r_array = foo[0];
    byte[] s_array = foo[1];

    byte[] prv_array = tmp[3];
    KeyPairECDSA kpair = new KeyPairECDSA(jsch,
                                          name,
                                          r_array, s_array,
                                          prv_array);
    kpair.publicKeyComment = new String(tmp[4]);
    kpair.vendor=VENDOR_OPENSSH;
    return kpair;
  }

  public byte[] forSSHAgent() throws JSchException {
    if(isEncrypted()){
      throw new JSchException("key is encrypted.");
    }
    Buffer buf = new Buffer();
    buf.putString(Util.str2byte("ecdsa-sha2-"+new String(name)));
    buf.putString(name);
    buf.putString(toPoint(r_array, s_array));
    buf.putString(prv_array);
    buf.putString(Util.str2byte(publicKeyComment));
    byte[] result = new byte[buf.getLength()];
    buf.getByte(result, 0, result.length);
    return result;
  }

  static byte[] toPoint(byte[] r_array, byte[] s_array) {
    byte[] tmp = new byte[1+r_array.length+s_array.length];
    tmp[0]=0x04;
    System.arraycopy(r_array, 0, tmp, 1, r_array.length);
    System.arraycopy(s_array, 0, tmp, 1+r_array.length, s_array.length);
    return tmp;
  }

  static byte[][] fromPoint(byte[] point) {
    int i = 0;
    while(point[i]!=4) i++;
    i++;
    byte[][] tmp = new byte[2][];
    byte[] r_array = new byte[(point.length-i)/2];
    byte[] s_array = new byte[(point.length-i)/2];
    // point[0] == 0x04 == POINT_CONVERSION_UNCOMPRESSED
    System.arraycopy(point, i, r_array, 0, r_array.length);
    System.arraycopy(point, i+r_array.length, s_array, 0, s_array.length);
    tmp[0] = r_array;
    tmp[1] = s_array;

    return tmp;
  }

  public void dispose(){
    super.dispose();
    Util.bzero(prv_array);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface KeyPairGenDSA{
  void init(int key_size) throws Exception;
  byte[] getX();
  byte[] getY();
  byte[] getP();
  byte[] getQ();
  byte[] getG();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface KeyPairGenECDSA{
  void init(int key_size) throws Exception;
  byte[] getD();
  byte[] getR();
  byte[] getS();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface KeyPairGenRSA{
  void init(int key_size) throws Exception;
  byte[] getD();
  byte[] getE();
  byte[] getN();

  byte[] getC();
  byte[] getEP();
  byte[] getEQ();
  byte[] getP();
  byte[] getQ();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




abstract class KeyPair {
    public static final int ERROR = 0;
    public static final int DSA = 1;
    public static final int RSA = 2;
    public static final int ECDSA = 3;
    public static final int DSA_CERT = 4;
    public static final int RSA_CERT = 5;
    public static final int ECDSA_CERT = 6;
    public static final int UNKNOWN = 7;

    static final int VENDOR_OPENSSH = 0;
    static final int VENDOR_FSECURE = 1;
    static final int VENDOR_PUTTY = 2;
    static final int VENDOR_PKCS8 = 3;
    private static final byte[] cr = Util.str2byte("\n");
    private static final String[] header1 = {
            "PuTTY-User-Key-File-2: ",
            "Encryption: ",
            "Comment: ",
            "Public-Lines: "
    };
    private static final String[] header2 = {
            "Private-Lines: "
    };
    private static final String[] header3 = {
            "Private-MAC: "
    };
    static byte[][] header = {Util.str2byte("Proc-Type: 4,ENCRYPTED"),
            Util.str2byte("DEK-Info: DES-EDE3-CBC,")};
    private static byte[] space = Util.str2byte(" ");
    protected String publicKeyComment = "no comment";
    protected boolean encrypted = false;
    protected byte[] data = null;
    int vendor = VENDOR_OPENSSH;
    JSch jsch = null;
    private Cipher cipher;
    private HASH hash;
    private Random random;
    private byte[] passphrase;
    private byte[] iv = null;
    private byte[] publickeyblob = null;
    public KeyPair(JSch jsch) {
        this.jsch = jsch;
    }

    public static KeyPair genKeyPair(JSch jsch, int type) throws JSchException {
        return genKeyPair(jsch, type, 1024);
    }

    public static KeyPair genKeyPair(JSch jsch, int type, int key_size) throws JSchException {
        KeyPair kpair = null;
        if (type == DSA) {
            kpair = new KeyPairDSA(jsch);
        } else if (type == RSA) {
            kpair = new KeyPairRSA(jsch);
        } else if (type == RSA_CERT) {
            kpair = new OpenSSHUserCertRSA(jsch);
        } else if (type == DSA_CERT) {
            kpair = new OpenSSHUserCertDSA(jsch);
        } else if (type == ECDSA) {
            kpair = new KeyPairECDSA(jsch);
        } else if (type == ECDSA_CERT) {
            kpair = new OpenSSHUserCertECDSA(jsch);
        }

        if (kpair != null) {
            kpair.generate(key_size);
        }
        return kpair;
    }

    public static KeyPair load(JSch jsch, String prvkey) throws JSchException {
        return load(jsch, prvkey, null);
    }

    public static KeyPair load(JSch jsch, String prvfile, String pubfile) throws JSchException {

        byte[] prvkey = null;
        byte[] pubkey = null;

        try {
            prvkey = Util.fromFile(prvfile);
        } catch (IOException e) {
            throw new JSchException(e.toString(), (Throwable) e);
        }

        String _pubfile = pubfile;
        if (pubfile == null) {
            if (new File(prvfile + ".pub").exists()) {
                _pubfile = prvfile + ".pub";
            } else if (new File(prvfile + "-cert.pub").exists()) {
                _pubfile = prvfile + "-cert.pub";
            }
        }

        try {
            pubkey = Util.fromFile(_pubfile);
        } catch (IOException e) {
            if (pubfile != null) {
                throw new JSchException(e.toString(), (Throwable) e);
            }
        }

        try {
            return load(jsch, prvkey, pubkey);
        } finally {
            Util.bzero(prvkey);
        }
    }

    public static KeyPair load(JSch jsch, byte[] prvkey, byte[] pubkey) throws JSchException {

        byte[] iv = new byte[8];       // 8
        boolean encrypted = true;
        byte[] data = null;

        byte[] publickeyblob = null;

        int type = ERROR;
        int vendor = VENDOR_OPENSSH;
        String publicKeyComment = "";
        Cipher cipher = null;

        // prvkey from "ssh-add" command on the remote.
        if (pubkey == null &&
                prvkey != null &&
                (prvkey.length > 11 &&
                        prvkey[0] == 0 && prvkey[1] == 0 && prvkey[2] == 0 &&
                        (prvkey[3] == 7 || prvkey[3] == 19))) {

            Buffer buf = new Buffer(prvkey);
            buf.skip(prvkey.length);  // for using Buffer#available()
            String _type = new String(buf.getString()); // ssh-rsa or ssh-dss
            buf.rewind();

            KeyPair kpair = null;
            if (_type.equals("ssh-rsa")) {
                kpair = KeyPairRSA.fromSSHAgent(jsch, buf);
            } else if (_type.equals("ssh-dss")) {
                kpair = KeyPairDSA.fromSSHAgent(jsch, buf);
            } else if (_type.equals("ecdsa-sha2-nistp256") ||
                    _type.equals("ecdsa-sha2-nistp384") ||
                    _type.equals("ecdsa-sha2-nistp512")) {
                kpair = KeyPairECDSA.fromSSHAgent(jsch, buf);
            } else {
                throw new JSchException("privatekey: invalid key " + new String(prvkey, 4, 7));
            }
            return kpair;
        }

        try {
            byte[] buf = prvkey;

            if (buf != null) {
                KeyPair ppk = loadPPK(jsch, buf);
                if (ppk != null) {
                    return ppk;
                }
            }

            int len = (buf != null ? buf.length : 0);
            int i = 0;

            // skip garbage lines.
            while (i < len) {
                if (buf[i] == '-' && i + 4 < len &&
                        buf[i + 1] == '-' && buf[i + 2] == '-' &&
                        buf[i + 3] == '-' && buf[i + 4] == '-') {
                    break;
                }
                i++;
            }

            while (i < len) {
                if (buf[i] == 'B' && i + 3 < len && buf[i + 1] == 'E' && buf[i + 2] == 'G' && buf[i + 3] == 'I') {
                    i += 6;
                    if (i + 2 >= len) {
                        throw new JSchException("invalid privatekey: " + prvkey);
                    }
                    if (buf[i] == 'D' && buf[i + 1] == 'S' && buf[i + 2] == 'A') {
                        type = DSA;
                    } else if (buf[i] == 'R' && buf[i + 1] == 'S' && buf[i + 2] == 'A') {
                        type = RSA;
                    } else if (buf[i] == 'E' && buf[i + 1] == 'C') {
                        type = ECDSA;
                    } else if (buf[i] == 'S' && buf[i + 1] == 'S' && buf[i + 2] == 'H') { // FSecure
                        type = UNKNOWN;
                        vendor = VENDOR_FSECURE;
                    } else if (i + 6 < len &&
                            buf[i] == 'P' && buf[i + 1] == 'R' &&
                            buf[i + 2] == 'I' && buf[i + 3] == 'V' &&
                            buf[i + 4] == 'A' && buf[i + 5] == 'T' && buf[i + 6] == 'E') {
                        type = UNKNOWN;
                        vendor = VENDOR_PKCS8;
                        encrypted = false;
                        i += 3;
                    } else if (i + 8 < len &&
                            buf[i] == 'E' && buf[i + 1] == 'N' &&
                            buf[i + 2] == 'C' && buf[i + 3] == 'R' &&
                            buf[i + 4] == 'Y' && buf[i + 5] == 'P' && buf[i + 6] == 'T' &&
                            buf[i + 7] == 'E' && buf[i + 8] == 'D') {
                        type = UNKNOWN;
                        vendor = VENDOR_PKCS8;
                        i += 5;
                    } else {
                        throw new JSchException("invalid privatekey: " + prvkey);
                    }
                    i += 3;
                    continue;
                }
                if (buf[i] == 'A' && i + 7 < len && buf[i + 1] == 'E' && buf[i + 2] == 'S' && buf[i + 3] == '-' &&
                        buf[i + 4] == '2' && buf[i + 5] == '5' && buf[i + 6] == '6' && buf[i + 7] == '-') {
                    i += 8;
                    if (Session.checkCipher((String) jsch.getConfig("aes256-cbc"))) {
                        Class c = Class.forName((String) jsch.getConfig("aes256-cbc"));
                        cipher = (Cipher) (c.newInstance());
                        // key=new byte[cipher.getBlockSize()];
                        iv = new byte[cipher.getIVSize()];
                    } else {
                        throw new JSchException("privatekey: aes256-cbc is not available " + prvkey);
                    }
                    continue;
                }
                if (buf[i] == 'A' && i + 7 < len && buf[i + 1] == 'E' && buf[i + 2] == 'S' && buf[i + 3] == '-' &&
                        buf[i + 4] == '1' && buf[i + 5] == '9' && buf[i + 6] == '2' && buf[i + 7] == '-') {
                    i += 8;
                    if (Session.checkCipher((String) jsch.getConfig("aes192-cbc"))) {
                        Class c = Class.forName((String) jsch.getConfig("aes192-cbc"));
                        cipher = (Cipher) (c.newInstance());
                        // key=new byte[cipher.getBlockSize()];
                        iv = new byte[cipher.getIVSize()];
                    } else {
                        throw new JSchException("privatekey: aes192-cbc is not available " + prvkey);
                    }
                    continue;
                }
                if (buf[i] == 'A' && i + 7 < len && buf[i + 1] == 'E' && buf[i + 2] == 'S' && buf[i + 3] == '-' &&
                        buf[i + 4] == '1' && buf[i + 5] == '2' && buf[i + 6] == '8' && buf[i + 7] == '-') {
                    i += 8;
                    if (Session.checkCipher((String) jsch.getConfig("aes128-cbc"))) {
                        Class c = Class.forName((String) jsch.getConfig("aes128-cbc"));
                        cipher = (Cipher) (c.newInstance());
                        // key=new byte[cipher.getBlockSize()];
                        iv = new byte[cipher.getIVSize()];
                    } else {
                        throw new JSchException("privatekey: aes128-cbc is not available " + prvkey);
                    }
                    continue;
                }
                if (buf[i] == 'C' && i + 3 < len && buf[i + 1] == 'B' && buf[i + 2] == 'C' && buf[i + 3] == ',') {
                    i += 4;
                    for (int ii = 0; ii < iv.length; ii++) {
                        iv[ii] = (byte) (((a2b(buf[i++]) << 4) & 0xf0) + (a2b(buf[i++]) & 0xf));
                    }
                    continue;
                }
                if (buf[i] == 0x0d && i + 1 < buf.length && buf[i + 1] == 0x0a) {
                    i++;
                    continue;
                }
                if (buf[i] == 0x0a && i + 1 < buf.length) {
                    if (buf[i + 1] == 0x0a) {
                        i += 2;
                        break;
                    }
                    if (buf[i + 1] == 0x0d &&
                            i + 2 < buf.length && buf[i + 2] == 0x0a) {
                        i += 3;
                        break;
                    }
                    boolean inheader = false;
                    for (int j = i + 1; j < buf.length; j++) {
                        if (buf[j] == 0x0a) {
                            break;
                        }
                        //if(buf[j]==0x0d) break;
                        if (buf[j] == ':') {
                            inheader = true;
                            break;
                        }
                    }
                    if (!inheader) {
                        i++;
                        if (vendor != VENDOR_PKCS8) {
                            encrypted = false;    // no passphrase
                        }
                        break;
                    }
                }
                i++;
            }

            if (buf != null) {

                if (type == ERROR) {
                    throw new JSchException("invalid privatekey: " + prvkey);
                }

                int start = i;
                while (i < len) {
                    if (buf[i] == '-') {
                        break;
                    }
                    i++;
                }

                if ((len - i) == 0 || (i - start) == 0) {
                    throw new JSchException("invalid privatekey: " + prvkey);
                }

                // The content of 'buf' will be changed, so it should be copied.
                byte[] tmp = new byte[i - start];
                System.arraycopy(buf, start, tmp, 0, tmp.length);
                byte[] _buf = tmp;

                start = 0;
                i = 0;

                int _len = _buf.length;
                while (i < _len) {
                    if (_buf[i] == 0x0a) {
                        boolean xd = (_buf[i - 1] == 0x0d);
                        // ignore 0x0a (or 0x0d0x0a)
                        System.arraycopy(_buf, i + 1, _buf, i - (xd ? 1 : 0), _len - (i + 1));
                        if (xd) {
                            _len--;
                        }
                        _len--;
                        continue;
                    }
                    if (_buf[i] == '-') {
                        break;
                    }
                    i++;
                }

                if (i - start > 0) {
                    data = Util.fromBase64(_buf, start, i - start);
                }

                Util.bzero(_buf);
            }

            if (data != null &&
                    data.length > 4 &&            // FSecure
                    data[0] == (byte) 0x3f &&
                    data[1] == (byte) 0x6f &&
                    data[2] == (byte) 0xf9 &&
                    data[3] == (byte) 0xeb) {

                Buffer _buf = new Buffer(data);
                _buf.getInt();  // 0x3f6ff9be
                _buf.getInt();
                byte[] _type = _buf.getString();
                //System.err.println("type: "+new String(_type));
                String _cipher = Util.byte2str(_buf.getString());
                //System.err.println("cipher: "+_cipher);
                if (_cipher.equals("3des-cbc")) {
                    _buf.getInt();
                    byte[] foo = new byte[data.length - _buf.getOffSet()];
                    _buf.getByte(foo);
                    data = foo;
                    encrypted = true;
                    throw new JSchException("unknown privatekey format: " + prvkey);
                } else if (_cipher.equals("none")) {
                    _buf.getInt();
                    _buf.getInt();

                    encrypted = false;

                    byte[] foo = new byte[data.length - _buf.getOffSet()];
                    _buf.getByte(foo);
                    data = foo;
                }
            }

            if (pubkey != null) {
                try {
                    buf = pubkey;
                    len = buf.length;
                    if (buf.length > 4 &&             // FSecure's public key
                            buf[0] == '-' && buf[1] == '-' && buf[2] == '-' && buf[3] == '-') {

                        boolean valid = true;
                        i = 0;
                        do {
                            i++;
                        } while (buf.length > i && buf[i] != 0x0a);
                        if (buf.length <= i) {
                            valid = false;
                        }

                        while (valid) {
                            if (buf[i] == 0x0a) {
                                boolean inheader = false;
                                for (int j = i + 1; j < buf.length; j++) {
                                    if (buf[j] == 0x0a) {
                                        break;
                                    }
                                    if (buf[j] == ':') {
                                        inheader = true;
                                        break;
                                    }
                                }
                                if (!inheader) {
                                    i++;
                                    break;
                                }
                            }
                            i++;
                        }
                        if (buf.length <= i) {
                            valid = false;
                        }

                        int start = i;
                        while (valid && i < len) {
                            if (buf[i] == 0x0a) {
                                System.arraycopy(buf, i + 1, buf, i, len - i - 1);
                                len--;
                                continue;
                            }
                            if (buf[i] == '-') {
                                break;
                            }
                            i++;
                        }
                        if (valid) {
                            publickeyblob = Util.fromBase64(buf, start, i - start);
                            if (prvkey == null || type == UNKNOWN) {
                                if (publickeyblob[8] == 'd') {
                                    type = DSA;
                                } else if (publickeyblob[8] == 'r') {
                                    type = RSA;
                                }
                            }
                        }
                    } else {
                        if (buf[0] == 's' && buf[1] == 's' && buf[2] == 'h' && buf[3] == '-') {
                            if (buf.length > 7) {
                                if (buf[4] == 'd') {
                                    if (buf.length >= 12 && buf[8] == 'c' && buf[9] == 'e' && buf[10] == 'r' && buf[11] == 't') {
                                        type = DSA_CERT;
                                    } else {
                                        type = DSA;
                                    }
                                } else if (buf[4] == 'r') {
                                    if (buf.length >= 12 && buf[8] == 'c' && buf[9] == 'e' && buf[10] == 'r' && buf[11] == 't') {
                                        type = RSA_CERT;
                                    } else {
                                        type = RSA;
                                    }
                                }
                            }
                            i = 0;
                            while (i < len) {
                                if (buf[i] == ' ') {
                                    break;
                                }
                                i++;
                            }
                            i++;
                            if (i < len) {
                                int start = i;
                                while (i < len) {
                                    if (buf[i] == ' ') {
                                        break;
                                    }
                                    i++;
                                }
                                publickeyblob = Util.fromBase64(buf, start, i - start);
                            }
                            if (i++ < len) {
                                int start = i;
                                while (i < len) {
                                    if (buf[i] == '\n') {
                                        break;
                                    }
                                    i++;
                                }
                                if (i > 0 && buf[i - 1] == 0x0d) {
                                    i--;
                                }
                                if (start < i) {
                                    publicKeyComment = new String(buf, start, i - start);
                                }
                            }
                        } else if (buf[0] == 'e' && buf[1] == 'c' && buf[2] == 'd' && buf[3] == 's') {
                            if (buf.length > 7) {
                                if (buf.length > 24 &&
                                        buf[6] == 's' && buf[7] == 'h' && buf[8] == 'a' && buf[9] == '2' &&
                                        buf[11] == 'n' && buf[12] == 'i' && buf[13] == 's' && buf[14] == 't' && buf[15] == 'p' &&
                                        buf[16] == '2' && buf[17] == '5' && buf[18] == '6' &&
                                        buf[20] == 'c' && buf[21] == 'e' && buf[22] == 'r' && buf[23] == 't') {
                                    type = ECDSA_CERT;
                                } else {
                                    type = ECDSA;
                                }
                            }
                            i = 0;
                            while (i < len) {
                                if (buf[i] == ' ') {
                                    break;
                                }
                                i++;
                            }
                            i++;
                            if (i < len) {
                                int start = i;
                                while (i < len) {
                                    if (buf[i] == ' ') {
                                        break;
                                    }
                                    i++;
                                }
                                publickeyblob = Util.fromBase64(buf, start, i - start);
                            }
                            if (i++ < len) {
                                int start = i;
                                while (i < len) {
                                    if (buf[i] == '\n') {
                                        break;
                                    }
                                    i++;
                                }
                                if (i > 0 && buf[i - 1] == 0x0d) {
                                    i--;
                                }
                                if (start < i) {
                                    publicKeyComment = new String(buf, start, i - start);
                                }
                            }
                        }
                    }
                } catch (Exception ee) {
                }
            }
        } catch (Exception e) {
            if (e instanceof JSchException) {
                throw (JSchException) e;
            }
            if (e instanceof Throwable) {
                throw new JSchException(e.toString(), (Throwable) e);
            }
            throw new JSchException(e.toString());
        }

        KeyPair kpair = null;
        if (type == DSA) {
            kpair = new KeyPairDSA(jsch);
        } else if (type == DSA_CERT) {
            kpair = new OpenSSHUserCertDSA(jsch);
        } else if (type == RSA) {
            kpair = new KeyPairRSA(jsch);
        } else if (type == RSA_CERT) {
            kpair = new OpenSSHUserCertRSA(jsch);
        } else if (type == ECDSA) {
            kpair = new KeyPairECDSA(jsch);
        } else if (type == ECDSA_CERT) {
            kpair = new OpenSSHUserCertECDSA(jsch);
        } else if (vendor == VENDOR_PKCS8) {
            kpair = new KeyPairPKCS8(jsch);
        }

        if (kpair != null) {
            kpair.encrypted = encrypted;
            kpair.publickeyblob = publickeyblob;
            kpair.vendor = vendor;
            kpair.publicKeyComment = publicKeyComment;
            kpair.cipher = cipher;

            if (encrypted) {
                kpair.encrypted = true;
                kpair.iv = iv;
                kpair.data = data;
            } else {
                if (kpair.parse(data)) {
                    kpair.encrypted = false;
                    return kpair;
                } else {
                    throw new JSchException("invalid privatekey: " + prvkey);
                }
            }
        }

        return kpair;
    }

    static private byte a2b(byte c) {
        if ('0' <= c && c <= '9') {
            return (byte) (c - '0');
        }
        return (byte) (c - 'a' + 10);
    }

    static private byte b2a(byte c) {
        if (0 <= c && c <= 9) {
            return (byte) (c + '0');
        }
        return (byte) (c - 10 + 'A');
    }

    static KeyPair loadPPK(JSch jsch, byte[] buf) throws JSchException {
        byte[] pubkey = null;
        byte[] prvkey = null;
        int lines = 0;

        Buffer buffer = new Buffer(buf);
        java.util.Hashtable v = new java.util.Hashtable();

        while (true) {
            if (!parseHeader(buffer, v)) {
                break;
            }
        }

        String typ = (String) v.get("PuTTY-User-Key-File-2");
        if (typ == null) {
            return null;
        }

        lines = Integer.parseInt((String) v.get("Public-Lines"));
        pubkey = parseLines(buffer, lines);

        while (true) {
            if (!parseHeader(buffer, v)) {
                break;
            }
        }

        lines = Integer.parseInt((String) v.get("Private-Lines"));
        prvkey = parseLines(buffer, lines);

        while (true) {
            if (!parseHeader(buffer, v)) {
                break;
            }
        }

        prvkey = Util.fromBase64(prvkey, 0, prvkey.length);
        pubkey = Util.fromBase64(pubkey, 0, pubkey.length);

        KeyPair kpair = null;

        if (typ.equals("ssh-rsa")) {

            Buffer _buf = new Buffer(pubkey);
            _buf.skip(pubkey.length);

            int len = _buf.getInt();
            _buf.getByte(new byte[len]);             // ssh-rsa
            byte[] pub_array = new byte[_buf.getInt()];
            _buf.getByte(pub_array);
            byte[] n_array = new byte[_buf.getInt()];
            _buf.getByte(n_array);

            kpair = new KeyPairRSA(jsch, n_array, pub_array, null);
        } else if (typ.equals("ssh-dss")) {
            Buffer _buf = new Buffer(pubkey);
            _buf.skip(pubkey.length);

            int len = _buf.getInt();
            _buf.getByte(new byte[len]);              // ssh-dss

            byte[] p_array = new byte[_buf.getInt()];
            _buf.getByte(p_array);
            byte[] q_array = new byte[_buf.getInt()];
            _buf.getByte(q_array);
            byte[] g_array = new byte[_buf.getInt()];
            _buf.getByte(g_array);
            byte[] y_array = new byte[_buf.getInt()];
            _buf.getByte(y_array);

            kpair = new KeyPairDSA(jsch, p_array, q_array, g_array, y_array, null);
        } else {
            return null;
        }

        if (kpair == null) {
            return null;
        }

        kpair.encrypted = !v.get("Encryption").equals("none");
        kpair.vendor = VENDOR_PUTTY;
        kpair.publicKeyComment = (String) v.get("Comment");
        if (kpair.encrypted) {
            if (Session.checkCipher((String) jsch.getConfig("aes256-cbc"))) {
                try {
                    Class c = Class.forName((String) jsch.getConfig("aes256-cbc"));
                    kpair.cipher = (Cipher) (c.newInstance());
                    kpair.iv = new byte[kpair.cipher.getIVSize()];
                } catch (Exception e) {
                    throw new JSchException("The cipher 'aes256-cbc' is required, but it is not available.");
                }
            } else {
                throw new JSchException("The cipher 'aes256-cbc' is required, but it is not available.");
            }
            kpair.data = prvkey;
        } else {
            kpair.data = prvkey;
            kpair.parse(prvkey);
        }
        return kpair;
    }

    private static byte[] parseLines(Buffer buffer, int lines) {
        byte[] buf = buffer.buffer;
        int index = buffer.index;
        byte[] data = null;

        int i = index;
        while (lines-- > 0) {
            while (buf.length > i) {
                if (buf[i++] == 0x0d) {
                    if (data == null) {
                        data = new byte[i - index - 1];
                        System.arraycopy(buf, index, data, 0, i - index - 1);
                    } else {
                        byte[] tmp = new byte[data.length + i - index - 1];
                        System.arraycopy(data, 0, tmp, 0, data.length);
                        System.arraycopy(buf, index, tmp, data.length, i - index - 1);
                        for (int j = 0; j < data.length; j++) {
                            data[j] = 0; // clear
                        }
                        data = tmp;
                    }
                    break;
                }
            }
            if (buf[i] == 0x0a) {
                i++;
            }
            index = i;
        }

        if (data != null) {
            buffer.index = index;
        }

        return data;
    }

    private static boolean parseHeader(Buffer buffer, java.util.Hashtable v) {
        byte[] buf = buffer.buffer;
        int index = buffer.index;
        String key = null;
        String value = null;
        for (int i = index; i < buf.length; i++) {
            if (buf[i] == 0x0d) {
                break;
            }
            if (buf[i] == ':') {
                key = new String(buf, index, i - index);
                i++;
                if (i < buf.length && buf[i] == ' ') {
                    i++;
                }
                index = i;
                break;
            }
        }

        if (key == null) {
            return false;
        }

        for (int i = index; i < buf.length; i++) {
            if (buf[i] == 0x0d) {
                value = new String(buf, index, i - index);
                i++;
                if (i < buf.length && buf[i] == 0x0a) {
                    i++;
                }
                index = i;
                break;
            }
        }

        if (value != null) {
            v.put(key, value);
            buffer.index = index;
        }

        return (key != null && value != null);
    }

    abstract void generate(int key_size) throws JSchException;

    abstract byte[] getBegin();

    abstract byte[] getEnd();

    abstract int getKeySize();

    public abstract byte[] getSignature(byte[] data);

    public abstract Signature getVerifier();

    public abstract byte[] forSSHAgent() throws JSchException;

    public String getPublicKeyComment() {
        return publicKeyComment;
    }

    public void setPublicKeyComment(String publicKeyComment) {
        this.publicKeyComment = publicKeyComment;
    }

    abstract byte[] getPrivateKey();

    /**
     * Writes the plain private key to the given output stream.
     *
     * @param out output stream
     * @see #writePrivateKey(java.io.OutputStream out, byte[] passphrase)
     */
    public void writePrivateKey(java.io.OutputStream out) {
        this.writePrivateKey(out, null);
    }

    /**
     * Writes the cyphered private key to the given output stream.
     *
     * @param out        output stream
     * @param passphrase a passphrase to encrypt the private key
     */
    public void writePrivateKey(java.io.OutputStream out, byte[] passphrase) {
        if (passphrase == null) {
            passphrase = this.passphrase;
        }

        byte[] plain = getPrivateKey();
        byte[][] _iv = new byte[1][];
        byte[] encoded = encrypt(plain, _iv, passphrase);
        if (encoded != plain) {
            Util.bzero(plain);
        }
        byte[] iv = _iv[0];
        byte[] prv = Util.toBase64(encoded, 0, encoded.length);

        try {
            out.write(getBegin());
            out.write(cr);
            if (passphrase != null) {
                out.write(header[0]);
                out.write(cr);
                out.write(header[1]);
                for (int i = 0; i < iv.length; i++) {
                    out.write(b2a((byte) ((iv[i] >>> 4) & 0x0f)));
                    out.write(b2a((byte) (iv[i] & 0x0f)));
                }
                out.write(cr);
                out.write(cr);
            }
            int i = 0;
            while (i < prv.length) {
                if (i + 64 < prv.length) {
                    out.write(prv, i, 64);
                    out.write(cr);
                    i += 64;
                    continue;
                }
                out.write(prv, i, prv.length - i);
                out.write(cr);
                break;
            }
            out.write(getEnd());
            out.write(cr);
            //out.close();
        } catch (Exception e) {
        }
    }

    abstract byte[] getKeyTypeName();

    public abstract int getKeyType();

    /**
     * Returns the blob of the public key.
     *
     * @return blob of the public key
     */
    public byte[] getPublicKeyBlob() {
        // TODO JSchException should be thrown
        //if(publickeyblob == null)
        //  throw new JSchException("public-key blob is not available");
        return publickeyblob;
    }

    /**
     * Writes the public key with the specified comment to the output stream.
     *
     * @param out     output stream
     * @param comment comment
     */
    public void writePublicKey(java.io.OutputStream out, String comment) {
        byte[] pubblob = getPublicKeyBlob();
        byte[] pub = Util.toBase64(pubblob, 0, pubblob.length);
        try {
            out.write(getKeyTypeName());
            out.write(space);
            out.write(pub, 0, pub.length);
            out.write(space);
            out.write(Util.str2byte(comment));
            out.write(cr);
        } catch (Exception e) {
        }
    }

    /**
     * Writes the public key with the specified comment to the file.
     *
     * @param name    file name
     * @param comment comment
     * @see #writePublicKey(java.io.OutputStream out, String comment)
     */
    public void writePublicKey(String name, String comment) throws java.io.FileNotFoundException, java.io.IOException {
        FileOutputStream fos = new FileOutputStream(name);
        writePublicKey(fos, comment);
        fos.close();
    }

    /**
     * Writes the public key with the specified comment to the output stream in
     * the format defined in http://www.ietf.org/rfc/rfc4716.txt
     *
     * @param out     output stream
     * @param comment comment
     */
    public void writeSECSHPublicKey(java.io.OutputStream out, String comment) {
        byte[] pubblob = getPublicKeyBlob();
        byte[] pub = Util.toBase64(pubblob, 0, pubblob.length);
        try {
            out.write(Util.str2byte("---- BEGIN SSH2 PUBLIC KEY ----"));
            out.write(cr);
            out.write(Util.str2byte("Comment: \"" + comment + "\""));
            out.write(cr);
            int index = 0;
            while (index < pub.length) {
                int len = 70;
                if ((pub.length - index) < len) {
                    len = pub.length - index;
                }
                out.write(pub, index, len);
                out.write(cr);
                index += len;
            }
            out.write(Util.str2byte("---- END SSH2 PUBLIC KEY ----"));
            out.write(cr);
        } catch (Exception e) {
        }
    }

    /**
     * Writes the public key with the specified comment to the output stream in
     * the format defined in http://www.ietf.org/rfc/rfc4716.txt
     *
     * @param name    file name
     * @param comment comment
     * @see #writeSECSHPublicKey(java.io.OutputStream out, String comment)
     */
    public void writeSECSHPublicKey(String name, String comment) throws java.io.FileNotFoundException, java.io.IOException {
        FileOutputStream fos = new FileOutputStream(name);
        writeSECSHPublicKey(fos, comment);
        fos.close();
    }

    /**
     * Writes the plain private key to the file.
     *
     * @param name file name
     * @see #writePrivateKey(String name, byte[] passphrase)
     */
    public void writePrivateKey(String name) throws java.io.FileNotFoundException, java.io.IOException {
        this.writePrivateKey(name, null);
    }

    /**
     * Writes the cyphered private key to the file.
     *
     * @param name       file name
     * @param passphrase a passphrase to encrypt the private key
     * @see #writePrivateKey(java.io.OutputStream out, byte[] passphrase)
     */
    public void writePrivateKey(String name, byte[] passphrase) throws java.io.FileNotFoundException, java.io.IOException {
        FileOutputStream fos = new FileOutputStream(name);
        writePrivateKey(fos, passphrase);
        fos.close();
    }

    /**
     * Returns the finger-print of the public key.
     *
     * @return finger print
     */
    public String getFingerPrint() {
        if (hash == null) {
            hash = genHash();
        }
        byte[] kblob = getPublicKeyBlob();
        if (kblob == null) {
            return null;
        }
        return Util.getFingerPrint(hash, kblob);
    }

    private byte[] encrypt(byte[] plain, byte[][] _iv, byte[] passphrase) {
        if (passphrase == null) {
            return plain;
        }

        if (cipher == null) {
            cipher = genCipher();
        }
        byte[] iv = _iv[0] = new byte[cipher.getIVSize()];

        if (random == null) {
            random = genRandom();
        }
        random.fill(iv, 0, iv.length);

        byte[] key = genKey(passphrase, iv);
        byte[] encoded = plain;

        // PKCS#5Padding
        {
            //int bsize=cipher.getBlockSize();
            int bsize = cipher.getIVSize();
            byte[] foo = new byte[(encoded.length / bsize + 1) * bsize];
            System.arraycopy(encoded, 0, foo, 0, encoded.length);
            int padding = bsize - encoded.length % bsize;
            for (int i = foo.length - 1; (foo.length - padding) <= i; i--) {
                foo[i] = (byte) padding;
            }
            encoded = foo;
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            cipher.update(encoded, 0, encoded.length, encoded, 0);
        } catch (Exception e) {
            //System.err.println(e);
        }
        Util.bzero(key);
        return encoded;
    }

    abstract boolean parse(byte[] data);

    private byte[] decrypt(byte[] data, byte[] passphrase, byte[] iv) {

        try {
            byte[] key = genKey(passphrase, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            Util.bzero(key);
            byte[] plain = new byte[data.length];
            cipher.update(data, 0, data.length, plain, 0);
            return plain;
        } catch (Exception e) {
            //System.err.println(e);
        }
        return null;
    }

    int writeSEQUENCE(byte[] buf, int index, int len) {
        buf[index++] = 0x30;
        index = writeLength(buf, index, len);
        return index;
    }

    int writeINTEGER(byte[] buf, int index, byte[] data) {
        buf[index++] = 0x02;
        index = writeLength(buf, index, data.length);
        System.arraycopy(data, 0, buf, index, data.length);
        index += data.length;
        return index;
    }

    int writeOCTETSTRING(byte[] buf, int index, byte[] data) {
        buf[index++] = 0x04;
        index = writeLength(buf, index, data.length);
        System.arraycopy(data, 0, buf, index, data.length);
        index += data.length;
        return index;
    }

    int writeDATA(byte[] buf, byte n, int index, byte[] data) {
        buf[index++] = n;
        index = writeLength(buf, index, data.length);
        System.arraycopy(data, 0, buf, index, data.length);
        index += data.length;
        return index;
    }

    int countLength(int len) {
        int i = 1;
        if (len <= 0x7f) {
            return i;
        }
        while (len > 0) {
            len >>>= 8;
            i++;
        }
        return i;
    }

    int writeLength(byte[] data, int index, int len) {
        int i = countLength(len) - 1;
        if (i == 0) {
            data[index++] = (byte) len;
            return index;
        }
        data[index++] = (byte) (0x80 | i);
        int j = index + i;
        while (i > 0) {
            data[index + i - 1] = (byte) (len & 0xff);
            len >>>= 8;
            i--;
        }
        return j;
    }

    private Random genRandom() {
        if (random == null) {
            try {
                Class c = Class.forName(jsch.getConfig("random"));
                random = (Random) (c.newInstance());
            } catch (Exception e) {
                System.err.println("connect: random " + e);
            }
        }
        return random;
    }

    private HASH genHash() {
        try {
            Class c = Class.forName(jsch.getConfig("md5"));
            hash = (HASH) (c.newInstance());
            hash.init();
        } catch (Exception e) {
        }
        return hash;
    }

    private Cipher genCipher() {
        try {
            Class c;
            c = Class.forName(jsch.getConfig("3des-cbc"));
            cipher = (Cipher) (c.newInstance());
        } catch (Exception e) {
        }
        return cipher;
    }

    /*
      hash is MD5
      h(0) <- hash(passphrase, iv);
      h(n) <- hash(h(n-1), passphrase, iv);
      key <- (h(0),...,h(n))[0,..,key.length];
    */
    synchronized byte[] genKey(byte[] passphrase, byte[] iv) {
        if (cipher == null) {
            cipher = genCipher();
        }
        if (hash == null) {
            hash = genHash();
        }

        byte[] key = new byte[cipher.getBlockSize()];
        int hsize = hash.getBlockSize();
        byte[] hn = new byte[key.length / hsize * hsize +
                (key.length % hsize == 0 ? 0 : hsize)];
        try {
            byte[] tmp = null;
            if (vendor == VENDOR_OPENSSH) {
                for (int index = 0; index + hsize <= hn.length; ) {
                    if (tmp != null) {
                        hash.update(tmp, 0, tmp.length);
                    }
                    hash.update(passphrase, 0, passphrase.length);
                    hash.update(iv, 0, iv.length > 8 ? 8 : iv.length);
                    tmp = hash.digest();
                    System.arraycopy(tmp, 0, hn, index, tmp.length);
                    index += tmp.length;
                }
                System.arraycopy(hn, 0, key, 0, key.length);
            } else if (vendor == VENDOR_FSECURE) {
                for (int index = 0; index + hsize <= hn.length; ) {
                    if (tmp != null) {
                        hash.update(tmp, 0, tmp.length);
                    }
                    hash.update(passphrase, 0, passphrase.length);
                    tmp = hash.digest();
                    System.arraycopy(tmp, 0, hn, index, tmp.length);
                    index += tmp.length;
                }
                System.arraycopy(hn, 0, key, 0, key.length);
            } else if (vendor == VENDOR_PUTTY) {
                Class c = Class.forName((String) jsch.getConfig("sha-1"));
                HASH sha1 = (HASH) (c.newInstance());
                tmp = new byte[4];
                key = new byte[20 * 2];
                for (int i = 0; i < 2; i++) {
                    sha1.init();
                    tmp[3] = (byte) i;
                    sha1.update(tmp, 0, tmp.length);
                    sha1.update(passphrase, 0, passphrase.length);
                    System.arraycopy(sha1.digest(), 0, key, i * 20, 20);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return key;
    }

    /**
     * @deprecated use #writePrivateKey(java.io.OutputStream out, byte[] passphrase)
     */
    public void setPassphrase(String passphrase) {
        if (passphrase == null || passphrase.length() == 0) {
            setPassphrase((byte[]) null);
        } else {
            setPassphrase(Util.str2byte(passphrase));
        }
    }

    /**
     * @deprecated use #writePrivateKey(String name, byte[] passphrase)
     */
    public void setPassphrase(byte[] passphrase) {
        if (passphrase != null && passphrase.length == 0) {
            passphrase = null;
        }
        this.passphrase = passphrase;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public boolean decrypt(String _passphrase) {
        if (_passphrase == null || _passphrase.length() == 0) {
            return !encrypted;
        }
        return decrypt(Util.str2byte(_passphrase));
    }

    public boolean decrypt(byte[] _passphrase) {

        if (!encrypted) {
            return true;
        }
        if (_passphrase == null) {
            return !encrypted;
        }
        byte[] bar = new byte[_passphrase.length];
        System.arraycopy(_passphrase, 0, bar, 0, bar.length);
        _passphrase = bar;
        byte[] foo = decrypt(data, _passphrase, iv);
        Util.bzero(_passphrase);
        if (parse(foo)) {
            encrypted = false;
        }
        return !encrypted;
    }

    public void dispose() {
        Util.bzero(passphrase);
    }

    public void finalize() {
        dispose();
    }

    void copy(KeyPair kpair) {
        this.publickeyblob = kpair.publickeyblob;
        this.vendor = kpair.vendor;
        this.publicKeyComment = kpair.publicKeyComment;
        this.cipher = kpair.cipher;
    }

    class ASN1Exception extends Exception {
    }

    class ASN1 {
        byte[] buf;
        int start;
        int length;

        ASN1(byte[] buf) throws ASN1Exception {
            this(buf, 0, buf.length);
        }

        ASN1(byte[] buf, int start, int length) throws ASN1Exception {
            this.buf = buf;
            this.start = start;
            this.length = length;
            if (start + length > buf.length) {
                throw new ASN1Exception();
            }
        }

        int getType() {
            return buf[start] & 0xff;
        }

        boolean isSEQUENCE() {
            return getType() == (0x30 & 0xff);
        }

        boolean isINTEGER() {
            return getType() == (0x02 & 0xff);
        }

        boolean isOBJECT() {
            return getType() == (0x06 & 0xff);
        }

        boolean isOCTETSTRING() {
            return getType() == (0x04 & 0xff);
        }

        private int getLength(int[] indexp) {
            int index = indexp[0];
            int length = buf[index++] & 0xff;
            if ((length & 0x80) != 0) {
                int foo = length & 0x7f;
                length = 0;
                while (foo-- > 0) {
                    length = (length << 8) + (buf[index++] & 0xff);
                }
            }
            indexp[0] = index;
            return length;
        }

        byte[] getContent() {
            int[] indexp = new int[1];
            indexp[0] = start + 1;
            int length = getLength(indexp);
            int index = indexp[0];
            byte[] tmp = new byte[length];
            System.arraycopy(buf, index, tmp, 0, tmp.length);
            return tmp;
        }

        ASN1[] getContents() throws ASN1Exception {
            int typ = buf[start];
            int[] indexp = new int[1];
            indexp[0] = start + 1;
            int length = getLength(indexp);
            if (typ == 0x05) {
                return new ASN1[0];
            }
            int index = indexp[0];
            java.util.Vector values = new java.util.Vector();
            while (length > 0) {
                index++;
                length--;
                int tmp = index;
                indexp[0] = index;
                int l = getLength(indexp);
                index = indexp[0];
                length -= (index - tmp);
                values.addElement(new ASN1(buf, tmp - 1, 1 + (index - tmp) + l));
                index += l;
                length -= l;
            }
            ASN1[] result = new ASN1[values.size()];
            for (int i = 0; i < values.size(); i++) {
                result[i] = (ASN1) values.elementAt(i);
            }
            return result;
        }
    }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2013-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class KeyPairPKCS8 extends KeyPair {
  private static final byte[] rsaEncryption = {
    (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x86,
    (byte)0xf7, (byte)0x0d, (byte)0x01, (byte)0x01, (byte)0x01
  };

  private static final byte[] dsaEncryption = {
    (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0xce,
    (byte)0x38, (byte)0x04, (byte)0x1
  };

  private static final byte[] pbes2 = {
    (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xf7,
    (byte)0x0d, (byte)0x01, (byte)0x05, (byte)0x0d
  };

  private static final byte[] pbkdf2 = {
    (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xf7,
    (byte)0x0d, (byte)0x01, (byte)0x05, (byte)0x0c
  };

  private static final byte[] aes128cbc = {
    (byte)0x60, (byte)0x86, (byte)0x48, (byte)0x01, (byte)0x65,
    (byte)0x03, (byte)0x04, (byte)0x01, (byte)0x02
  };

  private static final byte[] aes192cbc = {
    (byte)0x60, (byte)0x86, (byte)0x48, (byte)0x01, (byte)0x65,
    (byte)0x03, (byte)0x04, (byte)0x01, (byte)0x16
  };

  private static final byte[] aes256cbc = {
    (byte)0x60, (byte)0x86, (byte)0x48, (byte)0x01, (byte)0x65,
    (byte)0x03, (byte)0x04, (byte)0x01, (byte)0x2a
  };

  private static final byte[] pbeWithMD5AndDESCBC = {
    (byte)0x2a, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xf7,
    (byte)0x0d, (byte)0x01, (byte)0x05, (byte)0x03
  };

  private KeyPair kpair = null;

  public KeyPairPKCS8(JSch jsch){
    super(jsch);
  }

  void generate(int key_size) throws JSchException{
  }

  private static final byte[] begin=Util.str2byte("-----BEGIN DSA PRIVATE KEY-----");
  private static final byte[] end=Util.str2byte("-----END DSA PRIVATE KEY-----");

  byte[] getBegin(){ return begin; }
  byte[] getEnd(){ return end; }

  byte[] getPrivateKey(){
    return null;
  }

  boolean parse(byte[] plain){

    /* from RFC5208
      PrivateKeyInfo ::= SEQUENCE {
        version                   Version,
        privateKeyAlgorithm       PrivateKeyAlgorithmIdentifier,
        privateKey                PrivateKey,
        attributes           [0]  IMPLICIT Attributes OPTIONAL
      }
      Version ::= INTEGER
      PrivateKeyAlgorithmIdentifier ::= AlgorithmIdentifier
      PrivateKey ::= OCTET STRING
      Attributes ::= SET OF Attribute
    }
    */

    try{
      Vector values = new Vector();

      ASN1[] contents = null;
      ASN1 asn1 = new ASN1(plain);
      contents = asn1.getContents();

      ASN1 privateKeyAlgorithm = contents[1];
      ASN1 privateKey = contents[2];

      contents = privateKeyAlgorithm.getContents();
      byte[] privateKeyAlgorithmID = contents[0].getContent();
      contents = contents[1].getContents();
      if(contents.length>0){
        for(int i = 0; i < contents.length; i++){
          values.addElement(contents[i].getContent());
        }
      }

      byte[] _data = privateKey.getContent();

      KeyPair _kpair = null;
      if(Util.array_equals(privateKeyAlgorithmID, rsaEncryption)){
        _kpair = new KeyPairRSA(jsch);
        _kpair.copy(this);
        if(_kpair.parse(_data)){
          kpair = _kpair;
        }
      }
      else if(Util.array_equals(privateKeyAlgorithmID, dsaEncryption)){
        asn1 = new ASN1(_data);
        if(values.size() == 0) {  // embedded DSA parameters format
          /*
             SEQUENCE
               SEQUENCE
                 INTEGER    // P_array
                 INTEGER    // Q_array
                 INTEGER    // G_array
               INTEGER      // prv_array
          */
          contents = asn1.getContents();
          byte[] bar = contents[1].getContent();
          contents = contents[0].getContents();
          for(int i = 0; i < contents.length; i++){
            values.addElement(contents[i].getContent());
          }
          values.addElement(bar);
        }
        else {
          /*
             INTEGER      // prv_array
          */
          values.addElement(asn1.getContent());
        }

        byte[] P_array = (byte[])values.elementAt(0);
        byte[] Q_array = (byte[])values.elementAt(1);
        byte[] G_array = (byte[])values.elementAt(2);
        byte[] prv_array = (byte[])values.elementAt(3);
        // Y = g^X mode p
        byte[] pub_array =
          (new BigInteger(G_array)).
            modPow(new BigInteger(prv_array), new BigInteger(P_array)).
            toByteArray();

        KeyPairDSA _key = new KeyPairDSA(jsch,
                                         P_array, Q_array, G_array,
                                         pub_array, prv_array);
        plain = _key.getPrivateKey();

        _kpair = new KeyPairDSA(jsch);
        _kpair.copy(this);
        if(_kpair.parse(plain)){
          kpair = _kpair;
        }
      }
    }
    catch(ASN1Exception e){
      return false;
    }
    catch(Exception e){
      //System.err.println(e);
      return false;
    }
    return kpair != null;
  }

  public byte[] getPublicKeyBlob(){
    return kpair.getPublicKeyBlob();
  }

  byte[] getKeyTypeName(){ return kpair.getKeyTypeName();}
  public int getKeyType(){return kpair.getKeyType();}

  public int getKeySize(){
    return kpair.getKeySize();
  }

  public byte[] getSignature(byte[] data){
    return kpair.getSignature(data);
  }

  public Signature getVerifier(){
    return kpair.getVerifier();
  }

  public byte[] forSSHAgent() throws JSchException {
    return kpair.forSSHAgent();
  }

  public boolean decrypt(byte[] _passphrase){
    if(!isEncrypted()){
      return true;
    }
    if(_passphrase==null){
      return !isEncrypted();
    }

    /*
      SEQUENCE
        SEQUENCE
          OBJECT            :PBES2
          SEQUENCE
            SEQUENCE
              OBJECT            :PBKDF2
              SEQUENCE
                OCTET STRING      [HEX DUMP]:E4E24ADC9C00BD4D
                INTEGER           :0800
            SEQUENCE
              OBJECT            :aes-128-cbc
              OCTET STRING      [HEX DUMP]:5B66E6B3BF03944C92317BC370CC3AD0
        OCTET STRING      [HEX DUMP]:

or

      SEQUENCE
        SEQUENCE
          OBJECT            :pbeWithMD5AndDES-CBC
          SEQUENCE
            OCTET STRING      [HEX DUMP]:DBF75ECB69E3C0FC
            INTEGER           :0800
        OCTET STRING      [HEX DUMP]
    */

    try{

      ASN1[] contents = null;
      ASN1 asn1 = new ASN1(data);

      contents =  asn1.getContents();

      byte[] _data = contents[1].getContent();

      ASN1 pbes = contents[0];
      contents = pbes.getContents();
      byte[] pbesid = contents[0].getContent();
      ASN1 pbesparam = contents[1];

      byte[] salt = null;
      int iterations = 0;
      byte[] iv = null;
      byte[] encryptfuncid = null;

      if(Util.array_equals(pbesid, pbes2)){
        contents = pbesparam.getContents();
        ASN1 pbkdf = contents[0];
        ASN1 encryptfunc = contents[1];
        contents = pbkdf.getContents();
        byte[] pbkdfid = contents[0].getContent();
        ASN1 pbkdffunc = contents[1];
        contents = pbkdffunc.getContents();
        salt = contents[0].getContent();
        iterations =
          Integer.parseInt((new BigInteger(contents[1].getContent())).toString());

        contents = encryptfunc.getContents();
        encryptfuncid = contents[0].getContent();
        iv = contents[1].getContent();
      }
      else if(Util.array_equals(pbesid, pbeWithMD5AndDESCBC)){
        // not supported
        return false;
      }
      else {
        return false;
      }

      Cipher cipher=getCipher(encryptfuncid);
      if(cipher==null) return false;

      byte[] key=null;
      try{
        Class c=Class.forName((String)jsch.getConfig("pbkdf"));
        PBKDF tmp=(PBKDF)(c.newInstance());
        key = tmp.getKey(_passphrase, salt, iterations, cipher.getBlockSize());
      }
      catch(Exception ee){
      }

      if(key==null){
        return false;
      }

      cipher.init(Cipher.DECRYPT_MODE, key, iv);
      Util.bzero(key);
      byte[] plain=new byte[_data.length];
      cipher.update(_data, 0, _data.length, plain, 0);
      if(parse(plain)){
        encrypted=false;
        return true;
      }
    }
    catch(ASN1Exception e){
      // System.err.println(e);
    }
    catch(Exception e){
      // System.err.println(e);
    }

    return false;
  }

  Cipher getCipher(byte[] id){
    Cipher cipher=null;
    String name = null;
    try{
      if(Util.array_equals(id, aes128cbc)){
        name="aes128-cbc";
      }
      else if(Util.array_equals(id, aes192cbc)){
        name="aes192-cbc";
      }
      else if(Util.array_equals(id, aes256cbc)){
        name="aes256-cbc";
      }
      Class c=Class.forName((String)jsch.getConfig(name));
      cipher=(Cipher)(c.newInstance());
    }
    catch(Exception e){
      if(JSch.getLogger().isEnabled(Logger.FATAL)){
        String message="";
        if(name==null){
          message="unknown oid: "+Util.toHex(id);
        }
        else {
          message="function "+name+" is not supported";
        }
        JSch.getLogger().log(Logger.FATAL, "PKCS8: "+message);
      }
    }
    return cipher;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class KeyPairRSA extends KeyPair{
  private byte[] n_array;   // modulus   p multiply q
  private byte[] pub_array; // e
  private byte[] prv_array; // d         e^-1 mod (p-1)(q-1)

  private byte[] p_array;  // prime p
  private byte[] q_array;  // prime q
  private byte[] ep_array; // prime exponent p  dmp1 == prv mod (p-1)
  private byte[] eq_array; // prime exponent q  dmq1 == prv mod (q-1)
  private byte[] c_array;  // coefficient  iqmp == modinv(q, p) == q^-1 mod p

  private int key_size=1024;

  public KeyPairRSA(JSch jsch){
    this(jsch, null, null, null);
  }

  public KeyPairRSA(JSch jsch,
                    byte[] n_array,
                    byte[] pub_array,
                    byte[] prv_array){
    super(jsch);
    this.n_array = n_array;
    this.pub_array = pub_array;
    this.prv_array = prv_array;
    if(n_array!=null){
      key_size = (new java.math.BigInteger(n_array)).bitLength();
    }
  }

  void generate(int key_size) throws JSchException{
    this.key_size=key_size;
    try{
      Class c=Class.forName(jsch.getConfig("keypairgen.rsa"));
      KeyPairGenRSA keypairgen=(KeyPairGenRSA)(c.newInstance());
      keypairgen.init(key_size);
      pub_array=keypairgen.getE();
      prv_array=keypairgen.getD();
      n_array=keypairgen.getN();

      p_array=keypairgen.getP();
      q_array=keypairgen.getQ();
      ep_array=keypairgen.getEP();
      eq_array=keypairgen.getEQ();
      c_array=keypairgen.getC();

      keypairgen=null;
    }
    catch(Exception e){
      //System.err.println("KeyPairRSA: "+e);
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }
  }

  private static final byte[] begin=Util.str2byte("-----BEGIN RSA PRIVATE KEY-----");
  private static final byte[] end=Util.str2byte("-----END RSA PRIVATE KEY-----");

  byte[] getBegin(){ return begin; }
  byte[] getEnd(){ return end; }

  byte[] getPrivateKey(){
    int content=
      1+countLength(1) + 1 +                           // INTEGER
      1+countLength(n_array.length) + n_array.length + // INTEGER  N
      1+countLength(pub_array.length) + pub_array.length + // INTEGER  pub
      1+countLength(prv_array.length) + prv_array.length+  // INTEGER  prv
      1+countLength(p_array.length) + p_array.length+      // INTEGER  p
      1+countLength(q_array.length) + q_array.length+      // INTEGER  q
      1+countLength(ep_array.length) + ep_array.length+    // INTEGER  ep
      1+countLength(eq_array.length) + eq_array.length+    // INTEGER  eq
      1+countLength(c_array.length) + c_array.length;      // INTEGER  c

    int total=
      1+countLength(content)+content;   // SEQUENCE

    byte[] plain=new byte[total];
    int index=0;
    index=writeSEQUENCE(plain, index, content);
    index=writeINTEGER(plain, index, new byte[1]);  // 0
    index=writeINTEGER(plain, index, n_array);
    index=writeINTEGER(plain, index, pub_array);
    index=writeINTEGER(plain, index, prv_array);
    index=writeINTEGER(plain, index, p_array);
    index=writeINTEGER(plain, index, q_array);
    index=writeINTEGER(plain, index, ep_array);
    index=writeINTEGER(plain, index, eq_array);
    index=writeINTEGER(plain, index, c_array);
    return plain;
  }

  boolean parse(byte [] plain){

    try{
      int index=0;
      int length=0;

      if(vendor==VENDOR_PUTTY){
        Buffer buf = new Buffer(plain);
        buf.skip(plain.length);

        try {
          byte[][] tmp = buf.getBytes(4, "");
          prv_array = tmp[0];
          p_array = tmp[1];
          q_array = tmp[2];
          c_array = tmp[3];
        }
        catch(JSchException e){
          return false;
        }

        getEPArray();
        getEQArray();

        return true;
      }

      if(vendor==VENDOR_FSECURE){
        if(plain[index]!=0x30){                  // FSecure
          Buffer buf=new Buffer(plain);
          pub_array=buf.getMPIntBits();
          prv_array=buf.getMPIntBits();
          n_array=buf.getMPIntBits();
          byte[] u_array=buf.getMPIntBits();
          p_array=buf.getMPIntBits();
          q_array=buf.getMPIntBits();
          if(n_array!=null){
            key_size = (new java.math.BigInteger(n_array)).bitLength();
          }

          getEPArray();
          getEQArray();
          getCArray();

          return true;
        }
        return false;
      }

      /*
        Key must be in the following ASN.1 DER encoding,
        RSAPrivateKey ::= SEQUENCE {
          version           Version,
          modulus           INTEGER,  -- n
          publicExponent    INTEGER,  -- e
          privateExponent   INTEGER,  -- d
          prime1            INTEGER,  -- p
          prime2            INTEGER,  -- q
          exponent1         INTEGER,  -- d mod (p-1)
          exponent2         INTEGER,  -- d mod (q-1)
          coefficient       INTEGER,  -- (inverse of q) mod p
          otherPrimeInfos   OtherPrimeInfos OPTIONAL
        }
      */

      index++; // SEQUENCE
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }

      if(plain[index]!=0x02)return false;
      index++; // INTEGER
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      n_array=new byte[length];
      System.arraycopy(plain, index, n_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      pub_array=new byte[length];
      System.arraycopy(plain, index, pub_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      prv_array=new byte[length];
      System.arraycopy(plain, index, prv_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      p_array=new byte[length];
      System.arraycopy(plain, index, p_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      q_array=new byte[length];
      System.arraycopy(plain, index, q_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      ep_array=new byte[length];
      System.arraycopy(plain, index, ep_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      eq_array=new byte[length];
      System.arraycopy(plain, index, eq_array, 0, length);
      index+=length;

      index++;
      length=plain[index++]&0xff;
      if((length&0x80)!=0){
        int foo=length&0x7f; length=0;
        while(foo-->0){ length=(length<<8)+(plain[index++]&0xff); }
      }
      c_array=new byte[length];
      System.arraycopy(plain, index, c_array, 0, length);
      index+=length;

      if(n_array!=null){
        key_size = (new java.math.BigInteger(n_array)).bitLength();
      }

    }
    catch(Exception e){
      //System.err.println(e);
      return false;
    }
    return true;
  }

  public byte[] getPublicKeyBlob(){
    byte[] foo=super.getPublicKeyBlob();
    if(foo!=null) return foo;

    if(pub_array==null) return null;
    byte[][] tmp = new byte[3][];
    tmp[0] = sshrsa;
    tmp[1] = pub_array;
    tmp[2] = n_array;
    return Buffer.fromBytes(tmp).buffer;
  }

  private static final byte[] sshrsa=Util.str2byte("ssh-rsa");
  byte[] getKeyTypeName(){return sshrsa;}
  public int getKeyType(){return RSA;}

  public int getKeySize(){
    return key_size;
  }

  public byte[] getSignature(byte[] data){
    try{
      Class c=Class.forName((String)jsch.getConfig("signature.rsa"));
      SignatureRSA rsa=(SignatureRSA)(c.newInstance());
      rsa.init();
      rsa.setPrvKey(prv_array, n_array);

      rsa.update(data);
      byte[] sig = rsa.sign();
      byte[][] tmp = new byte[2][];
      tmp[0] = sshrsa;
      tmp[1] = sig;
      return Buffer.fromBytes(tmp).buffer;
    }
    catch(Exception e){
    }
    return null;
  }

  public Signature getVerifier(){
    try{
      Class c=Class.forName((String)jsch.getConfig("signature.rsa"));
      SignatureRSA rsa=(SignatureRSA)(c.newInstance());
      rsa.init();

      if(pub_array == null && n_array == null && getPublicKeyBlob()!=null){
        Buffer buf = new Buffer(getPublicKeyBlob());
        buf.getString();
        pub_array = buf.getString();
        n_array = buf.getString();
      }

      rsa.setPubKey(pub_array, n_array);
      return rsa;
    }
    catch(Exception e){
    }
    return null;
  }

  static KeyPair fromSSHAgent(JSch jsch, Buffer buf) throws JSchException {

    byte[][] tmp = buf.getBytes(8, "invalid key format");

    byte[] n_array = tmp[1];
    byte[] pub_array = tmp[2];
    byte[] prv_array = tmp[3];
    KeyPairRSA kpair = new KeyPairRSA(jsch, n_array, pub_array, prv_array);
    kpair.c_array = tmp[4];     // iqmp
    kpair.p_array = tmp[5];
    kpair.q_array = tmp[6];
    kpair.publicKeyComment = new String(tmp[7]);
    kpair.vendor=VENDOR_OPENSSH;
    return kpair;
  }

  public byte[] forSSHAgent() throws JSchException {
    if(isEncrypted()){
      throw new JSchException("key is encrypted.");
    }
    Buffer buf = new Buffer();
    buf.putString(sshrsa);
    buf.putString(n_array);
    buf.putString(pub_array);
    buf.putString(prv_array);
    buf.putString(getCArray());
    buf.putString(p_array);
    buf.putString(q_array);
    buf.putString(Util.str2byte(publicKeyComment));
    byte[] result = new byte[buf.getLength()];
    buf.getByte(result, 0, result.length);
    return result;
  }

  private byte[] getEPArray(){
    if(ep_array==null){
      ep_array=(new BigInteger(prv_array)).mod(new BigInteger(p_array).subtract(BigInteger.ONE)).toByteArray();
    }
    return ep_array;
  }

  private byte[] getEQArray(){
    if(eq_array==null){
      eq_array=(new BigInteger(prv_array)).mod(new BigInteger(q_array).subtract(BigInteger.ONE)).toByteArray();
    }
    return eq_array;
  }

  private byte[] getCArray(){
    if(c_array==null){
      c_array=(new BigInteger(q_array)).modInverse(new BigInteger(p_array)).toByteArray();
    }
    return c_array;
  }

  public void dispose(){
    super.dispose();
    Util.bzero(prv_array);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class KnownHosts implements HostKeyRepository{
  private static final String _known_hosts="known_hosts";

  private JSch jsch=null;
  private String known_hosts=null;
  private java.util.Vector pool=null;

  private MAC hmacsha1=null;

  KnownHosts(JSch jsch){
    super();
    this.jsch=jsch;
    this.hmacsha1 = getHMACSHA1();
    pool=new java.util.Vector();
  }

  void setKnownHosts(String filename) throws JSchException{
    try{
      known_hosts = filename;
      FileInputStream fis=new FileInputStream(Util.checkTilde(filename));
      setKnownHosts(fis);
    }
    catch(FileNotFoundException e){
      // The non-existing file should be allowed.
    }
  }
  void setKnownHosts(InputStream input) throws JSchException{
    pool.removeAllElements();
    StringBuffer sb=new StringBuffer();
    byte i;
    int j;
    boolean error=false;
    try{
      InputStream fis=input;
      String host;
      String key=null;
      int type;
      byte[] buf=new byte[1024];
      int bufl=0;
loop:
      while(true){
        bufl=0;
        while(true){
          j=fis.read();
          if(j==-1){
            if(bufl==0){ break loop; }
            else{ break; }
          }
          if(j==0x0d){ continue; }
          if(j==0x0a){ break; }
          if(buf.length<=bufl){
            if(bufl>1024*10) break;   // too long...
            byte[] newbuf=new byte[buf.length*2];
            System.arraycopy(buf, 0, newbuf, 0, buf.length);
            buf=newbuf;
          }
          buf[bufl++]=(byte)j;
        }

        j=0;
        while(j<bufl){
          i=buf[j];
          if(i==' '||i=='\t'){ j++; continue; }
          if(i=='#'){
            addInvalidLine(Util.byte2str(buf, 0, bufl));
            continue loop;
          }
          break;
        }
        if(j>=bufl){
          addInvalidLine(Util.byte2str(buf, 0, bufl));
          continue loop;
        }

        sb.setLength(0);
        while(j<bufl){
          i=buf[j++];
          if(i==0x20 || i=='\t'){ break; }
          sb.append((char)i);
        }
        host=sb.toString();
        if(j>=bufl || host.length()==0){
          addInvalidLine(Util.byte2str(buf, 0, bufl));
          continue loop;
        }

        while(j<bufl){
          i=buf[j];
          if(i==' '||i=='\t'){ j++; continue; }
          break;
        }

        String marker="";
        if(host.charAt(0) == '@'){
          marker = host;

          sb.setLength(0);
          while(j<bufl){
            i=buf[j++];
            if(i==0x20 || i=='\t'){ break; }
            sb.append((char)i);
          }
          host=sb.toString();
          if(j>=bufl || host.length()==0){
            addInvalidLine(Util.byte2str(buf, 0, bufl));
            continue loop;
          }

          while(j<bufl){
            i=buf[j];
            if(i==' '||i=='\t'){ j++; continue; }
            break;
          }
        }

        sb.setLength(0);
        type=-1;
        while(j<bufl){
          i=buf[j++];
          if(i==0x20 || i=='\t'){ break; }
          sb.append((char)i);
        }
        String tmp = sb.toString();
        if(HostKey.name2type(tmp)!=HostKey.UNKNOWN){
          type=HostKey.name2type(tmp);
        }
        else { j=bufl; }
        if(j>=bufl){
          addInvalidLine(Util.byte2str(buf, 0, bufl));
          continue loop;
        }

        while(j<bufl){
          i=buf[j];
          if(i==' '||i=='\t'){ j++; continue; }
          break;
        }

        sb.setLength(0);
        while(j<bufl){
          i=buf[j++];
          if(i==0x0d){ continue; }
          if(i==0x0a){ break; }
          if(i==0x20 || i=='\t'){ break; }
          sb.append((char)i);
        }
        key=sb.toString();
        if(key.length()==0){
          addInvalidLine(Util.byte2str(buf, 0, bufl));
          continue loop;
        }

        while(j<bufl){
          i=buf[j];
          if(i==' '||i=='\t'){ j++; continue; }
          break;
        }

        /**
          "man sshd" has following descriptions,
            Note that the lines in these files are typically hundreds
            of characters long, and you definitely don't want to type
            in the host keys by hand.  Rather, generate them by a script,
            ssh-keyscan(1) or by taking /usr/local/etc/ssh_host_key.pub and
            adding the host names at the front.
          This means that a comment is allowed to appear at the end of each
          key entry.
        */
        String comment=null;
        if(j<bufl){
          sb.setLength(0);
          while(j<bufl){
            i=buf[j++];
            if(i==0x0d){ continue; }
            if(i==0x0a){ break; }
            sb.append((char)i);
          }
          comment=sb.toString();
        }

        //System.err.println(host);
        //System.err.println("|"+key+"|");

        HostKey hk = null;
        hk = new HashedHostKey(marker, host, type,
                               Util.fromBase64(Util.str2byte(key), 0,
                                               key.length()), comment);
        pool.addElement(hk);
      }
      if(error){
        throw new JSchException("KnownHosts: invalid format");
      }
    }
    catch(Exception e){
      if(e instanceof JSchException)
        throw (JSchException)e;
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }
    finally {
      try{ input.close(); }
      catch(IOException e){
        throw new JSchException(e.toString(), (Throwable)e);
      }
    }
  }
  private void addInvalidLine(String line) throws JSchException {
    HostKey hk = new HostKey(line, HostKey.UNKNOWN, null);
    pool.addElement(hk);
  }
  String getKnownHostsFile(){ return known_hosts; }
  public String getKnownHostsRepositoryID(){ return known_hosts; }

  public int check(String host, byte[] key){
    int result=NOT_INCLUDED;
    if(host==null){
      return result;
    }

    HostKey hk = null;
    try {
      hk = new HostKey(host, HostKey.GUESS, key);
    }
    catch(JSchException e){  // unsupported key
      return result;
    }

    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        HostKey _hk=(HostKey)(pool.elementAt(i));
        if(_hk.isMatched(host) && _hk.type==hk.type){
          if(Util.array_equals(_hk.key, key)){
            return OK;
          }
          else{
            result=CHANGED;
          }
        }
      }
    }

    if(result==NOT_INCLUDED &&
       host.startsWith("[") &&
       host.indexOf("]:")>1
       ){
      return check(host.substring(1, host.indexOf("]:")), key);
    }

    return result;
  }

  public void add(HostKey hostkey, UserInfo userinfo){
    int type=hostkey.type;
    String host=hostkey.getHost();
    byte[] key=hostkey.key;

    HostKey hk=null;
    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        hk=(HostKey)(pool.elementAt(i));
        if(hk.isMatched(host) && hk.type==type){
/*
          if(Util.array_equals(hk.key, key)){ return; }
          if(hk.host.equals(host)){
            hk.key=key;
            return;
          }
          else{
            hk.host=deleteSubString(hk.host, host);
            break;
          }
*/
        }
      }
    }

    hk=hostkey;

    pool.addElement(hk);

    String bar=getKnownHostsRepositoryID();
    if(bar!=null){
      boolean foo=true;
      File goo=new File(Util.checkTilde(bar));
      if(!goo.exists()){
        foo=false;
        if(userinfo!=null){
          foo=userinfo.promptYesNo(bar+" does not exist.\n"+
                                   "Are you sure you want to create it?"
                                   );
          goo=goo.getParentFile();
          if(foo && goo!=null && !goo.exists()){
            foo=userinfo.promptYesNo("The parent directory "+goo+" does not exist.\n"+
                                     "Are you sure you want to create it?"
                                     );
            if(foo){
              if(!goo.mkdirs()){
                userinfo.showMessage(goo+" has not been created.");
                foo=false;
              }
              else{
                userinfo.showMessage(goo+" has been succesfully created.\nPlease check its access permission.");
              }
            }
          }
          if(goo==null)foo=false;
        }
      }
      if(foo){
        try{
          sync(bar);
        }
        catch(Exception e){ System.err.println("sync known_hosts: "+e); }
      }
    }
  }

  public HostKey[] getHostKey(){
    return getHostKey(null, (String)null);
  }
  public HostKey[] getHostKey(String host, String type){
    synchronized(pool){
      java.util.ArrayList v = new java.util.ArrayList();
      for(int i=0; i<pool.size(); i++){
        HostKey hk=(HostKey)pool.elementAt(i);
        if(hk.type==HostKey.UNKNOWN) continue;
        if(host==null ||
           (hk.isMatched(host) &&
            (type==null || hk.getType().equals(type)))){
          v.add(hk);
        }
      }
      HostKey[] foo = new HostKey[v.size()];
      for(int i=0; i<v.size(); i++){
        foo[i] = (HostKey)v.get(i);
      }
      if(host != null && host.startsWith("[") && host.indexOf("]:")>1){
        HostKey[] tmp =
          getHostKey(host.substring(1, host.indexOf("]:")), type);
        if(tmp.length > 0){
          HostKey[] bar = new HostKey[foo.length + tmp.length];
          System.arraycopy(foo, 0, bar, 0, foo.length);
          System.arraycopy(tmp, 0, bar, foo.length, tmp.length);
          foo = bar;
        }
      }
      return foo;
    }
  }
  public void remove(String host, String type){
    remove(host, type, null);
  }
  public void remove(String host, String type, byte[] key){
    boolean sync=false;
    synchronized(pool){
    for(int i=0; i<pool.size(); i++){
      HostKey hk=(HostKey)(pool.elementAt(i));
      if(host==null ||
         (hk.isMatched(host) &&
          (type==null || (hk.getType().equals(type) &&
                          (key==null || Util.array_equals(key, hk.key)))))){
        String hosts=hk.getHost();
        if(hosts.equals(host) ||
           ((hk instanceof HashedHostKey) &&
            ((HashedHostKey)hk).isHashed())){
          pool.removeElement(hk);
        }
        else{
          hk.host=deleteSubString(hosts, host);
        }
        sync=true;
      }
    }
    }
    if(sync){
      try{sync();}catch(Exception e){};
    }
  }

  protected void sync() throws IOException {
    if(known_hosts!=null)
      sync(known_hosts);
  }
  protected synchronized void sync(String foo) throws IOException {
    if(foo==null) return;
    FileOutputStream fos=new FileOutputStream(Util.checkTilde(foo));
    dump(fos);
    fos.close();
  }

  private static final byte[] space={(byte)0x20};
  private static final byte[] cr=Util.str2byte("\n");
  void dump(OutputStream out) throws IOException {
    try{
      HostKey hk;
      synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        hk=(HostKey)(pool.elementAt(i));
        //hk.dump(out);
        String marker=hk.getMarker();
        String host=hk.getHost();
        String type=hk.getType();
        String comment = hk.getComment();
        if(type.equals("UNKNOWN")){
          out.write(Util.str2byte(host));
          out.write(cr);
          continue;
        }
        if(marker.length()!=0){
          out.write(Util.str2byte(marker));
          out.write(space);
        }
        out.write(Util.str2byte(host));
        out.write(space);
        out.write(Util.str2byte(type));
        out.write(space);
        out.write(Util.str2byte(hk.getKey()));
        if(comment!=null){
          out.write(space);
          out.write(Util.str2byte(comment));
        }
        out.write(cr);
      }
      }
    }
    catch(Exception e){
      System.err.println(e);
    }
  }

  private String deleteSubString(String hosts, String host){
    int i=0;
    int hostlen=host.length();
    int hostslen=hosts.length();
    int j;
    while(i<hostslen){
      j=hosts.indexOf(',', i);
      if(j==-1) break;
      if(!host.equals(hosts.substring(i, j))){
        i=j+1;
        continue;
      }
      return hosts.substring(0, i)+hosts.substring(j+1);
    }
    if(hosts.endsWith(host) && hostslen-i==hostlen){
      return hosts.substring(0, (hostlen==hostslen) ? 0 :hostslen-hostlen-1);
    }
    return hosts;
  }

  private MAC getHMACSHA1(){
    if(hmacsha1==null){
      try{
        Class c=Class.forName(jsch.getConfig("hmac-sha1"));
        hmacsha1=(MAC)(c.newInstance());
      }
      catch(Exception e){
        System.err.println("hmacsha1: "+e);
      }
    }
    return hmacsha1;
  }

  HostKey createHashedHostKey(String host, byte[]key) throws JSchException {
    HashedHostKey hhk=new HashedHostKey(host, key);
    hhk.hash();
    return hhk;
  }
  class HashedHostKey extends HostKey{
    private static final String HASH_MAGIC="|1|";
    private static final String HASH_DELIM="|";

    private boolean hashed=false;
    byte[] salt=null;
    byte[] hash=null;

    HashedHostKey(String host, byte[] key) throws JSchException {
      this(host, GUESS, key);
    }
    HashedHostKey(String host, int type, byte[] key) throws JSchException {
      this("", host, type, key, null);
    }
    HashedHostKey(String marker, String host, int type, byte[] key, String comment) throws JSchException {
      super(marker, host, type, key, comment);
      if(this.host.startsWith(HASH_MAGIC) &&
         this.host.substring(HASH_MAGIC.length()).indexOf(HASH_DELIM)>0){
        String data=this.host.substring(HASH_MAGIC.length());
        String _salt=data.substring(0, data.indexOf(HASH_DELIM));
        String _hash=data.substring(data.indexOf(HASH_DELIM)+1);
        salt=Util.fromBase64(Util.str2byte(_salt), 0, _salt.length());
        hash=Util.fromBase64(Util.str2byte(_hash), 0, _hash.length());
        if(salt.length!=20 ||  // block size of hmac-sha1
           hash.length!=20){
          salt=null;
          hash=null;
          return;
        }
        hashed=true;
      }
    }

    boolean isMatched(String _host){
      if(!hashed){
        return super.isMatched(_host);
      }
      MAC macsha1=getHMACSHA1();
      try{
        synchronized(macsha1){
          macsha1.init(salt);
          byte[] foo=Util.str2byte(_host);
          macsha1.update(foo, 0, foo.length);
          byte[] bar=new byte[macsha1.getBlockSize()];
          macsha1.doFinal(bar, 0);
          return Util.array_equals(hash, bar);
        }
      }
      catch(Exception e){
        System.out.println(e);
      }
      return false;
    }

    boolean isHashed(){
      return hashed;
    }

    void hash(){
      if(hashed)
        return;
      MAC macsha1=getHMACSHA1();
      if(salt==null){
        Random random=Session.random;
        synchronized(random){
          salt=new byte[macsha1.getBlockSize()];
          random.fill(salt, 0, salt.length);
        }
      }
      try{
        synchronized(macsha1){
          macsha1.init(salt);
          byte[] foo=Util.str2byte(host);
          macsha1.update(foo, 0, foo.length);
          hash=new byte[macsha1.getBlockSize()];
          macsha1.doFinal(hash, 0);
        }
      }
      catch(Exception e){
      }
      host=HASH_MAGIC+Util.byte2str(Util.toBase64(salt, 0, salt.length))+
        HASH_DELIM+Util.byte2str(Util.toBase64(hash, 0, hash.length));
      hashed=true;
    }
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2012-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class LocalIdentityRepository implements IdentityRepository {
  private static final String name = "Local Identity Repository";

  private Vector identities = new Vector();
  private JSch jsch;

  LocalIdentityRepository(JSch jsch){
    this.jsch = jsch;
  }

  public String getName(){
    return name;
  }

  public int getStatus(){
    return RUNNING;
  }

  public synchronized Vector getIdentities() {
    removeDupulicates();
    Vector v = new Vector();
    for(int i=0; i<identities.size(); i++){
      v.addElement(identities.elementAt(i));
    }
    return v;
  }

  public synchronized void add(Identity identity) {
    if(!identities.contains(identity)) {
      byte[] blob1 = identity.getPublicKeyBlob();
      if(blob1 == null) {
        identities.addElement(identity);
        return;
      }
      for(int i = 0; i<identities.size(); i++){
        byte[] blob2 = ((Identity)identities.elementAt(i)).getPublicKeyBlob();
        if(blob2 != null && Util.array_equals(blob1, blob2)){
          if(!identity.isEncrypted() &&
             ((Identity)identities.elementAt(i)).isEncrypted()){
            remove(blob2);
          }
          else {
            return;
          }
        }
      }
      identities.addElement(identity);
    }
  }

  public synchronized boolean add(byte[] identity) {
    try{
      Identity _identity =
        IdentityFile.newInstance("from remote:", identity, null, jsch);
      add(_identity);
      return true;
    }
    catch(JSchException e){
      return false;
    }
  }

  synchronized void remove(Identity identity) {
    if(identities.contains(identity)) {
      identities.removeElement(identity);
      identity.clear();
    }
    else {
      remove(identity.getPublicKeyBlob());
    }
  }

  public synchronized boolean remove(byte[] blob) {
    if(blob == null) return false;
    for(int i=0; i<identities.size(); i++) {
      Identity _identity = (Identity)(identities.elementAt(i));
      byte[] _blob = _identity.getPublicKeyBlob();
      if(_blob == null || !Util.array_equals(blob, _blob))
        continue;
      identities.removeElement(_identity);
      _identity.clear();
      return true;
    }
    return false;
  }

  public synchronized void removeAll() {
    for(int i=0; i<identities.size(); i++) {
      Identity identity=(Identity)(identities.elementAt(i));
      identity.clear();
    }
    identities.removeAllElements();
  }

  private void removeDupulicates(){
    Vector v = new Vector();
    int len = identities.size();
    if(len == 0) return;
    for(int i=0; i<len; i++){
      Identity foo = (Identity)identities.elementAt(i);
      byte[] foo_blob = foo.getPublicKeyBlob();
      if(foo_blob == null) continue;
      for(int j=i+1; j<len; j++){
        Identity bar = (Identity)identities.elementAt(j);
        byte[] bar_blob = bar.getPublicKeyBlob();
        if(bar_blob == null) continue;
        if(Util.array_equals(foo_blob, bar_blob) &&
           foo.isEncrypted() == bar.isEncrypted()){
          v.addElement(foo_blob);
          break;
        }
      }
    }
    for(int i=0; i<v.size(); i++){
      remove((byte[])v.elementAt(i));
    }
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2006-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Logger{

  public final int DEBUG=0;
  public final int INFO=1;
  public final int WARN=2;
  public final int ERROR=3;
  public final int FATAL=4;

  public boolean isEnabled(int level);

  public void log(int level, String message);

  /*
  public final Logger SIMPLE_LOGGER=new Logger(){
      public boolean isEnabled(int level){return true;}
      public void log(int level, String message){System.err.println(message);}
    };
  final Logger DEVNULL=new Logger(){
      public boolean isEnabled(int level){return false;}
      public void log(int level, String message){}
    };
  */
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface MAC{
  String getName();
  int getBlockSize();
  void init(byte[] key) throws Exception;
  void update(byte[] foo, int start, int len);
  void update(int foo);
  void doFinal(byte[] buf, int offset);
}


interface OpenSSHCertifiedKey {
        int SSH_CERT_TYPE_USER  =  1;
        int SSH_CERT_TYPE_HOST  =  2;
        int getCertificateType();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2013-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




/**
 * This class implements ConfigRepository interface, and parses
 * OpenSSH's configuration file.  The following keywords will be recognized,
 * <ul>
 *   <li>Host</li>
 *   <li>User</li>
 *   <li>Hostname</li>
 *   <li>Port</li>
 *   <li>PreferredAuthentications</li>
 *   <li>IdentityFile</li>
 *   <li>NumberOfPasswordPrompts</li>
 *   <li>ConnectTimeout</li>
 *   <li>HostKeyAlias</li>
 *   <li>UserKnownHostsFile</li>
 *   <li>KexAlgorithms</li>
 *   <li>HostKeyAlgorithms</li>
 *   <li>Ciphers</li>
 *   <li>Macs</li>
 *   <li>Compression</li>
 *   <li>CompressionLevel</li>
 *   <li>ForwardAgent</li>
 *   <li>RequestTTY</li>
 *   <li>ServerAliveInterval</li>
 *   <li>LocalForward</li>
 *   <li>RemoteForward</li>
 *   <li>ClearAllForwardings</li>
 * </ul>
 *
 * @see ConfigRepository
 */
class OpenSSHConfig implements ConfigRepository {

  /**
   * Parses the given string, and returns an instance of ConfigRepository.
   *
   * @param conf string, which includes OpenSSH's config
   * @return an instanceof OpenSSHConfig
   */
  public static OpenSSHConfig parse(String conf) throws IOException {
    Reader r = new StringReader(conf);
    try {
      return new OpenSSHConfig(r);
    }
    finally {
      r.close();
    }
  }

  /**
   * Parses the given file, and returns an instance of ConfigRepository.
   *
   * @param file OpenSSH's config file
   * @return an instanceof OpenSSHConfig
   */
  public static OpenSSHConfig parseFile(String file) throws IOException {
    Reader r = new FileReader(Util.checkTilde(file));
    try {
      return new OpenSSHConfig(r);
    }
    finally {
      r.close();
    }
  }

  OpenSSHConfig(Reader r) throws IOException {
    _parse(r);
  }

  private final Hashtable config = new Hashtable();
  private final Vector hosts = new Vector();

  private void _parse(Reader r) throws IOException {
    BufferedReader br = new BufferedReader(r);

    String host = "";
    Vector/*<String[]>*/ kv = new Vector();
    String l = null;

    while((l = br.readLine()) != null){
      l = l.trim();
      if(l.length() == 0 || l.startsWith("#"))
        continue;

      String[] key_value = l.split("[= \t]", 2);
      for(int i = 0; i < key_value.length; i++)
        key_value[i] = key_value[i].trim();

      if(key_value.length <= 1)
        continue;

      if(key_value[0].equals("Host")){
        config.put(host, kv);
        hosts.addElement(host);
        host = key_value[1];
        kv = new Vector();
      }
      else {
        kv.addElement(key_value);
      }
    }
    config.put(host, kv);
    hosts.addElement(host);
  }

  public Config getConfig(String host) {
    return new MyConfig(host);
  }

  private static final Hashtable keymap = new Hashtable();
  static {
    keymap.put("kex", "KexAlgorithms");
    keymap.put("server_host_key", "HostKeyAlgorithms");
    keymap.put("cipher.c2s", "Ciphers");
    keymap.put("cipher.s2c", "Ciphers");
    keymap.put("mac.c2s", "Macs");
    keymap.put("mac.s2c", "Macs");
    keymap.put("compression.s2c", "Compression");
    keymap.put("compression.c2s", "Compression");
    keymap.put("compression_level", "CompressionLevel");
    keymap.put("MaxAuthTries", "NumberOfPasswordPrompts");
  }

  class MyConfig implements Config {

    private String host;
    private Vector _configs = new Vector();

    MyConfig(String host){
      this.host = host;

      _configs.addElement(config.get(""));

      byte[] _host = Util.str2byte(host);
      if(hosts.size() > 1){
        for(int i = 1; i < hosts.size(); i++){
          String patterns[] = ((String)hosts.elementAt(i)).split("[ \t]");
          for(int j = 0; j < patterns.length; j++){
            boolean negate = false;
            String foo = patterns[j].trim();
            if(foo.startsWith("!")){
              negate = true;
              foo = foo.substring(1).trim();
            }
            if(Util.glob(Util.str2byte(foo), _host)){
              if(!negate){
                _configs.addElement(config.get((String)hosts.elementAt(i)));
              }
            }
            else if(negate){
              _configs.addElement(config.get((String)hosts.elementAt(i)));
            }
          }
        }
      }
    }

    private String find(String key) {
      if(keymap.get(key)!=null) {
        key = (String)keymap.get(key);
      }
      key = key.toUpperCase();
      String value = null;
      for(int i = 0; i < _configs.size(); i++) {
        Vector v = (Vector)_configs.elementAt(i);
        for(int j = 0; j < v.size(); j++) {
          String[] kv = (String[])v.elementAt(j);
          if(kv[0].toUpperCase().equals(key)) {
            value = kv[1];
            break;
          }
        }
        if(value != null)
          break;
      }
      return value;
    }

    private String[] multiFind(String key) {
      key = key.toUpperCase();
      Vector value = new Vector();
      for(int i = 0; i < _configs.size(); i++) {
        Vector v = (Vector)_configs.elementAt(i);
        for(int j = 0; j < v.size(); j++) {
          String[] kv = (String[])v.elementAt(j);
          if(kv[0].toUpperCase().equals(key)) {
            String foo = kv[1];
            if(foo != null) {
              value.remove(foo);
              value.addElement(foo);
            }
          }
        }
      }
      String[] result = new String[value.size()];
      value.toArray(result);
      return result;
    }

    public String getHostname(){ return find("Hostname"); }
    public String getUser(){ return find("User"); }
    public int getPort(){
      String foo = find("Port");
      int port = -1;
      try {
        port = Integer.parseInt(foo);
      }
      catch(NumberFormatException e){
        // wrong format
      }
      return port;
    }
    public String getValue(String key){
      if(key.equals("compression.s2c") ||
         key.equals("compression.c2s")) {
        String foo = find(key);
        if(foo == null || foo.equals("no"))
          return "none,zlib@openssh.com,zlib";
        return "zlib@openssh.com,zlib,none";
      }
      return find(key);
    }
    public String[] getValues(String key){ return multiFind(key); }
  }
}


class OpenSSHUserCertDSA extends KeyPairDSA implements OpenSSHCertifiedKey {
    private static final String keyType = "ssh-dss-cert-v01@openssh.com";
    private static final byte[] sshdsacert = Util.str2byte(keyType);

    public OpenSSHUserCertDSA(JSch jsch){
        super(jsch);
    }

    public int getCertificateType() {
        return SSH_CERT_TYPE_USER;
    }

    @Override
    public int getKeyType(){
        return DSA_CERT;
    }

    @Override
    byte[] getKeyTypeName(){
        return sshdsacert;
    }
}


class OpenSSHUserCertECDSA extends KeyPairECDSA implements OpenSSHCertifiedKey {
    private static final String keyType = "ecdsa-sha2-nistp256-cert-v01@openssh.com";
    private static final byte[] sshrsacert = Util.str2byte(keyType);

    public OpenSSHUserCertECDSA(JSch jsch){
        super(jsch);
    }

    public int getCertificateType() {
        return SSH_CERT_TYPE_USER;
    }

    @Override
    public int getKeyType(){
        return RSA_CERT;
    }

    @Override
    byte[] getKeyTypeName(){
        return sshrsacert;
    }
}


/**
 *
 */
class OpenSSHUserCertRSA extends KeyPairRSA implements OpenSSHCertifiedKey {
    private static final String keyType = "ssh-rsa-cert-v01@openssh.com";
    private static final byte[] sshrsacert = Util.str2byte(keyType);

    public OpenSSHUserCertRSA(JSch jsch){
        super(jsch);
    }

    public int getCertificateType() {
        return SSH_CERT_TYPE_USER;
    }

    @Override
    public int getKeyType(){
        return RSA_CERT;
    }

    @Override
    byte[] getKeyTypeName(){
        return sshrsacert;
    }

}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class Packet{

  private static Random random=null;
  static void setRandom(Random foo){ random=foo;}

  Buffer buffer;
  byte[] ba4=new byte[4];
  public Packet(Buffer buffer){
    this.buffer=buffer;
  }
  public void reset(){
    buffer.index=5;
  }
  void padding(int bsize){
    int len=buffer.index;
    int pad=(-len)&(bsize-1);
    if(pad<bsize){
      pad+=bsize;
    }
    len=len+pad-4;
    ba4[0]=(byte)(len>>>24);
    ba4[1]=(byte)(len>>>16);
    ba4[2]=(byte)(len>>>8);
    ba4[3]=(byte)(len);
    System.arraycopy(ba4, 0, buffer.buffer, 0, 4);
    buffer.buffer[4]=(byte)pad;
    synchronized(random){
      random.fill(buffer.buffer, buffer.index, pad);
    }
    buffer.skip(pad);
    //buffer.putPad(pad);
/*
for(int i=0; i<buffer.index; i++){
System.err.print(Integer.toHexString(buffer.buffer[i]&0xff)+":");
}
System.err.println("");
*/
  }

  int shift(int len, int bsize, int mac){
    int s=len+5+9;
    int pad=(-s)&(bsize-1);
    if(pad<bsize)pad+=bsize;
    s+=pad;
    s+=mac;
    s+=32; // margin for deflater; deflater may inflate data

    /**/
    if(buffer.buffer.length<s+buffer.index-5-9-len){
      byte[] foo=new byte[s+buffer.index-5-9-len];
      System.arraycopy(buffer.buffer, 0, foo, 0, buffer.buffer.length);
      buffer.buffer=foo;
    }
    /**/

//if(buffer.buffer.length<len+5+9)
//  System.err.println("buffer.buffer.length="+buffer.buffer.length+" len+5+9="+(len+5+9));

//if(buffer.buffer.length<s)
//  System.err.println("buffer.buffer.length="+buffer.buffer.length+" s="+(s));

    System.arraycopy(buffer.buffer,
                     len+5+9,
                     buffer.buffer, s, buffer.index-5-9-len);

    buffer.index=10;
    buffer.putInt(len);
    buffer.index=len+5+9;
    return s;
  }
  void unshift(byte command, int recipient, int s, int len){
    System.arraycopy(buffer.buffer,
                     s,
                     buffer.buffer, 5+9, len);
    buffer.buffer[5]=command;
    buffer.index=6;
    buffer.putInt(recipient);
    buffer.putInt(len);
    buffer.index=len+5+9;
  }
  Buffer getBuffer(){
    return buffer;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2013-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface PBKDF {
  byte[] getKey(byte[] pass, byte[] salt, int iteration, int size);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class PortWatcher implements Runnable{
  private static java.util.Vector pool=new java.util.Vector();
  private static InetAddress anyLocalAddress=null;
  static{
    // 0.0.0.0
/*
    try{ anyLocalAddress=InetAddress.getByAddress(new byte[4]); }
    catch(UnknownHostException e){
    }
*/
    try{ anyLocalAddress=InetAddress.getByName("0.0.0.0"); }
    catch(UnknownHostException e){
    }
  }

  Session session;
  int lport;
  int rport;
  String host;
  InetAddress boundaddress;
  Runnable thread;
  ServerSocket ss;
  int connectTimeout=0;

  static String[] getPortForwarding(Session session){
    java.util.Vector foo=new java.util.Vector();
    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        PortWatcher p=(PortWatcher)(pool.elementAt(i));
        if(p.session==session){
          foo.addElement(p.lport+":"+p.host+":"+p.rport);
        }
      }
    }
    String[] bar=new String[foo.size()];
    for(int i=0; i<foo.size(); i++){
      bar[i]=(String)(foo.elementAt(i));
    }
    return bar;
  }
  static PortWatcher getPort(Session session, String address, int lport) throws JSchException{
    InetAddress addr;
    try{
      addr=InetAddress.getByName(address);
    }
    catch(UnknownHostException uhe){
      throw new JSchException("PortForwardingL: invalid address "+address+" specified.", uhe);
    }
    synchronized(pool){
      for(int i=0; i<pool.size(); i++){
        PortWatcher p=(PortWatcher)(pool.elementAt(i));
        if(p.session==session && p.lport==lport){
          if(/*p.boundaddress.isAnyLocalAddress() ||*/
             (anyLocalAddress!=null &&  p.boundaddress.equals(anyLocalAddress)) ||
             p.boundaddress.equals(addr))
          return p;
        }
      }
      return null;
    }
  }
  private static String normalize(String address){
    if(address!=null){
      if(address.length()==0 || address.equals("*"))
        address="0.0.0.0";
      else if(address.equals("localhost"))
        address="127.0.0.1";
    }
    return address;
  }
  static PortWatcher addPort(Session session, String address, int lport, String host, int rport, ServerSocketFactory ssf) throws JSchException{
    address = normalize(address);
    if(getPort(session, address, lport)!=null){
      throw new JSchException("PortForwardingL: local port "+ address+":"+lport+" is already registered.");
    }
    PortWatcher pw=new PortWatcher(session, address, lport, host, rport, ssf);
    pool.addElement(pw);
    return pw;
  }
  static void delPort(Session session, String address, int lport) throws JSchException{
    address = normalize(address);
    PortWatcher pw=getPort(session, address, lport);
    if(pw==null){
      throw new JSchException("PortForwardingL: local port "+address+":"+lport+" is not registered.");
    }
    pw.delete();
    pool.removeElement(pw);
  }
  static void delPort(Session session){
    synchronized(pool){
      PortWatcher[] foo=new PortWatcher[pool.size()];
      int count=0;
      for(int i=0; i<pool.size(); i++){
        PortWatcher p=(PortWatcher)(pool.elementAt(i));
        if(p.session==session) {
          p.delete();
          foo[count++]=p;
        }
      }
      for(int i=0; i<count; i++){
        PortWatcher p=foo[i];
        pool.removeElement(p);
      }
    }
  }
  PortWatcher(Session session,
              String address, int lport,
              String host, int rport,
              ServerSocketFactory factory) throws JSchException{
    this.session=session;
    this.lport=lport;
    this.host=host;
    this.rport=rport;
    try{
      boundaddress=InetAddress.getByName(address);
      ss=(factory==null) ?
        new ServerSocket(lport, 0, boundaddress) :
        factory.createServerSocket(lport, 0, boundaddress);
    }
    catch(Exception e){
      //System.err.println(e);
      String message="PortForwardingL: local port "+address+":"+lport+" cannot be bound.";
      if(e instanceof Throwable)
        throw new JSchException(message, (Throwable)e);
      throw new JSchException(message);
    }
    if(lport==0){
      int assigned=ss.getLocalPort();
      if(assigned!=-1)
        this.lport=assigned;
    }
  }

  public void run(){
    thread=this;
    try{
      while(thread!=null){
        Socket socket=ss.accept();
        socket.setTcpNoDelay(true);
        InputStream in=socket.getInputStream();
        OutputStream out=socket.getOutputStream();
        ChannelDirectTCPIP channel=new ChannelDirectTCPIP();
        channel.init();
        channel.setInputStream(in);
        channel.setOutputStream(out);
        session.addChannel(channel);
        ((ChannelDirectTCPIP)channel).setHost(host);
        ((ChannelDirectTCPIP)channel).setPort(rport);
        ((ChannelDirectTCPIP)channel).setOrgIPAddress(socket.getInetAddress().getHostAddress());
        ((ChannelDirectTCPIP)channel).setOrgPort(socket.getPort());
        channel.connect(connectTimeout);
        if(channel.exitstatus!=-1){
        }
      }
    }
    catch(Exception e){
      //System.err.println("! "+e);
    }
    delete();
  }

  void delete(){
    thread=null;
    try{
      if(ss!=null)ss.close();
      ss=null;
    }
    catch(Exception e){
    }
  }

  void setConnectTimeout(int connectTimeout){
    this.connectTimeout=connectTimeout;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class ProxyHTTP implements Proxy{
  private static int DEFAULTPORT=80;
  private String proxy_host;
  private int proxy_port;
  private InputStream in;
  private OutputStream out;
  private Socket socket;

  private String user;
  private String passwd;

  public ProxyHTTP(String proxy_host){
    int port=DEFAULTPORT;
    String host=proxy_host;
    if(proxy_host.indexOf(':')!=-1){
      try{
        host=proxy_host.substring(0, proxy_host.indexOf(':'));
        port=Integer.parseInt(proxy_host.substring(proxy_host.indexOf(':')+1));
      }
      catch(Exception e){
      }
    }
    this.proxy_host=host;
    this.proxy_port=port;
  }
  public ProxyHTTP(String proxy_host, int proxy_port){
    this.proxy_host=proxy_host;
    this.proxy_port=proxy_port;
  }
  public void setUserPasswd(String user, String passwd){
    this.user=user;
    this.passwd=passwd;
  }
  public void connect(SocketFactory socket_factory, String host, int port, int timeout) throws JSchException{
    try{
      if(socket_factory==null){
        socket=Util.createSocket(proxy_host, proxy_port, timeout);
        in=socket.getInputStream();
        out=socket.getOutputStream();
      }
      else{
        socket=socket_factory.createSocket(proxy_host, proxy_port);
        in=socket_factory.getInputStream(socket);
        out=socket_factory.getOutputStream(socket);
      }
      if(timeout>0){
        socket.setSoTimeout(timeout);
      }
      socket.setTcpNoDelay(true);

      out.write(Util.str2byte("CONNECT "+host+":"+port+" HTTP/1.0\r\n"));

      if(user!=null && passwd!=null){
        byte[] code=Util.str2byte(user+":"+passwd);
        code=Util.toBase64(code, 0, code.length);
        out.write(Util.str2byte("Proxy-Authorization: Basic "));
        out.write(code);
        out.write(Util.str2byte("\r\n"));
      }

      out.write(Util.str2byte("\r\n"));
      out.flush();

      int foo=0;

      StringBuffer sb=new StringBuffer();
      while(foo>=0){
        foo=in.read(); if(foo!=13){sb.append((char)foo);  continue;}
        foo=in.read(); if(foo!=10){continue;}
        break;
      }
      if(foo<0){
        throw new IOException();
      }

      String response=sb.toString();
      String reason="Unknow reason";
      int code=-1;
      try{
        foo=response.indexOf(' ');
        int bar=response.indexOf(' ', foo+1);
        code=Integer.parseInt(response.substring(foo+1, bar));
        reason=response.substring(bar+1);
      }
      catch(Exception e){
      }
      if(code!=200){
        throw new IOException("proxy error: "+reason);
      }

      /*
      while(foo>=0){
        foo=in.read(); if(foo!=13) continue;
        foo=in.read(); if(foo!=10) continue;
        foo=in.read(); if(foo!=13) continue;
        foo=in.read(); if(foo!=10) continue;
        break;
      }
      */

      int count=0;
      while(true){
        count=0;
        while(foo>=0){
          foo=in.read(); if(foo!=13){count++;  continue;}
          foo=in.read(); if(foo!=10){continue;}
          break;
        }
        if(foo<0){
          throw new IOException();
        }
        if(count==0)break;
      }
    }
    catch(RuntimeException e){
      throw e;
    }
    catch(Exception e){
      try{ if(socket!=null)socket.close(); }
      catch(Exception eee){
      }
      String message="ProxyHTTP: "+e.toString();
      if(e instanceof Throwable)
        throw new JSchException(message, (Throwable)e);
      throw new JSchException(message);
    }
  }
  public InputStream getInputStream(){ return in; }
  public OutputStream getOutputStream(){ return out; }
  public Socket getSocket(){ return socket; }
  public void close(){
    try{
      if(in!=null)in.close();
      if(out!=null)out.close();
      if(socket!=null)socket.close();
    }
    catch(Exception e){
    }
    in=null;
    out=null;
    socket=null;
  }
  public static int getDefaultPort(){
    return DEFAULTPORT;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Proxy{
  void connect(SocketFactory socket_factory, String host, int port, int timeout) throws Exception;
  InputStream getInputStream();
  OutputStream getOutputStream();
  Socket getSocket();
  void close();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2006-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
 This file depends on following documents,
   - SOCKS: A protocol for TCP proxy across firewalls, Ying-Da Lee
     http://www.socks.nec.com/protocol/socks4.protocol
 */




class ProxySOCKS4 implements Proxy{
  private static int DEFAULTPORT=1080;
  private String proxy_host;
  private int proxy_port;
  private InputStream in;
  private OutputStream out;
  private Socket socket;
  private String user;
  private String passwd;

  public ProxySOCKS4(String proxy_host){
    int port=DEFAULTPORT;
    String host=proxy_host;
    if(proxy_host.indexOf(':')!=-1){
      try{
        host=proxy_host.substring(0, proxy_host.indexOf(':'));
        port=Integer.parseInt(proxy_host.substring(proxy_host.indexOf(':')+1));
      }
      catch(Exception e){
      }
    }
    this.proxy_host=host;
    this.proxy_port=port;
  }
  public ProxySOCKS4(String proxy_host, int proxy_port){
    this.proxy_host=proxy_host;
    this.proxy_port=proxy_port;
  }
  public void setUserPasswd(String user, String passwd){
    this.user=user;
    this.passwd=passwd;
  }
  public void connect(SocketFactory socket_factory, String host, int port, int timeout) throws JSchException{
    try{
      if(socket_factory==null){
        socket=Util.createSocket(proxy_host, proxy_port, timeout);
        //socket=new Socket(proxy_host, proxy_port);
        in=socket.getInputStream();
        out=socket.getOutputStream();
      }
      else{
        socket=socket_factory.createSocket(proxy_host, proxy_port);
        in=socket_factory.getInputStream(socket);
        out=socket_factory.getOutputStream(socket);
      }
      if(timeout>0){
        socket.setSoTimeout(timeout);
      }
      socket.setTcpNoDelay(true);

      byte[] buf=new byte[1024];
      int index=0;

/*
   1) CONNECT

   The client connects to the SOCKS server and sends a CONNECT request when
   it wants to establish a connection to an application server. The client
   includes in the request packet the IP address and the port number of the
   destination host, and userid, in the following format.

               +----+----+----+----+----+----+----+----+----+----+....+----+
               | VN | CD | DSTPORT |      DSTIP        | USERID       |NULL|
               +----+----+----+----+----+----+----+----+----+----+....+----+
   # of bytes:   1    1      2              4           variable       1

   VN is the SOCKS protocol version number and should be 4. CD is the
   SOCKS command code and should be 1 for CONNECT request. NULL is a byte
   of all zero bits.
*/

      index=0;
      buf[index++]=4;
      buf[index++]=1;

      buf[index++]=(byte)(port>>>8);
      buf[index++]=(byte)(port&0xff);

      try{
        InetAddress addr=InetAddress.getByName(host);
        byte[] byteAddress = addr.getAddress();
        for (int i = 0; i < byteAddress.length; i++) {
          buf[index++]=byteAddress[i];
        }
      }
      catch(UnknownHostException uhe){
        throw new JSchException("ProxySOCKS4: "+uhe.toString(), uhe);
      }

      if(user!=null){
        System.arraycopy(Util.str2byte(user), 0, buf, index, user.length());
        index+=user.length();
      }
      buf[index++]=0;
      out.write(buf, 0, index);

/*
   The SOCKS server checks to see whether such a request should be granted
   based on any combination of source IP address, destination IP address,
   destination port number, the userid, and information it may obtain by
   consulting IDENT, cf. RFC 1413.  If the request is granted, the SOCKS
   server makes a connection to the specified port of the destination host.
   A reply packet is sent to the client when this connection is established,
   or when the request is rejected or the operation fails.

               +----+----+----+----+----+----+----+----+
               | VN | CD | DSTPORT |      DSTIP        |
               +----+----+----+----+----+----+----+----+
   # of bytes:   1    1      2              4

   VN is the version of the reply code and should be 0. CD is the result
   code with one of the following values:

   90: request granted
   91: request rejected or failed
   92: request rejected becasue SOCKS server cannot connect to
       identd on the client
   93: request rejected because the client program and identd
       report different user-ids

   The remaining fields are ignored.
*/

      int len=8;
      int s=0;
      while(s<len){
        int i=in.read(buf, s, len-s);
        if(i<=0){
          throw new JSchException("ProxySOCKS4: stream is closed");
        }
        s+=i;
      }
      if(buf[0]!=0){
        throw new JSchException("ProxySOCKS4: server returns VN "+buf[0]);
      }
      if(buf[1]!=90){
        try{ socket.close(); }
        catch(Exception eee){
        }
        String message="ProxySOCKS4: server returns CD "+buf[1];
        throw new JSchException(message);
      }
    }
    catch(RuntimeException e){
      throw e;
    }
    catch(Exception e){
      try{ if(socket!=null)socket.close(); }
      catch(Exception eee){
      }
      throw new JSchException("ProxySOCKS4: "+e.toString());
    }
  }
  public InputStream getInputStream(){ return in; }
  public OutputStream getOutputStream(){ return out; }
  public Socket getSocket(){ return socket; }
  public void close(){
    try{
      if(in!=null)in.close();
      if(out!=null)out.close();
      if(socket!=null)socket.close();
    }
    catch(Exception e){
    }
    in=null;
    out=null;
    socket=null;
  }
  public static int getDefaultPort(){
    return DEFAULTPORT;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
 This file depends on following documents,
   - RFC 1928  SOCKS Protocol Verseion 5
   - RFC 1929  Username/Password Authentication for SOCKS V5.
 */




class ProxySOCKS5 implements Proxy{
  private static int DEFAULTPORT=1080;
  private String proxy_host;
  private int proxy_port;
  private InputStream in;
  private OutputStream out;
  private Socket socket;
  private String user;
  private String passwd;

  public ProxySOCKS5(String proxy_host){
    int port=DEFAULTPORT;
    String host=proxy_host;
    if(proxy_host.indexOf(':')!=-1){
      try{
        host=proxy_host.substring(0, proxy_host.indexOf(':'));
        port=Integer.parseInt(proxy_host.substring(proxy_host.indexOf(':')+1));
      }
      catch(Exception e){
      }
    }
    this.proxy_host=host;
    this.proxy_port=port;
  }
  public ProxySOCKS5(String proxy_host, int proxy_port){
    this.proxy_host=proxy_host;
    this.proxy_port=proxy_port;
  }
  public void setUserPasswd(String user, String passwd){
    this.user=user;
    this.passwd=passwd;
  }
  public void connect(SocketFactory socket_factory, String host, int port, int timeout) throws JSchException{
    try{
      if(socket_factory==null){
        socket=Util.createSocket(proxy_host, proxy_port, timeout);
        //socket=new Socket(proxy_host, proxy_port);
        in=socket.getInputStream();
        out=socket.getOutputStream();
      }
      else{
        socket=socket_factory.createSocket(proxy_host, proxy_port);
        in=socket_factory.getInputStream(socket);
        out=socket_factory.getOutputStream(socket);
      }
      if(timeout>0){
        socket.setSoTimeout(timeout);
      }
      socket.setTcpNoDelay(true);

      byte[] buf=new byte[1024];
      int index=0;

/*
                   +----+----------+----------+
                   |VER | NMETHODS | METHODS  |
                   +----+----------+----------+
                   | 1  |    1     | 1 to 255 |
                   +----+----------+----------+

   The VER field is set to X'05' for this version of the protocol.  The
   NMETHODS field contains the number of method identifier octets that
   appear in the METHODS field.

   The values currently defined for METHOD are:

          o  X'00' NO AUTHENTICATION REQUIRED
          o  X'01' GSSAPI
          o  X'02' USERNAME/PASSWORD
          o  X'03' to X'7F' IANA ASSIGNED
          o  X'80' to X'FE' RESERVED FOR PRIVATE METHODS
          o  X'FF' NO ACCEPTABLE METHODS
*/

      buf[index++]=5;

      buf[index++]=2;
      buf[index++]=0;           // NO AUTHENTICATION REQUIRED
      buf[index++]=2;           // USERNAME/PASSWORD

      out.write(buf, 0, index);

/*
    The server selects from one of the methods given in METHODS, and
    sends a METHOD selection message:

                         +----+--------+
                         |VER | METHOD |
                         +----+--------+
                         | 1  |   1    |
                         +----+--------+
*/
      //in.read(buf, 0, 2);
      fill(in, buf, 2);

      boolean check=false;
      switch((buf[1])&0xff){
        case 0:                // NO AUTHENTICATION REQUIRED
          check=true;
          break;
        case 2:                // USERNAME/PASSWORD
          if(user==null || passwd==null)break;

/*
   Once the SOCKS V5 server has started, and the client has selected the
   Username/Password Authentication protocol, the Username/Password
   subnegotiation begins.  This begins with the client producing a
   Username/Password request:

           +----+------+----------+------+----------+
           |VER | ULEN |  UNAME   | PLEN |  PASSWD  |
           +----+------+----------+------+----------+
           | 1  |  1   | 1 to 255 |  1   | 1 to 255 |
           +----+------+----------+------+----------+

   The VER field contains the current version of the subnegotiation,
   which is X'01'. The ULEN field contains the length of the UNAME field
   that follows. The UNAME field contains the username as known to the
   source operating system. The PLEN field contains the length of the
   PASSWD field that follows. The PASSWD field contains the password
   association with the given UNAME.
*/
          index=0;
          buf[index++]=1;
          buf[index++]=(byte)(user.length());
          System.arraycopy(Util.str2byte(user), 0, buf, index, user.length());
          index+=user.length();
          buf[index++]=(byte)(passwd.length());
          System.arraycopy(Util.str2byte(passwd), 0, buf, index, passwd.length());
          index+=passwd.length();

          out.write(buf, 0, index);

/*
   The server verifies the supplied UNAME and PASSWD, and sends the
   following response:

                        +----+--------+
                        |VER | STATUS |
                        +----+--------+
                        | 1  |   1    |
                        +----+--------+

   A STATUS field of X'00' indicates success. If the server returns a
   `failure' (STATUS value other than X'00') status, it MUST close the
   connection.
*/
          //in.read(buf, 0, 2);
          fill(in, buf, 2);
          if(buf[1]==0)
            check=true;
          break;
        default:
      }

      if(!check){
        try{ socket.close(); }
        catch(Exception eee){
        }
        throw new JSchException("fail in SOCKS5 proxy");
      }

/*
      The SOCKS request is formed as follows:

        +----+-----+-------+------+----------+----------+
        |VER | CMD |  RSV  | ATYP | DST.ADDR | DST.PORT |
        +----+-----+-------+------+----------+----------+
        | 1  |  1  | X'00' |  1   | Variable |    2     |
        +----+-----+-------+------+----------+----------+

      Where:

      o  VER    protocol version: X'05'
      o  CMD
         o  CONNECT X'01'
         o  BIND X'02'
         o  UDP ASSOCIATE X'03'
      o  RSV    RESERVED
         o  ATYP   address type of following address
         o  IP V4 address: X'01'
         o  DOMAINNAME: X'03'
         o  IP V6 address: X'04'
      o  DST.ADDR       desired destination address
      o  DST.PORT desired destination port in network octet
         order
*/

      index=0;
      buf[index++]=5;
      buf[index++]=1;       // CONNECT
      buf[index++]=0;

      byte[] hostb=Util.str2byte(host);
      int len=hostb.length;
      buf[index++]=3;      // DOMAINNAME
      buf[index++]=(byte)(len);
      System.arraycopy(hostb, 0, buf, index, len);
      index+=len;
      buf[index++]=(byte)(port>>>8);
      buf[index++]=(byte)(port&0xff);

      out.write(buf, 0, index);

/*
   The SOCKS request information is sent by the client as soon as it has
   established a connection to the SOCKS server, and completed the
   authentication negotiations.  The server evaluates the request, and
   returns a reply formed as follows:

        +----+-----+-------+------+----------+----------+
        |VER | REP |  RSV  | ATYP | BND.ADDR | BND.PORT |
        +----+-----+-------+------+----------+----------+
        | 1  |  1  | X'00' |  1   | Variable |    2     |
        +----+-----+-------+------+----------+----------+

   Where:

   o  VER    protocol version: X'05'
   o  REP    Reply field:
      o  X'00' succeeded
      o  X'01' general SOCKS server failure
      o  X'02' connection not allowed by ruleset
      o  X'03' Network unreachable
      o  X'04' Host unreachable
      o  X'05' Connection refused
      o  X'06' TTL expired
      o  X'07' Command not supported
      o  X'08' Address type not supported
      o  X'09' to X'FF' unassigned
    o  RSV    RESERVED
    o  ATYP   address type of following address
      o  IP V4 address: X'01'
      o  DOMAINNAME: X'03'
      o  IP V6 address: X'04'
    o  BND.ADDR       server bound address
    o  BND.PORT       server bound port in network octet order
*/

      //in.read(buf, 0, 4);
      fill(in, buf, 4);

      if(buf[1]!=0){
        try{ socket.close(); }
        catch(Exception eee){
        }
        throw new JSchException("ProxySOCKS5: server returns "+buf[1]);
      }

      switch(buf[3]&0xff){
        case 1:
          //in.read(buf, 0, 6);
          fill(in, buf, 6);
          break;
        case 3:
          //in.read(buf, 0, 1);
          fill(in, buf, 1);
          //in.read(buf, 0, buf[0]+2);
          fill(in, buf, (buf[0]&0xff)+2);
          break;
        case 4:
          //in.read(buf, 0, 18);
          fill(in, buf, 18);
          break;
        default:
      }
    }
    catch(RuntimeException e){
      throw e;
    }
    catch(Exception e){
      try{ if(socket!=null)socket.close(); }
      catch(Exception eee){
      }
      String message="ProxySOCKS5: "+e.toString();
      if(e instanceof Throwable)
        throw new JSchException(message, (Throwable)e);
      throw new JSchException(message);
    }
  }
  public InputStream getInputStream(){ return in; }
  public OutputStream getOutputStream(){ return out; }
  public Socket getSocket(){ return socket; }
  public void close(){
    try{
      if(in!=null)in.close();
      if(out!=null)out.close();
      if(socket!=null)socket.close();
    }
    catch(Exception e){
    }
    in=null;
    out=null;
    socket=null;
  }
  public static int getDefaultPort(){
    return DEFAULTPORT;
  }
  private void fill(InputStream in, byte[] buf, int len) throws JSchException, IOException{
    int s=0;
    while(s<len){
      int i=in.read(buf, s, len-s);
      if(i<=0){
        throw new JSchException("ProxySOCKS5: stream is closed");
      }
      s+=i;
    }
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Random{
  void fill(byte[] foo, int start, int len);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2006-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestAgentForwarding extends Request{
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    setReply(false);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    // byte      SSH_MSG_CHANNEL_REQUEST(98)
    // uint32 recipient channel
    // string request type        // "auth-agent-req@openssh.com"
    // boolean want reply         // 0
    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("auth-agent-req@openssh.com"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    write(packet);
    session.agent_forwarding=true;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestEnv extends Request{
  byte[] name=new byte[0];
  byte[] value=new byte[0];
  void setEnv(byte[] name, byte[] value){
    this.name=name;
    this.value=value;
  }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("env"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putString(name);
    buf.putString(value);
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestExec extends Request{
  private byte[] command=new byte[0];
  RequestExec(byte[] command){
    this.command=command;
  }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    // send
    // byte     SSH_MSG_CHANNEL_REQUEST(98)
    // uint32 recipient channel
    // string request type       // "exec"
    // boolean want reply        // 0
    // string command
    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("exec"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.checkFreeSize(4+command.length);
    buf.putString(command);
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



abstract class Request{
  private boolean reply=false;
  private Session session=null;
  private Channel channel=null;
  void request(Session session, Channel channel) throws Exception{
    this.session=session;
    this.channel=channel;
    if(channel.connectTimeout>0){
      setReply(true);
    }
  }
  boolean waitForReply(){ return reply; }
  void setReply(boolean reply){ this.reply=reply; }
  void write(Packet packet) throws Exception{
    if(reply){
      channel.reply=-1;
    }
    session.write(packet);
    if(reply){
      long start=System.currentTimeMillis();
      long timeout=channel.connectTimeout;
      while(channel.isConnected() && channel.reply==-1){
        try{Thread.sleep(10);}
        catch(Exception ee){
        }
        if(timeout>0L &&
           (System.currentTimeMillis()-start)>timeout){
          channel.reply=0;
          throw new JSchException("channel request: timeout");
        }
      }

      if(channel.reply==0){
        throw new JSchException("failed to send channel request");
      }
    }
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestPtyReq extends Request{
  private String ttype="vt100";
  private int tcol=80;
  private int trow=24;
  private int twp=640;
  private int thp=480;

  private byte[] terminal_mode=Util.empty;

  void setCode(String cookie){
  }

  void setTType(String ttype){
    this.ttype=ttype;
  }

  void setTerminalMode(byte[] terminal_mode){
    this.terminal_mode=terminal_mode;
  }

  void setTSize(int tcol, int trow, int twp, int thp){
    this.tcol=tcol;
    this.trow=trow;
    this.twp=twp;
    this.thp=thp;
  }

  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("pty-req"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putString(Util.str2byte(ttype));
    buf.putInt(tcol);
    buf.putInt(trow);
    buf.putInt(twp);
    buf.putInt(thp);
    buf.putString(terminal_mode);
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestSftp extends Request{
  RequestSftp(){
    setReply(true);
  }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);
    packet.reset();
    buf.putByte((byte)Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("subsystem"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putString(Util.str2byte("sftp"));
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestShell extends Request{
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    // send
    // byte     SSH_MSG_CHANNEL_REQUEST(98)
    // uint32 recipient channel
    // string request type       // "shell"
    // boolean want reply        // 0
    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("shell"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestSignal extends Request{
  private String signal="KILL";
  public void setSignal(String foo){ signal=foo; }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("signal"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putString(Util.str2byte(signal));
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2005-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestSubsystem extends Request{
  private String subsystem=null;
  public void request(Session session, Channel channel, String subsystem, boolean want_reply) throws Exception{
    setReply(want_reply);
    this.subsystem=subsystem;
    this.request(session, channel);
  }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    packet.reset();
    buf.putByte((byte)Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("subsystem"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putString(Util.str2byte(subsystem));
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestWindowChange extends Request{
  int width_columns=80;
  int height_rows=24;
  int width_pixels=640;
  int height_pixels=480;
  void setSize(int col, int row, int wp, int hp){
    this.width_columns=col;
    this.height_rows=row;
    this.width_pixels=wp;
    this.height_pixels=hp;
  }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    //byte      SSH_MSG_CHANNEL_REQUEST
    //uint32    recipient_channel
    //string    "window-change"
    //boolean   FALSE
    //uint32    terminal width, columns
    //uint32    terminal height, rows
    //uint32    terminal width, pixels
    //uint32    terminal height, pixels
    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("window-change"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putInt(width_columns);
    buf.putInt(height_rows);
    buf.putInt(width_pixels);
    buf.putInt(height_pixels);
    write(packet);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class RequestX11 extends Request{
  public void setCookie(String cookie){
    ChannelX11.cookie=Util.str2byte(cookie);
  }
  public void request(Session session, Channel channel) throws Exception{
    super.request(session, channel);

    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);

    // byte      SSH_MSG_CHANNEL_REQUEST(98)
    // uint32 recipient channel
    // string request type        // "x11-req"
    // boolean want reply         // 0
    // boolean   single connection
    // string    x11 authentication protocol // "MIT-MAGIC-COOKIE-1".
    // string    x11 authentication cookie
    // uint32    x11 screen number
    packet.reset();
    buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
    buf.putInt(channel.getRecipient());
    buf.putString(Util.str2byte("x11-req"));
    buf.putByte((byte)(waitForReply() ? 1 : 0));
    buf.putByte((byte)0);
    buf.putString(Util.str2byte("MIT-MAGIC-COOKIE-1"));
    buf.putString(ChannelX11.getFakedCookie(session));
    buf.putInt(0);
    write(packet);

    session.x11_forwarding=true;
  }
}


/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the file transfer from remote to local
 *   $ CLASSPATH=.:../build javac ScpFrom.java
 *   $ CLASSPATH=.:../build java ScpFrom user@remotehost:file1 file2
 * You will be asked passwd.
 * If everything works fine, a file 'file1' on 'remotehost' will copied to
 * local 'file1'.
 *
 */
//import com.jcraft.jsch.*;

class ScpFrom{

  public static void custom(String[] arg){
    if(arg.length!=2 || !arg[0].contains(",") || !arg[0].contains("@")){
      System.err.println("usage: y scp user,pass@remotehost:file1 file2");
      System.exit(-1);
    }

    FileOutputStream fos=null;
    try{

      String senha=arg[0].split("@")[0].split(",")[1];
      arg=new String[]{arg[0].split("@")[0].split(",")[0]+"@"+arg[0].split("@")[1],arg[1]};

      String user=arg[0].substring(0, arg[0].indexOf('@'));
      arg[0]=arg[0].substring(arg[0].indexOf('@')+1);
      String host=arg[0].substring(0, arg[0].indexOf(':'));
      String rfile=arg[0].substring(arg[0].indexOf(':')+1);
      String lfile=arg[1];

      String prefix=null;
      if(new File(lfile).isDirectory()){
        prefix=lfile+File.separator;
      }

      JSch jsch=new JSch();
      Session session=jsch.getSession(user, host, 22);

      // username and password will be given via UserInfo interface.
      UserInfo ui=new MyUserInfo(senha);
      session.setUserInfo(ui);
      session.connect();

      // exec 'scp -f rfile' remotely
      String command="scp -f "+rfile;
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);

      // get I/O streams for remote scp
      OutputStream out=channel.getOutputStream();
      InputStream in=channel.getInputStream();

      channel.connect();

      byte[] buf=new byte[1024];

      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();

      while(true){
        int c=checkAck(in);
        if(c!='C'){
          break;
        }

        // read '0644 '
        in.read(buf, 0, 5);

        long filesize=0L;
        while(true){
          if(in.read(buf, 0, 1)<0){
            // error
            break;
          }
          if(buf[0]==' ')break;
          filesize=filesize*10L+(long)(buf[0]-'0');
        }

        String file=null;
        for(int i=0;;i++){
          in.read(buf, i, 1);
          if(buf[i]==(byte)0x0a){
            file=new String(buf, 0, i);
            break;
          }
        }

        //System.out.println("filesize="+filesize+", file="+file);

        // send '\0'
        buf[0]=0; out.write(buf, 0, 1); out.flush();

        // read a content of lfile
        fos=new FileOutputStream(prefix==null ? lfile : prefix+file);
        int foo;
        while(true){
          if(buf.length<filesize) foo=buf.length;
          else foo=(int)filesize;
          foo=in.read(buf, 0, foo);
          if(foo<0){
            // error
            break;
          }
          fos.write(buf, 0, foo);
          filesize-=foo;
          if(filesize==0L) break;
        }
        fos.close();
        fos=null;

        if(checkAck(in)!=0){
          System.exit(0);
        }

        // send '\0'
        buf[0]=0; out.write(buf, 0, 1); out.flush();
      }

      session.disconnect();

      System.exit(0);
    }
    catch(Exception e){
      System.out.println(e);
      try{if(fos!=null)fos.close();}catch(Exception ee){}
    }
  }

  static int checkAck(InputStream in) throws IOException{
    int b=in.read();
    // b may be 0 for success,
    //          1 for error,
    //          2 for fatal error,
    //          -1
    if(b==0) return b;
    if(b==-1) return b;

    if(b==1 || b==2){
      StringBuffer sb=new StringBuffer();
      int c;
      do {
        c=in.read();
        sb.append((char)c);
      }
      while(c!='\n');
      if(b==1){ // error
        System.out.print(sb.toString());
      }
      if(b==2){ // fatal error
        System.out.print(sb.toString());
      }
    }
    return b;
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


/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the file transfer from local to remote.
 *   $ CLASSPATH=.:../build javac ScpTo.java
 *   $ CLASSPATH=.:../build java ScpTo file1 user@remotehost:file2
 * You will be asked passwd.
 * If everything works fine, a local file 'file1' will copied to
 * 'file2' on 'remotehost'.
 *
 */
//import com.jcraft.jsch.*;

class ScpTo{

  public static void custom(String[] arg){
    if(arg.length!=2 || !arg[1].contains(",") || !arg[1].contains("@") ){
      System.err.println("usage: y scp file1 user,pass@remotehost:file2");
      System.exit(-1);
    }

    FileInputStream fis=null;
    try{

      String senha=arg[1].split("@")[0].split(",")[1];
      arg=new String[]{arg[0],arg[1].split("@")[0].split(",")[0]+"@"+arg[1].split("@")[1]};

      String lfile=arg[0];
      String user=arg[1].substring(0, arg[1].indexOf('@'));
      arg[1]=arg[1].substring(arg[1].indexOf('@')+1);
      String host=arg[1].substring(0, arg[1].indexOf(':'));
      String rfile=arg[1].substring(arg[1].indexOf(':')+1);

      JSch jsch=new JSch();
      Session session=jsch.getSession(user, host, 22);

      // username and password will be given via UserInfo interface.
      UserInfo ui=new MyUserInfo(senha);
      session.setUserInfo(ui);
      session.connect();

      boolean ptimestamp = true;

      // exec 'scp -t rfile' remotely
      String command="scp " + (ptimestamp ? "-p" :"") +" -t "+rfile;
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);

      // get I/O streams for remote scp
      OutputStream out=channel.getOutputStream();
      InputStream in=channel.getInputStream();

      channel.connect();

      if(checkAck(in)!=0){
        System.exit(0);
      }

      File _lfile = new File(lfile);

      if(ptimestamp){
        command="T"+(_lfile.lastModified()/1000)+" 0";
        // The access time should be sent here,
        // but it is not accessible with JavaAPI ;-<
        command+=(" "+(_lfile.lastModified()/1000)+" 0\n");
        out.write(command.getBytes()); out.flush();
        if(checkAck(in)!=0){
          System.exit(0);
        }
      }

      // send "C0644 filesize filename", where filename should not include '/'
      long filesize=_lfile.length();
      command="C0644 "+filesize+" ";
      if(lfile.lastIndexOf('/')>0){
        command+=lfile.substring(lfile.lastIndexOf('/')+1);
      }
      else{
        command+=lfile;
      }
      command+="\n";
      out.write(command.getBytes()); out.flush();
      if(checkAck(in)!=0){
        System.exit(0);
      }

      // send a content of lfile
      fis=new FileInputStream(lfile);
      byte[] buf=new byte[1024];
      while(true){
        int len=fis.read(buf, 0, buf.length);
        if(len<=0) break;
        out.write(buf, 0, len); //out.flush();
      }
      fis.close();
      fis=null;
      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();
      if(checkAck(in)!=0){
        System.exit(0);
      }
      out.close();

      channel.disconnect();
      session.disconnect();

      System.exit(0);
    }
    catch(Exception e){
      System.out.println(e);
      try{if(fis!=null)fis.close();}catch(Exception ee){}
    }
  }

  static int checkAck(InputStream in) throws IOException{
    int b=in.read();
    // b may be 0 for success,
    //          1 for error,
    //          2 for fatal error,
    //          -1
    if(b==0) return b;
    if(b==-1) return b;

    if(b==1 || b==2){
      StringBuffer sb=new StringBuffer();
      int c;
      do {
        c=in.read();
        sb.append((char)c);
      }
      while(c!='\n');
      if(b==1){ // error
        System.out.print(sb.toString());
      }
      if(b==2){ // fatal error
        System.out.print(sb.toString());
      }
    }
    return b;
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
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




interface ServerSocketFactory{
  public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class Session implements Runnable{

  // http://ietf.org/internet-drafts/draft-ietf-secsh-assignednumbers-01.txt
  static final int SSH_MSG_DISCONNECT=                      1;
  static final int SSH_MSG_IGNORE=                          2;
  static final int SSH_MSG_UNIMPLEMENTED=                   3;
  static final int SSH_MSG_DEBUG=                           4;
  static final int SSH_MSG_SERVICE_REQUEST=                 5;
  static final int SSH_MSG_SERVICE_ACCEPT=                  6;
  static final int SSH_MSG_KEXINIT=                        20;
  static final int SSH_MSG_NEWKEYS=                        21;
  static final int SSH_MSG_KEXDH_INIT=                     30;
  static final int SSH_MSG_KEXDH_REPLY=                    31;
  static final int SSH_MSG_KEX_DH_GEX_GROUP=               31;
  static final int SSH_MSG_KEX_DH_GEX_INIT=                32;
  static final int SSH_MSG_KEX_DH_GEX_REPLY=               33;
  static final int SSH_MSG_KEX_DH_GEX_REQUEST=             34;
  static final int SSH_MSG_GLOBAL_REQUEST=                 80;
  static final int SSH_MSG_REQUEST_SUCCESS=                81;
  static final int SSH_MSG_REQUEST_FAILURE=                82;
  static final int SSH_MSG_CHANNEL_OPEN=                   90;
  static final int SSH_MSG_CHANNEL_OPEN_CONFIRMATION=      91;
  static final int SSH_MSG_CHANNEL_OPEN_FAILURE=           92;
  static final int SSH_MSG_CHANNEL_WINDOW_ADJUST=          93;
  static final int SSH_MSG_CHANNEL_DATA=                   94;
  static final int SSH_MSG_CHANNEL_EXTENDED_DATA=          95;
  static final int SSH_MSG_CHANNEL_EOF=                    96;
  static final int SSH_MSG_CHANNEL_CLOSE=                  97;
  static final int SSH_MSG_CHANNEL_REQUEST=                98;
  static final int SSH_MSG_CHANNEL_SUCCESS=                99;
  static final int SSH_MSG_CHANNEL_FAILURE=               100;

  private static final int PACKET_MAX_SIZE = 256 * 1024;

  private byte[] V_S;                                 // server version
  private byte[] V_C=Util.str2byte("SSH-2.0-JSCH-"+JSch.VERSION); // client version

  private byte[] I_C; // the payload of the client's SSH_MSG_KEXINIT
  private byte[] I_S; // the payload of the server's SSH_MSG_KEXINIT
  private byte[] K_S; // the host key

  private byte[] session_id;

  private byte[] IVc2s;
  private byte[] IVs2c;
  private byte[] Ec2s;
  private byte[] Es2c;
  private byte[] MACc2s;
  private byte[] MACs2c;

  private int seqi=0;
  private int seqo=0;

  String[] guess=null;
  private Cipher s2ccipher;
  private Cipher c2scipher;
  private MAC s2cmac;
  private MAC c2smac;
  //private byte[] mac_buf;
  private byte[] s2cmac_result1;
  private byte[] s2cmac_result2;

  private Compression deflater;
  private Compression inflater;

  private IO io;
  private Socket socket;
  private int timeout=0;

  private volatile boolean isConnected=false;

  private boolean isAuthed=false;

  private Thread connectThread=null;
  private Object lock=new Object();

  boolean x11_forwarding=false;
  boolean agent_forwarding=false;

  InputStream in=null;
  OutputStream out=null;

  static Random random;

  Buffer buf;
  Packet packet;

  SocketFactory socket_factory=null;

  static final int buffer_margin = 32 + // maximum padding length
                                   64 + // maximum mac length
                                   32;  // margin for deflater; deflater may inflate data

  private java.util.Hashtable config=null;

  private Proxy proxy=null;
  private UserInfo userinfo;

  private String hostKeyAlias=null;
  private int serverAliveInterval=0;
  private int serverAliveCountMax=1;

  private IdentityRepository identityRepository = null;
  private HostKeyRepository hostkeyRepository = null;

  protected boolean daemon_thread=false;

  private long kex_start_time=0L;

  int max_auth_tries = 6;
  int auth_failures = 0;

  String host="127.0.0.1";
  String org_host="127.0.0.1";
  int port=22;

  String username=null;
  byte[] password=null;

  JSch jsch;

  Session(JSch jsch, String username, String host, int port) throws JSchException{
    super();
    this.jsch=jsch;
    buf=new Buffer();
    packet=new Packet(buf);
    this.username = username;
    this.org_host = this.host = host;
    this.port = port;

    applyConfig();

    if(this.username==null) {
      try {
        this.username=(String)(System.getProperties().get("user.name"));
      }
      catch(SecurityException e){
        // ignore e
      }
    }

    if(this.username==null) {
      throw new JSchException("username is not given.");
    }
  }

  public void connect() throws JSchException{
    connect(timeout);
  }

  public void connect(int connectTimeout) throws JSchException{
    if(isConnected){
      throw new JSchException("session is already connected");
    }

    io=new IO();
    if(random==null){
      try{
        Class c=Class.forName(getConfig("random"));
        random=(Random)(c.newInstance());
      }
      catch(Exception e){
        throw new JSchException(e.toString(), e);
      }
    }
    Packet.setRandom(random);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "Connecting to "+host+" port "+port);
    }

    try {
      int i, j;

      if(proxy==null){
        InputStream in;
        OutputStream out;
        if(socket_factory==null){
          socket=Util.createSocket(host, port, connectTimeout);
          in=socket.getInputStream();
          out=socket.getOutputStream();
        }
        else{
          socket=socket_factory.createSocket(host, port);
          in=socket_factory.getInputStream(socket);
          out=socket_factory.getOutputStream(socket);
        }
        //if(timeout>0){ socket.setSoTimeout(timeout); }
        socket.setTcpNoDelay(true);
        io.setInputStream(in);
        io.setOutputStream(out);
      }
      else{
        synchronized(proxy){
          proxy.connect(socket_factory, host, port, connectTimeout);
          io.setInputStream(proxy.getInputStream());
          io.setOutputStream(proxy.getOutputStream());
          socket=proxy.getSocket();
        }
      }

      if(connectTimeout>0 && socket!=null){
        socket.setSoTimeout(connectTimeout);
      }

      isConnected=true;

      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "Connection established");
      }

      jsch.addSession(this);

      {
        // Some Cisco devices will miss to read '\n' if it is sent separately.
        byte[] foo=new byte[V_C.length+1];
        System.arraycopy(V_C, 0, foo, 0, V_C.length);
        foo[foo.length-1]=(byte)'\n';
        io.put(foo, 0, foo.length);
      }

      while(true){
        i=0;
        j=0;
        while(i<buf.buffer.length){
          j=io.getByte();
          if(j<0)break;
          buf.buffer[i]=(byte)j; i++;
          if(j==10)break;
        }
        if(j<0){
          throw new JSchException("connection is closed by foreign host");
        }

        if(buf.buffer[i-1]==10){    // 0x0a
          i--;
          if(i>0 && buf.buffer[i-1]==13){  // 0x0d
            i--;
          }
        }

        if(i<=3 ||
           ((i!=buf.buffer.length) &&
            (buf.buffer[0]!='S'||buf.buffer[1]!='S'||
             buf.buffer[2]!='H'||buf.buffer[3]!='-'))){
          // It must not start with 'SSH-'
          continue;
        }

        if(i==buf.buffer.length ||
           i<7 ||                                      // SSH-1.99 or SSH-2.0
           (buf.buffer[4]=='1' && buf.buffer[6]!='9')  // SSH-1.5
           ){
          throw new JSchException("invalid server's version string");
        }
        break;
      }

      V_S=new byte[i]; System.arraycopy(buf.buffer, 0, V_S, 0, i);

      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "Remote version string: "+Util.byte2str(V_S));
        JSch.getLogger().log(Logger.INFO,
                             "Local version string: "+Util.byte2str(V_C));
      }

      send_kexinit();

      buf=read(buf);
      if(buf.getCommand()!=SSH_MSG_KEXINIT){
        in_kex=false;
        throw new JSchException("invalid protocol: "+buf.getCommand());
      }

      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "SSH_MSG_KEXINIT received");
      }

      KeyExchange kex=receive_kexinit(buf);

      while(true){
        buf=read(buf);
        if(kex.getState()==buf.getCommand()){
          kex_start_time=System.currentTimeMillis();
          boolean result=kex.next(buf);
          if(!result){
            in_kex=false;
            throw new JSchException("verify: "+result);
          }
        }
        else{
          in_kex=false;
          throw new JSchException("invalid protocol(kex): "+buf.getCommand());
        }
        if(kex.getState()==KeyExchange.STATE_END){
          break;
        }
      }

      try{
        long tmp=System.currentTimeMillis();
        in_prompt = true;
        checkHost(host, port, kex);
        in_prompt = false;
        kex_start_time+=(System.currentTimeMillis()-tmp);
      }
      catch(JSchException ee){
        in_kex=false;
        in_prompt = false;
        throw ee;
      }

      send_newkeys();

      // receive SSH_MSG_NEWKEYS(21)
      buf=read(buf);
      if(buf.getCommand()==SSH_MSG_NEWKEYS){

        if(JSch.getLogger().isEnabled(Logger.INFO)){
          JSch.getLogger().log(Logger.INFO,
                               "SSH_MSG_NEWKEYS received");
        }

        receive_newkeys(buf, kex);
      }
      else{
        in_kex=false;
        throw new JSchException("invalid protocol(newkyes): "+buf.getCommand());
      }

      try{
        String s = getConfig("MaxAuthTries");
        if(s!=null){
          max_auth_tries = Integer.parseInt(s);
        }
      }
      catch(NumberFormatException e){
        throw new JSchException("MaxAuthTries: "+getConfig("MaxAuthTries"), e);
      }

      boolean auth=false;
      boolean auth_cancel=false;

      UserAuth ua=null;
      try{
        Class c=Class.forName(getConfig("userauth.none"));
        ua=(UserAuth)(c.newInstance());
      }
      catch(Exception e){
        throw new JSchException(e.toString(), e);
      }

      auth=ua.start(this);

      String cmethods=getConfig("PreferredAuthentications");

      String[] cmethoda=Util.split(cmethods, ",");

      String smethods=null;
      if(!auth){
        smethods=((UserAuthNone)ua).getMethods();
        if(smethods!=null){
          smethods=smethods.toLowerCase();
        }
        else{
          smethods=cmethods;
        }
      }

      String[] smethoda=Util.split(smethods, ",");

      int methodi=0;

      loop:
      while(true){

        while(!auth &&
              cmethoda!=null && methodi<cmethoda.length){

          String method=cmethoda[methodi++];
          boolean acceptable=false;
          for(int k=0; k<smethoda.length; k++){
            if(smethoda[k].equals(method)){
              acceptable=true;
              break;
            }
          }
          if(!acceptable){
            continue;
          }

          if(JSch.getLogger().isEnabled(Logger.INFO)){
            String str="Authentications that can continue: ";
            for(int k=methodi-1; k<cmethoda.length; k++){
              str+=cmethoda[k];
              if(k+1<cmethoda.length)
                str+=",";
            }
            JSch.getLogger().log(Logger.INFO,
                                 str);
            JSch.getLogger().log(Logger.INFO,
                                 "Next authentication method: "+method);
          }

          ua=null;
          try{
            Class c=null;
            if(getConfig("userauth."+method)!=null){
              c=Class.forName(getConfig("userauth."+method));
              ua=(UserAuth)(c.newInstance());
            }
          }
          catch(Exception e){
            if(JSch.getLogger().isEnabled(Logger.WARN)){
              JSch.getLogger().log(Logger.WARN,
                                   "failed to load "+method+" method");
            }
          }

          if(ua!=null){
            auth_cancel=false;
            try{
              auth=ua.start(this);
              if(auth &&
                 JSch.getLogger().isEnabled(Logger.INFO)){
                JSch.getLogger().log(Logger.INFO,
                                     "Authentication succeeded ("+method+").");
              }
            }
            catch(JSchAuthCancelException ee){
              auth_cancel=true;
            }
            catch(JSchPartialAuthException ee){
              String tmp = smethods;
              smethods=ee.getMethods();
              smethoda=Util.split(smethods, ",");
              if(!tmp.equals(smethods)){
                methodi=0;
              }
              auth_cancel=false;
              continue loop;
            }
            catch(RuntimeException ee){
              throw ee;
            }
            catch(JSchException ee){
              throw ee;
            }
            catch(Exception ee){
              if(JSch.getLogger().isEnabled(Logger.WARN)){
                JSch.getLogger().log(Logger.WARN,
                                     "an exception during authentication\n"+ee.toString());
              }
              break loop;
            }
          }
        }
        break;
      }

      if(!auth){
        if(auth_failures >= max_auth_tries){
          if(JSch.getLogger().isEnabled(Logger.INFO)){
            JSch.getLogger().log(Logger.INFO,
                                 "Login trials exceeds "+max_auth_tries);
          }
        }
        if(auth_cancel)
          throw new JSchException("Auth cancel");
        throw new JSchException("Auth fail");
      }

      if(socket!=null && (connectTimeout>0 || timeout>0)){
        socket.setSoTimeout(timeout);
      }

      isAuthed=true;

      synchronized(lock){
        if(isConnected){
          connectThread=new Thread(this);
          connectThread.setName("Connect thread "+host+" session");
          if(daemon_thread){
            connectThread.setDaemon(daemon_thread);
          }
          connectThread.start();

          requestPortForwarding();
        }
        else{
          // The session has been already down and
          // we don't have to start new thread.
        }
      }
    }
    catch(Exception e) {
      in_kex=false;
      try{
        if(isConnected){
          String message = e.toString();
          packet.reset();
          buf.checkFreeSize(1+4*3+message.length()+2+buffer_margin);
          buf.putByte((byte)SSH_MSG_DISCONNECT);
          buf.putInt(3);
          buf.putString(Util.str2byte(message));
          buf.putString(Util.str2byte("en"));
          write(packet);
        }
      }
      catch(Exception ee){}
      try{ disconnect(); } catch(Exception ee){ }
      isConnected=false;
      //e.printStackTrace();
      if(e instanceof RuntimeException) throw (RuntimeException)e;
      if(e instanceof JSchException) throw (JSchException)e;
      throw new JSchException("Session.connect: "+e);
    }
    finally{
      Util.bzero(this.password);
      this.password=null;
    }
  }

  private KeyExchange receive_kexinit(Buffer buf) throws Exception {
    int j=buf.getInt();
    if(j!=buf.getLength()){    // packet was compressed and
      buf.getByte();           // j is the size of deflated packet.
      I_S=new byte[buf.index-5];
    }
    else{
      I_S=new byte[j-1-buf.getByte()];
    }
   System.arraycopy(buf.buffer, buf.s, I_S, 0, I_S.length);

   if(!in_kex){     // We are in rekeying activated by the remote!
     send_kexinit();
   }

    guess=KeyExchange.guess(I_S, I_C);
    if(guess==null){
      throw new JSchException("Algorithm negotiation fail");
    }

    if(!isAuthed &&
       (guess[KeyExchange.PROPOSAL_ENC_ALGS_CTOS].equals("none") ||
        (guess[KeyExchange.PROPOSAL_ENC_ALGS_STOC].equals("none")))){
      throw new JSchException("NONE Cipher should not be chosen before authentification is successed.");
    }

    KeyExchange kex=null;
    try{
      Class c=Class.forName(getConfig(guess[KeyExchange.PROPOSAL_KEX_ALGS]));
      kex=(KeyExchange)(c.newInstance());
    }
    catch(Exception e){
      throw new JSchException(e.toString(), e);
    }

    kex.init(this, V_S, V_C, I_S, I_C);
    return kex;
  }

  private volatile boolean in_kex=false;
  private volatile boolean in_prompt=false;
  public void rekey() throws Exception {
    send_kexinit();
  }
  private void send_kexinit() throws Exception {
    if(in_kex)
      return;

    String cipherc2s=getConfig("cipher.c2s");
    String ciphers2c=getConfig("cipher.s2c");

    String[] not_available_ciphers=checkCiphers(getConfig("CheckCiphers"));
    if(not_available_ciphers!=null && not_available_ciphers.length>0){
      cipherc2s=Util.diffString(cipherc2s, not_available_ciphers);
      ciphers2c=Util.diffString(ciphers2c, not_available_ciphers);
      if(cipherc2s==null || ciphers2c==null){
        throw new JSchException("There are not any available ciphers.");
      }
    }

    String kex=getConfig("kex");
    String[] not_available_kexes=checkKexes(getConfig("CheckKexes"));
    if(not_available_kexes!=null && not_available_kexes.length>0){
      kex=Util.diffString(kex, not_available_kexes);
      if(kex==null){
        throw new JSchException("There are not any available kexes.");
      }
    }

    String server_host_key = getConfig("server_host_key");
    String[] not_available_shks =
      checkSignatures(getConfig("CheckSignatures"));
    if(not_available_shks!=null && not_available_shks.length>0){
      server_host_key=Util.diffString(server_host_key, not_available_shks);
      if(server_host_key==null){
        throw new JSchException("There are not any available sig algorithm.");
      }
    }

    in_kex=true;
    kex_start_time=System.currentTimeMillis();

    Buffer buf = new Buffer();                // send_kexinit may be invoked
    Packet packet = new Packet(buf);          // by user thread.
    packet.reset();
    buf.putByte((byte) SSH_MSG_KEXINIT);
    synchronized(random){
      random.fill(buf.buffer, buf.index, 16); buf.skip(16);
    }
    buf.putString(Util.str2byte(kex));
    buf.putString(Util.str2byte(server_host_key));
    buf.putString(Util.str2byte(cipherc2s));
    buf.putString(Util.str2byte(ciphers2c));
    buf.putString(Util.str2byte(getConfig("mac.c2s")));
    buf.putString(Util.str2byte(getConfig("mac.s2c")));
    buf.putString(Util.str2byte(getConfig("compression.c2s")));
    buf.putString(Util.str2byte(getConfig("compression.s2c")));
    buf.putString(Util.str2byte(getConfig("lang.c2s")));
    buf.putString(Util.str2byte(getConfig("lang.s2c")));
    buf.putByte((byte)0);
    buf.putInt(0);

    buf.setOffSet(5);
    I_C=new byte[buf.getLength()];
    buf.getByte(I_C);

    write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_KEXINIT sent");
    }
  }

  private void send_newkeys() throws Exception {
    // send SSH_MSG_NEWKEYS(21)
    packet.reset();
    buf.putByte((byte)SSH_MSG_NEWKEYS);
    write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_NEWKEYS sent");
    }
  }

  private void checkHost(String chost, int port, KeyExchange kex) throws JSchException {
    String shkc=getConfig("StrictHostKeyChecking");

    if(hostKeyAlias!=null){
      chost=hostKeyAlias;
    }

    byte[] K_S=kex.getHostKey();
    String key_type=kex.getKeyType();
    String key_fprint=kex.getFingerPrint();

    if(hostKeyAlias==null && port!=22){
      chost=("["+chost+"]:"+port);
    }

    HostKeyRepository hkr=getHostKeyRepository();

    String hkh=getConfig("HashKnownHosts");
    if(hkh.equals("yes") && (hkr instanceof KnownHosts)){
      hostkey=((KnownHosts)hkr).createHashedHostKey(chost, K_S);
    }
    else{
      hostkey=new HostKey(chost, K_S);
    }

    int i=0;
    synchronized(hkr){
      i=hkr.check(chost, K_S);
    }

    boolean insert=false;
    if((shkc.equals("ask") || shkc.equals("yes")) &&
       i==HostKeyRepository.CHANGED){
      String file=null;
      synchronized(hkr){
        file=hkr.getKnownHostsRepositoryID();
      }
      if(file==null){file="known_hosts";}

      boolean b=false;

      if(userinfo!=null){
        String message=
"WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED!\n"+
"IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!\n"+
"Someone could be eavesdropping on you right now (man-in-the-middle attack)!\n"+
"It is also possible that the "+key_type+" host key has just been changed.\n"+
"The fingerprint for the "+key_type+" key sent by the remote host "+chost+" is\n"+
key_fprint+".\n"+
"Please contact your system administrator.\n"+
"Add correct host key in "+file+" to get rid of this message.";

        if(shkc.equals("ask")){
          b=userinfo.promptYesNo(message+
                                 "\nDo you want to delete the old key and insert the new key?");
        }
        else{  // shkc.equals("yes")
          userinfo.showMessage(message);
        }
      }

      if(!b){
        throw new JSchException("HostKey has been changed: "+chost);
      }

      synchronized(hkr){
        hkr.remove(chost,
                   kex.getKeyAlgorithName(),
                   null);
        insert=true;
      }
    }

    if((shkc.equals("ask") || shkc.equals("yes")) &&
       (i!=HostKeyRepository.OK) && !insert){
      if(shkc.equals("yes")){
        throw new JSchException("reject HostKey: "+host);
      }
      if(userinfo!=null){
        boolean foo=userinfo.promptYesNo(
"The authenticity of host '"+host+"' can't be established.\n"+
key_type+" key fingerprint is "+key_fprint+".\n"+
"Are you sure you want to continue connecting?"
                                         );
        if(!foo){
          throw new JSchException("reject HostKey: "+host);
        }
        insert=true;
      }
      else{
        if(i==HostKeyRepository.NOT_INCLUDED)
          throw new JSchException("UnknownHostKey: "+host+". "+key_type+" key fingerprint is "+key_fprint);
        else
          throw new JSchException("HostKey has been changed: "+host);
      }
    }

    if(shkc.equals("no") &&
       HostKeyRepository.NOT_INCLUDED==i){
      insert=true;
    }

    if(i==HostKeyRepository.OK){
      HostKey[] keys =
        hkr.getHostKey(chost, kex.getKeyAlgorithName());
      String _key= Util.byte2str(Util.toBase64(K_S, 0, K_S.length));
      for(int j=0; j< keys.length; j++){
        if(keys[i].getKey().equals(_key) &&
           keys[j].getMarker().equals("@revoked")){
          if(userinfo!=null){
            userinfo.showMessage(
"The "+ key_type +" host key for "+ host +" is marked as revoked.\n"+
"This could mean that a stolen key is being used to "+
"impersonate this host.");
          }
          if(JSch.getLogger().isEnabled(Logger.INFO)){
            JSch.getLogger().log(Logger.INFO,
                                 "Host '"+host+"' has provided revoked key.");
          }
          throw new JSchException("revoked HostKey: "+host);
        }
      }
    }

    if(i==HostKeyRepository.OK &&
       JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "Host '"+host+"' is known and matches the "+key_type+" host key");
    }

    if(insert &&
       JSch.getLogger().isEnabled(Logger.WARN)){
      JSch.getLogger().log(Logger.WARN,
                           "Permanently added '"+host+"' ("+key_type+") to the list of known hosts.");
    }

    if(insert){
      synchronized(hkr){
        hkr.add(hostkey, userinfo);
      }
    }
  }

//public void start(){ (new Thread(this)).start();  }

  public Channel openChannel(String type) throws JSchException{
    if(!isConnected){
      throw new JSchException("session is down");
    }
    try{
      Channel channel=Channel.getChannel(type);
      addChannel(channel);
      channel.init();
      if(channel instanceof ChannelSession){
        applyConfigChannel((ChannelSession)channel);
      }
      return channel;
    }
    catch(Exception e){
      //e.printStackTrace();
    }
    return null;
  }

  // encode will bin invoked in write with synchronization.
  public void encode(Packet packet) throws Exception{

    if(deflater!=null){
      compress_len[0]=packet.buffer.index;
      packet.buffer.buffer=deflater.compress(packet.buffer.buffer,
                                             5, compress_len);
      packet.buffer.index=compress_len[0];
    }
    if(c2scipher!=null){
      //packet.padding(c2scipher.getIVSize());
      packet.padding(c2scipher_size);
      int pad=packet.buffer.buffer[4];
      synchronized(random){
        random.fill(packet.buffer.buffer, packet.buffer.index-pad, pad);
      }
    }
    else{
      packet.padding(8);
    }

    if(c2smac!=null){
      c2smac.update(seqo);
      c2smac.update(packet.buffer.buffer, 0, packet.buffer.index);
      c2smac.doFinal(packet.buffer.buffer, packet.buffer.index);
    }
    if(c2scipher!=null){
      byte[] buf=packet.buffer.buffer;
      c2scipher.update(buf, 0, packet.buffer.index, buf, 0);
    }
    if(c2smac!=null){
      packet.buffer.skip(c2smac.getBlockSize());
    }
  }

  int[] uncompress_len=new int[1];
  int[] compress_len=new int[1];

  private int s2ccipher_size=8;
  private int c2scipher_size=8;
  public Buffer read(Buffer buf) throws Exception{
    int j=0;
    while(true){
      buf.reset();
      io.getByte(buf.buffer, buf.index, s2ccipher_size);
      buf.index+=s2ccipher_size;
      if(s2ccipher!=null){
        s2ccipher.update(buf.buffer, 0, s2ccipher_size, buf.buffer, 0);
      }
      j=((buf.buffer[0]<<24)&0xff000000)|
        ((buf.buffer[1]<<16)&0x00ff0000)|
        ((buf.buffer[2]<< 8)&0x0000ff00)|
        ((buf.buffer[3]    )&0x000000ff);
      // RFC 4253 6.1. Maximum Packet Length
      if(j<5 || j>PACKET_MAX_SIZE){
        start_discard(buf, s2ccipher, s2cmac, j, PACKET_MAX_SIZE);
      }
      int need = j+4-s2ccipher_size;
      //if(need<0){
      //  throw new IOException("invalid data");
      //}
      if((buf.index+need)>buf.buffer.length){
        byte[] foo=new byte[buf.index+need];
        System.arraycopy(buf.buffer, 0, foo, 0, buf.index);
        buf.buffer=foo;
      }

      if((need%s2ccipher_size)!=0){
        String message="Bad packet length "+need;
        if(JSch.getLogger().isEnabled(Logger.FATAL)){
          JSch.getLogger().log(Logger.FATAL, message);
        }
        start_discard(buf, s2ccipher, s2cmac, j, PACKET_MAX_SIZE-s2ccipher_size);
      }

      if(need>0){
        io.getByte(buf.buffer, buf.index, need); buf.index+=(need);
        if(s2ccipher!=null){
          s2ccipher.update(buf.buffer, s2ccipher_size, need, buf.buffer, s2ccipher_size);
        }
      }

      if(s2cmac!=null){
        s2cmac.update(seqi);
        s2cmac.update(buf.buffer, 0, buf.index);

        s2cmac.doFinal(s2cmac_result1, 0);
        io.getByte(s2cmac_result2, 0, s2cmac_result2.length);
        if(!java.util.Arrays.equals(s2cmac_result1, s2cmac_result2)){
          if(need > PACKET_MAX_SIZE){
            throw new IOException("MAC Error");
          }
          start_discard(buf, s2ccipher, s2cmac, j, PACKET_MAX_SIZE-need);
          continue;
        }
      }

      seqi++;

      if(inflater!=null){
        //inflater.uncompress(buf);
        int pad=buf.buffer[4];
        uncompress_len[0]=buf.index-5-pad;
        byte[] foo=inflater.uncompress(buf.buffer, 5, uncompress_len);
        if(foo!=null){
          buf.buffer=foo;
          buf.index=5+uncompress_len[0];
        }
        else{
          break;
        }
      }

      int type=buf.getCommand()&0xff;
      if(type==SSH_MSG_DISCONNECT){
        buf.rewind();
        buf.getInt();buf.getShort();
        int reason_code=buf.getInt();
        byte[] description=buf.getString();
        byte[] language_tag=buf.getString();
        throw new JSchException("SSH_MSG_DISCONNECT: "+
                                    reason_code+
                                " "+Util.byte2str(description)+
                                " "+Util.byte2str(language_tag));
      }
      else if(type==SSH_MSG_IGNORE){
      }
      else if(type==SSH_MSG_UNIMPLEMENTED){
        buf.rewind();
        buf.getInt();buf.getShort();
        int reason_id=buf.getInt();
        if(JSch.getLogger().isEnabled(Logger.INFO)){
          JSch.getLogger().log(Logger.INFO,
                               "Received SSH_MSG_UNIMPLEMENTED for "+reason_id);
        }
      }
      else if(type==SSH_MSG_DEBUG){
        buf.rewind();
        buf.getInt();buf.getShort();
      }
      else if(type==SSH_MSG_CHANNEL_WINDOW_ADJUST){
          buf.rewind();
          buf.getInt();buf.getShort();
          Channel c=Channel.getChannel(buf.getInt(), this);
          if(c==null){
          }
          else{
            c.addRemoteWindowSize(buf.getUInt());
          }
      }
      else if(type==UserAuth.SSH_MSG_USERAUTH_SUCCESS){
        isAuthed=true;
        if(inflater==null && deflater==null){
          String method;
          method=guess[KeyExchange.PROPOSAL_COMP_ALGS_CTOS];
          initDeflater(method);
          method=guess[KeyExchange.PROPOSAL_COMP_ALGS_STOC];
          initInflater(method);
        }
        break;
      }
      else{
        break;
      }
    }
    buf.rewind();
    return buf;
  }

  private void start_discard(Buffer buf, Cipher cipher, MAC mac,
                             int packet_length, int discard) throws JSchException, IOException{
    MAC discard_mac = null;

    if(!cipher.isCBC()){
      throw new JSchException("Packet corrupt");
    }

    if(packet_length!=PACKET_MAX_SIZE && mac != null){
      discard_mac = mac;
    }

    discard -= buf.index;

    while(discard>0){
      buf.reset();
      int len = discard>buf.buffer.length ? buf.buffer.length : discard;
      io.getByte(buf.buffer, 0, len);
      if(discard_mac!=null){
        discard_mac.update(buf.buffer, 0, len);
      }
      discard -= len;
    }

    if(discard_mac!=null){
      discard_mac.doFinal(buf.buffer, 0);
    }

    throw new JSchException("Packet corrupt");
  }

  byte[] getSessionId(){
    return session_id;
  }

  private void receive_newkeys(Buffer buf, KeyExchange kex) throws Exception {
    updateKeys(kex);
    in_kex=false;
  }
  private void updateKeys(KeyExchange kex) throws Exception{
    byte[] K=kex.getK();
    byte[] H=kex.getH();
    HASH hash=kex.getHash();

    if(session_id==null){
      session_id=new byte[H.length];
      System.arraycopy(H, 0, session_id, 0, H.length);
    }

    buf.reset();
    buf.putMPInt(K);
    buf.putByte(H);
    buf.putByte((byte)0x41);
    buf.putByte(session_id);
    hash.update(buf.buffer, 0, buf.index);
    IVc2s=hash.digest();

    int j=buf.index-session_id.length-1;

    buf.buffer[j]++;
    hash.update(buf.buffer, 0, buf.index);
    IVs2c=hash.digest();

    buf.buffer[j]++;
    hash.update(buf.buffer, 0, buf.index);
    Ec2s=hash.digest();

    buf.buffer[j]++;
    hash.update(buf.buffer, 0, buf.index);
    Es2c=hash.digest();

    buf.buffer[j]++;
    hash.update(buf.buffer, 0, buf.index);
    MACc2s=hash.digest();

    buf.buffer[j]++;
    hash.update(buf.buffer, 0, buf.index);
    MACs2c=hash.digest();

    try{
      Class c;
      String method;

      method=guess[KeyExchange.PROPOSAL_ENC_ALGS_STOC];
      c=Class.forName(getConfig(method));
      s2ccipher=(Cipher)(c.newInstance());
      while(s2ccipher.getBlockSize()>Es2c.length){
        buf.reset();
        buf.putMPInt(K);
        buf.putByte(H);
        buf.putByte(Es2c);
        hash.update(buf.buffer, 0, buf.index);
        byte[] foo=hash.digest();
        byte[] bar=new byte[Es2c.length+foo.length];
        System.arraycopy(Es2c, 0, bar, 0, Es2c.length);
        System.arraycopy(foo, 0, bar, Es2c.length, foo.length);
        Es2c=bar;
      }
      s2ccipher.init(Cipher.DECRYPT_MODE, Es2c, IVs2c);
      s2ccipher_size=s2ccipher.getIVSize();

      method=guess[KeyExchange.PROPOSAL_MAC_ALGS_STOC];
      c=Class.forName(getConfig(method));
      s2cmac=(MAC)(c.newInstance());
      MACs2c = expandKey(buf, K, H, MACs2c, hash, s2cmac.getBlockSize());
      s2cmac.init(MACs2c);
      //mac_buf=new byte[s2cmac.getBlockSize()];
      s2cmac_result1=new byte[s2cmac.getBlockSize()];
      s2cmac_result2=new byte[s2cmac.getBlockSize()];

      method=guess[KeyExchange.PROPOSAL_ENC_ALGS_CTOS];
      c=Class.forName(getConfig(method));
      c2scipher=(Cipher)(c.newInstance());
      while(c2scipher.getBlockSize()>Ec2s.length){
        buf.reset();
        buf.putMPInt(K);
        buf.putByte(H);
        buf.putByte(Ec2s);
        hash.update(buf.buffer, 0, buf.index);
        byte[] foo=hash.digest();
        byte[] bar=new byte[Ec2s.length+foo.length];
        System.arraycopy(Ec2s, 0, bar, 0, Ec2s.length);
        System.arraycopy(foo, 0, bar, Ec2s.length, foo.length);
        Ec2s=bar;
      }
      c2scipher.init(Cipher.ENCRYPT_MODE, Ec2s, IVc2s);
      c2scipher_size=c2scipher.getIVSize();

      method=guess[KeyExchange.PROPOSAL_MAC_ALGS_CTOS];
      c=Class.forName(getConfig(method));
      c2smac=(MAC)(c.newInstance());
      MACc2s = expandKey(buf, K, H, MACc2s, hash, c2smac.getBlockSize());
      c2smac.init(MACc2s);

      method=guess[KeyExchange.PROPOSAL_COMP_ALGS_CTOS];
      initDeflater(method);

      method=guess[KeyExchange.PROPOSAL_COMP_ALGS_STOC];
      initInflater(method);
    }
    catch(Exception e){
      if(e instanceof JSchException)
        throw e;
      throw new JSchException(e.toString(), e);
    }
  }


  /*
   * RFC 4253  7.2. Output from Key Exchange
   * If the key length needed is longer than the output of the HASH, the
   * key is extended by computing HASH of the concatenation of K and H and
   * the entire key so far, and appending the resulting bytes (as many as
   * HASH generates) to the key.  This process is repeated until enough
   * key material is available; the key is taken from the beginning of
   * this value.  In other words:
   *   K1 = HASH(K || H || X || session_id)   (X is e.g., "A")
   *   K2 = HASH(K || H || K1)
   *   K3 = HASH(K || H || K1 || K2)
   *   ...
   *   key = K1 || K2 || K3 || ...
   */
  private byte[] expandKey(Buffer buf, byte[] K, byte[] H, byte[] key,
                           HASH hash, int required_length) throws Exception {
    byte[] result = key;
    int size = hash.getBlockSize();
    while(result.length < required_length){
      buf.reset();
      buf.putMPInt(K);
      buf.putByte(H);
      buf.putByte(result);
      hash.update(buf.buffer, 0, buf.index);
      byte[] tmp = new byte[result.length+size];
      System.arraycopy(result, 0, tmp, 0, result.length);
      System.arraycopy(hash.digest(), 0, tmp, result.length, size);
      Util.bzero(result);
      result = tmp;
    }
    return result;
  }

  /*public*/ /*synchronized*/ void write(Packet packet, Channel c, int length) throws Exception{
    long t = getTimeout();
    while(true){
      if(in_kex){
        if(t>0L && (System.currentTimeMillis()-kex_start_time)>t){
          throw new JSchException("timeout in waiting for rekeying process.");
        }
        try{Thread.sleep(10);}
        catch(java.lang.InterruptedException e){};
        continue;
      }
      synchronized(c){

        if(c.rwsize<length){
          try{
            c.notifyme++;
            c.wait(100);
          }
          catch(java.lang.InterruptedException e){
          }
          finally{
            c.notifyme--;
          }
        }

        if(in_kex){
          continue;
        }

        if(c.rwsize>=length){
          c.rwsize-=length;
          break;
        }

      }
      if(c.close || !c.isConnected()){
        throw new IOException("channel is broken");
      }

      boolean sendit=false;
      int s=0;
      byte command=0;
      int recipient=-1;
      synchronized(c){
        if(c.rwsize>0){
          long len=c.rwsize;
          if(len>length){
            len=length;
          }
          if(len!=length){
            s=packet.shift((int)len,
                           (c2scipher!=null ? c2scipher_size : 8),
                           (c2smac!=null ? c2smac.getBlockSize() : 0));
          }
          command=packet.buffer.getCommand();
          recipient=c.getRecipient();
          length-=len;
          c.rwsize-=len;
          sendit=true;
        }
      }
      if(sendit){
        _write(packet);
        if(length==0){
          return;
        }
        packet.unshift(command, recipient, s, length);
      }

      synchronized(c){
        if(in_kex){
          continue;
        }
        if(c.rwsize>=length){
          c.rwsize-=length;
          break;
        }

      }
    }
    _write(packet);
  }

  public void write(Packet packet) throws Exception{
    long t = getTimeout();
    while(in_kex){
      if(t>0L &&
         (System.currentTimeMillis()-kex_start_time)>t &&
         !in_prompt
         ){
        throw new JSchException("timeout in waiting for rekeying process.");
      }
      byte command=packet.buffer.getCommand();
      //System.err.println("command: "+command);
      if(command==SSH_MSG_KEXINIT ||
         command==SSH_MSG_NEWKEYS ||
         command==SSH_MSG_KEXDH_INIT ||
         command==SSH_MSG_KEXDH_REPLY ||
         command==SSH_MSG_KEX_DH_GEX_GROUP ||
         command==SSH_MSG_KEX_DH_GEX_INIT ||
         command==SSH_MSG_KEX_DH_GEX_REPLY ||
         command==SSH_MSG_KEX_DH_GEX_REQUEST ||
         command==SSH_MSG_DISCONNECT){
        break;
      }
      try{Thread.sleep(10);}
      catch(java.lang.InterruptedException e){};
    }
    _write(packet);
  }

  private void _write(Packet packet) throws Exception{
    synchronized(lock){
      encode(packet);
      if(io!=null){
        io.put(packet);
        seqo++;
      }
    }
  }

  Runnable thread;
  public void run(){
    thread=this;

    byte[] foo;
    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);
    int i=0;
    Channel channel;
    int[] start=new int[1];
    int[] length=new int[1];
    KeyExchange kex=null;

    int stimeout=0;
    try{
      while(isConnected &&
            thread!=null){
        try{
          buf=read(buf);
          stimeout=0;
        }
        catch(InterruptedIOException/*SocketTimeoutException*/ ee){
          if(!in_kex && stimeout<serverAliveCountMax){
            sendKeepAliveMsg();
            stimeout++;
            continue;
          }
          else if(in_kex && stimeout<serverAliveCountMax){
            stimeout++;
            continue;
          }
          throw ee;
        }

        int msgType=buf.getCommand()&0xff;

        if(kex!=null && kex.getState()==msgType){
          kex_start_time=System.currentTimeMillis();
          boolean result=kex.next(buf);
          if(!result){
            throw new JSchException("verify: "+result);
          }
          continue;
        }

        switch(msgType){
        case SSH_MSG_KEXINIT:
          kex=receive_kexinit(buf);
          break;

        case SSH_MSG_NEWKEYS:
          send_newkeys();
          receive_newkeys(buf, kex);
          kex=null;
          break;

        case SSH_MSG_CHANNEL_DATA:
          buf.getInt();
          buf.getByte();
          buf.getByte();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          foo=buf.getString(start, length);
          if(channel==null){
            break;
          }

          if(length[0]==0){
            break;
          }

try{
          channel.write(foo, start[0], length[0]);
}
catch(Exception e){
  try{channel.disconnect();}catch(Exception ee){}
break;
}
          int len=length[0];
          channel.setLocalWindowSize(channel.lwsize-len);
          if(channel.lwsize<channel.lwsize_max/2){
            packet.reset();
            buf.putByte((byte)SSH_MSG_CHANNEL_WINDOW_ADJUST);
            buf.putInt(channel.getRecipient());
            buf.putInt(channel.lwsize_max-channel.lwsize);
            synchronized(channel){
              if(!channel.close)
                write(packet);
            }
            channel.setLocalWindowSize(channel.lwsize_max);
          }
          break;

        case SSH_MSG_CHANNEL_EXTENDED_DATA:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          buf.getInt();                   // data_type_code == 1
          foo=buf.getString(start, length);
          if(channel==null){
            break;
          }

          if(length[0]==0){
            break;
          }

          channel.write_ext(foo, start[0], length[0]);

          len=length[0];
          channel.setLocalWindowSize(channel.lwsize-len);
          if(channel.lwsize<channel.lwsize_max/2){
            packet.reset();
            buf.putByte((byte)SSH_MSG_CHANNEL_WINDOW_ADJUST);
            buf.putInt(channel.getRecipient());
            buf.putInt(channel.lwsize_max-channel.lwsize);
            synchronized(channel){
              if(!channel.close)
                write(packet);
            }
            channel.setLocalWindowSize(channel.lwsize_max);
          }
          break;

        case SSH_MSG_CHANNEL_WINDOW_ADJUST:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          if(channel==null){
            break;
          }
          channel.addRemoteWindowSize(buf.getUInt());
          break;

        case SSH_MSG_CHANNEL_EOF:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          if(channel!=null){
            channel.eof_remote();
          }
          break;
        case SSH_MSG_CHANNEL_CLOSE:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          if(channel!=null){
//            channel.close();
            channel.disconnect();
          }
          break;
        case SSH_MSG_CHANNEL_OPEN_CONFIRMATION:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          int r=buf.getInt();
          long rws=buf.getUInt();
          int rps=buf.getInt();
          if(channel!=null){
            channel.setRemoteWindowSize(rws);
            channel.setRemotePacketSize(rps);
            channel.open_confirmation=true;
            channel.setRecipient(r);
          }
          break;
        case SSH_MSG_CHANNEL_OPEN_FAILURE:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          if(channel!=null){
            int reason_code=buf.getInt();
            channel.setExitStatus(reason_code);
            channel.close=true;
            channel.eof_remote=true;
            channel.setRecipient(0);
          }
          break;
        case SSH_MSG_CHANNEL_REQUEST:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          foo=buf.getString();
          boolean reply=(buf.getByte()!=0);
          channel=Channel.getChannel(i, this);
          if(channel!=null){
            byte reply_type=(byte)SSH_MSG_CHANNEL_FAILURE;
            if((Util.byte2str(foo)).equals("exit-status")){
              i=buf.getInt();             // exit-status
              channel.setExitStatus(i);
              reply_type=(byte)SSH_MSG_CHANNEL_SUCCESS;
            }
            if(reply){
              packet.reset();
              buf.putByte(reply_type);
              buf.putInt(channel.getRecipient());
              write(packet);
            }
          }
          else{
          }
          break;
        case SSH_MSG_CHANNEL_OPEN:
          buf.getInt();
          buf.getShort();
          foo=buf.getString();
          String ctyp=Util.byte2str(foo);
          if(!"forwarded-tcpip".equals(ctyp) &&
             !("x11".equals(ctyp) && x11_forwarding) &&
             !("auth-agent@openssh.com".equals(ctyp) && agent_forwarding)){
            packet.reset();
            buf.putByte((byte)SSH_MSG_CHANNEL_OPEN_FAILURE);
            buf.putInt(buf.getInt());
            buf.putInt(Channel.SSH_OPEN_ADMINISTRATIVELY_PROHIBITED);
            buf.putString(Util.empty);
            buf.putString(Util.empty);
            write(packet);
          }
          else{
            channel=Channel.getChannel(ctyp);
            addChannel(channel);
            channel.getData(buf);
            channel.init();

            Thread tmp=new Thread(channel);
            tmp.setName("Channel "+ctyp+" "+host);
            if(daemon_thread){
              tmp.setDaemon(daemon_thread);
            }
            tmp.start();
          }
          break;
        case SSH_MSG_CHANNEL_SUCCESS:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          if(channel==null){
            break;
          }
          channel.reply=1;
          break;
        case SSH_MSG_CHANNEL_FAILURE:
          buf.getInt();
          buf.getShort();
          i=buf.getInt();
          channel=Channel.getChannel(i, this);
          if(channel==null){
            break;
          }
          channel.reply=0;
          break;
        case SSH_MSG_GLOBAL_REQUEST:
          buf.getInt();
          buf.getShort();
          foo=buf.getString();       // request name
          reply=(buf.getByte()!=0);
          if(reply){
            packet.reset();
            buf.putByte((byte)SSH_MSG_REQUEST_FAILURE);
            write(packet);
          }
          break;
        case SSH_MSG_REQUEST_FAILURE:
        case SSH_MSG_REQUEST_SUCCESS:
          Thread t=grr.getThread();
          if(t!=null){
            grr.setReply(msgType==SSH_MSG_REQUEST_SUCCESS? 1 : 0);
            if(msgType==SSH_MSG_REQUEST_SUCCESS && grr.getPort()==0){
              buf.getInt();
              buf.getShort();
              grr.setPort(buf.getInt());
            }
            t.interrupt();
          }
          break;
        default:
          throw new IOException("Unknown SSH message type "+msgType);
        }
      }
    }
    catch(Exception e){
      in_kex=false;
      if(JSch.getLogger().isEnabled(Logger.INFO)){
        JSch.getLogger().log(Logger.INFO,
                             "Caught an exception, leaving main loop due to " + e.getMessage());
      }
    }
    try{
      disconnect();
    }
    catch(NullPointerException e){
    }
    catch(Exception e){
    }
    isConnected=false;
  }

  public void disconnect(){
    if(!isConnected) return;
    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "Disconnecting from "+host+" port "+port);
    }

    Channel.disconnect(this);

    isConnected=false;

    PortWatcher.delPort(this);
    ChannelForwardedTCPIP.delPort(this);
    ChannelX11.removeFakedCookie(this);

    synchronized(lock){
      if(connectThread!=null){
        Thread.yield();
        connectThread.interrupt();
        connectThread=null;
      }
    }
    thread=null;
    try{
      if(io!=null){
        if(io.in!=null) io.in.close();
        if(io.out!=null) io.out.close();
        if(io.out_ext!=null) io.out_ext.close();
      }
      if(proxy==null){
        if(socket!=null)
          socket.close();
      }
      else{
        synchronized(proxy){
          proxy.close();
        }
        proxy=null;
      }
    }
    catch(Exception e){
    }
    io=null;
    socket=null;
    jsch.removeSession(this);
  }

  /**
   * Registers the local port forwarding for loop-back interface.
   * If <code>lport</code> is <code>0</code>, the tcp port will be allocated.
   * @param lport local port for local port forwarding
   * @param host host address for local port forwarding
   * @param rport remote port number for local port forwarding
   * @return an allocated local TCP port number
   * @see #setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf, int connectTimeout)
   */
  public int setPortForwardingL(int lport, String host, int rport) throws JSchException{
    return setPortForwardingL("127.0.0.1", lport, host, rport);
  }

  /**
   * Registers the local port forwarding.  If <code>bind_address</code> is an empty string
   * or '*', the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or
   * <code>null</code>, the listening port will be bound for local use only.
   * If <code>lport</code> is <code>0</code>, the tcp port will be allocated.
   * @param bind_address bind address for local port forwarding
   * @param lport local port for local port forwarding
   * @param host host address for local port forwarding
   * @param rport remote port number for local port forwarding
   * @return an allocated local TCP port number
   * @see #setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf, int connectTimeout)
   */
  public int setPortForwardingL(String bind_address, int lport, String host, int rport) throws JSchException{
    return setPortForwardingL(bind_address, lport, host, rport, null);
  }

  /**
   * Registers the local port forwarding.
   * If <code>bind_address</code> is an empty string or <code>"*"</code>,
   * the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or
   * <code>null</code>, the listening port will be bound for local use only.
   * If <code>lport</code> is <code>0</code>, the tcp port will be allocated.
   * @param bind_address bind address for local port forwarding
   * @param lport local port for local port forwarding
   * @param host host address for local port forwarding
   * @param rport remote port number for local port forwarding
   * @param ssf socket factory
   * @return an allocated local TCP port number
   * @see #setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf, int connectTimeout)
   */
  public int setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf) throws JSchException{
    return setPortForwardingL(bind_address, lport, host, rport, ssf, 0);
  }

  /**
   * Registers the local port forwarding.
   * If <code>bind_address</code> is an empty string
   * or <code>"*"</code>, the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or
   * <code>null</code>, the listening port will be bound for local use only.
   * If <code>lport</code> is <code>0</code>, the tcp port will be allocated.
   * @param bind_address bind address for local port forwarding
   * @param lport local port for local port forwarding
   * @param host host address for local port forwarding
   * @param rport remote port number for local port forwarding
   * @param ssf socket factory
   * @param connectTimeout timeout for establishing port connection
   * @return an allocated local TCP port number
   */
  public int setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf, int connectTimeout) throws JSchException{
    PortWatcher pw=PortWatcher.addPort(this, bind_address, lport, host, rport, ssf);
    pw.setConnectTimeout(connectTimeout);
    Thread tmp=new Thread(pw);
    tmp.setName("PortWatcher Thread for "+host);
    if(daemon_thread){
      tmp.setDaemon(daemon_thread);
    }
    tmp.start();
    return pw.lport;
  }

  /**
   * Cancels the local port forwarding assigned
   * at local TCP port <code>lport</code> on loopback interface.
   *
   * @param lport local TCP port
   */
  public void delPortForwardingL(int lport) throws JSchException{
    delPortForwardingL("127.0.0.1", lport);
  }

  /**
   * Cancels the local port forwarding assigned
   * at local TCP port <code>lport</code> on <code>bind_address</code> interface.
   *
   * @param bind_address bind_address of network interfaces
   * @param lport local TCP port
   */
  public void delPortForwardingL(String bind_address, int lport) throws JSchException{
    PortWatcher.delPort(this, bind_address, lport);
  }

  /**
   * Lists the registered local port forwarding.
   *
   * @return a list of "lport:host:hostport"
   */
  public String[] getPortForwardingL() throws JSchException{
    return PortWatcher.getPortForwarding(this);
  }

  /**
   * Registers the remote port forwarding for the loopback interface
   * of the remote.
   *
   * @param rport remote port
   * @param host host address
   * @param lport local port
   * @see #setPortForwardingR(String bind_address, int rport, String host, int lport, SocketFactory sf)
   */
  public void setPortForwardingR(int rport, String host, int lport) throws JSchException{
    setPortForwardingR(null, rport, host, lport, (SocketFactory)null);
  }

  /**
   * Registers the remote port forwarding.
   * If <code>bind_address</code> is an empty string or <code>"*"</code>,
   * the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or is not given,
   * the listening port will be bound for local use only.
   * Note that if <code>GatewayPorts</code> is <code>"no"</code> on the
   * remote, <code>"localhost"</code> is always used as a bind_address.
   *
   * @param bind_address bind address
   * @param rport remote port
   * @param host host address
   * @param lport local port
   * @see #setPortForwardingR(String bind_address, int rport, String host, int lport, SocketFactory sf)
   */
  public void setPortForwardingR(String bind_address, int rport, String host, int lport) throws JSchException{
    setPortForwardingR(bind_address, rport, host, lport, (SocketFactory)null);
  }

  /**
   * Registers the remote port forwarding for the loopback interface
   * of the remote.
   *
   * @param rport remote port
   * @param host host address
   * @param lport local port
   * @param sf socket factory
   * @see #setPortForwardingR(String bind_address, int rport, String host, int lport, SocketFactory sf)
   */
  public void setPortForwardingR(int rport, String host, int lport, SocketFactory sf) throws JSchException{
    setPortForwardingR(null, rport, host, lport, sf);
  }

  // TODO: This method should return the integer value as the assigned port.
  /**
   * Registers the remote port forwarding.
   * If <code>bind_address</code> is an empty string or <code>"*"</code>,
   * the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or is not given,
   * the listening port will be bound for local use only.
   * Note that if <code>GatewayPorts</code> is <code>"no"</code> on the
   * remote, <code>"localhost"</code> is always used as a bind_address.
   * If <code>rport</code> is <code>0</code>, the TCP port will be allocated on the remote.
   *
   * @param bind_address bind address
   * @param rport remote port
   * @param host host address
   * @param lport local port
   * @param sf socket factory
   */
  public void setPortForwardingR(String bind_address, int rport, String host, int lport, SocketFactory sf) throws JSchException{
    int allocated=_setPortForwardingR(bind_address, rport);
    ChannelForwardedTCPIP.addPort(this, bind_address,
                                  rport, allocated, host, lport, sf);
  }

  /**
   * Registers the remote port forwarding for the loopback interface
   * of the remote.
   * The TCP connection to <code>rport</code> on the remote will be
   * forwarded to an instance of the class <code>daemon</code>.
   * The class specified by <code>daemon</code> must implement
   * <code>ForwardedTCPIPDaemon</code>.
   *
   * @param rport remote port
   * @param daemon class name, which implements "ForwardedTCPIPDaemon"
   * @see #setPortForwardingR(String bind_address, int rport, String daemon, Object[] arg)
   */
  public void setPortForwardingR(int rport, String daemon) throws JSchException{
    setPortForwardingR(null, rport, daemon, null);
  }

  /**
   * Registers the remote port forwarding for the loopback interface
   * of the remote.
   * The TCP connection to <code>rport</code> on the remote will be
   * forwarded to an instance of the class <code>daemon</code> with
   * the argument <code>arg</code>.
   * The class specified by <code>daemon</code> must implement <code>ForwardedTCPIPDaemon</code>.
   *
   * @param rport remote port
   * @param daemon class name, which implements "ForwardedTCPIPDaemon"
   * @param arg arguments for "daemon"
   * @see #setPortForwardingR(String bind_address, int rport, String daemon, Object[] arg)
   */
  public void setPortForwardingR(int rport, String daemon, Object[] arg) throws JSchException{
    setPortForwardingR(null, rport, daemon, arg);
  }

  /**
   * Registers the remote port forwarding.
   * If <code>bind_address</code> is an empty string
   * or <code>"*"</code>, the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or is not given,
   * the listening port will be bound for local use only.
   * Note that if <code>GatewayPorts</code> is <code>"no"</code> on the
   * remote, <code>"localhost"</code> is always used as a bind_address.
   * The TCP connection to <code>rport</code> on the remote will be
   * forwarded to an instance of the class <code>daemon</code> with the
   * argument <code>arg</code>.
   * The class specified by <code>daemon</code> must implement <code>ForwardedTCPIPDaemon</code>.
   *
   * @param bind_address bind address
   * @param rport remote port
   * @param daemon class name, which implements "ForwardedTCPIPDaemon"
   * @param arg arguments for "daemon"
   * @see #setPortForwardingR(String bind_address, int rport, String daemon, Object[] arg)
   */
  public void setPortForwardingR(String bind_address, int rport, String daemon, Object[] arg) throws JSchException{
    int allocated = _setPortForwardingR(bind_address, rport);
    ChannelForwardedTCPIP.addPort(this, bind_address,
                                  rport, allocated, daemon, arg);
  }

  /**
   * Lists the registered remote port forwarding.
   *
   * @return a list of "rport:host:hostport"
   */
  public String[] getPortForwardingR() throws JSchException{
    return ChannelForwardedTCPIP.getPortForwarding(this);
  }

  private class Forwarding {
    String bind_address = null;
    int port = -1;
    String host = null;
    int hostport = -1;
  }

  /**
   * The given argument may be "[bind_address:]port:host:hostport" or
   * "[bind_address:]port host:hostport", which is from LocalForward command of
   * ~/.ssh/config .
   */
  private Forwarding parseForwarding(String conf) throws JSchException {
    String[] tmp = conf.split(" ");
    if(tmp.length>1){   // "[bind_address:]port host:hostport"
      Vector foo = new Vector();
      for(int i=0; i<tmp.length; i++){
        if(tmp[i].length()==0) continue;
        foo.addElement(tmp[i].trim());
      }
      StringBuffer sb = new StringBuffer(); // join
      for(int i=0; i<foo.size(); i++){
        sb.append((String)(foo.elementAt(i)));
        if(i+1<foo.size())
          sb.append(":");
      }
      conf = sb.toString();
    }

    String org = conf;
    Forwarding f = new Forwarding();
    try {
      if(conf.lastIndexOf(":") == -1)
        throw new JSchException ("parseForwarding: "+org);
      f.hostport = Integer.parseInt(conf.substring(conf.lastIndexOf(":")+1));
      conf = conf.substring(0, conf.lastIndexOf(":"));
      if(conf.lastIndexOf(":") == -1)
        throw new JSchException ("parseForwarding: "+org);
      f.host = conf.substring(conf.lastIndexOf(":")+1);
      conf = conf.substring(0, conf.lastIndexOf(":"));
      if(conf.lastIndexOf(":") != -1){
        f.port = Integer.parseInt(conf.substring(conf.lastIndexOf(":")+1));
        conf = conf.substring(0, conf.lastIndexOf(":"));
        if(conf.length() ==0 || conf.equals("*")) conf="0.0.0.0";
        if(conf.equals("localhost")) conf="127.0.0.1";
        f.bind_address = conf;
      }
      else {
        f.port = Integer.parseInt(conf);
        f.bind_address = "127.0.0.1";
      }
    }
    catch(NumberFormatException e){
      throw new JSchException ("parseForwarding: "+e.toString());
    }
    return f;
  }

  /**
   * Registers the local port forwarding.  The argument should be
   * in the format like "[bind_address:]port:host:hostport".
   * If <code>bind_address</code> is an empty string or <code>"*"</code>,
   * the port should be available from all interfaces.
   * If <code>bind_address</code> is <code>"localhost"</code> or is not given,
   * the listening port will be bound for local use only.
   *
   * @param conf configuration of local port forwarding
   * @return an assigned port number
   * @see #setPortForwardingL(String bind_address, int lport, String host, int rport)
   */
  public int setPortForwardingL(String conf) throws JSchException {
    Forwarding f = parseForwarding(conf);
    return setPortForwardingL(f.bind_address, f.port, f.host, f.hostport);
  }

  /**
   * Registers the remote port forwarding.  The argument should be
   * in the format like "[bind_address:]port:host:hostport".  If the
   * bind_address is not given, the default is to only bind to loopback
   * addresses.  If the bind_address is <code>"*"</code> or an empty string,
   * then the forwarding is requested to listen on all interfaces.
   * Note that if <code>GatewayPorts</code> is <code>"no"</code> on the remote,
   * <code>"localhost"</code> is always used for bind_address.
   * If the specified remote is <code>"0"</code>,
   * the TCP port will be allocated on the remote.
   *
   * @param conf configuration of remote port forwarding
   * @return an allocated TCP port on the remote.
   * @see #setPortForwardingR(String bind_address, int rport, String host, int rport)
   */
  public int setPortForwardingR(String conf) throws JSchException {
    Forwarding f = parseForwarding(conf);
    int allocated = _setPortForwardingR(f.bind_address, f.port);
    ChannelForwardedTCPIP.addPort(this, f.bind_address,
                                  f.port, allocated, f.host, f.hostport, null);
    return allocated;
  }

  /**
   * Instantiates an instance of stream-forwarder to <code>host</code>:<code>port</code>.
   * Set I/O stream to the given channel, and then invoke Channel#connect() method.
   *
   * @param host remote host, which the given stream will be plugged to.
   * @param port remote port, which the given stream will be plugged to.
   */
  public Channel getStreamForwarder(String host, int port) throws JSchException {
    ChannelDirectTCPIP channel = new ChannelDirectTCPIP();
    channel.init();
    this.addChannel(channel);
    channel.setHost(host);
    channel.setPort(port);
    return channel;
  }

  private class GlobalRequestReply{
    private Thread thread=null;
    private int reply=-1;
    private int port=0;
    void setThread(Thread thread){
      this.thread=thread;
      this.reply=-1;
    }
    Thread getThread(){ return thread; }
    void setReply(int reply){ this.reply=reply; }
    int getReply(){ return this.reply; }
    int getPort(){ return this.port; }
    void setPort(int port){ this.port=port; }
  }
  private GlobalRequestReply grr=new GlobalRequestReply();
  private int _setPortForwardingR(String bind_address, int rport) throws JSchException{
    synchronized(grr){
    Buffer buf=new Buffer(100); // ??
    Packet packet=new Packet(buf);

    String address_to_bind=ChannelForwardedTCPIP.normalize(bind_address);

    grr.setThread(Thread.currentThread());
    grr.setPort(rport);

    try{
      packet.reset();
      buf.putByte((byte) SSH_MSG_GLOBAL_REQUEST);
      buf.putString(Util.str2byte("tcpip-forward"));
      buf.putByte((byte)1);
      buf.putString(Util.str2byte(address_to_bind));
      buf.putInt(rport);
      write(packet);
    }
    catch(Exception e){
      grr.setThread(null);
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }

    int count = 0;
    int reply = grr.getReply();
    while(count < 10 && reply == -1){
      try{ Thread.sleep(1000); }
      catch(Exception e){
      }
      count++;
      reply = grr.getReply();
    }
    grr.setThread(null);
    if(reply != 1){
      throw new JSchException("remote port forwarding failed for listen port "+rport);
    }
    rport=grr.getPort();
    }
    return rport;
  }

  /**
   * Cancels the remote port forwarding assigned at remote TCP port <code>rport</code>.
   *
   * @param rport remote TCP port
   */
  public void delPortForwardingR(int rport) throws JSchException{
    this.delPortForwardingR(null, rport);
  }

  /**
   * Cancels the remote port forwarding assigned at
   * remote TCP port <code>rport</code> bound on the interface at
   * <code>bind_address</code>.
   *
   * @param bind_address bind address of the interface on the remote
   * @param rport remote TCP port
   */
  public void delPortForwardingR(String bind_address, int rport) throws JSchException{
    ChannelForwardedTCPIP.delPort(this, bind_address, rport);
  }

  private void initDeflater(String method) throws JSchException{
    if(method.equals("none")){
      deflater=null;
      return;
    }
    String foo=getConfig(method);
    if(foo!=null){
      if(method.equals("zlib") ||
         (isAuthed && method.equals("zlib@openssh.com"))){
        try{
          Class c=Class.forName(foo);
          deflater=(Compression)(c.newInstance());
          int level=6;
          try{ level=Integer.parseInt(getConfig("compression_level"));}
          catch(Exception ee){ }
          deflater.init(Compression.DEFLATER, level);
        }
        catch(NoClassDefFoundError ee){
          throw new JSchException(ee.toString(), ee);
        }
        catch(Exception ee){
          throw new JSchException(ee.toString(), ee);
        }
      }
    }
  }
  private void initInflater(String method) throws JSchException{
    if(method.equals("none")){
      inflater=null;
      return;
    }
    String foo=getConfig(method);
    if(foo!=null){
      if(method.equals("zlib") ||
         (isAuthed && method.equals("zlib@openssh.com"))){
        try{
          Class c=Class.forName(foo);
          inflater=(Compression)(c.newInstance());
          inflater.init(Compression.INFLATER, 0);
        }
        catch(Exception ee){
          throw new JSchException(ee.toString(), ee);
        }
      }
    }
  }

  void addChannel(Channel channel){
    channel.setSession(this);
  }

  public void setProxy(Proxy proxy){ this.proxy=proxy; }
  public void setHost(String host){ this.host=host; }
  public void setPort(int port){ this.port=port; }
  void setUserName(String username){ this.username=username; }
  public void setUserInfo(UserInfo userinfo){ this.userinfo=userinfo; }
  public UserInfo getUserInfo(){ return userinfo; }
  public void setInputStream(InputStream in){ this.in=in; }
  public void setOutputStream(OutputStream out){ this.out=out; }
  public void setX11Host(String host){ ChannelX11.setHost(host); }
  public void setX11Port(int port){ ChannelX11.setPort(port); }
  public void setX11Cookie(String cookie){ ChannelX11.setCookie(cookie); }
  public void setPassword(String password){
    if(password!=null)
      this.password=Util.str2byte(password);
  }
  public void setPassword(byte[] password){
    if(password!=null){
      this.password=new byte[password.length];
      System.arraycopy(password, 0, this.password, 0, password.length);
    }
  }

  public void setConfig(java.util.Properties newconf){
    setConfig((java.util.Hashtable)newconf);
  }

  public void setConfig(java.util.Hashtable newconf){
    synchronized(lock){
      if(config==null)
        config=new java.util.Hashtable();
      for(java.util.Enumeration e=newconf.keys() ; e.hasMoreElements() ;) {
        String key=(String)(e.nextElement());
        config.put(key, (String)(newconf.get(key)));
      }
    }
  }

  public void setConfig(String key, String value){
    synchronized(lock){
      if(config==null){
        config=new java.util.Hashtable();
      }
      config.put(key, value);
    }
  }

  public String getConfig(String key){
    Object foo=null;
    if(config!=null){
      foo=config.get(key);
      if(foo instanceof String) return (String)foo;
    }
    foo=jsch.getConfig(key);
    if(foo instanceof String) return (String)foo;
    return null;
  }

  public void setSocketFactory(SocketFactory sfactory){
    socket_factory=sfactory;
  }
  public boolean isConnected(){ return isConnected; }
  public int getTimeout(){ return timeout; }
  public void setTimeout(int timeout) throws JSchException {
    if(socket==null){
      if(timeout<0){
        throw new JSchException("invalid timeout value");
      }
      this.timeout=timeout;
      return;
    }
    try{
      socket.setSoTimeout(timeout);
      this.timeout=timeout;
    }
    catch(Exception e){
      if(e instanceof Throwable)
        throw new JSchException(e.toString(), (Throwable)e);
      throw new JSchException(e.toString());
    }
  }
  public String getServerVersion(){
    return Util.byte2str(V_S);
  }
  public String getClientVersion(){
    return Util.byte2str(V_C);
  }
  public void setClientVersion(String cv){
    V_C=Util.str2byte(cv);
  }

  public void sendIgnore() throws Exception{
    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);
    packet.reset();
    buf.putByte((byte)SSH_MSG_IGNORE);
    write(packet);
  }

  private static final byte[] keepalivemsg=Util.str2byte("keepalive@jcraft.com");
  public void sendKeepAliveMsg() throws Exception{
    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);
    packet.reset();
    buf.putByte((byte)SSH_MSG_GLOBAL_REQUEST);
    buf.putString(keepalivemsg);
    buf.putByte((byte)1);
    write(packet);
  }

  private static final byte[] nomoresessions=Util.str2byte("no-more-sessions@openssh.com");
  public void noMoreSessionChannels() throws Exception{
    Buffer buf=new Buffer();
    Packet packet=new Packet(buf);
    packet.reset();
    buf.putByte((byte)SSH_MSG_GLOBAL_REQUEST);
    buf.putString(nomoresessions);
    buf.putByte((byte)0);
    write(packet);
  }

  private HostKey hostkey=null;
  public HostKey getHostKey(){ return hostkey; }
  public String getHost(){return host;}
  public String getUserName(){return username;}
  public int getPort(){return port;}
  public void setHostKeyAlias(String hostKeyAlias){
    this.hostKeyAlias=hostKeyAlias;
  }
  public String getHostKeyAlias(){
    return hostKeyAlias;
  }

  /**
   * Sets the interval to send a keep-alive message.  If zero is
   * specified, any keep-alive message must not be sent.  The default interval
   * is zero.
   *
   * @param interval the specified interval, in milliseconds.
   * @see #getServerAliveInterval()
   */
  public void setServerAliveInterval(int interval) throws JSchException {
    setTimeout(interval);
    this.serverAliveInterval=interval;
  }

  /**
   * Returns setting for the interval to send a keep-alive message.
   *
   * @see #setServerAliveInterval(int)
   */
  public int getServerAliveInterval(){
    return this.serverAliveInterval;
  }

  /**
   * Sets the number of keep-alive messages which may be sent without
   * receiving any messages back from the server.  If this threshold is
   * reached while keep-alive messages are being sent, the connection will
   * be disconnected.  The default value is one.
   *
   * @param count the specified count
   * @see #getServerAliveCountMax()
   */
  public void setServerAliveCountMax(int count){
    this.serverAliveCountMax=count;
  }

  /**
   * Returns setting for the threshold to send keep-alive messages.
   *
   * @see #setServerAliveCountMax(int)
   */
  public int getServerAliveCountMax(){
    return this.serverAliveCountMax;
  }

  public void setDaemonThread(boolean enable){
    this.daemon_thread=enable;
  }

  private String[] checkCiphers(String ciphers){
    if(ciphers==null || ciphers.length()==0)
      return null;

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "CheckCiphers: "+ciphers);
    }

    String cipherc2s=getConfig("cipher.c2s");
    String ciphers2c=getConfig("cipher.s2c");

    Vector result=new Vector();
    String[] _ciphers=Util.split(ciphers, ",");
    for(int i=0; i<_ciphers.length; i++){
      String cipher=_ciphers[i];
      if(ciphers2c.indexOf(cipher) == -1 && cipherc2s.indexOf(cipher) == -1)
        continue;
      if(!checkCipher(getConfig(cipher))){
        result.addElement(cipher);
      }
    }
    if(result.size()==0)
      return null;
    String[] foo=new String[result.size()];
    System.arraycopy(result.toArray(), 0, foo, 0, result.size());

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      for(int i=0; i<foo.length; i++){
        JSch.getLogger().log(Logger.INFO,
                             foo[i]+" is not available.");
      }
    }

    return foo;
  }

  static boolean checkCipher(String cipher){
    try{
      Class c=Class.forName(cipher);
      Cipher _c=(Cipher)(c.newInstance());
      _c.init(Cipher.ENCRYPT_MODE,
              new byte[_c.getBlockSize()],
              new byte[_c.getIVSize()]);
      return true;
    }
    catch(Exception e){
      return false;
    }
  }

  private String[] checkKexes(String kexes){
    if(kexes==null || kexes.length()==0)
      return null;

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "CheckKexes: "+kexes);
    }

    java.util.Vector result=new java.util.Vector();
    String[] _kexes=Util.split(kexes, ",");
    for(int i=0; i<_kexes.length; i++){
      if(!checkKex(this, getConfig(_kexes[i]))){
        result.addElement(_kexes[i]);
      }
    }
    if(result.size()==0)
      return null;
    String[] foo=new String[result.size()];
    System.arraycopy(result.toArray(), 0, foo, 0, result.size());

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      for(int i=0; i<foo.length; i++){
        JSch.getLogger().log(Logger.INFO,
                             foo[i]+" is not available.");
      }
    }

    return foo;
  }

  static boolean checkKex(Session s, String kex){
    try{
      Class c=Class.forName(kex);
      KeyExchange _c=(KeyExchange)(c.newInstance());
      _c.init(s ,null, null, null, null);
      return true;
    }
    catch(Exception e){ return false; }
  }

  private String[] checkSignatures(String sigs){
    if(sigs==null || sigs.length()==0)
      return null;

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "CheckSignatures: "+sigs);
    }

    java.util.Vector result=new java.util.Vector();
    String[] _sigs=Util.split(sigs, ",");
    for(int i=0; i<_sigs.length; i++){
      try{
        Class c=Class.forName((String)jsch.getConfig(_sigs[i]));
        final Signature sig=(Signature)(c.newInstance());
        sig.init();
      }
      catch(Exception e){
        result.addElement(_sigs[i]);
      }
   }
   if(result.size()==0)
      return null;
   String[] foo=new String[result.size()];
    System.arraycopy(result.toArray(), 0, foo, 0, result.size());
    if(JSch.getLogger().isEnabled(Logger.INFO)){
      for(int i=0; i<foo.length; i++){
        JSch.getLogger().log(Logger.INFO,
                             foo[i]+" is not available.");
      }
    }
    return foo;
  }

  /**
   * Sets the identityRepository, which will be referred
   * in the public key authentication.  The default value is <code>null</code>.
   *
   * @param identityRepository
   * @see #getIdentityRepository()
   */
  public void setIdentityRepository(IdentityRepository identityRepository){
    this.identityRepository = identityRepository;
  }

  /**
   * Gets the identityRepository.
   * If this.identityRepository is <code>null</code>,
   * JSch#getIdentityRepository() will be invoked.
   *
   * @see JSch#getIdentityRepository()
   */
  IdentityRepository getIdentityRepository(){
    if(identityRepository == null)
      return jsch.getIdentityRepository();
    return identityRepository;
  }

  /**
   * Sets the hostkeyRepository, which will be referred in checking host keys.
   *
   * @param hostkeyRepository
   * @see #getHostKeyRepository()
   */
  public void setHostKeyRepository(HostKeyRepository hostkeyRepository){
    this.hostkeyRepository = hostkeyRepository;
  }

  /**
   * Gets the hostkeyRepository.
   * If this.hostkeyRepository is <code>null</code>,
   * JSch#getHostKeyRepository() will be invoked.
   *
   * @see JSch#getHostKeyRepository()
   */
  public HostKeyRepository getHostKeyRepository(){
    if(hostkeyRepository == null)
      return jsch.getHostKeyRepository();
    return hostkeyRepository;
  }

  private void applyConfig() throws JSchException {
    ConfigRepository configRepository = jsch.getConfigRepository();
    if(configRepository == null){
      return;
    }

    ConfigRepository.Config config =
      configRepository.getConfig(org_host);

    String value = null;

    value = config.getUser();
    if(value != null)
      username = value;

    value = config.getHostname();
    if(value != null)
      host = value;

    int port = config.getPort();
    if(port != -1)
      this.port = port;

    checkConfig(config, "kex");
    checkConfig(config, "server_host_key");

    checkConfig(config, "cipher.c2s");
    checkConfig(config, "cipher.s2c");
    checkConfig(config, "mac.c2s");
    checkConfig(config, "mac.s2c");
    checkConfig(config, "compression.c2s");
    checkConfig(config, "compression.s2c");
    checkConfig(config, "compression_level");

    checkConfig(config, "StrictHostKeyChecking");
    checkConfig(config, "HashKnownHosts");
    checkConfig(config, "PreferredAuthentications");
    checkConfig(config, "MaxAuthTries");
    checkConfig(config, "ClearAllForwardings");

    value = config.getValue("HostKeyAlias");
    if(value != null)
      this.setHostKeyAlias(value);

    value = config.getValue("UserKnownHostsFile");
    if(value != null) {
      KnownHosts kh = new KnownHosts(jsch);
      kh.setKnownHosts(value);
      this.setHostKeyRepository(kh);
    }

    String[] values = config.getValues("IdentityFile");
    if(values != null) {
      String[] global =
        configRepository.getConfig("").getValues("IdentityFile");
      if(global != null){
        for(int i = 0; i < global.length; i++){
          jsch.addIdentity(global[i]);
        }
      }
      else {
        global = new String[0];
      }
      if(values.length - global.length > 0){
        IdentityRepository.Wrapper ir =
          new IdentityRepository.Wrapper(jsch.getIdentityRepository(), true);
        for(int i = 0; i < values.length; i++){
          String ifile = values[i];
          for(int j = 0; j < global.length; j++){
            if(!ifile.equals(global[j]))
              continue;
            ifile = null;
            break;
          }
          if(ifile == null)
            continue;
          Identity identity =
            IdentityFile.newInstance(ifile, null, jsch);
          ir.add(identity);
        }
        this.setIdentityRepository(ir);
      }
    }

    value = config.getValue("ServerAliveInterval");
    if(value != null) {
      try {
        this.setServerAliveInterval(Integer.parseInt(value));
      }
      catch(NumberFormatException e){
      }
    }

    value = config.getValue("ConnectTimeout");
    if(value != null) {
      try {
        setTimeout(Integer.parseInt(value));
      }
      catch(NumberFormatException e){
      }
    }

    value = config.getValue("MaxAuthTries");
    if(value != null) {
      setConfig("MaxAuthTries", value);
    }

    value = config.getValue("ClearAllForwardings");
    if(value != null) {
      setConfig("ClearAllForwardings", value);
    }

  }

  private void applyConfigChannel(ChannelSession channel) throws JSchException {
    ConfigRepository configRepository = jsch.getConfigRepository();
    if(configRepository == null){
      return;
    }

    ConfigRepository.Config config =
      configRepository.getConfig(org_host);

    String value = null;

    value = config.getValue("ForwardAgent");
    if(value != null){
      channel.setAgentForwarding(value.equals("yes"));
    }

    value = config.getValue("RequestTTY");
    if(value != null){
      channel.setPty(value.equals("yes"));
    }
  }

  private void requestPortForwarding() throws JSchException {

    if(getConfig("ClearAllForwardings").equals("yes"))
      return;

    ConfigRepository configRepository = jsch.getConfigRepository();
    if(configRepository == null){
      return;
    }

    ConfigRepository.Config config =
      configRepository.getConfig(org_host);

    String[] values = config.getValues("LocalForward");
    if(values != null){
      for(int i = 0; i < values.length; i++) {
        setPortForwardingL(values[i]);
      }
    }

    values = config.getValues("RemoteForward");
    if(values != null){
      for(int i = 0; i < values.length; i++) {
        setPortForwardingR(values[i]);
      }
    }
  }

  private void checkConfig(ConfigRepository.Config config, String key){
    String value = config.getValue(key);
    if(value != null)
      this.setConfig(key, value);
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




/*
  uint32   flags
  uint64   size           present only if flag SSH_FILEXFER_ATTR_SIZE
  uint32   uid            present only if flag SSH_FILEXFER_ATTR_UIDGID
  uint32   gid            present only if flag SSH_FILEXFER_ATTR_UIDGID
  uint32   permissions    present only if flag SSH_FILEXFER_ATTR_PERMISSIONS
  uint32   atime          present only if flag SSH_FILEXFER_ACMODTIME
  uint32   mtime          present only if flag SSH_FILEXFER_ACMODTIME
  uint32   extended_count present only if flag SSH_FILEXFER_ATTR_EXTENDED
  string   extended_type
  string   extended_data
    ...      more extended data (extended_type - extended_data pairs),
             so that number of pairs equals extended_count
*/
class SftpATTRS {

  static final int S_ISUID = 04000; // set user ID on execution
  static final int S_ISGID = 02000; // set group ID on execution
  static final int S_ISVTX = 01000; // sticky bit   ****** NOT DOCUMENTED *****

  static final int S_IRUSR = 00400; // read by owner
  static final int S_IWUSR = 00200; // write by owner
  static final int S_IXUSR = 00100; // execute/search by owner
  static final int S_IREAD = 00400; // read by owner
  static final int S_IWRITE= 00200; // write by owner
  static final int S_IEXEC = 00100; // execute/search by owner

  static final int S_IRGRP = 00040; // read by group
  static final int S_IWGRP = 00020; // write by group
  static final int S_IXGRP = 00010; // execute/search by group

  static final int S_IROTH = 00004; // read by others
  static final int S_IWOTH = 00002; // write by others
  static final int S_IXOTH = 00001; // execute/search by others

  private static final int pmask = 0xFFF;

  public String getPermissionsString() {
    StringBuffer buf = new StringBuffer(10);

    if(isDir()) buf.append('d');
    else if(isLink()) buf.append('l');
    else buf.append('-');

    if((permissions & S_IRUSR)!=0) buf.append('r');
    else buf.append('-');

    if((permissions & S_IWUSR)!=0) buf.append('w');
    else buf.append('-');

    if((permissions & S_ISUID)!=0) buf.append('s');
    else if ((permissions & S_IXUSR)!=0) buf.append('x');
    else buf.append('-');

    if((permissions & S_IRGRP)!=0) buf.append('r');
    else buf.append('-');

    if((permissions & S_IWGRP)!=0) buf.append('w');
    else buf.append('-');

    if((permissions & S_ISGID)!=0) buf.append('s');
    else if((permissions & S_IXGRP)!=0) buf.append('x');
    else buf.append('-');

    if((permissions & S_IROTH) != 0) buf.append('r');
    else buf.append('-');

    if((permissions & S_IWOTH) != 0) buf.append('w');
    else buf.append('-');

    if((permissions & S_IXOTH) != 0) buf.append('x');
    else buf.append('-');
    return (buf.toString());
  }

  public String  getAtimeString(){
    Date date= new Date(((long)atime)*1000L);
    return (date.toString());
  }

  public String  getMtimeString(){
    Date date= new Date(((long)mtime)*1000L);
    return (date.toString());
  }

  public static final int SSH_FILEXFER_ATTR_SIZE=         0x00000001;
  public static final int SSH_FILEXFER_ATTR_UIDGID=       0x00000002;
  public static final int SSH_FILEXFER_ATTR_PERMISSIONS=  0x00000004;
  public static final int SSH_FILEXFER_ATTR_ACMODTIME=    0x00000008;
  public static final int SSH_FILEXFER_ATTR_EXTENDED=     0x80000000;

  static final int S_IFMT=0xf000;
  static final int S_IFIFO=0x1000;
  static final int S_IFCHR=0x2000;
  static final int S_IFDIR=0x4000;
  static final int S_IFBLK=0x6000;
  static final int S_IFREG=0x8000;
  static final int S_IFLNK=0xa000;
  static final int S_IFSOCK=0xc000;

  int flags=0;
  long size;
  int uid;
  int gid;
  int permissions;
  int atime;
  int mtime;
  String[] extended=null;

  private SftpATTRS(){
  }

  static SftpATTRS getATTR(Buffer buf){
    SftpATTRS attr=new SftpATTRS();
    attr.flags=buf.getInt();
    if((attr.flags&SSH_FILEXFER_ATTR_SIZE)!=0){ attr.size=buf.getLong(); }
    if((attr.flags&SSH_FILEXFER_ATTR_UIDGID)!=0){
      attr.uid=buf.getInt(); attr.gid=buf.getInt();
    }
    if((attr.flags&SSH_FILEXFER_ATTR_PERMISSIONS)!=0){
      attr.permissions=buf.getInt();
    }
    if((attr.flags&SSH_FILEXFER_ATTR_ACMODTIME)!=0){
      attr.atime=buf.getInt();
    }
    if((attr.flags&SSH_FILEXFER_ATTR_ACMODTIME)!=0){
      attr.mtime=buf.getInt();
    }
    if((attr.flags&SSH_FILEXFER_ATTR_EXTENDED)!=0){
      int count=buf.getInt();
      if(count>0){
        attr.extended=new String[count*2];
        for(int i=0; i<count; i++){
          attr.extended[i*2]=Util.byte2str(buf.getString());
          attr.extended[i*2+1]=Util.byte2str(buf.getString());
        }
      }
    }
    return attr;
  }

  int length(){
    int len=4;

    if((flags&SSH_FILEXFER_ATTR_SIZE)!=0){ len+=8; }
    if((flags&SSH_FILEXFER_ATTR_UIDGID)!=0){ len+=8; }
    if((flags&SSH_FILEXFER_ATTR_PERMISSIONS)!=0){ len+=4; }
    if((flags&SSH_FILEXFER_ATTR_ACMODTIME)!=0){ len+=8; }
    if((flags&SSH_FILEXFER_ATTR_EXTENDED)!=0){
      len+=4;
      int count=extended.length/2;
      if(count>0){
        for(int i=0; i<count; i++){
          len+=4; len+=extended[i*2].length();
          len+=4; len+=extended[i*2+1].length();
        }
      }
    }
    return len;
  }

  void dump(Buffer buf){
    buf.putInt(flags);
    if((flags&SSH_FILEXFER_ATTR_SIZE)!=0){ buf.putLong(size); }
    if((flags&SSH_FILEXFER_ATTR_UIDGID)!=0){
      buf.putInt(uid); buf.putInt(gid);
    }
    if((flags&SSH_FILEXFER_ATTR_PERMISSIONS)!=0){
      buf.putInt(permissions);
    }
    if((flags&SSH_FILEXFER_ATTR_ACMODTIME)!=0){ buf.putInt(atime); }
    if((flags&SSH_FILEXFER_ATTR_ACMODTIME)!=0){ buf.putInt(mtime); }
    if((flags&SSH_FILEXFER_ATTR_EXTENDED)!=0){
      int count=extended.length/2;
      if(count>0){
        for(int i=0; i<count; i++){
          buf.putString(Util.str2byte(extended[i*2]));
          buf.putString(Util.str2byte(extended[i*2+1]));
        }
      }
    }
  }
  void setFLAGS(int flags){
    this.flags=flags;
  }
  public void setSIZE(long size){
    flags|=SSH_FILEXFER_ATTR_SIZE;
    this.size=size;
  }
  public void setUIDGID(int uid, int gid){
    flags|=SSH_FILEXFER_ATTR_UIDGID;
    this.uid=uid;
    this.gid=gid;
  }
  public void setACMODTIME(int atime, int mtime){
    flags|=SSH_FILEXFER_ATTR_ACMODTIME;
    this.atime=atime;
    this.mtime=mtime;
  }
  public void setPERMISSIONS(int permissions){
    flags|=SSH_FILEXFER_ATTR_PERMISSIONS;
    permissions=(this.permissions&~pmask)|(permissions&pmask);
    this.permissions=permissions;
  }

  private boolean isType(int mask) {
    return (flags&SSH_FILEXFER_ATTR_PERMISSIONS)!=0 &&
           (permissions&S_IFMT)==mask;
  }

  public boolean isReg(){
    return isType(S_IFREG);
  }

  public boolean isDir(){
    return isType(S_IFDIR);
  }

  public boolean isChr(){
    return isType(S_IFCHR);
  }

  public boolean isBlk(){
    return isType(S_IFBLK);
  }

  public boolean isFifo(){
    return isType(S_IFIFO);
  }

  public boolean isLink(){
    return isType(S_IFLNK);
  }

  public boolean isSock(){
    return isType(S_IFSOCK);
  }

  public int getFlags() { return flags; }
  public long getSize() { return size; }
  public int getUId() { return uid; }
  public int getGId() { return gid; }
  public int getPermissions() { return permissions; }
  public int getATime() { return atime; }
  public int getMTime() { return mtime; }
  public String[] getExtended() { return extended; }

  public String toString() {
    return (getPermissionsString()+" "+getUId()+" "+getGId()+" "+getSize()+" "+getMtimeString());
  }
  /*
  public String toString(){
    return (((flags&SSH_FILEXFER_ATTR_SIZE)!=0) ? ("size:"+size+" ") : "")+
           (((flags&SSH_FILEXFER_ATTR_UIDGID)!=0) ? ("uid:"+uid+",gid:"+gid+" ") : "")+
           (((flags&SSH_FILEXFER_ATTR_PERMISSIONS)!=0) ? ("permissions:0x"+Integer.toHexString(permissions)+" ") : "")+
           (((flags&SSH_FILEXFER_ATTR_ACMODTIME)!=0) ? ("atime:"+atime+",mtime:"+mtime+" ") : "")+
           (((flags&SSH_FILEXFER_ATTR_EXTENDED)!=0) ? ("extended:?"+" ") : "");
  }
  */
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class SftpException extends Exception{
  //private static final long serialVersionUID=-5616888495583253811L;
  public int id;
  private Throwable cause=null;
  public SftpException (int id, String message) {
    super(message);
    this.id=id;
  }
  public SftpException (int id, String message, Throwable e) {
    super(message);
    this.id=id;
    this.cause=e;
  }
  public String toString(){
    return id+": "+getMessage();
  }
  public Throwable getCause(){
    return this.cause;
  }
}


/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the sftp protocol support.
 *   $ CLASSPATH=.:../build javac Sftp.java
 *   $ CLASSPATH=.:../build java Sftp
 * You will be asked username, host and passwd.
 * If everything works fine, you will get a prompt 'sftp>'.
 * 'help' command will show available command.
 * In current implementation, the destination path for 'get' and 'put'
 * commands must be a file, not a directory.
 *
 */



class Sftp{
  public static void custom(String[] arg){

    try{
      JSch jsch=new JSch();

      int port=22; // porta padrao 22

      // forçando dois parametros
      if(arg.length == 1)
          arg=new String[]{arg[0],port+""};

      if(arg.length!=2 || !arg[0].contains(",") || !arg[0].contains("@") ){
        System.err.println("usage: y sftp user,pass@remotehost");
        System.err.println("usage: y sftp user,pass@remotehost 22");
        System.exit(-1);
      }

      // pegando porta
      try{
        port=Integer.parseInt(arg[1]);
      }catch(Exception e){
        System.err.println("usage: y sftp user,pass@remotehost");
        System.err.println("usage: y sftp user,pass@remotehost 22");
        System.exit(-1);
      }

      String senha=arg[0].split("@")[0].split(",")[1];
      // tirando senha de arg
      arg=new String[]{arg[0].split("@")[0].split(",")[0]+"@"+arg[0].split("@")[1]};

      String user=arg[0].split("@")[0];
      String host=arg[0].split("@")[1];

      Session session=jsch.getSession(user, host, port);

      // username and password will be given via UserInfo interface.
      UserInfo ui=new MyUserInfo(senha);
      session.setUserInfo(ui);

      session.connect();

      Channel channel=session.openChannel("sftp");
      channel.connect();
      ChannelSftp c=(ChannelSftp)channel;

      java.io.InputStream in=System.in;
      java.io.PrintStream out=System.out;

      java.util.Vector cmds=new java.util.Vector();
      byte[] buf=new byte[1024];
      int i;
      String str;
      int level=0;

      while(true){
        out.print("sftp> ");
        cmds.removeAllElements();
        i=in.read(buf, 0, 1024);
        if(i<=0)break;

        i--;
        if(i>0 && buf[i-1]==0x0d)i--;
        //str=new String(buf, 0, i);
        //System.out.println("|"+str+"|");
        int s=0;
        for(int ii=0; ii<i; ii++){
          if(buf[ii]==' '){
            if(ii-s>0){ cmds.addElement(new String(buf, s, ii-s)); }
            while(ii<i){if(buf[ii]!=' ')break; ii++;}
            s=ii;
          }
        }
        if(s<i){ cmds.addElement(new String(buf, s, i-s)); }
        if(cmds.size()==0)continue;

        String cmd=(String)cmds.elementAt(0);
        if(cmd.equals("quit")){
          c.quit();
          break;
        }
        if(cmd.equals("exit")){
          c.exit();
          break;
        }
        if(cmd.equals("rekey")){
          session.rekey();
          continue;
        }
        if(cmd.equals("compression")){
          if(cmds.size()<2){
            out.println("compression level: "+level);
            continue;
          }
          try{
            level=Integer.parseInt((String)cmds.elementAt(1));
            if(level==0){
              session.setConfig("compression.s2c", "none");
              session.setConfig("compression.c2s", "none");
            }
            else{
              session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");
              session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none");
            }
          }
          catch(Exception e){}
          session.rekey();
          continue;
        }
        if(cmd.equals("cd") || cmd.equals("lcd")){
          if(cmds.size()<2) continue;
          String path=(String)cmds.elementAt(1);
          try{
            if(cmd.equals("cd")) c.cd(path);
            else c.lcd(path);
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("rm") || cmd.equals("rmdir") || cmd.equals("mkdir")){
          if(cmds.size()<2) continue;
          String path=(String)cmds.elementAt(1);
          try{
            if(cmd.equals("rm")) c.rm(path);
            else if(cmd.equals("rmdir")) c.rmdir(path);
            else c.mkdir(path);
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("chgrp") || cmd.equals("chown") || cmd.equals("chmod")){
          if(cmds.size()!=3) continue;
          String path=(String)cmds.elementAt(2);
          int foo=0;
          if(cmd.equals("chmod")){
            byte[] bar=((String)cmds.elementAt(1)).getBytes();
            int k;
            for(int j=0; j<bar.length; j++){
              k=bar[j];
              if(k<'0'||k>'7'){foo=-1; break;}
              foo<<=3;
              foo|=(k-'0');
            }
            if(foo==-1)continue;
          }
          else{
            try{foo=Integer.parseInt((String)cmds.elementAt(1));}
            catch(Exception e){continue;}
          }
          try{
            if(cmd.equals("chgrp")){ c.chgrp(foo, path); }
            else if(cmd.equals("chown")){ c.chown(foo, path); }
            else if(cmd.equals("chmod")){ c.chmod(foo, path); }
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("pwd") || cmd.equals("lpwd")){
           str=(cmd.equals("pwd")?"Remote":"Local");
           str+=" working directory: ";
          if(cmd.equals("pwd")) str+=c.pwd();
          else str+=c.lpwd();
          out.println(str);
          continue;
        }
        if(cmd.equals("ls") || cmd.equals("dir")){
          String path=".";
          if(cmds.size()==2) path=(String)cmds.elementAt(1);
          try{
            java.util.Vector vv=c.ls(path);
            if(vv!=null){
              for(int ii=0; ii<vv.size(); ii++){
//              out.println(vv.elementAt(ii).toString());

                Object obj=vv.elementAt(ii);
                if(obj instanceof ChannelSftp.LsEntry){
                  out.println(((ChannelSftp.LsEntry)obj).getLongname());
                }

              }
            }
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("lls") || cmd.equals("ldir")){
          String path=".";
          if(cmds.size()==2) path=(String)cmds.elementAt(1);
          try{
            java.io.File file=new java.io.File(path);
            if(!file.exists()){
              out.println(path+": No such file or directory");
              continue;
            }
            if(file.isDirectory()){
              String[] list=file.list();
              for(int ii=0; ii<list.length; ii++){
                out.println(list[ii]);
              }
              continue;
            }
            out.println(path);
          }
          catch(Exception e){
            System.out.println(e);
          }
          continue;
        }
        if(cmd.equals("get") ||
           cmd.equals("get-resume") || cmd.equals("get-append") ||
           cmd.equals("put") ||
           cmd.equals("put-resume") || cmd.equals("put-append")
           ){
          if(cmds.size()!=2 && cmds.size()!=3) continue;
          String p1=(String)cmds.elementAt(1);
//        String p2=p1;
          String p2=".";
          if(cmds.size()==3)p2=(String)cmds.elementAt(2);
          try{
            SftpProgressMonitor monitor=new MyProgressMonitor();
            if(cmd.startsWith("get")){
              int mode=ChannelSftp.OVERWRITE;
              if(cmd.equals("get-resume")){ mode=ChannelSftp.RESUME; }
              else if(cmd.equals("get-append")){ mode=ChannelSftp.APPEND; }
              c.get(p1, p2, monitor, mode);
            }
            else{
              int mode=ChannelSftp.OVERWRITE;
              if(cmd.equals("put-resume")){ mode=ChannelSftp.RESUME; }
              else if(cmd.equals("put-append")){ mode=ChannelSftp.APPEND; }
              c.put(p1, p2, monitor, mode);
            }
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("ln") || cmd.equals("symlink") ||
           cmd.equals("rename") || cmd.equals("hardlink")){
          if(cmds.size()!=3) continue;
          String p1=(String)cmds.elementAt(1);
          String p2=(String)cmds.elementAt(2);
          try{
            if(cmd.equals("hardlink")){  c.hardlink(p1, p2); }
            else if(cmd.equals("rename")) c.rename(p1, p2);
            else c.symlink(p1, p2);
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("df")){
          if(cmds.size()>2) continue;
          String p1 = cmds.size()==1 ? ".": (String)cmds.elementAt(1);
          SftpStatVFS stat = c.statVFS(p1);

          long size = stat.getSize();
          long used = stat.getUsed();
          long avail = stat.getAvailForNonRoot();
          long root_avail = stat.getAvail();
          long capacity = stat.getCapacity();

          System.out.println("Size: "+size);
          System.out.println("Used: "+used);
          System.out.println("Avail: "+avail);
          System.out.println("(root): "+root_avail);
          System.out.println("%Capacity: "+capacity);

          continue;
        }
        if(cmd.equals("stat") || cmd.equals("lstat")){
          if(cmds.size()!=2) continue;
          String p1=(String)cmds.elementAt(1);
          SftpATTRS attrs=null;
          try{
            if(cmd.equals("stat")) attrs=c.stat(p1);
            else attrs=c.lstat(p1);
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          if(attrs!=null){
            out.println(attrs);
          }
          else{
          }
          continue;
        }
        if(cmd.equals("readlink")){
          if(cmds.size()!=2) continue;
          String p1=(String)cmds.elementAt(1);
          String filename=null;
          try{
            filename=c.readlink(p1);
            out.println(filename);
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("realpath")){
          if(cmds.size()!=2) continue;
          String p1=(String)cmds.elementAt(1);
          String filename=null;
          try{
            filename=c.realpath(p1);
            out.println(filename);
          }
          catch(SftpException e){
            System.out.println(e.toString());
          }
          continue;
        }
        if(cmd.equals("version")){
          out.println("SFTP protocol version "+c.version());
          continue;
        }
        if(cmd.equals("help") || cmd.equals("?")){
          out.println(help);
          continue;
        }
        out.println("unimplemented command: "+cmd);
      }
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
    System.exit(0);
  }


  private static String help =
"      Available commands:\n"+
"      * means unimplemented command.\n"+
"cd path                       Change remote directory to 'path'\n"+
"lcd path                      Change local directory to 'path'\n"+
"chgrp grp path                Change group of file 'path' to 'grp'\n"+
"chmod mode path               Change permissions of file 'path' to 'mode'\n"+
"chown own path                Change owner of file 'path' to 'own'\n"+
"df [path]                     Display statistics for current directory or\n"+
"                              filesystem containing 'path'\n"+
"help                          Display this help text\n"+
"get remote-path [local-path]  Download file\n"+
"get-resume remote-path [local-path]  Resume to download file.\n"+
"get-append remote-path [local-path]  Append remote file to local file\n"+
"hardlink oldpath newpath      Hardlink remote file\n"+
"*lls [ls-options [path]]      Display local directory listing\n"+
"ln oldpath newpath            Symlink remote file\n"+
"*lmkdir path                  Create local directory\n"+
"lpwd                          Print local working directory\n"+
"ls [path]                     Display remote directory listing\n"+
"*lumask umask                 Set local umask to 'umask'\n"+
"mkdir path                    Create remote directory\n"+
"put local-path [remote-path]  Upload file\n"+
"put-resume local-path [remote-path]  Resume to upload file\n"+
"put-append local-path [remote-path]  Append local file to remote file.\n"+
"pwd                           Display remote working directory\n"+
"stat path                     Display info about path\n"+
"exit                          Quit sftp\n"+
"quit                          Quit sftp\n"+
"rename oldpath newpath        Rename remote file\n"+
"rmdir path                    Remove remote directory\n"+
"rm path                       Delete remote file\n"+
"symlink oldpath newpath       Symlink remote file\n"+
"readlink path                 Check the target of a symbolic link\n"+
"realpath path                 Canonicalize the path\n"+
"rekey                         Key re-exchanging\n"+
"compression level             Packet compression will be enabled\n"+
"version                       Show SFTP version\n"+
"?                             Synonym for help";




  public static class MyProgressMonitor implements SftpProgressMonitor{
    ProgressMonitor monitor;
    long count=0;
    long max=0;
    public void init(int op, String src, String dest, long max){
      this.max=max;
      monitor=new ProgressMonitor(null,
                                  ((op==SftpProgressMonitor.PUT)?
                                   "put" : "get")+": "+src,
                                  "",  0, (int)max);
      count=0;
      percent=-1;
      monitor.setProgress((int)this.count);
      monitor.setMillisToDecideToPopup(1000);
    }
    private long percent=-1;
    public boolean count(long count){
      this.count+=count;

      if(percent>=this.count*100/max){ return true; }
      percent=this.count*100/max;

      monitor.setNote("Completed "+this.count+"("+percent+"%) out of "+max+".");
      monitor.setProgress((int)this.count);

      return !(monitor.isCanceled());
    }
    public void end(){
      monitor.close();
    }
  }

//
//  public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
//    public String getPassword(){ return passwd; }
//    public boolean promptYesNo(String str){
//      Object[] options={ "yes", "no" };
//      int foo=JOptionPane.showOptionDialog(null,
//             str,
//             "Warning",
//             JOptionPane.DEFAULT_OPTION,
//             JOptionPane.WARNING_MESSAGE,
//             null, options, options[0]);
//       return foo==0;
//    }
//
//    String passwd;
//    JTextField passwordField=(JTextField)new JPasswordField(20);
//
//    public String getPassphrase(){ return null; }
//    public boolean promptPassphrase(String message){ return true; }
//    public boolean promptPassword(String message){
//      Object[] ob={passwordField};
//      int result=
//        JOptionPane.showConfirmDialog(null, ob, message,
//                                      JOptionPane.OK_CANCEL_OPTION);
//      if(result==JOptionPane.OK_OPTION){
//      passwd=passwordField.getText();
//      return true;
//      }
//      else{ return false; }
//    }
//    public void showMessage(String message){
//      JOptionPane.showMessageDialog(null, message);
//    }
//    final GridBagConstraints gbc =
//      new GridBagConstraints(0,0,1,1,1,1,
//                             GridBagConstraints.NORTHWEST,
//                             GridBagConstraints.NONE,
//                             new Insets(0,0,0,0),0,0);
//    private Container panel;
//    public String[] promptKeyboardInteractive(String destination,
//                                              String name,
//                                              String instruction,
//                                              String[] prompt,
//                                              boolean[] echo){
//      panel = new JPanel();
//      panel.setLayout(new GridBagLayout());
//
//      gbc.weightx = 1.0;
//      gbc.gridwidth = GridBagConstraints.REMAINDER;
//      gbc.gridx = 0;
//      panel.add(new JLabel(instruction), gbc);
//      gbc.gridy++;
//
//      gbc.gridwidth = GridBagConstraints.RELATIVE;
//
//      JTextField[] texts=new JTextField[prompt.length];
//      for(int i=0; i<prompt.length; i++){
//        gbc.fill = GridBagConstraints.NONE;
//        gbc.gridx = 0;
//        gbc.weightx = 1;
//        panel.add(new JLabel(prompt[i]),gbc);
//
//        gbc.gridx = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weighty = 1;
//        if(echo[i]){
//          texts[i]=new JTextField(20);
//        }
//        else{
//          texts[i]=new JPasswordField(20);
//        }
//        panel.add(texts[i], gbc);
//        gbc.gridy++;
//      }
//
//      if(JOptionPane.showConfirmDialog(null, panel,
//                                       destination+": "+name,
//                                       JOptionPane.OK_CANCEL_OPTION,
//                                       JOptionPane.QUESTION_MESSAGE)
//         ==JOptionPane.OK_OPTION){
//        String[] response=new String[prompt.length];
//        for(int i=0; i<prompt.length; i++){
//          response[i]=texts[i].getText();
//        }
//      return response;
//      }
//      else{
//        return null;  // cancel
//      }
//    }
//  }



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
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface SftpProgressMonitor{
  public static final int PUT=0;
  public static final int GET=1;
  public static final long UNKNOWN_SIZE = -1L;
  void init(int op, String src, String dest, long max);
  boolean count(long count);
  void end();
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class SftpStatVFS {

  /*
   It seems data is serializsed according to sys/statvfs.h; for example,
   http://pubs.opengroup.org/onlinepubs/009604499/basedefs/sys/statvfs.h.html
  */

  private long bsize;
  private long frsize;
  private long blocks;
  private long bfree;
  private long bavail;
  private long files;
  private long ffree;
  private long favail;
  private long fsid;
  private long flag;
  private long namemax;

  int flags=0;
  long size;
  int uid;
  int gid;
  int permissions;
  int atime;
  int mtime;
  String[] extended=null;

  private SftpStatVFS(){
  }

  static SftpStatVFS getStatVFS(Buffer buf){
    SftpStatVFS statvfs=new SftpStatVFS();

    statvfs.bsize = buf.getLong();
    statvfs.frsize = buf.getLong();
    statvfs.blocks = buf.getLong();
    statvfs.bfree = buf.getLong();
    statvfs.bavail = buf.getLong();
    statvfs.files = buf.getLong();
    statvfs.ffree = buf.getLong();
    statvfs.favail = buf.getLong();
    statvfs.fsid = buf.getLong();
    int flag = (int)buf.getLong();
    statvfs.namemax = buf.getLong();

    statvfs.flag =
      (flag & 1/*SSH2_FXE_STATVFS_ST_RDONLY*/) != 0 ? 1/*ST_RDONLY*/ : 0;
    statvfs.flag |=
      (flag & 2/*SSH2_FXE_STATVFS_ST_NOSUID*/) != 0 ? 2/*ST_NOSUID*/ : 0;

    return statvfs;
  }

  public long getBlockSize() { return bsize; }
  public long getFragmentSize() { return frsize; }
  public long getBlocks() { return blocks; }
  public long getFreeBlocks() { return bfree; }
  public long getAvailBlocks() { return bavail; }
  public long getINodes() { return files; }
  public long getFreeINodes() { return ffree; }
  public long getAvailINodes() { return favail; }
  public long getFileSystemID() { return fsid; }
  public long getMountFlag() { return flag; }
  public long getMaximumFilenameLength() { return namemax; }

  public long getSize(){
    return getFragmentSize()*getBlocks()/1024;
  }

  public long getUsed(){
    return getFragmentSize()*(getBlocks()-getFreeBlocks())/1024;
  }

  public long getAvailForNonRoot(){
    return getFragmentSize()*getAvailBlocks()/1024;
  }

  public long getAvail(){
    return getFragmentSize()*getFreeBlocks()/1024;
  }

  public int getCapacity(){
    return (int)(100*(getBlocks()-getFreeBlocks())/getBlocks());
  }

//  public String toString() { return ""; }
}


class Shell{
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
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface SignatureDSA extends Signature {
  void setPubKey(byte[] y, byte[] p, byte[] q, byte[] g) throws Exception;
  void setPrvKey(byte[] x, byte[] p, byte[] q, byte[] g) throws Exception;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2015-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface SignatureECDSA extends Signature {
  void setPubKey(byte[] r, byte[] s) throws Exception;
  void setPrvKey(byte[] s) throws Exception;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2012-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface Signature{
  void init() throws Exception;
  void update(byte[] H) throws Exception;
  boolean verify(byte[] sig) throws Exception;
  byte[] sign() throws Exception;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface SignatureRSA extends Signature {
  void setPubKey(byte[] e, byte[] n) throws Exception;
  void setPrvKey(byte[] d, byte[] n) throws Exception;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




interface SocketFactory{
  public Socket createSocket(String host, int port)throws IOException,
                                                          UnknownHostException;
  public InputStream getInputStream(Socket socket)throws IOException;
  public OutputStream getOutputStream(Socket socket)throws IOException;
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface UIKeyboardInteractive{
  String[] promptKeyboardInteractive(String destination,
                                     String name,
                                     String instruction,
                                     String[] prompt,
                                     boolean[] echo);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2006-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES(INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION)HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING
NEGLIGENCE OR OTHERWISE)ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class UserAuthGSSAPIWithMIC extends UserAuth {
  private static final int SSH_MSG_USERAUTH_GSSAPI_RESPONSE=         60;
  private static final int SSH_MSG_USERAUTH_GSSAPI_TOKEN=            61;
  private static final int SSH_MSG_USERAUTH_GSSAPI_EXCHANGE_COMPLETE=63;
  private static final int SSH_MSG_USERAUTH_GSSAPI_ERROR=            64;
  private static final int SSH_MSG_USERAUTH_GSSAPI_ERRTOK=           65;
  private static final int SSH_MSG_USERAUTH_GSSAPI_MIC=              66;

  private static final byte[][] supported_oid={
    // OID 1.2.840.113554.1.2.2 in DER
    {(byte)0x6,(byte)0x9,(byte)0x2a,(byte)0x86,(byte)0x48,
     (byte)0x86,(byte)0xf7,(byte)0x12,(byte)0x1,(byte)0x2,
     (byte)0x2}
  };

  private static final String[] supported_method={
    "gssapi-with-mic.krb5"
  };

  public boolean start(Session session)throws Exception{
    super.start(session);

    byte[] _username=Util.str2byte(username);

    packet.reset();

    // byte            SSH_MSG_USERAUTH_REQUEST(50)
    // string          user name(in ISO-10646 UTF-8 encoding)
    // string          service name(in US-ASCII)
    // string          "gssapi"(US-ASCII)
    // uint32          n, the number of OIDs client supports
    // string[n]       mechanism OIDS
    buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
    buf.putString(_username);
    buf.putString(Util.str2byte("ssh-connection"));
    buf.putString(Util.str2byte("gssapi-with-mic"));
    buf.putInt(supported_oid.length);
    for(int i=0; i<supported_oid.length; i++){
      buf.putString(supported_oid[i]);
    }
    session.write(packet);

    String method=null;
    int command;
    while(true){
      buf=session.read(buf);
      command=buf.getCommand()&0xff;

      if(command==SSH_MSG_USERAUTH_FAILURE){
        return false;
      }

      if(command==SSH_MSG_USERAUTH_GSSAPI_RESPONSE){
        buf.getInt(); buf.getByte(); buf.getByte();
        byte[] message=buf.getString();

        for(int i=0; i<supported_oid.length; i++){
          if(Util.array_equals(message, supported_oid[i])){
            method=supported_method[i];
            break;
          }
        }

        if(method==null){
          return false;
        }

        break; // success
      }

      if(command==SSH_MSG_USERAUTH_BANNER){
        buf.getInt(); buf.getByte(); buf.getByte();
        byte[] _message=buf.getString();
        byte[] lang=buf.getString();
        String message=Util.byte2str(_message);
        if(userinfo!=null){
          userinfo.showMessage(message);
        }
        continue;
      }
      return false;
    }

    GSSContext context=null;
    try{
      Class c=Class.forName(session.getConfig(method));
      context=(GSSContext)(c.newInstance());
    }
    catch(Exception e){
      return false;
    }

    try{
      context.create(username, session.host);
    }
    catch(JSchException e){
      return false;
    }

    byte[] token=new byte[0];

    while(!context.isEstablished()){
      try{
        token=context.init(token, 0, token.length);
      }
      catch(JSchException e){
        // TODO
        // ERRTOK should be sent?
        // byte        SSH_MSG_USERAUTH_GSSAPI_ERRTOK
        // string      error token
        return false;
      }

      if(token!=null){
        packet.reset();
        buf.putByte((byte)SSH_MSG_USERAUTH_GSSAPI_TOKEN);
        buf.putString(token);
        session.write(packet);
      }

      if(!context.isEstablished()){
        buf=session.read(buf);
        command=buf.getCommand()&0xff;
        if(command==SSH_MSG_USERAUTH_GSSAPI_ERROR){
          // uint32    major_status
          // uint32    minor_status
          // string    message
          // string    language tag

          buf=session.read(buf);
          command=buf.getCommand()&0xff;
          //return false;
        }
        else if(command==SSH_MSG_USERAUTH_GSSAPI_ERRTOK){
          // string error token

          buf=session.read(buf);
          command=buf.getCommand()&0xff;
          //return false;
        }

        if(command==SSH_MSG_USERAUTH_FAILURE){
          return false;
        }

        buf.getInt(); buf.getByte(); buf.getByte();
        token=buf.getString();
      }
    }

    Buffer mbuf=new Buffer();
    // string    session identifier
    // byte      SSH_MSG_USERAUTH_REQUEST
    // string    user name
    // string    service
    // string    "gssapi-with-mic"
    mbuf.putString(session.getSessionId());
    mbuf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
    mbuf.putString(_username);
    mbuf.putString(Util.str2byte("ssh-connection"));
    mbuf.putString(Util.str2byte("gssapi-with-mic"));

    byte[] mic=context.getMIC(mbuf.buffer, 0, mbuf.getLength());

    if(mic==null){
      return false;
    }

    packet.reset();
    buf.putByte((byte)SSH_MSG_USERAUTH_GSSAPI_MIC);
    buf.putString(mic);
    session.write(packet);

    context.dispose();

    buf=session.read(buf);
    command=buf.getCommand()&0xff;

    if(command==SSH_MSG_USERAUTH_SUCCESS){
      return true;
    }
    else if(command==SSH_MSG_USERAUTH_FAILURE){
      buf.getInt(); buf.getByte(); buf.getByte();
      byte[] foo=buf.getString();
      int partial_success=buf.getByte();
      //System.err.println(new String(foo)+
      //                 " partial_success:"+(partial_success!=0));
      if(partial_success!=0){
        throw new JSchPartialAuthException(Util.byte2str(foo));
      }
    }
    return false;
  }
}


/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



abstract class UserAuth{
  protected static final int SSH_MSG_USERAUTH_REQUEST=               50;
  protected static final int SSH_MSG_USERAUTH_FAILURE=               51;
  protected static final int SSH_MSG_USERAUTH_SUCCESS=               52;
  protected static final int SSH_MSG_USERAUTH_BANNER=                53;
  protected static final int SSH_MSG_USERAUTH_INFO_REQUEST=          60;
  protected static final int SSH_MSG_USERAUTH_INFO_RESPONSE=         61;
  protected static final int SSH_MSG_USERAUTH_PK_OK=                 60;

  protected UserInfo userinfo;
  protected Packet packet;
  protected Buffer buf;
  protected String username;

  public boolean start(Session session) throws Exception{
    this.userinfo=session.getUserInfo();
    this.packet=session.packet;
    this.buf=packet.getBuffer();
    this.username=session.getUserName();
    return true;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class UserAuthKeyboardInteractive extends UserAuth{
  public boolean start(Session session) throws Exception{
    super.start(session);

    if(userinfo!=null && !(userinfo instanceof UIKeyboardInteractive)){
      return false;
    }

    String dest=username+"@"+session.host;
    if(session.port!=22){
      dest+=(":"+session.port);
    }
    byte[] password=session.password;

    boolean cancel=false;

    byte[] _username=null;
    _username=Util.str2byte(username);

    while(true){

      if(session.auth_failures >= session.max_auth_tries){
        return false;
      }

      // send
      // byte      SSH_MSG_USERAUTH_REQUEST(50)
      // string    user name (ISO-10646 UTF-8, as defined in [RFC-2279])
      // string    service name (US-ASCII) "ssh-userauth" ? "ssh-connection"
      // string    "keyboard-interactive" (US-ASCII)
      // string    language tag (as defined in [RFC-3066])
      // string    submethods (ISO-10646 UTF-8)
      packet.reset();
      buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
      buf.putString(_username);
      buf.putString(Util.str2byte("ssh-connection"));
      //buf.putString("ssh-userauth".getBytes());
      buf.putString(Util.str2byte("keyboard-interactive"));
      buf.putString(Util.empty);
      buf.putString(Util.empty);
      session.write(packet);

      boolean firsttime=true;
      loop:
      while(true){
        buf=session.read(buf);
        int command=buf.getCommand()&0xff;

        if(command==SSH_MSG_USERAUTH_SUCCESS){
          return true;
        }
        if(command==SSH_MSG_USERAUTH_BANNER){
          buf.getInt(); buf.getByte(); buf.getByte();
          byte[] _message=buf.getString();
          byte[] lang=buf.getString();
          String message=Util.byte2str(_message);
          if(userinfo!=null){
            userinfo.showMessage(message);
          }
          continue loop;
        }
        if(command==SSH_MSG_USERAUTH_FAILURE){
          buf.getInt(); buf.getByte(); buf.getByte();
          byte[] foo=buf.getString();
          int partial_success=buf.getByte();
//        System.err.println(new String(foo)+
//                           " partial_success:"+(partial_success!=0));

          if(partial_success!=0){
            throw new JSchPartialAuthException(Util.byte2str(foo));
          }

          if(firsttime){
            return false;
            //throw new JSchException("USERAUTH KI is not supported");
            //cancel=true;  // ??
          }
          session.auth_failures++;
          break;
        }
        if(command==SSH_MSG_USERAUTH_INFO_REQUEST){
          firsttime=false;
          buf.getInt(); buf.getByte(); buf.getByte();
          String name=Util.byte2str(buf.getString());
          String instruction=Util.byte2str(buf.getString());
          String languate_tag=Util.byte2str(buf.getString());
          int num=buf.getInt();
          String[] prompt=new String[num];
          boolean[] echo=new boolean[num];
          for(int i=0; i<num; i++){
            prompt[i]=Util.byte2str(buf.getString());
            echo[i]=(buf.getByte()!=0);
          }

          byte[][] response=null;

          if(password!=null &&
             prompt.length==1 &&
             !echo[0] &&
             prompt[0].toLowerCase().indexOf("password:") >= 0){
            response=new byte[1][];
            response[0]=password;
            password=null;
          }
          else if(num>0
             ||(name.length()>0 || instruction.length()>0)
             ){
            if(userinfo!=null){
              UIKeyboardInteractive kbi=(UIKeyboardInteractive)userinfo;
              String[] _response=kbi.promptKeyboardInteractive(dest,
                                                               name,
                                                               instruction,
                                                               prompt,
                                                               echo);
              if(_response!=null){
                response=new byte[_response.length][];
                for(int i=0; i<_response.length; i++){
                  response[i]=Util.str2byte(_response[i]);
                }
              }
            }
          }

          // byte      SSH_MSG_USERAUTH_INFO_RESPONSE(61)
          // int       num-responses
          // string    response[1] (ISO-10646 UTF-8)
          // ...
          // string    response[num-responses] (ISO-10646 UTF-8)
          packet.reset();
          buf.putByte((byte)SSH_MSG_USERAUTH_INFO_RESPONSE);
          if(num>0 &&
             (response==null ||  // cancel
              num!=response.length)){

            if(response==null){
              // working around the bug in OpenSSH ;-<
              buf.putInt(num);
              for(int i=0; i<num; i++){
                buf.putString(Util.empty);
              }
            }
            else{
              buf.putInt(0);
            }

            if(response==null)
              cancel=true;
          }
          else{
            buf.putInt(num);
            for(int i=0; i<num; i++){
              buf.putString(response[i]);
            }
          }
          session.write(packet);
          /*
          if(cancel)
            break;
          */
          continue loop;
        }
        //throw new JSchException("USERAUTH fail ("+command+")");
        return false;
      }
      if(cancel){
        throw new JSchAuthCancelException("keyboard-interactive");
        //break;
      }
    }
    //return false;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class UserAuthNone extends UserAuth{
  private static final int SSH_MSG_SERVICE_ACCEPT=                  6;
  private String methods=null;

  public boolean start(Session session) throws Exception{
    super.start(session);


    // send
    // byte      SSH_MSG_SERVICE_REQUEST(5)
    // string    service name "ssh-userauth"
    packet.reset();
    buf.putByte((byte)Session.SSH_MSG_SERVICE_REQUEST);
    buf.putString(Util.str2byte("ssh-userauth"));
    session.write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_SERVICE_REQUEST sent");
    }

    // receive
    // byte      SSH_MSG_SERVICE_ACCEPT(6)
    // string    service name
    buf=session.read(buf);
    int command=buf.getCommand();

    boolean result=(command==SSH_MSG_SERVICE_ACCEPT);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO,
                           "SSH_MSG_SERVICE_ACCEPT received");
    }
    if(!result)
      return false;

    byte[] _username=null;
    _username=Util.str2byte(username);

    // send
    // byte      SSH_MSG_USERAUTH_REQUEST(50)
    // string    user name
    // string    service name ("ssh-connection")
    // string    "none"
    packet.reset();
    buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
    buf.putString(_username);
    buf.putString(Util.str2byte("ssh-connection"));
    buf.putString(Util.str2byte("none"));
    session.write(packet);

    loop:
    while(true){
      buf=session.read(buf);
      command=buf.getCommand()&0xff;

      if(command==SSH_MSG_USERAUTH_SUCCESS){
        return true;
      }
      if(command==SSH_MSG_USERAUTH_BANNER){
        buf.getInt(); buf.getByte(); buf.getByte();
        byte[] _message=buf.getString();
        byte[] lang=buf.getString();
        String message=Util.byte2str(_message);
        if(userinfo!=null){
          try{
            userinfo.showMessage(message);
          }
          catch(RuntimeException ee){
          }
        }
        continue loop;
      }
      if(command==SSH_MSG_USERAUTH_FAILURE){
        buf.getInt(); buf.getByte(); buf.getByte();
        byte[] foo=buf.getString();
        int partial_success=buf.getByte();
        methods=Util.byte2str(foo);
//System.err.println("UserAuthNONE: "+methods+
//                 " partial_success:"+(partial_success!=0));
//      if(partial_success!=0){
//        throw new JSchPartialAuthException(new String(foo));
//      }

        break;
      }
      else{
//      System.err.println("USERAUTH fail ("+command+")");
        throw new JSchException("USERAUTH fail ("+command+")");
      }
    }
   //throw new JSchException("USERAUTH fail");
    return false;
  }
  String getMethods(){
    return methods;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class UserAuthPassword extends UserAuth{
  private final int SSH_MSG_USERAUTH_PASSWD_CHANGEREQ=60;

  public boolean start(Session session) throws Exception{
    super.start(session);

    byte[] password=session.password;
    String dest=username+"@"+session.host;
    if(session.port!=22){
      dest+=(":"+session.port);
    }

    try{

    while(true){

      if(session.auth_failures >= session.max_auth_tries){
        return false;
      }

      if(password==null){
        if(userinfo==null){
          //throw new JSchException("USERAUTH fail");
          return false;
        }

        if(!userinfo.promptPassword("Password for "+dest)){
          throw new JSchAuthCancelException("password");
          //break;
        }

        String _password=userinfo.getPassword();
        if(_password==null){
          throw new JSchAuthCancelException("password");
          //break;
        }
        password=Util.str2byte(_password);
      }

      byte[] _username=null;
      _username=Util.str2byte(username);

      // send
      // byte      SSH_MSG_USERAUTH_REQUEST(50)
      // string    user name
      // string    service name ("ssh-connection")
      // string    "password"
      // boolen    FALSE
      // string    plaintext password (ISO-10646 UTF-8)
      packet.reset();
      buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
      buf.putString(_username);
      buf.putString(Util.str2byte("ssh-connection"));
      buf.putString(Util.str2byte("password"));
      buf.putByte((byte)0);
      buf.putString(password);
      session.write(packet);

      loop:
      while(true){
        buf=session.read(buf);
        int command=buf.getCommand()&0xff;

        if(command==SSH_MSG_USERAUTH_SUCCESS){
          return true;
        }
        if(command==SSH_MSG_USERAUTH_BANNER){
          buf.getInt(); buf.getByte(); buf.getByte();
          byte[] _message=buf.getString();
          byte[] lang=buf.getString();
          String message=Util.byte2str(_message);
          if(userinfo!=null){
            userinfo.showMessage(message);
          }
          continue loop;
        }
        if(command==SSH_MSG_USERAUTH_PASSWD_CHANGEREQ){
          buf.getInt(); buf.getByte(); buf.getByte();
          byte[] instruction=buf.getString();
          byte[] tag=buf.getString();
          if(userinfo==null ||
             !(userinfo instanceof UIKeyboardInteractive)){
            if(userinfo!=null){
              userinfo.showMessage("Password must be changed.");
            }
            return false;
          }

          UIKeyboardInteractive kbi=(UIKeyboardInteractive)userinfo;
          String[] response;
          String name="Password Change Required";
          String[] prompt={"New Password: "};
          boolean[] echo={false};
          response=kbi.promptKeyboardInteractive(dest,
                                                 name,
                                                 Util.byte2str(instruction),
                                                 prompt,
                                                 echo);
          if(response==null){
            throw new JSchAuthCancelException("password");
          }

          byte[] newpassword=Util.str2byte(response[0]);

          // send
          // byte      SSH_MSG_USERAUTH_REQUEST(50)
          // string    user name
          // string    service name ("ssh-connection")
          // string    "password"
          // boolen    TRUE
          // string    plaintext old password (ISO-10646 UTF-8)
          // string    plaintext new password (ISO-10646 UTF-8)
          packet.reset();
          buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
          buf.putString(_username);
          buf.putString(Util.str2byte("ssh-connection"));
          buf.putString(Util.str2byte("password"));
          buf.putByte((byte)1);
          buf.putString(password);
          buf.putString(newpassword);
          Util.bzero(newpassword);
          response=null;
          session.write(packet);
          continue loop;
        }
        if(command==SSH_MSG_USERAUTH_FAILURE){
          buf.getInt(); buf.getByte(); buf.getByte();
          byte[] foo=buf.getString();
          int partial_success=buf.getByte();
          //System.err.println(new String(foo)+
          //             " partial_success:"+(partial_success!=0));
          if(partial_success!=0){
            throw new JSchPartialAuthException(Util.byte2str(foo));
          }
          session.auth_failures++;
          break;
        }
        else{
          //System.err.println("USERAUTH fail ("+buf.getCommand()+")");
//        throw new JSchException("USERAUTH fail ("+buf.getCommand()+")");
          return false;
        }
      }

      if(password!=null){
        Util.bzero(password);
        password=null;
      }

    }

    }
    finally{
      if(password!=null){
        Util.bzero(password);
        password=null;
      }
    }

    //throw new JSchException("USERAUTH fail");
    //return false;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/




class UserAuthPublicKey extends UserAuth{

  public boolean start(Session session) throws Exception{
    super.start(session);

    Vector identities=session.getIdentityRepository().getIdentities();

    byte[] passphrase=null;
    byte[] _username=null;

    int command;

    synchronized(identities){
      if(identities.size()<=0){
        return false;
      }

      _username=Util.str2byte(username);

      for(int i=0; i<identities.size(); i++){

        if(session.auth_failures >= session.max_auth_tries){
          return false;
        }

        Identity identity=(Identity)(identities.elementAt(i));
        byte[] pubkeyblob=identity.getPublicKeyBlob();

        if(pubkeyblob!=null){
          // send
          // byte      SSH_MSG_USERAUTH_REQUEST(50)
          // string    user name
          // string    service name ("ssh-connection")
          // string    "publickey"
          // boolen    FALSE
          // string    public key algorithm name
          // string    public key blob
          packet.reset();
          buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
          buf.putString(_username);
          buf.putString(Util.str2byte("ssh-connection"));
          buf.putString(Util.str2byte("publickey"));
          buf.putByte((byte)0);
          buf.putString(Util.str2byte(identity.getAlgName()));
          buf.putString(pubkeyblob);
          session.write(packet);

          loop1:
          while(true){
            buf=session.read(buf);
            command=buf.getCommand()&0xff;

            if(command==SSH_MSG_USERAUTH_PK_OK){
              break;
            }
            else if(command==SSH_MSG_USERAUTH_FAILURE){
              break;
            }
            else if(command==SSH_MSG_USERAUTH_BANNER){
              buf.getInt(); buf.getByte(); buf.getByte();
              byte[] _message=buf.getString();
              byte[] lang=buf.getString();
              String message=Util.byte2str(_message);
              if(userinfo!=null){
                userinfo.showMessage(message);
              }
              continue loop1;
            }
            else{
            //System.err.println("USERAUTH fail ("+command+")");
            //throw new JSchException("USERAUTH fail ("+command+")");
              break;
            }
          }

          if(command!=SSH_MSG_USERAUTH_PK_OK){
            continue;
          }
        }

//System.err.println("UserAuthPublicKey: identity.isEncrypted()="+identity.isEncrypted());

        int count=5;
        while(true){
          if((identity.isEncrypted() && passphrase==null)){
            if(userinfo==null) throw new JSchException("USERAUTH fail");
            if(identity.isEncrypted() &&
               !userinfo.promptPassphrase("Passphrase for "+identity.getName())){
              throw new JSchAuthCancelException("publickey");
              //throw new JSchException("USERAUTH cancel");
              //break;
            }
            String _passphrase=userinfo.getPassphrase();
            if(_passphrase!=null){
              passphrase=Util.str2byte(_passphrase);
            }
          }

          if(!identity.isEncrypted() || passphrase!=null){
            if(identity.setPassphrase(passphrase)){
              if(passphrase!=null &&
                 (session.getIdentityRepository() instanceof IdentityRepository.Wrapper)){
                ((IdentityRepository.Wrapper)session.getIdentityRepository()).check();
              }
              break;
            }
          }
          Util.bzero(passphrase);
          passphrase=null;
          count--;
          if(count==0)break;
        }

        Util.bzero(passphrase);
        passphrase=null;
//System.err.println("UserAuthPublicKey: identity.isEncrypted()="+identity.isEncrypted());

        if(identity.isEncrypted()) continue;
        if(pubkeyblob==null) pubkeyblob=identity.getPublicKeyBlob();

//System.err.println("UserAuthPublicKey: pubkeyblob="+pubkeyblob);

        if(pubkeyblob==null) continue;

        // send
        // byte      SSH_MSG_USERAUTH_REQUEST(50)
        // string    user name
        // string    service name ("ssh-connection")
        // string    "publickey"
        // boolen    TRUE
        // string    public key algorithm name
        // string    public key blob
        // string    signature
        packet.reset();
        buf.putByte((byte)SSH_MSG_USERAUTH_REQUEST);
        buf.putString(_username);
        buf.putString(Util.str2byte("ssh-connection"));
        buf.putString(Util.str2byte("publickey"));
        buf.putByte((byte)1);
        buf.putString(Util.str2byte(identity.getAlgName()));
        buf.putString(pubkeyblob);

//      byte[] tmp=new byte[buf.index-5];
//      System.arraycopy(buf.buffer, 5, tmp, 0, tmp.length);
//      buf.putString(signature);

        byte[] sid=session.getSessionId();
        int sidlen=sid.length;
        byte[] tmp=new byte[4+sidlen+buf.index-5];
        tmp[0]=(byte)(sidlen>>>24);
        tmp[1]=(byte)(sidlen>>>16);
        tmp[2]=(byte)(sidlen>>>8);
        tmp[3]=(byte)(sidlen);
        System.arraycopy(sid, 0, tmp, 4, sidlen);
        System.arraycopy(buf.buffer, 5, tmp, 4+sidlen, buf.index-5);
        byte[] signature=identity.getSignature(tmp);
        if(signature==null){  // for example, too long key length.
          break;
        }
        buf.putString(signature);
        session.write(packet);

        loop2:
        while(true){
          buf=session.read(buf);
          command=buf.getCommand()&0xff;

          if(command==SSH_MSG_USERAUTH_SUCCESS){
            return true;
          }
          else if(command==SSH_MSG_USERAUTH_BANNER){
            buf.getInt(); buf.getByte(); buf.getByte();
            byte[] _message=buf.getString();
            byte[] lang=buf.getString();
            String message=Util.byte2str(_message);
            if(userinfo!=null){
              userinfo.showMessage(message);
            }
            continue loop2;
          }
          else if(command==SSH_MSG_USERAUTH_FAILURE){
            buf.getInt(); buf.getByte(); buf.getByte();
            byte[] foo=buf.getString();
            int partial_success=buf.getByte();
          //System.err.println(new String(foo)+
          //                   " partial_success:"+(partial_success!=0));
            if(partial_success!=0){
              throw new JSchPartialAuthException(Util.byte2str(foo));
            }
            session.auth_failures++;
            break;
          }
          //System.err.println("USERAUTH fail ("+command+")");
          //throw new JSchException("USERAUTH fail ("+command+")");
          break;
        }
      }
    }
    return false;
  }
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



interface UserInfo{
  String getPassphrase();
  String getPassword();
  boolean promptPassword(String message);
  boolean promptPassphrase(String message);
  boolean promptYesNo(String message);
  void showMessage(String message);
}
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002-2016 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/



class Util{

  private static final byte[] b64 =Util.str2byte("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");
  private static byte val(byte foo){
    if(foo == '=') return 0;
    for(int j=0; j<b64.length; j++){
      if(foo==b64[j]) return (byte)j;
    }
    return 0;
  }
  static byte[] fromBase64(byte[] buf, int start, int length) throws JSchException {
    try {
      byte[] foo=new byte[length];
      int j=0;
      for (int i=start;i<start+length;i+=4){
        foo[j]=(byte)((val(buf[i])<<2)|((val(buf[i+1])&0x30)>>>4));
        if(buf[i+2]==(byte)'='){ j++; break;}
        foo[j+1]=(byte)(((val(buf[i+1])&0x0f)<<4)|((val(buf[i+2])&0x3c)>>>2));
        if(buf[i+3]==(byte)'='){ j+=2; break;}
        foo[j+2]=(byte)(((val(buf[i+2])&0x03)<<6)|(val(buf[i+3])&0x3f));
        j+=3;
      }
      byte[] bar=new byte[j];
      System.arraycopy(foo, 0, bar, 0, j);
      return bar;
    }
    catch(ArrayIndexOutOfBoundsException e) {
      throw new JSchException("fromBase64: invalid base64 data", e);
    }
  }
  static byte[] toBase64(byte[] buf, int start, int length){

    byte[] tmp=new byte[length*2];
    int i,j,k;

    int foo=(length/3)*3+start;
    i=0;
    for(j=start; j<foo; j+=3){
      k=(buf[j]>>>2)&0x3f;
      tmp[i++]=b64[k];
      k=(buf[j]&0x03)<<4|(buf[j+1]>>>4)&0x0f;
      tmp[i++]=b64[k];
      k=(buf[j+1]&0x0f)<<2|(buf[j+2]>>>6)&0x03;
      tmp[i++]=b64[k];
      k=buf[j+2]&0x3f;
      tmp[i++]=b64[k];
    }

    foo=(start+length)-foo;
    if(foo==1){
      k=(buf[j]>>>2)&0x3f;
      tmp[i++]=b64[k];
      k=((buf[j]&0x03)<<4)&0x3f;
      tmp[i++]=b64[k];
      tmp[i++]=(byte)'=';
      tmp[i++]=(byte)'=';
    }
    else if(foo==2){
      k=(buf[j]>>>2)&0x3f;
      tmp[i++]=b64[k];
      k=(buf[j]&0x03)<<4|(buf[j+1]>>>4)&0x0f;
      tmp[i++]=b64[k];
      k=((buf[j+1]&0x0f)<<2)&0x3f;
      tmp[i++]=b64[k];
      tmp[i++]=(byte)'=';
    }
    byte[] bar=new byte[i];
    System.arraycopy(tmp, 0, bar, 0, i);
    return bar;

//    return sun.misc.BASE64Encoder().encode(buf);
  }

  static String[] split(String foo, String split){
    if(foo==null)
      return null;
    byte[] buf=Util.str2byte(foo);
    java.util.Vector bar=new java.util.Vector();
    int start=0;
    int index;
    while(true){
      index=foo.indexOf(split, start);
      if(index>=0){
        bar.addElement(Util.byte2str(buf, start, index-start));
        start=index+1;
        continue;
      }
      bar.addElement(Util.byte2str(buf, start, buf.length-start));
      break;
    }
    String[] result=new String[bar.size()];
    for(int i=0; i<result.length; i++){
      result[i]=(String)(bar.elementAt(i));
    }
    return result;
  }
  static boolean glob(byte[] pattern, byte[] name){
    return glob0(pattern, 0, name, 0);
  }
  static private boolean glob0(byte[] pattern, int pattern_index,
                              byte[] name, int name_index){
    if(name.length>0 && name[0]=='.'){
      if(pattern.length>0 && pattern[0]=='.'){
        if(pattern.length==2 && pattern[1]=='*') return true;
        return glob(pattern, pattern_index+1, name, name_index+1);
      }
      return false;
    }
    return glob(pattern, pattern_index, name, name_index);
  }
  static private boolean glob(byte[] pattern, int pattern_index,
                              byte[] name, int name_index){
    //System.err.println("glob: "+new String(pattern)+", "+pattern_index+" "+new String(name)+", "+name_index);

    int patternlen=pattern.length;
    if(patternlen==0)
      return false;

    int namelen=name.length;
    int i=pattern_index;
    int j=name_index;

    while(i<patternlen && j<namelen){
      if(pattern[i]=='\\'){
        if(i+1==patternlen)
          return false;
        i++;
        if(pattern[i]!=name[j])
          return false;
        i+=skipUTF8Char(pattern[i]);
        j+=skipUTF8Char(name[j]);
        continue;
      }

      if(pattern[i]=='*'){
        while(i<patternlen){
          if(pattern[i]=='*'){
            i++;
            continue;
          }
          break;
        }
        if(patternlen==i)
          return true;

        byte foo=pattern[i];
        if(foo=='?'){
          while(j<namelen){
            if(glob(pattern, i, name, j)){
              return true;
            }
            j+=skipUTF8Char(name[j]);
          }
          return false;
        }
        else if(foo=='\\'){
          if(i+1==patternlen)
            return false;
          i++;
          foo=pattern[i];
          while(j<namelen){
            if(foo==name[j]){
              if(glob(pattern, i+skipUTF8Char(foo),
                      name, j+skipUTF8Char(name[j]))){
                return true;
              }
            }
            j+=skipUTF8Char(name[j]);
          }
          return false;
        }

        while(j<namelen){
          if(foo==name[j]){
            if(glob(pattern, i, name, j)){
              return true;
            }
          }
          j+=skipUTF8Char(name[j]);
        }
        return false;
      }

      if(pattern[i]=='?'){
        i++;
        j+=skipUTF8Char(name[j]);
        continue;
      }

      if(pattern[i]!=name[j])
        return false;

      i+=skipUTF8Char(pattern[i]);
      j+=skipUTF8Char(name[j]);

      if(!(j<namelen)){         // name is end
        if(!(i<patternlen)){    // pattern is end
          return true;
        }
        if(pattern[i]=='*'){
          break;
        }
      }
      continue;
    }

    if(i==patternlen && j==namelen)
      return true;

    if(!(j<namelen) &&  // name is end
       pattern[i]=='*'){
      boolean ok=true;
      while(i<patternlen){
        if(pattern[i++]!='*'){
          ok=false;
          break;
        }
      }
      return ok;
    }

    return false;
  }

  static String quote(String path){
    byte[] _path=str2byte(path);
    int count=0;
    for(int i=0;i<_path.length; i++){
      byte b=_path[i];
      if(b=='\\' || b=='?' || b=='*')
        count++;
    }
    if(count==0)
      return path;
    byte[] _path2=new byte[_path.length+count];
    for(int i=0, j=0; i<_path.length; i++){
      byte b=_path[i];
      if(b=='\\' || b=='?' || b=='*'){
        _path2[j++]='\\';
      }
      _path2[j++]=b;
    }
    return byte2str(_path2);
  }

  static String unquote(String path){
    byte[] foo=str2byte(path);
    byte[] bar=unquote(foo);
    if(foo.length==bar.length)
      return path;
    return byte2str(bar);
  }
  static byte[] unquote(byte[] path){
    int pathlen=path.length;
    int i=0;
    while(i<pathlen){
      if(path[i]=='\\'){
        if(i+1==pathlen)
          break;
        System.arraycopy(path, i+1, path, i, path.length-(i+1));
        pathlen--;
        i++;
        continue;
      }
      i++;
    }
    if(pathlen==path.length)
      return path;
    byte[] foo=new byte[pathlen];
    System.arraycopy(path, 0, foo, 0, pathlen);
    return foo;
  }

  private static String[] chars={
    "0","1","2","3","4","5","6","7","8","9", "a","b","c","d","e","f"
  };
  static String getFingerPrint(HASH hash, byte[] data){
    try{
      hash.init();
      hash.update(data, 0, data.length);
      byte[] foo=hash.digest();
      StringBuffer sb=new StringBuffer();
      int bar;
      for(int i=0; i<foo.length;i++){
        bar=foo[i]&0xff;
        sb.append(chars[(bar>>>4)&0xf]);
        sb.append(chars[(bar)&0xf]);
        if(i+1<foo.length)
          sb.append(":");
      }
      return sb.toString();
    }
    catch(Exception e){
      return "???";
    }
  }
  static boolean array_equals(byte[] foo, byte bar[]){
    int i=foo.length;
    if(i!=bar.length) return false;
    for(int j=0; j<i; j++){ if(foo[j]!=bar[j]) return false; }
    //try{while(true){i--; if(foo[i]!=bar[i])return false;}}catch(Exception e){}
    return true;
  }
  static Socket createSocket(String host, int port, int timeout) throws JSchException{
    Socket socket=null;
    if(timeout==0){
      try{
        socket=new Socket(host, port);
        return socket;
      }
      catch(Exception e){
        String message=e.toString();
        if(e instanceof Throwable)
          throw new JSchException(message, (Throwable)e);
        throw new JSchException(message);
      }
    }
    final String _host=host;
    final int _port=port;
    final Socket[] sockp=new Socket[1];
    final Exception[] ee=new Exception[1];
    String message="";
    Thread tmp=new Thread(new Runnable(){
        public void run(){
          sockp[0]=null;
          try{
            sockp[0]=new Socket(_host, _port);
          }
          catch(Exception e){
            ee[0]=e;
            if(sockp[0]!=null && sockp[0].isConnected()){
              try{
                sockp[0].close();
              }
              catch(Exception eee){}
            }
            sockp[0]=null;
          }
        }
      });
    tmp.setName("Opening Socket "+host);
    tmp.start();
    try{
      tmp.join(timeout);
      message="timeout: ";
    }
    catch(java.lang.InterruptedException eee){
    }
    if(sockp[0]!=null && sockp[0].isConnected()){
      socket=sockp[0];
    }
    else{
      message+="socket is not established";
      if(ee[0]!=null){
        message=ee[0].toString();
      }
      tmp.interrupt();
      tmp=null;
      throw new JSchException(message, ee[0]);
    }
    return socket;
  }

  static byte[] str2byte(String str, String encoding){
    if(str==null)
      return null;
    try{ return str.getBytes(encoding); }
    catch(java.io.UnsupportedEncodingException e){
      return str.getBytes();
    }
  }

  static byte[] str2byte(String str){
    return str2byte(str, "UTF-8");
  }

  static String byte2str(byte[] str, String encoding){
    return byte2str(str, 0, str.length, encoding);
  }

  static String byte2str(byte[] str, int s, int l, String encoding){
    try{ return new String(str, s, l, encoding); }
    catch(java.io.UnsupportedEncodingException e){
      return new String(str, s, l);
    }
  }

  static String byte2str(byte[] str){
    return byte2str(str, 0, str.length, "UTF-8");
  }

  static String byte2str(byte[] str, int s, int l){
    return byte2str(str, s, l, "UTF-8");
  }

  static String toHex(byte[] str){
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i<str.length; i++){
      String foo = Integer.toHexString(str[i]&0xff);
      sb.append("0x"+(foo.length() == 1 ? "0" : "")+foo);
      if(i+1<str.length)
        sb.append(":");
    }
    return sb.toString();
  }

  static final byte[] empty = str2byte("");

  /*
  static byte[] char2byte(char[] foo){
    int len=0;
    for(int i=0; i<foo.length; i++){
      if((foo[i]&0xff00)==0) len++;
      else len+=2;
    }
    byte[] bar=new byte[len];
    for(int i=0, j=0; i<foo.length; i++){
      if((foo[i]&0xff00)==0){
        bar[j++]=(byte)foo[i];
      }
      else{
        bar[j++]=(byte)(foo[i]>>>8);
        bar[j++]=(byte)foo[i];
      }
    }
    return bar;
  }
  */
  static void bzero(byte[] foo){
    if(foo==null)
      return;
    for(int i=0; i<foo.length; i++)
      foo[i]=0;
  }

  static String diffString(String str, String[] not_available){
    String[] stra=Util.split(str, ",");
    String result=null;
    loop:
    for(int i=0; i<stra.length; i++){
      for(int j=0; j<not_available.length; j++){
        if(stra[i].equals(not_available[j])){
          continue loop;
        }
      }
      if(result==null){ result=stra[i]; }
      else{ result=result+","+stra[i]; }
    }
    return result;
  }

  static String checkTilde(String str){
    try{
      if(str.startsWith("~")){
        str = str.replace("~", System.getProperty("user.home"));
      }
    }
    catch(SecurityException e){
    }
    return str;
  }

  private static int skipUTF8Char(byte b){
    if((byte)(b&0x80)==0) return 1;
    if((byte)(b&0xe0)==(byte)0xc0) return 2;
    if((byte)(b&0xf0)==(byte)0xe0) return 3;
    return 1;
  }

  static byte[] fromFile(String _file) throws IOException {
    _file = checkTilde(_file);
    File file = new File(_file);
    FileInputStream fis = new FileInputStream(_file);
    try {
      byte[] result = new byte[(int)(file.length())];
      int len=0;
      while(true){
        int i=fis.read(result, len, result.length-len);
        if(i<=0)
          break;
        len+=i;
      }
      fis.close();
      return result;
    }
    finally {
      if(fis!=null)
        fis.close();
    }
  }
}


class Random2 implements Random{
  private byte[] tmp=new byte[16];
  private SecureRandom random=null;
  public Random2(){

    // We hope that 'new SecureRandom()' will use NativePRNG algorithm
    // on Sun's Java5 for GNU/Linux and Solaris.
    // It seems NativePRNG refers to /dev/urandom and it must not be blocked,
    // but NativePRNG is slower than SHA1PRNG ;-<
    // TIPS: By adding option '-Djava.security.egd=file:/dev/./urandom'
    //       SHA1PRNG will be used instead of NativePRNG.
    // On MacOSX, 'new SecureRandom()' will use NativePRNG algorithm and
    // it is also slower than SHA1PRNG.
    // On Windows, 'new SecureRandom()' will use SHA1PRNG algorithm.
    random=new SecureRandom();

    /*
    try{ 
      random=SecureRandom.getInstance("SHA1PRNG"); 
      return;
    }
    catch(java.security.NoSuchAlgorithmException e){ 
      // System.err.println(e); 
    }

    // The following code is for IBM's JCE
    try{ 
      random=SecureRandom.getInstance("IBMSecureRandom"); 
      return;
    }
    catch(java.security.NoSuchAlgorithmException ee){ 
      //System.err.println(ee); 
    }
    */
  }
  public void fill(byte[] foo, int start, int len){
    /*
    // This case will not become true in our usage.
    if(start==0 && foo.length==len){
      random.nextBytes(foo);
      return;
    }
    */
    if(len>tmp.length){ tmp=new byte[len]; }
    random.nextBytes(tmp);
    System.arraycopy(tmp, 0, foo, start, len);
  }
}
