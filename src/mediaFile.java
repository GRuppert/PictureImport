
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabor
 */
public class mediaFile {
    
    public static String EMPTYHASH = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

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
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    private class meta{
        public String originalFilename;
        public ZonedDateTime date;
        public String model;
        public String odID;
        public String dID;

        public meta(String originalFilename, ZonedDateTime date, String model, String odID, String dID) {
            this.originalFilename = originalFilename;
            this.date = date;
            this.model = model;
            this.odID = odID;
            this.dID = dID;
        }
    }

    private final SimpleBooleanProperty processing;
    private final SimpleStringProperty currentName;
    private final SimpleStringProperty newName;
    private final SimpleStringProperty note;
    private final SimpleBooleanProperty xmpMissing;
    private File file;
    private File fileXmp;
    private String originalName;
    private String targetDirectory;
    private String dID;
    private String odID;
    private long fileSize;
    private HashMap<String, String> exifPar = new HashMap<>();
    private ArrayList<String> exifMissing = null;
    private String model = null;//Filename, Exif, xmp, xml
    private ZonedDateTime date = null;//Filename, Exif, xmp, xml, mod +TZ


    public mediaFile(String fileIn) {
        this(new File(fileIn));
    }

    public mediaFile(String fileIn, long size) {        
        this(new File(fileIn));
        fileSize = size;
    }

    public mediaFile(String fileIn, String targetDir) {
        this(fileIn);
        targetDirectory = targetDir;
    }

    public mediaFile(File fileIn) {
        this(fileIn, null, null, "", null, null);
    }
    
    public mediaFile(textMeta meta) {
        this(new File(meta.filename), meta.model, meta.date, meta.note, meta.dID, meta.odID);
    }
    
    public mediaFile(File fileIn, String exifModel, String exifDate, String noteIn, String dIDin, String odIDin) {
        this.file = fileIn;
        processing = new SimpleBooleanProperty(true);
        currentName = new SimpleStringProperty(getFile().getName());
        note = new SimpleStringProperty("");
        if (!noteIn.equals("")) addNote(noteIn);
        xmpMissing = new SimpleBooleanProperty(false);
        originalName = file.getName();
        targetDirectory = PicOrganizes.view.getToDir().toString() + "\\" + file.getParentFile().getName();  
        if (PicOrganizes.supportedMediaFileType(currentName.get())) {
            String ext = FilenameUtils.getExtension(originalName.toLowerCase());
            //standardizes data in Sony mp4 
            if (ext.equals("mp4")) {
                repairMP4();
            }
            
            //read data from filename
            meta metaFile;
            if ((metaFile = getV1()) == null)
                if ((metaFile = getV2()) == null)
                    if ((metaFile = getV4()) == null)
                        metaFile = getV3();
            
            //Set original filename, model, date if possible from filename
            if (metaFile != null && metaFile.originalFilename != null) originalName = metaFile.originalFilename;
            
            //read External sidecars
            meta metaExif;
            fileXmp = new File(getFile().toString() + ".xmp");
            metaExif = readExif(fileXmp);
            if (metaExif == null) {//XMP must hold all the exif infos!
                if (exifDate == null) {//if it hasn't been batch readed in the caller function
                    metaExif = readExif(file);
                } else {
                    ZonedDateTime dateZ = StaticTools.getZonedTimeFromStr(exifDate);
                    if (dateZ == null) {
                        dateZ = StaticTools.getTimeFromStr(exifDate, PicOrganizes.view.getZone());
                        if (dateZ != null)
                            addExif("DateTimeOriginal", dateZ);
                    }
                    metaExif = new meta(null, dateZ, exifModel, odIDin, dIDin);
                }
            }
                        
            //compare/prioritizes filename and exif data(metaFile, metaXmp/metaExif)
            compareMeta(metaExif, metaFile);

            newName = new SimpleStringProperty(/*targetDirectory + "\\" + */dateFormat(date) + getOdID() + "-" + getdID() + "-" + originalName);
        } else {
            newName = new SimpleStringProperty(/*targetDirectory + "\\" + */originalName);
        }
    }

    private void compareMeta(meta metaExif, meta metaFile) {
        if (metaExif == null) metaExif = new meta(null, null, null, null, null);
        if (metaFile == null) metaFile = new meta(null, null, null, null, null);

        if (metaFile.model != null) model = metaFile.model;
        if (metaExif.model != null) {
            if (model != null) {
                if (!metaExif.model.trim().equals(model.trim())) {
                    addNote("model has been changed: " + model + " -> " + metaExif.model);
                }
            } else {
                model = metaExif.model;
            }
        } else {
            if (model == null) {
                addNote("model missing");
            } else {
                addExif("Model", model);
            }
        }
        
        
        try {
            this.dID = StaticTools.getHash(getFile());
        } catch (IOException ex) {
            this.dID = EMPTYHASH;
        }
        if (getdID().equals(EMPTYHASH)) {
            addNote("Error during hashing" + model);
        }

        if (metaExif.dID != null && !metaExif.dID.equals(this.dID)) addNote("dID has been changed");
        if (metaFile.dID != null && !metaFile.dID.equals(this.dID)) addNote("dID has been changed");
        if (metaExif.dID == null) addExif("DocumentID" , getdID());
        
        if (metaFile.odID != null) odID = metaFile.odID;
        if (metaExif.odID != null)
            if (getOdID() != null) {
                if (!metaExif.odID.equals(odID)) addNote("odID has been changed");
            } else {
                odID = metaFile.odID; addNote("odID already presented");
            }
        if (getOdID() == null) {
            odID = getdID();
            addExif("OriginalDocumentID" , getOdID());
        }
                
        if (metaFile.date != null) date = metaFile.date;
        if (metaExif.date != null) {
            if (date != null) {
                if (Math.abs(ChronoUnit.SECONDS.between(date, metaExif.date)) > 180) {
                    addNote("date has been changed: " + (date.toEpochSecond() - metaExif.date.toEpochSecond()) + "sec");
                } else {
                    date = metaExif.date;
                }
            } else {
                date = metaExif.date;
            }
        } else {
            if (date == null) {
                addNote("date missing");
            } else {
                addExif("DateTimeOriginal" , date);
            }
        }

    }
    
    private String getMetaDate(meta metaIn) {
        if (metaIn != null) {
            if (metaIn.date != null)
                return metaIn.date.format(PicOrganizes.XmpDateFormatTZ);
        }
        return "Null";
    }

    private String getMetaAltDate(meta metaIn) {
        if (metaIn != null) {
            if (metaIn.date != null)
                if (!metaIn.date.getOffset().equals(metaIn.date.withZoneSameInstant(PicOrganizes.view.getZone()).getOffset()))
                return metaIn.date.withZoneSameInstant(PicOrganizes.view.getZone()).format(PicOrganizes.XmpDateFormatTZ);
        }
        return "Null";
    }

    private String getMetaModel(meta metaIn) {
        if (metaIn != null) {
            if (metaIn.model != null)
                return metaIn.model;
        }
        return "Null";
    }

    private int question(String text, Object[] options) {
        return JOptionPane.showOptionDialog(null,
        text,
        getFile().toString(),
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]);                
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

    
    private File createXmp() {
        String[] commandAndOptions = {"exiftool", getFile().getName(), "-o", getFile().getName() + ".xmp"};
        ArrayList<String> result = StaticTools.exifTool(commandAndOptions, getFile().getParentFile());
        if (result.get(0).endsWith("files created")) return new File(getFile().getAbsolutePath() + ".xmp"); 
        return null;
    }
    
    private void addExif(String field, ZonedDateTime value) {
        addExif(field, value.format(PicOrganizes.ExifDateFormatTZ));
    }
    
    private void addExif(String field, String value) {
        if (exifMissing == null) {
            exifMissing = new ArrayList<String>();
            exifMissing.add("exiftool");
            exifMissing.add("-overwrite_original");
            exifMissing.add("-n");
        }
        exifMissing.add("-" + field + "=" + value);
//        exifMissing.add("-" + field + "=\"" + value + "\" ");
    }
    
    private void updateExif() {
        String ext = FilenameUtils.getExtension(getFile().getName().toLowerCase()).toLowerCase();
        if (ext.equals("arw") || ext.equals("jpg") || ext.equals("mp4")) {
            String updateFile = getFile().getName();
            if (ext.equals("arw")) {
                if (!(fileXmp != null && fileXmp.exists())) {fileXmp = createXmp();}
                if (fileXmp != null) {
                    updateFile = fileXmp.getName();
                } else {
                    StaticTools.errorOut("xmp", new Exception("Couldn't create xmp for: " + getFile().getName()));
                    return;
                }
            }
            if (exifMissing != null) {
                exifMissing.add(updateFile);
                StaticTools.exifTool(exifMissing.toArray(new String[0]), getFile().getParentFile());
            }
        }
    }
    
    public void write() {
        if (processing.get()) {
            try {                                    
                if (Files.notExists(this.getNewPath().getParent())) {
                        Files.createDirectory(this.getNewPath().getParent());
                }
                updateExif();
                if (PicOrganizes.view.getCopyOrMove() == PicOrganizes.COPY) {
                    Files.copy(this.getOldPath(), this.getNewPath());                               
                    if (fileXmp != null && fileXmp.exists())
                        Files.copy(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));                               
                } else if (PicOrganizes.view.getCopyOrMove() == PicOrganizes.MOVE) {
                    Files.move(this.getOldPath(), this.getNewPath());                               
                    if (fileXmp != null && fileXmp.exists())
                        Files.move(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));                               
                }
//                File f = new File(this.getNewPath().toString());
//                f.setReadOnly();

/*
                if (xmpMissing.get()) {

                    final String command = "exiftool" + getXmpParam() + " E:\\temp.xmp";
                    try {
                      String line;
                      Process p = Runtime.getRuntime().exec
                        (command);
                      BufferedReader input =
                        new BufferedReader
                          (new InputStreamReader(p.getInputStream()));
                      while ((line = input.readLine()) != null) {
                        System.out.println(line);
                      }
                      input.close();
                    }
                    catch (Exception e) {
                      errorOut("xmp", e);
                    }
                    Files.move(Paths.get("E:\\temp.xmp"), Paths.get(this.getNewPath() + ".xmp"));                               
                }
*/                                                
            } catch (IOException e) {
                StaticTools.errorOut(this.getNewName(), e);         
            }                      
        }
    }

/*            private ZonedDateTime getDate() {
        //TODO Visitson, ha nem jó az időzóna
        if (xmpDate != null) return xmpDate;
        if (metaDate != null) return metaDate;
        if (filenameDate != null) return filenameDate;
        return modDate;
    }*/

    private void addNote(String addition) {
        note.set(note.get() + addition);
        processing.set(false);
    }

    private meta getV1() {//20160924_144402_ILCE-5100-DSC00615.JPG
        if (getFile().getName().length() > 17+4+1) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(getFile().getName().substring(0, 15), PicOrganizes.dfV1).atZone(ZoneId.systemDefault());
                String[] parts = getFile().getName().substring(15 + 1).split("-");
                if (parts.length == 2)
                    return new meta(parts[1], captureDate, parts[0], null, null);
                if (parts.length > 2)
                    for (String camera : PicOrganizes.CAMERAS)
                        if (getFile().getName().substring(15 + 1).startsWith(camera)) {
                            return new meta(getFile().getName().substring(15 + 1 + camera.length() + 1), captureDate, camera, null, null);
                        }
                addNote("Not recognized camera");
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private meta getV2() {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
        if (getFile().getName().length() > 34+1+4) {
            try {
                ZonedDateTime captureDate = LocalDateTime.parse(getFile().getName().substring(1, 10) + getFile().getName().substring(11, 17) + getFile().getName().substring(18, 22) + getFile().getName().substring(27, 32), PicOrganizes.dfV2).atZone(ZoneId.systemDefault());
                return new meta(getFile().getName().substring(34), captureDate, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private meta getV3() {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-
        if (getFile().getName().length() > 34+1+4) {
            try {
                String dateString = 
                        getFile().getName().substring(1, 5) + 
                        getFile().getName().substring(6, 8) + 
                        getFile().getName().substring(9, 10) + 
                        getFile().getName().substring(11, 12) + 
                        getFile().getName().substring(13, 15) + 
                        getFile().getName().substring(16, 17) + 
                        getFile().getName().substring(18, 19) +
                        getFile().getName().substring(20, 22)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, PicOrganizes.dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(getFile().getName().substring(23, 28)));
                return new meta(getFile().getName().substring(35), captureDate, null, null, null);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private meta getV4() {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-
        if (getFile().getName().length() > 34+1+4+32+32) {
            try {
                String dateString = 
                        getFile().getName().substring(1, 5) + 
                        getFile().getName().substring(6, 8) + 
                        getFile().getName().substring(9, 10) + 
                        getFile().getName().substring(11, 12) + 
                        getFile().getName().substring(13, 15) + 
                        getFile().getName().substring(16, 17) + 
                        getFile().getName().substring(18, 19) +
                        getFile().getName().substring(20, 22)
                ;
                ZonedDateTime captureDate = LocalDateTime.parse(dateString, PicOrganizes.dfV3).atZone(ZoneOffset.UTC);
                captureDate = captureDate.withZoneSameInstant(ZoneId.of(getFile().getName().substring(23, 28)));
                
                if (getFile().getName().substring(34, 35).equals("-") && getFile().getName().substring(67, 68).equals("-")) {
                    return new meta(getFile().getName().substring(101), captureDate, null, getFile().getName().substring(35, 67), getFile().getName().substring(68, 100));
                    
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private meta readExif(File fileMeta) {//TODO merge with the one in PicOrganizes
        if (fileMeta.exists()) {
            ArrayList<String> files = new ArrayList<>();
            files.add(fileMeta.getName());
            ArrayList<textMeta> exifToMeta = StaticTools.exifToMeta(files, fileMeta.getParentFile());
            Iterator<textMeta> iterator = exifToMeta.iterator();
            if (iterator.hasNext()) {
                textMeta next = iterator.next();
                        String dateString = next.date;
                        ZonedDateTime captureDate = null;
                        if (dateString != null) {
                            captureDate = StaticTools.getZonedTimeFromStr(dateString);
                            if (captureDate == null) {
                                captureDate = StaticTools.getTimeFromStr(dateString, PicOrganizes.view.getZone());
                                if (captureDate != null)
                                    addExif("DateTimeOriginal", captureDate);
                            }
                        }
                      if (!next.note.equals("")) {StaticTools.errorOut("xmp", new Exception(next.note));}
                    return new meta(null, captureDate, next.model, next.odID, next.dID);
                
            }
/*            ArrayList<String> exifTool = StaticTools.exifTool(" -DateTimeOriginal -Model  -DocumentID -OriginalDocumentID \"" + fileMeta.getName() + "\"", fileMeta.getParentFile());
            Iterator<String> iterator = exifTool.iterator();
            String model = null;
            ZonedDateTime captureDate = null;
            String dID = null;
            String odID = null;
            while (iterator.hasNext()) {
                String line = iterator.next();
                String tagValue = line.substring(34);
                switch (line.substring(0, 4)) {
                    case "Date":
                        String dateString = tagValue.length()>25 ? tagValue.substring(0, 25) : tagValue; //2016:11:03 07:50:24
                        captureDate = StaticTools.getZonedTimeFromStr(dateString);
                        if (captureDate == null) {
                            captureDate = StaticTools.getTimeFromStr(dateString, view.getZone());
                            if (captureDate != null)
                                addExif("DateTimeOriginal", captureDate);
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
                        StaticTools.errorOut("xmp", new Exception(line));
                      break;
                }
            }
            return new meta(null, captureDate, model, odID, dID);*/
        }
        return null;
    }

    private void repairMP4() {
        String[] commandAndOptions = {"exiftool", "-DateTimeOriginal", "-CreationDateValue", getFile().getName()};
        ArrayList<String> exifTool = StaticTools.exifTool(commandAndOptions, getFile().getParentFile());
        Iterator<String> iterator = exifTool.iterator();
        String dto = null;
        String cdv = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            String tagValue = line.substring(34);
            switch (line.substring(0, 4)) {
                case "Date":
                    dto = tagValue;
                    break;
                case "Crea":
                    cdv = tagValue;
                    break;
            }
        }
        if (cdv != null && dto == null) {
            String[] commandAndOptions2 = {"exiftool", "-P", "-overwrite_original", "-DateTimeOriginal<CreationDateValue", "-Make<DeviceManufacturer", "-Model<DeviceModelName", getFile().getName()};
            StaticTools.exifTool(commandAndOptions2, getFile().getParentFile());
        }                
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
        
        String dateS = zoned.withZoneSameInstant(ZoneId.of("UTC")).format(PicOrganizes.outputFormat);
        dateS = dateS.substring(0, 9) + "_" + dateS.substring(9, 15) + "_" + dateS.substring(15) + "(" + offsetSign + offsetH + offsetM + ")" + "(" + zoned.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + ")" + "-";
        return PicOrganizes.view.getPictureSet() + dateS;
    }// "K2016-11-0_3@07-5_0-24(+0100)(Thu)-"

    //Unused code, meant to extract information from sony xml, but those are already presented in the mp4 file embedded
/*
//                    readXML(new File(file.toString().substring(0, file.toString().length()-4) + "M01.xml"));

    private void readXML(File inputFile) {
        if (inputFile.exists()) {
            try {	
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputFile);                        
                readNode(doc, "Device");
                readNode(doc, "CreationDate");
                readNode(doc, "LtcChangeTable");
                readNode(doc, "Duration");
                xmpMissing.set(true);
                String get = exifPar.get("CreationDate value");
                if (get != null) 
                    //2005:10:23 20:06:34.33-05:00
                    metaDate = getTimeFromStr(get);
                get = exifPar.get("Device modelName");
                if (get != null) {
                    model = get;
                    if (!model.equals(get)) 
                        addNote("Meta(" + get + ") and (" + model + ") Model mismatch ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }                
        }
    }

    private void readNode(Document doc, String nodeName) {
        NodeList childNodes = doc.getElementsByTagName(nodeName);
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            NamedNodeMap attributes = child.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                exifPar.put(child.getNodeName()+ " " + attributes.item(j).getNodeName(), attributes.item(j).getNodeValue());
            }  
        }
    }

    private String getXmpParam() {//, , LtcChangeTable halfStep, LtcChangeTable tcFps, 
        if (!xmpMissing.get()) return "";
        String param = "";
        String get = exifPar.get("CreationDate value");
        if (get != null) param += " -xmp:dateTimeOriginal=\"" + get + "\"";//2005:10:23 20:06:34.33-05:00
        get = exifPar.get("Device manufacturer");
        if (get != null) param += " -make=\"" + get + "\"";
        get = exifPar.get("Device modelName");
        if (get != null) param += " -model=\"" + get + "\"";
        get = exifPar.get("Device serialNo");
        if (get != null) param += " -SerialNumber=\"" + get + "\"";
        get = exifPar.get("Duration value");
        if (get != null) param += " -Duration={Scale=1.0,Value=" + get + "}";
        get = exifPar.get("LtcChangeTable tcFps");
        if (get != null) {
            int fps = Integer.parseInt(get);
            String dub = exifPar.get("LtcChangeTable halfStep");
            if (dub != null && dub.equals("true")) fps = fps * 2;
            param += " -VideoFrameRate=\"" + fps + "\"";
        }
        return param;
    }
*/            
}


