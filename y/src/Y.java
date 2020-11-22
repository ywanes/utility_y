
//
// https://github.com/ywanes/utility_y/blob/master/y/src/Y.java
//

import ComplementoJsch.Exec;
import ComplementoJsch.ScpFrom;
import ComplementoJsch.ScpTo;
import ComplementoJsch.Sftp;
import ComplementoJsch.Shell;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
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

// java 8
import java.util.Base64;
import java.util.Random;

// copia da classe Base64 do java8 para rodar no java6
//class Base64 {      private Base64() {}      /**      * Returns a {@link Encoder} that encodes using the      * <a href="#basic">Basic</a> type base64 encoding scheme.      *      * @return  A Base64 encoder.      */     public static Encoder getEncoder() {          return Encoder.RFC4648;     }      /**      * Returns a {@link Encoder} that encodes using the      * <a href="#url">URL and Filename safe</a> type base64      * encoding scheme.      *      * @return  A Base64 encoder.      */     public static Encoder getUrlEncoder() {          return Encoder.RFC4648_URLSAFE;     }      /**      * Returns a {@link Encoder} that encodes using the      * <a href="#mime">MIME</a> type base64 encoding scheme.      *      * @return  A Base64 encoder.      */     public static Encoder getMimeEncoder() {         return Encoder.RFC2045;     }      /**      * Returns a {@link Encoder} that encodes using the      * <a href="#mime">MIME</a> type base64 encoding scheme      * with specified line length and line separators.      *      * @param   lineLength      *          the length of each output line (rounded down to nearest multiple      *          of 4). If {@code lineLength <= 0} the output will not be separated      *          in lines      * @param   lineSeparator      *          the line separator for each output line      *      * @return  A Base64 encoder.      *      * @throws  IllegalArgumentException if {@code lineSeparator} includes any      *          character of "The Base64 Alphabet" as specified in Table 1 of      *          RFC 2045.      */     public static Encoder getMimeEncoder(int lineLength, byte[] lineSeparator) {          Objects.requireNonNull(lineSeparator);          int[] base64 = Decoder.fromBase64;          for (byte b : lineSeparator) {              if (base64[b & 0xff] != -1)                  throw new IllegalArgumentException(                      "Illegal base64 line separator character 0x" + Integer.toString(b, 16));          }          if (lineLength <= 0) {              return Encoder.RFC4648;          }          return new Encoder(false, lineSeparator, lineLength >> 2 << 2, true);     }      /**      * Returns a {@link Decoder} that decodes using the      * <a href="#basic">Basic</a> type base64 encoding scheme.      *      * @return  A Base64 decoder.      */     public static Decoder getDecoder() {          return Decoder.RFC4648;     }      /**      * Returns a {@link Decoder} that decodes using the      * <a href="#url">URL and Filename safe</a> type base64      * encoding scheme.      *      * @return  A Base64 decoder.      */     public static Decoder getUrlDecoder() {          return Decoder.RFC4648_URLSAFE;     }      /**      * Returns a {@link Decoder} that decodes using the      * <a href="#mime">MIME</a> type base64 decoding scheme.      *      * @return  A Base64 decoder.      */     public static Decoder getMimeDecoder() {          return Decoder.RFC2045;     }      /**      * This class implements an encoder for encoding byte data using      * the Base64 encoding scheme as specified in RFC 4648 and RFC 2045.      *      * <p> Instances of {@link Encoder} class are safe for use by      * multiple concurrent threads.      *      * <p> Unless otherwise noted, passing a {@code null} argument to      * a method of this class will cause a      * {@link java.lang.NullPointerException NullPointerException} to      * be thrown.      *      * @see     Decoder      * @since   1.8      */     public static class Encoder {          private final byte[] newline;         private final int linemax;         private final boolean isURL;         private final boolean doPadding;          private Encoder(boolean isURL, byte[] newline, int linemax, boolean doPadding) {             this.isURL = isURL;             this.newline = newline;             this.linemax = linemax;             this.doPadding = doPadding;         }          
//	/**          * This array is a lookup table that translates 6-bit positive integer          * index values into their "Base64 Alphabet" equivalents as specified          * in "Table 1: The Base64 Alphabet" of RFC 2045 (and RFC 4648).          */         private static final char[] toBase64 = {             'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',             'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',             'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',             'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'         };          /**          * It's the lookup table for "URL and Filename safe Base64" as specified          * in Table 2 of the RFC 4648, with the '+' and '/' changed to '-' and          * '_'. This table is used when BASE64_URL is specified.          */         private static final char[] toBase64URL = {             'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',             'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',             'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',             'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'         };          private static final int MIMELINEMAX = 76;         private static final byte[] CRLF = new byte[] {'\r', '\n'};          static final Encoder RFC4648 = new Encoder(false, null, -1, true);         static final Encoder RFC4648_URLSAFE = new Encoder(true, null, -1, true);         static final Encoder RFC2045 = new Encoder(false, CRLF, MIMELINEMAX, true);          private final int outLength(int srclen) {             int len = 0;             if (doPadding) {                 len = 4 * ((srclen + 2) / 3);             } else {                 int n = srclen % 3;                 len = 4 * (srclen / 3) + (n == 0 ? 0 : n + 1);             }             if (linemax > 0)                                                   len += (len - 1) / linemax * newline.length;             return len;         }          /**          * Encodes all bytes from the specified byte array into a newly-allocated          * byte array using the {@link Base64} encoding scheme. The returned byte          * array is of the length of the resulting bytes.          *          * @param   src          *          the byte array to encode          * @return  A newly-allocated byte array containing the resulting          *          encoded bytes.          */         public byte[] encode(byte[] src) {             int len = outLength(src.length);                       byte[] dst = new byte[len];             int ret = encode0(src, 0, src.length, dst);             if (ret != dst.length)                  return Arrays.copyOf(dst, ret);             return dst;         }          /**          * Encodes all bytes from the specified byte array using the          * {@link Base64} encoding scheme, writing the resulting bytes to the          * given output byte array, starting at offset 0.          *          * <p> It is the responsibility of the invoker of this method to make          * sure the output byte array {@code dst} has enough space for encoding          * all bytes from the input byte array. No bytes will be written to the          * output byte array if the output byte array is not big enough.          *          * @param   src          *          the byte array to encode          * @param   dst          *          the output byte array          * @return  The number of bytes written to the output byte array          *          * @throws  IllegalArgumentException if {@code dst} does not have enough          *          space for encoding all input bytes.          */         public int encode(byte[] src, byte[] dst) {
//	int len = outLength(src.length);                      if (dst.length < len)                 throw new IllegalArgumentException(                     "Output byte array is too small for encoding all input bytes");             return encode0(src, 0, src.length, dst);         }          /**          * Encodes the specified byte array into a String using the {@link Base64}          * encoding scheme.          *          * <p> This method first encodes all input bytes into a base64 encoded          * byte array and then constructs a new String by using the encoded byte          * array and the {@link java.nio.charset.StandardCharsets#ISO_8859_1          * ISO-8859-1} charset.          *          * <p> In other words, an invocation of this method has exactly the same          * effect as invoking          * {@code new String(encode(src), StandardCharsets.ISO_8859_1)}.          *          * @param   src          *          the byte array to encode          * @return  A String containing the resulting Base64 encoded characters          */         @SuppressWarnings("deprecation")         public String encodeToString(byte[] src) {             byte[] encoded = encode(src);             return new String(encoded, 0, 0, encoded.length);         }          /**          * Encodes all remaining bytes from the specified byte buffer into          * a newly-allocated ByteBuffer using the {@link Base64} encoding          * scheme.          *          * Upon return, the source buffer's position will be updated to          * its limit; its limit will not have been changed. The returned          * output buffer's position will be zero and its limit will be the          * number of resulting encoded bytes.          *          * @param   buffer          *          the source ByteBuffer to encode          * @return  A newly-allocated byte buffer containing the encoded bytes.          */         public ByteBuffer encode(ByteBuffer buffer) {             int len = outLength(buffer.remaining());             byte[] dst = new byte[len];             int ret = 0;             if (buffer.hasArray()) {                 ret = encode0(buffer.array(),                               buffer.arrayOffset() + buffer.position(),                               buffer.arrayOffset() + buffer.limit(),                               dst);                 buffer.position(buffer.limit());             } else {                 byte[] src = new byte[buffer.remaining()];                 buffer.get(src);                 ret = encode0(src, 0, src.length, dst);             }             if (ret != dst.length)                  dst = Arrays.copyOf(dst, ret);             return ByteBuffer.wrap(dst);         }          /**          * Wraps an output stream for encoding byte data using the {@link Base64}          * encoding scheme.          *          * <p> It is recommended to promptly close the returned output stream after          * use, during which it will flush all possible leftover bytes to the underlying          * output stream. Closing the returned output stream will close the underlying          * output stream.          *          * @param   os          *          the output stream.          * @return  the output stream for encoding the byte data into the          *          specified Base64 encoded format          */         public OutputStream wrap(OutputStream os) {             Objects.requireNonNull(os);             return new EncOutputStream(os, isURL ? toBase64URL : toBase64,                                        newline, linemax, doPadding);         }          
//	/**          * Returns an encoder instance that encodes equivalently to this one,          * but without adding any padding character at the end of the encoded          * byte data.          *          * <p> The encoding scheme of this encoder instance is unaffected by          * this invocation. The returned encoder instance should be used for          * non-padding encoding operation.          *          * @return an equivalent encoder that encodes without adding any          *         padding character at the end          */         public Encoder withoutPadding() {             if (!doPadding)                 return this;             return new Encoder(isURL, newline, linemax, false);         }          private int encode0(byte[] src, int off, int end, byte[] dst) {             char[] base64 = isURL ? toBase64URL : toBase64;             int sp = off;             int slen = (end - off) / 3 * 3;             int sl = off + slen;             if (linemax > 0 && slen  > linemax / 4 * 3)                 slen = linemax / 4 * 3;             int dp = 0;             while (sp < sl) {                 int sl0 = Math.min(sp + slen, sl);                 for (int sp0 = sp, dp0 = dp ; sp0 < sl0; ) {                     int bits = (src[sp0++] & 0xff) << 16 |                                (src[sp0++] & 0xff) <<  8 |                                (src[sp0++] & 0xff);                     dst[dp0++] = (byte)base64[(bits >>> 18) & 0x3f];                     dst[dp0++] = (byte)base64[(bits >>> 12) & 0x3f];                     dst[dp0++] = (byte)base64[(bits >>> 6)  & 0x3f];                     dst[dp0++] = (byte)base64[bits & 0x3f];                 }                 int dlen = (sl0 - sp) / 3 * 4;                 dp += dlen;                 sp = sl0;                 if (dlen == linemax && sp < end) {                     for (byte b : newline){                         dst[dp++] = b;                     }                 }             }             if (sp < end) {                                int b0 = src[sp++] & 0xff;                 dst[dp++] = (byte)base64[b0 >> 2];                 if (sp == end) {                     dst[dp++] = (byte)base64[(b0 << 4) & 0x3f];                     if (doPadding) {                         dst[dp++] = '=';                         dst[dp++] = '=';                     }                 } else {                     int b1 = src[sp++] & 0xff;                     dst[dp++] = (byte)base64[(b0 << 4) & 0x3f | (b1 >> 4)];                     dst[dp++] = (byte)base64[(b1 << 2) & 0x3f];                     if (doPadding) {                         dst[dp++] = '=';                     }                 }             }             return dp;         }     }      /**      * This class implements a decoder for decoding byte data using the      * Base64 encoding scheme as specified in RFC 4648 and RFC 2045.      *      * <p> The Base64 padding character {@code '='} is accepted and      * interpreted as the end of the encoded byte data, but is not      * required. So if the final unit of the encoded byte data only has      * two or three Base64 characters (without the corresponding padding      * character(s) padded), they are decoded as if followed by padding      * character(s). If there is a padding character present in the      * final unit, the correct number of padding character(s) must be      * present, otherwise {@code IllegalArgumentException} (      * {@code IOException} when reading from a Base64 stream) is thrown      * during decoding.      *      * <p> 
//	Instances of {@link Decoder} class are safe for use by      * multiple concurrent threads.      *      * <p> Unless otherwise noted, passing a {@code null} argument to      * a method of this class will cause a      * {@link java.lang.NullPointerException NullPointerException} to      * be thrown.      *      * @see     Encoder      * @since   1.8      */     public static class Decoder {          private final boolean isURL;         private final boolean isMIME;          private Decoder(boolean isURL, boolean isMIME) {             this.isURL = isURL;             this.isMIME = isMIME;         }          /**          * Lookup table for decoding unicode characters drawn from the          * "Base64 Alphabet" (as specified in Table 1 of RFC 2045) into          * their 6-bit positive integer equivalents.  Characters that          * are not in the Base64 alphabet but fall within the bounds of          * the array are encoded to -1.          *          */         private static final int[] fromBase64 = new int[256];         static {             Arrays.fill(fromBase64, -1);             for (int i = 0; i < Encoder.toBase64.length; i++)                 fromBase64[Encoder.toBase64[i]] = i;             fromBase64['='] = -2;         }          /**          * Lookup table for decoding "URL and Filename safe Base64 Alphabet"          * as specified in Table2 of the RFC 4648.          */         private static final int[] fromBase64URL = new int[256];          static {             Arrays.fill(fromBase64URL, -1);             for (int i = 0; i < Encoder.toBase64URL.length; i++)                 fromBase64URL[Encoder.toBase64URL[i]] = i;             fromBase64URL['='] = -2;         }          static final Decoder RFC4648         = new Decoder(false, false);         static final Decoder RFC4648_URLSAFE = new Decoder(true, false);         static final Decoder RFC2045         = new Decoder(false, true);          /**          * Decodes all bytes from the input byte array using the {@link Base64}          * encoding scheme, writing the results into a newly-allocated output          * byte array. The returned byte array is of the length of the resulting          * bytes.          *          * @param   src          *          the byte array to decode          *          * @return  A newly-allocated byte array containing the decoded bytes.          *          * @throws  IllegalArgumentException          *          if {@code src} is not in valid Base64 scheme          */         public byte[] decode(byte[] src) {             byte[] dst = new byte[outLength(src, 0, src.length)];             int ret = decode0(src, 0, src.length, dst);             if (ret != dst.length) {                 dst = Arrays.copyOf(dst, ret);             }             return dst;         }          /**          * Decodes a Base64 encoded String into a newly-allocated byte array          * using the {@link Base64} encoding scheme.          *          * <p> An invocation of this method has exactly the same effect as invoking          * {@code decode(src.getBytes(StandardCharsets.ISO_8859_1))}          *          * @param   src          *          the string to decode          *          * @return  A newly-allocated byte array containing the decoded bytes.          *          * @throws  IllegalArgumentException          *          if {@code src} is not in valid Base64 scheme          */         public byte[] decode(String src) {             return decode(src.getBytes(StandardCharsets.ISO_8859_1));         }          
//	/**          * Decodes all bytes from the input byte array using the {@link Base64}          * encoding scheme, writing the results into the given output byte array,          * starting at offset 0.          *          * <p> It is the responsibility of the invoker of this method to make          * sure the output byte array {@code dst} has enough space for decoding          * all bytes from the input byte array. No bytes will be be written to          * the output byte array if the output byte array is not big enough.          *          * <p> If the input byte array is not in valid Base64 encoding scheme          * then some bytes may have been written to the output byte array before          * IllegalargumentException is thrown.          *          * @param   src          *          the byte array to decode          * @param   dst          *          the output byte array          *          * @return  The number of bytes written to the output byte array          *          * @throws  IllegalArgumentException          *          if {@code src} is not in valid Base64 scheme, or {@code dst}          *          does not have enough space for decoding all input bytes.          */         public int decode(byte[] src, byte[] dst) {             int len = outLength(src, 0, src.length);             if (dst.length < len)                 throw new IllegalArgumentException(                     "Output byte array is too small for decoding all input bytes");             return decode0(src, 0, src.length, dst);         }          /**          * Decodes all bytes from the input byte buffer using the {@link Base64}          * encoding scheme, writing the results into a newly-allocated ByteBuffer.          *          * <p> Upon return, the source buffer's position will be updated to          * its limit; its limit will not have been changed. The returned          * output buffer's position will be zero and its limit will be the          * number of resulting decoded bytes          *          * <p> {@code IllegalArgumentException} is thrown if the input buffer          * is not in valid Base64 encoding scheme. The position of the input          * buffer will not be advanced in this case.          *          * @param   buffer          *          the ByteBuffer to decode          *          * @return  A newly-allocated byte buffer containing the decoded bytes          *          * @throws  IllegalArgumentException          *          if {@code src} is not in valid Base64 scheme.          */         public ByteBuffer decode(ByteBuffer buffer) {             int pos0 = buffer.position();             try {                 byte[] src;                 int sp, sl;                 if (buffer.hasArray()) {                     src = buffer.array();                     sp = buffer.arrayOffset() + buffer.position();                     sl = buffer.arrayOffset() + buffer.limit();                     buffer.position(buffer.limit());                 } else {                     src = new byte[buffer.remaining()];                     buffer.get(src);                     sp = 0;                     sl = src.length;                 }                 byte[] dst = new byte[outLength(src, sp, sl)];                 return ByteBuffer.wrap(dst, 0, decode0(src, sp, sl, dst));             } catch (IllegalArgumentException iae) {                 buffer.position(pos0);                 throw iae;             }         }          /**          * Returns an input stream for decoding {@link Base64} 
//	encoded byte stream.          *          * <p> The {@code read}  methods of the returned {@code InputStream} will          * throw {@code IOException} when reading bytes that cannot be decoded.          *          * <p> Closing the returned input stream will close the underlying          * input stream.          *          * @param   is          *          the input stream          *          * @return  the input stream for decoding the specified Base64 encoded          *          byte stream          */         public InputStream wrap(InputStream is) {             Objects.requireNonNull(is);             return new DecInputStream(is, isURL ? fromBase64URL : fromBase64, isMIME);         }          private int outLength(byte[] src, int sp, int sl) {             int[] base64 = isURL ? fromBase64URL : fromBase64;             int paddings = 0;             int len = sl - sp;             if (len == 0)                 return 0;             if (len < 2) {                 if (isMIME && base64[0] == -1)                     return 0;                 throw new IllegalArgumentException(                     "Input byte[] should at least have 2 bytes for base64 bytes");             }             if (isMIME) {                                                   int n = 0;                 while (sp < sl) {                     int b = src[sp++] & 0xff;                     if (b == '=') {                         len -= (sl - sp + 1);                         break;                     }                     if ((b = base64[b]) == -1)                         n++;                 }                 len -= n;             } else {                 if (src[sl - 1] == '=') {                     paddings++;                     if (src[sl - 2] == '=')                         paddings++;                 }             }             if (paddings == 0 && (len & 0x3) !=  0)                 paddings = 4 - (len & 0x3);             return 3 * ((len + 3) / 4) - paddings;         }          private int decode0(byte[] src, int sp, int sl, byte[] dst) {             int[] base64 = isURL ? fromBase64URL : fromBase64;             int dp = 0;             int bits = 0;             int shiftto = 18;                    while (sp < sl) {                 int b = src[sp++] & 0xff;                 if ((b = base64[b]) < 0) {                     if (b == -2) {                                  if (shiftto == 6 && (sp == sl || src[sp++] != '=') ||                             shiftto == 18) {                             throw new IllegalArgumentException(                                 "Input byte array has wrong 4-byte ending unit");                         }                         break;                     }                     if (isMIME)                             continue;                     else                         throw new IllegalArgumentException(                             "Illegal base64 character " +                             Integer.toString(src[sp - 1], 16));                 }                 bits |= (b << shiftto);                 shiftto -= 6;                 if (shiftto < 0) {                     dst[dp++] = (byte)(bits >> 16);                     dst[dp++] = (byte)(bits >>  8);                     dst[dp++] = (byte)(bits);                     shiftto = 18;                     bits = 0;                 }             }                          if (shiftto == 6) {                 dst[dp++] = (byte)(bits >> 16);             } else if (shiftto == 0) {                 dst[dp++] = (byte)(bits >> 16);
//	dst[dp++] = (byte)(bits >>  8);             } else if (shiftto == 12) {                                  throw new IllegalArgumentException(                     "Last unit does not have enough valid bits");             }                                       while (sp < sl) {                 if (isMIME && base64[src[sp++]] < 0)                     continue;                 throw new IllegalArgumentException(                     "Input byte array has incorrect ending byte at " + sp);             }             return dp;         }     }      /*      * An output stream for encoding bytes into the Base64.      */     private static class EncOutputStream extends FilterOutputStream {          private int leftover = 0;         private int b0, b1, b2;         private boolean closed = false;          private final char[] base64;             private final byte[] newline;            private final int linemax;         private final boolean doPadding;         private int linepos = 0;          EncOutputStream(OutputStream os, char[] base64,                         byte[] newline, int linemax, boolean doPadding) {             super(os);             this.base64 = base64;             this.newline = newline;             this.linemax = linemax;             this.doPadding = doPadding;         }          @Override         public void write(int b) throws IOException {             byte[] buf = new byte[1];             buf[0] = (byte)(b & 0xff);             write(buf, 0, 1);         }          private void checkNewline() throws IOException {             if (linepos == linemax) {                 out.write(newline);                 linepos = 0;             }         }          @Override         public void write(byte[] b, int off, int len) throws IOException {             if (closed)                 throw new IOException("Stream is closed");             if (off < 0 || len < 0 || len > b.length - off)                 throw new ArrayIndexOutOfBoundsException();             if (len == 0)                 return;             if (leftover != 0) {                 if (leftover == 1) {                     b1 = b[off++] & 0xff;                     len--;                     if (len == 0) {                         leftover++;                         return;                     }                 }                 b2 = b[off++] & 0xff;                 len--;                 checkNewline();                 out.write(base64[b0 >> 2]);                 out.write(base64[(b0 << 4) & 0x3f | (b1 >> 4)]);                 out.write(base64[(b1 << 2) & 0x3f | (b2 >> 6)]);                 out.write(base64[b2 & 0x3f]);                 linepos += 4;             }             int nBits24 = len / 3;             leftover = len - (nBits24 * 3);             while (nBits24-- > 0) {                 checkNewline();                 int bits = (b[off++] & 0xff) << 16 |                            (b[off++] & 0xff) <<  8 |                            (b[off++] & 0xff);                 out.write(base64[(bits >>> 18) & 0x3f]);                 out.write(base64[(bits >>> 12) & 0x3f]);                 out.write(base64[(bits >>> 6)  & 0x3f]);                 out.write(base64[bits & 0x3f]);                 linepos += 4;            }             if (leftover == 1) {                 b0 = b[off++] & 0xff;             } else if (leftover == 2) {                 b0 = b[off++] & 0xff;                 b1 = b[off++] & 0xff;             }         }          @Override         public void close() throws IOException {             if (!closed) {
//	closed = true;                 if (leftover == 1) {                     checkNewline();                     out.write(base64[b0 >> 2]);                     out.write(base64[(b0 << 4) & 0x3f]);                     if (doPadding) {                         out.write('=');                         out.write('=');                     }                 } else if (leftover == 2) {                     checkNewline();                     out.write(base64[b0 >> 2]);                     out.write(base64[(b0 << 4) & 0x3f | (b1 >> 4)]);                     out.write(base64[(b1 << 2) & 0x3f]);                     if (doPadding) {                        out.write('=');                     }                 }                 leftover = 0;                 out.close();             }         }     }      /*      * An input stream for decoding Base64 bytes      */     private static class DecInputStream extends InputStream {          private final InputStream is;         private final boolean isMIME;         private final int[] base64;               private int bits = 0;                     private int nextin = 18;                                                            private int nextout = -8;                                                           private boolean eof = false;         private boolean closed = false;          DecInputStream(InputStream is, int[] base64, boolean isMIME) {             this.is = is;             this.base64 = base64;             this.isMIME = isMIME;         }          private byte[] sbBuf = new byte[1];          @Override         public int read() throws IOException {             return read(sbBuf, 0, 1) == -1 ? -1 : sbBuf[0] & 0xff;         }          @Override         public int read(byte[] b, int off, int len) throws IOException {             if (closed)                 throw new IOException("Stream is closed");             if (eof && nextout < 0)                     return -1;             if (off < 0 || len < 0 || len > b.length - off)                 throw new IndexOutOfBoundsException();             int oldOff = off;             if (nextout >= 0) {                        do {                     if (len == 0)                         return off - oldOff;                     b[off++] = (byte)(bits >> nextout);                     len--;                     nextout -= 8;                 } while (nextout >= 0);                 bits = 0;             }             while (len > 0) {                 int v = is.read();                 if (v == -1) {                     eof = true;                     if (nextin != 18) {                         if (nextin == 12)                             throw new IOException("Base64 stream has one un-decoded dangling byte.");                                                                           b[off++] = (byte)(bits >> (16));                         len--;                         if (nextin == 0) {                                        if (len == 0) {                                           bits >>= 8;                                           nextout = 0;                             } else {                                 b[off++] = (byte) (bits >>  8);                             }                         }                     }                     if (off == oldOff)                         return -1;                     else                         return off - oldOff;                 }                 if (v == '=') {                                                                                   
//	if (nextin == 18 || nextin == 12 ||                         nextin == 6 && is.read() != '=') {                         throw new IOException("Illegal base64 ending sequence:" + nextin);                     }                     b[off++] = (byte)(bits >> (16));                     len--;                     if (nextin == 0) {                                    if (len == 0) {                                       bits >>= 8;                                       nextout = 0;                         } else {                             b[off++] = (byte) (bits >>  8);                         }                     }                     eof = true;                     break;                 }                 if ((v = base64[v]) == -1) {                     if (isMIME)                                          continue;                     else                         throw new IOException("Illegal base64 character " +                             Integer.toString(v, 16));                 }                 bits |= (v << nextin);                 if (nextin == 0) {                     nextin = 18;                         nextout = 16;                     while (nextout >= 0) {                         b[off++] = (byte)(bits >> nextout);                         len--;                         nextout -= 8;                         if (len == 0 && nextout >= 0) {                               return off - oldOff;                         }                     }                     bits = 0;                 } else {                     nextin -= 6;                 }             }             return off - oldOff;         }          @Override         public int available() throws IOException {             if (closed)                 throw new IOException("Stream is closed");             return is.available();            }          @Override         public void close() throws IOException {             if (!closed) {                 closed = true;                 is.close();             }         }     } }  final class StandardCharsets {      private StandardCharsets() {         throw new AssertionError("No java.nio.charset.StandardCharsets instances for you!");     }     /**      * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the      * Unicode character set      */     public static final Charset US_ASCII = Charset.forName("US-ASCII");     /**      * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1      */     public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");     /**      * Eight-bit UCS Transformation Format      */     public static final Charset UTF_8 = Charset.forName("UTF-8");     /**      * Sixteen-bit UCS Transformation Format, big-endian byte order      */     public static final Charset UTF_16BE = Charset.forName("UTF-16BE");     /**      * Sixteen-bit UCS Transformation Format, little-endian byte order      */     public static final Charset UTF_16LE = Charset.forName("UTF-16LE");     /**      * Sixteen-bit UCS Transformation Format, byte order identified by an      * optional byte-order mark      */     public static final Charset UTF_16 = Charset.forName("UTF-16"); }  final class Objects {     private Objects() {         throw new AssertionError("No java.util.Objects instances for you!");     }      /**      * Returns {@code true} if the arguments are equal to each other      * and {@code false} otherwise.      * Consequently, if both arguments are {@code null}, {@code true}      * is returned and if exactly one argument is {@code null}, {@code      
//	* false} is returned.  Otherwise, equality is determined by using      * the {@link Object#equals equals} method of the first      * argument.      *      * @param a an object      * @param b an object to be compared with {@code a} for equality      * @return {@code true} if the arguments are equal to each other      * and {@code false} otherwise      * @see Object#equals(Object)      */     public static boolean equals(Object a, Object b) {         return (a == b) || (a != null && a.equals(b));     }     /**     * Returns {@code true} if the arguments are deeply equal to each other     * and {@code false} otherwise.     *     * Two {@code null} values are deeply equal.  If both arguments are     * arrays, the algorithm in {@link Arrays#deepEquals(Object[],     * Object[]) Arrays.deepEquals} is used to determine equality.     * Otherwise, equality is determined by using the {@link     * Object#equals equals} method of the first argument.     *     * @param a an object     * @param b an object to be compared with {@code a} for deep equality     * @return {@code true} if the arguments are deeply equal to each other     * and {@code false} otherwise     * @see Arrays#deepEquals(Object[], Object[])     * @see Objects#equals(Object, Object)     */     public static boolean deepEquals(Object a, Object b) {         if (a == b)             return true;         else if (a == null || b == null)             return false;         else{                          System.out.println("Erro, erro na traduzação do java 6");             System.err.println("Erro, erro na traduzação do java 6");             return false;         }              }      /**      * Returns the hash code of a non-{@code null} argument and 0 for      * a {@code null} argument.      *      * @param o an object      * @return the hash code of a non-{@code null} argument and 0 for      * a {@code null} argument      * @see Object#hashCode      */     public static int hashCode(Object o) {         return o != null ? o.hashCode() : 0;     }     /**     * Generates a hash code for a sequence of input values. The hash     * code is generated as if all the input values were placed into an     * array, and that array were hashed by calling {@link     * Arrays#hashCode(Object[])}.     *     * <p>This method is useful for implementing {@link     * Object#hashCode()} on objects containing multiple fields. For     * example, if an object that has three fields, {@code x}, {@code     * y}, and {@code z}, one could write:     *     * <blockquote><pre>     * &#064;Override public int hashCode() {     *     return Objects.hash(x, y, z);     * }     * </pre></blockquote>     *     * <b>Warning: When a single object reference is supplied, the returned     * value does not equal the hash code of that object reference.</b> This     * value can be computed by calling {@link #hashCode(Object)}.     *     * @param values the values to be hashed     * @return a hash value of the sequence of input values     * @see Arrays#hashCode(Object[])     * @see List#hashCode     */     public static int hash(Object... values) {         return Arrays.hashCode(values);     }      /**      * Returns the result of calling {@code toString} for a non-{@code      * null} argument and {@code "null"} for a {@code null} argument.      *      * @param o an object      * @return the result of calling {@code toString} for a non-{@code      * null} argument and {@code "null"} for a {@code null} argument      * @see Object#toString      * @see String#valueOf(Object)      */   
//	public static String toString(Object o) {         return String.valueOf(o);     }      /**      * Returns the result of calling {@code toString} on the first      * argument if the first argument is not {@code null} and returns      * the second argument otherwise.      *      * @param o an object      * @param nullDefault string to return if the first argument is      *        {@code null}      * @return the result of calling {@code toString} on the first      * argument if it is not {@code null} and the second argument      * otherwise.      * @see Objects#toString(Object)      */     public static String toString(Object o, String nullDefault) {         return (o != null) ? o.toString() : nullDefault;     }      /**      * Returns 0 if the arguments are identical and {@code      * c.compare(a, b)} otherwise.      * Consequently, if both arguments are {@code null} 0      * is returned.      *      * <p>Note that if one of the arguments is {@code null}, a {@code      * NullPointerException} may or may not be thrown depending on      * what ordering policy, if any, the {@link Comparator Comparator}      * chooses to have for {@code null} values.      *      * @param <T> the type of the objects being compared      * @param a an object      * @param b an object to be compared with {@code a}      * @param c the {@code Comparator} to compare the first two arguments      * @return 0 if the arguments are identical and {@code      * c.compare(a, b)} otherwise.      * @see Comparable      * @see Comparator      */     public static <T> int compare(T a, T b, Comparator<? super T> c) {         return (a == b) ? 0 :  c.compare(a, b);     }      /**      * Checks that the specified object reference is not {@code null}. This      * method is designed primarily for doing parameter validation in methods      * and constructors, as demonstrated below:      * <blockquote><pre>      * public Foo(Bar bar) {      *     this.bar = Objects.requireNonNull(bar);      * }      * </pre></blockquote>      *      * @param obj the object reference to check for nullity      * @param <T> the type of the reference      * @return {@code obj} if not {@code null}      * @throws NullPointerException if {@code obj} is {@code null}      */     public static <T> T requireNonNull(T obj) {         if (obj == null)             throw new NullPointerException();         return obj;     }      /**      * Checks that the specified object reference is not {@code null} and      * throws a customized {@link NullPointerException} if it is. This method      * is designed primarily for doing parameter validation in methods and      * constructors with multiple parameters, as demonstrated below:      * <blockquote><pre>      * public Foo(Bar bar, Baz baz) {      *     this.bar = Objects.requireNonNull(bar, "bar must not be null");      *     this.baz = Objects.requireNonNull(baz, "baz must not be null");      * }      * </pre></blockquote>      *      * @param obj     the object reference to check for nullity      * @param message detail message to be used in the event that a {@code      *                NullPointerException} is thrown      * @param <T> the type of the reference      * @return {@code obj} if not {@code null}      * @throws NullPointerException if {@code obj} is {@code null}      */     public static <T> T requireNonNull(T obj, String message) {         if (obj == null)             throw new NullPointerException(message);         return obj;     }      /**      * Returns {@code true} if the provided reference is 
//	{@code null} otherwise      * returns {@code false}.      *      * @apiNote This method exists to be used as a      * {@link java.util.function.Predicate}, {@code filter(Objects::isNull)}      *      * @param obj a reference to be checked against {@code null}      * @return {@code true} if the provided reference is {@code null} otherwise      * {@code false}      *      * @see java.util.function.Predicate      * @since 1.8      */     public static boolean isNull(Object obj) {         return obj == null;     }      /**      * Returns {@code true} if the provided reference is non-{@code null}      * otherwise returns {@code false}.      *      * @apiNote This method exists to be used as a      * {@link java.util.function.Predicate}, {@code filter(Objects::nonNull)}      *      * @param obj a reference to be checked against {@code null}      * @return {@code true} if the provided reference is non-{@code null}      * otherwise {@code false}      *      * @see java.util.function.Predicate      * @since 1.8      */     public static boolean nonNull(Object obj) {         return obj != null;     }      /**      * Checks that the specified object reference is not {@code null} and      * throws a customized {@link NullPointerException} if it is.      *      * <p>Unlike the method {@link #requireNonNull(Object, String)},      * this method allows creation of the message to be deferred until      * after the null check is made. While this may confer a      * performance advantage in the non-null case, when deciding to      * call this method care should be taken that the costs of      * creating the message supplier are less than the cost of just      * creating the string message directly.      *      * @param obj     the object reference to check for nullity      * @param messageSupplier supplier of the detail message to be      * used in the event that a {@code NullPointerException} is thrown      * @param <T> the type of the reference      * @return {@code obj} if not {@code null}      * @throws NullPointerException if {@code obj} is {@code null}      * @since 1.8      */     public static <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {         if (obj == null)             throw new NullPointerException(messageSupplier.get());         return obj;     } }  interface Supplier<T> {     /**      * Gets a result.      *      * @return a result      */     T get(); } 


public class Y {    
    //public static String local_env=null;
    public static String local_env="c:\\tmp";

    public static java.util.Scanner scanner_pipe=null;
    public static java.util.Scanner scanner_pipe2=null;
    public static String linhaCSV=null;
    public static int ponteiroLinhaCSV=0;    
    public static int n_lines_buffer_DEFAULT=500;        
    public String [] ORAs=new String[]{};
    
    
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
        
        //args=new String[]{"banco","-fileCSV","a","connOut,hash","-outTable","TABELA_CCC","createtable","carga"};
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
                while( (line=read()) != null )
                    parm+=line+"\n";
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
                        tmp=tmp.replace("'","''");
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
                while( (line=read()) != null )
                    parm+=line+"\n";
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
                            tmp=tmp.substring(8, 10)+"/"+tmp.substring(5, 7)+"/"+tmp.substring(0, 4)+" "+tmp.substring(11, 19);
                        if ( tmp.length() <= 4000 ){
                            sb.append("'");
                            sb.append(tmp.replace("'","''"));
                            sb.append("'");
                        }else{
                            tmp=formatacaoInsertClobComAspetas(tmp);
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
                read(new FileInputStream(new File(fileCSV)));
            String line;
            String [] camposCSV=null;
            int qntCamposCSV=0;
            String valorColuna=null;
            StringBuilder sb=null;
            
            while ( (line=read()) != null ){
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
                while( (line=read()) != null )
                    parm+=line+"\n";                
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
                        tmp=tmp.trim();
                        
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

    public void executeInsert(String conn, InputStream in){        
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
        
        read2(in);
        
        try{
            con = getcon(conn);
            if ( con == null ){
                System.err.println("Não foi possível se conectar!!" );
                return;
            }
            con.setAutoCommit(false);
            stmt = con.createStatement();

            while( (line=read2()) != null ){
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
                while( (line=read()) != null )
                    parm+=line+"\n";                
            }

            if ( ! parm.trim().toUpperCase().startsWith("DECLARE") )
                parm=removePontoEVirgual(parm);
            
            stmt = con.createStatement();
            stmt.execute(parm);
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

            
            // testando remoção desse bloco
            /*
            // thread in
            new Thread() {
                public void run() {
                    // novo scanner in thread
                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    scanner.useDelimiter("\n");
                    while( true ){
                        if ( lista.size() < n_lines_buffer )
                        {
                            if ( scanner.hasNext()){
                                lista.add(scanner.next());
                                if ( caminhoLog[0] != null )
                                    contabiliza(countLinhasIn);
                            }else{
                                finishIn[0]=true;
                                break;
                            }
                        }
                    }
                    countLinhasIn[2]=1;
                }
            }.start();        
            */

            // thread in
            new Thread() {
                public void run() {
                    String line;
                    while( true ){
                        if ( lista.size() < n_lines_buffer )
                        {
                            if ( (line=read()) != null )
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
        }catch(Exception e){
            System.err.println(e.toString());
        }        
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
            in.close();
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

            BufferedReader in=new BufferedReader(new FileReader(caminho));
            while ((line = in.readLine()) != null)
                lista.add(line);
            in.close();

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
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return md5;
    }

    public String getMD5_SHA1_FILE(String txt,String tipo){
        try {
            byte[] bytesOfMessage = txt.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");            
            return new String(encodeHex(md.digest(bytesOfMessage)));                
        } catch (Exception e) {
            System.out.println(e.toString());
        }
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

    public void read(InputStream in){
        scanner_pipe=new java.util.Scanner(in);
        scanner_pipe.useDelimiter("\n");
    }
    
    public String read(){
        try{
            if ( scanner_pipe == null )
                read(System.in);
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
    
    public void read2(InputStream in){
        scanner_pipe2=new java.util.Scanner(in);
        scanner_pipe2.useDelimiter("\n");
    }
    
    // usando para comandos combinados por exemplo carga(read/scanner_pipe) que chama executeInsert(read2/scanner_pipe2).
    public String read2(){        
        try{
            if ( scanner_pipe2 == null )
                read2(System.in);
            if ( scanner_pipe2.hasNext() )
                return scanner_pipe2.next();
            else
                return null;
        }catch(java.util.NoSuchElementException no) {
            return null;
        }catch(Exception e){
            System.err.println("NOK: "+e.toString());
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
        }catch(Exception e){
            System.out.println(e.toString());
        }
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
                int BUFFER_SIZE = 512;
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
            int BUFFER_SIZE = 1024;
            byte[] buf = new byte[BUFFER_SIZE];            
            int len;
            while( (len=System.in.read(buf, 0, BUFFER_SIZE)) > -1 )
                digest.update(buf, 0, len);
            System.out.println(new String(encodeHex(digest.digest())));
        } catch (Exception ex) {
            System.err.println("Erro: "+ex.toString());
        }
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
            while ( (line=read()) != null ) {
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
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    public void wc_l()
    {
        try {
            long count=0;
            while ( (read()) != null )
                count++;
            System.out.println(count);
        }catch(Exception e){
        }
    }
    
    public void head(String [] args)
    {
        int p;
        String line;
        long count=0;
        
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
            while ( (line=read()) != null ) {
                if ( ++count <= p )
                    System.out.println(line);
                else{
                    while ( (read()) != null ) {}
                }
            }
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
            while ( (line=read()) != null ) {
                lista.add(line);
                if ( lista.size() > p )
                    lista.remove(0);
            }
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
            while ( (line=read()) != null ) {
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
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
            
    public void sed(String [] args)
    {
        try {
            String line=null;
            while ( (line=read()) != null ){
                for ( int i=1;i<args.length;i+=2 )
                    line=line.replaceAll(args[i], args[i+1]);
                System.out.println(line);
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
        
    public void tee(String caminho)
    {
        try{
            FileOutputStream out=new FileOutputStream(caminho);            
            int BUFFER_SIZE = 512;
            int len;
            byte[] buf = new byte[BUFFER_SIZE];
            while( (len=System.in.read(buf)) > -1){
                out.write(buf, 0, len);
                System.out.write(buf, 0, len);
            }
            out.close();
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
            while ( (line=read()) != null ) {            
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
            while ( (line=read()) != null ) {
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
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
            
    public void dev_null()
    {
        try{
            int BUFFER_SIZE = 512;
            byte[] buf = new byte[BUFFER_SIZE];
            while(System.in.read(buf) > -1){}
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void dev_in()
    {
        while(true)
            System.out.println(0);
    }

    public String base64(InputStream in,boolean encoding){
        // java 11 depreciated sun.misc.BASE64Encoder
        // tem que usar esse codigo zuado mesmo
        int BUFFER_SIZE = 1;
        byte[] buf = new byte[BUFFER_SIZE];                   
        ArrayList<Byte> lista=new ArrayList<Byte>();
        byte [] bytes=null;
        
        try {
            while( in.read(buf, 0, BUFFER_SIZE) > -1 ){
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
        while ( (line=read()) != null )
            SQL+=line+"\n";
        
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
        while ( (line=read()) != null )
            SQL+=line+"\n";
        
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
                while( (line=read()) != null )
                    select_+=line+"\n";
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

            while ( (line=read()) != null ){
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
        InputStream fstream=getClass().getResourceAsStream(caminho);
        // System.out.println(
        //   lendo_arquivo_pacote("/y/manual_mini")
        // );
        String result="";
        try{
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)
                result+=strLine+"\n";
            in.close();
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
        if ( txt.endsWith(";") )
            txt=txt.substring(0, txt.length()-1);
        return txt.replace("\"","").split(";");
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
        return linhaCSV.substring(ini,fim);
    }
    
    private String readColunaCSVSimples() {
        if ( linhaCSV.indexOf(";",ponteiroLinhaCSV) == ponteiroLinhaCSV )
        {
            ponteiroLinhaCSV++;
            return "";
        }
        
        int pos=linhaCSV.indexOf(";",ponteiroLinhaCSV+1);
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
        if ( args[1].contains("@") )
            ScpFrom.custom(new String[]{args[1],args[2]});                    
        else
            ScpTo.custom(new String[]{args[1],args[2]});                    
        System.exit(0);
    }
    
    private void execSsh(String[] args) {        
        // créditos
        // https://github.com/is/jsch/tree/master/examples
        if ( args.length != 3)
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
        Exec.custom(new String[]{args[1],args[2]});
        System.exit(0);
    }
    
    private void ssh(String[] args) {        
        // créditos
        // https://github.com/is/jsch/tree/master/examples
        if ( args.length != 2)
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
        Shell.custom(new String[]{args[1]});
        System.exit(0);
    }

    private void sftp(String[] args) {
        // créditos
        // https://github.com/is/jsch/tree/master/examples
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
        if ( args.length == 2 )
            Sftp.custom(new String[]{args[1]});
        else
            Sftp.custom(new String[]{args[1],args[2]});
        System.exit(0);
    }
    
    private void serverRouter(String[] args) {
        if ( args.length == 4 ){
            new Ponte().serverRouter(Integer.parseInt(args[1]),args[2],Integer.parseInt(args[3]),"");
            return;
        }
        if ( args.length == 5 && ( args[4].equals("show") || args[4].equals("showOnlySend") || args[4].equals("showOnlyReceive") ) ){
            new Ponte().serverRouter(Integer.parseInt(args[1]),args[2],Integer.parseInt(args[3]),args[4]);
            return;
        }
        comando_invalido(args);
        System.exit(0);
    }

    private void TESTEserver(String[] args) {
        if ( args.length == 2 ){
            new Ponte().TESTEserver(Integer.parseInt(args[1]));
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


}

class Ponte {
    //exemplo
    //new Ponte().serverRouter(8080,"localhost",9090,"");                
    //new Ponte().serverRouter(8080,"localhost",9090,"show");                
    //new Ponte().serverRouter(8080,"localhost",9090,"showOnlySend");                
    //new Ponte().serverRouter(8080,"localhost",9090,"showOnlyReceive");                
    
    // teste server
    //new Ponte().TESTEserver("9090");                        
    // teste client
    //new Ponte().TESTEclient("localhost","8080");

    public void serverRouter(int port0, String host1, int port1,String typeShow){
        Ambiente ambiente=null;
        try{
            ambiente=new Ambiente(port0);
        }catch(Exception e){
            System.out.println("Nao foi possível utilizar a porta "+port0+" - "+e.toString());
            System.exit(1);
        }     
        System.out.println("ServerRouter criado.");
        System.out.println("obs: A ponte só estabelece conexão com o destino quando detectar o início da origem");
        while(true){
            try{
                Socket credencialSocket=ambiente.getCredencialSocket();
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
        int id=new Random().nextInt(100000);
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

    private class Destino {
        OutputStream os=null;
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
            InputStream is=socket.getInputStream();                        
            os=socket.getOutputStream();
            new Thread(){
                public void run(){
                    int len=0;
                    byte[] buffer = new byte[2048];
                    try{
                        while( (len=is.read(buffer)) != -1 )
                            origem.volta(buffer);
                    }catch(Exception e){
                        System.out.println("desconectou destino");
                    }
                }
            }.start();                        
        }

        private void ida(byte[] buffer) throws Exception {
            os.write(buffer);
        }
    }

    private class Origem {    
        int ponteID=0;
        Socket socket=null;
        OutputStream os=null;
        Destino destino=null;
        boolean displayIda=false;
        boolean displayVolta=false;
        int port0;
        private Origem(Socket credencialSocket,int ponteID,String typeShow) {
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
            OutputStream os=null;
            BufferedInputStream bis=null;                            
            is = socket.getInputStream();
            os = socket.getOutputStream();
            bis=new BufferedInputStream(is);                            
            while( (len=bis.read(buffer)) != -1 ){
                if ( displayIda ){
                    System.out.println("->("+ponteID+"):");
                    System.out.println(buffer);
                }
                destino.ida(buffer);              
            }

            try{ bis.close(); }catch(Exception e){}
            try{ is.close(); }catch(Exception e){}
        }

        private void volta(byte[] buffer) throws Exception {
            if ( displayVolta ){
                System.out.println("<-("+ponteID+"):");
                System.out.println(buffer);
            }
            os.write(buffer);
        }

        private void destroy() {
            try{
                socket.close();
            }catch(Exception e){}
        }

    }

    // preparando para receber varias conexoes
    public void TESTEserver(int port){
        try{
            ServerSocket serverSocket = new ServerSocket(port, 1,InetAddress.getByName("localhost"));
            System.out.println("servidor porta "+port+" criado.");
            while (true) {
                Socket socket=serverSocket.accept();
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
        private Ambiente(int port0) throws Exception {
            serverSocket = new ServerSocket(port0, 1,InetAddress.getByName("localhost"));
        }
        private Socket getCredencialSocket() throws Exception {
            return serverSocket.accept();
        }
    }
}  


/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */    class Arquivos{
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */        public String lendo_arquivo_pacote(String caminho){
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */            if ( caminho.equals("/y/manual") )
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                return ""
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "usage:\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco fromCSV -outTable tabelaA selectInsert]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco conn,hash [select|selectInsert|selectCSV] [|select..]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco conn,hash executeInsert]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco conn,hash execute [|execute..]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco conn,hash createjobexecute]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco [connIn,hash|fileCSV,file] connOut,hash -outTable tabelaA [|trunc|createTable] [carga|createjobcarga]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco executejob]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco buffer [|-n_lines 500] [|-log buffer.log]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y token]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y gettoken]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y gzip]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y gunzip]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y echo]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y cat]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y md5]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sha1]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sha256]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y base64]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y grep]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y wc -l]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y head]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y tail]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y cut]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sed]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y tee]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y awk print]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y dev_null]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y dev_in]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y scp]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y execSsh]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y ssh]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y help]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Exemplos...\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco fromCSV -outTable tabelaA selectInsert]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo.csv | y banco fromCSV -outTable tabelaA selectInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco conn,hash [select|selectInsert|selectCSV] [|select..]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"select 1 from dual\" | y banco conn,hash select\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y banco conn,hash select \"select 1 from dual\"\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"select * from tabela1\" | y banco conn,hash selectInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat select.sql | y banco conn,hash selectCSV\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y banco -conn conn.. selectInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco conn,hash executeInsert]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat listaDeInsert.sql | y banco conn,hash executeInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"insert into tabela1 values(1,2,3)\" | y banco conn,hash executeInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"insert into tabela1 values(1,2,3);\" | y banco conn,hash executeInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco conn,hash execute [|execute..]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"truncate table tabela1\" | y banco conn,hash execute\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y banco conn,hash execute \"drop table tabela1\"\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat blocoAnonimo | y banco conn,hash execute\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco conn,hash createjobexecute]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"truncate table tabela1\" | y banco conn,hash createjobexecute\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco [connIn,hash|fileCSV,file] connOut,hash -outTable tabelaA [|trunc|createTable] [carga|createjobcarga]]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB carga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB trunc carga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB createTable carga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y banco -fileCSV arquivo.csv connOut,hash -outTable TABELA_CCC carga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y banco -fileCSV arquivo.csv connOut,hash -outTable TABELA_CCC trunc carga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y banco -fileCSV arquivo.csv connOut,hash -outTable TABELA_CCC createTable carga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco executejob]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    (\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "        echo \"select * from TABELA_AAA\" | y banco connIn,hash connOut,hash -outTable TABELA_BBB trunc createjobcarga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "        echo \"select * from TABELA_CCC\" | y banco connIn,hash connOut,hash -outTable TABELA_CCC trunc createjobcarga\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    ) | y banco executejob\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y banco buffer [|-n_lines 500] [|-log buffer.log]]    \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"select * from TABELA1 | y banco conn,hash selectInsert | y banco buffer -n_lines 500 -log buffer.log | y banco conn,hash executeInsert\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y token]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y token value\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y gettoken]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y gettoken hash\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y gzip]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y gzip > arquivo.gz\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y gunzip]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo.gz | y gunzip > arquivo\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y echo]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo a b c\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    echo \"a b c\"\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y cat]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y cat arquivo\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y md5]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y md5\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y sha1]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y sha1\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y sha256]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y sha256\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y base64]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y base64\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y grep]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y grep ^Texto$\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y grep AB\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y wc -l]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y wc -l\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y head]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y head\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y head -30\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y tail]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y tail\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y tail -30\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y cut]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y cut -c-10\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y cut -c5-10\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y cut -c5-\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y cut -c5\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y cut -c5-10,15-17\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y sed]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y sed A B\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y sed A1 A2 B1 B2\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y tee]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y tee saida.txt\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y awk]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk print 1 3 5,6\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk start AAA end BBB    \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk start AAA\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk end BBB    \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk -v start AAA end BBB    \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk -v start AAA\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y awk -v end BBB    \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    obs: \"-v\" e a negativa\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    obs2: start e end pode ocorrer varias vezes no texto\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y dev_null]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    cat arquivo | y banco buffer -n_lines 500 -log buffer.log | y dev_null\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y dev_in]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y dev_in | y banco buffer -n_lines 500 -log buffer.log | y dev_null\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y scp]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y scp file1 user,pass@servidor:file2\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y scp user,pass@servidor:file1 file2\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y execSsh]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y execSsh user,pass@servidor command\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y ssh]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y ssh user,pass@servidor\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y sftp]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y sftp user,pass@servidor\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y sftp user,pass@servidor 22\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y serverRouter]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y serverRouter 8080 localhost 9090\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y serverRouter 8080 localhost 9090 show\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y serverRouter 8080 localhost 9090 showOnlySend\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y serverRouter 8080 localhost 9090 showOnlyReceive\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    obs:\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "        8080 -> porta para conectar no router\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "        localhost -> local que o serverRouter conecta\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "        9090 -> porta que o serverRouter conecta\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y TESTEserver]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y TESTEserver 9090\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[y TESTEclient]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "    y TESTEclient localhost 8080\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Exemplo de conn: -conn \"jdbc:oracle:thin:@//host_name:1521/service_name|login|senha\"\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Exemplo de conn: -conn \"jdbc:oracle:thin:@host_name:1566:sid_name|login|senha\"\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Observacoes:\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "entrada de dados pode ser feito por |\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "export STATUS_FIM_Y=path/fim.log para receber a confirmacao de fim de processamento de selectCSV\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "export COUNT_Y=path/count.log para receber a quantidade de linhas geradas no CSV(sem o header) do comando selectCSV\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Dica: copiar o arquivo hash do token pra o nome do banco. cd $TOKEN_Y;cp 38b3492c4405f98972ba17c0a3dc072d servidor;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Dica2: vendo os tokens: grep \":\" $TOKEN_Y/*\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Dica3: vendo warnnings ORA: cat $ORAs_Y\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "alias no windows(set_alias_windows.reg):\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "Windows Registry Editor Version 5.00\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "[HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Command Processor]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\"AutoRun\"=\"DOSKEY y=java -cp c:\\\\y;c:\\\\y\\\\jsch-0.1.54E.jar;c:\\\\y\\\\ojdbc6.jar Y $*\"\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "alias no linux:\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "alias y='java -cp /y:/y/ojdbc6.jar:/y/jsch-0.1.54E.jar Y'\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "o alias windows funciona nesse comando \"y echo a\" mas nao nesse \"y echo a | y wc -l\"";
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */            if ( caminho.equals("/y/manual_mini") )
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                return ""
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "usage:\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y banco]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y token]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y gettoken]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y gzip]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y gunzip]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y echo]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y cat]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y md5]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sha1]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sha256]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y base64]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y grep]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y wc -l]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y head]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y tail]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y cut]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sed]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y tee]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y awk]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y dev_null]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y dev_in]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y scp]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y execSsh]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y ssh]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y sftp]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y serverRouter]\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "  [y help]  ";
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */            if ( caminho.equals("/y/ORAs") )
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                return ""
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00911\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00913\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00917\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00928\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00933\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00936\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00947\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-00972\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-01756\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-01742\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-01747\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "ORA-01438";
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */            if ( caminho.equals("/y/sql_get_ddl_createtable") )
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                return ""
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + " with\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + " FUNCTION func_fix_create_table(p_campo CLOB) RETURN CLOB AS \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vCampo     CLOB;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vResultado CLOB;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vC         VARCHAR2(2);\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vStart     VARCHAR2(1);\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vContador  number;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + " BEGIN\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vCampo := p_campo;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vStart := 'N';\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vResultado := '';\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   vContador := 0;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + " \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   FOR i IN 1..LENGTH(vCampo)\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   LOOP    \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     vC := substr(vCampo,i,1);\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     IF ( vC = '(' OR vC = 'C' OR vC = 'c' ) THEN\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "       vStart := 'S';\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     END IF;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     IF ( vC = '(' ) THEN\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "       vContador := vContador + 1;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     END IF;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     IF ( vStart = 'S' ) THEN\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "       vResultado := vResultado || vC;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     END IF;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     IF ( vC = ')' ) THEN\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "       vContador := vContador - 1;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "       IF ( vContador = 0 ) THEN          \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "         EXIT;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "       END IF;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "     END IF;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   END LOOP;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   return vResultado || ';';\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "   \n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + " END func_fix_create_table;\n"
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + " select func_fix_create_table(dbms_metadata.get_ddl('TABLE',UPPER('[TABELA]'),UPPER('[SCHEMA]'))) TXT from dual";
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */            if ( caminho.equals("/y/versao") )
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                return ""
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */                + "0.1.0";
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */            return "";
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */        }
/* NAO EDITAR AQUI - TEXTO GERATO AUTOMATICAMENTE */    }


