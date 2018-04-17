package Rename;


import static Main.PicOrganizes.ExifDateFormatTZ;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class metaProp {
    private final SimpleStringProperty originalFilename;
    private final SimpleStringProperty date;
    private final SimpleStringProperty model;
    private final SimpleBooleanProperty dateFormat;
    
    
    public metaProp(meta metaIn) {
        originalFilename = new SimpleStringProperty(metaIn.originalFilename == null ? "" : metaIn.originalFilename);
        date = new SimpleStringProperty(metaIn.date == null ? "" : metaIn.date.format(ExifDateFormatTZ));
        dateFormat = new SimpleBooleanProperty(metaIn.dateFormat == null ? false : metaIn.dateFormat);
        model = new SimpleStringProperty(metaIn.model == null ? "" : metaIn.model);
    }
    
    public final Boolean getDateFormat() {return dateFormat.get();}
    public final void setDateFormat(Boolean proc) {dateFormat.set(proc);}
    public SimpleBooleanProperty dateFormatProperty() {return dateFormat;}

    public final String getOriginalFilename() {return originalFilename.get();}
    public final void setOriginalFilename(String proc) {originalFilename.set(proc);}
    public SimpleStringProperty originalFilenameProperty() {return originalFilename;}
    
    public final String getDate() {return date.get();}
    public final void setDate(String proc) {date.set(proc);}
    public SimpleStringProperty dateProperty() {return date;}
    
    public final String getModel() {return model.get();}
    public final void setModel(String proc) {model.set(proc);}
    public SimpleStringProperty modelProperty() {return model;}
}
