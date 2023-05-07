package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.AccessFlowRequest;
import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.services.AccessFlowService;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/accessflows")
@RequiredArgsConstructor
public class AccessFlowController {
    private final AccessFlowService accessFlowService;
    private final Authentication authentication;
    private final UserService userService;

    @GetMapping
    public List<AccessFlow> getAllAccessFlows(){
        if(userService.isAdmin(authentication)){
            return accessFlowService.getAllAccessFlow();
        }
        throw new ForbiddenException("You are not authorized for this action");
    }

    @GetMapping("/{id}")
    public AccessFlow getAccessFlow(@PathVariable Long id){
        if(userService.isAdmin(authentication)){
            return accessFlowService.getAccessFlowById(id);
        }
        throw new ForbiddenException("You are not authorized");
    }

    @PutMapping("/{id}")
    public AccessFlow updateAccessFlow(@RequestBody AccessFlowRequest accessFlow, @PathVariable Long id){
        if(userService.isAdmin(authentication)){
            return accessFlowService.updateAccessFlow(id, accessFlow);
        }
        throw new ForbiddenException("You are not authorized");
    }
}
