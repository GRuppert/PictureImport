package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

public class ImageDAOImplLocal extends CRUDDAOImpHib<Image> implements ImageDAO {
    @Override
    public Image getImageByHash(ImageDTO image) {
        return null;
    }
}
