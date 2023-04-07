package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Currency;
import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final Scanner scanner = new Scanner(System.in);

    private final UserService userService;
    private final AccountService accountService;

    public void createAccount() {
        System.out.println("===== Create Account =====");

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
        System.out.print("Initial balance: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private String selectAccountCurrency() {
        System.out.println("Please select the currency for this Account:");
        Currency[] currencies = Currency.values();
        Arrays.stream(currencies)
                .map(currency -> String.format("%d. %s", currency.ordinal(), currency.name()))
                .forEach(System.out::println);

        return currencies[Integer.parseInt(scanner.nextLine())].name();
    }

    private User selectUserFromDb() {
        System.out.println("Please select user ID for whom an account will be created:");
        userService.findAll().stream()
                .map(user -> String.format("%d. %s", user.getId(), user.getName()))
                .forEach(System.out::println);
        final Integer selectedUserId = Integer.parseInt(scanner.nextLine());

        return userService.findById(selectedUserId);
    }
}
