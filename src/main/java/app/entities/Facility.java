//package app.entities;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Builder
//@Table(name ="facility")
//public class Facility {
//
//    public Facility(int id, String facility) {
//        this.id = id;
//        this.facility = facility;
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;
//    private String facility;
//
//
//    @ManyToMany(mappedBy = "facility", cascade = CascadeType.MERGE)
//    private Playground playground;
//}

package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name ="facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String facility;

    @Builder.Default
    @ManyToMany(mappedBy = "facilities")
    private Set<Playground> playgrounds = new HashSet<>();
}
