package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    private String object;
    private String description;
    private String type;
    private String progress;
    private String date;
    private String author;

}
