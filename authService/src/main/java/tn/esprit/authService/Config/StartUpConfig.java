package tn.esprit.authService.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.esprit.authService.Auth.AuthenticationService;
import tn.esprit.authService.Auth.NewUserReq;
import tn.esprit.authService.Entities.Credentials;
import tn.esprit.authService.Repositories.CredentialsRepository;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartUpConfig {
    @Bean
    CommandLineRunner commandLineRunner(AuthenticationService authenticationService, CredentialsRepository credentialsRepository){
        return args -> {
          var checkIfUserExists = credentialsRepository.findByEmail("abc@abc.com");
          if(!checkIfUserExists.isPresent()){
              var admin = NewUserReq.builder().email("abc@abc.com").firstName("test").lastName("user").role(Long.valueOf(1)).password("12345").build();
              var saved = authenticationService.saveUserNoAuthCheck(admin);
              log.info("registered primary user: {}", saved.get());
          } else {
              log.info("primary User abc@abc.com already exists");
          }
        };
    }
}
