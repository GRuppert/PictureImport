package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;

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

    @Override
    public Mediafile getByFile(MediafileDTO mediafileDTO) {
        final List resultList = hibConnection.getCurrentSession().createQuery("SELECT i from Mediafile i WHERE i.drive.id='" + mediafileDTO.driveId + "' and i.folder.path ='" + mediafileDTO.path + "' and i.filename ='" + mediafileDTO.filename + "'").getResultList();
        if (resultList == null || resultList.size() < 1) return null;
        return (Mediafile) resultList.get(0);
    }
}
