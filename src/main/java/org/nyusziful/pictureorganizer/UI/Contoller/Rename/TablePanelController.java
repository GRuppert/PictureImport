package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.ResourceBundle;

public class TablePanelController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private ProgressIndicator tableProgressIndicator;

    @FXML
    private Button btnGo;


    // </editor-fold>

    private MediaFileSet mediaFileSet;

    private TableView<? extends TableViewMediaFile> tableView;
    

    public TablePanelController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableProgressIndicator.progressProperty().addListener((observable, oldValue, newValue) -> {if (newValue.intValue() == 1) org.nyusziful.pictureorganizer.UI.StaticTools.beep();});
    }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
//        mediaFileSet.firstDateProperty().addListener((observable, oldValue, newValue) -> firstDatePicker.setValue(newValue));
    }



    @FXML
    private void handleSelectedButtonAction() {
        for (TableViewMediaFile f : tableView.getSelectionModel().getSelectedItems()) {
            f.setProcessing(!f.getProcessing());
        }
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
        btnGo.setDisable(true);
        tableProgressIndicator.setVisible(true);
        Task<Collection<TableViewMediaFile>> collectionTask = mediaFileSet.applyChanges(CommonProperties.getInstance().getCopyOrMove(), CommonProperties.getInstance().isOverwrite());
        tableProgressIndicator.progressProperty().bind(collectionTask.progressProperty());
        new Thread(collectionTask).start();
        btnGo.setDisable(false);
    }




    @FXML
    private void handleAbortButtonAction() { mediaFileSet.reset(); }

    @FXML
    private void handleRefreshButtonAction() {updateTargetDirectory();}

    private void updateTargetDirectory() {
        mediaFileSet.updatePaths(CommonProperties.getInstance().getToDir().toString());
    }


    public void setTableView(TableView<? extends TableViewMediaFile> tableView) {
        this.tableView = tableView;
    }
}
