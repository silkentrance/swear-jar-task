package silkentrance.swearjar.entities;

import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@Profile("test")
public interface PenaltyTestRepository extends PenaltyRepository {
    @Transactional
    default Penalty createWithMemberAndAmountAndDateTime(@NonNull TeamMember teamMember, @NonNull Integer amount,
                                                         @NonNull LocalDateTime dateTime) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0, got " + amount);
        }
        return save(Penalty.builder()
                .teamMember(teamMember)
                .dateTime(dateTime)
                .amount(amount)
                .build());
    }
}
