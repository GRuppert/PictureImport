package org.nyusziful.pictureorganizer.UI.Contoller.Rename;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectoryInstance;
import org.nyusziful.pictureorganizer.Service.MediaDirectoryService;

import java.io.File;
import java.util.Comparator;

public class MediaDirectoryDBSet extends MediaDirectorySet {
    public MediaDirectoryDBSet() {
        readDirectory();
    }

    @Override
    public void readDirectory() {
        reset();
        ModelMapper modelMapper = new ModelMapper();
        for (MediaDirectory mediaDirectory : MediaDirectoryService.getInstance().getAll()) {
            dataModel.add(modelMapper.map(mediaDirectory, MediaDirectoryInstance.class));

        }
        dataModel.sort((o1, o2) -> {
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
        });
    }
}
