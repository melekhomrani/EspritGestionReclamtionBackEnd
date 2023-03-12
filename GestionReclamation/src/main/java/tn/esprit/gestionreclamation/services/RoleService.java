package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.exceptions.AlreadyExistsException;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return roleRepository.findById(role.getId()).get();
    }

    public List<Role> getRolesByIds(List<Long> ids) {
        return roleRepository.findAllById(ids);
    }

    public Role getRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return roleRepository.findByName(role.getName()).get();
    }

    public Role saveRole(Role role) {
        Optional<Role> roleToSave = roleRepository.findByName(role.getName());
        if (roleToSave.isPresent()) {
            throw new AlreadyExistsException("Role already exists");
        }
        return roleRepository.saveAndFlush(role);
    }

    public List<Role> saveRoles(List<Role> roles) {
        return roleRepository.saveAllAndFlush(roles);
    }

    public Role updateRole(Long id, Role role) {
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        roleToUpdate.setName(role.getName());
        roleToUpdate.setDateModification(role.getDateModification());
        return roleRepository.saveAndFlush(roleToUpdate);
    }

    public void deleteRole(Long id) {
        Role roleToDelete = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        roleRepository.deleteById(roleToDelete.getId());
    }
}
