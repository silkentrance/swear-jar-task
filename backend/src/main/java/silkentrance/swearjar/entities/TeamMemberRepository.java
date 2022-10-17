package silkentrance.swearjar.entities;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {
    Iterable<TeamMember> findAllByOrderByNameAsc();

    Optional<TeamMember> findOneByName(@NonNull String name);

    /* here, I spared me the additional @Service layer and moved everything into the DAO, e.g.
    @Service
    public class TeamMemberService { ... }

    And I spared me the repository impl class, also
     */
    /**
     * Finds an existing member or creates (and saves) a new member.
     *
     * @param name the member's name
     * @return the existing or newly created member
     */
    @Transactional
    default TeamMember findOrCreateMember(@NonNull String name) {
        return findOneByName(name).orElseGet(() -> save(TeamMember.builder()
                .name(name)
                .build()));
    }
}
