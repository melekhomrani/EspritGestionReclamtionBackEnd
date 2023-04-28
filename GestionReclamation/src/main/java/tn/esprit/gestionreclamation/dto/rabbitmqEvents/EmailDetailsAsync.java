package tn.esprit.gestionreclamation.dto.rabbitmqEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.gestionreclamation.models.Users;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetailsAsync {
    private String subject;
    private Boolean withButton;
    private String buttonText;
    private String buttonLink;
    private Users recipient;
    private Users actionDoer;
    private String additionalText;
    private String action;

    public EmailDetails generateEmailDetails(){
        String textTitle = "Esprit Systeme de gestion de reclamations";
        String textGreeting= "Bienvenu " + recipient.getFirstName() + " " + recipient.getLastName() +",";
        String textMsgBody=actionDoer.getFirstName() + " " + actionDoer.getLastName() + " a " + action + "une reclamation qui vous concerne.";
        String textBodyAdditional="";
        if(additionalText!= null) textBodyAdditional = additionalText;
        String textPreview="CRM Esprit Notification: " + textMsgBody;
        return EmailDetails.builder()
                .previewText(textPreview)
                .title(textTitle)
                .subject(this.subject)
                .greeting(textGreeting)
                .msgBody(textMsgBody + textBodyAdditional)
                .withButton(this.withButton)
                .buttonText(this.buttonText)
                .buttonLink(this.buttonLink)
                .recipient(this.recipient.getEmail())
                .build();
    }
}
