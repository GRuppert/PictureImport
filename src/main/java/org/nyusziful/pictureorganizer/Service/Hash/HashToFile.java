package org.nyusziful.pictureorganizer.Service.Hash;

import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.UI.StaticTools;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class HashToFile {
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        File folder = new File(property);
        File output = new File(property+"\\imageHash.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(output)) {
            for (File file : folder.listFiles(a-> StaticTools.supportedFileType(a))) {
                final ImageDTO hash = MediaFileHash.getHash(file);
                String outline = file.getName() + " Image Hash:" + hash.hash + "\n";
                fileOutputStream.write(outline.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
        }
    }
}
