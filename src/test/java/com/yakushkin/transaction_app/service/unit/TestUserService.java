package com.yakushkin.transaction_app.service.unit;

import com.yakushkin.transaction_app.annotation.UT;
import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.repository.UserRepository;
import com.yakushkin.transaction_app.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_MORTY;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_RICK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@UT
class TestUserService {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void create() throws EmptyUsernameException {
        // Given
        doReturn(USER_RICK).when(userRepository).save(USER_RICK);

        // Actions
        final User createdUser = userService.create(USER_RICK);

        // Expectations
        assertNotNull(createdUser);
        assertEquals(USER_RICK.getName(), createdUser.getName());
        assertEquals(USER_RICK.getAddress(), createdUser.getAddress());
        assertEquals(USER_RICK.getId(), createdUser.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createUserWithoutName(String name) {
        // Given
        final User userWithEmptyName = User.builder()
                .id(1)
                .name(name)
                .build();

        // Expectations
        final EmptyUsernameException exception = assertThrows(
                EmptyUsernameException.class,
                () -> userService.create(userWithEmptyName)
        );
        assertEquals("com.yakushkin.transaction_app.exception.EmptyUsernameException", exception.getClass().getName());
    }

    @Test
    void findById() {
        // Given
        doReturn(Optional.of(USER_RICK)).when(userRepository).findById(USER_RICK.getId());

        // Actions
        final User foundedUser = userService.findById(USER_RICK.getId());

        // Expectations
        assertNotNull(foundedUser);
        assertEquals(USER_RICK.getName(), foundedUser.getName());
        assertEquals(USER_RICK.getAddress(), foundedUser.getAddress());
        assertEquals(USER_RICK.getId(), foundedUser.getId());
    }

    // TODO: 12.04.2023
    @Test
    void findAll() {
        // Given
        final List<User> userList = Arrays.asList(USER_RICK, USER_MORTY);
        doReturn(userList).when(userRepository).findAll();

        // Actions
        final List<User> foundedUsers = userService.findAll();

        // Expectations
        assertThat(foundedUsers, Matchers.containsInAnyOrder(userList.toArray()));
    }
}