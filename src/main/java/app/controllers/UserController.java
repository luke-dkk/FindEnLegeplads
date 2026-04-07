package app.controllers;

import app.dtos.RoleRequest;
import app.dtos.UserDTO;
import app.services.entityService.UserService;
import app.dtos.AuthUserDTO;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔓 Alle logged-in users
    public void getAll(Context ctx) {
        ctx.json(userService.getAll());
        ctx.status(HttpStatus.OK);
    }

    // 🔓 Alle logged-in users
    public void getById(Context ctx) {
        Integer id = getId(ctx);

        UserDTO user = userService.getById(id);

        if (user != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(user);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No user found with id", id));
        }
    }

    // 🔓 (eller ADMIN hvis du vil stramme den senere)
    public void create(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

        UserDTO created = userService.create(userDTO);

        ctx.status(HttpStatus.CREATED);
        ctx.json(created);
    }

    // 🔐 ADMIN ONLY
    public void delete(Context ctx) {
        AuthUserDTO user = ctx.attribute("user");

        if (!user.roles().contains("ADMIN")) {
            throw new ForbiddenResponse("Requires ADMIN role");
        }

        Integer id = getId(ctx);
        boolean deleted = userService.delete(id);

        if (deleted) {
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of(
                    "message", "User deleted",
                    "id", id
            ));
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No user found with id", id));
        }
    }

    // 🔐 ADMIN ONLY
    public void update(Context ctx) {
        AuthUserDTO user = ctx.attribute("user");

        if (!user.roles().contains("ADMIN")) {
            throw new ForbiddenResponse("Requires ADMIN role");
        }

        Integer id = getId(ctx);
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

        if (userService.getById(id) != null) {
            userDTO.setId(id);
            UserDTO updated = userService.update(userDTO);

            ctx.status(HttpStatus.OK);
            ctx.json(updated);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No user found with id", id));
        }
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
    public void addRole(Context ctx) {

        AuthUserDTO currentUser = ctx.attribute("user");

        if (!currentUser.roles().contains("ADMIN")) {
            throw new ForbiddenResponse("Requires ADMIN role");
        }

        RoleRequest request = ctx.bodyAsClass(RoleRequest.class);

        userService.addRole(request.getEmail(), request.getRole());

        ctx.status(200).json(Map.of(
                "message", "Role added",
                "email", request.getEmail(),
                "role", request.getRole()
        ));
    }
}