package com.local.accounts.unit.usecase.accounts;

import com.local.accounts.application.usecase.deleteaccount.impl.DefaultDeleteAccountImpl;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class DefaultDeleteAccountImplTest {

    @Mock
    private MongoEventStore eventStore;

    @Mock
    private GetAccountData getAccountData;

    @InjectMocks
    private DefaultDeleteAccountImpl defaultDeleteAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAccept() {
        // Given
        String accountId = "12345";
        AccountAggregate accountAggregate = mock(AccountAggregate.class);
        when(getAccountData.getAccount(accountId)).thenReturn(accountAggregate);

        // When
        defaultDeleteAccount.accept(accountId);

        // Then
        verify(accountAggregate).delete();
        verify(eventStore).save(eq(accountId), anyList());
        verify(accountAggregate).markChangesAsCommitted();
    }
}
