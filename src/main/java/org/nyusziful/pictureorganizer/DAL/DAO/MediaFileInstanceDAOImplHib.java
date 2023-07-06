package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.Service.FolderService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MediaFileInstanceDAOImplHib extends CRUDDAOImpHib<MediaFileInstance> implements MediaFileInstanceDAO {

    @Override
    public List<MediaFileInstance> getByDriveId(int id) {
        return getByDriveId(id, false);
    }

    @Override
    public List<MediaFileInstance> getByDriveId(int id, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileInstance> results = new ArrayList<>();
        try{
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MediaFileInstance> query = cb.createQuery(MediaFileInstance.class);
            Root<MediaFileInstance> root = query.from(MediaFileInstance.class);
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
    public MediaFileInstance getByFile(Drive drive, Path path) {
        return getByFile(drive, path, false);
    }

    @Override
    public MediaFileInstance getByFile(Drive drive, Path path, boolean batch) {
        if (path == null || drive == null) return null;
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileInstance> results;
        try{
            TypedQuery<MediaFileInstance> typedQuery = entityManager.createQuery("SELECT i from MediaFileInstance i WHERE i.folder.drive.id=:driveId and i.folder.path =:path and i.filename =:filename", MediaFileInstance.class);
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
    public List<MediaFileInstance> getByMediaFile(MediaFile mediaFile) {
        return getByMediaFile(mediaFile, false);
    }

    @Override
    public List<MediaFileInstance> getByMediaFile(MediaFile mediaFile, boolean batch) {
        if (mediaFile == null) return null;
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileInstance> results;
        try{
            TypedQuery<MediaFileInstance> typedQuery = entityManager.createQuery("SELECT i from MediaFileInstance i WHERE i.mediaFile=:mediaFile", MediaFileInstance.class);
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
            if(entityManager!=null && !batch){
                entityManager.close();
            }
        }
        if (!results.isEmpty())
            return results;
        else
            return null;
    }

    @Override
    public List<MediaFileInstance> getByPath(Drive drive, Path path) {
        return getByPath(drive, path, false);
    }

    @Override
    public List<MediaFileInstance> getByPath(Drive drive, Path path, boolean batch) {
        long start = System.nanoTime();
        EntityManager entityManager = jpaConnection.getEntityManager();
        long entity = System.nanoTime();
        EntityTransaction transaction = entityManager.getTransaction();
        long trans = System.nanoTime();
        List<MediaFileInstance> results = new ArrayList<>();
        long query = System.nanoTime();
        long commit = query;
        long close = query;
        try{
            TypedQuery<MediaFileInstance> typedQuery = entityManager.createQuery("SELECT i from MediaFileInstance i LEFT JOIN FETCH i.image WHERE i.folder.drive.id=:driveId and i.folder.path=:path", MediaFileInstance.class);
            typedQuery.setParameter("driveId", drive.getId());
            typedQuery.setParameter("path", FolderService.winToDataPath(path));
            results = typedQuery.getResultList();
            query = System.nanoTime();
            if (!batch) transaction.commit();
            commit = System.nanoTime();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;

        }finally{
            close = System.nanoTime();
            if(entityManager!=null && !batch){
                entityManager.close();
            }
        }
        long end = System.nanoTime();
/*
        System.out.println(
                "Init  " + TimeUnit.NANOSECONDS.toMillis(entity - start)
                        + "\nTrans " + TimeUnit.NANOSECONDS.toMillis(trans - entity)
                        + "\nQuery " + TimeUnit.NANOSECONDS.toMillis(query - trans)
                        + "\nComit " + TimeUnit.NANOSECONDS.toMillis(commit - query)
                        + "\nClose " + TimeUnit.NANOSECONDS.toMillis(end - close)
                        + "\nWhole " + TimeUnit.NANOSECONDS.toMillis(end - start)

        );
*/
        return results;
    }

    @Override
    public List<MediaFileInstance> getByPathRec(Drive drive, Path path) {
        return getByPathRec(drive, path, false);
    }

    @Override
    public List<MediaFileInstance> getByPathRec(Drive drive, Path path, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileInstance> results;
        try{
            TypedQuery<MediaFileInstance> typedQuery = entityManager.createQuery("SELECT i from MediaFileInstance i WHERE i.folder.drive.id=:driveId and i.folder.path like :path", MediaFileInstance.class);
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

    @Override
    public List<MediaFileInstance> getMediaFilesFromPathOfImage(Image image, Drive drive, Path target) {
        return getMediaFilesFromPathOfImage(image, drive, target, false);
    }

    @Override
    public List<MediaFileInstance> getMediaFilesFromPathOfImage(Image image, Drive drive, Path target, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileInstance> results;
        try{
            TypedQuery<MediaFileInstance> typedQuery = entityManager.createQuery("SELECT i from MediaFileInstance i LEFT JOIN FETCH i.image WHERE i.drive.id=:driveId and i.folder.path like :path and i.image=:image", MediaFileInstance.class);
            typedQuery.setParameter("driveId", drive.getId());
            typedQuery.setParameter("path", FolderService.winToDataPath(target) + "%");
            typedQuery.setParameter("image", image);
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
        return results;    }

    @Override
    public List<MediaFileInstance> getByVersion(MediaFileVersion mediaFileVersion) {
        return getByVersion(mediaFileVersion, false);
    }
    @Override
    public List<MediaFileInstance> getByVersion(MediaFileVersion mediaFileVersion, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<MediaFileInstance> results;
        try{
            TypedQuery<MediaFileInstance> typedQuery = entityManager.createQuery("SELECT i from MediaFileInstance i WHERE i.mediaFileVersion=:mediaFileVersion", MediaFileInstance.class);
            typedQuery.setParameter("mediaFileVersion", mediaFileVersion);
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
