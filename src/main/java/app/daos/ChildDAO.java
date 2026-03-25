package app.daos;

import app.entities.Child;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.Set;

public class ChildDAO implements IDAO<Child> {

    private static EntityManagerFactory emf;

    public ChildDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Child create(Child c) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        }
        return c;
    }

    public Long getChildCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(c) FROM Child c", Long.class);
            return q1.getSingleResult();
        }
    }

    @Override
    public Set<Child> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Child> query = em.createQuery("SELECT c FROM Child c", Child.class);
            return null;
        }
    }


    public Child getByName(String name) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Child> query = em.createQuery(
                    "SELECT c FROM Child c WHERE c.name = :name",
                    Child.class
            );
            query.setParameter("name", name);
            return query.getSingleResult();
        }
    }

    @Override
    public Child getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Child.class, id);
        }
    }

    @Override
    public Child update(Integer id, Child updatedChild) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Child child = em.find(Child.class, id);

            if (child != null) {
                child.setName(updatedChild.getName());
                child.setAge(updatedChild.getAge());

                em.getTransaction().commit();
                return updatedChild;
            }

            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Child childToDelete = em.find(Child.class, id);

            if (childToDelete != null) {
                em.remove(childToDelete);
                em.getTransaction().commit();
                return true;
            }

            em.getTransaction().rollback();
            return false;
        }
    }
}