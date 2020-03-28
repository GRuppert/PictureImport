package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.nyusziful.pictureorganizer.DAL.Entity.JPGMediaFile;
import org.nyusziful.pictureorganizer.DAL.Entity.RAWMediaFile;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.UI.Model.RenameMediaFile;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;
import org.nyusziful.pictureorganizer.UI.Progress;
import org.nyusziful.pictureorganizer.UI.StaticTools;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService.createXmp;
import static org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory.getV;

public class MediaFileSet {
//    ArrayList<AnalyzingMediaFile> files = new ArrayList<>();
    private final ObservableList<TableViewMediaFile> dataModel = FXCollections.observableArrayList();
    SimpleObjectProperty<LocalDate> firstDate;
    SimpleObjectProperty<LocalDate> lastDate;
    private SimpleStringProperty folderName;
    private String label;
    public static DateTimeFormatter FolderFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");//2018-06-14



    public MediaFileSet() {
        folderName = new SimpleStringProperty("");
        firstDate = new SimpleObjectProperty(null);
        lastDate = new SimpleObjectProperty(null);
    }

    private void fillData(Collection<? extends TableViewMediaFile> files) {
//        this.files = files;
        dataModel.removeAll(dataModel);
        dataModel.addAll(files);
    }

    public void addData(Collection<? extends TableViewMediaFile> files) {
        dataModel.addAll(files);
    }

    public void addData(TableViewMediaFile file) {
        dataModel.add(file);
    }

    public ObservableList<TableViewMediaFile> getDataModel() {
        return dataModel;
    }

    public void reset() {
        dataModel.clear();
        label = "";
        firstDate.set(LocalDate.now());
        lastDate.set(LocalDate.now());
        folderName.set("");
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
        getDataModel().stream().forEach(file -> {if (file.getProcessing()) ((RenameMediaFile)file).setTargetDirectory(replacePath + "\\" + folderName.getValue());});
    }

    public Task<Collection<TableViewMediaFile>> applyChanges(TableViewMediaFile.WriteMethod copyOrMove, boolean overwrite) {
        Task<Collection<TableViewMediaFile>> task = new Task<Collection<TableViewMediaFile>>() {
            ArrayList<TableViewMediaFile> tableViewMediaFile = new ArrayList();

            @Override
            public Collection<TableViewMediaFile> call() {
                int iterations = 0;
                int size = MediaFileSet.this.getDataModel().size();
                Iterator<? extends TableViewMediaFile> iter = MediaFileSet.this.getDataModel().iterator();
                while (iter.hasNext()) {
                    if (isCancelled()) {
                        return tableViewMediaFile;
                    }
                    TableViewMediaFile actFile = iter.next();
                    if (actFile.write(copyOrMove, overwrite)) {
                        tableViewMediaFile.add(actFile);
                    }
                    iterations++;
                    updateProgress(iterations, size);
                }
                return tableViewMediaFile;
            }
        };

        task.setOnSucceeded(workerStateEvent -> getDataModel().removeAll(task.getValue()));
        task.setOnFailed(workerStateEvent -> task.getException().printStackTrace());
        return task;
    }

    public LocalDate getFirstDate() {
        return firstDate.getValue();
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate.set(firstDate);
        updateRange();
    }

    public SimpleObjectProperty<LocalDate> firstDateProperty() {return firstDate;}

    public LocalDate getLastDate() {
        return lastDate.getValue();
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate.set(lastDate);
        updateRange();
    }

    public SimpleObjectProperty<LocalDate> lastDateProperty() {return lastDate;}

    private void updateRange() {
        if (getFirstDate() != null && getLastDate() != null)
            folderName.set(getFirstDate().format(FolderFormat) + " - " + getLastDate().format(FolderFormat) + ((label != null && !"".equals(label)) ? " " + label : "")); //2018-06-14 - 2018-07-10 Peru
        else
            folderName.set("");
    }

    public SimpleStringProperty folderNameProperty() {
        return folderName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        updateRange();
    }

    public void resetDates() {
        for (TableViewMediaFile tableViewMediaFile : getDataModel()) {
            if (tableViewMediaFile instanceof RenameMediaFile) {
                final Meta v = getV(((RenameMediaFile) tableViewMediaFile).getNewName());
                if (v != null && v.date != null) {
                    final LocalDate localDateFromFile = v.date.toLocalDate();
                    if (firstDate.getValue() == null || firstDate.getValue().isAfter(localDateFromFile)) setFirstDate(localDateFromFile);
                    if (lastDate.getValue() == null || lastDate.getValue().isBefore(localDateFromFile)) setLastDate(localDateFromFile);
                }
            }
        }


    }
}
