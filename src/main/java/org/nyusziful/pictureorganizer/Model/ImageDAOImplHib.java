package org.nyusziful.pictureorganizer.Model;

import org.nyusziful.pictureorganizer.DB.AbstractDAO;

import java.util.List;

public class ImageDAOImplHib extends AbstractDAO implements ImageDAO {
    @Override
    public List<ImageDTO> getImages() {
        return (List<ImageDTO>) getCurrentSession().createQuery("from ImageDTO").list();
    }

    @Override
    public ImageDTO getImageByHash(String hash) {
        return (ImageDTO)getCurrentSession().createQuery("SELECT i from ImageDTO i WHERE i.hash='" + hash + "'").list().get(0);
    }

    public ImageDTO save(ImageDTO image) {
        return (ImageDTO)getCurrentSession().save(image);
    }

    public void update(ImageDTO image) {
        getCurrentSession().beginTransaction();
        getCurrentSession().update(image);
        getCurrentSession().getTransaction().commit();
    }
}
