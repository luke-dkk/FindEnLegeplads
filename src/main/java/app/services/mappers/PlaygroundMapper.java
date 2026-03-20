package app.services.mappers;

import app.dtos.PlaygroundDTO;
import app.entities.Playground;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class PlaygroundMapper implements IMapper<Playground, PlaygroundDTO> {

    private final EntityManagerFactory emf;

    public PlaygroundMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Playground fromDTO(PlaygroundDTO dto) {
        EntityManager em = emf.createEntityManager();

        Playground playground = new Playground();

        playground.setName(dto.getName());
        playground.setLongitude(dto.getLongitude());
        playground.setLatitude(dto.getLatitude());
        playground.setCapacity(dto.getCapacity());

        em.close();
        return playground;
    }

    @Override
    public PlaygroundDTO toDTO(Playground playground) {
        PlaygroundDTO dto = new PlaygroundDTO();

        dto.setId(playground.getId());
        dto.setName(playground.getName());
        dto.setLongitude(playground.getLongitude());
        dto.setLatitude(playground.getLatitude());
        dto.setCapacity(playground.getCapacity());


        return dto;
    }
}