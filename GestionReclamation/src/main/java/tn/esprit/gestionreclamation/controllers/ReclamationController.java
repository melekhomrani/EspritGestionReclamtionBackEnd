package tn.esprit.gestionreclamation.controllers;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.CalendarResponse;
import tn.esprit.gestionreclamation.dto.ReclamationRequest;
import tn.esprit.gestionreclamation.dto.UpdateProgressRequest;
import tn.esprit.gestionreclamation.exceptions.UserNotAuthorizedException;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.services.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            if(userService.isAuthorized(authentication.getProfile().getRole(), accessFlowService.getAccessFlowByReclamation(reclamation.get()).get().getConsult()) || userService.isAdmin(authentication)){
                return ResponseEntity.ok(reclamation.get());
            }
        }
        throw new UserNotAuthorizedException("Not Authorized");
    }

    @PostMapping
    public ResponseEntity<Reclamation> createReclamation(@RequestBody ReclamationRequest reclamationRequest) throws AccessDeniedException {
        var user = authentication.getProfile();
        var accessFlowTable = accessFlowService.getAccessFlowByTypeId(reclamationRequest.getTypeId());
        if(userService.isAuthorized(user.getRole(), accessFlowTable.getCreate()) || user.getRole().getName().equalsIgnoreCase("Admin")){
            var newReclamation = reclamationService.saveReclamation(reclamationRequest, authentication);
            accessFlowService.sendNotification(newReclamation);
            return ResponseEntity.ok(newReclamation);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/progress/{id}")
    public ResponseEntity<Reclamation> updateProgress(@PathVariable Long id, @RequestBody UpdateProgressRequest updateProgressRequest){
        var updatedReclamation = reclamationService.updateProgress(id, authentication, updateProgressRequest);
        if(updatedReclamation != null) return ResponseEntity.ok(updatedReclamation);
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReclamation(@PathVariable Long id){
        if(userService.isAdmin(authentication)){
            reclamationService.deleteReclamation(id);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/archive/{id}")
    public ResponseEntity<Boolean> setToArchive(@PathVariable Long id){
        if(userService.isAdmin(authentication)){
            reclamationService.switchToArchive(id);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/calendar")
    public ResponseEntity<ArrayList<CalendarResponse>> getCalendarData(@RequestParam String start, @RequestParam String end){
        LocalDate newStart = LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate newEnd = LocalDate.parse(end, DateTimeFormatter.ISO_LOCAL_DATE);
        return ResponseEntity.ok(reclamationService.getCalendarData(newStart, newEnd));
    }

    @GetMapping(value = {"/count", "/count/{state}"})
    public ResponseEntity<Integer> getCountByState(@PathVariable(required = false) String state){
        if(state==null || "".equalsIgnoreCase(state)){
            return ResponseEntity.ok(reclamationService.getCountByUser(authentication));
        } else {
            return ResponseEntity.ok(reclamationService.getStateCountByUser(authentication, state));
        }
    }
}
