package sas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sas.backend.dto.TransactionCreationDto;
import sas.backend.dto.TransactionDto;
import sas.backend.exception.TransactionsServiceException;
import sas.backend.service.TransactionsService;

import java.util.List;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping("/get/{friendUsername}")
    public List<TransactionDto> getTransactions(@PathVariable String friendUsername)
            throws TransactionsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return transactionsService.getTransactions(accountUsername, friendUsername);
    }

    @PostMapping("/make/{friendUsername}")
    public void makeTransaction(
            @PathVariable String friendUsername,
            @RequestBody TransactionCreationDto transaction) throws TransactionsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        transactionsService.makeTransaction(accountUsername, friendUsername, transaction);
    }

    @DeleteMapping("/delete/{transactionId}")
    public void deleteTransaction(@PathVariable int transactionId) throws TransactionsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        transactionsService.deleteTransaction(accountUsername, transactionId);
    }
}
