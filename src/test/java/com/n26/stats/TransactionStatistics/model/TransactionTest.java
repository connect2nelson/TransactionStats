package com.n26.stats.TransactionStatistics.model;

import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TransactionTest {

    @Test
    public void shouldReturnTrueIfTheTransactionTimeIsWithinLast60Seconds() throws Exception {

        long currentTimeStampInMilliSecs = getCurrentTimeStampInMilliSecs();
        Transaction transaction1 = new Transaction(1, currentTimeStampInMilliSecs);
        Transaction transaction2 = new Transaction(2, transaction1.getTimeStamp().getTimeStampInMilliSecs()+ 1000);

        boolean inRange = transaction1.isInRange(transaction2.getTimeStamp().getTimeStampInMilliSecs());
        assertThat(inRange).isTrue();

    }

    @Test
    public void shouldReturnFalseIfTheTransactionTimeIsNotWithinLast60Seconds() throws Exception {

        long currentTimeStampInMilliSecs = getCurrentTimeStampInMilliSecs();
        Transaction transaction1 = new Transaction(1, currentTimeStampInMilliSecs);
        Transaction transaction2 = new Transaction(2, transaction1.getTimeStamp().getTimeStampInMilliSecs() + 70000);

        boolean inRange = transaction1.isInRange(transaction2.getTimeStamp().getTimeStampInMilliSecs());
        assertThat(inRange).isFalse();
    }

    private long getCurrentTimeStampInMilliSecs() {
        return Instant.now().getEpochSecond() * 1000;
    }


}