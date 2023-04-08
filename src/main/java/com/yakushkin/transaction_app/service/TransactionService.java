package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        if (Math.abs(transaction.getAmount()) > 100000000) {
            return null;
        }

        return transactionRepository.save(transaction);
    }
}
