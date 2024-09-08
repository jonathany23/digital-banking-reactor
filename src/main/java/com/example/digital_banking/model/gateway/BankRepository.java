package com.example.digital_banking.model.gateway;

import com.example.digital_banking.model.CreateAccountRequest;
import com.example.digital_banking.model.CustomerProfile;
import com.example.digital_banking.model.Loan;
import com.example.digital_banking.model.TransferRequest;
import com.example.digital_banking.model.UpdateAccountRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankRepository {

    Mono<String> createAccount(CreateAccountRequest account);
    Mono<CreateAccountRequest> getAccount(String accountId);
    Mono<String> updateAccount(UpdateAccountRequest account);
    Mono<String> closeAccount(String accountId);
    Mono<String> transferMoney(TransferRequest transfer);
    Mono<Double> getBalance(String accountId);
    Mono<CustomerProfile> getCustomerProfile(String accountId);
    Mono<String> createProfile(CustomerProfile profile, String accountId);
    Mono<String> createLoan(Loan loan, String customerId);
    Flux<Loan> getActiveLoans(String customerId);
    Mono<Loan> getLoan(String loanId);
}
