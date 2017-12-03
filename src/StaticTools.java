
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
import java.nio.ByteBuffer;
import java.nio.file.Files;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
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
    
    public static ArrayList<textMeta> exifToMeta(ArrayList<String> filenames, File dir) {
        String filename = null;
        if (filenames.size() == 1 && filenames.get(0).length() > 5) {filename = dir + "\\" + filenames.get(0);}
        filenames.add(0, "-OriginalDocumentID");
        filenames.add(0, "-DocumentID");
        filenames.add(0, "-Model");
        filenames.add(0, "-DateTimeOriginal");
        filenames.add(0, "exiftool");
        ArrayList<String> exifTool = StaticTools.exifTool(filenames.toArray(new String[0]), dir);
        Iterator<String> iterator = exifTool.iterator();
        ArrayList<textMeta> results = new ArrayList<>();
        int i = -1;
        String model = null;
        String note = "";
        String dID = null;
        String odID = null;
        String captureDate = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("========")) {
                if (i > -1) {
                    results.add(new textMeta(filename, model, captureDate, note, dID, odID));
                }
                i++;
                String fileTemp = line.substring(9).replaceAll("./", "").replaceAll("/", "\\");
                filename = dir + "\\" + fileTemp;
                model = null;
                captureDate = null;
                dID = null;
                odID = null;
                note = "";

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
                        captureDate = tagValue;
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
        if (filename != null) {results.add(new textMeta(filename, model, captureDate, note, dID, odID));}
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
            ArrayList<textMeta> exifToMeta = exifToMeta(files, dir);
            Iterator<textMeta> iterator = exifToMeta.iterator();
            String errors = "";
            while (iterator.hasNext()) {
                textMeta next = iterator.next();
                String model = next.model;
                ZonedDateTime dateZ = getZonedTimeFromStr(next.date);
                if (dateZ == null)
                    dateZ = getTimeFromStr(next.date, zone);
                if (dateZ != null) {
                    date = dateZ.toEpochSecond();
                    if (model == null) {model = "NA";}
                    Picture picture = new Picture(new File(/*dir + "\\" + */next.filename), date, model);
                    if (!stripes.containsKey(model)) {
                        stripes.put(model, new Stripes(model, stripes.size(), tl));
                    }
                    stripes.get(model).add(picture);
                } else {
                    errors += "\n" + next.filename;
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
                case 255:
                    c = in.read();
                    j++;
                    if (c == 216) {break OUTER;}
                default:
                    return -1;
            }
        }
        return j;
    }
    
    public static long startOfScanJPG(BufferedInputStream in) throws IOException {
        int c;
        long j = startOfImageJPG(in);
        if (j == -1) return j;
        Boolean marker = false;
        while ((c = in.read()) != -1) {
            if (marker)
                //Not a marker(byte stuffing), shouldn't happen in header
                if (c == 0/* || c==216*/) marker = false;
                //Start of Quantization table, practically the image
//Didn't work                else if (c == 219) return j;
                //Start of Scan
                else if (c == 218) return j; 
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
                if (c == 255) {marker = true;
//                } else if (c == 0) {//byte stuffing
                } else {
                    return -1;
                }
            }
            j++;
        }
        return -1;  
    }
    
    private static long readEndianValue(BufferedInputStream in, int length, boolean endian) {
        long result = 0;
        for(int i=0; i<length; i++) {
            try {
                long c = in.read();
                if (endian) {
                    c = (long) (c * Math.pow(256, i));
                } else {
                    c = (long) (c * Math.pow(256, length-i));
                }
                result += c;
            } catch (IOException ex) {
                return -1;
            }
        }
        return result;
    }

    private static long getPointers(ArrayList<ifd> directories, File file, boolean endian) throws FileNotFoundException, IOException {
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
        Iterator<ifd> iterator = directories.iterator();
        while (iterator.hasNext()) {
            ifd ifd = iterator.next();
            switch (ifd.tag) {
                case 256:
                    imageWidth = ifd.offset;
                    break;
                case 257:
                    imageLength = ifd.offset;
                    break;
                case 273:
                case 324:
                    pieceOffsets = ifd.offset;
                    pieceOffsetsCount = ifd.count;
                    pieceOffsetsLength = ifd.getTypeLength();
                    break;
                case 322:
                    pieceWidth = ifd.offset;
                    break;
                case 278:
                case 323:
                    pieceLength = ifd.offset;
                    break;
                case 279:
                case 325:
                    pieceByteCounts = ifd.offset;
                    pieceByteCountsCount = ifd.count;
                    pieceByteLength = ifd.getTypeLength();
                    break;
            }
        }
        if (pieceByteCountsCount != pieceOffsetsCount) return -1;
        RandomAccessFile fileRand = new RandomAccessFile(file.getAbsolutePath(), "r");
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
        }
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
//            System.out.println("Offset [" + j + "] =" + actualPieceOffset);
            fileRand.seek(actualPieceOffset);
            long readed = 0;
            int bufferSize = 4096;
            while (readed + bufferSize < actualPieceBytes) {
                byte chunk[] = new byte[bufferSize];
                readed += fileRand.read(chunk); 
                md5Digest.update(chunk);
            }
            while (readed < actualPieceBytes) {
                md5Digest.update((byte) fileRand.read());                
                readed++;
            }
        }
        byte[] digest = md5Digest.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        System.out.println(file.getName() + " : " + sb.toString());
        return -1;
    }
    
    private static long readSubDirectory(File file, boolean endian, long pointer) throws IOException {
        System.out.println("*********************************************************");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toString()));
        if (!skipBytes(in, pointer)) return -1;
        int numberofDirs = (int) readEndianValue(in, 2, endian);
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        boolean mainImage = false;
        ArrayList<ifd> directories = new ArrayList<>();
        for (int i = 0; i < numberofDirs; i++) {
            ifd ifd = new ifd();
            ifd.tag = (int) readEndianValue(in, 2, endian);
            ifd.type = (int) readEndianValue(in, 2, endian);
            ifd.count = readEndianValue(in, 4, endian);
            ifd.offset = readEndianValue(in, 4, endian);
            if (ifd.tag == 50972) System.out.println("!!!!!!!!!!" + ifd.count + " " + ifd.offset);
            if (ifd.tag == 254 && ifd.offset == 0) {mainImage = true;}
            if (ifd.tag == 330) {
                subIFDsPointer = ifd.offset;
                subIFDs = ifd.count;
                subIFDsPointerLength = ifd.getTypeLength();
            }
            System.out.println(ifd.getTag() + " " + ifd.getType() + " " + ifd.getCount() + " " + ifd.getValue() + " " + ifd.getPointer());
            if (ifd.tag == 257 || ifd.tag == 256) directories.add(ifd); //Image
            if (ifd.tag == 273 || ifd.tag == 278 || ifd.tag == 279) directories.add(ifd); //Stripe
            if (ifd.tag == 322 || ifd.tag == 323 || ifd.tag == 324 || ifd.tag == 325) directories.add(ifd); //Tile
        }
        if (mainImage) return getPointers(directories, file, endian);
        if (subIFDs == 1) {
            long readDirectory = readDirectory(file, endian, subIFDsPointer);
            if (readDirectory != -1) return readDirectory;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {
                in = new BufferedInputStream(new FileInputStream(file.toString()));
                if (!skipBytes(in, subIFDsPointer)) return -1;
                if (!skipBytes(in, j * subIFDsPointerLength)) return -1;
                long readDirectory = readDirectory(file, endian, readEndianValue(in, subIFDsPointerLength, endian));
                if (readDirectory != -1) return readDirectory;
            }
        }
        return -1;
    }
    
    private static long readDirectory(File file, boolean endian, long pointer) throws IOException {
        System.out.println("-------------------------------------------------------------");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toString()));
        if (!skipBytes(in, pointer)) return -1;
        int numberofDirs = (int) readEndianValue(in, 2, endian);
        long subIFDs = 0;
        long subIFDsPointer = 0;
        int subIFDsPointerLength = 0;
        long nextIFD = 0;
        boolean mainImage = false;
        ArrayList<ifd> directories = new ArrayList<>();
        for (int i = 0; i < numberofDirs; i++) {
            ifd ifd = new ifd();
            ifd.tag = (int) readEndianValue(in, 2, endian);
            ifd.type = (int) readEndianValue(in, 2, endian);
            ifd.count = readEndianValue(in, 4, endian);
            ifd.offset = readEndianValue(in, 4, endian);
            if (ifd.tag == 50972) System.out.println("!!!!!!!!!!" + ifd.count + " " + ifd.offset);
            if (ifd.tag == 254 && ifd.offset == 0) {mainImage = true;}
            if (ifd.tag == 330) {
                subIFDsPointer = ifd.offset;
                subIFDs = ifd.count;
                subIFDsPointerLength = ifd.getTypeLength();
            }
            System.out.println(ifd.getTag() + " " + ifd.getType() + " " + ifd.getCount() + " " + ifd.getValue() + " " + ifd.getPointer());
            if (ifd.tag == 257 || ifd.tag == 256) directories.add(ifd); //Image
            if (ifd.tag == 273 || ifd.tag == 278 || ifd.tag == 279) directories.add(ifd); //Stripe
            if (ifd.tag == 322 || ifd.tag == 323 || ifd.tag == 324 || ifd.tag == 325) directories.add(ifd); //Tile
        }
        nextIFD = readEndianValue(in, 4, endian);
        if (mainImage) return getPointers(directories, file, endian);
        if (subIFDs == 1) {
            long readDirectory = readSubDirectory(file, endian, subIFDsPointer);
            if (readDirectory != -1) return readDirectory;
        } else if (subIFDs > 1) {
            for (int j = 0; j < subIFDs; j++) {
                in = new BufferedInputStream(new FileInputStream(file.toString()));
                if (!skipBytes(in, subIFDsPointer)) return -1;
                if (!skipBytes(in, j * subIFDsPointerLength)) return -1;
                long readDirectory = readSubDirectory(file, endian, readEndianValue(in, subIFDsPointerLength, endian));
                if (readDirectory != -1) return readDirectory;
            }
        }
        return -nextIFD;
    }
    
    public static long startOfScanDNG(File file) throws IOException {
        System.out.println(file);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toString()));
        int c;
        long j = 0;
        long pointer;
        Boolean endian = null;
        c = in.read();
        if (c == 73) {
            if (in.read() == 73) endian = true;
        } else if (c == 77) {
            if (in.read() == 77) endian = false;
        }
        if (endian == null) {return -1;}
        if (in.read() != 42) {return -1;}
        c = in.read();
        if (!((c == 0 && endian) || (c == 1 && !endian))) {return -1;}
        boolean endOfFile = false;
        pointer = readEndianValue(in, 4, endian);
        while (!endOfFile) {
            if (pointer == -1) {return -1;}
            if (pointer == 0) {break;}
            pointer = readDirectory(file, endian, pointer);
            if (pointer > 1) {return pointer;}
            else {pointer = -1 * pointer;}
        }
        return -1;
    }
    
    private static boolean skipBytes(InputStream in, long pointer) throws IOException {
        do {
            long skip = in.skip(pointer);
            if (skip < 0) return false;
            pointer -= skip;
        } while (pointer > 0);
        return true;
    }
    
    public static byte[] readBytes(DigestInputStream in, int size) throws IOException {
        byte result[] = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte)in.read();
        }
        return result;
    }
    
    public static String getHash(File file) throws FileNotFoundException, IOException {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            return mediaFile.EMPTYHASH;
        }
        byte[] digestDef = md5Digest.digest();
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file.toString())); DigestInputStream in = new DigestInputStream(fileStream, md5Digest)) {
            byte[] buffer = new byte[4096];
            long length;
            String ext = FilenameUtils.getExtension(file.getName().toLowerCase());
            switch (ext) {
                case "mp4":
    // <editor-fold defaultstate="collapsed" desc="mp4">
                    in.on(false);
                    boolean EOF = false;
                    String desc = "";
                    do {
                        byte boxLength[] = readBytes(in, 4);
                        byte boxDesc[] = readBytes(in, 4);                        
                        ByteBuffer wrapped = ByteBuffer.wrap(boxLength); // big-endian by default
                        length = wrapped.getInt() - 8;
                        if (length == -7) {
                            //largesize
                            byte boxLargesize[]= readBytes(in, 8);
                            ByteBuffer wrappedLarge = ByteBuffer.wrap(boxLargesize); // big-endian by default
                            length = wrappedLarge.getLong() - 16;
                        } else if (length == -8) {
                            // until eof
                             EOF = true;
                        } else if (length == in.available()) {
                             EOF = true;
                        }
                        desc = new String(boxDesc);
                        if (desc.equals("mdat")) {
                            in.on(true);
                            while (buffer.length <= length) {
                                in.read(buffer);
                                length -= buffer.length;
                            }
                            in.read(buffer, 0, (int)length);
                            break;
                        } else {
                            if (!skipBytes(in, length)) break;
                        }
                    } while(!EOF);    
                    break;
    // </editor-fold>
                case "jpg":
                case "jpeg":
    // <editor-fold defaultstate="collapsed" desc="JPG">
                    int scanLength;
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
                        } while (!(oldc == 255 && c == 217));
                        long av = in.available();
//                        System.out.println(scanLength + " " + av);
                    } while (scanLength < in.available());
                    break;
    // </editor-fold>
                case "dng":
//                    startOfScanDNG(file);
                    break;
                case "arw":
//                case "dng":
                case "nef":
    // <editor-fold defaultstate="collapsed" desc="raw">
                    in.on(true);
                    while (in.read(buffer) != -1) {}
                    break;
    // </editor-fold>
            }
        }
        byte[] digest = md5Digest.digest();
        if (Arrays.equals(digest, digestDef)) {
            return mediaFile.EMPTYHASH;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
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

}
