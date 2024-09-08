package com.example.digital_banking.model;

public class CreateAccountRequest {
    private String accountId;
    private Double initialBalance;
    private String data;
    private Double rate;

    public CreateAccountRequest(String accountId, Double initialBalance, String data, Double rate) {
        this.accountId = accountId;
        this.initialBalance = initialBalance;
        this.data = data;
        this.rate = rate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
