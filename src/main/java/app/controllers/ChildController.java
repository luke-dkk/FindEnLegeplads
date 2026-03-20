package app.controllers;

import app.dtos.ChildDTO;
import app.services.entityService.ChildService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class ChildController {

    private final ChildService childService;

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
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No child found with id",
                    "id", id
            ));
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
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No child found with id",
                    "id", id
            ));
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
}