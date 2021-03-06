/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hash;

import static Main.StaticTools.errorOut;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabor
 */
public class Hash {
    public static String EMPTYHASH = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    
    private static long startOfImageJPG(BufferedInputStream in) throws IOException {
        int c;
        long j = 0;
        OUTER:
        while ((c = in.read()) != -1) {
            j++;
            switch (c) {
                case 0:
                    break;
                case 0xFF://255
                    c = in.read();
                    j++;
                    if (c == 0xD8 /*216*/) {break OUTER;}
                default:
                    return -1;
            }
        }
        return j;
    }
    
    private static long startOfScanJPG(BufferedInputStream in) throws IOException {
        int c;
        long j = startOfImageJPG(in);
        if (j == -1) return -1;
        Boolean marker = false;
        while ((c = in.read()) != -1) {
            if (marker)
                //Not a marker(byte stuffing), shouldn't happen in header
                if (c == 0/* || c==216*/) marker = false;
                //Start of Quantization table, practically the image
//Didn't work                else if (c == 219) return j;
                //Start of Scan
                else if (c == 0xDA /*218*/) return j; 
                else {
//                    System.out.print("ff" + Integer.toHexString(c) + " ");
                    long jump = 256*in.read() + in.read() - 2;
//                    System.out.println(Long.toHexString(jump));
                    do {
                        long skip = in.skip(jump);
                        if (skip < 0) return -1;
                        jump -= skip;
                    } while (jump > 0);
                    j += jump + 2;                        
                    marker = false;
                }
            else {
                if (c == 0xFF/*255*/) {marker = true;
//                } else if (c == 0) {//byte stuffing
                } else {
                    return -1;
                }
            }
            j++;
        }
        return -1;  
    }
    
    private static long readEndianValue(InputStream in, int length, boolean endian) {
        long result = 0;
        for(int i=0; i<length; i++) {
            try {
                long c = in.read();
                if (endian) {
                    c = (long) (c * Math.pow(256, i));
                } else {
                    c = (long) (c * Math.pow(256, length-1-i));
                }
                result += c;
            } catch (IOException ex) {
                return -1;
            }
        }
        return result;
    }

    private static byte[] getPointers(ArrayList<ifdField> imageLocationFields, File file, boolean endian) throws IOException {
/*      RawImageDigest
        Tag 50972 (C71C.H)
        Type BYTE
        Count 16
        Value See below
        Default Optional
        Usage IFD 0
        Description
        This tag is an MD5 digest of the raw image data. All pixels in the image are processed in rowscan
        order. Each pixel is zero padded to 16 or 32 bits deep (16-bit for data less than or equal to
        16 bits deep, 32-bit otherwise). The data for each pixel is processed in little-endian byte order         */
        long imageLength;
        long imageWidth;
        long pieceOffsets = 0;
        long pieceOffsetsCount = 0;
        int pieceOffsetsLength = 0;
        long pieceLength;
        long pieceWidth = 1;
        long pieceByteCounts = 0;
        long pieceByteCountsCount = 0;
        int pieceByteLength = 0;
        Iterator<ifdField> iterator = imageLocationFields.iterator();
        while (iterator.hasNext()) {
            ifdField field = iterator.next();
            switch (field.tag) {
                case 256:
                    imageWidth = field.offset;
                    break;
                case 257:
                    imageLength = field.offset;
                    break;
                case 273:
                case 324:
                    pieceOffsets = field.offset;
                    pieceOffsetsCount = field.count;
                    pieceOffsetsLength = field.getTypeLength();
                    break;
                case 322:
                    pieceWidth = field.offset;
                    break;
                case 278:
                case 323:
                    pieceLength = field.offset;
                    break;
                case 279:
                case 325:
                    pieceByteCounts = field.offset;
                    pieceByteCountsCount = field.count;
                    pieceByteLength = field.getTypeLength();
                    break;
            }
        }
        if (pieceByteCountsCount != pieceOffsetsCount) return null;
        try (RandomAccessFile fileRand = new RandomAccessFile(file.getAbsolutePath(), "r")) {
            MessageDigest md5Digest = null;
            try {
                md5Digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                return null;
            }
            if (pieceOffsetsCount == 1) {
                    System.out.println(file.getName() + " bytes readed: " + pieceByteCounts + " File size " + file.length() + " % " + (100*pieceByteCounts/file.length()));
                    fileRand.seek(pieceOffsets);
                    long readed = 0;
                    int bufferSize = 4096;
                    while (readed + bufferSize < pieceByteCounts) {
                        byte chunk[] = new byte[bufferSize];
                        int read = fileRand.read(chunk);
                        if (read == -1) return null;
                        readed += read; 
                        md5Digest.update(chunk);
                    }
                    int residue = (int)(pieceByteCounts - readed);
                    byte chunk[] = new byte[residue];
                    int read = fileRand.read(chunk);
                    if (read == -1) return null;
                    md5Digest.update(chunk);
            } else {
                long totalread = 0;
                for (int j = 0; j < pieceOffsetsCount; j++) {
                    fileRand.seek(pieceByteCounts + j * pieceByteLength);
                    long actualPieceBytes = 0;
                    for(int i=0; i<pieceByteLength; i++) {
                            long c = fileRand.read();
                            if (endian) {
                                c = (long) (c * Math.pow(256, i));
                            } else {
                                c = (long) (c * Math.pow(256, pieceByteLength-i));
                            }
                            actualPieceBytes += c;
                    }
        //            System.out.println("Count [" + j + "] =" + actualPieceBytes);
                    fileRand.seek(pieceOffsets + j * pieceOffsetsLength);
                    long actualPieceOffset = 0;
                    for(int i=0; i<pieceOffsetsLength; i++) {
                            long c = fileRand.read();
                            if (endian) {
                                c = (long) (c * Math.pow(256, i));
                            } else {
                                c = (long) (c * Math.pow(256, pieceOffsetsLength-i));
                            }
                            actualPieceOffset += c;
                    }
                    totalread += actualPieceBytes;
        //            System.out.println("Offset [" + j + "] =" + actualPieceOffset);
                    fileRand.seek(actualPieceOffset);
                    long readed = 0;
                    int bufferSize = 4096;
                    while (readed + bufferSize < actualPieceBytes) {
                        byte chunk[] = new byte[bufferSize];
                        int read = fileRand.read(chunk);
                        if (read == -1) return null;
                        readed += read; 
                        md5Digest.update(chunk);
                    }
                    int residue = (int)(actualPieceBytes - readed);
                    byte chunk[] = new byte[residue];
                    int read = fileRand.read(chunk);
                    if (read == -1) return null;
                    md5Digest.update(chunk);
                }
                System.out.println(file.getName() + " bytes readed: " + totalread + " File size " + file.length() + " % " + (100*totalread/file.length()));
            }
            return md5Digest.digest();
        }
    }
    
    private static byte[] readSubIFDirectory(ifdCursor cursor, BufferedInputStream in) throws IOException {
        in.reset();
        if (!skipBytes(in, cursor.getPointer())) return null;
        int tagEntryCount = (int) readEndianValue(in, 2, cursor.getEndian());
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        boolean mainImage = false;
        ArrayList<ifdField> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            ifdField field = new ifdField();
            field.tag = (int) readEndianValue(in, 2, cursor.getEndian());
            field.type = (int) readEndianValue(in, 2, cursor.getEndian());
            field.count = readEndianValue(in, 4, cursor.getEndian());
            field.offset = readEndianValue(in, 4, cursor.getEndian());
            if (field.tag == 50972) System.out.println("!!!!!!!!!!" + field.count + " " + field.offset);
            if (field.tag == 254 && field.offset == 0) {mainImage = true;}
            if (field.tag == 330) {
                subIFDsPointer = field.offset;
                subIFDs = field.count;
                subIFDsPointerLength = field.getTypeLength();
            }
//            System.out.println(field.getTag() + " " + field.getType() + " " + field.getCount() + " " + field.getValue() + " " + field.getPointer());
            if (field.tag == 257 || field.tag == 256) imageLocationFields.add(field); //Image
            if (field.tag == 273 || field.tag == 278 || field.tag == 279) imageLocationFields.add(field); //Stripe
            if (field.tag == 322 || field.tag == 323 || field.tag == 324 || field.tag == 325) imageLocationFields.add(field); //Tile
        }
        if (mainImage) return getPointers(imageLocationFields, cursor.getFile(), cursor.getEndian());
        if (subIFDs == 1) {
            cursor.setPointer(subIFDsPointer);
            byte[] hash = readSubIFDirectory(cursor, in);
            if (hash != null) return hash;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {
                in.reset();
//                    in = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                if (!skipBytes(in, subIFDsPointer)) return null;
                if (!skipBytes(in, j * subIFDsPointerLength)) return null;
                cursor.setPointer(readEndianValue(in, subIFDsPointerLength, cursor.getEndian()));
                byte[] hash = readSubIFDirectory(cursor, in);
                if (hash != null) return hash;
            }
        }
        return null;
    }
    
    private static byte[] readIFDirectory(ifdCursor cursor, BufferedInputStream in) throws IOException {
        in.reset();
        if (!skipBytes(in, cursor.getPointer())) return null;
        int tagEntryCount = (int) readEndianValue(in, 2, cursor.getEndian());
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        long nextIFD = 0;
        boolean mainImage = false;
        ArrayList<ifdField> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            ifdField field = new ifdField();
            field.tag = (int) readEndianValue(in, 2, cursor.getEndian());
            field.type = (int) readEndianValue(in, 2, cursor.getEndian());
            field.count = readEndianValue(in, 4, cursor.getEndian());
            field.offset = readEndianValue(in, 4, cursor.getEndian());
            if (field.tag == 50972) System.out.println("!!!!!!!!!!" + field.count + " " + field.offset);
            if (field.tag == 254 && field.offset == 0) {mainImage = true;}
            if (field.tag == 330) {
                subIFDsPointer = field.offset;
                subIFDs = field.count;
                subIFDsPointerLength = field.getTypeLength();
            }
//            System.out.println(field.getTag() + " " + field.getType() + " " + field.getCount() + " " + field.getValue() + " " + field.getPointer());
            if (field.tag == 257 || field.tag == 256) imageLocationFields.add(field); //Image
            if (field.tag == 273 || field.tag == 278 || field.tag == 279) imageLocationFields.add(field); //Stripe
            if (field.tag == 322 || field.tag == 323 || field.tag == 324 || field.tag == 325) imageLocationFields.add(field); //Tile
        }
        nextIFD = readEndianValue(in, 4, cursor.getEndian());
        if (mainImage) return getPointers(imageLocationFields, cursor.getFile(), cursor.getEndian());
        if (subIFDs == 1) {
            cursor.setPointer(subIFDsPointer);
            byte[] hash = readSubIFDirectory(cursor, in);
            if (hash != null) return hash;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {                    
                in.reset();
//                    in = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                if (!skipBytes(in, subIFDsPointer)) return null;
                if (!skipBytes(in, j * subIFDsPointerLength)) return null;
                cursor.setPointer(readEndianValue(in, subIFDsPointerLength, cursor.getEndian()));
                byte[] hash = readSubIFDirectory(cursor, in);
                if (hash != null) return hash;
            }
        }
        cursor.setPointer(nextIFD);
        return readIFDirectory(cursor, in);
    }
        
    //returns the pointer to the main image data in tiff based files
    private static byte[] startOfScanTiff(File file, BufferedInputStream in) throws IOException {
        in.mark(0);
        int c;
        long j = 0;
        Boolean endian = null;
        c = in.read();
        if (c == 73) {
            if (in.read() == 73) endian = true;
        } else if (c == 77) {
            if (in.read() == 77) endian = false;
        }
        if (endian == null) {return null;}
        long tiffCheck = readEndianValue(in, 2, endian);
        if (tiffCheck != 42) {return null;}
        ifdCursor cursor = new ifdCursor(file, endian, readEndianValue(in, 4, endian));
        if (cursor.getPointer() == -1) {return null;}
        if (cursor.getPointer() == 0) {return null;}
        return readIFDirectory(cursor, in);
    }
    
    private static boolean skipBytes(InputStream in, long pointer) throws IOException {
        do {
            long skip = in.skip(pointer);
            if (skip < 0) return false;
            pointer -= skip;
        } while (pointer > 0);
        return true;
    }
    
    private static byte[] readBytes(InputStream in, int size) throws IOException {
        byte result[] = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte)in.read();
        }
        return result;
    }
    
/*    public static ArrayList<String> getHash(Path folderPath) throws FileNotFoundException, IOException {
        ArrayList<String> hashes = new ArrayList<>();
        File[] listOfFiles = folderPath.toFile().listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (PicOrganizes.supportedFileType(listOfFile.getName())) {
                    hashes.add(getHash(listOfFile));
                }
            } else if (listOfFile.isDirectory()) {
                hashes.addAll(getHash(listOfFile.toPath()));    
            }
        }
        return hashes;
    }*/
    
    private static String getFullHashPS(File file) throws FileNotFoundException, IOException {
        String[] parameters = new String[]{"powershell.exe", "Get-ChildItem", "-File", "-Recurse", "|", "Get-FileHash", "-Algorithm", "MD5", ">>", "e:\\ps.md5"};
        String lines = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(parameters, null, file);
/*            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(() -> {
                try {
                    String s;
                    while (( s=stdinReader.readLine()) != null) {
                        System.out.println(s);
                    }
                }
                catch(IOException e) {
                }
            }).start();
            new Thread(() -> {
                try {
                    String s;
                    while (( s=stderrReader.readLine()) != null) {
                        System.out.println(s);
                    }
                }
                catch(IOException e) {
                }
            }).start();*/
            int returnVal = p.waitFor();
        } catch (Exception e) {
            errorOut("xmp", e);
        } 
        return lines;

    }
    
    public static String getFullHash(File file) throws FileNotFoundException, IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file.toString())); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {            
            byte[] buffer = new byte[4096];
            in.on(true);
            while (in.read(buffer) != -1) {}
            digest = md5Digest.digest();
        }
        if (digest == null) {
            return EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            return EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    
    public static String getHash(File file) throws FileNotFoundException, IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        String ext = FilenameUtils.getExtension(file.getName().toLowerCase());
        if (ext.equals("arw") || ext.equals("nef") || ext.equals("dng") || ext.equals("tif") || ext.equals("tiff")) {
        // <editor-fold defaultstate="collapsed" desc="raw">
            try (BufferedInputStream rawIn = new BufferedInputStream(new FileInputStream(file.toString()));) {                    
                digest = startOfScanTiff(file, rawIn);
            } catch(IOException e) {
                errorOut("Hash", e);         
            } 
        // </editor-fold>            
        } else {
            try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {            
                byte[] buffer = new byte[4096];
                long length;
                switch (ext) {
        // <editor-fold defaultstate="collapsed" desc="mp4">
                    case "mp4":
                        in.on(false);
                        boolean EOF = false;
                        do {
                            length = readEndianValue(in, 4, false) - 8;
    //                        byte boxLength[] = readBytes(in, 4);
                            String desc = new String(readBytes(in, 4));                        
    //                        ByteBuffer wrapped = ByteBuffer.wrap(boxLength); // big-endian by default
    //                        length = wrapped.getInt() - 8;
                            if (length == -7) {
                                //largesize
                                length = readEndianValue(in, 8, false) - 16;
                            } else if (length == -8) {
                                // until eof
                                 EOF = true;
                            } else if (length == in.available()) {
                                 EOF = true;
                            }
                            if (desc.equals("mdat")) {
                                in.on(true);
                                while (buffer.length <= length) {
                                    int read = in.read(buffer);
                                    length -= read;
                                }
                                while (length > 0) {
                                    int read = in.read(buffer, 0, (int)length);
                                    if (read == -1) {digest = null; break;}
                                    length -= read;
                                }
    //                            break;
                            } else {
                                if (!skipBytes(in, length)) break;
                            }
                        } while(!EOF);    
                        digest = md5Digest.digest();
                        break;
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="JPG">
                    case "jpg":
                    case "jpeg":
                        int scanLength;
                        int scanLengthOld = 0;
                        do {
                            in.on(false);
                            md5Digest.reset();
                            scanLength = 0;
                            if (startOfScanJPG(fileStream) == -1) break;
                            in.on(true);
                            int c = 0;
                            int oldc;
                            do {
                                oldc = c;
                                c = in.read();
                                if (c == -1) {
                                    return EMPTYHASH;
                                }
                                scanLength ++;
                            } while (!(oldc == 0xFF && c == 0xD9/*217*/));
                            if (scanLength > scanLengthOld) {digest = md5Digest.digest(); scanLengthOld = scanLength;}
                        } while (scanLengthOld < in.available());
                        break;
        // </editor-fold>
                    default:
                        in.on(true);
                        while (in.read(buffer) != -1) {}
                        digest = md5Digest.digest();
                        break;
                }
            }  catch(IOException e) {
                errorOut("Hash", e);         
            } 
        }
        if (digest == null) {
            return EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            return EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    
    public static String formatHash(String hash) {
        if (hash.length()!=32) return hash;
        return hash.substring(0, 8) + "-" + hash.substring(8, 12) + "-" + hash.substring(12, 16) + "-" + hash.substring(16, 20) + "-" + hash.substring(20, 32);
    }

    
}
