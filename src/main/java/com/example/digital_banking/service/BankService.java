package com.example.digital_banking.service;

import com.example.digital_banking.model.CreateAccountRequest;
import com.example.digital_banking.model.CustomerProfile;
import com.example.digital_banking.model.Loan;
import com.example.digital_banking.model.Transaction;
import com.example.digital_banking.model.TransferRequest;
import com.example.digital_banking.model.UpdateAccountRequest;
import com.example.digital_banking.model.gateway.BankRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Arrays;
import java.util.List;

@Service
public class BankService {
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private BankRepository repository;

    public BankService(BankRepository repository) {
        this.repository = repository;
    }

    public Mono<Double> getBalance(String accountId) {
        // Caso de uso: Consultar el saldo actual de una cuenta bancaria. Sino hay balance se debe tener un valor de 0.0
        //return Mono.empty(); // Implementar la lógica de consulta aquí
        return repository.getBalance(accountId);
    }

    public Mono<String> transferMoney(TransferRequest request) {
        // Caso de uso: Transferir dinero de una cuenta a otra. Hacer llamado de otro flujo simulando el llamado
        //return Mono.empty(); // Implementar la lógica de consulta aquí  ???????
        return repository.transferMoney(request);
    }

    public Flux<Transaction> getTransactions(String accountId) {
        // Caso de uso: Consultar el historial de transacciones de una cuenta bancaria.
        List<Transaction> transactions = Arrays.asList(
                new Transaction("1", accountId, 200.00),
                new Transaction("2", accountId, -150.00),
                new Transaction("3", accountId, 300.00)
        );
        //return Flux.empty(); // Implementar la lógica de consulta aquí
        return Flux.fromIterable(transactions);
    }

    public Mono<String> createAccount(CreateAccountRequest request) {
        // Caso de uso: Crear una nueva cuenta bancaria con un saldo inicial.
        return repository.createAccount(request); // Implementar la lógica de consulta aquí
    }

    public Mono<String> closeAccount(String accountId) {
        // Caso de uso: Cerrar una cuenta bancaria especificada. Verificar que la ceunta exista y si no existe debe retornar un error controlado
        //return Mono.empty(); // Implementar la lógica de consulta aquí
        return repository.getAccount(accountId)
                .flatMap(account -> repository.closeAccount(accountId))
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }

    public Mono<String> updateAccount(UpdateAccountRequest request) {
        // Caso de uso: Actualizar la información de una cuenta bancaria especificada. Verificar que la ceunta exista y si no existe debe retornar un error controlado
        return repository.updateAccount(request); // Implementar la lógica de consulta aquí
    }

    public Mono<CustomerProfile> getCustomerProfile(String accountId) {
        // Caso de uso: Consultar el perfil del cliente que posee la cuenta bancaria. Obtener los valores por cada uno de los flujos y si no existe alguno debe presentar un error
//        Mono<String> customerIdMono = Mono.just("12345");
//        Mono<String> nameMono = Mono.just("John Doe");
//        Mono<String> emailMono = Mono.just("john.doe@example.com");
        //return Mono.empty(); // Implementar la lógica de consulta aquí
        return repository.getCustomerProfile(accountId);
    }

    public Flux<Loan> getActiveLoans(String customerId) {
        // Caso de uso: Consultar todos los préstamos activos asociados al cliente especificado.
//        List<Loan> loans = Arrays.asList(
//                new Loan("loan1", 5000.00, 0.05),
//                new Loan("loan2", 10000.00, 0.04)
//        );
//        return Flux.empty(); // Implementar la lógica de consulta aquí
        return repository.getActiveLoans(customerId);
    }

    public Flux<Double> simulateInterest(String accountId) {
        //double principal = 1000.00;
        //double rate = 0.05;

        // Caso de uso: Simular el interés compuesto en una cuenta bancaria. Sacar un rago de 10 años y aplicar la siguiente formula = principal * Math.pow(1 + rate, year)
        //return Flux.empty(); // Implementar la lógica de consulta aquí
        return repository.getAccount(accountId)
                .flatMapMany(account -> Flux.range(1, 10)
                        .map(year -> account.getInitialBalance() * Math.pow(1 + account.getRate(), year)));
    }

    public Mono<String> getLoanStatus(String loanId) {
        // Caso de uso: Consultar el estado de un préstamo. se debe tener un flujo balanceMono y interestRateMono. Imprimir con el formato siguiente el resultado   "Loan ID: %s, Balance: %.2f, Interest Rate: %.2f%%"
        //return Mono.empty(); // Implementar la lógica de consulta aquí
        return repository.getLoan(loanId)
                .flatMap(account ->
                        Mono.just(
                                String.format("Loan ID: %s, Balance: %.2f, Interest Rate: %.2f%%",
                                        account.getLoanId(),
                                        account.getBalance(),
                                        account.getInterestRate())));
    }

    public Mono<String> createProfile(CustomerProfile request, String customerId) {
        return repository.createProfile(request, customerId);
    }

    public Mono<String> createLoan(Loan loan, String customerId) {
        return repository.createLoan(loan, customerId);
    }
}
