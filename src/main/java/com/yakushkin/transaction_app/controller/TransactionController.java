package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.entity.TransactionType;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.TransactionService;
import com.yakushkin.transaction_app.service.UserService;
import com.yakushkin.transaction_app.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private static final String PLUS = "+";
    private static final String MINUS = "-";

    private static final Scanner SCANNER = Util.SCANNER;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;

    public void createTransaction() {
        System.out.println(MessageHelper.CREATE_TRANSACTION_HEADER);

        final Integer selectedUserId = getSelectedUserId();
        final int selectedAccountId = selectUserAccount(selectedUserId);
        final TransactionType selectedTransactionType = getSelectedTransactionType();

        executeTransaction(selectedAccountId, selectedTransactionType);
    }

    private void executeTransaction(int selectedAccountId, TransactionType selectedTransactionType) {
        switch (selectedTransactionType) {
            case INCOME -> {
                final Integer amount = inputTransactionAmount();
                final Account updatedAccount = accountService.updateBalance(selectedAccountId, amount, PLUS);
                final Transaction transaction = buildTransaction(amount, updatedAccount);
                final Transaction savedTransaction = transactionService.save(transaction);
                System.out.println(MessageHelper.TRANSACTION_SAVED_MESSAGE + savedTransaction);
            }
            case EXPENSE -> {
                final Integer amount = inputTransactionAmount();
                final Account updatedAccount = accountService.updateBalance(selectedAccountId, amount, MINUS);
                final Transaction transaction = buildTransaction(-amount, updatedAccount);
                final Transaction savedTransaction = transactionService.save(transaction);
                System.out.println(MessageHelper.TRANSACTION_SAVED_MESSAGE + savedTransaction);
            }
        }
    }

    private static Transaction buildTransaction(Integer amount, Account updatedAccount) {
        return Transaction.builder()
                .account(updatedAccount)
                .amount(amount)
                .build();
    }

    private Integer inputTransactionAmount() {
        System.out.print("Amount: ");
        return Integer.parseInt(SCANNER.nextLine());
    }

    private TransactionType getSelectedTransactionType() {
        System.out.println(MessageHelper.SELECT_TRANSACTION_TYPE_MESSAGE);
        final TransactionType[] transactionTypes = TransactionType.values();
        Arrays.stream(transactionTypes).
                map(transactionType -> String.format("%d. %s", transactionType.ordinal(), transactionType.name()))
                .forEach(System.out::println);

        return transactionTypes[Integer.parseInt(SCANNER.nextLine())];
    }

    private int selectUserAccount(Integer selectedUserId) {
        System.out.println(MessageHelper.SELECT_USER_ID_FOR_TRANSACTION_MESSAGE);
        accountService.findAllAccountsByUserId(selectedUserId).stream()
                .map(account ->
                        String.format("%d. %s (%s)", account.getId(), account.getBalance(), account.getCurrency()))
                .forEach(System.out::println);

        return Integer.parseInt(SCANNER.nextLine());
    }

    private Integer getSelectedUserId() {
        System.out.println(MessageHelper.SELECT_USER_ID_MESSAGE);
        userService.findAll().stream()
                .map(user -> String.format("%d. %s", user.getId(), user.getName()))
                .forEach(System.out::println);

        return Integer.parseInt(SCANNER.nextLine());
    }
}
