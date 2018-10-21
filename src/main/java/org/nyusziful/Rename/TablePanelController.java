package org.nyusziful.Rename;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.nyusziful.Main.CommonProperties;

import java.net.URL;
import java.util.ResourceBundle;

public class TablePanelController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableColumn<AnalyzingMediaFile, Boolean > processingCol;

    @FXML
    private TableColumn oldNameCol;

    @FXML
    private TableColumn newNameCol;

    @FXML
    private TableColumn noteCol;

    @FXML
    private TableColumn<AnalyzingMediaFile, Boolean > xmpCol;
    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public TablePanelController() {
    }


    public void initialize(URL url, ResourceBundle rb) {

/*        oldNameCol.setCellValueFactory(new PropertyValueFactory<tableViewMediaFile, String>("currentName"));
        newNameCol.setCellValueFactory(new PropertyValueFactory<tableViewMediaFile, String>("newName"));
        noteCol.setCellValueFactory(new PropertyValueFactory<tableViewMediaFile, String>("note"));
*/
//        processingCol.setCellValueFactory( f -> f.getValue().processingProperty());
        processingCol.setCellFactory(CheckBoxTableCell.forTableColumn(processingCol));

//        xmpCol.setCellValueFactory( f -> f.getValue().xmpMissingProperty());
        xmpCol.setCellFactory(CheckBoxTableCell.forTableColumn(xmpCol));
    }

    public ObservableList<? extends tableViewMediaFile> getDataModel() { return mediaFileSet.getDataModel(); }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
    }

    @FXML
    private void handleAllButtonAction() {
        mediaFileSet.selectAll();
    }

    @FXML
    private void handleNoneButtonAction() {
        mediaFileSet.selectNone();
    }

    @FXML
    private void handleInvertButtonAction() {
        mediaFileSet.invertSelection();
    }

    @FXML
    private void handleGoButtonAction() {
        mediaFileSet.applyChanges(CommonProperties.getInstance().getCopyOrMove());
    }

    @FXML
    private void handleAbortButtonAction() { mediaFileSet.removeAll(); }

    @FXML
    private void handleRefreshButtonAction() { mediaFileSet.updatePaths(CommonProperties.getInstance().getToDir().toString()); }


}
