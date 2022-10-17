package silkentrance.swearjar.api;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import silkentrance.swearjar.entities.TeamMember;
import silkentrance.swearjar.entities.TeamMemberRepository;
import silkentrance.swearjar.entities.PenaltyRepository;

import java.util.List;

@RestController
public class DashboardController {
    /*
    here I spared me from polluting the package namespace with reqres classes that are used by the controller only
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DashboardResponse {
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Member {
            @NonNull
            private Long id;

            @NonNull
            private String name;

            @NonNull
            private Integer amountCalculated;

            @NonNull
            private Integer amountAdjusted;
        }

        @Singular
        private List<Member> members;
    }

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    // TODO for very large teams this will become both slow and memory hungry, so members must be fetched in a separate request and paging must be applied
    @GetMapping(path = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<DashboardResponse> dashboard() {
        Iterable<TeamMember> members = teamMemberRepository.findAllByOrderByNameAsc();
        DashboardResponse.DashboardResponseBuilder builder = DashboardResponse.builder();
        for (TeamMember teamMember : members) {
            builder.member(DashboardResponse.Member.builder()
                    .id(teamMember.getId())
                    .name(teamMember.getName())
                    .amountCalculated(penaltyRepository.calculatePenaltyTotalByTeamMember(teamMember))
                    .amountAdjusted(teamMember.getAdjustedPenaltyTotal().getAmount())
                    .build());
        }
        return ResponseEntity.ok().body(builder.build());
    }
}
