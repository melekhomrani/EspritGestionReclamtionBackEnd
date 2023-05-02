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
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final CredentialsRepository credentialsRepository;

//    @PostMapping("auth/first")
//    public ResponseEntity<Boolean> register(@RequestBody NewUserReq newUserReq){
//        var user = authenticationService.saveUserNoAuthCheck(newUserReq);
//        log.info("saved user {}", user.get());
//        if(user.isPresent()) return ResponseEntity.ok(true);
//        return ResponseEntity.badRequest().build();
//    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<Credentials> registerNewUser(Authentication authentication ,@RequestBody NewUserReq newUserReq){
        var user = authenticationService.saveUser(authentication, newUserReq);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/internal/auth/register")
    public ResponseEntity<Credentials> internalRegister(Authentication authentication ,@RequestBody NewUserReq newUserReq){
        var user = authenticationService.saveUserNoAuthCheck(newUserReq);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<Credentials> getMe(Authentication authentication, HttpServletRequest req){
        Optional<Credentials> res = authenticationService.getMe(authentication);
        return res.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/internal/test")
    public Boolean testInternal(){
        return true;
    }
}
