package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public interface FolderDAO {
    public List<FolderDTO> getFolders();
    public FolderDTO getFolderByName(String name);
    public FolderDTO getFolderById(int id);
    public int saveFolder(FolderDTO folder);
    public boolean deleteFolder(FolderDTO folder);
}
