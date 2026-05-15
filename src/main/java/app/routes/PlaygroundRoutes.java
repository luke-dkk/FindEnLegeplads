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
            post("/create", playgroundController::createPlayground, Role.ANYONE);
            put("/{id}", playgroundController::update, Role.ANYONE);
            delete("/{id}", playgroundController::delete, Role.ANYONE);
            post("/import", playgroundController::importPlaygrounds, Role.ADMIN);
            post("/nearme",playgroundController::getPlaygroundsNearMe, Role.ANYONE );

            path("/{id}/facility", () -> {
                get("",playgroundController::getFacility, Role.ANYONE);
                put("/update",playgroundController::updateFacility, Role.ANYONE);
                post("/attach",playgroundController::attachFacility, Role.ANYONE);
                delete(playgroundController::deleteFacility, Role.ANYONE);
            });


            //findenlegeplads.dk/facility/create
            //findenlegeplads.dk/playgrounds/facility/create

            //findenlegeplads.dk/playgrounds/1/facility/attach
            //findenlegeplads.dk/playgrounds/1/facility/update
            //findenlegeplads.dk/playgrounds/1/facility/update

            path("/{id}/checkins", () -> {
                post(checkInController::create, Role.ANYONE);
                get(checkInController::getByPlayground, Role.ANYONE);
            });
            path("/{id}/checkout", () -> {
                put(checkInController::checkout, Role.ANYONE);
            });
        };
    }
}