package tn.esprit.authService.dto.rabbitmqEvents;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RabbitmqMsg implements Serializable {
    private Long id;
    private String email;
    private String userRoles;
}