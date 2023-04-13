package com.yakushkin.transaction_app.service.integration;

import com.yakushkin.transaction_app.annotation.IT;
import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_MORTY;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_RICK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IT
class ITUserService {

    @Autowired
    private UserService userService;

    @BeforeEach
    void initDatabaseData() throws EmptyUsernameException {
        userService.create(USER_RICK);
        userService.create(USER_MORTY);
    }

    @Test
    void create() throws EmptyUsernameException {
        // Actions
        final User createdUser = userService.create(USER_RICK);

        // Expectations
        assertAll(
                () -> assertThat(createdUser).isNotNull(),
                () -> assertThat(createdUser.getName()).isEqualTo(USER_RICK.getName()),
                () -> assertThat(createdUser.getAddress()).isEqualTo(USER_RICK.getAddress())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createUserWithIncorrectName(String name) {
        // Given
        final User userWithIncorrectName = User.builder()
                .id(1)
                .name(name)
                .build();

        // Expectations
        final EmptyUsernameException exception = assertThrows(
                EmptyUsernameException.class,
                () -> userService.create(userWithIncorrectName)
        );
        assertAll(
                () -> assertThat(exception).isInstanceOf(EmptyUsernameException.class),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.USERNAME_SHOULD_PRESENTED_MESSAGE)
        );
    }

    @Test
    void findUserById() {
        // Actions
        int userId = 1;
        final User userFromDb = userService.findById(userId);

        // Expectations
        assertAll(
                () -> assertThat(userFromDb).isNotNull(),
                () -> assertThat(userFromDb.getId()).isEqualTo(userId)
        );
    }

    @Test
    void findAllUsers() {
        // Given
        final int expectedCountOfUsers = 2;

        // Actions
        final List<User> usersFromDb = userService.findAll();

        // Expectations
        assertThat(usersFromDb).hasSize(expectedCountOfUsers);
    }
}