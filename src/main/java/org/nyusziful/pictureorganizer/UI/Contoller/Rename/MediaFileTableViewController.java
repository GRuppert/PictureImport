package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaFileTableViewController implements MediaFileSetTableViewController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableView Table;

    @FXML
    private TableColumn<TableViewMediaFile, Boolean > processingCol;

    @FXML
    private TableColumn<TableViewMediaFile, Boolean > xmpCol;
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
