/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.ExifUtils;

import org.nyusziful.pictureorganizer.DTO.Meta;
import com.drew.imaging.ImageProcessingException;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.nyusziful.pictureorganizer.UI.StaticTools.getFile;
import static org.nyusziful.pictureorganizer.UI.StaticTools.getZonedTimeFromStr;

/**
 *
 * @author gabor
 */
public class ExifService {

    /**
     * Reads the standard metadata from the specified files in the given directory
     * @param files list of the files
     * @param defaultTZ default time zone in case it can't be read from the filename
     * @return a list of the <code> Meta </code> objects for every file if the read was unsuccessful the note field of the object will contain the error message
     */
    public static ArrayList<Meta> readFileMeta(File[] files, ZoneId defaultTZ) {
        return ExifReadWriteIMR.readFileMeta(files, defaultTZ);
    }

    public static Meta readMeta(File file, ZoneId defaultTZ) {
        return ExifReadWriteIMR.readFileMeta(file, defaultTZ);
    }

    public static ArrayList<String[]> readMeta(File file) throws ImageProcessingException, IOException {
        return ExifReadWriteIMR.readMeta(file);
    }

    public static ArrayList<String> getExif(String[] values, File file) {
        return ExifReadWriteET.getExif(values, file);
    }

    public static File createXmp(File file) {
        return ExifReadWriteET.createXmp(file);
    }

    public static void updateExif(List<String> valuePairs, File directory) {
        ExifReadWriteET.updateExif(valuePairs, directory);
    }
}
