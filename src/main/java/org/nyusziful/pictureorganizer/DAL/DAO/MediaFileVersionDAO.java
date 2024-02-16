package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Media;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileVersion;

import java.util.List;

public interface MediaFileVersionDAO extends CRUDDAO<MediaFileVersion> {
    MediaFileVersion getMediafileVersionByFileHash(String filehash);
    MediaFileVersion getMediafileVersionByFileHash(String filehash, boolean batch);
    List<MediaFileVersion> getMediafileVersionsByMediaFile(MediaFile mediaFile);
    List<MediaFileVersion> getMediafileVersionsByMediaFile(MediaFile mediaFile, boolean batch);
    List<MediaFileVersion> getMediafileVersionsByParent(MediaFileVersion mediaFileVersion);
    List<MediaFileVersion> getMediafileVersionsByParent(MediaFileVersion mediaFileVersion, boolean batch);
    MediaFileVersion getOriginalMediafileVersion(MediaFile mediaFile);
    MediaFileVersion getOriginalMediafileVersion(MediaFile mediaFile, boolean batch);
}
