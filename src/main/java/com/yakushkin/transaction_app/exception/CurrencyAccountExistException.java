package com.yakushkin.transaction_app.exception;

import com.yakushkin.transaction_app.helper.MessageHelper;

public class CurrencyAccountExistException extends Exception {

    public CurrencyAccountExistException() {
        super(MessageHelper.CURRENCY_ACCOUNT_EXIST_MESSAGE);
    }
}
