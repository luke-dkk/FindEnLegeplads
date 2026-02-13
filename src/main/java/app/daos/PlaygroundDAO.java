package app.daos;


import app.entities.Playground;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaygroundDAO implements IDAO<Playground>{
    private static EntityManagerFactory emf;

    public PlaygroundDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Playground create(Playground p) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        }
        return p;
    }

    @Override
    public Set<Playground> getAll() {

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Playground> query = em.createQuery("SELECT p FROM Playground p", Playground.class);
            return new HashSet(query.getResultList());
        }
    }

    public Long getPlaygroundCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(p) FROM Playground p", Long.class);
            return q1.getSingleResult();
        }
    }
    @Override
    public Playground getById(int id) {
        ;
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Playground> query = em.createQuery("SELECT p FROM Playground p WHERE p.id = :id", Playground.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
    }

    @Override
    public Playground update(Playground playground) {
        ;      try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Playground updatedPlayground = em.merge(playground);
            em.getTransaction().commit();
            return updatedPlayground;
        }
    }

    @Override
    public Playground delete(int id) {
        ;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Playground playgroundToDelete = em.find(Playground.class, id);
            if (playgroundToDelete != null) {
                em.remove(playgroundToDelete);
            }
            em.getTransaction().commit();
            return playgroundToDelete;
        }
    }
}

