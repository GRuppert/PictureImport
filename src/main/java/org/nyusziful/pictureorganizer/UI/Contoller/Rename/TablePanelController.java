package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class TablePanelController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private ProgressIndicator tableProgressIndicator;

    @FXML
    private Button btnGo;

    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public TablePanelController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

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
        btnGo.setDisable(true);
        tableProgressIndicator.setVisible(true);
        Task<Collection<TableViewMediaFile>> collectionTask = mediaFileSet.applyChanges(CommonProperties.getInstance().getCopyOrMove(), CommonProperties.getInstance().isOverwrite());
        tableProgressIndicator.progressProperty().bind(collectionTask.progressProperty());
        new Thread(collectionTask).start();
        btnGo.setDisable(false);
    }

    @FXML
    private void handleAbortButtonAction() { mediaFileSet.removeAll(); }

    @FXML
    private void handleRefreshButtonAction() { mediaFileSet.updatePaths(CommonProperties.getInstance().getToDir().toString()); }


}
