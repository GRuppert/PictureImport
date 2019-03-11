package org.nyusziful.pictureorganizer.Rename;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleMediaFile implements TableViewMediaFile {
    private final SimpleBooleanProperty processing;
    private final SimpleStringProperty currentName;
    private final SimpleStringProperty newName;
    private final SimpleStringProperty note;
    private final SimpleBooleanProperty xmpMissing;
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


    public final String getNewName() {return newName.get();}
    public final void setNewName(String fName) {newName.set(fName);}
    public SimpleStringProperty newNameProperty() {return newName;}

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

    public String getTargetDirectory() {return targetDirectory;}

    public void setTargetDirectory(String targetDirectory) {this.targetDirectory = targetDirectory;}
}
