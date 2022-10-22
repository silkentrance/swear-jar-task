package silkentrance.swearjar.api;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import silkentrance.swearjar.entities.Penalty;
import silkentrance.swearjar.entities.PenaltyRepository;
import silkentrance.swearjar.entities.TeamMember;
import silkentrance.swearjar.entities.TeamMemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class TeamMemberDetailsController {
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class TeamMemberDetailResponse extends ApiResponse {
        private long id;

        private String name;

        private int amountCalculated;

        private int amountAdjusted;

        @Singular
        private List<PenaltyDetail> penalties;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PenaltyDetail {
        private long id;

        private LocalDateTime dateTime;

        private int amount;
    }

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @CrossOrigin(originPatterns = "*:*")
    @GetMapping(path = "/api/team_member/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamMemberDetailResponse> teamMemberDetails(@PathVariable(name = "id") long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body(
                    TeamMemberDetailResponse.builder()
                            .errorMessage("invalid id " + id)
                            .build()
            );
        }
        Optional<TeamMember> teamMemberOptional = teamMemberRepository.findById(id);
        if (!teamMemberOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        final TeamMember teamMember = teamMemberOptional.get();
        final TeamMemberDetailResponse.TeamMemberDetailResponseBuilder<?,?> responseBuilder =
                TeamMemberDetailResponse.builder()
                        .id(teamMember.getId())
                        .name(teamMember.getName())
                        .amountCalculated(penaltyRepository.calculatePenaltyTotalByTeamMember(teamMember))
                        .amountAdjusted(teamMember.getAdjustedPenaltyTotal().getAmount());
        // TODO this will get slow over time :-), refactor to separate method and enable paging and filtering
        final Iterable<Penalty> penalties = penaltyRepository.findByTeamMemberOrderByDateTimeDesc(teamMember);
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
