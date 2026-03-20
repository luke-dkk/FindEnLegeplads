package app.controllers;

import app.dtos.UserDTO;
import app.services.entityService.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class UserController {

    private final UserService userService;

    public UserController(EntityManagerFactory emf) {
        this.userService = new UserService(emf);
    }

    public void getUsers(Context ctx){
        ctx.json(userService.getAll());
        ctx.status(HttpStatus.OK);
    }



    public void createUser(Context ctx){
        // Modtag og konverter en liste af digte (fra json til dto)
        UserDTO[] UserDTOS = ctx.bodyAsClass(UserDTO[].class);
        // Gem alle digtene i databasen (dao) og modtag en liste af de nye digte
        ctx.json(userService.createUsers(UserDTOS));
        ctx.status(HttpStatus.CREATED);
    }

    public void create(Context ctx){
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        ctx.json(userService.create(userDTO));
        ctx.status(HttpStatus.CREATED);
    }



    public void delete(Context ctx){
        Integer id =  getId(ctx);
        boolean deleted = userService.delete(id);
        if(deleted)
        {
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "Poem deleted",
                    "id", id
            ));
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No poem found with id",
                    "id", id
            ));
        }
    }



    public void update(Context ctx){
        Integer id = getId(ctx);
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        if (userService.getById(id) != null) {
            userDTO.setId(id);
            userService.update(userDTO);
            ctx.status(HttpStatus.OK);
            ctx.json(userDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No poem found with id",
                    "id", id
            ));
        }
    }

    public void getById(Context ctx){
        int id = getId(ctx);
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        if (userDTO != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(userService.getById(userDTO.getId()));
        }
        else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(Map.of(
                    "message", "No poem found with id",
                    "id", id
            ));
        }
    }

    private Integer getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class).get();
    }
}
