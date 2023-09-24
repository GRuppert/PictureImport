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
import com.drew.metadata.*;
import com.drew.metadata.mp4.Mp4Directory;
import org.nyusziful.pictureorganizer.DTO.Meta;
import com.adobe.internal.xmp.XMPException;
import com.adobe.internal.xmp.XMPIterator;
import com.adobe.internal.xmp.XMPMeta;
import com.adobe.internal.xmp.XMPMetaFactory;
import com.adobe.internal.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.xmp.XmpDirectory;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

    public static void main(String[] args) {
        readFileMeta(new File("t:\\G\\Pictures\\Photos\\V5\\Dupla\\KépekÚj\\2015-07-25 - 2015-07-25 Strasburg\\V5_K2015-07-2_5@12-3_2-49(+0200)(Sat)-9177e64514ec8ffd9deb8ff3d3fa0c5b-9e38b415d46f913693442b15012ab931-DSC09457.JPG"), ZoneId.systemDefault());

/*
        readFileMeta(new File("e:\\Work\\Testfiles\\failingmp4\\diff\\2\\V6_K2017-08-3_1@16-2_6-35(+0200)(Thu)-2104cc0ae509423700f1c0a6695f76f0-0-C0018.MP4"), ZoneId.systemDefault());
        readFileMeta(new File("E:\\Work\\Testfiles\\DSC07666.jpg"), ZoneId.systemDefault());
        readFileMeta(new File("G:\\Pictures\\Photos\\DBSaved\\2017-12-31 - 2018-01-01 Europapark szilveszter\\V6_K2017-12-3_1@11-2_3-40(+0100)(Sun)-6d6d425fbb10fd44c077ca69af817ab3-0-C0001.mp4"), ZoneId.systemDefault());
        readFileMeta(new File("G:\\Pictures\\Photos\\Új\\Sustenpass\\2019_0922_150428_412.mov"), ZoneId.systemDefault());
        readFileMeta(new File("G:\\Pictures\\Photos\\Új\\14584100.avi"), ZoneId.systemDefault());
        readFileMeta(new File("G:\\Pictures\\Photos\\Régi képek\\Szelektálás\\!Válogatós\\Gabus\\!IMAG\\2007-05-05 - 2007-05-05\\MPG_0008.mpg"), ZoneId.systemDefault());
        readFileMeta(new File("G:\\Pictures\\Photos\\Régi képek\\Original\\Közös\\2016-06-19 - 2016-06-19 Streetworkout\\K2016-06-1_9@11-1_0-02(+0200)(Sun)-d41d8cd98f00b204e9800998ecf8427e-d41d8cd98f00b204e9800998ecf8427e-00041.mts"), ZoneId.systemDefault());
        readFileMeta(new File("G:\\Pictures\\Photos\\Régi képek\\Szelektálás\\!Válogatós\\Gabus\\20101121 Düsseldorf\\Modern\\VID_20101120_120509.3gp"), ZoneId.systemDefault());
*/
    }

    public static Meta readFileMeta(File file, ZoneId defaultTZ) {
        String name = file.getName();
        try {
            return readFileMeta(new FileInputStream(file), name, defaultTZ);
        } catch (IOException ex) {
            Meta meta = new Meta();
            return meta;
        }
    }

    public static Meta readFileMeta(InputStream inputStream, String name, ZoneId defaultTZ) {
        ArrayList<String[]> tags;
        String model = null;
        String iID = null;
        String dID = null;
        String odID = null;
        String captureDate = null;
        ZonedDateTime wTZ = null;
        Boolean dateFormat = false;
        String orig = null;
        String quality = null;
        long duration = 0;
        Integer orientation = null;
        Integer rating = null;
        String make = null;
        String title = null;
        String keyword = null;
        Integer shotnumber = null;
        try {
            tags = readMeta(inputStream, name);
        } catch (ImageProcessingException | IOException ex) {
            Meta meta = createMeta(name, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, orig, quality, duration);
            meta.note = ex.toString();
            System.out.println(meta);
            return meta;
        }


        for (String[] tag : tags) {
            System.out.println(tag[0] + " : " + tag[1]);
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
                    if (FilenameUtils.getExtension(name.toLowerCase()).equals("mp4"))
                        try {
                            wTZ = ZonedDateTime.parse(tag[1], MP4DateFormatTZ);
                        } catch (DateTimeParseException exc) {
                        }
                case "exif:DateTimeOriginal":
                    try {
                        wTZ = ZonedDateTime.parse(tag[1], XmpDateFormatTZ);
                        if ((captureDate != null && LocalDateTime.parse(captureDate, ExifDateFormat).equals(wTZ.toLocalDateTime()))
                                || FilenameUtils.getExtension(name.toLowerCase()).equals("xmp"))
                            dateFormat = true;
                    } catch (DateTimeParseException exc) {
                    }
                    break;
                case "Image Quality":
                    quality = tag[1];
                    break;
                case "Duration in Seconds":
                    try{
                        if ("00:00:60".equals(tag[1])) {
                            duration = 60;
                        } else {
                            duration = Duration.between (LocalTime.MIN, LocalTime.parse ( tag[1] )).getSeconds();
                        }
                    }catch(NumberFormatException excep){
                        excep.printStackTrace();
                    }catch(DateTimeParseException excep){
                        excep.printStackTrace();
                    }
                    break;
                case "Duration":
                    try{
                        duration = Duration.between (LocalTime.MIN, LocalTime.parse ( tag[1] )).getSeconds();
                    }catch(DateTimeParseException excep){}
                    break;
                case "Orientation":
                    try {
                        orientation = Integer.parseInt(tag[1]);
                    } catch (NumberFormatException nfe) {
  /*
                            1	top	left side
                            2	top	right side
                            3	bottom	right side
                            4	bottom	left side
                            5	left side	top
                            6	right side	top
                            7	right side	bottom
                            8	left side	bottom
*/
                    }
                    break;
                case "Image Description":
                case "Windows XP Title":
                    title = tag[1];
                    break;
                case "Make":
                    make = tag[1];
                    break;
                case "Windows XP Keywords":
                case "Keywords":
                    keyword = tag[1];
                    break;
                case "Rating":
                case "xmp:Rating":
                    try {
                        rating = Integer.parseInt(tag[1]);
                    } catch (NumberFormatException nfe) {
                    }
                    break;
                case "Sequence Number":
                    if ("Single".equals(tag[1])) {shotnumber = 1;} else {
                        try {
                            shotnumber = Integer.parseInt(tag[1]);
                        } catch (NumberFormatException nfe) {
                        }
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
        Meta meta = createMeta(name, OrigDT, dateFormat, model, iID, dID, odID, orig, quality, duration);
        meta.orientation = orientation;
        meta.title = title;
        meta.keyword = keyword;
        meta.rating = rating;
        meta.make = make;
        meta.shotnumber = shotnumber;
        System.out.println(meta);
        return meta;
    }

    private static Meta createMeta(String originalFilename, ZonedDateTime date, Boolean dateFormat, String model, String iID, String dID, String odID, String orig, String quality, long duration) {
        Meta meta = new Meta();
        meta.originalFilename = originalFilename;
        meta.date = date;
        meta.dateFormat = dateFormat;
        meta.model = model;
        meta.odID = odID;
        meta.dID = dID;
        meta.iID = iID;
        meta.orig = orig;
        meta.quality = quality;
        meta.duration = duration;
        return meta;
    }

    //_tagType == exiftool TagId
    public static ArrayList<String[]> readMeta(File file) throws ImageProcessingException, IOException {
        return readMeta(new FileInputStream(file), file.getName());
    }

    public static ArrayList<String[]> readMeta(InputStream inputStream, String name) throws ImageProcessingException, IOException {
        Metadata metadata;
        ArrayList<String[]> tags = new ArrayList();
        Collection<XmpDirectory> xmpDirectories = null;
        if (FilenameUtils.getExtension(name.toLowerCase()).equals("xmp")) {
            XmpDirectory directory = new XmpDirectory();
            try {
                XMPMeta xmpMeta;
                // If all xmpBytes are requested, no need to make a new ByteBuffer
                xmpMeta = XMPMetaFactory.parse(inputStream);
                directory.setXMPMeta(xmpMeta);
            } catch (XMPException e) {
                directory.addError("Error processing XMP data: " + e.getMessage());
            }
            ArrayList<XmpDirectory> collect = new ArrayList<>();
            collect.add(directory);
            xmpDirectories = collect;
        } else {
            metadata = readDrew(inputStream, name);
            for (Directory directory : metadata.getDirectories()) {
                if (!directory.getClass().equals(XmpDirectory.class))
                    for (Tag tag : directory.getTags()) {
                        String value = null;
                        try {
                            value = "Orientation".equals(tag.getTagName()) ? Integer.toString(directory.getInt(tag.getTagType())) : tag.getDescription();
                        } catch (MetadataException e) {
                            value = tag.getDescription();
                        }
                        String[] temp = {tag.getTagName(), value};
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

            if (tag.getDescription() != null && !tag.getDescription().replaceAll("\\s+", "").equals("")) {
                String value = tag.getDescription();
                if ("Orientation".equals(tag.getTagName()))
                try {
                    value = Integer.toString(directory.getInt(tag.getTagType()));
                } catch (MetadataException e) {
                }
                tags.add(new String[]{tag.getTagName(), value});
            }
        }));
        return tags;
    }

    private static Metadata readDrew(File file) throws ImageProcessingException, IOException {
        return readDrew(new FileInputStream(file), file.getName());
    }

    private static Metadata readDrew(InputStream inputStream, String name) throws ImageProcessingException, IOException {
        String ext = FilenameUtils.getExtension(name).toLowerCase();
        switch(ext) {
//            case "mts":
//                return MetadataReader..readMetadata(inputStream);
            case "jpeg":
            case "jpg":
                return JpegMetadataReader.readMetadata(inputStream);
            case "tiff":
            case "tif":
            case "arw":
            case "dng":
            case "nef":
                return TiffMetadataReader.readMetadata(inputStream);
            case "png":
                return PngMetadataReader.readMetadata(inputStream);
            case "bmp":
                return BmpMetadataReader.readMetadata(inputStream);
            case "gif":
                return GifMetadataReader.readMetadata(inputStream);
            case "avi":
                return AviMetadataReader.readMetadata(inputStream);
            case "mov":
                return QuickTimeMetadataReader.readMetadata(inputStream);
            case "mp4":
                return Mp4MetadataReader.readMetadata(inputStream);
            case "heif":
                return HeifMetadataReader.readMetadata(inputStream);
            case "Unknown":
                throw new ImageProcessingException("File format could not be determined");
            default:
                return new Metadata();
        }
    }

    public static Boolean originalJPG(File file) {
        try {
            ArrayList<String[]> strings = readMetaNew(file);
            HashMap<String, String > tags = new HashMap<>();
            for (String[] string : strings) {
                String s = tags.get(string[0]);
                if (s != null) System.out.println(string[0] + s);
                tags.put(string[0], string[1]);
            }
            if (tags.get("Application Record Version") != null || tags.get("Enveloped Record Version") != null) return false;
            switch (tags.get("Model")) {
                case "Lumia 1020":
                    if (!"2.20".equals(tags.get("Exif Version"))) return false;
                    if (!"Windows Phone".equals(tags.get("Software"))) return false;

                    break;
                case "":
                    break;
                default:
                    System.out.println(tags.get("Model"));
            }

            return true;
        } catch (Exception e) {
            return null;
        }

    }

}
