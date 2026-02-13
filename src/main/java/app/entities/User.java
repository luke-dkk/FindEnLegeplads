package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private int id;

    @Column(name = "parent_name", nullable = false)
    private String parentName;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

//    @ForeignKey
//    private ArrayList<Child> children;


    @PrePersist
    public void validateEmail() {
        if (email == null || !email.contains("@")) {
            System.out.println("Invalid email address");
            throw new IllegalArgumentException("Invalid email address");


        }
    }
}
