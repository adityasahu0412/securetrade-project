package com.securetrade.service;

import com.securetrade.model.Transaction;
import com.securetrade.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// this service checks if a transaction is fraud or not
// using simple rule based logic

@Service
public class FraudDetectionService {

    @Autowired
    private TransactionRepository repo;

    public boolean isFraud(Transaction txn) {

        // Rule 1: flag if amount is more than 1 lakh
        if (txn.getAmount() > 100000) {
            return true;
        }

        // Rule 2: flag if same user made more than 5 transactions
        // getting all transactions and filtering by userId
        List<Transaction> allTransactions = repo.findAll();

        long userTransactionCount = allTransactions.stream()
                .filter(t -> t.getUserId() != null && t.getUserId().equals(txn.getUserId()))
                .count();

        if (userTransactionCount > 5) {
            return true;
        }

        // Rule 3: flag if location is Unknown
        if ("Unknown".equalsIgnoreCase(txn.getLocation())) {
            return true;
        }

        return false;
    }
}