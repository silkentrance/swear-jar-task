package silkentrance.swearjar.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static silkentrance.swearjar.api.TeamMemberDetailsController.TeamMemberDetailResponse;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamMemberDetailsControllerTest extends AbstractControllerTest {

    private static final String PATH = "api/team_member/{id}";

    @Nested
    public class StandardErrorResponsesTest {
        @Test
        public void teamMemberDetailsMustReturn400WhenTeamMemberIdIsZero() {
            assertThat(getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, 0).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void teamMemberDetailsMustReturn400WhenTeamMemberIdIsNegative() {
            assertThat(getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, -1l).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void teamMemberDetailsMustReturn404ForUnknownTeamMemberId() {
            assertThat(getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, Long.MAX_VALUE).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    public class ExistingMemberDetailsTest {
        @Test
        public void teamMemberDetailsMustReturnExpectedResult() {
            ResponseEntity<TeamMemberDetailResponse> responseEntity = getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, getTeamMember().getId());
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            TeamMemberDetailResponse response = responseEntity.getBody();
            assertThat(response.getId()).isEqualTo(getTeamMember().getId());
            assertThat(response.getName()).isEqualTo(getTeamMember().getName());
            assertThat(response.getAmountCalculated()).isEqualTo(getPenalty1().getAmount() + getPenalty2().getAmount());
            assertThat(response.getAmountAdjusted()).isEqualTo(getAdjustedPenaltyTotal().getAmount());
            List<TeamMemberDetailsController.PenaltyDetail> penalties = response.getPenalties();
            assertThat(penalties.size()).isEqualTo(2);
            TeamMemberDetailsController.PenaltyDetail penaltyDetail = penalties.get(0);
            assertThat(penaltyDetail.getDateTime()).isEqualTo(getPenalty2().getDateTime());
            assertThat(penaltyDetail.getAmount()).isEqualTo(getPenalty2().getAmount());
            penaltyDetail = penalties.get(1);
            assertThat(penaltyDetail.getDateTime()).isEqualTo(getPenalty1().getDateTime());
            assertThat(penaltyDetail.getAmount()).isEqualTo(getPenalty1().getAmount());
        }
    }
}
