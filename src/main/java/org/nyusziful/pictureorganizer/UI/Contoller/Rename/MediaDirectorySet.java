package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;

import java.io.File;

public class MediaDirectorySet {
    private final ObservableList<MediaDirectory> dataModel = FXCollections.observableArrayList();

    public MediaDirectorySet() {
        readDirectory();
    }

    public void readDirectory() {
        reset();
        final File[] files = CommonProperties.getInstance().getToDir().toFile().listFiles(File::isDirectory);
        for (File file : files) {
            try {
                dataModel.add(new MediaDirectory(file));
            } catch (InvalidArgumentException ex) {

            }
        }
    }

    public void reset() {
        dataModel.clear();
    }

    public ObservableList<MediaDirectory> getDataModel() {
        return dataModel;
    }

}
