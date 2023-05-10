package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.models.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessFlowRepository extends JpaRepository<AccessFlow, Long> {
    Optional<AccessFlow> findById(Long id);
    Optional<AccessFlow> findByReclamationTypeId(Long id);
    List<AccessFlow> findAccessFlowsByReclamationTypeId(Long id);
    List<AccessFlow> findAccessFlowsByCreateContaining(Role role);

    List<AccessFlow> findAccessFlowsByConsultContaining(Role role);

    List<AccessFlow> findAccessFlowsByNotifyContaining(Role role);

    List<AccessFlow> findAccessFlowsByApproveContaining(Role role);

    List<AccessFlow> findAccessFlowsByValidateContaining(Role role);

}
