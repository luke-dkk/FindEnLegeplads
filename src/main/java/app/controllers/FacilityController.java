package app.controllers;

import app.dtos.FacilityDTO;
import app.services.entityService.FacilityService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FacilityController {

    private final FacilityService facilityService;
    private final Logger logger = LoggerFactory.getLogger(FacilityController.class);


    public FacilityController(EntityManagerFactory emf) {
        this.facilityService = new FacilityService(emf);
    }

    public void getFacilities(Context ctx){
        ctx.json(facilityService.getAll());
        ctx.status(HttpStatus.OK);
    }

    public void create(Context ctx){
        FacilityDTO facilityDTO = ctx.bodyAsClass(FacilityDTO.class);
        ctx.json(facilityService.create(facilityDTO));
        ctx.status(HttpStatus.CREATED);
        logger.info("Facility created with id: " + facilityDTO.getId());
    }

    public void delete(Context ctx){
        Integer id = getId(ctx);
        boolean deleted = facilityService.delete(id);

        if(deleted){
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "Facility deleted",
                    "id", id
            ));
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No facility found with id",
                    "id", id
            ));
        }
    }

    public void update(Context ctx){
        Integer id = getId(ctx);
        FacilityDTO facilityDTO = ctx.bodyAsClass(FacilityDTO.class);

        if (facilityService.getById(id) != null){
            facilityDTO.setId(id);
            facilityService.update(facilityDTO);
            ctx.status(HttpStatus.OK);
            ctx.json(facilityDTO);
            logger.info("Facility updated with id: " + facilityDTO.getId());
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No facility found with id",
                    "id", id
            ));
            logger.info("Failed to update facility with id: " + id + " - not found");
        }
    }

    public void getById(Context ctx){
        Integer id = getId(ctx);
        FacilityDTO facilityDTO = facilityService.getById(id);

        if(facilityDTO != null){
            ctx.status(HttpStatus.OK);
            ctx.json(facilityDTO);
            logger.info("Facility retrieved with id: " + facilityDTO.getId());
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No facility found with id",
                    "id", id
            ));
            logger.debug("Failed to retrieve facility with id: {} - not found", id);
        }
    }

    private Integer getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class).get();
    }
}