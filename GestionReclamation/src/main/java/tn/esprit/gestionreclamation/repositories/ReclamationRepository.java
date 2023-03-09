package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.Reclamation;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
}
