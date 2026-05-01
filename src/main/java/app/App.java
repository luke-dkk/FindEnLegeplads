package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.*;
import app.dtos.UserDTO;
import app.routes.*;
import app.services.entityService.PlaygroundService;
import app.services.entityService.UserService;
import app.services.security.SecurityController;
import app.services.security.SecurityService;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;


public class App {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static final String API_KEY = System.getenv("GOOGLE_API_KEY");

    public static void initiate() {
        UserService userService = new UserService(emf);

        if (userService.findByEmail("admin@test.com") == null) {
            UserDTO admin = new UserDTO();
            admin.setEmail("admin@test.com");
            admin.setPassword("1234");
            admin.setParentName("Admin");

            userService.create(admin);

            userService.addRole("admin@test.com", "ADMIN");
        }


        SecurityService securityService = new SecurityService(userService);
        SecurityController securityController = new SecurityController(securityService, userService);

        UserController userController = new UserController(userService);
        PlaygroundService playgroundService = new PlaygroundService(emf);
        PlaygroundController playgroundController = new PlaygroundController(playgroundService, securityService);
        ChildController childController = new ChildController(emf);
        CheckInController checkInController = new CheckInController(emf);

        UserRoutes userRoutes = new UserRoutes(userController, childController);
        PlaygroundRoutes playgroundRoutes = new PlaygroundRoutes(playgroundController, checkInController);


        Routes routes = new Routes(userRoutes, playgroundRoutes, securityController, checkInController);
        playgroundService.importPlaygrounds(55.68, 12.57, 1000);


        ApplicationConfig applicationConfig = new ApplicationConfig(securityService)
                .cors()
                .route(routes.getRoutes())
                .auth()
                .exceptions()
                .apiExceptions()
                .notFound()
                .routeOverview()
                .requestLogger();

        Javalin app = applicationConfig.start(7075);

    }
}