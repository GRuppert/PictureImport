package org.nyusziful.Rename;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.Main.Progress;
import org.nyusziful.Main.StaticTools;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.nyusziful.ExifUtils.ExifReadWrite.readFileMeta;

public class MediaFileSet {
//    ArrayList<AnalyzingMediaFile> files = new ArrayList<>();
    private final ObservableList<tableViewMediaFile> dataModel = FXCollections.observableArrayList();

    public MediaFileSet(Collection<? extends tableViewMediaFile> files) {
        fillData(files);
    }

    private void fillData(Collection<? extends tableViewMediaFile> files) {
//        this.files = files;
        dataModel.removeAll(dataModel);
        files.stream().forEach((obj) -> {dataModel.add(obj);});
    }

    public ObservableList<tableViewMediaFile> getDataModel() {
        return dataModel;
    }

    public void selectAll() {
        getDataModel().forEach(f -> f.setProcessing(Boolean.TRUE));
    }

    public void selectNone() {
        getDataModel().forEach(f -> f.setProcessing(Boolean.FALSE));
    }

    public void invertSelection() {
        getDataModel().forEach(f -> f.setProcessing(!f.getProcessing()));
    }

    public void updatePaths(String replacePath) {
        getDataModel().stream().forEach(file -> file.setTargetDirectory(replacePath));
    }

    public void applyChanges(tableViewMediaFile.WriteMethod copyOrMove) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Progress progress = Progress.getInstance();
                while (progress.timeToReady() != 0) {
                    try {
                        wait(progress.timeToReady());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                progress.setGoal(getDataModel().size());
                Iterator<tableViewMediaFile> iter = getDataModel().iterator();
                while (iter.hasNext()) {
                    tableViewMediaFile record = iter.next();
                    if (record.write(copyOrMove)) {
//                        getDataModel().remove(record);
                    }
                    progress.increaseProgress();
                }
                StaticTools.beep();
            }
        });
    }

    public void removeAll() {
        getDataModel().removeAll(getDataModel());
    }
}
