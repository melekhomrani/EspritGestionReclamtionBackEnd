package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.Progress;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.models.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findAllByTypeIn(List<ReclamationType> typeList);
    Integer countAllByDateCreationBetween(LocalDateTime start, LocalDateTime end);
    Integer countAllByAuthor(Users user);

    Integer countAllByAuthorAndProgress(Users user, Progress progress);

    List<Reclamation> findByAuthor(Users users);

    List<Reclamation> findReclamationsByTypeIsInAndProgressIsNotIn(List<ReclamationType> types, List<Progress> progress);
    List<Reclamation> findReclamationsByDateCreationIsBetween(LocalDateTime start, LocalDateTime end);
}
