package org.nyusziful.Rename;


import static org.nyusziful.ExifUtils.ExifReadWrite.createXmp;
import static org.nyusziful.ExifUtils.ExifReadWrite.getExif;
import static org.nyusziful.Hash.MediaFileHash.EMPTYHASH;
import static org.nyusziful.Hash.MediaFileHash.getHash;
import static org.nyusziful.Hash.MediaFileHash.getFullHash;
import static org.nyusziful.Main.PicOrganizes.COPY;
import static org.nyusziful.Main.PicOrganizes.MOVE;
import static org.nyusziful.Main.PicOrganizes.view;
import static org.nyusziful.Main.StaticTools.ExifDateFormat;
import static org.nyusziful.Main.StaticTools.ExifDateFormatTZ;
import static org.nyusziful.Main.StaticTools.errorOut;
import static org.nyusziful.Main.StaticTools.getZonedTimeFromStr;
import static org.nyusziful.Main.StaticTools.supportedMediaFileType;
import static org.nyusziful.Main.StaticTools.supportedRAWFileType;
import static org.nyusziful.Main.StaticTools.supportedVideoFileType;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.io.FilenameUtils;
import static org.nyusziful.ExifUtils.ExifReadWrite.exifToMeta;

/**
 *
 * @author gabor
 */
public class mediaFile {
    public static String[] CAMERAS = {
        "NA",
        "ILCE-5100",
        "ILCE-6000",
        "GT-I9192",
        "GT-I9195I",
        "Lumia 1020",
        "FinePix S5800 S800",
        "TG-3            ",
        "ST25i",
        "GT-I8190N",
        "E52-1"
    };
    
    public static DateTimeFormatter dfV1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");//20161124_200546
    public static DateTimeFormatter dfV2 = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ssZ");//2016-11-24@20-05-46+0200
    public static DateTimeFormatter dfV3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//20161124200546
    public static DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss");//2016-11-24@20-05-46
    
    public static String Version = "6";
    
    private final SimpleBooleanProperty processing;
    private final SimpleStringProperty currentName;
    private final SimpleStringProperty newName;
    private final SimpleStringProperty note;
    private final SimpleBooleanProperty xmpMissing;

    private File file;
    private File fileXmp;
    private String originalName;
    private String orig = "0";
    private String targetDirectory;
    private boolean forceRewrite;
    
    private String iID;
    private String dID;
    private String odID;
    private String model = null;//Filename, Exif, xmp, xml
    private ZonedDateTime date = null;//Filename, Exif, xmp, xml, mod +TZ

    private HashMap<String, String> exifPar = new HashMap<>();
    private ArrayList<String> exifMissing = new ArrayList<String>();

    /**
     * @param iID the iID to set
     */
    private final void setiID() {
        this.iID = getFullHash(file);
        newName.set(getNewFileName("5"));
    }
    

    public mediaFile(String fileIn) {
        this(new File(fileIn));
    }

    public mediaFile(String fileIn, String targetDir) {
        this(fileIn);
        targetDirectory = targetDir;
    }

    public mediaFile(File fileIn) {
        this(fileIn, null, true);
    }
    
    public mediaFile(meta metaExif) {
        this(new File(metaExif.originalFilename), metaExif, true);
    }
    
    public mediaFile(File fileIn, meta metaExif, boolean forceRewrite) {
        this.file = fileIn;
        this.forceRewrite = forceRewrite;
        processing = new SimpleBooleanProperty(true);
        currentName = new SimpleStringProperty(fileIn.getName());
        note = new SimpleStringProperty("");
        if (metaExif != null && !metaExif.note.equals("")) addNote(metaExif.note, false);
        xmpMissing = new SimpleBooleanProperty(false);
        originalName = file.getName();
        targetDirectory = view.getToDir().toString() + "\\" + file.getParentFile().getName();
        
        if (!supportedMediaFileType(currentName.get())) {
            newName = new SimpleStringProperty(/*targetDirectory + "\\" + */originalName);
        } else {
            String ext = FilenameUtils.getExtension(originalName.toLowerCase());
            if (metaExif == null) {//if it hasn't been batch readed in the caller function
                metaExif = exifToMeta(file);
            }

            //standardizes data in Sony mp4 
            if (ext.equals("mp4")) {
                metaExif = repairMP4(metaExif);
            }
            
            //read data from filename
            meta metaFile;
            metaFile = getV(getFile().getName());
            
            //Set original filename, orig if possible from filename
            if (metaFile != null && metaFile.originalFilename != null) originalName = metaFile.originalFilename;
            if (metaFile != null && metaFile.orig != null) orig = metaFile.orig;
            
            //read External sidecars
            meta metaXmp = null;
            if (supportedRAWFileType(originalName)) {
                fileXmp = new File(getFile().toString() + ".xmp");
                if (fileXmp.exists()) metaXmp = exifToMeta(fileXmp);
            }
                        
            //compare/prioritizes filename and exif data(metaFile, metaXmp/metaExif)
            compareMeta(metaExif, metaFile, metaXmp);
            if (!orig.matches("[a-w]") && ext.equals("jpg")) checkRAW();
            
            if (!odID.equals(dID)) notOrig();
            
            newName = new SimpleStringProperty(getNewFileName(Version));
        }
    }

    private String getNewFileName(String ver) {
        switch (ver) {
            case "5":
                setiID();
                return "V" + ver + "_" + dateFormat(date) + getiID() + "-" + getdID() + "-" + originalName;

            case "6":
                return "V" + ver + "_" + dateFormat(date) + getdID() + "-" + orig + "-" + originalName;
            default:
                return null;
        }
    }

    private void compareModel(meta metaExif, meta metaFile) {
        if (metaFile.model != null) model = metaFile.model;
        if (metaExif.model != null) {
            if (model != null) {
                if (!metaExif.model.trim().equals(model.trim())) {
                    addNote("model has been changed: " + model + " -> " + metaExif.model, true);
                }
            } else {
                model = metaExif.model;
            }
        } else {
            if (model == null) {
                addNote("model missing", false);
            } else {
                addExif("Model", model);
            }
        }
    }
    
    private void compareHash(meta metaExif, meta metaFile) {
        if (!forceRewrite) {//version change
           if (metaExif.dID != null) {
               this.dID = metaExif.dID;
           } else if (metaFile.dID != null) {
               this.dID = metaFile.dID;
               addExif("DocumentID" , getdID());
           } else {
               this.dID = getHash(getFile());
               addExif("DocumentID" , getdID());
           }
           if (metaExif.odID != null) {
               this.odID = metaExif.odID;
           } else if (metaFile.odID != null) {
               this.odID = metaFile.odID;
               addExif("OriginalDocumentID" , getOdID());
           } else {
                this.odID = this.dID;
               addExif("OriginalDocumentID" , getOdID());
           }
        } else {
            this.dID = getHash(getFile());
            if (getdID().equals(EMPTYHASH)) {
                addNote("Error during hashing" + model, true);
            }

            if (metaExif.dID != null && !metaExif.dID.equals(this.dID)) {addNote("dID has been changed", false); addExif("DocumentID" , getdID()); notOrig();}
            else if (metaFile.dID != null && !metaFile.dID.equals(this.dID)) {addNote("dID has been changed", false); addExif("DocumentID" , getdID()); notOrig();}
            if (metaExif.dID == null) addExif("DocumentID" , getdID());

            if (metaFile.odID != null) odID = metaFile.odID;
            if (metaExif.odID != null)
                if (getOdID() != null) {
                    if (!metaExif.odID.equals(odID)) addNote("odID has been changed", true);
                } else {
                    odID = metaExif.odID; //addNote("odID already presented", false);
                }
            if (getOdID() == null) {
                if (orig.equals("0")) {
                    odID = getdID();
                } else if (metaFile.dID != null) {
                    odID = metaFile.dID;
                } else if (metaExif.dID != null) {
                    odID = metaExif.dID;
                }
                if (getOdID() == null) {
                    //Todo nyers-jpg
                    odID = org.nyusziful.Hash.MediaFileHash.EMPTYHASH;
                } else {
                    addExif("OriginalDocumentID" , getOdID());
                }
            }
        }
    }

    private void compareDate(meta metaExif, meta metaFile) {            
        if (metaFile.date != null) date = metaFile.date;
        if (metaExif.date != null) {
            if (date != null) {
                Boolean equal = true;
                if (supportedVideoFileType(originalName)) {
                    if (Math.abs(ChronoUnit.SECONDS.between(date, metaExif.date)) > 180) {equal = false;}                    
                } else {
                    if (Math.abs(ChronoUnit.SECONDS.between(date, metaExif.date)) > 0) {equal = false;}                    
                }
                if (!equal) {
                    addNote("date has been changed: " + (date.toEpochSecond() - metaExif.date.toEpochSecond()) + "sec", true);
                }
            } else {
                date = metaExif.date;
            }
            if (metaExif.dateFormat == null || !metaExif.dateFormat) addExif(date);
        } else {
            if (date == null) {
                addNote("date missing", false);
            } else {
                addExif(date);
            }
        }
        if (date != null && !date.isEqual(date.withZoneSameInstant(view.getZone()))) {addNote("TZ different", false);}
    }
    
    private void compareXMP(meta metaXmp) {
        if (metaXmp.date != null && !metaXmp.date.isEqual(date)) addNote("XMP date wrong", false);
        if (metaXmp.model != null && !metaXmp.model.equals(model)) addNote("XMP model wrong", false);
        if (metaXmp.dID != null && !metaXmp.dID.equals(dID)) addNote("XMP dID wrong", false);
        if (metaXmp.odID != null && !metaXmp.odID.equals(odID)) addNote("XMP odID wrong", false);
    }
    
    private void compareMeta(meta metaExifFile, meta metaFile, meta metaExifXmp) {
        meta metaExif;
        meta metaSec = null;
        if (supportedRAWFileType(getFile().getName())) {
            if (metaExifXmp != null) metaExif = metaExifXmp;
            else {
                if (metaExifFile != null) metaExif = metaExifFile;
                else metaExif = new meta(null, null, null, null, null, null, null, null, null);
            }
            if (metaExifFile != null && metaExifFile.dID != null) notOrig();
        } else {
            if (metaExifFile == null) metaExif = new meta(null, null, null, null, null, null, null, null, null);
            else metaExif = metaExifFile;
            if (metaExifXmp != null) metaSec  = metaExifXmp;
        }
        if (metaFile == null) metaFile = new meta(null, null, null, null, null, null, null, null, null);
        
        compareModel(metaExif, metaFile);
        compareHash(metaExif, metaFile);
        compareDate(metaExif, metaFile);
        if (metaSec != null) compareXMP(metaSec);
                
    }
 
    private void notOrig() {
        if (orig.matches("[0-9x-zX-Z]")) orig = "a";
    }
    
    private File checkDir(String targetDirectory) {
        File targetDir = new File(targetDirectory);
        File[] listFiles = targetDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                meta v = getV(name);
                if (v != null) {
                    name = v.originalFilename;
                }
                if (originalName.toLowerCase().replaceFirst("jpg$", "arw").equals(name.toLowerCase())) return true;
                return false;
            }
        });
        return (listFiles != null && listFiles.length > 0) ? listFiles[0] : null;
    }
    
    private void checkRAW() {
        File rawFile;
        rawFile = checkDir(file.getParent());
        if  (rawFile != null && !rawFile.exists()) {
            rawFile = checkDir(targetDirectory);
        } 
        if  (rawFile != null && rawFile.exists()) {
            meta v = getV(rawFile.getName());
            if (v != null && v.odID != null) odID = v.odID;
            else if (v != null && v.dID != null) odID = v.dID;
            else odID = getHash(rawFile);
        }
    }
    
    private void addExif(ZonedDateTime value) {
        addExif("DateTimeOriginal", value.format(ExifDateFormat));
        addExif("xmp:DateTimeOriginal", value.format(ExifDateFormatTZ));
    }
    
    private void addExif(String field, String value) {
        exifMissing.add("-" + field + "=" + value);
    }
    
    private void updateExif() {
        String ext = FilenameUtils.getExtension(getFile().getName().toLowerCase()).toLowerCase();
        if (supportedRAWFileType(getFile().getName()) || ext.equals("jpg") || ext.equals("mp4")) {
            String updateFile = getFile().getName();
            if (supportedRAWFileType(getFile().getName())) {
                if (!(fileXmp != null && fileXmp.exists())) if (createXmp(getFile())==null) errorOut("xmp", new Exception("Couldn't create xmp for: " + getFile().getName()));
                if (fileXmp != null) {
                    exifMissing.add(fileXmp.getName());
                    org.nyusziful.ExifUtils.ExifReadWrite.updateExif(exifMissing, getFile().getParentFile());
                    exifMissing.remove(fileXmp.getName());
                } 
            }
            if (!exifMissing.isEmpty() && !(supportedRAWFileType(getFile().getName()) && orig.matches("[0-9]"))) {
                exifMissing.add(updateFile);
                    org.nyusziful.ExifUtils.ExifReadWrite.updateExif(exifMissing, getFile().getParentFile());
//                setiID();
            }
        }
    }
    
    private void validPath(Path path) throws IOException {
        if (Files.notExists(path.getParent())) {
            validPath(path.getParent());    
            Files.createDirectory(path.getParent());
        }
    }
    
    public void write() {
        if (processing.get()) {
            try {        
                validPath(this.getNewPath());
                updateExif();
//                if (iID == null && supportedMediaFileType(currentName.get())) setiID();
                if (view.getCopyOrMove() == COPY) {
                    Files.copy(this.getOldPath(), this.getNewPath());                               
                    if (fileXmp != null && fileXmp.exists())
                        Files.copy(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));                               
                } else if (view.getCopyOrMove() == MOVE) {
                    Files.move(this.getOldPath(), this.getNewPath());                               
                    if (fileXmp != null && fileXmp.exists())
                        Files.move(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));                               
                }
            } catch (IOException e) {
                System.out.println(e);
                //Todo logging, All for error OK
//                errorOut(this.getNewName(), e);         
            }                      
        }
    }

    private void addNote(String addition, Boolean critical) {
        note.set(note.get() + addition);
        if (critical) processing.set(false);
    }

    /**
     * 
     * @param filename
     * @return meta with information from the fileName, null if no compatible format found, unknown values are null 
     */
    public static meta getV(String filename) {
        meta metaFile = null;
        if (filename.startsWith("V") && filename.substring(2, 3).equals("_")) {
            switch (filename.substring(1, 2)) {
                case "5":
                    metaFile = getV5(filename);
                    break;
                case "6":
                    metaFile = getV6(filename);
                    break;
                default:
                    metaFile = null;
            }
        } else {
            if ((metaFile = getV1(filename)) == null)
                if ((metaFile = getV2(filename)) == null)
                    if ((metaFile = getV4(filename)) == null)
                        metaFile = getV3(filename);
            
        }
        return metaFile;
    }
    
    public static meta getV1(String filename) {//20160924_144402_ILCE-5100-DSC00615.JPG
        if (filename.length() > 17+4+1) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(filename.substring(0, 15), dfV1).atZone(ZoneId.systemDefault());
                String[] parts = filename.substring(15 + 1).split("-");
                if (parts.length == 2)
                    return new meta(parts[1], captureDate, null, parts[0], null, null, null, null, null);
                if (parts.length > 2)
                    for (String camera : CAMERAS)
                        if (filename.substring(15 + 1).startsWith(camera)) {
                            return new meta(filename.substring(15 + 1 + camera.length() + 1), captureDate, null, camera, null, null, null, null, null);
                        }
                errorOut("Not recognized camera", new Exception());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static meta getV2(String filename) {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
        if (filename.length() > 34+1+4) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(filename.substring(1, 10) + filename.substring(11, 17) + filename.substring(18, 22) + filename.substring(27, 32), dfV2).atZone(ZoneId.systemDefault());
                return new meta(filename.substring(34), captureDate, null, null, null, null, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static meta getV3(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-
        if (filename.length() > 34+1+4) {
            try {
                String dateString = 
                        filename.substring(1, 5) + 
                        filename.substring(6, 8) + 
                        filename.substring(9, 10) + 
                        filename.substring(11, 12) + 
                        filename.substring(13, 15) + 
                        filename.substring(16, 17) + 
                        filename.substring(18, 19) +
                        filename.substring(20, 22)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23, 28)));
                return new meta(filename.substring(35), captureDate, null, null, null, null, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static meta getV4(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-
        if (filename.length() > 34+1+4+32+32) {
            try {
                String dateString = 
                        filename.substring(1, 5) + 
                        filename.substring(6, 8) + 
                        filename.substring(9, 10) + 
                        filename.substring(11, 12) + 
                        filename.substring(13, 15) + 
                        filename.substring(16, 17) + 
                        filename.substring(18, 19) +
                        filename.substring(20, 22)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23, 28)));
                
                if (filename.substring(34, 35).equals("-") && filename.substring(67, 68).equals("-")) {
                    return new meta(filename.substring(101), captureDate, null, null, null, filename.substring(35, 67), filename.substring(68, 100), null, null);
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static meta getV5(String filename) {//V5_K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-
        int offsetV = 3;
        if (filename.length() > offsetV+34+1+4+32+32) {
            try {
                String dateString = 
                        filename.substring(1 + offsetV, 5 + offsetV) + 
                        filename.substring(6 + offsetV, 8 + offsetV) + 
                        filename.substring(9 + offsetV, 10 + offsetV) + 
                        filename.substring(11 + offsetV, 12 + offsetV) + 
                        filename.substring(13 + offsetV, 15 + offsetV) + 
                        filename.substring(16 + offsetV, 17 + offsetV) + 
                        filename.substring(18 + offsetV, 19 + offsetV) +
                        filename.substring(20 + offsetV, 22 + offsetV)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23 + offsetV, 28 + offsetV)));
                
                if (filename.substring(34 + offsetV, 35 + offsetV).equals("-") && filename.substring(67 + offsetV, 68 + offsetV).equals("-")) {
                    return new meta(filename.substring(101 + offsetV), captureDate, null, null, filename.substring(35 + offsetV, 67 + offsetV), filename.substring(68 + offsetV, 100 + offsetV), null, null, null);
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static meta getV6(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-0-
        int offsetV = 3;
        if (filename.length() > offsetV+34+1+4+32+2) {
            try {
                String dateString = 
                        filename.substring(1 + offsetV, 5 + offsetV) + 
                        filename.substring(6 + offsetV, 8 + offsetV) + 
                        filename.substring(9 + offsetV, 10 + offsetV) + 
                        filename.substring(11 + offsetV, 12 + offsetV) + 
                        filename.substring(13 + offsetV, 15 + offsetV) + 
                        filename.substring(16 + offsetV, 17 + offsetV) + 
                        filename.substring(18 + offsetV, 19 + offsetV) +
                        filename.substring(20 + offsetV, 22 + offsetV)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23 + offsetV, 28 + offsetV)));
                
                if (filename.substring(34 + offsetV, 35 + offsetV).equals("-") && filename.substring(67 + offsetV, 68 + offsetV).equals("-")) {
                    return new meta(filename.substring(70 + offsetV), captureDate, null, null, null, filename.substring(35 + offsetV, 67 + offsetV), null, null, filename.substring(68 + offsetV, 69 + offsetV));
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private meta repairMP4(meta metaExif) {
        ArrayList<String> meta = getExif(new String[]{"-DateTimeOriginal", "-DeviceManufacturer", "-DeviceModelName", "-CreationDateValue"}, getFile());
        Iterator<String> iterator = meta.iterator();
        String dto = null;
        String cdv = null;
        String model = null;
        String make = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            String tagValue = line.substring(34);
            switch (line.substring(0, 9)) {
                case "Date/Time":
                    dto = tagValue;
                    break;
                case "Creation ":
                    cdv = tagValue;
                    break;
                case "Device Ma":
                    make = tagValue;
                    break;
                case "Device Mo":
                    model = tagValue;
                    break;
            }
        }
        if (cdv != null && dto == null) {
            metaExif.model = model;
            addExif("Model", metaExif.model);
            metaExif.date = getZonedTimeFromStr(cdv);
            addExif(metaExif.date);
            addExif("Make", make);           
//            String[] commandAndOptions2 = {"exiftool", "-P", "-overwrite_original", "-DateTimeOriginal<CreationDateValue", "-Make<DeviceManufacturer", "-Model<DeviceModelName", getFile().getName()};
        }
        return metaExif;
    }

    private String dateFormat(ZonedDateTime zoned) {
        if (zoned == null) return "E0000-00-0_0@00-0_0-00(+0000)(Xxx)-";
        int offsetSec = zoned.getOffset().getTotalSeconds();
        String offsetSign;
        if (offsetSec < 0) {
            offsetSign = "-";
            offsetSec = offsetSec * -1;
        } else offsetSign = "+";
        String offsetH = Integer.toString(offsetSec / 3600);        
        if (offsetH.length() == 1) offsetH = "0" + offsetH;
        String offsetM = Integer.toString((offsetSec - (int)(offsetSec / 3600) * 3600)/ 60);
        if (offsetM.length() == 1) offsetM = "0" + offsetM;
        
        String dateS = zoned.withZoneSameInstant(ZoneId.of("UTC")).format(outputFormat);
        dateS = dateS.substring(0, 9) + "_" + dateS.substring(9, 15) + "_" + dateS.substring(15) + "(" + offsetSign + offsetH + offsetM + ")" + "(" + zoned.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + ")" + "-";
        return view.getPictureSet() + dateS;
    }// "K2016-11-0_3@07-5_0-24(+0100)(Thu)-"
    
    
    
    public final String getNewName() {return newName.get();}
    public final void setNewName(String fName) {newName.set(fName);}
    public SimpleStringProperty newNameProperty() {return newName;}

    public final String getCurrentName() {return currentName.get();}
    public final void setCurrentName(String fName) {currentName.set(fName);}
    public SimpleStringProperty currentNameProperty() {return currentName;}

    public final Boolean getProcessing() {return processing.get();}
    public final void setProcessing(Boolean proc) {processing.set(proc);}
    public SimpleBooleanProperty processingProperty() {return processing;}

    public final Boolean getXmpMissing() {return xmpMissing.get();}
    public final void setXmpMissing(Boolean xmp) {xmpMissing.set(xmp);}
    public SimpleBooleanProperty xmpMissingProperty() {return xmpMissing;}

    public final String getNote() {return note.get();}
    public final void setNote(String fName) {note.set(fName);}
    public SimpleStringProperty noteProperty() {return note;}

    /**
     * @return the iID
     */
    public String getiID() {
        return iID;
    }

    /**
     * @return the dID
     */
    public String getdID() {
        return dID;
    }

    /**
     * @return the odID
     */
    public String getOdID() {
        return odID;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    public Path getOldPath() {
        return getFile().toPath();
    }

    public Path getNewPath() {
        return Paths.get(targetDirectory + "\\" + this.getNewName());
    }
    
}


