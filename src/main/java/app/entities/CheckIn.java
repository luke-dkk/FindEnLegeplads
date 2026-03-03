package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(name = "id")
    private Integer id;
    LocalDateTime checkIn;
    LocalDateTime checkout;
    LocalDateTime plannedCheckIn;
    LocalDateTime plannedCheckout;

    @ManyToOne
    private Playground playground;

    @ManyToOne
    private User user;

//    Playground playground;
//    User user;



}
