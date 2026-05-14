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
@Table(name ="playgrounds", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "latitude", "longitude"})
        )
public class Playground {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "capacity")
    private Integer capacity;

    @OneToOne(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private Facility facility;

    @OneToMany(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CheckIn> checkIns = new HashSet<>();

    @OneToMany(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Rating> ratings = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "facility_enum")
    private FacilityEnum facilityEnum;


    @Transient
    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 3.5;
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
