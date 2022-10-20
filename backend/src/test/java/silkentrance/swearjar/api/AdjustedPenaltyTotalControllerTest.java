package silkentrance.swearjar.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdjustedPenaltyTotalControllerTest extends AbstractControllerTest {

    private static final String PATH = "penalty_total/";

    @Nested
    public class StandardErrorResponsesTest {
        @Test
        public void adjustPenaltyTotalMustReturn400WhenNoRequestBodyGiven() {
            assertThat(getTestRestTemplate().postForEntity(getUri(PATH), getHttpEntityFor(null),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void adjustPenaltyTotalMustReturn400WhenTeamMemberIdIsZero() {
            assertThat(getTestRestTemplate().postForEntity(getUri(PATH),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalRequest.builder()
                            .memberId(0L)
                            .amount(0)
                            .build(),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void adjustPenaltyTotalMustReturn400WhenTeamMemberIdIsNegative() {
            assertThat(getTestRestTemplate().postForEntity(getUri(PATH),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalRequest.builder()
                            .memberId(-1L)
                            .amount(0)
                            .build(),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void adjustPenaltyTotalMustReturn404ForUnknownTeamMemberId() {
            assertThat(getTestRestTemplate().postForEntity(getUri(PATH),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalRequest.builder()
                            .memberId(Long.MAX_VALUE)
                            .amount(0)
                            .build(),
                    AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    public class AdjustPenaltyTotalTest {
        @Test
        public void adjustPenaltyTotalMustReturnExpectedResult() {
            ResponseEntity<AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse> responseEntity =
                    getTestRestTemplate().postForEntity(getUri(PATH),
                            AdjustedPenaltyTotalController.AdjustPenaltyTotalRequest.builder()
                                    .memberId(getTeamMember().getId())
                                    .amount(100)
                                    .build(),
                            AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse.class);
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            AdjustedPenaltyTotalController.AdjustPenaltyTotalResponse response = responseEntity.getBody();
            assertThat(response.getMemberId()).isEqualTo(getTeamMember().getId());
            assertThat(response.getAmount()).isEqualTo(100);
        }
    }
}
