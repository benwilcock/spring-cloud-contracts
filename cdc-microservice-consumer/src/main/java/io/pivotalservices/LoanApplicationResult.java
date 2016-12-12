package io.pivotalservices;

public class LoanApplicationResult {

	private LoanApplicationStatus loanApplicationStatus;

	private String resultText;

	public LoanApplicationResult() {
	}

	public LoanApplicationResult(LoanApplicationStatus loanApplicationStatus, String resultText) {
		this.loanApplicationStatus = loanApplicationStatus;
		this.resultText = resultText;
	}

	public LoanApplicationStatus getLoanApplicationStatus() {
		return loanApplicationStatus;
	}

	public void setLoanApplicationStatus(LoanApplicationStatus loanApplicationStatus) {
		this.loanApplicationStatus = loanApplicationStatus;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
}
