package com.n26.stats.TransactionStatistics.controller;


import com.n26.stats.TransactionStatistics.exception.NoStatisticAvailableException;
import com.n26.stats.TransactionStatistics.model.Statistic;
import com.n26.stats.TransactionStatistics.service.StatisticsCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/stats")
public class StatisticController {

    @Autowired
    private StatisticsCacheService statisticsCacheService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Statistic getStatistics() throws NoStatisticAvailableException {
        return statisticsCacheService.getStatistic();
    }
}