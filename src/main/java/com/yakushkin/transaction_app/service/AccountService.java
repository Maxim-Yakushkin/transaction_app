package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.dto.AccountInfoDto;
import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.exception.CurrencyAccountExistException;
import com.yakushkin.transaction_app.repository.AccountRepository;
import com.yakushkin.transaction_app.repository.FilterAccountRepository;
import com.yakushkin.transaction_app.sql.AccountSqlQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements FilterAccountRepository {

    private static final String FIND_BY_USER_ID_SQL = AccountSqlQuery.FIND_BY_USER_ID;
    private static final String FIND_BY_USER_ID_AND_CURRENCY_SQL = AccountSqlQuery.FIND_BY_USER_ID_AND_CURRENCY;
    private static final int TRANSACTION_LIMIT = 100000000;
    private static final int ACCOUNT_BALANCE_MAX = 2000000000;
    private static final int ACCOUNT_BALANCE_MIN = 0;

    private final AccountRepository accountRepository;
    private final JdbcTemplate jdbcTemplate;

    public Account create(Account account) throws CurrencyAccountExistException {
        final List<AccountInfoDto> existUserAccounts = jdbcTemplate.query(FIND_BY_USER_ID_AND_CURRENCY_SQL,
                (rs, rowNum) -> AccountInfoDto.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .balance(rs.getInt("balance"))
                        .currency(rs.getString("currency"))
                        .build(),
                account.getUser().getId(), account.getCurrency());

        if (existUserAccounts.size() == 0) {
            return accountRepository.save(account);
        } else {
            throw new CurrencyAccountExistException();
        }
    }

    public Optional<Account> update(Account accountForUpdate) {
        final Account dbAccount = accountRepository.findById(accountForUpdate.getId())
                .orElse(new Account());

        Account updatedAccount = null;
        if (dbAccount.getId() != null) {
            dbAccount.setUser(accountForUpdate.getUser());
            dbAccount.setBalance(accountForUpdate.getBalance());
            dbAccount.setCurrency(accountForUpdate.getCurrency());
            updatedAccount = accountRepository.saveAndFlush(dbAccount);
        }

        return Optional.ofNullable(updatedAccount);
    }

    @Override
    public List<AccountInfoDto> findAllAccountsByUserId(Integer userId) {
        return jdbcTemplate.query(
                FIND_BY_USER_ID_SQL,
                (rs, rowNum) -> AccountInfoDto.builder()
                        .id(rs.getInt("id"))
                        .balance(rs.getInt("balance"))
                        .currency(rs.getString("currency"))
                        .build(),
                userId);
    }

    public Account updateBalance(Integer accountId, Integer amount, String operator) {
        if (amount > TRANSACTION_LIMIT) {
            return null;
        }

        final Account account = accountRepository.findById(accountId).orElseGet(Account::new);

        if ((account.getBalance() - amount) < ACCOUNT_BALANCE_MIN || account.getBalance() + amount > ACCOUNT_BALANCE_MAX) {
            return null;
        }

        Account updatedAccount = new Account();
        if (account.getId() != null) {
            if (operator.equals("-")) {
                account.setBalance(account.getBalance() - amount);
            } else {
                account.setBalance(account.getBalance() + amount);
            }
            updatedAccount = update(account).get();
        }

        return updatedAccount;
    }
}
