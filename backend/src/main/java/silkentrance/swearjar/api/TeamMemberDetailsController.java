package silkentrance.swearjar.api;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silkentrance.swearjar.entities.Penalty;
import silkentrance.swearjar.entities.PenaltyRepository;
import silkentrance.swearjar.entities.TeamMember;
import silkentrance.swearjar.entities.TeamMemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class TeamMemberDetailsController {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TeamMemberDetailResponse {
        private @NonNull Long id;
        private @NonNull String name;
        private @NonNull Integer amountCalculated;
        private @NonNull Integer amountAdjusted;
        @Singular
        private List<PenaltyDetail> penalties;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PenaltyDetail {
        private @NonNull Long id;
        private @NonNull LocalDateTime dateTime;
        private @NonNull Integer amount;
    }

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @GetMapping(path = "/team_member/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamMemberDetailResponse> teamMemberDetails(@PathVariable(name = "id") long teamMemberId) {
        if (teamMemberId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<TeamMember> teamMemberOptional = teamMemberRepository.findById(teamMemberId);
        if (!teamMemberOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final TeamMember teamMember = teamMemberOptional.get();
        final TeamMemberDetailResponse.TeamMemberDetailResponseBuilder responseBuilder =
                TeamMemberDetailResponse.builder()
                        .id(teamMember.getId())
                        .name(teamMember.getName())
                        .amountCalculated(penaltyRepository.calculatePenaltyTotalByTeamMember(teamMember))
                        .amountAdjusted(teamMember.getAdjustedPenaltyTotal().getAmount());
        // TODO this will get slow over time :-), refactor to separate method and enable paging and datetime filtering
        final Iterable<Penalty > penalties = penaltyRepository.findByTeamMemberOrderByDateTimeDesc(teamMember);
        for (Penalty penalty : penalties) {
            responseBuilder.penalty(PenaltyDetail.builder()
                    .id(penalty.getId())
                    .dateTime(penalty.getDateTime())
                    .amount(penalty.getAmount())
                    .build());
        }
        return ResponseEntity.ok().body(responseBuilder.build());
    }
}
