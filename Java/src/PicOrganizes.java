import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.awt.Dialog;
import java.io.BufferedReader;


import java.io.File;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javafx.event.EventType;
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
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Exiftool must be in PATH

public class PicOrganizes extends Application {        
        String pictureSet = "K";
        Path toDir = Paths.get("J:\\Pictures");
        //Path toDir = Paths.get("E:\\exiftoolTest");
        
        File fromDir = new File("G:\\Pictures\\!Válogatós\\Közös\\Időzóna\\2015-05-01 - 2015-05-31 Ámerika");
        //File fromDir = new File("G:\\Pictures\\!VÃ¡logatÃ¡s\\KÃ¶zÃ¶s");
        TimeZone timeZoneLocal = TimeZone.getTimeZone("PST");
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

	static SimpleDateFormat exifDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        static SimpleDateFormat xmpDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); //2016-11-24T20:05:46+02:00
        static DateFormat xmpDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"); //2016-11-24T20:05:46+02:00
        static int MOVE = 1;
        static int COPY = 0;
        int copyOrMove;
        int maxWidth, maxHeight;
        final ProgressIndicator progressIndicator = new ProgressIndicator(0);
        private TableView<mediaFile> table = new TableView<>();
        private final ObservableList<mediaFile> data = FXCollections.observableArrayList();
        
        public class mediaFile {
            private final SimpleBooleanProperty processing;
            private final SimpleStringProperty currentName;
            private final SimpleStringProperty newName;
            private final SimpleStringProperty note;
            private final SimpleBooleanProperty xmpMissing;
            private File file;
            private File fileXmp;
            private File fileXml;
            private String originalName;
            private String model = null;
            private String targetDirectory;
            private HashMap<String, String> exifPar = new HashMap<>();;
            private GregorianCalendar modDate = new GregorianCalendar();
            private GregorianCalendar filenameDate = null;
            private GregorianCalendar metaDate = null;
            private GregorianCalendar xmpDate = null;
          
                    
            private mediaFile(String fileIn) {
                this(new File(fileIn));
            }

            private mediaFile(String fileIn, String targetDir) {
                this(fileIn);
                targetDirectory = targetDir;
            }

            private mediaFile(File fileIn) {
                this.file = fileIn;
                processing = new SimpleBooleanProperty(true);
                currentName = new SimpleStringProperty(file.toString());
                note = new SimpleStringProperty("");
                xmpMissing = new SimpleBooleanProperty(false);
                originalName = file.getName();
                targetDirectory = toDir.toString() + "\\" + file.getParentFile().getName();
                if (supportedMediaFileType(currentName.get())) {

                    String ext = FilenameUtils.getExtension(originalName.toLowerCase());
                    if (ext.equals("mp4")) {
                        System.out.println();
                    }

                    fileXmp = new File(file.toString() + ".xmp");
                    modDate.setTimeZone(timeZoneLocal);
                    modDate.setTimeInMillis(file.lastModified());
                    metaDate = readMeta(file);
                    xmpDate = readMeta(fileXmp);
                    getV1();
                    getV2();
                    fileXml = new File(file.toString().substring(0, file.toString().length()-4) + "M01.xml");
                    readXML(fileXml);
                    if (filenameDate != null) {
                        if (metaDate != null) {
                            if (Math.abs(filenameDate.getTimeInMillis() - metaDate.getTimeInMillis()) > 1000) {
                                addNote("Filename and Meta(" + metaDate.getTime().toString() + ") Date mismatch");
                            }
                        }
                        if (xmpDate != null) {
                            if (Math.abs(filenameDate.getTimeInMillis() - xmpDate.getTimeInMillis()) > 1000) {
                                addNote("Filename and XMP(" + xmpDate.getTime().toString() + ") Date mismatch");
                            }
                        }
                    }
                    if (metaDate != null) {
                        if (xmpDate != null) {
                            if (Math.abs(metaDate.getTimeInMillis() - xmpDate.getTimeInMillis()) > 1000) {
                                addNote("Meta(" + metaDate.getTime().toString() + ") and XMP(" + xmpDate.getTime().toString() + ") Date mismatch");
                            }
                        }
                    }
                    newName = new SimpleStringProperty(targetDirectory + "\\" + dateFormat(getDate()) + originalName);
                } else {
                    newName = new SimpleStringProperty(targetDirectory + "\\" + originalName);
                }
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
         
/*                            try {
                                Process exec = Runtime.getRuntime().exec(command);
                            } catch(Exception e) {
                                errorOut("xmp", e);
                            }*/
                            Files.move(Paths.get("E:\\temp.xmp"), Paths.get(this.getNewPath() + ".xmp"));                               
                        }
                    } catch (IOException e) {
                        errorOut(this.getNewName(), e);         
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
            
            private GregorianCalendar getDate() {
                if (xmpDate != null) return xmpDate;
                if (metaDate != null) return metaDate;
                if (filenameDate != null) return filenameDate;
                return modDate;
            }
            
            private void addNote(String addition) {
                note.set(note.get() + addition);
                processing.set(false);
            }
            
            private void getV1() {//20160924_144402_ILCE-5100-DSC00615.JPG
                if (file.getName().length() > 17+4+1) {
                    try {
                        DateFormat dfV1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        dfV1.setTimeZone(timeZoneLocal);
                        GregorianCalendar captureDate = new GregorianCalendar();
                        captureDate.setTimeZone(timeZoneLocal);
                        captureDate.setTime(dfV1.parse(file.getName().substring(0, 15)));
                        filenameDate = captureDate;
                        if (metaDate == null  && xmpDate == null) {
                            exifPar.put("CreationDate value", xmpDateFormat.format(captureDate.getTime()));
                            xmpMissing.set(true);
                        }
                        for (String camera : cameras)
                            if (file.getName().substring(15 + 1).startsWith(camera)) {
                                if (!camera.equals("NA")) {
                                    if (model != null) {
                                        if (!model.equals(camera))
                                            addNote("Filename(" + camera + ") and (" + model + ") Model mismatch ");
                                    } else {
                                        model = camera;
                                        exifPar.put("Device modelName", model);
                                        xmpMissing.set(true);
                                    }
                                }
                                originalName = file.getName().substring(15 + 1 + camera.length() + 1);
                                return;
                            }
                        addNote("Not recognized camera");
                    } catch (ParseException e) {
                    }
                }
            }

            private void getV2() {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
                if (file.getName().length() > 34+1+4) {
                    try {
                        DateFormat dfV2 = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ssZ");
                        GregorianCalendar captureDate = new GregorianCalendar();
                        captureDate.setTime(dfV2.parse(file.getName().substring(1, 10) + file.getName().substring(11, 17) + file.getName().substring(18, 22) + file.getName().substring(27, 32)));
                        filenameDate = captureDate;
                        if (metaDate == null  && xmpDate == null) {
                            exifPar.put("CreationDate value", xmpDateFormat.format(captureDate.getTime()));
                            xmpMissing.set(true);
                        }
                        originalName = file.getName().substring(34);
                    } catch (ParseException e) {
                    }
                }
            }

            private GregorianCalendar readMeta(File fileMeta) {
                if (fileMeta.exists()) {
                    Metadata metadata;
                    GregorianCalendar captureDate = null;
                    try {
                        metadata = ImageMetadataReader.readMetadata(fileMeta);
                        Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                        if (directory != null)
                            for (Tag tag : directory.getTags()) {
                                if (tag.getTagName().equals("Model"))
                                    if (model != null) {
                                        if (!model.equals(tag.getDescription())) 
                                            addNote("Meta(" + tag.getDescription() + ") and (" + model + ") Model mismatch ");
                                    } else {
                                        model = tag.getDescription();
                                    }
                                if (tag.getTagName().equals("Date/Time")) {
                                    String dateString = tag.getDescription(); //2016:11:03 07:50:24
                                    try {
                                        captureDate = new GregorianCalendar();
                                        captureDate.setTimeZone(timeZoneLocal);
                                        captureDate.setTime(exifDateFormat.parse(dateString));
                                    } catch (ParseException e) {
                                        captureDate = null;         
                                    }
                                }
                            }
                        Iterable<ExifSubIFDDirectory> directories2 = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
                        for (ExifSubIFDDirectory directory2 : directories2) {
            //            ExifSubIFDDirectory directory2 = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                            if (directory2 != null) {
                                if (directory2.getDateOriginal(timeZoneLocal) != null) {
                                    captureDate = new GregorianCalendar();
                                    captureDate.setTimeZone(timeZoneLocal);
                                    captureDate.setTime(directory2.getDateOriginal(timeZoneLocal));
                                }
                            }
                        }
                    } catch (ImageProcessingException e) {
                        captureDate = null;         
                    } catch (IOException e) {
                        captureDate = null;         
                        errorOut(fileMeta.getName(), e);         
                    }
                    return captureDate;
                } else {return null;}
            }

            private String dateFormat(GregorianCalendar calendar) {
                int offsetInMillis = (calendar.get(Calendar.ZONE_OFFSET)+calendar.get(Calendar.DST_OFFSET));
                String offset = String.format("%02d%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
                Date date = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("ETC/UTC"));
                DateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
                dayFormat.setTimeZone(TimeZone.getTimeZone("ETC/UTC"));
                String dateS = dateFormat.format(date);
                dateS = dateS.substring(0, 9) + "_" + dateS.substring(9, 15) + "_" + dateS.substring(15) + "_" + dayFormat.format(date) + "(" + ((offsetInMillis<0) ? "-" : "+") + offset + ")-";
                return pictureSet + dateS;
            }// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
            
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
                            try {
                                //2005:10:23 20:06:34.33-05:00
                                metaDate = new GregorianCalendar();
                                metaDate.setTime(xmpDateFormat.parse(get));
                            } catch (ParseException e) {
                            }
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

	private ArrayList<mediaFile> fileRenameList(ArrayList<String> directories, Path target) {
            Iterator<String> iter = directories.iterator();
            ArrayList<mediaFile> files = new ArrayList<>();
            while(iter.hasNext()) {
                File dir1 = new File(iter.next());
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                    JProgressBar progressBar = new JProgressBar(0, content.length);
                    JDialog progressDialog = progressDiag(progressBar);                           
                    for(int i = 0; i < content.length; i++) {
                        
                        files.add(new mediaFile(content[i]));
                        progressBar.setValue(i);
                        progressIndicator.setProgress(i/content.length);
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
                            GregorianCalendar cal = new GregorianCalendar();
                            if (!fileName.equals(oldName)) {
                                Date fileDate = new GregorianCalendar(Integer.parseInt(fileName.substring(0, 4)), Integer.parseInt(fileName.substring(4, 6))-1, Integer.parseInt(fileName.substring(6, 8))).getTime();
                                File[] dirs = fromDir.listFiles((File dir, String name) -> dir.isDirectory());
                                for (int j = 0; j < dirs.length; j++) {
                                    String actDir = dirs[j].getName();
                                    int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                                    int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                                    cal.clear();
                                    cal.set(sY, sM, sD);
                                    Date startDate = cal.getTime();
                                    cal.set(eY, eM, eD);
                                    Date endDate = cal.getTime();
//                                        Date startDate = new GregorianCalendar(sY, sM, sD).getTime();
//                                        Date endDate = new GregorianCalendar(eY, eM, eD).getTime();
                                    if (!(fileDate.before(startDate) || fileDate.after(endDate))) {
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
            public static final String OK = "OK";
            File file;
            String originalFileName;
            String date;
            Long size;
            String sfv;
            String result;
            
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
                        DateFormat dfV1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        dfV1.setTimeZone(timeZoneLocal);
                        GregorianCalendar captureDate = new GregorianCalendar();
                        captureDate.setTimeZone(timeZoneLocal);
                        captureDate.setTime(dfV1.parse(file.getName().substring(0, 15)));
                        for (String camera : cameras)
                            if (file.getName().substring(15 + 1).startsWith(camera)) {
                                originalFileName = file.getName().substring(15 + 1 + camera.length() + 1);
                                return;
                            }
                    } catch (ParseException e) {
                    }
                }
            }

            private void getV2() {// "K2016-11-0_3@07-5_0-24_Thu(p0100)-"
                if (file.getName().length() > 34+1+4) {
                    try {
                        DateFormat dfV2 = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ssZ");
                        GregorianCalendar captureDate = new GregorianCalendar();
                        captureDate.setTime(dfV2.parse(file.getName().substring(1, 10) + file.getName().substring(11, 17) + file.getName().substring(18, 22) + file.getName().substring(27, 32)));
                        originalFileName = file.getName().substring(34);
                    } catch (ParseException e) {
                    }
                }
            }

            private void getV3() {
                if (file.getName().length() > 7+1+4) {
                    try {
                        Integer.parseInt(file.getName().substring(1, 7));
                        originalFileName = file.getName().substring(7);
                    } catch (NumberFormatException e) {
                    }
                }
            }

        }              

        private String compareMeta(File fileMeta, File basefileMeta) {
            String result = "";
            if (fileMeta.exists() && basefileMeta.exists()) {
                Metadata metadata;
                Metadata metadataBase;
                try {
                    metadata = ImageMetadataReader.readMetadata(fileMeta);
                    metadataBase = ImageMetadataReader.readMetadata(basefileMeta);
                    for (Directory directory : metadata.getDirectories()) {
                        for (Directory directoryBase : metadataBase.getDirectories()) {
                            if (directoryBase.getName().equals(directory.getName())) {
                                Collection<Tag> tagsBase = directoryBase.getTags();
                                for (Tag tag : directory.getTags()) {
                                    if (!tagsBase.contains(tag)) {
                                        result += tag.getTagName() + ":" + tag.getDescription() + ";";
                                    }
                                }
                                break;
                            }
                        }
                    }
                } catch (ImageProcessingException e) {
                } catch (IOException e) {
                    errorOut(fileMeta.getName(), e);         
                }
            } else {return "No file found";}
            return result;
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
                                files.add(new comparableFile(file.toFile(), exifDateFormat.format(attrs.lastModifiedTime().toMillis()), attrs.size()));                               
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

        private void compare() {
            ArrayList<comparableFile> fromFiles = readDirectoryContent(fromDir.toPath());
            ArrayList<comparableFile> toFiles = readDirectoryContent(toDir);
            Boolean needsSFV = false;
            for (comparableFile file : toFiles) {
                for (comparableFile baseFile : fromFiles) {
                    if (file.originalFileName.equals(baseFile.originalFileName)) {
                        if (Objects.equals(file.size, baseFile.size)) {
                            file.result = comparableFile.OK;
                        } else {
                            if (Math.abs(file.size - baseFile.size) < 1000) {
                                file.result = compareMeta(file.file, baseFile.file);
                            }
                        }
                    } else {
                        if (Objects.equals(file.size, baseFile.size) && (file.file.getName().substring(file.file.getName().length() - 9, file.file.getName().length()).equals(baseFile.file.getName().substring(baseFile.file.getName().length() - 9, baseFile.file.getName().length())))) {
                            file.result = comparableFile.OK;
                        } else {
                            needsSFV = true;
                            file.result = "SFV";
                        }
                    }
                }
            }
            if (needsSFV) {
                
            } 
            int okFiles = 0;
            for (comparableFile file : toFiles) {
                if (file.result.equals(comparableFile.OK)) {
                    okFiles++;
                } else {
                    System.out.println(file.file.getName() + " " + file.result);
                }
            }            
            System.out.println("OK("+okFiles+"/"+toFiles.size()+")");
        }

    
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
            ObservableList<String> timeZones = FXCollections.observableArrayList(TimeZone.getAvailableIDs());
            exifDateFormat.setTimeZone(timeZoneLocal);
            xmpDateFormat.setTimeZone(timeZoneLocal);
            ComboBox comboBox = new ComboBox(timeZones);
            comboBox.setOnAction((event) -> {
                timeZoneLocal = TimeZone.getTimeZone(comboBox.getSelectionModel().getSelectedItem().toString());
                exifDateFormat.setTimeZone(timeZoneLocal);
                xmpDateFormat.setTimeZone(timeZoneLocal);
            });
            comboBox.getSelectionModel().select(timeZoneLocal.getID());
            RadioButton rbCopy = new RadioButton("Copy");
            rbCopy.setToggleGroup(group);
            rbCopy.setUserData(COPY);
            rbCopy.setSelected(true);
            RadioButton rbMove = new RadioButton("Move");
            rbMove.setUserData(MOVE);
            rbMove.setToggleGroup(group);

                Button btnImport = new Button("Import");
                btnImport.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        ArrayList<String> directories = chooseDirectories(); 
                        listOnScreen(fileRenameList(directories, toDir));
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
                        GregorianCalendar cal = new GregorianCalendar();
                        File[] dirc = fromDir.listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
                        Date startDate = cal.getTime();
                        Date endDate = cal.getTime();
                        String oldDir = "";
                        for(int j = 0; j < dirc.length; j++) {
                            String actDir = dirc[j].getName();
                            int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                            int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                            cal.clear();
                            cal.set(sY, sM, sD);
                            startDate = cal.getTime();
                            cal.set(eY, eM, eD);
    //                                        Date startDate = new GregorianCalendar(sY, sM, sD).getTime();
    //                                        Date endDate = new GregorianCalendar(eY, eM, eD).getTime();
                            if (!(endDate.before(startDate)) && j > 0) {
                                System.out.println(oldDir + " -><- " + actDir);
                            }
                            oldDir = actDir;
                            endDate = cal.getTime();
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
                    header.getChildren().addAll(btnImport, btnRename, btnMove, btnShow, btnComp);
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
                                from.setText(toDir.toString());
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
                            progressIndicator.setProgress(i/data.size());
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
