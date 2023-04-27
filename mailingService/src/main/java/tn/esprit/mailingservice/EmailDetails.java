package tn.esprit.mailingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String previewText;
    private String title;
    private String subject;
    private String greeting;
    private String msgBody;
    private Boolean withButton;
    private String buttonText;
    private String buttonLink;
    private String recipient;
}
