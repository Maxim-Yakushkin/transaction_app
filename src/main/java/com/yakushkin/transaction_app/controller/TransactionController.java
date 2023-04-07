package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.entity.TransactionType;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.TransactionService;
import com.yakushkin.transaction_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final Scanner scanner = new Scanner(System.in);

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;

    public void createTransaction() {
        System.out.println("===== Create Transaction =====");

        final Integer selectedUserId = getSelectedUserId();
        final int selectedAccountId = selectUserAccount(selectedUserId);
        final TransactionType selectedTransactionType = getSelectedTransactionType();

        executeTransaction(selectedAccountId, selectedTransactionType);
    }

    private void executeTransaction(int selectedAccountId, TransactionType selectedTransactionType) {
        switch (selectedTransactionType) {
            case INCOME -> {
                final Integer amount = inputTransactionAmount();
                final Account updatedAccount = accountService.addIncome(selectedAccountId, amount);
                final Transaction transaction = Transaction.builder()
                        .account(updatedAccount)
                        .amount(amount)
                        .build();
                transactionService.save(transaction);
            }
            case EXPENSE -> {
                final Integer amount = inputTransactionAmount();
                final Account updatedAccount = accountService.addExpense(selectedAccountId, amount);
                final Transaction transaction = Transaction.builder()
                        .account(updatedAccount)
                        .amount(-amount)
                        .build();
                transactionService.save(transaction);
            }
        }
    }

    private Integer inputTransactionAmount() {
        System.out.print("Amount: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private TransactionType getSelectedTransactionType() {
        System.out.println("Please select transaction type:");
        TransactionType[] transactionTypes = TransactionType.values();
        Arrays.stream(transactionTypes).
                map(transactionType -> String.format("%d. %s", transactionType.ordinal(), transactionType.name()))
                .forEach(System.out::println);

        return transactionTypes[Integer.parseInt(scanner.nextLine())];
    }

    private int selectUserAccount(Integer selectedUserId) {
        System.out.println("Please select User Account ID for transaction:");
        accountService.findAllAccountsByUserId(selectedUserId).stream()
                .map(account ->
                        String.format("%d. %s (%s)", account.getId(), account.getBalance(), account.getCurrency()))
                .forEach(System.out::println);

        return Integer.parseInt(scanner.nextLine());
    }

    private Integer getSelectedUserId() {
        System.out.println("Please select User ID:");
        userService.findAll().stream()
                .map(user -> String.format("%d. %s", user.getId(), user.getName()))
                .forEach(System.out::println);

        return Integer.parseInt(scanner.nextLine());
    }
}
