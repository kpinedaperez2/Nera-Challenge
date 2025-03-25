package com.local.accounts.unit.usecase.accounts;

import com.local.accounts.application.exception.AccountNotFoundException;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.updateaccount.impl.DefaultUpdateAccountImpl;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import com.local.accounts.router.dto.AccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultUpdateAccountImplTest {

    @Mock
    private MongoEventStore eventStore;

    @Mock
    private GetAccountData getAccountData;

    @InjectMocks
    private DefaultUpdateAccountImpl defaultUpdateAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply_Success() {
        // Given
        String accountId = "123456789";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(accountId);
        accountDTO.setAccountName("Prueba 1");
        accountDTO.setBalance(1000.0);
        accountDTO.setAccountType("ahorros");
        accountDTO.setCurrency("USD");

        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        when(getAccountData.getAccount(accountId)).thenReturn(accountAggregate);
        when(accountAggregate.getAccountId()).thenReturn(accountId);
        when(accountAggregate.getUncommittedChanges()).thenReturn(List.of());

        // When
        AccountAggregate result = defaultUpdateAccount.apply(accountId, accountDTO);

        // Then
        verify(eventStore).save(eq(accountId), anyList());
        verify(accountAggregate).markChangesAsCommitted();
        assertEquals(accountAggregate, result);
    }

    @Test
    public void testApply_AccountNotFoundException() {
        // Given
        String accountId = "12345";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(accountId);

        when(getAccountData.getAccount(accountId)).thenReturn(null);

        // When & Then
        assertThrows(AccountNotFoundException.class, () -> defaultUpdateAccount.apply(accountId, accountDTO));
    }

}




//@Test
//public void testApply_Success() {
//    // Given
//    String accountId = "12345";
//    AccountAggregate accountAggregate = mock(AccountAggregate.class);
//
//    AccountUpdatedEvent event = new AccountUpdatedEvent("12345", "prueba 1", 1000.0, "", "");
//    List<AccountEvent> events = List.of(event);
//    when(eventStore.getEvents(accountId)).thenReturn(events);
//    when(accountAggregate.getBalance()).thenReturn(1000.0);
//
//    // When
//    Double result = defaultGetAccountData.apply(accountId);
//
//    // Then
//    assertEquals(1000.0, result);
//    verify(eventStore).getEvents(accountId);
//}
//
//
//@Test
//public void testGetAccount_MongoClientException() {
//    // Given
//    String accountId = "12345";
//    when(eventStore.getEvents(accountId)).thenThrow(new MongoClientException("Cant read Database"));
//
//    // When & Then
//    assertThrows(MongoClientException.class, () -> defaultGetAccountData.getAccount(accountId));
//}
//
//
//@Test
//public void testApply_MongoClientException() {
//    // Given
//    String accountId = "12345";
//    when(eventStore.getEvents(accountId)).thenThrow(new MongoClientException("Cant read Database"));
//
//    // When & Then
//    assertThrows(MongoClientException.class, () -> defaultGetAccountData.apply(accountId));
//}
