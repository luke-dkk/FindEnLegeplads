package app.services.security;

import app.entities.User;
import app.dtos.AuthUserDTO;
import app.services.entityService.UserService;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class SecurityService {

    private final UserService userService;
    private final JwtService jwtService;

    public SecurityService(UserService userService) {
        this.userService = userService;
        this.jwtService = new JwtService();
    }

    public String login(String email, String password) {

        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UnauthorizedResponse("User could not be validated");
        }

        if (!user.validatePassword(password)) {
            throw new UnauthorizedResponse("User could not be validated");
        }

        return jwtService.generateToken(
                new AuthUserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getRolesAsStrings()
                )
        );
    }
    public AuthUserDTO verifyTokenFromHeader(Context ctx) {
        String header = ctx.header("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("Missing token");
        }

        String token = header.substring(7);

        return jwtService.validateToken(token);
    }
    public AuthUserDTO verifyToken(String token) {
        return jwtService.validateToken(token);
    }
}