package com.local.accounts.application.strategy.impl;

import com.local.accounts.application.strategy.TransactionHandle;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.transactions.impl.DefaultDepositImpl;
import com.local.accounts.application.usecase.transactions.impl.DefaultWithdrawImpl;
import com.local.accounts.application.usecase.transactions.TransactionStrategy;
import com.local.accounts.domain.transactionevents.TransactionType;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.local.accounts.domain.transactionevents.TransactionType.DEPOSIT;
import static com.local.accounts.domain.transactionevents.TransactionType.WITHDRAW;

@Service
public class TransactionHandleImpl implements TransactionHandle {

    private final Map<TransactionType, TransactionStrategy> strategyMap;

    public TransactionHandleImpl(MongoEventStore eventStore, GetAccountData getAccountData, DefaultDepositImpl defaultDeposit, DefaultWithdrawImpl defaultWithdraw) {
        strategyMap = new HashMap<>();
        strategyMap.put(DEPOSIT, defaultDeposit);
        strategyMap.put(WITHDRAW, defaultWithdraw);
    }

    /**
     * Accepts a transaction and applies the corresponding strategy based on the transaction type.
     *
     * @param transactionDTO the transaction data transfer object containing the transaction details
     * @throws IllegalArgumentException if the transaction type is not supported
     */
    @Override
    public void accept(TransactionDTO transactionDTO) {
        TransactionStrategy strategy = strategyMap.get(transactionDTO.getType());
        if (strategy != null) {
            strategy.apply(transactionDTO);
        } else {
            throw new IllegalArgumentException("Transaction type not supported: " + transactionDTO.getType());
        }
    }
}
