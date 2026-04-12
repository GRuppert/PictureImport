package org.nyusziful.pictureorganizer.UI.Contoller.Rename;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectoryInstance;

import java.io.File;
import java.util.Comparator;

public class MediaDirectorySet {
    protected final ObservableList<MediaDirectoryInstance> dataModel = FXCollections.observableArrayList();

    public MediaDirectorySet() {
        readDirectory();
    }

    public void readDirectory() {
        reset();
        final File[] files = CommonProperties.getInstance().getToDir().toFile().listFiles(File::isDirectory);
        for (File file : files) {
            try {
                dataModel.add(new MediaDirectoryInstance(file));
            } catch (IllegalArgumentException ex) {

            }
        }
        dataModel.sort(new Comparator<MediaDirectoryInstance>() {
            @Override
            public int compare(MediaDirectoryInstance o1, MediaDirectoryInstance o2) {
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

    public ObservableList<MediaDirectoryInstance> getDataModel() {
        return dataModel;
    }

}
