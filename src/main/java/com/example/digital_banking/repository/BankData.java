package com.example.digital_banking.repository;


import com.example.digital_banking.exeption.BusinessException;
import com.example.digital_banking.model.CreateAccountRequest;
import com.example.digital_banking.model.CustomerProfile;
import com.example.digital_banking.model.Loan;
import com.example.digital_banking.model.TransferRequest;
import com.example.digital_banking.model.UpdateAccountRequest;
import com.example.digital_banking.model.gateway.BankRepository;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BankData implements BankRepository {

    private final ReactiveMongoTemplate mongodbTemplate;

    public BankData(ReactiveMongoTemplate mongodbTemplate) {
        this.mongodbTemplate = mongodbTemplate;
    }

    @Override
    @Transactional
    public Mono<String> createAccount(CreateAccountRequest account) {
        AccountData accountData = new AccountData(account.getAccountId(), account.getInitialBalance(), null, null);

        return mongodbTemplate.insert(accountData)
                .then(Mono.just("Account created successfully"))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error creating account"));
    }

    @Override
    public Mono<CreateAccountRequest> getAccount(String accountId) {
        return mongodbTemplate.findOne(Query.query(Criteria.where("accountId").is(accountId)), AccountData.class)
                .map(this::mapToCreateAccountRequest)
                .switchIfEmpty(Mono.error(new BusinessException("Account not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error getting account"));
    }

    @Override
    public Mono<String> updateAccount(UpdateAccountRequest account) {
        Query query = Query.query(Criteria.where("accountId").is(account.getAccountId()));
        Update update = new Update().set("data", account.getNewData()).set("rate", account.getRate());

        return mongodbTemplate.findAndModify(query, update, AccountData.class)
                .map(accountData -> "Account updated successfully")
                .switchIfEmpty(Mono.error(new BusinessException("Account not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error updating account"));
    }

    @Override
    public Mono<String> closeAccount(String accountId) {
        return mongodbTemplate.remove(Query.query(Criteria.where("accountId").is(accountId)), AccountData.class)
                .then(Mono.just("Account closed successfully"))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error closing account"));
    }

    @Override
    public Mono<String> transferMoney(TransferRequest transfer) {
        TransferData transferData = new TransferData(
                transfer.getFromAccount(),
                transfer.getToAccount(),
                transfer.getAmount());

        return mongodbTemplate.insert(transferData)
                .then(Mono.just("Transfer completed successfully"))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error transferring money"));
    }

    @Override
    public Mono<Double> getBalance(String accountId) {
        return mongodbTemplate.findOne(Query.query(Criteria.where("accountId").is(accountId)), AccountData.class)
                .map(AccountData::getBalance)
                .switchIfEmpty(Mono.just(0.0))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error getting balance"));
    }

    @Override
    public Mono<CustomerProfile> getCustomerProfile(String accountId) {
        return mongodbTemplate.findOne(Query.query(Criteria.where("accountId").is(accountId)), CustomerProfileData.class)
                .map(accountData -> new CustomerProfile(accountData.getCustomerId(), accountData.getName(), accountData.getEmail()))
                .switchIfEmpty(Mono.error(new BusinessException("Account not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error getting customer profile"));
    }

    @Override
    public Mono<String> createProfile(CustomerProfile profile, String accountId) {
        return getAccount(accountId)
                .flatMap(account -> {
                    CustomerProfileData customerProfileData = new CustomerProfileData(
                            profile.getCustomerId(),
                            profile.getName(),
                            profile.getEmail(),
                            accountId);

                    return mongodbTemplate.insert(customerProfileData)
                            .then(Mono.just("Profile created successfully"));
                })
                .switchIfEmpty(Mono.error(new BusinessException("Account not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error creating profile"));
    }

    @Override
    public Mono<String> createLoan(Loan loan, String customerId) {
        return mongodbTemplate.findOne(Query.query(Criteria.where("customerId").is(customerId)), CustomerProfileData.class)
                .flatMap(customerProfileData -> {
                    LoanData loanData = new LoanData(
                            loan.getLoanId(),
                            loan.getBalance(),
                            loan.getInterestRate(),
                            customerId);

                    return mongodbTemplate.insert(loanData)
                            .then(Mono.just("Loan created successfully"));
                })
                .switchIfEmpty(Mono.error(new BusinessException("Customer not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error creating loan"));
    }

    @Override
    public Flux<Loan> getActiveLoans(String customerId) {
        return mongodbTemplate.findOne(Query.query(Criteria.where("customerId").is(customerId)), CustomerProfileData.class)
                .flatMapMany(customerProfileData -> mongodbTemplate.find(Query.query(Criteria.where("customerId").is(customerId)), LoanData.class)
                        .map(loanData -> new Loan(loanData.getLoanId(), loanData.getBalance(), loanData.getInterestRate())))
                .switchIfEmpty(Flux.error(new BusinessException("Customer not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error getting active loans"));
    }

    @Override
    public Mono<Loan> getLoan(String loanId) {
        return mongodbTemplate.findOne(Query.query(Criteria.where("loanId").is(loanId)), LoanData.class)
                .map(loanData -> new Loan(loanData.getLoanId(), loanData.getBalance(), loanData.getInterestRate()))
                .switchIfEmpty(Mono.error(new BusinessException("Loan not found")))
                .onErrorMap(DataAccessResourceFailureException.class, e -> new BusinessException("Error getting loan"));
    }

    private CreateAccountRequest mapToCreateAccountRequest(AccountData accountData) {
        return new CreateAccountRequest(accountData.getAccountId(), accountData.getBalance(), accountData.getData(), accountData.getRate());
    }


}
