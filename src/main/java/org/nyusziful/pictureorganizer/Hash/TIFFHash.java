/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Hash;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.nyusziful.pictureorganizer.Hash.BasicFileReader.readEndianValue;


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

    private static boolean readSubDirectories(List<IfdTag> tags, TIFFMediaFileStruct tiffMediaFileStruct, ImageFileDirectory parent) throws IOException {
        BufferedRandomAccessFile bufferedInputStream = tiffMediaFileStruct.getBufferedRandomAccessFile();
        for (IfdTag ifdTag : tags) {
            long subIFDsPointer = ifdTag.offset;
            long subIFDs = ifdTag.count;
            int subIFDsPointerLength = ifdTag.getTypeLength();
            if (subIFDsPointerLength == 0) {tiffMediaFileStruct.addWarningMessage("Unknown pointer type " + ifdTag.type + " in tag " + ifdTag.tagIdName + " at " + ifdTag.address + " count " + subIFDs); continue;}
            if (subIFDs == 1) {
                if(!readIFDirectory(subIFDsPointer, IfdNames.getTag(ifdTag.tagId), tiffMediaFileStruct, parent)) return false;
            } else if (subIFDs > 1) {
                for (int j = 0; j < subIFDs; j++) {
                    if (!tiffMediaFileStruct.jumpTo(subIFDsPointer + j * subIFDsPointerLength)) return false;
    /*                bufferedInputStream.reset();
    //                    bufferedInputStream = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                    if (!skipBytes(bufferedInputStream, subIFDsPointer)) {return false;}
                    if (!skipBytes(bufferedInputStream, j * subIFDsPointerLength)) {return false;}*/
                    long nextSubIFD = BasicFileReader.readEndianValue(bufferedInputStream, subIFDsPointerLength, tiffMediaFileStruct.getEndian());
                    if(!readIFDirectory(nextSubIFD, IfdNames.getTag(ifdTag.tagId), tiffMediaFileStruct, parent)) return false;
                }
            }
        }
        return true;
    }

    private static boolean readIFDirectory(long thisIFD, String id, TIFFMediaFileStruct tiffMediaFileStruct, ImageFileDirectory parent) throws IOException {
        BufferedRandomAccessFile bufferedInputStream = tiffMediaFileStruct.getBufferedRandomAccessFile();
        if (!tiffMediaFileStruct.jumpTo(thisIFD)) return false;
        ImageFileDirectory imageFileDirectory = new ImageFileDirectory(thisIFD, id);
        parent.addTIFFDirectory(imageFileDirectory);
        int tagEntryCount = (int) BasicFileReader.readEndianValue(bufferedInputStream, 2, tiffMediaFileStruct.getEndian());
        ArrayList<IfdTag> recognizedSubDirs = new ArrayList<>();
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        boolean mainImage = false;
        ArrayList<IfdTag> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            IfdTag ifdTag = new IfdTag(tiffMediaFileStruct.getBufferedRandomAccessFile().getFilePosition());
            ifdTag.tagId = (int) BasicFileReader.readEndianValue(bufferedInputStream, 2, tiffMediaFileStruct.getEndian());
            ifdTag.tagIdName = ifdTag.getTagId();
            ifdTag.type = (int) BasicFileReader.readEndianValue(bufferedInputStream, 2, tiffMediaFileStruct.getEndian());
            ifdTag.count = BasicFileReader.readEndianValue(bufferedInputStream, 4, tiffMediaFileStruct.getEndian());
            ifdTag.offset = BasicFileReader.readEndianValue(bufferedInputStream, 4, tiffMediaFileStruct.getEndian());
/*
*Handling of known Tags
 */
            if (ifdTag.tagId == 330 ) recognizedSubDirs.add(ifdTag); //SubIFD
            if (tiffMediaFileStruct.getMode() == TIFFMediaFileStruct.MAPPING) {
                if (ifdTag.tagId == 34665 ) recognizedSubDirs.add(ifdTag); //Exif IFD
    //            if (ifdTag.tagId == 33723 ) recognizedSubDirs.add(ifdTag); //IPTC
    //            if (ifdTag.tagId == 34152 ) recognizedSubDirs.add(ifdTag); //IPTC
    //            if (ifdTag.tagId == 34377 ) recognizedSubDirs.add(ifdTag); //Photoshop
    //            if (ifdTag.tagId == 34675 ) recognizedSubDirs.add(ifdTag); //ICC
                if (ifdTag.tagId == 34853 ) recognizedSubDirs.add(ifdTag); //GPS
                //TODO kills it
                if (ifdTag.tagId == 37500 ) recognizedSubDirs.add(ifdTag); //MakerNote
                if (ifdTag.tagId == 37724 ) recognizedSubDirs.add(ifdTag); //Photoshop DocumentData Tags
                if (ifdTag.tagId == 40965 ) recognizedSubDirs.add(ifdTag); //InteropOffset
                if (ifdTag.tagId == 50740 ) recognizedSubDirs.add(ifdTag); //MakerNote
                if (ifdTag.tagId == 50828 ) recognizedSubDirs.add(ifdTag); //DNG OriginalRaw Tags
    //            if (ifdTag.tagId == 50831 ) recognizedSubDirs.add(ifdTag); //ICC*/
    //            if (ifdTag.tagId == 50833 ) recognizedSubDirs.add(ifdTag); //ICC
                if (ifdTag.tagId == 50933 ) recognizedSubDirs.add(ifdTag); //ProfileIFD
    //            if (ifdTag.tagId == 65024 ) recognizedSubDirs.add(ifdTag); //Kodak
            }

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
        long nextIFD = BasicFileReader.readEndianValue(bufferedInputStream, 4, tiffMediaFileStruct.getEndian());
        if (tiffMediaFileStruct.getMode() == TIFFMediaFileStruct.HASHING) {
            if (mainImage) {tiffMediaFileStruct.setDigestBytes(getPointers(imageLocationFields, tiffMediaFileStruct.getFile(), tiffMediaFileStruct.getEndian()));}
        }
        final boolean brokenSubdir = readSubDirectories(recognizedSubDirs, tiffMediaFileStruct, imageFileDirectory);
/*        if (subIFDs == 1) {
            if(!readIFDirectory(subIFDsPointer, tiffMediaFileStruct, imageFileDirectory)) return false;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {
                if (!tiffMediaFileStruct.jumpTo(subIFDsPointer + j * subIFDsPointerLength)) return false;
/*                bufferedInputStream.reset();
//                    bufferedInputStream = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                if (!skipBytes(bufferedInputStream, subIFDsPointer)) {return false;}
                if (!skipBytes(bufferedInputStream, j * subIFDsPointerLength)) {return false;}* /
                long nextSubIFD = readEndianValue(bufferedInputStream, subIFDsPointerLength, tiffMediaFileStruct.getEndian());
                if(!readIFDirectory(nextSubIFD, tiffMediaFileStruct, imageFileDirectory)) return false;
            }
        }*/
        if (nextIFD == 0) {
            return true;
        }
        return readIFDirectory(nextIFD, "IFD", tiffMediaFileStruct, parent);
    }

    public static TIFFMediaFileStruct scan(File file, Long startPosition, int mode) {
        try (TIFFMediaFileStruct tiffMediaFileStruct = new TIFFMediaFileStruct(file, startPosition, mode)) {
            try {
                BufferedRandomAccessFile bufferedInputStream = tiffMediaFileStruct.getBufferedRandomAccessFile();
                if (!tiffMediaFileStruct.jumpTo(0)) {
                    tiffMediaFileStruct.setTerminationMessage("Starting point " + startPosition + " could not be reached");
                    return tiffMediaFileStruct;
                }
//                bufferedInputStream.mark(0);
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
                long tiffCheck = BasicFileReader.readEndianValue(bufferedInputStream, 2, endian);
                if (tiffCheck != 42) {tiffMediaFileStruct.setTerminationMessage("Not 42."); return tiffMediaFileStruct;}
                long nextIFD = BasicFileReader.readEndianValue(bufferedInputStream, 4, endian);
                if (nextIFD == -1) {tiffMediaFileStruct.setTerminationMessage("File end reached before first directory."); return tiffMediaFileStruct;}
                if (nextIFD == 0) {tiffMediaFileStruct.setTerminationMessage("File first pointer is zero."); return tiffMediaFileStruct;}
                ImageFileDirectory root = new ImageFileDirectory(startPosition, "root");
                tiffMediaFileStruct.addSegment(root);
                if (readIFDirectory(nextIFD, "IFD0", tiffMediaFileStruct, root)) tiffMediaFileStruct.setTerminationMessage("Successful."); else tiffMediaFileStruct.setTerminationMessage("Unsuccessful.");
                return tiffMediaFileStruct;
            } catch (FileNotFoundException e) {
                tiffMediaFileStruct.setTerminationMessage("File couldn't be opened.");
            } catch (IOException e) {
                tiffMediaFileStruct.setTerminationMessage("IO error: "+ e.getMessage());
            }
            return tiffMediaFileStruct;
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        final TIFFMediaFileStruct scan = scan(new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\V5_K2015-07-2_5@12-3_2-29(+0200)(Sat)-cd293a213a3291ad280629d38e7d3a3c-47e0be579ef91106cdd6c818b2976ce2-DSC09461.ARW"), 0L, TIFFMediaFileStruct.MAPPING);
        scan.drawMap();
    }

    //returns the pointer to the main image data bufferedInputStream tiff based files
    public static byte[] readDigest(File file, BufferedInputStream buffered_InputStream) {
        return scan(file, 0L, TIFFMediaFileStruct.HASHING).getDigestBytes();
    }
}
