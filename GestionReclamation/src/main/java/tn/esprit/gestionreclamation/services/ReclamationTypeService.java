package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.repositories.ReclamationTypeRepository;
import tn.esprit.gestionreclamation.services.IService.IReclamationTypeService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReclamationTypeService implements IReclamationTypeService {

    private final ReclamationTypeRepository reclamationTypeRepository;

    @Override
    public List<ReclamationType> getAllReclamationType() {
        return reclamationTypeRepository.findAll();
    }

    @Override
    public Optional<ReclamationType> getReclamationTypeById(Long id) {
        return reclamationTypeRepository.findById(id);
    }

    @Override
    public ReclamationType saveReclamationType(ReclamationType reclamationType) {
        return reclamationTypeRepository.save(reclamationType);
    }

    @Override
    public List<ReclamationType> saveReclamationTypes(List<ReclamationType> reclamationTypes) {
        return reclamationTypeRepository.saveAll(reclamationTypes);
    }

    @Override
    public ReclamationType updateReclamationType(ReclamationType reclamationType) {
        return reclamationTypeRepository.save(reclamationType);
    }

    @Override
    public void deleteReclamationType(Long id) {
        reclamationTypeRepository.deleteById(id);
    }
}
