package io.pivotalservices.fraud;

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

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private HttpHeaders headers;

	@Before
    public void setup(){
        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", "application/vnd.fraud.v1+json");

    }


	@Test
	public void contextLoads() {
	}

	@Test
    public void shouldRejectWhenAmountIsTooHigh(){

	    // Given
        FraudCheck check = new FraudCheck("", new BigDecimal(5001));
        HttpEntity entity = new HttpEntity(check, headers);

        // When
        ResponseEntity<FraudCheckResult> result = restTemplate.exchange("/fraudcheck", HttpMethod.PUT, entity, FraudCheckResult.class);


        // Then
        Assert.assertEquals("Amount too high", result.getBody().getResultText());

    }

    @Test
    public void shouldAcceptWhenAmountIsOK(){

        // Given
        FraudCheck check = new FraudCheck("", new BigDecimal(4999));
        HttpEntity entity = new HttpEntity(check, headers);

        // When
        ResponseEntity<FraudCheckResult> result = restTemplate.exchange("/fraudcheck", HttpMethod.PUT, entity, FraudCheckResult.class);


        // Then
        Assert.assertEquals("Accepted", result.getBody().getResultText());

    }

}
