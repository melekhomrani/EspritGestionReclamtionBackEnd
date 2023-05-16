package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.AccessFlowRequest;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.repositories.AccessFlowRepository;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccessFlowService {

    private final AccessFlowRepository accessFlowRepository;
    private final ReclamationTypeService reclamationTypeService;
    private final RoleService roleService;
    private final UserService userService;

    public List<AccessFlow> getAllAccessFlow() {
        return accessFlowRepository.findAll();
    }

    public AccessFlow getAccessFlowById(Long id) {
        AccessFlow accessFlow = accessFlowRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccessFlow not found"));
        return accessFlowRepository.findById(accessFlow.getId()).get();
    }

    public AccessFlow getAccessFlowByReclamationTypeId(Long id) {
        AccessFlow accessFlow = accessFlowRepository.findByReclamationTypeId(id)
                .orElseThrow(() -> new EntityNotFoundException("AccessFlow not found"));
        return accessFlowRepository.findById(accessFlow.getId()).get();
    }

    public AccessFlow saveAccessFlow(AccessFlow accessFlow){
        return accessFlowRepository.saveAndFlush(accessFlow);
    }

    public AccessFlow saveNewAccessFlow(AccessFlowRequest accessFlow) {
        var type = reclamationTypeService.getReclamationTypeById(accessFlow.getReclamationTypeId());
        List<Role> approveRoles = roleService.getRolesByIds(accessFlow.getApproveId().stream().toList());
        List<Role> consultRoles = roleService.getRolesByIds(accessFlow.getConsultId().stream().toList());
        List<Role> creatRoles = roleService.getRolesByIds(accessFlow.getCreateId().stream().toList());
        List<Role> notifyRoles = roleService.getRolesByIds(accessFlow.getNotifyId().stream().toList());
        List<Role> validateRoles = roleService.getRolesByIds(accessFlow.getValidateId().stream().toList());
        AccessFlow accessFlowToSave = AccessFlow.builder()
                .approve(approveRoles)
                .consult(consultRoles)
                .create(creatRoles)
                .notify(notifyRoles)
                .validate(validateRoles)
                .reclamationType(type)
                .build();
        return accessFlowRepository.saveAndFlush(accessFlowToSave);
    }

    public List<AccessFlow> saveAccessFlows(List<AccessFlow> accessFlows) {
        return accessFlowRepository.saveAllAndFlush(accessFlows);
    }

    public AccessFlow updateAccessFlow(Long id, AccessFlowRequest accessFlow) {
        AccessFlow accessFlowToUpdate = accessFlowRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccessFlow not found"));
        List<Role> approveRoles = roleService.getRolesByIds(accessFlow.getApproveId().stream().toList());
        List<Role> consultRoles = roleService.getRolesByIds(accessFlow.getConsultId().stream().toList());
        List<Role> creatRoles = roleService.getRolesByIds(accessFlow.getCreateId().stream().toList());
        List<Role> notifyRoles = roleService.getRolesByIds(accessFlow.getNotifyId().stream().toList());
        List<Role> validateRoles = roleService.getRolesByIds(accessFlow.getValidateId().stream().toList());
        accessFlowToUpdate.setReclamationType(reclamationTypeService.getReclamationTypeById(accessFlow.getReclamationTypeId()));
        accessFlowToUpdate.setApprove(approveRoles);
        accessFlowToUpdate.setConsult(consultRoles);
        accessFlowToUpdate.setCreate(creatRoles);
        accessFlowToUpdate.setNotify(notifyRoles);
        accessFlowToUpdate.setValidate(validateRoles);
        return accessFlowRepository.save(accessFlowToUpdate);

    }

    public void deleteAccessFlow(Long id) {
        accessFlowRepository.deleteById(id);
    }

    public AccessFlow getAccessFlowByTypeId(Long typeId) {
        var accessFlow = accessFlowRepository.findByReclamationTypeId(typeId);
        if (accessFlow.isPresent()) return accessFlow.get();
        throw new EntityNotFoundException("No Access flow with id: '" + typeId.toString() + "' found");
    }

    public List<Role> getAllowedToCreate(Long accessFlowId) {
        Optional<AccessFlow> accessFlow = accessFlowRepository.findById(accessFlowId);
        if (accessFlow.isPresent()) {
            return accessFlow.get().getCreate();
        }
        throw new EntityNotFoundException("Not Found");
    }

    public List<Role> getAllowedToNotify(Long accessFlowId) {
        Optional<AccessFlow> accessFlow = accessFlowRepository.findById(accessFlowId);
        if (accessFlow.isPresent()) {
            return accessFlow.get().getNotify();
        }
        throw new EntityNotFoundException("Not Found");
    }

    public List<Role> getAllowedToConsult(Long accessFlowId) {
        Optional<AccessFlow> accessFlow = accessFlowRepository.findById(accessFlowId);
        if (accessFlow.isPresent()) {
            return accessFlow.get().getConsult();
        }
        throw new EntityNotFoundException("Not Found");
    }

    public List<Role> getAllowedToValidate(Long accessFlowId) {
        Optional<AccessFlow> accessFlow = accessFlowRepository.findById(accessFlowId);
        if (accessFlow.isPresent()) {
            return accessFlow.get().getValidate();
        }
        throw new EntityNotFoundException("Not Found");
    }

    public List<Role> getAllowedToApprobate(Long accessFlowId) {
        Optional<AccessFlow> accessFlow = accessFlowRepository.findById(accessFlowId);
        if (accessFlow.isPresent()) {
            return accessFlow.get().getApprove();
        }
        throw new EntityNotFoundException("Not Found");
    }

    public Optional<AccessFlow> getAccessFlowByReclamation(Reclamation reclamation) {
        return accessFlowRepository.findByReclamationTypeId(reclamation.getType().getId());
    }

    public List<ReclamationType> findAllowedToCreateTypes(Authentication authentication) throws AccessDeniedException {
        var user = userService.getUserByUserName(authentication.getName());
        if (user.isPresent()) {
            Role role = roleService.getRoleById(user.get().getRole().getId());
            var accessFlowTab = accessFlowRepository.findAccessFlowsByCreateContaining(role);
            if (accessFlowTab.isEmpty()) {
                return List.of(new ReclamationType());
            }
            List<ReclamationType> allowedTypes = new java.util.ArrayList<>();
            accessFlowTab.forEach((i) -> allowedTypes.add(i.getReclamationType()));
            return allowedTypes;
        }
        else throw new AccessDeniedException("Not Authenticated");
    }

    public List<ReclamationType> findAllowedToConsultTypes(Authentication authentication) throws AccessDeniedException {
        var user = userService.getUserByUserName(authentication.getName());
        if (user.isPresent()) {
            Role role = roleService.getRoleById(user.get().getRole().getId());
            var accessFlowTab = accessFlowRepository.findAccessFlowsByConsultContaining(role);
            List<ReclamationType> allowedTypes = List.of(null);
            accessFlowTab.forEach((i) -> allowedTypes.add(i.getReclamationType()));
            return allowedTypes;
        }
        throw new AccessDeniedException("Not Authenticated");
    }

    public List<ReclamationType> findAllowedToNotifyTypes(Authentication authentication) throws AccessDeniedException {
        var user = userService.getUserByUserName(authentication.getName());
        if (user.isPresent()) {
            Role role = roleService.getRoleById(user.get().getRole().getId());
            var accessFlowTab = accessFlowRepository.findAccessFlowsByNotifyContaining(role);
            List<ReclamationType> allowedTypes = List.of(null);
            accessFlowTab.forEach((i) -> allowedTypes.add(i.getReclamationType()));
            return allowedTypes;
        }
        throw new AccessDeniedException("Not Authenticated");
    }

    public List<ReclamationType> findAllowedToApproveTypes(Authentication authentication) throws AccessDeniedException {
        var user = userService.getUserByUserName(authentication.getName());
        if (user.isPresent()) {
            Role role = roleService.getRoleById(user.get().getRole().getId());
            var accessFlowTab = accessFlowRepository.findAccessFlowsByApproveContaining(role);
            List<ReclamationType> allowedTypes = List.of(null);
            accessFlowTab.forEach((i) -> allowedTypes.add(i.getReclamationType()));
            return allowedTypes;
        }
        throw new AccessDeniedException("Not Authenticated");
    }

    public List<ReclamationType> findAllowedToValidateTypes(Authentication authentication) throws AccessDeniedException {
        var user = userService.getUserByUserName(authentication.getName());
        if (user.isPresent()) {
            Role role = roleService.getRoleById(user.get().getRole().getId());
            var accessFlowTab = accessFlowRepository.findAccessFlowsByValidateContaining(role);
            List<ReclamationType> allowedTypes = List.of(null);
            accessFlowTab.forEach((i) -> allowedTypes.add(i.getReclamationType()));
            return allowedTypes;
        }
        throw new AccessDeniedException("Not Authenticated");
    }

    public void sendNotification(Reclamation newReclamation) {
        AccessFlow accessFlow = getAccessFlowByReclamation(newReclamation).orElseThrow();
        List<Role> toNotify = getAllowedToNotify(accessFlow.getId());

    }

    public void deleteAccessFlowsByReclamationTypeId(Long id){
        Optional<AccessFlow> accessFlows = accessFlowRepository.findByReclamationTypeId(id);
        List<AccessFlow> accessFlowsToDelete = Collections.singletonList(accessFlows.orElse(null));
        accessFlowRepository.deleteAll(accessFlowsToDelete);
    }
}
