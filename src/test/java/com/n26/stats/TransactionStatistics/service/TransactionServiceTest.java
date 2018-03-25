package com.n26.stats.TransactionStatistics.service;

import com.n26.stats.TransactionStatistics.exception.TransactionTimeOutOfRangeException;
import com.n26.stats.TransactionStatistics.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private StatisticsCacheService statisticCacheService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void shouldSaveTransactionWhenTransactionIsNotOlderThan60Seconds() throws Exception {
        Instant olderThan10seconds = Instant.now().minusSeconds(10);
        Transaction transaction = new Transaction(111, olderThan10seconds.toEpochMilli());

        transactionService.saveTransaction(transaction);

        verify(statisticCacheService).addTransaction(transaction);
    }

    @Test(expected = TransactionTimeOutOfRangeException.class)
    public void shouldNotSaveTransactionWhenTransactionIsOlderThan60Seconds() throws Exception {

        Instant olderThan60seconds = Instant.now().minusSeconds(61);
        Transaction transaction = new Transaction(111, olderThan60seconds.toEpochMilli());

        doThrow(TransactionTimeOutOfRangeException.class).when(statisticCacheService).addTransaction(transaction);

        transactionService.saveTransaction(transaction);

    }

    @Test
    public void shouldSaveTransactionWhenTransactionIsExactly60SecondsOld() throws Exception {

        Instant olderThan60seconds = Instant.now().minusSeconds(60);
        Transaction transaction = new Transaction(111, olderThan60seconds.toEpochMilli());

        transactionService.saveTransaction(transaction);

        verify(statisticCacheService).addTransaction(transaction);
    }

}