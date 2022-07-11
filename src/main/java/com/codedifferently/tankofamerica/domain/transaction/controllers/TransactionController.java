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

    @ShellMethod("Make a deposit")
    public String deposit(@ShellOption({"-I", "--accountid"}) String accountId,
                          @ShellOption({"A", "--amount"}) Double amount) throws AccountNotFoundException, OverdraftException {
        Account account = accountService.getById(accountId);
        Transaction transaction = new Transaction(amount, account);
        transactionService.create(accountId, transaction);
        return transaction.toString();
    }

    @ShellMethod("Make a withdrawal")
    public String withdrawal(@ShellOption({"-I", "accountid"}) String accountId,
                            @ShellOption({"-A", "--amount"}) Double amount) throws AccountNotFoundException {
        try {
            Account account = accountService.getById(accountId);
            Transaction transaction = new Transaction((-1) * amount, account);
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

    @ShellMethod("Get all transactions from account")
    public String accountTransactions(@ShellOption({"A", "-account"}) String accountId) throws AccountNotFoundException {
        return transactionService.getAllFromAccount(accountId);
    }

    @ShellMethod("Get all deposits")
    public String accountDeposits(@ShellOption({"A", "-account"}) String accountId) throws AccountNotFoundException {
        return transactionService.getAllDeposits(accountId);
    }
    
    @ShellMethod("Get all withdrawals")
    public String accountWithdrawals(@ShellOption({"-A", "-account"}) String accountId) throws AccountNotFoundException {
        return transactionService.getAllWithdrawals(accountId);
    }

    @ShellMethod("Get most recent transaction")
    public String getRecentTransaction(@ShellOption({"-A", "--account"}) String accountId) throws AccountNotFoundException {
        return transactionService.getMostRecentTransaction(accountId);
    }

    @ShellMethod("Get first transaction")
    public String getFirstTransaction(@ShellOption({"-A", "-account"}) String accountId) throws AccountNotFoundException {
        return transactionService.getFirstTransaction(accountId);
    }
}
