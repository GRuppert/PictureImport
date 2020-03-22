package org.nyusziful.pictureorganizer.UI.Model;

import org.nyusziful.pictureorganizer.UI.ProgressLeakingTask;

import java.io.File;
import java.util.Collection;

public abstract class RenamingTask<V> extends ProgressLeakingTask<V> {
    protected Collection<File> directories;

    public RenamingTask(Collection<File> directories) {
        this.directories = directories;
    }


}
