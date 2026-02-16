package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "children")
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "parent_name", nullable = false)
    private String parentName;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Child> children = new HashSet<>();




    public void validatePasswordAndEmail() {
        validatePassword();
        validateEmail();
    }


    public void validatePassword() {
        if (password == null || password.length() < 8) {
            System.out.println("Password must be at least 8 characters long");
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    public void validateEmail() {
        if (email == null || !email.contains("@")) {
            System.out.println("Invalid email address");
            throw new IllegalArgumentException("Invalid email address");


        }
    }

    public void addChild(Child child) {
        if (child != null) {
            if (children.contains(child.getName())) {
                System.out.println("Child with the same name already exists");
                throw new IllegalArgumentException("Child with the same name already exists");
            }
            {
                children.add(child);
                child.setUser(this);
            }
        }
    }

}
