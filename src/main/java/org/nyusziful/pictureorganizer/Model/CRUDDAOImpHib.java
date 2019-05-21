package org.nyusziful.pictureorganizer.Model;

import org.hibernate.Session;
import org.nyusziful.pictureorganizer.DB.HibConnection;

import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CRUDDAOImpHib<T> implements CRUDDAO<T> {
    private Class<T> entityBeanType;
    protected HibConnection hibConnection;
    public CRUDDAOImpHib() {
        this.entityBeanType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        hibConnection = HibConnection.getInstance();
    }


    @Override
    public List<T> getAll() {
        Session session = hibConnection.getCurrentSession();
        CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(entityBeanType);
        criteriaQuery.from(entityBeanType);
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public T getById(final long id){
        return (T) hibConnection.getCurrentSession().get(entityBeanType, id);
    }

    @Override
    public T save(final T item){
        return (T) hibConnection.getCurrentSession().save(item);
    }

    @Override
    public T merge(final T o)   {
        return (T) hibConnection.getCurrentSession().merge(o);
    }

    @Override
    public void delete(final T item){
        hibConnection.getCurrentSession().delete(item);
    }

}
