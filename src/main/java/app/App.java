package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.*;
import app.controllers.SecurityController;
import app.daos.PlaygroundDAO;
import app.dtos.UserDTO;
import app.entities.Playground;
import app.routes.*;
import app.services.entityService.PlaygroundService;
import app.services.entityService.UserService;
import app.services.security.SecurityService;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        ChildRoutes childRoutes = new ChildRoutes(childController);



        Routes routes = new Routes(userRoutes, playgroundRoutes, childRoutes, securityController, checkInController);
        playgroundService.importPlaygrounds(55.68, 12.57, 1000);


        ApplicationConfig applicationConfig = new ApplicationConfig(securityService)
                .route(routes.getRoutes())
                .cors()
                .auth()
                .exceptions()
                .apiExceptions()
                .notFound()
                .routeOverview()
                .requestLogger();

        Javalin app = applicationConfig.start(7075);

    }

    private static void importPlaygrounds(double latitude, double longitude, int radiusInMeters) {
        try {


            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                    + "location=" + latitude + "," + longitude
                    + "&radius=" + radiusInMeters
                    + "&keyword=playground"
                    + "&key=" + API_KEY;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.get("results");

            PlaygroundDAO playgroundDAO = new PlaygroundDAO(emf);

            for (JsonNode place : results) {

                String name = place.get("name").asText();

                Playground playground = Playground.builder()
                        .name(name)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();

                playgroundDAO.create(playground);

                System.out.println("Saved playground: " + name);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to import playgrounds");
            e.printStackTrace();
        }
    }
}