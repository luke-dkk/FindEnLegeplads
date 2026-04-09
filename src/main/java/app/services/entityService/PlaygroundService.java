package app.services.entityService;

import app.daos.FacilityDAO;
import app.daos.PlaygroundDAO;
import app.dtos.FacilityDTO;
import app.dtos.PlaygroundDTO;
import app.entities.Facility;
import app.entities.Playground;
import app.services.mappers.PlaygroundMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class PlaygroundService implements IService<PlaygroundDTO> {

    private final EntityManagerFactory emf;
    private final PlaygroundDAO playgroundDAO;
    private final PlaygroundMapper playgroundMapper;
    private final FacilityDAO facilityDAO;

    public PlaygroundService(EntityManagerFactory emf) {
        this.emf = emf;
        this.playgroundDAO = new PlaygroundDAO(emf);
        this.playgroundMapper = new PlaygroundMapper(emf);
        this.facilityDAO = new FacilityDAO(emf);
    }

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


    public FacilityDTO getFacilityByPlaygroundId(Integer playgroundId) {
        Playground playground = playgroundDAO.getById(playgroundId);

        if (playground == null || playground.getFacility() == null) {
            return null;
        }

        return playgroundMapper.toFacilityDTO(playground.getFacility());
    }

    public FacilityDTO createFacility(Integer playgroundId, FacilityDTO dto) {
        Playground playground = playgroundDAO.getById(playgroundId);

        if (playground == null) {
            throw new RuntimeException("Playground not found");
        }

        Facility facility = playgroundMapper.fromFacilityDTO(dto);

        playground.addFacility(facility);

        playgroundDAO.update(playgroundId, playground);

        return playgroundMapper.toFacilityDTO(facility);
    }

    public FacilityDTO updateFacility(Integer playgroundId, FacilityDTO dto) {

        Playground playground = playgroundDAO.getById(playgroundId);

        if (playground == null) {
            throw new RuntimeException("Playground not found");
        }

        Facility existingFacility = playground.getFacility();

        Facility updatedFacility = playgroundMapper.fromFacilityDTO(dto);

        if (existingFacility == null) {
            updatedFacility.setPlayground(playground);
            facilityDAO.create(updatedFacility);
        } else {
            updatedFacility.setId(existingFacility.getId());
            facilityDAO.update(existingFacility.getId(), updatedFacility);
        }

        return playgroundMapper.toFacilityDTO(updatedFacility);
    }

    public boolean deleteFacility(Integer playgroundId) {
        Playground playground = playgroundDAO.getById(playgroundId);

        if (playground == null || playground.getFacility() == null) {
            return false;
        }

        playground.setFacility(null);

        playgroundDAO.update(playgroundId, playground);

        return true;
    }

    public void importPlaygrounds(double latitude, double longitude, int radiusInMeters) {
        try {
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                    + "location=" + latitude + "," + longitude
                    + "&radius=" + radiusInMeters
                    + "&keyword=playground"
                    + "&key=" + System.getenv("GOOGLE_API_KEY");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode results = root.get("results");

            for (JsonNode place : results) {

                String name = place.get("name").asText();

                JsonNode location = place.get("geometry").get("location");

                double lat = location.get("lat").asDouble();
                double lng = location.get("lng").asDouble();

                Playground playground = Playground.builder()
                        .name(name)
                        .latitude(lat)
                        .longitude(lng)
                        .build();
                Facility facility = new Facility();
                facility.setPlayground(playground);

                playground.setFacility(facility);

                playgroundDAO.create(playground);

            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to import playgrounds", e);
        }
    }
}