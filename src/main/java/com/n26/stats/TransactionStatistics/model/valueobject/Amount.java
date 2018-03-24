package com.n26.stats.TransactionStatistics.model.valueobject;

public class Amount {

    private final double value;

    public Amount(final double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
