package app.config;

import app.entities.*;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Child.class);
        configuration.addAnnotatedClass(Playground.class);
        configuration.addAnnotatedClass(Facility.class);
        configuration.addAnnotatedClass(CheckIn.class);
        // TODO: Add more entities here...
    }
}