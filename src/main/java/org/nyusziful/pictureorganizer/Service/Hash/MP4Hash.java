/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.nyusziful.pictureorganizer.Service.Hash.BasicFileReader.readBytes;
import static org.nyusziful.pictureorganizer.Service.Hash.BasicFileReader.readEndianValue;
import static org.nyusziful.pictureorganizer.Service.Hash.BasicFileReader.skipBytes;


/**
 *
 * @author gabor
 */
public class MP4Hash implements Hasher {
    private static final Logger LOG = LoggerFactory.getLogger(MP4Hash.class);

    public static byte[] readDigest(File file, BufferedInputStream fileStream, MessageDigest md5Digest, DigestInputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        long length;
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
            } else if (length >= in.available()) {
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
                    if (read == -1) {throw new IOException("File ended unexpectedly");}
                    length -= read;
                }
                if (in.available() < 1) break;
            } else {
                if (!skipBytes(in, length)) break;
            }
        } while(!EOF);    
        return md5Digest.digest();
    }
}
