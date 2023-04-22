package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.services.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

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
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/allowedTypes")
    public ResponseEntity<List<ReclamationType>> getAllowedToCreate() throws AccessDeniedException {
        return ResponseEntity.ok(accessFlowService.findAllowedTypes(authentication));
    }
}
