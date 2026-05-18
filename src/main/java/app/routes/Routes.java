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
    private final PlaygroundController playgroundController;
    private final SecurityController securityController;
    private final CheckInController checkInController;

    public Routes(UserRoutes userRoutes, PlaygroundRoutes playgroundRoutes, SecurityController securityController, CheckInController checkInController, PlaygroundController playgroundController) {
        this.userRoutes = userRoutes;
        this.playgroundRoutes = playgroundRoutes;
        this.securityController = securityController;
        this.checkInController = checkInController;
        this.playgroundController= playgroundController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", ctx -> ctx.result("Hello World"), Role.ANYONE);
            get("/fortest", ctx -> ctx.result("Hello Fortest"), Role.ANYONE);
            path("/users", userRoutes.getRoutes());
            post("/facility/create", playgroundController::createFacility, Role.ANYONE);
            path("/playgrounds", playgroundRoutes.getRoutes());
            put("/checkins/{id}/checkin", checkInController::checkIn, Role.ANYONE);
            put("/checkins/{id}/checkout", checkInController::checkout, Role.ANYONE);
            path("/auth", () -> {
                post("/login", securityController::login);
                post("/register", securityController::register);
            });

        };
    }
}

