package silkentrance.swearjar.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

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
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public final class Penalty {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.PACKAGE)
    private Long id;

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
    @Column(nullable = false, updatable = false)
    @NonNull
    private Integer amount;

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Penalty) {
            final Penalty other = (Penalty) o;
            return (this == other
                    || (this.getId().equals(other.getId())
                    && this.getTeamMember().equals(other.getTeamMember())
                    && this.getAmount().equals(other.getAmount()))
                    && this.getDateTime().equals(other.getDateTime()));
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
