package com.local.accounts.unit.usecase.transactions;

import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.transactions.impl.DefaultDepositImpl;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.domain.transactionevents.DepositEvent;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class DefaultDepositImplTest {

    @Mock
    private MongoEventStore eventStore;

    @Mock
    private GetAccountData getAccountData;

    @InjectMocks
    private DefaultDepositImpl defaultDeposit;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply() {
        // Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId("12345");
        transactionDTO.setAmount(500.0);

        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        when(getAccountData.getAccount(transactionDTO.getAccountId())).thenReturn(accountAggregate);

        // When
        AccountAggregate result = defaultDeposit.apply(transactionDTO);

        // Then
        verify(accountAggregate).apply(any(DepositEvent.class));
        verify(eventStore).save(eq(transactionDTO.getAccountId()), anyList());
        verify(accountAggregate).markChangesAsCommitted();
        assertEquals(accountAggregate, result);
    }
}
