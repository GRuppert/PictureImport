package org.nyusziful.Main;

import org.nyusziful.Comparison.Listing;
import org.nyusziful.Comparison.comparableMediaFile;
import org.nyusziful.Rename.WritableMediaFile;
import org.nyusziful.Rename.metaProp;
import org.nyusziful.Comparison.duplicate;
import org.nyusziful.Comparison.metaChanges;
import static org.nyusziful.Rename.fileRenamer.getV;
import static org.nyusziful.Hash.MediaFileHash.getHash;
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
import java.net.URL;
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
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import static org.nyusziful.ExifUtils.ExifReadWrite.exifToMeta;

//Exiftool must be in PATH
// <2GB file support
public class MainController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private ToggleGroup group;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ComboBox TimeZone;
    @FXML
    private Label statusLabel;
    @FXML
    private AreaChart<Number,Number> ac;
    @FXML
    private Label from;
    @FXML
    private Label to;
    @FXML
    private BorderPane mainPane;
    // </editor-fold>

    
    /**
    * Variables
    */

    private long fileSizeCountTotal = 0;
    private long fileSizeCount = 0;
    private long fileCountTotal = 0;
    private long fileCount = 0;
    private BorderPane root;

    private Task currentTask;
    private final ObservableList<metaProp> meta = FXCollections.observableArrayList();
    private final ObservableList<duplicate> duplicates = FXCollections.observableArrayList();
    private final ObservableList<duplicate> modpic = FXCollections.observableArrayList();

    private CommonProperties commonProperties;

    public MainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CommonProperties.getInstance();
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (group.getSelectedToggle() != null) {
                commonProperties.setCopyOrMove((int) group.getSelectedToggle().getUserData());
            }
        });

        ac.getData().addAll(Progress.getInstance().getSpeeds());
        progressIndicator.progressProperty().bindBidirectional(Progress.getInstance().getProgressProperty());
        
        ObservableList<String> timeZones = FXCollections.observableArrayList(ZoneId.getAvailableZoneIds()).sorted(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String o1trim = o1.toString().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
                String o2trim = o2.toString().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
                return o1trim.compareTo(o2trim);
            }
            
        });
        TimeZone.getItems().addAll(timeZones);
        commonProperties.setZone(ZoneId.systemDefault());
        TimeZone.getSelectionModel().select(ZoneId.systemDefault().getId());
    }    




    /**
     * Creates a WritableMediaFile object for each media file in the directories non-recursive
     * @param directories list of the directories to process
     * @return the list of the <code> WritableMediaFile </code> objects
     */
    private List<WritableMediaFile> fileRenameList(List<String> directories) {
        Iterator<String> iter = directories.iterator();
        ArrayList<WritableMediaFile> files = new ArrayList<>();
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
                    List<meta> exifToMeta = exifToMeta(fileList, dir1, this.getZone());
                    Iterator<meta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        meta next = iterator.next();
                        files.add(new WritableMediaFile(next));
                        progressBar.setValue(i + j*chunkSize);
                        this.setProgress((i + j*chunkSize)/content.length);
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
                    metas.addAll(exifToMeta(fileList, dir1, this.getZone()));
                    progressBar.setValue(j*chunkSize);
                    this.setProgress((j*chunkSize)/content.length);
                }
                progressDialog.dispose();
            }				
        }
        return metas;
    }       

    private ArrayList<comparableMediaFile> readDirectoryContent(Path path) {
        final ArrayList<comparableMediaFile> files = new ArrayList();
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                  @Override public FileVisitResult 
                visitFile(Path file, BasicFileAttributes attrs) {
                        if (!attrs.isDirectory() && attrs.isRegularFile()) {
                            files.add(new comparableMediaFile(file.toFile(), getV(file.toFile().getName())));                               
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


    // <editor-fold defaultstate="collapsed" desc="View Action">
    @FXML
    private void handleImportButtonAction() {
        List<String> directories = StaticTools.defaultImportDirectories(new File("G:\\Pictures\\Photos\\Új\\Peru\\6500"));
        Path backupdrive = null;
        if ((backupdrive = Services.backupMounted()) == null) {
            StaticTools.errorOut("No backup Drive", new Exception("Attach a backup drive!"));
            if ((backupdrive = Services.backupMounted()) == null) {
//                        return;
            }
        }
        listOnScreen(createMediafileTable(fileRenameList(directories)));
    }

    @FXML
    private void handleShiftButtonAction() {
        File file = StaticTools.getDir(getFromDir());
        if(file != null) {
            stripesOnScreen(file);
        }
    }

    @FXML
    private void handleMetaButtonAction() {
        File file = StaticTools.getDir(getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            Path tempDir = Paths.get(getToDir().toString() + "\\" + file.getName());
            listOnScreen(createMetaTable(fileMetaList(directories, tempDir)));
        }
    }

    @FXML
    private void handleRenameButtonAction() {
        File file = StaticTools.getDir(getFromDir());
        if(file != null) {
            List<String> directories = new ArrayList<>();
            directories.add(file.toString());
            listOnScreen(createMediafileTable(fileRenameList(directories)));
        }
    }

    @FXML
    private void handleMoveButtonAction() {
        File file = StaticTools.getDir(getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            sortToDateDirectories(directories);
        }
    }

    @FXML
    private void handleShowButtonAction() {
        show();
    }

    /**
     * Compare Files according to their filenames
     */
    @FXML
    private void handleCompareButtonAction() {
        compare();
    }

    @FXML
    private void handleListButtonAction() {
        createList();
    }

    @FXML
    private void handleToButtonAction() {
        File file = StaticTools.getDir(getToDir().toFile());
        if (file != null) {
            toDir = file.toPath();
            setToText(getToDir().toString());
        }
    }

    @FXML
    private void handleFromButtonAction() {
        File file = StaticTools.getDir(getFromDir());
        if (file != null) {
            fromDir = file;
            setFromText(getFromDir().toString());
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="View Update">
    @FXML
    private void handleTimeZoneComboBoxAction() {
        zone = ZoneId.of(TimeZone.getSelectionModel().getSelectedItem().toString());
    }

    private void setFromText(String text) {
        from.setText(text);
    }

    private void setToText(String text) {
        to.setText(text);
    }

    //Sets the center view to table format
    private void listOnScreen(TableView tableView) {
        mainPane.setCenter(tableView);
        StaticTools.beep();
    }

    //Sets the center view to pic stripes
    private void stripesOnScreen(File dir){
        TimeLine timeLine = new TimeLine(dir, this.getZone());
        root.setCenter(timeLine.getStripeBox());
        timeLine.resetView();
    }



    public void setProgress(double percent) {
        progressIndicator.setProgress(percent < 1 ? percent : 1);
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Model Notify">

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Model Update">
    private void createList() {
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
                    ArrayList<WritableMediaFile> files = new ArrayList<>();
                    JProgressBar progressBar = new JProgressBar(0, content.length);
                    JDialog progressDialog = progressDiag(progressBar);
                    int i = 0;
                    for (File content1 : content) {
                        Path source = content1.toPath();
                        String fileName = content1.getName().substring(1, 12);
                        fileName = fileName.substring(0, 4) + fileName.substring(5, 7) + fileName.substring(8, 9) + fileName.substring(10, 11);
                        if (!fileName.equals(oldName)) {
                            int fY = Integer.parseInt(fileName.substring(0, 4)), fM = Integer.parseInt(fileName.substring(4, 6))-1, fD = Integer.parseInt(fileName.substring(6, 8));
                            File[] dirs = commonProperties.getToDir().toFile().listFiles((File dir, String name) -> dir.isDirectory());
                            for (int j = 0; j < dirs.length; j++) {
                                String actDir = dirs[j].getName();
                                int sY = Integer.parseInt(actDir.substring(0, 4)), sM = Integer.parseInt(actDir.substring(5, 7))-1, sD = Integer.parseInt(actDir.substring(8, 10));
                                int eY = Integer.parseInt(actDir.substring(13, 17)), eM = Integer.parseInt(actDir.substring(18, 20))-1, eD = Integer.parseInt(actDir.substring(21, 23));
                                if ((fY*10000 + fM*100 + fD >= sY*10000 + sM*100 + sD) && (fY*10000 + fM*100 + fD <= eY*10000 + eM*100 + eD)) {
                                    target = dirs[j].toPath();
                                    files.add(new WritableMediaFile(source.toString(), target + "\\"));
                                    System.out.println(source.toString() + " -> " + target);
                                    j = dirs.length;
                                }
                            }
                        } else {
                            files.add(new WritableMediaFile(source.toString(), target + "\\"));
                            System.out.println(source.toString() + " -> " + target);
                        }
                        oldName = fileName;
                        i++;
                        progressBar.setValue(i);
                        this.setProgress((i)/content.length);
                    }
                    progressDialog.dispose();
                    listOnScreen(createMediafileTable(files));
                }
            }
        }

    }

    private void show() {
        File[] dirs = commonProperties.getToDir().toFile().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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
        File[] dirc = commonProperties.getFromDir().listFiles(new FilenameFilter() {public boolean accept(File dir, String name) {return dir.isDirectory();}});
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

    private void compare() {
        ArrayList<comparableMediaFile> fromFiles = readDirectoryContent(commonProperties.getFromDir().toPath());
        ArrayList<comparableMediaFile> toFiles = readDirectoryContent(commonProperties.getToDir());
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
                        this.setProgress(fileSizeCount/fileSizeCountTotal);
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

    // </editor-fold>


    public static JDialog progressDiag(JProgressBar bar) {
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
}
