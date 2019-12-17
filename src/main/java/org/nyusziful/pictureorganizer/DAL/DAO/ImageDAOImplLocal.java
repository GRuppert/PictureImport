package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;

public class ImageDAOImplLocal extends CRUDDAOImpHib<Image> implements ImageDAO {
    @Override
    public Image getImageByHash(String hash) {
        return null;
    }
}
