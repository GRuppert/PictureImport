package org.nyusziful.Main;

import javafx.collections.ObservableList;
import org.nyusziful.Rename.metaProp;

import java.net.URL;
import java.util.ResourceBundle;

public class Meta2TableViewController {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    // </editor-fold>

    private ObservableList<metaProp> meta;

    public Meta2TableViewController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    public ObservableList<metaProp> getMeta() {
        return meta;
    }

    public void setMeta(ObservableList<metaProp> meta) {
        this.meta = meta;
    }
}
