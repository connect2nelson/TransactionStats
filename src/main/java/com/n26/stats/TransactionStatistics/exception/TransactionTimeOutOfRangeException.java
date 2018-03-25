package com.n26.stats.TransactionStatistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Transaction time is older than 60 seconds")
public class TransactionTimeOutOfRangeException extends Exception {
}
