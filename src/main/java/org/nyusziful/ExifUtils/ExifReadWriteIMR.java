/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.ExifUtils;

import static org.nyusziful.Main.StaticTools.ExifDateFormat;
import static org.nyusziful.Main.StaticTools.XmpDateFormatTZ;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabor
 */
public class ExifReadWriteIMR {
    public static ArrayList<Meta> readFileMeta(File[] files, ZoneId defaultTZ) {
        ArrayList<Meta> results = new ArrayList<>();
        for (File file : files) {
            ArrayList<String[]> tags;
            String model = null;
            String note = "";
            String iID = null;
            String dID = null;
            String odID = null;
            String captureDate = null;
            ZonedDateTime wTZ = null;
            Boolean dateFormat = false;
            try {
                tags = readMeta(file);
            } catch (ImageProcessingException | IOException ex) {
                Meta meta = new Meta(file.getName(), getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, ex.toString(), null);
                System.out.println(meta);
                results.add(meta);
                continue;
            }
            for (String[] tag : tags) {
//                System.out.println(tag[0] + " : " + tag[1]);
                switch (tag[0]) {
                    case "Model":
                    case "tiff:Model":
                        model = tag[1];
                        break;
                    case "xmpMM:InstanceID":
                        iID = tag[1];
                        break;
                    case "xmpMM:DocumentID":
                        dID = tag[1];
                        break;
                    case "xmpMM:OriginalDocumentID":
                        odID = tag[1];
                        break;
                    case "Offset Time Original":
                    case "Unknown tag (0x9011)":
                        if (captureDate == null) captureDate = tag[1];
                        else captureDate += tag[1];
                        dateFormat = true;
                        break;
                    case "Date/Time Original":
                        if (captureDate == null) captureDate = tag[1];
                        else captureDate = tag[1] + captureDate;
                        if (wTZ != null && LocalDateTime.parse(captureDate, ExifDateFormat).equals(wTZ.toLocalDateTime()))
                            dateFormat = true;
                        break;
                    case "exif:DateTimeOriginal":
                        try {
                            wTZ = ZonedDateTime.parse(tag[1], XmpDateFormatTZ);
                            if ((captureDate != null && LocalDateTime.parse(captureDate, ExifDateFormat).equals(wTZ.toLocalDateTime()))
                                    || FilenameUtils.getExtension(file.getName().toLowerCase()).equals("xmp"))
                                dateFormat = true;
                        }
                        catch (DateTimeParseException exc) {
                        }
                        break;
                }
            }
            ZonedDateTime OrigDT = null;
            if (captureDate != null) {
                OrigDT = getZonedTimeFromStr(captureDate);
                if (OrigDT == null) OrigDT = getTimeFromStr(captureDate, defaultTZ);
            } else if (wTZ != null) {
                OrigDT = wTZ;
            }
            Meta meta = new Meta(file.getName(), OrigDT, dateFormat, model, iID, dID, odID, note, null);
            System.out.println(meta);
            results.add(meta);
        }
        return results;
    }


    //_tagType == exiftool TagId
    public static ArrayList<String[]> readMeta(File file) throws ImageProcessingException, IOException {
        //TODO mp4
        Metadata metadata;
        ArrayList<String[]> tags = new ArrayList();
        Collection<XmpDirectory> xmpDirectories = null;
        if (FilenameUtils.getExtension(file.getName().toLowerCase()).equals("xmp")) {
            XmpDirectory directory = new XmpDirectory();
            try {
                XMPMeta xmpMeta;
                // If all xmpBytes are requested, no need to make a new ByteBuffer
                xmpMeta = XMPMetaFactory.parse(new FileInputStream(file));
                directory.setXMPMeta(xmpMeta);
            } catch (XMPException e) {
                directory.addError("Error processing XMP data: " + e.getMessage());
            }
            ArrayList<XmpDirectory> collect = new ArrayList<>();
            collect.add(directory);
            xmpDirectories = collect;
        }   else {
            metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                if (!directory.getClass().equals(XmpDirectory.class))
                    for (Tag tag : directory.getTags()) {
                        String[] temp = {tag.getTagName(), tag.getDescription()};
                        String value = tag.getDescription();
                        if (value != null && !value.replaceAll("\\s+","").equals("")) tags.add(temp);
                    }
            }
            xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);
        }
        for (XmpDirectory xmpDirectory : xmpDirectories) {
            XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
            XMPIterator iterator;
            try {
                iterator = xmpMeta.iterator();
                while (iterator.hasNext()) {
                    XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo)iterator.next();
                    if (xmpPropertyInfo.getPath() != null && xmpPropertyInfo.getValue() != null && !xmpPropertyInfo.getValue().replaceAll("\\s+","").equals("")) {
                        String[] temp = {xmpPropertyInfo.getPath(), xmpPropertyInfo.getValue()};
                        tags.add(temp);
                    }
                }
            } catch (XMPException ex) {
            }
        }
        return tags;
    }

    public static ArrayList<String[]> readMetaNew(File file) throws ImageProcessingException, IOException {
        ArrayList<String[]> tags = new ArrayList();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        metadata.getDirectories().forEach(directory -> directory.getTags().stream().forEach(tag -> tags.add(new String[]{tag.getTagName(), tag.getDescription()})));
        return tags;
    }

}
