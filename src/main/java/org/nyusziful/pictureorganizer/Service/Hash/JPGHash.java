/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.*;
import java.lang.reflect.Array;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author gabor
 */
public class JPGHash implements Hasher {
    private static final int markerLength = 2;
    private static final Logger LOG = LogManager.getLogger(JPGHash.class);
    private static final String BACKUPID = "Backup";
    
    private static long startOfImageJPG(BufferedInputStream in) throws IOException {
        int lastReadByte;
        long lastReadBytePosition = -1;
        OUTER:
        while ((lastReadByte = in.read()) != -1) {
            lastReadBytePosition++;
            switch (lastReadByte) {
                case 0:
                    break;
                case 0xFF://255
                    lastReadByte = in.read();
                    lastReadBytePosition++;
                    if (lastReadByte == 0xD8 /*216*/) {break OUTER;}
                default:
                    return -1;
            }
        }
        return lastReadBytePosition;
    }

    public static void main(String[] args) {

//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\20160627_183440_GT-I9195I-20160627_173440.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\DSC08806_bak_digi.jpg");
        File file  = new File("e:\\20180717_183213.jpg");
//        addBackupExif(file);
        file  = new File("e:\\20180717_183213bak_digi.jpg");

        if (!checkBackupExif(file)) {
        }
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\20181007_120044331_iOS.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\V6_K2018-06-1_6@19-5_7-24(-0500)(Sat)-ecb60326c6f29a67b8e39c1825cfc083-0-D5C04877.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\K2005-01-3_1@10-0_1-12(+0100)(Mon)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0001.jpg");
/*        final JPEGMediaFileStruct fileStruct = scan(file);
        fileStruct.drawMap();
        file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\DSC08806.jpg");
        addBackupExif(file);
        */
//        final JPEGMediaFileStruct fileStruct2 = scan(file);
//        fileStruct2.drawMap();
    }

    //TODO use it
    public static boolean addBackupExif(File file) {
        final JPEGMediaFileStruct fileStruct = scan(file);
        if (!fileStruct.isBackup()) {
            byte[] buffer = new byte[4096];
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                FileOutputStream fos = new FileOutputStream(file.getPath().concat(".bak"));
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for (JPEGSegment segment : fileStruct.getSegments()) {
                    long startAddress = segment.getStartAddress();
                    byte[] bytes = writeBytes(bis, bos, segment.getLength());
                    if (segment.getId().equals("Exif\0\0")) {
                        bytes = turnExiftoBackup(bytes);
                        bos.write(bytes);
                    }
                }

                bis.close();
                bos.close();
                return true;
            } catch (IOException e)
            {
                return false;
            }
        }
        return true;
    }

    public static boolean checkBackupExif(File file) {
        final JPEGMediaFileStruct fileStruct = scan(file);
        if (fileStruct.isBackup()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                byte[] exif = null;
                byte[] backup = null;
                for (JPEGSegment segment : fileStruct.getSegments()) {
                    byte[] segmentContent = BasicFileReader.readBytes(bis,(int) segment.getLength());
                    segment.getStartAddress();
                    if (segment.getId().equals("Exif\0\0")) {
                        exif = Arrays.copyOfRange(segmentContent, 4+6, (int) (segment.getLength()-4-6));
                    }
                    if (segment.getId().equals(BACKUPID)) {
                        backup = Arrays.copyOfRange(segmentContent, 4+6, (int) (segment.getLength()-4-6));
                    }
                }
                if (exif == null || backup == null) return false;
                if (Arrays.equals(exif, backup)) return true;
                else {
                    StringBuilder exifSb = new StringBuilder();
                    StringBuilder backupSb = new StringBuilder();
                    for (int i = 0; i < exif.length; i++) {
                        if (i+1 < exif.length && i+1 < backup.length && exif[i] != backup[i]) {
                            exifSb.append(new String(new byte[] {exif[i]}));
                            backupSb.append(new String(new byte[] {backup[i]}));
                            if (i+1 < exif.length && i+1 < backup.length && (exif[i+1] == backup[i+1])) {
                                exifSb.append(" : ");
                                backupSb.append(" : ");
                            }
                        }
                    }
                    System.out.println(exifSb.toString());
                    System.out.println("Backup");
                    System.out.println(backupSb.toString());
                }
            } catch (IOException e)
            {
                return false;
            }

        }
        return false;
    }

    private static byte[] turnExiftoBackup(byte[] bytes) {
        final byte[] chars = BACKUPID.getBytes();
        for (int i = 0; i < chars.length; i++) {
            bytes[i+4] = chars[i];
        }
        return bytes;
    }

    private static byte[] writeBytes(BufferedInputStream bis, BufferedOutputStream bos, long length) throws IOException {
        byte[] segment = new byte[0];
        int numBytes;
        byte[] buffer = new byte[4096];
        long read = 0;
        while ((numBytes = bis.read(buffer, 0, (int)(Math.min(buffer.length, length - read))))!= -1 && read < length)
        {
            read += numBytes;
            byte[] bufferfilled = Arrays.copyOfRange(buffer, 0, numBytes);
            bos.write(bufferfilled);
            segment = concatenate(segment, bufferfilled);
        }
        return segment;
    }

    public static byte[] concatenate(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }


    private static JPEGSegment readScan(BufferedInputStream in, AtomicInteger marker, long payLoadAddress) throws IOException {
        int segmentMarker = marker.get();
        long read = markerLength;
        int lastReadByte;
        Boolean markerFF = false;
        while ((lastReadByte = in.read()) != -1){
            read++;
            if (markerFF) {
                if (lastReadByte != 0x00 && !(0xD0 <= lastReadByte && lastReadByte <= 0xD7)) {
                    marker.set(lastReadByte);
                    break;
                } else {
                    markerFF = false;
                }
            } else if (lastReadByte == 0xFF/*255*/) {
                markerFF = true;
            }
        }
        if (lastReadByte == -1) return null;
        JPEGSegment segment = new JPEGSegment(payLoadAddress - markerLength, read - markerLength, segmentMarker);
        segment.addRead(read);
        return segment;
    }

    private static JPEGSegment readAPP(BufferedInputStream in, AtomicInteger marker, long payLoadAddress, File file) throws IOException {
        int segmentMarker = marker.get();
        long lengthPayload = 0;
        long read = markerLength;
        lengthPayload = 256*in.read() + in.read();
        read += 2;
        byte b[] = new byte[6];
        read += in.read(b, 0, 6);
/*
* APP1 “Exif\0\0” / "45786966 0000" -> TIFF structure
* APP1 „http://ns.adobe.com/xap/1.0/x00”
* APP1 "backup" Exif backup
* APP13 IPTC usually "Photoshop 3.0\000", but also 'Adobe_Photoshop2.5:',
* APP2 ICC 0..11 ICC_PROFILE\0 12 icc chunk-count 13 icc total chunks
* */
        JPEGSegment segment = new JPEGSegment(payLoadAddress - markerLength, lengthPayload + markerLength, segmentMarker);
        if (Arrays.equals(b, new byte[] {0x45,0x78,0x69,0x66,0x00,0x00})) {
            TIFFMediaFileStruct scan = TIFFHash.scan(file, payLoadAddress + read - markerLength, TIFFMediaFileStruct.MAPPING);
            segment.setData(scan);
        }
        String header = new String(b);
        segment.addRead(read);
        segment.setId(header);
        return segment;

    }

    private static JPEGSegment readSegment(BufferedInputStream in, AtomicInteger marker, long payLoadAddress, File file) throws IOException {
        int segmentMarker = marker.get();
        long lengthPayload = 0;
        long read = markerLength;
        if (208 <= segmentMarker && segmentMarker <= 217) {
            lengthPayload = 0;
        } else if (221 == segmentMarker) {
            lengthPayload = 4;
        } else if (218 == segmentMarker) {
            return readScan(in, marker, payLoadAddress);
        } else if (224 <= segmentMarker && segmentMarker <= 233) {
            return readAPP(in, marker, payLoadAddress, file);
        } else {
            lengthPayload = 256*in.read() + in.read();
            read += 2;
        }
        //payLoadAddress -2 because marker is already read
        //length should contain the header as well -> +2
        JPEGSegment segment = new JPEGSegment(payLoadAddress - markerLength, lengthPayload + markerLength, segmentMarker);
        segment.addRead(read);
        return segment;
    }

    public static JPEGMediaFileStruct scan(File file) {
        JPEGMediaFileStruct fileStruct = new JPEGMediaFileStruct(file);
        JPGCursor cursor = new JPGCursor(file);
        try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream);) {
            cursor.setBufferedInStream(fileStream);
            Integer lastReadByte = -1;
            boolean scan = false;
            OUTER:
            while ((lastReadByte = fileStream.read()) != -1) {
                cursor.position++;
                switch (lastReadByte) {
                    case 0:
                        break;
                    case 0xFF://255
                        lastReadByte = fileStream.read();
                        cursor.position++;
                        if (lastReadByte == 0xD8 /*216*/) {break OUTER;}
                    default:
                        fileStruct.setTerminationMessage("No \"Start Of Image\" found");
                        return fileStruct;
                }
            }
            fileStruct.addSegment(new JPEGSegment(cursor.position + 1 - markerLength, 0 + markerLength, 216));
            Boolean marker = false;
            while (scan || (lastReadByte = fileStream.read()) != -1) {
                if (!scan) {cursor.position++; }
                else {cursor.position +=2;}
                if (marker) {
                    //Not a marker(byte stuffing), shouldn't happen in header
                    if (lastReadByte == 0/* || lastReadByte==216*/) {
                        marker = false;
                        fileStruct.addWarningMessage("Strange bytestuffing/broken marker at: " + cursor.position);
                    } else {
                        if (scan) {
                            scan = false;
                        }
                        if (lastReadByte == 218) {
                            scan = true;
                        } else {
                            marker = false;
                        }
    //                    System.out.print("ff" + Integer.toHexString(lastReadByte) + " ");
                        AtomicInteger segmentMarker = new AtomicInteger(lastReadByte);
                        JPEGSegment segment = readSegment(fileStream, segmentMarker, cursor.position + 1, file);
                        if (segment == null) {
                            fileStruct.setTerminationMessage("Segment " + JPEGSegment.getMarkerText(lastReadByte) + " read error");
                            return fileStruct;
                        }
                        lastReadByte = segmentMarker.get();
                        fileStruct.addSegment(segment);
                        long bytesLeft = segment.getBytesLeft();
    //                    lastReadBytePosition += bytesLeft;
    //                    System.out.println(Long.toHexString(bytesLeft));
                        if (!BasicFileReader.skipBytes(fileStream, bytesLeft)) {
                            fileStruct.setTerminationMessage("Reached the end of the file during reading segment " + segment.getMarkerText() + " at " + segment.getStartAddress());
                            return fileStruct;
                        }
                        cursor.position += segment.getLength() - markerLength;
                        //SOS segment doesn't have a length; it will be read until the next marker which is then already loaded into the variables
                    }
                } else {
                    if (lastReadByte == 0xFF/*255*/) {marker = true;
    //                } else if (lastReadByte == 0) {//byte stuffing
                    } else if (lastReadByte == 0x00) {
                        //Padding
                    } else {
                        fileStruct.setTerminationMessage("Not found marker after segment");
                        return fileStruct;
                    }
                }
            }
            fileStruct.setTerminationMessage("Reached the end of the file");
        } catch (FileNotFoundException e) {
            fileStruct.setTerminationMessage("File couldn't be opened.");
        } catch (IOException e) {
            fileStruct.setTerminationMessage("IO error: "+ e.getMessage());
        }
        return fileStruct;
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
                    long nextHeaderAddress = 256*in.read() + in.read() - 2;
                    if (!BasicFileReader.skipBytes(in, nextHeaderAddress)) {
                        return -1;
                    }
                    j += nextHeaderAddress + 2;
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

    public static byte[] readDigest(File file, BufferedInputStream fileStream, MessageDigest md5Digest, DigestInputStream in) throws IOException {
        final JPEGMediaFileStruct fileStruct = scan(file);
        final JPEGSegment mainImage = fileStruct.getMainImage();
        if (mainImage == null) throw new IOException("No valid image found");
        in.on(false);
        md5Digest.reset();
        BasicFileReader.skipBytes(in, mainImage.getStartAddress() + markerLength);
        in.on(true);
        int c = 0;
        int oldc;
        do {
            oldc = c;
            c = in.read();
            if (c == -1) {
                throw new IOException("File ended unexpectedly");
            }
        } while (!(oldc == 0xFF && c == 0xD9/*217*/));
        return md5Digest.digest();
    }

    public static byte[] read_Digest(File file, BufferedInputStream fileStream, MessageDigest md5Digest, DigestInputStream in) throws IOException {
        byte[] digest = null;
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
                    throw new IOException("File ended unexpectedly");
                }
                scanLength ++;
            } while (!(oldc == 0xFF && c == 0xD9/*217*/));
            if (scanLength > scanLengthOld) {digest = md5Digest.digest(); scanLengthOld = scanLength;}
        } while (scanLengthOld < in.available());
        return digest;
    }
    
}
