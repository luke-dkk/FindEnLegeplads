package app.services.security;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    USER,
    ADMIN,
    ANYONE
}