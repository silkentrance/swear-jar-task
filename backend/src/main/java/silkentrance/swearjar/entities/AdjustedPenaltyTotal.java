package silkentrance.swearjar.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * TODO document
 * The class <em>AdjustedPenaltyTotal</em> models an {@link Entity} for storing manually adjusted
 * <em>monetary penalty totals</em> for individual {@link TeamMember}s.
 *
 * @see AdjustedPenaltyTotalRepository
 * @see TeamMember
 * @see Penalty
 */
@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdjustedPenaltyTotal extends AbstractEntity {

    /**
     * The team member.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @ToString.Exclude
    private TeamMember teamMember;

    /**
     * The adjusted amount in (Euro) cents.
     */
    @Column(nullable = false)
    @Builder.Default
    private int amount = 0;

    @Override
    public boolean equals(final Object other) {
        if (other instanceof AdjustedPenaltyTotal) {
            final AdjustedPenaltyTotal adjustedPenaltyTotal = (AdjustedPenaltyTotal) other;
            return super.equals(other)
                    && this.getAmount() == adjustedPenaltyTotal.getAmount();
        }
        return false;
    }
}
