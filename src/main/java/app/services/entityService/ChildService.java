package app.services.entityService;

import app.daos.ChildDAO;
import app.daos.UserDAO;
import app.dtos.ChildDTO;
import app.entities.Child;
import app.entities.User;
import app.services.mappers.ChildMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ChildService implements IService<ChildDTO> {

    private final EntityManagerFactory emf;
    private final ChildDAO childDAO;
    private final ChildMapper childMapper;
    private final UserDAO userDAO;

    public ChildService(EntityManagerFactory emf) {
        this.emf = emf;
        this.childDAO = new ChildDAO(emf);
        this.childMapper = new ChildMapper(emf);
        this.userDAO = new UserDAO(emf);
    }

    @Override
    public ChildDTO create(ChildDTO childDTO) {
        Child child = childMapper.fromDTO(childDTO);
        Child createdChild = childDAO.create(child);
        return childMapper.toDTO(createdChild);
    }

    public ChildDTO createForUser(Integer userId, ChildDTO dto) {

        User user = userDAO.getById(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Child child = childMapper.fromDTO(dto);

        child.setUser(user);

        Child created = childDAO.create(child);

        return childMapper.toDTO(created);
    }
    public HashSet<ChildDTO> getByUserId(Integer userId) {

        return (HashSet<ChildDTO>) childDAO.getByUserId(userId)
                .stream()
                .map(childMapper::toDTO)
                .collect(Collectors.toSet());
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