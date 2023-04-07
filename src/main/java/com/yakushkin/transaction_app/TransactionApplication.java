package com.yakushkin.transaction_app;

import com.yakushkin.transaction_app.controller.TransactionController;
import com.yakushkin.transaction_app.controller.UserController;
import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TransactionApplication implements CommandLineRunner {

    private final AccountService accountService;
    private final UserService userService;
    private final TransactionController transactionController;
    private final UserController userController;

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            userController.registration();
        } catch (EmptyUsernameException e) {
            throw new RuntimeException(e);
        }
    }
}
