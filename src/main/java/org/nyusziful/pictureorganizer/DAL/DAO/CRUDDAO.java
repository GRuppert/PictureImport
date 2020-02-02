package org.nyusziful.pictureorganizer.DAL.DAO;

import java.util.List;

public interface CRUDDAO<T> {
    public List<T> getAll();
    public T getById(int id);
    public void persist(T item);
    public T merge(T item);
    public void delete(T item);
    public void flush();
}
