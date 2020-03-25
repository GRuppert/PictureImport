package org.nyusziful.pictureorganizer.DAL.DAO;

import java.util.List;

public interface CRUDDAO<T> {
    public List<T> getAll();
    public T getById(int id);
    public void persist(T item);
    public T merge(T item);
    public void delete(T item);
    public List<T> getAll(boolean batch);
    public T getById(int id, boolean batch);
    public void persist(T item, boolean batch);
    public T merge(T item, boolean batch);
    public void delete(T item, boolean batch);
    public void flush();
    public void close();
}
