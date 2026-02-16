package app.daos;

import app.entities.Facility;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;


public class FacilityDAO implements IDAO<Facility> {

    private static EntityManagerFactory emf;

    public FacilityDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Facility create(Facility facility) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(facility);
            em.getTransaction().commit();
            return facility;
        }
    }

    @Override
    public Facility getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Facility.class, id);
        }
    }

    @Override
    public Facility update(Facility facility) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Facility updatedFacility = em.merge(facility);
            em.getTransaction().commit();
            return updatedFacility;
        }
    }

    @Override
    public Facility delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Facility facilityToDelete = em.find(Facility.class, id);
            if (facilityToDelete != null) {
                em.remove(facilityToDelete);
            }
            em.getTransaction().commit();
            return facilityToDelete;
        }
    }

    @Override
    public Set<Facility> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Facility> query = em.createQuery("SELECT u FROM User u", Facility.class);
            return new HashSet<>(query.getResultList());
        }
    }
}
