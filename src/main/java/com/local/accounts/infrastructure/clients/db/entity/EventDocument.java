package com.local.accounts.infrastructure.clients.db.entity;

import com.local.accounts.domain.accountevents.AccountEvent;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "events")
public class EventDocument {

    @Id
    private String accountId;
    private String aggregateId;
    private AccountEvent event;
    private String createdAt;

    public EventDocument(String aggregateId, AccountEvent event) {
        this.aggregateId = aggregateId;
        this.event = event;
    }
}
