package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.StatusHistory;

import java.util.Optional;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    Optional<StatusHistory> findByReclamationId(Long id);
    Optional<StatusHistory> findByLastModifierId(Long id);
}
