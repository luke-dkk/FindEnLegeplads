package entities;

import app.entities.User;
import app.entities.Role;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldHashPasswordCorrectly() {
        String password = "realPassword";
        User user = new User("luke_persson@yahoo.dk", password, "Luke");

        assertTrue(BCrypt.checkpw(password, user.getPassword()));
    }

    @Test
    void shouldFailWithWrongPassword() {
        String password = "realPassword";
        User user = new User("luke_persson@yahoo.dk", password, "Luke");

        assertFalse(BCrypt.checkpw("wrongPassword", user.getPassword()));
    }

    @Test
    void shouldHaveDefaultUserRole() {
        User user = new User("luke_persson@yahoo.dk", "realPassword", "Luke");
        Set<String> roles = user.getRolesAsStrings();

        assertTrue(roles.contains("USER"));
        }


    @Test
    void shouldAddAdminRole() {
        User user = new User("test@mail.com", "password", "Luke");

        Role userRole = new Role("USER");
        Role adminRole = new Role("ADMIN");

        user.addRole(userRole);
        user.addRole(adminRole);

        Set<String> roleNames = user.getRolesAsStrings();

        assertTrue(roleNames.contains("USER"));
        assertTrue(roleNames.contains("ADMIN"));
    }
}