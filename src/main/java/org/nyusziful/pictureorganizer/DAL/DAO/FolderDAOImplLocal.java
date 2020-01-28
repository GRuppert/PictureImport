package org.nyusziful.pictureorganizer.DAL.DAO;


import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;

public class FolderDAOImplLocal extends CRUDDAOImpHib<Folder> implements FolderDAO {
    @Override
    public Folder getFolderByPath(FolderDTO image) {
        return null;
    }
}
