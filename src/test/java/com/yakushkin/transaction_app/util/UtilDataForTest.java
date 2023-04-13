package com.yakushkin.transaction_app.util;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Currency;
import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.entity.User;

public class UtilDataForTest {

    public static final User USER_RICK = User.builder()
            .id(1)
            .name("Rick")
            .address("Universe-1")
            .build();
    public static final User USER_MORTY = User.builder()
            .id(2)
            .name("Morty")
            .address("Universe-2")
            .build();

    public static final Account ACCOUNT_RICK_BYN = Account.builder()
            .id(1)
            .user(USER_RICK)
            .currency(Currency.BYN.name())
            .balance(100)
            .build();

    public static final Account ACCOUNT_RICK_USD = Account.builder()
            .id(2)
            .user(USER_RICK)
            .currency(Currency.USD.name())
            .balance(100)
            .build();

    public static final Account ACCOUNT_MORTY_BYN = Account.builder()
            .id(3)
            .user(USER_MORTY)
            .currency(Currency.BYN.name())
            .balance(100)
            .build();

    public static final Account ACCOUNT_MORTY_USD = Account.builder()
            .id(4)
            .user(USER_MORTY)
            .currency(Currency.USD.name())
            .balance(100)
            .build();

    public static final Transaction TRANSACTION_1 = Transaction.builder()
            .id(1)
            .account(ACCOUNT_RICK_BYN)
            .amount(100)
            .build();

    public static final Transaction TRANSACTION_2 = Transaction.builder()
            .id(1)
            .account(ACCOUNT_RICK_BYN)
            .amount(200)
            .build();
}
