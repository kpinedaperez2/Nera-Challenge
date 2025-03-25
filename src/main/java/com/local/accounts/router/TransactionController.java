package com.local.accounts.router;

import com.local.accounts.application.strategy.TransactionHandle;
import com.local.accounts.router.dto.TransactionDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TransactionController {

    private final TransactionHandle transactionHandle;


    public TransactionController(TransactionHandle transactionHandle) {
        this.transactionHandle = transactionHandle;

    }

    /**
     * Handles a transaction request.
     *
     * @param transactionDTO the transaction data transfer object
     */
    @PostMapping("/transactions")
    public void transaction(@RequestBody TransactionDTO transactionDTO) {
        transactionHandle.accept(transactionDTO);
    }
}
