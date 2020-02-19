package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DAL.HibConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CRUDDAOImpHib<T> implements CRUDDAO<T> {
    private Class<T> entityBeanType;
    protected HibConnection hibConnection;
    protected EntityManager entityManager;

    public CRUDDAOImpHib() {
        this.entityBeanType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        hibConnection = HibConnection.getInstance();
        entityManager = hibConnection.getCurrentSession().getEntityManagerFactory().createEntityManager();
    }


    @Override
    public List<T> getAll() {
        Session session = hibConnection.getCurrentSession();
        CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(entityBeanType);
        criteriaQuery.from(entityBeanType);
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public T getById(final int id){
        return (T) hibConnection.getCurrentSession().get(entityBeanType, id);
    }

    public void persist(final T item) {
        EntityTransaction transaction = null;
        try{
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(item);
            transaction.commit();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
/*
        }finally{
            if(session!=null){
                session.close();
            }
*/
        }
    }

    @Override
    public T merge(final T o)   {
        EntityTransaction transaction = null;
        try{
            transaction = entityManager.getTransaction();
            transaction.begin();
            Object res = entityManager.merge(o);
            transaction.commit();
            return (T) res;
        }catch(RuntimeException e){
            try{
                transaction.rollback();
                return null;
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
/*
        }finally{
            if(session!=null){
                session.close();
            }
*/
        }
    }

    @Override
    public void delete(final T item){
//        hibConnection.getCurrentSession().delete(item);
        EntityTransaction transaction = null;
        try{
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.remove(item);
            transaction.commit();
        }catch(RuntimeException e){
            try{
                transaction.rollback();
            }catch(RuntimeException rbe){
//                log.error("Couldn’t roll back transaction", rbe);
            }
            throw e;
/*
        }finally{
            if(session!=null){
                session.close();
            }
*/
        }

    }

    @Override
    public void flush() {
        HibConnection.getInstance().getCurrentSession().flush();
    }

}
