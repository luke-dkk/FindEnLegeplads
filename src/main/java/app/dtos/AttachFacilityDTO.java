package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachFacilityDTO {

    public AttachFacilityDTO(){}

    public AttachFacilityDTO(String facility, Integer id) {
        this.facilityName = facility;
        this.facilityId = id;
    }

    public AttachFacilityDTO(String playgroundName, String facilityName, Integer playgroundId, Integer facilityId) {
        this.playgroundId = playgroundId;
        this.facilityId = facilityId;
        this.playgroundName = playgroundName;
        this.facilityName = facilityName;
    }

    @JsonProperty("playground_id")
    private Integer playgroundId;

    @JsonProperty("facility_id")
    private Integer facilityId;

    @JsonProperty("playground_name")
    private String playgroundName;

    @JsonProperty("facility_name")
    private String facilityName;


}
