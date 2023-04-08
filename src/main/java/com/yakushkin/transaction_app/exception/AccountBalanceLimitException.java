package com.yakushkin.transaction_app.exception;

public class AccountBalanceLimitException extends Exception {

    public AccountBalanceLimitException() {
        super("The account balance cannot be negative or more than 2000000000");
    }
}
