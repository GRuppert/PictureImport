/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

import java.io.*;
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

        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\20160627_183440_GT-I9195I-20160627_173440.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\DSC08806.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\20181007_120044331_iOS.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\V6_K2018-06-1_6@19-5_7-24(-0500)(Sat)-ecb60326c6f29a67b8e39c1825cfc083-0-D5C04877.jpg");
//        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\K2005-01-3_1@10-0_1-12(+0100)(Mon)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0001.jpg");
        final JPEGFileStruct fileStruct = scanJPG(file);
        fileStruct.drawMap();
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
* APP13 IPTC usually "Photoshop 3.0\000", but also 'Adobe_Photoshop2.5:',
* APP2 ICC 0..11 ICC_PROFILE\0 12 icc chunk-count 13 icc total chunks
* */
        if (Arrays.equals(b, new byte[] {0x45,0x78,0x69,0x66,0x00,0x00})) {
            TIFFHash.readDigest(file, in);
        }
        String header = new String(b);
        JPEGSegment segment = new JPEGSegment(payLoadAddress - markerLength, lengthPayload + markerLength, segmentMarker);
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

    public static JPEGFileStruct scanJPG(File file) {
        JPEGFileStruct fileStruct = new JPEGFileStruct(file);
        JPGCursor cursor = new JPGCursor(file);
        try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream, 65000);) {
            cursor.setBufferedInStream(fileStream);
            Integer lastReadByte = -1;
            boolean scan = false;
            cursor.position = startOfImageJPG(fileStream);
            if (cursor.position == -1) {fileStruct.setTerminationMessage("No \"Start Of Image\" found"); return fileStruct;}
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
                            fileStruct.setTerminationMessage("Segment " + JPEGSegment.getMarker(lastReadByte) + " read error");
                            return fileStruct;
                        }
                        lastReadByte = segmentMarker.get();
                        fileStruct.addSegment(segment);
                        long bytesLeft = segment.getBytesLeft();
    //                    lastReadBytePosition += bytesLeft;
    //                    System.out.println(Long.toHexString(bytesLeft));
                        do {
                            long skip = fileStream.skip(bytesLeft);
                            if (skip < 0) {
                                fileStruct.setTerminationMessage("Reached the end of the file during reading segment " + segment.getMarker() + " at " + segment.getStartAddress());
                                return fileStruct;
                            }
                            bytesLeft -= skip;
                        } while (bytesLeft > 0);
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
//                    System.out.print("ff" + Integer.toHexString(c) + " ");
                    long nextHeaderAddress = 256*in.read() + in.read() - 2;
//                    System.out.println(Long.toHexString(nextHeaderAddress));
                    do {
                        long skip = in.skip(nextHeaderAddress);
                        if (skip < 0) return -1;
                        nextHeaderAddress -= skip;
                    } while (nextHeaderAddress > 0);
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
