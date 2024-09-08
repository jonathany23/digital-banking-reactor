package com.example.digital_banking.repository;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customer_profile")
public class CustomerProfileData {
    private String customerId;
    private String name;
    private String email;
    private String accountId;

    public CustomerProfileData(String customerId, String name, String email, String accountId) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
