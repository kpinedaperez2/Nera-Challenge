package com.local.accounts.unit.usecase.transactions;

import com.local.accounts.application.exception.InsufficientFundsException;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.transactions.impl.DefaultWithdrawImpl;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.domain.transactionevents.WithdrawalEvent;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DefaultWithdrawImplTest {

    @Mock
    private MongoEventStore eventStore;

    @Mock
    private GetAccountData getAccountData;

    @InjectMocks
    private DefaultWithdrawImpl defaultWithdraw;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply_Success() {
        // Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId("12345");
        transactionDTO.setAmount(500.0);

        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        when(getAccountData.getAccount(transactionDTO.getAccountId())).thenReturn(accountAggregate);
        when(accountAggregate.getBalance()).thenReturn(1000.0);

        // When
        AccountAggregate result = defaultWithdraw.apply(transactionDTO);

        // Then
        verify(accountAggregate).apply(any(WithdrawalEvent.class));
        verify(eventStore).save(eq(transactionDTO.getAccountId()), anyList());
        verify(accountAggregate).markChangesAsCommitted();
        assertEquals(accountAggregate, result);
    }

    @Test
    public void testApply_InsufficientFunds() {
        // Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId("12345");
        transactionDTO.setAmount(1500.0);

        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        when(getAccountData.getAccount(transactionDTO.getAccountId())).thenReturn(accountAggregate);
        when(accountAggregate.getBalance()).thenReturn(1000.0);

        // Then
        assertThrows(InsufficientFundsException.class, () -> defaultWithdraw.apply(transactionDTO));
    }
}
