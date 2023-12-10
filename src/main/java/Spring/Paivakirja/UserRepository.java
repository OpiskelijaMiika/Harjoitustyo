package Spring.Paivakirja;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<PaivakirjaUser, Long> {
    Optional<PaivakirjaUser> findByUsername(String username);
}

