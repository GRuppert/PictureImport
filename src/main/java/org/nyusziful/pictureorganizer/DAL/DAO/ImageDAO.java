package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;

public interface ImageDAO extends CRUDDAO<Image> {
    public Image getImageByHash(String hash);
}
