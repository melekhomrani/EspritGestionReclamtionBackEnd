package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.repositories.ReclamationRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    public Reclamation saveReclamation(Reclamation reclamation) {
        return reclamationRepository.saveAndFlush(reclamation);
    }

    public List<Reclamation> saveReclamations(List<Reclamation> reclamations) {
        return reclamationRepository.saveAllAndFlush(reclamations);
    }

    public Reclamation updateReclamation(Long id, Reclamation reclamation) {
        Reclamation reclamationToUpdate = reclamationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found"));
        reclamationToUpdate.setType(reclamation.getType());
        reclamationToUpdate.setSubject(reclamation.getSubject());
        reclamationToUpdate.setDescription(reclamation.getDescription());
        reclamationToUpdate.setAuthor(reclamation.getAuthor());
        reclamationToUpdate.setProgress(reclamation.getProgress());
        return reclamationRepository.saveAndFlush(reclamationToUpdate);
    }

    public void deleteReclamation(Long id) {
        Reclamation reclamationToDelete = reclamationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found"));
        reclamationRepository.deleteById(reclamationToDelete.getId());
    }
}
