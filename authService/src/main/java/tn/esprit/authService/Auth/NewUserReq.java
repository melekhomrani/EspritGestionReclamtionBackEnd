package tn.esprit.authService.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserReq {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long role;
    private Boolean isAdmin;
}
