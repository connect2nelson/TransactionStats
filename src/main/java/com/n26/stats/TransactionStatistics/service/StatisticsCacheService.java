package com.n26.stats.TransactionStatistics.service;


import com.n26.stats.TransactionStatistics.exception.NoStatisticAvailableException;
import com.n26.stats.TransactionStatistics.exception.TransactionTimeOutOfRangeException;
import com.n26.stats.TransactionStatistics.model.Statistic;
import com.n26.stats.TransactionStatistics.model.Transaction;
import com.n26.stats.TransactionStatistics.model.valueobject.Amount;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.n26.stats.TransactionStatistics.model.StatsConstants.WINDOW_PERIOD;

@Service
public class StatisticsCacheService {

    private final ConcurrentNavigableMap<Long, DoubleSummaryStatistics> perSecondStatsCacheMap;

    private static final ReentrantLock lock = new ReentrantLock();

    StatisticsCacheService() {
        perSecondStatsCacheMap = new ConcurrentSkipListMap<>();
    }

    void addTransaction(final Transaction transaction) throws TransactionTimeOutOfRangeException {

        long timeInSec = TimeUnit.MILLISECONDS.toSeconds(transaction.getTimeStamp().getTimeStampInMilliSecs());

        if (transaction.isWithinLast60Seconds(Instant.now().toEpochMilli())) {

            DoubleSummaryStatistics statSummaryForAGivenSecond = new DoubleSummaryStatistics();
            Amount amount = transaction.getAmount();

            try {
                lock.lock();
                if (perSecondStatsCacheMap.containsKey(timeInSec)) {

                    statSummaryForAGivenSecond = perSecondStatsCacheMap.get(timeInSec);
                    statSummaryForAGivenSecond.accept(amount.getValue());

                    perSecondStatsCacheMap.put(timeInSec, statSummaryForAGivenSecond);
                } else {
                    statSummaryForAGivenSecond.accept(amount.getValue());
                    perSecondStatsCacheMap.put(timeInSec, statSummaryForAGivenSecond);
                }
            } finally {
                lock.unlock();
            }
        } else {
            throw new TransactionTimeOutOfRangeException();
        }

    }

    private Statistic calculateStatistics() throws NoStatisticAvailableException {
        try {
            lock.lock();
            if (perSecondStatsCacheMap.size() > 0) {
                DoubleSummaryStatistics statSummary = new DoubleSummaryStatistics();

                perSecondStatsCacheMap.values().forEach(statSummary::combine);

                return new Statistic(statSummary.getSum(),
                        statSummary.getAverage(), statSummary.getMin(), statSummary.getMax(),
                        statSummary.getCount());
            } else {
                throw new NoStatisticAvailableException();
            }
        } finally {
            lock.unlock();
        }
    }

    public Statistic getStatistic() throws NoStatisticAvailableException {
        removeExpiredStats();
        return calculateStatistics();
    }

    @Async
    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void addBucketForNewStatEntriesToBeAddedAndCleanup() {
        long timeInSec = Instant.now().getEpochSecond();

        removeExpiredStats();
        try {
            lock.lock();
            if (!perSecondStatsCacheMap.containsKey(timeInSec)) {
                perSecondStatsCacheMap.put(timeInSec, new DoubleSummaryStatistics());
            }
        } finally {
            lock.unlock();
        }
    }

    private void removeExpiredStats() {
        try {
            lock.lock();

            while (perSecondStatsCacheMap.size() > WINDOW_PERIOD) {
                perSecondStatsCacheMap.remove(perSecondStatsCacheMap.firstKey());
            }
        } finally {
            lock.unlock();
        }

    }

}
