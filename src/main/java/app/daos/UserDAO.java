package app.daos;


import app.entities.Child;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PrePersist;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserDAO implements IDAO<User> {
    private static EntityManagerFactory emf;

    public UserDAO (EntityManagerFactory emf) {
        this.emf = emf;
    }

    public User create(User u) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        }
        return u;
    }
    public Long getUserCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return q1.getSingleResult();

        }
    }
    @Override
    public Set<User> getAll() {

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return new HashSet<>(query.getResultList());
        }
    }


    @Override
    public User getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(User.class, id);
        }
    }

    @Override
    public User update(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        }
    }

    @Override
    public User delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User userToDelete = em.find(User.class, id);
            if (userToDelete != null) {
                em.remove(userToDelete);
            }
            em.getTransaction().commit();
            return userToDelete;
        }
    }




}

