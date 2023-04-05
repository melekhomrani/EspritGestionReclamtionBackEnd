package tn.esprit.gestionreclamation.services;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.UserRequest;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.NewUserMsg;
import tn.esprit.gestionreclamation.dto.rabbitmqEvents.RabbitmqMsg;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitmqServices {
    final RabbitTemplate rabbitTemplate;
    final UserService userService;
    @RabbitListener(queues = {"q.user-registration"})
    public void onUserRegister(RabbitmqMsg event){
        log.info("UserRegistrationEvent captured: {}", event.getEmail());
    }

    @RabbitListener(queues = {"q.new-user"})
    public void onNewUser(NewUserMsg newUserMsg){
        log.info("New user msg: {}, userReq: {}", newUserMsg, newUserMsg.getNewUserReq());
        userService.saveUser(newUserMsg.getNewUserReq(), newUserMsg.getId());
    }
}
