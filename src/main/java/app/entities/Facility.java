package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name ="facility")
public class Facility {

    @Id
    @Column(name = "id")
    private Integer id;

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



    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Playground playground;
}
