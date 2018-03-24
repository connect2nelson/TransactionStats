package com.n26.stats.TransactionStatistics.model;

import com.n26.stats.TransactionStatistics.model.valueobject.Amount;
import com.n26.stats.TransactionStatistics.model.valueobject.TimeStamp;


public class Transaction {

    private final Amount amount;
    private final TimeStamp timeStamp;
    private static final Integer WINDOW_PERIOD = 60;


    public Transaction(double amount, long timeStampInMilliSecs) {
        this.amount = new Amount(amount);
        this.timeStamp = new TimeStamp(timeStampInMilliSecs);
    }

    public Amount getAmount() {
        return amount;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public boolean isWithinLast60Seconds(long currentTimeInEchoMillis) {
        return ((currentTimeInEchoMillis - timeStamp.getTimeStampInMilliSecs()) / 1000) <= WINDOW_PERIOD;
    }
}
