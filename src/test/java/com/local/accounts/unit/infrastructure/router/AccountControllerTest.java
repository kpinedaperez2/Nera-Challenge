package com.local.accounts.unit.infrastructure.router;

import com.local.accounts.application.usecase.createaccount.CreateAccount;
import com.local.accounts.application.usecase.deleteaccount.DeleteAccount;
import com.local.accounts.application.usecase.getbalance.GetAccountData;
import com.local.accounts.application.usecase.updateaccount.UpdateAccount;
import com.local.accounts.domain.accountevents.AccountAggregate;
import com.local.accounts.router.AccountController;
import com.local.accounts.router.dto.AccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    @Mock
    private CreateAccount createAccount;

    @Mock
    private GetAccountData getAccountData;

    @Mock
    private UpdateAccount updateAccount;

    @Mock
    private DeleteAccount deleteAccount;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        when(createAccount.apply(any(AccountDTO.class))).thenReturn("accountId");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\":\"value\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("accountId"));

        verify(createAccount).apply(any(AccountDTO.class));
    }

    @Test
    public void testGetAccountBalance() throws Exception {
        when(getAccountData.apply("123")).thenReturn(100.0);

        mockMvc.perform(get("/accounts/123/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));

        verify(getAccountData).apply("123");
    }

    @Test
    public void testGetAccount() throws Exception {
        AccountAggregate accountAggregate = new AccountAggregate();
        when(getAccountData.getAccount("123")).thenReturn(accountAggregate);

        mockMvc.perform(get("/accounts/123"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(getAccountData).getAccount("123");
    }

    @Test
    public void testUpdateAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        AccountAggregate accountAggregate = new AccountAggregate();
        when(updateAccount.apply(eq("123"), any(AccountDTO.class))).thenReturn(accountAggregate);

        mockMvc.perform(put("/accounts/123/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\":\"value\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(updateAccount).apply(eq("123"), any(AccountDTO.class));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        doNothing().when(deleteAccount).accept("123");

        mockMvc.perform(delete("/accounts/123"))
                .andExpect(status().isOk());

        verify(deleteAccount).accept("123");
    }

}
