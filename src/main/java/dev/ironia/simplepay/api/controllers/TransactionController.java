package dev.ironia.simplepay.api.controllers;

import dev.ironia.simplepay.api.domain.transaction.Transaction;
import dev.ironia.simplepay.api.dtos.TransactionDto;
import dev.ironia.simplepay.api.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto transactionDto) throws Exception {
        Transaction transaction = this.transactionService.createTransaction(transactionDto);

        return new ResponseEntity<>(
                transaction,
                HttpStatus.CREATED
        );
    }
}
