package com.n26.stats.TransactionStatistics;

import com.n26.stats.TransactionStatistics.coverage.IgnoreCoverage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.n26.stats")
public class TransactionStatisticsApplication {


	@IgnoreCoverage
	public static void main(String[] args) {
		SpringApplication.run(TransactionStatisticsApplication.class, args);
	}
}
