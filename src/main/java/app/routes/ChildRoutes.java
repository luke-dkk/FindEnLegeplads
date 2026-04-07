package app.routes;

import app.controllers.ChildController;
import io.javalin.apibuilder.EndpointGroup;

public class ChildRoutes {
    private final ChildController childController;

    public ChildRoutes(ChildController childController) {
        this.childController = childController;
    }

    public EndpointGroup getRoutes(){
        return () -> {
            // get("/", childController::getChildren);
            // get("/{id}", childController::getById);
            // post("/", childController::createChild);
            // delete("/{id}", childController::delete);
            // put("/{id}", childController::update);
        };


    }

}
