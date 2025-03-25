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

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AccountController(CreateAccount createAccount, GetAccountData getAccountData, UpdateAccount updateAccount, DeleteAccount deleteAccount,  AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.createAccount = createAccount;
        this.getAccountData = getAccountData;
        this.updateAccount = updateAccount;
        this.deleteAccount = deleteAccount;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/accounts")
    public String createAccount(@RequestBody AccountDTO accountDTO) {
        return createAccount.apply(accountDTO);
    }

    @GetMapping("/accounts/{id}/balance")
    public Double getAccountBalance(@PathVariable String id) {
        return getAccountData.apply(id);
    }

    @GetMapping("/accounts/{id}")
    public AccountAggregate getAccount(@PathVariable String id) {
        return getAccountData.getAccount(id);
    }

    @PutMapping("/accounts/{id}/update")
    public AccountAggregate createAccount(@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        return updateAccount.apply(id, accountDTO);
    }

    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable String id) {
        deleteAccount.accept(id);
    }

}