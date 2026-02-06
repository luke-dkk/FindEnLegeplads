package app.config;

import app.entities.Child;
import app.entities.Playground;
import app.entities.User;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Child.class);
        configuration.addAnnotatedClass(Playground.class);
        configuration.addAnnotatedClass(User.class);
        // TODO: Add more entities here...
    }
}