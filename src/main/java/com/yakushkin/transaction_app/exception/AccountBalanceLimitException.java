package com.yakushkin.transaction_app.exception;

import com.yakushkin.transaction_app.helper.MessageHelper;

public class AccountBalanceLimitException extends Exception {

    public AccountBalanceLimitException() {
        super(MessageHelper.ACCOUNT_BALANCE_LIMIT_MESSAGE);
    }
}
