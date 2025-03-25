package com.local.accounts.unit.infrastructure;

import com.local.accounts.domain.accountevents.AccountEvent;
import com.local.accounts.infrastructure.clients.db.entity.EventDocument;
import com.local.accounts.infrastructure.clients.db.EventRepository;
import com.local.accounts.infrastructure.clients.db.MongoEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MongoEventStoreTest {


    @Mock
    private EventRepository repository;

    @InjectMocks
    private MongoEventStore eventStore;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_Success() {
        // Given
        String aggregateId = "12345";
        AccountEvent event = mock(AccountEvent.class);
        List<AccountEvent> events = List.of(event);
        EventDocument expectedDocument = new EventDocument(aggregateId, event);

        // When
        eventStore.save(aggregateId, events);

        // Then
        ArgumentCaptor<List<EventDocument>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());
        List<EventDocument> capturedDocuments = captor.getValue();
        assertEquals(1, capturedDocuments.size());
        assertEquals(expectedDocument, capturedDocuments.get(0));
    }


    @Test
    public void testGetEvents_Success() {
        // Given
        String aggregateId = "12345";
        AccountEvent event = mock(AccountEvent.class);
        EventDocument document = new EventDocument(aggregateId, event);
        List<EventDocument> documents = List.of(document);
        when(repository.findByAggregateId(aggregateId)).thenReturn(documents);

        // When
        List<AccountEvent> result = eventStore.getEvents(aggregateId);

        // Then
        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
        verify(repository).findByAggregateId(aggregateId);
    }
}
