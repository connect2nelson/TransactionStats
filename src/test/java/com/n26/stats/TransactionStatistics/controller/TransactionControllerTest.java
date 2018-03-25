package com.n26.stats.TransactionStatistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.stats.TransactionStatistics.exception.TransactionTimeOutOfRangeException;
import com.n26.stats.TransactionStatistics.model.Transaction;
import com.n26.stats.TransactionStatistics.model.TransactionDTO;
import com.n26.stats.TransactionStatistics.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private static String TRANSACTIONS_ENDPOINT = "/v1/transactions/";

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController controller;


    private MockMvc mockMvc;
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private TransactionDTO transactionDTO;

    @Before
    public void setUp() throws Exception {
        transactionDTO = new TransactionDTO(12.5, 12345678);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void shouldRespondResourceCreatedWhenTransactionIsSaved() throws Exception {

        String transactionAsString = jsonMapper.writeValueAsString(transactionDTO);

        MockHttpServletResponse response = mockMvc.perform(
                post(TRANSACTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionAsString))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void shouldRespondNoContentWhenTransactionIsNotSaved() throws Exception {

        doThrow(TransactionTimeOutOfRangeException.class)
                .when(transactionService)
                .saveTransaction(isA(Transaction.class));

        String transactionAsString = jsonMapper.writeValueAsString(transactionDTO);

        MockHttpServletResponse response = mockMvc.perform(
                post(TRANSACTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionAsString))
                .andDo(print()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isEmpty();
    }
}