package org.nyusziful.pictureorganizer.Model;

import java.util.List;

public interface CRUDDAO<T> {
    public List<T> getAll();
    public T getById(long id);
    public T save(T item) throws Exception;
    public T merge(T item) throws Exception;
    public void delete(T item) throws Exception;
}
