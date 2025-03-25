package com.local.accounts.application.strategy;

import com.local.accounts.router.dto.TransactionDTO;

import java.util.function.Consumer;

public interface TransactionHandle extends Consumer<TransactionDTO> {
}
