package app.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private int childId;

    @Column(name = "name", length = 100)
    private int name;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;
}
