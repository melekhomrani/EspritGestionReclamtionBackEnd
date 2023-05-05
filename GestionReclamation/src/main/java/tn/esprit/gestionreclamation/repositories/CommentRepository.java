package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.Comment;
import tn.esprit.gestionreclamation.models.Reclamation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findCommentsByReclamationAndVisibilityTrue(Reclamation reclamation);
    Optional<List<Comment>> findCommentsByReclamation(Reclamation reclamation);
    Optional<List<Comment>> findCommentsByReclamationAndVisibilityFalse(Reclamation reclamation);
}
