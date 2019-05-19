package org.nyusziful.pictureorganizer.Model;

import org.nyusziful.pictureorganizer.DB.AbstractDAO;
import org.nyusziful.pictureorganizer.DB.HibConnection;

import java.util.List;

public class ImageDAOImplHib extends CRUDDAOImpHib<ImageDTO> implements ImageDAO {
    @Override
    public ImageDTO getImageByHash(String hash) {
        return (ImageDTO) hibConnection.getCurrentSession().createQuery("SELECT i from ImageDTO i WHERE i.hash='" + hash + "'").list().get(0);
    }
}
