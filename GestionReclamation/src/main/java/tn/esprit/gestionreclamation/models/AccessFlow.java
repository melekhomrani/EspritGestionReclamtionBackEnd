package tn.esprit.gestionreclamation.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessFlow {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private ReclamationType reclamationType;
    @ManyToMany
    private List<Role> notify;
    @ManyToMany
    private List<Role> create;
    @ManyToMany
    private List<Role> consult;
    @ManyToMany
    private List<Role> approve;
    @ManyToMany
    private List<Role> validate;
    @CreationTimestamp
    private LocalDateTime dateCreation;
    @UpdateTimestamp
    private LocalDateTime dateModification;

}