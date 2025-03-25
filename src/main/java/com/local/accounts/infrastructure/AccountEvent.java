package com.local.accounts.infrastructure;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "account_events")
public class AccountEvent {
    private String accountId;
    private Object event; // Puede ser AccountCreatedEvent, AccountUpdatedEvent o TransactionEvent

    public AccountEvent(String accountId, Object event) {
        this.accountId = accountId;
        this.event = event;
    }

}
