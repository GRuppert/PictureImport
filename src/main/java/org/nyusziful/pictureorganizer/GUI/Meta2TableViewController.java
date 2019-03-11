package org.nyusziful.pictureorganizer.GUI;

import javafx.collections.ObservableList;
import org.nyusziful.pictureorganizer.Rename.MetaProp;

import java.net.URL;
import java.util.ResourceBundle;

public class Meta2TableViewController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    // </editor-fold>

    private ObservableList<MetaProp> meta;

    public Meta2TableViewController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    public ObservableList<MetaProp> getMeta() {
        return meta;
    }

    public void setMeta(ObservableList<MetaProp> meta) {
        this.meta = meta;
    }
}
