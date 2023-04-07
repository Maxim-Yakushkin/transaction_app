package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.Transaction;
import com.yakushkin.transaction_app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction findById(Integer transactionId) {
        return transactionRepository.findById(transactionId)
                .orElse(new Transaction());
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> update(Transaction transactionForUpdate) {
        Transaction dbTransaction = transactionRepository.findById(transactionForUpdate.getId())
                .orElse(new Transaction());

        Transaction updatedTransaction = null;
        if (dbTransaction.getId() != null) {
            dbTransaction.setAccount(transactionForUpdate.getAccount());
            dbTransaction.setAmount(transactionForUpdate.getAmount());
            updatedTransaction = transactionRepository.saveAndFlush(dbTransaction);
        }

        return Optional.ofNullable(updatedTransaction);
    }

    public boolean delete(Integer transactionId) {
        return transactionRepository.findById(transactionId)
                .map(entity -> {
                    transactionRepository.delete(entity);
                    transactionRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
