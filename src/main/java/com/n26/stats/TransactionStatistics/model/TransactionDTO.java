package com.n26.stats.TransactionStatistics.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    private double amount;
    private long timestamp;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, long timeStampInMilliSecs) {
        this.amount = amount;
        this.timestamp = timeStampInMilliSecs;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
