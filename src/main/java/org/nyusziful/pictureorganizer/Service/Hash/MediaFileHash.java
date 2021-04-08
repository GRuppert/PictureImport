/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.*;
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
        MP4 ("mp4", new String[] {"mp4", "3gp"}),
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

    public static void main(String[] args) {
        ImageDTO hashJ = getHash(new File("H:\\Képek\\Photos\\Új\\Frankfurt\\5100\\PRIVATE\\M4ROOT\\CLIP\\C0006.MP4"));
/*
        ImageDTO hashJ = getHash(new File("G:\\Képek\\Photos\\Regi Kepek\\Meg nem\\!Válogatós\\Gabus\\!IMAG\\2005-07-04 - 2005-07-04\\x\\IMAG0001.JPG"));
        ImageDTO hash = getHash(new File("e:\\Work\\Testfiles\\failingmp4\\diff\\2\\V6_K2017-08-3_1@16-2_6-35(+0200)(Thu)-2104cc0ae509423700f1c0a6695f76f0-0-C0018.MP4"));
        ImageDTO hash2 = getHash(new File("e:\\Work\\Testfiles\\failingmp4\\diff\\1\\V6_K2017-08-3_1@16-2_6-35(+0200)(Thu)-d2054e7f9e485f415a5e7bcc8b35b835-0-C0018.MP4"));

*/
        System.out.println(hashJ.hash);
//        System.out.println(hash2.hash);
    }
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
        byte[] fileContent = null;
        try (FileInputStream fileStream = new FileInputStream(file.toString()); DigestInputStream in = new DigestInputStream(fileStream, md5Digest)) {
            in.on(true);
            if (file.length() < Integer.MAX_VALUE - 5) {
                fileContent = new byte[in.available()];
                int read = in.read(fileContent);
                if (read == -1) {
                    throw new IOException("File ended unexpectedly");
                }
            } else {
                byte[] buffer = new byte[4096];
                while (in.read(buffer) != -1) {}
            }
            in.on(false);
            imageDTO.fullhash = processHash(md5Digest.digest());
            md5Digest.reset();
        } catch (Exception ex) {
            LOG.warn("Returning EMPTYHASH: " + ex.getMessage());
            return imageDTO;
        }

        Type type = Type.getType(file);
        imageDTO.type = type.getDBName();
        if (fileContent != null) {
            try {
                getDigest(type, fileContent, imageDTO);
            }  catch(Exception e) {
//            errorOut("Hash", e);
                return imageDTO;
            }
        }
        return imageDTO;
    }

    private static void getDigest(Type type, byte[] fileContent, ImageDTO imageDTO) throws IOException {

        switch (type) {
            case TIFF:
                TIFFHash.readDigest(fileContent, imageDTO);
                break;
            case JPG:
                JPGHash.readDigest(fileContent, imageDTO);
                break;
            case MP4:
                MP4Hash.readDigest(fileContent, imageDTO);
                break;
/*                case "heif":
                case "heifs":
                case "heic":
                case "heics":
                case "avci":
                case "avcs":
                    break;*/
        }
    }

    protected static String processHash(byte[] digest) {
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
