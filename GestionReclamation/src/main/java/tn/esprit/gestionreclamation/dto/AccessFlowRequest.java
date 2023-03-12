package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessFlowRequest {
    private Long reclamationTypeId;
    private List<Long> notifyId;
    private List<Long> createId;
    private List<Long> consultId;
    private List<Long> approveId;
    private List<Long> validateId;
}
