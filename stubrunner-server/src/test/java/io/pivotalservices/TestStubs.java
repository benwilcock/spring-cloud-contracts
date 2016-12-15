package io.pivotalservices;

import io.pivotalservices.fraud.FraudCheck;
import io.pivotalservices.fraud.FraudCheckResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Created by benwilcock on 15/12/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestStubs {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @Before
    public void setup(){

        //restTemplate = new RestTemplate();

        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", "application/vnd.fraud.v1+json");
    }

    @Test
    public void shouldRejectWhenAmountIsTooHigh(){

        // Given
        FraudCheck check = new FraudCheck("1234567890", new BigDecimal(99999));
        HttpEntity entity = new HttpEntity(check, headers);

        // When
        ResponseEntity<FraudCheckResult> result = restTemplate.exchange("http://localhost:6565/fraudcheck", HttpMethod.PUT, entity, FraudCheckResult.class);


        // Then
        Assert.assertEquals("Amount too high", result.getBody().getResultText());
    }

    @Test
    public void shouldRejectWhenAmountIsOK(){

        // Given
        FraudCheck check = new FraudCheck("1234567890", BigDecimal.valueOf(123.123d));
        HttpEntity entity = new HttpEntity(check, headers);

        // When
        ResponseEntity<FraudCheckResult> result = restTemplate.exchange("http://localhost:6565/fraudcheck", HttpMethod.PUT, entity, FraudCheckResult.class);


        // Then
        Assert.assertEquals("Accepted", result.getBody().getResultText());
    }
}
