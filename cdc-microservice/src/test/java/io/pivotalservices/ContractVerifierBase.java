package io.pivotalservices;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import io.pivotalservices.fraud.FraudDetectionController;
import org.junit.Before;

/**
 * Required by the Client Driven Contracts framework as the base class for all the auto-generated source classes.
 *
 * The Service's contract definitions (the groovy files located in in `/test/resources/contracts`) will be used
 * during the build to create a series of additional test that extend this base class. These test classes will be
 * written to `/build/generated-test-sources` and they will be run during the build phase.
 *
 * This adds an additional layer or behavioural testing that verifies the functionality in our service.
 */
public class ContractVerifierBase {

    @Before
    public void setup() {
        // Set up the programmable Mock for the Controller under test, in this case the `FraudDetectionController`.
        RestAssuredMockMvc.standaloneSetup(new FraudDetectionController());
    }
}
