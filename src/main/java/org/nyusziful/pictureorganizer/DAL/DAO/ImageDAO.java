package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.util.List;

public interface ImageDAO extends CRUDDAO<Image> {
    public Image getImageByHash(ImageDTO image);
}
