package com.n26.stats.TransactionStatistics.model.valueobject;

public class TimeStamp {

    private final long timeStampInMilliSecs;

    public TimeStamp(long timeStampInMilliSecs) {
        this.timeStampInMilliSecs = timeStampInMilliSecs;
    }

    public long getTimeStampInMilliSecs() {
        return timeStampInMilliSecs;
    }
}
