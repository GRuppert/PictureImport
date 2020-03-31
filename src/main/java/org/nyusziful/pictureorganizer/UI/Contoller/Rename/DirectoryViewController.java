package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DirectoryViewController implements Initializable {
    @FXML
    private ListView<MediaDirectory> directoryList;
    private MediaDirectorySet mediaDirectorySet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryList = new ListView<MediaDirectory>();
        mediaDirectorySet = new MediaDirectorySet();
        directoryList.setItems(mediaDirectorySet.getDataModel());
    }

}
