package com.n26.stats.TransactionStatistics.service;

import com.n26.stats.TransactionStatistics.exception.NoStatisticAvailableException;
import com.n26.stats.TransactionStatistics.exception.TransactionTimeOutOfRangeException;
import com.n26.stats.TransactionStatistics.model.Statistic;
import com.n26.stats.TransactionStatistics.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsCacheServiceTest {

    @InjectMocks
    private StatisticsCacheService statisticsCacheService;

    private Instant now;

    @Before
    public void setup() {
        now = Instant.now();
    }

    @Test(expected = NoStatisticAvailableException.class)
    public void shouldThrowNoStatisticAvailableExceptionWhenNoTransactionExists() throws Exception {
        statisticsCacheService.getStatistic();
    }

    @Test
    public void shouldAddATransactionAndGetStatisticForTheSame() throws Exception {

        Transaction transaction = createTransaction(11., now.toEpochMilli());
        statisticsCacheService.addTransaction(transaction);

        Statistic summaryStat = statisticsCacheService.getStatistic();

        assertThat(11.).isEqualTo(summaryStat.getMin());
        assertThat(11.).isEqualTo(summaryStat.getSum());
        assertThat(11.).isEqualTo(summaryStat.getAvg());
        assertThat(11.).isEqualTo(summaryStat.getMax());
        assertThat(1L).isEqualTo(summaryStat.getCount());
    }

    @Test(expected = TransactionTimeOutOfRangeException.class)
    public void shouldNotAddTransactionWhenATransactionCreatedBefore60SecondsFromNow() throws TransactionTimeOutOfRangeException {
        Instant nowMinus60Seconds = now.minusSeconds(61);
        statisticsCacheService.addTransaction(createTransaction(11., nowMinus60Seconds.toEpochMilli()));
    }

    @Test
    public void shouldAddMultipleTransactionsNotOlderThan1SecAndGetStatistic() throws TransactionTimeOutOfRangeException, NoStatisticAvailableException {
        statisticsCacheService.addTransaction(createTransaction(11., now.toEpochMilli()));
        statisticsCacheService.addTransaction(createTransaction(111., now.toEpochMilli()));

        Statistic summaryStat = statisticsCacheService.getStatistic();
        assertThat(2L).isEqualTo(summaryStat.getCount());
        assertThat(11.).isEqualTo(summaryStat.getMin());
        assertThat(111.).isEqualTo(summaryStat.getMax());
        assertThat(122.).isEqualTo(summaryStat.getSum());
        assertThat(61.).isEqualTo(summaryStat.getAvg());
    }

    @Test
    public void shouldAddMultipleTransactionsNotOlderThan60secsAndGetStatistics() throws TransactionTimeOutOfRangeException, NoStatisticAvailableException {
        Instant timeOlderThan20Secs = now.minusSeconds(20);

        statisticsCacheService.addTransaction(createTransaction(11., now.toEpochMilli()));
        statisticsCacheService.addTransaction(createTransaction(111., timeOlderThan20Secs.toEpochMilli()));

        Statistic summaryStat = statisticsCacheService.getStatistic();
        assertThat(2L).isEqualTo(summaryStat.getCount());
        assertThat(11.).isEqualTo(summaryStat.getMin());
        assertThat(111.).isEqualTo(summaryStat.getMax());
        assertThat(122.).isEqualTo(summaryStat.getSum());
        assertThat(61.).isEqualTo(summaryStat.getAvg());
    }

    @Test
    public void shouldAddTransactionsInRangeAndNotAddTransactionOutOfRangeAndGetStatistics() throws NoStatisticAvailableException {
        Instant timeOlderThan20Secs = now.minusSeconds(20);
        Instant timeOlderThan60Secs = now.minusSeconds(61);

        try {
            statisticsCacheService.addTransaction(createTransaction(111., now.toEpochMilli()));
            statisticsCacheService.addTransaction(createTransaction(11., timeOlderThan20Secs.toEpochMilli()));
            statisticsCacheService.addTransaction(createTransaction(1., timeOlderThan60Secs.toEpochMilli()));

            //Execution should not proceed further.
            fail();
        } catch (TransactionTimeOutOfRangeException e) {
            assertThat(e).isInstanceOf(TransactionTimeOutOfRangeException.class);
        }

        Statistic summaryStat = statisticsCacheService.getStatistic();
        assertThat(2L).isEqualTo(summaryStat.getCount());
        assertThat(11.).isEqualTo(summaryStat.getMin());
        assertThat(111.).isEqualTo(summaryStat.getMax());
        assertThat(122.).isEqualTo(summaryStat.getSum());
        assertThat(61.).isEqualTo(summaryStat.getAvg());

    }

    private Transaction createTransaction(double amount, long nowTime) {
        Transaction transaction = new Transaction(amount, nowTime);
        return transaction;
    }

}