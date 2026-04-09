package app.controllers;

import app.dtos.FacilityDTO;
import app.dtos.PlaygroundDTO;
import app.services.entityService.PlaygroundService;
import app.services.security.SecurityService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PlaygroundController {

    private final PlaygroundService playgroundService;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(PlaygroundController.class);


    public PlaygroundController(PlaygroundService playgroundService, SecurityService securityService) {

        this.playgroundService = playgroundService;
        this.securityService = securityService;
    }



    public void getAll(Context ctx) {
        ctx.json(playgroundService.getAll());
        ctx.status(HttpStatus.OK);
    }

    public void getById(Context ctx) {
        Integer id = getId(ctx);
        PlaygroundDTO playgroundDTO = playgroundService.getById(id);

        if (playgroundDTO != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(playgroundDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No playground found with id", id));
        }
    }

    public void create(Context ctx) {
        PlaygroundDTO playgroundDTO = ctx.bodyAsClass(PlaygroundDTO.class);
        PlaygroundDTO created = playgroundService.create(playgroundDTO);

        ctx.json(created);
        ctx.status(HttpStatus.CREATED);
        logger.info("Playground created with id: {}", created.getId());
    }

    public void createMany(Context ctx) {
        PlaygroundDTO[] playgroundDTOS = ctx.bodyAsClass(PlaygroundDTO[].class);
        ctx.json(playgroundService.createPlaygrounds(playgroundDTOS));
        ctx.status(HttpStatus.CREATED);
    }

    public void update(Context ctx) {
        Integer id = getId(ctx);
        PlaygroundDTO playgroundDTO = ctx.bodyAsClass(PlaygroundDTO.class);

        if (playgroundService.getById(id) != null) {
            playgroundDTO.setId(id);
            PlaygroundDTO updated = playgroundService.update(playgroundDTO);

            ctx.status(HttpStatus.OK);
            ctx.json(updated);
            logger.info("Playground updated with id: {}", id);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No playground found with id", id));
            logger.debug("Failed to update playground with id: {} - not found", id);
        }
    }

    public void delete(Context ctx) {
        Integer id = getId(ctx);
        boolean deleted = playgroundService.delete(id);

        if (deleted) {
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "Playground deleted",
                    "id", id
            ));
            logger.info("Playground deleted with id: {}", id);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No playground found with id", id));
            logger.debug("Failed to delete playground with id: {} - not found", id);
        }
    }


    public void getFacility(Context ctx) {
        Integer playgroundId = getId(ctx);

        FacilityDTO facility = playgroundService.getFacilityByPlaygroundId(playgroundId);

        if (facility != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(facility);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No facility found for playground", playgroundId));
            logger.debug("Failed to get facility for playground with id: {} - not found", playgroundId);
        }
    }

    public void createFacility(Context ctx) {
        Integer playgroundId = getId(ctx);
        FacilityDTO dto = ctx.bodyAsClass(FacilityDTO.class);

        FacilityDTO created = playgroundService.createFacility(playgroundId, dto);

        ctx.status(HttpStatus.CREATED);
        ctx.json(created);
    }

    public void updateFacility(Context ctx) {
        Integer playgroundId = getId(ctx);
        FacilityDTO dto = ctx.bodyAsClass(FacilityDTO.class);

        FacilityDTO updated = playgroundService.updateFacility(playgroundId, dto);

        ctx.status(HttpStatus.OK);
        ctx.json(updated);
        logger.info("Facility updated for playground with id: {}", playgroundId);
    }

    public void deleteFacility(Context ctx) {
        Integer playgroundId = getId(ctx);

        boolean deleted = playgroundService.deleteFacility(playgroundId);

        if (deleted) {
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "Facility deleted for playground",
                    "playgroundId", playgroundId

            ));
            logger.info("Facility deleted for playground with id: {}", playgroundId);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No facility found for playground", playgroundId));
            logger.debug("Failed to delete facility for playground with id: {} - not found", playgroundId);
        }
    }
    public void importPlaygrounds(Context ctx) {

        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        double lat = Double.parseDouble(body.get("lat").toString());
        double lng = Double.parseDouble(body.get("lng").toString());
        int radius = Integer.parseInt(body.get("radius").toString());

        playgroundService.importPlaygrounds(lat, lng, radius);

        ctx.json(Map.of("msg", "Playgrounds imported"));
    }

    private Integer getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class).get();
    }

    private Map<String, Object> error(String message, Integer id) {
        return Map.of(
                "message", message,
                "id", id
        );
    }

}