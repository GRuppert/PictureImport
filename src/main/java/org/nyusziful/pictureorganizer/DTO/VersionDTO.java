package org.nyusziful.pictureorganizer.DTO;

import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileVersion;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VersionDTO {
    private VersionDTO parent = null;
    private Set<VersionDTO> children = new HashSet<>();
    private Set<Folder> folders;
    private HashMap<Integer, Set<Integer>> versions = new HashMap<>();
    private Set<MediaFileVersion> newVersions = new HashSet<>();

    public VersionDTO(Set<Folder> folders) {
        this.folders = folders;
    }

    public VersionDTO getParent() {
        return parent;
    }

    public void setParent(VersionDTO parent) {
        this.parent = parent;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public HashMap<Integer, Set<Integer>> getVersions() {
        return versions;
    }

    @Override
    public String toString() {
        return  folders.size() + " Folders " + newVersions.size() + " new Files from  (" + versions.size() + ")";
    }

    public Collection<MediaFileVersion> getNewVersions() {
        return newVersions;
    }
}
