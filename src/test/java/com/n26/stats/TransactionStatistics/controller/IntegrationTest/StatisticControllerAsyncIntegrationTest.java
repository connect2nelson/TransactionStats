package com.n26.stats.TransactionStatistics.controller.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.stats.TransactionStatistics.TransactionStatisticsApplication;
import com.n26.stats.TransactionStatistics.model.Statistic;
import com.n26.stats.TransactionStatistics.model.TransactionDTO;
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

import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TransactionStatisticsApplication.class})
@AutoConfigureMockMvc
public class StatisticControllerAsyncIntegrationTest {

    private final static String STATS_ENDPOINT = "/api/v1/stats";
    private final static String TRANSACTIONS_ENDPOINT = "/api/v1/transactions/";

    @Autowired
    private MockMvc mvc;

    private TransactionDTO transactionDTO;
    private static ObjectMapper jsonMapper = new ObjectMapper();

    private void addTransaction(TransactionDTO transactionDTO) throws Exception {

        this.transactionDTO = new TransactionDTO(transactionDTO.getAmount(), transactionDTO.getTimestamp());

        String transactionAsString = jsonMapper.writeValueAsString(this.transactionDTO);

        MockHttpServletResponse response = mvc.perform(
                post(TRANSACTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionAsString))
                .andDo(print())
                .andReturn().getResponse();
    }

    //Integration test for @Async and @EnableScheduling annotation.
    @Test
    public void shouldRemoveExpiredTransactions() throws Exception {

        //Adding more than 60 transactions with 1 sec interval
        Instant originalTimeStamp = Instant.now();
        for (int i = 0; i < 70; i++) {
            Thread.sleep(1000);
            TransactionDTO transactionDTO = new TransactionDTO(1, originalTimeStamp.plusSeconds(i).toEpochMilli());
            addTransaction(transactionDTO);
        }

        MockHttpServletResponse response = mvc.perform(get(STATS_ENDPOINT))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Statistic statistic = new Statistic(60, 1, 1, 1, 60L);
        assertThat(response.getContentAsString()).isEqualTo(jsonMapper.writeValueAsString(statistic));
    }
}