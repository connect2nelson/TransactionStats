package com.n26.stats.TransactionStatistics.controller.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.stats.TransactionStatistics.TransactionStatisticsApplication;
import com.n26.stats.TransactionStatistics.model.Statistic;
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

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TransactionStatisticsApplication.class})
@AutoConfigureMockMvc
public class StatisticControllerIT {

    private final static String STATS_ENDPOINT = "/api/v1/stats";

    @Autowired
    private MockMvc mvc;

    private static ObjectMapper jsonMapper = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(1, Instant.now().toEpochMilli());
        addAndCheckWhetherATempTransactionIsAdded(transactionDTO);
    }

    @Test
    public void shouldReturnStatisticFromCacheServiceWhenStatsAreAvailable() throws Exception {

        MockHttpServletResponse response = mvc.perform(get(STATS_ENDPOINT))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.getContentAsString()).isNotEmpty();
        Statistic statistic = jsonMapper.readValue(response.getContentAsString(), Statistic.class);

        assertThat(statistic).isNotNull();

        //As we are adding a transaction before running the test, we should see a non-zero count
        assertThat(statistic.getCount()).isGreaterThan(0);
    }

    private void addAndCheckWhetherATempTransactionIsAdded(TransactionDTO transactionDTO) throws Exception {

        String TRANSACTIONS_ENDPOINT = "/api/v1/transactions/";

        String transactionAsString = jsonMapper.writeValueAsString(transactionDTO);

        MockHttpServletResponse response = mvc.perform(
                post(TRANSACTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionAsString))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }


}