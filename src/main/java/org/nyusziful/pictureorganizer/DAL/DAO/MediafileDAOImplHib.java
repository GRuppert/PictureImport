package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public Mediafile getByFile(MediafileDTO mediafileDTO) {
        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Mediafile> typedQuery = entityManager.createQuery("SELECT i from Mediafile i WHERE i.drive.id=:driveId and i.folder.path =:path and i.filename =:filename", Mediafile.class);
        typedQuery.setParameter("driveId", mediafileDTO.driveId);
        typedQuery.setParameter("path", mediafileDTO.path);
        typedQuery.setParameter("filename", mediafileDTO.filename);
        List<Mediafile> results = typedQuery.getResultList();
        if (!results.isEmpty())
            return results.get(0);
        else
        return null;
    }

    @Override
    public List<Mediafile> getByPath(MediafileDTO mediafileDTO) {
        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Mediafile> typedQuery = entityManager.createQuery("SELECT i from Mediafile i WHERE i.drive.id=:driveId and i.folder.path like:path", Mediafile.class);
        typedQuery.setParameter("driveId", mediafileDTO.driveId);
        typedQuery.setParameter("path", mediafileDTO.path);
        return typedQuery.getResultList();
    }

}
