package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("playground_id")
    private Integer playgroundId;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("children")
    private Set<ChildDTO> children;

    @JsonProperty("planned_check_in")
    private LocalDateTime plannedCheckIn;

    @JsonProperty("planned_check_out")
    private LocalDateTime plannedCheckOut;

    @JsonProperty("check_in")
    private LocalDateTime checkIn;

    @JsonProperty("check_out")
    private LocalDateTime checkOut;
}