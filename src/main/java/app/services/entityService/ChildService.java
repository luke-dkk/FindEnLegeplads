package app.services.entityService;

import app.daos.ChildDAO;
import app.dtos.ChildDTO;
import app.entities.Child;
import app.services.mappers.ChildMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class ChildService implements IService<ChildDTO> {

    private final EntityManagerFactory emf;
    private final ChildDAO childDAO;
    private final ChildMapper childMapper;

    public ChildService(EntityManagerFactory emf) {
        this.emf = emf;
        this.childDAO = new ChildDAO(emf);
        this.childMapper = new ChildMapper(emf);
    }

    @Override
    public ChildDTO create(ChildDTO childDTO) {
        Child child = childMapper.fromDTO(childDTO);
        Child createdChild = childDAO.create(child);
        return childMapper.toDTO(createdChild);
    }

    @Override
    public List<ChildDTO> getAll() {
        return childDAO.getAll()
                .stream()
                .map(childMapper::toDTO)
                .toList();
    }

    @Override
    public ChildDTO getById(Integer id) {
        Child child = childDAO.getById(id);
        if (child == null) {
            return null;
        }
        return childMapper.toDTO(child);
    }

    @Override
    public ChildDTO update(ChildDTO childDTO) {
        Child child = childMapper.fromDTO(childDTO);
        Child updatedChild = childDAO.update(childDTO.getId(), child);
        return childMapper.toDTO(updatedChild);
    }

    @Override
    public boolean delete(Integer id) {
        return childDAO.delete(id);
    }

    public List<ChildDTO> createChildren(ChildDTO[] childDTOs) {
        List<ChildDTO> createdChildren = new ArrayList<>();

        for (ChildDTO childDTO : childDTOs) {
            Child child = childMapper.fromDTO(childDTO);
            Child createdChild = childDAO.create(child);
            createdChildren.add(childMapper.toDTO(createdChild));
        }

        return createdChildren;
    }
}