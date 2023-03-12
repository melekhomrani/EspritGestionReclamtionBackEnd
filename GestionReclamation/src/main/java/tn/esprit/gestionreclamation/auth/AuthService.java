package tn.esprit.gestionreclamation.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.config.JwtService;
import tn.esprit.gestionreclamation.exceptions.AlreadyExistsException;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.models.Users;
import tn.esprit.gestionreclamation.repositories.RoleRepository;
import tn.esprit.gestionreclamation.repositories.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ResponseEntity<AuthenticationResponse> register(UserRegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }
        Role role = roleRepository.findById(registerRequest.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        Users user = Users.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        userRepository.saveAndFlush(user);
        var jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwt).build());
    }

    public ResponseEntity<AuthenticationResponse> login(UserLoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Login failed, User not found"));
        var jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthenticationResponse.builder().token(jwt).build());
    }
}
