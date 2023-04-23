package tn.esprit.gestionreclamation.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Reclamation {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String subject;
    @NotBlank
    private String description;
    @ManyToOne
    private Users author;
    @Enumerated(EnumType.STRING)
    private Progress progress = Progress.WAITING;
    @ManyToOne
    private ReclamationType type;
    @CreationTimestamp
    private LocalDateTime dateCreation;
    @UpdateTimestamp
    private LocalDateTime dateUpdate;
    @Column()
    private Boolean archived = false;
}
