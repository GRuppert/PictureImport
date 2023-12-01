package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileVersionDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileVersionDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.DTO.Meta;

import java.util.*;

import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

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

    public MediaFileVersion createMediaFileVersion(String name, Meta meta, String fullhash, long fileSize, MediaFile mediaFile, MediaFileVersion parent) {
        if (supportedRAWFileType(name)) {
            return new RAWMediaFileVersion(fullhash, (RAWMediaFileVersion) parent, fileSize, (RAWMediaFile)mediaFile, meta.date);
        } else if (supportedJPGFileType(name)) {
            return new JPGMediaFileVersion(fullhash, (JPGMediaFileVersion) parent, fileSize, (JPGMediaFile)mediaFile, meta.date);
        } else if (supportedVideoFileType(name)) {
            return new VideoMediaFileVersion(fullhash, (VideoMediaFileVersion) parent, fileSize, (VideoMediaFile)mediaFile, meta.date);
        } else {
            return new MediaFileVersion(fullhash, parent, fileSize, mediaFile, meta.date);
        }
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

    public List<MediaFileVersion> getMediafileVersionsByParent(MediaFileVersion mediaFileVersion) {
        return mediaFileVersionDAO.getMediafileVersionsByParent(mediaFileVersion);
    }
}