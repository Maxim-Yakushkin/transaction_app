package com.yakushkin.transaction_app.exception;

public class TransactionLimitException extends Exception {

    public TransactionLimitException() {
        super("The limit for one transaction is 100,000,000");
    }
}
