package app.daos;

import app.entities.Playground;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class PlaygroundDAO implements IDAO<Playground> {

    private static EntityManagerFactory emf;

    public PlaygroundDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Playground create(Playground p) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        }
        return p;
    }

    public Long getPlaygroundCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(p) FROM Playground p", Long.class);
            return q1.getSingleResult();
        }
    }

    @Override
    public Set<Playground> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Playground> query =
                    em.createQuery("SELECT p FROM Playground p", Playground.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Playground getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Playground.class, id);
        }
    }

    @Override
    public Playground update(Integer id, Playground updatedPlayground) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Playground playground = em.find(Playground.class, id);

            if (playground != null) {
                playground.setName(updatedPlayground.getName());
                playground.setLatitude(updatedPlayground.getLatitude());
                playground.setLongitude(updatedPlayground.getLongitude());
                em.getTransaction().commit();
                return updatedPlayground;
            }

            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Playground playgroundToDelete = em.find(Playground.class, id);

            if (playgroundToDelete != null) {
                em.remove(playgroundToDelete);
                em.getTransaction().commit();
                return true;
            }

            em.getTransaction().rollback();
            return false;
        }
    }
}