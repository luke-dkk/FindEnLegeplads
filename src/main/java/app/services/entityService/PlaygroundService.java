package app.services.entityService;

import app.daos.FacilityDAO;
import app.daos.PlaygroundDAO;
import app.dtos.FacilityDTO;
import app.dtos.PlaygroundDTO;
import app.entities.Facility;
import app.entities.Playground;
import app.services.mappers.FacilityMapper;
import app.services.mappers.PlaygroundMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class PlaygroundService implements IService<PlaygroundDTO> {
    private final FacilityMapper facilityMapper;
    private final EntityManagerFactory emf;
    private final PlaygroundDAO playgroundDAO;
    private final PlaygroundMapper playgroundMapper;
    private final FacilityDAO facilityDAO;

    public PlaygroundService(EntityManagerFactory emf) {
        this.emf = emf;
        this.playgroundDAO = new PlaygroundDAO(emf);
        this.playgroundMapper = new PlaygroundMapper(emf);
        this.facilityDAO = new FacilityDAO(emf);
        this.facilityMapper = new FacilityMapper(emf);
    }

    //this creates a playground
    @Override
    public PlaygroundDTO create(PlaygroundDTO dto) {
        Playground playground = playgroundMapper.fromDTO(dto);
        Playground created = playgroundDAO.create(playground);
        return playgroundMapper.toDTO(created);
    }

    @Override
    public List<PlaygroundDTO> getAll() {
        return playgroundDAO.getAll()
                .stream()
                .map(playgroundMapper::toDTO)
                .toList();
    }

    @Override
    public PlaygroundDTO getById(Integer id) {
        Playground playground = playgroundDAO.getById(id);
        if (playground == null) return null;
        return playgroundMapper.toDTO(playground);
    }

    @Override
    public PlaygroundDTO update(PlaygroundDTO dto) {
        Playground playground = playgroundMapper.fromDTO(dto);
        Playground updated = playgroundDAO.update(dto.getId(), playground);
        return playgroundMapper.toDTO(updated);
    }

    @Override
    public boolean delete(Integer id) {
        return playgroundDAO.delete(id);
    }

    public List<PlaygroundDTO> createPlaygrounds(PlaygroundDTO[] dtos) {
        List<PlaygroundDTO> created = new ArrayList<>();
        for (PlaygroundDTO dto : dtos) {
            Playground playground = playgroundMapper.fromDTO(dto);
            Playground saved = playgroundDAO.create(playground);
            created.add(playgroundMapper.toDTO(saved));
        }
        return created;
    }


    public Set<FacilityDTO> getFacilityByPlaygroundId(Integer playgroundId) {
        Playground playground = playgroundDAO.getById(playgroundId);

        if (playground == null || playground.getFacilities() == null) {
            return null;
        }

        return facilityMapper.loopToDTO(playground.getFacilities());
    }

    public FacilityDTO createFacility(FacilityDTO dto) {
        Facility facility = facilityMapper.toSingleFacility(dto);
        Facility created = facilityDAO.create(facility);
        FacilityDTO response = facilityMapper.toSingleDTO(created);
        return response;
    }

//    public FacilityDTO updateFacility(Integer playgroundId, FacilityDTO dto) {
//
//        Playground playground = playgroundDAO.getById(playgroundId);
//
//        if (playground == null) {
//            throw new RuntimeException("Playground not found");
//        }
//
//        Facility existingFacility = playground.getFacility();
//
//        Facility updatedFacility = playgroundMapper.fromFacilityDTO(dto);
//
//        if (existingFacility == null) {
//            updatedFacility.setPlayground(playground);
//            facilityDAO.create(updatedFacility);
//        } else {
//            updatedFacility.setId(existingFacility.getId());
//            facilityDAO.update(existingFacility.getId(), updatedFacility);
//        }
//
//        return playgroundMapper.toFacilityDTO(updatedFacility);
//    }

    public boolean deleteFacility(Integer facilityId) {

        facilityDAO.delete(facilityId);
        return true;
    }

    public void importPlaygrounds(double latitude, double longitude, int radiusInMeters) {
        try {
            String overpassQuery = String.format(Locale.US, """
                            [out:json][timeout:60];
                            (
                              node["leisure"="playground"](around:%d,%.7f,%.7f);
                              way["leisure"="playground"](around:%d,%.7f,%.7f);
                              relation["leisure"="playground"](around:%d,%.7f,%.7f);
                            );
                            out center tags;
                            """,
                    radiusInMeters, latitude, longitude,
                    radiusInMeters, latitude, longitude,
                    radiusInMeters, latitude, longitude
            );

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://overpass-api.de/api/interpreter"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "data=" + URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8)
                    ))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Overpass API error: " + response.body());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode elements = root.get("elements");

            if (elements == null || !elements.isArray()) {
                return;
            }

            for (JsonNode element : elements) {
                double lat;
                double lng;

                if (element.has("lat") && element.has("lon")) {
                    lat = element.get("lat").asDouble();
                    lng = element.get("lon").asDouble();
                } else if (element.has("center")) {
                    JsonNode center = element.get("center");
                    lat = center.get("lat").asDouble();
                    lng = center.get("lon").asDouble();
                } else {
                    continue;
                }

                JsonNode tags = element.get("tags");

                String name = null;

                if (tags != null) {
                    if (tags.has("name")) {
                        name = tags.get("name").asText();
                    } else if (tags.has("operator")) {
                        name = tags.get("operator").asText() + " playground";
                    } else if (tags.has("addr:street")) {
                        name = "Playground at " + tags.get("addr:street").asText();
                    }
                }

                if (name == null || name.isBlank()) {
                    name = "Legeplads";
                }

                Playground playground = Playground.builder()
                        .name(name)
                        .latitude(lat)
                        .longitude(lng)
                        .build();
                playgroundDAO.create(playground);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to import playgrounds from Overpass", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import playgrounds from Overpass", e);
        }
    }

    public List<PlaygroundDTO> getPlaygroundNearClient(double lat, double lon, int radiusInMeters, int page, int size) {
        if (radiusInMeters < 0) {
            throw new IllegalArgumentException("Radius must be zero or greater");
        }

        return playgroundDAO.getPlaygroundsNearClient(lat, lon, radiusInMeters, page, size)
                .stream()
                .map(playground -> {

                    PlaygroundDTO dto = playgroundMapper.toDTO(playground);

                    double distance =
                            calculateDistance(
                                    lat,
                                    lon,
                                    playground.getLatitude(),
                                    playground.getLongitude()
                            );

                    dto.setDistance(distance);

                    return dto;
                })
                .toList();
    }
    private double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        double earthRadius = 6371000;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}
