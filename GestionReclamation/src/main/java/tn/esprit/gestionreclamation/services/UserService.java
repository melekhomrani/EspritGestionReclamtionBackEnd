package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.UserRegisterRequestDto;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.models.UserRole;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.repositories.UserRepository;
import tn.esprit.gestionreclamation.services.IService.IUserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<Users> getUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }

    @Override
    public Users saveUser(UserRegisterRequestDto user) {
        Optional<Role> role = roleService.getRoleById(user.getRoleId());
        if (role.isPresent()) {
            Users newUser = Users.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(role.get())
                    .userRole(UserRole.USER)
                    .build();
            return userRepository.saveAndFlush(newUser);
        }
        return null;
    }

    @Override
    public List<Users> saveUsers(List<Users> users) {
        return userRepository.saveAllAndFlush(users);
    }

    @Override
    public Users updateUser(Long id, Users user) {
        Optional<Users> checkedUser = userRepository.findById(id);
        Optional<Role> role = roleService.getRoleById(user.getRole().getId());
        if (checkedUser.isPresent() && checkedUser.get().getId().equals(user.getId()) && role.isPresent()) {
            Users newUser = Users.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .password(user.getPassword())
                    .role(roleService.getRoleByRoleName(role.get().getRoleName()).get())
                    .userRole(user.getUserRole())
                    .build();
            return userRepository.saveAndFlush(newUser);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Boolean isAdmin(Authentication authentication) {
        log.info(String.valueOf(authentication.getAuthorities().toArray()[0].toString().equals("ADMIN")));
        return authentication.getAuthorities().toArray()[0].toString().equals("ADMIN");
    }

    public Boolean isAuthorized(Role role, List<Role> allowedRoles) {
        return allowedRoles.contains(role);
    }
}
