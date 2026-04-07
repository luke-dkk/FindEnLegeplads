package app.services.mappers;

import app.dtos.FacilityDTO;
import app.dtos.PlaygroundDTO;
import app.entities.Facility;
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

    public FacilityDTO toFacilityDTO(Facility facility) {
        if (facility == null) return null;

        return FacilityDTO.builder()
                .id(facility.getId())
                .toilet(facility.isToilet())
                .swings(facility.isSwings())
                .sandbox(facility.isSandbox())
                .slide(facility.isSlide())
                .climbingWall(facility.isClimbingWall())
                .seesaw(facility.isSeesaw())
                .playHouse(facility.isPlayHouse())
                .merryGoRound(facility.isMerryGoRound())
                .basketballCourt(facility.isBasketballCourt())
                .soccerField(facility.isSoccerField())
                .picnicArea(facility.isPicnicArea())
                .lighting(facility.isLighting())
                .benches(facility.isBenches())
                .drinkingFountain(facility.isDrinkingFountain())
                .accessibilityFeatures(facility.isAccessibilityFeatures())
                .firstAidStation(facility.isFirstAidStation())
                .dogPark(facility.isDogPark())
                .miscellaneous(facility.getMiscellaneous())
                .build();
    }

    public Facility fromFacilityDTO(FacilityDTO dto) {
        if (dto == null) return null;

        return Facility.builder()
                .toilet(dto.isToilet())
                .swings(dto.isSwings())
                .sandbox(dto.isSandbox())
                .slide(dto.isSlide())
                .climbingWall(dto.isClimbingWall())
                .seesaw(dto.isSeesaw())
                .playHouse(dto.isPlayHouse())
                .merryGoRound(dto.isMerryGoRound())
                .basketballCourt(dto.isBasketballCourt())
                .soccerField(dto.isSoccerField())
                .picnicArea(dto.isPicnicArea())
                .lighting(dto.isLighting())
                .benches(dto.isBenches())
                .drinkingFountain(dto.isDrinkingFountain())
                .accessibilityFeatures(dto.isAccessibilityFeatures())
                .firstAidStation(dto.isFirstAidStation())
                .dogPark(dto.isDogPark())
                .miscellaneous(dto.getMiscellaneous())
                .build();
    }
}