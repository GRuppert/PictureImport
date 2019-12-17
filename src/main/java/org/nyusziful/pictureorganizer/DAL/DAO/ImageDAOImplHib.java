package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;

public class ImageDAOImplHib extends CRUDDAOImpHib<Image> implements ImageDAO {
    @Override
    public Image getImageByHash(String hash) {
        return (Image) hibConnection.getCurrentSession().createQuery("SELECT i from Image i WHERE i.hash='" + hash + "'").list().get(0);
    }
}
