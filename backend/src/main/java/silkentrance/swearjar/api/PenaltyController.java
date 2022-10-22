package silkentrance.swearjar.api;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import silkentrance.swearjar.entities.*;

import java.util.Optional;

@RestController
public class PenaltyController {
    /*
    here I spared me from polluting the package namespace with reqres classes that are used by the controller only
     */

    /**
     * TODO document
     * The class <em>AddPenaltyRequest</em> models a <em>DTO</em> that is used for adding {@link Penalty}s for
     * individual {@link TeamMember}s by their {@link #memberName}.
     * <p>
     * If a <em>TeamMember</em> does not exist, it will be created.
     *
     * @see #addPenalty(AddPenaltyRequest)
     * @see AddPenaltyResponse
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class AddPenaltyRequest {
        private String memberName = "";

        private int amount;
    }

    /**
     * TODO document
     * The class <em>AddPenaltyResponse</em> models a <em>DTO</em> that is returned when adding {@link Penalty}s for
     * individual {@link TeamMember}s.
     * <p>
     * The <em>DTO</em> includes the newly calculated penalty totals that can be used to update client side views.
     *
     * @see #addPenalty(AddPenaltyRequest)
     * @see AddPenaltyRequest
     */
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class AddPenaltyResponse extends ApiResponse {
        private long memberId;

        @NonNull
        private String memberName = "";

        private int amount;

        private int calculatedTotal;
    }

    /**
     * TODO document
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ChangePenaltyRequest {
        private long id;

        private int amount;
    }

    /**
     * TODO document
     */
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ChangePenaltyResponse extends ApiResponse {
        private long id;

        private int amount;
    }

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private AdjustedPenaltyTotalRepository adjustedPenaltyTotalRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @CrossOrigin(originPatterns = "*:*")
    @PostMapping(path = "/api/penalty", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<AddPenaltyResponse> addPenalty(@RequestBody AddPenaltyRequest request) {
        // TODO add null check on memberName, add missing test
        if ("".equals(request.getMemberName().trim())) {
            return ResponseEntity.badRequest().body(
                    AddPenaltyResponse.builder()
                            .errorMessage("invalid member name '" + request.getMemberName() + "'")
                            .build()
            );
        }
        if (request.getAmount() <= 0) {
            return ResponseEntity.badRequest().body(
                    AddPenaltyResponse.builder()
                            .errorMessage("invalid amount")
                            .memberName(request.memberName)
                            .amount(request.amount)
                            .build()
            );
        }
        final TeamMember teamMember = teamMemberRepository.findOrCreateMember(request.getMemberName());
        // TODO add missing tests for possibly missing adjustedPenaltyTotal
        if (teamMember.getAdjustedPenaltyTotal() == null) {
            AdjustedPenaltyTotal adjustedPenaltyTotal = adjustedPenaltyTotalRepository.createWithMember(teamMember);
            teamMember.setAdjustedPenaltyTotal(adjustedPenaltyTotal);
        }
        final Penalty penalty = penaltyRepository.createWithMemberAndAmount(teamMember, request.getAmount());
        AddPenaltyResponse response = AddPenaltyResponse.builder()
                .memberId(penalty.getTeamMember().getId())
                .memberName(penalty.getTeamMember().getName())
                .amount(penalty.getAmount())
                .calculatedTotal(penaltyRepository.calculatePenaltyTotalByTeamMember(teamMember))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @CrossOrigin(originPatterns = "*:*")
    @DeleteMapping(path = "/api/penalty/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse> removePenalty(@PathVariable(name = "id") long id) {
        // TODO add tests
        if (id <= 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .errorMessage("invalid id " + id)
                            .build()
            );
        }
        Optional<Penalty> penaltyOptional = penaltyRepository.findById(id);
        if (!penaltyOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Penalty penalty = penaltyOptional.get();
        penaltyRepository.delete(penalty);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(originPatterns = "*:*")
    @PostMapping(path = "/api/penalty/change/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ChangePenaltyResponse> changePenalty(@RequestBody ChangePenaltyRequest request) {
        // TODO add tests
        if (request.id <= 0) {
            return ResponseEntity.badRequest().body(
                    ChangePenaltyResponse.builder()
                            .errorMessage("invalid id " + request.id)
                            .build()
            );
        }
        if (request.amount <= 0) {
            return ResponseEntity.badRequest().body(
                    ChangePenaltyResponse.builder()
                            .errorMessage("invalid amount " + request.amount)
                            .build()
            );
        }
        Optional<Penalty> penaltyOptional = penaltyRepository.findById(request.id);
        if (!penaltyOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Penalty penalty = penaltyOptional.get();
        penalty.setAmount(request.amount);
        penaltyRepository.save(penalty);
        return ResponseEntity.ok().build();
    }
}
