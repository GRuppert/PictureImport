package org.nyusziful.Comparison;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.apache.commons.io.FilenameUtils;
import org.nyusziful.Main.CommonProperties;
import org.nyusziful.Main.DirectoryElement;
import org.nyusziful.Main.StaticTools;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.nyusziful.Rename.fileRenamer.getV;

public class ComparePanelController {
    private ObservableList<String> pairs = FXCollections.observableArrayList ();
    private ObservableList<comparableMediaFile> singles = FXCollections.observableArrayList ();
    private ObservableList<duplicate> duplicates = FXCollections.observableArrayList();
    private ObservableList<duplicate> modpic = FXCollections.observableArrayList();

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;
    // </editor-fold>

    public ComparePanelController() {
    }


    private ArrayList<comparableMediaFile> readsDirectoryToComparableMediaFile(Path path) {
        List<DirectoryElement> directoryElements = StaticTools.getDirectoryElementsRecursive(path);
        final ArrayList<comparableMediaFile> files = new ArrayList();
        directoryElements.stream().forEach((de) -> files.add(new comparableMediaFile(de.file, getV(de.file.getName()))));
        return files;
    }

    public void initialize(URL url, ResourceBundle rb) {
        CommonProperties commonProperties = CommonProperties.getInstance();
        ArrayList<comparableMediaFile> fromFiles = readsDirectoryToComparableMediaFile(commonProperties.getFromDir().toPath());
        ArrayList<comparableMediaFile> toFiles = readsDirectoryToComparableMediaFile(commonProperties.getToDir());
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
        tab2.setContent(createDuplicateTable(duplicates));
        tab3.setContent(createDuplicateTable(modpic));
        ListView<String> listSingle = new ListView<String>();
        ObservableList<String> single = FXCollections.observableArrayList ();
        singles.stream().forEach((file) -> single.add(file.file.getName() + "\n"));
        singles.stream().forEach((file) -> singleStr.append(file.file.getAbsolutePath()).append("\n"));
        listSingle.setItems(single);

        StaticTools.beep();
    }

    private TableView createDuplicateTable(ObservableList<duplicate> input) {
        TableView tableView = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/mediaFileTableView.fxml"));
            tableView = (TableView) loader.load();
            compareTableViewController ctrl = loader.getController();
            ctrl.setInput(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView;
    }


    public ObservableList<String> getPairs() {
        return pairs;
    }

    public ObservableList<comparableMediaFile> getSingles() {
        return singles;
    }
}
