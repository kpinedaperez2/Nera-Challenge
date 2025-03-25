package com.local.accounts.domain.accountevents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountDeletedEvent implements AccountEvent {

    private final String accountId;

}
