package org.nyusziful.pictureorganizer.Service.Hash;

import javafx.util.Pair;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.UI.StaticTools;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class VerifyHash {
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        File folder = new File(property);
        folder = new File("e:\\Work\\Testfiles\\JPG");
        File input = new File(property+"\\imageHash.txt");
        HashMap<String, Pair<String, String>> previous = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(input))) {
            String line;
            while((line=bufferedReader.readLine())!=null) {
                if (line.startsWith("Image Hash:")) {
                    previous.put(line.substring(87), new Pair<>(line.substring(11, 43), line.substring(54, 86)));
                }
            }
        } catch (Exception ex) {
        }
        StringBuilder sb = new StringBuilder();
        for (File file : folder.listFiles(a-> StaticTools.supportedFileType(a))) {
            final ImageDTO hash = MediaFileHash.getHash(file);
            String fileName = file.getName();
            Pair<String, String> hashes = previous.get(fileName);
            previous.remove(fileName);
            if (hashes == null) sb.append("new");
            else {
                sb.append("image ").append(hashes.getKey().equals(hash.hash) ? "matching" : "CHANGED");
                sb.append(" exif ").append(hashes.getValue().equals(hash.exifHash) ? "matching" : "CHANGED");
            }
            sb.append(" " + fileName + "\n");
        }
        for (String s : previous.keySet()) {
            sb.append("Missing " + s + "\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
