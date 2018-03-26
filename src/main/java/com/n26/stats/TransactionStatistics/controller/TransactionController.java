package com.n26.stats.TransactionStatistics.controller;


import com.n26.stats.TransactionStatistics.exception.TransactionTimeOutOfRangeException;
import com.n26.stats.TransactionStatistics.model.Transaction;
import com.n26.stats.TransactionStatistics.model.TransactionDTO;
import com.n26.stats.TransactionStatistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    //
    // When TransactionTimeOutOfRangeException is thrown; it returns with Http status code 204
    //
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransaction(@RequestBody TransactionDTO transactionDTO) throws TransactionTimeOutOfRangeException {

        Transaction transaction = new Transaction(transactionDTO.getAmount(), transactionDTO.getTimestamp());
        transactionService.saveTransaction(transaction);
    }
}
