package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.ReclamationTypeService;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reclamationTypes")
public class ReclamationTypeController {

    private final ReclamationTypeService reclamationTypeService;
    private final UserService userService;
    final Authentication authentication;

    @GetMapping
    public List<ReclamationType> getAllReclamationTypes() {
        return reclamationTypeService.getAllReclamationType();
    }

    @GetMapping("/{id}")
    public ReclamationType getReclamationTypeById(@PathVariable Long id) {
        return reclamationTypeService.getReclamationTypeById(id);
    }

    @PostMapping("/new")
    public ReclamationType saveReclamationType(@RequestBody ReclamationType reclamationType) {
        if (userService.isAdmin(authentication)) {
            return reclamationTypeService.saveReclamationType(reclamationType);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @PutMapping("/{id}")
    public ReclamationType updateReclamationType(@PathVariable Long id, @RequestBody ReclamationType reclamationType) {
        if (userService.isAdmin(authentication)) {
            return reclamationTypeService.updateReclamationType(id, reclamationType);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }

    @DeleteMapping("/{id}")
    public void deleteReclamationType(@PathVariable Long id) {
        if (userService.isAdmin(authentication)) {
            reclamationTypeService.deleteReclamationType(id);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }
}
