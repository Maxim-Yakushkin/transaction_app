package com.yakushkin.transaction_app.service.unit;

import com.yakushkin.transaction_app.annotation.UT;
import com.yakushkin.transaction_app.dto.AccountInfoDto;
import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Currency;
import com.yakushkin.transaction_app.exception.AccountBalanceLimitException;
import com.yakushkin.transaction_app.exception.CurrencyAccountExistException;
import com.yakushkin.transaction_app.exception.TransactionLimitException;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.repository.AccountRepository;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.sql.AccountSqlQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_RICK_BYN;
import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_RICK_USD;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_RICK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@UT
class TestAccountService {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    @Test
    void create() throws CurrencyAccountExistException {
        // Given
        doReturn(Collections.emptyList()).when(jdbcTemplate).query(
                eq(AccountSqlQuery.FIND_BY_USER_ID_AND_CURRENCY),
                any(RowMapper.class),
                anyInt(), anyString());
        doReturn(ACCOUNT_RICK_BYN).when(accountRepository).save(ACCOUNT_RICK_BYN);

        // Actions
        final Account createdAccount = accountService.create(ACCOUNT_RICK_BYN);

        // Expectations
        assertAll(
                () -> assertThat(createdAccount).isNotNull(),
                () -> assertThat(createdAccount.getUser()).isEqualTo(USER_RICK),
                () -> assertThat(createdAccount.getCurrency()).isEqualTo(Currency.BYN.name()),
                () -> assertThat(createdAccount.getBalance()).isEqualTo(ACCOUNT_RICK_BYN.getBalance())
        );
    }

    @Test
    void createNewAccountIfAccountWithSelectedCurrencyAlreadyExistForSelectedUser() {
        // Given
        doReturn(Collections.singletonList(new AccountInfoDto())).when(jdbcTemplate).query(
                anyString(),
                any(RowMapper.class),
                any(Integer.class), any(String.class));


        // Expectations
        final CurrencyAccountExistException exception = assertThrows(
                CurrencyAccountExistException.class,
                () -> accountService.create(ACCOUNT_RICK_BYN));
        assertAll(
                () -> assertThat(exception).isNotNull(),
                () -> assertInstanceOf(CurrencyAccountExistException.class, exception),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.CURRENCY_ACCOUNT_EXIST_MESSAGE)
        );
    }

    @Test
    void update() {
        // Given
        doReturn(Optional.of(ACCOUNT_RICK_BYN)).when(accountRepository).findById(ACCOUNT_RICK_BYN.getId());
        doReturn(ACCOUNT_RICK_BYN).when(accountRepository).saveAndFlush(ACCOUNT_RICK_BYN);

        // Actions
        final Optional<Account> updatedAccount = accountService.update(ACCOUNT_RICK_BYN);

        // Expectations
        assertAll(
                () -> assertThat(updatedAccount.isPresent()).isTrue(),
                () -> assertThat(updatedAccount.get().getId()).isEqualTo(ACCOUNT_RICK_BYN.getId()),
                () -> assertThat(updatedAccount.get().getUser()).isEqualTo(USER_RICK),
                () -> assertThat(updatedAccount.get().getBalance()).isEqualTo(ACCOUNT_RICK_BYN.getBalance()),
                () -> assertThat(updatedAccount.get().getCurrency()).isEqualTo(Currency.BYN.name())
        );
    }

    @Test
    void findAllAccountsByUserId() {
        // Given
        List<Account> accountsInDb = Arrays.asList(ACCOUNT_RICK_BYN, ACCOUNT_RICK_USD);
        doReturn(accountsInDb).when(jdbcTemplate).query(
                eq(AccountSqlQuery.FIND_BY_USER_ID),
                any(RowMapper.class),
                anyInt());

        // Actions
        final List<AccountInfoDto> allAccountsByUserId = accountService.findAllAccountsByUserId(USER_RICK.getId());

        // Expectations
        assertAll(
                () -> assertArrayEquals(accountsInDb.toArray(), allAccountsByUserId.toArray())
        );
    }

    @ParameterizedTest
    @CsvSource({"1000,+,100,1100", "1000,-,100,900"})
    void updateBalance(Integer initAccountBalance, String transactionTypeOperator, Integer transactionAmount,
                       Integer expectedBalance) throws AccountBalanceLimitException, TransactionLimitException {
        // Given
        ACCOUNT_RICK_BYN.setBalance(initAccountBalance);
        doReturn(Optional.of(ACCOUNT_RICK_BYN)).when(accountRepository).findById(ACCOUNT_RICK_BYN.getId());
        doReturn(ACCOUNT_RICK_BYN).when(accountRepository).saveAndFlush(ACCOUNT_RICK_BYN);

        // Actions
        final Account updatedAccount = accountService.updateBalance(ACCOUNT_RICK_BYN.getId(), transactionAmount,
                transactionTypeOperator);

        // Expectations
        assertAll(
                () -> assertThat(updatedAccount.getId()).isEqualTo(ACCOUNT_RICK_BYN.getId()),
                () -> assertThat(updatedAccount.getUser()).isEqualTo(USER_RICK),
                () -> assertThat(updatedAccount.getBalance()).isEqualTo(expectedBalance)
        );
    }


    @ParameterizedTest
    @ValueSource(strings = {"+", "-"})
    void updateBalanceWhenTransactionLimitExceeded(String transactionTypeOperator) {
        // Given
        final Integer exceededTransactionValue = 100000001;

        // Expectations
        final TransactionLimitException exception = assertThrows(TransactionLimitException.class,
                () -> accountService.updateBalance(ACCOUNT_RICK_BYN.getId(), exceededTransactionValue,
                        transactionTypeOperator));
        assertAll(
                () -> assertThat(exception).isInstanceOf(TransactionLimitException.class),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.TRANSACTION_LIMIT_MESSAGE)
        );
    }

    @ParameterizedTest
    @CsvSource({"1,-,2", "2000000000,+,1"})
    void updateBalanceWhenBalanceLimitReached(Integer initAccountBalance, String transactionTypeOperator,
                                              Integer transactionAmount) {
        // Given
        ACCOUNT_RICK_BYN.setBalance(initAccountBalance);
        doReturn(Optional.of(ACCOUNT_RICK_BYN)).when(accountRepository).findById(ACCOUNT_RICK_BYN.getId());

        // Expectations
        final AccountBalanceLimitException exception = assertThrows(AccountBalanceLimitException.class,
                () -> accountService.updateBalance(ACCOUNT_RICK_BYN.getId(), transactionAmount, transactionTypeOperator));
        assertAll(
                () -> assertThat(exception).isInstanceOf(AccountBalanceLimitException.class),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.ACCOUNT_BALANCE_LIMIT_MESSAGE)
        );
    }
}