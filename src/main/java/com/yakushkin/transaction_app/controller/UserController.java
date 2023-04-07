package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;

    public void registration() {
        System.out.println("===== User Registration =====");

        System.out.print("User name: ");
        final String userName = scanner.nextLine();
        System.out.print("User address: ");
        final String userAddress = scanner.nextLine();

        final User user = User.builder()
                .name(userName)
                .address(userAddress)
                .build();

        userService.create(user);
    }
}
