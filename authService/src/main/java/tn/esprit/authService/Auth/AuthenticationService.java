package tn.esprit.authService.Auth;

import com.rabbitmq.tools.json.JSONWriter;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.security.core.Authentication;
import tn.esprit.authService.Config.JwtService;
import tn.esprit.authService.Entities.Credentials;
import tn.esprit.authService.dto.rabbitmqEvents.NewUserMsg;
import tn.esprit.authService.dto.rabbitmqEvents.RabbitmqMsg;
import tn.esprit.authService.Entities.UserRoles;
import tn.esprit.authService.Repositories.CredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    final CredentialsRepository credentialsRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;
    final RabbitTemplate rabbitTemplate;

    public AuthenticationResponse register(RegisterRequest registerRequest) throws EntityNotFoundException {
        try{
            var user = Credentials.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .userRoles(UserRoles.Admin)
                    .build();
            var savedUser = credentialsRepository.save(user);
            RabbitmqMsg response = RabbitmqMsg.builder().id(savedUser.getId()).email(savedUser.getEmail()).userRoles(savedUser.getUserRoles().toString()).build();
            rabbitTemplate.convertAndSend("", "q.user-registration", response);
            var jwt = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwt).build();

        }catch (Exception e){
            throw e;
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

    public Optional<Credentials> getMe(Authentication authentication){
        return credentialsRepository.findByEmail(authentication.getName());
    }

    public Optional<Credentials> saveUser(Authentication authentication, NewUserReq newUserReq){
        if(authentication.getAuthorities().contains(UserRoles.Admin)){
            var user = Credentials.builder()
                    .email(newUserReq.getEmail())
                    .password(passwordEncoder.encode(newUserReq.getPassword()))
                    .userRoles(UserRoles.User)
                    .build();
            var savedEntity = credentialsRepository.save(user);
            rabbitTemplate.convertAndSend("", "q.new-user",NewUserMsg.builder()
                    .newUserReq(newUserReq)
                    .email(user.getEmail())
                    .id(user.getId())
            );
            return Optional.of(savedEntity);
        }
        return Optional.empty();
    }


}
