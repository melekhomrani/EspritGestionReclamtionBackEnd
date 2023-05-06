package tn.esprit.gestionreclamation.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.gestionreclamation.dto.CommentRequest;
import tn.esprit.gestionreclamation.models.Comment;
import tn.esprit.gestionreclamation.services.Authentication;
import tn.esprit.gestionreclamation.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/gest/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final Authentication authentication;

    @GetMapping("/{id}")
    public List<Comment> getAllComments(@RequestParam(required = false) Boolean visibility, @PathVariable Long id){
        if(visibility == null){
            return commentService.getAllByReclamationIdIgnoreVisibility(id).orElseThrow();
        } else if (visibility) {
            return commentService.getAllByReclamationId(id).orElseThrow();
        } else {
            return commentService.getAllByReclamationIdAndVisibilityFalse(id).orElseThrow();
        }
    }

    @PostMapping
    public ResponseEntity<Comment> createNewComment(CommentRequest request){
        return ResponseEntity.ok(commentService.createComment(request, authentication).orElseThrow());
    }

    @PutMapping("/visibility/{id}")
    public ResponseEntity<Boolean> switchVisibility(@PathVariable Long id){
        return ResponseEntity.ok(commentService.switchVisibility(id, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.deleteComment(id, authentication));
    }
}
