package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;
import org.nyusziful.pictureorganizer.Service.FolderService;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static org.nyusziful.pictureorganizer.Model.MediaDirectory.FolderFormat;

public class DirectoryViewController implements Initializable {
    @FXML
    private ListView<MediaDirectory> directoryList;

    @FXML
    private javafx.scene.control.Label targetFolderName;

    @FXML
    private TextField eventNameField;

    @FXML
    private DatePicker firstDatePicker;

    @FXML
    private DatePicker lastDatePicker;

    private SimpleStringProperty folderName;
    private SimpleObjectProperty<LocalDate> firstDate;
    private SimpleObjectProperty<LocalDate> lastDate;

    private MediaDirectorySet mediaDirectorySet;

    private FolderService folderService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediaDirectorySet = new MediaDirectorySet();
        folderService = new FolderService();
        folderName = new SimpleStringProperty("");
        firstDate = new SimpleObjectProperty(null);
        lastDate = new SimpleObjectProperty(null);
        directoryList.setItems(mediaDirectorySet.getDataModel());
        directoryList.setCellFactory(param -> new ListCell<MediaDirectory>() {
            @Override
            protected void updateItem(MediaDirectory item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.toString() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                } else {
                    if (item.isConflicting())
                        setStyle("-fx-control-inner-background: #ffc0cb;");
                    else
                        setStyle(null);
                    setText(item.toString());
                }
            }
        });
        directoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MediaDirectory>() {
                    public void changed(ObservableValue<? extends MediaDirectory> observable,
                                        MediaDirectory oldValue, MediaDirectory newValue) {
                        if (newValue == null) {
                            resetFields();
                        } else {
                            folderName.setValue(newValue.toString());
                            firstDate.set(newValue.getFirstDate());
                            lastDate.set(newValue.getLastDate());
                            eventNameField.setText(newValue.getLabel());
                        }
                    }
                });
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (firstDatePicker != null && item != null) {
                                    if (item.isBefore(firstDatePicker.getValue())) {
                                        setDisable(true);
                                        setStyle("-fx-background-color: #ffc0cb;");
                                    }
                                    long p = ChronoUnit.DAYS.between(firstDatePicker.getValue(), item);
                                    setTooltip(new Tooltip("Range of " + p + " days"));
                                }
                            }
                        };
                    }
                };
        lastDatePicker.setDayCellFactory(dayCellFactory);
        firstDate.bindBidirectional(firstDatePicker.valueProperty());
        lastDate.bindBidirectional(lastDatePicker.valueProperty());
        eventNameField.setPromptText("Event name");

    }

    private void resetFields() {
        folderName.setValue("");
        eventNameField.setText("");
        firstDate.set(null);
        lastDate.set(null);
    }

    @FXML
    private void handleRefreshButtonAction() {
        mediaDirectorySet.readDirectory();
    }

    @FXML
    private void handleApplyEventNameButtonAction() {
        String newFolderName = getFirstDate().format(FolderFormat) + " - " + getLastDate().format(FolderFormat) + ((!"".equals(getEventName())) ? " " + getEventName() : "");
        File newFolder = new File(CommonProperties.getInstance().getToDir() + "\\" + newFolderName);
        if (newFolder.exists()) return;
        final MediaDirectory selectedItem = directoryList.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            if (newFolder.mkdir()) {
                folderService.getFolder(newFolder.toPath());
            }
        } else {
            final Folder folder = folderService.getFolder(selectedItem.getDirectory().toPath());
            try {
                Files.move(folder.getJavaPath(), newFolder.toPath());
                folder.updatePath(newFolder.toPath());
                folderService.mergeFolder(folder);
            } catch (Exception ex) {
                return;
            }
        }
        mediaDirectorySet.readDirectory();
    }

    public SimpleStringProperty getFolderName() {
        return folderName;
    }

    private String getEventName() {
        return eventNameField.getText();
    }

    private LocalDate getFirstDate() {
        return firstDate.get();
    }

    private LocalDate getLastDate() {
        return lastDate.get();
    }
}
