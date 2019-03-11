package org.nyusziful.pictureorganizer.GUI;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.nyusziful.pictureorganizer.Comparison.Listing;
import org.nyusziful.pictureorganizer.Rename.*;

import static java.lang.Integer.*;

import org.nyusziful.pictureorganizer.TimeShift.TimeLine;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.layout.BorderPane;

import javax.swing.JOptionPane;

import org.nyusziful.pictureorganizer.ExifUtils.ExifReadWrite;
import org.nyusziful.pictureorganizer.Rename.*;

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
    private HBox buttonHBox;
    @FXML
    private BorderPane mainPane;
    // </editor-fold>

    private Task currentTask;

    private CommonProperties commonProperties;

    public MainController() {
        commonProperties = CommonProperties.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group.getToggles().stream().forEach(toggle -> toggle.setSelected(TableViewMediaFile.WriteMethod.valueOf((String) toggle.getUserData()).equals(commonProperties.getCopyOrMove())));
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (group.getSelectedToggle() != null) {
                commonProperties.setCopyOrMove(TableViewMediaFile.WriteMethod.valueOf((String) group.getSelectedToggle().getUserData()));
            }
        });

        ac.getData().addAll(Progress.getInstance().getSpeeds());
        progressIndicator.progressProperty().bindBidirectional(Progress.getInstance().getProgressProperty());
        
        SortedList timeZones = FXCollections.observableArrayList(ZoneId.getAvailableZoneIds()).sorted((Comparator) (o1, o2) -> {
            String o1trim = o1.toString().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            String o2trim = o2.toString().replaceAll("[^A-Za-z0-9]", "").toLowerCase();
            return o1trim.compareTo(o2trim);
        });
        TimeZone.getItems().addAll(timeZones);
        commonProperties.setZone(ZoneId.systemDefault());
        TimeZone.getSelectionModel().select(ZoneId.systemDefault().getId());
        from.setText(commonProperties.getFromDir().toString());
        to.setText(commonProperties.getToDir().toString());
    }

    // <editor-fold defaultstate="collapsed" desc="View Action">
    @FXML
    private void handleImportButtonAction() {
        disableButtons(true);
        showTablePane(importFiles(), "/fxml/mediaFileTableView.fxml");
    }

    @FXML
    private void handleShiftButtonAction() {
        disableButtons(true);
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
            ArrayList<String> directories = new ArrayList<>();
            directories.add(file.toString());
            disableButtons(true);
            showTablePane(itWasImport(directories), "/fxml/mediaFileTableView.fxml");
        }
    }

    @FXML
    private void handleMoveButtonAction() {
        File file = StaticTools.getDir(commonProperties.getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            showTablePane(sortToDateDirectories(directories), "/fxml/mediaFileTableView.fxml");
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

    private void createMetaTable(Collection<Meta> newData) {
        TableView tableView = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/meta2TableView.fxml"));
            tableView = (TableView) loader.load();
            Meta2TableViewController ctrl = loader.getController();
            ObservableList<MetaProp> meta = FXCollections.observableArrayList();
            meta.removeAll(meta);
            newData.stream().forEach((obj) -> {meta.add(new MetaProp(obj));});
            ctrl.setMeta(meta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPane.setCenter(tableView);
        StaticTools.beep();
    }


    //Sets the center view to table format
    private void showTablePane(Collection<? extends TableViewMediaFile> mediaFiles, String tableViewFXML) {
        MediaFileSet mediaFileSet = new MediaFileSet(mediaFiles);
        BorderPane tablePane = null;
        TableView table = null;
        try {
            FXMLLoader loaderTable = new FXMLLoader(getClass().getResource(tableViewFXML));
            table = (TableView) loaderTable.load();
            MediaFileSetTableViewController ctrlTable = loaderTable.getController();
            ctrlTable.setMediaFileSet(mediaFileSet);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tablePanel.fxml"));
            tablePane = (BorderPane) loader.load();
            tablePane.setCenter(table);
            TablePanelController ctrl = loader.getController();
            ctrl.setMediaFileSet(mediaFileSet);
            mediaFileSet.getDataModel().addListener(new ListChangeListener<TableViewMediaFile>() {
                @Override
                public void onChanged(Change<? extends TableViewMediaFile> c) {
                    if (mediaFileSet.getDataModel().size() == 0) {
                        resetAction();
                    }
                }
            });
            mainPane.setCenter(tablePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StaticTools.beep();
    }

    //Sets the center view to pic stripes
    private void stripesOnScreen(File dir){
        TimeLine timeLine = new TimeLine(dir, commonProperties.getZone());
        //TODO replace with load FXML
        mainPane.setCenter(timeLine.getStripeBox());
        timeLine.resetView();
    }

    private void resetAction() {
        disableButtons(false);
        mainPane.setCenter(null);
    }

    private void disableButtons(boolean disabled) {
        ObservableList<Node> children = buttonHBox.getChildren();
        for (Node buttonNode : children) {
            buttonNode.setDisable(disabled);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Model Notify">

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Model Update">

    // </editor-fold>



    //TODO reads dir:
    //Input processed mediafiles Output dated directory+old filename
    private Collection<SimpleMediaFile> sortToDateDirectories(Collection<String> directories) {
        Collection<DirectoryElement> directoryElements = StaticTools.getDirectoryElementsNonRecursive(directories, new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        name = name.toLowerCase();
                        return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".dng");
                    }});
        ArrayList<File> fileList = new ArrayList<>();
        directoryElements.stream().forEach((directoryElement) -> {fileList.add(directoryElement.file);});
        ArrayList<SimpleMediaFile> files = new ArrayList<>();
        Path target = null;
        ZonedDateTime previousFileDate = ZonedDateTime.now();

        for (File file : fileList) {
            ZonedDateTime fileDate = FileRenamer.getV(file.getName()).date;
            if (!fileDate.truncatedTo(ChronoUnit.DAYS).equals(previousFileDate.truncatedTo(ChronoUnit.DAYS))) {
                previousFileDate = fileDate;
                File[] dirs = commonProperties.getToDir().toFile().listFiles((File dir, String name) -> dir.isDirectory());
                for (int j = 0; j < dirs.length; j++) {
                    MediaDirectory mediaDirectory = new MediaDirectory(dirs[j].getName());
                    if (mediaDirectory.from == null) continue;
                    if ((fileDate.isAfter(mediaDirectory.from)) && (fileDate.isAfter(mediaDirectory.to))) {
                        target = dirs[j].toPath();
                        j = dirs.length;
                    }
                }
            }
            files.add(new SimpleMediaFile(file.toPath(), Paths.get(target + "\\" + file.getName())));
//            System.out.println(file.toString() + " -> " + target);
        }
        return files;
    }

    //Input mediafiles Output standard
    public Collection<AnalyzingMediaFile> itWasImport(Collection<String> directories) {
        Collection<DirectoryElement> directoryElements = StaticTools.getDirectoryElementsNonRecursive(directories, (File dir, String name) -> StaticTools.supportedFileType(name));
        ArrayList<AnalyzingMediaFile> files = new ArrayList<>();
        Progress instance = Progress.getInstance();
        instance.reset();
        instance.setGoal(directoryElements.size());
        directoryElements.stream().forEach((directoryElement) -> {files.add(new AnalyzingMediaFile(directoryElement.file, commonProperties.getZone(), commonProperties.getPictureSet(), commonProperties.getToDir().toString(), false)); instance.increaseProgress();});
/*
        ArrayList<File> fileList = new ArrayList<>();
        directoryElements.stream().forEach((directoryElement) -> {fileList.add(directoryElement.file);});
        Collection<Meta> exifToMeta = readFileMeta(fileList.toArray(new File[0]), commonProperties.getZone());
        ArrayList<AnalyzingMediaFile> files = new ArrayList<>();
        for (Meta next : exifToMeta) {
            files.add(new AnalyzingMediaFile(next));
        }
        */
        return files;
    }

    //import and rename are basically the same
    private Collection<AnalyzingMediaFile> importFiles() {
        Collection<String> directories = StaticTools.defaultImportDirectories(new File("G:\\Pictures\\Photos\\Új\\Peru\\6500"));
        Path backupdrive = null;
            if ((backupdrive = Services.backupMounted()) == null) {
            StaticTools.errorOut("No backup Drive", new Exception("Attach a backup drive!"));
            if ((backupdrive = Services.backupMounted()) == null) {
    //                        return;
            }
        }
        return itWasImport(directories);
    }

    //no mediafile, creates a list of Meta
    private Collection<Meta> fileMetaList(Collection<String> directories, Path target) {
        Collection<DirectoryElement> directoryElements = StaticTools.getDirectoryElementsNonRecursive(directories, (File dir, String name) -> StaticTools.supportedFileType(name));
        ArrayList<File> fileList = new ArrayList<>();
        directoryElements.stream().forEach((directoryElement) -> {fileList.add(directoryElement.file);});
        return ExifReadWrite.readFileMeta(fileList.toArray(new File[0]), commonProperties.getZone());
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
