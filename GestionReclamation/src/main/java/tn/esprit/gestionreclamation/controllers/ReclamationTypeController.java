package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.ReclamationTypeRequest;
import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.services.AccessFlowService;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.ReclamationTypeService;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gest/reclamationTypes")
public class ReclamationTypeController {

    private final ReclamationTypeService reclamationTypeService;
    private final UserService userService;
    final Authentication authentication;
    final AccessFlowService accessFlowService;

    @GetMapping
    public List<ReclamationType> getAllReclamationTypes() {
        return reclamationTypeService.getAllReclamationType();
    }

    @GetMapping("/{id}")
    public ReclamationType getReclamationTypeById(@PathVariable Long id) {
        return reclamationTypeService.getReclamationTypeById(id);
    }

    @PostMapping
    public ReclamationType saveWithAccessFlow(@RequestBody ReclamationTypeRequest reclamationTypeRequest){
        if(userService.isAdmin(authentication)){
            return reclamationTypeService.saveWithAccessFlow(reclamationTypeRequest);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
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
    public ResponseEntity<Boolean> deleteReclamationType(@PathVariable Long id) {
        if (userService.isAdmin(authentication)) {
            accessFlowService.deleteAccessFlowsByReclamationTypeId(id);
            reclamationTypeService.deleteReclamationType(id);
            return ResponseEntity.ok(true);
        }
        throw new ForbiddenException("You are not authorized to perform this action");
    }
}
