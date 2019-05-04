package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DB.HibConnection;
import org.nyusziful.pictureorganizer.Model.ImageDAO;
import org.nyusziful.pictureorganizer.Model.ImageDAOImplHib;
import org.nyusziful.pictureorganizer.Model.ImageDTO;

import java.util.List;

public class ImageService {
    private static ImageDAO imageDAO;

    public ImageService() {
        imageDAO = new ImageDAOImplHib();
    }

    public List<ImageDTO> getImages() {
        openConnection();
        List<ImageDTO> getImages = imageDAO.getImages();
        closeConnection();
        return getImages;
    }

    public ImageDTO getImage(String hash) {
        openConnection();
        ImageDTO getImage = imageDAO.getImageByHash(hash);
        closeConnection();
        return getImage;
    }

    public ImageDTO saveImage(ImageDTO image) {
        openConnection();
        ImageDTO getImage = imageDAO.save(image);
        closeConnection();
        return getImage;
    }

    public void updateImage(ImageDTO image) {
        openConnection();
        imageDAO.update(image);
        closeConnection();
    }

    private void openConnection() {
        if (((ImageDAOImplHib)imageDAO).getCurrentSession() == null || !((ImageDAOImplHib)imageDAO).getCurrentSession().isOpen())
            ((ImageDAOImplHib)imageDAO).setCurrentSession(HibConnection.getCurrentSession());
//        ((ImageDAOImplHib)ImageDAO).getCurrentSession().beginTransaction();

    }

    private void closeConnection() {
//        ((ImageDAOImplHib)ImageDAO).getCurrentSession().getTransaction().commit();
        HibConnection.closeSession();
    }

    public static void main(String[] args) {
        final ImageService ImageService = new ImageService();
        ImageDTO image = ImageService.getImage("001ccb41c7eb77075051f3febdcafe71");
        System.out.println(image);
        image.setOringinalFilename("test");
        ImageService.updateImage(image);
    }

}
