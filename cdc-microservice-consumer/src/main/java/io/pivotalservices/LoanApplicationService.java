package io.pivotalservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * This class has a runtime dependency on the `cdc-microservice' and it's `/fraudcheck` REST API.
 * At runtime it will attempt to call this API using a standard Spring RestTemplate. The endpoint, the requests
 * and the responses form the 'contract' that this application relies upon to communicate with the service.
 * Therefore, it's vital that we have confidence that we are communicating with the server in the way that we have
 * agreed. If we, or the server deviate from this contract it's helpful to know as soon as possible so that
 * steps can be taken to rectify the divergence before users are impacted.
 *
 * In order to do obtain this certainty without creating tight-coupling, we can use the same contracts that the server
 * team uses to define a mock that mimics the real thing. Head to the test cases in `/test/java/io/pivotalservices`
 * to find out more...
 */
@Service
public class LoanApplicationService {

	private static final String FRAUD_SERVICE_JSON_VERSION_1 =
			"application/vnd.fraud.v1+json";

	private final RestTemplate restTemplate;

	private int port = 6565;

	@Autowired
	public LoanApplicationService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public LoanApplicationResult loanApplication(LoanApplication loanApplication) {
		FraudServiceRequest request =
				new FraudServiceRequest(loanApplication);

		FraudServiceResponse response =
				sendRequestToFraudDetectionService(request);

		return buildResponseFromFraudResult(response);
	}

	private FraudServiceResponse sendRequestToFraudDetectionService(
			FraudServiceRequest request) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, FRAUD_SERVICE_JSON_VERSION_1);

		// tag::client_call_server[]
		ResponseEntity<FraudServiceResponse> response =
				restTemplate.exchange("http://localhost:" + port + "/fraudcheck", HttpMethod.PUT,
						new HttpEntity<>(request, httpHeaders),
						FraudServiceResponse.class);
		// end::client_call_server[]

		return response.getBody();
	}

	private LoanApplicationResult buildResponseFromFraudResult(FraudServiceResponse response) {
		LoanApplicationStatus applicationStatus = null;
		if (FraudCheckStatus.OK == response.getFraudCheckStatus()) {
			applicationStatus = LoanApplicationStatus.LOAN_APPLIED;
		} else if (FraudCheckStatus.FRAUD == response.getFraudCheckStatus()) {
			applicationStatus = LoanApplicationStatus.LOAN_APPLICATION_REJECTED;
		}

		return new LoanApplicationResult(applicationStatus, response.getResultText());
	}

	public void setPort(int port) {
		this.port = port;
	}

}
