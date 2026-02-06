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
@Table(name ="playgrounds")
public class Playground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playground_id")
    private int playgroundId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "capacity")
    private int capacity;

}
