package app.routes;

import app.controllers.*;
import app.services.security.Role;
import app.services.security.SecurityController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class Routes {

    private final UserRoutes userRoutes;
    private final PlaygroundRoutes playgroundRoutes;
    private final SecurityController securityController;
    private final CheckInController checkInController;

    public Routes(UserRoutes userRoutes, PlaygroundRoutes playgroundRoutes, SecurityController securityController, CheckInController checkInController) {
        this.userRoutes = userRoutes;
        this.playgroundRoutes = playgroundRoutes;
        this.securityController = securityController;
        this.checkInController = checkInController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", ctx -> ctx.result("Hello World"), Role.ADMIN);
            get("/fortest", ctx -> ctx.result("Hello Forest"), Role.USER);
            path("/users", userRoutes.getRoutes());
            path("/playgrounds", playgroundRoutes.getRoutes());
            put("/checkins/{id}/checkin", checkInController::checkIn, Role.USER);
            put("/checkins/{id}/checkout", checkInController::checkout, Role.USER);
            path("/auth", () -> {
                post("/login", securityController::login);
                post("/register", securityController::register);
            });
        };
    }
}

