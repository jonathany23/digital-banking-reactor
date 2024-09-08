package com.example.digital_banking.repository;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account")
public class AccountData {
    private String accountId;
    private Double balance;
    private String data;
    private Double rate;

    public AccountData(String accountId, Double balance, String data, Double rate) {
        this.accountId = accountId;
        this.balance = balance;
        this.data = data;
        this.rate = rate;
    }

    public String getAccountId() {
        return accountId;
    }

    public Double getBalance() {
        return balance;
    }

    public String getData() {
        return data;
    }

    public Double getRate() {
        return rate;
    }
}
