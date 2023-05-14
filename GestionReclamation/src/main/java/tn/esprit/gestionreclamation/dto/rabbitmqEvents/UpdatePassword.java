package tn.esprit.gestionreclamation.dto.rabbitmqEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassword implements Serializable {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
