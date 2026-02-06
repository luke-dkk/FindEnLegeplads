package app.daos;


import app.entities.Playground;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PlaygroundDAO {
    private static EntityManagerFactory emf;

    public PlaygroundDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Playground createPlayground(Playground p) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        }
        return p;

    }

    public Long getPlaygroundCount() {
        try (EntityManager em = emf.createEntityManager()) {
            // Find the number of Point objects in the database:
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(p) FROM Playground p", Long.class);
            return q1.getSingleResult();

        }
    }



    public List<Playground> getAllPlaygrounds() {

        try (EntityManager em = emf.createEntityManager()) {
            // Retrieve all the Point objects from the database:
            TypedQuery<Playground> query = em.createQuery("SELECT p FROM Playground p", Playground.class);
            return query.getResultList();


        }
    }
}

