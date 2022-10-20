package silkentrance.swearjar.api;

import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import silkentrance.swearjar.entities.*;

public class AbstractControllerTest {

    @Autowired
    @Getter(AccessLevel.PACKAGE)
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    @Getter(AccessLevel.PACKAGE)
    private AdjustedPenaltyTotalRepository adjustedPenaltyTotalRepository;

    @Autowired
    @Getter(AccessLevel.PACKAGE)
    private PenaltyRepository penaltyRepository;

    @LocalServerPort
    @Getter(AccessLevel.PACKAGE)
    private int port;

    @Autowired
    @Getter(AccessLevel.PACKAGE)
    private TestRestTemplate testRestTemplate;

    @Getter(AccessLevel.PACKAGE)
    private TeamMember teamMember;

    @Getter(AccessLevel.PACKAGE)
    private AdjustedPenaltyTotal adjustedPenaltyTotal;

    @Getter(AccessLevel.PACKAGE)
    private Penalty penalty1;

    @Getter(AccessLevel.PACKAGE)
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

    protected <T> HttpEntity<T> getHttpEntityFor(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(null, headers);
    }

    protected String getUri(final String path) {
        return "http://localhost:" + getPort() + "/" + path;
    }
}
