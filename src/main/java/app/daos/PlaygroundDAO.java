package app.daos;

import app.dtos.PlaygroundDTO;
import app.entities.Facility;
import app.entities.Playground;
import app.services.mappers.PlaygroundMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaygroundDAO implements IDAO<Playground> {

    private static EntityManagerFactory emf;
    private final PlaygroundMapper playgroundMapper;
    public PlaygroundDAO(EntityManagerFactory emf) {
        this.emf = emf;
        this.playgroundMapper = new PlaygroundMapper(emf);
    }

    public Playground create(Playground p) {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Playground> query = em.createQuery(
                    "SELECT pl FROM Playground pl WHERE pl.name = :name AND pl.latitude = :lat AND pl.longitude = :lon",
                    Playground.class
            );

            query.setParameter("name", p.getName());
            query.setParameter("lat", p.getLatitude());
            query.setParameter("lon", p.getLongitude());

            List<Playground> result = query.getResultList();

            if (!result.isEmpty()) {
                return result.get(0);
            }

            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();

            return p;
        }
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
                    em.createQuery("SELECT p FROM Playground p LEFT JOIN FETCH p.facilities", Playground.class);
            return new HashSet<>(query.getResultList());
        }
    }

    public List<Playground> getPlaygroundsNearClient(double lat, double lon, int radiusInMeters, int page, int size) {

        double earthRadiusInMeters = 6371000.0;
        double radiusInDegrees = Math.toDegrees(radiusInMeters / earthRadiusInMeters);
        double latitudeRadians = Math.toRadians(lat);
        double longitudeRadiusInDegrees = Math.abs(Math.cos(latitudeRadians)) < 0.000001
                ? 180.0
                : radiusInDegrees / Math.cos(latitudeRadians);

        String distanceExpression = """
                (2.0 * :earthRadius * asin(sqrt(
                    power(sin(radians(p.latitude - :lat) / 2.0), 2.0)
                    + cos(radians(:lat))
                    * cos(radians(p.latitude))
                    * power(sin(radians(p.longitude - :lon) / 2.0), 2.0)
                )))
                """;

        String jpql = """
                SELECT p
                FROM Playground p
                LEFT JOIN FETCH p.facilities
                WHERE p.latitude BETWEEN :minLat AND :maxLat
                  AND p.longitude BETWEEN :minLon AND :maxLon
                  AND %s <= :radius
                ORDER BY %s
                """.formatted(distanceExpression, distanceExpression);

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Playground> query = em.createQuery(jpql, Playground.class);
            query.setParameter("lat", lat);
            query.setParameter("lon", lon);
            query.setParameter("radius", (double) radiusInMeters);
            query.setParameter("earthRadius", earthRadiusInMeters);
            query.setParameter("minLat", Math.max(-90.0, lat - radiusInDegrees));
            query.setParameter("maxLat", Math.min(90.0, lat + radiusInDegrees));
            query.setParameter("minLon", Math.max(-180.0, lon - longitudeRadiusInDegrees));
            query.setParameter("maxLon", Math.min(180.0, lon + longitudeRadiusInDegrees));

            query.setFirstResult(page * size);
            query.setMaxResults(size);

            return query.getResultList();

        }
    }
    public Integer getCheckedInChildrenCount(Integer playgroundId) {

        try (EntityManager em =emf.createEntityManager()) {
            Long count =
                    em.createQuery(
                                    """
                                    SELECT COUNT(child)
                                    FROM CheckIn c
                                    JOIN c.children child
                                    WHERE c.playground.id = :playgroundId
                                    AND c.checkout IS NULL
                                    """,
                                    Long.class
                            )
                            .setParameter("playgroundId",playgroundId)
                            .getSingleResult();
            return count.intValue();
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

    public PlaygroundDTO attachFacility(Integer playgroundId, Integer facilityId) {

        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            Playground playground =
                    em.find(Playground.class, playgroundId);

            Facility facility =
                    em.find(Facility.class, facilityId);

            if (playground == null) {
                throw new IllegalArgumentException(
                        "Playground not found"
                );
            }

            if (facility == null) {
                throw new IllegalArgumentException(
                        "Facility not found"
                );
            }

            // avoid duplicates
            if (!playground.getFacilities().contains(facility)) {
                playground.getFacilities().add(facility);
            }

            em.merge(playground);

            em.getTransaction().commit();

            PlaygroundDTO dtoResponse = playgroundMapper.toDTO(playground);

            return dtoResponse;
        }
    }
}
