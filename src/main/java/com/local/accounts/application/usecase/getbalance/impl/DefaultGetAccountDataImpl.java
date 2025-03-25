package com.local.accounts.application.usecase.getbalance.impl;

import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.domain.accountevents.AccountEvent;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.mongodb.MongoClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DefaultGetAccountDataImpl implements GetAccountData {


    private final MongoEventStore eventStore;

    public DefaultGetAccountDataImpl(MongoEventStore eventStore) {
        this.eventStore = eventStore;

    }

    @Override
    public AccountAggregate getAccount(String accountId) {
        try {
            return getAccountAggregate(accountId);
        } catch (MongoClientException e) {
            log.error("Error trying of obtain account balance for account: {}", accountId, e);
            throw new MongoClientException("Cant read Database", e);
        }
    }

    @Override
    public Double apply(String accountId) {
        try {
            AccountAggregate aggregate = getAccountAggregate(accountId);
            return aggregate.getBalance();
        } catch (MongoClientException e) {
            log.error("Error trying of obtain account balance for account: {}", accountId, e);
            throw new MongoClientException("Cant read Database", e);
        }
    }

    private AccountAggregate getAccountAggregate(String accountId) {
        List<AccountEvent> events = eventStore.getEvents(accountId);
        AccountAggregate aggregate = new AccountAggregate();
        aggregate.replayEvents(events);
        return aggregate;
    }
}
