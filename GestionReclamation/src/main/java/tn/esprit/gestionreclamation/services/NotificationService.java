package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.EmailDetails;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.EmailDetailsAsync;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.models.Users;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    final RabbitTemplate rabbitTemplate;
    final UserService userService;
    final RoleService roleService;
    final AccessFlowService accessFlowService;
    @Value("${FRONT_URL}")
    private String frontUrl;

    @Async
    public void notifyUser(EmailDetails details){
        rabbitTemplate.convertAndSend("", "q.send-mail", details);
    }

    @Async
    public void notifyList(List<Role> roles, EmailDetailsAsync details){
        log.info("Sending mail...");
        roles.forEach(role -> {
            userService.getAllByRole(role).forEach(user -> {
                details.setRecipient(user);
                rabbitTemplate.convertAndSend("", "q.send-mail", details.generateEmailDetails());
            });
        });
    }

    @Async
    public void notifyAuthor(Reclamation reclamation, String action, Users actionDoer){
        var author = reclamation.getAuthor();
        notifyUser(EmailDetailsAsync.builder()
                .subject("Nouvelle Reclamation")
                .recipient(author)
                .actionDoer(actionDoer)
                .action(action)
                .withButton(true)
                .buttonText("Consulter votre reclamation ici")
                .buttonLink(frontUrl+"/reclamation/" + reclamation.getId())
                .build().generateEmailDetails()
        );
    }

    public void getListsAndNotify(Reclamation reclamation, String action, Users actionDoer){
        var accessFlow = accessFlowService.getAccessFlowByReclamation(reclamation).orElseThrow();
        var validators = userService.getAllInRolesList(accessFlow.getValidate());
        var approvers = userService.getAllInRolesList(accessFlow.getApprove());
        var notifiers = userService.getAllInRolesList(accessFlow.getNotify());
        validators.forEach(validator ->{
            notifyUser(EmailDetailsAsync.builder()
                    .subject("Nouvelle Reclamation")
                    .recipient(validator)
                    .actionDoer(actionDoer)
                    .action(action)
                    .withButton(true)
                    .buttonText("Consulter Ici")
                    .buttonLink(frontUrl+"/reclamation/"+reclamation.getId().toString())
                    .build().generateEmailDetails()
            );
        });
        approvers.forEach(validator ->{
            notifyUser(EmailDetailsAsync.builder()
                    .subject("Nouvelle Reclamation")
                    .recipient(validator)
                    .actionDoer(reclamation.getAuthor())
                    .action("Creer")
                    .withButton(true)
                    .buttonText("Consulter Ici")
                    .buttonLink(frontUrl+"/reclamation/"+reclamation.getId().toString())
                    .build().generateEmailDetails()
            );
        });
        notifiers.forEach(validator ->{
            notifyUser(EmailDetailsAsync.builder()
                    .subject("Nouvelle Reclamation")
                    .recipient(validator)
                    .actionDoer(reclamation.getAuthor())
                    .action("Creer")
                    .withButton(true)
                    .buttonText("Consulter Ici")
                    .buttonLink(frontUrl+"/reclamation/"+reclamation.getId().toString())
                    .build().generateEmailDetails()
            );
        });
    }
}
