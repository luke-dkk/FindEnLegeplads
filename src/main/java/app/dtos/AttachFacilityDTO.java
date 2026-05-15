package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class AttachFacilityDTO {

    @JsonProperty("playground_id")
    private Integer playgroundId;
    @JsonProperty("facility_id")
    private Integer facilityId;
}
