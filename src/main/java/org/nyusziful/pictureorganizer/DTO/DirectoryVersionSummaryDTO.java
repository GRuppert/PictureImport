package org.nyusziful.pictureorganizer.DTO;

import org.jetbrains.annotations.NotNull;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;

import java.util.HashSet;
import java.util.Set;

public class DirectoryVersionSummaryDTO implements Comparable<DirectoryVersionSummaryDTO> {
    private MediaDirectory mediaDirectory;
    private Set<Integer> noCollisionVersionIds = new HashSet<>();
    private Set<Integer> collisionVersionIds = new HashSet<>();

    public DirectoryVersionSummaryDTO(MediaDirectory mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
    }

    public void addBackupVersion(Integer mediaFileVersionId) {
        getCollisionVersionIds().add(mediaFileVersionId);
    }

    public void addNoBackupVersion(Integer mediaFileVersionId) {
        getNoCollisionVersionIds().add(mediaFileVersionId);
    }

    public MediaDirectory getMediaDirectory() {
        return mediaDirectory;
    }

    public Set<Integer> getNoCollisionVersionIds() {
        return noCollisionVersionIds;
    }

    public Set<Integer> getCollisionVersionIds() {
        return collisionVersionIds;
    }

    @Override
    public int compareTo(@NotNull DirectoryVersionSummaryDTO o) {
        if (o.getMediaDirectory() == null) return 1;
        if (getMediaDirectory() == null) return -1;
        return getMediaDirectory().getId() - o.getMediaDirectory().getId();
    }
}
