package com.codedifferently.tankofamerica.domain.transaction.exceptions;

public class OverdraftException extends Exception {
    public OverdraftException(String message) {
        super(message);
    }
}
