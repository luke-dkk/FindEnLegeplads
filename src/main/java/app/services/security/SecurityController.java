package app.services.security;

import app.dtos.LoginRequest;
import app.dtos.UserDTO;
import app.services.entityService.UserService;
import app.services.security.SecurityService;
import io.javalin.http.Context;

import java.util.Map;

public class SecurityController {

    private final SecurityService securityService;
    private final UserService userService;

    public SecurityController(SecurityService securityService , UserService userService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    public void login(Context ctx) {
        LoginRequest user = ctx.bodyAsClass(LoginRequest.class);

        String token = securityService.login(user.email, user.password);

        ctx.json(Map.of(
                "token", token,
                "email", user.email
        ));
    }
    public void register(Context ctx) {
        UserDTO user = ctx.bodyAsClass(UserDTO.class);

        userService.create(user);

        ctx.status(201).json(user);
    }

}