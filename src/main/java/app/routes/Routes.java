package app.routes;

import app.controllers.*;
import app.controllers.SecurityController;
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
            get("/", ctx -> ctx.result("Hello World"));
            path("/users", userRoutes.getRoutes());
            path("/playgrounds", playgroundRoutes.getRoutes());
            put("/checkins/{id}/checkin", checkInController::checkIn);
            put("/checkins/{id}/checkout", checkInController::checkout);
            path("/auth", () -> {
                post("/login", securityController::login);
                post("/register", securityController::register);
            });
        };
    }
}

