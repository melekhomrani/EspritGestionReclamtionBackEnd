package tn.esprit.gestionreclamation.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.gestionreclamation.dto.UserLoginRequestDto;
import tn.esprit.gestionreclamation.dto.UserRegisterRequestDto;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegisterRequestDto registerRequest) {
        try {
            return authService.register(registerRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserLoginRequestDto loginRequest) {
        try {
            return authService.login(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
