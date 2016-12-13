package io.pivotalservices.fraud;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * This is our Fraud Detection microservice's controller which defines the REST API that
 * consumers can use to perform fraud checks on applicants.
 */
@RestController
public class FraudDetectionController {

    private static final String FRAUD_SERVICE_JSON_VERSION_1 = "application/vnd.fraud.v1+json";
    private static final String ACCEPTED = "Accepted";
    private static final String AMOUNT_TOO_HIGH = "Amount too high";
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("5000");

    /**
     * The service offers the `/fraudcheck` endpoint which accepts a 'PUT' request body in the shape of
     * a FraudCheck object and returns a FraudCheckResult containing the status of the applicants application after
     * the fraud check has been completed.
     *
     * If the amount requested is greater than MAX_AMOUNT (5000) the the application is refused, otherwise it
     * is accepted.
     *
     * @param fraudCheck
     * @return
     */
    @RequestMapping(
            value = "/fraudcheck",
            method = PUT,
            consumes = FRAUD_SERVICE_JSON_VERSION_1,
            produces = FRAUD_SERVICE_JSON_VERSION_1)
    public FraudCheckResult fraudCheck(@RequestBody FraudCheck fraudCheck) {

        if (amountGreaterThanThreshold(fraudCheck)) {
            return new FraudCheckResult(FraudCheckStatus.FRAUD, AMOUNT_TOO_HIGH);
        }

        return new FraudCheckResult(FraudCheckStatus.OK, ACCEPTED);
    }

    protected boolean amountGreaterThanThreshold(FraudCheck fraudCheck) {
        return MAX_AMOUNT.compareTo(fraudCheck.getLoanAmount()) < 0;
    }

}

