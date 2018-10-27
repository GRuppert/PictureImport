package org.nyusziful.Rename;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.nyusziful.Main.Progress;
import org.nyusziful.Main.StaticTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MediaFileSet {
//    ArrayList<AnalyzingMediaFile> files = new ArrayList<>();
    private final ObservableList<TableViewMediaFile> dataModel = FXCollections.observableArrayList();

    public MediaFileSet(Collection<? extends TableViewMediaFile> files) {
        fillData(files);
    }

    private void fillData(Collection<? extends TableViewMediaFile> files) {
//        this.files = files;
        dataModel.removeAll(dataModel);
        files.stream().forEach((obj) -> {dataModel.add(obj);});
    }

    public ObservableList<TableViewMediaFile> getDataModel() {
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

    public Task<Collection<TableViewMediaFile>> applyChanges(TableViewMediaFile.WriteMethod copyOrMove) {
        Task<Collection<TableViewMediaFile>> task = new Task<Collection<TableViewMediaFile>>() {
            ArrayList<TableViewMediaFile> tableViewMediaFile = new ArrayList();

            @Override
            public Collection<TableViewMediaFile> call() {
                int iterations = 0;
                Progress progress = Progress.getInstance();
                while (progress.timeToReady() != 0) {
                    if (isCancelled()) {
                        return tableViewMediaFile;
                    }
                    try {
                        MediaFileSet.this.wait(progress.timeToReady());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int size = MediaFileSet.this.getDataModel().size();
                progress.setGoal(size);
                Iterator<TableViewMediaFile> iter = MediaFileSet.this.getDataModel().iterator();
                while (iter.hasNext()) {
                    if (isCancelled()) {
                        return tableViewMediaFile;
                    }
                    TableViewMediaFile record = iter.next();
                    if (record.write(copyOrMove)) {
                        tableViewMediaFile.add(record);
                    }
                    iterations++;
                    updateProgress(iterations, size);
                    progress.increaseProgress();
                }
                StaticTools.beep();
                return tableViewMediaFile;
            }
        };

        task.setOnSucceeded(workerStateEvent -> getDataModel().removeAll(task.getValue()));
        return task;
    }

    public void removeAll() {
        getDataModel().removeAll(getDataModel());
    }
}
