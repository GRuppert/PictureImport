package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;

import java.nio.file.Path;
import java.util.List;

public interface MediafileDAO extends CRUDDAO<Mediafile> {
    public Mediafile getById(int id);
    public List<Mediafile> getByDriveId(int id);
    public List<Mediafile> getByPath(Drive drive, Path path);
    public Mediafile getByFile(Drive drive, Path path);
}
