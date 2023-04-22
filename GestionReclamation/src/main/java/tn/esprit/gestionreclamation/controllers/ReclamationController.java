package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.exceptions.UserNotAuthorizedException;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.services.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamation")
@RequiredArgsConstructor
public class ReclamationController {
    final ReclamationService reclamationService;
    final Authentication authentication;
    final UserService userService;
    final AccessFlowService accessFlowService;

    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamation(){
        if(userService.isAdmin(authentication)){
            return ResponseEntity.ok(reclamationService.getAllReclamations());
        } else {

        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/allowedTypes")
    public ResponseEntity<List<ReclamationType>> getAllowedToCreate() throws AccessDeniedException {
        return ResponseEntity.ok(accessFlowService.findAllowedToCreateTypes(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable Long id){
        Optional<Reclamation> reclamation = reclamationService.getReclamationById(id);
        if(reclamation.isPresent()){
            if(userService.isAuthorized(authentication.getProfile().getRole(), accessFlowService.getAccessFlowByReclamation(reclamation.get()).get().getConsult())){
                return ResponseEntity.ok(reclamation.get());
            }
        }
        throw new UserNotAuthorizedException("Not Authorized");
    }
}
