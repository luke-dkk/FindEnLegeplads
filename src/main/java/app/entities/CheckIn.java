package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name ="check_in")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime checkIn;
    private LocalDateTime checkout;

    private LocalDateTime plannedCheckIn;
    private LocalDateTime plannedCheckout;

    @ManyToOne
    private Playground playground;

    @ManyToOne
    private User user;

    @ManyToMany
    private Set<Child> children = new HashSet<>();
}
