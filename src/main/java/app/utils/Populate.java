package app.utils;

import app.daos.ChildDAO;
import app.daos.PlaygroundDAO;
import app.daos.UserDAO;
import app.entities.*;
import jakarta.persistence.EntityManagerFactory;

public class Populate {
    private static EntityManagerFactory emf;

    public Populate() {
        this.emf = app.config.HibernateConfig.getEntityManagerFactory();
    }
    public void populateDB() {

        PlaygroundDAO playgroundDAO = new PlaygroundDAO(emf);
        ChildDAO childDAO = new ChildDAO(emf);
        UserDAO userDAO = new UserDAO(emf);

        User user = User.builder().parentName("Niels").email("niels@.dk").password("12345678").build();

        userDAO.create(user);
        System.out.println(userDAO.getAll());
        System.out.println(userDAO.getUserCount());




        Playground pg = Playground.builder().name("Fælledparken").capacity(1).latitude(123.3).longitude(3.14).build();
        playgroundDAO.create(pg);
        System.out.println(playgroundDAO.getAll());
        System.out.println(playgroundDAO.getPlaygroundCount());

        Child child = Child.builder().age(1).gender(Gender.Fluid).sex(Sex.BOY).name("Henrik").build();
        Child child2 = Child.builder().age(1).gender(Gender.Fluid).sex(Sex.BOY).name("Henrik").build();
        user.addChild(child);
        user.addChild(child2);
        userDAO.update(user);
        System.out.println(childDAO.getChildCount());
        System.out.println(childDAO.getAllChildren());

        System.out.println(childDAO.getAllChildren());
        System.out.println(playgroundDAO.getAll());
        System.out.println(userDAO.getAll());

        Facility facility = Facility.builder().basketballCourt(true).build();
        pg.addFacility(facility);
        playgroundDAO.update(pg);
    }
}
