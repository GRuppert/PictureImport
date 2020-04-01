package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static org.nyusziful.pictureorganizer.UI.StaticTools.defaultImportDirectories;

public class DirectoryViewController implements Initializable {
    @FXML
    private ListView<MediaDirectory> directoryList;
    private MediaDirectorySet mediaDirectorySet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediaDirectorySet = new MediaDirectorySet();
        directoryList.setItems(mediaDirectorySet.getDataModel());
        directoryList.setCellFactory(param -> new ListCell<MediaDirectory>() {
            @Override
            protected void updateItem(MediaDirectory item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.toString() == null) {
                    setText(null);
                } else {
//                    if (item.isConflicting()) setTextFill(isSelected() ? Color.ORANGE : Color.PINK);
                    setText(item.toString());
                }
            }
        });
    }

    @FXML
    private void handleRefreshButtonAction() {
        mediaDirectorySet.readDirectory();
    }
}
