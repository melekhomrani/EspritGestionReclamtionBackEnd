package tn.esprit.gestionreclamation.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StatusHistory {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Reclamation reclamation;

    @ManyToOne
    private Users lastModifier;
}

