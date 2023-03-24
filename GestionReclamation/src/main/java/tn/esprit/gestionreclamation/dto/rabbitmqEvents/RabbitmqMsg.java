package tn.esprit.gestionreclamation.dto.rabbitmqEvents;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitmqMsg implements Serializable{
    private Long id;
    private String email;
    private String userRoles;
}
