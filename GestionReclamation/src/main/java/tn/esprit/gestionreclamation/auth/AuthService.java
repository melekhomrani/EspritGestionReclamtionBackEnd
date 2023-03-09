package tn.esprit.gestionreclamation.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.config.JwtService;
import tn.esprit.gestionreclamation.dto.UserLoginRequestDto;
import tn.esprit.gestionreclamation.dto.UserRegisterRequestDto;
import tn.esprit.gestionreclamation.models.UserRole;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.repositories.UserRepository;
import tn.esprit.gestionreclamation.services.RoleService;
import tn.esprit.gestionreclamation.services.UserService;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthenticationResponse> register(UserRegisterRequestDto registerRequest) throws EntityNotFoundException {
        try {
            Users user = userService.saveUser(registerRequest);
            var jwt = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().token(jwt).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<AuthenticationResponse> login(UserLoginRequestDto loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            var user = userService.getUserByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("User not Found"));
            var jwt = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().token(jwt).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
