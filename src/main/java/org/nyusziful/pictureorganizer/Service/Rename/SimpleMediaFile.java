package org.nyusziful.pictureorganizer.Service.Rename;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.nyusziful.pictureorganizer.UI.Model.AbstractTableViewMediaFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleMediaFile extends AbstractTableViewMediaFile {
    private Path path;
    private Path newPath;
    private String targetDirectory;

    public SimpleMediaFile(Path path, Path newPath) {
        this.path = path;
        this.newPath = newPath;
        processing = new SimpleBooleanProperty(true);
        currentName = new SimpleStringProperty(path.getFileName().toString());
        newName = new SimpleStringProperty(newPath.getFileName().toString());
        xmpMissing = new SimpleBooleanProperty(false);
        note = new SimpleStringProperty("");
    }

    private void validPath(Path path) throws IOException {
        if (Files.notExists(path.getParent())) {
            validPath(path.getParent());
            Files.createDirectory(path.getParent());
        }
    }

    public boolean write(WriteMethod writeMethod) {
        if (processing.get()) {
            try {
                validPath(this.newPath.getParent());
                switch (writeMethod) {
                    case COPY:
                        Files.copy(this.path, this.newPath);
                        break;
                    case MOVE:
                        Files.move(this.path, this.newPath);
                        break;
                }
                return true;
            } catch (IOException e) {
                System.out.println(e);
                //Todo logging, All for error OK
//                errorOut(this.getNewName(), e);
            }
        }
        return false;
    }

    public String getTargetDirectory() {return targetDirectory;}

    public void setTargetDirectory(String targetDirectory) {this.targetDirectory = targetDirectory;}
}
