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
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;



//// inicio base 64 java 8 ou superior
// import java.util.Base64;
//// fim base 64 java 8

//// inicio - class Base64 - java 6 ou superior
//// teste ok(compila) para os javas 6 8 9 11 16
 /* class Base64 - java 6 */ class Base64 {      
 /* class Base64 - java 6 */ private Base64() {}        public static Encoder getEncoder() {          return Encoder.RFC4648;     }        public static Encoder getUrlEncoder() {          return Encoder.RFC4648_URLSAFE;     }  public static Encoder getMimeEncoder() {         return Encoder.RFC2045;     }        public static Decoder getDecoder() {          return Decoder.RFC4648;     }        public static Decoder getUrlDecoder() {          return Decoder.RFC4648_URLSAFE;     }        public static Decoder getMimeDecoder() {          return Decoder.RFC2045;     }        public static class Encoder {          private final byte[] newline;         private final int linemax;         private final boolean isURL;         private final boolean doPadding;          private Encoder(boolean isURL, byte[] newline, int linemax, boolean doPadding) {             this.isURL = isURL;             this.newline = newline;             this.linemax = linemax;             this.doPadding = doPadding;         }           	  private static final char[] toBase64 = {             'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',             'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',             'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',             'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'         };            private static final char[] toBase64URL = {             'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',             'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',             'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',             'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'         };          private static final int MIMELINEMAX = 76;         private static final byte[] CRLF = new byte[] {'\r', '\n'};          static final Encoder RFC4648 = new Encoder(false, null, -1, true);         static final Encoder RFC4648_URLSAFE = new Encoder(true, null, -1, true);         static final Encoder RFC2045 = new Encoder(false, CRLF, MIMELINEMAX, true);          private final int outLength(int srclen) {             int len = 0;             if (doPadding) {                 len = 4 * ((srclen + 2) / 3);             } else {                 int n = srclen % 3;                 len = 4 * (srclen / 3) + (n == 0 ? 0 : n + 1);             }             if (linemax > 0)                                                   len += (len - 1) / linemax * newline.length;             return len;         }            public byte[] encode(byte[] src) {             int len = outLength(src.length);                       byte[] dst = new byte[len];             int ret = encode0(src, 0, src.length, dst);             if (ret != dst.length)                  return Arrays.copyOf(dst, ret);             return dst;         }            public int encode(byte[] src, byte[] dst) { 	int len = outLength(src.length);                      if (dst.length < len)                 throw new IllegalArgumentException(                     "Output byte array is too small for encoding all input bytes");             return encode0(src, 0, src.length, dst);         }            @SuppressWarnings("deprecation")         public String encodeToString(byte[] src) {             byte[] encoded = encode(src);             return new String(encoded, 0, 0, encoded.length);         }            public ByteBuffer encode(ByteBuffer buffer) {             int len = outLength(buffer.remaining());             byte[] dst = new byte[len];             int ret = 0;             if (buffer.hasArray()) {ret = encode0(buffer.array(),buffer.arrayOffset() + buffer.position(),
 /* class Base64 - java 6 */ buffer.arrayOffset() + buffer.limit(),                               dst);                 buffer.position(buffer.limit());             } else {                 byte[] src = new byte[buffer.remaining()];                 buffer.get(src);                 ret = encode0(src, 0, src.length, dst);             }             if (ret != dst.length)                  dst = Arrays.copyOf(dst, ret);             return ByteBuffer.wrap(dst);         }            public OutputStream wrap(OutputStream os) {             Objects.requireNonNull(os);             return new EncOutputStream(os, isURL ? toBase64URL : toBase64,                                        newline, linemax, doPadding);         }            	  public Encoder withoutPadding() {             if (!doPadding)                 return this;             return new Encoder(isURL, newline, linemax, false);         }          private int encode0(byte[] src, int off, int end, byte[] dst) {             char[] base64 = isURL ? toBase64URL : toBase64;             int sp = off;             int slen = (end - off) / 3 * 3;             int sl = off + slen;             if (linemax > 0 && slen  > linemax / 4 * 3)                 slen = linemax / 4 * 3;             int dp = 0;             while (sp < sl) {                 int sl0 = Math.min(sp + slen, sl);                 for (int sp0 = sp, dp0 = dp ; sp0 < sl0; ) {                     int bits = (src[sp0++] & 0xff) << 16 |                                (src[sp0++] & 0xff) <<  8 |                                (src[sp0++] & 0xff);                     dst[dp0++] = (byte)base64[(bits >>> 18) & 0x3f];                     dst[dp0++] = (byte)base64[(bits >>> 12) & 0x3f];                     dst[dp0++] = (byte)base64[(bits >>> 6)  & 0x3f];                     dst[dp0++] = (byte)base64[bits & 0x3f];                 }                 int dlen = (sl0 - sp) / 3 * 4;                 dp += dlen;                 sp = sl0;                 if (dlen == linemax && sp < end) {                     for (byte b : newline){                         dst[dp++] = b;                     }                 }             }             if (sp < end) {                                int b0 = src[sp++] & 0xff;                 dst[dp++] = (byte)base64[b0 >> 2];                 if (sp == end) {                     dst[dp++] = (byte)base64[(b0 << 4) & 0x3f];                     if (doPadding) {                         dst[dp++] = '=';                         dst[dp++] = '=';                     }                 } else {                     int b1 = src[sp++] & 0xff;                     dst[dp++] = (byte)base64[(b0 << 4) & 0x3f | (b1 >> 4)];                     dst[dp++] = (byte)base64[(b1 << 2) & 0x3f];                     if (doPadding) {                         dst[dp++] = '=';                     }                 }             }             return dp;         }     }               public static class Decoder {          private final boolean isURL;         private final boolean isMIME;          private Decoder(boolean isURL, boolean isMIME) {             this.isURL = isURL;             this.isMIME = isMIME;         }                   private static final int[] fromBase64 = new int[256];         static {             Arrays.fill(fromBase64, -1);             for (int i = 0; i < Encoder.toBase64.length; i++)                 fromBase64[Encoder.toBase64[i]] = i;             fromBase64['='] = -2;         }                   private static final int[] fromBase64URL = new int[256];          static {             Arrays.fill(fromBase64URL, -1);             for (int i = 0; i < Encoder.toBase64URL.length; i++)                 fromBase64URL[Encoder.toBase64URL[i]] = i;             fromBase64URL['='] = -2;         }          static final Decoder RFC4648         = new Decoder(false, false);         
 /* class Base64 - java 6 */ static final Decoder RFC4648_URLSAFE = new Decoder(true, false);         static final Decoder RFC2045         = new Decoder(false, true);                   public byte[] decode(byte[] src) {             byte[] dst = new byte[outLength(src, 0, src.length)];             int ret = decode0(src, 0, src.length, dst);             if (ret != dst.length) {                 dst = Arrays.copyOf(dst, ret);             }             return dst;         }                   public byte[] decode(String src) {             return decode(src.getBytes(StandardCharsets.ISO_8859_1));         }                   public int decode(byte[] src, byte[] dst) {             int len = outLength(src, 0, src.length);             if (dst.length < len)                 throw new IllegalArgumentException(                     "Output byte array is too small for decoding all input bytes");             return decode0(src, 0, src.length, dst);         }                   public ByteBuffer decode(ByteBuffer buffer) {             int pos0 = buffer.position();             try {                 byte[] src;                 int sp, sl;                 if (buffer.hasArray()) {                     src = buffer.array();                     sp = buffer.arrayOffset() + buffer.position();                     sl = buffer.arrayOffset() + buffer.limit();                     buffer.position(buffer.limit());                 } else {                     src = new byte[buffer.remaining()];                     buffer.get(src);                     sp = 0;                     sl = src.length;                 }                 byte[] dst = new byte[outLength(src, sp, sl)];                 return ByteBuffer.wrap(dst, 0, decode0(src, sp, sl, dst));             } catch (IllegalArgumentException iae) {                 buffer.position(pos0);                 throw iae;             }         }                    public InputStream wrap(InputStream is) {             Objects.requireNonNull(is);             return new DecInputStream(is, isURL ? fromBase64URL : fromBase64, isMIME);         }          private int outLength(byte[] src, int sp, int sl) {             int[] base64 = isURL ? fromBase64URL : fromBase64;             int paddings = 0;             int len = sl - sp;             if (len == 0)                 return 0;             if (len < 2) {                 if (isMIME && base64[0] == -1)                     return 0;                 throw new IllegalArgumentException(                     "Input byte[] should at least have 2 bytes for base64 bytes");             }             if (isMIME) {                                                   int n = 0;                 while (sp < sl) {                     int b = src[sp++] & 0xff;                     if (b == '=') {                         len -= (sl - sp + 1);                         break;                     }                     if ((b = base64[b]) == -1)                         n++;                 }                 len -= n;             } else {                 if (src[sl - 1] == '=') {                     paddings++;                     if (src[sl - 2] == '=')                         paddings++;                 }             }             if (paddings == 0 && (len & 0x3) !=  0)                 paddings = 4 - (len & 0x3);             return 3 * ((len + 3) / 4) - paddings;         }          private int decode0(byte[] src, int sp, int sl, byte[] dst) {             int[] base64 = isURL ? fromBase64URL : fromBase64;             int dp = 0;             int bits = 0;             int shiftto = 18;                    while (sp < sl) {                 int b = src[sp++] & 0xff; if ((b = base64[b]) < 0) { if (b == -2) { if (shiftto == 6 && (sp == sl || src[sp++] != '=') || shiftto == 18) { 
 /* class Base64 - java 6 */ throw new IllegalArgumentException(                                 "Input byte array has wrong 4-byte ending unit");                         }                         break;                     }                     if (isMIME)                             continue;                     else                         throw new IllegalArgumentException(                             "Illegal base64 character " +                             Integer.toString(src[sp - 1], 16));                 }                 bits |= (b << shiftto);                 shiftto -= 6;                 if (shiftto < 0) {                     dst[dp++] = (byte)(bits >> 16);                     dst[dp++] = (byte)(bits >>  8);                     dst[dp++] = (byte)(bits);                     shiftto = 18;                     bits = 0;                 }             }                          if (shiftto == 6) {                 dst[dp++] = (byte)(bits >> 16);             } else if (shiftto == 0) {                 dst[dp++] = (byte)(bits >> 16); 	dst[dp++] = (byte)(bits >>  8);             } else if (shiftto == 12) {                                  throw new IllegalArgumentException(                     "Last unit does not have enough valid bits");             }                                       while (sp < sl) {                 if (isMIME && base64[src[sp++]] < 0)                     continue;                 throw new IllegalArgumentException(                     "Input byte array has incorrect ending byte at " + sp);             }             return dp;         }     }               private static class EncOutputStream extends FilterOutputStream {          private int leftover = 0;         private int b0, b1, b2;         private boolean closed = false;          private final char[] base64;             private final byte[] newline;            private final int linemax;         private final boolean doPadding;         private int linepos = 0;          EncOutputStream(OutputStream os, char[] base64,                         byte[] newline, int linemax, boolean doPadding) {             super(os);             this.base64 = base64;             this.newline = newline;             this.linemax = linemax;             this.doPadding = doPadding;         }          @Override         public void write(int b) throws IOException {             byte[] buf = new byte[1];             buf[0] = (byte)(b & 0xff);             write(buf, 0, 1);         }          private void checkNewline() throws IOException {             if (linepos == linemax) {                 out.write(newline);                 linepos = 0;             }         }          @Override         public void write(byte[] b, int off, int len) throws IOException {             if (closed)                 throw new IOException("Stream is closed");             if (off < 0 || len < 0 || len > b.length - off)                 throw new ArrayIndexOutOfBoundsException();             if (len == 0)                 return;             if (leftover != 0) {                 if (leftover == 1) {                     b1 = b[off++] & 0xff;                     len--;                     if (len == 0) {                         leftover++;                         return;                     }                 }                 b2 = b[off++] & 0xff;                 len--;                 checkNewline();                 out.write(base64[b0 >> 2]);                 out.write(base64[(b0 << 4) & 0x3f | (b1 >> 4)]);                 out.write(base64[(b1 << 2) & 0x3f | (b2 >> 6)]);                 out.write(base64[b2 & 0x3f]);                 linepos += 4;             }             int nBits24 = len / 3;             leftover = len - (nBits24 * 3);             while (nBits24-- > 0) {                 checkNewline();                 
 /* class Base64 - java 6 */ int bits = (b[off++] & 0xff) << 16 |                            (b[off++] & 0xff) <<  8 |                            (b[off++] & 0xff);                 out.write(base64[(bits >>> 18) & 0x3f]);                 out.write(base64[(bits >>> 12) & 0x3f]);                 out.write(base64[(bits >>> 6)  & 0x3f]);                 out.write(base64[bits & 0x3f]);                 linepos += 4;            }             if (leftover == 1) {                 b0 = b[off++] & 0xff;             } else if (leftover == 2) {                 b0 = b[off++] & 0xff;                 b1 = b[off++] & 0xff;             }         }          @Override         public void close() throws IOException {             if (!closed) { 	closed = true;                 if (leftover == 1) {                     checkNewline();                     out.write(base64[b0 >> 2]);                     out.write(base64[(b0 << 4) & 0x3f]);                     if (doPadding) {                         out.write('=');                         out.write('=');                     }                 } else if (leftover == 2) {                     checkNewline();                     out.write(base64[b0 >> 2]);                     out.write(base64[(b0 << 4) & 0x3f | (b1 >> 4)]);                     out.write(base64[(b1 << 2) & 0x3f]);                     if (doPadding) {                        out.write('=');                     }                 }                 leftover = 0;                 out.close();             }         }     }               private static class DecInputStream extends InputStream {          private final InputStream is;         private final boolean isMIME;         private final int[] base64;               private int bits = 0;                     private int nextin = 18;                                                            private int nextout = -8;                                                           private boolean eof = false;         private boolean closed = false;          DecInputStream(InputStream is, int[] base64, boolean isMIME) {             this.is = is;             this.base64 = base64;             this.isMIME = isMIME;         }          private byte[] sbBuf = new byte[1];          @Override         public int read() throws IOException {             return read(sbBuf, 0, 1) == -1 ? -1 : sbBuf[0] & 0xff;         }          @Override         public int read(byte[] b, int off, int len) throws IOException {             if (closed)                 throw new IOException("Stream is closed");             if (eof && nextout < 0)                     return -1;             if (off < 0 || len < 0 || len > b.length - off)                 throw new IndexOutOfBoundsException();             int oldOff = off;             if (nextout >= 0) {                        do {                     if (len == 0)                         return off - oldOff;                     b[off++] = (byte)(bits >> nextout);                     len--;                     nextout -= 8;                 } while (nextout >= 0);                 bits = 0;             }             while (len > 0) {                 int v = is.read();                 if (v == -1) {                     eof = true;                     if (nextin != 18) {                         if (nextin == 12)                             throw new IOException("Base64 stream has one un-decoded dangling byte.");                                                                           b[off++] = (byte)(bits >> (16));                         len--;                         if (nextin == 0) {                                        if (len == 0) {                                           bits >>= 8;                                           nextout = 0; } else {
 /* class Base64 - java 6 */ b[off++] = (byte) (bits >>  8); } } } if (off == oldOff) return -1; else return off - oldOff; } if (v == '=') { if (nextin == 18 || nextin == 12 ||                         nextin == 6 && is.read() != '=') {                         throw new IOException("Illegal base64 ending sequence:" + nextin);                     }                     b[off++] = (byte)(bits >> (16));                     len--;                     if (nextin == 0) {                                    if (len == 0) {                                       bits >>= 8;                                       nextout = 0;                         } else {                             b[off++] = (byte) (bits >>  8);                         }                     }                     eof = true;                     break;                 }                 if ((v = base64[v]) == -1) {                     if (isMIME)                                          continue;                     else                         throw new IOException("Illegal base64 character " +                             Integer.toString(v, 16));                 }                 bits |= (v << nextin);                 if (nextin == 0) {                     nextin = 18;                         nextout = 16;                     while (nextout >= 0) {                         b[off++] = (byte)(bits >> nextout);                         len--;                         nextout -= 8;                         if (len == 0 && nextout >= 0) {                               return off - oldOff;                         }                     }                     bits = 0;                 } else {                     nextin -= 6;                 }             }             return off - oldOff;         }          @Override         public int available() throws IOException {             if (closed)                 throw new IOException("Stream is closed");             return is.available();            }          @Override         public void close() throws IOException {             if (!closed) {                 closed = true;                 is.close();             }         }     } }  final class StandardCharsets {      private StandardCharsets() {         throw new AssertionError("No java.nio.charset.StandardCharsets instances for you!");     }              public static final Charset US_ASCII = Charset.forName("US-ASCII");              public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");             public static final Charset UTF_8 = Charset.forName("UTF-8");              public static final Charset UTF_16BE = Charset.forName("UTF-16BE");              public static final Charset UTF_16LE = Charset.forName("UTF-16LE");              public static final Charset UTF_16 = Charset.forName("UTF-16"); }  final class Objects {     private Objects() {         throw new AssertionError("No java.util.Objects instances for you!");     }               public static boolean equals(Object a, Object b) {         return (a == b) || (a != null && a.equals(b));     }              public static boolean deepEquals(Object a, Object b) {         if (a == b)             return true;         else if (a == null || b == null)             return false;         else{                          System.out.println("Erro, erro na traduzação do java 6");             System.err.println("Erro, erro na traduzação do java 6");             return false;         }              }               public static int hashCode(Object o) {         return o != null ? o.hashCode() : 0;     }              public static int hash(Object... values) {         return Arrays.hashCode(values);     }       	public static String toString(Object o) {         return String.valueOf(o);
 /* class Base64 - java 6 */ }               public static String toString(Object o, String nullDefault) {         return (o != null) ? o.toString() : nullDefault;     }               public static <T> int compare(T a, T b, Comparator<? super T> c) {         return (a == b) ? 0 :  c.compare(a, b);     }               public static <T> T requireNonNull(T obj) {         if (obj == null)             throw new NullPointerException();         return obj;     }               public static <T> T requireNonNull(T obj, String message) {         if (obj == null)             throw new NullPointerException(message);         return obj;     }               public static boolean isNull(Object obj) {         return obj == null;     }                public static boolean nonNull(Object obj) {         return obj != null;     }               public static <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {         if (obj == null)             throw new NullPointerException(messageSupplier.get());         return obj;     } }  interface Supplier<T> {                  T get(); } 
//// fim - class Base64 - java 6

public class Y {    
    //public static String local_env=null;
    public static String local_env="c:\\tmp";

    public static int BUFFER_SIZE=1024;
    public static java.util.Scanner scanner_pipe=null;
    public static java.util.Scanner scanner_pipeB=null;
    public static InputStream inputStream_pipe=null;
    public static String linhaCSV=null;
    public static int ponteiroLinhaCSV=0;    
    public static String sepCSV=";";
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
                    createjobexecute(conn);
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
                    createjobcarga(connIn,fileCSV,connOut,outTable,trunc,app);
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

        if ( args[0].equals("base64") 
            && ( 
                args.length == 1 
                || ( args.length == 2 && args[1].equals("-d") )
            )    
        ){
            if ( args.length == 1 )
                System.out.println(
                    base64(System.in,true)
                );
            else
                System.out.print(
                    base64(System.in,false)
                );
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
        if ( args[0].equals("sed") && args.length >= 3 && args.length % 2 == 1 ){
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
        
        String sep=System.getenv("CSV_SEP_Y");
        if ( sep == null || sep.trim().equals("") )
            sep=";";
        
        int countCommit=0;
        try{
            if ( ! fileCSV.equals("") )
                readLine(fileCSV);
            String line;
            String [] camposCSV=null;
            int qntCamposCSV=0;
            String valorColuna=null;
            StringBuilder sb=null;
            
            readColunaCSV(new String[]{sep});// init parmCSV sep, usando a assinatura array String para diferenciar de String
            
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
        
        String sep=System.getenv("CSV_SEP_Y");
        if ( sep == null || sep.trim().equals("") )
            sep=";";
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
                            header+="\""+campos.get(i)+"\""+sep;
                            first_detail+="\"\""+sep;
                        }else{
                            sb.append("\"\"");
                            sb.append(sep);
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
                            header+="\""+campos.get(i)+"\""+sep;
                            first_detail+="\""+tmp+"\""+sep;
                        }else{
                            // nao imprime delimitador em onlychar e tipos.get(i) == 2
                            if ( !onlychar || tipos.get(i) != 2 )
                                sb.append("\"");
                            sb.append(tmp);
                            if ( !onlychar || tipos.get(i) != 2 )
                                sb.append("\"");
                            sb.append(sep);
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
        return lpad(inputLong+"",length,append);
    }
    
    public String lpad(String inputString, int length,String append) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(append);
        }
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

    public void readLine(String caminho) throws Exception{
        readLine(new FileInputStream(new File(caminho)));
    }
    
    public void readLine(InputStream in){        
        scanner_pipe=new java.util.Scanner(in);
        scanner_pipe.useDelimiter("\n");
    }    
    
    public String readLine(){
        try{            
            if ( scanner_pipe == null )
                readLine(System.in);
            if ( scanner_pipe.hasNext() )
                return scanner_pipe.next();                        
            else
                return null;            
        }catch(java.util.NoSuchElementException no) {
            return null;
        }catch(Exception e){
            System.err.println("NOK: "+e.toString());
        }
        return null;
    }

    public void closeLine(){
        try{
            scanner_pipe.close();            
        }catch(Exception e){}
        scanner_pipe=null;
    }
    
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
            while ( (line=readLine()) != null ) {
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
                if ( first && tail && line.startsWith(grep) && line.endsWith(grep) ){
                    System.out.println(line);
                    continue;
                }                
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
         
    public void sed(String [] args)
    {
        try {
            String line=null;
            while ( (line=readLine()) != null ){
                for ( int i=1;i<args.length;i+=2 )
                    line=line.replaceAll(args[i], args[i+1]);
                System.out.println(line);
            }
            closeLine();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    int BARRA_R=13; // \r
    int BARRA_N=10; // \n
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

    private void iconvOLD(String tipoOrigem, String tipoDestino, String caminho) {
        if ( tipoOrigem.equals("ISO-8859-1") && tipoDestino.equals("UTF-8") ){
            iconvWindowsToUTF8(caminho);           
            return;
        }
        if ( tipoOrigem.equals("UTF-8") && tipoDestino.equals("ISO-8859-1") ){
            iconvUTF8ToWindows(caminho);
            return;
        }
        System.out.println("Erro, encode nao suportado: "+tipoDestino+"/"+tipoOrigem);
        System.exit(1);
    }
    
    private void iconvUTF8ToWindows(String caminho) {
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
            out.close();
            closeBytes();
        }catch(Exception e){
            System.err.println("Erro, "+e.toString());
        }
    }
        
    public void awk_print(String [] args)
    {
        ArrayList<Integer> lista=new ArrayList<Integer>();
        int [] elem;
        String [] partes;
        int p;
        
        try{
            for ( int i=2;i<args.length;i++ ){
                partes=args[i].split(",");
                for ( int j=0;j<partes.length;j++ ){
                    if ( j != 0 )
                        lista.add(-1);
                    p=Integer.parseInt(partes[j]);
                    if ( p < 0 ){
                        comando_invalido(args);
                        return;
                    }                    
                    lista.add(p);
                }
            }
        }catch(Exception e){
            comando_invalido(args);
            return;
        }
        
        elem=new int[lista.size()];
        for ( int i=0;i<lista.size();i++ )
            elem[i]=lista.get(i);
        
        try {
            String line=null;
            while ( (line=readLine()) != null ) {            
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
                    if ( elem[i] == -1 )
                    {
                        System.out.print(" ");
                        continue;
                    }
                    if ( elem[i] > partes.length ) continue;
                    System.out.print(partes[elem[i]-1]);
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

    public String base64(InputStream pipe,boolean encoding){
        // java 11 depreciated sun.misc.BASE64Encoder
        // tem que usar esse codigo zuado mesmo
        int BUFFER_SIZE_ = 1;
        byte[] buf = new byte[BUFFER_SIZE_];                   
        ArrayList<Byte> lista=new ArrayList<Byte>();
        byte [] bytes=null;
        
        try {
            while( pipe.read(buf, 0, BUFFER_SIZE_) > -1 ){
                if ( encoding 
                    || ( (int)buf[0] != 10 && (int)buf[0] != 13 ) // remove \r and \n
                ){
                    lista.add(buf[0]);
                }
            }
            bytes=new byte[lista.size()];
            for ( int i=0;i<lista.size();i++ )
                bytes[i]=lista.get(i);
            if ( encoding )
                return new String( Base64.getEncoder().encode(bytes) );
            else
                return new String( Base64.getDecoder().decode(bytes) );
        } catch (Exception ex) {
            System.err.println("Erro, "+ex.toString());
            System.err.println(new String(bytes));
        }
        return null;
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

    public void createjobexecute(String conn) {
        String line;
        String SQL="";
        while ( (line=readLine()) != null )
            SQL+=line+"\n";
        closeLine();
        
        System.out.print("jobexecute "); // funciona como orientador, não tem função prática
        System.out.println( 
            base64(
                new ByteArrayInputStream(
                    (
                        "jobexecute\n"
                        + "-conn\n"
                        + conn+"\n"
                        + "SQL\n"
                        + SQL
                    ).getBytes()
                )
                ,true
            )
        );
    }

    public void createjobcarga(String connIn, String fileCSV, String connOut, String outTable, String trunc, String app) {
        String line;
        String SQL="";
        while ( (line=readLine()) != null )
            SQL+=line+"\n";
        closeLine();
        
        System.out.print("jobcarga "+outTable+" "); // funciona como orientador, não tem função prática
        System.out.println(
            base64(
                new ByteArrayInputStream(
                    (
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
                    ).getBytes()
                )
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

                    value_=base64(
                        new ByteArrayInputStream(
                            hash.getBytes()
                        )
                        ,false
                    );

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

    
    private String [] getCamposCSV(String txt) {
        // modelos
        // HEADER_CAMPO1;BB;CC;3;4;5;
        // HEADER_CAMPO1;BB;CC;3;4;5
        
        String sep=System.getenv("CSV_SEP_Y");
        if ( sep == null || sep.trim().equals("") )
            sep=";";
        
        txt=txt.trim();
        if ( txt.endsWith(sep) )
            txt=txt.substring(0, txt.length()-1);
        return txt.replace("\"","").split( sep.equals("|")?"\\|":sep ); // split nao funciona com |, tem que usar \\|
    }

    private void readColunaCSV(String [] parm) {
        sepCSV=parm[0];
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
            return readColunaCSVComplexa().replace("\r","").replace("\n","");
        }else{
            return readColunaCSVSimples().replace("\r","").replace("\n","");
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
        return linhaCSV.substring(ini,fim);
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
        return linhaCSV.substring(ini,fim);
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
                    java.io.Console console=console=System.console();
                    if ( console == null ){
                        System.out.println("Error, input não suportado nesse ambiente, rodando no netbeans?...");
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
/* class by manual */                + "  [y token]\n"
/* class by manual */                + "  [y gettoken]\n"
/* class by manual */                + "  [y gzip]\n"
/* class by manual */                + "  [y gunzip]\n"
/* class by manual */                + "  [y echo]\n"
/* class by manual */                + "  [y cat]\n"
/* class by manual */                + "  [y md5]\n"
/* class by manual */                + "  [y sha1]\n"
/* class by manual */                + "  [y sha256]\n"
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
/* class by manual */                + "  [y awk print]\n"
/* class by manual */                + "  [y dev_null]\n"
/* class by manual */                + "  [y dev_in]\n"
/* class by manual */                + "  [y scp]\n"
/* class by manual */                + "  [y execSsh]\n"
/* class by manual */                + "  [y ssh]\n"
/* class by manual */                + "  [y sftp]\n"
/* class by manual */                + "  [y serverRouter]\n"
/* class by manual */                + "  [y HttpServer]\n"
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
/* class by manual */                + "[y token]\n"
/* class by manual */                + "    y token value\n"
/* class by manual */                + "[y gettoken]\n"
/* class by manual */                + "    y gettoken hash\n"
/* class by manual */                + "[y gzip]\n"
/* class by manual */                + "    cat arquivo | y gzip > arquivo.gz\n"
/* class by manual */                + "[y gunzip]\n"
/* class by manual */                + "    cat arquivo.gz | y gunzip > arquivo\n"
/* class by manual */                + "[y echo]\n"
/* class by manual */                + "    echo a b c\n"
/* class by manual */                + "    echo \"a b c\"\n"
/* class by manual */                + "[y cat]\n"
/* class by manual */                + "    y cat arquivo\n"
/* class by manual */                + "[y md5]\n"
/* class by manual */                + "    cat arquivo | y md5\n"
/* class by manual */                + "[y sha1]\n"
/* class by manual */                + "    cat arquivo | y sha1\n"
/* class by manual */                + "[y sha256]\n"
/* class by manual */                + "    cat arquivo | y sha256\n"
/* class by manual */                + "[y base64]\n"
/* class by manual */                + "    cat arquivo | y base64\n"
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
/* class by manual */                + "    cat arquivo | y sed A B\n"
/* class by manual */                + "    cat arquivo | y sed A1 A2 B1 B2\n"
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
/* class by manual */                + "[y awk]\n"
/* class by manual */                + "    cat arquivo | y awk print 1 3 5,6\n"
/* class by manual */                + "    cat arquivo | y awk start AAA end BBB    \n"
/* class by manual */                + "    cat arquivo | y awk start AAA\n"
/* class by manual */                + "    cat arquivo | y awk end BBB    \n"
/* class by manual */                + "    cat arquivo | y awk -v start AAA end BBB    \n"
/* class by manual */                + "    cat arquivo | y awk -v start AAA\n"
/* class by manual */                + "    cat arquivo | y awk -v end BBB    \n"
/* class by manual */                + "    obs: \"-v\" e a negativa\n"
/* class by manual */                + "    obs2: start e end pode ocorrer varias vezes no texto\n"
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
/* class by manual */                + "  [y token]\n"
/* class by manual */                + "  [y gettoken]\n"
/* class by manual */                + "  [y gzip]\n"
/* class by manual */                + "  [y gunzip]\n"
/* class by manual */                + "  [y echo]\n"
/* class by manual */                + "  [y cat]\n"
/* class by manual */                + "  [y md5]\n"
/* class by manual */                + "  [y sha1]\n"
/* class by manual */                + "  [y sha256]\n"
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


