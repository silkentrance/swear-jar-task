package silkentrance.swearjar.entities;

import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PenaltyRepository extends CrudRepository<Penalty, Long> {
    List<Penalty> findByTeamMemberOrderByDateTimeDesc(@NonNull TeamMember teamMember);

    @Query("SELECT SUM(p.amount) FROM Penalty p WHERE p.teamMember = :teamMember")
    Integer calculatePenaltyTotalByTeamMember(@NonNull @Param("teamMember") TeamMember teamMember);

    /* here, I spared me the additional @Service layer and moved everything into the DAO, e.g.
    @Service
    public class PenaltyService { ... }

    And I spared me the repository impl class, also
     */
    @Transactional
    default Penalty createWithMemberAndAmount(@NonNull TeamMember teamMember, @NonNull Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0, got " + amount);
        }
        return save(Penalty.builder()
                .teamMember(teamMember)
                .amount(amount)
                .build());
    }
}
