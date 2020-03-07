package org.nyusziful.pictureorganizer.UI.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.Model.AbstractTableViewMediaFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenameMediaFile extends AbstractTableViewMediaFile {
    protected SimpleStringProperty newName;
    private String targetDirectory;

    public RenameMediaFile(MediafileDTO mediafileDTO, String newName, String note, String targetDirectory) {
        super(mediafileDTO, note);
        this.newName = new SimpleStringProperty(newName);
        this.targetDirectory = targetDirectory;
    }

    public final String getNewName() {return newName.get();}
    public final void setNewName(String fName) {newName.set(fName);}
    public SimpleStringProperty newNameProperty() {return newName;}

    public boolean write(WriteMethod writeMethod, boolean overwrite) {
        if (processing.get()) {
//            Paths.get(targetDirectory + "\\" + file.getParentFile().getName() + "\\" + this.getNewName())
            return RenameService.write(Paths.get(mediafileDTO.abolutePath), Paths.get(targetDirectory + "\\" + newName.get()), writeMethod, overwrite);
        }
        return false;
    }

    public String getTargetDirectory() {return targetDirectory;}

    public void setTargetDirectory(String targetDirectory) {this.targetDirectory = targetDirectory;}
}
