package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ImageDAOImplHib extends CRUDDAOImpHib<Image> implements ImageDAO {
    @Override
    public Image getImageByHash(ImageDTO image) {
//        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Image> typedQuery = entityManager.createQuery("SELECT i from Image i WHERE i.hash=:hash and i.type =:type", Image.class);
        typedQuery.setParameter("hash", image.hash);
        typedQuery.setParameter("type", image.type);
        List<Image> results = typedQuery.getResultList();
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;

    }
}
