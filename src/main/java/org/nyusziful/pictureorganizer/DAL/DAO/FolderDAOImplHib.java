package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;
import org.nyusziful.pictureorganizer.Service.FolderService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.nio.file.Path;
import java.util.List;

public class FolderDAOImplHib extends CRUDDAOImpHib<Folder> implements FolderDAO {
    @Override
    public Folder getFolderByPath(Drive drive, Path path) {
        final EntityManager entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
        TypedQuery<Folder> typedQuery = entityManager.createQuery("SELECT i from Folder i WHERE i.drive.id=:driveId and i.path =:path", Folder.class);
        typedQuery.setParameter("driveId", drive.getId());
        typedQuery.setParameter("path", FolderService.winToDataPath(path));
        List<Folder> results = typedQuery.getResultList();
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;
    }

}
