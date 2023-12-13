package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaDirectoryDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaDirectoryDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;

import java.util.Collection;
import java.util.Collections;
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

    public void saveMediaDirectory(MediaDirectory mediaDirectory) {
        saveMediaDirectory(mediaDirectory, false);
    }

    public void saveMediaDirectory(MediaDirectory mediaDirectory, boolean batch) {
        saveMediaDirectory(Collections.singleton(mediaDirectory), batch);
    }

    public void persistMediaDirectory(MediaDirectory mediaDirectory) {
        mediaDirectoryDAO.persist(mediaDirectory);
    }

    public void saveMediaDirectory(Collection<MediaDirectory> mediaDirectorys, boolean batch) {
        for (MediaDirectory mediaDirectory: mediaDirectorys) {
            if (mediaDirectory.getId() > -1)
                mediaDirectoryDAO.merge(mediaDirectory, batch);
            else
                mediaDirectoryDAO.persist(mediaDirectory, batch);
        }
    }

    public void updateMediaDirectory(MediaDirectory mediaDirectory) {
        mediaDirectoryDAO.merge(mediaDirectory);
    }

    public void deleteMediaDirectory(MediaDirectory mediaDirectory) {
        mediaDirectoryDAO.delete(mediaDirectory);
    }

    public void flush() {
        mediaDirectoryDAO.flush();
    }
    
}
