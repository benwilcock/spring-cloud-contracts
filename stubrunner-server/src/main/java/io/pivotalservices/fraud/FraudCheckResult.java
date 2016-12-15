package io.pivotalservices.fraud;

public class FraudCheckResult {

	private FraudCheckStatus fraudCheckStatus;

	private String resultText;

	public FraudCheckResult() {
	}

	public FraudCheckResult(FraudCheckStatus fraudCheckStatus, String resultText) {
		this.fraudCheckStatus = fraudCheckStatus;
		this.resultText = resultText;
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
