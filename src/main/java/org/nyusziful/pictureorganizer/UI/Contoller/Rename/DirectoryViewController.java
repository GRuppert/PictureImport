package org.nyusziful.pictureorganizer.UI.Contoller.Rename;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Model.MediaDirectory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static org.nyusziful.pictureorganizer.UI.StaticTools.defaultImportDirectories;

public class DirectoryViewController implements Initializable {
    @FXML
//    private ListView<MediaDirectory> directoryList;
    private ListView<Book> directoryList;
    private MediaDirectorySet mediaDirectorySet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediaDirectorySet = new MediaDirectorySet();
        directoryList = new ListView<>();
        Book book1 = new Book(1L, "J01", "Java IO Tutorial");
        Book book2 = new Book(2L, "J02", "Java Enums Tutorial");
        Book book3 = new Book(2L, "C01", "C# Tutorial for Beginners");

        // To Creating a Observable List
        ObservableList<Book> books = FXCollections.observableArrayList(book1, book2, book3);

        // Create a ListView
        ListView<Book> directoryList = new ListView<Book>(books);
 /*
        directoryList.setCellFactory(param -> new ListCell<MediaDirectory>() {
            @Override
            protected void updateItem(MediaDirectory item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.toString() == null) {
                    setText(null);
                } else {
//                    if (item.isConflicting()) setTextFill(isSelected() ? Color.ORANGE : Color.PINK);
                    setText(item.toString());
                }
            }
        });
        directoryList.setItems(mediaDirectorySet.getDataModel());
        */
    }

    @FXML
    private void handleRefreshButtonAction() {
        mediaDirectorySet.readDirectory();
    }


    public class Book {

        private Long id;
        private String code;
        private String name;

        public Book(Long id, String code, String name) {
            this.id = id;
            this.code = code;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

}
