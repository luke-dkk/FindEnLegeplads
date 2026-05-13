package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDTO {

    @JsonProperty()
    private double latitude;
    @JsonProperty()
    private double longitude;
    @JsonProperty()
    private int radiusInMeters;
}
