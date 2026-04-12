package org.nyusziful.pictureorganizer.UI.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.nyusziful.pictureorganizer.DTO.MediafileInstanceDTO;

public abstract class AbstractTableViewMediaFileInstance implements TableViewMediaFileInstance {
    protected SimpleBooleanProperty processing;
    protected SimpleStringProperty currentName;
    protected SimpleStringProperty note;
    protected SimpleBooleanProperty xmpMissing;
    private MediafileInstanceDTO mediafileDTO;


    public AbstractTableViewMediaFileInstance(MediafileInstanceDTO mediafileDTO, String note) {
        this.processing = new SimpleBooleanProperty(true);
        this.currentName = new SimpleStringProperty(mediafileDTO.filename);
        this.note = new SimpleStringProperty(note);
        this.xmpMissing = new SimpleBooleanProperty(mediafileDTO.hasXMP);
        this.mediafileDTO = mediafileDTO;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter section">
    public final String getCurrentName() {return currentName.get();}
    public final void setCurrentName(String fName) {currentName.set(fName);}
    public SimpleStringProperty currentNameProperty() {return currentName;}

    public final Boolean getProcessing() {return processing.get();}
    public final void setProcessing(Boolean proc) {processing.set(proc);}
    public SimpleBooleanProperty processingProperty() {return processing;}

    public final Boolean getXmpMissing() {return xmpMissing.get();}
    public final void setXmpMissing(Boolean xmp) {xmpMissing.set(xmp);}
    public SimpleBooleanProperty xmpMissingProperty() {return xmpMissing;}

    public final String getNote() {return note.get();}
    public final void setNote(String fName) {note.set(fName);}
    public SimpleStringProperty noteProperty() {return note;}

    public MediafileInstanceDTO getMediafileDTO() {
        return mediafileDTO;
    }
    // </editor-fold>
}
