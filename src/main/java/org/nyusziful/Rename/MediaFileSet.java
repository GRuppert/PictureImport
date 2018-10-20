package org.nyusziful.Rename;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.Main.Progress;
import org.nyusziful.Main.StaticTools;

import java.nio.file.Path;
import java.util.List;

import static org.nyusziful.ExifUtils.ExifReadWrite.readFileMeta;

public class MediaFileSet {
//    ArrayList<AnalyzingMediaFile> files = new ArrayList<>();
    private final ObservableList<tableViewMediaFile> dataModel = FXCollections.observableArrayList();

    public MediaFileSet(List<tableViewMediaFile> files) {
        fillData(files);
    }

    private void fillData(List<tableViewMediaFile> files) {
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
        for(tableViewMediaFile paths: getDataModel()) {
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
            progress.setGoal(getDataModel().size());
            for (tableViewMediaFile record : getDataModel()) {
                record.write(copyOrMove);
                getDataModel().remove(record);
                i++;
                progress.setProgress(i);
            }
            StaticTools.beep();
        };
        new Thread(r).start();
    }

    public void removeAll() {
        getDataModel().removeAll(getDataModel());
    }
}
