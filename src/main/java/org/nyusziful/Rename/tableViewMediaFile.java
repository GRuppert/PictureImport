package org.nyusziful.Rename;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public interface tableViewMediaFile {
    public enum WriteMethod {
        MOVE, COPY
    }
    // <editor-fold defaultstate="collapsed" desc="Static variables">
//    public static int MOVE = 1;
//    public static int COPY = 0;
    // </editor-fold>

    public String getNewName();
    public void setNewName(String fName);
    public SimpleStringProperty newNameProperty();

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

    public String getTargetDirectory();
    public void setTargetDirectory(String targetDirectory);

    public boolean write(WriteMethod writeMethod);


}
