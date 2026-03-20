package app.controllers;

import java.util.List;

    public interface IController<T> {

        T create(T dto);

        List<T> getAll();

        T getById(int id);

        T update(T dto);

        boolean delete(int id);

    }

