package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;

import java.io.File;
import java.util.Comparator;

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
        dataModel.sort(new Comparator<MediaDirectory>() {
            @Override
            public int compare(MediaDirectory o1, MediaDirectory o2) {
                if (o1.getLastDate().isBefore(o2.getFirstDate())) {
                    return -1;
                } else {
                    if (o2.getLastDate().isBefore(o1.getFirstDate())) return 1;
                    else {
                        o1.setConflicting(true);
                        o2.setConflicting(true);
                        return o1.getFirstDate().compareTo(o2.getFirstDate());
                    }
                }
            }
        });
    }

    public void reset() {
        dataModel.clear();
    }

    public ObservableList<MediaDirectory> getDataModel() {
        return dataModel;
    }

}
