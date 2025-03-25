package com.local.accounts.application.usecase.transactions;

import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.router.dto.TransactionDTO;

import java.util.function.Function;

public interface TransactionStrategy extends Function<TransactionDTO, AccountAggregate> {

//    Double execute(AccountAggregate account, Double amount);
}
