package app.services.mappers;

import app.dtos.FacilityDTO;
import app.entities.Facility;
import app.entities.Playground;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class FacilityMapper implements IMapper<Facility, FacilityDTO> {

    private final EntityManagerFactory emf;

    public FacilityMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Facility fromDTO(FacilityDTO facilityDTO) {

        EntityManager em = emf.createEntityManager();

        Facility facility = new Facility();

        if (facilityDTO.getPlaygroundId() != null) {
            Playground playground = em.find(Playground.class, facilityDTO.getPlaygroundId());
            facility.setPlayground(playground);
        }

        facility.setToilet(facilityDTO.isToilet());
        facility.setSwings(facilityDTO.isSwings());
        facility.setSandbox(facilityDTO.isSandbox());
        facility.setSlide(facilityDTO.isSlide());
        facility.setClimbingWall(facilityDTO.isClimbingWall());
        facility.setSeesaw(facilityDTO.isSeesaw());
        facility.setPlayHouse(facilityDTO.isPlayHouse());
        facility.setMerryGoRound(facilityDTO.isMerryGoRound());
        facility.setBasketballCourt(facilityDTO.isBasketballCourt());
        facility.setSoccerField(facilityDTO.isSoccerField());
        facility.setPicnicArea(facilityDTO.isPicnicArea());
        facility.setLighting(facilityDTO.isLighting());
        facility.setBenches(facilityDTO.isBenches());
        facility.setDrinkingFountain(facilityDTO.isDrinkingFountain());
        facility.setAccessibilityFeatures(facilityDTO.isAccessibilityFeatures());
        facility.setFirstAidStation(facilityDTO.isFirstAidStation());
        facility.setDogPark(facilityDTO.isDogPark());
        facility.setMiscellaneous(facilityDTO.getMiscellaneous());

        em.close();

        return facility;
    }

    @Override
    public FacilityDTO toDTO(Facility facility) {

        FacilityDTO facilityDTO = new FacilityDTO();

        if (facility.getId() != null) {
            facilityDTO.setId(facility.getId());
        }

        facilityDTO.setToilet(facility.isToilet());
        facilityDTO.setSwings(facility.isSwings());
        facilityDTO.setSandbox(facility.isSandbox());
        facilityDTO.setSlide(facility.isSlide());
        facilityDTO.setClimbingWall(facility.isClimbingWall());
        facilityDTO.setSeesaw(facility.isSeesaw());
        facilityDTO.setPlayHouse(facility.isPlayHouse());
        facilityDTO.setMerryGoRound(facility.isMerryGoRound());
        facilityDTO.setBasketballCourt(facility.isBasketballCourt());
        facilityDTO.setSoccerField(facility.isSoccerField());
        facilityDTO.setPicnicArea(facility.isPicnicArea());
        facilityDTO.setLighting(facility.isLighting());
        facilityDTO.setBenches(facility.isBenches());
        facilityDTO.setDrinkingFountain(facility.isDrinkingFountain());
        facilityDTO.setAccessibilityFeatures(facility.isAccessibilityFeatures());
        facilityDTO.setFirstAidStation(facility.isFirstAidStation());
        facilityDTO.setDogPark(facility.isDogPark());
        facilityDTO.setMiscellaneous(facility.getMiscellaneous());

        return facilityDTO;
    }
}