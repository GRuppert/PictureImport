/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Hash;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.UI.StaticTools;


/**
 *
 * @author gabor
 */
public class MediaFileHash {
    public static String EMPTYHASH = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public enum Type {
        MP4 ("mp4", new String[] {"mp4"}),
        JPG ("jpg", new String[] {"jpg", "jpeg"}),
        TIFF ("tif", new String[] {"arw", "dng", "nef", "tif", "tiff"}),
        UNKNOWN ("n/a", new String[] {});

        private final String name;
        private final String[] extensions;

        Type(String name, String[] extensions) {
            this.name = name;
            this.extensions = extensions;
        }

        public String getDBName() {
            return name;
        }

        public static Type getType(File file) {
            String ext = FilenameUtils.getExtension(file.getName().toLowerCase());
            for (Type type : Type.values()) {
                for (String extension : type.extensions) {
                    if (extension.equals(ext)) return type;
                }
            }
            return UNKNOWN;
        }
    };

    private static final Logger LOG = LoggerFactory.getLogger(MediaFileHash.class);

    /**
     * 
     * @param file
     * @return the Hash of the media data in the file for known file types
     */
    public static ImageDTO getHash(File file) {
        MessageDigest md5Digest = null;
        ImageDTO imageDTO = new ImageDTO();
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return imageDTO;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        Type type = Type.UNKNOWN;
        try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {
            type = Type.getType(file);
            imageDTO.type = type.getDBName();
            switch (type) {
                case TIFF:
                    digest = TIFFHash.readDigest(file, fileStream);
                    break;
                case JPG:
                    digest = JPGHash.readDigest(file, fileStream, md5Digest, in);
                    break;
                case MP4:
                    digest = MP4Hash.readDigest(file, fileStream, md5Digest, in);
                    break;
/*                case "heif":
                case "heifs":
                case "heic":
                case "heics":
                case "avci":
                case "avcs":
                    break;*/
            }
        }  catch(IOException e) {
//            errorOut("Hash", e);
            return imageDTO;
        }
        if (digest == null) {
            return imageDTO;
        }
        if (Arrays.equals(digest, digestDef)) {
            return imageDTO;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        imageDTO.hash = sb.toString();
        return imageDTO;
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
            StaticTools.errorOut("xmp", e);
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
            LOG.warn("Returning EMPTYHASH: " + ex.getMessage());
            return EMPTYHASH;
        }
        if (digest == null) {
            LOG.warn("Returning EMPTYHASH: no bytes read");
            return EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            LOG.warn("Returning EMPTYHASH: no bytes read");
            return EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
}
