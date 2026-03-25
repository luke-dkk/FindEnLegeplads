package app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.config.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ApplicationConfig {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private final List<EndpointGroup> routes = new ArrayList<>();
    private final List<Consumer<JavalinConfig>> configSteps = new ArrayList<>();

    private Javalin app;

    public ApplicationConfig() {
        configSteps.add(this::applyBaseConfig);
    }

    // ✅ Add routes
    public ApplicationConfig route(EndpointGroup route) {
        routes.add(route);
        return this;
    }

    // ✅ Enable CORS
    public ApplicationConfig cors() {
        configSteps.add(config -> {
            config.bundledPlugins.enableCors(cors ->
                    cors.addRule(rule -> rule.anyHost())
            );
            config.bundledPlugins.enableHttpAllowedMethodsOnRoutes();
        });
        return this;
    }

    // ✅ Handle custom API exceptions (optional)
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

    // ✅ Handle all other exceptions
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

    // ✅ 404 handler
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

    // ✅ Request logger (simple)
    public ApplicationConfig requestLogger() {
        configSteps.add(config ->
                config.routes.before(ctx ->
                        System.out.println(ctx.method() + " " + ctx.path())
                )
        );
        return this;
    }

    // ✅ Start server
    public Javalin start(int port) {
        app = Javalin.create(config -> {

            // Apply all config steps
            for (Consumer<JavalinConfig> step : configSteps) {
                step.accept(config);
            }

            // Register routes
            for (EndpointGroup route : routes) {
                config.routes.apiBuilder(route);
            }
        });

        app.start(port);
        return app;
    }

    // ✅ Stop server
    public void stop() {
        if (app != null) {
            app.stop();
            app = null;
        }
    }

    // ✅ Base config (runs first)
    private void applyBaseConfig(JavalinConfig config) {
        config.http.defaultContentType = "application/json";
        config.router.contextPath = "/api";

        config.bundledPlugins.enableDevLogging();

        config.events.serverStarted(() ->
                System.out.println("Server started: http://localhost:7070/api")
        );

        config.events.serverStopped(() ->
                System.out.println("Server stopped")
        );
    }
}