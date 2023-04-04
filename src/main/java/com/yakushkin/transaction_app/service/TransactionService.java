package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
