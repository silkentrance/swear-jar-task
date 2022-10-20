package silkentrance.swearjar.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DashboardControllerTest extends AbstractControllerTest {

    private static final String PATH = "api/dashboard/";

    @Nested
    public class DashboardTest {
        @Test
        public void dashBoardMustReturnExpectedResult() {
            ResponseEntity<DashboardController.DashboardResponse> responseEntity =
                    getTestRestTemplate().getForEntity(getUri(PATH),
                            DashboardController.DashboardResponse.class);
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            DashboardController.DashboardResponse response = responseEntity.getBody();
            List<DashboardController.DashboardResponse.Member> members = response.getMembers();
            assertThat(members.size()).isEqualTo(1);
            DashboardController.DashboardResponse.Member member = members.get(0);
            assertThat(member.getId()).isEqualTo(getTeamMember().getId());
            assertThat(member.getName()).isEqualTo(getTeamMember().getName());
            assertThat(member.getAmountCalculated()).isEqualTo(
                    getPenaltyRepository().calculatePenaltyTotalByTeamMember(getTeamMember()));
            assertThat(member.getAmountAdjusted()).isEqualTo(getAdjustedPenaltyTotal().getAmount());
        }
    }
}
