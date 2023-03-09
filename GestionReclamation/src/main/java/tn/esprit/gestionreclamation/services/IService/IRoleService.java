package tn.esprit.gestionreclamation.services.IService;

import org.springframework.security.core.Authentication;
import tn.esprit.gestionreclamation.models.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Optional<Role> getRoleByRoleName(String roleName);
    Role saveRole(Role role);
    List<Role> saveRoles(List<Role> roles);
    Role updateRole(Role role);
    void deleteRole(Long id);
}
