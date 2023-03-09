package tn.esprit.gestionreclamation.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReclamationType {
    @Id
    @GeneratedValue
    private Long id;
    private String typeName;
    @CreationTimestamp
    private LocalDateTime dateCreation = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime dateModification = LocalDateTime.now();
}