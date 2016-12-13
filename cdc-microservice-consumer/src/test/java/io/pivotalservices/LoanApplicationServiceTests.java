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

/**
 * In this test case we introduce the Stub Runner - a library that can create dynamic 'Mock' services
 * based on the exact same contracts that the server-side team have used to verify their server-side code.
 * Using this contract-based-mock will allow us to remain loosly-coupled but still have visibility of any
 * divergence away from the agreed service contract.
 *
 * In the line below: `@AutoConfigureStubRunner(ids = {"io.pivotalservices:cdc-microservice:+:stubs:6565"}, workOffline = true)`
 * we are telling the Stub Runner to go and get the services contract definitions files from the local Maven repository
 * and then use these definitions to build a Mock service that behaves similarly to how the real service would.
 *
 * If you look carefully in the output as you run this test you should notice that two Spring Boot apps are started.
 * The first is for our class under test, the `LoanApplicationService`.
 * The second is our mock service which is auto-configued in the annotation above and told to run on port 6565.
 * It is this mock service that our `LoanApplicationService` will use at runtime to perform it's fraud checks.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"io.pivotalservices:cdc-microservice:+:stubs:6565"}, workOffline = true)
@DirtiesContext
public class LoanApplicationServiceTests {

    @Autowired
    private LoanApplicationService service;

    /**
     * Test what happens when the loan amount is under the limit.
     */
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

    /**
     * Test what happens when the loan amount is over the limit.
     */
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

