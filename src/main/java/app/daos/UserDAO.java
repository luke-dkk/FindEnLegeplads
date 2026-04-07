package app.daos;


import app.entities.Child;
import app.entities.Role;
import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PrePersist;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserDAO implements IDAO<User> {
    private static EntityManagerFactory emf;

    public UserDAO (EntityManagerFactory emf) {
        this.emf = emf;
    }


    public User create(User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            if(findByEmail(user.getEmail()) != null){
                throw new IllegalArgumentException("Email already exists: " + user.getEmail());
            }
            Role userRole = em.find(Role.class, "USER");
            if(userRole == null){
                userRole = new Role("USER");
                em.persist(userRole);
            }

            user.addRole(userRole);

            em.persist(user);
            em.getTransaction().commit();

            return user;
        }
    }
    public Long getUserCount() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Long> q1 = em.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return q1.getSingleResult();

        }
    }

    public User findByEmail(String email) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();
            return users.isEmpty() ? null : users.get(0);
        }
    }
    @Override
    public Set<User> getAll() {

        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return new HashSet<>(query.getResultList());
        }
    }


    @Override
    public User getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(User.class, id);
        }
    }

    @Override
    public User update(Integer id, User updatedUser){
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                user.setParentName(updatedUser.getParentName());
                user.setEmail(updatedUser.getEmail());
                user.setPassword(updatedUser.getPassword());
                em.getTransaction().commit();
                return updatedUser;
            }
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public boolean delete(Integer id){
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            User userToDelete = em.find(User.class, id);
            if (userToDelete != null){
                em.remove(userToDelete);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        }
    }

    public void addUserRole(String email, String roleName) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            User user = findByEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User with email " + email + " not found.");
            }

            Role role = em.find(Role.class, roleName);
            if (role == null) {
                role = new Role(roleName);
                em.persist(role);
            }

            user.addRole(role);
            em.merge(user);

            em.getTransaction().commit();
        }
    }
}

