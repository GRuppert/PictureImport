/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.*;

/**
 *
 * @author gabor
 */
public class BasicFileReader {
    public static boolean BIGENDIAN = true;
    public static boolean LITTLEENDIAN = false;

    public static boolean reset(InputStream in, long pointer, File file) throws IOException {
        if (in.markSupported()) {in.reset(); return true;}
        else {
            FileInputStream fileInStream = new FileInputStream(file.toString());
            in = new BufferedInputStream(fileInStream);
            return skipBytes(in, pointer);
        }
    }

    public static boolean skipBytes(InputStream in, long pointer) throws IOException {
        while (pointer > 0) {
            long skip = in.skip(pointer);
            if (skip < 1)
                return false;
            pointer -= skip;
        }
        return true;
    }
    
    public static byte[] readBytes(InputStream in, int size) throws IOException {
        byte result[] = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte)in.read();
        }
        return result;
    }
    
    public static long readEndianValue(InputStream in, int length, boolean endian) throws IOException {
        long result = 0;
        for(int i=0; i<length; i++) {
            long c = in.read();
            if (endian) {
                c = (long) (c * Math.pow(256, i));
            } else {
                c = (long) (c * Math.pow(256, length-1-i));
            }
            result += c;
        }
        return result;
    }

    public static long readEndianValue(RandomAccessStream in, int length, boolean endian) throws IOException {
        long result = 0;
        for(int i=0; i<length; i++) {
            long c = in.read();
            if (endian) {
                c = (long) (c * Math.pow(256, i));
            } else {
                c = (long) (c * Math.pow(256, length-1-i));
            }
            result += c;
        }
        return result;
    }

}
