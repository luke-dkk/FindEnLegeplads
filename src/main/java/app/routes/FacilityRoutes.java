package app.routes;

import app.controllers.ChildController;
import app.controllers.FacilityController;
import io.javalin.apibuilder.EndpointGroup;

public class FacilityRoutes {
    private final FacilityController facilityController;

    public FacilityRoutes(FacilityController facilityController) {
        this.facilityController = facilityController;
    }

    public EndpointGroup getRoutes(){
        return () -> {
            // Define child-specific routes here
            // For example:
            // get("/", childController::getChildren);
            // get("/{id}", childController::getById);
            // post("/", childController::createChild);
            // delete("/{id}", childController::delete);
            // put("/{id}", childController::update);
        };


    }

}
