package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.Comments;
import tn.esprit.gestionreclamation.models.Reclamation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    Optional<List<Comments>> findCommentsByReclamationAndVisibilityTrue(Reclamation reclamation);
    Optional<List<Comments>> findCommentsByReclamation(Reclamation reclamation);
    Optional<List<Comments>> findCommentsByReclamationAndVisibilityFalse(Reclamation reclamation);
}
