/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.ExifUtils;

import org.nyusziful.pictureorganizer.DTO.Meta;
import com.adobe.internal.xmp.XMPException;
import com.drew.imaging.ImageProcessingException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


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

    public static Meta readMeta(InputStream inputStream, String name, ZoneId defaultTZ) {
        return ExifReadWriteIMR.readFileMeta(inputStream, name, defaultTZ);
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

    public static Boolean originalJPG(File file) {return ExifReadWriteIMR.originalJPG(file);
    }

    /**
     * Appends a provenance history event to the XMP metadata of {@code file}.
     *
     * @param file                  target file — {@code .xmp} sidecar or image (JPG etc.)
     * @param ver         agent string, e.g. {@code "PictureOrganizer/2.0"}
     * @param changes               field-level deltas; use {@code _contentHash} synthetic entry at IMPORT/VIDEO_TRIM
     * @param previousCanonicalHash hash before this write; supply at IMPORT, {@code null} otherwise
     */
    public static void addXmpHistoryEvent(File file,
                                          String ver,
                                          List<MetadataChange> changes,
                                          String previousCanonicalHash)
            throws IOException, XMPException {
        XmpHistoryWriter.addEvent(file, ver, changes, previousCanonicalHash);
    }

    public static void main(String[] args) {
        Meta meta = readMeta(new File("G:\\Pictures\\Photos\\Régi képek\\Original\\Idozona\\2016-03-25 - 2016-04-17 Japán\\Európa\\20160324_060559_GT-I9195I-20160324_060559.jpg"), ZoneId.systemDefault());
        System.out.println(meta);
    }

}
