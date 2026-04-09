package app.controllers;

import app.dtos.ChildDTO;
import app.services.entityService.ChildService;
import app.dtos.AuthUserDTO;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ChildController {

    private final ChildService childService;
    private final Logger logger = LoggerFactory.getLogger(ChildController.class);


    public ChildController(EntityManagerFactory emf) {
        this.childService = new ChildService(emf);
    }

    public void getChildren(Context ctx){
        ctx.json(childService.getAll());
        ctx.status(HttpStatus.OK);
    }

    public void create(Context ctx){
        ChildDTO childDTO = ctx.bodyAsClass(ChildDTO.class);
        ctx.json(childService.create(childDTO));
        ctx.status(HttpStatus.CREATED);
        logger.info("Child created with id: " + childDTO.getId());
    }

    public void delete(Context ctx){
        Integer id = getId(ctx);
        boolean deleted = childService.delete(id);

        if(deleted){
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "Child deleted",
                    "id", id
            ));
            logger.info("Child deleted with id: " + id);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No child found with id",
                    "id", id

            ));
            logger.info("child not found with id: " + id);
        }
    }

    public void update(Context ctx){
        Integer id = getId(ctx);
        ChildDTO childDTO = ctx.bodyAsClass(ChildDTO.class);

        if (childService.getById(id) != null){
            childDTO.setId(id);
            childService.update(childDTO);
            ctx.status(HttpStatus.OK);
            ctx.json(childDTO);
            logger.info("Child updated with id: " + id);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No child found with id",
                    "id", id
            ));
            logger.info("child not found with id: " + id);
        }
    }

    public void getById(Context ctx){
        Integer id = getId(ctx);
        ChildDTO childDTO = childService.getById(id);

        if(childDTO != null){
            ctx.status(HttpStatus.OK);
            ctx.json(childDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No child found with id",
                    "id", id
            ));
        }
    }

    private Integer getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class).get();
    }

    public void createForUser(Context ctx) {

        Integer userId = ctx.pathParamAsClass("userId", Integer.class).get();

        AuthUserDTO authUser = ctx.attribute("user");

        if (!authUser.id().equals(userId)) {
            logger.debug("User " + authUser.id() + " attempted to create a child for user " + userId);
            throw new ForbiddenResponse("You can only create children for yourself");
        }

        ChildDTO dto = ctx.bodyAsClass(ChildDTO.class);

        ChildDTO created = childService.createForUser(userId, dto);

        ctx.status(201).json(created);
        logger.info("Child created for user " + userId + " with child id: " + created.getId());
    }

    public void getByUser(Context ctx) {

        Integer userId = ctx.pathParamAsClass("userId", Integer.class).get();

        AuthUserDTO authUser = ctx.attribute("user");


        if (!authUser.id().equals(userId)) {
            logger.debug("User " + authUser.id() + " attempted to access children for user " + userId);
            throw new ForbiddenResponse("You can only access your own children");
        }

        ctx.json(childService.getByUserId(userId));
        logger.info("User " + authUser.id() + " accessed children for user " + userId);
    }
}