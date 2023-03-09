package tn.esprit.gestionreclamation.services.IService;

import tn.esprit.gestionreclamation.models.ReclamationType;

import java.util.List;
import java.util.Optional;


public interface IReclamationTypeService {
    List<ReclamationType> getAllReclamationType();
    Optional<ReclamationType> getReclamationTypeById(Long id);
    ReclamationType saveReclamationType(ReclamationType reclamationType);
    List<ReclamationType> saveReclamationTypes(List<ReclamationType> reclamationTypes);
    ReclamationType updateReclamationType(ReclamationType reclamationType);
    void deleteReclamationType(Long id);

}
