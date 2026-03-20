package app.services.entityService;

import app.daos.PlaygroundDAO;
import app.dtos.PlaygroundDTO;
import app.entities.Playground;
import app.services.mappers.PlaygroundMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundService implements IService<PlaygroundDTO> {

    private final EntityManagerFactory emf;
    private final PlaygroundDAO playgroundDAO;
    private final PlaygroundMapper playgroundMapper;

    public PlaygroundService(EntityManagerFactory emf) {
        this.emf = emf;
        this.playgroundDAO = new PlaygroundDAO(emf);
        this.playgroundMapper = new PlaygroundMapper(emf);
    }

    @Override
    public PlaygroundDTO create(PlaygroundDTO dto) {
        Playground playground = playgroundMapper.fromDTO(dto);
        Playground created = playgroundDAO.create(playground);
        return playgroundMapper.toDTO(created);
    }

    @Override
    public List<PlaygroundDTO> getAll() {
        return playgroundDAO.getAll()
                .stream()
                .map(playgroundMapper::toDTO)
                .toList();
    }

    @Override
    public PlaygroundDTO getById(Integer id) {
        Playground playground = playgroundDAO.getById(id);
        if (playground == null) return null;
        return playgroundMapper.toDTO(playground);
    }

    @Override
    public PlaygroundDTO update(PlaygroundDTO dto) {
        Playground playground = playgroundMapper.fromDTO(dto);
        Playground updated = playgroundDAO.update(dto.getId(), playground);
        return playgroundMapper.toDTO(updated);
    }

    @Override
    public boolean delete(Integer id) {
        return playgroundDAO.delete(id);
    }

    public List<PlaygroundDTO> createPlaygrounds(PlaygroundDTO[] dtos) {
        List<PlaygroundDTO> created = new ArrayList<>();
        for (PlaygroundDTO dto : dtos) {
            Playground playground = playgroundMapper.fromDTO(dto);
            Playground saved = playgroundDAO.create(playground);
            created.add(playgroundMapper.toDTO(saved));
        }
        return created;
    }
}