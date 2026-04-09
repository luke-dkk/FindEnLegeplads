package config;

import app.config.ApplicationConfig;
import app.config.HibernateBaseProperties;
import app.config.HibernateEmfBuilder;
import app.controllers.*;
import app.controllers.SecurityController;
import app.routes.*;
import app.services.entityService.PlaygroundService;
import app.services.entityService.UserService;
import app.services.security.SecurityService;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.ServerSocket;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoutesTest {

    private Javalin app;
    private EntityManagerFactory emf;
    private int port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    void setup() {
        postgres.start();

        Properties props = HibernateBaseProperties.createBase();

        props.put("hibernate.connection.url", postgres.getJdbcUrl());
        props.put("hibernate.connection.username", postgres.getUsername());
        props.put("hibernate.connection.password", postgres.getPassword());
        props.put("hibernate.hbm2ddl.auto", "create-drop");

        emf = HibernateEmfBuilder.build(props);

        UserService userService = new UserService(emf);
        SecurityService securityService = new SecurityService(userService);

        UserController userController = new UserController(userService);
        ChildController childController = new ChildController(emf);
        SecurityController securityController = new SecurityController(securityService, userService);
        CheckInController checkInController = new CheckInController(emf);
        PlaygroundController playgroundController = new PlaygroundController(new PlaygroundService(emf), securityService);

        UserRoutes userRoutes = new UserRoutes(userController, childController);
        PlaygroundRoutes playgroundRoutes = new PlaygroundRoutes(playgroundController, checkInController);

        Routes routes = new Routes(
                userRoutes,
                playgroundRoutes,
                securityController,
                checkInController
        );

        ApplicationConfig config = new ApplicationConfig(securityService)
                .route(routes.getRoutes())
                .auth()
                .apiExceptions()
                .exceptions()
                .cors();

        port = findFreePort();
        app = config.start(port);


        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @AfterAll
    void teardown() {
        if (app != null) app.stop();
        if (emf != null) emf.close();
        postgres.stop();
    }

    @Test
    void shouldRegisterAndLogin() {
        String email = randomEmail();

        given()
                .contentType("application/json")
                .body(userJson(email, "12345678"))
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("msg", equalTo("User created"));

        given()
                .contentType("application/json")
                .body(loginJson(email, "12345678"))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("email", equalTo(email));
    }

    @Test
    void shouldRejectUnauthorizedAccess() {
        given()
                .when()
                .get("/fortest")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldAllowAuthorizedAccess() {
        String token = registerAndLogin();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/users")
                .then()
                .statusCode(200);
    }



    private String registerAndLogin() {
        String email = randomEmail();
        String password = "12345678";

        given()
                .contentType("application/json")
                .body(userJson(email, password))
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        return given()
                .contentType("application/json")
                .body(loginJson(email, password))
                .when()
                .post("/auth/login")
                .then()
                .extract()
                .path("token");
    }

    private String userJson(String email, String password) {
        return """
        {
          "email": "%s",
          "password": "%s",
          "parentName": "Test Parent"
        }
        """.formatted(email, password);
    }

    private String loginJson(String email, String password) {
        return """
        {
          "email": "%s",
          "password": "%s"
        }
        """.formatted(email, password);
    }

    private String randomEmail() {
        return "test-" + UUID.randomUUID() + "@example.com";
    }

    private int findFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}