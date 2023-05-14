package tn.esprit.authService.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.authService.Repositories.CredentialsRepository;
import tn.esprit.authService.dto.rabbitmqEvents.UpdateEmailMsg;
import tn.esprit.authService.dto.rabbitmqEvents.UpdatePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitmqServices {
    final RabbitTemplate rabbitTemplate;
    final CredentialsRepository credentialsRepository;
    final PasswordEncoder passwordEncoder;

    @RabbitListener(queues = {"q.delete-user"})
    public void deleteUser(Long id){
        credentialsRepository.deleteById(id);
    }

    @RabbitListener(queues = {"q.update-email"})
    public void updateEmail(UpdateEmailMsg event){
        var user = credentialsRepository.findById(event.getId());
        if(user.isPresent()){
            var updated = user.get();
            updated.setEmail(event.getEmail());
            credentialsRepository.save(updated);
        }else{
            log.warn("User with Id " + event.getId() + "Not Found !!!");
        }
    }

    @RabbitListener(queues = {"q.update-password"})
    public void updatePassword(UpdatePassword updatePassword){
        var user = credentialsRepository.findById(updatePassword.getId());
        try {
            if(user.isPresent()){
                var updated = user.get();
                // check if old password is correct
                if(!passwordEncoder.matches(updatePassword.getOldPassword(), updated.getPassword())){
                    log.warn("Couldn't update User with id: " + updatePassword.getId() + " / Old Password is incorrect");
                    throw new RuntimeException("Old Password is incorrect");
                }else {
                    updated.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
                    credentialsRepository.save(updated);
                }

            }else{
                log.warn("Couldn't update User with id: " + updatePassword.getId() + " / Could Not find User");
                throw new RuntimeException("Could Not find User");
            }
        } catch (Exception e) {
            log.warn("Couldn't update User with id: " + updatePassword.getId() + " / " + e.getMessage());
        }
    }
}
