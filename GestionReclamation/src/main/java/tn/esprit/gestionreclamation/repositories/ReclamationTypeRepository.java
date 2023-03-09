package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.ReclamationType;

@Repository
public interface ReclamationTypeRepository extends JpaRepository<ReclamationType, Long> {
}
