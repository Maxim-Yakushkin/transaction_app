package com.yakushkin.transaction_app;

import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.TransactionService;
import com.yakushkin.transaction_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TransactionApplication implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("----- All Users -----");
        userService.findAll().forEach(System.out::println);

        System.out.println("----- All Accounts -----");
        accountService.findAll().forEach(System.out::println);

        System.out.println("----- All Transactions -----");
        transactionService.findAll().forEach(System.out::println);
    }
}
