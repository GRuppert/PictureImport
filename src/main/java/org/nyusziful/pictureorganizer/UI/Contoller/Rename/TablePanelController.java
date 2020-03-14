package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;
import org.nyusziful.pictureorganizer.UI.Progress;

import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicMenuUI;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class TablePanelController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private ProgressIndicator tableProgressIndicator;

    @FXML
    private Button btnGo;

    @FXML
    private Label fileDateRange;

    @FXML
    private TextField eventNameField;

    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public TablePanelController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
        mediaFileSet.getFolderName().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                fileDateRange.setText(newValue != null ? newValue : "");
            }
        });
        eventNameField.setPromptText("Event name");
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
    private void handleApplyEventNameButtonAction() {
        mediaFileSet.setLabel(eventNameField.getText());
        updateTargetDirectory();
    }

    @FXML
    private void handleAbortButtonAction() { mediaFileSet.reset(); }

    @FXML
    private void handleRefreshButtonAction() {updateTargetDirectory();}

    private void updateTargetDirectory() {
        mediaFileSet.updatePaths(CommonProperties.getInstance().getToDir().toString());
    }


}
