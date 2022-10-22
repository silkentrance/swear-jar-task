package silkentrance.swearjar.api;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import silkentrance.swearjar.entities.AdjustedPenaltyTotal;
import silkentrance.swearjar.entities.AdjustedPenaltyTotalRepository;
import silkentrance.swearjar.entities.TeamMember;
import silkentrance.swearjar.entities.TeamMemberRepository;

import java.util.Optional;

@RestController
public class AdjustedPenaltyTotalController {
    /*
    here I spared me from polluting the package namespace with reqres classes that are used by the controller only
     */

    /**
     * TODO document
     * The class <em>AdjustPenaltyTotalRequest</em> models a <em>DTO</em> that is used for manually adjusting the
     * {@link AdjustedPenaltyTotal} for existing {@link TeamMember}s.
     *
     * @see #adjustPenaltyTotal(AdjustPenaltyTotalRequest)
     * @see AdjustPenaltyTotalResponse
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AdjustPenaltyTotalRequest {
        private long memberId;
        private int amount;
    }

    /**
     * TODO document
     * The class <em>AdjustPenaltyTotalResponse</em> models a <em>DTO</em> that is returned when manually adjusting the
     * {@link AdjustedPenaltyTotal} for existing {@link TeamMember}s.
     *
     * @see #adjustPenaltyTotal(AdjustPenaltyTotalRequest)
     * @see AdjustPenaltyTotalRequest
     */
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class AdjustPenaltyTotalResponse extends ApiResponse {
        private long memberId;
        private int amount;
    }

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private AdjustedPenaltyTotalRepository adjustedPenaltyTotalRepository;

    @CrossOrigin(originPatterns = "*:*")
    @PostMapping(path = "/api/penalty_total", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<AdjustPenaltyTotalResponse> adjustPenaltyTotal(
            @RequestBody AdjustPenaltyTotalRequest request) {
        if (request.getMemberId() <= 0) {
            return ResponseEntity.badRequest().body(
                    AdjustPenaltyTotalResponse.builder()
                            .errorMessage("invalid member id " + request.getMemberId())
                            .build()
            );
        }
        if (request.getAmount() < 0) {
            return ResponseEntity.badRequest().body(
                    AdjustPenaltyTotalResponse.builder()
                            .errorMessage("invalid amount " + request.getAmount())
                            .build()
            );
        }
        Optional<TeamMember> memberOptional = teamMemberRepository.findById(request.getMemberId());
        if (!memberOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        TeamMember teamMember = memberOptional.get();
        AdjustedPenaltyTotal adjustedPenaltyTotal = teamMember.getAdjustedPenaltyTotal();
        adjustedPenaltyTotal.setAmount(request.getAmount());
        adjustedPenaltyTotalRepository.save(adjustedPenaltyTotal);
        AdjustPenaltyTotalResponse response = AdjustPenaltyTotalResponse.builder()
                .memberId(adjustedPenaltyTotal.getTeamMember().getId())
                .amount(adjustedPenaltyTotal.getAmount())
                .build();
        return ResponseEntity.ok().body(response);
    }
}
