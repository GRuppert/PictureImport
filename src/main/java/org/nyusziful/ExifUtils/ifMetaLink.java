package org.nyusziful.ExifUtils;


import org.nyusziful.Rename.Meta;
import com.drew.imaging.ImageProcessingException;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public interface ifMetaLink {
    /**
     * Reads the standard metadata from the specified files in the given directory
     * @param filenames String list of the representation of the file names
     * @param dir the directory where the files are
     * @return a list of the <code> Meta </code> objects for every file if the read was unsuccessful the note field of the object will contain the error message
     */
    public Meta exifToMeta(File file, ZoneId zone);

    public ArrayList<String[]> readMeta(File file) throws ImageProcessingException, IOException;

    public File createXmp(File file);

    public ArrayList<String> getExif(String[] values, File file);

    public void updateExif(List<String> valuePairs, File directory);
}
