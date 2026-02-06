package app.config;

import app.entities.Child;
import app.entities.Facilities;
import app.entities.Playground;
import app.entities.User;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Child.class);
        configuration.addAnnotatedClass(Playground.class);
        configuration.addAnnotatedClass(Facilities.class);
        // TODO: Add more entities here...
    }
}