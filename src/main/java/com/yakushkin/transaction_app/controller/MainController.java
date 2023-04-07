package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.helper.CommandHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class MainController {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String ADD_USER_COMMAND = CommandHelper.ADD_USER;
    private static final String ADD_ACCOUNT_COMMAND = CommandHelper.ADD_ACCOUNT;
    private static final String ADD_TRANSACTION_COMMAND = CommandHelper.ADD_TRANSACTION;

    private final UserController userController;
    private final AccountController accountController;
    private final TransactionController transactionController;

    public void run() throws EmptyUsernameException {
        printCommand();

        String consoleCommand = SCANNER.nextLine();
        switch (consoleCommand) {
            case ADD_USER_COMMAND -> userController.registration();
            case ADD_ACCOUNT_COMMAND -> accountController.createAccount();
            case ADD_TRANSACTION_COMMAND -> transactionController.createTransaction();
        }
    }

    private void printCommand() {
        Map<String, String> commandMap = Map.of(
                ADD_USER_COMMAND, "create user",
                ADD_ACCOUNT_COMMAND, "create account",
                ADD_TRANSACTION_COMMAND, "create transaction");

        commandMap.entrySet().stream()
                .map(entry -> String.format("%s - %s", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);
    }
}
