package com.local.accounts.domain.transactionevents;

import com.local.accounts.domain.accountevents.AccountEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class DepositEvent  implements AccountEvent {
    private final String accountId;
    private final String transactionId;
    private final Double amount;
    private final LocalDateTime timestamp;

    public DepositEvent(String accountId, Double amount) {
        this.accountId = accountId;
        this.transactionId = UUID.randomUUID().toString();
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getAccountId() {
        return accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
