package org.nyusziful.pictureorganizer.Service.Hash;

import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class HashToFile {
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        File folder = new File(property);
        File output = new File(property+"\\imageHash.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(output)) {
            for (File file : folder.listFiles()) {
                final ImageDTO hash = MediaFileHash.getHash(file);
                fileOutputStream.write((file.getName() + " Image Hash:" + hash.hash + "\n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {

        }
    }
}
