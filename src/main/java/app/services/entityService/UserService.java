package app.services.entityService;

import app.daos.UserDAO;
import app.dtos.UserDTO;
import app.entities.User;

import app.services.mappers.UserMapper;
import jakarta.persistence.EntityManagerFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<UserDTO> {

    private final EntityManagerFactory emf;
    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UserService(EntityManagerFactory emf) {
        this.emf = emf;
        this.userDAO = new UserDAO(emf);
        this.userMapper = new UserMapper(emf);
    }

    @Override
    public UserDTO create(UserDTO userDTO) {

        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        String email = userDTO.getEmail().toLowerCase();
        User user = userMapper.fromDTO(userDTO);
        user.setPassword(hashedPassword);
        user.setEmail(email);

        User createdUser = userDAO.create(user);

        return userMapper.toDTO(createdUser);
    }

    @Override
    public List<UserDTO> getAll() {
        return userDAO.getAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO getById(Integer id) {
        User user = userDAO.getById(id);
        if (user == null) {
            return null;
        }
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        User user = userMapper.fromDTO(userDTO);
        User updatedUser = userDAO.update(userDTO.getId(), user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public boolean delete(Integer id) {
        return userDAO.delete(id);
    }

    public boolean deleteUserFromAdmin(String email) {
       User user = userDAO.findByEmail(email);
       return userDAO.delete(user.getId());
    }

    public List<UserDTO> createUsers(UserDTO[] userDTOs) {
        List<UserDTO> createdUsers = new ArrayList<>();

        for (UserDTO userDTO : userDTOs) {

            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());

            User user = userMapper.fromDTO(userDTO);
            user.setPassword(hashedPassword);

            User createdUser = userDAO.create(user);

            createdUsers.add(userMapper.toDTO(createdUser));
        }

        return createdUsers;
    }
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    public void addRole(String email, String roleName) {
        userDAO.addUserRole(email, roleName);
    }

}