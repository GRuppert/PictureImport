package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.ResourceBundle;

public class TablePanelController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private ProgressIndicator tableProgressIndicator;

    @FXML
    private Button btnGo;

    @FXML
    private Label targetFolderName;

    @FXML
    private TextField eventNameField;

    @FXML
    private DatePicker firstDatePicker;

    @FXML
    private DatePicker lastDatePicker;

    // </editor-fold>

    private MediaFileSet mediaFileSet;

    public TablePanelController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        firstDatePicker.getValue().plusDays(1))
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                                long p = ChronoUnit.DAYS.between(
                                        firstDatePicker.getValue(), item
                                );
                                setTooltip(new Tooltip("Range of " + p + " days"));
                            }
                        };
                    }
                };
        lastDatePicker.setDayCellFactory(dayCellFactory);
    }

    public void setMediaFileSet(MediaFileSet mediaFileSet) {
        this.mediaFileSet = mediaFileSet;
        mediaFileSet.folderNameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                targetFolderName.setText(newValue != null ? newValue : "");
            }
        });
//        mediaFileSet.firstDateProperty().addListener((observable, oldValue, newValue) -> firstDatePicker.setValue(newValue));
        mediaFileSet.firstDateProperty().bindBidirectional(firstDatePicker.valueProperty());
        mediaFileSet.lastDateProperty().bindBidirectional(lastDatePicker.valueProperty());

        eventNameField.setPromptText("Event name");
    }

    @FXML
    private void handleAllButtonAction() {
        mediaFileSet.selectAll();
    }

    @FXML
    private void handleNoneButtonAction() {
        mediaFileSet.selectNone();
    }

    @FXML
    private void handleInvertButtonAction() {
        mediaFileSet.invertSelection();
    }

    @FXML
    private void handleGoButtonAction() {
        btnGo.setDisable(true);
        tableProgressIndicator.setVisible(true);
        Task<Collection<TableViewMediaFile>> collectionTask = mediaFileSet.applyChanges(CommonProperties.getInstance().getCopyOrMove(), CommonProperties.getInstance().isOverwrite());
        tableProgressIndicator.progressProperty().bind(collectionTask.progressProperty());
        new Thread(collectionTask).start();
        btnGo.setDisable(false);
    }

    @FXML
    private void handleApplyEventNameButtonAction() {
        mediaFileSet.setLabel(eventNameField.getText());
        updateTargetDirectory();
    }

    @FXML
    private void handleResetDatesButtonAction() {
        mediaFileSet.resetDates();
        updateTargetDirectory();
    }



    @FXML
    private void handleAbortButtonAction() { mediaFileSet.reset(); }

    @FXML
    private void handleRefreshButtonAction() {updateTargetDirectory();}

    @FXML
    private void firstDatePickerAction() {mediaFileSet.setFirstDate(firstDatePicker.getValue());}

    @FXML
    private void lastDatePickerAction() {mediaFileSet.setLastDate(lastDatePicker.getValue());}

    private void updateTargetDirectory() {
        mediaFileSet.updatePaths(CommonProperties.getInstance().getToDir().toString());
    }


}
