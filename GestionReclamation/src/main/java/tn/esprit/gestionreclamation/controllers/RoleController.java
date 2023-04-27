package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.RoleService;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;
    final Authentication authentication;

    @GetMapping
    public List<Role> getAllRoles() {
        if (userService.isAdmin(authentication)) {
            return roleService.getAllRoles();
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        if (userService.isAdmin(authentication)) {
            return roleService.getRoleById(id);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @PostMapping
    public Role saveRole(@RequestBody Role role) {
        if (userService.isAdmin(authentication)) {
            return roleService.saveRole(role);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role role) {
        if (userService.isAdmin(authentication)) {
            return roleService.updateRole(id, role);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        if (userService.isAdmin(authentication)) {
            roleService.deleteRole(id);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }
}
