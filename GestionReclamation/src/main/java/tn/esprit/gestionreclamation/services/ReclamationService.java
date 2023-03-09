package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.repositories.ReclamationRepository;
import tn.esprit.gestionreclamation.services.IService.IReclamationService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReclamationService implements IReclamationService {

    private final ReclamationRepository reclamationRepository;

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    @Override
    public Reclamation saveReclamation(Reclamation reclamation) {
        return reclamationRepository.saveAndFlush(reclamation);
    }

    @Override
    public List<Reclamation> saveReclamations(List<Reclamation> reclamations) {
        return reclamationRepository.saveAllAndFlush(reclamations);
    }

    @Override
    public Reclamation updateReclamation(Reclamation reclamation) {
        return reclamationRepository.saveAndFlush(reclamation);
    }

    @Override
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }
}
