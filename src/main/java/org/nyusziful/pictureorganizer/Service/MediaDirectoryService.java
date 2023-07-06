package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaDirectoryDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaDirectoryDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;

import java.util.List;

public class MediaDirectoryService {
    private MediaDirectoryDAO mediaDirectoryDAO;
    private static MediaDirectoryService instance;

    private MediaDirectoryService() {
        mediaDirectoryDAO = new MediaDirectoryDAOImplHib();
    }

    public static MediaDirectoryService getInstance() {
        if (instance == null) {
            instance = new MediaDirectoryService();
        }
        return instance;
    }

    public List<MediaDirectory> getAll() {
        return mediaDirectoryDAO.getAll();
    }

    public MediaDirectory getMediaDirectoryById(int id) {
        return mediaDirectoryDAO.getById(id);
    }
}
