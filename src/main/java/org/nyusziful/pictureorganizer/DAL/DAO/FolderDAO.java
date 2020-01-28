package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;

public interface FolderDAO extends CRUDDAO<Folder> {
    public Folder getFolderByPath(FolderDTO image);

}
