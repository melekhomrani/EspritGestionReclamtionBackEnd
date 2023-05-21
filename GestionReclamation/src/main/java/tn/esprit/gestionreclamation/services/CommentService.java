package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.CommentRequest;
import tn.esprit.gestionreclamation.exceptions.UserNotAuthorizedException;
import tn.esprit.gestionreclamation.models.Comments;
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

    public Optional<List<Comments>> getAllByReclamation(Reclamation reclamation){
        return commentRepository.findCommentsByReclamationAndVisibilityTrue(reclamation);
    }

    public Optional<List<Comments>> getAllByReclamationId(Long id){
        var reclamation = reclamationService.getReclamationById(id).orElseThrow();
        return commentRepository.findCommentsByReclamationAndVisibilityTrue(reclamation);
    }

    public Optional<List<Comments>> getAllByReclamationIgnoreVisibility(Reclamation reclamation){
        return commentRepository.findCommentsByReclamation(reclamation);
    }

    public Optional<List<Comments>> getAllByReclamationIdIgnoreVisibility(Long id){
        var reclamation = reclamationService.getReclamationById(id).orElseThrow();
        return commentRepository.findCommentsByReclamation(reclamation);
    }

    public Optional<List<Comments>> getAllByReclamationAndVisibilityFalse(Reclamation reclamation){
        return commentRepository.findCommentsByReclamationAndVisibilityFalse(reclamation);
    }

    public Optional<Comments> createComment(String request, Authentication authentication, Long id){
        var reclamation = reclamationService.getReclamationById(id);
        if(reclamation.isEmpty()) throw new EntityNotFoundException("No Reclamation with that id found");
        Comments newComments = Comments.builder()
                .author(authentication.getProfile())
                .reclamation(reclamation.get())
                .text(request)
                .visibility(true)
                .build();
        var saved = commentRepository.saveAndFlush(newComments);
        return Optional.of(saved);
    }

    public Boolean switchVisibility(Long id, Authentication authentication){ //Only if user is Admin or validator or comment owner.
        Comments comments = commentRepository.findById(id).orElseThrow();
        if(isAuthorCheck(authentication, comments) || isValidatorCheck(authentication, comments) || userService.isAdmin(authentication)){
            comments.setVisibility(!comments.getVisibility());
            commentRepository.saveAndFlush(comments);
            return true;
        }
        throw new UserNotAuthorizedException("You are not authorized for this action");
    }

    public Boolean deleteComment(Long id, Authentication authentication){
        Comments comments = commentRepository.findById(id).orElseThrow();
        if(userService.isAdmin(authentication)){
            commentRepository.delete(comments);
            return true;
        }
        throw new UserNotAuthorizedException("You are not authorized for this action");
    }

    public Boolean isAuthorCheck(Authentication authentication, Comments comments){
        return authentication.getProfile().equals(comments.getAuthor());
    }

    public Boolean isValidatorCheck(Authentication authentication, Comments comments){
        var reclamation = comments.getReclamation();
        var accessFlow = accessFlowService.getAccessFlowByReclamation(reclamation).orElseThrow();
        return userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getValidate());
    }

    public Optional<List<Comments>> getAllByReclamationIdAndVisibilityFalse(Long id) {
        var reclamation = reclamationService.getReclamationById(id).orElseThrow();
        return commentRepository.findCommentsByReclamationAndVisibilityFalse(reclamation);
    }
}
