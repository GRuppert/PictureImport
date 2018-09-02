package org.nyusziful.Main;

import org.nyusziful.Comparison.Listing;
import org.nyusziful.Comparison.comparableMediaFile;
import org.nyusziful.Rename.mediaFile;
import org.nyusziful.Rename.metaProp;
import org.nyusziful.Comparison.duplicate;
import org.nyusziful.Comparison.metaChanges;
import static org.nyusziful.ExifUtils.ExifReadWrite.exifToMeta;
import static org.nyusziful.Hash.abstractHash.getHash;
import static org.nyusziful.Main.StaticTools.errorOut;
import static org.nyusziful.Main.StaticTools.supportedFileType;
import static org.nyusziful.Main.StaticTools.supportedMediaFileType;
import org.nyusziful.Rename.meta;
import org.nyusziful.TimeShift.TimeLine;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Dialog;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import static org.nyusziful.ExifUtils.ExifReadWrite.exifToMeta;

//Exiftool must be in PATH
// <2GB file support
public class PicOrganizes extends Application {        
    // <editor-fold defaultstate="collapsed" desc="User variables">
    private String pictureSet = "K";
    private Path toDir = Paths.get("G:\\Pictures\\Photos\\Új\\SzandranakUj");
    private File fromDir = new File("G:\\Pictures\\Photos\\Új\\Szandranak");
//    private Path toDir = Paths.get("E:\\temp\\compare\\1");
//    private File fromDir = new File("E:\\temp\\compare\\2");
//    private Path toDir = Paths.get("G:\\Pictures\\Photos\\V5\\Közös");
//    private File fromDir = new File("G:\\Pictures\\Photos\\Új");
    private String naModel;
    private int copyOrMove;
    private ZoneId zone;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Static variables">
    public static int MOVE = 1;
    public static int COPY = 0;
    // </editor-fold>

    /**
    * Variables
    */
    public static PicOrganizes view;
    public final ProgressIndicator progressIndicator = new ProgressIndicator(0);
    public XYChart.Series speeds = new XYChart.Series();

    private long fileSizeCountTotal = 0;
    private long fileSizeCount = 0;
    private long fileCountTotal = 0;
    private long fileCount = 0;
    private Stage primaryStage;
    private BorderPane root;
    private final Tab tab1 = new Tab();
    private final Tab tab2 = new Tab();
    private final Tab tab3 = new Tab();
    private final Tab tab4 = new Tab();
    private final Button btnGo = new Button("Go");

    private int maxWidth, maxHeight;
    private Task currentTask;
    private Label statusLabel;
    private final ObservableList<mediaFile> data = FXCollections.observableArrayList();
    private final ObservableList<metaProp> meta = FXCollections.observableArrayList();
    private final ObservableList<duplicate> duplicates = FXCollections.observableArrayList();
    private final ObservableList<duplicate> modpic = FXCollections.observableArrayList();

    
    /**
     * Creates a List with the predefined standard directories on recognized volumes
     * @return a List of String which are the default on the recognized media
     */
    private List<String> defaultImportDirectories(File nonExt) {
            ArrayList<File> drives = new ArrayList<>();
            ArrayList<String> list = new ArrayList<>();
            File[] paths;
            FileSystemView fsv = FileSystemView.getFileSystemView();
            paths = File.listRoots();
            ArrayList<String> Sony = new ArrayList<>();
            Sony.add("\\DCIM\\");
            Sony.add("\\PRIVATE\\AVCHD\\BDMV\\STREAM\\");
            Sony.add("\\PRIVATE\\M4ROOT\\CLIP\\");
            ArrayList<String> Samsung = new ArrayList<>();
            Samsung.add("\\DCIM\\Camera");
            Samsung.add("\\WhatsApp\\Media\\WhatsApp Images");

            for(File path:paths) {
                String desc = fsv.getSystemTypeDescription(path);
                if (desc.startsWith("USB") || desc.startsWith("SD")) drives.add(path);
            }
            if (nonExt != null && nonExt.exists()) {
                for(File path:nonExt.listFiles()) {
                    if (path.isDirectory()) drives.add(path);
                }
            }
            
            for(File drive:drives) {                    
                boolean valid = true;
                for(String criteria:Sony) {
                    //Todo might not work when drive is just a drive: double backslash
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

    /**
     * Creates a mediaFile object for each media file in the directories non-recursive
     * @param directories list of the directories to process
     * @return the list of the <code> mediaFile </code> objects
     */
    private List<mediaFile> fileRenameList(List<String> directories) {
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
                    List<meta> exifToMeta = exifToMeta(fileList, dir1);
                    Iterator<meta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        meta next = iterator.next();
                        files.add(new mediaFile(next));
                        progressBar.setValue(i + j*chunkSize);
                        progressIndicator.setProgress((i + j*chunkSize)/content.length);
                    }
                }
                progressDialog.dispose();
            }				
        }
        return files;
    }       

    private ArrayList<meta> fileMetaList(ArrayList<String> directories, Path target) {
        Iterator<String> iter = directories.iterator();
        ArrayList<meta> metas = new ArrayList<>();
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
                    metas.addAll(exifToMeta(fileList, dir1));
                    progressBar.setValue(j*chunkSize);
                    progressIndicator.setProgress((j*chunkSize)/content.length);
                }
                progressDialog.dispose();
            }				
        }
        return metas;
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
                                    files.add(new mediaFile(source.toString(), target + "\\"));
                                    System.out.println(source.toString() + " -> " + target);
                                    j = dirs.length;
                                }
                            }
                        } else {
                            files.add(new mediaFile(source.toString(), target + "\\"));
                            System.out.println(source.toString() + " -> " + target);
                        }   
                        oldName = fileName;
                        i++;
                        progressBar.setValue(i);
                        progressIndicator.setProgress((i)/content.length);
                    }
                    progressDialog.dispose();
                    listOnScreen(createMediafileTable(files));
                }
            }				
        }

    }

    private ArrayList<comparableMediaFile> readDirectoryContent(Path path) {
        final ArrayList<comparableMediaFile> files = new ArrayList();
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) {
                        if (!attrs.isDirectory() && attrs.isRegularFile()) {
                            files.add(new comparableMediaFile(file.toFile(), mediaFile.getV(file.toFile().getName())));                               
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
    
    /**
     * Compare Files according to their filenames
     */
    private void compare() {
        ArrayList<comparableMediaFile> fromFiles = readDirectoryContent(getFromDir().toPath());
        ArrayList<comparableMediaFile> toFiles = readDirectoryContent(getToDir());
        ArrayList<comparableMediaFile> singles = new ArrayList<>();
        ObservableList<String> pairs =FXCollections.observableArrayList ();
        StringBuilder pairTo = new StringBuilder();
        StringBuilder pairFrom = new StringBuilder();
        StringBuilder singleStr = new StringBuilder();
        fromFiles.stream().forEach((file) -> singles.add(file));
        toFiles.stream().forEach((file) -> singles.add(file));
        this.duplicates.clear();
        this.modpic.clear();
        toFiles.stream().forEach((file) -> {
            fromFiles.stream().forEach((baseFile) -> {
                if (file.meta != null && baseFile.meta != null && FilenameUtils.getExtension(file.file.getName()).equals(FilenameUtils.getExtension(baseFile.file.getName()))) {
                    if (file.meta.iID != null && baseFile.meta.iID != null && file.meta.iID.equals(baseFile.meta.iID)) {
                        pairs.add(baseFile.file.getName() + " : " + file.file.getName() + "\n");
                        pairTo.append(file.file.getAbsolutePath()).append("\n");
                        pairFrom.append(baseFile.file.getAbsolutePath()).append("\n");
                        singles.remove(baseFile);
                        singles.remove(file);
                    } else if (file.meta.dID != null && baseFile.meta.dID != null && file.meta.dID.equals(baseFile.meta.dID)) {
                        this.duplicates.add(new duplicate(baseFile, file, true));
                        singles.remove(baseFile);
                        singles.remove(file);
                    } else if (file.meta.odID != null && baseFile.meta.odID != null && file.meta.odID.equals(baseFile.meta.odID)) {
                        this.modpic.add(new duplicate(baseFile, file, false));
                        singles.remove(baseFile);
                        singles.remove(file);
                    }
                }
            });
        });
        ArrayList<metaChanges> metaChange = new ArrayList();
        ArrayList<String> metaTags = new ArrayList();
        dupFor:
        for (duplicate dup : duplicates) {
            for (String item : dup.footprint) {
                if (!metaTags.contains(item)) metaTags.add(item);
            }
            for (metaChanges change : metaChange) {
                if (change.compare(dup.footprint, dup.getDir())) {
                    continue dupFor;
                }
            }            
            metaChange.add(new metaChanges(dup.footprint, dup.getDir()));
        }
        for (metaChanges change : metaChange) {
            System.out.println(change.getChanges());
            System.out.println(change.getCount());
            System.out.println(change.getDirs());
            System.out.println();
        }
        metaTags.stream().forEach((tag) -> {System.out.println(tag);});
        tab1.setText("Pairs");
        ListView<String> listPair = new ListView<String>();
        listPair.setItems(pairs);
        tab1.setContent(listPair);
        tab2.setText("Duplicates");
        tab2.setContent(createDuplicateTable(duplicates));
        tab3.setText("Modified pic");
        tab3.setContent(createDuplicateTable(modpic));
        tab4.setText("Singles");
        ListView<String> listSingle = new ListView<String>();
        ObservableList<String> single = FXCollections.observableArrayList ();
        singles.stream().forEach((file) -> single.add(file.file.getName() + "\n"));
        singles.stream().forEach((file) -> singleStr.append(file.file.getAbsolutePath()).append("\n"));
        listSingle.setItems(single);
        tab4.setContent(listSingle);
        try {
            FileUtils.writeStringToFile(new File("e:\\single.txt"), single.toString(), "ISO-8859-1");
            FileUtils.writeStringToFile(new File("e:\\pairTo.txt"), pairTo.toString(), "ISO-8859-1");
            FileUtils.writeStringToFile(new File("e:\\pairFrom.txt"), pairFrom.toString(), "ISO-8859-1");
        } catch (IOException ex) {
            errorOut("Write to file", ex);
        }

        StaticTools.beep();
    }

    private TableView createDuplicateTable(ObservableList<duplicate> input) {
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
        buttonCol.setCellValueFactory(new PropertyValueFactory<duplicate, String>("meta"));
        buttonCol.setCellFactory(new Callback<TableColumn<duplicate, String>, TableCell<duplicate, String>>() {
          @Override 
          public TableCell<duplicate, String> call(TableColumn<duplicate, String> buttonCol) {
            return new TableCell<duplicate, String>() {
              final Button button = new Button(); {
                button.setMinWidth(130);
              }
              @Override
              public void updateItem(final String object, boolean empty) {
                super.updateItem(object, empty);
                int index = this.getIndex();
                setGraphic(button);
                if (object != null) {
                    button.setText(object);
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
                        table.getItems().addAll(input.get(index).getConflicts().toArray());
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
        table.setItems(input);
        table.getColumns().addAll(processingCol, firstNameCol, buttonCol, secondNameCol); 
        return table;
    }
   
    //Sets the center view to table format
    private void listOnScreen(TableView tableView) {
        tab1.setText("Results");
        tab1.setContent(tableView);
        tab2.setText("");
        tab2.setContent(null);
        StaticTools.beep();
    }

    private TableView createMediafileTable(List<mediaFile> newData) {
        data.removeAll(data);
        newData.stream().forEach((obj) -> {data.add(obj);});
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
    
    private TableView createMetaTable(ArrayList<meta> newData) {
        meta.removeAll(meta);
        newData.stream().forEach((obj) -> {meta.add(new metaProp(obj));});
        TableView<metaProp> table = new TableView<>();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn< metaProp, Boolean > dateFormatCol = new TableColumn<>( "dateFormat" );
        dateFormatCol.setCellValueFactory( f -> f.getValue().dateFormatProperty());
        dateFormatCol.setCellFactory(CheckBoxTableCell.forTableColumn(dateFormatCol));
        dateFormatCol.setPrefWidth(50);
        dateFormatCol.setResizable(false);
        TableColumn nameCol = new TableColumn("Filename");
        nameCol.setCellValueFactory(new PropertyValueFactory<metaProp, String>("originalFilename"));
        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<metaProp, String>("date"));
        TableColumn modelCol = new TableColumn("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<metaProp, String>("model"));
        table.setItems(meta);
        table.getColumns().addAll(nameCol, dateCol, dateFormatCol, modelCol); 
        return table;
    }
   
    //Sets the center view to pic stripes
    private void stripesOnScreen(File dir){
        TimeLine timeLine = new TimeLine(dir);
        root.setCenter(timeLine.getStripeBox());
        timeLine.resetView();
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
            File[] content = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return supportedMediaFileType(name);
                }});
            for(int i = 0; i < content.length; i++) {
                String hash = getHash(content[i]);
                str.append(hash + "\t" + content[i] + "\n");
                content[i].renameTo(new File(content[i].getParentFile() + "\\" + hash + content[i].getName()));
            } 
        }       
        return str;
    }

    private void listFiles(Path path, int start) {
        fileSizeCountTotal = 0;
        fileSizeCount = 0;
        fileCountTotal = 0;
        fileCount = 0;
        String filename = "";
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
                            fileSizeCountTotal += attrs.size();
                            fileCountTotal++;
//                            System.out.println(file.getFileName());
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
            JProgressBar progressBar = new JProgressBar(0, (int)(fileSizeCountTotal/1000000));
            JDialog progressDialog = progressDiag(progressBar); 
            PrintWriter pw = new PrintWriter(new File("e:\\test2.csv"));
            long startTime = System.nanoTime();
            String delimiter = "\t";
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
                            StringBuilder sb = new StringBuilder();
                            String hash = getHash(file.toFile());
                            sb.append(start + fileCount).append(delimiter);
                            sb.append(hash).append(delimiter);
                            sb.append(hash).append(delimiter);
                            sb.append(delimiter);
                            sb.append(file.getParent().toString().substring(2)).append(delimiter);
                            sb.append(file.getFileName()).append(delimiter);
                            sb.append(attrs.size());
                            sb.append('\n');
                            pw.write(sb.toString());
                            fileSizeCount += attrs.size();
                            fileCount++;
                            if ((fileCount % 10000) == 0) {
                                long toSeconds = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime);
                                System.out.print("Time elasped: " + toSeconds + "s Data processed: " + fileSizeCount/1048576 + "MB from " + fileCount + " files" );
                                System.out.println(" Avg. speed " + fileSizeCount/1048576/toSeconds + "MB/s ETA: " + toSeconds*(fileSizeCountTotal - fileSizeCount)/fileSizeCount + "s Data left: " + (fileSizeCountTotal-fileSizeCount)/1048576 + "MB from " + (fileCountTotal-fileCount) + " files");
                                pw.flush();
                            }
                            progressBar.setValue((int)(fileSizeCount/1000000));
                            progressIndicator.setProgress(fileSizeCount/fileSizeCountTotal);
                        }
                        return FileVisitResult.CONTINUE;                            
                    }

                  @Override public FileVisitResult 
                visitFileFailed(Path file, IOException exc) {
//                        StaticTools.errorOut(file.toString(), exc);
                        // Skip folders that can't be traversed
                        return FileVisitResult.CONTINUE;
                    }

                  @Override public FileVisitResult
                postVisitDirectory (Path dir, IOException exc) {
//                        if (exc != null)
//                        StaticTools.errorOut(dir.toString(), exc);
                        // Ignore errors traversing a folder
                        return FileVisitResult.CONTINUE;
                    }
            });
            pw.close();
            progressDialog.dispose();
        }
        catch (IOException e)
        {
            throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
        }
    }
    
    private void setGoAction(String action) {
        switch (action) {
            default:
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
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Sets the whole look and function for the GUI">
    private HBox createFunctionButtons() {
        //Definition of the function buttons
        Button btnImport = new Button("Import");
        btnImport.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                List<String> directories = defaultImportDirectories(new File("G:\\Pictures\\Photos\\Új\\Peru\\6500"));
                Path backupdrive = null;
                if ((backupdrive = Services.backupMounted()) == null) {
                    StaticTools.errorOut("No backup Drive", new Exception("Attach a backup drive!"));
                    if ((backupdrive = Services.backupMounted()) == null) {
//                        return;
                    }
                }
                listOnScreen(createMediafileTable(fileRenameList(directories))); 
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

        Button btnMeta = new Button("Meta");
        btnMeta.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                File file = StaticTools.getDir(getFromDir());
                if(file != null) {
                    ArrayList<String> directories = new ArrayList<String>();
                    directories.add(file.toString());
                    Path tempDir = Paths.get(getToDir().toString() + "\\" + file.getName());
                    listOnScreen(createMetaTable(fileMetaList(directories, tempDir))); 
                }                    
            }
        });

        Button btnRename = new Button("Rename");
        btnRename.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                File file = StaticTools.getDir(getFromDir());
                if(file != null) {
                    List<String> directories = new ArrayList<>();
                    directories.add(file.toString());
                    listOnScreen(createMediafileTable(fileRenameList(directories))); 
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

        Button btnList = new Button("List");
        btnList.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                File file = StaticTools.getDir(getFromDir());
                File output = StaticTools.getFile(getFromDir());
                int start = -1;
                do {
                    String result= JOptionPane.showInputDialog("Last record value: ");
                    try {start = Integer.parseInt(result);} catch (NumberFormatException e) {start = -1;}
                } while (start == -1);
                if(file != null && output != null) {
//                    listFiles(file.toPath(), start + 1);
                    currentTask = new Listing(file.toPath(), start + 1, output);
                    Thread th = new Thread(currentTask);
                    progressIndicator.progressProperty().unbind();
                    progressIndicator.progressProperty().bind(currentTask.progressProperty());
                    statusLabel.textProperty().bind(currentTask.messageProperty());
                    th.setDaemon(true);
                    th.start();
                }
                
//                Listing p = new Listing();
//                new Thread(p).start();

/*                File file = StaticTools.getDir(getFromDir());
                int start = -1;
                do {
                    String result= JOptionPane.showInputDialog("Last record value: ");
                    try {start = Integer.parseInt(result);} catch (NumberFormatException e) {start = -1;}
                } while (start == -1);
                if(file != null) {
                    listFiles(file.toPath(), start + 1);
                }
                */
            }
        });

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(btnImport, btnShift, btnRename, btnMeta, btnMove, btnShow, btnComp, btnList);
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
        ObservableList<String> timeZones = FXCollections.observableArrayList(ZoneId.getAvailableZoneIds()).sorted(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String o1trim = o1.toString().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
                String o2trim = o2.toString().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
                return o1trim.compareTo(o2trim);
            }
            
        });
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
        setGoAction("");

        Button btnClr = new Button("Abort");
        btnClr.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                data.removeAll(data);
                currentTask.cancel();
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

        final NumberAxis xAxis = new NumberAxis(0, 100, 10);
        xAxis.setMinorTickVisible(false);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setMinorTickVisible(false);
        final AreaChart<Number,Number> ac =  new AreaChart<>(xAxis,yAxis);
//        ac.setTitle("Speed");
        ac.getData().addAll(speeds);
        ac.setLegendVisible(false);
        ac.getXAxis().setTickLabelsVisible(false);
        ac.getXAxis().setOpacity(0);

        ac.setPrefSize(300, 100);
        ac.setMinSize(300, 100);
        ac.setMaxSize(300, 100);
        
        statusLabel = new Label();

        HBox footer = new HBox();
        footer.setSpacing(10);
        footer.setAlignment(Pos.CENTER);
        footer.getChildren().addAll(btnGo, btnClr, btnRefresh, progressIndicator, statusLabel, ac);
        return footer;
    }
    
    private TabPane createCenter() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
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

    private void osNev() {
        zone = ZoneId.systemDefault();
        copyOrMove = MOVE;
        File dirRoot = new File("G:\\Pictures\\Photos\\Régi képek\\Dupla\\Képek");
        toDir = Paths.get("G:\\Pictures\\Photos\\V5");
//        File dirRoot = new File("G:\\Pictures\\Photos\\Új");
        System.out.println("---- " + dirRoot.getName() + " ----\n\n");
        File[] dirs = dirRoot.listFiles(new FilenameFilter() {
             public boolean accept(File dir, String name) {
                 return (new File(dir + "\\" + name).isDirectory());
             }});
        JProgressBar progressBar;
        JDialog progressDialog;
        for(int k = 0; k < dirs.length; k++) {
            File dir1 = dirs[k];
            if(dir1.isDirectory()) {
                System.out.println(dir1.getName());
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                progressBar = new JProgressBar(0, content.length);
                progressDialog = progressDiag(progressBar); 
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<String> files = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        files.add(content[j*chunkSize + f].getName());
                    }
                    List<meta> exifToMeta = exifToMeta(files, dir1);
                    Iterator<meta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        meta next = iterator.next();
                        mediaFile media = new mediaFile(next);
                        if (media.getProcessing()) {
                            media.write();
                        } else {
                            System.out.println(media.getNewName() + ": " + media.getNote());
                        }
                        i++;
                        progressBar.setValue(i + j*chunkSize);
                        progressIndicator.setProgress((i + j*chunkSize)/content.length);
                    }
                }
                progressDialog.dispose();
            }
        }
    }
    
    public void test() {
        List<String> directories = defaultImportDirectories(new File("G:\\Pictures\\Photos\\Új\\Umag"));
        listOnScreen(createMediafileTable(fileRenameList(directories))); 
/*        zone = ZoneId.of("America/Lima");
        /*
        try {
            ArrayList<String[]> readMeta = ExifReadWrite.readMeta(new File("e:\\Képek\\Dev\\ExifDamage\\orig\\DSC07620.JPG"));
            ArrayList<String[]> readMeta1 = ExifReadWrite.readMeta(new File("e:\\Képek\\Dev\\ExifDamage\\digi\\DSC07620.JPG"));
            readMeta.stream().forEach((meta) -> System.out.println(meta[0] + " : " + meta[1]));
            System.out.println("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
            readMeta1.stream().forEach((meta) -> System.out.println(meta[0] + " : " + meta[1]));
        } catch (ImageProcessingException | IOException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*        ArrayList<comparableMediaFile> readDirectoryContent = readDirectoryContent(Paths.get("E:\\temp\\compare\\"));
        for (comparableMediaFile mFile : readDirectoryContent) {
        System.out.println(mFile.file.getName());
        System.out.println("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
        ArrayList<String[]> readMeta = StaticTools.readMeta(mFile.file);
        for (String[] tagValue : readMeta) {
        System.out.println(tagValue[0] + " : " + tagValue[1]);
        }
        }
        /*        String fullHash = "";
        StringBuilder builder = new StringBuilder();
        File input = new File("K:\\Képek\\Photos\\Nagyok\\Japán\\Nyers");
        long startTime = System.nanoTime();
        try {
        Files.walkFileTree (input.toPath(), new SimpleFileVisitor<Path>() {
        @Override public FileVisitResult
        visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
        builder.append(StaticTools.getFullHash(file.toFile())).append(" ").append(file.getFileName()).append("\n");
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
        FileUtils.writeStringToFile(new File("e:\\java.md5"), builder.toString(), "ISO-8859-1");
        } catch (Exception ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
        startTime = System.nanoTime();
        try {
        fullHash = StaticTools.getFullHashPS(input);
        } catch (IOException ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
        /*
        osNev();
        try {
        Path move = Files.move(Paths.get("E:\\temp\\V5\\K2016-05-2_6@19-3_4-36(+0200)(Thu)-9e56374d932984f18cdf67adfdd5789d-9e56374d932984f18cdf67adfdd5789d-_DSC1920.ARW"), Paths.get("G:\\Pictures\\Photos\\V5\\Közös\\V5\\V5_K2016-05-2_6@19-3_4-36(+0200)(Thu)-null-4078f129a436f88812c97b9ae7500199-_DSC1920.ARW"));
        } catch (IOException e) {
        StaticTools.errorOut("Test", e);
        }
        /*        try {
        ArrayList<String> hash = StaticTools.getHash(Paths.get("E:\\rosszJPG"));
        System.out.println("h");
        } catch (IOException ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
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
//            readMetaDataTest(new File("e:\\DSC07914.ARW"));
//            removeFiles();
//            compare();
        
            
            /*        ArrayList<comparableMediaFile> readDirectoryContent = readDirectoryContent(Paths.get("E:\\temp\\compare\\"));
            for (comparableMediaFile mFile : readDirectoryContent) {
            System.out.println(mFile.file.getName());
            System.out.println("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
            ArrayList<String[]> readMeta = StaticTools.readMeta(mFile.file);
            for (String[] tagValue : readMeta) {
            System.out.println(tagValue[0] + " : " + tagValue[1]);
            }
            }
            
            /*        String fullHash = "";
            StringBuilder builder = new StringBuilder();
            File input = new File("K:\\Képek\\Photos\\Nagyok\\Japán\\Nyers");
            long startTime = System.nanoTime();
            try {
            Files.walkFileTree (input.toPath(), new SimpleFileVisitor<Path>() {
            @Override public FileVisitResult
            visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
            builder.append(StaticTools.getFullHash(file.toFile())).append(" ").append(file.getFileName()).append("\n");
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
            FileUtils.writeStringToFile(new File("e:\\java.md5"), builder.toString(), "ISO-8859-1");
            } catch (Exception ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
            startTime = System.nanoTime();
            try {
            fullHash = StaticTools.getFullHashPS(input);
            } catch (IOException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
            /*
            osNev();
            try {
            Path move = Files.move(Paths.get("E:\\temp\\V5\\K2016-05-2_6@19-3_4-36(+0200)(Thu)-9e56374d932984f18cdf67adfdd5789d-9e56374d932984f18cdf67adfdd5789d-_DSC1920.ARW"), Paths.get("G:\\Pictures\\Photos\\V5\\Közös\\V5\\V5_K2016-05-2_6@19-3_4-36(+0200)(Thu)-null-4078f129a436f88812c97b9ae7500199-_DSC1920.ARW"));
            } catch (IOException e) {
            StaticTools.errorOut("Test", e);
            }
            /*        try {
            ArrayList<String> hash = StaticTools.getHash(Paths.get("E:\\rosszJPG"));
            System.out.println("h");
            } catch (IOException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
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
//            readMetaDataTest(new File("e:\\DSC07914.ARW"));
//            removeFiles();
//            compare();

    }
    
    @Override
    public void start(Stage pStage) {
        view = this;
        this.primaryStage = pStage;

        Parent mainLook = createMainLook();
        Scene mainScene = new Scene(mainLook);
        mainScene.getStylesheets().add("Chart.css");
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
        test();
    }       

    public static void main(String[] args) {
        launch(args);
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
