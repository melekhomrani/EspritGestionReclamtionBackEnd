package tn.esprit.authService.dto.rabbitmqEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmailMsg implements Serializable {
    private String email;
    private Long id;
}
