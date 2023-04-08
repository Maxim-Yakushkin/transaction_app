package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Currency;
import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.UserService;
import com.yakushkin.transaction_app.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private static final Scanner SCANNER = Util.SCANNER;

    private final UserService userService;
    private final AccountService accountService;

    public void createAccount() {
        System.out.println(MessageHelper.CREATE_ACCOUNT_HEADER);

        final User selectedUser = selectUserFromDb();
        final String selectedCurrency = selectAccountCurrency();
        int initialBalanceValue = initAccountBalance();

        final Account account = Account.builder()
                .user(selectedUser)
                .currency(selectedCurrency)
                .balance(initialBalanceValue)
                .build();

        accountService.create(account);
    }

    private int initAccountBalance() {
        System.out.print(MessageHelper.INIT_BALANCE_MESSAGE);
        return Integer.parseInt(SCANNER.nextLine());
    }

    private String selectAccountCurrency() {
        System.out.println(MessageHelper.SELECT_CURRENCY_MESSAGE);
        Currency[] currencies = Currency.values();
        Arrays.stream(currencies)
                .map(currency -> String.format("%d. %s", currency.ordinal(), currency.name()))
                .forEach(System.out::println);

        return currencies[Integer.parseInt(SCANNER.nextLine())].name();
    }

    private User selectUserFromDb() {
        System.out.println(MessageHelper.SELECT_USER_ID_FOR_CREATE_ACCOUNT_MESSAGE);
        userService.findAll().stream()
                .map(user -> String.format("%d. %s", user.getId(), user.getName()))
                .forEach(System.out::println);
        final Integer selectedUserId = Integer.parseInt(SCANNER.nextLine());

        return userService.findById(selectedUserId);
    }
}
