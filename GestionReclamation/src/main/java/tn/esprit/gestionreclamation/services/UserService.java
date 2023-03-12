package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.UserRequest;
import tn.esprit.gestionreclamation.dto.UserResponse;
import tn.esprit.gestionreclamation.exceptions.AlreadyExistsException;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).toList();
    }

    public UserResponse getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.findById(user.getId()).map(this::mapToUserResponse).get();
    }

    public UserResponse getUserByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.findByEmail(user.getEmail()).map(this::mapToUserResponse).get();
    }

    public Users getUserByUserName(String userName) {
        Users user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.findByEmail(user.getEmail()).get();
    }

    public UserResponse saveUser(UserRequest user) {
        Optional<Users> checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser.isPresent()) {
            throw new AlreadyExistsException("Email address already exists");
        }
        Role role = roleService.getRoleById(user.getRole());
        Users newUser = Users.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(role)
                .build();
        return mapToUserResponse(userRepository.saveAndFlush(newUser));
    }

    public List<UserResponse> saveUsers(List<Users> users) {
        return userRepository.saveAllAndFlush(users).stream().map(this::mapToUserResponse).toList();
    }

    public UserResponse updateUser(Long id, UserRequest user) {
        Users userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Role role = roleService.getRoleById(user.getRole());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setRole(role);
        return mapToUserResponse(userRepository.saveAndFlush(userToUpdate));
    }

    public UserResponse updateUserPassword(Long id, String password) {
        Users userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userToUpdate.setPassword(passwordEncoder.encode(password));
        return mapToUserResponse(userRepository.saveAndFlush(userToUpdate));
    }

    public void deleteUser(Long id) {
        Users userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.deleteById(userToDelete.getId());
    }

    public Boolean isAdmin(Authentication authentication) {
        log.info("#################################################################################################");
        log.info("{}", authentication.getAuthorities().toArray());
        log.info("#################################################################################################");
        return authentication.getAuthorities().toArray()[0].toString().equals("ADMIN");
    }

    public Boolean isAuthorized(Role role, List<Role> allowedRoles) {
        return allowedRoles.contains(role);
    }

    public UserResponse mapToUserResponse(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .dateCreation(user.getDateCreation())
                .dateModification(user.getDateModification())
                .build();
    }
}
