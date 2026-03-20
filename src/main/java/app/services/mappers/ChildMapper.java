package app.services.mappers;

import app.dtos.ChildDTO;
import app.entities.Child;
import jakarta.persistence.EntityManagerFactory;

public class ChildMapper implements IMapper<Child, ChildDTO> {

    private final EntityManagerFactory emf;

    public ChildMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Child fromDTO(ChildDTO childDTO) {
        Child child = new Child();

        if (childDTO.getId() != null) {
            child.setId(childDTO.getId());
        }

        child.setName(childDTO.getName());
        child.setAge(childDTO.getAge());

        return child;
    }

    @Override
    public ChildDTO toDTO(Child child) {
        ChildDTO childDTO = new ChildDTO();

        if (child.getId() != null) {
            childDTO.setId(child.getId());
        }

        childDTO.setName(child.getName());
        childDTO.setAge(child.getAge());
        childDTO.setGender(child.getGender());
        childDTO.setSex(child.getSex());
        return childDTO;
    }
}