package com.local.accounts.application.usecase.transactions.impl;

import com.local.accounts.application.exception.AccountNotFoundException;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.transactions.TransactionStrategy;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.domain.transactionevents.DepositEvent;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DefaultDepositImpl implements TransactionStrategy {

    private final MongoEventStore eventStore;
    private final GetAccountData getAccountData;

    public DefaultDepositImpl(MongoEventStore eventStore, GetAccountData getAccountData) {
        this.eventStore = eventStore;
        this.getAccountData = getAccountData;
    }

    /**
     * Applies a deposit transaction to the specified account.
     *
     * @param transactionDTO the transaction data transfer object containing the account ID and the amount to deposit
     * @return the updated account aggregate after applying the deposit event
     * @throws AccountNotFoundException if the account with the specified ID is not found
     */
    @Override
    @Transactional
    public AccountAggregate apply(TransactionDTO transactionDTO) {
        AccountAggregate aggregate = getAccountData.getAccount(transactionDTO.getAccountId());
        DepositEvent event = new DepositEvent(transactionDTO.getAccountId(), transactionDTO.getAmount());
        aggregate.apply(event);
        eventStore.save(transactionDTO.getAccountId(), List.of(event));
        aggregate.markChangesAsCommitted();
        log.info("Deposit applied to account with ID: {}", transactionDTO.getAccountId());
        return aggregate;
    }
}
