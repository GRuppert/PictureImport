package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.Service.FolderService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.nio.file.Path;
import java.util.List;

public class MediafileDAOImplHib extends CRUDDAOImpHib<Mediafile> implements MediafileDAO {
    @Override
    public List<Mediafile> getByDriveId(int id) {
        Session session = hibConnection.getCurrentSession();
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Mediafile> query = cb.createQuery(Mediafile.class);
        Root<Mediafile> root = query.from(Mediafile.class);
        query = query.select(root).where(cb.equal(root.get("drive"), id));
        try {
            return em.createQuery(query).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public Mediafile getByFile(Drive drive, Path path) {
        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Mediafile> typedQuery = entityManager.createQuery("SELECT i from Mediafile i WHERE i.drive.id=:driveId and i.folder.path =:path and i.filename =:filename", Mediafile.class);
        typedQuery.setParameter("driveId", drive.getId());
        typedQuery.setParameter("path", FolderService.winToDataPath(path));
        typedQuery.setParameter("filename", path.getFileName());
        List<Mediafile> results = typedQuery.getResultList();
        if (!results.isEmpty())
            return results.get(0);
        else
        return null;
    }

    @Override
    public List<Mediafile> getByPath(Drive drive, Path path) {
        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Mediafile> typedQuery = entityManager.createQuery("SELECT i from Mediafile i WHERE i.drive.id=:driveId and i.folder.path like:path", Mediafile.class);
        typedQuery.setParameter("driveId", drive.getId());
        typedQuery.setParameter("path", FolderService.winToDataPath(path));
        return typedQuery.getResultList();
    }

}
