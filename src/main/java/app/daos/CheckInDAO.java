package app.daos;
import app.dtos.AuthUserDTO;
import app.dtos.CheckInDTO;
import app.services.mappers.CheckInMapper;
import io.javalin.http.ForbiddenResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import app.entities.User;
import app.entities.Playground;
import app.entities.Child;
import app.entities.CheckIn;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckInDAO {
private static EntityManagerFactory emf;
private final CheckInMapper checkInMapper;

    public CheckInDAO(EntityManagerFactory emf) {
        this.emf = emf;
        this.checkInMapper = new CheckInMapper(emf);
    }

    public CheckInDTO createCheckIn(CheckInDTO dto,AuthUserDTO authUser) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, authUser.id());
            Playground playground = em.find(Playground.class, dto.getPlaygroundId());
            if (playground == null)
            {
                throw new RuntimeException(
                        "Playground not found"
                );
            }
            Set<Child> children = new HashSet<>();
            for (Integer childId : dto.getChildIds()) {

                Child child = em.find(Child.class, childId);
                if (child == null) {throw new RuntimeException("Child not found");
                }



                if (!child.getUser()
                        .getId()
                        .equals(authUser.id())) {

                    throw new RuntimeException("Child does not belong to user");
                }
                if (childHasActiveCheckIn(em,childId
                )) {throw new RuntimeException("Child is already checked in");

                }
                children.add(child);
            }
            CheckIn checkIn =CheckIn.builder()
                            .user(user)
                            .playground(playground)
                            .children(children)
                            .checkIn(LocalDateTime.now())
                            .plannedCheckIn(dto.getPlannedCheckIn())
                            .plannedCheckout(dto.getPlannedCheckOut())
                            .build();
            em.persist(checkIn);
            em.getTransaction().commit();
            return checkInMapper.toDTO(checkIn);
        }
    }

    public CheckInDTO checkoutFromPlayground(Integer playgroundId,Integer authUserId)
    {
        try (EntityManager em =emf.createEntityManager())
        {
            em.getTransaction().begin();
            CheckIn checkout =
                    em.createQuery(
                                    """
                                    SELECT c
                                    FROM CheckIn c
                                    WHERE c.user.id = :userId
                                    AND c.playground.id = :playgroundId
                                    AND c.checkout IS NULL
                                    """,
                                    CheckIn.class
                            )

                            .setParameter("userId",authUserId)
                            .setParameter("playgroundId",playgroundId)
                            .getResultStream()
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException("No active checkin found"));
            checkout.setCheckout(LocalDateTime.now());
            em.getTransaction().commit();
            return checkInMapper.toDTO(checkout);
        }
    }
//
//    public CheckInDTO checkoutFromPlayground(Integer checkInId,Integer authUserId)
//    {try (EntityManager em =emf.createEntityManager()) {
//
//            em.getTransaction().begin();
//
//
//
//            CheckIn checkout =
//                    em.find(
//                            CheckIn.class,
//                            checkInId
//                    );
//
//
//
//            if (checkout == null) {
//
//                throw new RuntimeException(
//                        "CheckIn not found"
//                );
//            }
//
//
//
//            if (!checkout.getUser()
//                    .getId()
//                    .equals(authUserId)) {
//
//                throw new ForbiddenResponse(
//                        "Not your check-in"
//                );
//            }
//
//
//
//            if (checkout.getCheckout() != null) {
//
//                throw new RuntimeException(
//                        "Already checked out"
//                );
//            }
//
//
//
//            checkout.setCheckout(
//                    LocalDateTime.now()
//            );
//
//
//
//            em.getTransaction().commit();
//
//
//
//            return checkInMapper.toDTO(
//                    checkout
//            );
//        }
//    }

//    public CheckInDTO createCheckIn(CheckInDTO dto, AuthUserDTO authUser)
//    {
//        try (EntityManager em = emf.createEntityManager()) {
//            em.getTransaction().begin();
//            User user = em.find(User.class, authUser.id());
//
//            Playground playground = em.find(Playground.class, dto.getPlaygroundId());
//            if (playground == null) {throw new RuntimeException("Playground not found");
//            }
//
//            Set<Child> children = new HashSet<>();
//
//            for (Integer childId : dto.getChildIds()) {
//                Child child = em.find( Child.class, childId );
//                if (child == null) {
//                    throw new RuntimeException("Child not found");
//                }
//                if (!child.getUser().getId().equals(authUser.id())) {
//                    throw new RuntimeException("Child does not belong to user");
//                }
//                if (childHasActiveCheckIn(em,childId)) {
//
//                    throw new RuntimeException(
//                            "Child is already checked in"
//                    );
//                }
//                children.add(child);
//            }
//            CheckIn checkIn =CheckIn.builder()
//                            .user(user)
//                            .playground(playground)
//                            .children(children)
//                            .checkIn(LocalDateTime.now())
//                            .plannedCheckIn(dto.getPlannedCheckIn())
//                            .plannedCheckout(dto.getPlannedCheckOut())
//                            .build();
//
//            em.persist(checkIn);
//            em.getTransaction().commit();
//
//            return checkInMapper.toDTO(checkIn);
//        }
//    }
//
    private boolean childHasActiveCheckIn(EntityManager em,Integer childId)
    {
        Long count = em.createQuery(
                        """
                        SELECT COUNT(c)
                        FROM CheckIn c
                        JOIN c.children child
                        WHERE child.id = :childId
                        AND c.checkIn IS NOT NULL
                        AND c.checkout IS NULL
                        """,
                        Long.class
                )
                .setParameter("childId", childId)
                .getSingleResult();

        return count > 0;
    }



    public List<CheckInDTO> checkoutFromEveryWhere(Integer authUserId) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            List<CheckIn> checkouts = em.createQuery(
                            """
                            SELECT c
                            FROM CheckIn c
                            WHERE c.user.id = :userId
                            AND c.checkout IS NULL
                            """,
                            CheckIn.class
                    )


                    .setParameter("userId", authUserId)
                    .getResultList();

            if (checkouts.isEmpty()) {
                throw new RuntimeException("No active checkins found");
            }

            LocalDateTime now = LocalDateTime.now();

            checkouts.forEach(c -> c.setCheckout(now));

            em.getTransaction().commit();

            return checkouts.stream()
                    .map(checkInMapper::toDTO)
                    .toList();
        }
    }

//    public CheckInDTO checkoutFromPlayground(Integer checkInId,Integer authUserId)
//    {
//
//        try (EntityManager em =emf.createEntityManager())
//        {
//            em.getTransaction().begin();
//            CheckIn checkout = em.find(CheckIn.class,checkInId);
//            if (checkout == null)
//            {throw new RuntimeException
//                    ("CheckIn not found");
//            }
//            if (!checkout.getUser()
//                    .getId()
//                    .equals(authUserId)) {
//
//                throw new ForbiddenResponse("Not your check-in");
//            }
//            if (checkout.getCheckIn() == null)
//            {
//                throw new RuntimeException("You must check in first");
//            }
//            if (checkout.getCheckout() != null) {
//                throw new RuntimeException("Already checked out");
//            }
//            checkout.setCheckout(java.time.LocalDateTime.now());
//            em.getTransaction().commit();
//            return checkInMapper.toDTO(checkout);
//        }
//    }
}