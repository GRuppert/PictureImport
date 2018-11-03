/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.nyusziful.Hash.BasicFileReader.readEndianValue;
import static org.nyusziful.Hash.BasicFileReader.skipBytes;


/**
 *
 * @author gabor
 */
public class TIFFHash implements Hasher {
    private static final Logger LOG = LogManager.getLogger(TIFFHash.class);
    
    private static byte[] getPointers(ArrayList<IfdField> imageLocationFields, File file, boolean endian) throws IOException {
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
        Iterator<IfdField> iterator = imageLocationFields.iterator();
        while (iterator.hasNext()) {
            IfdField field = iterator.next();
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
    
    private static byte[] readSubIFDirectory(IfdCursor cursor, BufferedInputStream in) throws IOException {
        in.reset();
        if (!skipBytes(in, cursor.getPointer())) return null;
        int tagEntryCount = (int) readEndianValue(in, 2, cursor.getEndian());
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        boolean mainImage = false;
        ArrayList<IfdField> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            IfdField field = new IfdField();
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
    
    private static byte[] readIFDirectory(IfdCursor cursor, BufferedInputStream in) throws IOException {
        in.reset();
        if (!skipBytes(in, cursor.getPointer())) return null;
        int tagEntryCount = (int) readEndianValue(in, 2, cursor.getEndian());
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        long nextIFD = 0;
        boolean mainImage = false;
        ArrayList<IfdField> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            IfdField field = new IfdField();
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
    public static byte[] readDigest(File file, BufferedInputStream in) throws IOException {
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
        IfdCursor cursor = new IfdCursor(file, endian, readEndianValue(in, 4, endian));
        if (cursor.getPointer() == -1) {return null;}
        if (cursor.getPointer() == 0) {return null;}
        return readIFDirectory(cursor, in);
    }
}
