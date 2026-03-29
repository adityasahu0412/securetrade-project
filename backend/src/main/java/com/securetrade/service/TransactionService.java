package com.securetrade.service;

import com.securetrade.model.Transaction;
import com.securetrade.model.User;
import com.securetrade.repository.TransactionRepository;
import com.securetrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

// main service for handling transactions
// also calls fraud detection and ai service

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    @Autowired
    private FraudDetectionService fraudService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AiService aiService;

    public Transaction createTransaction(Transaction txn) {

        // get the username of currently logged in user from JWT
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        System.out.println("Creating transaction for user: " + username);

        // find user in DB to get their id
        User user = userRepo.findByUsername(username)
                .orElseThrow();

        // set userId so we know which user made this transaction
        txn.setUserId(user.getId());

        // run fraud detection
        boolean isFraud = fraudService.isFraud(txn);
        txn.setFraud(isFraud);

        // if fraud, get reason from ai service
        if (isFraud) {
            String reason = aiService.explainFraud(txn.getAmount(), txn.getLocation());
            txn.setFraudReason(reason);
            System.out.println("Fraud detected! Reason: " + reason);
        }

        return repo.save(txn);
    }

    public List<Transaction> getAll() {

        // get logged in username from security context
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // get user object from DB
        User user = userRepo.findByUsername(username)
                .orElseThrow();

        // return only this user's transactions
        return repo.findByUserId(user.getId());
    }

    public List<Transaction> getFrauds() {

        // get logged in username from security context
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // get user object from DB
        User user = userRepo.findByUsername(username)
                .orElseThrow();

        // return only this user's fraud transactions
        return repo.findByUserIdAndFraudTrue(user.getId());
    }
}