package app.daos;

import java.util.Set;

public interface IDAO <T>{
    T create (T t);
    T getById(Integer id);
    T update (Integer id, T updatedT);
    boolean delete (Integer id);
    Set<T> getAll();

}
