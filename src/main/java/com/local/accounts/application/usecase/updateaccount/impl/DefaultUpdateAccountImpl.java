package com.local.accounts.application.usecase.updateaccount.impl;

import com.local.accounts.application.exception.AccountNotFoundException;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.updateaccount.UpdateAccount;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.AccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DefaultUpdateAccountImpl implements UpdateAccount {


    private final MongoEventStore eventStore;
    private final GetAccountData getAccountData;

    public DefaultUpdateAccountImpl(MongoEventStore eventStore, GetAccountData getAccountData) {
        this.eventStore = eventStore;
        this.getAccountData = getAccountData;
    }

    /**
     * Updates the specified account with the provided account details.
     *
     * @param accountId the ID of the account to update
     * @param accountDTO the account data transfer object containing the updated account details
     * @return the updated account aggregate after applying the changes
     * @throws AccountNotFoundException if the account with the specified ID is not found
     */
    @Override
    @Transactional
    public AccountAggregate apply(String accountId, AccountDTO accountDTO) {
        AccountAggregate aggregate = getAccountData.getAccount(accountDTO.getAccountId());
        if (aggregate == null) {
            throw new AccountNotFoundException("Account with ID " + accountDTO.getAccountId() + " not found");
        }
        toAccountAggregate(aggregate, accountDTO);
        eventStore.save(aggregate.getAccountId(), aggregate.getUncommittedChanges());
        aggregate.markChangesAsCommitted();
        log.info("Account updated");
        return aggregate;
    }

    private void toAccountAggregate(AccountAggregate aggregate, AccountDTO accountDTO) {
        aggregate.update(
                accountDTO.getAccountName(),
                accountDTO.getBalance(),
                accountDTO.getAccountType(),
                accountDTO.getCurrency());
    }
}
