package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.nyusziful.pictureorganizer.UI.Model.MetaProp;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import java.net.URL;
import java.util.ResourceBundle;

public class MetaTableViewController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableColumn<TableViewMediaFile, Boolean > processingCol;

    @FXML
    private TableColumn oldNameCol;

    @FXML
    private TableColumn newNameCol;

    @FXML
    private TableColumn noteCol;

    @FXML
    private TableColumn<TableViewMediaFile, Boolean > xmpCol;
    // </editor-fold>

    private ObservableList<MetaProp> meta;

    public MetaTableViewController() {
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

/*        oldNameCol.setCellValueFactory(new PropertyValueFactory<TableViewMediaFile, String>("currentName"));
        newNameCol.setCellValueFactory(new PropertyValueFactory<TableViewMediaFile, String>("newName"));
        noteCol.setCellValueFactory(new PropertyValueFactory<TableViewMediaFile, String>("note"));
*/
//        processingCol.setCellValueFactory( f -> f.getValue().processingProperty());
        processingCol.setCellFactory(CheckBoxTableCell.forTableColumn(processingCol));

//        xmpCol.setCellValueFactory( f -> f.getValue().xmpMissingProperty());
        xmpCol.setCellFactory(CheckBoxTableCell.forTableColumn(xmpCol));
    }



    public ObservableList<MetaProp> getMeta() {
        return meta;
    }

    public void setMeta(ObservableList<MetaProp> meta) {
        this.meta = meta;
    }
}
