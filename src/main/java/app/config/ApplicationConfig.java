package app.config;

import app.dtos.AuthUserDTO;
import app.services.security.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.config.JavalinConfig;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ApplicationConfig {
    private final SecurityService securityService;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private final List<EndpointGroup> routes = new ArrayList<>();
    private final List<Consumer<JavalinConfig>> configSteps = new ArrayList<>();

    private Javalin app;

    public ApplicationConfig(SecurityService securityService) {
        this.securityService = securityService;
        configSteps.add(this::applyBaseConfig);
    }

    public ApplicationConfig route(EndpointGroup route) {
        routes.add(route);
        return this;
    }

    public ApplicationConfig cors() {
        configSteps.add(config -> {
            config.bundledPlugins.enableCors(cors ->
                    cors.addRule(rule -> rule.anyHost())
            );
            config.bundledPlugins.enableHttpAllowedMethodsOnRoutes();
        });
        return this;
    }

    public ApplicationConfig apiExceptions() {
        configSteps.add(config ->
                config.routes.exception(RuntimeException.class, (e, ctx) -> {
                    ObjectNode body = jsonMapper.createObjectNode()
                            .put("status", 400)
                            .put("msg", e.getMessage());
                    ctx.status(400).json(body);
                })
        );
        return this;
    }

    public ApplicationConfig exceptions() {
        configSteps.add(config ->
                config.routes.exception(Exception.class, (e, ctx) -> {
                    ObjectNode body = jsonMapper.createObjectNode()
                            .put("status", 500)
                            .put("msg", "An unexpected error occurred");

                    logger.error("Unhandled exception", e);
                    ctx.status(500).json(body);
                })
        );
        return this;
    }

    public ApplicationConfig notFound() {
        configSteps.add(config ->
                config.routes.error(404, ctx -> {
                    ObjectNode body = jsonMapper.createObjectNode()
                            .put("msg", "Not found");
                    ctx.json(body);
                })
        );
        return this;
    }

    public ApplicationConfig requestLogger() {
        configSteps.add(config ->
                config.routes.before(ctx ->
                        System.out.println(ctx.method() + " " + ctx.path())
                )
        );
        return this;
    }

    public Javalin start(int port) {
        app = Javalin.create(config -> {

            for (Consumer<JavalinConfig> step : configSteps) {
                step.accept(config);
            }

            for (EndpointGroup route : routes) {
                config.routes.apiBuilder(route);
            }
        });

        app.start(port);
        return app;
    }

    public void stop() {
        if (app != null) {
            app.stop();
            app = null;
        }
    }

    private void applyBaseConfig(JavalinConfig config) {
        config.http.defaultContentType = "application/json";
        config.router.contextPath = "/api";

        config.bundledPlugins.enableDevLogging();

        config.events.serverStarted(() ->
                System.out.println("Server started: http://localhost:7075/api or started on server")
        );

        config.events.serverStopped(() ->
                System.out.println("Server stopped")
        );
    }

public ApplicationConfig auth() {
    configSteps.add(config ->
            config.routes.beforeMatched(ctx -> {

                var allowedRoles = ctx.routeRoles()
                        .stream()
                        .map(role -> role.toString())
                        .collect(java.util.stream.Collectors.toSet());

                if (allowedRoles.isEmpty() || allowedRoles.contains("ANYONE")) {
                    return;
                }

                AuthUserDTO user = securityService.verifyTokenFromHeader(ctx);

                ctx.attribute("user", user);

                boolean hasRole = user.roles().stream()
                        .anyMatch(role -> allowedRoles.contains(role));

                if (!hasRole) {
                    throw new ForbiddenResponse("Forbidden");
                }
            })
    );
    return this;
}

    public ApplicationConfig routeOverview() {
        configSteps.add(config ->
                config.bundledPlugins.enableRouteOverview("/routes")
        );
        return this;
    }
}