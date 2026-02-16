package app.entities;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Builder
@Entity
@Table(name ="child")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "age", nullable = true)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = true)
    private Sex sex;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private User user;

}
