/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
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
