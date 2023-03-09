package tn.esprit.gestionreclamation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {
    private String email;
    private String password;
}

