import java.util.ArrayList;
import java.util.Iterator;


import java.awt.Dialog;

import java.io.File;
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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    private Path toDir = Paths.get("E:\\UDI");
    private File fromDir = new File("E:\\uid");
//    private Path toDir = Paths.get("G:\\Pictures\\Photos\\V4\\Közös");
//    private File fromDir = new File("G:\\Pictures\\Photos\\Új");
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
    private PicOrganizes view;
    private BorderPane root;
    private Tab tab1 = new Tab();
    private Tab tab2 = new Tab();
    private int maxWidth, maxHeight;
    private final ProgressIndicator progressIndicator = new ProgressIndicator(0);
    private final ObservableList<mediaFile> data = FXCollections.observableArrayList();
    private final ObservableList<duplicate> pairs = FXCollections.observableArrayList();

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

    private ArrayList<mediaFile> readDirectoryContent(Path path) {
        final ArrayList<mediaFile> files = new ArrayList();
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) {
                        if (!attrs.isDirectory() && attrs.isRegularFile()) {
                            files.add(new mediaFile(view, file.toString(), attrs.size()));                               
                        }
                        return FileVisitResult.CONTINUE;                            
                    }

                  @Override public FileVisitResult 
                visitFileFailed(Path file, IOException exc) {
                        StaticTools.errorOut(file.toString(), exc);
                        // Skip folders that can't be traversed
                        return FileVisitResult.CONTINUE;
                    }

                  @Override public FileVisitResult
                postVisitDirectory (Path dir, IOException exc) {
                        if (exc != null)
                        StaticTools.errorOut(dir.toString(), exc);
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
        ArrayList<mediaFile> fromFiles = readDirectoryContent(getFromDir().toPath());
        ArrayList<mediaFile> toFiles = readDirectoryContent(getToDir());
        ArrayList<mediaFile> singles = new ArrayList<>();
        fromFiles.stream().forEach((file) -> singles.add(file));
        toFiles.stream().forEach((file) -> singles.add(file));
        pairs.clear();
        toFiles.stream().forEach((file) -> {
            fromFiles.stream().forEach((baseFile) -> {
                if (file.getdID().equals(baseFile.getdID())) {
                    pairs.add(new duplicate(baseFile, file, true, baseFile.getFileSize() == file.getFileSize()));
                    singles.remove(baseFile);
                    singles.remove(file);
                } else
                    if (file.getOdID().equals(baseFile.getOdID())) {
                        pairs.add(new duplicate(baseFile, file, false, baseFile.getFileSize() == file.getFileSize()));
                        singles.remove(baseFile);
                        singles.remove(file);
                    }
            });
        });
        data.removeAll(data);
        singles.stream().forEach((obj) -> {data.add(obj);});
        tab1.setText("Singles");
        tab1.setContent(createMediafileTable());
        tab2.setText("Duplicates");
        tab2.setContent(createDuplicateTable());
        StaticTools.beep();
    }

    private TableView createDuplicateTable() {
        TableView<duplicate> table = new TableView<>();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn< duplicate, Boolean > processingCol = new TableColumn<>( "Ok" );
        processingCol.setCellValueFactory( f -> f.getValue().processingProperty());
        processingCol.setCellFactory(CheckBoxTableCell.forTableColumn(processingCol));
        processingCol.setPrefWidth(50);
        processingCol.setResizable(false);
        processingCol.setEditable(true);
        TableColumn firstNameCol = new TableColumn("Left Filename");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<duplicate, String>("firstName"));
        TableColumn buttonCol = new TableColumn("Metadata");
        buttonCol.setMinWidth(150);
        buttonCol.setCellValueFactory(new PropertyValueFactory<duplicate, Object>("metaDiffs"));
        buttonCol.setCellFactory(new Callback<TableColumn<duplicate, Object>, TableCell<duplicate, Object>>() {
          @Override 
          public TableCell<duplicate, Object> call(TableColumn<duplicate, Object> buttonCol) {
            return new TableCell<duplicate, Object>() {
              final Button button = new Button(); {
                button.setMinWidth(130);
              }
              @Override public void updateItem(final Object object, boolean empty) {
                super.updateItem(object, empty);
                String[][] metaDiffs = (String[][]) object;
                setGraphic(button);
                if (metaDiffs != null) {
                    button.setText(Integer.toString(metaDiffs.length));
                    button.setOnAction(new EventHandler<ActionEvent>() {
                      @Override public void handle(ActionEvent event) {
                        StackPane pane = new StackPane();
                        Scene scene = new Scene(pane);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        TableView table = new TableView();
                        table.setEditable(false);

                        TableColumn nameCol = new TableColumn("Field");
                        TableColumn firstCol = new TableColumn("Left Value");
                        TableColumn secondCol = new TableColumn("Right Value");
                        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                            @Override
                             public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                 String[] x = p.getValue();
                                 if (x != null && x.length>0) {
                                     return new SimpleStringProperty(x[0]);
                                 } else {
                                     return new SimpleStringProperty("<no name>");
                                 }
                             }
                        });
                        firstCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                            @Override
                             public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                 String[] x = p.getValue();
                                 if (x != null && x.length>0) {
                                     return new SimpleStringProperty(x[1]);
                                 } else {
                                     return new SimpleStringProperty("<no name>");
                                 }
                             }
                        });
                        secondCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                            @Override
                             public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                 String[] x = p.getValue();
                                 if (x != null && x.length>0) {
                                     return new SimpleStringProperty(x[2]);
                                 } else {
                                     return new SimpleStringProperty("<no name>");
                                 }
                             }
                        });
                        table.getItems().addAll(Arrays.asList(metaDiffs));
                        table.getColumns().addAll(nameCol, firstCol, secondCol);
                        pane.getChildren().add(table);
                        stage.show();
                      }
                    });
                } else {
                    button.setText("");
                }
              }
            };
          }
        });     
        
        
        TableColumn secondNameCol = new TableColumn("Right Filename");
        secondNameCol.setCellValueFactory(new PropertyValueFactory<duplicate, String>("secondName"));
        table.setItems(pairs);
        table.getColumns().addAll(processingCol, firstNameCol, buttonCol, secondNameCol); 
        return table;
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
        tab1.setText("Results");
        tab1.setContent(createMediafileTable());
        tab2.setText("");
        tab2.setContent(null);
        StaticTools.beep();
    }

    private TableView createMediafileTable() {
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
        File dirRoot = new File("G:\\Pictures\\Photos\\Régi képek\\Dupla\\Képek");
        toDir = Paths.get("G:\\Pictures\\Photos\\Régi képek\\Dupla\\rename");
//        File dirRoot = new File("G:\\Pictures\\Photos\\Új");
        System.out.println("---- " + dirRoot.getName() + " ----\n\n");
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

        Button btnAll = new Button("All");
        btnAll.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                data.forEach( f -> f.setProcessing(Boolean.TRUE));
            }
        });

        Button btnNone = new Button("None");
        btnNone.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                data.forEach( f -> f.setProcessing(Boolean.FALSE));
            }
        });

        Button btnInvert = new Button("Invert");
        btnInvert.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                data.forEach( f -> f.setProcessing(!f.getProcessing()));
            }
        });
        
        HBox buttonBox = new HBox();
            buttonBox.getChildren().addAll(btnAll, btnNone, btnInvert, comboBox);                   

        HBox fromBox = new HBox();
            Label from = new Label(getFromDir().toString());
            Button btnFrom = new Button("Set");
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
            fromBox.getChildren().addAll(btnFrom, from);                   

        HBox toBox = new HBox();
            Label to = new Label(getToDir().toString());
            Button btnTo = new Button("Set");
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
            toBox.getChildren().addAll(btnTo, to);                   
        
            
        VBox head = new VBox();
        head.getChildren().addAll(createFunctionButtons(), fromBox, toBox, buttonBox);
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
    
    private TabPane createCenter() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(tab1, tab2);
        return tabPane;
    }
    
    private BorderPane createMainLook() {
        root = new BorderPane();            
        root.setLeft(createSettings());
        root.setTop(createHeader());
        root.setBottom(createFooter());
        root.setCenter(createCenter());
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
        
*       osNev();
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
        view = this;
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
