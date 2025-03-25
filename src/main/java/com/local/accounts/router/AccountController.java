package com.local.accounts.router;

import com.local.accounts.application.usecase.createaccount.CreateAccount;
import com.local.accounts.application.usecase.deleteaccount.DeleteAccount;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.updateaccount.UpdateAccount;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.infrastructure.config.auth.JwtUtil;
import com.local.accounts.router.dto.AccountDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AccountController {

    private final CreateAccount createAccount;
    private final GetAccountData getAccountData;
    private final UpdateAccount updateAccount;
    private final DeleteAccount deleteAccount;


    public AccountController(CreateAccount createAccount, GetAccountData getAccountData, UpdateAccount updateAccount, DeleteAccount deleteAccount) {
        this.createAccount = createAccount;
        this.getAccountData = getAccountData;
        this.updateAccount = updateAccount;
        this.deleteAccount = deleteAccount;
    }

    /**
     * Creates a new account.
     *
     * @param accountDTO the account data transfer object
     * @return the ID of the created account
     */
    @PostMapping("/accounts")
    public String createAccount(@RequestBody AccountDTO accountDTO) {
        return createAccount.apply(accountDTO);
    }

    /**
     * Retrieves the balance of the account with the specified ID.
     *
     * @param id the ID of the account
     * @return the balance of the account
     */
    @GetMapping("/accounts/{id}/balance")
    public Double getAccountBalance(@PathVariable String id) {
        return getAccountData.apply(id);
    }

    /**
     * Retrieves the account details with the specified ID.
     *
     * @param id the ID of the account
     * @return the account aggregate
     */
    @GetMapping("/accounts/{id}")
    public AccountAggregate getAccount(@PathVariable String id) {
        return getAccountData.getAccount(id);
    }

    /**
     * Updates the account with the specified ID.
     *
     * @param id the ID of the account
     * @param accountDTO the account data transfer object
     * @return the updated account aggregate
     */
    @PutMapping("/accounts/{id}/update")
    public AccountAggregate createAccount(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        return updateAccount.apply(id, accountDTO);
    }

    /**
     * Deletes the account with the specified ID.
     *
     * @param id the ID of the account
     */
    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable String id) {
        deleteAccount.accept(id);
    }
}
