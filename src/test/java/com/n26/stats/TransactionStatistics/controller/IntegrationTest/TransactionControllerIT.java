package com.n26.stats.TransactionStatistics.controller.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.stats.TransactionStatistics.TransactionStatisticsApplication;
import com.n26.stats.TransactionStatistics.model.TransactionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TransactionStatisticsApplication.class})
@AutoConfigureMockMvc
public class TransactionControllerIT {

    private static String TRANSACTIONS_ENDPOINT = "/api/v1/transactions/";

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper jsonMapper = new ObjectMapper();
    private TransactionDTO transactionDTO;

    @Before
    public void setUp() throws Exception {
        transactionDTO = new TransactionDTO(12.5, 12345678);
    }

    @Test
    public void shouldReturn2xxWhenTransactionIsSaved() throws Exception {

        String transactionDTOAsJsonString = jsonMapper.writeValueAsString(transactionDTO);

        MockHttpServletResponse response = mockMvc.perform(
                post(TRANSACTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionDTOAsJsonString))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();
    }

}