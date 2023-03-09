package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.repositories.RoleRepository;
import tn.esprit.gestionreclamation.services.IService.IRoleService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public List<Role> saveRoles(List<Role> roles) {
        return roleRepository.saveAllAndFlush(roles);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
