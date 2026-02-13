package app.daos;


import app.entities.Child;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PrePersist;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;

public class ChildDAO implements IDAO<Child>
{
    private static EntityManagerFactory emf;

    public ChildDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }



    @Override
    public Child create(Child c) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return c;
        }

    }

    public Long getChildCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(c) FROM Child c", Long.class);
            return q1.getSingleResult();

        }
    }



    public List<Child> getAllChildren() {

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Child> query = em.createQuery("SELECT c FROM Child c", Child.class);
            return query.getResultList();


        }
    }

    @Override
    public Child getById(int id) {
        return null;
    }

    @Override
    public Child update(Child child) {
        return null;
    }

    @Override
    public Child delete(int id) {
        return null;
    }

    @Override
    public Set<Child> getAll() {
        return Set.of();
    }
}

