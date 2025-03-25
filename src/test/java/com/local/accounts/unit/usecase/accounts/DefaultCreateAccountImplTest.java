package com.local.accounts.unit.usecase.accounts;

import com.local.accounts.application.usecase.createaccount.impl.DefaultCreateAccountImpl;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.AccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class DefaultCreateAccountImplTest {

    @Mock
    private MongoEventStore eventStore;

    @InjectMocks
    private DefaultCreateAccountImpl defaultCreateAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply() {
        // Given
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountName("Prueba 1");
        accountDTO.setBalance(1000.0);
        accountDTO.setAccountType("ahorros");
        accountDTO.setCurrency("USD");

        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        when(accountAggregate.getUncommittedChanges()).thenReturn(List.of());

        // When
        String accountId = defaultCreateAccount.apply(accountDTO);

        // Then
        verify(eventStore).save(eq(accountId), anyList());
        assertEquals(accountId.length(), UUID.randomUUID().toString().length());
    }
}
