package com.example.digital_banking.exeption;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
