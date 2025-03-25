package com.local.accounts.application.usecase.createaccount.impl;

import com.local.accounts.application.usecase.createaccount.CreateAccount;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.AccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DefaultCreateAccountImpl implements CreateAccount {

    private final MongoEventStore eventStore;

    public DefaultCreateAccountImpl( MongoEventStore eventStore) {
        this.eventStore = eventStore;
    }

    /**
     * Creates a new account and applies the provided account details.
     *
     * @param accountDTO the account data transfer object containing the account details
     * @return the ID of the newly created account
     */
    @Override
    public String apply(AccountDTO accountDTO) {
        String accountId = UUID.randomUUID().toString();
        AccountAggregate aggregate = toAccountAggregate(accountId, accountDTO);
        eventStore.save(String.valueOf(accountId), aggregate.getUncommittedChanges());
        aggregate.markChangesAsCommitted();
        log.info("Account created with ID: {}", accountId);
        return accountId;
    }

    private AccountAggregate toAccountAggregate(String accountId, AccountDTO accountDTO) {
        return AccountAggregate.create(
                accountId,
                accountDTO.getAccountName(),
                accountDTO.getBalance(),
                accountDTO.getAccountType(),
                accountDTO.getCurrency());
    }
}
