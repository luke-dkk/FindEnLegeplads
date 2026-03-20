package app.routes;

import app.controllers.ChildController;
import app.controllers.FacilityController;
import app.controllers.PlaygroundController;
import app.controllers.UserController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class Routes {

    private final UserRoutes userRoutes;
    private final PlaygroundRoutes playgroundRoutes;
    private final FacilityRoutes facilityRoutes;
    private final ChildRoutes childRoutes;

    public Routes(UserRoutes userRoutes, PlaygroundRoutes playgroundRoutes, FacilityRoutes facilityRoutes, ChildRoutes childRoutes) {
        this.userRoutes = userRoutes;
        this.playgroundRoutes = playgroundRoutes;
        this.facilityRoutes = facilityRoutes;
        this.childRoutes = childRoutes;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", ctx -> ctx.result("Hello World"));
            path("/users", userRoutes.getRoutes());
            path("/playgrounds", playgroundRoutes.getRoutes());
        };
    }
}

