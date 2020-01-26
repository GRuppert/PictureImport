package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.util.List;

public class ImageDAOImplHib extends CRUDDAOImpHib<Image> implements ImageDAO {
    @Override
    public Image getImageByHash(ImageDTO image) {
        final List resultList = hibConnection.getCurrentSession().createQuery("SELECT i from Image i WHERE i.hash='" + image.hash + "' and i.type ='" + image.type + "'").getResultList();
        if (resultList == null || resultList.size() < 1) return null;
        return (Image) resultList.get(0);
    }
}
