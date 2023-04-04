package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.Account;
import com.yakushkin.transaction_app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
