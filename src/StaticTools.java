
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
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class StaticTools {
    
    public static meta exifToMeta(File fileMeta) {
        ArrayList<String> files = new ArrayList<>();
        files.add(fileMeta.getName());
        ArrayList<meta> exifToMeta = StaticTools.exifToMeta(files, fileMeta.getParentFile());
        Iterator<meta> iterator = exifToMeta.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
    
    public static ArrayList<meta> exifToMeta(ArrayList<String> filenames, File dir) {
        String filename = null;
        if (filenames.size() == 1 && filenames.get(0).length() > 5) {filename = dir + "\\" + filenames.get(0);}
        filenames.add(0, "-OriginalDocumentID");
        filenames.add(0, "-DocumentID");
        filenames.add(0, "-DateTimeOriginal");
        filenames.add(0, "-xmp:DateTimeOriginal");
        filenames.add(0, "-Model");
        filenames.add(0, "exiftool");
        ArrayList<String> exifTool = StaticTools.exifTool(filenames.toArray(new String[0]), dir);
        Iterator<String> iterator = exifTool.iterator();
        ArrayList<meta> results = new ArrayList<>();
        int i = -1;
        String model = null;
        String note = "";
        String dID = null;
        String odID = null;
        String captureDate = null;
        Boolean dateFormat = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("========")) {
                if (i > -1) {
                    results.add(new meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, note, dID, odID));
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
                            if (captureDate.length() == 19) dateFormat = true;
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
                    case "Warn":
                        note = line;
                        break;
                }
            }
        }
        if (filename != null) {results.add(new meta(filename, getZonedTimeFromStr(captureDate), dateFormat, model, note, dID, odID));}
        return results;
    }

    public static ArrayList<String> exifTool(String[] parameters, File directory) {
        return exifTool_exec(parameters, directory);
    }

    private static ArrayList<String> exifTool_exec(String[] parameters, File directory) {
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
            StaticTools.errorOut("xmp", e);
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
            StaticTools.errorOut("xmp", e);
        } 
        return lines;
    }

    public static void errorOut(String source, Exception e) {
        JOptionPane.showMessageDialog(null, "From :" + source + "\nMessage: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void beep() {
        final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        if (runnable != null) runnable.run();
    }
    
    /**
     * @recheck the max and min values of the picture dates
     */
    public static HashMap<String, Stripes> readFiles(File dir, TimeLine tl, ZoneId zone) {
        HashMap<String, Stripes> stripes = new HashMap<String, Stripes>();
        if(dir.isDirectory()) {
/*            File[] content = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {                    
                    return (!name.toLowerCase().startsWith("thumb") && !name.toLowerCase().endsWith("_thmb.jpg") && !new File(dir + "\\" + name).isDirectory());
                }});*/
            long date = -1;
            String[] commandAndOptions = {"exiftool", "-b", "-ThumbnailImage", "-w", "thmb/%d%f_thmb.jpg", "."};
            exifTool(commandAndOptions, dir);
//            exifTool(" -b -ThumbnailImage -w thmb/%d%f_thmb.jpg .", dir);
//            ArrayList<String> exifTool = exifTool(" -DateTimeOriginal -Model " + fileNames, dir);
            ArrayList<String> files = new ArrayList<>();
            files.add(".");
            ArrayList<meta> exifToMeta = exifToMeta(files, dir);
            Iterator<meta> iterator = exifToMeta.iterator();
            String errors = "";
            while (iterator.hasNext()) {
                meta next = iterator.next();
                String model = next.model;
                ZonedDateTime dateZ = next.date;
                if (dateZ != null) {
                    date = dateZ.toEpochSecond();
                    if (model == null) {model = "NA";}
                    Picture picture = new Picture(new File(/*dir + "\\" + */next.originalFilename), date, model);
                    if (!stripes.containsKey(model)) {
                        stripes.put(model, new Stripes(model, stripes.size(), tl));
                    }
                    stripes.get(model).add(picture);
                } else {
                    errors += "\n" + next.originalFilename;
                }                        
                
            }
            if (!errors.equals("")) {errorOut("xmp", new Exception(errors));}
        }
/*            ArrayList<String> exifToolResponse = exifTool(" -DateTimeOriginal -Model .", dir);
            Iterator<String> iterator = exifToolResponse.iterator();
            if (iterator.hasNext()) {
                String line = iterator.next();
                if (line.startsWith("========")) {
                    filename = line.substring(9);
                    model = null;
                    date = -1;
                }
                String errors = "";
                while (iterator.hasNext()) {
                    line = iterator.next();
                    if (line.startsWith("========") || line.contains("image files read")) {
                        if (date > -1 && model != null) {
                            Picture picture = new Picture(new File(dir + "\\" + filename), date, model);
                            if (!stripes.containsKey(model)) {
                                stripes.put(model, new Stripes(model, stripes.size(), tl));
                            }
                            stripes.get(model).add(picture);
                        } else {
                            errors += "\n" + filename;
                        }                        
                        filename = line.substring(9);
                        model = null;
                        date = -1;
                    } else {
                        switch (line.substring(0, 4)) {
                            case "Date":
                                line = line.substring(34);
                                String dateString = line.length()>25 ? line.substring(0, 25) : line; //2016:11:03 07:50:24+02:00
                                ZonedDateTime dateZ = getZonedTimeFromStr(dateString);
                                if (dateZ == null)
                                    dateZ = getTimeFromStr(dateString, zone);
                                if (dateZ != null)
                                    date = dateZ.toEpochSecond();
                                break;
                            case "Came":
                                model = line.substring(34);
                                break;
                            case "Warn":
                                errorOut("xmp", new Exception(line));
                                break;
                        }
                    }
                }
                if (!errors.equals("")) StaticTools.errorOut("Missing Exif info", new Exception(errors));
            }
        }*/
        return stripes;
    }   
    
    public static ZonedDateTime getTimeFromStr(String input, ZoneId zone) {
        ZonedDateTime result = null;
        try {
            result = LocalDateTime.parse(input, PicOrganizes.ExifDateFormat).atZone(zone);
        } catch (DateTimeParseException e) {
            try {
                result = LocalDateTime.parse(input, PicOrganizes.XmpDateFormat).atZone(zone);
            } catch (DateTimeParseException e1) {
            }
        }
        return result;
    }

    public static ZonedDateTime getZonedTimeFromStr(String input) {
        ZonedDateTime result = null;
        if (input != null)
            try {
                result = OffsetDateTime.parse(input, PicOrganizes.ExifDateFormatTZ).toZonedDateTime();
            } catch (DateTimeParseException e) {
                try {
                    result = OffsetDateTime.parse(input, PicOrganizes.XmpDateFormatTZ).toZonedDateTime();
                } catch (DateTimeParseException e1) {
                }
            }
        return result;
    }
   
    public static long startOfImageJPG(BufferedInputStream in) throws IOException {
        int c;
        long j = 0;
        OUTER:
        while ((c = in.read()) != -1) {
            j++;
            switch (c) {
                case 0:
                    break;
                case 0xFF://255
                    c = in.read();
                    j++;
                    if (c == 0xD8 /*216*/) {break OUTER;}
                default:
                    return -1;
            }
        }
        return j;
    }
    
    public static long startOfScanJPG(BufferedInputStream in) throws IOException {
        int c;
        long j = startOfImageJPG(in);
        if (j == -1) return -1;
        Boolean marker = false;
        while ((c = in.read()) != -1) {
            if (marker)
                //Not a marker(byte stuffing), shouldn't happen in header
                if (c == 0/* || c==216*/) marker = false;
                //Start of Quantization table, practically the image
//Didn't work                else if (c == 219) return j;
                //Start of Scan
                else if (c == 0xDA /*218*/) return j; 
                else {
//                    System.out.print("ff" + Integer.toHexString(c) + " ");
                    long jump = 256*in.read() + in.read() - 2;
//                    System.out.println(Long.toHexString(jump));
                    do {
                        long skip = in.skip(jump);
                        if (skip < 0) return -1;
                        jump -= skip;
                    } while (jump > 0);
                    j += jump + 2;                        
                    marker = false;
                }
            else {
                if (c == 0xFF/*255*/) {marker = true;
//                } else if (c == 0) {//byte stuffing
                } else {
                    return -1;
                }
            }
            j++;
        }
        return -1;  
    }
    
    private static long readEndianValue(InputStream in, int length, boolean endian) {
        long result = 0;
        for(int i=0; i<length; i++) {
            try {
                long c = in.read();
                if (endian) {
                    c = (long) (c * Math.pow(256, i));
                } else {
                    c = (long) (c * Math.pow(256, length-1-i));
                }
                result += c;
            } catch (IOException ex) {
                return -1;
            }
        }
        return result;
    }

    private static byte[] getPointers(ArrayList<ifdField> imageLocationFields, File file, boolean endian) throws IOException {
/*      RawImageDigest
        Tag 50972 (C71C.H)
        Type BYTE
        Count 16
        Value See below
        Default Optional
        Usage IFD 0
        Description
        This tag is an MD5 digest of the raw image data. All pixels in the image are processed in rowscan
        order. Each pixel is zero padded to 16 or 32 bits deep (16-bit for data less than or equal to
        16 bits deep, 32-bit otherwise). The data for each pixel is processed in little-endian byte order         */
        long imageLength;
        long imageWidth;
        long pieceOffsets = 0;
        long pieceOffsetsCount = 0;
        int pieceOffsetsLength = 0;
        long pieceLength;
        long pieceWidth = 1;
        long pieceByteCounts = 0;
        long pieceByteCountsCount = 0;
        int pieceByteLength = 0;
        Iterator<ifdField> iterator = imageLocationFields.iterator();
        while (iterator.hasNext()) {
            ifdField field = iterator.next();
            switch (field.tag) {
                case 256:
                    imageWidth = field.offset;
                    break;
                case 257:
                    imageLength = field.offset;
                    break;
                case 273:
                case 324:
                    pieceOffsets = field.offset;
                    pieceOffsetsCount = field.count;
                    pieceOffsetsLength = field.getTypeLength();
                    break;
                case 322:
                    pieceWidth = field.offset;
                    break;
                case 278:
                case 323:
                    pieceLength = field.offset;
                    break;
                case 279:
                case 325:
                    pieceByteCounts = field.offset;
                    pieceByteCountsCount = field.count;
                    pieceByteLength = field.getTypeLength();
                    break;
            }
        }
        if (pieceByteCountsCount != pieceOffsetsCount) return null;
        try (RandomAccessFile fileRand = new RandomAccessFile(file.getAbsolutePath(), "r")) {
            MessageDigest md5Digest = null;
            try {
                md5Digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                return null;
            }
            if (pieceOffsetsCount == 1) {
                    System.out.println(file.getName() + " bytes readed: " + pieceByteCounts + " File size " + file.length() + " % " + (100*pieceByteCounts/file.length()));
                    fileRand.seek(pieceOffsets);
                    long readed = 0;
                    int bufferSize = 4096;
                    while (readed + bufferSize < pieceByteCounts) {
                        byte chunk[] = new byte[bufferSize];
                        int read = fileRand.read(chunk);
                        if (read == -1) return null;
                        readed += read; 
                        md5Digest.update(chunk);
                    }
                    int residue = (int)(pieceByteCounts - readed);
                    byte chunk[] = new byte[residue];
                    int read = fileRand.read(chunk);
                    if (read == -1) return null;
                    md5Digest.update(chunk);
            } else {
                long totalread = 0;
                for (int j = 0; j < pieceOffsetsCount; j++) {
                    fileRand.seek(pieceByteCounts + j * pieceByteLength);
                    long actualPieceBytes = 0;
                    for(int i=0; i<pieceByteLength; i++) {
                            long c = fileRand.read();
                            if (endian) {
                                c = (long) (c * Math.pow(256, i));
                            } else {
                                c = (long) (c * Math.pow(256, pieceByteLength-i));
                            }
                            actualPieceBytes += c;
                    }
        //            System.out.println("Count [" + j + "] =" + actualPieceBytes);
                    fileRand.seek(pieceOffsets + j * pieceOffsetsLength);
                    long actualPieceOffset = 0;
                    for(int i=0; i<pieceOffsetsLength; i++) {
                            long c = fileRand.read();
                            if (endian) {
                                c = (long) (c * Math.pow(256, i));
                            } else {
                                c = (long) (c * Math.pow(256, pieceOffsetsLength-i));
                            }
                            actualPieceOffset += c;
                    }
                    totalread += actualPieceBytes;
        //            System.out.println("Offset [" + j + "] =" + actualPieceOffset);
                    fileRand.seek(actualPieceOffset);
                    long readed = 0;
                    int bufferSize = 4096;
                    while (readed + bufferSize < actualPieceBytes) {
                        byte chunk[] = new byte[bufferSize];
                        int read = fileRand.read(chunk);
                        if (read == -1) return null;
                        readed += read; 
                        md5Digest.update(chunk);
                    }
                    int residue = (int)(actualPieceBytes - readed);
                    byte chunk[] = new byte[residue];
                    int read = fileRand.read(chunk);
                    if (read == -1) return null;
                    md5Digest.update(chunk);
                }
                System.out.println(file.getName() + " bytes readed: " + totalread + " File size " + file.length() + " % " + (100*totalread/file.length()));
            }
            return md5Digest.digest();
        }
    }
    
    private static byte[] readSubIFDirectory(ifdCursor cursor, BufferedInputStream in) throws IOException {
        in.reset();
        if (!skipBytes(in, cursor.getPointer())) return null;
        int tagEntryCount = (int) readEndianValue(in, 2, cursor.getEndian());
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        boolean mainImage = false;
        ArrayList<ifdField> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            ifdField field = new ifdField();
            field.tag = (int) readEndianValue(in, 2, cursor.getEndian());
            field.type = (int) readEndianValue(in, 2, cursor.getEndian());
            field.count = readEndianValue(in, 4, cursor.getEndian());
            field.offset = readEndianValue(in, 4, cursor.getEndian());
            if (field.tag == 50972) System.out.println("!!!!!!!!!!" + field.count + " " + field.offset);
            if (field.tag == 254 && field.offset == 0) {mainImage = true;}
            if (field.tag == 330) {
                subIFDsPointer = field.offset;
                subIFDs = field.count;
                subIFDsPointerLength = field.getTypeLength();
            }
//            System.out.println(field.getTag() + " " + field.getType() + " " + field.getCount() + " " + field.getValue() + " " + field.getPointer());
            if (field.tag == 257 || field.tag == 256) imageLocationFields.add(field); //Image
            if (field.tag == 273 || field.tag == 278 || field.tag == 279) imageLocationFields.add(field); //Stripe
            if (field.tag == 322 || field.tag == 323 || field.tag == 324 || field.tag == 325) imageLocationFields.add(field); //Tile
        }
        if (mainImage) return getPointers(imageLocationFields, cursor.getFile(), cursor.getEndian());
        if (subIFDs == 1) {
            cursor.setPointer(subIFDsPointer);
            byte[] hash = readSubIFDirectory(cursor, in);
            if (hash != null) return hash;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {
                in.reset();
//                    in = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                if (!skipBytes(in, subIFDsPointer)) return null;
                if (!skipBytes(in, j * subIFDsPointerLength)) return null;
                cursor.setPointer(readEndianValue(in, subIFDsPointerLength, cursor.getEndian()));
                byte[] hash = readSubIFDirectory(cursor, in);
                if (hash != null) return hash;
            }
        }
        return null;
    }
    
    private static byte[] readIFDirectory(ifdCursor cursor, BufferedInputStream in) throws IOException {
        in.reset();
        if (!skipBytes(in, cursor.getPointer())) return null;
        int tagEntryCount = (int) readEndianValue(in, 2, cursor.getEndian());
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        long nextIFD = 0;
        boolean mainImage = false;
        ArrayList<ifdField> imageLocationFields = new ArrayList<>();
        for (int i = 0; i < tagEntryCount; i++) {
            ifdField field = new ifdField();
            field.tag = (int) readEndianValue(in, 2, cursor.getEndian());
            field.type = (int) readEndianValue(in, 2, cursor.getEndian());
            field.count = readEndianValue(in, 4, cursor.getEndian());
            field.offset = readEndianValue(in, 4, cursor.getEndian());
            if (field.tag == 50972) System.out.println("!!!!!!!!!!" + field.count + " " + field.offset);
            if (field.tag == 254 && field.offset == 0) {mainImage = true;}
            if (field.tag == 330) {
                subIFDsPointer = field.offset;
                subIFDs = field.count;
                subIFDsPointerLength = field.getTypeLength();
            }
//            System.out.println(field.getTag() + " " + field.getType() + " " + field.getCount() + " " + field.getValue() + " " + field.getPointer());
            if (field.tag == 257 || field.tag == 256) imageLocationFields.add(field); //Image
            if (field.tag == 273 || field.tag == 278 || field.tag == 279) imageLocationFields.add(field); //Stripe
            if (field.tag == 322 || field.tag == 323 || field.tag == 324 || field.tag == 325) imageLocationFields.add(field); //Tile
        }
        nextIFD = readEndianValue(in, 4, cursor.getEndian());
        if (mainImage) return getPointers(imageLocationFields, cursor.getFile(), cursor.getEndian());
        if (subIFDs == 1) {
            cursor.setPointer(subIFDsPointer);
            byte[] hash = readSubIFDirectory(cursor, in);
            if (hash != null) return hash;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {                    
                in.reset();
//                    in = new BufferedInputStream(new FileInputStream(cursor.getFile().toString()));
                if (!skipBytes(in, subIFDsPointer)) return null;
                if (!skipBytes(in, j * subIFDsPointerLength)) return null;
                cursor.setPointer(readEndianValue(in, subIFDsPointerLength, cursor.getEndian()));
                byte[] hash = readSubIFDirectory(cursor, in);
                if (hash != null) return hash;
            }
        }
        cursor.setPointer(nextIFD);
        return readIFDirectory(cursor, in);
    }
        
    //returns the pointer to the main image data in tiff based files
    public static byte[] startOfScanTiff(File file, BufferedInputStream in) throws IOException {
        in.mark(0);
        int c;
        long j = 0;
        Boolean endian = null;
        c = in.read();
        if (c == 73) {
            if (in.read() == 73) endian = true;
        } else if (c == 77) {
            if (in.read() == 77) endian = false;
        }
        if (endian == null) {return null;}
        long tiffCheck = readEndianValue(in, 2, endian);
        if (tiffCheck != 42) {return null;}
        ifdCursor cursor = new ifdCursor(file, endian, readEndianValue(in, 4, endian));
        if (cursor.getPointer() == -1) {return null;}
        if (cursor.getPointer() == 0) {return null;}
        return readIFDirectory(cursor, in);
    }
    
    private static boolean skipBytes(InputStream in, long pointer) throws IOException {
        do {
            long skip = in.skip(pointer);
            if (skip < 0) return false;
            pointer -= skip;
        } while (pointer > 0);
        return true;
    }
    
    public static byte[] readBytes(InputStream in, int size) throws IOException {
        byte result[] = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte)in.read();
        }
        return result;
    }
    
    public static ArrayList<String> getHash(Path folderPath) throws FileNotFoundException, IOException {
        ArrayList<String> hashes = new ArrayList<>();
        File[] listOfFiles = folderPath.toFile().listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (PicOrganizes.supportedFileType(listOfFile.getName())) {
                    hashes.add(getHash(listOfFile));
                }
            } else if (listOfFile.isDirectory()) {
                hashes.addAll(getHash(listOfFile.toPath()));    
            }
        }
        return hashes;
    }
    
    public static String getFullHashPS(File file) throws FileNotFoundException, IOException {
        String[] parameters = new String[]{"powershell.exe", "Get-ChildItem", "-File", "-Recurse", "|", "Get-FileHash", "-Algorithm", "MD5", ">>", "e:\\ps.md5"};
        String lines = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(parameters, null, file);
/*            final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "ISO-8859-1"));
            final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "ISO-8859-1"));
            new Thread(() -> {
                try {
                    String s;
                    while (( s=stdinReader.readLine()) != null) {
                        System.out.println(s);
                    }
                }
                catch(IOException e) {
                }
            }).start();
            new Thread(() -> {
                try {
                    String s;
                    while (( s=stderrReader.readLine()) != null) {
                        System.out.println(s);
                    }
                }
                catch(IOException e) {
                }
            }).start();*/
            int returnVal = p.waitFor();
        } catch (Exception e) {
            StaticTools.errorOut("xmp", e);
        } 
        return lines;

    }
    
    public static String getFullHash(File file) throws FileNotFoundException, IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return mediaFile.EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file.toString())); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {            
            byte[] buffer = new byte[4096];
            in.on(true);
            while (in.read(buffer) != -1) {}
            digest = md5Digest.digest();
        }
        if (digest == null) {
            return mediaFile.EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            return mediaFile.EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    
    public static String getHash(File file) throws FileNotFoundException, IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return mediaFile.EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        byte[] digest = null;
        String ext = FilenameUtils.getExtension(file.getName().toLowerCase());
        if (ext.equals("arw") || ext.equals("nef") || ext.equals("dng") || ext.equals("tif") || ext.equals("tiff")) {
        // <editor-fold defaultstate="collapsed" desc="raw">
            try (BufferedInputStream rawIn = new BufferedInputStream(new FileInputStream(file.toString()));) {                    
                digest = startOfScanTiff(file, rawIn);
            } catch(IOException e) {
                errorOut("Hash", e);         
            } 
        // </editor-fold>            
        } else {
            try (FileInputStream fileInStream = new FileInputStream(file.toString()); BufferedInputStream fileStream = new BufferedInputStream(fileInStream); DigestInputStream in = new DigestInputStream(fileStream, md5Digest);) {            
                byte[] buffer = new byte[4096];
                long length;
                switch (ext) {
        // <editor-fold defaultstate="collapsed" desc="mp4">
                    case "mp4":
                        in.on(false);
                        boolean EOF = false;
                        do {
                            length = readEndianValue(in, 4, false) - 8;
    //                        byte boxLength[] = readBytes(in, 4);
                            String desc = new String(readBytes(in, 4));                        
    //                        ByteBuffer wrapped = ByteBuffer.wrap(boxLength); // big-endian by default
    //                        length = wrapped.getInt() - 8;
                            if (length == -7) {
                                //largesize
                                length = readEndianValue(in, 8, false) - 16;
                            } else if (length == -8) {
                                // until eof
                                 EOF = true;
                            } else if (length == in.available()) {
                                 EOF = true;
                            }
                            if (desc.equals("mdat")) {
                                in.on(true);
                                while (buffer.length <= length) {
                                    int read = in.read(buffer);
                                    length -= read;
                                }
                                while (length > 0) {
                                    int read = in.read(buffer, 0, (int)length);
                                    if (read == -1) {digest = null; break;}
                                    length -= read;
                                }
    //                            break;
                            } else {
                                if (!skipBytes(in, length)) break;
                            }
                        } while(!EOF);    
                        digest = md5Digest.digest();
                        break;
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="JPG">
                    case "jpg":
                    case "jpeg":
                        int scanLength;
                        int scanLengthOld = 0;
                        do {
                            in.on(false);
                            md5Digest.reset();
                            scanLength = 0;
                            if (startOfScanJPG(fileStream) == -1) break;
                            in.on(true);
                            int c = 0;
                            int oldc;
                            do {
                                oldc = c;
                                c = in.read();
                                if (c == -1) {
                                    return mediaFile.EMPTYHASH;
                                }
                                scanLength ++;
                            } while (!(oldc == 0xFF && c == 0xD9/*217*/));
                            if (scanLength > scanLengthOld) {digest = md5Digest.digest(); scanLengthOld = scanLength;}
                        } while (scanLengthOld < in.available());
                        break;
        // </editor-fold>
                    default:
                        in.on(true);
                        while (in.read(buffer) != -1) {}
                        digest = md5Digest.digest();
                        break;
                }
            }  catch(IOException e) {
                errorOut("Hash", e);         
            } 
        }
        if (digest == null) {
            return mediaFile.EMPTYHASH;
        }
        if (Arrays.equals(digest, digestDef)) {
            return mediaFile.EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    
    public static String formatHash(String hash) {
        if (hash.length()!=32) return hash;
        return hash.substring(0, 8) + "-" + hash.substring(8, 12) + "-" + hash.substring(12, 16) + "-" + hash.substring(16, 20) + "-" + hash.substring(20, 32);
    }

    public static boolean copyAndBackup(File source, File dest, File backup) {
        FileInputStream input = null;
        FileOutputStream output = null;
        FileOutputStream outputII = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            outputII = new FileOutputStream(backup);
            byte[] bufR = new byte[4096];
            byte[] bufW;
            int bytesRead;
            while ((bytesRead = input.read(bufR)) > 0)
            {
                bufW = bufR;
                output.write(bufW, 0, bytesRead);
                outputII.write(bufW, 0, bytesRead);
            } 
            
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                input.close();
                output.close();
                outputII.close();
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }
    
    public static File getDir(File dir) {
        DirectoryChooser chooser = new DirectoryChooser();
        if (Files.exists(dir.toPath()))
            chooser.setInitialDirectory(dir);
        else
            chooser.setInitialDirectory(new File("C:\\"));
        return chooser.showDialog(null);
    }
    
    public static File getFile(File dir) {
        FileChooser chooser = new FileChooser();
        if (Files.exists(dir.toPath()))
            chooser.setInitialDirectory(dir);
        else
            chooser.setInitialDirectory(new File("C:\\"));
        return chooser.showSaveDialog(null);
    }
    

    //remove 5/41MP Nokia "duplicates"
    private static void removeFiles(File file) {
        File[] directories = file.listFiles((File dir, String name) -> dir.isDirectory());
        for (File dir1 : directories) {
            if(dir1.isDirectory()) {
                File[] content = dir1.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                            name = name.toLowerCase();
                        return name.endsWith("__highres.jpg");
                    }});
                if (content.length > 0) {
                    for(int i = 0; i < content.length; i++) {
                        String absolutePath = content[i].getAbsolutePath();
                        try {
                            Files.deleteIfExists(Paths.get(absolutePath.substring(0, absolutePath.length()-13) + ".jpg"));
                        } catch (IOException e) {
                            errorOut(content[i].getName(), e);         
                        }
                    }
                }
            }				
        }

    }

    private static void compareCSV() {
        CsvParserSettings csvParserSettings = new CsvParserSettings();
        CsvParser parser = new CsvParser(new CsvParserSettings());
        try {
            List<String[]> filterData = parser.parseAll(new FileReader("e:\\filter.csv"));
            List<String[]> backupData = parser.parseAll(new FileReader("e:\\fekete.csv"));
            ArrayList<String[]> sizeMismatch = new ArrayList();
            ArrayList<String[]> wrong = new ArrayList();
            data:
            for (String[] data : backupData) {
                for (String[] filter : filterData) {
                    if (filter[0].endsWith(data[0])) {
                        if (filter[1].equals(data[1])) {
                            break data;
                        } else {
                            sizeMismatch.add(data);
                            break data;
                        }
                    }
                }
                wrong.add(data);
            }
            System.out.println("Ennyi: " + sizeMismatch.size() + "/" + wrong.size() + "/" + backupData.size());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String[]> readMeta(File file) {
        Metadata metadata;
        ArrayList<String[]> tags = new ArrayList();
        try {
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
                    Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }                    
        } catch (ImageProcessingException | IOException e) {
            StaticTools.errorOut(file.getName(), e);         
        }
        return tags;
    }
    
    
}
