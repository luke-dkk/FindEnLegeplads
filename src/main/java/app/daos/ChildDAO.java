package app.daos;


import app.entities.Child;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PrePersist;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ChildDAO {
    private static EntityManagerFactory emf;

    public ChildDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @PrePersist
    public Child createChild(Child c) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;

    }

    public Long getChildCount() {
        try (EntityManager em = emf.createEntityManager()) {
            // Find the number of Point objects in the database:
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(c) FROM Child c", Long.class);
            return q1.getSingleResult();

        }
    }



    public List<Child> getAllChildren() {

        try (EntityManager em = emf.createEntityManager()) {
            // Retrieve all the Point objects from the database:
            TypedQuery<Child> query = em.createQuery("SELECT c FROM Child c", Child.class);
            return query.getResultList();


        }
    }
}

