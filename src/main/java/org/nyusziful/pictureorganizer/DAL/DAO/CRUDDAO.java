package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.TrackingEntity;

import java.util.Collection;
import java.util.List;

public interface CRUDDAO<T> {
    public List<T> getAll();
    public T getById(int id);
    public List<T> getByIds(Collection<Integer> ids);
    //    public T save(T item);
    public void persist(T item);
    public T merge(T item);
    public void delete(T item);
    public List<T> getAll(boolean batch);
    public T getById(int id, boolean batch);
//    public T save(T item, boolean batch);
public List<T> getByIds(Collection<Integer> ids, boolean batch);
    public void persist(T item, boolean batch);
    public T merge(T item, boolean batch);
    public void delete(T item, boolean batch);
    public void flush();
    public void close();
}
