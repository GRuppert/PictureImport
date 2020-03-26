package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;

import java.nio.file.Path;

public interface FolderDAO extends CRUDDAO<Folder> {
    public Folder getFolderByPath(Drive drive, Path path);
    public Folder getFolderByPath(Drive drive, Path path, boolean batch);
}
