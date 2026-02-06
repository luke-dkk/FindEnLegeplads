package app.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private int childId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "age", nullable = true)
    private int age;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "sex", nullable = true)
    private String sex;
}
