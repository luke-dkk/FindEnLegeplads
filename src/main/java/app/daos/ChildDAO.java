package app.daos;

import app.entities.Child;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChildDAO implements IDAO<Child> {

    private static EntityManagerFactory emf;

    public ChildDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    public Child create(Child child) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(child);
            em.getTransaction().commit();
            return child;
        }
    }

    @Override
    public Child getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Child.class, id);
        }
    }

    @Override
    public Set<Child> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Child> query = em.createQuery(
                    "SELECT c FROM Child c", Child.class);
            return new HashSet<>(query.getResultList());
        }
    }

    public Child getByName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT c FROM Child c WHERE c.name = :name", Child.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }
    }

    public List<Child> getByUserId(Integer userId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT c FROM Child c WHERE c.user.id = :userId", Child.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }

    public Long getChildCount() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT COUNT(c) FROM Child c", Long.class)
                    .getSingleResult();
        }
    }


    @Override
    public Child update(Integer id, Child updatedChild) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Child child = em.find(Child.class, id);
            if (child == null) {
                em.getTransaction().rollback();
                return null;
            }

            child.setName(updatedChild.getName());
            child.setAge(updatedChild.getAge());

            em.getTransaction().commit();
            return child;
        }
    }

    // DELETE
    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Child child = em.find(Child.class, id);
            if (child == null) {
                em.getTransaction().rollback();
                return false;
            }

            em.remove(child);
            em.getTransaction().commit();
            return true;
        }
    }
}

