package org.nyusziful.Rename;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.Main.Progress;
import org.nyusziful.Main.StaticTools;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class MediaFileSet {
    ArrayList<WritableMediaFile> files = new ArrayList<>();
    private final ObservableList<WritableMediaFile> dataModel = FXCollections.observableArrayList();
    Thread applier;

    public MediaFileSet(ArrayList<WritableMediaFile> files) {
        this.files = files;
    }

    public MediaFileSet(File rootdir, boolean recursive) {
        //Todo implement from the controller
    }

    public ObservableList<WritableMediaFile> getDataModel() {
        return dataModel;
    }

    public void selectAll() {
        dataModel.forEach(f -> f.setProcessing(Boolean.TRUE));
    }

    public void selectNone() {
        dataModel.forEach(f -> f.setProcessing(Boolean.FALSE));
    }

    public void invertSelection() {
        dataModel.forEach(f -> f.setProcessing(!f.getProcessing()));
    }

    public void updatePaths(String replacePath) {
        for(WritableMediaFile paths: dataModel) {
            Path newPath = paths.getNewPath();
            String fileName = newPath.getFileName().toString();
            String replacement =  replacePath + "\\" + fileName;
            paths.setNewName(replacement);
        }
    }

    public void applyChanges(int copyOrMove) {
        Runnable r = () -> {
            int i = 0;
            Progress progress = Progress.getInstance();
            while (progress.timeToReady() != 0) {
                try {
                    wait(progress.timeToReady());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            progress.setGoal(dataModel.size());
            for (WritableMediaFile record : dataModel) {
                record.write(copyOrMove);
                dataModel.remove(record);
                i++;
                progress.setProgress(i);
            }
            StaticTools.beep();
        };
        new Thread(r).start();
    }

    public void removeAll() {
        dataModel.removeAll(dataModel);
    }
}
