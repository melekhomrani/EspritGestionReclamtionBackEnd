package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationRequest {
    private Long typeId;
    private String object;
    private String description;
}
