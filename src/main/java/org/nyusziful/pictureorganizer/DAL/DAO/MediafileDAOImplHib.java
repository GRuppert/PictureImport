package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
}
