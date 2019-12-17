package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.nyusziful.pictureorganizer.Service.Rename.MetaProp;

import java.net.URL;
import java.util.ResourceBundle;

public class Meta2TableViewController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML variables">
    // </editor-fold>

    private ObservableList<MetaProp> meta;

    public Meta2TableViewController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public ObservableList<MetaProp> getMeta() {
        return meta;
    }

    public void setMeta(ObservableList<MetaProp> meta) {
        this.meta = meta;
    }
}
