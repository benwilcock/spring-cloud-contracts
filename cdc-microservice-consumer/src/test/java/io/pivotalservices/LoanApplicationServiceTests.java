package io.pivotalservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"io.pivotalservices:cdc-microservice:+:stubs:6565"}, workOffline = true)
@DirtiesContext
public class LoanApplicationServiceTests {

    @Autowired
    private LoanApplicationService service;

    @Test
    public void shouldSuccessfullyApplyForLoan() {
        // given:
        LoanApplication application = new LoanApplication(new Client("1234567890"),
                123.123);
        // when:
        LoanApplicationResult loanApplication = service.loanApplication(application);
        // then:
        assertThat(loanApplication.getLoanApplicationStatus())
                .isEqualTo(LoanApplicationStatus.LOAN_APPLIED);
        assertEquals("Accepted", loanApplication.getResultText());
    }

    @Test
    public void shouldBeRejectedDueToAbnormalLoanAmount() {
        // given:
        LoanApplication application = new LoanApplication(new Client("1234567890"),
                99999);
        // when:
        LoanApplicationResult loanApplication = service.loanApplication(application);
        // then:
        assertThat(loanApplication.getLoanApplicationStatus())
                .isEqualTo(LoanApplicationStatus.LOAN_APPLICATION_REJECTED);
        assertThat(loanApplication.getResultText()).isEqualTo("Amount too high");
    }
}

