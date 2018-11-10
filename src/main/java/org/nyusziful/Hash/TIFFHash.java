/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

import java.io.*;
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
    
    private static byte[] getPointers(ArrayList<IfdTag> imageLocationFields, File file, boolean endian) throws IOException {
/*      RawImageDigest
        Tag 50972 (C71C.H)
        Type BYTE
        Count 16
        Value See below
        Default Optional
        Usage IFD 0
        Description
        This tagId is an MD5 digest of the raw image data. All pixels in the image are processed in rowscan
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
        Iterator<IfdTag> iterator = imageLocationFields.iterator();
        while (iterator.hasNext()) {
            IfdTag field = iterator.next();
            switch (field.tagId) {
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
        try (RandomAccessFile fileRand = new RandomAccessFile(file.getAbsolutePath(), "r"); FileInputStream fis = new FileInputStream(fileRand.getFD()); BufferedInputStream bis = new BufferedInputStream(fis);) {
//        try (RandomAccessFile fileRand = new RandomAccessFile(file.getAbsolutePath(), "r")) {
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
                    int bufferSize = Integer.MAX_VALUE;
                    while (readed + bufferSize < pieceByteCounts) {
                        byte chunk[] = new byte[bufferSize];
                        int read = bis.read(chunk);
                        if (read == -1) return null;
                        readed += read;
                        md5Digest.update(chunk);
                    }
                    int residue = (int)(pieceByteCounts - readed);
                    byte chunk[] = new byte[residue];
                    int read = bis.read(chunk);
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
    
    //0x8769 34665 Exif offset
    //700	, "	XMP

    private static boolean readIFDirectory(long thisIFD, TIFFMediaFileStruct tiffMediaFileStruct, ImageFileDirectory parent) throws IOException {
        BufferedInputStream in = tiffMediaFileStruct.getBufferedInputStream();
        long readFromBuffer = -8192;
        if (!tiffMediaFileStruct.jumpTo(thisIFD)) return false;
        ImageFileDirectory imageFileDirectory = new ImageFileDirectory(thisIFD);
        parent.addTIFFDirectory(imageFileDirectory);
        int tagEntryCount = (int) readEndianValue(in, 2, tiffMediaFileStruct.getEndian());
        readFromBuffer += 2;
        long exif = 0;
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        boolean mainImage = false;
        ArrayList<IfdTag> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            IfdTag ifdTag = new IfdTag(tiffMediaFileStruct.getRandomAccessFile().getFilePointer() + readFromBuffer);
            ifdTag.tagId = (int) readEndianValue(in, 2, tiffMediaFileStruct.getEndian());
            ifdTag.type = (int) readEndianValue(in, 2, tiffMediaFileStruct.getEndian());
            ifdTag.count = readEndianValue(in, 4, tiffMediaFileStruct.getEndian());
            ifdTag.offset = readEndianValue(in, 4, tiffMediaFileStruct.getEndian());
            readFromBuffer += 12;
/*
*Handling of known Tags
 */
            if (ifdTag.tagId == 34665 && ifdTag.count < 4 ) exif = ifdTag.offset;
            if (ifdTag.tagId == 50972) System.out.println("!!!!!!!!!!" + ifdTag.count + " " + ifdTag.offset);
            if (ifdTag.tagId == 254 && ifdTag.offset == 0) {mainImage = true;}
            if (ifdTag.tagId == 330) {
                subIFDsPointer = ifdTag.offset;
                subIFDs = ifdTag.count;
                subIFDsPointerLength = ifdTag.getTypeLength();
            }
            if (ifdTag.tagId == 257 || ifdTag.tagId == 256) imageLocationFields.add(ifdTag); //Image
            if (ifdTag.tagId == 273 || ifdTag.tagId == 278 || ifdTag.tagId == 279) imageLocationFields.add(ifdTag); //Stripe
            if (ifdTag.tagId == 322 || ifdTag.tagId == 323 || ifdTag.tagId == 324 || ifdTag.tagId == 325) imageLocationFields.add(ifdTag); //Tile
            imageFileDirectory.addTag(ifdTag);
        }
        long nextIFD = readEndianValue(in, 4, tiffMediaFileStruct.getEndian());
        if (mainImage) {tiffMediaFileStruct.setDigestBytes(getPointers(imageLocationFields, tiffMediaFileStruct.getFile(), tiffMediaFileStruct.getEndian()));}
        if (exif != 0) {
            if(!readIFDirectory(exif, tiffMediaFileStruct, imageFileDirectory)) return false;
        }
        if (subIFDs == 1) {
            if(!readIFDirectory(subIFDsPointer, tiffMediaFileStruct, imageFileDirectory)) return false;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {
                if (!tiffMediaFileStruct.jumpTo(subIFDsPointer + j * subIFDsPointerLength)) return false;
/*                in.reset();
//                    in = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                if (!skipBytes(in, subIFDsPointer)) {return false;}
                if (!skipBytes(in, j * subIFDsPointerLength)) {return false;}*/
                long nextSubIFD = readEndianValue(in, subIFDsPointerLength, tiffMediaFileStruct.getEndian());
                if(!readIFDirectory(nextSubIFD, tiffMediaFileStruct, imageFileDirectory)) return false;
            }
        }
        if (nextIFD == 0) {
            return true;
        }
        return readIFDirectory(nextIFD, tiffMediaFileStruct, parent);
    }

    public static TIFFMediaFileStruct scan(File file, Long startPosition) {
        TIFFMediaFileStruct tiffMediaFileStruct = new TIFFMediaFileStruct(file, startPosition);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r"); FileInputStream fis = new FileInputStream(randomAccessFile.getFD()); BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);) {
//        try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInStream, 100000);) {
            tiffMediaFileStruct.setBufferedInputStream(bufferedInputStream);
            tiffMediaFileStruct.setRandomAccessFile(randomAccessFile);
            skipBytes(bufferedInputStream, startPosition);
            bufferedInputStream.mark(0);
            int readByte;
            Boolean endian = null;
            readByte = bufferedInputStream.read();
            if (readByte == 73) {
                if (bufferedInputStream.read() == 73) endian = true;
            } else if (readByte == 77) {
                if (bufferedInputStream.read() == 77) endian = false;
            }
            if (endian == null) {tiffMediaFileStruct.setTerminationMessage("Endian definition error."); return tiffMediaFileStruct;}
            tiffMediaFileStruct.setEndian(endian);
            long tiffCheck = readEndianValue(bufferedInputStream, 2, endian);
            if (tiffCheck != 42) {tiffMediaFileStruct.setTerminationMessage("Not 42."); return tiffMediaFileStruct;}
            long nextIFD = readEndianValue(bufferedInputStream, 4, endian);
            if (nextIFD == -1) {tiffMediaFileStruct.setTerminationMessage("File end reached before first directory."); return tiffMediaFileStruct;}
            if (nextIFD == 0) {tiffMediaFileStruct.setTerminationMessage("File first pointer is zero."); return tiffMediaFileStruct;}
            ImageFileDirectory root = new ImageFileDirectory(startPosition);
            tiffMediaFileStruct.addSegment(root);
            if (readIFDirectory(nextIFD, tiffMediaFileStruct, root)) tiffMediaFileStruct.setTerminationMessage("Successful."); else tiffMediaFileStruct.setTerminationMessage("Unsuccessful.");
            return tiffMediaFileStruct;
        } catch (FileNotFoundException e) {
            tiffMediaFileStruct.setTerminationMessage("File couldn't be opened.");
        } catch (IOException e) {
            tiffMediaFileStruct.setTerminationMessage("IO error: "+ e.getMessage());
        }
        return tiffMediaFileStruct;
    }

    //returns the pointer to the main image data bufferedInputStream tiff based files
    public static byte[] readDigest(File file, BufferedInputStream buffered_InputStream) {
        TIFFMediaFileStruct tiffMediaFileStruct = new TIFFMediaFileStruct(file, 0);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r"); FileInputStream fis = new FileInputStream(randomAccessFile.getFD()); BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);) {
            tiffMediaFileStruct.setBufferedInputStream(bufferedInputStream);
            tiffMediaFileStruct.setRandomAccessFile(randomAccessFile);
            bufferedInputStream.mark(0);
            int c;
            long j = 0;
            Boolean endian = null;
                c = bufferedInputStream.read();
            if (c == 73) {
                if (bufferedInputStream.read() == 73) endian = true;
            } else if (c == 77) {
                if (bufferedInputStream.read() == 77) endian = false;
            }
            if (endian == null) {return null;}
            tiffMediaFileStruct.setEndian(endian);
            long tiffCheck = readEndianValue(bufferedInputStream, 2, endian);
            if (tiffCheck != 42) {return null;}
            long nextIFD = readEndianValue(bufferedInputStream, 4, endian);
            if (nextIFD == -1) {return null;}
            if (nextIFD == 0) {return null;}
            ImageFileDirectory root = new ImageFileDirectory(0);
            tiffMediaFileStruct.addSegment(root);
            if (readIFDirectory(nextIFD, tiffMediaFileStruct, root)) tiffMediaFileStruct.setTerminationMessage("Successful."); else tiffMediaFileStruct.setTerminationMessage("Unsuccessful.");
        } catch (FileNotFoundException e) {
            tiffMediaFileStruct.setTerminationMessage("File couldn't be opened.");
        } catch (IOException e) {
            tiffMediaFileStruct.setTerminationMessage("IO error: "+ e.getMessage());
        }
//        tiffMediaFileStruct.drawMap();
        return tiffMediaFileStruct.getDigestBytes();
    }
}
