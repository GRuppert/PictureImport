package org.nyusziful.Rename;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.apache.commons.io.FileUtils;
import org.nyusziful.Main.CommonProperties;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.nyusziful.Main.StaticTools.errorOut;

public class ComparePanelController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableColumn< WritableMediaFile, Boolean > xmpCol;
    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public ComparePanelController() {
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

    public ObservableList<WritableMediaFile> getDataModel() { return mediaFileSet.getDataModel(); }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
    }

    @FXML
    private void handleSingleButtonAction() {
        try {
            FileUtils.writeStringToFile(new File("e:\\single.txt"), single.toString(), "ISO-8859-1");
        } catch (IOException ex) {
            errorOut("Write to file", ex);
        }

    }

    @FXML
    private void handlePairsToButtonAction() {
        try {
            FileUtils.writeStringToFile(new File("e:\\pairTo.txt"), pairTo.toString(), "ISO-8859-1");
        } catch (IOException ex) {
            errorOut("Write to file", ex);
        }

    }

    @FXML
    private void handlePairsFromButtonAction() {
        try {
            FileUtils.writeStringToFile(new File("e:\\pairFrom.txt"), pairFrom.toString(), "ISO-8859-1");
        } catch (IOException ex) {
            errorOut("Write to file", ex);
        }

    }


}
