/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Hash;

import static org.nyusziful.Main.StaticTools.errorOut;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author gabor
 */
public class MediaFileHash {
    public static String EMPTYHASH = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final Logger LOG = LogManager.getLogger(MediaFileHash.class);
    
    private static String getType(File file) {
        String ext = FilenameUtils.getExtension(file.getName().toLowerCase());
        switch (ext) {
            case "mp4":
                return "mp4";
            case "jpg":
            case "jpeg":
                return "jpeg";
            case "arw":
            case "dng":
            case "nef":
            case "tif":
            case "tiff":
                return "tiff";
        }
        return "";
    }
   
    /**
     * 
     * @param file
     * @return the Hash of the media data in the file for known file types
     */
    public static String getHash(File file) {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {            
            switch (getType(file)) {
                case "tiff":
                    digest = TIFFHash.readDigest(file, fileStream);
                    break;
                case "jpeg":
                    digest = JPGHash.readDigest(file, fileStream, md5Digest, in);
                    break;
                case "mp4":
                    digest = MP4Hash.readDigest(file, fileStream, md5Digest, in);
                    break;
            }
        }  catch(IOException e) {
            errorOut("Hash", e);         
        } 
        if (digest == null) {
            return EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            return EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    
    private static String getFullHashPS(File file) throws FileNotFoundException, IOException {
        String[] parameters = new String[]{"powershell.exe", "Get-ChildItem", "-File", "-Recurse", "|", "Get-FileHash", "-Algorithm", "MD5", ">>", "e:\\ps.md5"};
        String lines = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(parameters, null, file);
/*            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(() -> {
                try {
                    String s;
                    while (( s=stdinReader.readLine()) != null) {
                        System.out.println(s);
                    }
                }
                catch(IOException e) {
                }
            }).start();
            new Thread(() -> {
                try {
                    String s;
                    while (( s=stderrReader.readLine()) != null) {
                        System.out.println(s);
                    }
                }
                catch(IOException e) {
                }
            }).start();*/
            int returnVal = p.waitFor();
        } catch (Exception e) {
            errorOut("xmp", e);
        } 
        return lines;

    }
    
    /**
     * 
     * @param file
     * @return MD5 hash of the whole file 
     */
    public static String getFullHash(File file) {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file.toString())); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {            
            byte[] buffer = new byte[4096];
            in.on(true);
            while (in.read(buffer) != -1) {}
            digest = md5Digest.digest();
        } catch (IOException ex) {
            LOG.warn(() -> "Returning EMPTYHASH: " + ex.getMessage());
            return EMPTYHASH;
        }
        if (digest == null) {
            LOG.warn(() -> "Returning EMPTYHASH: no bytes read");
            return EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            LOG.warn(() -> "Returning EMPTYHASH: no bytes read");
            return EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
}
