package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileInstance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MediaFileDAOImplHib extends CRUDDAOImpHib<MediaFile> implements MediaFileDAO {

    @Override
    public List<MediaFile> getMediaFileByMediaDirectory(MediaDirectory mediaDirectory) {
        return getMediaFileByMediaDirectory(mediaDirectory, false);
    }
    @Override
    public List<MediaFile> getMediaFileByMediaDirectory(MediaDirectory mediaDirectory, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFile> results;
        try{
            TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i WHERE i.mediaDirectory=:mediaDirectory", MediaFile.class);
            typedQuery.setParameter("mediaDirectory", mediaDirectory);
            results = typedQuery.getResultList();
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
        return results;
    }
}
