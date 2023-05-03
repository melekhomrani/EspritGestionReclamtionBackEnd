package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.gestionreclamation.models.Users;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long role;
}
