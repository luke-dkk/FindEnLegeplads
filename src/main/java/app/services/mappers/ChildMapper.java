package app.services.mappers;

import app.dtos.ChildDTO;
import app.entities.Child;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Set;

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

    public Set<ChildDTO> loopToDTO(Set<Child> children) {
        Set<ChildDTO> result = new HashSet<>();

        for (Child c : children) {
            result.add(toDTO(c));
        }
        return result;
    }


    public Set<Child> loopFromDTO(Set<ChildDTO> children){
        Set<Child> result = new HashSet<>();

        for (ChildDTO c : children) {
            result.add(fromDTO(c));
        }
        return result;
    }
    }

