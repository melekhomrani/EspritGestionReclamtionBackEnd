package tn.esprit.gestionreclamation.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import tn.esprit.gestionreclamation.models.Users;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Authentication {
    private String name;
    private String authorities;
    private Users profile;
}
