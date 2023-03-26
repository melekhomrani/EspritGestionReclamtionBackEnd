package tn.esprit.authService.Auth;

import tn.esprit.authService.Config.JwtService;
import tn.esprit.authService.Entities.Credentials;
import tn.esprit.authService.Entities.UserRoles;
import tn.esprit.authService.Repositories.CredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    final CredentialsRepository credentialsRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) throws EntityNotFoundException {
        try{
            var user = Credentials.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .userRoles(UserRoles.Admin)
                    .build();
            credentialsRepository.save(user);
            var jwt = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwt).build();

        }catch (Exception e){
            return null;
        }
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = credentialsRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new EntityNotFoundException("User Not Found"));
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }
}
