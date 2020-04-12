/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.Service.ExifUtils;

import com.drew.imaging.avi.AviMetadataReader;
import com.drew.imaging.bmp.BmpMetadataReader;
import com.drew.imaging.gif.GifMetadataReader;
import com.drew.imaging.heif.HeifMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.imaging.png.PngMetadataReader;
import com.drew.imaging.quicktime.QuickTimeMetadataReader;
import com.drew.imaging.tiff.TiffMetadataReader;
import org.nyusziful.pictureorganizer.DTO.Meta;
import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPIterator;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.xmp.XmpDirectory;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.FilenameUtils;

import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

/**
 *
 * @author gabor
 */
public class ExifReadWriteIMR {
    public static ArrayList<Meta> readFileMeta(File[] files, ZoneId defaultTZ) {
        ArrayList<Meta> results = new ArrayList<>();
        for (File file : files) {
            results.add(readFileMeta(file, defaultTZ));
        }
        return results;
    }

    public static Meta readFileMeta(File file, ZoneId defaultTZ) {
        ArrayList<String[]> tags;
        String model = null;
        String note = "";
        String iID = null;
        String dID = null;
        String odID = null;
        String captureDate = null;
        ZonedDateTime wTZ = null;
        Boolean dateFormat = false;
        String orig = null;
        String quality = null;
        try {
            tags = readMeta(file);
        } catch (ImageProcessingException | IOException ex) {
            Meta meta = new Meta(file.getName(), getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, ex.toString(), orig, quality);
            System.out.println(meta);
            return meta;
        }
        for (String[] tag : tags) {
//                System.out.println(tag[0] + " : " + tag[1]);
            switch (tag[0]) {
                case "Model":
                case "tiff:Model":
                case "Device Model Name":
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
//                    else captureDate += tag[1];
                    dateFormat = true;
                    break;
                case "Date/Time Original":
                    if (captureDate == null) captureDate = tag[1];
//                    else captureDate = tag[1] + captureDate;
                    if (wTZ != null && LocalDateTime.parse(captureDate, ExifDateFormat).equals(wTZ.toLocalDateTime()))
                        dateFormat = true;
                    break;
                case "Creation Time":
                    if (FilenameUtils.getExtension(file.getName().toLowerCase()).equals("mp4"))
                        try {
                            wTZ = ZonedDateTime.parse(tag[1], MP4DateFormatTZ);
                        } catch (DateTimeParseException exc) {
                        }
                case "exif:DateTimeOriginal":
                    try {
                        wTZ = ZonedDateTime.parse(tag[1], XmpDateFormatTZ);
                        if ((captureDate != null && LocalDateTime.parse(captureDate, ExifDateFormat).equals(wTZ.toLocalDateTime()))
                                || FilenameUtils.getExtension(file.getName().toLowerCase()).equals("xmp"))
                            dateFormat = true;
                    } catch (DateTimeParseException exc) {
                    }
                    break;
                case "Image Quality":
                    quality = tag[1];
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
        Meta meta = new Meta(file.getName(), OrigDT, dateFormat, model, iID, dID, odID, note, orig, quality);
        System.out.println(meta);
        return meta;
    }


    //_tagType == exiftool TagId
    public static ArrayList<String[]> readMeta(File file) throws ImageProcessingException, IOException {
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
        } else {
            metadata = readDrew(file);
            for (Directory directory : metadata.getDirectories()) {
                if (!directory.getClass().equals(XmpDirectory.class))
                    for (Tag tag : directory.getTags()) {
                        String[] temp = {tag.getTagName(), tag.getDescription()};
                        String value = tag.getDescription();
                        if (value != null && !value.replaceAll("\\s+", "").equals("")) tags.add(temp);
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
                    XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo) iterator.next();
                    if (xmpPropertyInfo.getPath() != null && xmpPropertyInfo.getValue() != null && !xmpPropertyInfo.getValue().replaceAll("\\s+", "").equals("")) {
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
        Metadata metadata = readDrew(file);
        metadata.getDirectories().forEach(directory -> directory.getTags().stream().forEach(tag -> {
            if (tag.getDescription() != null && !tag.getDescription().replaceAll("\\s+", "").equals(""))
                tags.add(new String[]{tag.getTagName(), tag.getDescription()});
        }));
        return tags;
    }

    private static Metadata readDrew(File file) throws ImageProcessingException, IOException {
        String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
        switch(ext) {
            case "jpeg":
            case "jpg":
                return JpegMetadataReader.readMetadata(file);
            case "tiff":
            case "tif":
            case "arw":
            case "dng":
            case "nef":
                return TiffMetadataReader.readMetadata(file);
            case "png":
                return PngMetadataReader.readMetadata(file);
            case "bmp":
                return BmpMetadataReader.readMetadata(file);
            case "gif":
                return GifMetadataReader.readMetadata(file);
            case "avi":
                return AviMetadataReader.readMetadata(file);
            case "mov":
                return QuickTimeMetadataReader.readMetadata(file);
            case "mp4":
                return Mp4MetadataReader.readMetadata(file);
            case "heif":
                return HeifMetadataReader.readMetadata(new FileInputStream(file));
            default:
                throw new ImageProcessingException("File format could not be determined");
        }
    }
}
