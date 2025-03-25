package com.local.accounts.domain.transactionevents;

public enum TransactionType {
    DEPOSIT("deposit"),
    WITHDRAW("withdraw");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
