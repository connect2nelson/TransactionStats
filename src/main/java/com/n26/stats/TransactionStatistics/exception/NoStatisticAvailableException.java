package com.n26.stats.TransactionStatistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No stats available")
public class NoStatisticAvailableException extends Exception {
}
