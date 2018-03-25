package com.n26.stats.TransactionStatistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.stats.TransactionStatistics.exception.NoStatisticAvailableException;
import com.n26.stats.TransactionStatistics.model.Statistic;
import com.n26.stats.TransactionStatistics.service.StatisticsCacheService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(MockitoJUnitRunner.class)
public class StatisticControllerTest {

    private final static String STATS_ENDPOINT = "/v1/stats/";

    private MockMvc mockMvc;

    @Mock
    private StatisticsCacheService statisticCacheService;

    @InjectMocks
    private StatisticController statisticController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticController).build();
    }

    @Test
    public void shouldReturnStatisticFromCacheServiceWhenStatsAreAvailable() throws Exception {

        Statistic statistic = new Statistic(1, 1, 1, 1, 1);
        when(statisticCacheService.getStatistic()).thenReturn(statistic);

        ObjectMapper objectMapper = new ObjectMapper();
        String statisticAsString = objectMapper.writeValueAsString(statistic);

        MockHttpServletResponse response = mockMvc.perform(get(STATS_ENDPOINT))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(statisticAsString);
    }

    @Test
    public void shouldThrowExceptionStatusNotAvailableWhenNoStatsArePresentInCacheSErvice() throws Exception {

        when(statisticCacheService.getStatistic()).thenThrow(NoStatisticAvailableException.class);


        MockHttpServletResponse response = mockMvc.perform(get(STATS_ENDPOINT))
                .andDo(print())
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }
}