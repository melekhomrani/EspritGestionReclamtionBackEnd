package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.AccessFlowRequest;
import tn.esprit.gestionreclamation.exceptions.ForbiddenException;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.services.AccessFlowService;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/gest/accessFlows")
@RequiredArgsConstructor
public class AccessFlowController {
    private final AccessFlowService accessFlowService;
    private final Authentication authentication;
    private final UserService userService;

    @GetMapping
    public List<AccessFlow> getAllAccessFlows(){
        if(userService.isAdmin(authentication)){
            log.info("#################################################");
            log.info("get all access flows for all reclamation");
            log.info("#################################################");
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

    // access flow by reclamation id
    @GetMapping("/reclamationType/{id}")
    public AccessFlow getAccessFlowByReclamTypeId(@PathVariable Long id){
        if(userService.isAdmin(authentication)){
            return accessFlowService.getAccessFlowByTypeId(id);
        }
        throw new ForbiddenException("You are not authorized");
    }

    @PostMapping
    public AccessFlow createAccessFlow(@RequestBody AccessFlowRequest accessFlow){
        if(userService.isAdmin(authentication)){
            log.info("#################################################");
            log.info("create access flow for reclamation type id: {}", accessFlow.getReclamationTypeId());
            log.info("#################################################");
            log.info(("access flow: {}"), accessFlow);
            log.info("#################################################");
            return accessFlowService.saveNewAccessFlow(accessFlow);
        }
        throw new ForbiddenException("You are not authorized");
    }

    @PutMapping("/{id}")
    public AccessFlow updateAccessFlow(@RequestBody AccessFlowRequest accessFlow, @PathVariable Long id){
        if(userService.isAdmin(authentication)){
            log.info("#################################################");
            log.info("update access flow for reclamation type id: {}", id);
            log.info("#################################################");
            log.info(("access flow: {}"), accessFlow);
            log.info("#################################################");
            return accessFlowService.updateAccessFlow(id, accessFlow);
        }
        throw new ForbiddenException("You are not authorized");
    }
}
