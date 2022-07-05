package com.codedifferently.tankofamerica.domain.transaction.controllers;

import com.codedifferently.tankofamerica.domain.account.exceptions.AccountNotFoundException;
import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.services.AccountService;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.OverdraftException;
import com.codedifferently.tankofamerica.domain.transaction.exceptions.TransactionNotFoundException;
import com.codedifferently.tankofamerica.domain.transaction.models.Transaction;
import com.codedifferently.tankofamerica.domain.transaction.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.UUID;

@ShellComponent
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @ShellMethod("Make a deposit") // creates new transaction but does not properly update account balance
    public String deposit(@ShellOption({"-I", "--accountid"}) String accountId,
                          @ShellOption({"A", "--amount"}) Double amount) throws AccountNotFoundException, OverdraftException {
        Account account = accountService.getById(accountId);
        account.updateBalance(amount);
        account = accountService.update(account);
        Transaction transaction = new Transaction(amount, account);
        transactionService.create(accountId, transaction);
        return transaction.toString();
    }

    @ShellMethod("Make a withdrawal")
    public String withdrawal(@ShellOption({"-I", "accountid"}) String accountId,
                            @ShellOption({"-A", "--amount"}) Double amount) throws AccountNotFoundException {
        try {
            Account account = accountService.getById(accountId);
            amount = (-1) * amount;
            account.updateBalance(amount);
            account = accountService.update(account);
            Transaction transaction = new Transaction(amount, account);
            transactionService.create(accountId, transaction);
            return transaction.toString();
        } catch (OverdraftException e) {
            return e.getMessage();
        }
    }

    @ShellMethod("Find a transaction by id")
    public Transaction getTransactionById(@ShellOption({"-I", "--transactionId"}) Long transactionId) {
        Transaction transaction = null;
        try {
            transaction = transactionService.getById(transactionId);
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return transaction;
    }


}
