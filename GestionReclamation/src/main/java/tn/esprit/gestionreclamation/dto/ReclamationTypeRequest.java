package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.gestionreclamation.models.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReclamationTypeRequest {
    private String typeName;
    private List<Long> notify;
    private List<Long> create;
    private List<Long> consult;
    private List<Long> approve;
    private List<Long> validate;
}
