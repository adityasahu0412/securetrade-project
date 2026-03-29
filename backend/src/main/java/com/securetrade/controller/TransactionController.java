package com.securetrade.controller;

import com.securetrade.model.Transaction;
import com.securetrade.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// all transaction related APIs are here

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService service;

    // create a new transaction and check for fraud
    @PostMapping("/transactions")
    public Transaction create(@RequestBody Transaction txn) {
        return service.createTransaction(txn);
    }

    // get all transactions
    @GetMapping("/transactions")
    public List<Transaction> getAll() {
        return service.getAll();
    }

    // get only fraud transactions
    @GetMapping("/fraud")
    public List<Transaction> getFrauds() {
        return service.getFrauds();
    }

    // get total count of fraud transactions
    @GetMapping("/fraud/count")
    public int getFraudCount() {
        return service.getFrauds().size();
    }
}