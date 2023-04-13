package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.exception.TransactionLimitException;
import com.yakushkin.transaction_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final int TRANSACTION_LIMIT = 100000000;
    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) throws TransactionLimitException {
        if (Math.abs(transaction.getAmount()) > TRANSACTION_LIMIT) {
            throw new TransactionLimitException();
        }

        return transactionRepository.save(transaction);
    }
}
