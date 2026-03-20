package app.daos;

import app.entities.Facility;
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

    public Facility create(Facility facility) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(facility);
            em.getTransaction().commit();
        }
        return facility;
    }

    public Long getFacilityCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(f) FROM Facility f", Long.class);
            return q1.getSingleResult();
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
                facility.setToilet(updatedFacility.isToilet());
                facility.setSwings(updatedFacility.isSwings());
                facility.setSandbox(updatedFacility.isSandbox());
                facility.setSlide(updatedFacility.isSlide());
                facility.setClimbingWall(updatedFacility.isClimbingWall());
                facility.setSeesaw(updatedFacility.isSeesaw());
                facility.setPlayHouse(updatedFacility.isPlayHouse());
                facility.setMerryGoRound(updatedFacility.isMerryGoRound());
                facility.setBasketballCourt(updatedFacility.isBasketballCourt());
                facility.setSoccerField(updatedFacility.isSoccerField());
                facility.setPicnicArea(updatedFacility.isPicnicArea());
                facility.setLighting(updatedFacility.isLighting());
                facility.setBenches(updatedFacility.isBenches());
                facility.setDrinkingFountain(updatedFacility.isDrinkingFountain());
                facility.setAccessibilityFeatures(updatedFacility.isAccessibilityFeatures());
                facility.setFirstAidStation(updatedFacility.isFirstAidStation());
                facility.setDogPark(updatedFacility.isDogPark());
                facility.setMiscellaneous(updatedFacility.getMiscellaneous());
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
}