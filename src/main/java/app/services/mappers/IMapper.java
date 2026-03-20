package app.services.mappers;

public interface IMapper<E, D> {

    E fromDTO(D dto);

    D toDTO(E entity);
}