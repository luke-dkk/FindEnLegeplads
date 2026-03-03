package app.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Nullability;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name ="playgrounds")
public class Playground {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer playgroundId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "capacity")
    private int capacity;

    @OneToOne(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true)
    private Facility facility;


    @OneToMany(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CheckIn> checkIns = new HashSet<>();

    @OneToMany(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Rating> ratings = new HashSet<>();

    private Double averageRating;


    @Transient
    public Double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 1.0;
        }
        return ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(1.0);
    }



    public void addFacility(Facility facility) {
        this.facility = facility;
        if(facility!=null) {
            facility.setPlayground(this);
        }
    }
}
