package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.util.List;
import java.util.Set;

public interface ImageDAO extends CRUDDAO<Image> {
    public Image getImageByHash(ImageDTO imageDTO);
    public Image getImageByHash(ImageDTO imageDTO, boolean batch);
}
