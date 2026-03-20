package app.controllers;

import app.dtos.PlaygroundDTO;
import app.services.entityService.PlaygroundService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class PlaygroundController {

    private final PlaygroundService playgroundService;

    public PlaygroundController(EntityManagerFactory emf) {
        this.playgroundService = new PlaygroundService(emf);
    }

    public void getPlaygrounds(Context ctx){
        ctx.json(playgroundService.getAll());
        ctx.status(HttpStatus.OK);
    }

    public void createPlayground(Context ctx){
        PlaygroundDTO[] playgroundDTOS = ctx.bodyAsClass(PlaygroundDTO[].class);
        ctx.json(playgroundService.createPlaygrounds(playgroundDTOS));
        ctx.status(HttpStatus.CREATED);
    }

    public void create(Context ctx){
        PlaygroundDTO playgroundDTO = ctx.bodyAsClass(PlaygroundDTO.class);
        ctx.json(playgroundService.create(playgroundDTO));
        ctx.status(HttpStatus.CREATED);
    }

    public void delete(Context ctx){
        Integer id = getId(ctx);
        boolean deleted = playgroundService.delete(id);

        if(deleted){
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "Playground deleted",
                    "id", id
            ));
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No playground found with id",
                    "id", id
            ));
        }
    }

    public void update(Context ctx){
        Integer id = getId(ctx);
        PlaygroundDTO playgroundDTO = ctx.bodyAsClass(PlaygroundDTO.class);

        if (playgroundService.getById(id) != null){
            playgroundDTO.setId(id);
            playgroundService.update(playgroundDTO);
            ctx.status(HttpStatus.OK);
            ctx.json(playgroundDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No playground found with id",
                    "id", id
            ));
        }
    }

    public void getById(Context ctx){
        Integer id = getId(ctx);
        PlaygroundDTO playgroundDTO = playgroundService.getById(id);

        if(playgroundDTO != null){
            ctx.status(HttpStatus.OK);
            ctx.json(playgroundDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No playground found with id",
                    "id", id
            ));
        }
    }

    private Integer getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class).get();
    }
}