package com.local.accounts.infrastructure.clients.db;

import com.local.accounts.domain.accountevents.AccountEvent;
import com.local.accounts.infrastructure.clients.db.entity.EventDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class MongoEventStore {

    private final EventRepository repository;

    public MongoEventStore(EventRepository repository) {
        this.repository = repository;
    }

    /**
     * Saves a list of account events to the repository.
     *
     * @param aggregateId the ID of the aggregate to which the events belong
     * @param events the list of account events to be saved
     */
    public void save(String aggregateId, List<AccountEvent> events) {
        List<EventDocument> documents = events.stream()
                .map(event -> new EventDocument(aggregateId, event))
                .collect(Collectors.toList());
        repository.saveAll(documents);
        log.info("Events saved ");
    }

    /**
     * Retrieves a list of account events from the repository based on the aggregate ID.
     *
     * @param aggregateId the ID of the aggregate whose events are to be retrieved
     * @return a list of account events associated with the given aggregate ID
     */
    public List<AccountEvent> getEvents(String aggregateId) {
        List<EventDocument> documents = repository.findByAggregateId(aggregateId);
        log.info("Events retrieved ");
        return documents.stream()
                .map(EventDocument::getEvent)
                .collect(Collectors.toList());
    }
}
