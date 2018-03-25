package com.n26.stats.TransactionStatistics.service;

import com.n26.stats.TransactionStatistics.exception.TransactionTimeOutOfRangeException;
import com.n26.stats.TransactionStatistics.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final StatisticsCacheService statisticCacheService;

    @Autowired
    public TransactionService(StatisticsCacheService statisticCacheService) {
        this.statisticCacheService = statisticCacheService;
    }

    public void saveTransaction(final Transaction transaction) throws TransactionTimeOutOfRangeException {
        statisticCacheService.addTransaction(transaction);
    }
}

