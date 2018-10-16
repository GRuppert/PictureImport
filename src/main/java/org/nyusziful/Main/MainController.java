package org.nyusziful.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.nyusziful.Comparison.Listing;
import org.nyusziful.Comparison.comparableMediaFile;
import org.nyusziful.Rename.*;
import org.nyusziful.Comparison.duplicate;
import org.nyusziful.Comparison.metaChanges;

import static java.lang.Integer.*;
import static org.nyusziful.Main.StaticTools.*;
import static org.nyusziful.Rename.fileRenamer.getV;
import static org.nyusziful.Hash.MediaFileHash.getHash;

import org.nyusziful.TimeShift.TimeLine;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import static org.nyusziful.ExifUtils.ExifReadWrite.readFileMeta;

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

    private Task currentTask;

    private CommonProperties commonProperties;

    public MainController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    // <editor-fold defaultstate="collapsed" desc="View Action">
    @FXML
    private void handleImportButtonAction() {
        listOnScreen(importFiles());
    }

    @FXML
    private void handleShiftButtonAction() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        if(file != null) {
            stripesOnScreen(file);
        }
    }

    @FXML
    private void handleMetaButtonAction() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            Path tempDir = Paths.get(commonProperties.getToDir().toString() + "\\" + file.getName());
            createMetaTable(fileMetaList(directories, tempDir));
        }
    }

    @FXML
    private void handleRenameButtonAction() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        if(file != null) {
            List<String> directories = new ArrayList<>();
            directories.add(file.toString());
            listOnScreen(itWasImport(directories));
        }
    }

    @FXML
    private void handleMoveButtonAction() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            sortToDateDirectories(directories);
        }
    }

    @FXML
    private void handleShowButtonAction() {
        StringBuilder sb = new StringBuilder();
        sb.append(listDirectorySize(commonProperties.getToDir().toFile()));
        sb.append(checkDirectoryDates(commonProperties.getFromDir()));
        mainPane.setCenter(new Label(sb.toString()));

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
        File file = StaticTools.getDir(commonProperties.getToDir().toFile());
        if (file != null) {
            commonProperties.setToDir(file.toPath());
            setToText(commonProperties.getToDir().toString());
        }
    }

    @FXML
    private void handleFromButtonAction() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        if (file != null) {
            commonProperties.setFromDir(file);
            setFromText(commonProperties.getFromDir().toString());
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="View Update">
    @FXML
    private void handleTimeZoneComboBoxAction() {
        commonProperties.setZone(ZoneId.of(TimeZone.getSelectionModel().getSelectedItem().toString()));
    }

    private void setFromText(String text) {
        from.setText(text);
    }

    private void setToText(String text) {
        to.setText(text);
    }

    //Sets the center view to comparison format
    private void compare() {
        TabPane comparePane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/comparePanel.fxml"));
            comparePane = (TabPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(comparePane);
        StaticTools.beep();

    }

    private void createMetaTable(ArrayList<meta> newData) {
        TableView tableView = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/meta2TableView.fxml"));
            tableView = (TableView) loader.load();
            Meta2TableViewController ctrl = loader.getController();
            ObservableList<metaProp> meta = FXCollections.observableArrayList();
            meta.removeAll(meta);
            newData.stream().forEach((obj) -> {meta.add(new metaProp(obj));});
            ctrl.setMeta(meta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(tableView);
        StaticTools.beep();
    }


    //Sets the center view to table format
    private void listOnScreen(List<WritableMediaFile> writableMediaFiles) {
        BorderPane tablePane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/mediaFileTableView.fxml"));
            tablePane = (BorderPane) loader.load();
            MediaFileTableViewController ctrl = loader.getController();
            ctrl.setMediaFileSet(new MediaFileSet(writableMediaFiles));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(tablePane);
        StaticTools.beep();
    }

    //Sets the center view to pic stripes
    private void stripesOnScreen(File dir){
        TimeLine timeLine = new TimeLine(dir, commonProperties.getZone());
        //TODO replace with load FXML
        mainPane.setCenter(timeLine.getStripeBox());
        timeLine.resetView();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Model Notify">

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Model Update">

    // </editor-fold>




    //TODO reads dir:
    //Input processed mediafiles Output dated directory+old filename
    private void sortToDateDirectories(ArrayList<String> directories) {
        List<DirectoryElement> directoryElements = getDirectoryElementsNonRecursive(directories, new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        name = name.toLowerCase();
                        return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".dng");
                    }}
        );

        for (String directory : directories) {
            File dir1 = new File(directory);
            if (dir1.isDirectory()) {
                File[] content = dir1.listFiles(
                        new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        name = name.toLowerCase();
                        return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".dng");
                    }
                });
                if (content.length > 0) {
                    Path target = null;
                    String oldName = "";
                    ArrayList<WritableMediaFile> files = new ArrayList<>();
                    JProgressBar progressBar = new JProgressBar(0, content.length);
//                    JDialog progressDialog = progressDiag(progressBar);
                    int i = 0;
                    for (File content1 : content) {
                        Path source = content1.toPath();
                        String fileName = content1.getName().substring(1, 12);
                        fileName = fileName.substring(0, 4) + fileName.substring(5, 7) + fileName.substring(8, 9) + fileName.substring(10, 11);
                        if (!fileName.equals(oldName)) {
                            int fY = parseInt(fileName.substring(0, 4)), fM = parseInt(fileName.substring(4, 6)) - 1, fD = parseInt(fileName.substring(6, 8));
                            File[] dirs = commonProperties.getToDir().toFile().listFiles((File dir, String name) -> dir.isDirectory());
                            for (int j = 0; j < dirs.length; j++) {
                                String actDir = dirs[j].getName();
                                int sY = parseInt(actDir.substring(0, 4)), sM = parseInt(actDir.substring(5, 7)) - 1, sD = parseInt(actDir.substring(8, 10));
                                int eY = parseInt(actDir.substring(13, 17)), eM = parseInt(actDir.substring(18, 20)) - 1, eD = parseInt(actDir.substring(21, 23));
                                if ((fY * 10000 + fM * 100 + fD >= sY * 10000 + sM * 100 + sD) && (fY * 10000 + fM * 100 + fD <= eY * 10000 + eM * 100 + eD)) {
                                    target = dirs[j].toPath();
//                                    files.add(new WritableMediaFile(source.toString(), target + "\\"));
                                    System.out.println(source.toString() + " -> " + target);
                                    j = dirs.length;
                                }
                            }
                        } else {
//                            files.add(new WritableMediaFile(source.toString(), target + "\\"));
                            System.out.println(source.toString() + " -> " + target);
                        }
                        oldName = fileName;
                        i++;
                        progressBar.setValue(i);
//                        this.setProgress((i) / content.length);
                    }
//                    progressDialog.dispose();
//                    listOnScreen(createMediafileTable(files));
                }
            }
        }

    }

    //Input mediafiles Output standard
    public List<WritableMediaFile> itWasImport(List<String> directories) {
        Iterator<String> iter = directories.iterator();
        ArrayList<WritableMediaFile> files = new ArrayList<>();
        while(iter.hasNext()) {
            File dir1 = new File(iter.next());
            if(dir1.isDirectory()) {
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                JProgressBar progressBar = new JProgressBar(0, content.length);
//                JDialog progressDialog = progressDiag(progressBar);
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<String> fileList = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        fileList.add(content[j*chunkSize + f].getName());
                    }
                    List<meta> exifToMeta = readFileMeta(fileList, dir1, commonProperties.getZone());
                    Iterator<meta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        meta next = iterator.next();
//                        files.add(new WritableMediaFile(next));
                        progressBar.setValue(i + j*chunkSize);
//                        this.setProgress((i + j*chunkSize)/content.length);
                    }
                }
//                progressDialog.dispose();
            }
        }
        return files;
    }

    //import and rename are basically the same
    private List<WritableMediaFile> importFiles() {
        List<String> directories = StaticTools.defaultImportDirectories(new File("G:\\Pictures\\Photos\\Új\\Peru\\6500"));
        Path backupdrive = null;
            if ((backupdrive = Services.backupMounted()) == null) {
            StaticTools.errorOut("No backup Drive", new Exception("Attach a backup drive!"));
            if ((backupdrive = Services.backupMounted()) == null) {
    //                        return;
            }
        }
        return itWasImport(directories);
    }

    //no mediafile, creates a list of meta
    private ArrayList<meta> fileMetaList(ArrayList<String> directories, Path target) {
        Iterator<String> iter = directories.iterator();
        ArrayList<meta> metas = new ArrayList<>();
        while(iter.hasNext()) {
            File dir1 = new File(iter.next());
            if(dir1.isDirectory()) {
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                JProgressBar progressBar = new JProgressBar(0, content.length);
//                JDialog progressDialog = progressDiag(progressBar);
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<String> fileList = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        fileList.add(content[j*chunkSize + f].getName());
                    }
                    metas.addAll(readFileMeta(fileList, dir1, commonProperties.getZone()));
                    progressBar.setValue(j*chunkSize);
//                    this.setProgress((j*chunkSize)/content.length);
                }
//                progressDialog.dispose();
            }
        }
        return metas;
    }





    /**
     * Creates a new background task which reads the From directory files' hashes into a list file
     */
    private void createList() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        File output = StaticTools.getFile(commonProperties.getFromDir());
        int start;
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

    private String listDirectorySize(File directory) {
        File[] dirs = directory.listFiles((dir, name) -> dir.isDirectory());
        StringBuilder sb = new StringBuilder();
        if (dirs != null) {
            for (File dir : dirs) {
                File[] content = dir.listFiles();
        /*                              File[] content = dirs[j].listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            name = name.toLowerCase();
                        return name.endsWith(".mts") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4");
                    }});
        */
                if (content != null) {
                    sb.append(dir.getName()).append(" : ").append(content.length).append("\n");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Prints out a list of folders which are overlapping
     * Folder name format:"YYYY-MM-DD - YYYY-MM-DD Description"
     */
    private String checkDirectoryDates(File directory) {
        File[] dirc = directory.listFiles((dir, name) -> dir.isDirectory());
        StringBuilder sb = new StringBuilder();
        String oldDir = "";
        if (dirc != null) {
            for(int j = 0; j < dirc.length - 1; j++) {
                String actDir = dirc[j].getName();
                int eY = parseInt(actDir.substring(13, 17)), eM = parseInt(actDir.substring(18, 20))-1, eD = parseInt(actDir.substring(21, 23));
                String nextDir = dirc[j].getName();
                int sY = parseInt(nextDir.substring(0, 4)), sM = parseInt(nextDir.substring(5, 7))-1, sD = parseInt(nextDir.substring(8, 10));
                if (sY*10000 + sM*100 + sD <= eY*10000 + eM*100 +  eD) {
                    sb.append(oldDir).append(" -><- ").append(actDir).append("\n");
                }
            }
        }
        return sb.toString();
    }
}
