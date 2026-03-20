package app.routes;

import app.controllers.UserController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;


public class UserRoutes {
    private final UserController userController;

    public UserRoutes(UserController userController) {
        this.userController = userController;
    }

     public EndpointGroup getRoutes() {
        return () -> {
            get("/", userController::getUsers);
            get("/{id}", userController::getById);
            post("/", userController::createUser);
            delete("/{id}", userController::delete);
            put("/{id}", userController::update);
        };
    }
}
