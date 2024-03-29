package tn.esprit.gestionreclamation.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comments {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Users author;
    @ManyToOne
    private Reclamation reclamation;
    private String text;
    @CreationTimestamp
    private LocalDateTime dateCreation;
    @UpdateTimestamp
    private LocalDateTime dateModification;
    private Boolean visibility = true;
}
