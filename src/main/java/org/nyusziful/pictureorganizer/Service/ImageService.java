package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.ImageDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.ImageDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.util.Collection;
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
        return imageDAO.getImageByHash(image);
    }

    public ImageDTO getImageDTO(Image image) {
        throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    public void persistImage(Image image) {
        imageDAO.persist(image);
    }

    public void persistImage(Collection<Image> images) {
        for (Image image: images) {
            if (image.getId() > -1)
                imageDAO.merge(image);
            else
                imageDAO.persist(image);
        }
    }

    public void updateImage(Image image) {
        imageDAO.merge(image);
    }

    public void deleteImage(Image image) {
        imageDAO.delete(image);
    }

    public void flush() {
        imageDAO.flush();
    }

}
