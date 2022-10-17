package silkentrance.swearjar.entities;

import lombok.*;

import javax.persistence.*;

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
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AdjustedPenaltyTotal {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PACKAGE)
    private Long id;

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
    @NonNull
    @Builder.Default
    private Integer amount = 0;

    @Override
    public boolean equals(final Object o) {
        if (o instanceof AdjustedPenaltyTotal) {
            final AdjustedPenaltyTotal other = (AdjustedPenaltyTotal) o;
            return (this == other
                    || (this.getId().equals(other.getId())
                    && this.getAmount().equals(other.getAmount())));
        }
        return false;
    }

    @Override
    public int hashCode() {
        final Object id = this.getId();
        // newly created entities do not have an id assigned
        if (id == null) {
            return super.hashCode();
        }
        return id.hashCode();
    }
}
