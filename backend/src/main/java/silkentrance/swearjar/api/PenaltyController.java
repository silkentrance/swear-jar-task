package silkentrance.swearjar.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import silkentrance.swearjar.entities.*;

@RestController
public class PenaltyController {
    /*
    here I spared me from polluting the package namespace with reqres classes that are used by the controller only
     */
    /**
     * TODO document
     * The class <em>AddPenaltyRequest</em> models a <em>DTO</em> that is used for adding {@link Penalty}s for
     * individual {@link TeamMember}s by their {@link #memberName}.
     *
     * If a <em>TeamMember</em> does not exist, it will be created.
     *
     * @see #addPenalty(AddPenaltyRequest)
     * @see AddPenaltyResponse
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AddPenaltyRequest {
        private String memberName;
        private Integer amount;
    }

    /**
     * TODO document
     * The class <em>AddPenaltyResponse</em> models a <em>DTO</em> that is returned when adding {@link Penalty}s for
     * individual {@link TeamMember}s.
     *
     * The <em>DTO</em> includes the newly calculated penalty totals that can be used to update client side views.
     *
     * @see #addPenalty(AddPenaltyRequest)
     * @see AddPenaltyRequest
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AddPenaltyResponse {
        private Long memberId;
        private Integer amount;
        private Integer calculatedTotal;
    }

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    /* alternatively
    @Autowired
    public PenaltyController(TeamMemberRepository teamMemberRepository, PenaltyRepository penaltyRepository) {
        this.teamMemberRepository = teamMemberRepository;
        this.penaltyRepository = penaltyRepository;
    }

    or

    @Autowired
    public void setTeamMemberRepository(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    ...
     */

    @PostMapping(path = "/penalty", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<AddPenaltyResponse> addPenalty(@RequestBody AddPenaltyRequest request) {
        if ("".equals(request.getMemberName().trim())) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        final TeamMember teamMember = teamMemberRepository.findOrCreateMember(request.getMemberName());
        final Penalty penalty = penaltyRepository.createWithMemberAndAmount(teamMember, request.getAmount());
        AddPenaltyResponse response = AddPenaltyResponse.builder()
                .memberId(penalty.getTeamMember().getId())
                .amount(penalty.getAmount())
                .calculatedTotal(penaltyRepository.calculatePenaltyTotalByTeamMember(teamMember))
                .build();
        return ResponseEntity.ok().body(response);
    }
}
