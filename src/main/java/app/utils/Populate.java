package app.utils;

import app.daos.*;
import app.entities.*;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.Map;

public class Populate {

    private final EntityManagerFactory emf;
    private final UserDAO userDAO;
    private final PlaygroundDAO playgroundDAO;
    private final ChildDAO childDAO;

    public Populate(EntityManagerFactory emf) {
        this.emf = emf;
        this.userDAO = new UserDAO(emf);
        this.playgroundDAO = new PlaygroundDAO(emf);
        this.childDAO = new ChildDAO(emf);
    }

    public Map<String, Object> populate() {

        // ---------- USERS ----------
        User user1 = User.builder()
                .parentName("Niels")
                .email("niels@play.dk")
                .password("12345678")
                .build();

        User user2 = User.builder()
                .parentName("Anna")
                .email("anna@play.dk")
                .password("12345678")
                .build();

        userDAO.create(user1);
        userDAO.create(user2);

        // ---------- CHILDREN ----------
        Child child1 = Child.builder()
                .name("Henrik")
                .age(5)
                .gender(Gender.Male)
                .sex(Sex.BOY)
                .build();

        Child child2 = Child.builder()
                .name("Sofie")
                .age(4)
                .gender(Gender.Female)
                .sex(Sex.GIRL)
                .build();

        Child child3 = Child.builder()
                .name("Lucas")
                .age(6)
                .gender(Gender.Male)
                .sex(Sex.BOY)
                .build();

        user1.addChild(child1);
        user1.addChild(child2);
        user2.addChild(child3);

        userDAO.update(user1);
        userDAO.update(user2);

        // ---------- PLAYGROUNDS ----------
        Playground pg1 = Playground.builder()
                .name("Fælledparken")
                .capacity(50)
                .latitude(55.7000)
                .longitude(12.5700)
                .build();

        Playground pg2 = Playground.builder()
                .name("Superkilen")
                .capacity(40)
                .latitude(55.6995)
                .longitude(12.5500)
                .build();

        playgroundDAO.create(pg1);
        playgroundDAO.create(pg2);

        // ---------- FACILITIES ----------
        // Ensure that you use the existing facility or update it
        Facility facility1 = pg1.getFacility();
        if (facility1 == null) {
            // Create a new Facility if not already present
            facility1 = Facility.builder()
                    .toilet(true)
                    .swings(true)
                    .sandbox(true)
                    .basketballCourt(true)
                    .build();
            pg1.addFacility(facility1);
        }

        Facility facility2 = pg2.getFacility();
        if (facility2 == null) {
            // Create a new Facility if not already present
            facility2 = Facility.builder()
                    .toilet(false)
                    .swings(true)
                    .climbingWall(true)
                    .soccerField(true)
                    .build();
            pg2.addFacility(facility2);
        }

        // Update the Playground (and Facility)
//        playgroundDAO.update(pg1);
//        playgroundDAO.update(pg2);

        // ---------- RATINGS ----------
        Rating rating1 = Rating.builder()
                .rating(4.5)
                .comment("Great playground!")
                .user(user1)
                .playground(pg1)
                .build();

        Rating rating2 = Rating.builder()
                .rating(3.0)
                .comment("Nice but crowded.")
                .user(user2)
                .playground(pg1)
                .build();

        Rating rating3 = Rating.builder()
                .rating(5.0)
                .comment("Kids loved it!")
                .user(user1)
                .playground(pg2)
                .build();

        pg1.getRatings().add(rating1);
        pg1.getRatings().add(rating2);
        pg2.getRatings().add(rating3);

//        playgroundDAO.update(pg1);
//        playgroundDAO.update(pg2);

        // ---------- CHECK-INS ----------
        CheckIn checkIn1 = CheckIn.builder()
                .plannedCheckIn(LocalDateTime.now())
                .plannedCheckout(LocalDateTime.now().plusHours(2))
                .user(user1)
                .playground(pg1)
                .build();

        CheckIn checkIn2 = CheckIn.builder()
                .plannedCheckIn(LocalDateTime.now())
                .plannedCheckout(LocalDateTime.now().plusHours(1))
                .user(user2)
                .playground(pg2)
                .build();

        pg1.getCheckIns().add(checkIn1);
        pg2.getCheckIns().add(checkIn2);

        playgroundDAO.update(pg1);
        playgroundDAO.update(pg2);

        return Map.of(
                "user1", user1,
                "user2", user2,
                "child1", child1,
                "child2", child2,
                "child3", child3,
                "playground1", pg1,
                "playground2", pg2
        );
    }
}
