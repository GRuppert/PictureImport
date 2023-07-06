package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.*;

import java.nio.file.Path;
import java.util.List;

public interface MediaFileInstanceDAO extends CRUDDAO<MediaFileInstance>{
    List<MediaFileInstance> getByDriveId(int id);
    List<MediaFileInstance> getByDriveId(int id, boolean batch);
    List<MediaFileInstance> getByPath(Drive drive, Path path);
    List<MediaFileInstance> getByPath(Drive drive, Path path, boolean batch);
    List<MediaFileInstance> getByPathRec(Drive drive, Path path);
    List<MediaFileInstance> getByPathRec(Drive drive, Path path, boolean batch);
    List<MediaFileInstance> getMediaFilesFromPathOfImage(Image image, Drive drive, Path target);
    List<MediaFileInstance> getMediaFilesFromPathOfImage(Image image, Drive drive, Path target, boolean batch);
    List<MediaFileInstance> getByVersion(MediaFileVersion mediaFileVersion);
    List<MediaFileInstance> getByVersion(MediaFileVersion mediaFileVersion, boolean batch);
    MediaFileInstance getByFile(Drive drive, Path path);
    MediaFileInstance getByFile(Drive drive, Path path, boolean batch);
    List<MediaFileInstance> getByMediaFile(MediaFile mediaFile);
    List<MediaFileInstance> getByMediaFile(MediaFile mediaFile, boolean batch);
}
