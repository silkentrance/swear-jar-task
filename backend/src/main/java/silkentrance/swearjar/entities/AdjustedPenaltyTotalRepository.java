package silkentrance.swearjar.entities;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AdjustedPenaltyTotalRepository extends CrudRepository<AdjustedPenaltyTotal, String> {
    Optional<AdjustedPenaltyTotal> findByTeamMember(@NonNull TeamMember teamMember);

    /* here, I spared me the additional @Service layer and moved everything into the DAO, e.g.
    @Service
    public class AdjustedPenaltyTotalService { ... }

    And I spared me the repository impl class, also
     */
    @Transactional
    default AdjustedPenaltyTotal createWithMember(@NonNull TeamMember teamMember) {
        return save(AdjustedPenaltyTotal.builder()
                .teamMember(teamMember)
                .build());
    }
}
