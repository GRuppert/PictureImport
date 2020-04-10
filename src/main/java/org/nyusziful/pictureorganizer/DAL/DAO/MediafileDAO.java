package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;

import java.nio.file.Path;
import java.util.List;

public interface MediafileDAO extends CRUDDAO<MediaFile>{
    public List<MediaFile> getByDriveId(int id);
    public List<MediaFile> getByDriveId(int id, boolean batch);
    public List<MediaFile> getByPath(Drive drive, Path path);
    public List<MediaFile> getByPath(Drive drive, Path path, boolean batch);
    public List<MediaFile> getByPathRec(Drive drive, Path path);
    public List<MediaFile> getByPathRec(Drive drive, Path path, boolean batch);
    public MediaFile getByFile(Drive drive, Path path, boolean withImega);
    public MediaFile getByFile(Drive drive, Path path, boolean withImega, boolean batch);
}
