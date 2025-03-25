package com.local.accounts.domain.accountevents;

import com.local.accounts.domain.transactionevents.DepositEvent;
import com.local.accounts.domain.transactionevents.WithdrawalEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
public class AccountAggregate {

    private String accountId;
    private String accountName;
    private Double balance;
    private String accountType;
    private String currency;
    private boolean deleted = false;

    public String getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<AccountEvent> getChanges() {
        return changes;
    }

    private final List<AccountEvent> changes = new ArrayList<>();

    public AccountAggregate() { }

    public static AccountAggregate create(String accountId, String accountName, Double balance, String accountType, String currency) {
        AccountAggregate aggregate = new AccountAggregate();
        AccountCreatedEvent event = new AccountCreatedEvent(accountId, accountName, balance, accountType, currency);
        aggregate.apply(event);
        aggregate.changes.add(event);
        return aggregate;
    }

    public void update(String accountName, Double balance, String accountType, String currency) {
        AccountUpdatedEvent event = new AccountUpdatedEvent(this.accountId, accountName, balance, accountType, currency);
        apply(event);
        changes.add(event);
    }

    /**
     * Marks the account as deleted and records the delete event.
     */
    public void delete() {
        AccountDeletedEvent event = new AccountDeletedEvent(this.accountId);
        apply(event);
        changes.add(event);
    }

    /**
     * Applies the given account event to update the account state.
     *
     * @param event the account event to apply
     */
    public void apply(AccountEvent event) {
        if (event instanceof AccountCreatedEvent eventClass) {
            this.accountId = eventClass.getAccountId();
            this.accountName = eventClass.getAccountName();
            this.balance = eventClass.getBalance();
            this.accountType = eventClass.getAccountType();
            this.currency = eventClass.getCurrency();
        } else if (event instanceof AccountUpdatedEvent eventClass) {
            this.accountName = eventClass.getAccountName();
            this.balance = eventClass.getBalance();
            this.accountType = eventClass.getAccountType();
            this.currency = eventClass.getCurrency();
        } else if (event instanceof AccountDeletedEvent) {
            this.deleted = true;
        } else if (event instanceof DepositEvent depositEvent) {
            this.balance += depositEvent.getAmount();
        } else if (event instanceof WithdrawalEvent withdrawalEvent) {
            this.balance -= withdrawalEvent.getAmount();
        }
    }

    /**
     * Replays the given list of account events to restore the account state.
     *
     * @param events the list of account events to replay
     */
    public void replayEvents(List<AccountEvent> events) {
        events.forEach(this::apply);
    }

    /**
     * Returns the list of uncommitted account events.
     *
     * @return the list of uncommitted account events
     */
    public List<AccountEvent> getUncommittedChanges() {
        return new ArrayList<>(changes);
    }

    /**
     * Marks all changes as committed by clearing the list of uncommitted events.
     */
    public void markChangesAsCommitted() {
        changes.clear();
    }
}
