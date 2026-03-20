package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;

import app.controllers.ChildController;
import app.controllers.FacilityController;
import app.controllers.PlaygroundController;
import app.controllers.UserController;
import app.daos.PlaygroundDAO;
import app.entities.*;
import app.routes.*;
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
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static final String API_KEY = System.getenv("GOOGLE_API_KEY");


    public static void initiate() throws IOException, InterruptedException {
//
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        UserController userController = new UserController(emf);
        PlaygroundController playgroundController = new PlaygroundController(emf);
        FacilityController facilityController = new FacilityController(emf);
        ChildController childController = new ChildController(emf);
        UserRoutes userRoutes = new UserRoutes(userController);
        PlaygroundRoutes playgroundRoutes = new PlaygroundRoutes(playgroundController);
        FacilityRoutes facilityRoutes = new FacilityRoutes(facilityController);
        ChildRoutes childRoutes = new ChildRoutes(childController);



        Routes routes = new Routes(userRoutes, playgroundRoutes, facilityRoutes, childRoutes);
        ApplicationConfig applicationConfig = new ApplicationConfig(routes);
        Javalin app = applicationConfig.startServer(7070);


        double latitude = 55.68;
        double longitude = 12.57;
        int radius = 1000;

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + latitude + "," + longitude
                + "&radius=" + radius
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

        for (JsonNode place : results) {

            String name = place.get("name").asText();

//            JsonNode location = place
//                    .get("geometry")
//                    .get("location");
//
//            double lat = location.get("lat").asDouble();
//            double lng = location.get("lng").asDouble();

            System.out.println("Name: " + name);
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            System.out.println("----------------------------");
            Playground playground = Playground.builder()
                    .name(name)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            PlaygroundDAO playgroundDAO = new PlaygroundDAO(emf);
            playgroundDAO.create(playground);
        }
//
//        Populate populate = new Populate(emf);
//        populate.populate();

//        }
    }
}

