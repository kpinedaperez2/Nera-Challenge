package com.local.accounts.infrastructure.clients.db;

import com.local.accounts.infrastructure.clients.db.entity.EventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<EventDocument, String> {
    List<EventDocument> findByAggregateId(String accountId);
}
