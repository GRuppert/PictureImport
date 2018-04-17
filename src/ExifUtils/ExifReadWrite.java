/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExifUtils;

import static Main.PicOrganizes.ExifDateFormat;
import static Main.PicOrganizes.XmpDateFormatTZ;
import static Main.StaticTools.errorOut;
import static Main.StaticTools.getZonedTimeFromStr;
import Rename.meta;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabor
 */
public class ExifReadWrite {
    public static meta exifToMeta(File fileMeta) {
        ArrayList<String> files = new ArrayList<>();
        files.add(fileMeta.getName());
        List<meta> exifToMeta = exifToMeta(files, fileMeta.getParentFile());
        Iterator<meta> iterator = exifToMeta.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
    
    /**
     * Reads the standard metadata from the specified files in the given directory
     * @param filenames String list of the representation of the file names
     * @param dir the directory where the files are
     * @return a list of the <code> meta </code> objects for every file if the read was unsuccessful the note field of the object will contain the error message
     */
    public static List<meta> exifToMeta(ArrayList<String> filenames, File dir) {
/*        long startTime = System.nanoTime();
        exifToMetaIMR(filenames, dir);
        System.out.println("IMR:" + (System.nanoTime() - startTime)/1000000);
        startTime = System.nanoTime();
        List<meta> exifToMetaET = exifToMetaET(filenames, dir);
        System.out.println("ET:" + (System.nanoTime() - startTime)/1000000);
        return exifToMetaET;*/
        return exifToMetaIMR(filenames, dir);
    }
    
    private static List<meta> exifToMetaIMR(ArrayList<String> filenames, File dir) {
        List<meta> results = new ArrayList<>();      
        for (String filename : filenames) {
            ArrayList<String[]> tags;
            String model = null;
            String note = "";
            String iID = null;
            String dID = null;
            String odID = null;
            String captureDate = null;
            Boolean dateFormat = false;
            try {
                tags = readMeta(new File(dir + "\\" + filename));
            } catch (ImageProcessingException | IOException ex) {
                meta meta = new meta(dir + "\\" + filename, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, ex.toString());
                System.out.println(meta);
                results.add(meta);
                continue;
            }
            for (String[] tag : tags) {
//                System.out.println(tag[0]);
                switch (tag[0]) {
                    case "Model":
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
                        break;
                    case "exif:DateTimeOriginal":
                        try {
                            ZonedDateTime wTZ = ZonedDateTime.parse(tag[1], XmpDateFormatTZ);
                            if (LocalDateTime.parse(captureDate, ExifDateFormat).equals(wTZ.toLocalDateTime()))
                                dateFormat = true;
                        }
                        catch (DateTimeParseException exc) {
                        }  
                        break;
                }
            }
            meta meta = new meta(dir + "\\" + filename, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, note);
            System.out.println(meta);
            results.add(meta);
        }
        return results;
    }
    
    private static List<meta> exifToMetaET(ArrayList<String> filenames, File dir) {
        String filename = null;
        if (filenames.size() == 1 && filenames.get(0).length() > 5) {filename = dir + "\\" + filenames.get(0);}
        filenames.add(0, "-OriginalDocumentID");
        filenames.add(0, "-DocumentID");
        filenames.add(0, "-InstanceID");
        filenames.add(0, "-DateTimeOriginal");
        filenames.add(0, "-xmp:DateTimeOriginal");
        filenames.add(0, "-Model");
        filenames.add(0, "exiftool");
        ArrayList<String> exifTool = exifTool(filenames.toArray(new String[0]), dir);
        Iterator<String> iterator = exifTool.iterator();
        ArrayList<meta> results = new ArrayList<>();
        int i = -1;
        String model = null;
        String note = "";
        String iID = null;
        String dID = null;
        String odID = null;
        String captureDate = null;
        Boolean dateFormat = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("========")) {
                if (i > -1) {
                    meta meta = new meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, note);
                    System.out.println(meta);
                    results.add(meta);
                }
                i++;
                String fileTemp = line.substring(9).replaceAll("./", "").replaceAll("/", "\\");
                filename = dir + "\\" + fileTemp;
                model = null;
                captureDate = null;
                dID = null;
                odID = null;
                note = "";
                dateFormat = false;

            //End of exiftool output
            } else if (line.contains("image files read")){
                if (!line.contains(" 0 image files read")) {
                }
            } else if (line.contains("files could not be read")){
                            
            } else {
                String tagValue = "";
                if (line.length() > 34) tagValue = line.substring(34);
                switch (line.substring(0, 4)) {
                    case "Date":
                        if (captureDate == null)
                            captureDate = tagValue;
                        else {
                            if (Math.abs(captureDate.length()-tagValue.length()) == 6) dateFormat = true;
                            captureDate = tagValue;
                        }
                        break;
                    case "Came":
                        model = tagValue;
                        break;
                    case "Orig":
                        odID = tagValue;
                        break;
                    case "Docu":
                        dID = tagValue;
                        break;
                    case "Inst":
                        iID = tagValue;
                        break;
                    case "Warn":
                        note = line;
                        break;
                }
            }
        }
        if (filename != null) {
            meta meta = new meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, iID, dID, odID, note);
            System.out.println(meta);
            results.add(meta);
//            results.add(new meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, note, dID, odID));
        }
        return results;
    }

    public static File createXmp(File file) {
        String[] commandAndOptions = {"exiftool", file.getName(), "-o", file.getName() + ".xmp"};
        ArrayList<String> result = exifTool(commandAndOptions, file.getParentFile());
        if (result.get(0).endsWith("files created")) return new File(file.getAbsolutePath() + ".xmp"); 
        return null;
    }

    public static ArrayList<String> getExif(String[] values, File file) {
        return getExifET(values, file);
    }
    
    private static ArrayList<String> getExifET(String[] values, File file) {
        List<String> command = new ArrayList<>(Arrays.asList(values));
        command.add(0, "exiftool");
        command.add(file.getName());
        return exifTool(command, file.getParentFile());
    }
    
    public static void updateExif(List<String> valuePairs, File directory) {
        valuePairs.add(0, "-overwrite_original");
        valuePairs.add(0, "exiftool");
        exifTool(valuePairs, directory);
    }

    private static ArrayList<String> exifTool(List<String> parameters, File directory) {
        return exifTool(parameters.toArray(new String[0]), directory);
    }
    
    public static ArrayList<String> exifTool(String[] parameters, File directory) {
//        final String command[] = "exiftool " + parameters;
        ArrayList<String> lines = new ArrayList<>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(parameters, null, directory);
            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stdinReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stderrReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            int returnVal = p.waitFor();
        } catch (Exception e) {
            errorOut("xmp", e);
        } 
        return lines;
    }

    private static ArrayList<String> exifTool_builder(String[] parameters, File directory) {
//        final String command[] = "exiftool " + parameters;
        ArrayList<String> lines = new ArrayList<>();
        try {           
            ProcessBuilder processBuilder = new ProcessBuilder(parameters).directory(directory);
            Process p = processBuilder.start();
            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stdinReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s;
                        while (( s=stderrReader.readLine()) != null) {
                            lines.add(s);
                        }
                    }
                    catch(IOException e) {
                    }
                }
            }).start();
            int returnVal = p.waitFor();
        } catch (Exception e) {
            errorOut("xmp", e);
        } 
        return lines;
    }
    
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
    
    
}
