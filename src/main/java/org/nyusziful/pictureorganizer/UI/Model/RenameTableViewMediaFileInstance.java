package org.nyusziful.pictureorganizer.UI.Model;

import javafx.beans.property.SimpleStringProperty;
import org.nyusziful.pictureorganizer.DTO.MediafileInstanceDTO;
import org.nyusziful.pictureorganizer.Service.MediaFileInstanceService;

import java.nio.file.Paths;

public class RenameTableViewMediaFileInstance extends AbstractTableViewMediaFileInstance {
    protected SimpleStringProperty newName;
    private SimpleStringProperty targetDirectory;

    public RenameTableViewMediaFileInstance(MediafileInstanceDTO mediafileDTO, String newName, String note, String targetDirectory) {
        super(mediafileDTO, note);
        this.newName = new SimpleStringProperty(newName);
        this.targetDirectory = new SimpleStringProperty(targetDirectory);
    }

    public final String getNewName() {return newName.get();}
    public final void setNewName(String fName) {newName.set(fName);}
    public SimpleStringProperty newNameProperty() {return newName;}

    public boolean write(WriteMethod writeMethod, boolean overwrite) {
        if (processing.get()) {
            MediaFileInstanceService mediafileInstanceService = MediaFileInstanceService.getInstance();
            return mediafileInstanceService.renameMediaFileInstance(getMediafileDTO(), Paths.get(targetDirectory.getValue() + "\\" + newName.get()), writeMethod, overwrite);
        }
        return false;
    }

    public final String getTargetDirectory() {return targetDirectory.get();}
    public final void setTargetDirectory(String targetDirectory) {this.targetDirectory.setValue(targetDirectory);}
    public SimpleStringProperty targetDirectoryProperty() {return targetDirectory;}

}
