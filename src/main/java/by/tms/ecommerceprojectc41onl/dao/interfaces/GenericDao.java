package by.tms.ecommerceprojectc41onl.dao.interfaces;

import java.util.List;

public interface GenericDao<T, ID> {
    void save(T entity);

    List<T> findAll();

    T findById(ID id);
}
