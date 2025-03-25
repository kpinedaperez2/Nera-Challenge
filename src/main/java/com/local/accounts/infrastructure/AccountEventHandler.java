package com.local.accounts.infrastructure;

import com.local.accounts.infrastructure.clients.db.EventRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountEventHandler {
    private final EventRepository accountEventRepository;

    public AccountEventHandler(EventRepository accountEventRepository) {
        this.accountEventRepository = accountEventRepository;
    }

//    public void handleAccountCreated(AccountCreatedEvent event) {
//        accountEventRepository.save(new AccountEvent(event.getAccountId(), event));
//    }
//
//    public void handleTransaction(TransactionEvent event) {
//        accountEventRepository.save(new AccountEvent(event.getAccountId(), event));
//    }
//
//    public Account getAccount(String accountId) {
//        List<AccountEvent> events = accountEventRepository.findByAccountId(accountId);
//        return Account.reconstructFromEvents(events.stream()
//                .map(AccountEvent::getEvent)
//                .collect(Collectors.toList()));
//    }
}
