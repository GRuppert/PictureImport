package org.nyusziful.pictureorganizer.Model;

import org.nyusziful.pictureorganizer.DB.AbstractDAO;

import java.util.List;

public class FolderDAOImplHib extends AbstractDAO implements FolderDAO {
    @Override
    public List<FolderDTO> getFolders() {
        return (List<FolderDTO>) getCurrentSession().createQuery("from FolderDTO").list();
    }

    @Override
    public FolderDTO getFolderByName(String name) {
        return null;
    }

    @Override
    public FolderDTO getFolderById(int id) {
        return null;
    }

    @Override
    public int saveFolder(FolderDTO folder) {
        return 0;
    }

    @Override
    public boolean deleteFolder(FolderDTO folder) {
        return false;
    }
}
