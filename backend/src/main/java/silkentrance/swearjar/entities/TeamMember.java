package silkentrance.swearjar.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

/**
 * TODO document
 * The class <em>TeamMember</em> models an {@link Entity} for storing <em>TeamMember</em> specific data.
 *
 * @see TeamMemberRepository
 * @see Penalty
 * @see AdjustedPenaltyTotal
 */
@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamMember extends AbstractEntity {
    /**
     * The team member's name.
     */
    @Column(unique = true, nullable = false)
    @NonNull
    private String name;

    /**
     * The existing {@link Penalty}s.
     */
    @OneToMany(mappedBy = "teamMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Penalty> penalties;

    /**
     * The {@link AdjustedPenaltyTotal}.
     */
    // cannot use optional = false here
    @OneToOne(mappedBy = "teamMember", orphanRemoval = true)
    @ToString.Exclude
    private AdjustedPenaltyTotal adjustedPenaltyTotal;

    @Override
    public boolean equals(final Object other) {
        if (other instanceof TeamMember) {
            final TeamMember teamMember = (TeamMember) other;
            return super.equals(other)
                     && this.getName().equals(teamMember.getName());
        }
        return false;
    }
}
