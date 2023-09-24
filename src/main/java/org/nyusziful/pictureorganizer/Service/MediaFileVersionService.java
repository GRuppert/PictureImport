package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileVersionDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileVersionDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;

import java.util.*;

public class MediaFileVersionService {
    private MediaFileVersionDAO mediaFileVersionDAO;
    private ImageService imageService;
    private static MediaFileVersionService instance;

    private MediaFileVersionService() {
        imageService = new ImageService();
        mediaFileVersionDAO = new MediaFileVersionDAOImplHib();
    }

    public static MediaFileVersionService getInstance() {
        if (instance == null) {
            instance = new MediaFileVersionService();
        }
        return instance;
    }

    public void saveMediaFileVersion(MediaFileVersion mediaFileVersion) {
        saveMediaFileVersion(mediaFileVersion, false);
    }
    public void saveMediaFileVersion(MediaFileVersion mediaFileVersion, boolean batch) {
        saveMediaFileVersions(Collections.singleton(mediaFileVersion), batch);
    }

    public void saveMediaFileVersions(Collection<? extends MediaFileVersion> mediaFileVersions) {
        saveMediaFileVersions(mediaFileVersions, false);
    }
    public void saveMediaFileVersions(Collection<? extends MediaFileVersion> mediaFileVersions, boolean batch) {
        for (MediaFileVersion version : mediaFileVersions) {
            if (version.getId() > -1)
                mediaFileVersionDAO.merge(version, batch);
            else
                mediaFileVersionDAO.persist(version, batch);
        }
    }

    public void flush() {
        mediaFileVersionDAO.flush();
    }

    public void close() {
        mediaFileVersionDAO.close();
    }
    public MediaFileVersion getMediafileVersion(String filehash) {
        return mediaFileVersionDAO.getMediafileVersionByFileHash(filehash);
    }

    public List<MediaFileVersion> getMediafileVersionsByMediaFile(MediaFile mediaFile) {
        return mediaFileVersionDAO.getMediafileVersionsByMediaFile(mediaFile);
    }

    public List<MediaFileVersion> getMediafileVersionsByImageHash(String hash) {
        return mediaFileVersionDAO.getMediafileVersionsByImageHash(hash);
    }

    public List<MediaFileVersion> getMediafileVersionsByParent(MediaFileVersion mediaFileVersion) {
        return mediaFileVersionDAO.getMediafileVersionsByParent(mediaFileVersion);
    }
}