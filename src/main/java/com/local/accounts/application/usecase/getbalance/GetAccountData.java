package com.local.accounts.application.usecase.getbalance;

import com.local.accounts.domain.accountevents.AccountAggregate;

import java.util.function.Function;

public interface GetAccountData extends Function<String, Double> {

    AccountAggregate getAccount(String accountId);
}
