package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityDTO {

    public FacilityDTO(String name) {
        this.name = name;
    }



    @JsonProperty("id")
    private Integer id;
    private String name;
}