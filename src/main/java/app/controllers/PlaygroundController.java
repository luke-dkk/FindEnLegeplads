package app.controllers;

import app.daos.FacilityDAO;
import app.daos.PlaygroundDAO;
import app.dtos.AttachFacilityDTO;
import app.dtos.FacilityDTO;
import app.dtos.LocationDTO;
import app.dtos.PlaygroundDTO;
import app.entities.Facility;
import app.services.entityService.PlaygroundService;
import app.services.mappers.FacilityMapper;
import app.services.security.SecurityService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlaygroundController {
    private final FacilityDAO facilityDAO;
    private final PlaygroundService playgroundService;
    private final SecurityService securityService;
    private final FacilityMapper facilityMapper;
    private final PlaygroundDAO playgroundDAO;
    private final Logger logger = LoggerFactory.getLogger(PlaygroundController.class);


    public PlaygroundController(PlaygroundService playgroundService, SecurityService securityService, EntityManagerFactory emf) {
        this.facilityDAO = new FacilityDAO(emf);
        this.playgroundService = playgroundService;
        this.securityService = securityService;
        this.facilityMapper = new FacilityMapper(emf);
        this.playgroundDAO = new PlaygroundDAO(emf);
    }

    public void createFacility(Context ctx){
        FacilityDTO dto = ctx.bodyAsClass(FacilityDTO.class);
        FacilityDTO created = playgroundService.createFacility(dto);
        ctx.json(created);
        ctx.status(HttpStatus.CREATED);
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

    public void createPlayground(Context ctx) {
        PlaygroundDTO facilityDTO = ctx.bodyAsClass(PlaygroundDTO.class);
        PlaygroundDTO created = playgroundService.create(facilityDTO);

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
        Integer facilityID = getId(ctx);

        Facility facility = facilityDAO.getById(facilityID);

        if (facility != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(facility);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No facility found for playground", facilityID));
            logger.debug("Failed to get facility for playground with id: {} - not found", facilityID);
        }
    }


    public void getAllFacilities(Context ctx) {

        Set<Facility> facility = facilityDAO.getAll();
        Set<FacilityDTO> facilityDTOS = facilityMapper.loopToDTO(facility);



        if (facilityDTOS != null) {
            ctx.json(facilityDTOS);
            ctx.status(HttpStatus.OK);

        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No facilities found", null));
            logger.debug("Failed to find facilities - none found");
        }
    }

    public void attachFacility(Context ctx) {
        AttachFacilityDTO dto = ctx.bodyAsClass(AttachFacilityDTO.class);

        PlaygroundDTO updated = playgroundDAO.attachFacility(
                dto.getPlaygroundId(),
                dto.getFacilityId()
        );
        String facilityName = facilityDAO.getById(dto.getFacilityId()).getFacility();
        String playgroundName = playgroundDAO.getById(dto.getPlaygroundId()).getName();

        AttachFacilityDTO response = new AttachFacilityDTO(playgroundName,facilityName,dto.getPlaygroundId(),dto.getFacilityId());
        ctx.status(HttpStatus.CREATED);
        ctx.json(response);
    }

    public void updateFacility(Context ctx) {
        Integer facilityId = getId(ctx);
        FacilityDTO dto = ctx.bodyAsClass(FacilityDTO.class);
        Facility updated = facilityDAO.update(facilityId, facilityMapper.toSingleFacility(dto));

        ctx.status(HttpStatus.OK);
        ctx.json(updated);
        logger.info("Facility updated for playground with id: {}", facilityId);
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

    public void getPlaygroundsNearMe( Context context) {

        LocationDTO location = context.bodyAsClass(LocationDTO.class);

        double lat = location.getLatitude();
        double lon = location.getLongitude();
        int radius = location.getRadiusInMeters();
        int page = location.getPage();
        int size = location.getSize();



       List<PlaygroundDTO> playgroundDTOList= playgroundService.getPlaygroundNearClient(lat, lon, radius, page, size);

       context.json(playgroundDTOList);
       context.status(HttpStatus.OK);
    }

    public void createAndAttachFacility(Context ctx) {

        AttachFacilityDTO dto = ctx.bodyAsClass(AttachFacilityDTO.class);
        Facility facility =facilityDAO.findByName(dto.getFacilityName());

        if (facility == null)
        {
            FacilityDTO created = playgroundService.createFacility(new FacilityDTO(
                    dto.getFacilityName()));

            facility =facilityDAO.getById(created.getId());
        }

        playgroundDAO.attachFacility(dto.getPlaygroundId(),facility.getId());

        AttachFacilityDTO response =new AttachFacilityDTO(
                        facility.getFacility(),
                        facility.getId()
                );
        ctx.status(HttpStatus.CREATED);
        ctx.json(response);
    }
}
