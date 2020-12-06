package org.nyusziful.pictureorganizer.UI.Model;

import javafx.beans.property.SimpleStringProperty;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.Service.MediafileService;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RenameMediaFile extends AbstractTableViewMediaFile {
    protected SimpleStringProperty newName;
    private SimpleStringProperty targetDirectory;

    public RenameMediaFile(MediafileDTO mediafileDTO, String newName, String note, String targetDirectory) {
        super(mediafileDTO, note);
        this.newName = new SimpleStringProperty(newName);
        this.targetDirectory = new SimpleStringProperty(targetDirectory);
    }

    public final String getNewName() {return newName.get();}
    public final void setNewName(String fName) {newName.set(fName);}
    public SimpleStringProperty newNameProperty() {return newName;}

    public boolean write(WriteMethod writeMethod, boolean overwrite) {
        if (processing.get()) {
            MediafileService mediafileService = MediafileService.getInstance();
            return mediafileService.renameMediaFile(getMediafileDTO(), Paths.get(targetDirectory.getValue() + "\\" + newName.get()), writeMethod, overwrite);
        }
        return false;
    }

    public final String getTargetDirectory() {return targetDirectory.get();}
    public final void setTargetDirectory(String targetDirectory) {this.targetDirectory.setValue(targetDirectory);}
    public SimpleStringProperty targetDirectoryProperty() {return targetDirectory;}

}
