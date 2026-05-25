package app.daos;

import app.entities.Facility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacilityDAO implements IDAO<Facility> {

    private static EntityManagerFactory emf;

    public FacilityDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Facility create(Facility facility) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            List<Facility> existingFacilities = em.createQuery(
                            "SELECT f FROM Facility f WHERE LOWER(f.facility) = :name",
                            Facility.class
                    )
                    .setParameter("name", facility.getFacility().toLowerCase())
                    .getResultList();

            if (!existingFacilities.isEmpty()) {
                return existingFacilities.get(0);
            }

            em.persist(facility);

            em.getTransaction().commit();

            return facility;
        }
    }


    @Override
    public Set<Facility> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Facility> query = em.createQuery("SELECT f FROM Facility f", Facility.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Facility getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Facility.class, id);
        }
    }

    @Override
    public Facility update(Integer id, Facility updatedFacility) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Facility facility = em.find(Facility.class, id);

            if (facility != null) {
                facility.setFacility(updatedFacility.getFacility());
                em.getTransaction().commit();
                return updatedFacility;
            }

            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Facility facilityToDelete = em.find(Facility.class, id);
            if (facilityToDelete != null) {
                em.remove(facilityToDelete);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        }
    }

    public Facility findByName(String facilityName) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT f FROM Facility f WHERE LOWER(f.facility) = :name", Facility.class)
                    .setParameter("name", facilityName.toLowerCase())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}