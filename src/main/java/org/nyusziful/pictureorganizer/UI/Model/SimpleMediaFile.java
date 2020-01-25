package org.nyusziful.pictureorganizer.UI.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.Model.AbstractTableViewMediaFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleMediaFile extends AbstractTableViewMediaFile {
    private Path path;
    private Path newPath;
    private String targetDirectory;

    public SimpleMediaFile() {

    }

    public SimpleMediaFile(Path path, Path newPath) {
        this.path = path;
        this.newPath = newPath;
        processing = new SimpleBooleanProperty(true);
        currentName = new SimpleStringProperty(path.getFileName().toString());
        newName = new SimpleStringProperty(newPath.getFileName().toString());
        xmpMissing = new SimpleBooleanProperty(false);
        note = new SimpleStringProperty("");
    }

    public boolean write(WriteMethod writeMethod) {
        if (processing.get()) {
            return RenameService.write(path, newPath, writeMethod);
        }
    }

    public String getTargetDirectory() {return targetDirectory;}

    public void setTargetDirectory(String targetDirectory) {this.targetDirectory = targetDirectory;}
}
