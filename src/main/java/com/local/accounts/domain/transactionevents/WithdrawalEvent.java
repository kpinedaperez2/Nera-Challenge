package com.local.accounts.domain.transactionevents;

import com.local.accounts.domain.accountevents.AccountEvent;

import java.time.LocalDateTime;

public class WithdrawalEvent implements AccountEvent {
    private final String accountId;
    private final Double amount;
    private final LocalDateTime timestamp;

    public WithdrawalEvent(String accountId, Double amount) {
        this.accountId = accountId;
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
}