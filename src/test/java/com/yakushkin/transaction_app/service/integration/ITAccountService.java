package com.yakushkin.transaction_app.service.integration;

import com.yakushkin.transaction_app.annotation.IT;
import com.yakushkin.transaction_app.dto.AccountInfoDto;
import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.entity.Currency;
import com.yakushkin.transaction_app.exception.AccountBalanceLimitException;
import com.yakushkin.transaction_app.exception.CurrencyAccountExistException;
import com.yakushkin.transaction_app.exception.EmptyUsernameException;
import com.yakushkin.transaction_app.exception.TransactionLimitException;
import com.yakushkin.transaction_app.helper.MessageHelper;
import com.yakushkin.transaction_app.repository.AccountRepository;
import com.yakushkin.transaction_app.service.AccountService;
import com.yakushkin.transaction_app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_MORTY_BYN;
import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_MORTY_USD;
import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_RICK_BYN;
import static com.yakushkin.transaction_app.util.UtilDataForTest.ACCOUNT_RICK_USD;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_MORTY;
import static com.yakushkin.transaction_app.util.UtilDataForTest.USER_RICK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IT
class ITAccountService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void initDatabaseData() throws EmptyUsernameException, CurrencyAccountExistException {
        userService.create(USER_RICK);
        userService.create(USER_MORTY);
        accountService.create(ACCOUNT_RICK_USD);
        accountService.create(ACCOUNT_MORTY_USD);
        accountService.create(ACCOUNT_MORTY_BYN);
    }

    @Test
    void create() throws CurrencyAccountExistException {
        // Actions
        final Account createdAccount = accountService.create(ACCOUNT_RICK_BYN);

        // Expectations
        assertAll(
                () -> assertThat(createdAccount).isNotNull(),
                () -> assertThat(createdAccount.getUser()).isEqualTo(USER_RICK),
                () -> assertThat(createdAccount.getCurrency()).isEqualTo(ACCOUNT_RICK_BYN.getCurrency()),
                () -> assertThat(createdAccount.getBalance()).isEqualTo(ACCOUNT_RICK_BYN.getBalance())
        );
    }

    @Test
    void update() {
        // Given
        final Account accountFromDb = accountRepository.findAll().stream().findFirst().get();
        accountFromDb.setUser(USER_RICK);
        accountFromDb.setCurrency(Currency.RUB.name());
        accountFromDb.setBalance(999);

        // Actions
        final Account updatedAccount = accountService.update(accountFromDb).get();

        // Expectations
        assertAll(
                () -> assertThat(updatedAccount.getId()).isEqualTo(accountFromDb.getId()),
                () -> assertThat(updatedAccount.getCurrency()).isEqualTo(Currency.RUB.name()),
                () -> assertThat(updatedAccount.getBalance()).isEqualTo(999),
                () -> assertThat(updatedAccount.getUser()).isEqualTo(USER_RICK)
        );
    }

    @Test
    void findAllAccountsByUserId() {
        // Given
        final Integer expectedMortyCountAccounts = 2;

        // Actions
        final List<AccountInfoDto> mortyAccounts = accountService.findAllAccountsByUserId(USER_MORTY.getId());

        // Expectations
        assertAll(
                () -> assertThat(mortyAccounts.size()).isEqualTo(expectedMortyCountAccounts),
                () -> assertThat(mortyAccounts.stream()
                        .map(AccountInfoDto::getUserId)
                        .allMatch(userId -> userId.equals(USER_MORTY.getId()))).isTrue()
        );
    }

    @ParameterizedTest
    @CsvSource({"+,100,200", "-,100,0"})
    void updateBalance(String transactionTypeOperator, Integer transactionAmount, Integer expectedBalance)
            throws AccountBalanceLimitException, TransactionLimitException {
        // Given
        final Integer accountId = 1;

        // Actions
        final Account accountFromDb = accountRepository.findById(accountId).get();
        final Account updatedAccount = accountService.updateBalance(accountFromDb.getId(), transactionAmount,
                transactionTypeOperator);

        // Expectations
        assertAll(
                () -> assertThat(updatedAccount.getId()).isEqualTo(accountId),
                () -> assertThat(updatedAccount.getBalance()).isEqualTo(expectedBalance)
        );
    }

    @ParameterizedTest
    @CsvSource({"1,2,-", "2000000000,1,+"})
    void updateAccountBalanceWithIncorrectTransactionValue(Integer initAccountBalance, Integer transactionAmount,
                                                           String transactionTypeOperator) {
        // Given
        final Integer accountId = 1;
        final Account accountFromDb = accountRepository.findById(accountId).get();
        accountFromDb.setBalance(initAccountBalance);
        accountService.update(accountFromDb).get();

        // Expectations
        final AccountBalanceLimitException exception = assertThrows(AccountBalanceLimitException.class,
                () -> accountService.updateBalance(accountFromDb.getId(), transactionAmount, transactionTypeOperator));
        assertAll(
                () -> assertThat(exception).isInstanceOf(AccountBalanceLimitException.class),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.ACCOUNT_BALANCE_LIMIT_MESSAGE)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"+", "-"})
    void updateAccountBalanceWithIncorrectTransactionValue(String transactionTypeOperator) {
        // Given
        final Integer accountId = 1;
        final Integer incorrectTransactionValue = 100000001;
        final Account accountFromDb = accountRepository.findById(accountId).get();

        // Expectations
        final TransactionLimitException exception = assertThrows(TransactionLimitException.class,
                () -> accountService.updateBalance(accountFromDb.getId(), incorrectTransactionValue, transactionTypeOperator));
        assertAll(
                () -> assertThat(exception).isInstanceOf(TransactionLimitException.class),
                () -> assertThat(exception.getMessage()).isEqualTo(MessageHelper.TRANSACTION_LIMIT_MESSAGE)
        );
    }
}