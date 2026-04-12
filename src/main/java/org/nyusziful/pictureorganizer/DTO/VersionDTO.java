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
    private Set<MediaFileVersion> specificVersions = new HashSet<>(); //Versions which exist only in these folders

    public VersionDTO(Set<Folder> folders) {
        this.folders = folders;
    }

    public VersionDTO getParent() {
        return parent;
    }

    public void setParent(VersionDTO parent) {
        this.parent = parent;
        parent.getChildren().add(this);
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public HashMap<Integer, Set<Integer>> getVersions() {
        return versions;
    }

    @Override
    public String toString() {
        return  getLevel() + ") " + folders.size() + " Folders " + specificVersions.size() + " new Files from  (" + versions.size() + ")";
    }

    private int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public Collection<MediaFileVersion> getSpecificVersions() {
        return specificVersions;
    }

    public Set<VersionDTO> getChildren() {
        return children;
    }
}
