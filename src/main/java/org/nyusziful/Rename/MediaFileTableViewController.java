package org.nyusziful.Rename;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.nyusziful.Main.CommonProperties;

public class MediaFileTableViewController {
    private MediaFileSet mediaFileSet;

    public MediaFileTableViewController() {
    }

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private Tab tab4;
    // </editor-fold>

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
    private void handleAbortButtonAction() {
    }

    @FXML
    private void handleRefreshButtonAction() {
        mediaFileSet.updatePaths(CommonProperties.getInstance().getToDir().toString());
    }


    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
    }
}
