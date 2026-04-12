package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFileInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MediaFileTableViewController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableView<TableViewMediaFileInstance> tableView;

    @FXML
    private TableColumn<TableViewMediaFileInstance, Boolean > processingCol;

    @FXML
    private TableColumn<TableViewMediaFileInstance, Boolean > xmpCol;
    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public MediaFileTableViewController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
        tableView.setItems(mediaFileSet.getDataModel());
        processingCol.setCellFactory( tc -> new CheckBoxTableCell<>());
        xmpCol.setCellFactory( tc -> new CheckBoxTableCell<>());
    }


    public TableView<TableViewMediaFileInstance> getTableView() {
        return tableView;
    }
}
