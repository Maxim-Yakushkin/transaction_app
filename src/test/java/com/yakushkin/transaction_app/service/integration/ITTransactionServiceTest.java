package com.yakushkin.transaction_app.service.integration;

import com.yakushkin.transaction_app.annotation.IT;
import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.exception.CurrencyAccountExistException;
import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.exception.TransactionLimitException;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.TransactionService;
import com.yakushkin.transaction_app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_RICK_BYN;
import static com.yakushkin.transaction_app.util.UtilDataForTest.TRANSACTION_1;
import static com.yakushkin.transaction_app.util.UtilDataForTest.TRANSACTION_2;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_RICK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IT
class ITTransactionServiceTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void initDatabaseData() throws TransactionLimitException, CurrencyAccountExistException, EmptyUsernameException {
        userService.create(USER_RICK);
        accountService.create(ACCOUNT_RICK_BYN);
        transactionService.save(TRANSACTION_1);
        transactionService.save(TRANSACTION_2);
    }

    @Test
    void save() throws TransactionLimitException {
        // Given
        final Integer transactionAmount = 123;
        final Transaction transaction = Transaction.builder()
                .account(ACCOUNT_RICK_BYN)
                .amount(transactionAmount)
                .build();

        // Actions
        final Transaction savedTransaction = transactionService.save(transaction);

        // Expectations
        assertAll(
                () -> assertThat(savedTransaction.getAccount()).isEqualTo(ACCOUNT_RICK_BYN),
                () -> assertThat(savedTransaction.getAmount()).isEqualTo(transactionAmount),
                () -> assertThat(savedTransaction.getAccount().getUser()).isEqualTo(USER_RICK)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"100000001", "-100000001"})
    void updateAccountBalanceWithIncorrectTransactionValue(Integer transactionAmount) {
        // Given
        final Transaction transaction = Transaction.builder()
                .amount(transactionAmount)
                .account(ACCOUNT_RICK_BYN)
                .build();

        // Expectations
        final TransactionLimitException exception = assertThrows(TransactionLimitException.class,
                () -> transactionService.save(transaction));
        assertAll(
                () -> assertThat(exception).isInstanceOf(TransactionLimitException.class),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.TRANSACTION_LIMIT_MESSAGE)
        );
    }
}