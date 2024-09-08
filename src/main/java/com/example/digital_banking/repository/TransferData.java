package com.example.digital_banking.repository;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transfer")
public class TransferData {
    private String fromAccount;
    private String toAccount;
    private Double amount;

    public TransferData(String fromAccount, String toAccount, Double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public Double getAmount() {
        return amount;
    }
}
