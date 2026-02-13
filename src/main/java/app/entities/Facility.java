package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name ="facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facilities_id")
    private int facilitiesId;

    private boolean toilet;
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



}
