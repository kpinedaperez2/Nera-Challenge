package com.local.accounts.infrastructure.config.aspect;

import com.local.accounts.router.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DepositLoggingAspect {

    private static final double THRESHOLD = 10000.0;

    @Before("execution(* com.local.accounts.application.usecase.transactions.impl.DefaultDepositImpl.apply(..)) && args(transactionDTO)")
    public void logDeposit(JoinPoint joinPoint, TransactionDTO transactionDTO) {
        double amount = transactionDTO.getAmount();
        if (amount > THRESHOLD) {
            log.warn("Big deposit: {} in account {}", amount, transactionDTO.getAccountId());
        }
    }
}
