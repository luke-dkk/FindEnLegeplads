package app.services.entityService;

import app.dtos.AuthUserDTO;
import app.dtos.CheckInDTO;
import app.entities.*;
import app.services.mappers.CheckInMapper;
import io.javalin.http.ForbiddenResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class CheckInService {

    private final EntityManagerFactory emf;
    private final CheckInMapper mapper;

    public CheckInService(EntityManagerFactory emf) {
        this.emf = emf;
        this.mapper = new CheckInMapper(emf);
    }

    public CheckInDTO create(CheckInDTO dto, Integer authUserId) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            User user = em.find(User.class, authUserId);

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            Playground playground = em.find(Playground.class, dto.getPlaygroundId());

            if (playground == null) {
                throw new RuntimeException("Playground not found");
            }

            Set<Child> children = dto.getChildIds().stream()
                    .map(id -> {
                        Child child = em.find(Child.class, id);

                        if (child == null) {
                            throw new RuntimeException("Child not found: " + id);
                        }

                        if (!child.getUser().getId().equals(user.getId())) {
                            throw new ForbiddenResponse("You can only use your own children");
                        }

                        return child;
                    })
                    .collect(java.util.stream.Collectors.toSet());

            CheckIn checkIn = new CheckIn();
            checkIn.setUser(user);
            checkIn.setPlayground(playground);
            checkIn.setChildren(children);

            checkIn.setPlannedCheckIn(dto.getPlannedCheckIn());
            checkIn.setPlannedCheckout(dto.getPlannedCheckOut());

            checkIn.setCheckIn(java.time.LocalDateTime.now());

            em.persist(checkIn);

            em.getTransaction().commit();

            return mapper.toDTO(checkIn);
        }
    }
    public List<CheckInDTO> getByPlaygroundId(Integer playgroundId) {

        try (EntityManager em = emf.createEntityManager()) {

            List<CheckIn> checkIns = em.createQuery(
                            "SELECT c FROM CheckIn c WHERE c.playground.id = :id",
                            CheckIn.class
                    ).setParameter("id", playgroundId)
                    .getResultList();

            return checkIns.stream()
                    .map(mapper::toDTO)
                    .toList();
        }
    }
    public CheckInDTO checkIn(Integer checkInId, AuthUserDTO authUser) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            CheckIn checkIn = em.find(CheckIn.class, checkInId);

            if (checkIn == null) {
                throw new RuntimeException("CheckIn not found");
            }

            if (!checkIn.getUser().getId().equals(authUser.id())) {
                throw new ForbiddenResponse("Not your check-in");
            }

            if (checkIn.getCheckIn() != null) {
                throw new RuntimeException("Already checked in");
            }

            checkIn.setCheckIn(java.time.LocalDateTime.now());

            em.getTransaction().commit();

            return mapper.toDTO(checkIn);
        }
    }
    public CheckInDTO checkout(Integer checkInId, Integer authUserId) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            CheckIn checkout = em.find(CheckIn.class, checkInId);

            User user = em.find(User.class, authUserId);

            if (checkout == null) {
                throw new io.javalin.http.NotFoundResponse("CheckIn not found");
            }

            if (!checkout.getUser().getId().equals(authUserId)) {
                throw new io.javalin.http.ForbiddenResponse("Not your check-in");
            }

            if (checkout.getCheckIn() == null) {
                throw new RuntimeException("You must check in first");
            }

            if (checkout.getCheckout() != null) {
                throw new RuntimeException("Already checked out");
            }
            checkout.setUser(user);
            checkout.setCheckout(java.time.LocalDateTime.now());
            em.merge(checkout);


            em.getTransaction().commit();

            return mapper.toDTO(checkout);
        }
    }



}