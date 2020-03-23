package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.Service.FolderService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.nio.file.Path;
import java.util.List;

public class MediafileDAOImplHib extends CRUDDAOImpHib<MediaFile> implements MediafileDAO<MediaFile> {
    @Override
    public List<MediaFile> getByDriveId(int id) {
        Session session = hibConnection.getCurrentSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MediaFile> query = cb.createQuery(MediaFile.class);
        Root<MediaFile> root = query.from(MediaFile.class);
        query = query.select(root).where(cb.equal(root.get("drive"), id));
        try {
            return em.createQuery(query).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public MediaFile getByFile(Drive drive, Path path) {
        if (path == null || drive == null) return null;
        EntityManager entityManager = factory.createEntityManager();
        TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i WHERE i.drive.id=:driveId and i.folder.path =:path and i.filename =:filename", MediaFile.class);
        typedQuery.setParameter("driveId", drive.getId());
        typedQuery.setParameter("path", FolderService.winToDataPath(path.getParent()));
        typedQuery.setParameter("filename", path.getFileName().toString());
        List<MediaFile> results = typedQuery.getResultList();
        if (!results.isEmpty())
            return results.get(0);
        else
        return null;
    }

    @Override
    public List<MediaFile> getByPath(Drive drive, Path path) {
        if (path == null || drive == null) return null;
        EntityManager entityManager = factory.createEntityManager();
        TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i WHERE i.drive.id=:driveId and i.folder.path=:path", MediaFile.class);
        typedQuery.setParameter("driveId", drive.getId());
        typedQuery.setParameter("path", FolderService.winToDataPath(path));
        return typedQuery.getResultList();
    }

    @Override
    public List<MediaFile> getByPathRec(Drive drive, Path path) {
        if (path == null || drive == null) return null;
        EntityManager entityManager = factory.createEntityManager();
        TypedQuery<MediaFile> typedQuery = entityManager.createQuery("SELECT i from MediaFile i WHERE i.drive.id=:driveId and i.folder.path like :path", MediaFile.class);
        typedQuery.setParameter("driveId", drive.getId());
        typedQuery.setParameter("path", FolderService.winToDataPath(path) + "%");
        return typedQuery.getResultList();
    }

    public List<String> getByHash(String hash, int driveId) {
        Session session = hibConnection.getCurrentSession();
        final List list = session.createSQLQuery("SELECT distinct(filename) FROM picture.media_file WHERE image_id_oldtodel = '" + hash + "' and drive_id = " + driveId).list();
        return list;
    }
}
