package com.securetrade.service;

import org.springframework.stereotype.Service;

// this service gives a reason/explanation for why a transaction was flagged
// for now it uses simple if-else logic
// can be connected to a real AI API later

@Service
public class AiService {

    public String explainFraud(double amount, String location) {

        if (amount > 100000) {
            return "Transaction flagged due to unusually high amount";
        }

        if ("Unknown".equalsIgnoreCase(location)) {
            return "Transaction flagged due to suspicious location";
        }

        // default reason
        return "Transaction pattern appears suspicious";
    }
}