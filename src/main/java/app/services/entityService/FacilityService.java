package app.services.entityService;

import app.daos.FacilityDAO;
import app.dtos.FacilityDTO;
import app.entities.Facility;
import app.services.mappers.FacilityMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class FacilityService implements IService<FacilityDTO> {

    private final EntityManagerFactory emf;
    private final FacilityDAO facilityDAO;
    private final FacilityMapper facilityMapper;

    public FacilityService(EntityManagerFactory emf) {
        this.emf = emf;
        this.facilityDAO = new FacilityDAO(emf);
        this.facilityMapper = new FacilityMapper(emf);
    }

    @Override
    public FacilityDTO create(FacilityDTO facilityDTO) {
        Facility facility = facilityMapper.toSingleFacility(facilityDTO);
        Facility createdFacility = facilityDAO.create(facility);
        return facilityMapper.toSingleDTO(createdFacility);
    }

    @Override
    public List<FacilityDTO> getAll() {
        return facilityDAO.getAll()
                .stream()
                .map(facilityMapper::toSingleDTO)
                .toList();
    }

    @Override
    public FacilityDTO getById(Integer id) {
        Facility facility = facilityDAO.getById(id);

        if (facility == null) {
            return null;
        }

        return facilityMapper.toSingleDTO(facility);
    }

    @Override
    public FacilityDTO update(FacilityDTO facilityDTO) {
        Facility facility = facilityMapper.toSingleFacility(facilityDTO);
        Facility updatedFacility = facilityDAO.update(facilityDTO.getId(), facility);
        return facilityMapper.toSingleDTO(updatedFacility);
    }

    @Override
    public boolean delete(Integer id) {
        return facilityDAO.delete(id);
    }
}