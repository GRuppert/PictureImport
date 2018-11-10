/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

import java.io.*;

/**
 *
 * @author gabor
 */
public class BasicFileReader {
    public static boolean BIGENDIAN = true;
    public static boolean LITTLEENDIAN = true;

    public static boolean reset(InputStream in, long pointer, File file) throws IOException {
        if (in.markSupported()) {in.reset(); return true;}
        else {
            FileInputStream fileInStream = new FileInputStream(file.toString());
            in = new BufferedInputStream(fileInStream);
            return skipBytes(in, pointer);
        }
    }

    public static boolean skipBytes(InputStream in, long pointer) throws IOException {
        do {
            long skip = in.skip(pointer);
            if (skip < 0) return false;
            pointer -= skip;
        } while (pointer > 0);
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

}
