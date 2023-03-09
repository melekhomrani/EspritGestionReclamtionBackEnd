package tn.esprit.gestionreclamation.services.IService;

import org.springframework.security.core.Authentication;
import tn.esprit.gestionreclamation.dto.UserRegisterRequestDto;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.models.Users;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<Users> getAllUsers();

    Optional<Users> getUserById(Long id);

    Optional<Users> getUserByEmail(String email);

    Optional<Users> getUserByUserName(String userName);

    Users saveUser(UserRegisterRequestDto user);

    List<Users> saveUsers(List<Users> users);

    Users updateUser(Long id, Users user);

    void deleteUser(Long id);

    Boolean isAdmin(Authentication authentication);

    Boolean isAuthorized(Role role, List<Role> allowedRoles);

}
