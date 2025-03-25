package com.local.accounts.unit.infrastructure.router;

import com.local.accounts.application.strategy.TransactionHandle;
import com.local.accounts.router.TransactionController;
import com.local.accounts.router.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {

    @Mock
    private TransactionHandle transactionHandle;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testTransaction() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();

        doNothing().when(transactionHandle).accept(transactionDTO);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"property1\":\"value1\",\"property2\":\"value2\"}"))
                .andExpect(status().isOk());
    }
}
