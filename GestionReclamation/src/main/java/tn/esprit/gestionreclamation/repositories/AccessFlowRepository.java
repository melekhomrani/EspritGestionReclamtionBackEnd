package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.AccessFlow;

@Repository
public interface AccessFlowRepository extends JpaRepository<AccessFlow, Long> {
}
