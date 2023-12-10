package Spring.Paivakirja;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaivakirjaEntryRepository extends JpaRepository<PaivakirjaEntry, Long> {
    List<PaivakirjaEntry> findByUser(PaivakirjaUser user);
}


