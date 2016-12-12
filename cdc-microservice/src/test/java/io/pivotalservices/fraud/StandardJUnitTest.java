package io.pivotalservices.fraud;

import io.pivotalservices.fraud.FraudCheck;
import io.pivotalservices.fraud.FraudCheckResult;
import io.pivotalservices.fraud.FraudDetectionController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by benwilcock on 12/12/2016.
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
