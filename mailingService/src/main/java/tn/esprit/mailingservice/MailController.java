package tn.esprit.mailingservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final EmailService emailService;

    //This Controller should not be exposed.
    //This is exclusively for internal calls on the same network. DO NOT EXPOSE TO INTERNET.
    //melekher ken t7el l PORT ta3 l server hedha w yfi9ou bih l3bed rahou bch ywaliw ynajmou yab3thou emails bl addresse ta3 esprit.
    //KEEP INTERNAL OR USE RABBITMQ INSTEAD
    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody EmailDetails details){
        String status = emailService.sendMailWithAttachements(details);
        return ResponseEntity.ok(status);
    }
}
