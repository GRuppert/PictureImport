package org.nyusziful.pictureorganizer.UI;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.nyusziful.pictureorganizer.Service.MediafileService;
import org.nyusziful.pictureorganizer.UI.Model.RenameMediaFile;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ProgressLeakingTask<V> extends Task<V> {
    public void updateProgress(int workDone, int max) {
        updateProgress((double)workDone, (double)max);
    }

    protected void createNewName(Set<RenameMediaFile> renameMediaFiles) {
        MediafileService mediafileService = new MediafileService();
        for (RenameMediaFile renameMediaFile : renameMediaFiles) {
            final String newName = mediafileService.getMediaFileName(renameMediaFile.getMediafileDTO(), "6");
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    renameMediaFile.setNewName(newName);
                }
            });
        }
    }


}
