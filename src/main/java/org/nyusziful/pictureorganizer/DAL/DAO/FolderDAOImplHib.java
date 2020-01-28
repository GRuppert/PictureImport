package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class FolderDAOImplHib extends CRUDDAOImpHib<Folder> implements FolderDAO {
    @Override
    public Folder getFolderByPath(FolderDTO folderDTO) {
        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Folder> typedQuery = entityManager.createQuery("SELECT i from Folder i WHERE i.drive.id=:driveId and i.path =:path", Folder.class);
        typedQuery.setParameter("driveId", folderDTO.driveId);
        typedQuery.setParameter("path", folderDTO.path);
        List<Folder> results = typedQuery.getResultList();
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;
    }
}
