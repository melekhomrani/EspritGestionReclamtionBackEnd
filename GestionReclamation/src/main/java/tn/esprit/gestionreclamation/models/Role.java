package tn.esprit.gestionreclamation.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String roleName;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<Users> users;
    @CreationTimestamp
    private LocalDateTime dateCreation = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime dateModification = LocalDateTime.now();
}
