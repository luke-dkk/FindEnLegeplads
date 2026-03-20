package app.services.entityService;

import java.util.List;

public interface IService <T> {


        T create(T dto);

        List<T> getAll();

        T getById(Integer id);

        T update(T dto);

        boolean delete(Integer id);

}

