package app.routes;

import app.controllers.ChildController;
import app.controllers.UserController;
import app.services.security.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;


public class UserRoutes {
    private final UserController userController;
    private final ChildController childController;

    public UserRoutes(UserController userController, ChildController childController) {
        this.userController = userController;
        this.childController = childController;
    }

     public EndpointGroup getRoutes() {
        return () -> {
            get("/", userController::getAll, Role.ANYONE);
            get("/{id}", userController::getById, Role.ADMIN);
            post("/", userController::create);
            delete("/{id}", userController::delete, Role.ADMIN);
            put("/{id}", userController::update, Role.USER);
            post("/role", userController::addRole, Role.ADMIN);
            path("/{userId}/children", () -> {
                get(childController::getByUser, Role.USER);
                post(childController::createForUser, Role.USER);
            });

        };
    }
}
