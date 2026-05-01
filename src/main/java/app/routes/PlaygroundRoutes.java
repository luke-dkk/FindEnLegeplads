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
            get("/{id}", playgroundController::getById, Role.ANYONE);
            post("/", playgroundController::create, Role.ANYONE);
            put("/{id}", playgroundController::update, Role.ANYONE);
            delete("/{id}", playgroundController::delete, Role.ANYONE);
            post("/import", playgroundController::importPlaygrounds, Role.ADMIN);

            path("/{id}/facility", () -> {
                get(playgroundController::getFacility, Role.ANYONE);
                put(playgroundController::updateFacility, Role.ANYONE);
                post(playgroundController::createFacility, Role.ANYONE);
                delete(playgroundController::deleteFacility, Role.ANYONE);
            });

            path("/{id}/checkins", () -> {
                post(checkInController::create, Role.ANYONE);
                get(checkInController::getByPlayground, Role.ANYONE);
            });
        };
    }
}