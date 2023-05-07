package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.ReclamationTypeRequest;
import tn.esprit.gestionreclamation.exceptions.AlreadyExistsException;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.repositories.ReclamationTypeRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReclamationTypeService {

    private final ReclamationTypeRepository reclamationTypeRepository;
    private final AccessFlowService accessFlowService;
    private final RoleService roleService;

    public List<ReclamationType> getAllReclamationType() {
        return reclamationTypeRepository.findAll();
    }

    public ReclamationType getReclamationTypeById(Long id) {
        ReclamationType reclamationType = reclamationTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReclamationType not found"));
        return reclamationTypeRepository.findById(reclamationType.getId()).get();
    }

    public ReclamationType saveReclamationType(ReclamationType reclamationType) {
        Optional<ReclamationType> reclamationTypeToSave = reclamationTypeRepository.findByTypeName(reclamationType.getTypeName());
        if (!reclamationTypeToSave.isEmpty()) {
            throw new AlreadyExistsException("ReclamationType already exists");
        }
        return reclamationTypeRepository.save(reclamationType);
    }

    public List<ReclamationType> saveReclamationTypes(List<ReclamationType> reclamationTypes) {
        return reclamationTypeRepository.saveAll(reclamationTypes);
    }

    public ReclamationType updateReclamationType(Long id, ReclamationType reclamationType) {
        ReclamationType reclamationTypeToUpdate = reclamationTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReclamationType not found"));
        reclamationTypeToUpdate.setTypeName(reclamationType.getTypeName());
        return reclamationTypeRepository.save(reclamationTypeToUpdate);
    }

    public void deleteReclamationType(Long id) {
        ReclamationType reclamationTypeToDelete = reclamationTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReclamationType not found"));
        reclamationTypeRepository.deleteById(reclamationTypeToDelete.getId());
    }

    public ReclamationType saveWithAccessFlow(ReclamationTypeRequest reclamationTypeRequest){
        var type = saveReclamationType(ReclamationType.builder()
                .typeName(reclamationTypeRequest.getTypeName())
                .build()
        );
        List< Role > notify = roleService.getRolesByIds(reclamationTypeRequest.getNotify());
        List< Role > consult = roleService.getRolesByIds(reclamationTypeRequest.getConsult());
        List< Role > create = roleService.getRolesByIds(reclamationTypeRequest.getCreate());
        List< Role > approve = roleService.getRolesByIds(reclamationTypeRequest.getApprove());
        List< Role > validate = roleService.getRolesByIds(reclamationTypeRequest.getValidate());
        AccessFlow accessFlow = accessFlowService.saveAccessFlow(AccessFlow.builder()
                .reclamationType(type)
                .consult(consult)
                .notify(notify)
                .create(create)
                .approve(approve)
                .validate(validate)
                .build()
        );
        return type;
    }
}
