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
            get("/{id}", playgroundController::getById, Role.USER);
            post("/", playgroundController::create, Role.USER);
            put("/{id}", playgroundController::update, Role.USER);
            delete("/{id}", playgroundController::delete, Role.ADMIN);
            post("/import", playgroundController::importPlaygrounds, Role.ADMIN);

            path("/{id}/facility", () -> {
                get(playgroundController::getFacility, Role.ANYONE);
                put(playgroundController::updateFacility, Role.USER);
                post(playgroundController::createFacility, Role.USER);
                delete(playgroundController::deleteFacility, Role.ADMIN);
            });

            path("/{id}/checkins", () -> {
                post(checkInController::create, Role.USER);
                get(checkInController::getByPlayground, Role.USER);
            });
        };
    }
}