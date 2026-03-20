package app.services.mappers;

import app.dtos.UserDTO;
import app.entities.User;
import jakarta.persistence.EntityManagerFactory;

public class UserMapper implements IMapper<User, UserDTO> {

    private final EntityManagerFactory emf;

    public UserMapper(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();

        if (userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }

        user.setParentName(userDTO.getParentName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();

        if (user.getId() != null) {
            userDTO.setId(user.getId());
        }

        userDTO.setParentName(user.getParentName());
        userDTO.setEmail(user.getEmail());

        // Usually you DO NOT expose password in DTO
        // userDTO.setPassword(user.getPassword());

        return userDTO;
    }
}