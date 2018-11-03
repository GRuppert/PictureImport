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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author gabor
 */
public class JPGHash implements Hasher {
    private static final Logger LOG = LogManager.getLogger(JPGHash.class);
    
    private static long startOfImageJPG(BufferedInputStream in) throws IOException {
        int c;
        long j = -1;
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

    public static void main(String[] args) {
        File file  = new File("E:\\work\\JAVA\\pictureOrganizer\\pictureOrganizer\\src\\test\\resources\\K2005-01-3_1@10-0_1-12(+0100)(Mon)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-IMAG0001.jpg");
        try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream);) {
            JPEGFileStruct fileStruct = new JPEGFileStruct(file);
            scanJPG(fileStream, fileStruct, file);
            fileStruct.drawMap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JPEGSegment readScan(BufferedInputStream in, Integer marker, long address) throws IOException {
        int segmentMarker = marker;
        long lengthPayload = 0;
        long read = 2;
        int c;
        Boolean markerFF = false;
        while ((c = in.read()) != -1){
            read++;
            if (markerFF) {
                if (c != 0x00) {
                    lengthPayload = read - 2;
                    marker = c;
                    break;
                } else {
                    markerFF = false;
                }
            } else if (c == 0xFF/*255*/) {
                markerFF = true;
            }
        }
        if (c == -1) return null;
        JPEGSegment segment = new JPEGSegment(address-2, lengthPayload + 2, segmentMarker);
        segment.addRead(read);
        return segment;
    }

    private static JPEGSegment readAPP(BufferedInputStream in, Integer marker, long address, File file) throws IOException {
        int segmentMarker = marker;
        long lengthPayload = 0;
        long read = 2;
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
//            TIFFHash.readDigest(file, in);
        }
        String header = new String(b);
        JPEGSegment segment = new JPEGSegment(address-2, lengthPayload + 2, segmentMarker);
        segment.addRead(read);
        segment.setId(header);
        return segment;

    }


    private static JPEGSegment readSegment(BufferedInputStream in, Integer marker, long address, File file) throws IOException {
        int segmentMarker = marker;
        long lengthPayload = 0;
        long read = 2;
        if (208 <= marker && marker <= 217) {
            lengthPayload = 0;
        } else if (221 == marker) {
            lengthPayload = 4;
        } else if (218 == marker) {
            return readScan(in, marker, address);
        } else if (224 <= marker && marker <= 233) {
            return readAPP(in, marker, address, file);
        } else {
            lengthPayload = 256*in.read() + in.read();
            read += 2;
        }
        //address -2 because marker is already read
        //length should contain the header as well -> +2
        JPEGSegment segment = new JPEGSegment(address-2, lengthPayload + 2, segmentMarker);
        segment.addRead(read);
        return segment;
    }

    private static void scanJPG(BufferedInputStream in, JPEGFileStruct fileStruct, File file) throws IOException {
        Integer c = -1;
        boolean scan = false;
        long j = startOfImageJPG(in);
        if (j == -1) {fileStruct.setTerminationMessage("No \"Start Of Image\" found"); return;}
        Boolean marker = false;
        while (scan || (c = in.read()) != -1) {
            if (marker) {
                //Not a marker(byte stuffing), shouldn't happen in header
                j++;
                if (c == 0/* || c==216*/) {
                    marker = false;
                    fileStruct.addWarningMessage("Strange bytestuffing/broken marker at: " + j);
                } else {
                    if (c == 217) {
                        scan = true;
                    } else {
                        marker = false;
                    }
//                    System.out.print("ff" + Integer.toHexString(c) + " ");
                    JPEGSegment segment = readSegment(in, c, j, file);
                    if (segment == null) {
                        fileStruct.setTerminationMessage("Segment " + JPEGSegment.getMarker(c) + " read error");
                        return;
                    }
                    fileStruct.addSegment(segment);
                    long bytesLeft = segment.getBytesLeft();
                    j += bytesLeft;
//                    System.out.println(Long.toHexString(bytesLeft));
                    do {
                        long skip = in.skip(bytesLeft);
                        if (skip < 0) {
                            fileStruct.setTerminationMessage("Reached the end of the file during reading segment " + segment.getMarker() + " at " + segment.getStartAddress());
                            return;
                        }
                        bytesLeft -= skip;
                    } while (bytesLeft > 0);
                    //SOS segment doesn't have a length; it will be read until the next marker which is then already loaded into the variables
                }
            } else {
                if (c == 0xFF/*255*/) {marker = true; j++;
//                } else if (c == 0) {//byte stuffing
                } else {
                    fileStruct.setTerminationMessage("Not found marker after segment");
                    return;
                }
            }
            j++;
        }
        fileStruct.setTerminationMessage("Reached the end of the file");
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
