package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.Service.Comparison.Listing;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Service.MediafileService;
import org.nyusziful.pictureorganizer.UI.Contoller.Rename.MediaFileSet;
import org.nyusziful.pictureorganizer.UI.Contoller.Rename.MediaFileTableViewController;
import org.nyusziful.pictureorganizer.UI.Contoller.Rename.TablePanelController;
import org.nyusziful.pictureorganizer.UI.DirectoryElement;
import org.nyusziful.pictureorganizer.UI.Model.MetaProp;
import org.nyusziful.pictureorganizer.UI.Model.RenameMediaFile;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;
import org.nyusziful.pictureorganizer.UI.Progress;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;
import org.nyusziful.pictureorganizer.DTO.Meta;

import static java.lang.Integer.*;
import static org.nyusziful.pictureorganizer.Service.MediafileService.getMediafileDTO;
import static org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory.getV;
import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

import org.nyusziful.pictureorganizer.Service.TimeShift.TimeLine;

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

import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.UI.ProgressLeakingTask;

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
    private MediaFileSet mediaFileSet = new MediaFileSet();

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

        BorderPane summaryPane = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/summary.fxml"));
            summaryPane = (BorderPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainPane.setCenter(summaryPane);
        progressIndicator.progressProperty().addListener((observable, oldValue, newValue) -> {if (newValue.intValue() == 1) beep();});
    }

    // <editor-fold defaultstate="collapsed" desc="View Action">
    @FXML
    private void handleImportButtonAction() {
        disableButtons(true);
        showTablePane();
        itWasImport(defaultImportDirectories());
    }

    @FXML
    private void handleImportFromButtonAction() {
        disableButtons(true);
        showTablePane();
        itWasImport(importDirectories(getDir(commonProperties.getFromDir())));
    }

    @FXML
    private void handleShiftButtonAction() {
        disableButtons(true);
        File file = getDir(commonProperties.getFromDir());
        if(file != null) {
            stripesOnScreen(file);
        }
    }

    @FXML
    private void handleMetaButtonAction() {
        File file = getDir(commonProperties.getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            Path tempDir = Paths.get(commonProperties.getToDir().toString() + "\\" + file.getName());
            createMetaTable(fileMetaList(directories, tempDir));
        }
    }

    @FXML
    private void handleRenameButtonAction() {
        File file = getDir(commonProperties.getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<>();
            directories.add(file.toString());
            disableButtons(true);
            showTablePane();
            itWasImport(directories);
        }
    }

    @FXML
    private void handleMoveButtonAction() {
        File file = getDir(commonProperties.getFromDir());
        if(file != null) {
            ArrayList<String> directories = new ArrayList<String>();
            directories.add(file.toString());
            showTablePane();
            sortToDateDirectories(directories);
        }
    }

    @FXML
    private void handleReorgButtonAction() {
        File file = getDir(commonProperties.getFromDir());
        if(file != null) {
            disableButtons(true);
            showTablePane();
            reorganizeFolder(file);
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
        File file = getDir(commonProperties.getToDir().toFile());
        if (file != null) {
            commonProperties.setToDir(file.toPath());
            setToText(commonProperties.getToDir().toString());
        }
    }

    @FXML
    private void handleFromButtonAction() {
        File file = getDir(commonProperties.getFromDir());
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
    }

    private void createMetaTable(Collection<Meta> newData) {
        TableView<? extends TableViewMediaFile> tableView = null;
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
    }


    //Sets the center view to table format
    private void showTablePane() {
        BorderPane tablePane = null;
        TableView<? extends TableViewMediaFile> table = null;
        mediaFileSet.reset();
        try {
            FXMLLoader loaderTable = new FXMLLoader(getClass().getResource("/fxml/mediaFileTableView.fxml"));
            table = (TableView) loaderTable.load();
            MediaFileTableViewController ctrlTable = loaderTable.getController();
            ctrlTable.setMediaFileSet(mediaFileSet);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tablePanel.fxml"));
            tablePane = (BorderPane) loader.load();
            tablePane.setCenter(table);
            TablePanelController ctrl = loader.getController();
            ctrl.setMediaFileSet(mediaFileSet);
            ctrl.setTableView(ctrlTable.getTableView());
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
    private Collection<RenameMediaFile> sortToDateDirectories(Collection<String> directories) {
        Collection<DirectoryElement> directoryElements = getDirectoryElementsNonRecursive(directories, new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        name = name.toLowerCase();
                        return name.endsWith(".mts") || name.endsWith(".arw") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".mp4") || name.endsWith(".dng");
                    }});
        ArrayList<File> fileList = new ArrayList<>();
        directoryElements.stream().forEach((directoryElement) -> {fileList.add(directoryElement.file);});
        ArrayList<RenameMediaFile> files = new ArrayList<>();
        Path target = null;
        ZonedDateTime previousFileDate = ZonedDateTime.now();

        for (File file : fileList) {
            ZonedDateTime fileDate = getV(file.getName()).date;
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
            files.add(new RenameMediaFile(getMediafileDTO(file), file.getName(), "", target.toString()));
//            System.out.println(file.toString() + " -> " + target);
        }
        return files;
    }

    public class ImportTask extends Task<Integer> {
        private final Collection<String> directories;
        private int numberOfFiles;
        private int processedFiles;

        public ImportTask(Collection<String> directories) {
            this.directories = directories;
        }

        @Override
        public Integer call() {
            Collection<DirectoryElement> directoryElements = getDirectoryElementsNonRecursive(directories, new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return supportedFileType(name);
                }});
            processedFiles = 0;
            numberOfFiles = directoryElements.size();
            updateProgress(0, numberOfFiles);
            MediafileService mediafileService = new MediafileService();
            String notes = "";
            for (String directory : directories) {
                final Set<MediafileDTO> mediaFiles = mediafileService.readMediaFilesFromFolder(Paths.get(directory), true, false, commonProperties.getZone(), notes, this);
                Set<RenameMediaFile> renameMediaFiles = new HashSet<>();
                for (MediafileDTO mediafileDTO : mediaFiles) {
                    RenameMediaFile renameMediaFile = new RenameMediaFile(mediafileDTO, "", notes, commonProperties.getToDir().toString());
                    renameMediaFiles.add(renameMediaFile);
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            mediaFileSet.addData(renameMediaFile);
                        }
                    });
                }
                int iter = 0;
                for (RenameMediaFile renameMediaFile : renameMediaFiles) {
                    final String newName = mediafileService.getMediaFileName(renameMediaFile.getMediafileDTO(), "6");
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            renameMediaFile.setNewName(newName);
                        }
                    });
                }
            }
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    mediaFileSet.resetDates();
                }
            });
            return processedFiles;
        }

        public void increaseProgress() {
            processedFiles++;
            updateProgress((double)processedFiles, (double)numberOfFiles);
        }
    }

    public class ReorganizatorTask extends ProgressLeakingTask<Integer> {

        public ReorganizatorTask(Collection<File> directories) {
            super(directories);
        }

        @Override
        protected Integer call() throws Exception {
            MediafileService mediafileService = new MediafileService();
            Set<MediafileDTO> mediaFiles = new HashSet<>();
            for (File directory : directories) {
                mediaFiles.addAll(mediafileService.reOrganizeFilesInSubFolders(directory.toPath(), this));
            }
            Set<RenameMediaFile> renameMediaFiles = new HashSet<>();
            for (MediafileDTO mediafileDTO : mediaFiles) {
                RenameMediaFile renameMediaFile = new RenameMediaFile(mediafileDTO, "", "", commonProperties.getToDir().toString());
                renameMediaFiles.add(renameMediaFile);
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        mediaFileSet.addData(renameMediaFile);
                    }
                });
            }
            int iter = 0;
            for (RenameMediaFile renameMediaFile : renameMediaFiles) {
                final String newName = mediafileService.getMediaFileName(renameMediaFile.getMediafileDTO(), "6");
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        renameMediaFile.setNewName(newName);
                    }
                });
            }
            return mediaFiles.size();
        }
    }

    public void reorganizeFolder(File directory) {
        ReorganizatorTask task = new ReorganizatorTask(Collections.singleton(directory));
        statusLabel.setText("");
        task.setOnFailed(a -> {System.err.println(task.getException()); progressIndicator.progressProperty().unbind(); mediaFileSet.reset(); resetAction();});
        task.setOnSucceeded(
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        if (task.getValue().intValue() == 0) {
                            resetAction();
                        }
                        statusLabel.setText(task.getValue().intValue() + " file(s) processed.");
                    }
                });
        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }


    //Input mediafiles Output standard
    public void itWasImport(Collection<String> directories) {
        Task<Integer> task = new ImportTask(directories);
        statusLabel.setText("");
        task.setOnFailed(a -> {System.err.println(task.getException()); progressIndicator.progressProperty().unbind(); mediaFileSet.reset(); resetAction();});
        task.setOnSucceeded(
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        if (task.getValue().intValue() == 0) {
                            resetAction();
                        }
                        statusLabel.setText(task.getValue().intValue() + " file(s) processed.");
                    }
                });
        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    //import and rename are basically the same
/*    private Collection<TableViewMediaFile> importFiles() {
        Collection<String> directories = defaultImportDirectories();
        Path backupdrive = null;
            if ((backupdrive = Services.backupMounted()) == null) {
            errorOut("No backup DriveDTO", new Exception("Attach a backup drive!"));
            if ((backupdrive = Services.backupMounted()) == null) {
    //                        return;
            }
        }
        return itWasImport(directories);
    }*/

    //no mediafile, creates a list of Meta
    private Collection<Meta> fileMetaList(Collection<String> directories, Path target) {
        Collection<DirectoryElement> directoryElements = getDirectoryElementsNonRecursive(directories, (File dir, String name) -> supportedFileType(name));
        ArrayList<File> fileList = new ArrayList<>();
        directoryElements.stream().forEach((directoryElement) -> {fileList.add(directoryElement.file);});
        return ExifService.readFileMeta(fileList.toArray(new File[0]), commonProperties.getZone());
    }





    /**
     * Creates a new background task which reads the From directory files' hashes into a list file
     */
    private void createList() {
        File file = getDir(commonProperties.getFromDir());
        File output = getFile(commonProperties.getFromDir());
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
