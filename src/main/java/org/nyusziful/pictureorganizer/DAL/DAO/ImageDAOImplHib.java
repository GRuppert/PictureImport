package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ImageDAOImplHib extends CRUDDAOImpHib<Image> implements ImageDAO {
    @Override
    public Image getImageByHash(ImageDTO image) {
        return getImageByHash(image, false);
    }

    @Override
    public Image getImageByHash(ImageDTO image, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Image> results = new ArrayList<>();
        try{//LEFT JOIN FETCH i.mediaFiles
            TypedQuery<Image> typedQuery = entityManager.createQuery("SELECT DISTINCT i from Image i LEFT JOIN FETCH i.mediaFiles WHERE i.hash=:hash AND i.type=:type", Image.class);
            typedQuery.setParameter("hash", image.hash);
            typedQuery.setParameter("type", image.type);
            results = typedQuery.getResultList();
            if (!batch) transaction.commit();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;

        }finally{
            if(entityManager!=null && !batch){
                entityManager.close();
            }
        }
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;
    }
}
