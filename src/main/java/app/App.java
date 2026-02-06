package app;

import app.config.HibernateConfig;
import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import jakarta.persistence.EntityManagerFactory;

public class App {
    private static EntityManagerFactory emf;

    public static void initiate() {
        emf = HibernateConfig.getEntityManagerFactory();


        Javalin app = Javalin.create(config ->
        {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            config.staticFiles.add("/templates");
        }).start(7072);

    }
}

