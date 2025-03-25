package com.local.accounts.unit.usecase.transactions;

import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.transactions.impl.DefaultDepositImpl;
import com.local.accounts.application.usecase.transactions.impl.DefaultWithdrawImpl;
import com.local.accounts.application.strategy.impl.TransactionHandleImpl;
import com.local.accounts.domain.transactionevents.TransactionType;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class TransactionHandleImplTest {

    @Mock
    private MongoEventStore eventStore;

    @Mock
    private GetAccountData getAccountData;

    @Mock
    private DefaultDepositImpl defaultDeposit;

    @Mock
    private DefaultWithdrawImpl defaultWithdraw;

    @InjectMocks
    private TransactionHandleImpl transactionHandle;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionHandle = new TransactionHandleImpl(eventStore, getAccountData, defaultDeposit, defaultWithdraw);
    }

    @Test
    public void testAccept_Deposit() {
        // Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setType(TransactionType.DEPOSIT);
        transactionDTO.setAccountId("12345");
        transactionDTO.setAmount(500.0);

        // When
        transactionHandle.accept(transactionDTO);

        // Then
        verify(defaultDeposit).apply(transactionDTO);
    }

    @Test
    public void testAccept_Withdraw() {
        // Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setType(TransactionType.WITHDRAW);
        transactionDTO.setAccountId("12345");
        transactionDTO.setAmount(500.0);

        // When
        transactionHandle.accept(transactionDTO);

        // Then
        verify(defaultWithdraw).apply(transactionDTO);
    }

    @Test
    public void testAccept_UnsupportedTransactionType() {
        // Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setType(null); // Unsupported transaction type

        // When & Then
        try {
            transactionHandle.accept(transactionDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Transaction type not supported: null", e.getMessage());
        }
    }
}
