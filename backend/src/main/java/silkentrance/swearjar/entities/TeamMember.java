package silkentrance.swearjar.entities;

import lombok.*;
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
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class TeamMember {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PACKAGE)
    private Long id;

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
    public boolean equals(final Object o) {
        if (o instanceof TeamMember) {
            final TeamMember other = (TeamMember) o;
            return (this == other
                    || (this.getId().equals(other.getId())
                    && this.getName().equals(other.getName())));
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
