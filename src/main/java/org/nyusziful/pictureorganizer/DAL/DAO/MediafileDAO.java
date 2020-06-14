package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;

import java.nio.file.Path;
import java.util.List;

public interface MediafileDAO extends CRUDDAO<MediaFile>{
    List<MediaFile> getByDriveId(int id);
    List<MediaFile> getByDriveId(int id, boolean batch);
    List<MediaFile> getByPath(Drive drive, Path path);
    List<MediaFile> getByPath(Drive drive, Path path, boolean batch);
    List<MediaFile> getByPathRec(Drive drive, Path path);
    List<MediaFile> getByPathRec(Drive drive, Path path, boolean batch);
    List<MediaFile> getMediaFilesFromPathOfImage(Image image, Drive drive, Path target);
    List<MediaFile> getMediaFilesFromPathOfImage(Image image, Drive drive, Path target, boolean batch);
    MediaFile getByFile(Drive drive, Path path, boolean withImega);
    MediaFile getByFile(Drive drive, Path path, boolean withImega, boolean batch);

}
