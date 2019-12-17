package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;

import java.util.List;

public interface MediafileDAO extends CRUDDAO<Mediafile> {
    public Mediafile getById(int id);
    public List<Mediafile> getByDriveId(int id);


}
