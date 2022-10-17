package silkentrance.swearjar.api;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import silkentrance.swearjar.entities.AdjustedPenaltyTotal;
import silkentrance.swearjar.entities.Penalty;
import silkentrance.swearjar.entities.TeamMember;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static silkentrance.swearjar.api.TeamMemberDetailsController.TeamMemberDetailResponse;

// https://spring.io/guides/gs/testing-web/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamMemberDetailsControllerTest extends AbstractControllerTest {

    private static final String PATH = "team_member/{id}";

    @Nested
    public class StandardResponsesTest {
        @Test
        public void teamMemberDetailsMustReturn400WhenTeamMemberIdIsZero() {
            assertThat(getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, 0).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void teamMemberDetailsMustReturn400WhenTeamMemberIdIsNegative() {
            assertThat(getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, -1).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        public void teamMemberDetailsMustReturn404WhenNoTeamMemberHasBeenRecorded() {
            assertThat(getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, Long.MAX_VALUE).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    public class ExistingMemberDetailsTest {
        private TeamMember teamMember;
        private AdjustedPenaltyTotal adjustedPenaltyTotal;
        private Penalty penalty1;
        private Penalty penalty2;

        @BeforeEach
        public void setUp() {
            teamMember = getTeamMemberRepository().findOrCreateMember("Tester");
            adjustedPenaltyTotal = getAdjustedPenaltyTotalRepository().createWithMember(teamMember);
            adjustedPenaltyTotal.setAmount(300);
            getAdjustedPenaltyTotalRepository().save(adjustedPenaltyTotal);
            penalty1 = getPenaltyRepository().createWithMemberAndAmount(teamMember, 600);
            penalty2 = getPenaltyRepository().createWithMemberAndAmount(teamMember, 400);
        }

        @AfterEach
        public void tearDown() {
            getTeamMemberRepository().deleteAll();
            getPenaltyRepository().deleteAll();
            getAdjustedPenaltyTotalRepository().deleteAll();
            teamMember = null;
            adjustedPenaltyTotal = null;
            penalty1 = null;
            penalty2 = null;
        }

        @Test
        public void teamMemberDetailsMustReturnExpectedResult() {
            ResponseEntity<TeamMemberDetailResponse> responseEntity = getTestRestTemplate().getForEntity(getUri(PATH),
                    TeamMemberDetailResponse.class, teamMember.getId());
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            TeamMemberDetailResponse response = responseEntity.getBody();
            assertThat(response.getId()).isEqualTo(teamMember.getId());
            assertThat(response.getName()).isEqualTo(teamMember.getName());
            assertThat(response.getAmountCalculated()).isEqualTo(penalty1.getAmount() + penalty2.getAmount());
            assertThat(response.getAmountAdjusted()).isEqualTo(adjustedPenaltyTotal.getAmount());
            List<TeamMemberDetailsController.PenaltyDetail> penalties = response.getPenalties();
            assertThat(penalties.size()).isEqualTo(2);
            TeamMemberDetailsController.PenaltyDetail penaltyDetail = penalties.get(0);
            assertThat(penaltyDetail.getDateTime()).isEqualTo(penalty2.getDateTime());
            assertThat(penaltyDetail.getAmount()).isEqualTo(penalty2.getAmount());
            penaltyDetail = penalties.get(1);
            assertThat(penaltyDetail.getDateTime()).isEqualTo(penalty1.getDateTime());
            assertThat(penaltyDetail.getAmount()).isEqualTo(penalty1.getAmount());
        }
    }
}
