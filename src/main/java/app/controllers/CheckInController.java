package app.controllers;

import app.daos.CheckInDAO;
import app.daos.UserDAO;
import app.dtos.CheckInDTO;
import app.dtos.AuthUserDTO;
import app.entities.User;
import app.services.entityService.CheckInService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CheckInController {

    private final CheckInService checkInService;
    private final Logger logger = LoggerFactory.getLogger(CheckInController.class);
    private final CheckInDAO checkInDAO;

    public CheckInController(EntityManagerFactory emf) {
        this.checkInService = new CheckInService(emf);
        this.checkInDAO = new CheckInDAO(emf);
    }

    public void create(Context ctx) {
//        AuthUserDTO user = ctx.attribute("user");
        Integer playgroundId = ctx.pathParamAsClass("id", Integer.class).get();
        CheckInDTO dto = ctx.bodyAsClass(CheckInDTO.class);
        dto.setPlaygroundId(playgroundId);
        CheckInDTO created = checkInService.create(dto, dto.getUserId());

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
//    public void checkout(Context ctx) {
//
//        Integer checkInId = ctx.pathParamAsClass("id", Integer.class).get();
//        CheckInDTO dto = ctx.bodyAsClass(CheckInDTO.class);
//        dto.setPlaygroundId(dto.getPlaygroundId());
//        CheckInDTO updated = checkInService.checkout(checkInId, dto.getUserId());
//        ctx.json(updated);
//    }

    public void createCheckIn(Context ctx) {

        CheckInDTO dto = ctx.bodyAsClass(CheckInDTO.class);

        Integer playgroundId =Integer.parseInt(ctx.pathParam("id"));

        dto.setPlaygroundId(playgroundId);

        AuthUserDTO authUser = ctx.attribute("user");

        try {
            CheckInDTO created = checkInDAO.createCheckIn(dto, authUser);
            ctx.status(HttpStatus.CREATED);
            ctx.json(created);
        }
        catch (RuntimeException e)
        {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of(
                    "message",
                    e.getMessage()
            ));
        }

    }
    public void checkoutFromPlayground(Context ctx) {
        Integer playgroundId =Integer.parseInt(ctx.pathParam("id"));
        AuthUserDTO authUser =ctx.attribute("user");
        CheckInDTO response =checkInDAO.checkoutFromPlayground(playgroundId,authUser.id());
        ctx.status(HttpStatus.OK);
        ctx.json(response);
    }
    public void checkoutFromEverywhere(Context ctx) {
        AuthUserDTO authUser =ctx.attribute("user");
        List<CheckInDTO> response =checkInDAO.checkoutFromEveryWhere(authUser.id());
        ctx.status(HttpStatus.OK);
        ctx.json(response);
    }

    public void getCheckinsByUserId(Context ctx){
        Integer userId = Integer.parseInt(ctx.pathParam("userId"));
        Set<CheckInDTO> checkIns = checkInDAO.getActiveCheckIns(userId);
        ctx.status(HttpStatus.OK);
        ctx.json(checkIns);
    }
}