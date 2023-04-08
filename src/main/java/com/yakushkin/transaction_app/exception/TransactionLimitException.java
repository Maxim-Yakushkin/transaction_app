package com.yakushkin.transaction_app.exception;

import com.yakushkin.transaction_app.helper.MessageHelper;

public class TransactionLimitException extends Exception {

    public TransactionLimitException() {
        super(MessageHelper.TRANSACTION_LIMIT_MESSAGE);
    }
}
