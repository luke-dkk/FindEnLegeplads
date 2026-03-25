package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"children", "ratings"})
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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Child> children = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Rating> ratings = new HashSet<>();



    public User (String email, String password, String parentName){
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        this.email = validateEmail(email);
        this.password = hashedPassword;
        this.parentName = parentName;
    }

    public Set<String> getRolesAsStrings() {
        this.roles.stream().map(Role::getRoleName).toList();
        return this.roles.stream().map(Role::getRoleName).collect(java.util.stream.Collectors.toSet());
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }



    public boolean validatePassword(String pw) {
            return BCrypt.checkpw(pw, this.password);
        }



    public String validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            System.out.println("Invalid email address");
            throw new IllegalArgumentException("Invalid email address");
            }
        else{
            return email;
        }
    }

    public void addChild(Child child) {
        if (child != null) {
            if (children.contains(child)) {
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
