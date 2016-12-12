package io.pivotalservices;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import io.pivotalservices.fraud.FraudDetectionController;
import org.junit.Before;

/**
 * Required by the Client Driven Contracts framework as the base class for all generated sources.
 * Created by benwilcock on 08/12/2016.
 *
 * IMPORTANT - The Name of this class has to reflect the package name of the groovy tests. So of the
 * groovy tests are in 'resources/io.pivotalservices.contracts/fraud' this class must be called 'FraudTest'. If the package of
 * the groovy tests changed to 'resources/io.pivotalservices.contracts/fraudster' then this class would need to be renamed to
 * 'FraudsterBase'.
 */
public class ContractVerifierBase {

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new FraudDetectionController()); // Set up the programmable Mock for the Controller.
    }
}
