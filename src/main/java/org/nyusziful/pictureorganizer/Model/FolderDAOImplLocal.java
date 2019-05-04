package org.nyusziful.pictureorganizer.Model;


import java.util.List;

public class FolderDAOImplLocal implements FolderDAO {
    @Override
    public List<FolderDTO> getFolders() {
        return null;
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
