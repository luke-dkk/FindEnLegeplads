package app.services.mappers;

import app.dtos.CheckInDTO;
import app.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class CheckInMapper implements IMapper<CheckIn, CheckInDTO> {

    private final EntityManagerFactory emf;

    public CheckInMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public CheckIn fromDTO(CheckInDTO dto) {

        try (EntityManager em = emf.createEntityManager()) {

            CheckIn checkIn = new CheckIn();

            if (dto.getId() != null) {
                checkIn.setId(dto.getId());
            }

            Playground playground = em.find(Playground.class, dto.getPlaygroundId());
            checkIn.setPlayground(playground);

            User user = em.find(User.class, dto.getUserId());
            checkIn.setUser(user);

            if (dto.getChildIds() != null) {
                Set<Child> children = dto.getChildIds().stream()
                        .map(id -> em.find(Child.class, id))
                        .collect(Collectors.toSet());

                checkIn.setChildren(children);
            }

            // tider
            checkIn.setPlannedCheckIn(dto.getPlannedCheckIn());
            checkIn.setPlannedCheckout(dto.getPlannedCheckOut());
            checkIn.setCheckIn(dto.getCheckIn());
            checkIn.setCheckout(dto.getCheckOut());

            return checkIn;
        }
    }

    @Override
    public CheckInDTO toDTO(CheckIn entity) {

        return CheckInDTO.builder()
                .id(entity.getId())
                .playgroundId(entity.getPlayground().getId())
                .userId(entity.getUser().getId())

                .childIds(
                        entity.getChildren().stream()
                                .map(Child::getId)
                                .collect(Collectors.toSet())
                )

                .plannedCheckIn(entity.getPlannedCheckIn())
                .plannedCheckOut(entity.getPlannedCheckout())
                .checkIn(entity.getCheckIn())
                .checkOut(entity.getCheckout())
                .build();
    }
}