package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.Service.FolderService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FolderDAOImplHib extends CRUDDAOImpHib<Folder> implements FolderDAO {
    @Override
    public Folder getFolderByPath(Drive drive, Path path) {
        return getFolderByPath(drive, path, false);
    }

    @Override
    public Folder getFolderByPath(Drive drive, Path path, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Folder> results = new ArrayList<>();
        try{
            TypedQuery<Folder> typedQuery = entityManager.createQuery("SELECT i from Folder i WHERE i.drive.id=:driveId and i.path =:path", Folder.class);
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
        if (!results.isEmpty())
            return results.get(0);
        else
            return null;
    }
}
