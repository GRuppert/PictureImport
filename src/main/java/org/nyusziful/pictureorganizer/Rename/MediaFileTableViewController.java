package org.nyusziful.pictureorganizer.Rename;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaFileTableViewController implements MediaFileSetTableViewController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableView Table;

    @FXML
    private TableColumn<AnalyzingMediaFile, Boolean > processingCol;

    @FXML
    private TableColumn<AnalyzingMediaFile, Boolean > xmpCol;
    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public MediaFileTableViewController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
        Table.setItems(mediaFileSet.getDataModel());
        processingCol.setCellFactory( tc -> new CheckBoxTableCell<>());
        xmpCol.setCellFactory( tc -> new CheckBoxTableCell<>());
    }


}
