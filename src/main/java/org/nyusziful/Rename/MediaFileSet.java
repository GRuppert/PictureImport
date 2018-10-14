package org.nyusziful.Rename;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.nyusziful.Main.Progress;
import org.nyusziful.Main.StaticTools;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.nyusziful.ExifUtils.ExifReadWrite.exifToMeta;
import static org.nyusziful.Main.StaticTools.supportedFileType;

public class MediaFileSet {
//    ArrayList<WritableMediaFile> files = new ArrayList<>();
    private final ObservableList<WritableMediaFile> dataModel = FXCollections.observableArrayList();

    public MediaFileSet(ArrayList<WritableMediaFile> files) {
        fillData(files);
    }

    /**
     * Creates a WritableMediaFile object for each media file in the directories non-recursive
     * @param directories list of the directories to process
     */
    public MediaFileSet(List<String> directories) {
        Iterator<String> iter = directories.iterator();
        ArrayList<WritableMediaFile> files = new ArrayList<>();
        while(iter.hasNext()) {
            File dir1 = new File(iter.next());
            if(dir1.isDirectory()) {
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                JProgressBar progressBar = new JProgressBar(0, content.length);
                JDialog progressDialog = progressDiag(progressBar);
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<String> fileList = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        fileList.add(content[j*chunkSize + f].getName());
                    }
                    List<meta> exifToMeta = exifToMeta(fileList, dir1, commonProperties.getZone());
                    Iterator<meta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        meta next = iterator.next();
                        files.add(new WritableMediaFile(next));
                        progressBar.setValue(i + j*chunkSize);
                        this.setProgress((i + j*chunkSize)/content.length);
                    }
                }
                progressDialog.dispose();
            }
        }


        //Todo implement from the controller
        ArrayList<WritableMediaFile> files = null;
        fillData(files);
    }

    private void fillData(List<WritableMediaFile> files) {
//        this.files = files;
        dataModel.removeAll(dataModel);
        files.stream().forEach((obj) -> {dataModel.add(obj);});
    }

    public ObservableList<WritableMediaFile> getDataModel() {
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
        for(WritableMediaFile paths: getDataModel()) {
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
            for (WritableMediaFile record : getDataModel()) {
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
