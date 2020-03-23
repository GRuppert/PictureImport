package org.nyusziful.pictureorganizer.DAL.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.nyusziful.pictureorganizer.DAL.HibConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CRUDDAOImpHib<T> implements CRUDDAO<T> {
    private Class<T> entityBeanType;
    protected HibConnection hibConnection;
    protected static EntityManagerFactory factory;
//           = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)    protected HibConnection hibConnection;

    public CRUDDAOImpHib() {
        this.entityBeanType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        hibConnection = HibConnection.getInstance();
        factory = hibConnection.getCurrentSession().getEntityManagerFactory();
    }

    @Override
    public List<T> getAll() {
        Session session = hibConnection.getCurrentSession();
        Transaction tx = null;
        List<T> result = null;
        try {
            tx = session.beginTransaction();
            CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(entityBeanType);
            criteriaQuery.from(entityBeanType);
            result = session.createQuery(criteriaQuery).getResultList();
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            throw e;
        }
        finally {
            session.close();
        }
        return result;
    }

    @Override
    public T getById(final int id){
        return (T) hibConnection.getCurrentSession().get(entityBeanType, id);
    }

    public void persist(final T item) {
        EntityManager entityManager = factory.createEntityManager();
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
        }finally{
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }

    @Override
    public T merge(final T o)   {
        EntityManager entityManager = factory.createEntityManager();
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
        }finally{
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }

    @Override
    public void delete(final T item){
        EntityManager entityManager = factory.createEntityManager();
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

        }finally{
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }

    @Override
    public void flush() {
        HibConnection.getInstance().getCurrentSession().flush();
    }

}
