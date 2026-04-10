package app.controllers;

import app.dtos.CheckInDTO;
import app.dtos.AuthUserDTO;
import app.services.entityService.CheckInService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckInController {

    private final CheckInService checkInService;
    private final Logger logger = LoggerFactory.getLogger(CheckInController.class);

    public CheckInController(EntityManagerFactory emf) {
        this.checkInService = new CheckInService(emf);
    }

    public void create(Context ctx) {
        AuthUserDTO user = ctx.attribute("user");
        Integer playgroundId = ctx.pathParamAsClass("id", Integer.class).get();
        CheckInDTO dto = ctx.bodyAsClass(CheckInDTO.class);
        dto.setPlaygroundId(playgroundId);
        CheckInDTO created = checkInService.create(dto, user);
        ctx.status(HttpStatus.CREATED).json(created);
    }

    public void getByPlayground(Context ctx) {
        Integer playgroundId = ctx.pathParamAsClass("id", Integer.class).get();
        ctx.json(checkInService.getByPlaygroundId(playgroundId));
    }

    public void checkIn(Context ctx) {

        Integer checkInId = ctx.pathParamAsClass("id", Integer.class).get();
        AuthUserDTO user = ctx.attribute("user");
        CheckInDTO updated = checkInService.checkIn(checkInId, user);
        ctx.json(updated);
    }
    public void checkout(Context ctx) {

        Integer checkInId = ctx.pathParamAsClass("id", Integer.class).get();
        AuthUserDTO user = ctx.attribute("user");
        CheckInDTO updated = checkInService.checkout(checkInId, user);
        ctx.json(updated);
    }
}