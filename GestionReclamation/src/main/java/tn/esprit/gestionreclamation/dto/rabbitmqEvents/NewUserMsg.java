package tn.esprit.gestionreclamation.dto.rabbitmqEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.gestionreclamation.dto.UserRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserMsg implements Serializable {
    private String email;
    private Long id;
    private UserRequest newUserReq;
}
