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
        if(user.isPresent()){
            var updated = user.get();
            updated.setPassword(passwordEncoder.encode(updatePassword.getPassword()));
            credentialsRepository.save(updated);
        }else{
            log.warn("Couldn't update User with id: " + updatePassword.getId() + " / Could Not find User");
        }
    }
}
