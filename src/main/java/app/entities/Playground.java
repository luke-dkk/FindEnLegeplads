package app.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name ="playgrounds")
public class Playground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playground_id")
    private int playgroundId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "longitude")
    private long longitude;

    @Column(name = "latitude")
    private long latitude;

    @Column(name = "capacity")
    private int capacity;

}
