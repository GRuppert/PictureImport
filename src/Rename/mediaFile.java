package Rename;


import static ExifUtils.ExifReadWrite.createXmp;
import static ExifUtils.ExifReadWrite.exifToMeta;
import static ExifUtils.ExifReadWrite.getExif;
import static Hash.Hash.EMPTYHASH;
import static Hash.Hash.getHash;
import static Hash.Hash.getFullHash;
import static Main.PicOrganizes.COPY;
import static Main.PicOrganizes.MOVE;
import static Main.PicOrganizes.view;
import static Main.StaticTools.ExifDateFormat;
import static Main.StaticTools.ExifDateFormatTZ;
import static Main.StaticTools.errorOut;
import static Main.StaticTools.getZonedTimeFromStr;
import static Main.StaticTools.supportedMediaFileType;
import static Main.StaticTools.supportedRAWFileType;
import static Main.StaticTools.supportedVideoFileType;
import java.io.File;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.io.FilenameUtils;

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
    
    public static String Version = "5";
    
    private final SimpleBooleanProperty processing;
    private final SimpleStringProperty currentName;
    private final SimpleStringProperty newName;
    private final SimpleStringProperty note;
    private final SimpleBooleanProperty xmpMissing;

    private File file;
    private File fileXmp;
    private String originalName;
    private String targetDirectory;

    private String iID;
    private String dID;
    private String odID;
    private String model = null;//Filename, Exif, xmp, xml
    private ZonedDateTime date = null;//Filename, Exif, xmp, xml, mod +TZ

    private HashMap<String, String> exifPar = new HashMap<>();
    private ArrayList<String> exifMissing = new ArrayList<String>();

    /**
     * @return the iID
     */
    public String getiID() {
        return iID;
    }

    /**
     * @param iID the iID to set
     */
    public final void setiID() {
        try {
            this.iID = getFullHash(file);
            newName.set(getNewFileName());

        } catch (IOException ex) {
            Logger.getLogger(mediaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getNewFileName() {
        return "V" + Version + "_" + dateFormat(date) + getiID() + "-" + getdID() + "-" + originalName;
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


    public mediaFile(String fileIn) {
        this(new File(fileIn));
    }

    public mediaFile(String fileIn, String targetDir) {
        this(fileIn);
        targetDirectory = targetDir;
    }

    public mediaFile(File fileIn) {
        this(fileIn, null);
    }
    
    public mediaFile(meta metaExif) {
        this(new File(metaExif.originalFilename), metaExif);
    }
    
    public mediaFile(File fileIn, meta metaExif) {
        this.file = fileIn;
        processing = new SimpleBooleanProperty(true);
        currentName = new SimpleStringProperty(fileIn.getName());
        note = new SimpleStringProperty("");
        if (metaExif != null && !metaExif.note.equals("")) addNote(metaExif.note, false);
        xmpMissing = new SimpleBooleanProperty(false);
        originalName = file.getName();
        targetDirectory = view.getToDir().toString() + "\\" + file.getParentFile().getName();  
        if (supportedMediaFileType(currentName.get())) {
            String ext = FilenameUtils.getExtension(originalName.toLowerCase());
            //standardizes data in Sony mp4 
            if (metaExif == null) {//if it hasn't been batch readed in the caller function
                metaExif = exifToMeta(file);
            }

            if (ext.equals("mp4")) {
                metaExif = repairMP4(metaExif);
            }
            
            //read data from filename
            meta metaFile;
            metaFile = getV(getFile().getName());
            
            //Set original filename, model, date if possible from filename
            if (metaFile != null && metaFile.originalFilename != null) originalName = metaFile.originalFilename;
            
            //read External sidecars
            meta metaXmp;
            if (supportedRAWFileType(originalName)) {
                fileXmp = new File(getFile().toString() + ".xmp");
                metaXmp = exifToMeta(fileXmp);
            } else {
                metaXmp = null;
            }
                        
            //compare/prioritizes filename and exif data(metaFile, metaXmp/metaExif)
            compareMeta(metaExif, metaFile, metaXmp);
 
            newName = new SimpleStringProperty(getNewFileName());
            if (exifMissing.isEmpty()) 
                setiID();
        } else {
            newName = new SimpleStringProperty(/*targetDirectory + "\\" + */originalName);
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
        if (true) {//version change
            try {
                this.dID = getHash(getFile());
                this.odID = this.dID;
            } catch (IOException ex) {
                this.dID = EMPTYHASH;
                this.odID = this.dID;
            }
            addExif("DocumentID" , getdID());
            addExif("OriginalDocumentID" , getOdID());
        } else {
            try {
                this.dID = getHash(getFile());
            } catch (IOException ex) {
                this.dID = EMPTYHASH;
            }
            if (getdID().equals(EMPTYHASH)) {
                addNote("Error during hashing" + model, true);
            }

            if (metaExif.dID != null && !metaExif.dID.equals(this.dID)) {addNote("dID has been changed", false); addExif("DocumentID" , getdID());}
            if (metaFile.dID != null && !metaFile.dID.equals(this.dID)) {addNote("dID has been changed", false); addExif("DocumentID" , getdID());}
            if (metaExif.dID == null) addExif("DocumentID" , getdID());

            if (metaFile.odID != null) odID = metaFile.odID;
            if (metaExif.odID != null)
                if (getOdID() != null) {
                    if (!metaExif.odID.equals(odID)) addNote("odID has been changed", true);
                } else {
                    odID = metaFile.odID; addNote("odID already presented", true);
                }
            if (getOdID() == null) {
                odID = getdID();
                addExif("OriginalDocumentID" , getOdID());
            }
        }
    }

    private void compareDate(meta metaExif, meta metaFile) {            
        if (metaFile.date != null) date = metaFile.date;
        if (metaExif.date != null) {
            if (metaExif.dateFormat == null || !metaExif.dateFormat) addExif(date);
            if (date != null) {
                Boolean equal = true;
                if (supportedVideoFileType(originalName)) {
                    if (Math.abs(ChronoUnit.SECONDS.between(date, metaExif.date)) > 180) {equal = false;}                    
                } else {
                    if (Math.abs(ChronoUnit.SECONDS.between(date, metaExif.date)) > 0) {equal = false;}                    
                }
                if (!equal) {
                    addNote("date has been changed: " + (date.toEpochSecond() - metaExif.date.toEpochSecond()) + "sec", true);
                } else {
                    date = metaExif.date;
                }
            } else {
                date = metaExif.date;
            }
        } else {
            if (date == null) {
                addNote("date missing", false);
            } else {
                addExif(date);
            }
        }
        if (date != null && date.getZone().equals(view.getZone())) {addNote("TZ different", true);}
    }
    
    private void compareXMP(meta metaXmp) {
        if (metaXmp.date != null && !metaXmp.date.isEqual(date)) addNote("XMP date wrong", false);
        if (metaXmp.model != null && !metaXmp.model.equals(model)) addNote("XMP model wrong", false);
        if (metaXmp.dID != null && !metaXmp.dID.equals(dID)) addNote("XMP dID wrong", false);
        if (metaXmp.odID != null && !metaXmp.odID.equals(odID)) addNote("XMP odID wrong", false);
    }
    
    private void compareMeta(meta metaExif, meta metaFile, meta metaXmp) {
        if (metaExif == null) metaExif = new meta(null, null, null, null, null, null, null, null);
        if (metaFile == null) metaFile = new meta(null, null, null, null, null, null, null, null);
        if (metaXmp == null) metaXmp = new meta(null, null, null, null, null, null, null, null);
        
        compareModel(metaExif, metaFile);
        compareHash(metaExif, metaFile);
        compareDate(metaExif, metaFile);
        compareXMP(metaXmp);
                
    }
 
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


    public Path getOldPath() {
        return getFile().toPath();
    }

    public Path getNewPath() {
        return Paths.get(targetDirectory + "\\" + this.getNewName());
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
                    ExifUtils.ExifReadWrite.updateExif(exifMissing, getFile().getParentFile());
                    exifMissing.remove(fileXmp.getName());
                } 
            }
            if (!exifMissing.isEmpty()) {
                exifMissing.add(updateFile);
                    ExifUtils.ExifReadWrite.updateExif(exifMissing, getFile().getParentFile());
                setiID();
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
                if (iID == null && supportedMediaFileType(currentName.get())) setiID();
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
                errorOut(this.getNewName(), e);         
            }                      
        }
    }

    private void addNote(String addition, Boolean critical) {
        note.set(note.get() + addition);
        if (critical) processing.set(false);
    }

    public static meta getV(String filename) {
        meta metaFile;
        if (filename.startsWith("V") && filename.substring(2, 3).equals("_")) {
            switch (filename.substring(1, 2)) {
                case "5":
                    metaFile = getV5(filename);
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
                    return new meta(parts[1], captureDate, null, parts[0], null, null, null, null);
                if (parts.length > 2)
                    for (String camera : CAMERAS)
                        if (filename.substring(15 + 1).startsWith(camera)) {
                            return new meta(filename.substring(15 + 1 + camera.length() + 1), captureDate, null, camera, null, null, null, null);
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
                return new meta(filename.substring(34), captureDate, null, null, null, null, null, null);
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
                return new meta(filename.substring(35), captureDate, null, null, null, null, null, null);
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
                    return new meta(filename.substring(101), captureDate, null, null, null, filename.substring(35, 67), filename.substring(68, 100), null);
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static meta getV5(String filename) {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-
        int offsetV5 = 3;
        if (filename.length() > offsetV5+34+1+4+32+32) {
            try {
                String dateString = 
                        filename.substring(1 + offsetV5, 5 + offsetV5) + 
                        filename.substring(6 + offsetV5, 8 + offsetV5) + 
                        filename.substring(9 + offsetV5, 10 + offsetV5) + 
                        filename.substring(11 + offsetV5, 12 + offsetV5) + 
                        filename.substring(13 + offsetV5, 15 + offsetV5) + 
                        filename.substring(16 + offsetV5, 17 + offsetV5) + 
                        filename.substring(18 + offsetV5, 19 + offsetV5) +
                        filename.substring(20 + offsetV5, 22 + offsetV5)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(filename.substring(23 + offsetV5, 28 + offsetV5)));
                
                if (filename.substring(34 + offsetV5, 35 + offsetV5).equals("-") && filename.substring(67 + offsetV5, 68 + offsetV5).equals("-")) {
                    return new meta(filename.substring(101 + offsetV5), captureDate, null, null, filename.substring(35 + offsetV5, 67 + offsetV5), filename.substring(68 + offsetV5, 100 + offsetV5), null, null);
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private meta repairMP4(meta metaExif) {
        ArrayList<String> meta = getExif(new String[]{"DateTimeOriginal", "DeviceManufacturer", "DeviceModelName", "CreationDateValue"}, getFile());
        Iterator<String> iterator = meta.iterator();
        String dto = null;
        String cdv = null;
        String model = null;
        String make = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            String tagValue = line.substring(34);
            switch (line.substring(0, 8)) {
                case "DateTime":
                    dto = tagValue;
                    break;
                case "Creation":
                    cdv = tagValue;
                    break;
                case "DeviceMa":
                    make = tagValue;
                    break;
                case "DeviceMo":
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
}


