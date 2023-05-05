package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.CommentRequest;
import tn.esprit.gestionreclamation.exceptions.UserNotAuthorizedException;
import tn.esprit.gestionreclamation.models.Comment;
import tn.esprit.gestionreclamation.models.Reclamation;
import tn.esprit.gestionreclamation.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReclamationService reclamationService;
    private final AccessFlowService accessFlowService;
    private final UserService userService;

    public Optional<List<Comment>> getAllByReclamation(Reclamation reclamation){
        return commentRepository.findCommentsByReclamationAndVisibilityTrue(reclamation);
    }

    public Optional<List<Comment>> getAllByReclamationId(Long id){
        var reclamation = reclamationService.getReclamationById(id).orElseThrow();
        return commentRepository.findCommentsByReclamationAndVisibilityTrue(reclamation);
    }

    public Optional<List<Comment>> getAllByReclamationIgnoreVisibility(Reclamation reclamation){
        return commentRepository.findCommentsByReclamation(reclamation);
    }

    public Optional<List<Comment>> getAllByReclamationIdIgnoreVisibility(Long id){
        var reclamation = reclamationService.getReclamationById(id).orElseThrow();
        return commentRepository.findCommentsByReclamation(reclamation);
    }

    public Optional<List<Comment>> getAllByReclamationAndVisibilityFalse(Reclamation reclamation){
        return commentRepository.findCommentsByReclamationAndVisibilityFalse(reclamation);
    }

    public Optional<Comment> createComment(CommentRequest request, Authentication authentication){
        var reclamation = reclamationService.getReclamationById(request.getReclamationId());
        if(reclamation.isEmpty()) throw new EntityNotFoundException("No Reclamation with that id found");
        Comment newComment = Comment.builder()
                .author(authentication.getProfile())
                .reclamation(reclamation.get())
                .text(request.getText())
                .visibility(true)
                .build();
        return Optional.of(commentRepository.save(newComment));
    }

    public Boolean switchVisibility(Long id, Authentication authentication){ //Only if user is Admin or validator or comment owner.
        Comment comment = commentRepository.findById(id).orElseThrow();
        if(isAuthorCheck(authentication, comment) || isValidatorCheck(authentication, comment) || userService.isAdmin(authentication)){
            comment.setVisibility(!comment.getVisibility());
            commentRepository.saveAndFlush(comment);
            return true;
        }
        throw new UserNotAuthorizedException("You are not authorized for this action");
    }

    public Boolean deleteComment(Long id, Authentication authentication){
        Comment comment = commentRepository.findById(id).orElseThrow();
        if(userService.isAdmin(authentication)){
            commentRepository.delete(comment);
            return true;
        }
        throw new UserNotAuthorizedException("You are not authorized for this action");
    }

    public Boolean isAuthorCheck(Authentication authentication, Comment comment){
        return authentication.getProfile().equals(comment.getAuthor());
    }

    public Boolean isValidatorCheck(Authentication authentication, Comment comment){
        var reclamation = comment.getReclamation();
        var accessFlow = accessFlowService.getAccessFlowByReclamation(reclamation).orElseThrow();
        return userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getValidate());
    }

    public Optional<List<Comment>> getAllByReclamationIdAndVisibilityFalse(Long id) {
        var reclamation = reclamationService.getReclamationById(id).orElseThrow();
        return commentRepository.findCommentsByReclamationAndVisibilityFalse(reclamation);
    }
}
