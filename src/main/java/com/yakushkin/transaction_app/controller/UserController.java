package com.yakushkin.transaction_app.controller;

import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.service.UserService;
import com.yakushkin.transaction_app.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class UserController {

    private static final Scanner SCANNER = Util.SCANNER;
    private final UserService userService;

    public void registration() throws EmptyUsernameException {
        System.out.println(MessageHelper.REGISTRATION_USER_HEADER);

        System.out.print(MessageHelper.INPUT_USERNAME_MESSAGE);
        final String userName = SCANNER.nextLine();
        if (userName.isEmpty() || userName.isBlank()) {
            throw new EmptyUsernameException();
        }
        System.out.print(MessageHelper.INPUT_USER_ADDRESS_MESSAGE);
        final String userAddress = SCANNER.nextLine();

        final User user = User.builder()
                .name(userName)
                .address(userAddress)
                .build();

        userService.create(user);
    }
}
