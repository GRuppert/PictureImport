package org.nyusziful.pictureorganizer.UI;

import javafx.concurrent.Task;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public abstract class ProgressLeakingTask<V> extends Task<V> {
    protected Collection<File> directories;

    public ProgressLeakingTask(Collection<File> directories) {
        this.directories = directories;
    }


    public void updateProgress(int workDone, int max) {
        updateProgress((double)workDone, (double)max);
    }
}
