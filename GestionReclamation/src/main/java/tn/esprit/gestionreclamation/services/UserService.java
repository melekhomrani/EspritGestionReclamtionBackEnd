package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.UserRequest;
import tn.esprit.gestionreclamation.dto.UserResponse;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.UpdateEmailMsg;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.UpdatePassword;
import tn.esprit.gestionreclamation.exceptions.AlreadyExistsException;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.models.Reclamation;
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
    private final RabbitTemplate rabbitTemplate;

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

    public Optional<Users> getUserByUserName(String userName) {
        Users user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userRepository.findByEmail(user.getEmail());
    }


    //This is only for saving users who were alrady registered through the auth service...
    //Don't use this service directly please...
    public UserResponse saveUser(UserRequest user, Long id) {
        Optional<Users> checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser.isPresent()) {
            throw new AlreadyExistsException("Email address already exists");
        }
        try {
            if(user.getRole() == null){
                throw new EntityNotFoundException("UserService.saveUser: UserRequest Role Id cannot be null");
            }
            Role role = roleService.getRoleById(user.getRole());
            Users newUser = Users.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .id(id)
                    .role(role)
                    .build();
            return mapToUserResponse(userRepository.saveAndFlush(newUser));
        }catch (Exception e){
            throw e;
        }
    }

//    public List<UserResponse> saveUsers(List<Users> users) {
//        return userRepository.saveAllAndFlush(users).stream().map(this::mapToUserResponse).toList();
//    }

    public UserResponse updateUser(Long id, UserRequest user) {
        Users userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Role role = roleService.getRoleById(user.getRole());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setRole(role);
        userRepository.save(userToUpdate);
        rabbitTemplate.convertAndSend("", "q.update-email", UpdateEmailMsg.builder().email(userToUpdate.getEmail()).id(userToUpdate.getId()).build());
        return mapToUserResponse(userRepository.saveAndFlush(userToUpdate));
    }

    public boolean updatePassword(Long id, String newPassword, String oldPassword){
        var user = getUserById(id);
        if(user == null){
            throw new EntityNotFoundException("User not found");
        }
        try {
            rabbitTemplate.convertAndSend("", "q.update-password", UpdatePassword.builder().newPassword(newPassword).oldPassword(oldPassword).id(id).build());
            return true;
        } catch (Exception e) {
            log.error("Error updating password: {}", e.getMessage());
            return false;
        }
    }

    public void deleteUser(Long id) {
        Users userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.deleteById(userToDelete.getDbId());
        rabbitTemplate.convertAndSend("","q.delete-user", id);
    }

    public Boolean isAdmin(Authentication authentication) {
        return authentication.getProfile().getRole().getName().equalsIgnoreCase("Admin");
    }

    public Boolean isAuthorized(Role role, List<Role> allowedRoles) {
        return allowedRoles.contains(role);
    }

    public UserResponse mapToUserResponse(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .dbid(user.getDbId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .dateCreation(user.getDateCreation())
                .dateModification(user.getDateModification())
                .build();
    }

    public List<Users> getAll(){
        return userRepository.findAll();
    }

    public List<Users> getAllByRole(Role role){
        return userRepository.findAllByRole(role);
    }

    public Optional<Users> getByRoleId(Long id){
        return userRepository.findByRoleId(id);
    }
    public Boolean isMe(Long id,Authentication authentication) {
        return authentication.getProfile().getId().equals(id);
    }

    public Boolean canConsult(Authentication authentication, Reclamation reclamation, AccessFlow accessFlow){
        if(accessFlow.getConsult().contains(authentication.getProfile().getRole()) ||
                accessFlow.getValidate().contains(authentication.getProfile().getRole()) ||
                accessFlow.getApprove().contains(authentication.getProfile().getRole()) ||
                isAdmin(authentication)
        ){
            return true;
        } else return reclamation.getAuthor().equals(authentication.getProfile());
    }
}
