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

import java.awt.Dialog;
import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JDialog;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FilenameUtils;

//Exiftool must be in PATH
// <2GB file support
public class PicOrganizes extends Application {        
    // <editor-fold defaultstate="collapsed" desc="User variables">
    private String pictureSet = "K";
    private Path toDir = Paths.get("G:\\Pictures\\Photos\\V4\\Közös");
    private File fromDir = new File("G:\\Pictures\\Photos\\Új");
    private String naModel;
    private int copyOrMove;
    private ZoneId zone;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Static variables">
    static String[] CAMERAS = {
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
    static DateTimeFormatter ExifDateFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");//2016:11:24 20:05:46
    static DateTimeFormatter ExifDateFormatTZ = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX");//2016:11:24 20:05:46+02:00
    static DateTimeFormatter XmpDateFormat = DateTimeFormatter.ISO_DATE_TIME;//2016-11-24T20:05:46
    static DateTimeFormatter XmpDateFormatTZ = DateTimeFormatter.ISO_OFFSET_DATE_TIME;//2016-11-24T20:05:46+02:00
    static DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss");//2016-11-24@20-05-46
    static DateTimeFormatter dfV1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");//20161124_200546
    static DateTimeFormatter dfV2 = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ssZ");//2016-11-24@20-05-46+0200
    static DateTimeFormatter dfV3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//20161124200546

    static int MOVE = 1;
    static int COPY = 0;
    // </editor-fold>

    /**
    * Variables
    */
    private Stage primaryStage;
    private BorderPane root;
    private int maxWidth, maxHeight;
    private final ProgressIndicator progressIndicator = new ProgressIndicator(0);
    private final ObservableList<mediaFile> data = FXCollections.observableArrayList();

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

    //Creates a mediaFile object for each media file in the directories
    private ArrayList<mediaFile> fileRenameList(ArrayList<String> directories, Path target) {
        Iterator<String> iter = directories.iterator();
        ArrayList<mediaFile> files = new ArrayList<>();
        while(iter.hasNext()) {
            File dir1 = new File(iter.next());
            if(dir1.isDirectory()) {
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                JProgressBar progressBar = new JProgressBar(0, content.length);
                JDialog progressDialog = progressDiag(progressBar); 
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<String> fileList = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        fileList.add(content[j*chunkSize + f].getName());
                    }
                    ArrayList<textMeta> exifToMeta = StaticTools.exifToMeta(fileList, dir1);
                    Iterator<textMeta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        textMeta next = iterator.next();
                        files.add(new mediaFile(this, next));
                        progressBar.setValue(i + j*chunkSize);
                        progressIndicator.setProgress((i + j*chunkSize)/content.length);
                    }
/*                    String fileList = "";
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        fileList += " \"" + content[j*chunkSize + f].getName() + "\"";
                    }
                    ArrayList<String> exifTool = StaticTools.exifTool(" -DateTimeOriginal -Model -DocumentID -OriginalDocumentID " + fileList, dir1);
                    Iterator<String> iterator = exifTool.iterator();
                    
                    int i = -1;
                    String filename = null;
                    String model = null;
                    String note = "";
                    String dID = null;
                    String odID = null;
                    String captureDate = null;
                    while (iterator.hasNext()) {
                        String line = iterator.next();
                        if (line.startsWith("========")) {
                            if (i > -1) {
                                files.add(new mediaFile(this, new File(filename), model, captureDate, note, dID, odID));
                                progressBar.setValue(i + j*chunkSize);
                                progressIndicator.setProgress((i + j*chunkSize)/content.length);
                            }
                            i++;
                            filename = dir1 + "\\" + line.substring(9);
                            model = null;
                            captureDate = null;
                            dID = null;
                            odID = null;
                            note = "";
                            
                        //End of exiftool output
                        } else if (line.contains("image files read")){
                            if (!line.contains(" 0 image files read")) {
                                files.add(new mediaFile(this, new File(filename), model, captureDate, note, dID, odID));
                            }
                        } else if (line.contains("files could not be read")){
                            
                        } else {
                            String tagValue = line.substring(34);
                            switch (line.substring(0, 4)) {
                                case "Date":
                                    captureDate = tagValue;
//                                    String dateString = tagValue.length()>25 ? tagValue.substring(0, 25) : tagValue; //2016:11:03 07:50:24+02:00
//                                    captureDate = StaticTools.getTimeFromStr(dateString, zone);
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
                    }*/
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
                    JProgressBar progressBar = new JProgressBar(0, content.length);
                    JDialog progressDialog = progressDiag(progressBar); 
                    int i = 0;
                    for (File content1 : content) {
                        Path source = content1.toPath();
                        String fileName = content1.getName().substring(1, 12);
                        fileName = fileName.substring(0, 4) + fileName.substring(5, 7) + fileName.substring(8, 9) + fileName.substring(10, 11);
                        if (!fileName.equals(oldName)) {
                            int fY = Integer.parseInt(fileName.substring(0, 4)), fM = Integer.parseInt(fileName.substring(4, 6))-1, fD = Integer.parseInt(fileName.substring(6, 8));
                            File[] dirs = getToDir().toFile().listFiles((File dir, String name) -> dir.isDirectory());
                            for (int j = 0; j < dirs.length; j++) {
                                String actDir = dirs[j].getName();
                                int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                                int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                                if ((fY*10000 + fM*100 + fD >= sY*10000 + sM*100 + sD) && (fY*10000 + fM*100 + fD <= eY*10000 + eM*100 + eD)) {
                                    target = dirs[j].toPath();
                                    files.add(new mediaFile(this, source.toString(), target + "\\"));
                                    System.out.println(source.toString() + " -> " + target);
                                    j = dirs.length;
                                }
                            }
                        } else {
                            files.add(new mediaFile(this, source.toString(), target + "\\"));
                            System.out.println(source.toString() + " -> " + target);
                        }   
                        oldName = fileName;
                        i++;
                        progressBar.setValue(i);
                        progressIndicator.setProgress((i)/content.length);
                    }
                    progressDialog.dispose();
                    listOnScreen(files);
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
                StaticTools.errorOut(fileMeta.getName(), e);         
                errors += e.getMessage() + " ";
            } catch (IOException e) {
                StaticTools.errorOut(fileMeta.getName(), e);         
                errors += e.getMessage() + " ";
            }
        } else {
            errors += "MetaData unavailable ";
        }
        res = new String[]{errors, warnings};
        return res;
    }

    private ArrayList<comparableMediaFile> readDirectoryContent(Path path) {
        final ArrayList<comparableMediaFile> files = new ArrayList();
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() 
            {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) {
                        if (!attrs.isDirectory() && attrs.isRegularFile()) {
                            files.add(new comparableMediaFile(file.toFile(), attrs.lastModifiedTime().toString(), attrs.size()));                               
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

    private String compareJPG(BufferedInputStream in, BufferedInputStream inB, long diff) {
        try {
            long j = StaticTools.startOfScanJPG(in);
            long jB = StaticTools.startOfScanJPG(inB);
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
        ArrayList<comparableMediaFile> fromFiles = readDirectoryContent(getFromDir().toPath());
        ArrayList<comparableMediaFile> toFiles = readDirectoryContent(getToDir());
        Boolean needsSFV = false;
        JProgressBar progressBar = new JProgressBar(0, toFiles.size());
        JDialog progressDialog = progressDiag(progressBar);                           
        int i = 1;
        for (comparableMediaFile file : toFiles) {
            progressBar.setValue(i); i++;
            for (comparableMediaFile baseFile : fromFiles) {
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
                                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.file.toString()));
                                    BufferedInputStream inB = new BufferedInputStream(new FileInputStream(baseFile.file.toString()));
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
        for (comparableMediaFile file : toFiles) {
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

    //Sets the center view to table format
    private void listOnScreen(ArrayList<mediaFile> newData) {
        data.removeAll(data);
        newData.stream().forEach((obj) -> {data.add(obj);});
        root.setCenter(createCenterTable());
        StaticTools.beep();
    }

    private TableView createCenterTable() {
        TableView<mediaFile> table = new TableView<>();
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
        return table;
    }
   
    //Sets the center view to pic stripes
    private void stripesOnScreen(File dir){
        TimeLine timeLine = new TimeLine(dir, this);
        root.setCenter(timeLine.getStripeBox());
        timeLine.resetView();
    }

    private void osNev() {
        zone = ZoneId.systemDefault();
        copyOrMove = MOVE;
        File dirRoot = new File("G:\\Pictures\\Photos\\Új");
        File[] dirs = dirRoot.listFiles(new FilenameFilter() {
             public boolean accept(File dir, String name) {
                 return (new File(dir + "\\" + name).isDirectory());
             }});
        for(int k = 0; k < dirs.length; k++) {
            File dir1 = dirs[k];
            if(dir1.isDirectory()) {
                System.out.println(dir1.getName());
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                JProgressBar progressBar = new JProgressBar(0, content.length);
                JDialog progressDialog = progressDiag(progressBar); 
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<String> files = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        files.add(content[j*chunkSize + f].getName());
                    }
                    ArrayList<textMeta> exifToMeta = StaticTools.exifToMeta(files, dir1);
                    Iterator<textMeta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        textMeta next = iterator.next();
                        mediaFile media = new mediaFile(this, next);
                        if (media.getProcessing()) {
                            media.write();
                        } else {
                            System.out.println(media.getNewName() + ": " + media.getNote());
                        }
                        i++;
                        progressBar.setValue(i + j*chunkSize);
                        progressIndicator.setProgress((i + j*chunkSize)/content.length);
                    }
                    
/*
                    String fileList = "";
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        fileList += " \"" + content[j*chunkSize + f].getName() + "\"";
                    }
                    ArrayList<String> exifTool = StaticTools.exifTool(" -DateTimeOriginal -Model -DocumentID -OriginalDocumentID " + fileList, dir1);
                    Iterator<String> iterator = exifTool.iterator();
                    
                    int i = -1;
                    String filename = null;
                    String model = null;
                    String note = "";
                    String dID = null;
                    String odID = null;
                    String captureDate = null;
                    while (iterator.hasNext()) {
                        String line = iterator.next();
                        if (line.startsWith("========")) {
                            if (i > -1) {
                                mediaFile media = new mediaFile(this, new File(filename), model, captureDate, note, dID, odID);
                                if (media.getProcessing()) {
                                    media.write();
                                } else {
                                    System.out.println(media.getNewName() + ": " + media.getNote());
                                }
                                progressBar.setValue(i + j*chunkSize);
                                progressIndicator.setProgress((i + j*chunkSize)/content.length);
                            }
                            i++;
                            filename = dir1 + "\\" + line.substring(9);
                            model = null;
                            captureDate = null;
                            dID = null;
                            odID = null;
                            note = "";
                            
                        //End of exiftool output
                        } else if (line.contains("image files read")){
                            if (!line.contains(" 0 image files read")) {
                                mediaFile media = new mediaFile(this, new File(filename), model, captureDate, note, dID, odID);
                                if (media.getProcessing()) {
                                    media.write();
                                } else {
                                    System.out.println(media.getNewName() + ": " + media.getNote());
                                }
                            }
                        } else if (line.contains("files could not be read")){
                            
                        } else {
                            String tagValue = line.substring(34);
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
                                    StaticTools.errorOut("xmp", new Exception(line));
                                    break;
                            }
                        }
                    }*/
                }
                progressDialog.dispose();
            }				
        }
    }

    private StringBuffer getDirHash(File dir, StringBuffer str) {
        if(dir.isDirectory()) {
            File[] dirs = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (new File(dir + "\\" + name).isDirectory());
                }});
            for(int i = 0; i < dirs.length; i++) {
                str = getDirHash(dirs[i], str);
            } 
            try {
                File[] content = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return supportedMediaFileType(name);
                    }});
                for(int i = 0; i < content.length; i++) {
                    String hash = StaticTools.getHash(content[i]);
                    str.append(hash + "\t" + content[i] + "\n");
                    content[i].renameTo(new File(content[i].getParentFile() + "\\" + hash + content[i].getName()));
                } 
            } catch (IOException ex) {
                Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }       
        return str;
    }

    
    // <editor-fold defaultstate="collapsed" desc="Sets the whole look and function for the GUI">
    private HBox createFunctionButtons() {
        //Definition of the function buttons
        Button btnImport = new Button("Import");
        btnImport.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                ArrayList<String> directories = chooseDirectories();
                Path backupdrive = null;
                while((backupdrive = backupMounted()) == null) {
                    StaticTools.errorOut("No backup Drive", new Exception("Attach a backup drive!"));
                }
                importFiles(directories, backupdrive);
            }
        });

        Button btnShift = new Button("Time shift");
        btnShift.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                File file = StaticTools.getDir(getFromDir());
                if(file != null) {
                    stripesOnScreen(file);
                }                    
            }
        });

        Button btnRename = new Button("Rename");
        btnRename.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                File file = StaticTools.getDir(getFromDir());
                if(file != null) {
                    ArrayList<String> directories = new ArrayList<String>();
                    directories.add(file.toString());
                    Path tempDir = Paths.get(getToDir().toString() + "\\" + file.getName());
                    listOnScreen(fileRenameList(directories, tempDir)); 
                }                    
            }
        });

        Button btnMove = new Button("Sort to Directories");
        btnMove.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                File file = StaticTools.getDir(getFromDir());
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
                File[] dirs = getToDir().toFile().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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
                File[] dirc = getFromDir().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(btnImport, btnShift, btnRename, btnMove, btnShow, btnComp);
        return header;
    }
    
    private VBox createSettings() {
        final ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    copyOrMove = (int) group.getSelectedToggle().getUserData();
                }                
            }
        });
        
        RadioButton rbCopy = new RadioButton("Copy");
        rbCopy.setToggleGroup(group);
        rbCopy.setUserData(COPY);
        RadioButton rbMove = new RadioButton("Move");
        rbMove.setUserData(MOVE);
        rbMove.setSelected(true);
        rbMove.setToggleGroup(group);

        VBox settings = new VBox();
        settings.getChildren().add(rbMove);
        settings.getChildren().add(rbCopy);
        return settings;
    }
    
    private VBox createHeader() {
        ObservableList<String> timeZones = FXCollections.observableArrayList(ZoneId.getAvailableZoneIds());
        final ComboBox comboBox = new ComboBox(timeZones);
        comboBox.setOnAction((event) -> {
            zone = ZoneId.of(comboBox.getSelectionModel().getSelectedItem().toString());
        });
        zone = ZoneId.systemDefault();
        comboBox.getSelectionModel().select(ZoneId.systemDefault().getId());

        HBox fromTo = new HBox();
            Label from = new Label(getFromDir().toString());
            Button btnFrom = new Button("Edit");
            btnFrom.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    File file = StaticTools.getDir(getFromDir());
                    if (file != null) {
                        fromDir = file;
                        from.setText(getFromDir().toString());
                    }
                }
            });
            Label to = new Label(getToDir().toString());
            Button btnTo = new Button("Edit");
            btnTo.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    File file = StaticTools.getDir(getToDir().toFile());
                    if (file != null) {
                        toDir = file.toPath();
                        to.setText(getToDir().toString());
                    }
                }
            });
            fromTo.getChildren().addAll(from, btnFrom, to, btnTo, comboBox);                   
        VBox head = new VBox();
        head.getChildren().addAll(createFunctionButtons(), fromTo);
        return head;
    }
    
    private HBox createFooter() {
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
                StaticTools.beep();
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
                    String replacement = getToDir() + "\\" + fileName;
                    paths.setNewName(replacement);
                }
            }
        });
        HBox footer = new HBox();
        footer.setSpacing(10);
        footer.setAlignment(Pos.CENTER);
        footer.getChildren().addAll(btnGo, btnClr, btnRefresh, progressIndicator);
        return footer;
    }
    
    private BorderPane createMainLook() {
        root = new BorderPane();            
        root.setLeft(createSettings());
        root.setTop(createHeader());
        root.setBottom(createFooter());
        return root;
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
    // </editor-fold>

    
    @Override
    public void start(Stage pStage) {
/*        StaticTools.copyAndBackup(new File("F:\\proba.arw"), new File("E:\\proba.arw"), new File("G:\\proba.arw"));
        String[] command = new String[]{"exiftool", "-a", "-G1", "20160325_130704_ILCE-5100-DSC05306.JPG"}; 
//        String[] command = new String[]{"exiftool", "-overwrite_original", "-n", "-DateTimeOriginal=2017:05:10 21:10:36+02:00", "-DocumentID=\"48f57c56c937f9dfe8ffdf73ee979c56\" ", "-OriginalDocumentID=\"48f57c56c937f9dfe8ffdf73ee979c56\" ", "DSC06063.JPG"};
        ArrayList<String> exifTool = StaticTools.exifTool(command, new File("E:\\jatszoter\\jpg"));
        String[] command2 = new String[]{"exiftool", "-a", "-G1", "20160325_050704_ILCE-5100-DSC05306_2.JPG"}; 
        ArrayList<String> exifTool2 = StaticTools.exifTool(command2, new File("E:\\jatszoter\\jpg"));
        Iterator<String> iterator = exifTool.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (!exifTool2.remove(next)) {
                System.out.println(next);
            }
        }
        System.out.println("----------------------------------");
        Iterator<String> iterator2 = exifTool2.iterator();
        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }
        
/*       osNev();
        if (true) return;
/*        StringBuffer str = getDirHash(new File("E:\\KÃƒÂ©pek"), new StringBuffer());
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File("E:\\hash.txt"), "UTF-8");
            writer.println(str);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
*/
        this.primaryStage = pStage;

        Parent mainLook = createMainLook();
        Scene mainScene = new Scene(mainLook);
        primaryStage.setScene(mainScene);

        //set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
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

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter section">
    /**
     * @return the pictureSet
     */
    public String getPictureSet() {
        return pictureSet;
    }

    /**
     * @return the toDir
     */
    public Path getToDir() {
        return toDir;
    }

    /**
     * @return the fromDir
     */
    public File getFromDir() {
        return fromDir;
    }

    /**
     * @return the naModel
     */
    public String getNaModel() {
        return naModel;
    }

    /**
     * @return the copyOrMove
     */
    public int getCopyOrMove() {
        return copyOrMove;
    }

    /**
     * @return the zone
     */
    public ZoneId getZone() {
        return zone;
    }
    // </editor-fold>
}
