package org.nyusziful.pictureorganizer.UI;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.nyusziful.pictureorganizer.Service.MediaFileInstanceService;
import org.nyusziful.pictureorganizer.UI.Model.RenameTableViewMediaFileInstance;

import java.util.Set;

public abstract class ProgressLeakingTask<V> extends Task<V> {
    public void updateProgress(int workDone, int max) {
        updateProgress((double)workDone, (double)max);
    }

    protected void createNewName(Set<RenameTableViewMediaFileInstance> renameMediaFiles) {
        MediaFileInstanceService mediafileInstanceService = MediaFileInstanceService.getInstance();
        for (RenameTableViewMediaFileInstance renameMediaFile : renameMediaFiles) {
            final String newName = mediafileInstanceService.getMediaFileName(renameMediaFile.getMediafileDTO(), "6");
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    renameMediaFile.setNewName(newName);
                }
            });
        }
    }


}
