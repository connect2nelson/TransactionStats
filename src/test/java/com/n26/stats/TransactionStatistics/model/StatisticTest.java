package com.n26.stats.TransactionStatistics.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class StatisticTest {

    @Test
    public void shouldReturnCurrentMaxValueIfMaxValueIsNotEqualToNegDoubleInfinity(){
        Statistic statistic = new Statistic(1,1,1,1,1);
        assertThat(statistic.getMax()).isEqualTo( 1);
    }


    @Test
    public void shouldReturnCurrentMinValueIfMinValueIsNotEqualToNegDoubleInfinity(){
        Statistic statistic = new Statistic(1,1,1,1,1);
        assertThat(statistic.getMin()).isEqualTo( 1);
    }

    @Test
    public void shouldReturnZeroIfMaxValueIsNotSet(){
        Statistic statistic = new Statistic(0, 0, 0, Double.NEGATIVE_INFINITY ,1);
        assertThat(statistic.getMax()).isEqualTo( 0);
    }


    @Test
    public void shouldReturnZeroIfMinValueIsNotSet(){
        Statistic statistic = new Statistic(0, 0, Double.POSITIVE_INFINITY, 0 ,1);
        assertThat(statistic.getMin()).isEqualTo( 0);
    }
}