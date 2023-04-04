package com.yakushkin.transaction_app;

import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TransactionApplication implements CommandLineRunner {

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        List<User> all = userService.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }
}
