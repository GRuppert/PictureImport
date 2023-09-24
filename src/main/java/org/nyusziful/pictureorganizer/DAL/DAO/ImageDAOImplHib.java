package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            TypedQuery<Image> typedQuery = entityManager.createQuery("SELECT i from Image i WHERE i.hash=:hash AND i.type=:type AND i.valid=1", Image.class);
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

    public List<Image> getInvalid() {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Image> results = new ArrayList<>();
        try{//LEFT JOIN FETCH i.mediaFiles
            TypedQuery<Image> typedQuery = entityManager.createQuery("SELECT i from Image i WHERE i.valid=false AND i.type='mp4'", Image.class);
            results = typedQuery.getResultList();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
        }finally{
            entityManager.close();
        }
        return results;
    }

    public List<Image> getJPGUpdate() {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Image> results = new ArrayList<>();
        try{//LEFT JOIN FETCH i.mediaFiles
            TypedQuery<Image> typedQuery = entityManager.createQuery("SELECT i from Image i WHERE i.type='jpg' AND i.valid = 0", Image.class);
            results = typedQuery.getResultList();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
        }finally{
            entityManager.close();
        }
        return results;
    }

    public void merge(Image root, Image toMerge)   {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            String sqlScript = "UPDATE media_file SET image_id = " + root.getId() + " WHERE image_id = " + toMerge.getId() + ";";
            Query q = entityManager.createNativeQuery(sqlScript);
            q.executeUpdate();
            delete(toMerge);
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
        }finally{
            entityManager.close();
        }
    }

}
