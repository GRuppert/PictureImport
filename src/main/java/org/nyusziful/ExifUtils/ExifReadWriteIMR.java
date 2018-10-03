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
import org.nyusziful.Rename.meta;
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
public class ExifReadWriteIMR implements ifMetaLink {
    /**
     * Reads the standard metadata from the specified files in the given directory
     * @param file file to be read
     * @param zone Timezone used if it is not presented in the exif
     * @return a list of the <code> meta </code> objects for every file if the read was unsuccessful the note field of the object will contain the error message
     */
    public meta exifToMeta(File file, ZoneId zone) {
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
            meta meta = new meta(file.getName(), getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, ex.toString(), null);
            System.out.println(meta);
            return meta;
        }
        for (String[] tag : tags) {
//                System.out.println(tag[0]);
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
                case "Date/Time Original":
                    captureDate = tag[1];
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
            if (OrigDT == null) OrigDT = getTimeFromStr(captureDate, zone);
        } else if (wTZ != null) {
            OrigDT = wTZ;
        }
        meta meta = new meta(file.getName(), OrigDT, dateFormat, model, iID, dID, odID, note, null);
        return meta;
    }
    
    public ArrayList<String[]> readMeta(File file) throws ImageProcessingException, IOException {
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

    @Override
    public File createXmp(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<String> getExif(String[] values, File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateExif(List<String> valuePairs, File directory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
