package io.pivotalservices.fraud;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Even when using consumer driven contracts and automated testing, it's OK to still have plenty of testing.
 * In this regular JUnit test we are checking the functionality of the service's controller class without the need for
 * Spring Boot at all. This is very fast but does not confirm we have the right runtime configuration or that we have
 * a working REST API on our service.
 */
public class StandardJUnitTest {

    FraudDetectionController controller;

    @Before
    public void setup(){
        this.controller = new FraudDetectionController();
    }

    @Test
    public void testLimiter(){
        Assert.assertTrue(
                controller.amountGreaterThanThreshold(
                        new FraudCheck("", new BigDecimal(5001))
                )
        );

        Assert.assertFalse(
                controller.amountGreaterThanThreshold(
                        new FraudCheck("", new BigDecimal(4999))
                )
        );
    }

    @Test
    public void testFraudCheckResultForTooHigh(){

        // Given
        FraudCheck check = new FraudCheck("", new BigDecimal(5001));

        // When
        FraudCheckResult result = controller.fraudCheck(check);

        // Then
        Assert.assertEquals(result.getResultText(), "Amount too high");
    }

    @Test
    public void testFraudCheckResultForOK(){

        // Given
        FraudCheck check = new FraudCheck("", new BigDecimal(4999));

        // When
        FraudCheckResult result = controller.fraudCheck(check);

        // Then
        Assert.assertEquals(result.getResultText(), "Accepted");
    }
}
