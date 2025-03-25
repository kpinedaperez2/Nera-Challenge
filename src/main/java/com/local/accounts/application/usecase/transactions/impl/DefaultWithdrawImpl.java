package com.local.accounts.application.usecase.transactions.impl;

import com.local.accounts.application.exception.AccountNotFoundException;
import com.local.accounts.application.exception.InsufficientFundsException;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.transactions.TransactionStrategy;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.domain.transactionevents.WithdrawalEvent;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DefaultWithdrawImpl implements TransactionStrategy {

    private final MongoEventStore eventStore;
    private final GetAccountData getAccountData;

    public DefaultWithdrawImpl(MongoEventStore eventStore, GetAccountData getAccountData) {
        this.eventStore = eventStore;
        this.getAccountData = getAccountData;
    }

    /**
     * Applies a withdrawal transaction to the specified account.
     *
     * @param transactionDTO the transaction data transfer object containing the account ID and the amount to withdraw
     * @return the updated account aggregate after applying the withdrawal event
     * @throws AccountNotFoundException if the account with the specified ID is not found
     * @throws InsufficientFundsException if the account does not have sufficient funds for the withdrawal
     */
    @Override
    @Transactional
    public AccountAggregate apply(TransactionDTO transactionDTO) {
        AccountAggregate aggregate = getAccountData.getAccount(transactionDTO.getAccountId());
        WithdrawalEvent event = new WithdrawalEvent(transactionDTO.getAccountId(), transactionDTO.getAmount());
        if (aggregate.getBalance() < transactionDTO.getAmount()) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        aggregate.apply(event);
        eventStore.save(transactionDTO.getAccountId(), List.of(event));
        aggregate.markChangesAsCommitted();
        return aggregate;
    }
}
