package silkentrance.swearjar.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import silkentrance.swearjar.entities.AdjustedPenaltyTotalRepository;
import silkentrance.swearjar.entities.PenaltyRepository;
import silkentrance.swearjar.entities.TeamMemberRepository;

public class AbstractControllerTest {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private AdjustedPenaltyTotalRepository adjustedPenaltyTotalRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    protected int getPort() {
        return port;
    }

    protected TestRestTemplate getTestRestTemplate() {
        return testRestTemplate;
    }

    protected TeamMemberRepository getTeamMemberRepository() {
        return teamMemberRepository;
    }

    protected PenaltyRepository getPenaltyRepository() {
        return penaltyRepository;
    }

    protected AdjustedPenaltyTotalRepository getAdjustedPenaltyTotalRepository() {
        return adjustedPenaltyTotalRepository;
    }

    protected String getUri(final String path) {
        return "http://localhost:" + getPort() + "/" + path;
    }
}
