package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaygroundDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty ("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("name")
    private String name;

    @JsonProperty
    private Integer capacity;

    @JsonProperty
    private Set<FacilityDTO> facility;

    @JsonProperty()
    private double distance;

    @JsonProperty("currently_checked_in")
    private Integer currentlyCheckedIn;


}
