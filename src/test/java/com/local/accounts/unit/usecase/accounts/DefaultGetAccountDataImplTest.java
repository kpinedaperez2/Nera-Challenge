package com.local.accounts.unit.usecase.accounts;

import com.local.accounts.application.usecase.getbalance.impl.DefaultGetAccountDataImpl;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.domain.accountevents.AccountEvent;
import com.local.accounts.domain.accountevents.AccountUpdatedEvent;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.mongodb.MongoClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class DefaultGetAccountDataImplTest {

    @Mock
    private MongoEventStore eventStore;

    @Spy
    @InjectMocks
    private DefaultGetAccountDataImpl defaultGetAccountData;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAccount_Success() {
        // Given
        String accountId = "12345";
        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        List<AccountEvent> events = List.of();
        when(eventStore.getEvents(accountId)).thenReturn(events);
        doNothing().when(accountAggregate).replayEvents(events);

        doReturn(accountAggregate).when(defaultGetAccountData).getAccount(accountId);

        // When
        AccountAggregate result = defaultGetAccountData.getAccount(accountId);

        // Then
        assertEquals(accountAggregate, result);
    }


    @Test
    public void testApply_Success() {
        // Given
        String accountId = "12345";
        AccountAggregate accountAggregate = mock(AccountAggregate.class);

        AccountUpdatedEvent event = new AccountUpdatedEvent("12345", "prueba 1", 1000.0, "", "");
        List<AccountEvent> events = List.of(event);
        when(eventStore.getEvents(accountId)).thenReturn(events);
        when(accountAggregate.getBalance()).thenReturn(1000.0);

        // When
        Double result = defaultGetAccountData.apply(accountId);

        // Then
        assertEquals(1000.0, result);
        verify(eventStore).getEvents(accountId);
    }


    @Test
    public void testGetAccount_MongoClientException() {
        // Given
        String accountId = "12345";
        when(eventStore.getEvents(accountId)).thenThrow(new MongoClientException("Cant read Database"));

        // When & Then
        assertThrows(MongoClientException.class, () -> defaultGetAccountData.getAccount(accountId));
    }


    @Test
    public void testApply_MongoClientException() {
        // Given
        String accountId = "12345";
        when(eventStore.getEvents(accountId)).thenThrow(new MongoClientException("Cant read Database"));

        // When & Then
        assertThrows(MongoClientException.class, () -> defaultGetAccountData.apply(accountId));
    }
}
