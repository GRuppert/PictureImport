package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Media;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileVersion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MediaFileVersionDAOImplHib extends CRUDDAOImpHib<MediaFileVersion> implements MediaFileVersionDAO {
    @Override
    public MediaFileVersion getMediafileVersionByFileHash(String filehash) {
        return getMediafileVersionByFileHash(filehash, false);
    }
    @Override
    public MediaFileVersion getMediafileVersionByFileHash(String filehash, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileVersion> results;
        try{//LEFT JOIN FETCH i.mediaFiles
            TypedQuery<MediaFileVersion> typedQuery = entityManager.createQuery("SELECT mfv from MediaFileVersion mfv WHERE mfv.filehash=:hash", MediaFileVersion.class);
            typedQuery.setParameter("hash", filehash);
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
            if(!batch){
                entityManager.close();
            }
        }
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;
    }
    @Override
    public List<MediaFileVersion> getMediafileVersionsByMediaFile(MediaFile mediaFile) {
        return getMediafileVersionsByMediaFile(mediaFile, false);
    }
    @Override
    public List<MediaFileVersion> getMediafileVersionsByMediaFile(MediaFile mediaFile, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileVersion> results;
        try{//LEFT JOIN FETCH i.mediaFiles
            TypedQuery<MediaFileVersion> typedQuery = entityManager.createQuery("SELECT mfv from MediaFileVersion mfv WHERE mfv.mediaFile=:mediaFile", MediaFileVersion.class);
            typedQuery.setParameter("mediaFile", mediaFile);
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
            if(!batch){
                entityManager.close();
            }
        }
        if (!results.isEmpty())
            return results;
        else
            return null;
    }

    @Override
    public List<MediaFileVersion> getMediafileVersionsByParent(MediaFileVersion mediaFileVersion) {
        return getMediafileVersionsByParent(mediaFileVersion, false);
    }
    @Override
    public List<MediaFileVersion> getMediafileVersionsByParent(MediaFileVersion mediaFileVersion, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileVersion> results;
        try{
            TypedQuery<MediaFileVersion> typedQuery = entityManager.createQuery("SELECT DISTINCT mfv from MediaFileVersion mfv WHERE mfv.parent=:parent", MediaFileVersion.class);
            typedQuery.setParameter("parent", mediaFileVersion);
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
            if(!batch){
                entityManager.close();
            }
        }
        if (!results.isEmpty())
            return results;
        else
            return null;
    }

    @Override
    public MediaFileVersion getOriginalMediafileVersion(MediaFile mediaFile) {
        return getOriginalMediafileVersion(mediaFile, false);
    }
    @Override
    public MediaFileVersion getOriginalMediafileVersion(MediaFile mediaFile, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileVersion> results;
        try{//LEFT JOIN FETCH i.mediaFiles
            TypedQuery<MediaFileVersion> typedQuery = entityManager.createQuery("SELECT mfv from MediaFileVersion mfv WHERE mfv.mediaFile=:mediaFile AND mfv.original", MediaFileVersion.class);
            typedQuery.setParameter("mediaFile", mediaFile);
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
            if(!batch){
                entityManager.close();
            }
        }
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;
    }

}
