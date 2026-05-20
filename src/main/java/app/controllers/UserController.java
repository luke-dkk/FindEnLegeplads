package app.controllers;

import app.dtos.ChildDTO;
import app.dtos.RoleRequest;
import app.dtos.UserDTO;
import app.services.entityService.ChildService;
import app.services.entityService.UserService;
import app.dtos.AuthUserDTO;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;

public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ChildService childService;

    public UserController(UserService userService, ChildService childService) {
        this.userService = userService;
        this.childService = childService;
    }

    public void getAll(Context ctx) {
        ctx.json(userService.getAll());
        ctx.status(HttpStatus.OK);
    }

    public void getById(Context ctx) {
        Integer id = getId(ctx);

        UserDTO user = userService.getById(id);
        HashSet<ChildDTO> child = childService.getByUserId(id);
        user.setChildren(child);

        if (user != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(user);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No user found with id", id));
        }
    }

    public void create(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

        UserDTO created = userService.create(userDTO);

        ctx.status(HttpStatus.CREATED);
        ctx.json(created);
        logger.info("User created with id: {}", created.getId());
    }

    public void delete(Context ctx) {
        AuthUserDTO user = ctx.attribute("user");

        if (!user.roles().contains("ADMIN")) {
            logger.debug("User with email {} attempted to delete user without ADMIN role", user.email());
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
            logger.info("User with id {} deleted by admin {}", id, user.email());
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(error("No user found with id", id));
            logger.debug("Admin {} attempted to delete non-existent user with id {}", user.email(), id);
        }
    }

    public void update(Context ctx) {
        AuthUserDTO user = ctx.attribute("user");

        if (!user.roles().contains("ADMIN")) {
            logger.debug("User with email {} attempted to update user without ADMIN role", user.email());
            throw new ForbiddenResponse("Requires ADMIN role");
        }

        Integer id = getId(ctx);
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

        if (userService.getById(id) != null) {
            userDTO.setId(id);
            UserDTO updated = userService.update(userDTO);

            ctx.status(HttpStatus.OK);
            ctx.json(updated);
            logger.info("User with id {} updated by admin {}", id, user.email());
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
            logger.debug("User with email {} attempted to add role without ADMIN role", currentUser.email());
            throw new ForbiddenResponse("Requires ADMIN role");
        }

        RoleRequest request = ctx.bodyAsClass(RoleRequest.class);

        userService.addRole(request.getEmail(), request.getRole());

        ctx.status(200).json(Map.of(
                "message", "Role added",
                "email", request.getEmail(),
                "role", request.getRole()
        ));
        logger.info("Role {} added to user {} by admin {}", request.getRole(), request.getEmail(), currentUser.email());
    }
}