package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.gestionreclamation.models.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
