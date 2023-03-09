package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long roleId;
    private String userRole;
}
