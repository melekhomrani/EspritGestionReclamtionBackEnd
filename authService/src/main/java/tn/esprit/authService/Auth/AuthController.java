package tn.esprit.authService.Auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.esprit.authService.Entities.Credentials;
import tn.esprit.authService.Repositories.CredentialsRepository;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final CredentialsRepository credentialsRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        var jwt = authenticationService.register(registerRequest);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/auth/register")
    public ResponseEntity registerNewUser(Authentication authentication ,@RequestBody NewUserReq newUserReq){
        var user = authenticationService.saveUser(authentication, newUserReq);
        if(user.isPresent()) return ResponseEntity.ok(user.get());
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<Credentials> getMe(Authentication authentication, HttpServletRequest req){
        Optional<Credentials> res = authenticationService.getMe(authentication);
        log.info(req.getHeader("id"));
        log.info(req.getHeader("email"));
        log.info(req.getHeader("Authorization"));
        if(res.isPresent()){
            return ResponseEntity.ok(res.get());
        }
        return ResponseEntity.notFound().build();
    }
}
