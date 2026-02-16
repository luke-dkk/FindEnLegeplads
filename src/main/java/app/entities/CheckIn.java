package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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
//    Playground playground;
//    User user;



}
