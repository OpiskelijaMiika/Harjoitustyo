package Spring.Paivakirja;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaivakirjaEntryRepository extends JpaRepository<PaivakirjaEntry, Long> {
}

