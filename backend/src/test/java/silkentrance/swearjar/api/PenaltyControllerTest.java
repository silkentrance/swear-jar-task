package silkentrance.swearjar.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import silkentrance.swearjar.entities.TeamMember;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PenaltyControllerTest extends AbstractControllerTest {

    @Nested
    public class AddPenaltyTest {
        private static final String PATH = "api/penalty/";

        @Nested
        public class StandardErrorResponsesTest {
            @Test
            public void addPenaltyMustReturn400WhenNoRequestBodyGiven() {
                assertThat(getTestRestTemplate().postForEntity(getUri(PATH), getHttpEntityFor(null),
                        PenaltyController.AddPenaltyRequest.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            public void addPenaltyMustReturn400WhenTeamMemberNameIsEmpty() {
                assertThat(getTestRestTemplate().postForEntity(getUri(PATH),
                        PenaltyController.AddPenaltyRequest.builder()
                                .memberName("")
                                .amount(0)
                                .build(),
                        PenaltyController.AddPenaltyResponse.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            public void addPenaltyMustReturn400WhenAmountIsZero() {
                assertThat(getTestRestTemplate().postForEntity(getUri(PATH),
                        PenaltyController.AddPenaltyRequest.builder()
                                .memberName(getTeamMember().getName())
                                .amount(0)
                                .build(),
                        PenaltyController.AddPenaltyResponse.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            public void addPenaltyMustReturn400WhenAmountIsNegative() {
                assertThat(getTestRestTemplate().postForEntity(getUri(PATH),
                        PenaltyController.AddPenaltyRequest.builder()
                                .memberName(getTeamMember().getName())
                                .amount(-1)
                                .build(),
                        PenaltyController.AddPenaltyResponse.class).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }
        }

        @Nested
        public class AddPenaltyToExistingMemberTest {
            @Test
            public void addPenaltyMustReturnExpectedResult() {
                ResponseEntity<PenaltyController.AddPenaltyResponse> responseEntity =
                        getTestRestTemplate().postForEntity(getUri(PATH),
                                PenaltyController.AddPenaltyRequest.builder()
                                        .memberName(getTeamMember().getName())
                                        .amount(100)
                                        .build(),
                                PenaltyController.AddPenaltyResponse.class);
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                PenaltyController.AddPenaltyResponse response = responseEntity.getBody();
                assertThat(response.getMemberId()).isEqualTo(getTeamMember().getId());
                assertThat(response.getMemberName()).isEqualTo(getTeamMember().getName());
                assertThat(response.getAmount()).isEqualTo(100);
                assertThat(response.getCalculatedTotal()).isEqualTo(
                        getPenaltyRepository().calculatePenaltyTotalByTeamMember(getTeamMember()));
            }
        }

        @Nested
        public class AddPenaltyToNewMemberTest {
            @Test
            public void addPenaltyMustReturnExpectedResult() {
                final String newMemberName = "newmember";
                ResponseEntity<PenaltyController.AddPenaltyResponse> responseEntity =
                        getTestRestTemplate().postForEntity(getUri(PATH),
                                PenaltyController.AddPenaltyRequest.builder()
                                        .memberName(newMemberName)
                                        .amount(100)
                                        .build(),
                                PenaltyController.AddPenaltyResponse.class);
                assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
                PenaltyController.AddPenaltyResponse response = responseEntity.getBody();
                Optional<TeamMember> newMemberOptional = getTeamMemberRepository().findOneByName(newMemberName);
                assertThat(newMemberOptional.isPresent()).isTrue();
                TeamMember newTeamMember = newMemberOptional.get();
                assertThat(newTeamMember.getName()).isEqualTo(newMemberName);
                assertThat(response.getMemberId()).isEqualTo(newTeamMember.getId());
                assertThat(response.getMemberName()).isEqualTo(newTeamMember.getName());
                assertThat(response.getAmount()).isEqualTo(100);
                assertThat(response.getCalculatedTotal()).isEqualTo(
                        getPenaltyRepository().calculatePenaltyTotalByTeamMember(newTeamMember));
            }
        }
    }
}
