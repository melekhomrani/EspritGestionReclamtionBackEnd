package tn.esprit.gestionreclamation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.gestionreclamation.models.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
