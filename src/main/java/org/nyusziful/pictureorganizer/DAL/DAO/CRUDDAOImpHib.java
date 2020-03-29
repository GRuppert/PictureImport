package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.HibConnection;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CRUDDAOImpHib<T> implements CRUDDAO<T> {
    private Class<T> entityBeanType;
    protected JPAConnection jpaConnection;
//           = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)    protected HibConnection hibConnection;

    public CRUDDAOImpHib() {
        this.entityBeanType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        jpaConnection = JPAConnection.getInstance();
    }

    @Override
    public List<T> getAll() {
        return getAll(false);
    }

    @Override
    public List<T> getAll(boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<T> result = null;
        try{
            CriteriaQuery<T> criteria = entityManager.getCriteriaBuilder().createQuery( entityBeanType );
            Root<T> cat = criteria.from( entityBeanType );
            criteria.select( cat );
            result = entityManager.createQuery( criteria ).getResultList();
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
        return result;
    }

    @Override
    public T getById(final int id){
        return getById(id, false);
    }

    @Override
    public T getById(final int id, boolean batch){
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        T result = null;
        try{
            result = entityManager.find(entityBeanType, id);
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
        return result;
    }

    public void persist(final T item) {
        persist(item, false);
    }

    public void persist(final T item, boolean batch) {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            entityManager.persist(item);
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
    }

    @Override
    public T merge(final T item)   {
        return merge(item, false);
    }

    @Override
    public T merge(final T item, boolean batch)   {
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        T result = null;
        try{
            result = entityManager.merge(item);
            if (!batch) transaction.commit();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
                return null;
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
        }finally{
            if(entityManager!=null && !batch){
                entityManager.close();
            }
        }
        return (T) result;
    }

    @Override
    public void delete(final T item){
        delete(item, false);
    }

    @Override
    public void delete(final T item, boolean batch){
        EntityManager entityManager = jpaConnection.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            entityManager.remove(item);
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
    }

    @Override
    public void close() {
        EntityManager entityManager = jpaConnection.getEntityManager();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void flush() {
        jpaConnection.getEntityManager().flush();
    }

}
