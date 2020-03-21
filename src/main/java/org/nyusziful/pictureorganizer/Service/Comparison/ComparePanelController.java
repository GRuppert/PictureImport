package org.nyusziful.pictureorganizer.Service.Comparison;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.commons.io.FilenameUtils;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.UI.DirectoryElement;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;
import org.nyusziful.pictureorganizer.UI.StaticTools;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import static org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory.getV;

public class ComparePanelController implements Initializable {
    private ObservableList<String> pairs = FXCollections.observableArrayList ();
    private ObservableList<ComparableMediaFile> singles = FXCollections.observableArrayList ();
    private ObservableList<Duplicate> duplicates = FXCollections.observableArrayList();
    private ObservableList<Duplicate> modpic = FXCollections.observableArrayList();

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;
    // </editor-fold>

    public ComparePanelController() {
    }


    private Collection<ComparableMediaFile> readsDirectoryToComparableMediaFile(Path path) {
        Collection<DirectoryElement> directoryElements = StaticTools.getDirectoryElementsRecursive(path);
        final ArrayList<ComparableMediaFile> files = new ArrayList();
        directoryElements.stream().forEach((de) -> files.add(new ComparableMediaFile(de.file, getV(de.file.getName()))));
        return files;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CommonProperties commonProperties = CommonProperties.getInstance();
        Collection<ComparableMediaFile> fromFiles = readsDirectoryToComparableMediaFile(commonProperties.getFromDir().toPath());
        Collection<ComparableMediaFile> toFiles = readsDirectoryToComparableMediaFile(commonProperties.getToDir());
        StringBuilder pairTo = new StringBuilder();
        StringBuilder pairFrom = new StringBuilder();
        StringBuilder singleStr = new StringBuilder();
        fromFiles.stream().forEach((file) -> singles.add(file));
        toFiles.stream().forEach((file) -> singles.add(file));
        this.duplicates.clear();
        this.modpic.clear();
        toFiles.stream().forEach((file) -> {
            fromFiles.stream().forEach((baseFile) -> {
                if (FilenameUtils.getExtension(file.file.getName()).equals(FilenameUtils.getExtension(baseFile.file.getName()))) {
                    if (file.iID != null && baseFile.iID != null && file.iID.equals(baseFile.iID)) {
                        pairs.add(baseFile.file.getName() + " : " + file.file.getName() + "\n");
                        pairTo.append(file.file.getAbsolutePath()).append("\n");
                        pairFrom.append(baseFile.file.getAbsolutePath()).append("\n");
                        singles.remove(baseFile);
                        singles.remove(file);
                    } else if (file.dID != null && baseFile.dID != null && file.dID.equals(baseFile.dID)) {
                        this.duplicates.add(new Duplicate(baseFile, file, true));
                        singles.remove(baseFile);
                        singles.remove(file);
                    } else if (file.odID != null && baseFile.odID != null && file.odID.equals(baseFile.odID)) {
                        this.modpic.add(new Duplicate(baseFile, file, false));
                        singles.remove(baseFile);
                        singles.remove(file);
                    }
                }
            });
        });
        ArrayList<MetaChanges> metaChange = new ArrayList();
        ArrayList<String> metaTags = new ArrayList();
        dupFor:
        for (Duplicate dup : duplicates) {
            for (String item : dup.footprint) {
                if (!metaTags.contains(item)) metaTags.add(item);
            }
            for (MetaChanges change : metaChange) {
                if (change.compare(dup.footprint, dup.getDir())) {
                    continue dupFor;
                }
            }
            metaChange.add(new MetaChanges(dup.footprint, dup.getDir()));
        }
        for (MetaChanges change : metaChange) {
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
    }

    private TableView<? extends TableViewMediaFile> createDuplicateTable(ObservableList<Duplicate> input) {
        TableView<? extends TableViewMediaFile> tableView = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/fxml/mediaFileTableView.fxml"));
            tableView = (TableView) loader.load();
            CompareTableViewController ctrl = loader.getController();
            ctrl.setInput(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView;
    }


    public ObservableList<String> getPairs() {
        return pairs;
    }

    public ObservableList<ComparableMediaFile> getSingles() {
        return singles;
    }
}
