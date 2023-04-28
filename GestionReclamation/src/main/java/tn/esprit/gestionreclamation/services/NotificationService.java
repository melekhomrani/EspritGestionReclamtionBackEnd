package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.EmailDetails;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.EmailDetailsAsync;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.models.Users;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    final RabbitTemplate rabbitTemplate;
    final UserService userService;
    final RoleService roleService;

    public void notifyUser(EmailDetails details){
        rabbitTemplate.convertAndSend("", "q.send-mail", details);
    }

    @Async
    public void notifyList(List<Role> roles, EmailDetailsAsync details){
        roles.forEach(role -> {
            userService.getAllByRole(role).forEach(user -> {
                details.setRecipient(user);
                rabbitTemplate.convertAndSend("", "q.send-mail", details.generateEmailDetails());
            });
        });
    }
}
