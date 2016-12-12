package io.pivotalservices;

public class FraudServiceResponse {

	private FraudCheckStatus fraudCheckStatus;

	private String resultText;

	public FraudServiceResponse() {
	}

	public FraudCheckStatus getFraudCheckStatus() {
		return fraudCheckStatus;
	}

	public void setFraudCheckStatus(FraudCheckStatus fraudCheckStatus) {
		this.fraudCheckStatus = fraudCheckStatus;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
}
