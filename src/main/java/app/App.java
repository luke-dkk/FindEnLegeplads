package app;

import app.config.HibernateConfig;
//import app.config.ThymeleafConfig;
import app.daos.ChildDAO;
import app.daos.PlaygroundDAO;
import app.daos.UserDAO;
import app.entities.*;
//import io.javalin.Javalin;
//import io.javalin.rendering.template.JavalinThymeleaf;
import app.utils.Populate;
import jakarta.persistence.EntityManagerFactory;

public class App {
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public static void initiate() {
        Populate populate = new Populate();
        populate.populateDB();


//        Javalin app = Javalin.create(config ->
//        {
//            config.staticFiles.add("/public");
//            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
//            config.staticFiles.add("/templates");
//        }).start(7072);
        emf.close();
    }

    private void populateDB() {

    }
}

