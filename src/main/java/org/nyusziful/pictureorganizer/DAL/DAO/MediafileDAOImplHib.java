package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.Service.FolderService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MediafileDAOImplHib extends CRUDDAOImpHib<MediaFile> implements MediafileDAO {

    @Override
    public List<MediaFile> getByDriveId(int id) {
        return getByDriveId(id, false);
    }

    @Override
    public List<MediaFile> getByDriveId(int id, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFile> results = new ArrayList<>();
        try{
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MediaFile> query = cb.createQuery(MediaFile.class);
            Root<MediaFile> root = query.from(MediaFile.class);
            query = query.select(root).where(cb.equal(root.get("drive"), id));
            try {
                results = entityManager.createQuery(query).getResultList();
            } catch (NoResultException nre) {
            }
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
        return results;
    }

    @Override
    public MediaFile getByFile(Drive drive, Path path, boolean withImega) {
        return getByFile(drive, path, withImega, false);
    }

    @Override
    public MediaFile getByFile(Drive drive, Path path, boolean withImega, boolean batch) {
        if (path == null || drive == null) return null;
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFile> results = new ArrayList<>();
        try{
            TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i " + (withImega ? "LEFT JOIN FETCH i.image" : "") + " WHERE i.drive.id=:driveId and i.folder.path =:path and i.filename =:filename", MediaFile.class);
            typedQuery.setParameter("driveId", drive.getId());
            typedQuery.setParameter("path", FolderService.winToDataPath(path.getParent()));
            typedQuery.setParameter("filename", path.getFileName().toString());
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

    @Override
    public List<MediaFile> getByPath(Drive drive, Path path) {
        return getByPath(drive, path, false);
    }

    @Override
    public List<MediaFile> getByPath(Drive drive, Path path, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFile> results = new ArrayList<>();
        try{
            TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i LEFT JOIN FETCH i.image WHERE i.drive.id=:driveId and i.folder.path=:path", MediaFile.class);
            typedQuery.setParameter("driveId", drive.getId());
            typedQuery.setParameter("path", FolderService.winToDataPath(path));
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
        return results;    }

    @Override
    public List<MediaFile> getByPathRec(Drive drive, Path path) {
        return getByPathRec(drive, path, false);
    }

    @Override
    public List<MediaFile> getByPathRec(Drive drive, Path path, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFile> results = new ArrayList<>();
        try{
            TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i WHERE i.drive.id=:driveId and i.folder.path like :path", MediaFile.class);
            typedQuery.setParameter("driveId", drive.getId());
            typedQuery.setParameter("path", FolderService.winToDataPath(path) + "%");
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
        return results;
    }
/*
    public List<String> getByHash(String hash, int driveId) {
        Session session = jpaConnection.getEntityManager();
        Transaction tx = session.getTransaction();
        List<String> list = null;
        try {
            list = session.createSQLQuery("SELECT distinct(filename) FROM picture.media_file WHERE image_id_oldtodel = '" + hash + "' and drive_id = " + driveId).list();
            tx.commit();
        }

        return results;

    }

 */
}
