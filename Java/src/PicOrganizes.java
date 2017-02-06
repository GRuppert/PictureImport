import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.properties.XMPPropertyInfo;
import java.util.ArrayList;
import java.util.Iterator;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.xmp.XmpDirectory;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.awt.Dialog;
import java.io.BufferedReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FilenameUtils;

//Exiftool must be in PATH

public class PicOrganizes extends Application {        
        String pictureSet = "K";
        Path toDir = Paths.get("g:\\Ido\\x\\");
//        Path toDir = Paths.get("E:\\Purgatorium");
        
        File fromDir = new File("G:\\Pictures\\Régi képek\\Meg nem\\!Válogatós\\Közös\\Multi Esemény\\2014-02-25 - 2014-02-25 Síelés Jégmászás Freiburg");
//        File fromDir = new File("E:\\Képek\\Backup6000Test2\\DCIM\\100MSDCF");
//        TimeZone timeZoneLocal = TimeZone.getTimeZone("PST");
//        TimeZone timeZoneLocal = TimeZone.getTimeZone("Europe/Budapest");

        static String[] cameras = {
            "NA",
            "ILCE-5100",
            "ILCE-6000",
            "GT-I9192",
            "GT-I9195I",
            "Lumia 1020",
            "FinePix S5800 S800",
            "TG-3            "
        };
        static String[] imageFiles = {
            "jpg",
            "jpeg",
            "png",
            "gif",
            "tif",
            "arw",
            "nef",
            "dng",
            "nar"            
        };
        static String[] videoFiles = {
            "avi",
            "mpg",
            "mp4",
            "mts",
            "3gp",
            "mov"
        };
        static String[] metaFiles = {
            "gpx",
            "sfv",
            "pdf",
            "doc",
            "xls",
            "xlsx"
        };
        String naModel;

//	static SimpleDateFormat exifDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        static DateTimeFormatter exifDateFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");//2016:11:24 20:05:46+02:00
        static DateTimeFormatter exifDateFormatTZ = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX");//2016:11:24 20:05:46+02:00
        static DateTimeFormatter xmpDateFormatTZ = DateTimeFormatter.ISO_OFFSET_DATE_TIME;//2016:11:24 20:05:46+02:00

//        static SimpleDateFormat exifDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); //2016-11-24T20:05:46
//	static SimpleDateFormat exifDateFormatTZ = new SimpleDateFormat("yyyy:MM:dd HH:mm:ssX"); //2016-11-24T20:05:46+02:00
//        static SimpleDateFormat xmpDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); //2016-11-24T20:05:46+02:00
//        static DateFormat xmpDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"); //2016-11-24T20:05:46+02:00
        static int MOVE = 1;
        static int COPY = 0;
        int copyOrMove;
        int maxWidth, maxHeight;
        final ProgressIndicator progressIndicator = new ProgressIndicator(0);
        private TableView<mediaFile> table = new TableView<>();
        private final ObservableList<mediaFile> data = FXCollections.observableArrayList();
        ZoneId zone;
        
        private ArrayList<String> exifTool(String parameters) {
            final String command = "exiftool " + parameters;
            ArrayList<String> lines = new ArrayList<>();
            try {
                String line;
                Process p = Runtime.getRuntime().exec(command);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    lines.add(line);
                }
                input.close();
            } catch (Exception e) {
                errorOut("xmp", e);
            } 
            return lines;
        }
        
        public class mediaFile {
            private class meta{
                public String originalFilename;
                public ZonedDateTime date;
                public String model;
                
                public meta(String originalFilename, ZonedDateTime date, String model) {
                    this.originalFilename = originalFilename;
                    this.date = date;
                    this.model = model;
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
            private HashMap<String, String> exifPar = new HashMap<>();;
            private String model = null;//Filename, Exif, xmp, xml
            private ZonedDateTime date = null;//Filename, Exif, xmp, xml, mod +TZ
          
                    
            private mediaFile(String fileIn) {
                this(new File(fileIn));
            }

            private mediaFile(String fileIn, String targetDir) {
                this(fileIn);
                targetDirectory = targetDir;
            }

            private mediaFile(File fileIn) {
                this(fileIn, null, null, "");
            }

            private mediaFile(File fileIn, String exifModel, ZonedDateTime exifDate, String noteIn) {
                this.file = fileIn;
                System.out.println(file.toString());
                processing = new SimpleBooleanProperty(true);
                currentName = new SimpleStringProperty(file.toString());
                note = new SimpleStringProperty("");
                if (!noteIn.equals("")) addNote(noteIn);
                xmpMissing = new SimpleBooleanProperty(false);
                originalName = file.getName();
                targetDirectory = toDir.toString() + "\\" + file.getParentFile().getName();               
                
                if (supportedMediaFileType(currentName.get())) {
                    String ext = FilenameUtils.getExtension(originalName.toLowerCase());
                    if (ext.equals("mp4")) {
                        repairMP4();
                    }

                    //Set original filename, model, date if possible from filename
                    meta metaFile;
                    if ((metaFile = getV1()) == null)
                        if ((metaFile = getV2()) == null)
                            metaFile = getV3();
                    if (metaFile != null && metaFile.originalFilename != null) originalName = metaFile.originalFilename;
                    //External sidecars
                    meta metaXmp;
                    fileXmp = new File(file.toString() + ".xmp");
                    metaXmp = readExif(fileXmp);
//                    readXML(new File(file.toString().substring(0, file.toString().length()-4) + "M01.xml"));
                    
                    meta metaExif = null;
                    if (metaXmp == null) {
                        if (exifDate == null) {
                            metaExif = readExif(file);
                        } else {
                            metaExif = new meta(null, exifDate, exifModel);
                        }
                    }
                    
                    boolean dateOk;
                    boolean modelOk;
                    modelOk = compareModel(metaFile);
                    dateOk = compareDate(metaFile);
                    modelOk = modelOk && compareModel(metaXmp);
                    dateOk = dateOk && compareDate(metaXmp);
                    modelOk = modelOk && compareModel(metaExif);
                    dateOk = dateOk && compareDate(metaExif);
                    ArrayList<String> opt = new ArrayList<>();
                    if (!dateOk || date == null) {  
                        opt = new ArrayList<>();
                        if (!getMetaDate(metaFile).equals("Null")) opt.add(getMetaDate(metaFile));
                        if (!getMetaAltDate(metaFile).equals("Null")) opt.add(getMetaAltDate(metaFile));
                        if (!getMetaDate(metaXmp).equals("Null")) opt.add(getMetaDate(metaXmp));
                        if (!getMetaAltDate(metaXmp).equals("Null")) opt.add(getMetaAltDate(metaXmp));
                        if (!getMetaDate(metaExif).equals("Null")) opt.add(getMetaDate(metaExif));
                        if (!getMetaAltDate(metaExif).equals("Null")) opt.add(getMetaAltDate(metaExif));
                        addNote("Date problem:" + opt.toString());
/*                        int dateOption = question("Which date is correct?\n"
                                + "Filename own Zone"
                                + "              "
                                + "Filename program Zone"
                                + "                     "
                                + "Xmp own Zone"
                                + "                            "
                                + "Xmp program Zone"
                                + "                           "
                                + "Exif own Zone"
                                + "                            "
                                + "Exif program Zone"
                                , opt.toArray());*/
                        if (date == null) date = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault());                                
                    }
                        
                    if (!modelOk || model == null) {
                        opt = new ArrayList<>();
                        if (!getMetaModel(metaFile).equals("Null")) opt.add(getMetaModel(metaFile));
                        if (!getMetaModel(metaXmp).equals("Null")) opt.add(getMetaModel(metaXmp));
                        if (!getMetaModel(metaExif).equals("Null")) opt.add(getMetaModel(metaExif));
                        addNote("Date problem:" + opt.toString());
//                        int modelOption = question("Which model is correct?", opt.toArray());
                    }
/*                    
                    if (metaDate == null  && xmpDate == null) {
                        exifPar.put("CreationDate value", captureDate.format(exifDateFormat));
                        xmpMissing.set(true);
                    }
*/                    
                    newName = new SimpleStringProperty(targetDirectory + "\\" + dateFormat(date) + originalName);
                } else {
                    newName = new SimpleStringProperty(targetDirectory + "\\" + originalName);
                }
            }
            
            private String getMetaDate(meta metaIn) {
                if (metaIn != null) {
                    if (metaIn.date != null)
                        return metaIn.date.format(xmpDateFormatTZ);
                }
                return "Null";
            }
               
            private String getMetaAltDate(meta metaIn) {
                if (metaIn != null) {
                    if (metaIn.date != null)
                        if (!metaIn.date.getOffset().equals(metaIn.date.withZoneSameInstant(zone).getOffset()))
                        return metaIn.date.withZoneSameInstant(zone).format(xmpDateFormatTZ);
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
                file.toString(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);                
            }
            
            private boolean compareModel(meta metaIn) {
                if (metaIn != null)
                    if (metaIn.model != null)
                        if (model != null) {
                            if (model.equals(metaIn.equals(note))) return true;
                            else return false;
                        } else {
                            model = metaIn.model;
                            return true;
                        }
                return true;
            }
            
            public boolean compareDate(meta metaIn) {
                if (metaIn != null)
                    if (metaIn.date != null)
                        if (date != null) {
                            if (metaIn.date.getOffset().equals(metaIn.date.withZoneSameInstant(zone).getOffset()) && date.format(xmpDateFormatTZ).equals(metaIn.date.format(xmpDateFormatTZ)))
                                return true;
                            else
                                return false;
                        } else {
                            date = metaIn.date;
                            return true;
                        }
                return true;
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
                return Paths.get(currentName.get());
            }
            
            public Path getNewPath() {
                return Paths.get(newName.get());
            }
             
            public void write() {
                if (processing.get()) {
                    try {                                    
                        if (Files.notExists(this.getNewPath().getParent())) {
                                Files.createDirectory(this.getNewPath().getParent());
                        }
                        if (copyOrMove == COPY) {
                            Files.copy(this.getOldPath(), this.getNewPath());                               
                            if (fileXmp != null && fileXmp.exists())
                                Files.copy(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));                               
                        } else if (copyOrMove == MOVE) {
                            Files.move(this.getOldPath(), this.getNewPath());                               
                            if (fileXmp != null && fileXmp.exists())
                                Files.move(fileXmp.toPath(), Paths.get(this.getNewPath() + ".xmp"));                               
                        }
                        File f = new File(this.getNewPath().toString());
                        f.setReadOnly();
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
                        errorOut(this.getNewName(), e);         
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
                if (file.getName().length() > 17+4+1) {
                    try {
                        DateTimeFormatter dfV1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                        ZonedDateTime captureDate = LocalDateTime.parse(file.getName().substring(0, 15), dfV1).atZone(ZoneId.systemDefault());
                        for (String camera : cameras)
                            if (file.getName().substring(15 + 1).startsWith(camera)) {
                                return new meta(file.getName().substring(15 + 1 + camera.length() + 1), captureDate, camera);
                            }
                        addNote("Not recognized camera");
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                }
                return null;
            }

            private meta getV2() {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
                if (file.getName().length() > 34+1+4) {
                    try {
                        DateTimeFormatter dfV2 = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ssZ");
                        ZonedDateTime captureDate = LocalDateTime.parse(file.getName().substring(1, 10) + file.getName().substring(11, 17) + file.getName().substring(18, 22) + file.getName().substring(27, 32), dfV2).atZone(ZoneId.systemDefault());
                        return new meta(file.getName().substring(34), captureDate, null);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                }
                return null;
            }

            private meta getV3() {//K2016_11!0_4@15_1_0_38(+0100)(Fri)-
                if (file.getName().length() > 34+1+4) {
                    try {
                        DateTimeFormatter dfV3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        String dateString = 
                                file.getName().substring(1, 5) + 
                                file.getName().substring(6, 8) + 
                                file.getName().substring(9, 10) + 
                                file.getName().substring(11, 12) + 
                                file.getName().substring(13, 15) + 
                                file.getName().substring(16, 17) + 
                                file.getName().substring(18, 19) +
                                file.getName().substring(20, 22)
                        ;
                        ZonedDateTime captureDate = LocalDateTime.parse(dateString, dfV3).atZone(ZoneOffset.UTC);
                        captureDate = captureDate.withZoneSameInstant(ZoneId.of(file.getName().substring(23, 28)));
                        return new meta(file.getName().substring(35), captureDate, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            }
            
            private meta readExif(File fileMeta) {
                if (fileMeta.exists()) {
                    ArrayList<String> exifTool = exifTool(" -DateTimeOriginal -Model \"" + fileMeta + "\"");
                    Iterator<String> iterator = exifTool.iterator();
                    String model = null;
                    ZonedDateTime captureDate = null;
                    while (iterator.hasNext()) {
                        String line = iterator.next();
                        String tagValue = line.substring(34);
                        switch (line.substring(0, 4)) {
                            case "Date":
                                String dateString = tagValue.length()>25 ? tagValue.substring(0, 25) : tagValue; //2016:11:03 07:50:24
                                captureDate = getTimeFromStr(dateString);
                              break;
                            case "Came":
                                model = tagValue;
                              break;
                            case "Warn":
                                errorOut("xmp", new Exception(line));
                              break;
                        }
                    }
                    return new meta(null, captureDate, model);
                }
                return null;
            }

            private void repairMP4() {
                ArrayList<String> exifTool = exifTool(" -DateTimeOriginal -CreationDateValue \"" + file + "\"");
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
                    exifTool(" -P -overwrite_original \"-DateTimeOriginal<CreationDateValue\" \"-Make<DeviceManufacturer\" \"-Model<DeviceModelName\" \"" + file + "\"");
                }                
            }

            private String dateFormat(ZonedDateTime zoned) {
                String offsetS = zoned.getOffset().toString();
                offsetS = offsetS.replace(":", "");
                DateTimeFormatter newParser = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss");//2016-11-24@20-05-46+0200
                String dateS = zoned.withZoneSameInstant(ZoneId.of("UTC")).format(newParser);
                dateS = dateS.substring(0, 9) + "_" + dateS.substring(9, 15) + "_" + dateS.substring(15) + "(" + offsetS + ")" + "(" + zoned.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + ")" + "-";
                return pictureSet + dateS;
            }// "K2016-11-0_3@07-5_0-24(+0100)(Thu)-"
            
            //Unused code, meant to extract information from sony xml, but those are already presented in the mp4 file embedded
/*
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

        private ZonedDateTime getTimeFromStr(String input) {
            ZonedDateTime result = null;
            DateTimeFormatter newParser = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");//2016:11:24 20:05:46+02:00
            DateTimeFormatter newParserTZ = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX");//2016:11:24 20:05:46+02:00
            try {
                result = LocalDateTime.parse(input, newParser).atZone(zone);
            } catch (DateTimeParseException e) {
                try {
                    result = OffsetDateTime.parse(input, newParserTZ).toZonedDateTime();
                } catch (DateTimeParseException e2) {
                    //Bummer
                }
            }
            return result;
        }
        
        public static Boolean supportedFileType(String name) {
            if (supportedMetaFileType(name)) return true;
            if (supportedMediaFileType(name)) return true;
            return false;
        }
        public static Boolean supportedMetaFileType(String name) {
            String ext = FilenameUtils.getExtension(name.toLowerCase());
            for (String extSupported : metaFiles) {
                if (ext.equals(extSupported)) return true;
            }
            return false;
            
        }
        public static Boolean supportedMediaFileType(String name) {
            String ext = FilenameUtils.getExtension(name.toLowerCase());
            for (String extSupported : imageFiles) {
                if (ext.equals(extSupported)) return true;
            }
            for (String extSupported : videoFiles) {
                if (ext.equals(extSupported)) return true;
            }
            return false;
        }
        
        private ArrayList<String> chooseDirectories() {
		ArrayList<File> drives = new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();
                File[] paths;
                FileSystemView fsv = FileSystemView.getFileSystemView();
                paths = File.listRoots();
		ArrayList<String> Sony = new ArrayList<>();
		Sony.add("DCIM\\");
		Sony.add("PRIVATE\\AVCHD\\BDMV\\STREAM\\");
		Sony.add("PRIVATE\\M4ROOT\\CLIP\\");
		ArrayList<String> Samsung = new ArrayList<>();
		Samsung.add("DCIM\\Camera");
		Samsung.add("WhatsApp\\Media\\WhatsApp Images");

                for(File path:paths)
                {
                    String desc = fsv.getSystemTypeDescription(path);
                    if (desc.startsWith("USB") || desc.startsWith("SD")) drives.add(path);
                }
                
                for(File drive:drives) {                    
                    boolean valid = true;
                    for(String criteria:Sony) {
                        File probe = new File(drive+criteria);
                        if(probe.exists() && probe.isDirectory()) {
                            continue;
                        }
                        valid = false;
                        break;
                    }
                    if (valid) {
                        for (File subdir:new File(drive+Sony.get(0)).listFiles((File dir, String name) -> dir.isDirectory())) {
                            list.add(subdir.toString());
                        }
                        list.add(drive+Sony.get(1));
                        list.add(drive+Sony.get(2));
                    }
                }
                return list;		
	}
       
        private void listOnScreen(ArrayList<mediaFile> newData) {
            data.removeAll(data);
            newData.stream().forEach((obj) -> {data.add(obj);});
        }
                 
        private void errorOut(String source, Exception e) {
            JOptionPane.showMessageDialog(null, "From :" + source + "\nMessage: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        //Creates a mediaFile object for each media file in the directories
	private ArrayList<mediaFile> fileRenameList(ArrayList<String> directories, Path target) {
            Iterator<String> iter = directories.iterator();
            ArrayList<mediaFile> files = new ArrayList<>();
            while(iter.hasNext()) {
                File dir1 = new File(iter.next());
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                    JProgressBar progressBar = new JProgressBar(0, content.length);
                    JDialog progressDialog = progressDiag(progressBar); 
                    int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                    for (int j = 0; j*chunkSize < content.length; j++) {
                        String fileList = "";
                        for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                            fileList += " \"" + content[j*chunkSize + f].getPath() + "\"";
                        }
                        int i = -1;
                        String filename = null;
                        String model = null;
                        String note = "";
                        ZonedDateTime captureDate = null;
                        ArrayList<String> exifTool = exifTool("exiftool -DateTimeOriginal -Model \"" + fileList + "\"");
                        Iterator<String> iterator = exifTool.iterator();
                        while (iterator.hasNext()) {
                            String line = iterator.next();
                            if (line.startsWith("========")) {
                                if (i > -1) {
                                    files.add(new mediaFile(new File(filename), model, captureDate, note));
                                    progressBar.setValue(i + j*chunkSize);
                                    progressIndicator.setProgress((i + j*chunkSize)/content.length);
                                }
                                i++;
                                filename = line.substring(9);
                                model = null;
                                captureDate = null;
                            } else if (line.contains("image files read")){
                                files.add(new mediaFile(new File(filename), model, captureDate, note));
                            } else {
                                String tagValue = line.substring(34);
                                switch (line.substring(0, 4)) {
                                    case "Date":
                                        String dateString = tagValue.length()>25 ? tagValue.substring(0, 25) : tagValue; //2016:11:03 07:50:24+02:00
                                        captureDate = getTimeFromStr(dateString);
                                        break;
                                    case "Came":
                                        model = tagValue;
                                        break;
                                    case "Warn":
                                        errorOut("xmp", new Exception(line));
                                        break;
                                }
                            }
                        }
                    }
                    progressDialog.dispose();
                }				
            }
            return files;
	}       
       
	private void sortToDateDirectories(ArrayList<String> directories) {
            Iterator<String> iter = directories.iterator();
            while(iter.hasNext()) {
                File dir1 = new File(iter.next());
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                                name = name.toLowerCase();
                            return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".dng");
                        }});
                    if (content.length > 0) {
                        Path target = null;
                        String oldName = "";
                        ArrayList<mediaFile> files = new ArrayList<>();
                        for (File content1 : content) {
                            Path source = content1.toPath();
                            String fileName = content1.getName().substring(0, 8);
                            if (!fileName.equals(oldName)) {
                                int fY = Integer.parseInt(fileName.substring(0, 4)), fM = Integer.parseInt(fileName.substring(4, 6))-1, fD = Integer.parseInt(fileName.substring(6, 8));
                                File[] dirs = fromDir.listFiles((File dir, String name) -> dir.isDirectory());
                                for (int j = 0; j < dirs.length; j++) {
                                    String actDir = dirs[j].getName();
                                    int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                                    int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                                    if ((fY*10000 + fM*100 + fD >= sY*10000 + sM*100 + sD) && (fY*10000 + fM*100 + fD <= eY*10000 + eM*100 + eD)) {
                                        target = dirs[j].toPath();
                                        files.add(new mediaFile(source.toString(), target + "\\" + content1.getName()));
                                        j = dirs.length;
                                    }
                                }
                            }    
                            oldName = fileName;
                        }
                        listOnScreen(files);
                    }
                }				
            }
            
        }

        private class comparableFile {
            File file;
            String originalFileName;
            String date;
            Long size;
            String sfv;
            int match = 0;
            Boolean ok = false;
            String warnings = "";
            String errors = "";
                        
            comparableFile(File fileIn, String dateIn, Long sizeIn) {
                this.file = fileIn;
                this.date = dateIn;
                this.size = sizeIn;
                originalFileName = file.getName();
                getV1();
                getV2();
                getV3();
            }
            
            private void getV1() {//20160924_144402_ILCE-5100-DSC00615.JPG
                if (file.getName().length() > 17+4+1) {
                    try {
                        DateTimeFormatter dfV1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                        ZonedDateTime captureDate = LocalDateTime.parse(file.getName().substring(0, 15), dfV1).atZone(ZoneId.systemDefault());
                        for (String camera : cameras)
                            if (file.getName().substring(15 + 1).startsWith(camera)) {
                                originalFileName = file.getName().substring(15 + 1 + camera.length() + 1);
                                return;
                            }
                    } catch (DateTimeParseException e) {
                    }
                }
            }

            private void getV2() {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
                if (file.getName().length() > 34+1+4) {
                    try {
                        DateTimeFormatter dfV2 = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ssZ");
                        ZonedDateTime captureDate = LocalDateTime.parse(file.getName().substring(1, 10) + file.getName().substring(11, 17) + file.getName().substring(18, 22) + file.getName().substring(27, 32), dfV2).atZone(ZoneId.systemDefault());
                        originalFileName = file.getName().substring(34);
                    } catch (DateTimeParseException e) {
                    }
                }
            }

            private void getV3() {
                if (file.getName().length() > 7+1+4) {
                    try {
                        int parseInt = Integer.parseInt(file.getName().substring(0, 6));
                        if (parseInt < 110000)
                        originalFileName = file.getName().substring(7);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }              

        private String[] compareMeta(File fileMeta, File basefileMeta) {
            String warnings = "";
            String errors = "";
            String[] res;
            if (fileMeta.exists() && basefileMeta.exists()) {
                Metadata metadata;
                Metadata metadataBase;
                ArrayList<String[]> tags = new ArrayList();
                ArrayList<String[]> tagsBase = new ArrayList();
                ArrayList<String> unimportant = new ArrayList();
                unimportant.add("Date/Time");
                unimportant.add("Thumbnail Offset");
                unimportant.add("File Size");
                unimportant.add("File Modified Date");
                unimportant.add("File Name");
                unimportant.add("User Comment");
                try {
                    metadata = ImageMetadataReader.readMetadata(fileMeta);
                    metadataBase = ImageMetadataReader.readMetadata(basefileMeta);
                    for (Directory directoryBase : metadataBase.getDirectories()) {
                        if (!directoryBase.getClass().equals(XmpDirectory.class))
                            for (Tag tagBase : directoryBase.getTags()) {
                                String[] temp = {tagBase.getTagName(), tagBase.getDescription()};
                                tagsBase.add(temp);
                            }
                    }
                    for (Directory directory : metadata.getDirectories()) {
                        if (!directory.getClass().equals(XmpDirectory.class))
                            for (Tag tag : directory.getTags()) {
                                String[] temp = {tag.getTagName(), tag.getDescription()};
                                tags.add(temp);
                            }
                    }
                    Collection<XmpDirectory> xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);
                    for (XmpDirectory xmpDirectory : xmpDirectories) {
                        XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
                        XMPIterator iterator;
                        try {
                            iterator = xmpMeta.iterator();
                            while (iterator.hasNext()) {
                                XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo)iterator.next();
                                if (xmpPropertyInfo.getPath() != null && xmpPropertyInfo.getValue() != null) {
                                    String[] temp = {xmpPropertyInfo.getPath(), xmpPropertyInfo.getValue()};
                                    tagsBase.add(temp);
                                }
                            }
                        } catch (XMPException ex) {
                            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                    
                    Collection<XmpDirectory> xmpDirectoriesBase = metadata.getDirectoriesOfType(XmpDirectory.class);
                    for (XmpDirectory xmpDirectory : xmpDirectoriesBase) {
                        XMPMeta xmpMeta = xmpDirectory.getXMPMeta();
                        XMPIterator iterator;
                        try {
                            iterator = xmpMeta.iterator();
                            while (iterator.hasNext()) {
                                XMPPropertyInfo xmpPropertyInfo = (XMPPropertyInfo)iterator.next();
                                if (xmpPropertyInfo.getPath() != null && xmpPropertyInfo.getValue() != null) {
                                    String[] temp = {xmpPropertyInfo.getPath(), xmpPropertyInfo.getValue()};
                                    tagsBase.add(temp);
                                }
                            }
                        } catch (XMPException ex) {
                            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                    
                    
                    outerloop:
                    for (int i = 0; i < tags.size();) {
                        String[] tag = tags.get(i);
                        for (int j = 0; j < tagsBase.size();) {
                            String[] tagBase = tagsBase.get(j);
                            if (tag[0].equals(tagBase[0])) {
                                if (tag[1] != null && !tag[1].equals("")) {
                                    if (!tag[1].equals(tagBase[1])) {
                                        if (!unimportant.contains(tag[0])) {
                                            errors += tag[0] + ":" + tag[1] + "-><-" +tagBase[1] + " | ";
                                        }
                                    }
                                    tagsBase.remove(tagBase);                                
                                }
                                tags.remove(tag);
                                continue outerloop;
                            }
                            j++;
                        }
                        i++;
                    }
                    for (String[] tag : tags) {
                        errors += "+" + tag[0] + ":" + tag[1] + " | ";
                    }
                    for (String[] tag : tagsBase) {
                        warnings += "-" + tag[0] + ":" + tag[1] + " | ";
                    }
                } catch (ImageProcessingException e) {
                    errorOut(fileMeta.getName(), e);         
                    errors += e.getMessage() + " ";
                } catch (IOException e) {
                    errorOut(fileMeta.getName(), e);         
                    errors += e.getMessage() + " ";
                }
            } else {
                errors += "MetaData unavailable ";
            }
            res = new String[]{errors, warnings};
            return res;
        }
        
        private ArrayList<comparableFile> readDirectoryContent(Path path) {
            final ArrayList<comparableFile> files = new ArrayList();
            try
            {
                Files.walkFileTree (path, new SimpleFileVisitor<Path>() 
                {
                      @Override public FileVisitResult 
                    visitFile(Path file, BasicFileAttributes attrs) {
                            if (!attrs.isDirectory() && attrs.isRegularFile()) {
                                files.add(new comparableFile(file.toFile(), attrs.lastModifiedTime().toString(), attrs.size()));                               
                            }
                            return FileVisitResult.CONTINUE;                            
                        }

                      @Override public FileVisitResult 
                    visitFileFailed(Path file, IOException exc) {
                            System.out.println("skipped: " + file + " (" + exc + ")");
                            // Skip folders that can't be traversed
                            return FileVisitResult.CONTINUE;
                        }

                      @Override public FileVisitResult
                    postVisitDirectory (Path dir, IOException exc) {
                            if (exc != null)
                                System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
                            // Ignore errors traversing a folder
                            return FileVisitResult.CONTINUE;
                        }
                });
            }
            catch (IOException e)
            {
                throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
            }
            return files;
        } 

        private long startOfImage(FileInputStream in) throws IOException {
            int c;
            long j = 0;
            while ((c = in.read()) != -1) {
                if (c == 255) {
                    j++;
                    c = in.read();
                    if (c == 216) {
                        break;
                    }
                }
                j++;
            }
            Boolean marker = false;
            while ((c = in.read()) != -1) {
                if (marker)
                    //Not a marker(byte stuffing), shouldn't happen in header
                    if (c == 0) marker = false;
                    //Start of Quantization table, practically the image
                    else if (c == 219) return j; 
                    else {
                        long jump = 256*in.read() + in.read() - 2;
                        in.skip(jump);
                        j += jump + 2;                        
                        marker = false;
                    }
                else {
                    if (c == 255 ) {
                        marker = true;
                    }
                    else {
                        return -1;
                    }
                }
                j++;
            }
            return -1;  
        }

        private String compareJPG(FileInputStream in, FileInputStream inB, long diff) {
            try {
                long j = startOfImage(in);
                long jB = startOfImage(inB);
                if (j == -1 || jB == -1) return "Couldn't read the file ";
                if (diff == Math.abs(j - jB)) return "";
            } catch (FileNotFoundException ex) {
                return ex.getMessage();
            } catch (IOException ex) {
                return ex.getMessage();
            } finally {
                try {
                    in.close();
                    inB.close();
                } catch (IOException ex) {
                    return ex.getMessage();
                }
            }
            return "Difference after the first header ";
        }
        
        private void compare() {
            ArrayList<comparableFile> fromFiles = readDirectoryContent(fromDir.toPath());
            ArrayList<comparableFile> toFiles = readDirectoryContent(toDir);
            Boolean needsSFV = false;
            JProgressBar progressBar = new JProgressBar(0, toFiles.size());
            JDialog progressDialog = progressDiag(progressBar);                           
            int i = 1;
            for (comparableFile file : toFiles) {
                progressBar.setValue(i); i++;
                for (comparableFile baseFile : fromFiles) {
                    //Same filename
                    if (file.originalFileName.equals(baseFile.originalFileName)) {
                        //Same filename, same filesize consider it's ok
                        if (Objects.equals(file.size, baseFile.size)) {
                            file.ok = true;
                            file.match++;
                            break;
                        //Same filename, but different size. Where does it come from?
                        } else {
//                            if (Math.abs(file.size - baseFile.size) < 1000) {
                                if (FilenameUtils.getExtension(file.file.getName().toLowerCase()).equals("jpg") || FilenameUtils.getExtension(file.file.getName().toLowerCase()).equals("jpeg")) {
                                    long sizeDiff = Math.abs(file.size - baseFile.size);
                                    try {
                                        FileInputStream in = new FileInputStream(file.file.toString());
                                        FileInputStream inB = new FileInputStream(baseFile.file.toString());
                                        String resComp = compareJPG(in, inB, sizeDiff);
                                        if (resComp.equals("")) {
                                            String[] res = compareMeta(file.file, baseFile.file);
                                            file.errors += res[0];
                                            file.warnings += res[1];
                                        } else {
                                            file.errors +=  resComp; 
                                        }
                                        file.match++;
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
//                            }
                        }
                    //Different name, but
                    } else {
                        //Same filesize and the last 5 digits as well as the format match, presume it's the same
                        if (Objects.equals(file.size, baseFile.size) && (file.file.getName().substring(file.file.getName().length() - 9, file.file.getName().length()).equals(baseFile.file.getName().substring(baseFile.file.getName().length() - 9, baseFile.file.getName().length())))) {
                            file.ok = true;
                            file.match++;
                            break;
                        }
                    }
                }
                if (file.match == 0) {
                    needsSFV = true;
                    file.errors += "No match found ";
                }
            }
            progressDialog.dispose();
            if (needsSFV) {
                
            } 
            int okFiles = 0;
            ArrayList<String> errorList = new ArrayList();
            ArrayList<String> warningList = new ArrayList();
            for (comparableFile file : toFiles) {
                if (file.ok) {
                    okFiles++;
                } else {
                    if (!file.errors.equals("")) 
                        errorList.add(file.file.getName() + " " + file.errors);
                    if (!file.warnings.equals(""))
                        warningList.add(file.file.getName() + " " + file.warnings);
                }
            }              
            System.out.println("Warnings:");
            for (String war : warningList) {
                System.out.println(war);
            }
            System.out.println("Errors:");
            for (String err : errorList) {
                System.out.println(err);
            }
            System.out.println("OK:"+okFiles+"/Warning:"+warningList.size()+"/Error:"+errorList.size()+"/All:"+toFiles.size());
//            System.out.println("OK:"+okFiles+"/Missing Meta:"+metaMissing+"/Meta Conflict:"+metaConflict+"/Pic changed:"+changedPic+"/Error:"+error+"/All:"+toFiles.size()+")");
        }

        private Path backupMounted() {
            ArrayList<File> drives = new ArrayList<>();
            File[] paths;
            FileSystemView fsv = FileSystemView.getFileSystemView();
            paths = File.listRoots();
            for(File path:paths)
            {
                String desc = fsv.getSystemDisplayName(path);
                if (desc.startsWith("SP PHD U3")) return path.toPath();
            }
            return null;
        }

        private void importFiles(ArrayList<String> directories, Path backupdrive) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        //Sets the whole look and function for the GUI
        @Override
        public void start(Stage primaryStage) {
            DirectoryChooser chooser = new DirectoryChooser();
            final ToggleGroup group = new ToggleGroup();
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    if (group.getSelectedToggle() != null) {
                        copyOrMove = (int) group.getSelectedToggle().getUserData();
                    }                
                }
            });
            ObservableList<String> timeZones = FXCollections.observableArrayList(ZoneId.getAvailableZoneIds());
            final ComboBox comboBox = new ComboBox(timeZones);
            comboBox.setOnAction((event) -> {
                zone = ZoneId.of(comboBox.getSelectionModel().getSelectedItem().toString());
            });
            zone = ZoneId.systemDefault();
            comboBox.getSelectionModel().select(ZoneId.systemDefault().getId());
            RadioButton rbCopy = new RadioButton("Copy");
            rbCopy.setToggleGroup(group);
            rbCopy.setUserData(COPY);
            rbCopy.setSelected(true);
            RadioButton rbMove = new RadioButton("Move");
            rbMove.setUserData(MOVE);
            rbMove.setToggleGroup(group);
            
                //Definition of the function buttons

                Button btnImport = new Button("Import");
                btnImport.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        ArrayList<String> directories = chooseDirectories();
                        Path backupdrive = null;
                        while((backupdrive = backupMounted()) == null) {
                            errorOut("No backup Drive", new Exception("Attach a backup drive!"));
                        }
                        importFiles(directories, backupdrive);
                    }
                });

                Button btnShift = new Button("Time shift");
                btnShift.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                    }
                });

                Button btnRename = new Button("Rename");
                btnRename.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        if (Files.exists(fromDir.toPath()))
                            chooser.setInitialDirectory(fromDir);
                        else
                            chooser.setInitialDirectory(new File("C:\\"));
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            Path tempDir = Paths.get(toDir.toString() + "\\" + file.getName());
                            listOnScreen(fileRenameList(directories, tempDir)); 
                        }                    
                    }
                });

                Button btnMove = new Button("Sort to Directories");
                btnMove.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        if (Files.exists(toDir))
                            chooser.setInitialDirectory(toDir.toFile());
                        else
                            chooser.setInitialDirectory(new File("C:\\"));
                        File file = chooser.showDialog(primaryStage);
                        if(file != null) {
                            ArrayList<String> directories = new ArrayList<String>();
                            directories.add(file.toString());
                            sortToDateDirectories(directories); 
                        }                    
                    }
                });

                Button btnShow = new Button("Distribute");
                btnShow.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        File[] dirs = toDir.toFile().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
                        for(int j = 0; j < dirs.length; j++) {
                                File[] content = dirs[j].listFiles();
    /*                              File[] content = dirs[j].listFiles(new FilenameFilter() {
                                    public boolean accept(File dir, String name) {
                                        name = name.toLowerCase();
                                    return name.endsWith(".mts") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4");
                                }});                                
    */
                                System.out.println(dirs[j].getName() + " : " + content.length);
                        }
                        ZonedDateTime cal;
                        File[] dirc = fromDir.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
                        String oldDir = "";
                        for(int j = 0; j < dirc.length - 1; j++) {
                            String actDir = dirc[j].getName();
                            int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                            String nextDir = dirc[j].getName();
                            int sY = Integer.parseInt(nextDir.substring(0, 4)), sM = Integer.parseInt(nextDir.substring(5, 7))-1, sD = Integer.parseInt(nextDir.substring(8, 10));
                            if (sY*10000 + sM*100 + sD <= eY*10000 + eM*100 +  eD) {
                                System.out.println(oldDir + " -><- " + actDir);
                            }
                        }
                    }
                });
            
                Button btnComp = new Button("Compare");
                btnComp.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        compare();
                    }
                });
            
            BorderPane root = new BorderPane();            
                VBox settings = new VBox();
                    settings.getChildren().add(rbCopy);
                    settings.getChildren().add(rbMove);
            root.setLeft(settings);
                HBox header = new HBox();
                    header.setAlignment(Pos.CENTER);
                    header.getChildren().addAll(btnImport, btnShift, btnRename, btnMove, btnShow, btnComp);
                HBox fromTo = new HBox();
                    Label from = new Label(fromDir.toString());
                    Button btnFrom = new Button("Edit");
                    btnFrom.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            if (Files.exists(fromDir.toPath()))
                                chooser.setInitialDirectory(fromDir);
                            else
                                chooser.setInitialDirectory(new File("C:\\"));
                            File file = chooser.showDialog(primaryStage);
                            if (file != null) {
                                fromDir = file;
                                from.setText(fromDir.toString());
                            }
                        }
                    });
                    Label to = new Label(toDir.toString());
                    Button btnTo = new Button("Edit");
                    btnTo.setOnAction(new EventHandler<ActionEvent>(){
                        @Override
                        public void handle(ActionEvent event) {
                            if (Files.exists(toDir))
                                chooser.setInitialDirectory(toDir.toFile());
                            else
                                chooser.setInitialDirectory(new File("C:\\"));
                            File file = chooser.showDialog(primaryStage);
                            if (file != null) {
                                toDir = file.toPath();
                                to.setText(toDir.toString());
                            }
                        }
                    });
                    fromTo.getChildren().addAll(from, btnFrom, to, btnTo, comboBox);                   
                VBox head = new VBox();
                head.getChildren().addAll(header, fromTo);
            root.setTop(head);


            
                table.setEditable(true);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn< mediaFile, Boolean > processingCol = new TableColumn<>( "Ok" );
                processingCol.setCellValueFactory( f -> f.getValue().processingProperty());
                processingCol.setCellFactory(CheckBoxTableCell.forTableColumn(processingCol));
                processingCol.setPrefWidth(50);
                processingCol.setResizable(false);
                processingCol.setEditable(true);
                TableColumn oldNameCol = new TableColumn("Current Filename");
                oldNameCol.setCellValueFactory(new PropertyValueFactory<mediaFile, String>("currentName"));
                TableColumn newNameCol = new TableColumn("After Rename");
                newNameCol.setCellValueFactory(new PropertyValueFactory<mediaFile, String>("newName"));
                TableColumn noteCol = new TableColumn("Remarks");
                noteCol.setCellValueFactory(new PropertyValueFactory<mediaFile, String>("note"));
                TableColumn< mediaFile, Boolean > xmpCol = new TableColumn<>( "xmp" );
                xmpCol.setCellValueFactory( f -> f.getValue().xmpMissingProperty());
                xmpCol.setCellFactory(CheckBoxTableCell.forTableColumn(xmpCol));
                xmpCol.setPrefWidth(50);
                xmpCol.setResizable(false);
                table.setItems(data);
                table.getColumns().addAll(processingCol, oldNameCol, newNameCol, noteCol, xmpCol);  
            root.setCenter(table);
                Button btnGo = new Button("Go");
                btnGo.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        int i = 0;

                        JProgressBar progressBar = new JProgressBar(0, data.size());
                        JDialog progressDialog = progressDiag(progressBar);                           
                        for (mediaFile record : data) {
                            record.write();
                            progressBar.setValue(i);
                            progressIndicator.setProgress(i/data.size());
                            i++;
                        }
                        data.removeAll(data);                        
                        progressDialog.dispose();


                    }
                });

                Button btnClr = new Button("Abort");
                btnClr.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        data.removeAll(data);
                    }
                });

                Button btnRefresh = new Button("Refresh");
                btnRefresh.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        for(mediaFile paths:data) {
                            Path newPath = paths.getNewPath();
                            String fileName = newPath.getFileName().toString();
                            String replacement = toDir + "\\" + fileName;
                            paths.setNewName(replacement);
                        }
                    }
                });
                HBox footer = new HBox();
                    footer.setSpacing(10);
                    footer.setAlignment(Pos.CENTER);
                    footer.getChildren().addAll(btnGo, btnClr, btnRefresh, progressIndicator);
           
            root.setBottom(footer);
            Scene mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            //set Stage boundaries to visible bounds of the main screen
            maxWidth = (int) (primaryScreenBounds.getWidth() - 150);
            maxHeight = (int) (primaryScreenBounds.getHeight() - 150);
            primaryStage.setX(primaryScreenBounds.getMinX());
            primaryStage.setY(primaryScreenBounds.getMinY());
            primaryStage.setWidth(maxWidth);
            primaryStage.setHeight(maxHeight);
            primaryStage.show();
 	}       

        //Main function just launches the app
	public static void main(String[] args) {
            launch(args);
//            readMetaDataTest(new File("e:\\DSC07914.ARW"));
//            removeFiles();
//            compare();
	}
     
        
        private JDialog progressDiag(JProgressBar bar) {
            JDialog progressDialog = new JDialog(null, Dialog.ModalityType.MODELESS);
            JPanel newContentPane = new JPanel();
            newContentPane.setOpaque(true); //content panes must be opaque
            progressDialog.setContentPane(newContentPane);
            bar.setValue(0);
            bar.setStringPainted(true);
            newContentPane.add(bar);                            
            progressDialog.pack();
            progressDialog.setVisible(true);           
            return progressDialog;
        }
        
        public void createXMP(File file) {
         //exiftool -ext jpg -tagsfromfile -@ exif2xmp.args -@ iptc2xmp.args %d%f.xmp
         //exiftool -xmp:dateTimeOriginal="2005:10:23 20:06:34.33-05:00" a.jpg
         // -ext EXT
        }
        
	public void readMetaDataTest(File file) {
            Metadata metadata;
            try {
                metadata = ImageMetadataReader.readMetadata(file);
                Iterable<Directory> directories = metadata.getDirectories();
                for (Directory directory : directories) {
                    System.out.println("\n" + directory.getName() + "-------------\n");
                    for (Tag tag : directory.getTags()) {
                        System.out.println(tag.getTagName() + " : " + tag.getDescription());
                     }
                }
            } catch (ImageProcessingException e) {
                errorOut(file.getName(), e);         
            } catch (IOException e) {
                errorOut(file.getName(), e);         
            }
	}
        
	private void removeFiles() {
            File[] directories = toDir.toFile().listFiles((File dir, String name) -> dir.isDirectory());
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

        private void compareCSV() {
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
