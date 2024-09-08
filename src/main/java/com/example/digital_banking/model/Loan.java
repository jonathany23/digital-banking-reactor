package com.example.digital_banking.model;

public class Loan {
    private String loanId;
    private Double balance;
    private Double interestRate;

    public Loan(String loanId, Double balance, Double interestRate) {
        this.loanId = loanId;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }
}
