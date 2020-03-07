package org.nyusziful.pictureorganizer.Service.Rename;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.io.FilenameUtils;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.UI.Model.AbstractTableViewMediaFile;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.*;
import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

/**
 *
 * @author gabor
 */
public class AnalyzingMediaFile {
    private static String Version = "6";

    private File file;
    private File fileXmp;
    private String originalName;
    private int orig = -1;
    private String targetDirectory;
    private boolean forceRewrite;
    private ZoneId zone;
    private String pictureSet;

    private String iID;
    private String dID;
    private String odID;
    private String model = null;//Filename, Exif, xmp, xml
    private ZonedDateTime date = null;//Filename, Exif, xmp, xml, mod +TZ

//    private HashMap<String, String> exifPar = new HashMap<>();
    private ArrayList<String> exifMissing = new ArrayList<>();

/*
    public AnalyzingMediaFile(File fileIn, ZoneId zone, String pictureSet, String targetDirectory, boolean forceRewrite) {
        this.file = fileIn;
        this.zone = zone;
        this.pictureSet = pictureSet;
        this.forceRewrite = forceRewrite;
        this.targetDirectory = targetDirectory;
        Meta metaExif = null;

        processing = new SimpleBooleanProperty(true);
        currentName = new SimpleStringProperty(fileIn.getName());
        xmpMissing = new SimpleBooleanProperty(false);
        note = new SimpleStringProperty("");
        if (metaExif != null && !metaExif.note.equals("")) addNote(metaExif.note, false);

        originalName = file.getName();

        if (!supportedMediaFileType(currentName.get())) {
            newName = new SimpleStringProperty(originalName);
        } else {
            String ext = FilenameUtils.getExtension(originalName.toLowerCase());
            if (metaExif == null) {//if it hasn't been batch readed in the caller function
                metaExif = ExifService.readFileMeta(new File[] {file}, zone).iterator().next();
            }

            //standardizes data in Sony mp4 
            if (ext.equals("mp4")) {
                metaExif = repairMP4(metaExif);
            }
            
            //read data from filename
            Meta metaFile;
            metaFile = FileNameFactory.getV(getFile().getName());
            
            //Set original filename, orig if possible from filename
            if (metaFile != null && metaFile.originalFilename != null) originalName = metaFile.originalFilename;
            if (metaFile != null && metaFile.orig > -1) orig = metaFile.orig;
            
            //read External sidecars
            Meta metaXmp = null;
            if (supportedRAWFileType(originalName)) {
                fileXmp = new File(getFile().toString() + ".xmp");
                if (fileXmp.exists()) metaXmp = ExifService.readFileMeta(new File[] {fileXmp}, zone).iterator().next();
            }
                        
            //compare/prioritizes filename and exif data(metaFile, metaXmp/metaExif)
            compareMeta(metaExif, metaFile, metaXmp);
            if (orig == 0 && ext.equals("jpg")) checkRAW();
            
            if (!odID.equals(dID)) notOrig();
            
            newName = new SimpleStringProperty(getNewFileName(Version));
        }
    }

    private String getNewFileName(String ver) {
        switch (ver) {
            case "5":
                setiID();
                return FileNameFactory.getFileName(ver, pictureSet, originalName, date, getiID(), getdID(), -1);

            case "6":
                return FileNameFactory.getFileName(ver, pictureSet, originalName, date, "", getdID(), orig);
            default:
                return null;
        }
    }

    private void compareModel(Meta metaExif, Meta metaFile) {
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
    
    private void compareHash(Meta metaExif, Meta metaFile) {
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
                if (orig == 0) {
                    odID = getdID();
                } else if (metaFile.dID != null) {
                    odID = metaFile.dID;
                } else if (metaExif.dID != null) {
                    odID = metaExif.dID;
                }
                if (getOdID() == null) {
                    //Todo nyers-jpg
                    odID = MediaFileHash.EMPTYHASH;
                } else {
                    addExif("OriginalDocumentID" , getOdID());
                }
            }
        }
    }

    private void compareDate(Meta metaExif, Meta metaFile) {
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
        if (date != null && !date.isEqual(date.withZoneSameInstant(zone))) {addNote("TZ different", false);}
    }
    
    private void compareXMP(Meta metaXmp) {
        if (metaXmp.date != null && !metaXmp.date.isEqual(date)) addNote("XMP date wrong", false);
        if (metaXmp.model != null && !metaXmp.model.equals(model)) addNote("XMP model wrong", false);
        if (metaXmp.dID != null && !metaXmp.dID.equals(dID)) addNote("XMP dID wrong", false);
        if (metaXmp.odID != null && !metaXmp.odID.equals(odID)) addNote("XMP odID wrong", false);
    }
    
    private void compareMeta(Meta metaExifFile, Meta metaFile, Meta metaExifXmp) {
        Meta metaExif;
        Meta metaSec = null;
        if (supportedRAWFileType(getFile().getName())) {
            if (metaExifXmp != null) metaExif = metaExifXmp;
            else {
                if (metaExifFile != null) metaExif = metaExifFile;
                else metaExif = new Meta(null, null, null, null, null, null, null, null, -1);
            }
            if (metaExifFile != null && metaExifFile.dID != null) notOrig();
        } else {
            if (metaExifFile == null) metaExif = new Meta(null, null, null, null, null, null, null, null, -1);
            else metaExif = metaExifFile;
            if (metaExifXmp != null) metaSec  = metaExifXmp;
        }
        if (metaFile == null) metaFile = new Meta(null, null, null, null, null, null, null, null, -1);
        
        compareModel(metaExif, metaFile);
        compareHash(metaExif, metaFile);
        compareDate(metaExif, metaFile);
        if (metaSec != null) compareXMP(metaSec);
                
    }
 
    private void notOrig() {
        orig = getVersionNumber();
    }

    private File checkDir(File targetDir) {
        File[] listFiles = targetDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Meta v = FileNameFactory.getV(name);
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
        rawFile = checkDir(file.getParentFile());
        if  (rawFile != null && !rawFile.exists()) {
            rawFile = checkDir(this.getNewPath().getParent().toFile());
        } 
        if  (rawFile != null && rawFile.exists()) {
            Meta v = FileNameFactory.getV(rawFile.getName());
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
                if (!(fileXmp != null && fileXmp.exists())) if (ExifService.createXmp(getFile())==null) errorOut("xmp", new Exception("Couldn't create xmp for: " + getFile().getName()));
                if (fileXmp != null) {
                    exifMissing.add(fileXmp.getName());
                    ExifService.updateExif(exifMissing, getFile().getParentFile());
                    exifMissing.remove(fileXmp.getName());
                } 
            }
            if (!exifMissing.isEmpty() && !(supportedRAWFileType(getFile().getName()) && orig == 0)) {
                exifMissing.add(updateFile);
                    ExifService.updateExif(exifMissing, getFile().getParentFile());
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
    
    public boolean write(WriteMethod writeMethod) {
        if (processing.get()) {
            try {        
                validPath(this.getNewPath());
                updateExif();
//                if (iID == null && supportedMediaFileType(currentName.get())) setiID();
                switch (writeMethod) {
                    case COPY:
                        Files.copy(this.getOldPath(), this.getNewPath());
                        if (fileXmp != null && fileXmp.exists())
                            Files.copy(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));
                        break;
                    case MOVE:
                        Files.move(this.getOldPath(), this.getNewPath());
                        if (fileXmp != null && fileXmp.exists())
                            Files.move(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));
                        break;
                }
                return true;
            } catch (IOException e) {
                System.out.println(e);
                //Todo logging, All for error OK
//                errorOut(this.getNewName(), e);
            }
        }
        return false;
    }

    private void addNote(String addition, Boolean critical) {
        note.set(note.get() + addition);
        if (critical) processing.set(false);
    }

    private Meta repairMP4(Meta metaExif) {
        ArrayList<String> meta = ExifService.getExif(new String[]{"-DateTimeOriginal", "-DeviceManufacturer", "-DeviceModelName", "-CreationDateValue"}, getFile());
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

    private void setiID() {
        this.iID = getFullHash(file);
        newName.set(getNewFileName("5"));
    }
*/
    // <editor-fold defaultstate="collapsed" desc="Getter-Setter section">
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

    /**
     * Target directory\Parent directory\New filename
     * @return
     */
/*    public Path getNewPath() {
        return Paths.get(targetDirectory + "\\" + file.getParentFile().getName() + "\\" + this.getNewName());
    }
*/

}


