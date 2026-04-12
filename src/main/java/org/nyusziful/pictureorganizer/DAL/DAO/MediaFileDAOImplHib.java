package org.nyusziful.pictureorganizer.DAL.DAO;

import jakarta.persistence.Query;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;

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

    @Override
    public MediaFile getMediafileByImage(String hash, String type, Integer shotnumber) {
        return getMediafileByImage(hash, type, shotnumber, false);
    }

    @Override
    public MediaFile getMediafileByImage(String hash, String type, Integer shotnumber, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Integer> results;
        try{
            Query nativeQuery = JPAConnection.getInstance().getEntityManager().createNativeQuery(
                    "SELECT DISTINCT mf.id FROM media_file_version mfv LEFT JOIN media_file mf ON mf.id = mfv.media_file_id LEFT JOIN media_image mi ON mi.media_file_version_id = mfv.id LEFT JOIN image i ON i.id = mi.image_id WHERE i.odid = :hash AND mf.file_type = :type AND mf.shotnumber = :shotnumber");
            nativeQuery.setParameter("hash", hash);
            nativeQuery.setParameter("shotnumber", shotnumber);
            nativeQuery.setParameter("type", type.toUpperCase());
            results = nativeQuery.getResultList();
            if (!batch) transaction.commit();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;

        }finally{
            if(!batch){
                entityManager.close();
            }
        }
        if (results.size() == 0) return null;
        if (results.size() == 1) return getById(results.get(0), batch);
        throw new RuntimeException("more than one Mediafile for " + hash + ":" + type + ":" + shotnumber);
    }

}
