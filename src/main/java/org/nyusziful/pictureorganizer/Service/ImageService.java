package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.HibConnection;
import org.nyusziful.pictureorganizer.DAL.DAO.ImageDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.ImageDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.util.List;

public class ImageService {
    private static ImageDAO imageDAO;

    public ImageService() {
        imageDAO = new ImageDAOImplHib();
    }

    public List<Image> getImages() {
        List<Image> getImages = imageDAO.getAll();
        return getImages;
    }

    public Image getImage(ImageDTO image) {
        Image getImage = imageDAO.getImageByHash(image);
        return getImage;
    }

    public ImageDTO getImageDTO(Image image) {
        throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    public Image saveImage(Image image) throws Exception {
        Image getImage = imageDAO.save(image);
        return getImage;
    }

    public void updateImage(Image image) throws Exception {
        imageDAO.merge(image);
    }
}
