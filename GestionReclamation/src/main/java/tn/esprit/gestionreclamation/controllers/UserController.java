//package tn.esprit.gestionreclamation.controllers;
//
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import tn.esprit.gestionreclamation.dto.UserRequest;
//import tn.esprit.gestionreclamation.dto.UserResponse;
//import tn.esprit.gestionreclamation.exceptions.BadRequestException;
//import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
//import tn.esprit.gestionreclamation.exceptions.UserNotAuthorizedException;
//import tn.esprit.gestionreclamation.services.Authentication;
//import tn.esprit.gestionreclamation.services.UserService;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//    private final UserService userService;
//
//    @GetMapping
//    public ResponseEntity<List<UserResponse>> getAllUsers(Authentication authentication) {
//        if (userService.isAdmin(authentication)) {
//            return ResponseEntity.ok(userService.getAllUsers());
//        }
//        throw new ForbiddenException("You are not authorized to perform this action");
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
//        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserResponse> getUserById(Authentication authentication, @PathVariable Long id) {
//        if (userService.isAdmin(authentication)) {
//            return ResponseEntity.ok(userService.getUserById(id));
//        }
//        throw new ForbiddenException("You are not authorized to perform this action");
//    }
//
//
//    @GetMapping("/email/{email}")
//    public ResponseEntity<UserResponse> getUserByEmail(Authentication authentication, @PathVariable String email) {
//        if (userService.isAdmin(authentication)) {
//            return ResponseEntity.ok(userService.getUserByEmail(email));
//        }
//        throw new ForbiddenException("You are not authorized to perform this action");
//    }
//
//    @PostMapping
//    public ResponseEntity<UserResponse> saveUser(Authentication authentication, @RequestBody UserRequest user) {
//        try {
//            if (userService.isAdmin(authentication)) {
//                return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
//            }
//            throw new ForbiddenException("You are not authorized to perform this action");
//        } catch (Exception e) {
//            throw new BadRequestException("Error while saving user");
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserResponse> updateUser(Authentication authentication, @PathVariable Long id, @RequestBody UserRequest user) {
//        if (userService.isAdmin(authentication)) {
//            try {
//                return ResponseEntity.ok(userService.updateUser(id, user));
//            } catch (Exception e) {
//                throw new BadRequestException("Error while updating user");
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//    }
//
//    @PutMapping("/password/{id}")
//    public ResponseEntity<String> updatePassword(Authentication authentication, @PathVariable Long id, @RequestBody String password) {
//        if (userService.isAdmin(authentication)) {
//            try {
//                userService.updateUserPassword(id, password);
//                return ResponseEntity.ok("Password updated successfully");
//            } catch (Exception e) {
//                throw new BadRequestException("Error while updating password");
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(Authentication authentication, @PathVariable Long id) {
//        try {
//            if (userService.isAdmin(authentication)) {
//                userService.deleteUser(id);
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
//            }
//            throw new UserNotAuthorizedException("You are not authorized to delete a user");
//
//        } catch (Exception e) {
//            throw new BadRequestException("Error while deleting user");
//        }
//    }
//
//}
