package app.routes;

import app.controllers.CheckInController;
import app.controllers.PlaygroundController;
import app.services.security.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PlaygroundRoutes {

    private final PlaygroundController playgroundController;
    private final CheckInController checkInController;

    public PlaygroundRoutes(PlaygroundController playgroundController,
                            CheckInController checkInController) {
        this.playgroundController = playgroundController;
        this.checkInController = checkInController;
    }

    public EndpointGroup getRoutes() {
        return () -> {

            get("/", playgroundController::getAll, Role.ANYONE);
            get("/{id}", playgroundController::getById);
            post("/", playgroundController::create);
            put("/{id}", playgroundController::update);
            delete("/{id}", playgroundController::delete);
            post("/import", playgroundController::importPlaygrounds);

            path("/{id}/facility", () -> {
                get(playgroundController::getFacility);
                put(playgroundController::updateFacility);
                post(playgroundController::createFacility);
                delete(playgroundController::deleteFacility);
            });

            path("/{id}/checkins", () -> {
                post(checkInController::create);
                get(checkInController::getByPlayground);
            });
        };
    }
}