package tn.esprit.authService.dto.rabbitmqEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.authService.Auth.NewUserReq;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserMsg implements Serializable {
    private String email;
    private Long id;
    private NewUserReq newUserReq;
}
