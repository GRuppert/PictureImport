package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;

import java.util.List;

public interface MediaFileDAO extends CRUDDAO<MediaFile> {
    List<MediaFile> getMediaFileByMediaDirectory(MediaDirectory mediaDirectory);
    List<MediaFile> getMediaFileByMediaDirectory(MediaDirectory mediaDirectory, boolean batch);
}
