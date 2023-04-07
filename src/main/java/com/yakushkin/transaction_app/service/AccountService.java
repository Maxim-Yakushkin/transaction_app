package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElse(new Account());
    }

    public List<Account> findAll() {
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
}
