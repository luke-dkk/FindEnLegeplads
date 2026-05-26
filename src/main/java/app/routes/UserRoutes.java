package app.routes;

import app.controllers.CheckInController;
import app.controllers.ChildController;
import app.controllers.UserController;
import app.services.security.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;


public class UserRoutes {
    private final UserController userController;
    private final ChildController childController;
    private final CheckInController checkInController;

    public UserRoutes(UserController userController, ChildController childController, CheckInController checkInController) {
        this.userController = userController;
        this.childController = childController;
        this.checkInController = checkInController;
    }

     public EndpointGroup getRoutes() {
        return () -> {
            get("/", userController::getAll, Role.ANYONE);
            get("/{id}", userController::getById, Role.ADMIN);
            post("/register", userController::create);
            delete("/{id}", userController::delete, Role.ADMIN);
            put("/{id}", userController::update, Role.USER);
            post("/role", userController::addRole, Role.ADMIN);
            path("/{userId}/children", () -> {
                get(childController::getByUser, Role.USER);
                post(childController::createForUser, Role.USER);
                put("/{childId}", childController::update, Role.USER);
                delete("/{childId}", childController::delete, Role.USER);
                });

        };
    }
}
