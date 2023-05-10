package tn.esprit.gestionreclamation.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.UserRequest;
import tn.esprit.gestionreclamation.dto.UserResponse;
import tn.esprit.gestionreclamation.exceptions.BadRequestException;
import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
import tn.esprit.gestionreclamation.exceptions.UserNotAuthorizedException;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173",
        methods = { RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PUT,  },
        allowedHeaders = { "Content-Type", "Authorization" }
)
@RequestMapping("/api/gest/users")
public class UserController {
    private final UserService userService;
    final Authentication authentication;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        if (userService.isAdmin(authentication)) {
            return ResponseEntity.ok(userService.getAllUsers());
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @GetMapping("/isadmin")
    public ResponseEntity<Boolean> isAdmin() {
        log.info("##############################################################" +
                "uthentication object: {}" +
                "###########################################################", authentication);
        return ResponseEntity.ok(userService.isAdmin(authentication));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        log.info("Authentication object: {}", authentication);
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        if (userService.isAdmin(authentication)) {
            return ResponseEntity.ok(userService.getUserById(id));
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        if (userService.isAdmin(authentication)) {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    //No Post mapping here...
    //Do not use  this please
    //Only register users through auth microservice on port: 8083
    //DO NOT USE - DANGER ! ! ! !
//    @PostMapping
//    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
//
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest user) {
        if (userService.isAdmin(authentication)) {
            log.info("###################################################" +
                    "User id: {}", id +
                    "#############################################");
            log.info("User object: {}", user);
            return ResponseEntity.ok(userService.updateUser(id, user));
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody String password) {
        if (userService.isAdmin(authentication)) {
            try {
                userService.updatePassword(id, password);
                return ResponseEntity.ok("Password updated successfully");
            } catch (Exception e) {
                throw new BadRequestException("Error while updating password");
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        try {
            if (userService.isAdmin(authentication)) {
                userService.deleteUser(id);
                return ResponseEntity.ok(true);
            }
            throw new UserNotAuthorizedException("You are not authorized to delete a user");

        } catch (Exception e) {
            throw new BadRequestException("Error while deleting user");
        }
    }

}
