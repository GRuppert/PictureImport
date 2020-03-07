package org.nyusziful.pictureorganizer.UI.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public interface TableViewMediaFile {
    public enum WriteMethod {
        MOVE, COPY
    }
    // <editor-fold defaultstate="collapsed" desc="Static variables">
//    public static int MOVE = 1;
//    public static int COPY = 0;
    // </editor-fold>

    public String getCurrentName();
    public void setCurrentName(String fName);
    public SimpleStringProperty currentNameProperty();

    public Boolean getProcessing();
    public void setProcessing(Boolean proc);
    public SimpleBooleanProperty processingProperty();

    public Boolean getXmpMissing();
    public void setXmpMissing(Boolean xmp);
    public SimpleBooleanProperty xmpMissingProperty();

    public String getNote();
    public void setNote(String fName);
    public SimpleStringProperty noteProperty();

    public boolean write(WriteMethod writeMethod, boolean overwrite);
}
