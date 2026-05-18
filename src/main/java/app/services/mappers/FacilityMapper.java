package app.services.mappers;

import app.daos.FacilityDAO;
import app.dtos.FacilityDTO;
import app.entities.Facility;
import app.entities.Playground;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacilityMapper {

    private final EntityManagerFactory emf;
    FacilityDAO facilityDAO;

    public FacilityMapper(EntityManagerFactory emf) {
        this.emf = emf;
        this.facilityDAO = new FacilityDAO(emf);
    }


    public Set<FacilityDTO> loopToDTO (Set<Facility> facility) {
        if (facility == null) return null;

        Set<FacilityDTO> dtos = new HashSet<>();

        for (Facility f : facility) {
            FacilityDTO dto = new FacilityDTO();
            dto.setId(f.getId());
            dto.setName(f.getFacility());
            dtos.add(dto);
        }
        return dtos;
    }

    public Set<Facility> loopToFacility(Set<FacilityDTO> dto) {
        if (dto == null) return null;

        Set <Facility> facilities = new HashSet<>();

        for (FacilityDTO f : dto) {
            Facility facility;
            int id = f.getId();
            facility = facilityDAO.getById(id);
            facilities.add(facility);
        }
        return facilities;
    }

    public Facility toSingleFacility(FacilityDTO dto) {
        if (dto == null) return null;

        Facility facility = Facility.builder()
                .facility(dto.getName())
                .build();
        return facility;
    }

    public FacilityDTO toSingleDTO(Facility facility){
        if (facility == null) return null;

        FacilityDTO dto = new FacilityDTO();
        dto.setId(facility.getId());
        dto.setName(facility.getFacility());
        return dto;
    }
}