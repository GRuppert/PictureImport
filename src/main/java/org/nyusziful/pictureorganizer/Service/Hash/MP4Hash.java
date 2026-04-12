/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.nyusziful.pictureorganizer.DTO.ImageDTO;
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

    public static void readDigest(byte[] fileContent, ImageDTO imageDTO) throws IOException {
        MessageDigest md5Digest = null;
         try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return;
        }
        try (DigestInputStream in = new DigestInputStream(new ByteArrayInputStream(fileContent), md5Digest)) {
            byte[] digestDef = md5Digest.digest();
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
                        if (read == -1) {
                            throw new IOException("File ended unexpectedly");
                        }
                        length -= read;
                    }
                    while (length > 0) {
                        int read = in.read(buffer, 0, (int)length);
                        if (read == -1) {throw new IOException("File ended unexpectedly");}
                        length -= read;
                    }
                    in.on(false);
                    if (in.available() < 1) break;
/*
            } else if (desc.equals("meta")) {
                imageDTO.exif = new byte[(int)length];
                int read = in.read(imageDTO.exif, 0, (int)length);
                if (read == -1) {throw new IOException("File ended unexpectedly");}
                if (read != length) {throw new IOException("Read error");}
*/
                } else {
                    if (!skipBytes(in, length)) break;
                }
            } while(!EOF);
            byte[] digest = md5Digest.digest();
            if (digest != null && !Arrays.equals(digest, digestDef)) {
                imageDTO.hash = MediaFileHash.processHash(digest);
            }
        }  catch(Exception e) {
        }
    }
}
