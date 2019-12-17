package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.HibConnection;
import org.nyusziful.pictureorganizer.DAL.DAO.ImageDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.ImageDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;

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

    public Image getImage(String hash, Mediafile requester) {
        Image getImage = imageDAO.getImageByHash(hash);
        if (getImage == null) {
            getImage = new Image(hash, requester.getFilename(), requester.getType());
        }
        return getImage;
    }

    public Image saveImage(Image image) throws Exception {
        Image getImage = imageDAO.save(image);
        return getImage;
    }

    public void updateImage(Image image) throws Exception {
        imageDAO.merge(image);
    }

    public static void main(String[] args) {
        final ImageService imageService = new ImageService();
        Image image = imageService.getImage("001ccb41c7eb77075051f3febdcafe71", new Mediafile());
        System.out.println(image);
        image.setOringinalFilename("test2");
        try {
            imageService.updateImage(image);
            HibConnection.getInstance().commit();
        } catch (Exception e) {
            HibConnection.getInstance().rollback();
            e.printStackTrace();
        }
    }

}
