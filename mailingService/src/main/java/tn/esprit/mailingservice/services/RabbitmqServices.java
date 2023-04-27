package tn.esprit.mailingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.mailingservice.EmailDetails;
import tn.esprit.mailingservice.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitmqServices {
    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;

    @RabbitListener(queues = {"q.send-mail"})
    public void onSendMail(EmailDetails emailDetails){
        emailService.sendMailWithAttachements(emailDetails);
        log.info(
                "Sending Mail {\n"
                + "To user: " + emailDetails.getRecipient() + " ,\n"
                + "With Subject: " + emailDetails.getSubject() + " ,\n"
                + "With Title: " + emailDetails.getTitle() + " ,\n"
                +"}"
        );
    }
}
