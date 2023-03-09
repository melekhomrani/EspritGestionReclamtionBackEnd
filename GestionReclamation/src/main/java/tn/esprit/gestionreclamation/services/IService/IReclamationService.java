package tn.esprit.gestionreclamation.services.IService;

import tn.esprit.gestionreclamation.models.Reclamation;

import java.util.List;
import java.util.Optional;

public interface IReclamationService {
    List<Reclamation> getAllReclamations();
    Optional<Reclamation> getReclamationById(Long id);
    Reclamation saveReclamation(Reclamation reclamation);
    List<Reclamation> saveReclamations(List<Reclamation> reclamations);
    Reclamation updateReclamation(Reclamation reclamation);
    void deleteReclamation(Long id);
}
