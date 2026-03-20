package app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityDTO {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("toilet")
    private boolean toilet;
    @JsonProperty("swings")
    private boolean swings;
    private boolean sandbox;
    private boolean slide;
    private boolean climbingWall;
    private boolean seesaw;
    private boolean playHouse;
    private boolean merryGoRound;
    private boolean basketballCourt;
    private boolean soccerField;
    private boolean picnicArea;
    private boolean lighting;
    private boolean benches;
    private boolean drinkingFountain;
    private boolean accessibilityFeatures;
    private boolean firstAidStation;
    private boolean dogPark;
    private String miscellaneous;

    private Integer playgroundId;
}