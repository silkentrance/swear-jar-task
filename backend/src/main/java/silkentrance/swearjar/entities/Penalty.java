package silkentrance.swearjar.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * TODO document
 * The class <em>Penalty</em> models an {@link Entity} for storing <em>monetary penalties</em> for individual
 * {@link TeamMember}s.
 *
 * @see PenaltyRepository
 * @see TeamMember
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"team_member_id", "amount", "date_time"})})
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@ToString
public final class Penalty extends AbstractEntity {
    /**
     * The team member.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    @NonNull
    @ToString.Exclude
    private TeamMember teamMember;

    /**
     * The date and time this was created.
     */
    @Column(name = "date_time", nullable = false, updatable = false)
    @NonNull
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    /**
     * The amount in (Euro) cents.
     */
    @Column(nullable = false)
    private int amount;

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Penalty) {
            final Penalty penalty = (Penalty) other;
            return super.equals(other)
                    && this.getTeamMember().equals(penalty.getTeamMember())
                    && this.getAmount() == penalty.getAmount()
                    && this.getDateTime().equals(penalty.getDateTime());
        }
        return false;
    }
}
