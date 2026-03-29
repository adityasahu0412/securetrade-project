package com.securetrade.model;

import jakarta.persistence.*;

// Transaction model - stores each transaction made by user
// fraud flag is set by FraudDetectionService

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private double amount;
    private String location;
    private long timestamp;

    // these fields are set automatically when transaction is created
    private boolean fraud;
    private String fraudReason;

    // getters and setters
    // not using Lombok because it was giving errors so wrote manually

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isFraud() {
        return fraud;
    }

    public void setFraud(boolean fraud) {
        this.fraud = fraud;
    }

    public String getFraudReason() {
        return fraudReason;
    }

    public void setFraudReason(String fraudReason) {
        this.fraudReason = fraudReason;
    }
}