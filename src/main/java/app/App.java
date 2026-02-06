package app;

import app.config.HibernateConfig;
//import app.config.ThymeleafConfig;
import app.daos.ChildDAO;
import app.daos.PlaygroundDAO;
import app.daos.UserDAO;
import app.entities.Child;
import app.entities.Playground;
import app.entities.User;
//import io.javalin.Javalin;
//import io.javalin.rendering.template.JavalinThymeleaf;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PrePersist;

public class App {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public static void initiate() {
        PlaygroundDAO playgroundDAO = new PlaygroundDAO(emf);
        ChildDAO childDAO = new ChildDAO(emf);
        UserDAO userDAO = new UserDAO(emf);

        User user = User.builder().parentName("Niels").email("niels.dk").password("1234").build();
        user.setEmail("niels");
        userDAO.createUser(user);


        Playground pg = Playground.builder().name("Fælledparken").capacity(1).latitude(123.3).longitude(3.14).build();
        playgroundDAO.createPlayground(pg);

        Child child = Child.builder().age(1).gender("fluid").sex("boy").name("Henrik").build();
        childDAO.createChild(child);

        System.out.println(childDAO.getAllChildren());
        System.out.println(playgroundDAO.getAllPlaygrounds());
        System.out.println(userDAO.getAllUsers());

//        Javalin app = Javalin.create(config ->
//        {
//            config.staticFiles.add("/public");
//            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
//            config.staticFiles.add("/templates");
//        }).start(7072);
        emf.close();
    }
}

