package app.routes;

import app.controllers.PlaygroundController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;


public class PlaygroundRoutes {
    private final PlaygroundController playgroundController;

    public PlaygroundRoutes(PlaygroundController playgroundController) {
        this.playgroundController = playgroundController;
    }

        public EndpointGroup getRoutes() {
//            return () -> {
//                get("/", playgroundController::getAll);
//                get("/{id}", playgroundController::getById);
//                post("/", playgroundController::create);
//                delete("/{id}", playgroundController::delete);
//                put("/{id}", playgroundController::update);
//            };
//        }
            return () -> {
                get("/hello", ctx -> ctx.result("Hello from the playground!"));
            };
        }
}
