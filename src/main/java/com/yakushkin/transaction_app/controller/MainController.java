package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.helper.CommandHelper;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class MainController {

    private static final Scanner SCANNER = Util.SCANNER;

    private final UserController userController;
    private final AccountController accountController;
    private final TransactionController transactionController;

    public void run() {
        printAllCommands();

        final String consoleCommand = SCANNER.nextLine();
        final CommandHelper typedCommand = CommandHelper.findByCommand(consoleCommand);
        switch (typedCommand) {
            case ADD_USER -> userController.registration();
            case ADD_ACCOUNT -> accountController.createAccount();
            case ADD_TRANSACTION -> transactionController.createTransaction();
        }
    }

    private void printAllCommands() {
        System.out.println(MessageHelper.AVAILABLE_COMMAND_HEADER);
        Arrays.stream(CommandHelper.values())
                .map(commandHelper -> String.format("%s\t%s",
                        commandHelper.getCommand(),
                        commandHelper.getDescription()))
                .forEach(System.out::println);
    }
}
