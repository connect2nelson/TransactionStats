package com.n26.stats.TransactionStatistics.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    private double amount;
    private long timeStamp;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, long timeStampInMilliSecs) {
        this.amount = amount;
        this.timeStamp = timeStampInMilliSecs;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
