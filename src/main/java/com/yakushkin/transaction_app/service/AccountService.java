package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.dto.AccountInfoDto;
import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.repository.AccountRepository;
import com.yakushkin.transaction_app.repository.FilterAccountRepository;
import com.yakushkin.transaction_app.sql.AccountSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements FilterAccountRepository {

    private static final String FIND_BY_USER_ID = AccountSql.FIND_BY_USER_ID;

    private final AccountRepository accountRepository;
    private final JdbcTemplate jdbcTemplate;

    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElse(new Account());
    }

    public List<Account> findAllByUserId() {
        return accountRepository.findAll();
    }

    public Optional<Account> update(Account accountForUpdate) {
        Account dbAccount = accountRepository.findById(accountForUpdate.getId())
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

    public boolean delete(Integer accountId) {
        return accountRepository.findById(accountId)
                .map(account -> {
                    accountRepository.delete(account);
                    accountRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public List<AccountInfoDto> findAllAccountsByUserId(Integer userId) {
        return jdbcTemplate.query(
                FIND_BY_USER_ID,
                (rs, rowNum) -> AccountInfoDto.builder()
                        .id(rs.getInt("id"))
                        .balance(rs.getInt("balance"))
                        .currency(rs.getString("currency"))
                        .build(),
                userId);
    }

    public Account addIncome(Integer accountId, Integer amount) {
        final Account account = accountRepository.findById(accountId).orElseGet(Account::new);

        Account updatedAccount = new Account();
        if (account.getId() != null) {
            account.setBalance(account.getBalance() + amount);
            updatedAccount = update(account).get();
        }

        return updatedAccount;
    }

    public Account addExpense(Integer accountId, Integer amount) {
        final Account account = accountRepository.findById(accountId).orElseGet(Account::new);

        Account updatedAccount = new Account();
        if (account.getId() != null) {
            account.setBalance(account.getBalance() - amount);
            updatedAccount = update(account).get();
        }

        return updatedAccount;
    }
}
