package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MediafileDAOImplLocal {
/*    public MediaFile save(MediaFile item) {
        //TODO from global param
        String delimiter = "\t";
        int start = 0;
        final File outFile = new File("e:\\test2.csv");
        try (PrintWriter pw = new PrintWriter(outFile)) {
            StringBuilder sb = new StringBuilder();
            String hash = item.getFilehash();
            sb.append(start + "fileCount").append(delimiter);
            sb.append(hash).append(delimiter);
            sb.append(delimiter);
            sb.append(item.getDrive().getId()).append(delimiter);
            sb.append(item.getFilename()).append(delimiter);
            sb.append(item.getSize());
            sb.append('\n');
            pw.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return item;
    }
*/
}
