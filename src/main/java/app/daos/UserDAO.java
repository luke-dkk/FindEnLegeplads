package app.daos;


import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDAO {
    private static EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public User createPoint(User u) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        }
        return u;

    }

    public Long getUserCount() {
        try (EntityManager em = emf.createEntityManager()) {
            // Find the number of Point objects in the database:
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return q1.getSingleResult();

        }
    }



    public List<User> getAllUsers() {

        try (EntityManager em = emf.createEntityManager()) {
            // Retrieve all the Point objects from the database:
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();


        }
    }
}

