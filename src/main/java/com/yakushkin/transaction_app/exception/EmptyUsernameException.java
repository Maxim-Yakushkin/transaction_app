package com.yakushkin.transaction_app.exception;

import com.yakushkin.transaction_app.helper.MessageHelper;

public class EmptyUsernameException extends Exception {

    public EmptyUsernameException() {
        super(MessageHelper.USERNAME_SHOULD_PRESENTED_MESSAGE);
    }
}
