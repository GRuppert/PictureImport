package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;

import java.util.List;

public interface MediafileDAO extends CRUDDAO<Mediafile> {
    public Mediafile getById(int id);
    public List<Mediafile> getByDriveId(int id);
    public List<Mediafile> getByPath(MediafileDTO mediafileDTO);
    public Mediafile getByFile(MediafileDTO mediafileDTO);
}
