package com.local.accounts.application.usecase.deleteaccount.impl;

import com.local.accounts.application.usecase.deleteaccount.DeleteAccount;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DefaultDeleteAccountImpl implements DeleteAccount {

    private final MongoEventStore eventStore;
    private final GetAccountData getAccountData;

    public DefaultDeleteAccountImpl(MongoEventStore eventStore, GetAccountData getAccountData) {
        this.eventStore = eventStore;
        this.getAccountData = getAccountData;
    }

    @Override
    @Transactional
    public void accept(String accountId) {
        AccountAggregate aggregate = getAccountData.getAccount(accountId);
        aggregate.delete();
        eventStore.save(accountId, aggregate.getUncommittedChanges());
        aggregate.markChangesAsCommitted();
        log.info("Account deleted");
    }
}
