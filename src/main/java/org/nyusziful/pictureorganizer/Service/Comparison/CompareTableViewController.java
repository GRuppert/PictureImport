package org.nyusziful.pictureorganizer.Service.Comparison;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import java.net.URL;
import java.util.ResourceBundle;

public class CompareTableViewController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    @FXML
    private TableColumn<TableViewMediaFile, Boolean > processingCol;

    @FXML
    private TableColumn buttonCol;
    // </editor-foldY

    private ObservableList<Duplicate> input;

    public CompareTableViewController() {
    }


    public void initialize(URL url, ResourceBundle rb) {
        processingCol.setCellValueFactory( f -> f.getValue().processingProperty());
        processingCol.setCellFactory(CheckBoxTableCell.forTableColumn(processingCol));
        buttonCol.setCellValueFactory(new PropertyValueFactory<Duplicate, String>("Meta"));
        buttonCol.setCellFactory(new Callback<TableColumn<Duplicate, String>, TableCell<Duplicate, String>>() {
            @Override
            public TableCell<Duplicate, String> call(TableColumn<Duplicate, String> buttonCol) {
                return new TableCell<Duplicate, String>() {
                    final Button button = new Button(); {
                        button.setMinWidth(130);
                    }
                    @Override
                    public void updateItem(final String object, boolean empty) {
                        super.updateItem(object, empty);
                        int index = this.getIndex();
                        setGraphic(button);
                        if (object != null) {
                            button.setText(object);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent event) {
                                    StackPane pane = new StackPane();
                                    Scene scene = new Scene(pane);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    TableView table = new TableView();
                                    table.setEditable(false);

                                    TableColumn nameCol = new TableColumn("Field");
                                    TableColumn firstCol = new TableColumn("Left Value");
                                    TableColumn secondCol = new TableColumn("Right Value");
                                    nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                                        @Override
                                        public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                            String[] x = p.getValue();
                                            if (x != null && x.length>0) {
                                                return new SimpleStringProperty(x[0]);
                                            } else {
                                                return new SimpleStringProperty("<no name>");
                                            }
                                        }
                                    });
                                    firstCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                                        @Override
                                        public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                            String[] x = p.getValue();
                                            if (x != null && x.length>0) {
                                                return new SimpleStringProperty(x[1]);
                                            } else {
                                                return new SimpleStringProperty("<no name>");
                                            }
                                        }
                                    });
                                    secondCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                                        @Override
                                        public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                                            String[] x = p.getValue();
                                            if (x != null && x.length>0) {
                                                return new SimpleStringProperty(x[2]);
                                            } else {
                                                return new SimpleStringProperty("<no name>");
                                            }
                                        }
                                    });
                                    table.getItems().addAll(input.get(index).getConflicts().toArray());
                                    table.getColumns().addAll(nameCol, firstCol, secondCol);
                                    pane.getChildren().add(table);
                                    stage.show();
                                }
                            });
                        } else {
                            button.setText("");
                        }
                    }
                };
            }
        });
    }

    public ObservableList<Duplicate> getInput() {
        return input;
    }

    public void setInput(ObservableList<Duplicate> input) {
        this.input = input;
    }
}
