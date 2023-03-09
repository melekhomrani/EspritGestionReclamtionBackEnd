package tn.esprit.gestionreclamation.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.UserRegisterRequestDto;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(Authentication authentication) {
        if (userService.isAdmin(authentication)) {
            log.info("Admin {} is trying to get all users", authentication.getName());
            try {
                List<Users> users = userService.getAllUsers();
                log.info("Users found: {}", users.size());
                return ResponseEntity.ok(users);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/me")
    public ResponseEntity<Users> getMe(Authentication authentication) {
        try {
            Optional<Users> user = userService.getUserByEmail(authentication.getName());
            if (user.isPresent()) {
                log.info("User found with email: {}", authentication.getName());
                return ResponseEntity.ok(user.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(Authentication authentication, @PathVariable Long id) {
        if (userService.isAdmin(authentication)) {
            try {
                Optional<Users> user = userService.getUserById(id);
                if (user.isPresent()) {
                    log.info("User found with id: {}", id);
                    return ResponseEntity.ok(user.get());
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getUserByEmail(Authentication authentication, @PathVariable String email) {
        if (userService.isAdmin(authentication)) {
            try {
                Optional<Users> user = userService.getUserByEmail(email);
                if (user.isPresent()) {
                    log.info("User found with email: {}", email);
                    return ResponseEntity.ok(user.get());
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping
    public ResponseEntity<Users> saveUser(Authentication authentication, @RequestBody UserRegisterRequestDto user) {
        if (userService.isAdmin(authentication)) {
            try {
                log.info("User saved");
                Users savedUser = userService.saveUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            } catch (Exception e) {
                log.error("Error while saving user");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(Authentication authentication, @PathVariable Long id, @RequestBody Users user) {
        if (userService.isAdmin(authentication)) {
            try {
                Optional<Users> userById = userService.getUserById(id);
                if (userById.isPresent()) {
                    log.info("User updated");
                    Users updatedUser = userService.updateUser(id, user);
                    return ResponseEntity.ok(updatedUser);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                log.error("Error while updating user");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Users> deleteUser(Authentication authentication, @PathVariable Long id) {
        if (userService.isAdmin(authentication)) {
            try {
                Optional<Users> user = userService.getUserById(id);
                if (user.isPresent()) {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
