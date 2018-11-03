/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.ExifUtils;

import static org.nyusziful.Main.StaticTools.ExifDateFormat;
import static org.nyusziful.Main.StaticTools.XmpDateFormatTZ;
import static org.nyusziful.Main.StaticTools.errorOut;
import static org.nyusziful.Main.StaticTools.getTimeFromStr;
import static org.nyusziful.Main.StaticTools.getZonedTimeFromStr;

import org.nyusziful.Rename.Meta;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.xmp.XmpDirectory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabor
 */
public class ExifReadWrite {

    /**
     * Reads the standard metadata from the specified files in the given directory
     * @param files list of the files
     * @param defaultTZ default time zone in case it can't be read from the filename
     * @return a list of the <code> Meta </code> objects for every file if the read was unsuccessful the note field of the object will contain the error message
     */
    public static ArrayList<Meta> readFileMeta(File[] files, ZoneId defaultTZ) {
        return ExifReadWriteIMR.readFileMeta(files, defaultTZ);
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
