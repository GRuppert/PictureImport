package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DB.HibConnection;
import org.nyusziful.pictureorganizer.Model.ImageDAO;
import org.nyusziful.pictureorganizer.Model.ImageDAOImplHib;
import org.nyusziful.pictureorganizer.Model.ImageDTO;

import javax.transaction.Transactional;
import java.util.List;

public class ImageService {
    private static ImageDAO imageDAO;

    public ImageService() {
        imageDAO = new ImageDAOImplHib();
    }

    public List<ImageDTO> getImages() {
        List<ImageDTO> getImages = imageDAO.getAll();
        return getImages;
    }

    public ImageDTO getImage(String hash) {
        ImageDTO getImage = imageDAO.getImageByHash(hash);
        return getImage;
    }

    public ImageDTO saveImage(ImageDTO image) throws Exception {
        ImageDTO getImage = imageDAO.save(image);
        return getImage;
    }

    public void updateImage(ImageDTO image) throws Exception {
        imageDAO.merge(image);
    }

    public static void main(String[] args) {
        final ImageService imageService = new ImageService();
        ImageDTO image = imageService.getImage("001ccb41c7eb77075051f3febdcafe71");
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
