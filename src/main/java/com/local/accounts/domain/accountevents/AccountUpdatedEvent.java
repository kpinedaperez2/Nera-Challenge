package com.local.accounts.domain.accountevents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountUpdatedEvent implements AccountEvent {

    private final String accountId;
    private final String accountName;
    private final Double balance;
    private final String accountType;
    private final String currency;

}
