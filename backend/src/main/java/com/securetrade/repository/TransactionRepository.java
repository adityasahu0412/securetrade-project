package com.securetrade.repository;

import com.securetrade.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA automatically gives us save, findAll, findById, delete etc.
// we just need to add custom queries here

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // get all transactions where fraud = true
    List<Transaction> findByFraudTrue();

    // get all transactions by a specific user
    List<Transaction> findByUserId(Long userId);

    // get fraud transactions of a specific user
    List<Transaction> findByUserIdAndFraudTrue(Long userId);
}