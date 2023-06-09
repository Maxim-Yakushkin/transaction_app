package com.yakushkin.transaction_app;

import com.yakushkin.transaction_app.controller.MainController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TransactionApplication implements CommandLineRunner {

    private final MainController mainController;

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Need to comment out before running Integration Test (IT) tests
//        mainController.run();
    }
}
