package com.n26.stats.TransactionStatistics.model;

public class Statistic {
    private final double sum;
    private final double avg;
    private final double max;
    private final double min;
    private final long count;

    public Statistic(double sum, double avg, double min, double max, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Statistic() {
        this.sum = 0;
        this.avg = 0;
        this.max = 0;
        this.min = 0;
        this.count = 0;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max == Double.NEGATIVE_INFINITY ? 0 : max;
    }

    public double getMin() {
        return min == Double.POSITIVE_INFINITY ? 0 : min;
    }

    public long getCount() {
        return count;
    }
}

