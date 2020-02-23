package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;

import java.nio.file.Path;
import java.util.List;

public interface MediafileDAO<T> extends CRUDDAO<T>{
    public List<MediaFile> getByDriveId(int id);
    public List<MediaFile> getByPath(Drive drive, Path path);
    public MediaFile getByFile(Drive drive, Path path);
}
